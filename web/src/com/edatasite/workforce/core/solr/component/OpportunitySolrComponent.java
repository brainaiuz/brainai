package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.solr.document.OpportunitySolrDoc;
import com.edatasite.workforce.core.solr.repository.OpportunitySolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.EventSolrItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunitySolrItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.*;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:29.
 */
@Component
public class  OpportunitySolrComponent {

    private static final Logger log = LoggerFactory.getLogger(OpportunitySolrComponent.class);

    @Autowired
    private OpportunitySolrDocRepository opportunitySolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private CommonService commonService;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmServiceLocal crmService;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsOpportunity edsOpportunity) throws InterruptedException {
        this.indexes(Arrays.asList(edsOpportunity));
    }

    @Transactional
    public void indexes(List<EdsOpportunity> edsOpportunityList) throws InterruptedException {
        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsOpportunityList)) {
            List<OpportunitySolrDoc> opportunitySolrDocs = new ArrayList<>();

            for (EdsOpportunity edsOpportunity : edsOpportunityList) {
                if (edsOpportunity != null && !edsOpportunity.isDeleted()) {
                    try {
                        opportunitySolrDocs.add(createOpportunityDocument(edsOpportunity.getOpportunitySolrRPC(), companyID, edsOpportunity.getCustomFields()));
                        log.info("Indexed Opportunity Core CID - {}, objId - {}", companyID, edsOpportunity.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on EdsCrmAccount with id {}, and error message {} **********************", edsOpportunity.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!opportunitySolrDocs.isEmpty()) {
                log.info("========= Create Opportunity solr docs for company {} with size {} =========", companyID, opportunitySolrDocs.size());
                opportunitySolrDocRepository.saveAll(opportunitySolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsOpportunity> edsOpportunityList) throws InterruptedException {
        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsOpportunityList)) {
            ConcurrentLinkedQueue<OpportunitySolrDoc> opportunitySolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsOpportunity edsOpportunity : edsOpportunityList) {
                if (edsOpportunity != null && !edsOpportunity.isDeleted()) {
                    Hibernate.initialize(edsOpportunity.getCustomFields());
                    OpportunitySolrItem opportunitySolrRPC = edsOpportunity.getOpportunitySolrRPC();
                    EdsCrmCustomFields customFields = edsOpportunity.getCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);
                            sync.execute(getSynchronizedKey(opportunitySolrRPC), () -> {
                                        opportunitySolrDocs.add(createOpportunityDocument(opportunitySolrRPC, companyID, customFields));
                                        log.info("Indexed Opportunity Core CID - {}, objId - {}", companId, edsOpportunity.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("********************* Error on EdsOpportunity with id {}, and error message {} **********************", edsOpportunity.getObjectID(), e.getMessage());
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
                log.error("Error on loading Opprotunity list", e);
            }

            if (!opportunitySolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Opportunity solr docs for company {} with size {} =========", companyID, opportunitySolrDocs.size());
                    opportunitySolrDocRepository.saveAll(opportunitySolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Opportunity list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(OpportunitySolrItem opportunity) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + opportunity.getOpportunity().getId();
    }

    private OpportunitySolrDoc createOpportunityDocument(OpportunitySolrItem opportunity, Integer companyID, EdsCustomFields customFields) {
        OpportunitySolrDoc opportunitySolrDoc = new OpportunitySolrDoc();

        opportunitySolrDoc.setOid(SolrUtils.generatedOId(companyID, opportunity.getOpportunity().getId()));
        opportunitySolrDoc.setCompanyId(companyID);
        opportunitySolrDoc.setOpportunityId(opportunity.getOpportunity().getId());
        opportunitySolrDoc.setOpportunityName(opportunity.getOpportunity().getName());
        opportunitySolrDoc.setOpportunityIdName(SolrUtils.getIdName(opportunity.getOpportunity().getId(), opportunity.getOpportunity().getName()));
        opportunitySolrDoc.setOpportunityNumber(opportunity.getOpportunity().getNumber());
        opportunitySolrDoc.setOpportunityStringNumber(opportunity.getOpportunity().getNumber());
        opportunitySolrDoc.setOpportunityIntNumber(opportunity.getOpportunityIntNumber());
        if (opportunity.getOwner() != null) {
            opportunitySolrDoc.setOwnerId(opportunity.getOwner().getId());
            opportunitySolrDoc.setOwnerName(opportunity.getOwner().getName());
            opportunitySolrDoc.setOwnerIdName(SolrUtils.getIdName(opportunity.getOwner().getId(), opportunity.getOwner().getName()));
        }
        if (opportunity.getAssignee() != null) {
            opportunitySolrDoc.setAssigneeId(opportunity.getAssignee().getId());
            opportunitySolrDoc.setAssigneeName(opportunity.getAssignee().getName());
            opportunitySolrDoc.setAssigneeIdName(SolrUtils.getIdName(opportunity.getAssignee().getId(), opportunity.getAssignee().getName()));
        }
        if (opportunity.getBackupAssignee() != null) {
            opportunitySolrDoc.setBackupAssigneeId(opportunity.getBackupAssignee().getId());
            opportunitySolrDoc.setBackupAssigneeName(opportunity.getBackupAssignee().getName());
            opportunitySolrDoc.setBackupAssigneeIdName(SolrUtils.getIdName(opportunity.getBackupAssignee().getId(), opportunity.getBackupAssignee().getName()));
        }
        opportunitySolrDoc.setClosingDate(opportunity.getClosingDate());
        if (opportunity.getCreationDate() != null) {
            opportunitySolrDoc.setCreationDate(opportunity.getCreationDate());
            opportunitySolrDoc.setModificationDate(opportunity.getModificationDate());
        }
        if (opportunity.getCreator() != null) {
            opportunitySolrDoc.setCreatorId(opportunity.getCreator().getId());
            opportunitySolrDoc.setCreatorName(opportunity.getCreator().getName());
            opportunitySolrDoc.setCreatorIdName(SolrUtils.getIdName(opportunity.getCreator().getId(), opportunity.getCreator().getName()));
        }
        if (opportunity.getCrmAccount() != null) {
            opportunitySolrDoc.setCrmAccountId(opportunity.getCrmAccount().getId());
            opportunitySolrDoc.setCrmAccountName(opportunity.getCrmAccount().getName());
            opportunitySolrDoc.setCrmAccountIdName(SolrUtils.getIdName(opportunity.getCrmAccount().getId(), opportunity.getCrmAccount().getName()));
            opportunitySolrDoc.setCrmAccountNumber(opportunity.getCrmAccount().getNumber());

            if (opportunity.getCrmAccountCountry() != null) {
                opportunitySolrDoc.setCrmAccountCountryId(opportunity.getCrmAccountCountry().getId());
                opportunitySolrDoc.setCrmAccountCountryName(opportunity.getCrmAccountCountry().getName());
                opportunitySolrDoc.setCrmAccountCountryIdName(SolrUtils.getIdName(opportunity.getCrmAccountCountry().getId(), opportunity.getCrmAccountCountry().getName()));
            }
        }
        if (opportunity.getCrmContact() != null) {
            opportunitySolrDoc.setCrmContactId(opportunity.getCrmContact().getId());
            opportunitySolrDoc.setCrmContactName(opportunity.getCrmContact().getName());
            opportunitySolrDoc.setCrmContactIdName(SolrUtils.getIdName(opportunity.getCrmContact().getId(), opportunity.getCrmContact().getName()));
            opportunitySolrDoc.setCrmContactPrimaryEmail(opportunity.getCrmContactPrimaryEmail());
            opportunitySolrDoc.setCrmContactEmailAllowed(opportunity.getCrmContactEmailAllowed());
            opportunitySolrDoc.setCrmContactPrimaryPhone(opportunity.getCrmContactPrimaryPhone());
        }
        if (opportunity.getOpportunityStage() != null) {
            opportunitySolrDoc.setOpportunityStageId(opportunity.getOpportunityStage().getId());
            opportunitySolrDoc.setOpportunityStageName(opportunity.getOpportunityStage().getName());
            opportunitySolrDoc.setOpportunityStageCode(opportunity.getOpportunityStage().getDescription());
            opportunitySolrDoc.setOpportunityStageIdCode(SolrUtils.getIdName(opportunity.getOpportunityStage().getId(), opportunity.getOpportunityStage().getDescription()));
            if (opportunity.getStageLocale() != null) {
                ReferenceLocale locale = opportunity.getStageLocale();
                opportunitySolrDoc.setStageUzName(locale.getUzbek());
                opportunitySolrDoc.setStageEnName(locale.getEnglish());
                opportunitySolrDoc.setStageArName(locale.getArabic());
                opportunitySolrDoc.setStageRuName(locale.getRussian());
            }
        }
        opportunitySolrDoc.setOpportunityConvertProject(opportunity.getOpportunityConvertProject());
        opportunitySolrDoc.setConvertedFromLead(opportunity.getConvertedFromLead());
        opportunitySolrDoc.setAmount(opportunity.getAmount());
        opportunitySolrDoc.setAmountBaseCurrency(opportunity.getAmountBaseCurrency());
        opportunitySolrDoc.setExpectedRevenue(opportunity.getExpectedRevenue());
        if (opportunity.getCampaign() != null) {
            opportunitySolrDoc.setCampaignId(opportunity.getCampaign().getId());
            opportunitySolrDoc.setCampaignName(opportunity.getCampaign().getName());
            opportunitySolrDoc.setCampaignIdName(SolrUtils.getIdName(opportunity.getCampaign().getId(), opportunity.getCampaign().getName()));
        }
        if (opportunity.getCurrency() != null) {
            opportunitySolrDoc.setCurrencyId(opportunity.getCurrency().getId());
            opportunitySolrDoc.setCurrencyName(opportunity.getCurrency().getName());
            opportunitySolrDoc.setCurrencyIdName(SolrUtils.getIdName(opportunity.getCurrency().getId(), opportunity.getCurrency().getName()));
        }
        if (opportunity.getType() != null) {
            opportunitySolrDoc.setTypeId(opportunity.getType().getId());
            opportunitySolrDoc.setTypeName(opportunity.getType().getName());
            opportunitySolrDoc.setTypeIdName(SolrUtils.getIdName(opportunity.getType().getId(), opportunity.getType().getName()));
        }
        if (opportunity.getLeadSource() != null) {
            opportunitySolrDoc.setLeadSourceId(opportunity.getLeadSource().getId());
            opportunitySolrDoc.setLeadSourceName(opportunity.getLeadSource().getName());
            opportunitySolrDoc.setLeadSourceIdName(SolrUtils.getIdName(opportunity.getLeadSource().getId(), opportunity.getLeadSource().getName()));
        }
        opportunitySolrDoc.setNextStep(opportunity.getNextStep());
        opportunitySolrDoc.setProbability(opportunity.getProbability());
        opportunitySolrDoc.setOpportunityKanbanOrder(opportunity.getOpportunityKanbanOrder());

        if (opportunity.getRelatedProject() != null) {
            opportunitySolrDoc.setRelatedProjectId(opportunity.getRelatedProject().getId());
            opportunitySolrDoc.setRelatedProjectName(opportunity.getRelatedProject().getName());
            opportunitySolrDoc.setRelatedProjectNumber(opportunity.getRelatedProject().getNumber());
            if (opportunity.getRelatedProject().getCode() != null) {
                opportunitySolrDoc.setRelatedProjectCode(opportunity.getRelatedProject().getCode());
            }
            opportunitySolrDoc.setRelatedProjectIdName(opportunity.getRelatedProject().getId() + SolrOpportunityRepresenter.SPLIT + opportunity.getRelatedProject().getName());
        }

        if (opportunity.getMultiProject() != null) {
            for (SelectItem project : opportunity.getMultiProject()) {
                opportunitySolrDoc.getMultiProjectId().add(project.getId());
                opportunitySolrDoc.getMultiProjectName().add(project.getName());
                opportunitySolrDoc.getMultiProjectNumber().add(project.getNumber());
                opportunitySolrDoc.getMultiProjectIdName().add(project.getId() + SolrOpportunityRepresenter.SPLIT + project.getName());
                opportunitySolrDoc.getMultiProjectNumberName().add(project.getNumber() + SolrOpportunityRepresenter.ARROW + project.getName());
            }
        }

        List<EdsRelation> relationList = relationManager.getAllRelations(EdsRelation.TYPE_OPPORTUNITY, opportunity.getOpportunity().getId());

        SolrRelationUtils.addToRelationBaseSolrDoc(opportunitySolrDoc, relationList, EdsRelation.TYPE_OPPORTUNITY);
        CustomFieldsUtils.setSolrDocDynamicFields(opportunitySolrDoc, customFields);

        return opportunitySolrDoc;
    }

    @Transactional
    public Page<OpportunitySolrDoc> getList(ListingFilterParameter filterParameter) {
        FacetFilterRpc opportunityFacetFilter = filterParameter.getFacetFilter();
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        if (panelTools == null) {
            ArrayList<String> columnCodeName = OpportunityListItem.defaultColumnNames;
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(columnCodeName);
            filterParameter.setColumnsOfListing(columnCodeName);
        }
        if (panelTools.isCustomFieldsShown()) {
            filterParameter.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.Opportunity));
        }
        if (opportunityFacetFilter != null && !opportunityFacetFilter.isFilterChanges()) {
            opportunityFacetFilter = commonServiceLocal.getUserFacetFilter(opportunityFacetFilter);
        }

        String mainSolrQuery = getOpportunityFacetQuery(filterParameter, opportunityFacetFilter);

        SelectItem[] stages = crmService.getOpportunityStages(false);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" AND ( (-").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append("[* TO *] AND *:*)");
        if (stages != null && stages.length > 0) {
            for (SelectItem stage : stages) {
                queryBuilder.append(" OR ").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append(stage.getId());
            }
        }
        queryBuilder.append(" ) ");
        mainSolrQuery += queryBuilder.toString();

        SimpleQuery solrQuery = new SimpleQuery(new SimpleStringCriteria(mainSolrQuery));

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrOpportunityRepresenter.FIELD_MODIFICATION_DATE);
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                boolean desc = true;
                if (filterParameter.isAscending()) {
                    desc = false;
                }
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                String sortField = SolrOpportunityRepresenter.getSortField(filterParameter.getSortField());
                if (sortField != null) {
                    solrSort = Sort.by(sortDirection, sortField);
                } else {
                    solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), desc, true);
                }
            }
        }
        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        solrQuery.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_OPPORTUNITY_CORE, solrQuery, OpportunitySolrDoc.class);
    }

    public String getOpportunityFacetQuery(ListingFilterParameter filterParameter, FacetFilterRpc opportunityFacetFilter) {

        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(getOpportunityCoreSolrQuery(edsUser, filterParameter));
        solrQuery.append(SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(
                opportunityFacetFilter,
                FacetContentType.OpportunityFacetFilter.getContentCode()[4]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(opportunityFacetFilter, edsCompany,
                SolrOpportunityRepresenter.FIELD_CLOSING_DATE,
                SolrOpportunityRepresenter.FIELD_CLOSING_DATE,
                FacetContentType.OpportunityFacetFilter.getContentCode()[4]));
        if (filterParameter.getRelationID() != null && filterParameter.getRelationType() != null) {
            List<Integer> opportunityIDs = relationManager.getRelationIDsByType(filterParameter.getRelationID(), null, filterParameter.getRelationType(), RelationItem.TYPE_OPPORTUNITY);
            if (opportunityIDs != null && !opportunityIDs.isEmpty()) {
                solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID).append(":(").append(ServerUtils.getAsCommoDelimited(opportunityIDs, "0", " ")).append(")");
            }
        }
        // ---- from kanban board ----
        if (filterParameter.getColumnMetadataId() != null) {
            if (Integer.valueOf(-1).equals(filterParameter.getColumnMetadataId())) {
                solrQuery.append(" AND -(").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append("[* TO *]").append(")");
            } else {
                solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append(filterParameter.getColumnMetadataId()).append(")");
            }
        }
        return solrQuery.toString();
    }

    public String getOpportunityCoreSolrQuery(EdsUser edsUser, ListingFilterParameter filterParameter) {
        StringBuffer solrQuery = new StringBuffer();
        solrQuery.append(SolrOpportunityRepresenter.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            if (filterParameter.isFromMobile()) {
                solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME_COMPOSITE)
                        .append(":(");
                solrQuery.append(QueryBuilderForSolr.normalaizeKeywordByCriteria(filterParameter.getSearchKey(), true, true));
                solrQuery.append(") ");

            } else {
                solrQuery.append(" AND (")
                        .append(filterParameter.isLookUp() ? SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME_COMPOSITE : SolrOpportunityRepresenter.FIELD_COMPOSITE)
                        .append(":( ")
                        .append(QueryBuilderForSolr.normalaizeKeyword(filterParameter.getSearchKey(), filterParameter.isLookUp()))
                        .append(" )");
            }

            SolrSearchUtils searchUtils = new SolrSearchUtils();
            if (filterParameter.isFromMobile()) {
                Map<String, Double> fields = new HashMap<>();
                fields.put(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME_COMPOSITE, SolrSearchUtils.HIGH_PRIORITY);
                searchUtils.generateSearchQuery(solrQuery, fields, filterParameter.getSearchKey());
            } else if (!filterParameter.isLookUp()) {
                searchUtils.generateSearchQuery(solrQuery, QueryBuilderForSolr.getOpportunitySearchFields(), filterParameter.getSearchKey());
            }
            solrQuery.append(")");
        }

        if (filterParameter.getAccountID() != null) {
            solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID).append(":(").append(filterParameter.getAccountID()).append(")").append(")");
        }
        if (filterParameter.getContactID() != null && filterParameter.getContactID() > 0) {
            solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_ID).append(":").append(filterParameter.getContactID());
        }
        if (filterParameter.getCampaignID() != null) {
            solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_CAMPAIGN_ID).append(":").append(filterParameter.getCampaignID());
        }
        if (filterParameter.getStatusID() != null) {
            solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append(filterParameter.getStatusID());
        }

        if (!ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_OPPORTUNITIES_LIST)) {
            boolean supervisorAccess = ServerUtils.hasPermission(PermissionConstants.CRM_SHOW_SUPERVISED_OPPORTUNITIES);
            boolean ownerAccess = ServerUtils.hasPermission(PermissionConstants.OPPORTUNITY_SEE_OWN);
            if (supervisorAccess) {
                List<Integer> childEmployeeIds = new ArrayList<>();
                getChildEmployeeIds(edsUser.getObjectID(), childEmployeeIds);
                childEmployeeIds.add(edsUser.getObjectID());
                if (childEmployeeIds.size() > 0) {
                    solrQuery.append(" AND ").append(SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(childEmployeeIds, "0", " ")).append(")");
                }
            } else {
                StringBuilder clientIDsStr = new StringBuilder();
                if (filterParameter.getAccountID() != null) {
                    EdsCrmAccount crmAccount = crmAccountManager.get(filterParameter.getAccountID());
                    ownerAccess = ownerAccess && crmAccount.getOwners().contains(edsUser);
                }
                if (ownerAccess && !edsUser.hasRole(EdsRole.ADMIN_CODE)) {
                    List<Integer> clientIDs = crmAccountManager.getAccountIDsByOwner(edsUser.getObjectID());
                    if (clientIDs != null && clientIDs.size() > 0) {
                        for (Integer clientID : clientIDs) {
                            clientIDsStr.append(" ").append(clientID);
                        }
                    }
                }

                if (filterParameter.hasOnlyClientAccess() && edsUser.isClientContact() && edsUser.getClientContact().getCrmContact() != null) {
                    EdsCrmContact contact = edsUser.getClientContact().getCrmContact();
                    solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_ID).append(":").append(contact.getObjectID());
                    if (contact.getCrmAccount() != null) {
                        solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID).append(":").append(contact.getCrmAccount().getObjectID());
                    }
                    solrQuery.append(" ) ");
                } else {
                    solrQuery.append(" AND (").append(SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID).append(":").append(edsUser.getObjectID());
                    solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_ESTIMATOR_ID).append(":").append(edsUser.getObjectID());
                    solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_OWNER_ID).append(":").append(edsUser.getObjectID());
                    if (rolePermissionManager.hasPermissionCheckedForCreator(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
                        solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_CREATOR_ID).append(":").append(edsUser.getObjectID());
                    }
                    solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_ID).append(":").append(edsUser.getObjectID());
                }

                if (!clientIDsStr.toString().trim().isEmpty()) {
                    solrQuery.append(" OR (");
                    solrQuery.append(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID).append(":(").append(clientIDsStr.toString().trim()).append(") ");
                    solrQuery.append(")");
                }
                solrQuery.append(" ) ");
            }
        }
        return solrQuery.toString();
    }

    private void getChildEmployeeIds(Integer supervisorId, List<Integer> allEmployeeIds) {
        List<Integer> childEmployeeIds = employeeManager.getChildEmployees(supervisorId);
        if (childEmployeeIds != null && !childEmployeeIds.isEmpty()) {
            for (Integer childId : childEmployeeIds) {
                allEmployeeIds.add(childId);
                getChildEmployeeIds(childId, allEmployeeIds);
            }
        }
    }

    public ArrayList<SelectItem> getCrmLookNamesForOpportunity(Page<OpportunitySolrDoc> opportunitySolrDocPage) throws SolrServerException, IOException {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        if (opportunitySolrDocPage != null && opportunitySolrDocPage.getContent() != null && opportunitySolrDocPage.getContent().size() > 0) {
            for (int i = 0, resultsSize = opportunitySolrDocPage.getContent().size(); i < resultsSize; i++) {
                OpportunitySolrDoc doc = opportunitySolrDocPage.getContent().get(i);
                String number = doc.getOpportunityNumber();
                String name = (!"".equals(number) ? number + " -> " + doc.getOpportunityName() : "");
                SelectItem item = new SelectItem(doc.getOpportunityId(), name, number);
                item.setReferenceCode(number);
                item.setCode(number);
                selectItems.add(item);
            }
        }
        return selectItems;
    }
}
