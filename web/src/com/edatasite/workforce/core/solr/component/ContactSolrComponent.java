package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.solr.document.ContactSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.ContactSolrDocRepository;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.GroupParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SimpleFacetFieldEntry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CONTACT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;

/**
 * @author: Dilsh0d Tadjiev on 11.08.2020 10:31.
 */

@Component
public class ContactSolrComponent {

    private static Logger log = LoggerFactory.getLogger(ContactSolrComponent.class);
    @Resource
    private ContactSolrComponent contactComponent;

    @Autowired
    private ContactSolrDocRepository contactSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private MailListManager mailListManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsCrmContact crmContact) throws InterruptedException {
        contactComponent.indexes(Arrays.asList(crmContact));
    }

    @Transactional
    public void indexes(List<EdsCrmContact> crmContacts) throws InterruptedException {

        Integer companyId = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(crmContacts)) {
            List<ContactSolrDoc> contactSolrDocs = new ArrayList<>();

            for (EdsCrmContact crmContact : crmContacts) {
                if (crmContact != null) {
                    try {
                        List<SelectItem> mailList = mailListManager.getContactsMailingLists(crmContact.getObjectID());
                        contactSolrDocs.add(createContactDocument(crmContact.getSolrRPC(), Integer.valueOf(companyId), mailList, crmContact.getCustomFields()));
                        log.info("Indexed Contact Core CID - {}, objId - {}", companyId, crmContact.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on Contact with id {}, and error: {} **********************", crmContact.getObjectID(), e);
                        throw e;
                    }
                }
            }

            if (!contactSolrDocs.isEmpty()) {
                log.info("========= Create Contact solr docs for company {} with size {} =========", companyId, contactSolrDocs.size());
                contactSolrDocRepository.saveAll(contactSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsCrmContact> crmContacts) throws InterruptedException {
        if (!CollectionUtils.isEmpty(crmContacts)) {
            ConcurrentLinkedQueue<ContactSolrDoc> contactSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsCrmContact crmContact : crmContacts) {
                if (crmContact != null) {
                    ContactSolrItem solrRPC = crmContact.getSolrRPC();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        List<SelectItem> mailList = mailListManager.getContactsMailingLists(crmContact.getObjectID());
                                        contactSolrDocs.add(createContactDocument(solrRPC, Integer.valueOf(companyId), mailList, crmContact.getCustomFields()));
                                        log.info("Indexed Contact Core CID - {}, objId - {}", companyId, crmContact.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on Contact with id {}, and error: {} **********************", solrRPC.getObjectId(), e);
                        }
                        return null;
                    };
                    tasks.add(task);
                }
            }

            try {
                List<Future<Void>> results = executor.invokeAll(tasks);
                for (Future<Void> f : results) {
                    try {
                        f.get();
                    } catch (ExecutionException e) {
                        log.error("❌ Task execution failed", e.getCause());
                    }
                }
            } catch (InterruptedException e) {
                log.error("Error on loading Contact list", e);
            }

            if (!contactSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Contact solr docs for company {} with size {} =========", companyId, contactSolrDocs.size());
                    contactSolrDocRepository.saveAll(contactSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Contact list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(ContactSolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getObjectId();
    }

    private ContactSolrDoc createContactDocument(ContactSolrItem contact, Integer companyID, List<SelectItem> mailList, EdsCustomFields customFields) {
        long start = System.currentTimeMillis();
        ContactSolrDoc contactSolrDoc = new ContactSolrDoc();
        List<SelectItem> categoryList = contact.getCategory();
        boolean isLead = false;
        boolean isCandidate = false;

        contactSolrDoc.setOid(SolrUtils.generatedOId(companyID, contact.getObjectId()));
        contactSolrDoc.setCompanyId(companyID);
        contactSolrDoc.setContactId(contact.getObjectId());
        contactSolrDoc.setContactName(contact.getContactName());
        contactSolrDoc.setContactType(contact.getContactType());
        contactSolrDoc.setPrimaryContact(contact.getPrimaryContact());
        contactSolrDoc.setContactNameComposite(contact.getContactName());
        contactSolrDoc.setLeadNameComposite(contact.getContactName());
        contactSolrDoc.setFirstName(contact.getFirstName());
        contactSolrDoc.setMiddleName(contact.getMiddleName());
        contactSolrDoc.setLastName(contact.getLastName());
        contactSolrDoc.setRefIndNumber(contact.getRefIndNumber());
        contactSolrDoc.setTitle(contact.getTitle());
        contactSolrDoc.setJobTitle(contact.getJobTitle());
        contactSolrDoc.setExtension(contact.getExtension());
        contactSolrDoc.setPrimaryEmail(contact.getPrimaryEmail());
        contactSolrDoc.setPrimaryPhone(contact.getPrimaryPhone());
        contactSolrDoc.setFax(contact.getFax());
        contactSolrDoc.setMobile(contact.getMobile());
        contactSolrDoc.setWorkPhone(contact.getWorkPhone());
        contactSolrDoc.setWebsite(contact.getWebsite());
        contactSolrDoc.setDepartment(contact.getDepartment());
        contactSolrDoc.setUpdateDate(contact.getUpdateDate());
        contactSolrDoc.setCreationDate(contact.getCreationDate());
        contactSolrDoc.setDateOfBirth(contact.getDateOfBirth());
        contactSolrDoc.setReportsTo(contact.getReportsTo());
        contactSolrDoc.setReportsToId(contact.getReportsToId());
        contactSolrDoc.setEmailAllowed(contact.getEmailAllowed());
        contactSolrDoc.setGoogleId(contact.getGoogleId());
        contactSolrDoc.setLeadKanbanOrder(contact.getLeadKanbanOrder());
        contactSolrDoc.setFavourited(contact.isFavourite());

        if (contact.getCreator() != null) {
            contactSolrDoc.setCreatorId(contact.getCreator().getId());
            contactSolrDoc.setCreatorName(contact.getCreator().getName());
            contactSolrDoc.setCreatorIdName(SolrUtils.getIdName(contact.getCreator().getId(), contact.getCreator().getName()));
        }

        if (contact.getUpdater() != null) {
            contactSolrDoc.setUpdaterId(contact.getUpdater().getId());
            contactSolrDoc.setUpdaterName(contact.getUpdater().getName());
            contactSolrDoc.setUpdaterIdName(SolrUtils.getIdName(contact.getUpdater().getId(), contact.getUpdater().getName()));
        }

        StringBuilder categoryNames = new StringBuilder();
        if (categoryList != null && !categoryList.isEmpty()) {
            for (SelectItem category : categoryList) {
                contactSolrDoc.getCategoryId().add(category.getId());
                contactSolrDoc.getCategoryName().add(category.getName());
                contactSolrDoc.getCategoryIdName().add(SolrUtils.getIdName(category.getId(), category.getName()));
                if ("".equals(categoryNames.toString())) {
                    categoryNames.append(category.getName() != null ? category.getName() : "");
                }
            }
            contactSolrDoc.setCategoryNameSort(categoryNames.toString());
        }
        if (mailList != null && !mailList.isEmpty()) {
            for (SelectItem mail : mailList) {
                contactSolrDoc.getMailListId().add(mail.getId());
                contactSolrDoc.getMailListName().add(mail.getName());
                contactSolrDoc.getMailListIdName().add(SolrUtils.getIdName(mail.getId(), mail.getName()));
            }
        }
        if (contact.getOwner() != null) {
            contactSolrDoc.setOwnerId(contact.getOwner().getId());
            contactSolrDoc.setOwnerName(contact.getOwner().getName());
            contactSolrDoc.setOwnerIdName(SolrUtils.getIdName(contact.getOwner().getId(), contact.getOwner().getName()));
        }
        if (contact.getAccount() != null) {
            contactSolrDoc.setAccountId(contact.getAccount().getId());
            contactSolrDoc.setAccountName(contact.getAccount().getName());
            contactSolrDoc.setAccountIdName(SolrUtils.getIdName(contact.getAccount().getId(), contact.getAccount().getName()));
            contactSolrDoc.setAccountNumber(contact.getAccount().getNumber());
            if (contact.getAccountIndustry() != null) {
                contactSolrDoc.setAccountIndustry(contact.getAccountIndustry().getName());
                contactSolrDoc.setAccountIndustryId(contact.getAccountIndustry().getId());
            }
            if (contact.getAccountOwnerId() != null) {
                contactSolrDoc.getAccountOwnerId().addAll(contact.getAccountOwnerId());
            }
            if (contact.getAccountType() != null) {
                contactSolrDoc.getAccountType().addAll(contact.getAccountType());
            }
        }
        if (Boolean.TRUE.equals(contact.getAccessEnabled())) {
            contactSolrDoc.setAccessEnabled(contact.getAccessEnabled());
            EdsClientContact clientContact = clientContactManager.getClientContactByCrmContact(contact.getObjectId());
            if (clientContact != null) {
                contactSolrDoc.setClientContactId(clientContact.getObjectID());
            }
        } else {
            contactSolrDoc.setAccessEnabled(Boolean.FALSE);
        }
        if (contact.isCandidate()) {
            if (contact.getStatus() != null) {
                contactSolrDoc.setStatus(contact.getStatus().getName());
                contactSolrDoc.setStatusId(contact.getStatus().getObjectID());
                contactSolrDoc.setStatusCode(contact.getStatus().getCode());
                contactSolrDoc.setStatusIdCode(SolrUtils.getIdName(contact.getStatus().getObjectID(), contact.getStatus().getCode()));
                contactSolrDoc.setStatusIdCodeName(contact.getStatus().getObjectID() + SolrContactRepresenter.SPLIT + contact.getStatus().getCode() + SolrContactRepresenter.SPLIT + contact.getStatus().getName());
                contactSolrDoc.setStatusSorder(contact.getStatus().getOrder());
            }
            if (contact.getLeadSource() != null) {
                contactSolrDoc.setLeadSource(contact.getLeadSource().getName());
                contactSolrDoc.setLeadSourceId(contact.getLeadSource().getId());
                contactSolrDoc.setLeadSourceIdCode(SolrUtils.getIdName(contact.getLeadSource().getId(), contact.getLeadSource().getCode()));
                contactSolrDoc.setLeadSourceIdCodeName(contact.getLeadSource().getId() + SolrContactRepresenter.SPLIT + contact.getLeadSource().getCode() + SolrContactRepresenter.SPLIT + contact.getLeadSource().getName());
                contactSolrDoc.setLeadSourceCode(contact.getLeadSource().getCode());
            }
            contactSolrDoc.setNumber(contact.getNumber());
            contactSolrDoc.setWorkExperience(contact.getWorkExperience());
            contactSolrDoc.setWorkExperienceMonthYear(contact.getWorkExperienceMonthYear());
            contactSolrDoc.setCurrentEmployer(contact.getCurrentEmployer());
            contactSolrDoc.setExpectedSalary(contact.getExpectedSalary());
            contactSolrDoc.setShortList(contact.getShortList());
            contactSolrDoc.setCandidateSkills(contact.getCandidateSkills());
            if (contact.getCandidateProject() != null) {
                contactSolrDoc.setCandidateProjectId(contact.getCandidateProject().getId());
                contactSolrDoc.setCandidateProject(contact.getCandidateProject().getName());
                contactSolrDoc.setCandidateProjectIdName(SolrUtils.getIdName(contact.getCandidateProject().getId(), contact.getCandidateProject().getName()));
            }
            if (contact.getPreferredLocation() != null) {
                contactSolrDoc.setPreferredLocation(contact.getPreferredLocation().getName());
                contactSolrDoc.setPreferredLocationId(contact.getPreferredLocation().getId());
                contactSolrDoc.setPreferredLocationIdName(SolrUtils.getIdName(contact.getPreferredLocation().getId(), contact.getPreferredLocation().getName()));
            }
            if (contact.getCandidateDepartment() != null) {
                contactSolrDoc.setCandidateDepartment(contact.getCandidateDepartment().getName());
                contactSolrDoc.setCandidateDepartmentId(contact.getCandidateDepartment().getId());
                contactSolrDoc.setCandidateDepartmentIdName(SolrUtils.getIdName(contact.getCandidateDepartment().getId(), contact.getCandidateDepartment().getName()));
            }
            if (contact.getCandidatePosition() != null) {
                contactSolrDoc.setCandidatePosition(contact.getCandidatePosition().getName());
                contactSolrDoc.setCandidatePositionId(contact.getCandidatePosition().getId());
                contactSolrDoc.setCandidatePositionIdName((SolrUtils.getIdName(contact.getCandidatePosition().getId(), contact.getCandidatePosition().getName())));
            }
            if (!contact.getVacancy().isEmpty()) {
                contact.getVacancy().forEach(vacancy -> {
                    contactSolrDoc.getVacancyId().add(vacancy.getId());
                    contactSolrDoc.getVacancyName().add(vacancy.getName());
                    contactSolrDoc.getVacancyIdName().add(SolrUtils.getIdName(vacancy.getId(), vacancy.getName()));
                });
            }
            if (contact.getStatus() != null) {
                contactSolrDoc.setCandidateStatus(contact.getStatus().getName());
                contactSolrDoc.setCandidateStatusId(contact.getStatus().getObjectID());
                contactSolrDoc.setCandidateStatusIdName(SolrUtils.getIdName(contact.getStatus().getObjectID(), contact.getStatus().getName()));
            }
            isCandidate = true;
        } else if (contact.isLead()) {
            isLead = true;
            if (contact.getAssignee() != null) {
                contactSolrDoc.setAssignee(contact.getAssignee().getName());
                contactSolrDoc.setAssigneeId(contact.getAssignee().getId());
                contactSolrDoc.setAssigneeIdName(SolrUtils.getIdName(contact.getAssignee().getId(), contact.getAssignee().getName()));
            }
            if (contact.getBackupAssignee() != null) {
                contactSolrDoc.setBackupAssignee(contact.getBackupAssignee().getName());
                contactSolrDoc.setBackupAssigneeId(contact.getBackupAssignee().getId());
                contactSolrDoc.setBackupAssigneeIdName(SolrUtils.getIdName(contact.getBackupAssignee().getId(), contact.getBackupAssignee().getName()));
            }
            if (contact.getRating() != null) {
                contactSolrDoc.setRating(contact.getRating().getName());
                contactSolrDoc.setRatingId(contact.getRating().getId());
                contactSolrDoc.setRatingCode(contact.getRating().getCode());
                contactSolrDoc.setRatingIdCode(SolrUtils.getIdName(contact.getRating().getId(), contact.getRating().getCode()));
            }
            if (contact.getLeadSource() != null) {
                contactSolrDoc.setLeadSource(contact.getLeadSource().getName());
                contactSolrDoc.setLeadSourceId(contact.getLeadSource().getId());
                contactSolrDoc.setLeadSourceCode(contact.getLeadSource().getCode());
                contactSolrDoc.setLeadSourceIdCode(SolrUtils.getIdName(contact.getLeadSource().getId(), contact.getLeadSource().getCode()));
                contactSolrDoc.setLeadSourceIdCodeName(contact.getLeadSource().getId() + SolrContactRepresenter.SPLIT + contact.getLeadSource().getCode() + SolrContactRepresenter.SPLIT + contact.getLeadSource().getName());

            }
            contactSolrDoc.setLeadSourceOther(contact.getLeadSourceOther());
            if (contact.getStatus() != null) {
                contactSolrDoc.setStatus(contact.getStatus().getName());
                contactSolrDoc.setStatusId(contact.getStatus().getObjectID());
                contactSolrDoc.setStatusCode(contact.getStatus().getCode());
                contactSolrDoc.setStatusIdCode(SolrUtils.getIdName(contact.getStatus().getObjectID(), contact.getStatus().getCode()));
                contactSolrDoc.setStatusIdCodeName(SolrUtils.getIdName(contact.getStatus().getObjectID(), contact.getStatus().getCode()));
            }
        }
        if (contact.getCampaign() != null) {
            contactSolrDoc.setCampaignName(contact.getCampaign().getName());
            contactSolrDoc.setCampaignId(contact.getCampaign().getId());
            contactSolrDoc.setCampaignIdName(SolrUtils.getIdName(contact.getCampaign().getId(), contact.getCampaign().getName()));
        }
        if (contact.getCountry() != null) {
            contactSolrDoc.setCountryId(contact.getCountry().getId());
            contactSolrDoc.setCountryName(contact.getCountry().getName());
            contactSolrDoc.setCountryCode(contact.getCountry().getCode());
            contactSolrDoc.setCountryIdCode(SolrUtils.getIdName(contact.getCountry().getId(), contact.getCountry().getCode()));
            contactSolrDoc.setCountryIdCodeName(contact.getCountry().getId() + SolrContactRepresenter.SPLIT + contact.getCountry().getCode() + SolrContactRepresenter.SPLIT + contact.getCountry().getName());
        }
        if (contact.getState() != null) {
            contactSolrDoc.setStateName(contact.getState().getName());
            contactSolrDoc.setStateId(contact.getState().getId());
            contactSolrDoc.setStateIdName(SolrUtils.getIdName(contact.getState().getId(), contact.getState().getName()));
        }
        contactSolrDoc.setCity(contact.getCity());
        contactSolrDoc.setStreet(contact.getStreet());
        contactSolrDoc.setStreet2(contact.getStreet2());
        contactSolrDoc.setPostCode(contact.getPostCode());
        contactSolrDoc.setLongitude(contact.getLongitude());
        contactSolrDoc.setLatitude(contact.getLatitude());
        if (customFields != null) {
            CustomFieldsUtils.setSolrDocDynamicFields(contactSolrDoc, customFields);
        }

        log.info(String.format("cId=%s, %s", companyID, isLead ? "LEAD -> " : (isCandidate ? "Candidate -> " : "Contact -> ") + "_" + contact.getObjectId() + " is going to be added to solr" + " time=" + (System.currentTimeMillis() - start) + "ms"));

        return contactSolrDoc;
    }

    @Transactional
    public Page<ContactSolrDoc> getList(ListingFilterParameter fp, ListLoadConfig config, EdsUser user) {

        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (config.getLimit() == 0) {
            config.setLimit(20);
        }
        FacetFilterRpc contactFacetFilter = fp.getFacetFilter();
        if (contactFacetFilter != null && !contactFacetFilter.isFilterChanges()) {
            contactFacetFilter = commonServiceLocal.getUserFacetFilter(contactFacetFilter);
        }

        EdsCompany edsCompany = user.getCompany();
        String categoryIdsForUserForSolr = fp.isFiltirize() ? contactCategoryManager.getCategoryIDsForUserForSOLR(null, user, null, null) : null;

        StringBuilder query = new StringBuilder();
        query.append(getContactListSolrQuery(fp, contactFacetFilter, user, categoryIdsForUserForSolr));
        if (fp.isFiltirize()) {
            String facetFilterQuery = SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(contactFacetFilter, edsCompany, SolrContactRepresenter.FIELD_UPDATE_DATE, null);
            if (!StringUtils.isEmpty(facetFilterQuery)) {
                query.append(facetFilterQuery);
            }
        }
        SimpleQuery solrQuery = new SimpleQuery(new SimpleStringCriteria(query.toString()));

        if (fp.isDetectDuplicates()) {
            Set<String> duplicates = getContactDuplicateNames(solrQuery, fp.getObjectIDs());
            if (duplicates != null && !duplicates.isEmpty()) {
                StringBuilder duplicateQuery = new StringBuilder();
                boolean isFirst = true;
                boolean found = false;
                for (String duplicate : duplicates) {
                    duplicate = duplicate.trim();
                    if (!"".equals(duplicate)) {
                        if (isFirst) {
                            found = true;
                            duplicateQuery.append(SolrContactRepresenter.FIELD_CONTACT_NAME_TEXT_FIELD).append(":(");
                        }
                        duplicateQuery.append(!isFirst ? " OR " : "").append(duplicate).append(" OR ").append(duplicate).append("*");
                        isFirst = false;
                    }
                }
                if (found) {
                    duplicateQuery.append(")");
                }
                if (!duplicateQuery.isEmpty()) {
                    solrQuery.addCriteria(new SimpleStringCriteria(" AND (" + duplicateQuery + ")"));
                }
            }
        }

        Sort solrSort = null;
        if (!fp.isSearchButton() && !fp.isLookUp()) {
            if (fp.isDetectDuplicates()) {
                boolean desc = false;
                if (Constants.DESC == config.getSortDir()) {
                    desc = true;
                }
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                solrSort = Sort.by(sortDirection, SolrContactRepresenter.SORTABLE_CONTACT_NAME);
            } else {
                if (config.getSortField() != null && !"".equals(config.getSortField())) {
                    boolean desc = false;
                    if (Constants.DESC == config.getSortDir()) {
                        desc = true;
                    }
                    Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                    String solrSortField = SolrContactRepresenter.getSortingField(config.getSortField());
                    if (solrSortField != null) {
                        solrSort = Sort.by(sortDirection, solrSortField);
                    } else {
                        solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(config.getSortField(), desc, true);
                    }
                } else {
                    if (fp.isFavourite()) {
                        solrSort = Sort.by(Sort.Direction.DESC, SolrContactRepresenter.FIELD_IS_FAVOURITED, SolrContactRepresenter.FIELD_UPDATE_DATE);
                    } else {
                        solrSort = Sort.by(Sort.Direction.DESC, SolrContactRepresenter.FIELD_IS_FAVOURITED, SolrContactRepresenter.FIELD_UPDATE_DATE);
                    }
                }
            }
        } else {
            solrSort = Sort.by(Sort.Direction.DESC, SolrContactRepresenter.FIELD_IS_FAVOURITED, SolrContactRepresenter.FIELD_UPDATE_DATE);
        }
        if (!fp.isFiltirize() && fp.isLookUp()) {
            solrQuery.addProjectionOnFields(SolrContactRepresenter.FIELD_PRIMARY_EMAIL, SolrContactRepresenter.FIELD_FIRST_NAME, SolrContactRepresenter.FIELD_LAST_NAME, SolrContactRepresenter.FIELD_CONTACT_ID);//we need only these field for lookup in message center.
        }

        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        solrQuery.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_CONTACT_CORE, solrQuery, ContactSolrDoc.class);
    }

    @Transactional
    public Page<ContactSolrDoc> getCandidateList(ListingFilterParameter fp, EdsUser user) {

        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (fp.getLimit() == 0) {
            fp.setLimit(20);
        }
        FacetFilterRpc facetFilter = fp.getFacetFilter();
        if (facetFilter != null && !facetFilter.isFilterChanges()) {
            facetFilter = commonServiceLocal.getUserFacetFilter(facetFilter);
        }

        EdsCompany edsCompany = user.getCompany();
        fp.setUserID(user.getObjectID());
        StringBuilder candidateSolrQuery = new StringBuilder(QueryBuilderForSolr.getCandidateListSolrQuery(fp, facetFilter, edsCompany, user));

        String facetFilterQuery = SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(facetFilter, edsCompany, SolrContactRepresenter.FIELD_UPDATE_DATE, null);
        if (!StringUtils.isEmpty(facetFilterQuery)) {
            candidateSolrQuery.append(facetFilterQuery);
        }
        SimpleQuery solrQuery = new SimpleQuery(new SimpleStringCriteria(candidateSolrQuery.toString()));

        Sort solrSort = null;
        if (!fp.isSearchButton() && !fp.isLookUp()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                boolean desc = false;
                if (Constants.DESC == fp.getSortDir()) {
                    desc = true;
                }
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                String solrSortField = SolrContactRepresenter.getSortingField(fp.getSortField());
                if (solrSortField != null) {
                    solrSort = Sort.by(sortDirection, solrSortField);
                } else {
                    solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(fp.getSortField(), desc, true);
                }
            } else {
                solrSort = Sort.by(Sort.Direction.DESC, SolrContactRepresenter.FIELD_UPDATE_DATE);
            }
        } else {
            solrSort = Sort.by(Sort.Direction.DESC, SolrContactRepresenter.FIELD_UPDATE_DATE);
        }

        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        solrQuery.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_CONTACT_CORE, solrQuery, ContactSolrDoc.class);
    }

    @Transactional
    public Page<ContactSolrDoc> getLeadList(ListingFilterParameter fp, ListLoadConfig config) {

        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (config.getLimit() == 0) {
            config.setLimit(20);
        }

        FacetFilterRpc leadFacetFilter = fp.getFacetFilter();
        if (leadFacetFilter != null) {
            leadFacetFilter.setUserID(fp.getUserID());
            if (!leadFacetFilter.isFilterChanges()) {
                leadFacetFilter = commonServiceLocal.getUserFacetFilter(leadFacetFilter);
            }
        }
        EdsUser edsUser;
        if (fp.getUserID() != null) {
            edsUser = userManager.get(fp.getUserID());
        } else {
            edsUser = crmContactManager.getUser();
        }
        EdsCompany edsCompany = edsUser.getCompany();

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getLeadListFacetFilterAssigneeQuery(edsCompany, edsUser, fp, leadFacetFilter, null));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(leadFacetFilter, edsCompany, null, null));

        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery.toString()));
        if (fp.isDetectDuplicates()) {
            Set<String> duplicates = getContactDuplicateNames(query, fp.getObjectIDs());
            if (duplicates != null && !duplicates.isEmpty()) {
                StringBuilder duplicateQuery = new StringBuilder();
                boolean isFirst = true;
                boolean found = false;
                for (String duplicate : duplicates) {
                    duplicate = duplicate.trim();
                    if (!"".equals(duplicate)) {
                        if (isFirst) {
                            found = true;
                            duplicateQuery.append(SolrContactRepresenter.FIELD_CONTACT_NAME_L_TEXT_FIELD).append(":(");
                        }
                        duplicateQuery.append(!isFirst ? " OR " : "").append(duplicate).append(" OR ").append(duplicate).append("*");
                        isFirst = false;
                    }
                }
                if (found) {
                    duplicateQuery.append(")");
                }
                if (!duplicateQuery.isEmpty()) {
                    query.addCriteria(new SimpleStringCriteria(" AND (" + duplicateQuery + ")"));
                }
            }
        }

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrContactRepresenter.FIELD_UPDATE_DATE);
        if (!fp.isSearchButton() && !fp.isLookUp()) {
            if (!fp.isDetectDuplicates()) {
                if (config.getSortField() != null && !"".equals(config.getSortField())) {
                    boolean desc = Constants.DESC == config.getSortDir();
                    Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                    String solrSortField = SolrContactRepresenter.getSortingField(config.getSortField());
                    if (solrSortField != null) {
                        solrSort = Sort.by(sortDirection, solrSortField);
                    } else {
                        solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(config.getSortField(), desc, true);
                    }
                }
            }
        }

        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), config.getLimit(), solrSort));

        return solrTemplate.query(SOLR_CONTACT_CORE, query, ContactSolrDoc.class);
    }

    public FacetFilterRpc getContactFacetFilterData(FacetFilterRpc contactFacet) {
        if (!contactFacet.isFilterChanges()) {
            contactFacet = commonServiceLocal.getUserFacetFilter(contactFacet);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        if (contactFacet != null && contactFacet.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE) != null && contactFacet.getCustomDataValue(FacetFilterCutomField.RELATION_ID) != null) {
            if (contactFacet.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE).equalsIgnoreCase(RelationItem.TYPE_CAMPAIGN)) {
                fp.setCampaignID(Integer.parseInt(contactFacet.getCustomDataValue(FacetFilterCutomField.RELATION_ID)));
            }
            if (contactFacet.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE).equalsIgnoreCase(RelationItem.TYPE_CRM_ACCOUNT)) {
                fp.setCrmAccountId(Integer.parseInt(contactFacet.getCustomDataValue(FacetFilterCutomField.RELATION_ID)));
            }
        }
        fp.setSearchKey(contactFacet.getSearchKey());
        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        String caegoryIdsForUserForSolr = contactCategoryManager.getCategoryIDsForUserForSOLR(null, edsUser, null, null);

        String[] fields = contactFacet.getSolrFieldMapCodeList(FacetContentType.ContactFacetFilter.getContentCode()[5]);

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(crmServiceLocal.getContactListSolrQuery(fp, contactFacet, edsUser, caegoryIdsForUserForSolr));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(contactFacet, edsCompany, SolrContactRepresenter.FIELD_UPDATE_DATE, null,
                FacetContentType.ContactFacetFilter.getContentCode()[5] // fields to remove!!!
        ));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_CONTACT_CORE, solrQuery.toString(), contactFacet, ContactSolrDoc.class);

        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, contactFacet, fields);
        if (contactFacet.getShowSolrFieldMap().containsKey(FacetContentType.ContactFacetFilter.getContentCode()[5])) {
            SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, contactFacet, FacetContentType.ContactFacetFilter.getContentCode()[5]);
        }
        return contactFacet;
    }

    public FacetFilterRpc getCandidateFacetFilterData(FacetFilterRpc candidateFacet) {
        if (!candidateFacet.isFilterChanges()) {
            candidateFacet = commonServiceLocal.getUserFacetFilter(candidateFacet);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(candidateFacet.getSearchKey());
        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        StringBuilder solrQuery = new StringBuilder();
        fp.setShortList(candidateFacet.getCustomDataValue(FacetFilterCutomField.ISSHORTLIST) != null && "true".equals(candidateFacet.getCustomDataValue(FacetFilterCutomField.ISSHORTLIST)));
        fp.setUserID(edsUser.getObjectID());
        solrQuery.append(QueryBuilderForSolr.getCandidateListFacetFilterAssigneeQuery(edsCompany, edsUser, fp, candidateFacet, null));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(candidateFacet, edsCompany, null, null));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_CONTACT_CORE, solrQuery.toString(), candidateFacet, ContactSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, candidateFacet, candidateFacet.getSolrFieldMapCodeList());

        return candidateFacet;
    }

    public FacetFilterRpc getLeadFacetFilterData(FacetFilterRpc leadFacet) {
        if (!leadFacet.isFilterChanges()) {
            leadFacet = commonServiceLocal.getUserFacetFilter(leadFacet);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(leadFacet.getSearchKey());
        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        StringBuilder solrQuery = new StringBuilder();

        solrQuery.append(QueryBuilderForSolr.getLeadListFacetFilterAssigneeQuery(edsCompany, edsUser, fp, leadFacet, null));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(leadFacet, edsCompany, null, null));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_CONTACT_CORE, solrQuery.toString(), leadFacet, ContactSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, leadFacet, leadFacet.getSolrFieldMapCodeList(FacetContentType.LeadFacetFilter.getContentCode()[5]));

        if (leadFacet.getFacetContentMap().containsKey(FacetContentType.LeadFacetFilter.getContentCode()[5])) {
            getLeadFacetAssigneeResultFromSolr(facetPage, leadFacet);
        }

        return leadFacet;
    }

    private FacetFilterRpc getLeadFacetAssigneeResultFromSolr(QueryResponse resp, FacetFilterRpc leadFacetFilter) {
        String assigneeKey = FacetContentType.LeadFacetFilter.getContentCode()[5];
        if (leadFacetFilter.getFacetContentMap().containsKey(assigneeKey)) {
            FacetField assigneeFacet = resp.getFacetField(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME);
            FacetField bassigneeFacet = resp.getFacetField(SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE_ID_NAME);
            assigneeFacet = mergeFacetFields(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME, assigneeFacet, bassigneeFacet);
            resp.getFacetFields().add(/*4, */assigneeFacet);// merged assignee facet field set
            SolrFacetUtils.fillFacetFilterDataWithNA(resp, leadFacetFilter, assigneeKey);
        }
        return leadFacetFilter;
    }

    private QueryResponse getSolrResponse(String solrCore, String solrQuery, FacetFilterRpc facetFilterData, boolean isMissing, int facetLimit) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(solrCore);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);

        for (String key : facetFilterData.getShowSolrFieldMap().keySet()) {
            query.addFacetField(facetFilterData.getShowSolrFieldMap().get(key).getSolrFacetFieldName());
        }
        query.setFacetMinCount(1);
        query.setFacet(true);

        if (Constants.SOLR_TASK_CORE.equals(solrCore)) {
            query.set(GroupParams.GROUP, true);
            query.set(GroupParams.GROUP_TRUNCATE, true);
            query.set(GroupParams.GROUP_MAIN, true);
            query.set(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_TASK_ID);
        }

        if (facetLimit != -1) {
            query.setFacetLimit(facetLimit);
        } else {
            query.setFacetLimit(WfmJpaTemplate.SOLR_FACET_LIMIT);
        }

        if (isMissing) {
            query.setFacetMissing(true);
        }

        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    private FacetFilterRpc getLeadFacetAssigneeResultFromSolr(FacetPage facetPage, FacetFilterRpc leadFacetFilter) {
        String assigneeKey = FacetContentType.LeadFacetFilter.getContentCode()[5];
        if (leadFacetFilter.getFacetContentMap().containsKey(assigneeKey)) {
            Page<SimpleFacetFieldEntry> assigneeFacet = facetPage.getFacetResultPage(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME);
            Page<SimpleFacetFieldEntry> bassigneeFacet = facetPage.getFacetResultPage(SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE_ID_NAME);
//            assigneeFacet = mergeFacetFields(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME, assigneeFacetList, facetPage); todo

            facetPage.getFacetFields().add(/*4, */assigneeFacet);// merged assignee facet field set
            SolrFacetUtils.fillNewSolr86FacetFilterDataWithNA(facetPage, leadFacetFilter, assigneeKey);
        }
        return leadFacetFilter;
    }

    private FacetField mergeFacetFields(String facedFieldName, FacetField... facetFields) {
        Map<String, Integer> fields = new TreeMap<>();
        if (facetFields != null && facetFields.length > 0) {
            boolean isNullSet = false;//faqat birinchisini nullarini hisoblaymiz tamom boshqalarini tashlavoramiz xolos...
            for (FacetField facetField : facetFields) {
                if (facetField != null) {
                    for (FacetField.Count count : facetField.getValues()) {
                        if (count.getName() != null) {
                            if (fields.containsKey(count.getName())) {
                                fields.put(count.getName(), Integer.valueOf((fields.get(count.getName()).longValue() + count.getCount()) + ""));
                            } else {
                                fields.put(count.getName(), Integer.valueOf(count.getCount() + ""));
                            }
                        } else if (!isNullSet) {
                            fields.put("N/A", Integer.valueOf(count.getCount() + ""));
                            isNullSet = true;
                        }
                    }
                }
            }
        }
        FacetField returning = new FacetField(facedFieldName);
        for (Map.Entry<String, Integer> entry : fields.entrySet()) {
            returning.add("N/A".equals(entry.getKey()) ? null : entry.getKey(), entry.getValue());
        }
        return returning;
    }


    public String getContactListSolrQuery(ListingFilterParameter fp,
                                          FacetFilterRpc contactFilter,
                                          EdsUser user,
                                          String categoryIdForUserForSolrQuery) {

        StringBuffer solrQuery = new StringBuffer("(");
        solrQuery.append(SolrContactRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());

        List<String> customAccessRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.CRM_SEE_ALL_CONTACT_LIST);
        boolean hasCustomFullAccessToListing = customAccessRoles.size() > 0 && user.hasEitherRoles(customAccessRoles.toArray(new String[]{}));

        if (fp.isFiltirize() && categoryIdForUserForSolrQuery != null && !"".equals(categoryIdForUserForSolrQuery)) {
            solrQuery.append(" AND (");
            solrQuery.append(categoryIdForUserForSolrQuery);
            solrQuery.append(")");
        }

        // Set Search key
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            if (fp.isFromMobile()) {
                solrQuery.append(" AND ((").append(SolrContactRepresenter.FIELD_CONTACT_NAME);
                solrQuery.append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), true)).append(")");
                solrQuery.append(" OR ").append(SolrContactRepresenter.FIELD_PRIMARY_PHONE);
                solrQuery.append(":(*").append(fp.getSearchKey()).append("*))");
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                Map<String, Double> fields = new HashMap<>();
                fields.put(SolrContactRepresenter.FIELD_LOOKUP_COMPOSITE_MOBILE, SolrSearchUtils.HIGH_PRIORITY);
                searchUtils.generateApiSearchQuery(solrQuery, fields, fp.getSearchKey());
                solrQuery.append(")");
            } else {
                solrQuery.append(" AND (");
                if (fp.isLookUp()) {
                    if (Constants.BY_BOTH.equals(fp.getLookUpBy())) {
                        solrQuery.append(SolrContactRepresenter.FIELD_EMAIL_NAME_COMPOSITE);
                    } else if (Constants.BY_EMAIL.equals(fp.getLookUpBy())) {
                        solrQuery.append(SolrContactRepresenter.FIELD_EMAIL_COMPOSITE);
                    } else if (fp.isLetterSearch()) {
                        solrQuery.append("((");
                        solrQuery.append(SolrContactRepresenter.FIELD_CONTACT_FIRST_COMPOSITE);
                        solrQuery.append(":").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), true));
                        solrQuery.append(")^20000.0) OR ((");
                        solrQuery.append(SolrContactRepresenter.FIELD_CONTACT_LAST_COMPOSITE);
                        solrQuery.append(":").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), true));
                        solrQuery.append(")^0.02)");
                    } else {
                        solrQuery.append(SolrContactRepresenter.FIELD_CONTACT_NAME_COMPOSITE);
                    }
                } else if (fp.isWidgetSearch()) {
                    solrQuery.append(SolrContactRepresenter.FIELD_CONTACT_NAME_COMPOSITE);
                } else {
                    solrQuery.append(SolrContactRepresenter.FIELD_COMPOSITE);
                }
                if (!fp.isLetterSearch()) {
                    solrQuery.append(":(").append(QueryBuilderForSolr.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp()));
                    solrQuery.append(")");
                }

                if (!fp.isLookUp()) {
                    SolrSearchUtils searchUtils = new SolrSearchUtils();
                    searchUtils.generateSearchQuery(solrQuery, QueryBuilderForSolr.getCrmContactSearchFields(), fp.getSearchKey());
                }
                solrQuery.append(")");
            }
        }

        //If Client Access enabled
        if (fp.isAccessEnabled()) {
            solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_ACCESS_ENABLED).append(":").append(fp.isAccessEnabled());
        }
        // for mycontacts widget
        if (fp.getContactID() != null && !StringUtils.isNotBlank(fp.getSearchKey())) {
            solrQuery.append(" AND -").append(SolrContactRepresenter.FIELD_CONTACT_ID).append(":").append(fp.getContactID());
        }
        //End Of If Client Access enabled
        //If filter by CrmAccount Type
        if (StringUtils.isNotBlank(fp.getAccountType())) {
            solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CRM_ACCOUNT_TYPE).append(":").append(fp.getAccountType());
        }
        //End of filter by CrmAccount Type

        if (!hasCustomFullAccessToListing) {
            boolean ownerAccess = ServerUtils.hasPermission(PermissionConstants.CONTACT_SEE_OWN);
            StringBuilder clientIDsStr = new StringBuilder();
            if (fp.getClientId() != null) {
                EdsCrmAccount crmAccount = crmAccountManager.get(fp.getClientId());
                ownerAccess = ownerAccess && crmAccount.getOwners().contains(user);
            }
            if (ownerAccess && !user.hasRole(EdsRole.ADMIN_CODE)) {
                List<Integer> clientIDs = crmAccountManager.getAccountIDsByOwner(user.getObjectID());
                if (clientIDs != null && clientIDs.size() > 0) {
                    for (Integer clientID : clientIDs) {
                        clientIDsStr.append(" ").append(clientID);
                    }
                }
            }

            if (!clientIDsStr.toString().trim().isEmpty()) {
                solrQuery.append(" AND (");
                solrQuery.append(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID).append(":(").append(clientIDsStr.toString().trim()).append(") ");
                solrQuery.append(")");
            }
            if (!ownerAccess && !user.hasRole(EdsRole.ADMIN_CODE)) {
                solrQuery.append(" AND ( ");
                solrQuery.append(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
                solrQuery.append(")");
            }
        }

        if (fp.isFiltirize()) {
            if (fp.getAccountID() != null) {
                solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID).append(":").append(fp.getAccountID());
            }
            if (fp.getCampaignID() != null) {
                solrQuery.append(" AND ").append(SolrContactRepresenter.FIELD_CAMPAIGN_ID).append(":").append(fp.getCampaignID());
            }

            if (contactFilter != null && contactFilter.getFacetContentMap().containsKey(FacetContentType.ContactFacetFilter.getContentCode()[5])) {
                SelectItem[] items = contactFilter.getFacetContentMap().get(FacetContentType.ContactFacetFilter.getContentCode()[5]).getFacetItems();
                if (items.length != 0) {
                    solrQuery.append(" AND (");
                    boolean appendOperator = false;
                    for (SelectItem item : items) {
                        if (appendOperator) {
                            solrQuery.append(!fp.useAndOperator() ? " OR " : " AND ");
                        } else {
                            appendOperator = true;
                        }
                        if (fp.useAndOperator() && item.getDescription() != null && !"".equals(item.getDescription().trim())) {
                            solrQuery.append(SolrContactRepresenter.FIELD_CATEGORY_ID).append(":(").append(item.getId()).append(" ").append(item.getDescription()).append(")");
                        } else {
                            solrQuery.append(SolrContactRepresenter.FIELD_CATEGORY_ID).append(":").append(item.getId());
                        }
                    }
                    solrQuery.append(") ");
                }
            }
        }
        solrQuery.append(")");

        if (fp.isFiltirize() && fp.isFromOutlook()) {
            EdsUser edsUser = (EdsUser) ServerSecurityContext.getInstance().getUser();
            solrQuery.append("OR (");
            solrQuery.append(QueryBuilderForSolr.getLeadListFacetFilterAssigneeQuery(edsUser.getCompany(), edsUser, fp, null, null));
            solrQuery.append(")");
        }

        return solrQuery.toString();
    }

    private Set<String> getContactDuplicateNames(FilterQuery filterQuery, List<Integer> inIDs) {
        SimpleQuery simpleSolrQuery = new SimpleQuery(filterQuery.getCriteria());
        simpleSolrQuery.setPageRequest(PageRequest.of(0, 10000));
        Page<ContactSolrDoc> contactSolrDocPage = solrTemplate.query(SOLR_CONTACT_CORE, simpleSolrQuery, ContactSolrDoc.class);

        List<Integer> contactIds = contactSolrDocPage.getContent().stream().map(contactSolrDoc -> contactSolrDoc.getContactId()).collect(Collectors.toList());

        return crmContactManager.getDuplicateNamesSet(contactIds, inIDs);
    }

    public List<ContactSolrDoc> getDocumentsExistingInBase(List<ContactSolrDoc> contactSolrDocs, String type) {
        List<ContactSolrDoc> contactSolrDocsNew = new ArrayList<>();
        Map<Integer, ContactSolrDoc> mapDocuments = new HashMap<>();
        if (!contactSolrDocs.isEmpty()) {
            for (ContactSolrDoc doc : contactSolrDocs) {
                contactSolrDocsNew.add(doc);
                mapDocuments.put(doc.getContactId(), doc);
            }
        }
        List<Integer> objectIDsFromDatabase = null;
        objectIDsFromDatabase = RelationItem.TYPE_LEAD.equals(type) ? crmContactManager.getLeadIDsByIDs(new ArrayList(mapDocuments.keySet())) : crmContactManager.getContactIDsByIDs(new ArrayList(mapDocuments.keySet()));
        if (objectIDsFromDatabase != null && objectIDsFromDatabase.size() > 0) {
            for (Integer objectID : objectIDsFromDatabase) {
                mapDocuments.remove(objectID);
            }
            if (mapDocuments.size() > 0) {
                contactSolrDocsNew.removeAll(mapDocuments.values());
            }
        }
        return contactSolrDocsNew;
    }

    /**
     * @param contactSolrDocs
     * @return
     * @TODO talking with Normurod aka and remove this method, because above use same name method. I think these method do same business logic(Comment write by Dilshod)
     */
    public List<ContactSolrDoc> getDocumentsExistingInBase2(List<ContactSolrDoc> contactSolrDocs) {
        List<ContactSolrDoc> documents = new ArrayList<>();
        Map<Integer, ContactSolrDoc> mapContactDocuments = new HashMap<>();
        Map<Integer, ContactSolrDoc> mapLeadDocuments = new HashMap<>();
        if (!contactSolrDocs.isEmpty()) {
            for (ContactSolrDoc doc : contactSolrDocs) {
                documents.add(doc);
                Integer contactType = doc.getContactType();
                if (contactType != null) {
                    if (contactType.equals(CrmConstants.TYPE_LEAD_CONTACT)) {
                        mapLeadDocuments.put(doc.getContactId(), doc);
                    } else {
                        mapContactDocuments.put(doc.getContactId(), doc);
                    }
                }
            }
        }
        List<Integer> contactIDsFromDatabase = crmContactManager.getContactIDsByIDs(new ArrayList(mapContactDocuments.keySet()));
        List<Integer> leadIDsFromDatabase = crmContactManager.getLeadIDsByIDs(new ArrayList(mapLeadDocuments.keySet()));


        if (contactIDsFromDatabase != null && contactIDsFromDatabase.size() > 0) {
            for (Integer objectID : contactIDsFromDatabase) {
                mapContactDocuments.remove(objectID);
            }
            if (mapContactDocuments.size() > 0) {
                documents.removeAll(mapContactDocuments.values());
            }
        }

        if (leadIDsFromDatabase != null && leadIDsFromDatabase.size() > 0) {
            for (Integer objectID : leadIDsFromDatabase) {
                mapLeadDocuments.remove(objectID);
            }
            if (mapLeadDocuments.size() > 0) {
                documents.removeAll(mapLeadDocuments.values());
            }
        }
        return documents;
    }

    public ArrayList<SelectItem> getCrmLookNamesForContact(Page<ContactSolrDoc> contactSolrDocPage, ListingFilterParameter filterParametrs) throws SolrServerException, IOException {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        if (contactSolrDocPage != null && contactSolrDocPage.getContent() != null && contactSolrDocPage.getContent().size() > 0) {
            for (int i = 0, resultsSize = contactSolrDocPage.getContent().size(); i < resultsSize; i++) {
                ContactSolrDoc contactSolrDoc = contactSolrDocPage.getContent().get(i);
                String nameField = Constants.BY_EMAIL.equals(filterParametrs.getLookUpBy()) ? contactSolrDoc.getPrimaryEmail() : contactSolrDoc.getContactName();
                if (Constants.BY_BOTH.equals(filterParametrs.getLookUpBy())) {
                    String firstName = contactSolrDoc.getFirstName();
                    String lastname = contactSolrDoc.getLastName();
                    String email = contactSolrDoc.getPrimaryEmail();
                    if (email != null) {
                        firstName = firstName != null ? firstName + " " : "";
                        lastname = lastname != null ? lastname : "";
                        selectItems.add(new SelectItem(contactSolrDoc.getContactId(), firstName + lastname + "<" + email + ">"));
                    }
                } else if (filterParametrs.isWithCode()) {
                    String number = contactSolrDoc.getNumber();
                    String name = (!"".equals(number) ? number + " -> " + nameField : "");
                    SelectItem item = new SelectItem(contactSolrDoc.getContactId(), name, number);
                    item.setReferenceCode(number);
                    item.setCode(number);
                    selectItems.add(item);
                } else {
                    selectItems.add(new SelectItem(contactSolrDoc.getContactId(), nameField));
                }
            }
        }
        return selectItems;
    }

}
