package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsProjectCustomFields;
import com.edatasite.workforce.core.solr.document.ProjectSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.ProjectSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunitySolrItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectSolrItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
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
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:30.
 */
@Component
public class ProjectSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(ProjectSolrComponent.class);

    @Autowired
    private ProjectSolrDocRepository projectSolrDocRepository;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsProject edsProject) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsProject));
    }

    @Transactional
    public void indexes(List<EdsProject> edsProjects) throws IOException, SolrServerException, InterruptedException {

        String companyId = ServerSecurityContext.getInstance().getCompanyId();

        List<ProjectSolrDoc> projectSolrDocs = new ArrayList<>();
        for (EdsProject edsProject : edsProjects) {
            if (edsProject != null) {
                try {
                    projectSolrDocs.add(createProjectDocument(edsProject.getSolrRPC(), Integer.valueOf(companyId), edsProject.getProjectCustomFields()));
                    log.info("Indexed Project Core CID - {}, objId - {}", companyId, edsProject.getObjectID());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("********************* Error on Project with id {}, and error message {} **********************", edsProject.getObjectID(), e.getMessage());
                    throw e;
                }
            }
            if (!projectSolrDocs.isEmpty()) {
                log.info("========= Create Project solr docs for company {} with size {} =========", companyId, projectSolrDocs.size());
                projectSolrDocRepository.saveAll(projectSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsProject> edsProjects) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(edsProjects)) {
            ConcurrentLinkedQueue<ProjectSolrDoc> projectSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsProject edsProject : edsProjects) {
                if (edsProject != null) {
                    ProjectSolrItem solrRPC = edsProject.getSolrRPC();
                    EdsProjectCustomFields projectCustomFields = edsProject.getProjectCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        projectSolrDocs.add(createProjectDocument(solrRPC, Integer.valueOf(companyId), projectCustomFields));
                                        log.info("Indexed Project Core CID - {}, objId - {}", companyId, edsProject.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on Project with id {}, and error message {} **********************", edsProject.getObjectID(), e.getMessage());
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
                log.error("Error on loading Project list", e);
            }

            if (!projectSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Project solr docs for company {} with size {} =========", companyId, projectSolrDocs.size());
                    projectSolrDocRepository.saveAll(projectSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Opportunity list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(ProjectSolrItem project) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + project.getProject().getId();
    }

    private ProjectSolrDoc createProjectDocument(ProjectSolrItem project, Integer companyId, EdsCustomFields customFields) {
        ProjectSolrDoc projectSolrDoc = new ProjectSolrDoc();
        boolean isAutomatic = numberingSettingsManager.getNumberingSetting() != null && numberingSettingsManager.getNumberingSetting().isAutomatic();
        boolean newProjectPercon = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT);
        List<EdsRelation> edsRelationList = relationManager.getAllRelations(EdsRelation.TYPE_PROJECT, project.getProject().getId());
        String invoiceNumber = projectManager.getProjectLastInvoiceNumber(project.getProject().getId());
        List<EdsEmployee> edsAssigneesList = projectEmployeeManager.getEmployeesByProject(project.getProject().getId());
        Float estimatedtime = 0f;
        Float timespent = 0f;
        Double[] projectCostAndTimeSpent = timeSheetManager.getProjectCostAndTimeSpent(project.getProject().getId(), null);
        timespent = projectCostAndTimeSpent != null && projectCostAndTimeSpent[2] != null && projectCostAndTimeSpent[2].toString() != "0.0" ? projectCostAndTimeSpent[2].floatValue() : 0;//PROJECT_ACTUAL_TIME_SPENT
        estimatedtime = projectCostAndTimeSpent != null && projectCostAndTimeSpent[5] != null && projectCostAndTimeSpent[5].toString() != "0.0" ? projectCostAndTimeSpent[5].floatValue() : 0;//PROJECT_HOURS_SPENT

        projectSolrDoc.setOid(SolrUtils.generatedOId(companyId, project.getProject().getId()));
        projectSolrDoc.setCompanyId(companyId);
        projectSolrDoc.setProjectId(project.getProject().getId());
        projectSolrDoc.setProjectNumber(project.getProject().getNumber());
        projectSolrDoc.setProjectName(project.getProject().getName());
        projectSolrDoc.setDescription(project.getDescription());
        if (newProjectPercon) {
            projectSolrDoc.setCompleted(project.getProjectTasksAveragePercentCompletedNewLogic1(timespent, estimatedtime));
        } else {
            if (isAutomatic) {
                projectSolrDoc.setCompleted(project.getProjectTasksAveragePercentCompleted());
            }
        }
        projectSolrDoc.setHourSpent(project.getHourSpent());
        projectSolrDoc.setStartDate(project.getStartDate());
        projectSolrDoc.setDueDate(project.getDueDate());
        projectSolrDoc.setEndDate(project.getEndDate());
        projectSolrDoc.setLastUpdate(project.getLastUpdate());
        projectSolrDoc.setProjectCreatedDate(project.getProjectCreatedDate());
        projectSolrDoc.setProjectModifiedDate(project.getProjectModifiedDate());
        if (invoiceNumber != null) {
            projectSolrDoc.setInvoice(invoiceNumber);
        }
        if (project.getParentId() != null) {
            projectSolrDoc.setParentId(project.getParentId());
        }
        if (project.getCreatedBy() != null) {
            projectSolrDoc.setProjectCreatorId(project.getCreatedBy().getId());
            projectSolrDoc.setProjectCreator(project.getCreatedBy().getName());
        }
        if (project.getProjectModifiedBy() != null) {
            projectSolrDoc.setProjectModifiedBy(project.getProjectModifiedBy());
        }

        if (project.getStatus() != null) {
            projectSolrDoc.setStatusId(project.getStatus().getObjectID());
            projectSolrDoc.setStatusName(project.getStatus().getName());
            projectSolrDoc.setStatusIdCode(SolrUtils.getIdName(project.getStatus().getObjectID(), project.getStatus().getCode()));
            projectSolrDoc.setStatusIdCodeName(SolrUtils.getIdCodeName(project.getStatus().getObjectID(), project.getStatus().getCode(), project.getStatus().getName()));
            projectSolrDoc.setStatusCode(project.getStatus().getCode());
            projectSolrDoc.setStatusSorder(project.getStatus().getOrder());
        }
        if (project.getManager() != null) {
            projectSolrDoc.setManagerId(project.getManager().getId());
            projectSolrDoc.setManagerName(project.getManager().getName());
            projectSolrDoc.setManagerIdName(SolrUtils.getIdName(project.getManager().getId(), project.getManager().getName()));
        }
        if (project.getClient() != null) {
            projectSolrDoc.setClientId(project.getClient().getId());
            projectSolrDoc.setClientName(project.getClient().getName());
            projectSolrDoc.setClientIdName(SolrUtils.getIdName(project.getClient().getId(), project.getClient().getName()));
        }

        StringBuilder clientNames = new StringBuilder("");
        if (project.getProjectMultiClient() != null) {
            project.getProjectMultiClient().forEach(edsCrmAccount -> {
                projectSolrDoc.getProjectMultiClientId().add(edsCrmAccount.getId());
                projectSolrDoc.getProjectMultiClientName().add(edsCrmAccount.getName());
                projectSolrDoc.getProjectMultiClientIdName().add(SolrUtils.getIdName(edsCrmAccount.getId(), edsCrmAccount.getName()));

                if ("".equals(clientNames.toString())) {
                    clientNames.append(edsCrmAccount.getName() != null ? edsCrmAccount.getName() : "");
                }
            });
        }
        projectSolrDoc.setClientNameSort(clientNames.toString());
        if (project.getBackupManager() != null) {
            project.getBackupManager().forEach(edsEmployee -> {
                projectSolrDoc.getBackupManagerId().add(edsEmployee.getId());
                projectSolrDoc.getBackupManagerName().add(edsEmployee.getName());
                projectSolrDoc.getBackupManagerIdName().add(SolrUtils.getIdName(edsEmployee.getId(), edsEmployee.getName()));
            });
        }
        if (project.getLocation() != null) {
            projectSolrDoc.setLocationId(project.getLocation().getId());
            projectSolrDoc.setLocationName(project.getLocation().getName());
            projectSolrDoc.setLocationIdName(SolrUtils.getIdName(project.getLocation().getId(), project.getLocation().getName()));
        }

        if (edsAssigneesList.size() != 0) {
            edsAssigneesList.forEach(edsEmployee -> {
                if (edsEmployee.getLocation() != null) {
                    projectSolrDoc.getUserLocationId().add(edsEmployee.getLocation().getObjectID());
                }
                projectSolrDoc.getUserId().add(edsEmployee.getObjectID());
                projectSolrDoc.getUserName().add(edsEmployee.getName());
                projectSolrDoc.getUserIdName().add(SolrUtils.getIdName(edsEmployee.getObjectID(), edsEmployee.getName()));
            });
        } else {
            projectSolrDoc.getUserId().add(project.getManager().getId());
            projectSolrDoc.getUserName().add(project.getManager().getName());
            projectSolrDoc.getUserIdName().add(SolrUtils.getIdName(project.getManager().getId(), project.getManager().getName()));
        }
        projectSolrDoc.setActualWageAmount(project.getActualWageAmount());
        projectSolrDoc.setActualClientChargeAmount(project.getActualClientChargeAmount());
        projectSolrDoc.setExpensesAmount(project.getExpensesAmount());
        projectSolrDoc.setIncomeAmount(project.getIncomeAmount());

        projectSolrDoc.setPlanedWageAmount(project.getPlanedWageAmount());
        projectSolrDoc.setPlanedClientChargeAmount(project.getPlanedClientChargeAmount());
        projectSolrDoc.setPlanedExpensesAmount(project.getPlanedExpensesAmount());
        projectSolrDoc.setPlanedIncomeAmount(project.getPlanedIncomeAmount());
        projectSolrDoc.setBillible(project.getBillable());

        SolrRelationUtils.addToRelationBaseSolrDoc(projectSolrDoc, edsRelationList, EdsRelation.TYPE_PROJECT);
        CustomFieldsUtils.setSolrDocDynamicFields(projectSolrDoc, customFields);
        return projectSolrDoc;
    }

    public Page<ProjectSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery, boolean enableMultiClientToProject) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrProjectListRepresenter.FIELD_LAST_UPDATE_DATE);
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                Sort.Direction sortDirection = filterParameter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                switch (filterParameter.getSortField()) {
                    case ProjectListItem.NUMBER ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.SORTABLE_PROJECT_NUMBER);
                    case ProjectListItem.NAME ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.SORTABLE_PROJECT_NAME);
                    case ProjectListItem.DESCRIPTION ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.SORTABLE_PROJECT_DESCRIPTION);
                    case ProjectListItem.MANAGER ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.SORTABLE_PROJECT_MANAGER);
                    case ProjectListItem.BACKUP_MANAGER ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.SORTABLE_PROJECT_BACKUP_MANAGER);
                    case ProjectListItem.CLIENT -> {
                        if (enableMultiClientToProject) {
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.SORTABLE_CLIENT_NAME_SORT);
                        } else {
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.SORTABLE_PROJECT_CLIENT);
                        }
                    }
                    case ProjectListItem.STATUS ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.FIELD_PROJECT_STATUS_SORDER);
                    case ProjectListItem.PERCENT_COMPLETED ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.FIELD_PROJECT_COMPLETED);
                    case ProjectListItem.START_DATE ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.FIELD_START_DATE);
                    case ProjectListItem.END_DATE ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.FIELD_DUE_DATE);
                    case ProjectListItem.INVOICES ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.SORTABLE_PROJECT_INVOICE);
                    case ProjectListItem.ACTUAL_TIME_SPENT ->
                            solrSort = Sort.by(sortDirection, SolrProjectListRepresenter.SORTABLE_PROJECT_HOUR_SPENT);
                    default ->
                            solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), true);
                }
            }
        }
        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));
        return solrTemplate.query(SOLR_PROJECT_CORE, query, ProjectSolrDoc.class);
    }

    public FacetFilterRpc getProjectFacetFilterData(FacetFilterRpc projectFacetFilter) {

        if (!projectFacetFilter.isFilterChanges()) {
            projectFacetFilter = commonServiceLocal.getUserFacetFilter(projectFacetFilter);
        }
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null) {
            edsUser = userManager.get(SecurityContext.getInstance().getStaticUserID());
        }
        EdsCompany edsCompany = edsUser.getCompany();
        Set<Integer> roles = edsUser.getRoleIds();


        if (edsUser.hasRole(SUPPLIER) && !roles.contains(EdsRole.CLIENT)) {
            QueryBuilderForSolr.supplierRelationForProjectList(projectFacetFilter, edsUser);
        }


        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(projectFacetFilter.getSearchKey());
        if (projectFacetFilter.getCustomData().containsKey(ProjectListItem.PROJECT_PARENT_ID)) {
            fp.setProjectId(Integer.valueOf(projectFacetFilter.getCustomData().get(ProjectListItem.PROJECT_PARENT_ID)));
        }
        fp.setRelationType(projectFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE));
        try {
            fp.setRelationID(projectFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_ID) != null ? Integer.valueOf(projectFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_ID)) : null);
            fp.setClientId(projectFacetFilter.getCustomDataValue(FacetFilterCutomField.CLIENT_ID) != null ? Integer.valueOf(projectFacetFilter.getCustomDataValue(FacetFilterCutomField.CLIENT_ID)) : null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        StringBuilder solrQuery = new StringBuilder();
        List<Integer> crmAccountIDs = null;
        if (fp.getClientId() != null) {
            crmAccountIDs = this.relationManager.getRelationIDsByType(fp.getClientId(), null, RelationItem.TYPE_CRM_ACCOUNT, RelationItem.TYPE_PROJECT);
        }

        solrQuery.append(QueryBuilderForSolr.getProjectSolrQuery(fp, edsUser, edsCompany, roles, crmAccountIDs));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(projectFacetFilter, edsCompany, SolrTaskRepresenter.FIELD_START_DATE, SolrTaskRepresenter.FIELD_DUE_DATE));

        QueryResponse facetPage = solrFacetFilterComponent.getFacetFilter(SOLR_PROJECT_CORE, solrQuery.toString(), projectFacetFilter, ProjectSolrDoc.class);
        SolrFacetUtils.fillFacetFilterDataWithNA(facetPage, projectFacetFilter);
        return projectFacetFilter;
    }
}
