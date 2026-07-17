package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.solr.document.CrmAccountSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.CrmAccountSolrDocRepository;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrClientRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSupplierRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ACCOUNTANT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADMIN;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CLIENT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DR;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PM;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CRM_ACCOUNT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SUPPLIER;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:26.
 */
@Component
public class CrmAccountSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(CrmAccountSolrComponent.class);

    @Autowired
    private CrmAccountSolrDocRepository crmAccountSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    @Qualifier("wfmLocalizer")
    private WfmMessageSource wfmLocalizer;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsCrmAccount edsCrmAccount) throws InterruptedException {
        this.indexes(Arrays.asList(edsCrmAccount));
    }

    @Transactional
    public void indexes(List<EdsCrmAccount> edsCrmAccountList) throws InterruptedException {

        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsCrmAccountList)) {
            List<CrmAccountSolrDoc> contactSolrDocs = new ArrayList<>();

            for (EdsCrmAccount edsCrmAccount : edsCrmAccountList) {
                if (edsCrmAccount != null && !edsCrmAccount.isDeleted()) {
                    try {
                        contactSolrDocs.add(createCrmAccountDocument(edsCrmAccount.getSolrRPC(), companyID, edsCrmAccount.getCustomFields()));
                        log.info("Indexed CrmAccount Core CID - {}, objId - {}", companyID, edsCrmAccount.getObjectID());
                    } catch (Exception e) {
                        log.error("********************* EdsCrmAccount = {} **********************", edsCrmAccount.getAccountNo());
                        throw e;
                    }
                }
            }

            if (!contactSolrDocs.isEmpty()) {
                log.info("========= Create EdsCrmAccount solr docs for company {} with size {} =========", companyID, contactSolrDocs.size());
                crmAccountSolrDocRepository.saveAll(contactSolrDocs);
            }
        }

    }

    @Transactional
    public void indexConcurrently(List<EdsCrmAccount> edsCrmAccountList) throws InterruptedException {
        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsCrmAccountList)) {
            ConcurrentLinkedQueue<CrmAccountSolrDoc> contactSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsCrmAccount edsCrmAccount : edsCrmAccountList) {
                if (edsCrmAccount != null && !edsCrmAccount.isDeleted()) {
                    CrmAccountSolrItem solrRPC = edsCrmAccount.getSolrRPC();
                    EdsCrmCustomFields customFields = edsCrmAccount.getCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        contactSolrDocs.add(createCrmAccountDocument(solrRPC, companyID, customFields));
                                        log.info("Indexed Crm Account Core CID - {}, objId - {}", companId, edsCrmAccount.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Crm Account = {} **********************", edsCrmAccount.getAccountNo());
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
                log.error("Error on loading CrmAccount list", e);
            }

            if (!contactSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Crm Account solr docs for company {} with size {} =========", companyID, contactSolrDocs.size());
                    crmAccountSolrDocRepository.saveAll(contactSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving CrmAccount list", e);
                }
            }
        }

    }

    protected String getSynchronizedKey(CrmAccountSolrItem solrRPC) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + solrRPC.getObjectId();
    }

    private CrmAccountSolrDoc createCrmAccountDocument(CrmAccountSolrItem account, Integer companyID, EdsCustomFields customFields) {
        CrmAccountSolrDoc crmAccountSolrDoc = new CrmAccountSolrDoc();

        crmAccountSolrDoc.setOid(SolrUtils.generatedOId(companyID, account.getObjectId()));
        crmAccountSolrDoc.setCompanyId(companyID);
        crmAccountSolrDoc.setCrmAccountId(account.getObjectId());
        crmAccountSolrDoc.setCrmAccountName(account.getCrmAccount().getName());
        crmAccountSolrDoc.setCrmAccountIdName(SolrUtils.getIdName(account.getObjectId(), account.getCrmAccount().getName()));
        crmAccountSolrDoc.setBlocked(account.getBlocked());

        if (account.getOwner() != null) {
            account.getOwner().forEach(owner -> {
                crmAccountSolrDoc.getOwnerId().add(owner.getId());
                crmAccountSolrDoc.getOwnerName().add(owner.getName());
                crmAccountSolrDoc.getOwnerIdName().add(SolrUtils.getIdName(owner.getId(), owner.getName()));
            });
        }
        if (account.getCrmAccountParent() != null) {
            crmAccountSolrDoc.setCrmAccountParentId(account.getCrmAccountParent().getId());
            crmAccountSolrDoc.setCrmAccountParentName(account.getCrmAccountParent().getName());
            crmAccountSolrDoc.setCrmAccountParentIdName(SolrUtils.getIdName(account.getCrmAccountParent().getId(), account.getCrmAccountParent().getName()));
        }
        crmAccountSolrDoc.setCrmAccountNumber(account.getCrmAccount().getCode());

        if (account.getOwnership() != null) {
            crmAccountSolrDoc.setOwnershipId(account.getOwnership().getId());
            crmAccountSolrDoc.setOwnershipName(account.getOwnership().getName());
            crmAccountSolrDoc.setOwnershipCode(account.getOwnership().getDescription());
            crmAccountSolrDoc.setOwnershipIdCode(SolrUtils.getIdName(account.getOwnership().getId(), account.getOwnership().getDescription()));
            crmAccountSolrDoc.setOwnershipIdCodeName(account.getOwnership().getId() + SolrCrmAccountRepresenter.SPLIT + account.getOwnership().getDescription() + SolrCrmAccountRepresenter.SPLIT + account.getOwnership().getName());
        }
        if (account.getOrganizationType() != null) {
            crmAccountSolrDoc.setOrganizationTypeId(account.getOrganizationType().getId());
            crmAccountSolrDoc.setOrganizationTypeName(account.getOrganizationType().getName());
            crmAccountSolrDoc.setOrganizationTypeCode(account.getOrganizationType().getDescription());
            crmAccountSolrDoc.setOrganizationTypeIdCode(SolrUtils.getIdName(account.getOrganizationType().getId(), account.getOrganizationType().getCode()));
            crmAccountSolrDoc.setOrganizationTypeIdCodeName(account.getOrganizationType().getId() + SolrCrmAccountRepresenter.SPLIT + account.getOrganizationType().getDescription() + SolrCrmAccountRepresenter.SPLIT + account.getOrganizationType().getName());
        }
        if (account.getTax() != null) {
            crmAccountSolrDoc.setTaxId(account.getTax().getId());
            crmAccountSolrDoc.setTaxName(account.getTax().getName());
            crmAccountSolrDoc.setTaxIdName(SolrUtils.getIdName(account.getTax().getId(), account.getTax().getName()));
        }
        if (account.getNumberOfEmployees() != null) {
            crmAccountSolrDoc.setNumberOfEmployeesId(account.getNumberOfEmployees().getId());
            crmAccountSolrDoc.setNumberOfEmployeesName(account.getNumberOfEmployees().getName());
            crmAccountSolrDoc.setNumberOfEmployeesCode(account.getNumberOfEmployees().getDescription());
            crmAccountSolrDoc.setNumberOfEmployeesIdCode(SolrUtils.getIdName(account.getNumberOfEmployees().getId(), account.getNumberOfEmployees().getDescription()));
        }
        if (account.getAnnualRevenue() != null) {
            crmAccountSolrDoc.setAnnualRevenueId(account.getAnnualRevenue().getId());
            crmAccountSolrDoc.setAnnualRevenueName(account.getAnnualRevenue().getName());
            crmAccountSolrDoc.setAnnualRevenueCode(account.getAnnualRevenue().getDescription());
            crmAccountSolrDoc.setAnnualRevenueIdCode(SolrUtils.getIdName(account.getAnnualRevenue().getId(), account.getAnnualRevenue().getDescription()));
            crmAccountSolrDoc.setAnnualRevenueIdCodeName(account.getAnnualRevenue().getId() + SolrCrmAccountRepresenter.SPLIT + account.getAnnualRevenue().getDescription() + SolrCrmAccountRepresenter.SPLIT + account.getAnnualRevenue().getName());
        }
        if (account.getRating() != null) {
            crmAccountSolrDoc.setRatingId(account.getRating().getId());
            crmAccountSolrDoc.setRatingName(account.getRating().getName());
            crmAccountSolrDoc.setRatingCode(account.getRating().getDescription());
            crmAccountSolrDoc.setRatingIdCode(SolrUtils.getIdName(account.getRating().getId(), account.getRating().getCode()));
        }
        if (account.getIndustry() != null) {
            crmAccountSolrDoc.setIndustryId(account.getIndustry().getId());
            crmAccountSolrDoc.setIndustryName(account.getIndustry().getName());
            crmAccountSolrDoc.setIndustryCode(account.getIndustry().getDescription());
            crmAccountSolrDoc.setIndustryIdCode(SolrUtils.getIdName(account.getIndustry().getId(), account.getIndustry().getDescription()));
            crmAccountSolrDoc.setIndustryIdCodeName(account.getIndustry().getId() + SolrCrmAccountRepresenter.SPLIT + account.getIndustry().getDescription() + SolrCrmAccountRepresenter.SPLIT + account.getIndustry().getName());
        }

        crmAccountSolrDoc.setEmail(account.getEmail());
        crmAccountSolrDoc.setPhone(account.getPhone());
        crmAccountSolrDoc.setFax(account.getFax());
        crmAccountSolrDoc.setWebsite(account.getWebsite());
        if (account.getContact() != null) {
            crmAccountSolrDoc.setContactId(account.getContact().getId());
            crmAccountSolrDoc.setContactName(account.getContact().getName());
        }

        if (account.getBillingAddress() != null) {
            Address billingAddress = account.getBillingAddress();
            crmAccountSolrDoc.setAdress1Id(billingAddress.getObjectID());
            crmAccountSolrDoc.setStreet(billingAddress.getAddress());
            crmAccountSolrDoc.setStreetb(billingAddress.getAddressb());
            crmAccountSolrDoc.setCity(billingAddress.getCity());
            crmAccountSolrDoc.setPostCode(billingAddress.getZipCode());

            if (billingAddress.getCountry() != null) {
                crmAccountSolrDoc.setCountryId(billingAddress.getCountryId());
                crmAccountSolrDoc.setCountryName(billingAddress.getCountry());
                crmAccountSolrDoc.setCountryCode(billingAddress.getCountryCode());
                crmAccountSolrDoc.setCountryIdCode(SolrUtils.getIdName(billingAddress.getCountryId(), billingAddress.getCountryCode()));
                crmAccountSolrDoc.setCountryIdCodeName(billingAddress.getCountryId() + SolrCrmAccountRepresenter.SPLIT +
                        billingAddress.getCountryCode() + SolrCrmAccountRepresenter.SPLIT + billingAddress.getCountry());
            }
            if (billingAddress.getState() != null) {
                crmAccountSolrDoc.setStateId(billingAddress.getStateId());
                crmAccountSolrDoc.setStateName(billingAddress.getState());
                crmAccountSolrDoc.setStateIdName(SolrUtils.getIdName(billingAddress.getStateId(), billingAddress.getState()));
            }
        }
        if (account.getMailingAddress() != null) {
            Address mailingAddress = account.getMailingAddress();
            crmAccountSolrDoc.setAdress2Id(mailingAddress.getObjectID());
            crmAccountSolrDoc.setStreet2(mailingAddress.getAddress());
            crmAccountSolrDoc.setStreet2b(mailingAddress.getAddressb());
            crmAccountSolrDoc.setCity2(mailingAddress.getCity());
            crmAccountSolrDoc.setPostCode2(mailingAddress.getZipCode());
            if (mailingAddress.getCountry() != null) {
                crmAccountSolrDoc.setCountryId2(mailingAddress.getCountryId());
                crmAccountSolrDoc.setCountryName2(mailingAddress.getCountry());
                crmAccountSolrDoc.setCountryCode2(mailingAddress.getCountryCode());
                crmAccountSolrDoc.setCountryIdCode2(SolrUtils.getIdName(mailingAddress.getCountryId(), mailingAddress.getCountryCode()));
            }
            if (mailingAddress.getState() != null) {
                crmAccountSolrDoc.setStateId2(mailingAddress.getStateId());
                crmAccountSolrDoc.setStateName2(mailingAddress.getState());
                crmAccountSolrDoc.setStateIdName2(SolrUtils.getIdName(mailingAddress.getStateId(), mailingAddress.getState()));
            }
        }
        if (account.getCurrency() != null) {
            crmAccountSolrDoc.setCurrencyId(account.getCurrency().getId());
            crmAccountSolrDoc.setCurrencyName(account.getCurrency().getName());
            crmAccountSolrDoc.setCurrencyIdName(SolrUtils.getIdName(account.getCurrency().getId(), account.getCurrency().getName()));
        }
        if (account.getTerm() != null) {
            crmAccountSolrDoc.setTermId(account.getTerm().getId());
            crmAccountSolrDoc.setTermName(account.getTerm().getName());
            crmAccountSolrDoc.setTermIdName(SolrUtils.getIdName(account.getTerm().getId(), account.getTerm().getName()));
        }
        crmAccountSolrDoc.setVatNumber(account.getVatNumber());
        crmAccountSolrDoc.setTrnNumber(account.getTrnNumber());
        crmAccountSolrDoc.setRegistrationNumber(account.getRegistrationNumber());

        if (account.getPaymentMethod() != null) {
            crmAccountSolrDoc.setPaymentMethodId(account.getPaymentMethod().getId());
            crmAccountSolrDoc.setPaymentMethodName(account.getPaymentMethod().getName());
            crmAccountSolrDoc.setPaymentMethodCode(account.getPaymentMethod().getCode());
            crmAccountSolrDoc.setPaymentMethodIdCode(SolrUtils.getIdName(account.getPaymentMethod().getId(), account.getPaymentMethod().getCode()));
        }
        crmAccountSolrDoc.setCreationDate(account.getCreationDate());
        crmAccountSolrDoc.setLastUpdateDate(account.getLastUpdateDate());
        crmAccountSolrDoc.setClientBalance(account.getClientBalance().doubleValue());
        crmAccountSolrDoc.setSupplierBalance(account.getSupplierBalance().doubleValue());
        crmAccountSolrDoc.setCreditLimit(account.getCreditLimit() != null ? account.getCreditLimit().doubleValue() : 0d);

        if (account.getBankName() != null) {
            crmAccountSolrDoc.setBankName(account.getBankName());
        }
        if (account.getTax() != null) {
            crmAccountSolrDoc.setTaxName(account.getTax().getName());
        }
        crmAccountSolrDoc.setInTarget(account.getInTarget());
        if (customFields != null) {
            CustomFieldsUtils.setSolrDocDynamicFields(crmAccountSolrDoc, customFields);
        }
        if (account.getSalesType() != null) {
            crmAccountSolrDoc.setSalesTypeId(account.getSalesType().getId());
            crmAccountSolrDoc.setSalesTypeName(account.getSalesType().getName());
        }

        account.getType().forEach(type -> {
            crmAccountSolrDoc.getTypeId().add(type.getId());
            crmAccountSolrDoc.getTypeName().add(type.getName());
            crmAccountSolrDoc.getTypeCode().add(type.getCode());
            crmAccountSolrDoc.getTypeIdCode().add(SolrUtils.getIdName(type.getId(), type.getCode()));
            crmAccountSolrDoc.getTypeIdCodeName().add(type.getId() + SolrCrmAccountRepresenter.SPLIT + type.getCode() + SolrCrmAccountRepresenter.SPLIT + type.getName());
            crmAccountSolrDoc.getTypeIds().add(type.getId());
        });
        return crmAccountSolrDoc;
    }

    public Page<CrmAccountSolrDoc> getCrmAccountList(ListingFilterParameter fp, ListLoadConfig config) {

        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        FacetFilterRpc accountFacetFilter = fp.getFacetFilter();
        if (accountFacetFilter != null && !accountFacetFilter.isFilterChanges()) {
            accountFacetFilter = commonServiceLocal.getUserFacetFilter(accountFacetFilter);
        }

        EdsUser edsUser = fp.getUserID() != null ? userManager.get(fp.getUserID()) : crmContactManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        String[] customFullAccessRoles = null;
        EdsReference edsAccountType = null;
        if (fp.getAccountType() != null) {
            edsAccountType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, fp.getAccountType());
            if (SUPPLIER.equals(fp.getAccountType())) {
                customFullAccessRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_SUPPLIER_FULL_LIST_ACCESS).toArray(new String[]{});
            }
        }
        fp.setClientIds(clientManager.getEmployeeClients(edsUser.getObjectID()));

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getCrmAccountListSolrQuery(fp, edsCompany, edsAccountType, edsUser, customFullAccessRoles));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(accountFacetFilter, edsCompany, SolrCrmAccountRepresenter.FIELD_CREATED_DATE, null));

        if (fp.isDetectDuplicates()) {
            Set<String> duplicates = getAccountDuplicateNames(solrQuery.toString(), fp.getObjectIDs());
            if (duplicates != null && !duplicates.isEmpty()) {
                StringBuilder duplicateQuery = new StringBuilder();
                boolean isFirst = true;
                boolean found = false;
                for (String duplicate : duplicates) {
                    duplicate = duplicate.trim();
                    if (!duplicate.isEmpty()) {
                        if (isFirst) {
                            found = true;
                            duplicateQuery.append(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME_TEXT_FIELD).append(":(");
                        }
                        duplicateQuery.append(!isFirst ? " OR " : "").append(duplicate).append(" OR ").append(duplicate).append("*");
                        isFirst = false;
                    }
                }
                if (found) {
                    duplicateQuery.append(")");
                }
                if (!duplicateQuery.isEmpty()) {
                    solrQuery.append(" AND (" + duplicateQuery + ")");
                }
            }
        }

        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery.toString()));

        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;

        if (!fp.isSearchButton() && !fp.isLookUp() && !ServerUtils.isNullOrEmpty(config.getSortField())) {
            Sort solrSort = Sort.by(Sort.Direction.DESC, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID);
            boolean desc = false;
            if (Constants.DESC == config.getSortDir()) {
                desc = true;
            }
            Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
            String solrSortField = SolrClientRepresenter.getSortingField(config.getSortField());
            if (solrSortField != null) {
                solrSort = Sort.by(sortDirection, solrSortField);
            } else {
                solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(config.getSortField(), desc, true);
            }
            query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));
        } else {
            query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit));
        }

        return solrTemplate.query(SOLR_CRM_ACCOUNT_CORE, query, CrmAccountSolrDoc.class);
    }

    public Page<CrmAccountSolrDoc> getClientList(ListingFilterParameter fp, ListLoadConfig config) {
        FacetFilterRpc clientFacetFilter = fp.getFacetFilter();
        if (clientFacetFilter != null && !clientFacetFilter.isFilterChanges()) {
            clientFacetFilter = commonServiceLocal.getUserFacetFilter(clientFacetFilter);
        }
        EdsUser edsUser = crmContactManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        EdsReference customer = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);

        List<Integer> customerIDList = null;
        if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_SEE_ALL_CUSTOMERS_LIST) && roleManager.hasRole(edsUser, PM) && !roleManager.hasEitherRoles(edsUser, ACCOUNTANT, DR, ADMIN)) {
            customerIDList = projectManager.getCustomerIDsByProjectManager(edsUser);
        } else if (roleManager.hasOnlyRoles(edsUser, CLIENT) && edsUser instanceof EdsClientContact) {
            if (((EdsClientContact) edsUser).getClientID() != null) {
                customerIDList = new ArrayList<>();
                customerIDList.add(((EdsClientContact) edsUser).getClientID());
                fp.setClientId(((EdsClientContact) edsUser).getClientID());
            }
        }

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getClientListSolrQuery(fp, clientFacetFilter, edsCompany, customer, customerIDList, edsUser, FacetContentType.ClientFacetFilter.getContentCode()[9]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(clientFacetFilter, edsCompany, SolrCrmAccountRepresenter.FIELD_CREATED_DATE, null, FacetContentType.ClientFacetFilter.getContentCode()[9]));

        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery.toString()));
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;

        if (!fp.isSearchButton() && !ServerUtils.isNullOrEmpty(config.getSortField())) {
            boolean desc = false;
            Sort solrSort = Sort.by(Sort.Direction.DESC, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID);
            if (Constants.DESC == config.getSortDir()) {
                desc = true;
            }
            Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
            String solrSortField = SolrClientRepresenter.getSortingField(config.getSortField());
            if (solrSortField != null) {
                solrSort = Sort.by(sortDirection, solrSortField);
            } else {
                solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(config.getSortField(), desc, true);
            }
            query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));
        } else {
            query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit));
        }

        return solrTemplate.query(SOLR_CRM_ACCOUNT_CORE, query, CrmAccountSolrDoc.class);
    }

    public Page<CrmAccountSolrDoc> getSupplierList(ListingFilterParameter fp, ListLoadConfig config) {
        FacetFilterRpc supplierFilter = fp.getFacetFilter();
        if (supplierFilter != null && !supplierFilter.isFilterChanges()) {// first request get User default Filter tasks
            supplierFilter = commonServiceLocal.getUserFacetFilter(supplierFilter);
        }
        EdsUser edsUser = crmContactManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        EdsReference edsSupplier = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER);

        String[] customFullAccessRoles = rolePermissionManager.getRolesByPermissionCode(LayoutRPC.LOGISTICS_SECTION.equals(fp.getModule())
                ? PermissionConstants.LOGISTICS_SUPPLIER_FULL_LIST_ACCESS
                : PermissionConstants.ACCOUNTING_SUPPLIER_FULL_LIST_ACCESS).toArray(new String[]{});

        SimpleQuery solrQuery = new SimpleQuery(new SimpleStringCriteria(QueryBuilderForSolr.getSupplierListSolrQuery(fp, supplierFilter, edsCompany, edsSupplier, edsUser, customFullAccessRoles, FacetContentType.SupplierFacetFilter.getContentCode()[8])));
        String facetFilterQuery = SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(supplierFilter, edsCompany, SolrCrmAccountRepresenter.FIELD_CREATED_DATE, null, FacetContentType.SupplierFacetFilter.getContentCode()[8]);
        if (!StringUtils.isEmpty(facetFilterQuery)) {
            solrQuery.addCriteria(new SimpleStringCriteria(facetFilterQuery));
        }

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrSupplierRepresenter.FIELD_LAST_UPDATED_DATE);
        if (!fp.isSearchButton()) {
            if (config.getSortField() != null && !"".equals(config.getSortField())) {
                boolean desc = Constants.DESC == config.getSortDir();
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                String solrSortField = SolrClientRepresenter.getSortingField(config.getSortField());
                if (solrSortField != null) {
                    solrSort = Sort.by(sortDirection, solrSortField);
                } else {
                    solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(config.getSortField(), desc, true);
                }
            }
        }
        int limit = config.getLimit() > 0 ? config.getLimit() : SOLR_LIMIT;
        solrQuery.setPageRequest(PageRequest.of(config.getStart(), limit, solrSort));

        return solrTemplate.query(SOLR_CRM_ACCOUNT_CORE, solrQuery, CrmAccountSolrDoc.class);
    }

    private Set<String> getAccountDuplicateNames(String solrQuery, List<Integer> inIDs) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(0);
        query.setRows(10000);
        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crmAccountManager.getDuplicateNamesSet(SolrUtils.getIdsFromSolrDocument(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID, resp.getResults()), inIDs);
    }

    public List<CrmAccountSolrDoc> getDocumentsExistingInBase(List<CrmAccountSolrDoc> crmAccountSolrDocs) {
        List<CrmAccountSolrDoc> crmAccountSolrDocsNew = new ArrayList<>();
        Map<Integer, CrmAccountSolrDoc> mapDocuments = new HashMap<>();
        if (!crmAccountSolrDocs.isEmpty()) {
            for (CrmAccountSolrDoc doc : crmAccountSolrDocs) {
                crmAccountSolrDocsNew.add(doc);
                mapDocuments.put(doc.getCrmAccountId(), doc);
            }
        }
        List<Integer> objectIDsFromDatabase = crmAccountManager.getCrmAccountIDsByIDs(new ArrayList(mapDocuments.keySet()));
        if (objectIDsFromDatabase != null && objectIDsFromDatabase.size() > 0) {
            for (Integer objectID : objectIDsFromDatabase) {
                mapDocuments.remove(objectID);
            }
            if (mapDocuments.size() > 0) {
                crmAccountSolrDocsNew.removeAll(mapDocuments.values());
            }
        }
        return crmAccountSolrDocsNew;
    }

    public FacetFilterRpc getCrmAccountFacetFilterData(FacetFilterRpc crmAccountFacet) {
        if (!crmAccountFacet.isFilterChanges()) {
            crmAccountFacet = commonServiceLocal.getUserFacetFilter(crmAccountFacet);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(crmAccountFacet.getSearchKey());

        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        String[] customFullAccessRoles = null;
        EdsReference edsAccountType = null;
        if (fp.getAccountType() != null) {
            edsAccountType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, fp.getAccountType());
            if (SUPPLIER.equals(fp.getAccountType())) {
                customFullAccessRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ACCOUNTING_SUPPLIER_FULL_LIST_ACCESS).toArray(new String[]{});
            }
        }
        StringBuilder solrQuery = new StringBuilder();
        ListingFilterParameter lfp = new ListingFilterParameter();
        if (fp != null) {
            lfp.setSearchKey(fp.getSearchKey());
            lfp.setLookUp(fp.isLookUp());
            lfp.setAccountType(fp.getAccountType());
        }
        //We must replace filter option values since values has additional "_" (see: getCrmAccountFacetResultFromSolr())
        if (crmAccountFacet.getFacetContentMap().containsKey(FacetContentType.CrmAccountFacetFilter.getContentCode()[8])) {

            String dotKey = FacetContentType.CrmAccountFacetFilter.getContentCode()[8];
            if (crmAccountFacet.getFacetContentMap().get(dotKey) != null) {
                Map<Integer, String> additionalInformation = new HashMap<>();
                additionalInformation.put("_false".hashCode(), "false");//false
                additionalInformation.put("false".hashCode(), "false");//false
                additionalInformation.put("_Qualified".hashCode(), "Qualified");//Qualified
                additionalInformation.put("Qualified".hashCode(), "Qualified");//Qualified
                additionalInformation.put("_true".hashCode(), "true");//true
                additionalInformation.put("true".hashCode(), "true");//true
                additionalInformation.put("_Unqualified".hashCode(), "Unqualified");//Unqualified
                additionalInformation.put("Unqualified".hashCode(), "Unqualified");//Unqualified

                //Facet Items
                if (crmAccountFacet.getFacetContentMap().get(dotKey).getFacetItems() != null) {
                    for (int i = 0; i < crmAccountFacet.getFacetContentMap().get(dotKey).getFacetItems().length; i++) {
                        String name = additionalInformation.get(crmAccountFacet.getFacetContentMap().get(dotKey).getFacetItems()[i].getId());
                        if (StringUtils.isNotBlank(name)) {
                            try {
                                crmAccountFacet.getFacetContentMap().get(dotKey).getFacetItems()[i].setName(name);
                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
                            }
                        }
                    }
                }
                //Saved Items
                HashMap<Integer, Integer> replacedSavedItems = new HashMap<>();

                for (Map.Entry<Integer, Integer> savedItem : crmAccountFacet.getFacetContentMap().get(dotKey).getSavedItems().entrySet()) {
                    String replaceId = additionalInformation.get(savedItem.getKey().toString());
                    if (StringUtils.isNotBlank(replaceId)) {
                        try {
                            replacedSavedItems.put(savedItem.getKey(), Integer.valueOf(replaceId));
                        } catch (NumberFormatException e) {
//                                e.printStackTrace();
                        }
                    } else {
                        replacedSavedItems.put(savedItem.getKey(), savedItem.getValue());
                    }
                }
                crmAccountFacet.getFacetContentMap().get(dotKey).setSavedItems(replacedSavedItems);
            }
        }
        solrQuery.append(QueryBuilderForSolr.getCrmAccountListSolrQuery(lfp, edsCompany, edsAccountType, edsUser, customFullAccessRoles));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(crmAccountFacet, edsCompany, SolrCrmAccountRepresenter.FIELD_CREATED_DATE, null));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_CRM_ACCOUNT_CORE, solrQuery.toString(), crmAccountFacet, CrmAccountSolrDoc.class);

        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, crmAccountFacet, crmAccountFacet.getSolrFieldMapCodeList());
        if (crmAccountFacet.getFacetContentMap().containsKey(FacetContentType.CrmAccountFacetFilter.getContentCode()[8])) {
            getCrmAccountFacetResultFromSolr(facetPage, crmAccountFacet);
        }
        return crmAccountFacet;
    }

    /**
     * @param facetPage             FacetPage
     * @param crmAccountFacetFilter FacetFilterRpc
     * @return
     */
    private FacetFilterRpc getCrmAccountFacetResultFromSolr(QueryResponse resp, FacetFilterRpc crmAccountFacetFilter) {
        String dotKey = FacetContentType.CrmAccountFacetFilter.getContentCode()[8];

        Map<String, String> additionalInformation = getCompanyStatusMap();

        crmAccountFacetFilter.getFacetContentMap().get(dotKey).setFacetItems(
                getFacetItemsByIdOnly(resp.getFacetField(SolrCrmAccountRepresenter.FIELD_BLOCKED),
                        additionalInformation, true)
        );
        return crmAccountFacetFilter;
    }

    private Map<String, String> getCompanyStatusMap() {
        Map<String, String> additionalInformation = new HashMap<>();
        additionalInformation.put("false", "Qualified");
        additionalInformation.put("" + "_false".hashCode(), "Qualified");
        additionalInformation.put("" + "_Qualified".hashCode(), "Qualified");
        additionalInformation.put("true", "Unqualified");
        additionalInformation.put("" + "_true".hashCode(), "Unqualified");
        additionalInformation.put("" + "_Unqualified".hashCode(), "Unqualified");
        return additionalInformation;
    }

    private SelectItem[] getFacetItemsByIdOnly(FacetField facetField, Map<String, String> additionalInformation, boolean forceToChangeUiName) {
        if (facetField != null && facetField.getValues() != null) {
            ArrayList<SelectItem> parents = new ArrayList<>();
            SelectItem nAItem = null;
            for (FacetField.Count count : facetField.getValues()) {
                if (count.getName() == null) {
                    if (count.getCount() > 0) {
                        nAItem = new SelectItem();
                        nAItem.setName("N/A");
                        nAItem.setDescription("N/A ( <b>" + count.getCount() + "</b> )");
                        nAItem.setId(-1);
                        nAItem.setTotalCount(count.getCount());
                    }
                } else {
                    SelectItem sItem = null;
                    Integer id = null;
                    if (count.getName().matches(Constants.REGEX_INTEGER)) {
                        try {
                            id = Integer.parseInt(count.getName());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    String name = null;
                    if (id != null) {
                        if (additionalInformation.containsKey(id.toString())) {
                            sItem = new SelectItem(id, additionalInformation.get(id.toString()));
                        }
                    } else {
                        sItem = new SelectItem(("_" + count.getName()).hashCode(), count.getName());
                        if (additionalInformation.containsKey(count.getName().toLowerCase())) {
                            name = additionalInformation.get(count.getName().toLowerCase());
                            sItem.setName(name);
                        }
                    }
                    if (sItem != null) {
                        sItem.setTotalCount(count.getCount());
                        sItem.setDescription((forceToChangeUiName && name != null ? name : sItem.getName()) + " ( <b>" + count.getCount() + "</b> )");
                        parents.add(sItem);
                    }
                }
            }
            SelectItem[] items = parents.toArray(new SelectItem[]{});
            items = ServerUtils.sortSelectItem(items);
            if (nAItem != null) {
                SelectItem[] nItems = items;
                items = new SelectItem[nItems.length + 1];
                int n = 0;
                for (SelectItem item : nItems) {
                    items[n] = item;
                    n++;
                }
                items[n] = nAItem;
            }

            return items;
        } else {
            return new SelectItem[0];
        }
    }

    public ArrayList<SelectItem> getCrmLookNamesForAccount(Page<CrmAccountSolrDoc> crmAccountSolrDocPage, ListingFilterParameter filterParametrs) throws SolrServerException, IOException {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        if (crmAccountSolrDocPage != null && crmAccountSolrDocPage.getContent() != null && crmAccountSolrDocPage.getContent().size() > 0) {
            for (int i = 0, resultsSize = crmAccountSolrDocPage.getContent().size(); i < resultsSize; i++) {
                CrmAccountSolrDoc doc = crmAccountSolrDocPage.getContent().get(i);
                String name = doc.getCrmAccountName();
                if (doc.getBlocked()) {
                    name = name + " <html><font color=\"red\">(" + wfmLocalizer.localize("blocked") + ") </font></html>";
                }
                if (filterParametrs.isWithCode()) {
                    String number = doc.getCrmAccountNumber();
                    name = (!"".equals(number) ? number + " -> " + name : "");
                    SelectItem item = new SelectItem(doc.getCrmAccountId(), name, number);
                    item.setReferenceCode(number);
                    item.setCode(number);
                    selectItems.add(item);
                } else {
                    selectItems.add(new SelectItem(doc.getCrmAccountId(), name));
                }
            }
        }
        return selectItems;
    }
}
