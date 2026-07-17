package com.edatasite.workforce.gwt.task.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsTaskCustomFields;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.rbac.permission.EdsTaskPermission;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityCircularResolver;
import com.edatasite.workforce.gwt.availability.server.pojo.Holiday;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnTool;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;
import com.edatasite.workforce.gwt.core.server.app.*;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.customfields.TaskCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.TaskManagerImpl.TaskSearchResult;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.*;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.KanbanCalculationEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.rpc.TaskPermissionItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.*;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueItem;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanServiceLocal;
import com.edatasite.workforce.gwt.note.server.NoteServiceLocal;
import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.project.server.app.ProjectCircularResolverService;
import com.edatasite.workforce.gwt.task.client.rpc.*;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.server.app.TimesheetServiceLocal;
import com.edatasite.workforce.gwt.workstream.client.rpc.WbsService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import net.sf.mpxj.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.GroupParams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Dec 26, 2007 Time: 6:31:05 PM To
 * change this template use File | Settings | File Templates.
 */

@Transactional
@Service("taskService")
public class TaskServiceImpl implements TaskService, TaskServiceLocal, Constants, SchedulerConstant {

    public static final DecimalFormat decimalFormat = new DecimalFormat("0000");

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskManager taskManager;//we have task manager
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    @Qualifier("emailTemplateService")
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;  //we have project employee
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;// we have employee task manager
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private TimesheetService timesheetService;
    @Autowired
    private WbsService wbsService;
    @Autowired
    private CommonService commonService;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private RecurrenceJobManager recurrenceJobManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private TaskCFManager taskCFManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private EmailNotificationSettingsManager emailNotificationSettingsManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    @Qualifier("noteService")
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private TaskHistoryManager taskHistoryManager;
    @Autowired
    private ItemReminderManager itemReminderManager;
    @Autowired
    private ProjectCircularResolverService projectCircularResolverService;
    @Autowired
    ClientContactManager clientContactManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private ClockManager clockManager;
    @Autowired
    private TaskReminderManager taskReminderManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("documentsService")
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    @Qualifier("rolePermissionService")
    private RolePermissionServiceLocal rolePermissionServiceLocal;
    @Autowired
    private AvailabilityCircularResolver availabilityCircularResolver;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;
    @Autowired
    protected GroupManager groupManager;
    @Autowired
    @Qualifier("timesheetService")
    protected TimesheetServiceLocal timesheetServiceLocal;
    @Autowired
    private GlobalAuthJdbcSpringManager jdbcSpringManager;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private TimeSheetManager timesheetManager;
    @Autowired
    private TimeSheetApprovalSessionManager timeSheetApprovalSessionManager;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    @Qualifier("kanbanService")
    private KanbanServiceLocal kanbanServiceLocal;
    @Autowired
    private ListPanelSettingsManager listPanelSettingsManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private TaskStatusHistoryManager statusHistoryManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    @Qualifier("taskChangesManager")
    private TaskChangesManager taskChangesManager;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    private boolean isThisTaskManager(EdsEmployee employee, EdsTask task) {
        return employee.equals(task.getProject().getManager())
                || task.getProject().isUserBackupManager(employee.getObjectID());
    }

    /**
     * <h1>... This is method generated company task list ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Updated date {21:31 08/06/2011} ...</h3>
     *
     * @param filterParameter
     * @return
     */
    //@CheckPermission(permissions = {PermissionConstants.PM_TASKS_LIST, PermissionConstants.CRM_TASKS_LIST})
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaskList getTaskList(ListingFilterParameter filterParameter) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTask.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get task list");
        if (filterParameter != null && filterParameter.isWorkflowTaskList()) {
            return getWorkflowTasks(filterParameter);
        }
        EdsUser edsUser = employeeManager.getUser();

        String query = getTaskFacetQuery(filterParameter, edsUser);

        return getTaskListResponse(filterParameter, edsUser, query);
    }

    private String getTaskFacetQuery(ListingFilterParameter filterParameter, EdsUser edsUser) {
        FacetFilterRpc taskFacetFilter = filterParameter.getFacetFilter();
        if (taskFacetFilter != null && !taskFacetFilter.isFilterChanges()) {
            taskFacetFilter = commonServiceLocal.getUserFacetFilter(taskFacetFilter);
        }

        EdsCompany edsCompany = edsUser.getCompany();
        if (edsCompany == null) {
            return SolrTaskRepresenter.FIELD_TASK_ID + ":(-1)"; // No results if no company
        }

        if (edsUser.hasRole(SUPPLIER) && !edsUser.getRoleIds().contains(EdsRole.CLIENT)) {
            QueryBuilderForSolr.supplierRelationForTaskList(taskFacetFilter, edsUser);
        }

        if (filterParameter.isCrmTaskList() && taskFacetFilter != null) {
            String projectKey = FacetContentType.TaskFacetFilter.getContentCode()[0];
            if (taskFacetFilter.getFacetContentMap().containsKey(projectKey)) {
                FacetContentRpc projectFacetContentRpc = taskFacetFilter.getFacetContentMap().get(projectKey);
                if (projectFacetContentRpc != null && (projectFacetContentRpc.getFacetItems() == null || projectFacetContentRpc.getFacetItems().length == 0)) {
                    EdsProject crmProject = projectManager.getCrmProject();
                    if (crmProject != null) {
                        projectFacetContentRpc.setFacetItems(new SelectItem[]{new SelectItem(crmProject.getObjectID())});
                    }
                }
            }
        }

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getTaskCoreSolrQuery(edsUser, edsCompany, taskFacetFilter, filterParameter, groupManager.getCompanyBuiltInGroup(EdsGroup.ADMINISTRATORS)));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(taskFacetFilter, edsCompany, SolrTaskRepresenter.FIELD_START_DATE, SolrTaskRepresenter.FIELD_DUE_DATE, FacetContentType.TaskFacetFilter.getContentCode()[5]));
        solrQuery.append(QueryBuilderForSolr.getTaskFacetFilterAssigneesQuery(taskFacetFilter, edsUser));

        //faqat relationlarga bog'liq bulgan tasklar kerak.
        if (filterParameter.getRelationID() != null && filterParameter.getRelationType() != null) {
            List<Integer> taskIDs = relationManager.getRelationIDsByType(filterParameter.getRelationID(), null, filterParameter.getRelationType(), RelationItem.TYPE_TASK);
            solrQuery.append(" AND (");
            solrQuery.append(SolrTaskRepresenter.FIELD_TASK_ID).append(":(").append(ServerUtils.getAsCommoDelimited(taskIDs, "0", " ")).append(")");
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(filterParameter.getRelationType())) {
                solrQuery.append(" OR ").append(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID).append(":(").append(filterParameter.getRelationID()).append(")");
            }
            solrQuery.append(")");
        }

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHOW_TASKS_WHEN_PREDECESSOR_COMPLETED)) {
            solrQuery.append(" AND ((*:* AND -").append(SolrTaskRepresenter.FIELD_PREDECESSOR_TASK_STATUS).append(":[* TO *]) OR (");
            solrQuery.append(SolrTaskRepresenter.FIELD_PREDECESSOR_TASK_STATUS).append(":").append(EdsTask.COMPLETED);
            solrQuery.append(" AND -").append(SolrTaskRepresenter.FIELD_TASK_STATUS_CODE).append(": ").append(EdsTask.COMPLETED).append(")) ");
        }

        // ---- from kanban board ----
        Integer columnMetadataId = filterParameter.getColumnMetadataId();
        if (columnMetadataId != null && columnMetadataId == -1) {
            solrQuery.append(" AND -(").append(SolrTaskRepresenter.FIELD_TASK_STATUS_ID).append(":").append("[* TO *]").append(")");
        } else if (columnMetadataId != null) {
            solrQuery.append(" AND (").append(SolrTaskRepresenter.FIELD_TASK_STATUS_ID).append(":").append(columnMetadataId).append(")");
        }

        return solrQuery.toString();
    }

    private TaskList getWorkflowTasks(ListingFilterParameter filterParameter) {
        if (filterParameter != null) {
            List<EdsTask> tasks = taskManager.workflowTaskList(filterParameter);
            ArrayList<TaskListItem> items = new ArrayList<>();
            if (tasks != null && !tasks.isEmpty()) {
                for (EdsTask task : tasks) {
                    items.add(task.createTaskListItem());
                }
            }
            return new TaskList(items, items.size(), null, false);
        }
        return null;
    }

    /**
     * <h1>... This is method cretae task solr response ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Cretaed date {21:33 08/06/2011} ...</h3>
     *
     * @param filterParameter
     * @param edsUser
     * @param solrQuery
     * @return
     */
    private TaskList getTaskListResponse(ListingFilterParameter filterParameter, EdsUser edsUser, String solrQuery) {
        QueryResponse taskSolrDocPage = taskSolrComponent.getList(filterParameter, solrQuery);
        return getTaskFromSolrResult(taskSolrDocPage, edsUser, filterParameter);
    }


    /**
     * <h1>... This is method generated task solr query ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Updated date {21:36 08/06/2011} ...</h3>
     *
     * @param filterParameter
     * @param solrQuery
     * @return
     */
    private SolrQuery getTaskSolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setHighlightSnippets(20);
        query.setParam(CommonParams.ROWS, filterParameter.getLimit() > 0 ? String.valueOf(filterParameter.getLimit()) : "50");
        query.setParam(GroupParams.GROUP, true);
        query.setParam(GroupParams.GROUP_LIMIT, "7");
        query.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
        query.setParam(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_TASK_ID);
        if (!filterParameter.isSearchButton()) {
            if (StringUtils.isNotBlank(filterParameter.getSortField())) {
                boolean desc = true;
                if (!filterParameter.isAscending()) {
                    desc = false;
                }
                switch (filterParameter.getSortField()) {
                    case TaskListItem.NUMBER ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.DESCRIPTION ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_DESCRIPTION, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.LAST_MODIFIED ->
                            query.setSort(SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.PRIORITY_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_TASK_PRIORITY, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.PROJECT_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_PROJECT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.PROJECT_NUMBER ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_PROJECT_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.PROJECT_MANAGER_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_PROJECT_MANAGER_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.CLIENT ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_PROJECT_CLIENT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.DUE_DATE ->
                            query.setSort(SolrTaskRepresenter.FIELD_DUE_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.COMPLETE ->
                            query.setSort(SolrTaskRepresenter.FILED_TASK_PERCENT_COMPLETED, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.ACTUAL_START_DATE ->
                            query.setSort(SolrTaskRepresenter.FIELD_ACTUAL_START_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.STATUS_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.START_DATE ->
                            query.setSort(SolrTaskRepresenter.FIELD_START_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.END_DATE ->
                            query.setSort(SolrTaskRepresenter.FIELD_END_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.ESTIMATED ->
                            query.setSort(SolrTaskRepresenter.FIELD_ESTIMATED_TIME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.PARENT_WORKSTREAM_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_WORKSTREAM_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.OVERALL_STATUS_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_TASK_STATUS_SORDER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.ID ->
                            query.setSort(SolrTaskRepresenter.FIELD_TASK_ID, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                    case TaskListItem.KANBAN_ORDER ->
                            query.setSort(SolrTaskRepresenter.KANBAN_ORDER, (SolrQuery.ORDER.asc));
                    default ->
                            CustomFieldsUtils.setCustomFieldsSortableNameToSolr(filterParameter.getSortField(), desc, query, true);
                }
            } else {
                query.setSort(SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    public ListResult<TaskListItem> getNewKanbanTasks(ListingFilterParameter filterParameter, SelectItem columnMetadata) {
        filterParameter.setColumnMetadataId(columnMetadata.getId());
        filterParameter.setSortField(null);
        filterParameter.setSortDir(1);

        EdsUser edsUser = userManager.getUser();
        String mainSolrQuery = getTaskFacetQuery(filterParameter, edsUser);
        ListResult<TaskListItem> result = getKanbanTaskList(filterParameter, mainSolrQuery);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTask.class.getSimpleName());
        kpiLog.setEntityType("TASK_KANBAN_LIST");
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Task Kanban list");
        return result;
    }

    @Override
    public LinkedHashMap<Integer, Long> getNewKanbanTasksCounts(ArrayList<Integer> columnIds) {
        if (columnIds == null || columnIds.isEmpty()) {
            return new LinkedHashMap<>();
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSortField(null);
        filterParameter.setSortDir(1);

        LinkedHashMap<Integer, Long> result = new LinkedHashMap<>();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_TASK_CORE);
        EdsUser edsUser = userManager.getUser();

        try {
            SolrQuery query = new SolrQuery("*:*");
            query.setRows(0);

            String baseQuery = getTaskFacetQuery(filterParameter, edsUser);
            query.addFilterQuery(baseQuery);

            StringBuilder facetQuery = new StringBuilder();
            facetQuery.append(SolrTaskRepresenter.FIELD_TASK_STATUS_ID).append(":(");
            for (int i = 0; i < columnIds.size(); i++) {
                if (i > 0) facetQuery.append(" OR ");
                facetQuery.append(columnIds.get(i));
            }
            facetQuery.append(")");

            query.addFilterQuery(facetQuery.toString());
            query.setFacet(true);
            query.setFacetMinCount(1);
            query.addFacetField(SolrTaskRepresenter.FIELD_TASK_STATUS_ID);

            QueryResponse resp = server.query(query, SolrRequest.METHOD.POST);

            if (resp.getFacetFields() != null && !resp.getFacetFields().isEmpty()) {
                FacetField statusFacet = resp.getFacetFields().get(0);
                for (FacetField.Count count : statusFacet.getValues()) {
                    Integer statusId = Integer.parseInt(count.getName());
                    result.put(statusId, count.getCount());
                }
            }

            for (Integer columnId : columnIds) {
                result.putIfAbsent(columnId, 0L);
            }

        } catch (SolrServerException | IOException e) {
            log.error("Error getting kanban task counts", e);
            for (Integer columnId : columnIds) {
                result.putIfAbsent(columnId, 0L);
            }
        }

        return result;
    }

    private ListResult<TaskListItem> getKanbanTaskList(ListingFilterParameter filterParameter, String solrQuery) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_TASK_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getTaskSolrQuery(filterParameter, solrQuery), SolrRequest.METHOD.POST);

        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getKanbanTaskFromSolrResult(resp);
    }

    private ListResult<TaskListItem> getKanbanTaskFromSolrResult(QueryResponse resp) {
        ArrayList<TaskListItem> list = new ArrayList<>();
        EdsUser currentUser = userManager.getUser();
        GroupCommand groupCommand = resp.getGroupResponse().getValues().get(0);
        int totalCount = groupCommand.getNGroups();

        List<String> columnCodes = commonServiceLocal.getCFsColumnCodeByUiTypes(ViewName.Task, ListUtils.getCFUITypesForKanbanItem());
        EdsListPanelSettings panelSettings = listPanelSettingsManager.getUserListPanelSettings(ListPanelType.TaskKanbanPanel.name(), null);
        ColumnTool columnTool = new ColumnTool();
        if (panelSettings != null) {
            JSONObject parentJSONObject = null;
            try {
                parentJSONObject = (JSONObject) new JSONParser().parse(panelSettings.getSettingsJSONData());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JSONObject columnToolObject = (JSONObject) parentJSONObject.get("columns");
            JSONObject colors = columnToolObject != null ? (JSONObject) columnToolObject.get("colors") : null;
            if (colors != null && !colors.isEmpty()) {
                for (Object entry : colors.values()) {
                    JSONObject colorJson = (JSONObject) entry;
                    if (colorJson != null && colorJson.get("w") != null && colorJson.get("c") != null) {
                        columnTool.addColor(new ColumnColor(colorJson.get("w").toString(), colorJson.get("t").toString(), colorJson.get("c").toString()));
                    }
                }
            }
        }

        List<Integer> ids = groupCommand.getValues().stream().map(group -> Integer.valueOf(group.getGroupValue())).collect(Collectors.toList());
        Map<Integer, String> taskNotes = noteHistoryManager.getLastNotesAsMap(EdsNoteHistory.TASK, ids);

        for (Group group : groupCommand.getValues()) {
            SolrDocumentList solrDocList = group.getResult();
            SolrDocument doc = solrDocList.get(0);

            TaskListItem item = new TaskListItem();

            String assigneeFullnames = ServerUtils.asListToString(SolrUtils.asListString(doc, SolrTaskRepresenter.FIELD_ASSIGNEE_NAMES));
            String taskStatusCode = SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_STATUS_CODE);

            if ((!(EdsTask.COMPLETED.equals(taskStatusCode) || EdsTask.CANCELLED.equals(taskStatusCode) || EdsTask.CLOSED.equals(taskStatusCode))) &&
                    assigneeFullnames != null && assigneeFullnames.contains(currentUser.getFullName())) {
                item.setShowTimer(true);
                item.setShowLogTime(true);
            }

            item.setObjectID(Integer.parseInt(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_ID)));
            item.setKanbanOrder(SolrUtils.asLong(doc, SolrTaskRepresenter.KANBAN_ORDER));
            item.setNumber(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_NUMBER));
            item.setName(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_NAME));
            item.setProjectName(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_PROJECT_NAME));
            item.setProjectCustomerName(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_NAME));
            item.setPriorityCode(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_PRIORITY_CODE));
            item.setTypeCode(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_TYPE_CODE));
            item.setStartDate(SolrUtils.asDate(doc, SolrTaskRepresenter.FIELD_START_DATE));
            item.setDueDate(SolrUtils.asDate(doc, SolrTaskRepresenter.FIELD_DUE_DATE));
            item.setDescription(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_DESCRIPTION));
            item.setPriorityName(referenceWfmMessageSource.localize(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_PRIORITY_CODE), SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_PRIORITY)));
            item.setAssigneeFullNames(assigneeFullnames);
            if (SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_PRIORITY) != null && !columnTool.getColors().isEmpty()) {
                item.setPriorityColor(columnTool.getColor(SolrUtils.asString(doc, SolrTaskRepresenter.FIELD_TASK_PRIORITY)));
            }

            //Set Last Note
            if (taskNotes.containsKey(item.getObjectID())) {
                item.setNote(taskNotes.get(item.getObjectID()));
            }
            item.setCustomFields(CustomFieldsUtils.getInSolrCustomFields(doc, columnCodes));
            list.add(item);
        }
        return new TaskList(list, totalCount);
    }

    public synchronized void changeTaskKanbanOrder(SelectItem columnLayoutData, Integer taskID, Integer prevTaskID, Integer afterTaskID) {
        if (taskID != null) {
            EdsTask task = taskManager.get(taskID);
            task.clear();
            if (task == null) {
                return;
            }
            if (prevTaskID != null && afterTaskID == null) {
                EdsTask potentialTask = taskManager.getSiblingTaskByPrevItem(prevTaskID, columnLayoutData.getId());
                afterTaskID = potentialTask != null ? potentialTask.getObjectID() : null;
            }

            EdsUser user = userManager.getUser();
            task.setUpdater(user);


            EdsReference newTaskStatus = referenceManager.get(columnLayoutData.getId());
            boolean isTaskStatusChanged = isValueChanged(task.getStatus(), newTaskStatus);
            log.info("KANBAN_STATUS_CHANGED. StatusID:" + newTaskStatus.getName() + " --- TaskID: " + task.getObjectID() + " --- UserID: " + user.getObjectID() + " --- COMPANYID: " + user.getCompany().getObjectID());
            if (isTaskStatusChanged) {
                updateTaskCellStatus(task, newTaskStatus, true, columnLayoutData.getCategory());
            }

            try {
                taskManager.update(task);
                taskSolrComponent.index(task);
            } catch (Exception e) {
                e.printStackTrace();
            }

            baseEventPostProcessor.registerCustomEvent(KanbanCalculationEventListenerImpl.TYPE_TASK, EdsMyUpdate.ADD, task, prevTaskID, afterTaskID);

            if (isTaskStatusChanged) {
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_TASK);
            }

        }
    }

    private boolean isValueChanged(EdsReference oldValue, EdsReference newValue) {
        if (oldValue == null && newValue == null) {
            return false;
        }
        if (oldValue == null || newValue == null) {
            return true;
        }

        return !oldValue.getCode().equals(newValue.getCode());
    }

    /**
     * <h1>... This is method read in solr tasks and fill rpc object ...</h1>
     * <br/>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated date {21:36 08/06/2011} ...</h3>
     *
     * @param taskID
     * @return
     */
    public Boolean checkAccess(Integer taskID, String permission, String context) {
        EdsUser user = checkForArtificateRoles(taskID);
        return ServerUtils.hasPermission(permission);

    }

    public HashSet<String> getPermissions(Integer taskID, String context) {
        return rolePermissionServiceLocal.getPermissionList(context, checkForArtificateRoles(taskID));
    }

    public HashSet<String> getPermissions(Integer taskID, String context, Integer userID) {
        EdsUser user = issueManager.getUser();
        EdsTask task = taskManager.get(taskID);
        return rolePermissionServiceLocal.getPermissionList(context, ArtificateRoles(task, user));
    }

    public EdsUser checkForArtificateRoles(int taskID) {
        EdsUser user = issueManager.getUser();
        EdsTask task = taskManager.get(taskID);
        return ArtificateRoles(task, user);
    }

    private EdsUser ArtificateRoles(EdsTask task, EdsUser user) {
        user.clearArtificialRoles();
        if (task != null) {
            if (task.getProject().getManager() != null && task.getProject() != null && user.getObjectID().equals(task.getProject().getManager().getObjectID())) {
                user.addArtificialRole(roleManager.getByCode(Constants.PMOFPR));
            }
            if (task.getCreator() != null && user.getObjectID().equals(task.getCreator().getObjectID())) {
                user.addArtificialRole(roleManager.getByCode(Constants.CREATOR));
            }
            if (task.getProject() != null && task.getProject().isUserBackupManager(user.getObjectID())) {
                user.addArtificialRole(roleManager.getByCode(Constants.BMOFPR));
            }
        }
        return user;
    }

    private SelectItem[] getAsSelectItem(List listOfObject, final int type) {
        return ServerUtils.getAsSelectItem(listOfObject, type);
    }

    private TaskList getTaskFromSolrResult(QueryResponse resp, EdsUser currentUser, ListingFilterParameter filterParameter) {
        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
        boolean atLeastOneTimerIsRunning = false;
        boolean percentOverComplete = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED);

        SelectItem[] statuses = null;
        Map<Integer, SelectItem> statusesMap = null;
        statuses = getAsSelectItem(referenceManager.listReferences(ReferenceParentEnum._TASK_STATUS.name()), ServerUtils.REFERENCE);
        statusesMap = SelectItem.asMap(statuses);

        if (panelSettings == null) {
            panelSettings = ListPanelToolRpc.createIntance();
            panelSettings.setColumnCodeName(new ArrayList<>(Arrays.asList(TaskListItem.NUMBER, TaskListItem.NAME,// default show column code
                    TaskListItem.PROJECT_NAME, TaskListItem.CLIENT,
                    TaskListItem.PRIORITY_NAME, TaskListItem.STATUS_NAME,
                    TaskListItem.START_DATE, TaskListItem.DUE_DATE,
                    TaskListItem.COMPLETE, TaskListItem.DESCRIPTION, TaskListItem.LAST_MODIFIED)));
        }

        Map<Integer, List<SolrDocument>> results = new HashMap<>();
        ArrayList<Integer> taskIds = new ArrayList<>();
        GroupCommand groupCommand = resp.getGroupResponse().getValues().get(0);
        int totalCount = groupCommand.getNGroups();
        for (Group group : groupCommand.getValues()) {
            SolrDocumentList solrDocList = group.getResult();
            SolrDocument solrDoc = solrDocList.get(0);
            Integer taskid = Integer.parseInt(SolrUtils.asString(solrDoc, SolrTaskRepresenter.FIELD_TASK_ID));
            results.put(taskid, solrDocList);
            taskIds.add(taskid);
        }

        Map<Integer, EdsTask> taskObjectMap = new HashMap<>();
        ArrayList<TaskListItem> taskItems = new ArrayList<>();

        boolean enableMultiClientToProject = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT);

        if (!taskIds.isEmpty()) {
            List<EdsTask> taskList = taskManager.getTaskByIds(ServerUtils.getAsCommoDelimited(taskIds, "0", ","));
            HashMap<Integer, Double[]> taskCostAndTimeMap;
            if (panelSettings.getColumnCodeName().contains(TaskListItem.ACTUAL_HOURS_SPENT) || panelSettings.getColumnCodeName().contains(TaskListItem.HOUR_SPENT) || panelSettings.getColumnCodeName().contains(TaskListItem.COMPLETE)) {
                taskCostAndTimeMap = timeSheetManager.getCostAndTimeSpentOnTasks(ServerUtils.getAsCommoDelimited(taskIds, "0", ","));
            } else {
                taskCostAndTimeMap = new HashMap<>();
            }
            HashMap<Integer, Double> waitingTimeMap;
            if (panelSettings.getColumnCodeName().contains(TaskListItem.WAITING_HOURS)) {
                waitingTimeMap = timesheetManager.getTaskTimeSpents(ServerUtils.getAsCommoDelimited(taskIds, "0", ","), EdsTimeSheet._WAITING);
            } else {
                waitingTimeMap = new HashMap<>();
            }

            HashMap<Integer, Double> rejectedTimeMap;
            if (panelSettings.getColumnCodeName().contains(TaskListItem.REJECTED_HOURS)) {
                rejectedTimeMap = timeSheetManager.getTaskTimeSpents(ServerUtils.getAsCommoDelimited(taskIds, "0", ","), EdsTimeSheet._REJECT);
            } else {
                rejectedTimeMap = new HashMap<>();
            }
            for (EdsTask task : taskList) {
                taskObjectMap.put(task.getObjectID(), task);
            }
            taskList.clear();
            Map<Integer, Integer> taskClockMap = clockManager.getActiveClockMapForCurrentUser(ServerUtils.getAsCommoDelimited(taskIds, "0", ","), PM_TASK, employeeManager.getUser().getObjectID());
            Map<Integer, List<Integer>> taskManagerBManagerMap = taskManager.getTaskProjectManagerAndBManagerMap(ServerUtils.getAsCommoDelimited(taskIds, "0", ","));

            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();

            for (Integer taskId : taskIds) {
                Double[] taskCostAndTimeSpent = taskCostAndTimeMap.get(taskId);

                SolrDocument relevantDoc = null;

                List<SolrDocument> relatedEntries = results.get(taskId);

                Integer rank = 0;

                for (SolrDocument doc : relatedEntries) {
                    Integer currentRank = (Integer) doc.getFieldValue(SolrTaskRepresenter.FIELD_RANK);
                    if (currentRank != null && rank < currentRank) {
                        rank = currentRank;
                    }
                    relevantDoc = doc;
                }

                TaskListItem taskItem = new TaskListItem();
                EdsTask edsTask = taskObjectMap.get(taskId);
                if (edsTask != null) {
                    taskItem.setObjectID(taskId);
                    taskItem.setCreationDate(SolrUtils.asDate(relevantDoc, SolrTaskRepresenter.FIELD_CREATION_DATE));
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.NUMBER)) {
                        taskItem.setNumber(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_NUMBER));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.NAME) && relevantDoc.getFieldValue(SolrTaskRepresenter.FIELD_TASK_NAME) != null) {
                        taskItem.setName(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_NAME));
                    }
                    if (filterParameter.isLookUp()) {
                        taskItems.add(taskItem);
                        continue;
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.CLIENT)) {
                        taskItem.setClient(enableMultiClientToProject ? ServerUtils.asListToString(SolrUtils.asListString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PROJECT_MULTI_CLIENT_NAME)) : SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_NAME));
                    }

                    if (panelSettings.getColumnCodeName().contains(TaskListItem.PROJECT_NAME)) {
                        taskItem.setProjectName(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PROJECT_NAME));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.PROJECT_CUSTOMER_NAME)) {
                        taskItem.setProjectCustomerName(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_NAME));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.PROJECT_NUMBER)) {
                        taskItem.setProjectNumber(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PROJECT_NUMBER));
                    }
                    taskItem.setProjectStatusCode(edsTask.getProject().getStatus().getCode());

                    if (panelSettings.getColumnCodeName().contains(TaskListItem.PROJECT_MANAGER_NAME)) {
                        taskItem.setProjectManagerName(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PROJECT_MANAGER_NAME));
                    }
                    taskItem.setProjectId(SolrUtils.asInteger(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PROJECT_ID));
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.PARENT_WORKSTREAM_NAME)) {
                        taskItem.setParentWorkstreamName(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_NAME));
                        taskItem.setParentWorkstreamId(SolrUtils.asInteger(relevantDoc, SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.DESCRIPTION)) {
                        taskItem.setDescription(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_DESCRIPTION));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.PRIORITY_NAME)) {
                        taskItem.setPriorityName(referenceWfmMessageSource.localize(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PRIORITY_CODE), SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PRIORITY)));
                    }
                    taskItem.setPriorityCode(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_PRIORITY_CODE));

                    if (panelSettings.getColumnCodeName().contains(TaskListItem.TYPE_NAME)) {
                        taskItem.setTypeCode(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_TYPE_CODE));
                        taskItem.setTypeName(referenceWfmMessageSource.localize(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_TYPE), SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_TYPE)));
                    }

                    taskItem.setTaskStatusId(SolrUtils.asInteger(relevantDoc, SolrTaskRepresenter.FIELD_TASK_STATUS_ID));
                    taskItem.setStatusCode(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_STATUS_CODE));
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.STATUS_NAME)) {
                        taskItem.setStatusName(referenceWfmMessageSource.localize(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS_CODE), SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS)));
                        taskItem.setStatus(new SelectItem(taskItem.getTaskStatusId(), taskItem.getStatusName(), taskItem.getStatusCode(), (statusesMap.get(taskItem.getTaskStatusId())) != null && (statusesMap.get(taskItem.getTaskStatusId())).isSelected()));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.OVERALL_STATUS_NAME)) {
                        taskItem.setOverallStatusName(referenceWfmMessageSource.localize(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_STATUS_CODE), SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_STATUS)));
                        taskItem.setOverallStatusId(SolrUtils.asInteger(relevantDoc, SolrTaskRepresenter.FIELD_TASK_STATUS_ID));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.START_DATE) || panelSettings.getColumnCodeName().contains(TaskListItem.DUE_DATE)
                            || panelSettings.getColumnCodeName().contains(TaskListItem.END_DATE)) {
                        taskItem.setStartDate(SolrUtils.asDate(relevantDoc, SolrTaskRepresenter.FIELD_START_DATE));
                        taskItem.setDueDate(SolrUtils.asDate(relevantDoc, SolrTaskRepresenter.FIELD_DUE_DATE));
                        taskItem.setActualStartDate(SolrUtils.asDate(relevantDoc, SolrTaskRepresenter.FIELD_ACTUAL_START_DATE));
                        taskItem.setEndDate(SolrUtils.asDate(relevantDoc, SolrTaskRepresenter.FIELD_END_DATE));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.LAST_MODIFIED)) {
                        taskItem.setLastModified(SolrUtils.asDate(relevantDoc, SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.ACTUAL_HOURS_SPENT)) { //approved timesheet hours
                        taskItem.setActualHoursSpent(taskCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(taskCostAndTimeSpent[TASK_ACTUAL_TIME_SPENT] != null ? taskCostAndTimeSpent[TASK_ACTUAL_TIME_SPENT].intValue() : 0) : "00:00");
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.HOUR_SPENT)) { //time Spent(timesheet hours)
                        taskItem.setHoursSpent(taskCostAndTimeSpent != null ? ServerUtils.getTimeSpentHM(taskCostAndTimeSpent[TASK_HOUR_SPENT] != null ? taskCostAndTimeSpent[TASK_HOUR_SPENT].intValue() : 0) : "00:00");
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.WAITING_HOURS)) {
                        taskItem.setWaitingHours(waitingTimeMap.get(taskId) != null ? ServerUtils.getTimeSpentHM(waitingTimeMap.get(taskId).intValue()) : "00:00");
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.REJECTED_HOURS)) {
                        taskItem.setRejectedHours(rejectedTimeMap.get(taskId) != null ? ServerUtils.getTimeSpentHM(rejectedTimeMap.get(taskId).intValue()) : "00:00");
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.LAST_MODIFIED_BY)) {
                        String lastModifiedBy = SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_LAST_MODIFIED_BY);
                        taskItem.setLastModifiedBy(lastModifiedBy != null ? lastModifiedBy : referenceWfmMessageSource.localize("NA", "N/A"));
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.CREATED_BY)) {
                        String createdBy = SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_CREATOR);
                        taskItem.setCreatedBy(createdBy != null ? createdBy : referenceWfmMessageSource.localize("NA", "N/A"));
                    }

                    taskItem.setPermissions(new PermissionListItem(getAggregatePermissionsFromSolrResult(results.get(edsTask.getObjectID()))));
                    String assigneeFullnames = ServerUtils.asListToString(SolrUtils.asListString(relevantDoc, SolrTaskRepresenter.FIELD_ASSIGNEE_NAMES));

                    if (panelSettings.getColumnCodeName().contains(TaskListItem.ASSIGNED_TO)) {
                        taskItem.setAssignedTo(assigneeFullnames);
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.ESTIMATED)) {
                        taskItem.setEstimated(edsTask.getEstimatedTime() != null ? edsTask.getEstimatedTime() : 0);
                    }
                    if (panelSettings.getColumnCodeName().contains(TaskListItem.TASK_AMOUNT)) {
                        taskItem.setTaskAmount(edsTask.getTaskAmount());
                    }

                    if (panelSettings.getColumnCodeName().contains(TaskListItem.COMPLETE)) {
                        float str;
                        if (settings != null && settings.isAutomatic()) {//if settings is null then project percent calculation mode is manual by default
                            str = (float) (edsTask.getEstimatedTime() != 0 ? ((taskCostAndTimeSpent != null ? (taskCostAndTimeSpent[TASK_ACTUAL_TIME_SPENT] != null ? taskCostAndTimeSpent[TASK_ACTUAL_TIME_SPENT].intValue() : 0) : 0.00) * 100 / edsTask.getEstimatedTime()) : 0.00);
                        } else {
                            str = SolrUtils.asFloat(relevantDoc, SolrTaskRepresenter.FILED_TASK_PERCENT_COMPLETED);
                        }
                        taskItem.setComplete(String.valueOf((str > 100f && !percentOverComplete) ? 100f : str));
                    }
                    taskItem.setBillable(edsTask.getBillable());
                    taskItem.setGoogleID(edsTask.getGoogleID());

                    taskItem.setTimerIsStarted(taskClockMap.get(taskId) != null);
                    if (taskItem.timerIsStarted()) {
                        atLeastOneTimerIsRunning = true;
                    }
                    String taskStatusCode = SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_STATUS_CODE);
                    if ((!(EdsTask.COMPLETED.equals(taskStatusCode) || EdsTask.CANCELLED.equals(taskStatusCode) || EdsTask.CLOSED.equals(taskStatusCode))) &&
                            assigneeFullnames != null && assigneeFullnames.contains(currentUser.getFullName())) {
                        taskItem.setShowTimer(true);
                        taskItem.setShowLogTime(true);
                    }
                    if (taskManagerBManagerMap.get(taskId) != null) {
                        taskItem.setProjectManagerID(taskManagerBManagerMap.get(taskId).get(0));
                        taskItem.setProjectBackupManagerID(taskManagerBManagerMap.get(taskId).get(1));
                        if (taskManagerBManagerMap.get(taskId).contains(currentUser.getObjectID())) {
                            taskItem.setPMorBackupPM(true);
                        }
                        taskItem.setProjectBackupManagerIDs((ArrayList<Integer>) taskManagerBManagerMap.get(taskId));
                    }
                    taskItem.setTaskCreatorID(SolrUtils.asInteger(relevantDoc, SolrTaskRepresenter.FIELD_TASK_CREATOR_ID));
                    taskItem.setAllDay(edsTask.isAllDay());
                    taskItem.setCustomFields(CustomFieldsUtils.getInSolrCustomFields(relevantDoc, panelSettings.getColumnCodeName()));
                    taskItem.setRelationValueMap(SolrRelationUtils.getSolrRelationValue(relevantDoc, EdsRelation.TYPE_TASK));
                    taskItems.add(taskItem);
                }
            }

        }
        return new TaskList(taskItems, totalCount, getEditTaskStatusDrop(null), atLeastOneTimerIsRunning);
    }

    private ArrayList<String> getAggregatePermissionsFromSolrResult(List<SolrDocument> entries) {
        ArrayList<String> permissions = new ArrayList<>();
        for (SolrDocument doc : entries) {
            try {
                List<String> dPerm = SolrUtils.asListString(doc, SolrTaskRepresenter.FIELD_PERMISSIONS);
                permissions.addAll(dPerm);
            } catch (ClassCastException ex) {
                String dPerm = (String) doc.getFieldValue(SolrTaskRepresenter.FIELD_PERMISSIONS);
                permissions.add(dPerm);
            }
        }
        return permissions;
    }

    public ArrayList<String> getAggregatePermissions(List<EdsTaskRbac> rbacEntries) {
        List<EdsTaskPermission> permissions = new ArrayList<>();

        for (EdsTaskRbac tRbac : rbacEntries) {
            permissions.add(tRbac.getTaskPermission());
        }
        EdsTaskPermission permission = permissions.get(0);
        TaskPermissionItem tPermission = permission.getAsTaskPermissionItem();
        tPermission = permission.getMergedPermissions(tPermission, permissions);

        return tPermission.getPemissionAsStringList();
    }

    private TaskPermissionItem getAggregatePermisionsAs(List<EdsTaskRbac> rbacEntries) {
        List<EdsTaskPermission> permissions = new ArrayList<>();

        for (EdsTaskRbac tRbac : rbacEntries) {
            permissions.add(tRbac.getTaskPermission());
        }

        EdsTaskPermission permission = permissions.get(0);
        TaskPermissionItem tPermission = permission.getAsTaskPermissionItem();
        tPermission = permission.getMergedPermissions(tPermission, permissions);
        return tPermission;
    }

    @Transactional
    public Integer indexProjectTasks(Integer projectID, Integer start, Integer limit) {
        List<EdsTask> tasks = taskManager.getProjectTasks(projectID, start, limit);
        if (tasks.isEmpty()) {
            return -1;
        }
        taskRbacManager.batchIndexTask(tasks);
        try {
            taskSolrComponent.indexes(tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-----------------------------Indexed " + limit + " Tasks-----------------------------");
        EdsTask ta = tasks.get(tasks.size() - 1);
        return ta.getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectItem[] getProjects() {
        return commonService.getProjects(false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAddTaskStatuses() {
        return commonService.getAddTaskStatusDrop();
    }

    /**
     * Method used to get task prioties for
     * Add Task and Task Edit views
     *
     * @return SelectItem[]
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPriorities() {
        return commonServiceLocal.convertReference2SelectItem(EdsTask.TASK_PRIORITY, false, EdsTask.MEDIUM);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTaskTypes() {
        return commonServiceLocal.convertReference2SelectItem(EdsTask.TASK_TYPES, false, NULL);
    }

    private void updateTaskStatus(EdsTask task) {
        if (task != null && task.getStatus() != null) {
            String TASK_STATUS = task.getTaskLastStatus();
            EdsReference status = referenceManager.findReference(EdsTask.TASK_STATUS, TASK_STATUS);
            if (status != null) {
                task.setStatus(status);
                task.setLastUpdateTime(new Date());
                Boolean isCompleted = EdsTask.COMPLETED.equals(TASK_STATUS);
                if (EdsTask.IN_PROGRESS.equals(TASK_STATUS) || isCompleted) {
                    updateProjectStatus(task);
                    if (isCompleted) {
                        baseEventPostProcessor.registerEvent(StatusTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, taskManager.getUser());
                    }
                }
            }
        }
    }

    /**
     * Method updates task with
     * given EditTask params
     *
     * @param editTask
     */
    public void updateTask(final EditTask editTask) throws NumberExistingException {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTask.class.getSimpleName());
        kpiLog.setEntityId(editTask.getObjectID());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        if (editTask != null && editTask.getObjectID() != null) {
            kpiLog.setEntityId(editTask.getObjectID());
        }
        ServerUtils.kpiLog(log, kpiLog, "Update task");

        if (editTask.isNonAssignedIncluded()) {
            saveNewProjectEmployees(editTask);
        }

        final EdsTask task = taskManager.get(editTask.getObjectID());
        task.clear();
        EdsTaskHistory taskHistory = new EdsTaskHistory();
        if (taskHistoryManager.getTaskHistoryByTaskId(task.getObjectID()) != null) {
            taskHistory = taskHistoryManager.getTaskHistoryByTaskId(task.getObjectID());
        }

        taskHistory.copyTaskToTaskHistory(task);
        taskHistoryManager.createOrUpdate(taskHistory);

        NumberData numberData = editTask.getNumberData();
        boolean taskDateChanged = false;
        boolean taskPredecessorsOrSuccessorsChanged = false;
        Set<EdsTask> oldPreds = new HashSet<>(task.getPredecessors());
        Set<EdsTask> oldSuccs = new HashSet<>(task.getSuccessors());
        Date taskOldDueDate = task.getDueDate();

        if ("".equals(editTask.getNumber())) {
            editTask.setNumber(null);
        }

        if (editTask.getNumber() != null && (numberData == null || numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
            throw new NumberExistingException("Incorrect task number format.");
        }
        EdsProject oldProject = null;
        EdsProject newProject = null;
        if (editTask.getProjectId() != null) {
            oldProject = projectManager.get(task.getProject().getObjectID());
            newProject = projectManager.get(editTask.getProjectId());
        }
        final EdsUser user = taskManager.getUser();
        EdsEmployee employee = employeeManager.get(MultiTaskList.FROM_TODO_LIST.equals(editTask.getCreatedFrom()) ? editTask.getEmployeeID() : user.getObjectID());
        final EdsCompany company = user.getCompany();
        EdsWorkStream workStreamBeforeEdit = task.getParentWS();

        boolean editPermission = ServerUtils.hasPermission(PermissionConstants.PM_TASKS_EDIT) || ServerUtils.hasPermission(PermissionConstants.CRM_TASKS_EDIT);

        List<EdsTaskRbac> entries = taskRbacManager.getEntriesForUserOrHisMemberGoups(task, user, user.getMembershipGroups());
        TaskPermissionItem overalPermission = new TaskPermissionItem();
        if (entries != null && !entries.isEmpty()) {
            overalPermission = getAggregatePermisionsAs(entries);
        }
        if ((task.getCreator() != null && task.getCreator().getObjectID().equals(user.getObjectID())) || editPermission) {
            overalPermission.setEdit(true);
        }
        if (employee == null) {
            employee = (EdsEmployee) user;
        }
        if (employeeManager.userIsAssignToTask(employee.getObjectID(), task.getObjectID())) {
            overalPermission.setAssigneeStatusEdit(true);
        }
        if ((entries == null || entries.isEmpty()) && !overalPermission.isEdit() && !overalPermission.isAssigneeStatusEdit()) {
            return;
        }
        task.enableTaskChangeListener(new EdsTask.ChangeListener() {
            public void onStatusChange(EdsReference status) {
                if (status.equals(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS)) || status.equals(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED))) {
                    updateProjectStatus(task);
                }
            }

            public void onDatesChanged(Date tstartDate, Date tdueDate) {
                Set<EdsEmployeeTask> etSet = task.getUnDeletedAssignments();
                if (editTask.isRecalculateResourceHours()) {

                    ArrayList<Holiday> companyHolidays = availabilityCircularResolver.getCompanyHolidayList();

                    for (EdsEmployeeTask et : etSet) {
                        EdsEmployee edsEmployee = et.getProjectEmployee().getEmployeeDepartment().getEmployee();
                        Set<EdsTimeSlotItem> timeSlotItem = edsEmployee.getTimeSlot().getItems();
                        Map<Integer, Integer> available = new HashMap<>();
                        for (EdsTimeSlotItem item : timeSlotItem) {
                            available.put(item.getDay(), item.getEndTime() - item.getStartTime());
                        }
                        if (task.getStartDate() != null && task.getDueDate() != null) {
                            Calendar startDate = new GregorianCalendar();

                            Calendar dueDate = new GregorianCalendar(TimeZone.getTimeZone(user.getCompany().getCountryZone().getZone().getZoneID()));
                            startDate.setTime(new Date(task.getStartDate().getTime() + user.getUserTimezone().getRawOffset()));
                            dueDate.setTime(new Date(task.getDueDate().getTime() + user.getUserTimezone().getRawOffset()));
                            int k = 0;

                            ArrayList<Holiday> employeeHolidays = availabilityCircularResolver.getHolidaysList(edsEmployee);
                            ArrayList<Calendar> employeeLeaves = availabilityCircularResolver.getEmployeeLeaves(tstartDate, tdueDate, edsEmployee);

                            ArrayList<Calendar> availableDays = new ArrayList<>();
                            while (dueDate.getTime().compareTo(startDate.getTime()) >= 0) {
                                Integer dayIndex = startDate.get(Calendar.DAY_OF_WEEK) - 1;

                                if (available.containsKey(dayIndex) && available.get(dayIndex) != null && available.get(dayIndex) != 0
                                        && !(availabilityCircularResolver.isHoliday(startDate, companyHolidays) != null || availabilityCircularResolver.isHoliday(startDate, employeeHolidays) != null)
                                        && !(availabilityCircularResolver.isLrDay(startDate, employeeLeaves))) {
                                    k++;
                                    Calendar nonDate = Calendar.getInstance();
                                    nonDate.setTime(startDate.getTime());
                                    ServerUtils.setBeginningOfTheDay(nonDate);
                                    availableDays.add(nonDate);
                                }
                                startDate.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            if (availableDays.isEmpty()) {//availableDays yo'q bolsa task end datega estimatelarni set qilishga kelishildi
                                Calendar nonDate = Calendar.getInstance();
                                nonDate.setTime(dueDate.getTime());
                                ServerUtils.setBeginningOfTheDay(nonDate);
                                availableDays.add(nonDate);
                            }

                            if (k == 0) {
                                k = 1;
                            }
                            int dailyLoad = (et.getEstimatedTime() != null ? et.getEstimatedTime() : 0) / k;
                            int dailyLoadQ = (et.getEstimatedTime() != null ? et.getEstimatedTime() : 0) % k;
                            et.setDailyLoad(dailyLoad);
                            //insert timeSheet data with daily estimated time
                            if (editTask.getCreatedFrom() == null || !EditTask.FROM_RESOURCE_UTIL.equals(editTask.getCreatedFrom())) {
                                if (/*dailyLoad != 0*/dailyLoad >= 0 && et.getObjectID() != null) {
                                    String from = null;
                                    if (editTask.isRecalculateResourceHours()) {
                                        from = AvailabilityCircularResolver.FROM_RESOURCE_UTIL;
                                        timeSheetManager.updateDailyEstimatedTimeByEmployeeTask(et);
                                        /*List<EdsTimeSheet> timeSheets = timeSheetManager.getTimeSheets(et);
                                        if (timeSheets != null && timeSheets.size() > 0) {
                                            for (EdsTimeSheet tsh : timeSheets) {
                                                tsh.setDailyEstimatedTime(null);
                                            }
                                        }*/
                                    }
                                    availabilityCircularResolver.createOrUpdateTimeSheetDataWithDailyEstimatedTime(edsEmployee, et, availableDays, dailyLoad, dailyLoadQ, from);
                                }
                            }
                        }
                    }
                }
            }
        });

        if (MultiTaskList.FROM_TODO_LIST.equals(editTask.getCreatedFrom()) || overalPermission.isEdit()) {
            if (numberData != null && numberData.getNumberString() != null && !"".equals(numberData.getNumberString().trim()) && taskManager.isTaskNumberExists(numberData.getNumberString(), editTask.getProjectId(), editTask.getObjectID())) {
                numberData = generateTaskNumber(editTask.getProjectId(), editTask.getStartDate(), null);
            }

            if (editTask.getProjectId() != null) {
                if (!oldProject.equals(newProject)) {
                    task.setProject(newProject);
                    // --------------------------------------------------- Task Rbac Entries -----------------------------------------------------------------------------
                    Integer projectID = task.getProject() != null ? task.getProject().getObjectID() : null;
                    taskRbacManager.updateTaskRbacEntries(task.getObjectID(), projectID);
                    // --------------------------------------------------- Task Assignees --------------------------------------------------------------------------------
                    boolean isManager = false;
                    if ((task.getCreator() != null && task.getCreator().equals(user)) || task.getProject().getManager().equals(user) ||
                            task.getProject().isUserBackupManager(user.getObjectID()) ||
                            user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsRole.ADMIN))) {
                        isManager = true;
                    }
                    List<Object[]> projectMembers = getProjectMembers(editTask.getProjectId(), user, isManager);
                    ArrayList<Integer> projectEmployees = new ArrayList<>();
                    for (Object[] item : projectMembers) {
                        EdsEmployee emp = (EdsEmployee) item[1];
                        if (emp != null) {
                            projectEmployees.add(emp.getObjectID());
                        }
                    }
                    Set<EdsEmployeeTask> assignments = task.getUnDeletedAssignments();
                    ArrayList<Integer> newTaskAssignees = new ArrayList<>();
                    HashMap<Integer, EdsEmployeeTask> hashMap = new HashMap<>();
                    for (EdsEmployeeTask employeeTask : assignments) {
                        newTaskAssignees.add(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID());
                        hashMap.put(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID(), employeeTask);
                    }
                    ArrayList<Integer> nonChangedAssignees = (ArrayList<Integer>) ServerUtils.intersect(projectEmployees, newTaskAssignees);
                    nonChangedAssignees.addAll(newTaskAssignees);
                    if (!nonChangedAssignees.isEmpty()) {
                        ArrayList<IdTime> taskAssignees = new ArrayList<>();
                        for (Integer employeeID : nonChangedAssignees) {
                            EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(employeeManager.get(employeeID), newProject);
                            if (projectEmployee != null) {
                                EdsEmployeeTask employeeTask = hashMap.get(projectEmployee.getEmployeeDepartment().getEmployee().getObjectID());
                                if (employeeTask != null) {
                                    taskAssignees.add(new IdTime(projectEmployee.getObjectID(), employeeTask.getEstimatedTime(), employeeTask.getPercent(),
                                            employeeTask.getStatus() != null ? employeeTask.getStatus().getObjectID() : null));
                                }
                            } else {
                                EdsEmployee edsEmployee = employeeManager.get(employeeID);
                                EdsProjectEmployee projEmployee = addMembers(newProject, edsEmployee);
                                IdTime idTime = new IdTime(projEmployee.getObjectID(), 0, Float.valueOf("0.0"));
                                taskAssignees.add(idTime);
                            }
                        }
                        updateTaskAssignees(task, taskAssignees.toArray(new IdTime[0]), user, false);
                    }
                    // ------------------------------------------------------- Task Documents --------------------------------------------------------------------------------
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
//                    filterParameter.setFolderType(F_TASK);
                    filterParameter.setCrmEntityId(task.getObjectID());
                    filterParameter.setTrashResource(false);
                    filterParameter.setOtherSharedResource(false);
                    filterParameter.setOtherResource(false);
                    filterParameter.setSharedResource(false);
                    try {
                        ListResult<FileResource> fileResourceListResult = documentsServiceLocal.listFile(filterParameter);
                        if (fileResourceListResult != null && !fileResourceListResult.getList().isEmpty()) {
                            for (FileResource file : fileResourceListResult.getList()) {
                                try {
                                    FolderResource folderResource = documentsServiceLocal.getFolderResource(F_TASK, task.getProject().getObjectID());
                                    documentsServiceLocal.moveFile(file.getObjectId(), folderResource.getObjectId());
                                } catch (InsufficientPermissionsException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();
                    }
                    // ------------------------------------------------------- Timesheet entries -----------------------------------------------------------------------------
                    timeSheetManager.updateTimeEntries(task.getObjectID(), task.getProject().getObjectID());
                    // ------------------------------------------------------- Task dependencies -----------------------------------------------------------------------------
                    taskManager.removeTaskPredecessors(editTask.getObjectID());
                    editTask.setParentWSItem(null);
                    taskManager.update(task);
                }
            }
            task.setVisibility(editTask.getVisibilityStatus());
            task.setNumber("".equals(numberData.getNumberString()) ? null : numberData.getNumberString());
            task.setIntNumber(numberData.getIntNumber());
            if (numberData.getSavedNumberFormula() != null && !"".equals(numberData.getSavedNumberFormula())) {
                task.setSavedNumberFormula(numberData.getSavedNumberFormula());
            } else {
                task.setSavedNumberFormula(("".equals(numberData.getFirstNumberString()) ? "null" : numberData.getFirstNumberString()) + SAV_NUM_DEL + (numberData.getIntNumber() == null || "".equals(numberData.getIntNumber()) ? "null" : numberData.getIntNumber()) + SAV_NUM_DEL + ("".equals(numberData.getLastNumberString()) ? "null" : numberData.getLastNumberString()));
            }
            task.setName(editTask.getName());
            task.setDescription(editTask.getDescription());
            task.setBillable(editTask.getBillable());
            if (task.getStartDate().getTime() != editTask.getStartDate().getTime() || task.getDueDate().getTime() != editTask.getDueDate().getTime()) {
                taskDateChanged = true;
            }
            task.setStartAndDueDates(editTask.getStartDate(), editTask.getDueDate());
            task.setAllDay(editTask.isAllDay());

            if (editTask.isWorkflowTask()) {
                task.setWorkflowID(editTask.getWorkflowRelationID());
                task.setWorkflowStartDate(editTask.getWorkflowStartDate());
                task.setWorkflowDueDate(editTask.getWorkflowDueDate());
                task.setWorkflowDueDateGranularity(editTask.getWorkflowDueDateGranularity());
                task.setWorkflowActionTimeBased(editTask.isWorkflowActionTimeBased());
                task.setWorkflowActionStartTime(editTask.isWorkflowActionTimeBased() ? editTask.getWorkflowActionStartTime() : null);
                task.setWorkflowActionStartTimeUnit(editTask.isWorkflowActionTimeBased() ? editTask.getWorkflowActionStartTimeUnit() : null);
                task.setWorkflowActionStartTimeGranularity(editTask.isWorkflowActionTimeBased() ? editTask.getWorkflowActionStartTimeGranularity() : null);
            }
            if (editTask.getPriorityId() != null) {
                task.setPriority(referenceManager.get(editTask.getPriorityId()));
            }
            if (editTask.getTypeId() != null) {
                task.setType(referenceManager.get(editTask.getTypeId()));
            }

            if (editTask.getParentWSItem() != null && editTask.getParentWSItem().getId() != null) {
                EdsWorkStream workstream = workStreamManager.get(editTask.getParentWSItem().getId());

                if (task.getParentWS() != null && !task.getParentWS().equals(workstream)) {
                    clearOldParentWSCalculatedItemsOfTask(task);
                } else if (task.getParentWS() == null || task.getParentWS().getObjectID() == null) {
                    task.setChangedCalculationFields(true);
                    task.setCalculated(false);
                }

                task.setParentWS(workstream);
            } else {
                //was parent WS is not null clear it
                if (task.getParentWS() != null) {
                    clearOldParentWSCalculatedItemsOfTask(task);
                }

                task.setParentWS(null);
            }
            Set<EdsTask> newPredecessors = new HashSet<>();
            if (editTask.getPredecessorTaskItems() != null) {
                for (TaskSelectItem item : editTask.getPredecessorTaskItems()) {
                    if (item != null) {
                        newPredecessors.add(taskManager.load(item.getId()));
                    }
                }
            }
            Set<EdsTask> newSuccessors = new HashSet<>();
            if (editTask.getSuccessorTaskItems() != null) {
                for (TaskSelectItem item : editTask.getSuccessorTaskItems()) {
                    if (item != null) {
                        newSuccessors.add(taskManager.load(item.getId()));
                    }
                }
            }
            refreshTaskDependencies(newPredecessors, task.getPredecessors());
            refreshTaskDependencies(newSuccessors, task.getSuccessors());

        }

        //EdsTaskCustomFields edsTaskCustomFields = createTaskCustomFields(editTask.getCustomFieldItems());
        //task.setTaskCustomFields(edsTaskCustomFields);

        //SAVE CUSTOM FIELDS
        task.setTaskCustomFields(createTaskCustomFields(task.getTaskCustomFields(), editTask.getCustomFieldItems()));

        log.info("Change status from update task. StatusID:" + editTask.getStatusId() + " --- TaskID: " + task.getObjectID() + " --- UserID: " + user.getObjectID() + " --- COMPANYID: " + user.getCompany().getObjectID());
        statusChange(editTask, task, user, employee, overalPermission, null);

        saveTaskReminder(task.getObjectID(), user.getCompany(), editTask.getReminders());

        //Calling new updateTask method which is acctually almost the same as update()
        // but just with jpaTemplate.flush() inside. (added by Anvar Akramov)
        if (user != null) {
            task.setUpdater(user);
        }
        task.setLastUpdateTime(new Date());
        taskManager.updateTask(task);
        employeeTaskManager.setEmployeeTasksModifiedDate(task, new Date()); //Used in Calendar Task
        taskRbacManager.addRbacEntries(task);
        EdsBusinessEvent taskUpdateEvent = baseEventPostProcessor.registerEvent(TaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);

        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_TASK);

        if (!oldPreds.containsAll(task.getPredecessors()) || !task.getPredecessors().containsAll(oldPreds)
                || !oldSuccs.containsAll(task.getSuccessors()) || !task.getSuccessors().containsAll(oldSuccs)) {
            taskPredecessorsOrSuccessorsChanged = true;
        }

        if (taskDateChanged || taskPredecessorsOrSuccessorsChanged) {
            shiftAllSuccessors(task, null, user, editTask.isDontKeepDelays(), 0, taskOldDueDate, availabilityCircularResolver.getUserTimeSlot(user));
            if (editTask.getProjectId() != null && oldProject != null && newProject != null && !oldProject.equals(newProject)) {
                task.setStartAndDueDates(editTask.getStartDate(), editTask.getDueDate());
            }
            //in case there is no ParentWS for task we need to find its predecessors or successors ParentWS
            Set<EdsWorkStream> workStreams = new HashSet<>();
            if (!task.getPredecessors().isEmpty()) {
                findPredecessorParentWS(task, workStreams);
            }
            if (!task.getSuccessors().isEmpty()) {
                findSuccessorParentWS(task, workStreams);
            }
            if (task.getParentWS() != null) {
                workStreams.add(task.getParentWS());
            }
            for (EdsWorkStream ws : workStreams) {
                updateWorkStreamDateRange(null, ws);
            }
            if (task.getParentWS() == null && workStreamBeforeEdit != null) {
                updateWorkStreamDateRange(null, workStreamBeforeEdit);
            }
        }
        if (task != null && task.getObjectID() != null && editTask.isRelationChanged()) {
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_TASK, task.getObjectID(), task.getName(), editTask.getRelations());
        }
        try {
            taskSolrComponent.deleteByTaskId(task.getObjectID());
            taskSolrComponent.index(task);
            taskUpdateEvent.setSolrIndexed(true);
        } catch (Exception e) {
            taskUpdateEvent.setSolrIndexed(false);
        }
        //after update task -> reindex task project solr
        //update project to solr
        try {
            projectSolrComponent.index(task.getProject());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

    }

    private void saveNewProjectEmployees(EditTask editTask) {
        IdTime[] newEmployees = editTask.getAssigneeItems();

        ArrayList<IdTime> newProjectEmployees = new ArrayList<>();
        if (newEmployees != null && newEmployees.length > 0 && editTask.getProjectId() != null) {
            EdsProject edsProject = projectManager.get(editTask.getProjectId());
            for (IdTime employeeIdTime : newEmployees) {
                EdsEmployee edsEmployee = employeeManager.get(employeeIdTime.getId());
                if (edsEmployee != null) {
                    EdsProjectEmployee edsProjectEmployee = projectEmployeeManager.getProjectEmployee(edsEmployee, edsProject);
                    if (edsProjectEmployee == null) {
                        edsProjectEmployee = addMembers(edsProject, edsEmployee);
                    }

                    IdTime idTime = new IdTime();
                    idTime.setId(edsProjectEmployee.getObjectID());
                    idTime.setTime(employeeIdTime.getTime());
                    if (employeeIdTime.getStatusId() == null) {
                        idTime.setStatusId(NOT_STARTED);
                    } else {
                        idTime.setStatusId(employeeIdTime.getStatusId());
                    }
                    idTime.setTime(employeeIdTime.getTime());
                    newProjectEmployees.add(idTime);
                }
            }
        }
        editTask.setAssigneeItems(newProjectEmployees.toArray(new IdTime[0]));
    }

    private void statusChange(EditTask editTask, final EdsTask task, final EdsUser user, EdsEmployee employee, TaskPermissionItem overalPermission, String note) {
        if (editTask.isUpdateAssignmentTaskStatus() && employee != null) {
            final EdsEmployeeTask eta = employeeTaskManager.getEmployeeRelatedTask(task, employee.getEmployee());
            if (eta != null) {
                if (overalPermission.isStatusEdit()) {
                    eta.enableTaskChangeListener(new EdsEmployeeTask.ChangeListener() {
                        public void onStatusChange(EdsReference value) {
                            baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, eta, user);
                            String TASK_STATUS = task.getTaskLastStatus();
                            EdsReference status = referenceManager.findReference(EdsTask.TASK_STATUS, TASK_STATUS);
                            task.setStatus(status);
                            if (status.equals(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED))) {
                                baseEventPostProcessor.registerEvent(StatusTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
                            }
                        }

                        public void onPercentChange(Float percent) {
                            Float average;
                            Float averageProjectTasks;
                            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                                average = eta.getTask().getTaskAveragePercentCompletedNewLogic();
                                averageProjectTasks = eta.getTask().getProject().getProjectTasksAveragePercentCompletedNewLogic();
                            } else {
                                average = eta.getTask().getTaskAveragePercentCompleted();
                                averageProjectTasks = eta.getTask().getProject().getProjectTasksAveragePercentCompleted();
                            }
                            eta.getTask().setPreviousPercent(eta.getTask().getPercent());
                            eta.getTask().setPercent(average);
                            task.getProject().setPercent(averageProjectTasks);

                        }
                    });
                    if (editTask.getStatusId() != null) {
                        EdsReference status = referenceManager.get(editTask.getStatusId());
                        eta.setStatus(status);
                        switch (status.getCode()) {
                            case EdsTask.COMPLETED -> eta.setCompletedDate(new Date());
                            case EdsTask.CLOSED -> eta.setClosedDate(new Date());
                            default -> eta.setCompletedDate(null);
                        }
                    }
                    EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                    if (settings == null || !settings.isAutomatic()) {//if settings is null then project percent calculation mode is manual by default
                        eta.setPercent(editTask.getPercent());
                    }
                }
            }
        } else {
            //Integer taskEditablePermission = getTaskEditablePermission(task.getObjectID());//todo DILSHOD. Create a new permission "Change Task Assignee Status"
            if (overalPermission.isAssigneeStatusEdit()/* && taskEditablePermission == EDIT*/) {

                List<EdsReference> taskStatuses = new ArrayList<>();

                if (editTask.getStatusId() != null) {
                    if (task.getStatus() != null && editTask.getStatusId().equals(task.getStatus().getObjectID())) {
                        if (editTask.getAssigneeItems() != null && editTask.getAssigneeItems().length > 0) {
                            updateTaskAssignees(task, editTask.getAssigneeItems(), user, false);
                            //update task daily load for the all undeleted task assignees
                            JSONArray jsArray = new JSONArray();
                            IdTime[] idTimes = editTask.getAssigneeItems();
                            for (int i = 0; i < idTimes.length; i++) {
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("id", idTimes[i].getId());
                                    obj.put("changeEstimate", idTimes[i].getChangeEstimateTime());
                                    obj.put("startResourceCalculateAssigneeFromToday", idTimes[i].getStartResourceCalculationForNewAssigneesFromToday() != null ? idTimes[1].getStartResourceCalculationForNewAssigneesFromToday() : false);
                                } catch (Exception e) {
                                    log.error("Failure in parsing JSON for postprocessor", e);
                                }
                                jsArray.add(i, obj);
                            }
                            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(TaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, task, user);
                            event.setCustomStringField(jsArray.toString());

                            IdTime[] newProjectEmployees = editTask.getAssigneeItems() != null ? editTask.getAssigneeItems().clone() : new IdTime[]{new IdTime(user.getObjectID(), 0)};
                            for (IdTime idTime : newProjectEmployees) {
                                taskStatuses.add(referenceManager.get(idTime.getStatusId()));
                            }

                            EdsReference cumulativeStatus = referenceManager.findReference(EdsTask.TASK_STATUS, task.getTaskLastStatus(task.getStatus(), taskStatuses));
                            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                            if (settings == null || settings.isAutomatic()) {
                                if (cumulativeStatus.getCode().equals(EdsTask.COMPLETED) && !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                                    task.setPercent(100f);
                                }
                            }
                            if (!ServerUtils.equalsReference(task.getStatus(), cumulativeStatus)) {
                                insertTaskStatusHistory(task, cumulativeStatus, user, note);
                            }
                            task.setStatus(cumulativeStatus);
                            task.setLastUpdateTime(new Date());
                            if (cumulativeStatus.equals(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED))/* && task.getSuccessors() != null && task.getSuccessors().size() > 0*/) {
                                baseEventPostProcessor.registerEvent(StatusTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
                            }
                        }

                    } else {

                        Set<EdsEmployeeTask> etList = task.getUnDeletedAssignments();
                        for (EdsEmployeeTask etTask : etList) {
                            etTask.enableTaskChangeListener(new EdsEmployeeTask.ChangeListener() {
                                public void onStatusChange(EdsReference value) {
                                    baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, etTask, user);
                                }

                                public void onPercentChange(Float percent) {
                                    Float average;
                                    Float averageProjectTasks;
                                    if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                                        average = task.getTaskAveragePercentCompletedNewLogic();
                                        averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompletedNewLogic();
                                    } else {
                                        average = task.getTaskAveragePercentCompleted();
                                        averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompleted();
                                    }
                                    task.setPreviousPercent(task.getPercent());
                                    task.setPercent(average);
                                    task.getProject().setPercent(averageProjectTasks);

                                }
                            });

                            EdsReference status = referenceManager.get(editTask.getStatusId());
                            etTask.setStatus(status);
                            switch (status.getCode()) {
                                case EdsTask.COMPLETED -> etTask.setCompletedDate(new Date());
                                case EdsTask.CLOSED -> etTask.setClosedDate(new Date());
                                default -> etTask.setCompletedDate(null);
                            }

                            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                            if (settings == null || !settings.isAutomatic()) {//if settings is null then project percent calculation mode is manual by default
                                etTask.setPercent(editTask.getPercent());
                            }
                        }

                        if (editTask.getStatusId() != null) {
                            taskStatuses.add(referenceManager.get(editTask.getStatusId()));
                        }
                        EdsReference cumulativeStatus;
                        if (editTask.isUpdateTaskStatusForAll()) {
                            cumulativeStatus = referenceManager.get(editTask.getStatusId());
                            if (cumulativeStatus.equals(EdsTask.COMPLETED) && !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                                task.setPercent(100f);
                            }
                        } else {
                            cumulativeStatus = referenceManager.findReference(EdsTask.TASK_STATUS, task.getTaskLastStatus(task.getStatus(), taskStatuses));
                        }
                        insertTaskStatusHistory(task, cumulativeStatus, user, note);
                        task.setStatus(cumulativeStatus);
                        task.setLastUpdateTime(new Date());
                        if (cumulativeStatus.equals(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED))/* && task.getSuccessors() != null && task.getSuccessors().size() > 0*/) {
                            baseEventPostProcessor.registerEvent(StatusTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
                        }
                    }
                }

            }

        }
        Set<EdsEmployeeTask> assignes = task.getUnDeletedAssignments();
        if (assignes.size() == 1) {
            for (EdsEmployeeTask employeeTask : assignes) {
                task.setStatus(employeeTask.getStatus());
                break;
            }
        }
    }

    private void insertTaskStatusHistory(EdsTask task, EdsReference newStatus, EdsUser user, String note) {
        EdsTaskStatusHistory statusHistory = new EdsTaskStatusHistory();
        statusHistory.setModifiedDate(new Date());
        statusHistory.setModifier(user.getEmployee());
        if (note != null) {
            statusHistory.setComment(note);
        }
        statusHistory.setStatus(newStatus);
        statusHistory.setTask(task);
        statusHistoryManager.create(statusHistory);
    }

    public void updateTasksStatus(HashSet<TaskListItem> tasks, SelectItem status) {
        for (TaskListItem task : tasks) {
            updateTaskStatus(task.getObjectID(), status.getId(), null);
        }
    }

    public void updateTaskStatus(Integer taskId, Integer statusId, String note) {
        EdsUser user = taskManager.getUser();
        EditTask task = new EditTask();

        EdsTask edsTask = taskManager.get(taskId);
        edsTask.clear();
        task.setStatusId(statusId);
        task.setPercent(edsTask.getPercent());

        List<EdsTaskRbac> entries = taskRbacManager.getEntriesForUserOrHisMemberGoups(edsTask, user, user.getMembershipGroups());
        if (entries == null || entries.isEmpty()) {
            return;
        }
        TaskPermissionItem overalPermission = getAggregatePermisionsAs(entries);

        if (getAggregatePermissions(entries).contains(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode())) {
            task.setUpdateTaskStatusForAll(true);
            task.setUpdateAssignmentTaskStatus(false);

        } else {
            task.setUpdateTaskStatusForAll(false);
            task.setUpdateAssignmentTaskStatus(true);

        }
        log.info("TaskListView. StatusID:" + statusId + " --- TaskID: " + taskId + " --- UserID: " + user.getObjectID() + " --- COMPANYID: " + user.getCompany().getObjectID());
        statusChange(task, edsTask, user, user.getEmployee(), overalPermission, note);
        EdsReference edsReference = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED);
        Integer objectId = edsReference != null ? edsReference.getObjectID() : null;
        if (task.getStatusId().equals(objectId) && !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
            task.setPercent(100f);
        }
//        taskManager.updateTask(edsTask);
        taskRbacManager.addRbacEntries(edsTask);
        EdsBusinessEvent taskUpdateEvent = baseEventPostProcessor.registerEvent(TaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsTask, user);

        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsTask, user);
        workflowEvent.setEntityType(RelationItem.TYPE_TASK);

        try {
            taskSolrComponent.index(edsTask);
            taskUpdateEvent.setSolrIndexed(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTasksPriority(HashSet<TaskListItem> tasks, SelectItem priority) {
        Iterator<TaskListItem> taskIterator = tasks.iterator();

        EdsUser user = taskManager.getUser();
        StringBuilder taskIds = new StringBuilder();

        while (taskIterator.hasNext()) {
            Integer objectID = taskIterator.next().getObjectID();
            EdsTask task = taskManager.get(objectID);
            List<String> taskPermissions = getAggregatePermissions(taskRbacManager.getEntriesForUserOrHisMemberGoups(task, user, user.getMembershipGroups()));
            if (!(taskPermissions.contains(TaskPermissionEnum.DELETE.getCode()) || (task.getCreator() != null && task.getCreator().getObjectID().equals(user.getObjectID())))) {
                continue;
            }

            if (!taskIds.toString().isEmpty()) {
                taskIds.append(", ");
            }
            taskIds.append(objectID);
        }

        if (!taskIds.toString().isEmpty()) {
            List<EdsTask> edsTask = taskManager.getTaskByIds(taskIds.toString());
            for (EdsTask task : edsTask) {   //?
                task.setPriority(referenceManager.get(priority.getId()));
                task.setLastUpdateTime(new Date());
            }

            try {
                taskSolrComponent.indexes(edsTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changeWorkstream(ArrayList<Integer> tasks, Integer workstreamID) {
        System.out.println("WorkstreamID = " + workstreamID);
        if (workstreamID == null) {
            return;
        }
        long begin = System.currentTimeMillis();
        List<EdsTask> taskList = taskManager.getTaskByIds(ServerUtils.getAsCommoDelimited(tasks, "0", ","));
        for (EdsTask edsTask : taskList) {
            changeWorkstream(edsTask, workstreamID);
        }
        try {
            taskSolrComponent.indexes(taskList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print("Time spent - " + (System.currentTimeMillis() - begin));
    }

    public void updateTasksStartDate(ArrayList<Integer> tasks, Date date) {

        long begin = System.currentTimeMillis();
        ArrayList<EdsTask> taskList = new ArrayList<>();
        EdsUser user = taskManager.getUser();

        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);

        for (Integer task : tasks) {
            EdsTask edsTask = taskManager.get(task);
            Date oldDueDate = edsTask.getDueDate();
            taskList.add(edsTask);
            edsTask.setDueDate(new Date(date.getTime() + (edsTask.getDueDate().getTime() - edsTask.getStartDate().getTime())));
            edsTask.setStartDate(date);

            long begin1 = System.currentTimeMillis();
            updateTaskDates(false, user, edsTask, oldDueDate);
            System.out.println("updateTaskDates - " + (System.currentTimeMillis() - begin1) + " " + edsTask.getName());
            long begin2 = System.currentTimeMillis();

            System.out.println("updateTaskDailyLoad - " + (System.currentTimeMillis() - begin2) + " " + edsTask.getName());
        }
        System.out.print("Time spent - " + (System.currentTimeMillis() - begin));
    }

    public ArrayList<String> updateTasksDueDate(ArrayList<Integer> taskIDs, Date date) {
        long begin = System.currentTimeMillis();
        EdsUser user = taskManager.getUser();
        ArrayList<String> unchangedTaskNames = new ArrayList<>();
        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);

        for (Integer task : taskIDs) {
            EdsTask edsTask = taskManager.get(task);
            Date oldDueDate = edsTask.getDueDate();
            if (date.after(edsTask.getStartDate())) {
                edsTask.setDueDate(date);
            } else {
                unchangedTaskNames.add(edsTask.getNumber() + " : " + edsTask.getName());
            }
            long begin1 = System.currentTimeMillis();
            updateTaskDates(false, user, edsTask, oldDueDate);
            System.out.println("updateTaskDates - " + (System.currentTimeMillis() - begin1) + " " + edsTask.getName());
            long begin2 = System.currentTimeMillis();

            System.out.println("updateTaskDailyLoad - " + (System.currentTimeMillis() - begin2) + " " + edsTask.getName());
        }
        System.out.print("Time spent - " + (System.currentTimeMillis() - begin));
        return unchangedTaskNames;

    }

    @Override
    public void setTaskBillable(ArrayList<Integer> tasks, boolean b) {
        long begin = System.currentTimeMillis();
        ArrayList<EdsTask> taskList = new ArrayList<>();
        for (Integer task : tasks) {
            EdsTask edsTask = taskManager.get(task);
            taskList.add(edsTask);
            edsTask.setBillable(b);
        }
        System.out.print("Time spent - " + (System.currentTimeMillis() - begin));
    }

    @Override
    public void updateTasksProject(ArrayList<Integer> taskIds, Integer projectId) throws NumberExistingException {
        for (Integer taskId : taskIds) {
            EdsTask edsTask = taskManager.get(taskId);

            EditTask editTask = new EditTask();
            editTask.setObjectID(edsTask.getObjectID());
            editTask.setProjectId(projectId);
            editTask.setNumber(edsTask.getNumber());

            NumberData numberData = generateTaskNumber(edsTask.getProject().getObjectID(), edsTask.getStartDate(), null);
            numberData.setNumberString(edsTask.getNumber());
            numberData.setIntNumber(edsTask.getIntNumber());
            editTask.setNumberData(numberData);

            editTask.setName(edsTask.getName());
            editTask.setDescription(edsTask.getDescription());
            editTask.setPercent(edsTask.getPercent());
            editTask.setBillable(edsTask.getBillable());
            editTask.setStartDate(edsTask.getStartDate());
            editTask.setDueDate(edsTask.getDueDate());
            editTask.setAllDay(edsTask.isAllDay());
            editTask.setVisibilityStatus(edsTask.getVisibility());

            updateTask(editTask);
        }
    }

    @Override
    public NumberData generateWorkstreamNumber(Integer projectID, Date startdate, Integer objectID) {
        if (startdate != null) {
            startdate = userManager.getUser().getUserDate(startdate);
        }
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        boolean isUnique = false;
        if (settings != null && settings.getWorkstreamNumberingFormat() != null) {
            isUnique = settings.isUniqueNumber(settings.getWorkstreamNumberingFormat(), WIDGET_DATE_YEAR, WIDGET_UNIQUE_NUMBER_ALL_PROJECT);
        }
        Integer intNumber = workStreamManager.getWorkSreamLastIntNumber(projectID, isUnique);
        String pojectNumber = "";
        Integer clientId = null;
        if (projectID != null) {
            Object[] tt = projectManager.getProjectNumberById(projectID).get(0);
            pojectNumber = tt[0] != null ? tt[0].toString() : "";
            clientId = tt[1] != null ? (Integer) tt[1] : null;
        }
        String clientCode = null;
        if (clientId != null) {
            clientCode = crmAccountManager.getCrmAccountNumberById(clientId).get(0);
        }
        String savedNumberFormat;
        if (settings != null && settings.getWorkstreamNumberingFormat() != null) {
            if (objectID != null) {
                savedNumberFormat = workStreamManager.getSavedNumberformat(objectID);
                return settings.parsNumberDataForEdit(intNumber, savedNumberFormat, settings.getWorkstreamNumberingFormat());
            }
            return settings.parseNumberDataForALL(intNumber, settings.getWorkstreamNumberingFormat(), settings.getDelimetrWorkstream(), startdate, clientCode, pojectNumber, "workstream");
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_WORKSTREAM_PREFIX/*false*/);
        }
    }

    @Override
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getTaskMembersWithTreeInfo(Integer taskID) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> projectEmployeesMap = new LinkedHashMap<>();
        EdsTask task = taskManager.get(taskID);

        if (task == null) {
            return null;
        }
        EdsProject taskProject = task.getProject();
        if (taskProject == null) {
            return null;
        }
        EdsUser user = userManager.getUser();
        boolean isManager = false;
        Integer projectId = task.getProject().getObjectID();
        if ((task.getCreator() != null && task.getCreator().equals(user)) ||
                task.getProject().getManager().equals(user) ||
                task.getProject().isUserBackupManager(user.getObjectID()) ||
                user.hasEitherRoles(EdsRole.ADMIN, EdsRole.PM, EdsRole.TL, EdsRole.DR)) {
            isManager = true;
        }
        List<Object[]> projectMembers = taskID != null ? getProjectMembers(projectId, user, isManager) : getProjectMembers(projectId, user, PermissionConstants.PM_ASSIGN_ISSUE_TO_MEMBER);
        KpiTreeInfo sItem;
        boolean team;
        if (projectMembers != null && !projectMembers.isEmpty()) {
            for (Object[] item : projectMembers) {
                EdsProjectEmployee proEmployee = (EdsProjectEmployee) item[0];
                EdsEmployee employee = (EdsEmployee) item[1];
                EdsDepartment edsDepartment = (EdsDepartment) item[2];
                team = false;
                sItem = new KpiTreeInfo();
                if (employee != null) {
                    if (user.equals(employee)) {
                        sItem.setMyself(true);
                    } else {
                        sItem.setMyself(false);
                    }
                    sItem.setId(proEmployee.getObjectID());
                    sItem.setStatusId(Constants.NOT_STARTED);

                    EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeTask(taskID, employee.getObjectID(), true);
                    if (employeeTask != null) {
                        sItem.setStatusId(employeeTask.getStatus() != null ? employeeTask.getStatus().getObjectID() : null);
                        sItem.setTime(employeeTask.getEstimatedTime());
                        sItem.setPercent(employeeTask.getPercent());
                        sItem.setActualTime(employeeTask.getTimeSpent());//for API
                        if (employee.getPosition() != null) {
                            sItem.setPositionName(employee.getPosition().getName());
                        }
                        sItem.setSelected(Boolean.TRUE);
                    }
                    if (sItem.isMyself()) {
                        sItem.setName(employee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
                    } else {
                        sItem.setName(employee.getName());
                    }
                    sItem.setEmployeeId(employee.getObjectID());
                    if (edsDepartment != null) {
                        sItem.setDepartmentId(edsDepartment.getObjectID());
                        sItem.setDepartmentName(edsDepartment.getName());
                        for (KpiTreeInfo s : projectEmployeesMap.keySet()) {
                            if (s.getId().equals(edsDepartment.getObjectID())) {
                                team = true;
                                projectEmployeesMap.get(s).add(sItem);
                                break;
                            }
                        }
                        if (!team) {
                            KpiTreeInfo department = new KpiTreeInfo(edsDepartment.getObjectID(), edsDepartment.getName());
                            ArrayList<KpiTreeInfo> list = new ArrayList<>();
                            list.add(sItem);
                            projectEmployeesMap.put(department, list);
                        }
                    }
                }
            }
        }
        return projectEmployeesMap;
    }

    private void updateTaskDependencies(TaskSingleItem taskItem, EdsTask task) {
        if (taskItem.getPredecessorTasks() != null) {
            ArrayList<SelectItem> newPredecessors = new ArrayList<>();
            Collections.addAll(newPredecessors, taskItem.getPredecessorTasks());
            ArrayList<SelectItem> oldPredecessors = new ArrayList<>();
            Collections.addAll(newPredecessors, wrapTaskListToSelectItem(task.getPredecessors()));
            ServerUtils.intersect(newPredecessors, oldPredecessors);
            task.getPredecessors().addAll(wrapSelectItemsToTaskList(newPredecessors.toArray(new SelectItem[]{})));
        }
        if (taskItem.getSuccessorTasks() != null) {
            ArrayList<SelectItem> newSuccessors = new ArrayList<>();
            Collections.addAll(newSuccessors, taskItem.getSuccessorTasks());
            ArrayList<SelectItem> oldSuccessors = new ArrayList<>();
            Collections.addAll(newSuccessors, wrapTaskListToSelectItem(task.getSuccessors()));
            ServerUtils.intersect(newSuccessors, oldSuccessors);
            task.getSuccessors().addAll(wrapSelectItemsToTaskList(newSuccessors.toArray(new SelectItem[]{})));
        }
        task.setLastUpdateTime(new Date());
    }

    //    @Deprecated
    public void refreshTaskDependencies(Set<EdsTask> newTasks, Set<EdsTask> currentTasks) {
        currentTasks.addAll(newTasks);
        List<EdsTask> tasksForRemove = new LinkedList<>();
        for (EdsTask currentTask : currentTasks) {
            boolean i = false;
            for (EdsTask newTask : newTasks) {
                if (newTask.getObjectID().equals(currentTask.getObjectID())) {
                    i = true;
                    break;
                }
            }
            if (!i) {
                tasksForRemove.add(currentTask);
            }
        }
        tasksForRemove.forEach(currentTasks::remove);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaskSingleItem getTask(Integer objectId, Boolean isFromCrm) {
        EdsUser user = userManager.getUser();
        TaskSingleItem taskItem = new TaskSingleItem();
        EdsTask task = taskManager.get(objectId);
        taskItem.setSupplier(user.hasRole(SUPPLIER));

        if (task != null && !task.getDeleted()) {
            HashMap<Integer, Double> waitingTimeMap = timesheetManager.getTaskTimeSpents(task.getObjectID().toString(), EdsTimeSheet._WAITING);
            HashMap<Integer, Double> rejectedTimeMap = timesheetManager.getTaskTimeSpents(task.getObjectID().toString(), EdsTimeSheet._REJECT);
            Double[] taskCostAndTime = timeSheetManager.getTaskCostAndTimeSpent(objectId);
            if ((task.getCreator() != null && task.getCreator().equals(user)) || (user.hasEitherRoles(EdsRole.ADMIN, EdsRole.PM, EdsRole.TL, EdsRole.DR))) {
                taskItem.setPermission(EDIT);
            } else if ((task.getProject() != null && task.getProject().getManager() != null &&
                    task.getProject().getManager().equals(user)) || (task.getProject() != null &&
                    task.getProject().isUserBackupManager(user.getObjectID()))) {
                taskItem.setPermission(EDIT);
            } else {
                taskItem.setPermission(READ);
            }

            taskItem.setObjectID(task.getObjectID());
            if (task.getNumber() != null) {
                NumberData numberData = new NumberData();
                numberData.setNumberString(task.getNumber());
                numberData.setIntNumber(task.getIntNumber());
                taskItem.setNumberData(numberData);
            }

            if (isFromCrm != null) {
                taskItem.setTaskPermissions(getPermissions(task.getObjectID(), isFromCrm ? PermissionConstants.CRM_CONTEXT : PermissionConstants.PM_CONTEXT));
            }
            taskItem.setBillable(task.getBillable() != null ? task.getBillable() : false);
            taskItem.setEncryptedID(EncryptionHelper.encryptURL("task/" + task.getObjectID().toString()));
            taskItem.setName(task.getName());
            taskItem.setWorkstreamID(task.getParentWS() != null ? task.getParentWS().getObjectID() : Integer.valueOf(0));
            if (task.getParentWS() != null) {
                taskItem.setWorkstreamName(task.getParentWS().getName());
            }
            taskItem.setDescription(task.getDescription());
            taskItem.setPercent((task.getPercent() > 100f && !genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) ? 100f : task.getPercent());
            taskItem.setProjectID(task.getProject() != null ? task.getProject()
                    .getObjectID() : Integer.valueOf(0));
            taskItem.setProjectName(task.getProject() != null ? task
                    .getProject().getName() : referenceWfmMessageSource.localize("NA", "N/A"));
            taskItem.setProjectStatusCode((task.getProject() != null && task.getProject().getStatus() != null) ? task
                    .getProject().getStatus().getCode() : null);
            taskItem.setClientName(task.getProject() != null && task.getProject().getClient() != null ? task
                    .getProject().getClient().getName() : referenceWfmMessageSource.localize("NA", "N/A"));
            if (task.getType() != null) {
                taskItem.setTypeID(task.getType().getObjectID());
                taskItem.setTypeCode(task.getType().getCode());
                taskItem.setTypeName(task.getType().getName());
            }
            taskItem.setPriorityID(task.getPriority() != null ? task.getPriority().getObjectID() : null);
            taskItem.setPriorityName(task.getPriority() != null ? referenceWfmMessageSource.localize(task.getPriority().getCode(), task.getPriority().getName()) : referenceWfmMessageSource.localize("NA", "N/A"));
            taskItem.setPriorityCode(task.getPriority() != null ? task.getPriority().getCode() : null);
            taskItem.setStatusID(task.getStatus() != null ? task.getStatus().getObjectID() : null);
            taskItem.setStatusName(task.getStatus() != null ? referenceWfmMessageSource.localize(task.getStatus().getCode(), task.getStatus().getName()) : referenceWfmMessageSource.localize("NA", "N/A"));
            if (task.getStatus() != null) {
                taskItem.setStatus(Collections.singletonList(new SelectItem(task.getStatus().getObjectID(), task.getName(), task.getStatus().getCode())).toArray(new SelectItem[]{}));
            }
            taskItem.setStatusColor(task.getStatus() != null ? task.getStatus().getColor() : "");
            taskItem.setStartDate(task.getStartDate());
            taskItem.setEndDate(task.getDueDate());
            taskItem.setActualStartDate(task.getActualStartDate());
            taskItem.setActualEndDate(task.getActualEndDate());
            taskItem.setEstimatedTime(task.getEstimatedTime() != null ? task.getEstimatedTime() : 0);
            taskItem.setActualTime(taskCostAndTime != null ? (taskCostAndTime[TASK_ACTUAL_TIME_SPENT] != null ? taskCostAndTime[TASK_ACTUAL_TIME_SPENT].intValue() : 0) : 0);
            taskItem.setTimeSpent(taskCostAndTime != null ? (taskCostAndTime[TASK_HOUR_SPENT] != null ? taskCostAndTime[TASK_HOUR_SPENT].intValue() : 0) : 0);
            DecimalFormat df = new DecimalFormat("0.00");
            taskItem.setEstimatedCost(taskCostAndTime != null ? (taskCostAndTime[PROJECT_ESTIMATED_COST] != null ? df.format(taskCostAndTime[PROJECT_ESTIMATED_COST]) : "0.00") : "0.00");
            taskItem.setActualCost(taskCostAndTime != null ? (taskCostAndTime[PROJECT_ACTUAL_COST] != null ? df.format(taskCostAndTime[PROJECT_ACTUAL_COST]) : "0.00") : "0.00");
            taskItem.setLastModified(task.getLastUpdateTime() != null ? new Date(
                    task.getLastUpdateTime().getTime()) : null);
            taskItem.setLastModifiedBy(task.getUpdater() != null ? task.getUpdater().getFullName() : referenceWfmMessageSource.localize("NA", "N/A"));
            taskItem.setDueDate(task.getDueDate() != null ? new Date(task.getDueDate().getTime()) : null);
            taskItem.setManager(user.equals(task.getProject().getManager())
                    || task.getProject().isUserBackupManager(user.getObjectID()));
            taskItem.setAllDay(task.isAllDay());
            taskItem.setWaitingHours(waitingTimeMap.get(task.getObjectID()) != null ? ServerUtils.getTimeSpentHM(waitingTimeMap.get(task.getObjectID()).intValue()) : "00:00");
            taskItem.setRejectedHours(rejectedTimeMap.get(task.getObjectID()) != null ? ServerUtils.getTimeSpentHM(rejectedTimeMap.get(task.getObjectID()).intValue()) : "00:00");
            if (task.getProject().getManager() != null) {
                taskItem.setProjectManager(task.getProject().getManager().getFullName());
                taskItem.setProjectManagerID(task.getProject().getManager().getObjectID());
            }
            if (task.getProject().getBackupManager() != null) {
                taskItem.setProjectBackupManager(task.getProject().getBackupManager().getFullName());
                taskItem.setProjectBackupManagerID(task.getProject().getBackupManager().getObjectID());
            }
            ArrayList<SelectItem> backupMangers = new ArrayList<>();
            task.getProject().getBackupManagers().forEach(bm -> backupMangers.add(new SelectItem(bm.getObjectID(), bm.getName())));
            backupMangers.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
            taskItem.setBackupManagers(backupMangers);

            if (task.getCreator() != null) {
                taskItem.setTaskCreatorID(task.getCreator().getObjectID());
                taskItem.setTaskCreator(task.getCreator().getFullName());
            } else {
                taskItem.setTaskCreator(referenceWfmMessageSource.localize("NA", "N/A"));
            }
            taskItem.setTaskCreationTime(task.getCreationTime());
            TaskInvolvedMember[] members = getAssignments(task.getObjectID());
            int i;
            boolean userIsAssigned = false;
            if (members.length > 0) {
                EdsReference edsReference = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS);
                Integer inProgressStatus = edsReference != null ? edsReference.getObjectID() : null;
                for (i = 0; i < members.length; i++) {
                    if (members[i].getStatusID().equals(inProgressStatus)) {
                        String emplDep = timeSheetManager.getEmployeeLastDepartment(members[i].getEmployeeID());
                        members[i].setEmployeeTeam(emplDep != null ? emplDep : referenceWfmMessageSource.localize("NA", "N/A"));
                    }
                    if (user.getObjectID().equals(members[i].getExactEmployeeID())) {
                        userIsAssigned = true;
                    }
                }
                taskItem.setInvolvedMembers(members);
            } else {
                taskItem.setInvolvedMembers(new TaskInvolvedMember[0]);
            }
            //            For assignee widget
            if (members.length > 0) {
                boolean sEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED);
                PositionsSelectItem[] taskEmployees = new PositionsSelectItem[members.length];
                int j = 0;
                EdsNumberingSettings numberingSetting = numberingSettingsManager.getNumberingSetting();
                boolean autoPercentage = numberingSetting != null && numberingSetting.isAutomatic();
                Integer viewAsFilter = ServerUtils.getMaxRoleID(user.getRolesAsIntegersString());
                for (TaskInvolvedMember member : members) {
                    taskEmployees[j] = new PositionsSelectItem();
                    taskEmployees[j].setId(member.getEmployeeID());
                    taskEmployees[j].setName(member.getEmployee());
                    taskEmployees[j].setDepartmentId(member.getEmployeeTeamID());
                    taskEmployees[j].setDepartmentName(member.getEmployeeTeam());
                    taskEmployees[j].setStatusName(member.getStatusName());
                    taskEmployees[j].setStatusId(member.getStatusID());
                    taskEmployees[j].setEmployeeNumber(member.getEmployeeNumber());
                    taskEmployees[j].setExactEmployeeId(member.getExactEmployeeID());
                    taskEmployees[j].setEmployeeId(member.getAssignEmployeeID());
                    EdsEmployee edsEmployee = employeeManager.get(member.getAssignEmployeeID());
                    if (edsEmployee != null) {
                        taskEmployees[j].setPositionName(edsEmployee.getPosition() != null ? edsEmployee.getPosition().getName() : "");
                    }

                    if (!CLIENT.equals(viewAsFilter) || user.getObjectID().equals(member.getAssignEmployeeID())) {
                        taskEmployees[j].setActualTime(member.getActualTime());
                        float p = 0.0f;
                        if (autoPercentage) {
                            if (member.getEstimatedTime() != null && member.getEstimatedTime() != 0 && member.getActualTime() != null) {
                                p = 100 * member.getActualTime().floatValue() / member.getEstimatedTime().floatValue();
                            }
                        } else {
                            p = member.getPercent();
                        }
                        taskEmployees[j].setPercent((p > 100f && !sEnabled) ? 100f : p);
                        taskEmployees[j].setTimeSpent(member.getTimeSpent());
                        taskEmployees[j].setTime(member.getEstimatedTime());
                    }
                    j++;
                }
                taskItem.setIssueEmployees(taskEmployees);
            }
            taskItem.setTaskStatuses(getEditTaskStatusDrop(task.getObjectID()));

            if (userIsAssigned && !(EdsTask.COMPLETED.equals(task.getStatus().getCode()) || EdsTask.CANCELLED.equals(task.getStatus().getCode()) || EdsTask.CLOSED.equals(task.getStatus().getCode()))) {
                taskItem.setShowTimer(true);
                taskItem.setShowLogTime(true);
                taskItem.setTimerIsStarted(clockManager.getActiveClockForCurrentUser(task.getObjectID(), PM_TASK, user.getObjectID()) != null);
                taskItem.setAtLeastOneTimerIsRunning(taskItem.isTimerIsStarted());
            }

            SelectItem[] predecessorTasks = new SelectItem[task.getPredecessors().size()];
            i = 0;
            for (EdsTask predecessor : task.getPredecessors()) {
                predecessorTasks[i] = new SelectItem(predecessor.getObjectID(), predecessor.getName());
                i++;
            }
            taskItem.setPredecessorTasks(predecessorTasks);

            SelectItem[] successorTasks = new SelectItem[task.getSuccessors().size()];
            i = 0;
            for (EdsTask successor : task.getSuccessors()) {
                successorTasks[i] = new SelectItem(successor.getObjectID(), successor.getName());
                i++;
            }
            taskItem.setSuccessorTasks(successorTasks);
            if (task.getRecurrenceID() != null) {
                EdsRecurrence recurrence = recurrenceManager.get(task.getRecurrenceID());
                if (recurrence != null) {
                    taskItem.setRecurrenceId(task.getRecurrenceID());
                    RecurrenceJobItem recurrenceJobItem = recurrence.createRecurrenceItem(RECURRING_TASK);
                    recurrenceJobItem.setEnabled(true);
                    taskItem.setRecurrenceJobItem(recurrenceJobItem);
                }
            }

            taskItem.setReminder(taskReminderManager.getReminders(objectId));
            taskItem.setPriority(getPriorities());
        } else {
            return null;
        }
        taskItem.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_TASK, task.getObjectID())));
        taskItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(), commonService.getCompanyCustomFields(ViewName.Task)));
        return taskItem;
    }

    @Override
    public ListResult<HistoryItem> getTaskUpdatesList(final ListingFilterParameter fp) {
        final ArrayList<HistoryItem> itemList = (ArrayList<HistoryItem>) this.taskChangesManager.changeList(fp);
        final Long totalCount = this.taskChangesManager.getChangesCount(fp);
        return new ListResult<>(itemList, totalCount != null ? totalCount.intValue() : 0);
    }

    /**
     * Method used for Task Edit
     * called first to filling fields in TaskEdit view
     *
     * @param objectId
     * @return EditTask
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EditTask getTaskForEdit(Integer objectId) {
        EdsUser user = employeeManager.getUser();
        EditTask taskItem = new EditTask();
        EdsTask task = taskManager.get(objectId);
        if (task.getStatus() != null) {
            taskItem.setStatusId(task.getStatus().getObjectID());
        }
        taskItem.setEstimatedTime(task.getEstimatedTime());
        taskItem.setPercent(task.getPercent());
        taskItem.setReminders(taskReminderManager.getReminders(objectId));
        taskItem.setProjects(getProjectItems());
        taskItem.setWorkflowID(task.getWorkflowID());
        taskItem.setWorkflowDueDate(task.getWorkflowDueDate());
        taskItem.setWorkflowStartDate(task.getWorkflowStartDate());
        taskItem.setWorkflowDueDateGranularity(task.getWorkflowDueDateGranularity());
        taskItem.setWorkflowActionTimeBased(task.isWorkflowActionTimeBased());
        taskItem.setWorkflowActionStartTime(task.getWorkflowActionStartTime());
        taskItem.setWorkflowActionStartTimeUnit(task.getWorkflowActionStartTimeUnit());
        taskItem.setWorkflowActionStartTimeGranularity(task.getWorkflowActionStartTimeGranularity());
        List<EdsTaskRbac> entries = taskRbacManager.getEntriesForUserOrHisMemberGoups(task, user, user.getMembershipGroups());
        if (entries == null || entries.isEmpty()) {
            taskItem.setPermissions(new ArrayList<>());
        } else {
            taskItem.setPermissions(getAggregatePermissions(entries));
        }

        boolean editPermission = ServerUtils.hasPermission(PermissionConstants.PM_TASKS_EDIT) || ServerUtils.hasPermission(PermissionConstants.CRM_TASKS_EDIT);

        boolean isCreator = false;
        if (editPermission) {
            taskItem.setPermission(EDIT);
        } else if (task.getProject().getManager().equals(user) || task.getProject().isUserBackupManager(user.getObjectID())) {
            taskItem.setPermission(EDIT);
        } else if (user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsRole.ADMIN)) || user.hasRole(roleManager.get(EdsRole.TL))) {
            taskItem.setPermission(EDIT);
        } else {
            if (task.getCreator() == null) {
                task.setCreator(user);
            }
            if (task.getCreator().equals(user)) {
                taskItem.setPermission(EDIT);
                isCreator = true;
            } else {
                taskItem.setPermission(READ);
            }
        }

        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Task);
        taskItem.setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(), customFieldsItems));
//        }
        Integer firstAssigneeID = null;
        for (EdsEmployeeTask et : task.getUnDeletedAssignments()) {
            EdsEmployee asignee = et.getProjectEmployee().getEmployeeDepartment().getEmployee();
            if (firstAssigneeID == null) firstAssigneeID = asignee.getObjectID();
            if (asignee.equals(user) && (taskItem.getPermission() == READ || (isCreator && taskItem.getPermission() == EDIT))) {
                taskItem.setEmployeeTaskID(et.getObjectID());
                taskItem.setStatusId(et.getStatus() != null ? et.getStatus().getObjectID() : null);
                taskItem.setEstimatedTime(et.getEstimatedTime());
                taskItem.setPercent((et.getPercent() != null && !et.getPercent().toString().equals("")) ? et.getPercent() : ((et.getTask() != null && et.getTask().getPercent() != null) ? et.getTask().getPercent() : 0));
            }
        }
        taskItem.setFirstAssigneeId(firstAssigneeID);

        taskItem.setObjectID(task.getObjectID());

        NumberData numberData = generateTaskNumber(task.getProject().getObjectID(), task.getStartDate(), objectId);
        numberData.setNumberString(task.getNumber());
        numberData.setIntNumber(task.getIntNumber());
        taskItem.setNumberData(numberData);
        taskItem.setNumber(numberData.getNumberString());

        taskItem.setName(task.getName());
        taskItem.setDescription(task.getDescription());
        taskItem.setBillable(task.getBillable());
        if (task.getProject() != null) {
            taskItem.setProjectId(task.getProject().getObjectID());
            taskItem.setProjectName(task.getProject().getName());
            taskItem.setProjectBillable(task.getProject().getBillable());
        }
        if (task.getType() != null) {
            taskItem.setTypeId(task.getType().getObjectID());
            taskItem.setTypeCode(task.getType().getCode());
            taskItem.setTypeName(task.getType().getName());
        }
        taskItem.setPriorityId(task.getPriority() != null ? task.getPriority().getObjectID() : null);
        taskItem.setStartDate(task.getStartDate() != null ? new Date(task.getStartDate().getTime()) : null);
        taskItem.setDueDate(task.getDueDate() != null ? new Date(task.getDueDate().getTime()) : null);
        taskItem.setEndDate(task.getDueDate() != null ? new Date(task.getDueDate().getTime()) : null);

        taskItem.setAllDay(task.isAllDay());

        if (task.getParentWS() != null) {
            WbsItem parentWS = new WbsItem();
            parentWS.setId(task.getParentWS().getObjectID());
            parentWS.setName(task.getParentWS().getName());
            taskItem.setParentWSItem(parentWS);
        }
        if (task.getPredecessors() != null && !task.getPredecessors().isEmpty()) {
            TaskSelectItem[] predTasks = new TaskSelectItem[task.getPredecessors().size()];
            int i = 0;
            for (EdsTask predTask : task.getPredecessors()) {
                predTasks[i] = new TaskSelectItem();
                predTasks[i].setId(predTask.getObjectID());
                predTasks[i].setTaskNumber(predTask.getNumber());
                predTasks[i].setName(predTask.getName());
                predTasks[i].setTaskStartDate(predTask.getStartDate());
                predTasks[i].setTaskDueDate(predTask.getDueDate());
                predTasks[i].setAllDay(predTask.isAllDay());
                predTasks[i].setProjectId(predTask.getProject() != null ? predTask.getProject().getObjectID() : null);
                i++;
            }
            taskItem.setPredecessorTaskItems(predTasks);
        }
        if (task.getSuccessors() != null && !task.getSuccessors().isEmpty()) {
            TaskSelectItem[] succTasks = new TaskSelectItem[task.getSuccessors().size()];
            int i = 0;
            for (EdsTask succTask : task.getSuccessors()) {
                succTasks[i] = new TaskSelectItem();
                succTasks[i].setId(succTask.getObjectID());
                succTasks[i].setTaskNumber(succTask.getNumber());
                succTasks[i].setName(succTask.getName());
                succTasks[i].setTaskStartDate(succTask.getStartDate());
                succTasks[i].setTaskDueDate(succTask.getDueDate());
                succTasks[i].setAllDay(succTask.isAllDay());
                succTasks[i].setProjectId(succTask.getProject() != null ? succTask.getProject().getObjectID() : null);
                i++;
            }
            taskItem.setSuccessorTaskItems(succTasks);
        }
        task.setLastUpdateTime(new Date());
        taskItem.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_TASK, taskItem.getObjectID())));
        return taskItem;
    }

    private SelectItem[] getProjectItems() {
        ProjectItem[] projectItems = commonService.getProjects(false);
        SelectItem[] castProjects = new SelectItem[projectItems.length];
        int i = 0;
        for (ProjectItem projectItem : projectItems) {
            castProjects[i] = new SelectItem(projectItem.getId(), projectItem.getName());
            castProjects[i].setDescription(projectItem.getDescription());
            castProjects[i].setSelected(projectItem.isSelected());
            i++;
        }
        return castProjects;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaskInvolvedMember[] getAssignments(Integer taskID) {
        if (taskID != null) {
            return timeSheetManager.getSumTimeSheets(taskID).toArray(new TaskInvolvedMember[]{});
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaskTimeEntriesItem[] getTaskTimeEntries(Integer taskID) {
        ArrayList<TaskTimeEntriesItem> items = new ArrayList<>();
        if (taskID == null) {
            return new TaskTimeEntriesItem[0];
        }
        EdsTask task = taskManager.get(taskID);
        Set<EdsEmployeeTask> assignees = task.getAssignments();
        for (EdsEmployeeTask employeeTask : assignees) {
            initEmplyeeTaskTimeEntries(employeeTask, items);
        }
        TaskTimeEntriesItem[] taskTimeEntriesItems = items.toArray(new TaskTimeEntriesItem[0]);
        Arrays.sort(taskTimeEntriesItems, (o1, o2) -> o2.getDate().getNonConvertedDate().compareTo(o1.getDate().getNonConvertedDate()));
        return taskTimeEntriesItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<TaskTimeEntriesItem> getTaskTimeEntriesList(ListingFilterParameter fp) {
        ArrayList<TaskTimeEntriesItem> items = new ArrayList<>();

        List<EdsTimeSheet> timesheets = timesheetManager.getTimesheetForTimeEntries(fp);
        int totalCount = timesheets.size();
        if (fp.getLimit() > 0) {
            timesheets = ListUtils.getSublist(timesheets, fp.getStart(), fp.getLimit());
        }
        for (EdsTimeSheet timesheet : timesheets) {
            if (timesheet.getTimeSpent() != null && timesheet.getTimeSpent() > 0) {
                TaskTimeEntriesItem entry = new TaskTimeEntriesItem();
                entry.setObjectID(timesheet.getObjectID());
                EdsEmployee employee = timesheet.getEmployeeTask().getProjectEmployee().getEmployeeDepartment().getEmployee();
                entry.setEmloyeeCode(employee.getProfile() != null ? employee.getProfile().getEmployeeCode() : "");
                entry.setEmloyee(employee.getFullName());
                entry.setEmployeeId(employee.getObjectID());
                entry.setComment(timesheet.getComment());
                entry.setManagerComment(timesheet.getManagerComment());
                entry.setTimeSpent(timesheet.getTimeSpent());
                String hourType = "-";
                if (timesheet.getType() != null) {
                    hourType = timesheet.getType().getName();
                }
                entry.setHourType(hourType);
                Date timesheetDate = timesheet.getDate();
                entry.setDate(new DateNonConvertable(timesheetDate));
                entry.setStatus(timesheet.getStatus() != null ? referenceWfmMessageSource.localizeRef(timesheet.getStatus()) : "");
                items.add(entry);
            }
        }

        return new ListResult<>(items, totalCount);
    }

    private void initEmplyeeTaskTimeEntries(EdsEmployeeTask employeeTask, ArrayList<TaskTimeEntriesItem> items) {
        List<EdsTimeSheet> timesheets = employeeTask.getTimeSheets();
        for (EdsTimeSheet timesheet : timesheets) {
            if (timesheet.getTimeSpent() != null && timesheet.getTimeSpent() > 0) {
                TaskTimeEntriesItem entry = new TaskTimeEntriesItem();
                entry.setObjectID(timesheet.getObjectID());
                entry.setEmloyee(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getFullName());
                entry.setEmployeeId(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID());
                entry.setComment(timesheet.getComment());
                entry.setManagerComment(timesheet.getManagerComment());
                if (timesheet.getTimeSpent() != null) {
                    entry.setTimeSpent(timesheet.getTimeSpent());
                } else {
                    entry.setTimeSpent(0);
                }
                String hourType = "-";
                if (timesheet.getType() != null) {
                    hourType = timesheet.getType().getName();
                }
                entry.setHourType(hourType);
                Date timesheetDate = timesheet.getDate();
                entry.setDate(new DateNonConvertable(timesheetDate));
                entry.setStatus(timesheet.getStatus() != null ? referenceWfmMessageSource.localizeRef(timesheet.getStatus()) : "");
                String taskName = "";
                if (employeeTask != null && employeeTask.getTask() != null && employeeTask.getTask().getName() != null) {
                    taskName = employeeTask.getTask().getName();
                }
                entry.setTaskName(taskName);
                items.add(entry);
            }
        }
    }

    public void moveTimeEntries(ArrayList<TaskTimeEntriesItem> selectedTimeEntry, Integer projectId, Integer taskId) {

        for (TaskTimeEntriesItem taskTimeEntriesItem : selectedTimeEntry) {
            Integer employeeId = taskTimeEntriesItem.getEmployeeId();
            EdsEmployee employee = employeeManager.get(employeeId);
            EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(employee, projectManager.get(projectId));

            EdsTask edsTask = taskManager.get(taskId);
            EdsEmployeeTask edsEmployeeTask = taskManager.getEmployeeTask(employeeId, taskId);

            if (edsEmployeeTask == null) {
                ArrayList<Integer> taskIDs = new ArrayList<>();
                taskIDs.add(taskId);
                ArrayList<IdTime> assignees = new ArrayList<>();
                IdTime newAssignee = new IdTime(employeeId, 0);
                assignees.add(newAssignee);
                addAssigneesToTask(taskIDs, assignees);
                edsEmployeeTask = taskManager.getEmployeeTask(employeeId, taskId);
            }

            Integer timesheetId = taskTimeEntriesItem.getObjectID();

            EdsTimeSheet edsTimeSheet = timeSheetManager.get(timesheetId);
            EdsTimeSheet existingTimesheet = timesheetManager.getTimeSheet(edsEmployeeTask, edsTimeSheet.getDate());

            if (existingTimesheet != null) {
                existingTimesheet.setTimeSpent((existingTimesheet.getTimeSpent() != null ? existingTimesheet.getTimeSpent() : 0) + edsTimeSheet.getTimeSpent());
                existingTimesheet.setProjectID(projectId);
                existingTimesheet.setTaskID(taskId);
                existingTimesheet.setComment(existingTimesheet.getComment() != null ? existingTimesheet.getComment() + ", " + taskTimeEntriesItem.getComment() : taskTimeEntriesItem.getComment());
                if (edsTimeSheet.getStatus() != null) {
                    timeSheetApprovalSessionManager.deleteTimesheetApprovalSession(edsTimeSheet.getObjectID());
                }
                timesheetManager.delete(edsTimeSheet);
                timesheetManager.update(existingTimesheet);
            } else {
                edsTimeSheet.setProjectID(projectId);
                edsTimeSheet.setTaskID(taskId);
                edsTimeSheet.setEmployeeTask(edsEmployeeTask);
                edsTimeSheet.setComment(taskTimeEntriesItem.getComment());
                timesheetManager.update(edsTimeSheet);
            }

            boolean updateTaskActualDates = false;

            if (edsTask.getActualEndDate() != null && edsTimeSheet.getDate() != null && edsTask.getActualEndDate().before(edsTimeSheet.getDate())) {
                updateTaskActualDates = true;
                edsTask.setActualEndDate(edsTimeSheet.getDate());
            }
            if (edsTask.getActualStartDate() != null && edsTimeSheet.getDate() != null && edsTask.getActualStartDate().after(edsTimeSheet.getDate())) {
                updateTaskActualDates = true;
                edsTask.setActualStartDate(edsTimeSheet.getDate());
            }
            if (updateTaskActualDates) {
                taskManager.updateTask(edsTask);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaskSelectItemList searchTasks(Integer projectId, String keyword,
                                          ListingFilterParameter fp, ListLoadConfig config, Integer[] taskIds, boolean includeParentExistSubtasks) {
        try {
            TaskSelectItemList result = new TaskSelectItemList();
            TaskSearchResult searchResult = taskManager.findByKeyword(
                    projectId, new String[]{"name", "name_partial"},
                    keyword, fp, config);
            List<Integer> taskIdList = new LinkedList<>();
            if (taskIds != null) {
                for (Integer taskId : taskIds) {
                    if (taskId != null) {
                        taskIdList.add(taskId);
                    }
                }
            }
            for (int i = 0; i < searchResult.getTasks().size(); i++) {
                if (searchResult.getTasks().get(i) != null && taskIdList.contains(searchResult.getTasks().get(i).getObjectID())) {
                    searchResult.getTasks().set(i, null);
                }
            }

            List<EdsTask> frTasks = new LinkedList<>();
            if (includeParentExistSubtasks) {
                for (EdsTask task : searchResult.getTasks()) {
                    if (task != null) {
                        frTasks.add(task);
                    }
                }
            } else {
                for (EdsTask task : searchResult.getTasks()) {
                    if (task != null && task.getParentWS() == null) {
                        frTasks.add(task);
                    }
                }
            }
            result.setResults(new ArrayList<>());
//            int i = 0;
            for (EdsTask task : frTasks) {
                result.getResults().add(task.createTaskSelectItem());
//                i++;
            }
            result.setTotalCount(frTasks.size());
            return result;
        } catch (EdsDbException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public TaskSelectItemList getLatestTasks(Integer projectId,
                                             ListLoadConfig config, Integer[] taskIds, boolean includeParentExistSubtasks) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProjectId(projectId);
        if (fp.getViewAsId() == null) {
            EdsRole maximumRole = taskManager.getUser().getRolesSortedByPattern().get(0);
            fp.setViewAsId(maximumRole.getObjectID());
        }
//        if (fp.getTaskStatusId() == null) {
//            fp.setTaskStatusId(ALL_DUE_TASKS);
//        }
        List<Integer> taskIdList = Arrays.asList(taskIds);
        fp.setObjectIDs((ArrayList<Integer>) taskIdList);
        List<EdsTask> tasks = taskManager.list(fp);

        /*for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i) != null && taskIdList.contains(tasks.get(i).getObjectID())) {
                tasks.remove(i);
            }
        }*/
        List<EdsTask> fTasks = new LinkedList<>();
        if (includeParentExistSubtasks) {
            for (EdsTask task : tasks) {
                if (task != null && task.getName() != null) {
                    fTasks.add(task);
                }
            }
        } else {
            for (EdsTask task : tasks) {
                if (task != null && task.getName() != null && task.getParentWS() == null) {
                    fTasks.add(task);
                }
            }
        }
        return createTaskSelectItemList(config, fTasks);
    }

    private TaskSelectItemList createTaskSelectItemList(ListLoadConfig config, List<EdsTask> tasks) {
        int totalCount = tasks.size();
        ArrayList<TaskSelectItem> result = new ArrayList<>();
        for (EdsTask task : tasks) {
            result.add(task.createTaskSelectItem());
        }
        TaskSelectItemList list = new TaskSelectItemList();
        list.setResults(result);
        list.setTotalCount(totalCount);
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileResource[] getTaskAttachments(EdsTask task) {
        List<FileResource> taskAttachments = attachmentUtilsManager.getAttachments(F_TASK, task.getProject().getObjectID(), task.getObjectID());
        return taskAttachments.toArray(new FileResource[0]);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileResource[] getTaskAttachments(Integer taskID) {
        EdsTask task = taskManager.get(taskID);
        if (task != null) {
            return getTaskAttachments(task);
        }
        return null;
    }

    public String setAssignees(Integer taskID, WfmTreeItem[] assignees) {
        try {
            EdsEmployee employee = (EdsEmployee) employeeManager.getUser();
            EdsTask task = taskManager.get(taskID);

            if (isThisTaskManager(employee, task)) {
                for (WfmTreeItem assignee : assignees) {
                    EdsProjectEmployee projectEmployee = projectEmployeeManager.get(assignee.getId());
                    EdsEmployeeTask assignment = new EdsEmployeeTask(task, projectEmployee);
                    assignment.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED));
                    task.getAssignments().add(assignment);
                }
                taskManager.update(task);
            } else {
                log.error("You are not Project Manager!");
                return "notprojmanager";
            }
        } catch (Throwable t) {
            log.error("Unexpected exception:", t);
            throw new RuntimeException(t);
        }
        return "success";
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAssignees(Integer projectId) {
        EdsUser user = employeeManager.getUser();
        List<Object[]> members = projectManager.getProjectEmployees(projectId);
        SelectItem[] items = new SelectItem[members.size()];
        int i = 0, myself = 0;
        if (user.isEmployee()) {
            EdsEmployee employee = user.getEmployee();
            for (Object[] item : members) {
                EdsProjectEmployee proEmployee = (EdsProjectEmployee) item[0];
                EdsEmployee edsEmployee = (EdsEmployee) item[1];
                if (!employee.equals(edsEmployee)) {
                    items[i] = new SelectItem(proEmployee.getObjectID(), edsEmployee.getName());
                } else {
                    items[i] = new SelectItem(proEmployee.getObjectID(), edsEmployee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
                    myself = i;
                }
                i++;
            }
        } else {
            for (Object[] item : members) {
                EdsProjectEmployee proEmployee = (EdsProjectEmployee) item[0];
                EdsEmployee employee = (EdsEmployee) item[1];
                items[i] = new SelectItem(proEmployee.getObjectID(), employee.getEmployee().getName());
                i++;
            }
        }
        //Now sets "Myself" as first element
        if (myself != 0) {
            SelectItem tmp = items[myself];
            items[myself] = items[0];
            items[0] = tmp;
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PositionsSelectItem[] getAssigneesWithPositions1(Integer projectId) {
        ExistingAndNewTaskMembers newMembers = getAssigneesWithPositions(null, projectId);
        PositionsSelectItem[] positions = newMembers.getNewMembers();
        if (positions == null) {
            positions = new PositionsSelectItem[0];
        }
        Arrays.sort(positions, getComparatorFactory().createComparator(1));
        if (positions.length > 0) {
            positions[0].setPermission(newMembers.getPermission());
        }
        return positions;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PositionsSelectItem[] getAssigneesWithPositionsForMobile(Integer projectId) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeList = getAssigneesWithTreeInfoLinkedHashMapWithParams(null, projectId, null, false);

        List<KpiTreeInfo> list = new ArrayList<>();
        for (ArrayList<KpiTreeInfo> items : assigneeList.values()) {
            list.addAll(items);
        }
        List<PositionsSelectItem> positions = new ArrayList<>();
        for (KpiTreeInfo item : list) {
            PositionsSelectItem positionsItem = new PositionsSelectItem();
            positionsItem.setId(item.getId());
            positionsItem.setMyself(item.isMyself());
            positionsItem.setEmployeeId(item.getEmployeeId());
            positionsItem.setName(item.getName());
            positionsItem.setDepartmentId(item.getDepartmentId());
            positionsItem.setDepartmentName(item.getDepartmentName());
            positionsItem.setPositionId(item.getPositionId());
            positionsItem.setPositionName(item.getPositionName());
            positions.add(positionsItem);
        }
        return positions.toArray(new PositionsSelectItem[0]);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<Integer, LinkedList<WfmTreeItem>> getProjectAssigneesWithPositions(Integer projectID) {
        HashMap<Integer, LinkedList<WfmTreeItem>> projectEmployeesWithClients = new HashMap<>();

        List<EdsProjectEmployee> projectEmployees = projectManager.getEmployeesByProject(projectID);

        LinkedList<WfmTreeItem> projectEmployeeItems = new LinkedList<>();
        for (EdsProjectEmployee projectEmployee : projectEmployees) {
            if (projectEmployee.getEmployeeDepartment() != null) {
                EdsEmployee employee = projectEmployee.getEmployeeDepartment().getEmployee();
                if (employee != null) {
                    WfmTreeItem member = new WfmTreeItem();
                    member.setId(projectEmployee.getObjectID());
                    member.setName(employee.getName());
                    member.setChecked(true);
                    member.setDescription(IS_EMPLOYEE);
                    projectEmployeeItems.add(member);
                }
            }
        }

        LinkedList<WfmTreeItem> projectClientContactItems = getProjectClientContacts(projectID);
        projectEmployeesWithClients.put(0, projectEmployeeItems);
        projectEmployeesWithClients.put(1, projectClientContactItems);

        return projectEmployeesWithClients;
    }

    private LinkedList<WfmTreeItem> getProjectClientContacts(Integer projectID) {
        LinkedList<WfmTreeItem> projectClientContactItems = new LinkedList<>();
        EdsProject project = projectManager.get(projectID);
        if (project != null && project.getClient() != null) { //for project clients
            List<EdsClientContact> clientContactList = clientContactManager.getAccessEnabledContacts(project.getClient());
            if (clientContactList != null && !clientContactList.isEmpty()) {
                for (EdsClientContact clientContact : clientContactList) {
                    WfmTreeItem cContact = new WfmTreeItem();
                    cContact.setId(clientContact.getObjectID());
                    cContact.setName(clientContact.getName());
                    cContact.setChecked(true);
                    cContact.setDescription(IS_CLIENT);
                    projectClientContactItems.add(cContact);
                }
            }
        }
        return projectClientContactItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<Integer, LinkedList<WfmTreeItem>> getTaskMembers(Integer taskID) {
        HashMap<Integer, LinkedList<WfmTreeItem>> projectEmployeesWithClients = new HashMap<>();

        ExistingAndNewTaskMembers existUsers = getAssigneesWithPositions(taskID, null);
        PositionsSelectItem[] results = existUsers.getExistingMembers();
        LinkedList<WfmTreeItem> taskMembersItems = new LinkedList<>();
        for (PositionsSelectItem result : results) {
            WfmTreeItem member = new WfmTreeItem();
            member.setId(result.getId());
            member.setName(result.getName());
            member.setChecked(true);
            member.setDescription(IS_EMPLOYEE);//cha
            taskMembersItems.add(member);
        }

        EdsTask task = taskManager.get(taskID);
        LinkedList<WfmTreeItem> projectClientContactItems = getProjectClientContacts(task.getProject().getObjectID());

        projectEmployeesWithClients.put(0, taskMembersItems);
        projectEmployeesWithClients.put(1, projectClientContactItems);
        return projectEmployeesWithClients;
    }

    /**
     * the purpose is to get only available employees of the project within the given period and sort members in alphabetical order
     * this method is used in Calendar Add Task therefore it is replicated in GoogleCalendarServiceImpl
     *
     * @param projectId
     * @param startDate
     * @param endDate
     * @return
     * @see com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceImpl#getOnlyAvailableAssigneesWithPosition1(Integer, java.util.Date, java.util.Date)
     * the method is also used in Project Management Add Task (AddTaskView)
     * @see com.edatasite.workforce.gwt.task.client.ui.AddTaskView#-reloadOnlyAvailableAssignees()
     */
    @Deprecated
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PositionsSelectItem[] getOnlyAvailableAssigneesWithPosition1(Integer projectId, Date startDate, Date endDate) {
        PositionsSelectItem[] newMembers = getOnlyAvailableEmployees(projectId, startDate, endDate);
        Arrays.sort(newMembers, getComparatorFactory().createComparator(1));
        return newMembers;
    }

    private ComparatorFactory<PositionsSelectItem> getComparatorFactory() {
        return sortOrder -> new AbstractComparator<PositionsSelectItem>() {
            public int compare(PositionsSelectItem o1, PositionsSelectItem o2) {
                return internalCompare(o1.getName() != null ? o1.getName() : "", o2.getName() != null ? o2.getName() : "", sortOrder);
            }
        };
    }

    private ComparatorFactory<PositionsSelectItem> getComparatorFactoryForDepartment() {
        return sortOrder -> new AbstractComparator<PositionsSelectItem>() {
            public int compare(PositionsSelectItem o1, PositionsSelectItem o2) {
                return internalCompare(o1.getDepartmentName() != null ? o1.getDepartmentName() : "", o2.getDepartmentName() != null ? o2.getDepartmentName() : "", sortOrder);
            }
        };
    }

    /**
     * the purpose is to get only available employees of the project within the given period
     *
     * @param projectId
     * @param startDate
     * @param endDate
     * @return
     * @see com.edatasite.workforce.gwt.task.server.app.TaskServiceImpl#
     */
    @Deprecated
    private PositionsSelectItem[] getOnlyAvailableEmployees(Integer projectId, Date startDate, Date endDate) {
        EdsUser user = employeeManager.getUser();

        Map<Integer, EdsProjectEmployee> projectMembers = new HashMap<>();
        List<Object[]> projectMembersTemp;
        if (user.hasRole(roleManager.get(EdsRole.DR))) {
            projectMembersTemp = projectManager.getProjectEmployees(projectId, EdsRole.DR);
            for (Object[] item : projectMembersTemp) {
                EdsProjectEmployee pe = (EdsProjectEmployee) item[0];
                EdsEmployee emp = (EdsEmployee) item[1];
                if (emp != null) {
                    projectMembers.put(emp.getObjectID(), pe);
                }
            }
        } else if (user.hasRole(roleManager.get(EdsRole.ADMIN))) {
            projectMembersTemp = projectManager.getProjectEmployees(projectId, EdsRole.ADMIN);
            for (Object[] item : projectMembersTemp) {
                EdsProjectEmployee pe = (EdsProjectEmployee) item[0];
                EdsEmployee employee = (EdsEmployee) item[1];
                projectMembers.put(employee.getObjectID(), pe);
            }
        } else if (user.hasRole(roleManager.get(EdsRole.PM))) {
            projectMembersTemp = projectManager.getProjectEmployees(projectId, EdsRole.PM);
            for (Object[] item : projectMembersTemp) {
                EdsProjectEmployee pe = (EdsProjectEmployee) item[0];
                EdsEmployee employee = (EdsEmployee) item[1];
                projectMembers.put(employee.getObjectID(), pe);
            }
        } else if (user.hasRole(roleManager.get(EdsRole.MEM))) {
            projectMembersTemp = projectManager.getProjectEmployees(projectId, EdsRole.MEM);
            for (Object[] item : projectMembersTemp) {
                EdsProjectEmployee pe = (EdsProjectEmployee) item[0];
                EdsEmployee employee = (EdsEmployee) item[1];
                projectMembers.put(employee.getObjectID(), pe);
            }
        } else if (user.hasRole(roleManager.get(EdsRole.CLIENT))) {
            projectMembersTemp = projectManager.getProjectEmployees(projectId, EdsRole.CLIENT);
            for (Object[] item : projectMembersTemp) {
                EdsProjectEmployee pe = (EdsProjectEmployee) item[0];
                EdsEmployee employee = (EdsEmployee) item[1];
                projectMembers.put(employee.getObjectID(), pe);
            }
        }

        // here we need to get all unavailable employees of the company, not the project
        EdsProject project = projectManager.get(projectId);
        List<EdsEmployee> projectUnavailableMembers = projectManager.getProjectUnavailableEmployees(project, startDate, endDate);

        //now we have all unavailable people of the project and we need to remove them from the projectMembers if exist
        for (EdsEmployee pe : projectUnavailableMembers) {
            if (pe != null) {
                projectMembers.remove(pe.getObjectID());
            }
        }
        List<PositionsSelectItem> newAssignees = new ArrayList<>();

        if (user.isEmployee()) {
            for (EdsProjectEmployee pe : projectMembers.values()) {
                EdsEmployeeDepartment edsEmployeeDepartment = pe.getEmployeeDepartment();
                EdsEmployee resEmployee = edsEmployeeDepartment != null ? edsEmployeeDepartment.getEmployee() : null;
                PositionsSelectItem sItem = new PositionsSelectItem();
                if (resEmployee != null) {
                    if (!user.equals(resEmployee)) {
                        sItem.setName(resEmployee.getName());
                    } else {
                        sItem.setMyself(true);
                        sItem.setName(resEmployee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
                    }
                    sItem.setId(pe.getObjectID());
                    EdsDepartment department = edsEmployeeDepartment.getTeam();
                    if (department != null) {
                        sItem.setDepartmentId(department.getObjectID());
                        sItem.setDepartmentName(department.getName());
                    }
                    sItem.setEmployeeId(resEmployee.getObjectID());
                    newAssignees.add(sItem);
                }
            }
        } else {
            for (EdsProjectEmployee pe : projectMembers.values()) {
                EdsEmployee resEmployee = pe.getEmployeeDepartment().getEmployee();
                PositionsSelectItem sItem = new PositionsSelectItem();
                sItem.setId(pe.getObjectID());
                sItem.setName(resEmployee.getName());

                EdsDepartment edsDepartment = pe.getEmployeeDepartment().getTeam();
                sItem.setDepartmentId(edsDepartment.getObjectID());
                sItem.setDepartmentName(edsDepartment.getName());
                newAssignees.add(sItem);
            }
        }
        PositionsSelectItem[] newAssigneesArray = newAssignees.toArray(new PositionsSelectItem[]{});
        Arrays.sort(newAssigneesArray, Comparator.comparing(SelectItem::getName));
        return newAssigneesArray;
    }

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssigneeListOnlyAvailableEmployees(List<Integer> userIDs, Integer projectId, Date startDate, Date endDate) {
        EdsUser user = employeeManager.getUser();
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeList = new LinkedHashMap<>();
        boolean team;

        List<Object[]> projectMembers = getProjectMembers(projectId, user, PermissionConstants.PM_ASSIGN_TASK_TO_MEMBER);
        // here we need to get all unavailable employees of the company, not the project
        List<EdsEmployee> projectUnavailableMembers = projectManager.getProjectUnavailableEmployees(/*project*/null, startDate, endDate);

        //now we have all unavailable people of the project and we need to remove them from the projectMembers if exist
        for (EdsEmployee pe : projectUnavailableMembers) {
            if (pe != null) {
                for (Object[] item : projectMembers) {
                    EdsProjectEmployee proEmployee = (EdsProjectEmployee) item[0];
                    EdsEmployee employee = (EdsEmployee) item[1];
                    if (employee.getObjectID().equals(pe.getObjectID())) {
                        projectMembers.remove(item);
                        break;
                    }
                }
            }
        }
        KpiTreeInfo sItem;
        EdsEmployee resEmployee;
        EdsDepartment department;
        if (user.isEmployee()) {
            for (Object[] item : projectMembers) {
                EdsProjectEmployee proEmployee = (EdsProjectEmployee) item[0];
                resEmployee = (EdsEmployee) item[1];
                department = (EdsDepartment) item[2];
                team = false;

                sItem = new KpiTreeInfo();
                if (resEmployee != null) {
                    if (!user.equals(resEmployee)) {
                        sItem.setName(resEmployee.getName());
                    } else {
                        sItem.setMyself(true);
                        sItem.setName(resEmployee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
                    }
                    sItem.setId(proEmployee.getObjectID());
                    if (userIDs != null && !userIDs.isEmpty() && userIDs.contains(resEmployee.getObjectID())) {
                        sItem.setSelected(true);
                    }
                    sItem.setEmployeeId(resEmployee.getObjectID());
                    if (department != null) {
                        sItem.setDepartmentId(department.getObjectID());
                        sItem.setDepartmentName(department.getName());
                        for (KpiTreeInfo s : assigneeList.keySet()) {
                            if (s.getId().equals(department.getObjectID())) {
                                team = true;
                                assigneeList.get(s).add(sItem);
                                break;
                            }
                        }

                        if (!team) {
                            KpiTreeInfo departmentInfo = new KpiTreeInfo(department.getObjectID(), department.getName());
                            ArrayList<KpiTreeInfo> list = new ArrayList<>();
                            list.add(sItem);
                            assigneeList.put(departmentInfo, list);
                        }
                    }

                }
            }
        } else {
            for (Object[] item : projectMembers) {
                EdsProjectEmployee projectEmployee = (EdsProjectEmployee) item[0];
                resEmployee = (EdsEmployee) item[1];
                department = (EdsDepartment) item[2];
                team = false;
                sItem = new KpiTreeInfo();
                sItem.setId(projectEmployee.getObjectID());
                sItem.setName(resEmployee.getName());
                sItem.setDepartmentId(department.getObjectID());
                sItem.setDepartmentName(department.getName());
                for (KpiTreeInfo s : assigneeList.keySet()) {
                    if (s.getId().equals(department.getObjectID())) {
                        team = true;
                        assigneeList.get(s).add(sItem);
                        break;
                    }
                }

                if (!team) {
                    KpiTreeInfo departmentInfo = new KpiTreeInfo(department.getObjectID(), department.getName());
                    ArrayList<KpiTreeInfo> list = new ArrayList<>();
                    list.add(sItem);
                    assigneeList.put(departmentInfo, list);
                }
            }
        }

        return assigneeList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ExistingAndNewTaskMembers getAssigneesWithPositions(Integer taskId, Integer projectId) {
        EdsUser user = employeeManager.getUser();
        return getAssigneesWithPositions(taskId, projectId, user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssigneesWithTreeInfoLinkedHashMapWithParams(LinkedHashMap<Integer, Integer> userIDs, Integer projectId, Integer basicTaskID, boolean selectMeOrFirst) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeList = new LinkedHashMap<>();
        EdsUser user = employeeManager.getUser();
        List<Object[]> projectMembers = getProjectMembers(projectId, user, PermissionConstants.PM_ASSIGN_TASK_TO_MEMBER);
        boolean team;
        KpiTreeInfo sItem;
        KpiTreeInfo defaultSelection = null;
        EdsEmployee resEmployee;
        Integer positionId;
        String positionName;
        boolean defaultSelected = false;
        EdsTask basicTask = null;
        Map<Integer, List<String>> taskAssignees = null;
        if (basicTaskID != null) {
            basicTask = taskManager.get(basicTaskID);
            taskAssignees = taskManager.getTaskAssigneeUserList(Collections.singletonList(basicTaskID));
        }
        if (user.isEmployee()) {
            for (Object[] item : projectMembers) {
                EdsProjectEmployee projectEmployee = (EdsProjectEmployee) item[0];
                resEmployee = (EdsEmployee) item[1];
                EdsDepartment department = (EdsDepartment) item[2];
                team = false;

                sItem = new KpiTreeInfo();

                positionId = null;
                positionName = null;
                if (resEmployee != null) {
                    if (resEmployee.getPosition() != null) {
                        positionId = resEmployee.getPosition().getObjectID();
                        positionName = resEmployee.getPosition().getName();
                    }
                    if (!user.equals(resEmployee)) {
                        sItem.setMyself(false);
                    } else {
                        sItem.setMyself(true);
                        sItem.setSelected(selectMeOrFirst);
                        defaultSelected = selectMeOrFirst;
                    }
                    if (basicTask != null && taskAssignees != null && taskAssignees.get(basicTaskID).contains(resEmployee)) {
                        sItem.setSelected(true);
                        EdsEmployeeTask employeeTask = taskManager.getEmployeeTask(resEmployee.getObjectID(), basicTaskID);
                        sItem.setTime(employeeTask != null ? employeeTask.getEstimatedTime() : null);
                    }
                    sItem.setId(projectEmployee.getObjectID());
                    if (userIDs != null && !userIDs.isEmpty() && userIDs.containsKey(resEmployee.getObjectID())) {
                        sItem.setStatusId(userIDs.get(resEmployee.getObjectID()));
                        sItem.setSelected(true);
                    }
                    if (sItem.isMyself()) {
                        if (resEmployee.getProfile() != null && resEmployee.getProfile().getEmployeeCode() != null) {
                            sItem.setName(resEmployee.getProfile().getEmployeeCode() + " - " + resEmployee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
                        } else {
                            sItem.setName(resEmployee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
                        }
                    } else {
                        if (resEmployee.getProfile() != null && resEmployee.getProfile().getEmployeeCode() != null) {
                            sItem.setName(resEmployee.getProfile().getEmployeeCode() + " - " + resEmployee.getName());
                        } else {
                            sItem.setName(resEmployee.getName());
                        }
                    }
                    sItem.setPositionId(positionId);
                    sItem.setPositionName(positionName);
                    sItem.setEmployeeId(resEmployee.getObjectID());
                    if (department != null) {
                        sItem.setDepartmentId(department.getObjectID());
                        sItem.setDepartmentName(department.getName());
                        defaultSelection = defaultSelection != null ? defaultSelection : sItem;
                        for (KpiTreeInfo s : assigneeList.keySet()) {
                            if (s.getId().equals(department.getObjectID())) {
                                team = true;
                                assigneeList.get(s).add(sItem);
                                break;
                            }
                        }

                        if (!team) {
                            KpiTreeInfo departmentInfo = new KpiTreeInfo(department.getObjectID(), department.getName());
                            ArrayList<KpiTreeInfo> list = new ArrayList<>();
                            list.add(sItem);
                            assigneeList.put(departmentInfo, list);
                        }
                    }
                }
            }
        } else {
            for (Object[] item : projectMembers) {
                EdsProjectEmployee projectEmployee = (EdsProjectEmployee) item[0];
                resEmployee = (EdsEmployee) item[1];
                EdsDepartment department = (EdsDepartment) item[2];
                team = false;

                sItem = new KpiTreeInfo();
                sItem.setId(projectEmployee.getObjectID());

                positionId = null;
                positionName = null;
                if (resEmployee.getPosition() != null) {
                    positionId = resEmployee.getPosition().getObjectID();
                    positionName = resEmployee.getPosition().getName();
                }
                sItem.setName(resEmployee.getName());

                sItem.setPositionId(positionId);
                sItem.setPositionName(positionName);
                sItem.setDepartmentId(department.getObjectID());
                sItem.setDepartmentName(department.getName());
                defaultSelection = defaultSelection != null ? defaultSelection : sItem;

                for (KpiTreeInfo s : assigneeList.keySet()) {
                    if (s.getId().equals(department.getObjectID())) {
                        team = true;
                        assigneeList.get(s).add(sItem);
                        break;
                    }
                }

                if (!team) {
                    KpiTreeInfo departmentInfo = new KpiTreeInfo(department.getObjectID(), department.getName());
                    ArrayList<KpiTreeInfo> list = new ArrayList<>();
                    list.add(sItem);
                    assigneeList.put(departmentInfo, list);
                }
            }
        }
        if (!defaultSelected && selectMeOrFirst && defaultSelection != null) {
            defaultSelection.setSelected(true);
        }
        return assigneeList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssigneesWithTreeInfoLinkedHashMap(LinkedHashMap<Integer, Integer> userIdWithStatus) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeList = new LinkedHashMap<>();
        EdsUser user = employeeManager.getUser();
        List<EdsEmployee> companyEmployees = employeeManager.getEmployees(user.getCompany());
        boolean team;
        boolean position;
        KpiTreeInfo sItem;
        if (user.isEmployee()) {
            for (EdsEmployee resEmployee : companyEmployees) {
                team = false;
                position = false;
                sItem = new KpiTreeInfo();
                if (resEmployee != null) {
                    if (!user.equals(resEmployee)) {
                        sItem.setMyself(false);
                    } else {
                        sItem.setMyself(true);
                    }
                    sItem.setId(resEmployee.getObjectID());

                    if (sItem.isMyself()) {
                        sItem.setName(resEmployee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
                    } else {
                        sItem.setName(resEmployee.getName());
                    }

                    if (resEmployee.getPrimaryPhone() != null) {
                        sItem.setPhone(resEmployee.getPrimaryPhone());
                    }
                    if (resEmployee.getEmail() != null) {
                        sItem.setEmail(resEmployee.getEmail());
                    }
                    if (userIdWithStatus != null && !userIdWithStatus.isEmpty() && userIdWithStatus.containsKey(resEmployee.getObjectID())) {
                        sItem.setStatusId(userIdWithStatus.get(resEmployee.getObjectID()));
                        sItem.setSelected(true);
                    }
                    sItem.setEmployeeId(resEmployee.getObjectID());
                    if (resEmployee.getTeam() != null) {
                        sItem.setDepartmentId(resEmployee.getTeam().getObjectID());
                        sItem.setDepartmentName(resEmployee.getTeam().getName());
                        for (KpiTreeInfo s : assigneeList.keySet()) {
                            if (s.getId().equals(resEmployee.getTeam().getObjectID())) {
                                team = true;
                                assigneeList.get(s).add(sItem);
                                break;
                            }
                        }

                        if (!team) {
                            KpiTreeInfo department = new KpiTreeInfo(resEmployee.getTeam().getObjectID(), resEmployee.getTeam().getName());
                            ArrayList<KpiTreeInfo> list = new ArrayList<>();
                            list.add(sItem);
                            assigneeList.put(department, list);
                        }
                    }
                    if (resEmployee.getPosition() != null) {
                        sItem.setPositionId(resEmployee.getPosition().getObjectID());
                        sItem.setPositionName(resEmployee.getPosition().getName());
                        for (KpiTreeInfo s : assigneeList.keySet()) {
                            if (s.getId().equals(resEmployee.getPosition().getObjectID())) {
                                position = true;
                                assigneeList.get(s).add(sItem);
                                break;
                            }
                        }

                        if (!position) {
                            KpiTreeInfo empPosition = new KpiTreeInfo(resEmployee.getPosition().getObjectID(), resEmployee.getPosition().getName());
                            ArrayList<KpiTreeInfo> list = new ArrayList<>();
                            list.add(sItem);
                            assigneeList.put(empPosition, list);
                        }
                    }
                }
            }
        } else {
            for (EdsEmployee resEmployee : companyEmployees) {
                team = false;
                sItem = new KpiTreeInfo();
                sItem.setId(resEmployee.getObjectID());
                sItem.setName(resEmployee.getName());
                sItem.setDepartmentId(resEmployee.getTeam().getObjectID());
                sItem.setDepartmentName(resEmployee.getTeam().getName());
                for (KpiTreeInfo s : assigneeList.keySet()) {
                    if (s.getId().equals(resEmployee.getTeam().getObjectID())) {
                        team = true;
                        assigneeList.get(s).add(sItem);
                        break;
                    }
                }

                if (!team) {
                    KpiTreeInfo department = new KpiTreeInfo(resEmployee.getTeam().getObjectID(), resEmployee.getTeam().getName());
                    ArrayList<KpiTreeInfo> list = new ArrayList<>();
                    list.add(sItem);
                    assigneeList.put(department, list);
                }
            }
        }
        return assigneeList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getTaskEditablePermission(Integer taskID) {
        EdsUser user = employeeManager.getUser();
        EdsTask task = taskManager.get(taskID);
        if ((task.getCreator() != null && task.getCreator().equals(user)) ||
                task.getProject().getManager().equals(user) ||
                task.getProject().isUserBackupManager(user.getObjectID()) ||
                user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsRole.ADMIN))) {
            return EDIT;
        } else {
            return READ;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ExistingAndNewTaskMembers getAssigneesWithPositions(Integer taskId, Integer projectId, EdsUser user) {

        ExistingAndNewTaskMembers newAndExistingResult = new ExistingAndNewTaskMembers();
        List<EdsProjectEmployee> pmAssignees = new ArrayList<>();
        Set<EdsEmployeeTask> taskAssignees;
        boolean isManager = false;
        if (taskId != null) {
            EdsTask task = taskManager.get(taskId);
            if ((task.getCreator() != null && task.getCreator().equals(user)) ||
                    task.getProject().getManager().equals(user) ||
                    task.getProject().isUserBackupManager(user.getObjectID()) ||
                    user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsRole.ADMIN))) {
                isManager = true;
                newAndExistingResult.setPermission(EDIT);
            } else {
                newAndExistingResult.setPermission(READ);
            }

            taskAssignees = task.getUnDeletedAssignments();
            PositionsSelectItem[] existingAssignees = new PositionsSelectItem[taskAssignees.size()];
            int j = 0;
            for (EdsEmployeeTask et : taskAssignees) {
                pmAssignees.add(et.getProjectEmployee());
                EdsEmployee resEmpl = et.getProjectEmployee().getEmployeeDepartment().getEmployee();
                existingAssignees[j] = new PositionsSelectItem(et.getProjectEmployee().getObjectID(), resEmpl.getName(),
                        (resEmpl.getPosition() != null ? resEmpl.getPosition().getObjectID() : null), (resEmpl.getPosition() != null ? resEmpl.getPosition().getName() : null));

                existingAssignees[j].setTime(et.getEstimatedTime());
                existingAssignees[j].setPercent(et.getPercent());
                existingAssignees[j].setActualTime(et.getTimeSpent());
                existingAssignees[j].setStatusId(et.getStatus() != null ? et.getStatus().getObjectID() : null);
                existingAssignees[j].setEmployeeId(resEmpl.getObjectID());
                j++;
            }
            Arrays.sort(existingAssignees, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
            newAndExistingResult.setExistingMembers(existingAssignees);
            projectId = task.getProject().getObjectID();

        } else if (projectId != null) {
            EdsProject project = projectManager.get(projectId);
            if (project != null) {
                if (project.getManager().equals(user) || project.isUserBackupManager(user.getObjectID()) || user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsRole.ADMIN))) {
                    isManager = true;
                    newAndExistingResult.setPermission(EDIT);
                } else {
                    newAndExistingResult.setPermission(READ);
                }
            }
        }
        List<Object[]> projectMembers = taskId != null ? getProjectMembers(projectId, user, isManager) : getProjectMembers(projectId, user, PermissionConstants.PM_ASSIGN_TASK_TO_MEMBER);
        if (projectMembers == null || projectMembers.isEmpty()) {
            return newAndExistingResult;
        }

        List<PositionsSelectItem> newAssignees = new ArrayList<>();
        //now compare taskAssignees and pmAssignees, then fill new list from that items that do not exist in  taskAssignees
        int i = 0, myself = 0;
        if (user.isEmployee()) {
            for (Object[] item : projectMembers) {
                EdsProjectEmployee pe = (EdsProjectEmployee) item[0];
                EdsEmployee resEmployee = (EdsEmployee) item[1];
                EdsDepartment department = (EdsDepartment) item[2];
                if (pmAssignees.contains(pe)) {
                    continue;
                }

                PositionsSelectItem sItem = new PositionsSelectItem();
                Integer positionId = null;
                String positionName = null;
                if (resEmployee != null) {
                    if (resEmployee.getPosition() != null) {
                        positionId = resEmployee.getPosition().getObjectID();
                        positionName = resEmployee.getPosition().getName();
                    }
                    if (!user.equals(resEmployee)) {
                        sItem.setMyself(false);
                    } else {
                        sItem.setMyself(true);
                        myself = i;
                    }
                    sItem.setId(pe.getObjectID());

                    if (sItem.isMyself()) {
                        sItem.setName(resEmployee.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
                    } else {
                        sItem.setName(resEmployee.getName());
                    }
                    sItem.setPositionId(positionId);
                    sItem.setPositionName(positionName);
                    if (department != null) {
                        sItem.setDepartmentId(department.getObjectID());
                        sItem.setDepartmentName(department.getName());
                    }
                    sItem.setEmployeeId(resEmployee.getObjectID());
                    newAssignees.add(sItem);
                }

                i++;
            }
        } else {
            for (Object[] item : projectMembers) {
                EdsProjectEmployee pe = (EdsProjectEmployee) item[0];
                EdsEmployee resEmployee = (EdsEmployee) item[1];
                EdsDepartment department = (EdsDepartment) item[2];
                if (pmAssignees.contains(pe)) {
                    continue;
                }
                PositionsSelectItem sItem = new PositionsSelectItem();
                sItem.setId(pe.getObjectID());

                Integer positionId = null;
                String positionName = null;
                if (resEmployee.getPosition() != null) {
                    positionId = resEmployee.getPosition().getObjectID();
                    positionName = resEmployee.getPosition().getName();
                }
                sItem.setName(resEmployee.getName());

                sItem.setPositionId(positionId);
                sItem.setPositionName(positionName);
                sItem.setDepartmentId(department.getObjectID());
                sItem.setDepartmentName(department.getName());
                newAssignees.add(sItem);
                i++;
            }
        }
        PositionsSelectItem[] newAssigneesArray = newAssignees.toArray(new PositionsSelectItem[]{});
        //Now sets "Myself" as first element
        if (myself != 0) {
            PositionsSelectItem tmp = newAssigneesArray[myself];
            newAssigneesArray[myself] = newAssigneesArray[0];
            newAssigneesArray[0] = tmp;
        }
        Arrays.sort(newAssigneesArray, Comparator.comparing(SelectItem::getName));
        newAndExistingResult.setNewMembers(newAssigneesArray);
        return newAndExistingResult;
    }

    public List<Object[]> getProjectMembers(Integer projectId, EdsUser user, boolean isManager) {
        List<Object[]> projectMembers = null;
        if (projectCircularResolverService.getProjectSpecificPermissions(projectId).contains(PermissionConstants.PM_ASSIGN_TASK_TO_MEMBER)) {
            projectMembers = projectManager.getProjectEmployees(projectId, EdsRole.ADMIN);
        } else {
            if (user.hasRole(roleManager.get(EdsRole.DR))) {
                projectMembers = projectManager.getProjectEmployees(projectId, EdsRole.DR);
            } else if (user.hasRole(roleManager.get(EdsRole.PM)) && (isManager)) {
                projectMembers = projectManager.getProjectEmployees(projectId, EdsRole.PM);
            } else if (user.hasRole(roleManager.get(EdsRole.MEM)) || user.hasEitherRoles(EdsRole.ESS_USER_CODE)) {
                projectMembers = projectManager.getProjectEmployees(projectId, EdsRole.MEM);
            } else if (user.hasRole(roleManager.get(EdsRole.CLIENT))) {
                projectMembers = projectManager.getProjectEmployees(projectId, EdsRole.CLIENT);
            }
        }
        return projectMembers;
    }

    public List<Object[]> getProjectMembers(Integer projectId, EdsUser user, String permission) {
        List<Object[]> projectMembers;
        if (projectCircularResolverService.getProjectSpecificPermissions(projectId).contains(permission)) {
            projectMembers = projectManager.getProjectEmployees(projectId, EdsRole.ADMIN);
        } else {
            projectMembers = projectManager.getProjectEmployees(projectId, EdsRole.MEM);
        }
        return projectMembers;

    }

    public Boolean getAssignEmployeeToProject(Integer projectId, String permission) {
        return projectCircularResolverService.getProjectSpecificPermissions(projectId).contains(permission);
    }

    @Override
    public String getTaskName(Integer taskID) {
        if (taskID != null) {
            EdsTask task = taskManager.get(taskID);
            if (task != null) {
                return task.getName();
            }
            return null;
        }
        return null;
    }

    /**
     * Timesheet plugun
     *
     * @param dataItem
     * @return
     */
    @Override
    public Boolean setTimeToTimesheet(TimesheetDataItem dataItem) {
        if (dataItem != null) {
            EdsUser user = (dataItem.getEmployeeID() != 0) ? userManager.get(dataItem.getEmployeeID()) : userManager.getUser();
            EdsTask task = taskManager.get(dataItem.getTaskID());
            EdsEmployee employee = employeeManager.get(user.getObjectID());
            if (task != null && employee != null) {
                EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeRelatedTask(task, employee);
                if (employeeTask != null) {
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(dataItem.getDate());
                    ServerUtils.setBeginningOfTheDay(calendar);
                    dataItem.setDate(calendar.getTime());
                    dataItem.setEmployeeTaskID(employeeTask.getObjectID());
                    timesheetService.applyUpdates(dataItem, null);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public TimesheetDataItem getValidationData(HashMap<String, Object> paramMap) {
        if (paramMap == null || !paramMap.containsKey("taskId") || !paramMap.containsKey("selectedDate")) {
            return null;
        }
        return getValidationData((Integer) paramMap.get("taskId"), (DateNonConvertable) paramMap.get("selectedDate"), null);
    }

    @Override
    public TimesheetDataItem getValidationData(Integer taskID, DateNonConvertable selectedDate, Integer employeeId) {
        System.out.println("************************** selected Date******************** : " + selectedDate.getNonConvertedDate());
        Calendar startDate = new GregorianCalendar();
        startDate.setTime(selectedDate.getNonConvertedDate());
        startDate.set(Calendar.AM_PM, 0);
        startDate.set(Calendar.HOUR, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = new GregorianCalendar();
        endDate.setTime(selectedDate.getNonConvertedDate());
        endDate.set(Calendar.AM_PM, 0);
        endDate.set(Calendar.HOUR, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        endDate.set(Calendar.MILLISECOND, 0);

        EdsTask task = taskManager.get(taskID);

        EdsUser user = null;
        if (employeeId != null) {
            user = userManager.get(employeeId);
        }

        if (user == null) {
            user = userManager.getUser();
        }

        EdsEmployee employee = employeeManager.get(user.getObjectID());

        List<EdsEmployeeTask> taskList = employeeTaskManager.listDueTasks(employee, startDate.getTime(), endDate.getTime(), new ListingFilterParameter());
        FastTaskTransfer transferTask = new FastTaskTransfer();
        if (taskList != null && !taskList.isEmpty()) {
            for (EdsEmployeeTask employeeTask : taskList) {
                if (employeeTask.getObjectID().equals(taskID)) {
                    transferTask.getTaskStatus().setEmployeeTaskId(employeeTask.getObjectID());
                    transferTask.getTaskStatus().setStatus(employeeTask.getStatus().getObjectID());
                    transferTask.getTaskStatus().setStatusName(referenceWfmMessageSource.localizeRef(employeeTask.getStatus()));
                    transferTask.getTaskStatus().setTaskId(employeeTask.getObjectID());
                    transferTask.setEstimatedTime(employeeTask.getEstimatedTime());
                    transferTask.setPercentCompleted(employeeTask.getPercent() == null ? 0 : employeeTask.getPercent());
                    transferTask.setTaskId(employeeTask.getTask().getObjectID());
                    break;
                }
            }
        }

        List<EdsTimeSheet> timesheets = timesheetManager.list(employee, startDate.getTime(), endDate.getTime());
        List<EdsAttendanceRawData> attendanceRawDataList = attendanceRawDataManager.getAttendanceRawDataByDates(startDate.getTime(), endDate.getTime(), user.getObjectID());

        if (attendanceRawDataList != null && !attendanceRawDataList.isEmpty() && task != null) {
            TimesheetDataItem dataItem = new TimesheetDataItem();
            dataItem.setTaskStart(task.getStartDate());
            dataItem.setTaskEnd(task.getDueDate());
            dataItem.setTimeslotMinutes(attendanceRawDataList.get(0).getTimeSlot());
            dataItem.setTimesheetMinutes(attendanceRawDataList.get(0).getTimeSheet() + attendanceRawDataList.get(0).getTimeSheetPending());
            dataItem.setLeaveRequestMinutes(attendanceRawDataList.get(0).getLeave());
            dataItem.setHoliday(attendanceRawDataList.get(0).getHoliday());
            dataItem.setDayOff(attendanceRawDataList.get(0).getDayOff());
            dataItem.setTaskTransfer(transferTask);
            dataItem.setOldMinutes(0);
            dataItem.setEmployeeID(attendanceRawDataList.get(0).getEmployee().getObjectID());

            if (timesheets != null && !timesheets.isEmpty()) {
                for (EdsTimeSheet edsTimeSheet : timesheets) {
                    if (edsTimeSheet.getTaskID().equals(taskID)) {
                        dataItem.setOldMinutes(edsTimeSheet.getTimeSpent());
                        dataItem.setOldComment(edsTimeSheet.getComment());
                        if (edsTimeSheet.getStatus() == null) {
                            dataItem.setStatus(TIMESHEET_ENTRY_NOTSUBMITTED);
                        } else if ("_APPROVE".equals(edsTimeSheet.getStatus().getCode())) {
                            dataItem.setStatus(TIMESHEET_ENTRY_APPROVED);
                        } else if ("_WAITING".equals(edsTimeSheet.getStatus().getCode())) {
                            dataItem.setStatus(TIMESHEET_ENTRY_WAITING);
                        } else {
                            dataItem.setStatus(TIMESHEET_ENTRY_NOTSUBMITTED);
                        }
                        dataItem.setId(edsTimeSheet.getObjectID());
                        break;
                    }
                }
            }
            return dataItem;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PositionsSelectItem[] getAssigneesToEmployee(Integer projectId, Integer employeeId) {
        EdsEmployee employee = employeeManager.get(employeeId);
        ExistingAndNewTaskMembers newMembers = getAssigneesWithPositions(null, projectId, employee);
        return newMembers.getNewMembers();
    }

    public Integer updateParentTask(Integer taskID, Integer parentWorkstreamID) {
        if (taskID != null && parentWorkstreamID != null) {
            EditTask task = getTaskForEdit(taskID);
            task.setParentWSItem(new SelectItem(parentWorkstreamID));
            try {
                updateTask(task);
            } catch (NumberExistingException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public Integer updateParentWorkstream(Integer workstreamID, Integer parentWorkstreamID) {
        if (workstreamID != null && parentWorkstreamID != null && !workstreamID.equals(parentWorkstreamID)) {
            EdsWorkStream workstream = workStreamManager.get(workstreamID);
            EdsWorkStream parentWorkstream = workStreamManager.get(parentWorkstreamID);

            //if old parent already exist clear it
            if (workstream.getParentWS() != null && workstream.getParentWS().getObjectID() != null) {
                clearOldParentWSCalculatedItemsOfWorkstream(workstream);
            }

            //set WS calculation items to new parent WS
            if (parentWorkstream != null && parentWorkstream.getObjectID() != null) {
                parentWorkstream.updateEstimatedTime(workstream.getEstimatedTime());
                parentWorkstream.updateActualTime(workstream.getActualTime());

                parentWorkstream.updatePlannedWageAmmount(workstream.getPlannedWageAmount());
                parentWorkstream.updatePlannedClientChargeAmmount(workstream.getPlannedClientChargeAmount());
                parentWorkstream.updateActualWageAmmount(workstream.getActualWageAmount());
                parentWorkstream.updateActualClientChargeAmmount(workstream.getActualClientChargeAmount());
                parentWorkstream.updateWageAmmount(workstream.getWageAmmount());
                parentWorkstream.updateClientChargeAmmount(workstream.getClientChargeAmmount());
                workstream.setParentWS(parentWorkstream);
                adjustParentWSDates(workstream);
            }

            workStreamManager.update(workstream);
        }

        return 0;
    }

    private void adjustParentWSDates(EdsWorkStream workstream) {
        if (workstream.getParentWS() != null) {
            if (workstream.getParentWS().getStartDate().getTime() > workstream.getStartDate().getTime()) {
                workstream.getParentWS().setStartDate(workstream.getStartDate());
            }
            if (workstream.getParentWS().getEndDate().getTime() < workstream.getEndDate().getTime()) {
                workstream.getParentWS().setEndDate(workstream.getEndDate());
            }
            adjustParentWSDates(workstream.getParentWS());
        }
    }

    public Integer createWorkstream(WorkstreamSingleItem newWorkstream, PositionProjectEmployeeIdTime assignees) throws NumberExistingException {
        if (newWorkstream != null && newWorkstream.getName() != null) {
            EdsUser user = employeeManager.getUser();
            Integer workstreamId = newWorkstream.getObjectID();
            boolean isNew = (workstreamId == null);
            NumberData numberData = newWorkstream.getNumberData();
            if (isNew && (numberData == null || numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
                throw new NumberExistingException("Incorrect workstream number format.");
            }
            EdsWorkStream workstream;
            if (!isNew) {
                workstream = workStreamManager.get(newWorkstream.getObjectID());
                if (!workstream.getName().equals(newWorkstream.getName())) {
                    baseEventPostProcessor.registerEvent(WorkstreamEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, workstream, user);
                }
                if (numberData == null) {
                    numberData = new NumberData(workstream.getNumber(), workstream.getIntNumber());
                }
                if (workstream.getNumber() != null && !"".equals(workstream.getNumber().trim()) && (numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
                    throw new NumberExistingException("Incorrect workstream number format.");
                }
            } else {
                workstream = new EdsWorkStream();
            }

            if (numberData.getNumberString() == null || numberData.getNumberString().isEmpty() /*|| taskManager.isTaskNumberExists(numberData.getNumberString(), newWorkstream.getProjectID(), newWorkstream.getObjectID())*/) {
                numberData = generateWorkstreamNumber(newWorkstream.getProjectID(), newWorkstream.getStartDate(), null);
            }

            if (numberData != null) {
                String fullNumber = numberData.getNumberString() != null ? numberData.getNumberString() : "";
                workstream.setNumber(fullNumber);
                if (numberData.getSavedNumberFormula() != null && !"".equals(numberData.getSavedNumberFormula())) {
                    workstream.setSavedNumberFormula(numberData.getSavedNumberFormula());
                } else {
                    workstream.setSavedNumberFormula(("".equals(numberData.getFirstNumberString()) ? "null" : numberData.getFirstNumberString()) + SAV_NUM_DEL + ("".equals(numberData.getIntNumber()) ? "null" : decimalFormat.format(numberData.getIntNumber())) + SAV_NUM_DEL + ("".equals(numberData.getLastNumberString()) ? "null" : numberData.getLastNumberString()));
                }
                workstream.setIntNumber(numberData.getIntNumber());
            }

            int workstreamEstimatedTime = 0;
            workstream.setName(newWorkstream.getName());
            workstream.setDescription(newWorkstream.getDescription());
            workstream.setTaskGanttOrder(newWorkstream.getTaskGanttOrder());
            if (user != null) {
                workstream.setCreator(user);
            }
            workstream.setStartDate(new Date(newWorkstream.getStartDate().getTime()));
            workstream.setEndDate(newWorkstream.getEndDate());
            if (newWorkstream.getParentWSID() != null) {
                EdsWorkStream parent = workStreamManager.get(newWorkstream.getParentWSID());

                if (workstream.getParentWS() != null && !workstream.getParentWS().equals(parent)) {
                    clearOldParentWSCalculatedItemsOfWorkstream(workstream);

                    parent.updateEstimatedTime(workstream.getEstimatedTime());
                    parent.updateActualTime(workstream.getActualTime());

                    parent.updatePlannedWageAmmount(workstream.getPlannedWageAmount());
                    parent.updatePlannedClientChargeAmmount(workstream.getPlannedClientChargeAmount());
                    parent.updateActualWageAmmount(workstream.getActualWageAmount());
                    parent.updateActualClientChargeAmmount(workstream.getActualClientChargeAmount());

                    parent.updateWageAmmount(workstream.getWageAmmount());
                    parent.updateClientChargeAmmount(workstream.getClientChargeAmmount());
                }

                workstream.setParentWS(parent);
            } else {
                //was parent WS is not null clear it
                if (workstream.getParentWS() != null) {
                    clearOldParentWSCalculatedItemsOfWorkstream(workstream);
                }

                workstream.setParentWS(null);
            }

            if (newWorkstream.getProjectID() != null) {
                workstream.setProject(projectManager.get(newWorkstream.getProjectID()));
            }

            if (workstream.getObjectID() != null) {
                if (user != null) {
                    workstream.setUpdater(user);
                }
                workstream.setLastUpdateTime(new Date());
                workStreamManager.update(workstream);
            } else {
                workstream.setCreationTime(new Date());
                workstream.setEstimatedTime(workstreamEstimatedTime);
                workStreamManager.create(workstream);
            }

            saveWorkstreamReminder(workstream.getObjectID(), user.getCompany(), newWorkstream.getReminder());

            adjustParentWSDates(workstream);

            System.out.println("Save MS Project Workstream. Workstream ID=" + workstream.getObjectID() + "; Workstream Name: " + workstream.getName());
            return workstream.getObjectID();
        }
        return 0;
    }

    public void deleteWorkstream(Integer workstreamID, Integer defaultWorkstreamID, boolean withAllTasksAndSUBW) {
        EdsWorkStream workStream = workStreamManager.get(workstreamID);
        EdsUser updater = userManager.getUser();
        EdsWorkStream defaultWorkStream = null;
        if (defaultWorkstreamID != null) {
            defaultWorkStream = workStreamManager.get(defaultWorkstreamID);
        }

        Set<EdsWorkStream> subWorkStreams = workStream.getSubWorkStreams();
        if (subWorkStreams != null && !subWorkStreams.isEmpty()) {
            for (EdsWorkStream subWorkStream : subWorkStreams) {
                if (withAllTasksAndSUBW) {
                    deleteWorkstream(subWorkStream.getObjectID(), defaultWorkstreamID, withAllTasksAndSUBW);
                } else {
                    if (defaultWorkStream != null) {
                        subWorkStream.setParentWS(defaultWorkStream);
                    } else {
                        subWorkStream.setParentWS(null);
                    }
                    subWorkStream.setUpdater(updater);
                    subWorkStream.setLastUpdateTime(updater.getCompany().getCompanyDate());
                    workStreamManager.update(subWorkStream);
                }
            }
        }
        Set<EdsTask> workstreamTasks = workStream.getTasks();
        if (workstreamTasks != null && !workstreamTasks.isEmpty()) {
            for (EdsTask workstreamTask : workstreamTasks) {
                if (withAllTasksAndSUBW) {
                    deleteTask(workstreamTask.getObjectID(), null);
                } else {
                    if (defaultWorkStream != null) {
                        workstreamTask.setParentWS(defaultWorkStream);
                    } else {
                        workstreamTask.setParentWS(null);
                    }
                    workstreamTask.setUpdater(updater);
                    workstreamTask.setLastUpdateTime(updater.getCompany().getCompanyDate());
                    taskManager.update(workstreamTask);
                }
            }
        }

        workStream.setUpdater(updater);
        workStream.setLastUpdateTime(updater.getCompany().getCompanyDate());

        if (workStream.getParentWS() != null && workStream.getParentWS().getObjectID() != null) {
            clearOldParentWSCalculatedItemsOfWorkstream(workStream);
        }

        workStream.setDeleted(true);
        workStreamManager.update(workStream);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getWorkstreamsSomeParent(Integer parentWorkstreamID) {
        List<Integer> workStreamIDs = workStreamManager.getWorkStreamsSomeParent(parentWorkstreamID);
        EdsWorkStream tasksWorksStream = workStreamManager.get(parentWorkstreamID);
        List<EdsWorkStream> newWorkstreams = workStreamManager.listByProjectId(tasksWorksStream.getProject().getObjectID());
        for (Integer workStreamID : workStreamIDs) {
            EdsWorkStream workStream = workStreamManager.get(workStreamID);
            if (!workStream.isDeleted()) {
                newWorkstreams.remove(workStream);
            }
        }
        SelectItem[] items = new SelectItem[newWorkstreams.size()];
        int i = 0;
        for (EdsWorkStream workStream : newWorkstreams) {
            items[i] = new SelectItem();
            items[i].setId(workStream.getObjectID());
            items[i].setName(workStream.getName());
            i++;
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BudgetItem[] getTaskBudget(Integer taskId) {
        EdsTask task = taskManager.get(taskId);
        boolean hasPermission = ServerUtils.hasPermission(PermissionConstants.PM_TASKS_BUDGET);
        if (!hasPermission) {
            return null;
        }
        return getTaskBudget(task);
    }

    /**
     * Gets the planned cost of a single task according to planned data and actual timespent
     * The task cost calculation takes into account the dynamics of assignees' "Charge Rates" and "Client Charge Rates"
     * alteration.
     *
     * @param task
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BudgetItem[] getTaskBudget(EdsTask task) {
        Set<EdsEmployeeTask> assignees = task.getAssignments();
        Set<EdsPositionTask> positions = task.getPositions();

        Map<String, BudgetItem> budgetList = new HashMap<>();
        Iterator<EdsEmployeeTask> iterator = assignees.iterator();
        int i = 0;
        String spentTime;
        while (iterator.hasNext()) {
            EdsEmployeeTask employeeTask = iterator.next();
            Integer employeeID = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID();
            BudgetItem item;
            if (budgetList.containsKey(employeeID.toString())) {
                item = budgetList.get(employeeID.toString());
            } else {
                item = new BudgetItem();
            }

            if (employeeTask.getProjectEmployee() != null) {
                EdsProjectEmployee employeeProject = employeeTask.getProjectEmployee();
                EdsEmployee employee = employeeProject.getEmployeeDepartment().getEmployee();
                List<EdsProjectEmployeeWageClientRateHistory> rateHistories = projectManager.getEmployeeWageClientRateHistory(employee.getObjectID(), task.getProject().getObjectID());
                EdsProjectEmployeeWageClientRateHistory changedBefore = null;
                List<EdsProjectEmployeeWageClientRateHistory> changedIn = new ArrayList<>();
                if (rateHistories != null && !rateHistories.isEmpty()) {
                    for (EdsProjectEmployeeWageClientRateHistory rateHistory : rateHistories) {
                        if (employeeTask.getTask().getDueDate().before(rateHistory.getChangeDate())) {
                            continue;
                        }
                        if (employeeTask.getTask().getStartDate().after(rateHistory.getChangeDate())) {
                            if (changedBefore == null) {// when have to not null
                                changedBefore = rateHistory;
                            }
                            if (rateHistory.getChangeDate().after(changedBefore.getChangeDate())) {
                                changedBefore = rateHistory;
                            }
                        } else {
                            if (employeeTask.getTask().getDueDate().after(rateHistory.getChangeDate()) && employeeTask.getTask().getStartDate().before(rateHistory.getChangeDate())) {
                                changedIn.add(rateHistory);
                            }
                        }
                    }
                }
                if (changedBefore != null && !changedIn.isEmpty()) {

                    Date start = employeeTask.getTask().getStartDate();
                    spentTime = timeSheetManager.getSumEmployeeSpentToTaskInterval(employeeTask, start, changedIn.get(0).getChangeDate());
                    if (spentTime != null) {
                        if (!employeeTask.getDeleted()) {
                            item.setPlannedWageAmount(employeeTask.getPlannedWageAmount() != null ? employeeTask.getPlannedWageAmount() : 0);
                            item.setPlannedClientChargeAmount(employeeTask.getPlannedClientChargeAmount() != null ? employeeTask.getPlannedClientChargeAmount() : 0);
                            item.setEstimatedTime(employeeTask.getEstimatedTime() != null ? employeeTask.getEstimatedTime() : 0);
                        }
                        item.setActualTime(Integer.parseInt(spentTime) + item.getActualTime());
                        item.setActuallWageAmmount((changedBefore.getWageRate() * (Double.parseDouble(spentTime) / 60)) + item.getActuallWageAmmount());
                        item.setActualClientChargeAmmount((employeeTask.getActualClientChargeAmmount() != null ? employeeTask.getActualClientChargeAmmount() : 0) + item.getActualClientChargeAmmount());
                        item.setEmployeeName(employeeProject.getEmployeeDepartment().getEmployee().getName());
                        item.setWageRate(changedBefore.getWageRate());
                        item.setClientChargeRate(employeeProject.getClientChargeRate() != null ? employeeProject.getClientChargeRate() : 0);
                        item.setChangedDateWageRate(changedBefore.getChangeDate());

                        budgetList.put(employeeID.toString(), item);
                        start = changedIn.get(0).getChangeDate();
                    }
                    Date end;
                    for (int j = 0; i < changedIn.size(); i++) {
                        end = changedIn.get(j).equals(changedIn.get(changedIn.size() - 1)) ? employeeTask.getTask().getDueDate() : changedIn.get(j + 1).getChangeDate();
                        spentTime = timeSheetManager.getSumEmployeeSpentToTaskInterval(employeeTask, start, end);

                        if (spentTime == null) {
                            continue;
                        }

                        try {
                            start = !changedIn.get(j).equals(changedIn.get(changedIn.size() - 1)) ? changedIn.get(j + 1).getChangeDate() : null;
                        } catch (Exception e) {
                            start = null;
                            System.out.print(e.getMessage());
                        }

                        item = new BudgetItem();
                        if (!employeeTask.getDeleted()) {
                            item.setPlannedWageAmount(employeeTask.getPlannedWageAmount() != null ? employeeTask.getPlannedWageAmount() : 0);
                            item.setPlannedClientChargeAmount(employeeTask.getPlannedClientChargeAmount() != null ? employeeTask.getPlannedClientChargeAmount() : 0);
                            item.setEstimatedTime(employeeTask.getEstimatedTime() != null ? employeeTask.getEstimatedTime() : 0);
                        }
                        item.setActualTime(Integer.parseInt(spentTime) + item.getActualTime());
                        item.setActuallWageAmmount((changedIn.get(j).getWageRate() * (Double.parseDouble(spentTime) / 60)) + item.getActuallWageAmmount());
                        item.setActualClientChargeAmmount((employeeTask.getActualClientChargeAmmount() != null ? employeeTask.getActualClientChargeAmmount() : 0) + item.getActualClientChargeAmmount());
                        item.setEmployeeName(employeeProject.getEmployeeDepartment().getEmployee().getName());
                        item.setWageRate(changedIn.get(j).getWageRate());
                        item.setClientChargeRate(employeeProject.getClientChargeRate() != null ? employeeProject.getClientChargeRate() : 0);
                        item.setChangedDateWageRate(changedIn.get(j).getChangeDate());

                        budgetList.put(employeeID.toString(), item);
                    }
                } else {
                    if (!employeeTask.getDeleted()) {
                        item.setPlannedWageAmount(employeeTask.getPlannedWageAmount() != null ? employeeTask.getPlannedWageAmount() : 0);
                        item.setPlannedClientChargeAmount(employeeTask.getPlannedClientChargeAmount() != null ? employeeTask.getPlannedClientChargeAmount() : 0);
                        item.setEstimatedTime(employeeTask.getEstimatedTime() != null ? employeeTask.getEstimatedTime() : 0);
                    }
                    item.setActualTime((employeeTask.getTimeSpent() != null ? employeeTask.getTimeSpent() : 0) + item.getActualTime());
                    item.setActuallWageAmmount((employeeTask.getActualWageAmmount() != null ? employeeTask.getActualWageAmmount() : 0) + item.getActuallWageAmmount());
                    item.setActualClientChargeAmmount((employeeTask.getActualClientChargeAmmount() != null ? employeeTask.getActualClientChargeAmmount() : 0) + item.getActualClientChargeAmmount());
                    item.setEmployeeName(employeeProject.getEmployeeDepartment().getEmployee().getName());
                    item.setWageRate(employeeProject.getWageRate() != null ? employeeProject.getWageRate() : 0);
                    item.setClientChargeRate(employeeProject.getClientChargeRate() != null ? employeeProject.getClientChargeRate() : 0);
                    budgetList.put(employeeID.toString(), item);

                }
            }

        }
        if (!positions.isEmpty()) {
            for (EdsPositionTask positionTask : positions) {
                BudgetItem item = new BudgetItem();
                item.setEmployeePosition(positionTask.getPosition().getName());
                item.setEstimatedTime(positionTask.getEstimatedTime() != null ? positionTask.getEstimatedTime() : 0);
                item.setWageRate(positionTask.getPosition().getAverageRate() != null ? positionTask.getPosition().getAverageRate() : 0);
                item.setClientChargeRate(positionTask.getPosition().getAverageClientchargeRate() != null ? positionTask.getPosition().getAverageClientchargeRate() : 0);
                budgetList.put("", item);
            }
        }

        return budgetList.values().toArray(new BudgetItem[]{});
    }

    //don't use for another logic's!
    public Integer[] saveTaskWithNewProjectEmployees(TaskSingleItem taskItem) throws NumberExistingException {
        IdTime[] newEmployees = taskItem.getProjectEmployees();

        ArrayList<IdTime> newProjectEmployees = new ArrayList<>();
        if (newEmployees != null && newEmployees.length > 0 && taskItem.getProjectID() != null) {
            EdsProject edsProject = projectManager.get(taskItem.getProjectID());
            for (IdTime employeeIdTime : newEmployees) {
                EdsEmployee edsEmployee = employeeManager.get(employeeIdTime.getId());
                if (edsEmployee != null) {
                    EdsProjectEmployee edsProjectEmployee = projectEmployeeManager.getProjectEmployee(edsEmployee, edsProject);
                    if (edsProjectEmployee == null) {
                        edsProjectEmployee = addMembers(edsProject, edsEmployee);
                    }
                    // set new project employee idTime
                    IdTime idTime = new IdTime();
                    idTime.setId(edsProjectEmployee.getObjectID());
                    idTime.setTime(employeeIdTime.getTime());
                    newProjectEmployees.add(idTime);
                }
            }
        }
        taskItem.setProjectEmployees(newProjectEmployees.toArray(new IdTime[0]));

        return saveTask(taskItem);
    }

    //@CheckPermission(permissions = {PermissionConstants.PM_TASKS_ADD, PermissionConstants.CRM_TASKS_ADD, PermissionConstants.PM_TASKS_EDIT, PermissionConstants.CRM_TASKS_EDIT})
    public Integer[] saveTask(TaskSingleItem taskItem) throws NumberExistingException {
        EdsUser user = userManager.getUser();
        if (user == null) {
            user = userManager.get(taskItem.getTaskCreatorID());
        }
        return saveTask(taskItem, user);
    }

    public Integer[] saveTask(TaskSingleItem taskItem, Integer userID) throws NumberExistingException {
        EdsUser user = userManager.get(userID);
        if (user == null) {
            user = userManager.get(taskItem.getTaskCreatorID());
        }
        return saveTask(taskItem, user);
    }

    private Integer[] saveTask(TaskSingleItem taskItem, EdsUser user) throws NumberExistingException {//todo

        boolean isNew = taskItem.getObjectID() == null;
        boolean isRecurring = (taskItem.getRecurrenceId() != null || (taskItem.getRecurrenceJobItem() != null && taskItem.getRecurrenceJobItem().isEnabled()));
        boolean isRecurringEdited = false;
        boolean isRecurringAdded = false;
        boolean isRecurringRemoved = (taskItem.getRecurrenceId() != null && (taskItem.getRecurrenceJobItem() == null));
        boolean isSeries = (isNew && isRecurring && taskItem.getRecurrenceJobItem() == null);
        long startDateDiff = 0;
        long endDateDiff = 0;
        Integer recurringDateSize = 0;
        ArrayList<IdTime> oldProjectEmployeesList;
        ArrayList<IdTime> newProjectEmployeesList;
        EdsTask task = new EdsTask();
        if (!isNew) {
            task = taskManager.get(taskItem.getObjectID());
            startDateDiff = taskItem.getStartDate().getTime() - task.getStartDate().getTime();
            endDateDiff = taskItem.getDueDate().getTime() - task.getDueDate().getTime();
            if (task.getRecurrenceID() != null) {
                EdsRecurrence oldRecurrence = recurrenceManager.get(task.getRecurrenceID());
                EdsRecurrence newRecurrence = new EdsRecurrence();
                if (taskItem.getRecurrenceJobItem() != null) {
                    if (oldRecurrence != null) {
                        recurrenceService.wrapRecurrenceJobItemToEdsRecurrence(taskItem.getRecurrenceJobItem(), newRecurrence, recurrenceJobManager.get(RECURRING_TASK));
                        String oldExpression = recurrenceManager.getCronExpression(oldRecurrence);
                        String newExpression = recurrenceManager.getCronExpression(newRecurrence);
                        if (!oldExpression.equals(newExpression) || recurrenceManager.getTriggerEndDate(oldRecurrence).getTime() != recurrenceManager.getTriggerEndDate(newRecurrence).getTime()) {
                            isRecurringEdited = true;
                        }
                    } else {
                        isRecurringAdded = true;
                    }
                }
            }
        }
        if (taskItem.isCallModal() && taskItem.getRelations() != null && !taskItem.getRelations().isEmpty()) {
            ListingFilterParameter filterParamters = new ListingFilterParameter();
            filterParamters.setRelationID(taskItem.getRelations().get(0).getToID());
            filterParamters.setRelationType(taskItem.getRelations().get(0).getToType());
            filterParamters.setLimit(1);
            ListResult<EventItem> events = crmServiceLocal.getEventList(filterParamters);
            RelationItem activitityRelation = new RelationItem();
            activitityRelation.setFromType("TASK");
            activitityRelation.setFromName(taskItem.getName());
            activitityRelation.setToID(events.getList().get(0).getObjectID());
            activitityRelation.setToType("event");
            activitityRelation.setToName(events.getList().get(0).getSubject());
            taskItem.getRelations().add(activitityRelation);
        }
        IdTime[] oldProjectEmployees = task != null ? wrapTaskAssigneesToIdTime(task) : null;
        IdTime[] newProjectEmployees = taskItem.getProjectEmployees() != null ? taskItem.getProjectEmployees().clone() : null;//new IdTime[]{new IdTime(user.getObjectID(), 0)};

        if (newProjectEmployees == null) {
            EdsProjectEmployee proEmp = projectEmployeeManager.getProjectEmployee(user.getEmployee(), projectManager.get(taskItem.getProjectID()));
            newProjectEmployees = proEmp != null ? new IdTime[]{new IdTime(proEmp.getObjectID(), 0)} : null;
        }

        if (isNew) {
            newProjectEmployeesList = newProjectEmployees != null ? new ArrayList<>(Arrays.asList(newProjectEmployees)) : null;
            final Date createTaskStart = new Date();
            if (taskItem.getRecurrenceJobItem() == null) {
                task = saveTaskDetailed(taskItem, user);// it saved task with assignees and Recurrence
            } else {
                //Recurrence and fire time is saved when the task is saved
                EdsRecurrence recurrence = new EdsRecurrence();
                recurrenceService.wrapRecurrenceJobItemToEdsRecurrence(taskItem.getRecurrenceJobItem(), recurrence, recurrenceJobManager.get(RECURRING_TASK));
                long timeDiff = taskItem.getDueDate().getTime() - taskItem.getStartDate().getTime();
                Date sd = (Date) taskItem.getStartDate().clone();
                recurrence.setStartDate(sd);  // this need for allDay tasks
                List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);
                if (recurringDates != null && !recurringDates.isEmpty()) {
                    recurringDateSize = recurringDates.size();
                    taskItem.setStartDate(recurringDates.get(0));
                    taskItem.setDueDate(new Date(recurringDates.get(0).getTime() + timeDiff));
                    task = saveTaskDetailed(taskItem, user);
                    EdsRecurrence taskRecurrence = recurrenceManager.get(task.getRecurrenceID());

                    baseEventPostProcessor.registerEvent(RecurringBgTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user, taskRecurrence);
                }
            }
        } else {
            oldProjectEmployees = wrapTaskAssigneesToIdTime(task);
            oldProjectEmployeesList = new ArrayList<>(Arrays.asList(oldProjectEmployees));
            newProjectEmployeesList = newProjectEmployees != null ? new ArrayList<>(Arrays.asList(newProjectEmployees)) : new ArrayList<>();
            ServerUtils.intersect(newProjectEmployeesList, oldProjectEmployeesList);
            if (isRecurring) {
                if (isRecurringRemoved) {
                    taskItem.setObjectID(task.getObjectID());
                    task = saveTaskDetailed(taskItem, user);
                    deleteAllInstances(task, false);
                } else if (isRecurringEdited) {
                    if (Constants.EDIT_ALL_SERIES.equals(taskItem.getAction())) {
                        EdsTask firstTask = taskManager.getFirstOrLastTaskInRecurringSeries(task.getRecurrenceID(), true);
                        Date startDate = (Date) firstTask.getStartDate().clone();
                        startDate = new Date(startDate.getTime() + startDateDiff);
                        deleteAllInstances(firstTask, false);
                        EdsRecurrence recurrence = new EdsRecurrence();
                        recurrenceService.wrapRecurrenceJobItemToEdsRecurrence(taskItem.getRecurrenceJobItem(), recurrence, recurrenceJobManager.get(RECURRING_TASK));
                        long timeDiff = taskItem.getDueDate().getTime() - taskItem.getStartDate().getTime();
                        recurrence.setStartDate(startDate);  // this need for allDay tasks
                        List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);
                        if (recurringDates != null && !recurringDates.isEmpty()) {
                            recurringDateSize = recurringDates.size();
                            taskItem.setObjectID(null);
                            task = saveTaskDetailed(taskItem, user);
                            if (recurringDates.size() > 1) {
                                if (recurringDates.size() > taskItem.getInstancesCount()) {
                                    List<Date> recDates = recurringDates.subList(0, taskItem.getInstancesCount() - 1);
                                    createTaskRecurringInstances(task, user, recurringDates);
                                    baseEventPostProcessor.registerEvent(RecurringTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user);
                                } else {
                                    createTaskRecurringInstances(task, user, recurringDates);
                                }
                            }
                        }
                    } else if (Constants.EDIT_ALL_FOLLOWING.equals(taskItem.getAction())) {
                        deleteAllInstances(task, true);
                        EdsRecurrence recurrence = new EdsRecurrence();
                        recurrenceService.wrapRecurrenceJobItemToEdsRecurrence(taskItem.getRecurrenceJobItem(), recurrence, recurrenceJobManager.get(RECURRING_TASK));
                        long timeDiff = taskItem.getDueDate().getTime() - taskItem.getStartDate().getTime();
                        Date sd = (Date) taskItem.getStartDate().clone();
                        recurrence.setStartDate(sd);  // this need for allDay tasks
                        List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);
                        if (recurringDates != null && !recurringDates.isEmpty()) {
                            recurringDateSize = recurringDates.size();
                            taskItem.setStartDate(recurringDates.get(0));
                            taskItem.setDueDate(new Date(recurringDates.get(0).getTime() + timeDiff));
                            task = saveTaskDetailed(taskItem, user);

                            if (recurringDates.size() > 1) {
                                List<Date> recDates = recurringDates.subList(1, recurringDates.size());
                                if (!recDates.isEmpty()) {
                                    createTaskRecurringInstances(task, user, recDates);
                                }
                            }
                        }
                    } else if (Constants.EDIT_THIS_INSTANCE.equals(taskItem.getAction())) {
                        saveTaskDetailed(taskItem, user);
                        recurringDateSize = 1;
                    }
                } else if (isRecurringAdded) {
                    task = saveTaskDetailed(taskItem, user);
                    // If new Recurring event has been saved, populate future instances
                    if (!isSeries) { // Important, might get in to recursive loop
                        createTaskRecurringInstances(task, user, taskItem.getInstancesCount());
                    }
                } else {
                    if (taskItem.getAction() == null || Constants.EDIT_THIS_INSTANCE.equals(taskItem.getAction())) {
                        task = saveTaskDetailed(taskItem, user);
                    } else if (Constants.EDIT_ALL_SERIES.equals(taskItem.getAction())) {
                        task = saveTaskDetailed(taskItem, user);
                        updateAllTaskInstances(task, false, startDateDiff, endDateDiff, user);
                    } else if (Constants.EDIT_ALL_FOLLOWING.equals(taskItem.getAction())) {
                        task = saveTaskDetailed(taskItem, user);
                        updateAllTaskInstances(task, true, startDateDiff, endDateDiff, user);
                    }
                    taskManager.update(task);
                }
            } else {
                task = saveTaskDetailed(taskItem, user);
            }
            /**
             * @see GoogleCalendarServiceImpl#synchronizeEvents(Integer, java.util.Date, java.util.Date)
             */
            employeeTaskManager.setEmployeeTasksModifiedDate(task, new Date());
        }

        try {
            if (!isSeries) {
                /*int taskEstimatedTime = 0;*/
                if (newProjectEmployeesList != null && !newProjectEmployeesList.isEmpty()) {
                    for (IdTime employeeID : newProjectEmployeesList) {
                        EdsProjectEmployee edsProjectEmployee = projectEmployeeManager.get(employeeID.getId());
                        if (edsProjectEmployee == null) {
                            continue;
                        }
                        EdsEmployee projectEmployee = projectEmployeeManager.get(employeeID.getId()).getEmployeeDepartment().getEmployee();
                        EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeTask(task.getObjectID(), projectEmployee.getObjectID());
                        /*if (employeeID.getTime() != null) {
                            taskEstimatedTime = taskEstimatedTime + employeeID.getTime();
                        }*/
                        if (employeeTask != null) {
                            EdsUser edsUser = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
                            boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(edsUser.getObjectID(), EmailNotificationConstants.TASK_ASSIGN_NOTIFICATION);
                            if (employeeTask != null && emailNotificationSettings &&
                                    !employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getAccountStatus().getCode().equals(EMPLOYEE_STATUS_NO_ACCCESS)) {
                                messageManager.sendTaskAssignNotification(employeeTask, edsUser);
                            }
                        }
                    }
                }
                baseEventPostProcessor.registerEvent(TaskRegisterUpdatesEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user);
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }

        if (task != null && task.getObjectID() != null) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsTask.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(task.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Added new task");
            taskItem.setTaskCreationTime(task.getCreationTime());
            taskItem.setLastModified(task.getLastUpdateTime());
            taskItem.setTaskCreator(task.getCreator() != null ? task.getCreator().getFullName() : referenceWfmMessageSource.localize("NA", "N/A"));
            taskItem.setLastModifiedBy(task.getUpdater() != null ? task.getUpdater().getFullName() : referenceWfmMessageSource.localize("NA", "N/A"));
            taskItem.setObjectID(task.getObjectID());
            return new Integer[]{task.getProject().getObjectID(), task.getObjectID(), task.getCreator() != null ? task.getCreator().getObjectID() : -1, recurringDateSize};
        } else {
            return new Integer[]{0, 0}; // For Mobile only returning 0
        }
    }

    public EdsTask saveTaskDetailed(TaskSingleItem newTask, EdsUser user) throws NumberExistingException {
        long begin = System.currentTimeMillis();
        Integer taskId = newTask.getObjectID();
        boolean isNew = (taskId == null);
        NumberData numberData = newTask.getNumberData();
        boolean taskDateChanged = false;

        if (isNew && (numberData == null || numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
            throw new NumberExistingException("Incorrect task number format.");
        }

        EdsTask task = new EdsTask();
        task.setNewItem(isNew);
        if (!isNew) {
            task = taskManager.get(taskId);
            if (numberData == null) {
                numberData = new NumberData(task.getNumber(), task.getIntNumber());
            }
            if (task.getNumber() != null && !"".equals(task.getNumber().trim()) && (numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
                throw new NumberExistingException("Incorrect task number format.");
            }
        }
        Date taskOldDueDate = task.getDueDate();
        if ((task.getStartDate() != null && task.getStartDate().getTime() != newTask.getStartDate().getTime()) ||
                (task.getDueDate() != null && task.getDueDate().getTime() != newTask.getDueDate().getTime())) {
            taskDateChanged = true;
        }

        if (isNew) {
            taskManager.create(task);
        } else {
            taskManager.update(task);
        }
        //Save Task with core and advanced fields
        wrapTaskSingleItemToEdsTask(newTask, task, user);
        if (isNew && task.getCreator() == null) {
            task.setCreator(user);
        }

        if (numberData.getNumberString() == null || numberData.getNumberString().isEmpty() || taskManager.isTaskNumberExists(numberData.getNumberString(), newTask.getProjectID(), newTask.getObjectID())) {
            numberData = generateTaskNumber(newTask.getProjectID(), newTask.getStartDate(), null);
        }

        if (numberData != null) {
            String fullNumber = numberData.getNumberString() != null ? numberData.getNumberString() : "";
            task.setNumber(fullNumber);
            if (numberData.getSavedNumberFormula() != null && !"".equals(numberData.getSavedNumberFormula())) {
                task.setSavedNumberFormula(numberData.getSavedNumberFormula());
            } else {
                task.setSavedNumberFormula(("".equals(numberData.getFirstNumberString()) ? "null" : numberData.getFirstNumberString()) + SAV_NUM_DEL + (numberData.getIntNumber() == null || "".equals(numberData.getIntNumber()) ? "null" : numberData.getIntNumber()) + SAV_NUM_DEL + ("".equals(numberData.getLastNumberString()) ? "null" : numberData.getLastNumberString()));
            }
            task.setIntNumber(numberData.getIntNumber());
        }

        //set kanbanboard order if its null
        if (task.getKanbanOrder() == null) {
            Long minKanbanOrderInStatus = taskManager.getMinKanbanOrder(task.getStatus() != null ? task.getStatus().getObjectID() : null);
            if (minKanbanOrderInStatus == null) {
                minKanbanOrderInStatus = CrmConstants.KANBAN_ORDER_GAP;
                task.setKanbanOrder(minKanbanOrderInStatus);
            } else {
                task.setKanbanOrder(minKanbanOrderInStatus - CrmConstants.KANBAN_ORDER_GAP);
            }
        }
        taskManager.update(task);
        crmServiceLocal.saveCrmNotes(RelationItem.TYPE_TASK, task.getObjectID(), newTask.getNotes());
        if (newTask.isWorkflowTask()) {
            task.setWorkflowID(newTask.getWorkflowRelationID());
            task.setWorkflowStartDate(newTask.getWorkflowStartDate());
            task.setWorkflowDueDate(newTask.getWorkflowDueDate());
            task.setWorkflowDueDateGranularity(newTask.getWorkflowDueDateGranularity());
            task.setWorkflowActionTimeBased(newTask.isWorkflowActionTimeBased());
            task.setWorkflowActionStartTime(newTask.isWorkflowActionTimeBased() ? newTask.getWorkflowActionStartTime() : null);
            task.setWorkflowActionStartTimeUnit(newTask.isWorkflowActionTimeBased() ? newTask.getWorkflowActionStartTimeUnit() : null);
            task.setWorkflowActionStartTimeGranularity(newTask.isWorkflowActionTimeBased() ? newTask.getWorkflowActionStartTimeGranularity() : null);
        }
        if (isNew && (Integer.valueOf(24899).equals(SecurityContext.getCompanyID())) && (task.getName() == null || "".equals(task.getName()))) {
            if (newTask.getCustomFieldItems() != null && !newTask.getCustomFieldItems().isEmpty()) {
                for (CompanyCustomFieldItem customField : newTask.getCustomFieldItems()) {
                    if (customField != null && customField.getAliasName() != null && customField.getAliasName().equals("task_category")) {
                        task.setName(customField.getFieldStringValue());
                    }
                }
            }
        }
        EdsTaskCustomFields edsTaskCusromFields = createTaskCustomFields(newTask.getCustomFieldItems());
        task.setTaskCustomFields(edsTaskCusromFields);
        //Save task Assignees, if no assignee try save the current user
        if (newTask.getProjectEmployees() != null && newTask.getProjectEmployees().length > 0) {
            if (isNew) {
                for (IdTime pem : newTask.getProjectEmployees()) {
                    EdsProjectEmployee pe = projectEmployeeManager.get(pem.getId());
                    if (pe != null) {
                        EdsEmployee employee = pe.getEmployeeDepartment().getEmployee();
                        if (user != null && employee != null && user.getObjectID().equals(employee.getObjectID())) {
                            pem.setStatusId(newTask.getStatusID());
                            pem.setTaskAmount(newTask.getTaskAmount());
                        }
                    }
                    if (newTask.getStatusID() != null) {// When adding a task if i set status to completed % completed is now shown as 100%;
                        EdsReference completedTaskStatus = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED);
                        if (completedTaskStatus != null && completedTaskStatus.getObjectID().equals(newTask.getStatusID())) {
                            if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                                pem.setPercent(100f);
                            }
                            pem.setStatusId(newTask.getStatusID());
                        }
                    }
                }

                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SINGLE_ASSIGNEE_TO_TASK)
                        && newTask.getProjectEmployees() != null && newTask.getProjectEmployees().length > 0
                        && newTask.getTaskAmount() != null && newTask.getTaskAmount().compareTo(BigDecimal.ZERO) > 0) {
                    newTask.getProjectEmployees()[0].setTaskAmount(newTask.getTaskAmount());
                    task.setTaskAmount(newTask.getTaskAmount());
                }

            }
            updateTaskAssignees(task, newTask.getProjectEmployees(), user, true);


            updateTaskDailyLoad(task, newTask.getProjectEmployees()); //update task daily load for the all undeleted task assignees

        } else if (user instanceof EdsEmployee) {
            EdsProjectEmployee proEmp = projectEmployeeManager.getProjectEmployee(user.getEmployee(), projectManager.get(newTask.getProjectID()));
            if (!newTask.getWithoutAssignees() && proEmp != null) { // newTask.getWithoutAssignees() - Copy Workstream qiganda taskni unassigned qilib ko`chirishuchu qilingan
                IdTime[] projectEmployees = new IdTime[1];
                IdTime idTime = new IdTime(proEmp.getObjectID(), 0);
                projectEmployees[0] = idTime;
                updateTaskAssignees(task, projectEmployees, user, isNew);
            }
        }

        //Update Predecessor/Successsor Task Dependencies
        updateTaskDependencies(newTask, task);

        //Save time spent today for current user if specified from UI
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SINGLE_ASSIGNEE_TO_TASK)
                && newTask.getProjectEmployees() != null && newTask.getProjectEmployees().length > 0
                && newTask.getActualTime() != null && newTask.getActualTime() > 0) {
            EdsProjectEmployee pe = projectEmployeeManager.get(newTask.getProjectEmployees()[0].getId());
            EdsEmployee employee = pe.getEmployeeDepartment().getEmployee();
            saveTaskTimeSpentToday(task, employee, newTask.getActualTime());
        } else if (user != null && user.isEmployee()) {
            if (newTask.getActualTime() != null && newTask.getActualTime() > 0) {
                saveTaskTimeSpentToday(task, user, newTask.getActualTime());
            }
        }

        //Update Task's overall status, and projects status
        log.info("Update Task's overall status, and projects status");
        updateTaskStatus(task);

        //Save Task's Attachments
        if (newTask.getAttachments() != null && newTask.getAttachments().length > 0) {
            saveTaskAttachments(newTask.getAttachments(), task);
        }

        //Save Task's Recurrence //
        Integer recurrenceId = newTask.getRecurrenceId();
        if (newTask.getRecurrenceJobItem() != null) {
            newTask.getRecurrenceJobItem().setBusObjectId(task.getObjectID());
            newTask.getRecurrenceJobItem().setJobType(RECURRING_TASK);
            recurrenceId = recurrenceService.saveRecurrenceJob(newTask.getRecurrenceJobItem());
        }
        if (recurrenceId != null) {
            EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
            task.setRecurrenceID(recurrenceId);
            recurrence.setChanged(false);
            if (newTask.getFireTime() != null) {
                task.setFireTime(newTask.getFireTime());
            } else {
                task.setFireTime(recurrence.getStartDate());
            }
            taskManager.update(task);
        }

        // Save Task Reminders
        saveTaskReminder(task.getObjectID(), user.getCompany(), newTask.getReminder());

        taskRbacManager.addRbacEntries(task);
        if (newTask.isRelationChanged()) {
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_TASK, task.getObjectID(), task.getName(), newTask.getRelations());
        }
        EdsBusinessEvent taskBusinessEvent;
        if (isNew) {
            taskBusinessEvent = baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user);
            workflowEvent.setEntityType(RelationItem.TYPE_TASK);
            try {
                taskSolrComponent.index(task);
                taskBusinessEvent.setSolrIndexed(true);
            } catch (Exception ex) {
                taskBusinessEvent.setSolrIndexed(false);
            }
        } else {
            taskBusinessEvent = baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
            workflowEvent.setEntityType(RelationItem.TYPE_TASK);
        }
        EdsCompany company = user.getCompany();

        if ((isNew && (newTask.getPredecessorTasks() != null && newTask.getPredecessorTasks().length != 0 ||
                newTask.getSuccessorTasks() != null && newTask.getSuccessorTasks().length != 0)) || taskDateChanged) {
            updateTaskDates(newTask.isDontKeepDelays(), user, task, taskOldDueDate);

        }
        log.info("SAVE TASK TOOK - " + (System.currentTimeMillis() - begin) + "ms, cid=" + company.getObjectID());
        return task;
    }

    public void updateTaskDates(boolean isDontKeepDelays, EdsUser user, EdsTask task, Date taskOldDueDate) {
        //Update current task's  start/end date based on the predecessor tasks latest due date
        //if there is a succeeding tasks of this new task then assign new start and end date to each of them
        shiftAllSuccessors(task, null, user, isDontKeepDelays, 0, taskOldDueDate, availabilityCircularResolver.getUserTimeSlot(user));
        //in case there is no ParentWS for task we need to find its predecessors or successors ParentWS
        Set<EdsWorkStream> workStreams = new HashSet<>();
        if (!task.getPredecessors().isEmpty()) {
            findPredecessorParentWS(task, workStreams);
        }
        if (!task.getSuccessors().isEmpty()) {
            findSuccessorParentWS(task, workStreams);
        }
        if (task.getParentWS() != null) {
            workStreams.add(task.getParentWS());
        }
        for (EdsWorkStream ws : workStreams) {
            updateWorkStreamDateRange(null, ws);
        }
    }

    public void shiftAllSuccessors(EdsTask task, EdsTask precedingTask, EdsUser user, boolean dontKeepDelays, long delayInMilliseconds, Date firstTaskOldDate, Map<Integer, Integer> defaultTimeslot) {
        Date latestDueDate = null;
        if (!task.getDeleted()) {
            latestDueDate = taskManager.getTaskPredecessorsMaxLastDueDate(task.getObjectID());
            if (latestDueDate != null && task.isAllDay()) {
                Calendar ldd = ServerUtils.convertDateIntoCalendar(latestDueDate);
                ldd.add(Calendar.DAY_OF_MONTH, 1);
                latestDueDate = ldd.getTime();
            }
        }
        //precedingTask might be a new Task therefore we should also consider its due date to determine the latest due date
        if (precedingTask != null) {
            //if precedingTask is not deleted then we should update latestDueDate to precedingTask's due date
            if (!precedingTask.getDeleted()) {
                if (latestDueDate == null || latestDueDate.getTime() < precedingTask.getDueDate().getTime()) {
                    latestDueDate = precedingTask.getDueDate();
                }
            }
            //if precedingTask is being deleted and it is the task's only predecessor we need to move task's start date to precedingTask's start date
            else if (task.getPredecessors().size() == 1) {
                if (latestDueDate == null || latestDueDate.getTime() < precedingTask.getStartDate().getTime()) {
                    Calendar taskStartDate = ServerUtils.convertDateIntoCalendar(precedingTask.getStartDate());
                    taskStartDate.add(Calendar.SECOND, -1);
                    latestDueDate = taskStartDate.getTime();
                }
            }
        }

        if (latestDueDate == null || latestDueDate.getTime() < task.getStartDate().getTime()) {
            Calendar taskStartDate = ServerUtils.convertDateIntoCalendar(task.getStartDate());
            taskStartDate.add(Calendar.SECOND, -1);
            latestDueDate = taskStartDate.getTime();
        }
        //in case Delays should remain the same we need to keep those delays exactly as it was before
        if (!dontKeepDelays && precedingTask != null) {
            if (latestDueDate.getTime() - precedingTask.getDueDate().getTime() != delayInMilliseconds) {
                Calendar latestDueDateWithDelay = ServerUtils.convertDateIntoCalendar(precedingTask.getDueDate());
                latestDueDateWithDelay.add(Calendar.SECOND, (int) (delayInMilliseconds / 1000));//upto 68 years of difference is fine, then cast into int will fail
                latestDueDate = latestDueDateWithDelay.getTime();
            }
        }
        if (latestDueDate != null) {
            //we need to add 1 second to make due date a new start date to the succeeding task
            Calendar newStartDate = ServerUtils.convertDateIntoCalendar(latestDueDate);

            //If successor task's start date is on weekend, we shift it to next day
            while (defaultTimeslot != null && defaultTimeslot.get(newStartDate.get(Calendar.DAY_OF_WEEK) - 1) == 0) {
                newStartDate.add(Calendar.DAY_OF_WEEK, 1);
            }

            newStartDate.add(Calendar.SECOND, 1);
            //new due date will be the difference of task's due date and start date added to the new start date
            Date taskOldDueDate = task.getDueDate();
            Date newDueDate = new Date(newStartDate.getTimeInMillis() + (task.getDueDate().getTime() - task.getStartDate().getTime()));
            task.setStartAndDueDates(newStartDate.getTime(), newDueDate);
            addTaskToSolr(task, user);
            //lastDueDate != null means there were change to the current task's dates, so we need to shift this tasks successors also
            for (EdsTask succeedingTask : task.getSuccessors()) {
                if (!succeedingTask.getDeleted()) {
                    long delay = 0;
                    if (!dontKeepDelays && precedingTask != null) {
                        delay = succeedingTask.getStartDate().getTime() - taskOldDueDate.getTime() - 1000;
                    } else if (!dontKeepDelays && firstTaskOldDate != null) {
                        delay = succeedingTask.getStartDate().getTime() - firstTaskOldDate.getTime() - 1000;
                    }
                    shiftAllSuccessors(succeedingTask, task, user, dontKeepDelays, delay, null, defaultTimeslot);
                }
            }
        }
        //if task doesn't have predecessors we still need to move its successors
        // because it might be a new task with old tasks assigned as successors or simply task start and due dates might have been changed
        else if (task.getPredecessors().isEmpty()) {
            for (EdsTask succeedingTask : task.getSuccessors()) {
                if (!succeedingTask.getDeleted()) {
                    long delay = 0;
                    if (!dontKeepDelays && precedingTask != null) {
                        delay = succeedingTask.getStartDate().getTime() - task.getDueDate().getTime() - 1000;
                    } else if (!dontKeepDelays && firstTaskOldDate != null) {
                        delay = succeedingTask.getStartDate().getTime() - firstTaskOldDate.getTime() - 1000;
                    }
                    shiftAllSuccessors(succeedingTask, task, user, dontKeepDelays, delay, null, defaultTimeslot);
                }
            }
        }
    }

    private void addTaskToSolr(EdsTask task, EdsUser user) {
        EdsBusinessEvent taskBusinessEvent = baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
    }

    public void updateWorkStreamDateRange(EdsTask task, EdsWorkStream workStream) {
        Date newStartDate = null;
        Date newEndDate = null;
        List<EdsTask> tasks;
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        if (workStream == null) {
            newStartDate = task.getStartDate();
            newEndDate = task.getDueDate();
            filterParameter.setWorkstreamID(task.getParentWS().getObjectID());
            tasks = taskManager.getOrderByTask(filterParameter);
        } else {
            filterParameter.setWorkstreamID(workStream.getObjectID());
            tasks = taskManager.getOrderByTask(filterParameter);
        }

        for (EdsTask t : tasks) {
            if (newStartDate == null || t.getStartDate().getTime() < newStartDate.getTime()) {
                newStartDate = t.getStartDate();
            }
            if (newEndDate == null || t.getDueDate().getTime() > newEndDate.getTime()) {
                newEndDate = t.getDueDate();
            }
        }
        if (workStream == null) {
            if (newStartDate != null) {
                task.getParentWS().setStartDate(newStartDate);
            }
            if (newEndDate != null) {
                task.getParentWS().setEndDate(newEndDate);
            }
        } else {
            if (newStartDate != null) {
                workStream.setStartDate(newStartDate);
            }
            if (newEndDate != null) {
                workStream.setEndDate(newEndDate);
            }
        }
    }

    public void findPredecessorParentWS(EdsTask task, Set<EdsWorkStream> workStreams) {
        if (!task.getPredecessors().isEmpty()) {
            for (EdsTask t : task.getPredecessors()) {
                if (t.getParentWS() != null) {
                    workStreams.add(t.getParentWS());
                }
                findPredecessorParentWS(t, workStreams);
            }
        }
    }

    public void findSuccessorParentWS(EdsTask task, Set<EdsWorkStream> workStreams) {
        if (!task.getSuccessors().isEmpty()) {
            for (EdsTask t : task.getSuccessors()) {
                if (t.getParentWS() != null) {
                    workStreams.add(t.getParentWS());
                }
                findSuccessorParentWS(t, workStreams);
            }
        }
    }

    @Transactional
    public EdsTaskCustomFields createTaskCustomFields(EdsTaskCustomFields edsTaskCustomField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsTaskCustomField == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsTaskCustomField = new EdsTaskCustomFields();
                taskCFManager.create(edsTaskCustomField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsTaskCustomField, customFieldItems);
            return edsTaskCustomField;
        }
        return null;
    }

    @Transactional
    public EdsTaskCustomFields createTaskCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            EdsTaskCustomFields edsTaskCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                edsTaskCustomFields = taskCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsTaskCustomFields = new EdsTaskCustomFields();
                taskCFManager.create(edsTaskCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsTaskCustomFields, customFieldItems);
            return edsTaskCustomFields;
        }
        return null;
    }

    private void saveTaskTimeSpentToday(EdsTask task, EdsUser user, Integer timeSpentToday) {
        for (EdsEmployeeTask employeeTask : task.getUnDeletedAssignments()) {
            if (employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID().equals(user.getEmployee().getObjectID()) && timeSpentToday != null && timeSpentToday > 0) {
                Date currentDate = new Date();
                currentDate.setMinutes(currentDate.getMinutes() + user.getUserTimezone().getRawOffset() / 60000);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime((Date) task.getStartDate().clone());
                ServerUtils.setBeginningOfTheDay(calendar);
                TimesheetDataItem timesheetItem = new TimesheetDataItem();
                timesheetItem.setDifference(timeSpentToday);
                timesheetItem.setEmployeeTaskID(employeeTask.getObjectID());
                timesheetItem.setDate(calendar.getTime());
                timesheetItem.setMinutes(timeSpentToday);
                timesheetService.applyUpdates(timesheetItem, null);

                task.setActualStartDate(calendar.getTime());
            }
        }
        if (task.getActualStartDate() == null) {
            task.setActualStartDate(new Date());
        }
    }

    private void updateAllTaskInstances(EdsTask task, boolean allFollowing, long startDateDiff, long endDateDiff, EdsUser user) {
        if (task.getRecurrenceID() != null) {
            List<EdsTask> allTaskInstances;
            if (allFollowing) {
                allTaskInstances = taskManager.getAllTaskInstancesAfter(task.getRecurrenceID(), task.getFireTime());
            } else {
                allTaskInstances = taskManager.getAllTaskInstances(task.getRecurrenceID());
            }
            TaskSingleItem taskItem = wrapEdsTaskToTaskSingleItem(task, user);
            for (EdsTask taskInstance : allTaskInstances) {
                if (!taskInstance.getObjectID().equals(task.getObjectID())) {
                    taskInstance.clear();
                    taskItem.setStartDate(new Date(taskInstance.getStartDate().getTime() + startDateDiff));
                    taskItem.setDueDate(new Date(taskInstance.getDueDate().getTime() + endDateDiff));
                    taskItem.setFireTime(taskInstance.getFireTime());
                    wrapTaskSingleItemToEdsTask(taskItem, taskInstance, user);
                    taskManager.update(taskInstance);
                    updateTaskAssignees(taskInstance.getObjectID(), taskItem.getProjectEmployees(), user.getObjectID());
                    taskRbacManager.addRbacEntries(task);
                    baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
                }
            }
        }
    }

    private void createTaskRecurringInstances(EdsTask task, EdsUser user, Integer instancesCount) {
        EdsTask lastTask = taskManager.getFirstOrLastTaskInRecurringSeries(task.getRecurrenceID(), false);
        if (lastTask.getRecurrenceID() != null) {
            EdsRecurrence recurrence = recurrenceManager.get(lastTask.getRecurrenceID());
            if (recurrence != null) {
                List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);

                if (instancesCount == null) {
                    instancesCount = recurringDates.size();
                }
                if (recurringDates != null && !recurringDates.isEmpty()) {
                    createTaskRecurringInstances(lastTask, user, recurringDates.subList(0, recurringDates.size() >= instancesCount ? instancesCount : recurringDates.size()));
                    if (recurringDates.size() > instancesCount) {
                        baseEventPostProcessor.registerEvent(RecurringTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, lastTask, user);
                    }
                }
            }
        }
    }

    @Transactional
    public void createRecurringTask() {
        ArrayList<EdsRecurrence> eventRecurrences = recurrenceManager.getFeaturedItemsRecurrences(RECURRING_TASK);
        if (eventRecurrences != null && !eventRecurrences.isEmpty()) {
            for (EdsRecurrence recurrence : eventRecurrences) {
                ServerSecurityContext.getInstance().setCompanyId(recurrence.getCompanyID());
                ServerSecurityContext.getInstance().setDatabase(jdbcSpringManager.getCompanyClusterType(recurrence.getCompanyID()));
                try {
                    EdsTask task = taskManager.get(recurrence.getBusObjectId());
                    EdsUser user = task.getCreator();
                    if (!task.getDeleted() && user != null && user.getCompany().getActive() && !user.getDeleted()) {
                        ServerSecurityContext.getInstance().setStaticUserID(user.getObjectID());

                        EdsRecurrence tempRecurrence = recurrence.cloneShallow();
                        tempRecurrence.setStartDate(recurrence.getExtendDate());
                        tempRecurrence.setOccurrence(Integer.valueOf(tempRecurrence.getBusObjectParams()) + CREATE_EVENT_INDEX);
                        List<Date> recurringDates = recurrenceService.getRecurringDates(tempRecurrence);

                        createTaskRecurringInstances(task, recurrence, recurringDates.subList(CREATE_EVENT_INDEX, recurringDates.size()));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ServerSecurityContext.getInstance().setStaticUserID(null);
                ServerSecurityContext.getInstance().removeCompanyId();
            }
        }
    }


    @Transactional
    public void createTaskRecurringInstancesBg(Integer taskID, Integer userID) {
        EdsRecurrence rec = recurrenceManager.get(taskManager.get(taskID).getRecurrenceID());
        if (rec != null) {
            List<Date> recurringDates = recurrenceService.getRecurringDates(rec);
            createTaskRecurringInstances(taskManager.get(taskID), rec, recurringDates);
        }
    }


    @Transactional
    public void createTaskRecurringInstances(EdsTask task, EdsRecurrence recurrence, List<Date> recurringDates) {
        List<Date> recDates = recurringDates;
        if (recurringDates != null && !recurringDates.isEmpty()) {
            if (recurringDates.size() > CREATE_EVENT_LIMIT) {
                recDates = recurringDates.subList(1, CREATE_EVENT_LIMIT);
                recurrence.setBusObjectParams(String.valueOf(recurringDates.size() > CREATE_EVENT_LIMIT ? recurringDates.size() - CREATE_EVENT_LIMIT : recurringDates.size()));
                recurrence.setExtendDate(recurringDates.get(CREATE_EVENT_LIMIT - CREATE_EVENT_INDEX));
            } else {
                recurrence.setBusObjectParams(null);
                recurrence.setExtendDate(null);
            }
            recurrenceManager.update(recurrence);
            recurrenceManager.flush();
            createTaskRecurringInstances(task, task.getCreator(), recDates);
        }
    }

    public void createTaskInstance(Integer taskId, Integer userId) {
        EdsTask task = taskManager.get(taskId);
        if (task.getRecurrenceID() != null) {
            EdsUser user = userManager.get(userId);
            EdsTask lastTask = taskManager.getFirstOrLastTaskInRecurringSeries(task.getRecurrenceID(), false);
            EdsRecurrence recurrence = recurrenceManager.get(lastTask.getRecurrenceID());
            if (recurrence != null) {
                EdsRecurrence cloneRecurrence = recurrence.cloneShallow();
                List<Date> recurringDates = recurrenceService.getRecurringDates(cloneRecurrence);
                if (recurringDates != null && !recurringDates.isEmpty()) {
                    List<Date> recDates = recurringDates.subList(1, recurringDates.size());
                    createTaskRecurringInstances(lastTask, user, recDates);
                }
            }
        }
    }

    @Override
    public void mergeTaskAccounts(Integer objectID, ArrayList<Integer> otherObjectIDs) {
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(objectID);
        EdsCompany edsCompany = userManager.getUser().getCompany();
        if (otherObjectIDs != null) {
            List<EdsRelation> taskRelations = relationManager.getRelationsByRelationFromTypeToID(RelationItem.TYPE_TASK, objectID);
            for (EdsRelation taskRelation : taskRelations) {
                EdsTask task = taskManager.get(taskRelation.getFromID());
                try {
                    taskSolrComponent.index(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createTaskRecurringInstances(final EdsTask task, final EdsUser user, List<Date> recurringDates) {
        Integer recurrenceID = task.getRecurrenceID();
        long dateDiff = task.getDueDate().getTime() - task.getStartDate().getTime();
        if (recurrenceID != null && recurringDates != null && !recurringDates.isEmpty()) {
            final int flushLimit = 10;
            int flushCount = 0;
            System.err.println("for start:" + new Date());
            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
            boolean isUniqueNumber = false;
            if (settings != null && settings.getTaskNumberingFormat() != null) {
                isUniqueNumber = settings.isUniqueNumber(settings.getTaskNumberingFormat(), WIDGET_DATE_YEAR, WIDGET_UNIQUE_NUMBER_ALL_PROJECT);
            }

            Map<Integer, Integer> defaultTimeslot = availabilityCircularResolver.getUserTimeSlot(user);
            EdsRecurrence rec = recurrenceManager.get(recurrenceID);
            Set<Date> createdDates = new HashSet<>();
            outerloop:
            for (Date recurringDate : recurringDates) {
                EdsTask checkTask = taskManager.getTaskInstance(recurrenceID, recurringDate);
                TaskSingleItem taskItem = wrapEdsTaskToTaskSingleItem(task, user);
                taskItem.setObjectID(null);
                taskItem.setRecurrenceJobItem(null);
                Calendar startDate = ServerUtils.convertDateIntoCalendar(recurringDate);
                if (rec.getType() != SchedulerConstant.RECURRENCE_TYPE_WEEKLY) {
                    while (defaultTimeslot != null && defaultTimeslot.get(startDate.get(Calendar.DAY_OF_WEEK) != 7 ? startDate.get(Calendar.DAY_OF_WEEK) : 0) == 0) {//Day off
                        if (rec.getInterval() > 1 || rec.getType() != SchedulerConstant.RECURRENCE_TYPE_DAILY) {
                            startDate.add(Calendar.DAY_OF_WEEK, 1);
                        } else {
                            if (rec.getEndType() == SchedulerConstant.END_AFTER_OCCURRENCES) {
                                startDate.add(Calendar.DAY_OF_WEEK, 1);
                            } else {
                                continue outerloop;
                            }
                        }
                    }
                }
                while (createdDates.contains(startDate.getTime()) || defaultTimeslot.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) == 0) {
                    startDate.add(Calendar.DAY_OF_WEEK, 1);
                }
                createdDates.add(startDate.getTime());

                taskItem.setStartDate(startDate.getTime());
                taskItem.setDueDate(new Date(startDate.getTime().getTime() + dateDiff));
                taskItem.setFireTime(recurringDate);
                for (IdTime assignee : taskItem.getProjectEmployees()) {
                    assignee.setGoogleID(null);
                }

                Integer intNumber = taskManager.getProjectTasksLastIntNumber(taskItem.getProjectID(), isUniqueNumber);
                String pojectNumber = "";
                Integer pojectClientCode = null;
                if (taskItem.getProjectID() != null) {
                    Object[] tt = projectManager.getProjectNumberById(taskItem.getProjectID()).get(0);
                    pojectNumber = tt[0] != null ? tt[0].toString() : "";
                    pojectClientCode = tt[1] != null ? (Integer) tt[1] : null;
                }
                String clientCode = null;
                if (pojectClientCode != null) {
                    clientCode = crmAccountManager.getCrmAccountNumberById(pojectClientCode).get(0);
                }
                NumberData taskNumber;
                if (settings != null && settings.getTaskNumberingFormat() != null) {
                    taskNumber = settings.parseNumberDataForALL(intNumber, settings.getTaskNumberingFormat(), settings.getDelimetrTask(), null, clientCode, pojectNumber, "task");
                } else {
                    taskNumber = EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_TASK_PREFIX);
                }
                taskItem.setNumberData(taskNumber);
                if (checkTask == null) {
                    try {
                        saveTask(taskItem, user);
                    } catch (NumberExistingException e) {
                        e.printStackTrace();
                    }
                    flushCount++;
                    if (flushCount == flushLimit) {
                        System.err.println("flush start: " + new Date());
                        taskManager.flushAndClear();
                        flushCount = 0;
                        System.err.println("flush end: " + new Date());
                    }
                }
            }
            System.err.println("for end: " + new Date());
            System.err.println("commit end: " + new Date());
        }
    }

    /**
     * Will wrap TaskSingleItem Transfer Object to EdsTask for add/edit task purposes
     * <br/><b>Note:</b> It will NOT wrap task assignees, predecessor/successor dependencies in this method.
     * They should be saved separately after the task has been saved
     * <br/><b>Note:</b> EdsProject fields of EdsTask is set only for new tasks, and is <b>NOT</b> set for existing tasks which already has project
     *
     * @param taskItem
     * @param task
     * @see TaskServiceImpl#updateTaskAssignees(Integer, com.edatasite.workforce.gwt.core.client.rpc.IdTime[])
     * @see TaskServiceImpl#updateTaskDependencies(com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem, com.edatasite.workforce.core.domain.EdsTask)
     */
    private void wrapTaskSingleItemToEdsTask(TaskSingleItem taskItem, EdsTask task, EdsUser user) {
        // Core Task attributes
        if (taskItem.getProjectID() != null && task.getProject() == null) {
            //Project can be set for new tasks only, you can't edit tasks project
            task.setProject(projectManager.get(taskItem.getProjectID()));
        }
        task.setName(taskItem.getName());
        task.setDescription(taskItem.getDescription());
        task.setStartAndDueDates(taskItem.getStartDate(), taskItem.getDueDate());
        task.setLastModifiedDate(new Date());
        if (taskItem.getStatusID() != null) {
            if (task.getStatus() == null || task.getStatus().getObjectID() != taskItem.getStatusID()) {
                insertTaskStatusHistory(task, referenceManager.get(taskItem.getStatusID()), user, null);
            }
            task.setStatus(referenceManager.get(taskItem.getStatusID()));
        } else {
            insertTaskStatusHistory(task, referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED), user, null);
            task.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED));
        }
        if (taskItem.getTypeID() != null) {
            task.setType(referenceManager.get(taskItem.getTypeID()));
        }

        if (taskItem.getPriorityID() != null) {
            task.setPriority(referenceManager.get(taskItem.getPriorityID()));
        } else {
            task.setPriority(referenceManager.getByCode(EdsTask.MEDIUM));
        }
        task.setBillable(taskItem.getBillable());

        //Calendar and Recurring related fields
        if (taskItem.getFireTime() != null) {
            task.setFireTime(taskItem.getFireTime());
        }
//        task.setRecurrence(null);
        task.setAllDay(taskItem.isAllDay());
        if (taskItem.getReminderTimes() != null) {
            task.setReminderTime(taskItem.getReminderTimes());
        }

        //Advanced task fields
        changeWorkstream(task, taskItem.getWorkstreamID());

        //set Task external GUID (the part for the Export Task data to QB)
        UUID externalGUID = UUID.randomUUID();
        task.setExternalGUID(externalGUID.toString());
    }

    private void changeWorkstream(EdsTask task, Integer workstreamID) {
        if (workstreamID != null) {
            EdsWorkStream workstream = workStreamManager.get(workstreamID);

            if (task.getParentWS() != null && !task.getParentWS().equals(workstream)) {
                clearOldParentWSCalculatedItemsOfTask(task);
            } else if (task.getParentWS() == null) {
                task.setChangedCalculationFields(true);
                task.setCalculated(false);
            }
            task.setParentWS(workstream);
        } else if (task.getParent() != null) {
            clearOldParentWSCalculatedItemsOfTask(task);
            task.setParentWS(null);
        }
    }

    /**
     * Will wrap EdsTask to TaskSingleItem Transfer Object for the specied user.<br/>
     * This should be used to transfer EdsTask from serverside to UI for edit purposes<br/>
     * It will also wrap task assignees and all other fields.<br/>
     *
     * @param task
     * @param user - EdsUser who is trying to access the task entity, and permission will be set based on this user priveleges which will be used on UI
     * @return
     */
    private TaskSingleItem wrapEdsTaskToTaskSingleItem(EdsTask task, EdsUser user) {
        TaskSingleItem taskItem = new TaskSingleItem();
        // Core Task attributes
        taskItem.setObjectID(task.getObjectID());
        taskItem.setProjectID(task.getProject().getObjectID());
        taskItem.setProjectName(task.getProject().getName());

        taskItem.setName(task.getName());
        taskItem.setDescription(task.getDescription());

        taskItem.setStartDate(task.getStartDate());
        taskItem.setDueDate(task.getDueDate());

        taskItem.setStatusID(task.getStatus() != null ? task.getStatus().getObjectID() : null);
        taskItem.setPriorityID(task.getPriority() != null ? task.getPriority().getObjectID() : null);
        taskItem.setBillable(task.getBillable());
        if (task.getType() != null) {
            taskItem.setTypeID(task.getType().getObjectID());
            taskItem.setTypeCode(task.getType().getCode());
            taskItem.setTypeName(task.getType().getName());
        }

        //More task display attributes for UI
        taskItem.setActualStartDate(task.getActualStartDate());
        taskItem.setActualEndDate(task.getActualEndDate());
        taskItem.setEstimatedTime(task.getEstimatedTime());
        taskItem.setPercent(task.getPercent());

        //Calendar and Recurring related fields
        taskItem.setFireTime(task.getFireTime());
        taskItem.setAllDay(task.isAllDay());
        if (task.getReminderTime() != null) {
            taskItem.setReminderTimes(task.getReminderTime());
        }
        if (task.getRecurrenceID() != null) {
            taskItem.setRecurrenceId(task.getRecurrenceID());
            EdsRecurrence recurrence = recurrenceManager.get(task.getRecurrenceID());
            RecurrenceJobItem recurrenceJobItem = recurrence.createRecurrenceItem(RECURRING_TASK);
            recurrenceJobItem.setEnabled(true);
            taskItem.setRecurrenceJobItem(recurrenceJobItem);
        }

        //Advanced task fields
        if (task.getParentWS() != null) {
            taskItem.setWorkstreamID(task.getParentWS().getObjectID());
            taskItem.setWorkstreamName(task.getParentWS().getName());
        }
        if (task.getPredecessors() != null && !task.getPredecessors().isEmpty()) {
            Integer projectId = task.getProject().getObjectID();
            TaskSelectItem[] predTasks = new TaskSelectItem[task.getPredecessors().size()];
            int i = 0;
            for (EdsTask predTask : task.getPredecessors()) {
                predTasks[i] = new TaskSelectItem();
                predTasks[i].setId(predTask.getObjectID());
                predTasks[i].setTaskNumber(predTask.getNumber());
                predTasks[i].setName(predTask.getName());
                predTasks[i].setTaskStartDate(predTask.getStartDate());
                predTasks[i].setTaskDueDate(predTask.getDueDate());
                predTasks[i].setAllDay(predTask.isAllDay());
                predTasks[i].setProjectId(projectId); // Not sure why needed project ID here??? task linkages should all be within the same project
                i++;
            }
            taskItem.setPredecessorTasks(predTasks);
        }
        if (task.getSuccessors() != null && !task.getSuccessors().isEmpty()) {
            Integer projectId = task.getProject().getObjectID();
            TaskSelectItem[] succTasks = new TaskSelectItem[task.getSuccessors().size()];
            int i = 0;
            for (EdsTask succTask : task.getSuccessors()) {
                succTasks[i] = new TaskSelectItem();
                succTasks[i].setId(succTask.getObjectID());
                succTasks[i].setTaskNumber(succTask.getNumber());
                succTasks[i].setName(succTask.getName());
                succTasks[i].setTaskStartDate(succTask.getStartDate());
                succTasks[i].setTaskDueDate(succTask.getDueDate());
                succTasks[i].setAllDay(succTask.isAllDay());
                succTasks[i].setProjectId(projectId); // Not sure why needed project ID here??? task linkages should all be within the same project
                i++;
            }
            taskItem.setSuccessorTasks(succTasks);
        }

        //task relations
        taskItem.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_TASK, taskItem.getObjectID())));

        //Task Assignees
        IdTime[] projectEmployees = wrapTaskAssigneesToIdTime(task);
        taskItem.setProjectEmployees(projectEmployees);

        // Set Task Permissions to be used on UI side
        if (task.getProject().getManager().equals(user) || task.getProject().isUserBackupManager(user.getObjectID())) {
            taskItem.setPermission(EDIT);
        } else if (user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsRole.ADMIN)) || user.hasRole(roleManager.get(EdsRole.TL))) {
            taskItem.setPermission(EDIT);
        } else {
            if (task.getCreator() == null) {
                task.setCreator(user);
            }
            if (task.getCreator().equals(user)) {
                taskItem.setPermission(EDIT);
            } else {
                taskItem.setPermission(READ);
            }
        }
        taskItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(), commonService.getCompanyCustomFields(ViewName.Task)));
        return taskItem;
    }

    private IdTime[] wrapTaskAssigneesToIdTime(EdsTask task) {
        Set<EdsEmployeeTask> employeeTasks = task.getUnDeletedAssignments();
        IdTime[] projectEmployees = new IdTime[employeeTasks.size()];
        int i = 0;
        for (EdsEmployeeTask employeeTask : employeeTasks) {
            projectEmployees[i] = new IdTime(
                    employeeTask.getProjectEmployee().getObjectID(),
                    employeeTask.getEstimatedTime(),
                    employeeTask.getTimeSpent(),
                    employeeTask.getPercent(),
                    employeeTask.getStatus() != null ? employeeTask.getStatus().getObjectID() : null,
                    employeeTask.getGoogleID());
            i++;

        }
        return projectEmployees;
    }

    private SelectItem[] wrapTaskListToSelectItem(Set<EdsTask> taskSet) {
        if (taskSet != null) {
            List<EdsTask> taskList = new ArrayList<>(taskSet);
            return wrapTaskListToSelectItem(taskList);
        } else {
            return null;
        }
    }

    private List<EdsTask> wrapSelectItemsToTaskList(SelectItem[] selectItems) {
        List<EdsTask> tasks = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem selectItem : selectItems) {
                if (selectItem != null) {
                    tasks.add(taskManager.get(selectItem.getId()));
                }
            }
        }
        return tasks;
    }

    private SelectItem[] wrapTaskListToSelectItem(List<EdsTask> taskList) {
        if (taskList != null) {
            SelectItem[] selectItems = new SelectItem[taskList.size()];
            for (int i = 0; i < selectItems.length; i++) {
                selectItems[i] = wrapTaskToSelectItem(taskList.get(i));
            }
            return selectItems;
        } else {
            return null;
        }
    }

    private SelectItem wrapTaskToSelectItem(EdsTask task) {
        return new SelectItem(task.getObjectID(), task.getName(), task.getDescription());
    }

    private void saveIssueAttachments(FileItem[] attachments, EdsIssue issue) {
        EdsProject edsProject = issue.getProject();
        Integer folderID = edsProject != null ? edsProject.getObjectID() : null;

        if (folderID != null && attachments != null && attachments.length > 0) {
            attachmentUtilsManager.saveAttachments(F_PR_ISSUE, folderID, issue.getObjectID(), attachments);
        }
    }

    private void saveTaskAttachments(FileItem[] attachments, EdsTask task) {
        attachmentUtilsManager.saveAttachments(F_TASK, task.getProject().getObjectID(), task.getObjectID(), attachments);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getProjectID(Integer taskID) {
        EdsTask task = taskManager.get(taskID);
        return task.getProject().getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem getProjectByTask(Integer taskID) {
        EdsTask task = taskManager.get(taskID);
        EdsProject project = task.getProject();
        return new SelectItem(project.getObjectID(), project.getName());
    }

    private void saveTaskReminder(Integer taskID, EdsCompany company, ArrayList<CalendarEventReminder> reminders) {
        taskReminderManager.deleteTaskReminders(taskID);
        if (reminders != null && !reminders.isEmpty()) {
            EdsTask task = taskManager.get(taskID);
            List<EdsRecurrence> recurrenceList = recurrenceManager.getRecurrenceJobList(TASK_OVERDUE_REMINDER, taskID, company.getObjectID());
            if (recurrenceList != null && !recurrenceList.isEmpty()) {
                for (EdsRecurrence rec : recurrenceList) {
                    recurrenceService.updateRecurrence(rec, true, true);
                }
            }
            RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
            recurrenceJobItem.setEnabled(true);
            recurrenceJobItem.setType(RECURRENCE_TYPE_YEARLY);
            recurrenceJobItem.setJobType(TASK_OVERDUE_REMINDER);
            recurrenceJobItem.setBusObjectId(taskID);
            recurrenceJobItem.setInterval(1);
            recurrenceJobItem.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
            recurrenceJobItem.setEndType(END_BY_DATE);
            for (CalendarEventReminder eventReminder : reminders) {
                Date recStartDate = DateUtil.addMinutes(task.getDueDate(), (-1) * eventReminder.getReminderTimes());
                if (recStartDate.after(new Date())) {
                    recurrenceJobItem.setEndDate(DateUtil.addMinutes(recStartDate, 5));
                    recurrenceJobItem.setStartDate(recStartDate);
                    recurrenceJobItem.setBusObjectParams(eventReminder.getReminderTimes().toString());
                    recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                    recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());
                    if (Integer.valueOf(1).equals(eventReminder.getValue())) {
                        recurrenceJobItem.setStartDate(recStartDate);
                        recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                        recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());
                        recurrenceService.saveRecurrenceJob(recurrenceJobItem);
                    }

                    EdsTaskReminder reminder = new EdsTaskReminder();
                    reminder.setTask(task);
                    reminder.setReminderType(eventReminder.getValue());
                    reminder.setMinutes(eventReminder.getReminderTimes());
                    taskReminderManager.create(reminder);
                }
            }
        }
    }

    private void saveWorkstreamReminder(Integer workstreamID, EdsCompany company, ArrayList<CalendarEventReminder> reminders) {
        if (reminders != null && !reminders.isEmpty()) {
            itemReminderManager.deleteItemReminders(workstreamID, Constants.WORKSTREAM_REMINDER);
            EdsWorkStream workstream = workStreamManager.get(workstreamID);
            List<EdsRecurrence> recurrenceList = recurrenceManager.getRecurrenceJobList(WORKSTREAM_OVERDUE_REMINDER, workstreamID, company.getObjectID());
            if (recurrenceList != null && !recurrenceList.isEmpty()) {
                for (EdsRecurrence rec : recurrenceList) {
                    recurrenceService.updateRecurrence(rec, true, true);
                }
            }
            RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
            recurrenceJobItem.setEnabled(true);
            recurrenceJobItem.setType(RECURRENCE_TYPE_YEARLY);
            recurrenceJobItem.setJobType(WORKSTREAM_OVERDUE_REMINDER);
            recurrenceJobItem.setBusObjectId(workstreamID);
            recurrenceJobItem.setInterval(1);
            recurrenceJobItem.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
            recurrenceJobItem.setEndType(END_BY_DATE);
            for (CalendarEventReminder eventReminder : reminders) {
                Date recStartDate = DateUtil.addMinutes(workstream.getEndDate(), (-1) * eventReminder.getReminderTimes());
                if (recStartDate.after(new Date())) {
                    recurrenceJobItem.setEndDate(DateUtil.addMinutes(recStartDate, 5));
                    recurrenceJobItem.setStartDate(recStartDate);
                    recurrenceJobItem.setBusObjectParams(eventReminder.getReminderTimes().toString());
                    recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                    recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());
                    if (Integer.valueOf(1).equals(eventReminder.getValue())) {
                        recurrenceJobItem.setStartDate(recStartDate);
                        recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                        recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());
                        recurrenceService.saveRecurrenceJob(recurrenceJobItem);
                    }

                    EdsItemReminder reminder = new EdsItemReminder();
                    reminder.setItem(workstreamID);
                    reminder.setItemType(WORKSTREAM_REMINDER);
                    reminder.setReminderType(eventReminder.getValue());
                    reminder.setMinutes(eventReminder.getReminderTimes());
                    itemReminderManager.create(reminder);
                }
            }
        }
    }

    public void sendEmailNotification(Integer taskID) {
        try {
            EdsTask task = taskManager.get(taskID);
            if (task != null && !task.getDeleted()) {
                EdsReference completedStatus = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED);
                EdsReference closedStatus = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CLOSED);
                EdsReference taskStatus = task.getStatus();

                if (!taskStatus.getObjectID().equals(completedStatus.getObjectID()) && !taskStatus.getObjectID().equals(closedStatus.getObjectID())) {
                    EdsEmailTemplate edsEmailTemplate = emailTemplateManager.getDefaultEmailTemplateByCategory(TASK_REMINDER);
                    for (EdsEmployeeTask employeeTask : task.getUnDeletedAssignments()) {
                        EdsEmployee employee = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
                        if (employee != null && employee.getCompany().getActive() && !employee.getDeleted() && !employeeTask.getDeleted()) {
                            if (edsEmailTemplate != null) {
                                EmailTemplateItem templateItem = emailTemplateServiceLocal.generateEmailTemplateItemForTaskReminder(task, employeeTask, edsEmailTemplate);
                                if (templateItem == null) {
                                    messageManager.sendTaskOverDueDateReminder(task, employeeTask);
                                } else {
                                    messageManager.sendTaskOverDueReminder(task, employeeTask, templateItem);
                                }
                            } else {
                                messageManager.sendTaskOverDueDateReminder(task, employeeTask);
                            }
                        }
                    }
                }
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void sendWorkstreamEmailNotification(Integer workstreamID) {
        try {
            EdsWorkStream workstream = workStreamManager.get(workstreamID);
            if (workstream != null) {
                List<WorkstreamAssigneeItem> wsAssigneeEmployee = getWorkStreamEmployeeStatisticList(employeeTaskManager.getETStatisticByWS(workstreamID), false);
                if (wsAssigneeEmployee != null && !wsAssigneeEmployee.isEmpty()) {
                    for (WorkstreamAssigneeItem assignEmployee : wsAssigneeEmployee) {
                        EdsEmployee employee = employeeManager.get(assignEmployee.getEmployeeID());
                        if (employee != null && employee.getCompany().getActive() && !employee.getDeleted()) {
                            messageManager.sendWorkstreamOverDueDateReminder(workstream, employee);
                        }
                    }
                }
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    private void updateProjectStatus(EdsTask task) {
        EdsProject project = projectManager.get(task.getProject().getObjectID());
        EdsReference notStartedReference = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED);
        EdsReference ongoingReference = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING);
        if (project != null) {
            if (project.getStatus().equals(notStartedReference)) {
                project.setStatus(ongoingReference);
                project.setCompletedDate(null);
                projectManager.update(project);
                baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, roleManager.getUser());
            }
            // change parent project's status
            EdsProject parentProject = project.getParent();
            if (parentProject != null && parentProject.getStatus().equals(notStartedReference)) {
                parentProject.setStatus(ongoingReference);
                project.setCompletedDate(null);
                projectManager.update(parentProject);
                baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, parentProject, roleManager.getUser());
            }
        }
    }

    public void addNewProjectMembersAndAssignTasks(Integer projectId, ProjectMember[] members) {

        EdsUser user = userManager.getUser();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date changeDate = cal.getTime();

        EdsProject project = projectManager.get(projectId);
        List<IdTime> assignees = new ArrayList<>();
        for (ProjectMember member1 : members) {
            EdsEmployee employee = employeeManager.get(member1.getId());
            EdsEmployeeDepartment employeeDepartment = employee.getEmployeeTeam();
            if (employeeDepartment != null) {
                EdsProjectEmployee pe = new EdsProjectEmployee(employeeDepartment, project);
                pe.setClientChargeRate(member1.getClientChargeRate());
                pe.setWageRate(member1.getWageRate());
                pe.setWorkloadPercentage(member1.getWorkloadPercentage());

                if (member1.getPositionId() != null) {
                    pe.setPosition(positionManager.get(member1.getPositionId()));
                }
                EdsProjectEmployeeWageClientRateHistory prate = new EdsProjectEmployeeWageClientRateHistory();
                prate.setClientChargeRate(member1.getClientChargeRate());
                prate.setWorkloadPercentage(member1.getWorkloadPercentage());
                prate.setWageRate(member1.getWageRate());
                prate.setProjectEmployee(pe);
                prate.setChangeDate(changeDate);

                pe.getWageClientRatesHistory().add(prate);
                projectEmployeeManager.create(pe);
                baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pe, user);
                IdTime member = new IdTime(pe.getObjectID(), 0);
                assignees.add(member);
            }
        }
        IdTime[] newAssignees = assignees.toArray(new IdTime[]{});
        List<EdsTask> tasks = taskManager.getProjectTasks(project);
        for (EdsTask itask : tasks) {
            EdsTask task = taskManager.get(itask.getObjectID());
            task.clear();
            saveTaskAssignees(user, task, newAssignees, null);
            updateTaskStatus(task);
            taskManager.flushAndClear();
        }
    }

    private ArrayList<DailyLoadRequest> saveTaskAssignees(EdsUser user, EdsTask task, IdTime[] assignees, Integer statusId) {
        return saveTaskAssignees(user, task, assignees, statusId, null);
    }

    @Deprecated
    public ArrayList<DailyLoadRequest> saveTaskAssignees(EdsUser user, EdsTask task, IdTime[] assignees, Integer statusId, Float percentCompleted) {
        int taskEstimatedTime = 0;
        String shortDateFormat = "MM/dd/yyyy";
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        ArrayList<DailyLoadRequest> dailyLoadRequestArrayList = new ArrayList<>();
        if (assignees != null) {
            for (IdTime assignee : assignees) {
                EdsProjectEmployee pe = projectEmployeeManager.get(assignee.getId());
                EdsEmployeeTask empTask = new EdsEmployeeTask(task, pe);
                task.getAssignments().add(empTask);
                EdsEmployee edsEmployee = pe.getEmployeeDepartment().getEmployee();
                if (assignee.getTime() != null) {
                    taskEstimatedTime = taskEstimatedTime + assignee.getTime();
                    empTask.setEstimatedTime(assignee.getTime());
                    updateBudgetByEstimatedTime(task.getParentWS(), empTask, assignee.getTime());
                    Set<EdsTimeSlotItem> timeSlotItem = edsEmployee.getTimeSlot().getItems();
                    Map<Integer, Integer> available = new HashMap<>();
                    for (EdsTimeSlotItem item : timeSlotItem) {
                        available.put(item.getDay(), item.getEndTime() - item.getStartTime());
                    }
                    Calendar startDate = Calendar.getInstance();
                    Calendar dueDate = Calendar.getInstance();
                    startDate.setTime(new Date(task.getStartDate().getTime() + user.getUserTimezone().getRawOffset()));
                    dueDate.setTime(new Date(task.getDueDate().getTime() + user.getUserTimezone().getRawOffset()));
                    int k = 0;
                    ArrayList<Calendar> availableDays = new ArrayList<>();
                    while (dueDate.getTime().compareTo(startDate.getTime()) > 0) {
                        if (available.containsKey(startDate.get(Calendar.DAY_OF_WEEK) - 1)
                                && available.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) != null && available.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) != 0) {
                            k++;
                            Calendar nonDate = Calendar.getInstance();
                            nonDate.setTime(startDate.getTime());
                            ServerUtils.setBeginningOfTheDay(nonDate);
                            availableDays.add(nonDate);
                        }
                        startDate.add(Calendar.DAY_OF_YEAR, 1);
                    }
                    if (k == 0) {
                        k = 1;
                    }
                    int dailyLoad = assignee.getTime() / k;
                    int dailyLoadQ = assignee.getTime() % k;
                    empTask.setDailyLoad(dailyLoad);
                    //insert timeSheet data with daily estimated time
                    if (dailyLoad >= 0) {
                        DailyLoadRequest dailyLoadRequest = new DailyLoadRequest();
                        dailyLoadRequest.setDailyEmployee(edsEmployee);
                        dailyLoadRequest.setDailyEmployeeTask(empTask);
                        dailyLoadRequest.setDailyAvailableDays(availableDays);
                        dailyLoadRequest.setDailyLoadTime(dailyLoad);
                        dailyLoadRequest.setDailyLoadQ(dailyLoadQ);
                        dailyLoadRequestArrayList.add(dailyLoadRequest);
                    }

                }
                empTask.setStartDate(new Date());
                empTask.setLastModifiedDate(new Date());
                if (user.getObjectID().equals(edsEmployee.getObjectID())) {
                    if (percentCompleted != null) {
                        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                        if (settings == null || !settings.isAutomatic()) {//if settings is null then project percent calculation mode is manual by default
                            empTask.setPercent(percentCompleted);
                            Float average;
                            EdsProject project = new EdsProject();
                            project = projectManager.get(task.getProject().getObjectID());
                            Float averageProjectTasks;
                            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                                average = task.getTaskAveragePercentCompletedNewLogic();
                                averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompletedNewLogic();
                            } else {
                                average = task.getTaskAveragePercentCompleted();
                                averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompleted();
                            }
                            task.setPreviousPercent(task.getPercent());
                            task.setPercent(average);
                            project.setPercent(averageProjectTasks);
                            projectManager.update(project);
                        }
                    }
                    if (statusId != null && referenceManager.getReference(statusId) != null) {
                        empTask.setStatus(referenceManager.getReference(statusId));
                    } else {
                        empTask.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED));
                    }
                } else {
                    empTask.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED));
                }
                empTask.setNewTask(true);
                empTask.setDeleted(false);
            }
        }
        return dailyLoadRequestArrayList;
    }

    public Integer createIssueItem(IssueItem item) {
        EdsUser user = projectManager.getUser();

        EdsIssue issue;

        if (item.getObjectID() == null) {
            issue = new EdsIssue();
        } else {
            issue = issueManager.get(item.getObjectID());
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        //set issue params
        ArrayList<DailyLoadRequest> dailyLoadRequestArrayList = setIssueParams(item, issue, user);

        if (item.getObjectID() != null) {
            if (issue.getEdsIssue() == null) {
                issue.setEdsIssue(issue);
            }
            taskManager.update(issue);
            baseEventPostProcessor.registerEvent(IssueEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, issue, user);
            createIssueKPILOG(kpiLog, EdsIssue.class.getSimpleName(), issue.getObjectID(), KpiLog.ActionType.UPDATE, PROJECT_ISSUE, "Update issue");
        } else {
            taskManager.create(issue);
            issue.setEdsIssue(issue);
            baseEventPostProcessor.registerEvent(IssueEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, issue, user);
            createIssueKPILOG(kpiLog, EdsIssue.class.getSimpleName(), issue.getObjectID(), KpiLog.ActionType.ADD, PROJECT_ISSUE, "Create issue");
        }
        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            saveIssueAttachments(item.getAttachments(), issue);
        }

        if (item.getNotes() != null && !item.getNotes().isEmpty()) {
            crmServiceLocal.saveCrmNotes(RelationItem.PM_ISSUE, issue.getObjectID(), item.getNotes());
        }
        if (item.isRelationChanged()) {
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_ISSUE, issue.getObjectID(), issue.getName(), item.getRelations());
        }

        if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
            if (dailyLoadRequestArrayList != null && !dailyLoadRequestArrayList.isEmpty()) {
                for (DailyLoadRequest dailyLoadRequest : dailyLoadRequestArrayList) {
                    availabilityCircularResolver.createOrUpdateTimeSheetDataWithDailyEstimatedTime(dailyLoadRequest.getDailyEmployee(), dailyLoadRequest.getDailyEmployeeTask(), dailyLoadRequest.getDailyAvailableDays(), dailyLoadRequest.getDailyLoadTime(), dailyLoadRequest.getDailyLoadQ());
                }
            }
        }
        EdsTaskCustomFields issueCustomFields = createTaskCustomFields(item.getCustomFields());
        issue.setTaskCustomFields(issueCustomFields);


        return issue.getObjectID();
    }

    private void createIssueKPILOG(KpiLog kpiLog, String entityName, Integer entityID, KpiLog.ActionType actionType, String entityType, String message) {
        kpiLog.setEntityName(entityName);
        kpiLog.setActionType(actionType);
        kpiLog.setEntityId(entityID);
        kpiLog.setEntityType(entityType);
        ServerUtils.kpiLog(log, kpiLog, message);
    }

    private ArrayList<DailyLoadRequest> setIssueParams(IssueItem item, EdsIssue issue, EdsUser user) {
        ArrayList<DailyLoadRequest> dailyLoadRequestArrayList = new ArrayList<>();
        //issue numbering
        NumberData numberData = item.getNumberData();
        if (numberData == null || numberData.getNumberString() == null || numberData.getNumberString().isEmpty() || issueManager.isIssueNumberExists(numberData.getNumberString(), item.getObjectID())) {
            numberData = issueManager.generateIssueNumber();
        }
        if (numberData != null) {
            issue.setNumber(numberData.getNumberString());
            issue.setIntNumber(numberData.getIntNumber());
        }
        //isIssue
        issue.setIssue(true);
        //issue creator
        if (issue.getCreator() == null) {
            issue.setCreator(user);
        }
        //issue name
        issue.setName(item.getName());
        //issue description
        issue.setDescription(item.getDescription());
        //issue visibility
        issue.setAccess(item.isPublic() == null ? INTERNAL_ISSUE : (item.isPublic() ? PUBLIC_ISSUE : PRIVATE_ISSUE));
        //issue period start/end date
        issue.setStartAndDueDates(item.getStartDate(), item.getEndDate());
        //issue priority
        if (item.getPriorityID() != null) {
            issue.setPriority(referenceManager.get(item.getPriorityID()));
        } else {
            issue.setPriority(referenceManager.getByCode(EdsIssue.IS_MEDIUM));
        }
        //issue status
        if (item.getStatusID() != null) {
            issue.setIssueStatus(referenceManager.get(item.getStatusID()));
        } else {
            issue.setIssueStatus(referenceManager.getByCode(EdsIssue._NEW));
        }
        //issue reported by
        if (item.getReportedByID() != null) {
            issue.setReportedBy(userManager.get(item.getReportedByID()));
        }
        //issue resolver
        if (item.getResolverID() != null) {
            issue.setResolver(employeeManager.get(item.getResolverID()));
        }
        //issue project
        EdsProject oldProject = item.getObjectID() != null ? issue.getProject() : null;
        EdsProject newProject = null;
        if (item.getProjectID() != null) {
            if (item.getObjectID() != null) {
                newProject = projectManager.get(item.getProjectID());
                issue.setProject(newProject);
            } else {
                issue.setProject(projectManager.get(item.getProjectID()));
            }
        }
        Integer viewAsFilter = ServerUtils.getMaxRoleID(user.getRolesAsIntegersString());
        if (oldProject != null && newProject != null) {
            // --------------------------------------------------- Issue Rbac Entries ----------------------------------
            Integer projectID = issue.getProject() != null ? issue.getProject().getObjectID() : null;
            taskRbacManager.updateTaskRbacEntries(issue.getObjectID(), projectID);
            /*List<EdsTaskRbac> taskRbacEntries = taskRbacManager.getTaskRbacEntries(issue);
            if (taskRbacEntries != null && !taskRbacEntries.isEmpty()) {
                for (EdsTaskRbac taskRbac : taskRbacEntries) {
                    taskRbac.setProject(issue.getProject());
                    taskRbacManager.update(taskRbac);
                }
            }*/
            // --------------------------------------------------- Issue Assignees -------------------------------------
            Set<EdsEmployeeTask> assignments = issue.getUnDeletedAssignments();
            HashMap<Integer, EdsEmployeeTask> oldTaskAssignees = new HashMap<>();
            for (EdsEmployeeTask employeeTask : assignments) {
                employeeTask.setDeleted(true);
                employeeTaskManager.update(employeeTask);
                oldTaskAssignees.put(employeeTask.getProjectEmployee().getObjectID(), employeeTask);
            }

            ArrayList<IdTime> taskAssignees = new ArrayList<>();
            if (!(MEM.equals(viewAsFilter)/* || CLIENT.equals(viewAsFilter)*/)) {
                for (IdTime ass : item.getAssignees()) {
                    EdsProjectEmployee pem = projectEmployeeManager.get(ass.getId());
                    if (pem != null && pem.getEmployeeDepartment() != null && pem.getEmployeeDepartment().getEmployee() != null) {
                        EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(pem.getEmployeeDepartment().getEmployee(), newProject);
                        if (projectEmployee == null) {
                            EdsProjectEmployee projEmployee = addMembers(newProject, pem.getEmployeeDepartment().getEmployee());
                            taskAssignees.add(new IdTime(projEmployee.getObjectID(), ass.getTime(), ass.getPercent()));
                        } else {
                            if (oldTaskAssignees.containsKey(projectEmployee.getObjectID())) {
                                EdsEmployeeTask et = oldTaskAssignees.get(projectEmployee.getObjectID());
                                et.setDeleted(false);
                                et.getTask().setProject(newProject);
                                et.setEstimatedTime(ass.getTime());
                                employeeTaskManager.update(et);
                            } else {
                                taskAssignees.add(ass);
                            }
                        }
                    }
                }
            } else {
                IdTime[] assignees = item.getAssignees();
                taskAssignees.add(assignees[0]);
                if (issue.getObjectID() != null) {
                    ArrayList<IdTime> oldAssignees = new ArrayList<>();
                    if (assignments != null && !assignments.isEmpty()) {
                        for (EdsEmployeeTask employeeTask : assignments.toArray(new EdsEmployeeTask[]{})) {
                            IdTime idTime = new IdTime(employeeTask.getProjectEmployee().getObjectID(), employeeTask.getEstimatedTime());
                            oldAssignees.add(idTime);
                        }
                    }
                    EdsEmployeeTask employeeTask = taskManager.getEmployeeTask(user.getObjectID(), issue.getObjectID());
                    employeeTask.setEstimatedTime(assignees[0].getTime());
                    employeeTaskManager.update(employeeTask);
                    for (IdTime et : oldAssignees) {
                        if (!et.getId().equals(assignees[0].getId())) {
                            taskAssignees.add(et);
                        }
                    }
                }
            }
            dailyLoadRequestArrayList = saveTaskAssignees(user, issue, taskAssignees.toArray(new IdTime[]{}), null);
            try {
                projectSolrComponent.index(newProject);
            } catch (SolrServerException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
            // ------------------------------------------------------- Issue Documents --------------------------------------------------------------------------------
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setFolderType(F_PR_ISSUE);
            filterParameter.setCrmEntityId(issue.getObjectID());
            filterParameter.setTrashResource(false);
            filterParameter.setOtherSharedResource(false);
            filterParameter.setOtherResource(false);
            filterParameter.setSharedResource(false);
            try {
                ListResult<FileResource> fileResourceListResult = documentsServiceLocal.listFile(filterParameter);
                if (fileResourceListResult != null && !fileResourceListResult.getList().isEmpty()) {
                    for (FileResource file : fileResourceListResult.getList()) {
                        FolderResource folderResource = documentsServiceLocal.getFolderResource(F_PR_ISSUE, issue.getProject().getObjectID());
                        documentsServiceLocal.moveFile(file.getObjectId(), folderResource.getObjectId());
                    }
                }
            } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                e.printStackTrace();
            }

            //------------------------------------------ Timesheet entries ---------------------------------------------
            if (item.isTimeSheetEnabled()) {
                timeSheetManager.updateTimeEntries(issue.getObjectID(), newProject.getObjectID());
                /*List<EdsTimeSheet> timeSheets = timeSheetManager.getTimeEntries(issue.getObjectID());
                if (timeSheets != null && !timeSheets.isEmpty()) {
                    for (EdsTimeSheet timeSheet : timeSheets) {
                        timeSheet.setProjectID(newProject.getObjectID());
                        timeSheetManager.update(timeSheet);
                        if (timeSheet.getTaskRbacHistory() != null) {
                            timeSheet.getTaskRbacHistory().setProject(issue.getProject());
                        }
                    }
                }*/
            }
            //------------------------------------------ Issue Notes ---------------------------------------------------

        } else {
            //issue assignees
            if (item.getAssignees() == null || item.getAssignees().length == 0) { //for quick add
                EdsProjectEmployee proEmp = projectEmployeeManager.getProjectEmployee(user.getEmployee(), projectManager.get(item.getProjectID()));
                if (proEmp != null) {
                    IdTime idTimes = new IdTime();
                    idTimes.setId(proEmp.getObjectID());

                    item.setAssignees(new IdTime[]{idTimes});
                }
            }
//            if (item.getAssignees() != null && item.getAssignees().length > 0) {
            ArrayList<IdTime> newAssignees = new ArrayList<>();
            List<EdsEmployeeTask> employeeTasks = issue.getObjectID() != null ? employeeTaskManager.getEmployeeTasks(null, issue) : null;
            ArrayList<IdTime> oldAssignees = new ArrayList<>();
            HashMap<IdTime, IdTime> oldAssigneesMap = new HashMap<>();
            if (employeeTasks != null && !employeeTasks.isEmpty()) {
                for (EdsEmployeeTask employeeTask : employeeTasks) {
                    IdTime idTime = new IdTime(employeeTask.getProjectEmployee().getObjectID(), employeeTask.getEstimatedTime());
                    oldAssignees.add(idTime);
                    oldAssigneesMap.put(idTime, new IdTime(employeeTask.getProjectEmployee().getObjectID(), employeeTask.getEstimatedTime(), employeeTask.getObjectID()));
                }
            }
            if (!(MEM.equals(viewAsFilter)/* || CLIENT.equals(viewAsFilter)*/)) {
                Collections.addAll(newAssignees, item.getAssignees());
                if (issue.getObjectID() != null) {
                    if (employeeTasks != null && !employeeTasks.isEmpty()) {
                        ArrayList<IdTime> nonChangedAssignees = (ArrayList<IdTime>) ServerUtils.intersect(newAssignees, oldAssignees);
                        if (nonChangedAssignees != null && !nonChangedAssignees.isEmpty()) {
                            for (IdTime nonChangedAssign : nonChangedAssignees) {
                                EdsProjectEmployee edsProjectEmployee = projectEmployeeManager.get(nonChangedAssign.getId());
                                if (edsProjectEmployee != null && edsProjectEmployee.getEmployeeDepartment() != null && edsProjectEmployee.getEmployeeDepartment().getEmployee() != null) {
                                    EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeTask(issue.getObjectID(), edsProjectEmployee.getEmployeeDepartment().getEmployee().getObjectID());
                                    if (employeeTask != null) {
                                        employeeTask.setEstimatedTime(nonChangedAssign.getTime());
                                        employeeTaskManager.update(employeeTask);
                                    }
                                }
                            }
                        }
                        if (oldAssignees != null && !oldAssignees.isEmpty()) {
                            for (IdTime idTime : oldAssignees) {
                                if (oldAssigneesMap.containsKey(idTime)) {
                                    EdsEmployeeTask employeeTask = employeeTaskManager.get(oldAssigneesMap.get(idTime).getStatusId());
                                    if (employeeTask != null) {
                                        employeeTask.setDeleted(true);
                                        employeeTaskManager.update(employeeTask);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                IdTime[] assignees = item.getAssignees();
                newAssignees.add(assignees[0]);
                if (issue.getObjectID() != null) {
                    EdsEmployeeTask employeeTask = taskManager.getEmployeeTask(user.getObjectID(), issue.getObjectID());
                    employeeTask.setEstimatedTime(assignees[0].getTime());
                    employeeTaskManager.update(employeeTask);
                    for (IdTime et : oldAssignees) {
                        if (!et.getId().equals(assignees[0].getId())) {
                            newAssignees.add(et);
                        }
                    }
                }
            }
            dailyLoadRequestArrayList = saveTaskAssignees(user, issue, newAssignees.toArray(new IdTime[]{}), null);
//            }
        }

        if (item.isTimeSheetEnabled()) {
            issue.setEnableTimesheet(true);
            issue.setBillable(item.isBillable());
        } else {
            issue.setEnableTimesheet(false);
            issue.setBillable(false);
        }
        return dailyLoadRequestArrayList;
    }

    public class DailyLoadRequest {
        private EdsEmployee dailyEmployee;
        private EdsEmployeeTask dailyEmployeeTask;
        private ArrayList<Calendar> dailyAvailableDays;
        private int dailyLoadTime;
        private int dailyLoadQ;

        public EdsEmployee getDailyEmployee() {
            return dailyEmployee;
        }

        public void setDailyEmployee(EdsEmployee dailyEmployee) {
            this.dailyEmployee = dailyEmployee;
        }

        public EdsEmployeeTask getDailyEmployeeTask() {
            return dailyEmployeeTask;
        }

        public void setDailyEmployeeTask(EdsEmployeeTask dailyEmployeeTask) {
            this.dailyEmployeeTask = dailyEmployeeTask;
        }

        public ArrayList<Calendar> getDailyAvailableDays() {
            return dailyAvailableDays;
        }

        public void setDailyAvailableDays(ArrayList<Calendar> dailyAvailableDays) {
            this.dailyAvailableDays = dailyAvailableDays;
        }

        public int getDailyLoadTime() {
            return dailyLoadTime;
        }

        public void setDailyLoadTime(int dailyLoadTime) {
            this.dailyLoadTime = dailyLoadTime;
        }

        public int getDailyLoadQ() {
            return dailyLoadQ;
        }

        public void setDailyLoadQ(int dailyLoadQ) {
            this.dailyLoadQ = dailyLoadQ;
        }
    }

    public void updateTimeForCurrentDay(EdsTask task, Integer timeSpent, EdsUser user) {
        for (EdsEmployeeTask employeeTask : task.getUnDeletedAssignments()) {
            if (user.isEmployee() && employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().equals(user.getEmployee()) && timeSpent != null) {
                EdsTimeSheet ts = new EdsTimeSheet();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(task.getCreationTime());
                ts.setDate(calendar.getTime());
                ts.setEmployeeTask(employeeTask);
                ts.setTimeSpent(timeSpent);
                ts.setEntryDate(new Date());
                timeSheetManager.create(ts);
                task.setActualStartDate(ts.getDate());
                task.setLastUpdateTime(new Date());
            }
        }
    }

    public void updateBudgetByEstimatedTime(EdsWorkStream parentWorkStream, EdsEmployeeTask employeeTask, Integer estTimeDiff) {
        if (estTimeDiff == 0) {
            return;
        }
        EdsProjectEmployee employee = employeeTask.getProjectEmployee();

        double wageAmmountDiff = ((double) estTimeDiff / 60) * employee.getWageRate();
        double clientChargeAmmountDiff = ((double) estTimeDiff / 60) * employee.getClientChargeRate();

        employeeTask.updatePlannedWageAmount(wageAmmountDiff);
        employeeTask.updatePlannedClientChargeAmount(clientChargeAmmountDiff);
        if (parentWorkStream != null) {
            parentWorkStream.updateEstimatedTime(estTimeDiff);
            parentWorkStream.updatePlannedWageAmmount(wageAmmountDiff);
            parentWorkStream.updatePlannedClientChargeAmmount(clientChargeAmmountDiff);

            parentWorkStream.updateWageAmmount(wageAmmountDiff);
            parentWorkStream.updateClientChargeAmmount(clientChargeAmmountDiff);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WorkstreamSingleItem getWorkstream(Integer objectId) {
        return getWorkstreamSummary(objectId, false);
    }

    public WorkstreamSingleItem getWorkstreamSummary(Integer objectId, Boolean isWSSummary) {
        WorkstreamSingleItem workstreamItem = new WorkstreamSingleItem();
        EdsWorkStream workstream = workStreamManager.get(objectId);
        if (workstream != null) {

            if (isWSSummary) {
                calculateRecursiveWSBudgets(workstreamItem, workstream.getObjectID());
            }

            workstreamItem.setObjectID(workstream.getObjectID());

            NumberData numberData = generateWorkstreamNumber(workstream.getProject().getObjectID(), workstream.getStartDate(), objectId);
            numberData.setNumberString(workstream.getNumber());
            numberData.setIntNumber(workstream.getIntNumber());
            workstreamItem.setNumberData(numberData);
            workstreamItem.setNumber(numberData.getNumberString());

            workstreamItem.setName(workstream.getName());
            workstreamItem.setParentWSName(workstream.getParentWS() != null ? workstream.getParentWS().getName() : referenceWfmMessageSource.localize("NA", "N/A"));
            workstreamItem.setParentWSID(workstream.getParentWS() != null ? workstream.getParentWS().getObjectID() : Integer.valueOf(0));
            workstreamItem.setDescription(workstream.getDescription());
            workstreamItem.setStartDate(workstream.getStartDate());
            workstreamItem.setEstimatedTime(getWorkstreamEstimatedTime(workstream));
            workstreamItem.setEstimatedCost(workstream.getPlannedWageAmount());
            workstreamItem.setActualTime(workstream.getActualTime());
            workstreamItem.setActualCost(workstream.getActualWageAmount());
            workstreamItem.setEndDate(workstream.getEndDate());
            workstreamItem.setCreatorName(workstream.getCreator() != null ? workstream.getCreator().getFullName() : referenceWfmMessageSource.localize("NA", "N/A"));
            workstreamItem.setCreationTime(workstream.getCreationTime());
            workstreamItem.setLastUpdateTime(workstream.getLastUpdateTime());
            workstreamItem.setLastUpdaterName(workstream.getUpdater() != null ? workstream.getUpdater().getFullName() : referenceWfmMessageSource.localize("NA", "N/A"));
            workstreamItem.setProjectID(workstream.getProject() != null ? workstream.getProject().getObjectID() : Integer.valueOf(0));
            workstreamItem.setProjectName(workstream.getProject() != null ? workstream.getProject().getName() : referenceWfmMessageSource.localize("NA", "N/A"));
            workstreamItem.setProjectManager(workstream.getProject() != null && workstream.getProject().getManager() != null ?
                    workstream.getProject().getManager().getName() : referenceWfmMessageSource.localize("NA", "N/A"));
            workstreamItem.setProjectManagerID(workstream.getProject() != null && workstream.getProject().getManager() != null ?
                    workstream.getProject().getManager().getObjectID() : Integer.valueOf(0));
            ArrayList<SelectItem> backupMangers = new ArrayList<>();
            for (EdsEmployee backupManager : workstream.getProject().getBackupManagers()) {
                SelectItem item = new SelectItem();
                item.setId(backupManager.getObjectID());
                item.setName(backupManager.getName());
                backupMangers.add(item);
            }
            workstreamItem.setBackupManagers(backupMangers);
            workstreamItem.setWageAmount(workstream.getWageAmmount() != null ? workstream.getWageAmmount() : 0);
            workstreamItem.setClientChargeAmmount(workstream.getClientChargeAmmount() != null ? workstream.getClientChargeAmmount() : 0);
            workstreamItem.setPercent(workstream.getPercent());

            workstreamItem.setReminder(itemReminderManager.getReminders(workstream.getObjectID(), WORKSTREAM_REMINDER));

            if (isWSSummary) {
                Set<EdsWorkStream> subWorkStreams = workstream.getSubWorkStreams();
                List<WorkstreamSingleItem> subW = new ArrayList<>();
                if (subWorkStreams != null && !subWorkStreams.isEmpty()) {
                    for (EdsWorkStream sw : subWorkStreams) {
                        WorkstreamSingleItem subWorkStreamSingleItem = new WorkstreamSingleItem();
                        subWorkStreamSingleItem.setObjectID(sw.getObjectID());
                        subWorkStreamSingleItem.setName(sw.getName());
                        subWorkStreamSingleItem.setEstimatedTime(sw.getEstimatedTime());
                        subWorkStreamSingleItem.setActualTime(sw.getActualTime());
                        subWorkStreamSingleItem.setPercent(sw.getPercent());
                        subW.add(subWorkStreamSingleItem);
                    }
                }
                workstreamItem.setSubWorkstreams(subW.toArray(new WorkstreamSingleItem[]{}));
            }
        } else {
            return null;
        }

        return workstreamItem;
    }

    private Integer getWorkstreamEstimatedTime(EdsWorkStream workstream) {
        Integer workStreamEstimateTotal = 0;
        for (EdsTask task : workstream.getTasks()) {
            workStreamEstimateTotal += task.getEstimatedTime() != null ? task.getEstimatedTime() : 0;
        }
        for (EdsWorkStream childWorkStream : workstream.getSubWorkStreams()) {
            workStreamEstimateTotal += getWorkstreamEstimatedTime(childWorkStream);
        }
        return workStreamEstimateTotal;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PositionsSelectItem[] getTaskPositionsAsPSI(Integer taskID) {
        EdsTask task = taskManager.get(taskID);
        PositionsSelectItem[] positions = new PositionsSelectItem[task.getPositions().size()];
        int i = 0;
        for (EdsPositionTask pt : task.getPositions()) {
            EdsPosition resPos = pt.getPosition();
            positions[i] = new PositionsSelectItem(pt.getPosition().getObjectID(), null,
                    resPos.getObjectID(), resPos.getName());
            positions[i].setTime(pt.getEstimatedTime());
            i++;
        }

        return positions;
    }

    @Override
    public Integer[] saveTaskAssignees(Integer taskId, IdTime[] assignees) throws NumberExistingException {
        final EdsUser user = userManager.getUser();
        EdsTask task = taskManager.get(taskId);
        TaskSingleItem taskItem = wrapEdsTaskToTaskSingleItem(task, user);
        taskItem.setProjectEmployees(assignees);
        NumberData taskNumber = new NumberData();
        if (task.getNumber() != null) {
            taskNumber.setNumberString(task.getNumber());
            taskNumber.setIntNumber(task.getIntNumber());
        }
        taskItem.setNumberData(taskNumber);

        return saveTask(taskItem);
    }

    public void updateTaskAssignees(TaskAssignee taskAssignee) {
        updateTaskAssignees(taskAssignee.getTaskId(), taskAssignee.getAssignees());
    }

    public void updateTaskAssignees(Integer taskId, IdTime[] assignees) {
        final EdsUser user = userManager.getUser();
        updateTaskAssignees(taskId, assignees, user.getObjectID());
    }

    private void updateTaskAssignees(Integer taskId, IdTime[] assignees, Integer userId) {
        EdsTask task = taskManager.get(taskId);
        EdsUser user = (userId != null ? userManager.get(userId) : userManager.getUser());
        updateTaskAssignees(task, assignees, user, false);
    }

    private void updateTaskAssignees(EdsTask task, IdTime[] assignees, EdsUser user, boolean isNewTask) {
        updateTaskAssignees(task, assignees, user, false, isNewTask);
    }

    /**
     * Will update the task assignees with the new assignees list from UI and will also update each employeeTask's attribute changes
     *
     * @param task      EdsTask of which assignees needs to be updated
     * @param assignees Array of EdsProjectEmployees wrapped into IdTime that represent the updated list of Task assignees
     * @param user      The EdsUser that is trying to update the task assignees. This will be used for permission check
     */
    private void updateTaskAssignees(EdsTask task, IdTime[] assignees, EdsUser user, boolean unassigned, boolean isNewTask) {
        //Check RBAC Entries for User permission
        List<EdsTaskRbac> entries = taskRbacManager.getEntriesForUserOrHisMemberGoups(task, user, user.getMembershipGroups());
        TaskPermissionItem overalPermission = new TaskPermissionItem();
        if (entries != null && !entries.isEmpty()) {
            overalPermission = getAggregatePermisionsAs(entries);
        }
        if ((task.getCreator() != null && task.getCreator().getObjectID().equals(user.getObjectID()))) {
            overalPermission.setAssigneeEdit(true);
        }
        if (employeeManager.userIsAssignToTask(user.getObjectID(), task.getObjectID())) {
            overalPermission.setAssigneeStatusEdit(true);
        }

        if (!overalPermission.isAssigneeStatusEdit()) {
            if (ServerUtils.isCrm()) {
                if (ServerUtils.hasPermission(PermissionConstants.CRM_TASKS_EDIT) && (ServerUtils.hasPermission(PermissionConstants.CRM_TASKS_LIST) || ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_TASKS_LIST))) {
                    overalPermission.setAssigneeStatusEdit(true);
                }
            } else {
                if (ServerUtils.hasPermission(PermissionConstants.PM_TASKS_EDIT) && (ServerUtils.hasPermission(PermissionConstants.PM_TASKS_LIST) || ServerUtils.hasPermission(PermissionConstants.PM_SHOW_ALL_TASKS))) {
                    overalPermission.setAssigneeStatusEdit(true);
                }
            }
        }

        if ((entries == null || entries.isEmpty()) && !overalPermission.isAssigneeEdit() && !overalPermission.isAssigneeStatusEdit()) {
            return;
        }

        IdTime[] oldProjectEmployees = wrapTaskAssigneesToIdTime(task);
        IdTime[] newProjectEmployees = assignees.clone();
        ArrayList<IdTime> oldProjectEmployeesList = new ArrayList<>(Arrays.asList(oldProjectEmployees));
        ArrayList<IdTime> newProjectEmployeesList = new ArrayList<>(Arrays.asList(newProjectEmployees));
        ArrayList<IdTime> nonChangedProjectEmployees = (ArrayList<IdTime>) ServerUtils.intersect(newProjectEmployeesList, oldProjectEmployeesList);
        Integer taskEstimatedTime = 0;

        //Remove Old Assignees
        for (IdTime oldProjectEmployee : oldProjectEmployeesList) {
            EdsProjectEmployee pe = projectEmployeeManager.get(oldProjectEmployee.getId());
            EdsEmployee employee = pe.getEmployeeDepartment().getEmployee();
            EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeTask(task.getObjectID(), employee.getObjectID(), true);
            if (employeeTask != null) {
                employeeTask.setDeleted(true);
                employeeTask.setGoogleID(null);
                employeeTaskManager.update(employeeTask);

                //this history for the calculation PROJECT COST
                EdsTaskEstimateTimeSpentHistory estimateTimeSpentHistory = new EdsTaskEstimateTimeSpentHistory();
                estimateTimeSpentHistory.setTask(task);
                estimateTimeSpentHistory.setOldEstimatedTime(employeeTask.getEstimatedTime());
                estimateTimeSpentHistory.setEstimatedTime(0);
                task.getEstimateTimeSpentHistoryList().add(estimateTimeSpentHistory);
                task.setChangedCalculationFields(true);
            }
        }

        //Add New Task Assignees
        for (IdTime newProjectEmployee : newProjectEmployeesList) {
            EdsProjectEmployee pe = projectEmployeeManager.get(newProjectEmployee.getId());
            EdsEmployee employee = null;
            if (pe != null) {
                employee = pe.getEmployeeDepartment().getEmployee();
            }
            EdsEmployeeTask employeeTask = null;
            if (!isNewTask) {
                employeeTask = employeeTaskManager.getEmployeeTask(task.getObjectID(), employee.getObjectID(), true);
            }
            // Create new employeeTask record or restore the previously deleted one
            if (employeeTask == null) {
                employeeTask = new EdsEmployeeTask();
                employeeTask.setProjectEmployee(pe);
                employeeTask.setTask(task);
                employeeTask.setTaskAmount(newProjectEmployee.getTaskAmount());
                if (isNewTask && task.getStatus() != null) {
                    newProjectEmployee.setStatusId(task.getStatus().getObjectID());
                }
                UpdateEmployeeTaskAttributes(newProjectEmployee, employeeTask, true);
                employeeTask.setDeleted(unassigned);
                employeeTaskManager.create(employeeTask);
                task.getAssignments().add(employeeTask);
            } else {
                UpdateEmployeeTaskAttributes(newProjectEmployee, employeeTask, true);
                employeeTask.setDeleted(false);// adding employee back to the task, as He was assigned before(and deleted)
            }
            baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, employeeTask, user);

            //calculation task estimated time
            if (newProjectEmployee.getTime() != null) {
                taskEstimatedTime += newProjectEmployee.getTime();
            }

           /* if (!isNewTask) {
                try {
                    EdsFolder folder = folderManager.getFolder(F_TASK, task.getProject().getObjectID());
                    if (folder != null) {
                        ArrayList<PermissionHolder> permissionList = new ArrayList<>(documentsServiceLocal.getFolderPermissions(folder.getObjectID()));
                        ArrayList<FileResource> files = documentsServiceLocal.getFileResources(F_TASK, folder.getObjectID(), task.getObjectID());
                        if (files != null && files.size() > 0) {
                            files.forEach(f -> {
                                try {
                                    documentsServiceLocal.updateFile(f.getObjectId(), null, false, permissionList);
                                } catch (DuplicateNameException e) {
                                    e.printStackTrace();
                                } catch (ObjectNotFoundException e) {
                                    e.printStackTrace();
                                } catch (InsufficientPermissionsException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                } catch (InsufficientPermissionsException e) {
                    e.printStackTrace();
                }
            }*/

        }

        //Update Non-Changed Assignees in case their attributes have been changed
        for (IdTime nonChangedProjectEmployee : nonChangedProjectEmployees) {
            EdsProjectEmployee pe = projectEmployeeManager.get(nonChangedProjectEmployee.getId());
            EdsEmployee employee = pe.getEmployeeDepartment().getEmployee();
            EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeTask(task.getObjectID(), employee.getObjectID(), true);
            if (employeeTask != null) {
                UpdateEmployeeTaskAttributes(nonChangedProjectEmployee, employeeTask, true);
                baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employeeTask, user);
            }
            //calculation task estimated time
            if (nonChangedProjectEmployee.getTime() != null) {
                taskEstimatedTime += nonChangedProjectEmployee.getTime();
            }
        }

        employeeTaskManager.setEmployeeTasksModifiedDate(task, new Date());

        //set task estimated times
        task.setEstimatedTime(taskEstimatedTime);
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (settings == null || !settings.isAutomatic()) {//if settings is null then project percent calculation mode is manual by default
            //set task average percent by employee tasks
            Float average = 0f;
            Float averageProjectTasks = 0f;
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                average = task.getTaskAveragePercentCompletedNewLogic();
                averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompletedNewLogic();
            } else {
                if (task.getProject() != null) {
                    average = task.getTaskAveragePercentCompleted();
                    averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompleted();
                }
            }
            task.setPreviousPercent(task.getPercent());
            task.setPercent(average);
            //set project average percent by project tasks
            task.getProject().setPercent(averageProjectTasks);
        } else {
            if (task.getPercent() == null) {
                task.setPercent(0f);
            } else {
                task.setPercent(taskManager.getTaskActualPercentCompleted(task.getObjectID()));
            }
            if (task.getProject().getPercent() == null) {
                task.getProject().setPercent(0f);
            } else {
                task.getProject().setPercent(projectManager.getProjectActualPercentCompleted(task.getProject().getObjectID()));
            }
        }

        taskManager.update(task);

        //Register index task documents
        baseEventPostProcessor.registerEvent(TaskDocumentsReIndexEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user);
        //update project to solr
        if (!isNewTask) {
            try {
//                solrManager.indexAddProject(task.getProject(), task.getProject().getCompany().getObjectID());
                projectSolrComponent.index(task.getProject());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Will update EdsEmployeeTask attributes such as estimatedTime, percent, status, start date, due date, etc.. with new values from IdTime Transfer Object
     * <br/><b>Note:</b> Only not Null values will be set when default values disabled.
     *
     * @param projectEmployee
     * @param employeeTask
     * @param enableDefaultValues - If enabled default values will be set for the attributes specified as Null
     */
    private void UpdateEmployeeTaskAttributes(IdTime projectEmployee, EdsEmployeeTask employeeTask, boolean enableDefaultValues) {
        EdsTaskEstimateTimeSpentHistory estimateTimeSpentHistory = new EdsTaskEstimateTimeSpentHistory();
        estimateTimeSpentHistory.setTask(employeeTask.getTask());

        //Estimated
        if (projectEmployee.getTime() != null) {
            if (employeeTask.getEstimatedTime().compareTo(projectEmployee.getTime()) != 0) {
                estimateTimeSpentHistory.setOldEstimatedTime(employeeTask.getEstimatedTime());
                estimateTimeSpentHistory.setEstimatedTime(projectEmployee.getTime());
            }

            employeeTask.setEstimatedTime(projectEmployee.getTime());
        } else if (enableDefaultValues) {
            employeeTask.setEstimatedTime(0);
        }

        //Percent
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (settings == null || !settings.isAutomatic()) {//if settings is null then project percent calculation mode is manual by default
            if (projectEmployee.getPercent() != null) {
                employeeTask.setPercent(projectEmployee.getPercent());
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                    employeeTask.setPercent(projectEmployee.getPercent());
                } else {
                    employeeTask.setPercent(projectEmployee.getPercent().compareTo(100f) > 0 ? 100f : projectEmployee.getPercent());
                }
            } else if (enableDefaultValues) {
                employeeTask.setPercent(0f);
            }
        } else {
            if (projectEmployee.getPercent() == null && enableDefaultValues) {
                employeeTask.setPercent(0f);
            } else {
                if (employeeTask.getEstimatedTime() == null || employeeTask.getEstimatedTime().equals(0) || employeeTask.getTimeSpent() == null) {
                    employeeTask.setPercent((float) 0);
                } else {
                    Float percentCompleted = ((float) employeeTask.getTimeSpent() / (float) employeeTask.getEstimatedTime()) * 100;
                    if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                        employeeTask.setPercent(percentCompleted);
                    } else {
                        employeeTask.setPercent(percentCompleted.compareTo(100f) > 0 ? 100f : percentCompleted);
                    }
                }
            }
        }

        if (enableDefaultValues && employeeTask.getObjectID() == null) {
            // if new task and default values needs to be set, use current date as default start date
            employeeTask.setStartDate(new Date());
        }

        if (projectEmployee.getStatusId() != null) {
            employeeTask.setStatus(referenceManager.getReference(projectEmployee.getStatusId()));
        } else if (enableDefaultValues) {
            employeeTask.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED));
        }

        if (projectEmployee.getGoogleID() != null) { // For Google Calendar Sync purposes
            employeeTask.setGoogleID(projectEmployee.getGoogleID());
        }
        employeeTask.setLastModifiedDate(new Date()); // For Google Calendar Sync purposes

        if (estimateTimeSpentHistory.getEstimatedTime().compareTo(estimateTimeSpentHistory.getOldEstimatedTime()) != 0) {
            //this is for the recalculate task budgets
            employeeTask.getTask().setChangedCalculationFields(true);

            employeeTask.getTask().getEstimateTimeSpentHistoryList().add(estimateTimeSpentHistory);
        }
        employeeTaskManager.update(employeeTask);
    }

    private void removeTaskAssignees(EdsWorkStream parentWorkstream, Set<EdsEmployeeTask> employeeTasks) {
        for (EdsEmployeeTask eTask : employeeTasks) {
            updateBudgetByEstimatedTime(parentWorkstream, eTask, -eTask.getEstimatedTime());
            eTask.setDeleted(true);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] getRecursivelyPredecessors(Integer rootTaskId) {
        List taskIds = this.taskManager.getRecursivelyPredecessors(rootTaskId);
        if (taskIds != null) {
            return (String[]) taskIds.toArray(new String[0]);
        } else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] getRecursivelySuccessors(Integer rootTaskId) {
        List taskIds = this.taskManager.getRecursivelySuccessors(rootTaskId);
        if (taskIds != null) {
            return (String[]) taskIds.toArray(new String[0]);
        } else {
            return null;
        }
    }

    public Integer[] saveMultipleTask(MultiTaskList multiTaskList) {
        ArrayList<Integer> taskIdsArrayList = new ArrayList<>();
        EdsUser user = employeeManager.getUser();
        HashMap<EdsEmployee, HashSet<EdsTask>> projectEmployeesWithTasks = new HashMap<>();

        for (TaskSingleItem taskSingleItem : multiTaskList.getTaskSingleItems()) {
            TaskSingleItem newTask = taskSingleItem;

            Calendar startDate = Calendar.getInstance();
            startDate.setTime(newTask.getStartDate());

            Calendar endDate = Calendar.getInstance();
            endDate.setTime(newTask.getDueDate());

            EdsTask task = new EdsTask();

            NumberData numberData = generateTaskNumber(multiTaskList.getProjectID(), newTask.getStartDate(), null);
            if (numberData != null) {
                task.setIntNumber(numberData.getIntNumber());
                String fullNumber = numberData.getNumberString() != null ? numberData.getNumberString() : "";
                if (numberData.getSavedNumberFormula() != null && !"".equals(numberData.getSavedNumberFormula())) {
                    task.setSavedNumberFormula(numberData.getSavedNumberFormula());
                } else {
                    task.setSavedNumberFormula(("".equals(numberData.getFirstNumberString()) ? "null" : numberData.getFirstNumberString()) + SAV_NUM_DEL + ("".equals(numberData.getIntNumber()) ? "null" : numberData.getIntNumber()) + SAV_NUM_DEL + ("".equals(numberData.getLastNumberString()) ? "null" : numberData.getLastNumberString()));
                }
                task.setNumber(fullNumber);
            }
            task.setShowInTimesheet(newTask.isShowInTimesheet());
            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
            task.setStartAndDueDates(startDate.getTime(), endDate.getTime());
            task.setAllDay(newTask.isAllDay());
            task.setBillable(newTask.getBillable());
            task.setPercent(newTask.getPercent());
            if (newTask.getEmployeeID() != null) {
                task.setToDoListAssignee(userManager.get(newTask.getEmployeeID()));
            }
            if (newTask.getStatusID() != null) {
                task.setStatus(referenceManager.get(newTask.getStatusID()));
            } else {
                task.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED));
            }
            task.setPriority(referenceManager.get(newTask.getPriorityID()));
            if (newTask.getTypeID() != null) {
                task.setType(referenceManager.get(newTask.getTypeID()));
            }
            if (multiTaskList.getProjectID() != null) {
                task.setProject(projectManager.get(multiTaskList.getProjectID()));
            }

            if (multiTaskList.getWorkstreamID() != null) {
                task.setParentWS(workStreamManager.get(multiTaskList.getWorkstreamID()));
            }
            if (newTask.getPredecessorTasks() != null) {
                for (SelectItem tasks : newTask.getPredecessorTasks()) {
                    if (tasks != null) {
                        EdsTask predTask = taskManager.get(tasks.getId());
                        if (predTask != null) {
                            predTask.getSuccessors().add(task);
                            task.getPredecessors().add(predTask);
                        }
                    }
                }
            }
            if (newTask.getSuccessorTasks() != null) {
                for (SelectItem tasks : newTask.getSuccessorTasks()) {
                    if (tasks != null) {
                        EdsTask succTask = taskManager.get(tasks.getId());
                        if (succTask != null) {
                            succTask.getPredecessors().add(task);
                            task.getSuccessors().add(succTask);
                        }
                    }
                }
            }

            //set kanbanboard order if its null
            if (task.getKanbanOrder() == null) {
                Long minKanbanOrderInStatus = taskManager.getMinKanbanOrder(task.getStatus() != null ? task.getStatus().getObjectID() : null);
                if (minKanbanOrderInStatus == null) {
                    minKanbanOrderInStatus = CrmConstants.KANBAN_ORDER_GAP;
                    task.setKanbanOrder(minKanbanOrderInStatus);
                } else {
                    task.setKanbanOrder(minKanbanOrderInStatus - CrmConstants.KANBAN_ORDER_GAP);
                }
            }

            task.setCreator(user);
            taskManager.create(task);

            taskIdsArrayList.add(task.getObjectID());

            // Create Task Custom Fields
            EdsTaskCustomFields edsTaskCusromFields = createTaskCustomFields(newTask.getCustomFieldItems());
            task.setTaskCustomFields(edsTaskCusromFields);

            //Save task Assignees, if no assignee try save the current user
            if (newTask.getProjectEmployees() != null && newTask.getProjectEmployees().length > 0) {
                for (IdTime pem : newTask.getProjectEmployees()) {
                    EdsProjectEmployee pe = projectEmployeeManager.get(pem.getId());
                    EdsEmployee employee = pe.getEmployeeDepartment().getEmployee();
                    if (user.getObjectID().equals(employee.getObjectID())) {
                        pem.setStatusId(newTask.getStatusID());
                    }
                    if (!employee.getAccountStatus().getCode().equals(EMPLOYEE_STATUS_NO_ACCCESS)) {
                        projectEmployeesWithTasks.computeIfAbsent(employee, k -> new HashSet<>());
                        projectEmployeesWithTasks.get(employee).add(task);
                    }
                }

                updateTaskAssignees(task, newTask.getProjectEmployees(), user, true);
                updateTaskDailyLoad(task); //update task daily load for the all undeleted task assignees
            } else if (user instanceof EdsEmployee) {
                EdsProjectEmployee proEmp = projectEmployeeManager.getProjectEmployee(user.getEmployee(), projectManager.get(newTask.getProjectID()));
                if (proEmp != null) {
                    IdTime[] projectEmployees = new IdTime[1];
                    IdTime idTime = new IdTime(proEmp.getObjectID(), 0);
                    projectEmployees[0] = idTime;
                    updateTaskAssignees(task, projectEmployees, user, true);
                    if (!user.getAccountStatus().getCode().equals(EMPLOYEE_STATUS_NO_ACCCESS)) {
                        if (projectEmployeesWithTasks.get(user) == null) {
                            projectEmployeesWithTasks.put((EdsEmployee) user, new HashSet<>());
                        }
                        projectEmployeesWithTasks.get(user).add(task);
                    }
                }
            }

            EdsBusinessEvent taskAddEvent = baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user);
            workflowEvent.setEntityType(RelationItem.TYPE_TASK);

            log.info("multi task status changed");
            updateTaskStatus(task);

            updateTimeForCurrentDay(task, newTask.getActualTime(), user);
            if (newTask.getAttachments() != null && newTask.getAttachments().length > 0) {
                saveTaskAttachments(newTask.getAttachments(), task);
            }
            taskRbacManager.addRbacEntries(task);
        }
        //send message
//        if (!MultiTaskList.FROM_TODO_LIST.equals(multiTaskList.getCreatedFrom())) {
        if (!projectEmployeesWithTasks.isEmpty() && user != null) {
            sendMultiTaskAddMessage(projectEmployeesWithTasks, user);
        }
//        }
        return taskIdsArrayList.toArray(new Integer[]{});
    }

    private void sendMultiTaskAddMessage(HashMap<EdsEmployee, HashSet<EdsTask>> projectEmployeesWithTasks, EdsUser creator) {
        try {
            for (EdsEmployee employee : projectEmployeesWithTasks.keySet()) {
                boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(employee.getObjectID(), EmailNotificationConstants.TASK_ASSIGN_NOTIFICATION);
                if (emailNotificationSettings) {
                    messageManager.sendMultiTaskAssignNotification(creator, employee, projectEmployeesWithTasks.get(employee));
                }
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    public void saveAttachments(Attachments attachments) {
        TaskSingleItem item = new TaskSingleItem();
        item.setAttachments(attachments.getAttachments());

        EdsTask task = taskManager.get(attachments.getObjectID());
        saveTaskAttachments(item.getAttachments(), task);
    }

    public String deleteTask(Integer taskId, String context) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTask.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(taskId);
        ServerUtils.kpiLog(log, kpiLog, "Task delete");

        EdsUser user = employeeManager.getUser();
        EdsTask task = taskManager.get(taskId);
        return deleteTask(task, user, context);
    }

    public String deleteTask(EdsTask task, EdsUser user, String context) {
        log.info("User's sessionID: " + ServerSecurityContext.getInstance().getSessionId() + "; CompanyID: " + ServerSecurityContext.getInstance().getCompanyId());
        EdsCompany company = user.getCompany();

        if (false && context != null) {
            String permission = PermissionConstants.PM_TASKS_REMOVE;
            if (PermissionConstants.CRM_CONTEXT.equals(context)) {
                permission = PermissionConstants.CRM_TASKS_REMOVE;
            }
            if (!checkAccess(task.getObjectID(), permission, context)) {
                return PermissionConstants.DENY;
            }
        }

        if (timesheetManager.isTaskUsedInInvoice(task.getObjectID())) {
            return "USED_IN_INVOICE";
        }

        //clear parent work stream calculated items
        if (task.getParentWS() != null && task.isCalculated()) {
            clearOldParentWSCalculatedItemsOfTask(task);
        }
        timesheetServiceLocal.updateAttendanceForDeletedTask(task); //update attendance rawdata records

        //Who is deleted this task
        task.setUpdater(user);
        task.setLastUpdateTime(user.getCompany().getCompanyDate());
        //Delete task in employees
        taskManager.deleteEmployeesTask(task);
        relationManager.deleteWorkflowRelatedRelations(task.getObjectID(), RelationItem.TYPE_TASK);
        taskRbacManager.removeTaskEntries(task);

        // delete task overdue reminders and recurrences
        taskReminderManager.deleteTaskReminders(task.getObjectID());
        List<EdsRecurrence> recurrenceList = recurrenceManager.getRecurrenceJobList(TASK_OVERDUE_REMINDER, task.getObjectID(), user.getCompany().getObjectID());
        if (recurrenceList != null && !recurrenceList.isEmpty()) {
            for (EdsRecurrence rec : recurrenceList) {
                recurrenceService.updateRecurrence(rec, true, true);
            }
        }

        //Delete tasks
        taskManager.deleteTask(task);
        task.setWorkflowID(null);
        task.setDeleted(true);
        task.setLastUpdateTime(new Date());
        EdsBusinessEvent taskDeleteEvent = baseEventPostProcessor.registerEvent(TaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, task, user);
        try {
            solrManager.removeTask(task, company);
            taskDeleteEvent.setSolrIndexed(true);
        } catch (SolrServerException | IOException e) {
            taskDeleteEvent.setSolrIndexed(false);
        }

        //when deleting a task if it is the only task between its predecessors and successors we need to link them up.
        for (EdsTask p : task.getPredecessors()) {
            for (EdsTask s : task.getSuccessors()) {
                if (!s.getPredecessors().contains(p) && s.getPredecessors().size() == 1) {
                    s.getPredecessors().add(p);
                }
            }
        }
        shiftAllSuccessors(task, null, user, true, 0, null, availabilityCircularResolver.getUserTimeSlot(user));
        //in case there is no ParentWS for task we need to find its predecessors or successors ParentWS
        Set<EdsWorkStream> workStreams = new HashSet<>();
        if (!task.getPredecessors().isEmpty()) {
            findPredecessorParentWS(task, workStreams);
        }
        if (!task.getSuccessors().isEmpty()) {
            findSuccessorParentWS(task, workStreams);
        }
        if (task.getParentWS() != null) {
            workStreams.add(task.getParentWS());
        }
        for (EdsWorkStream ws : workStreams) {
            updateWorkStreamDateRange(null, ws);
        }
        task.getSuccessors().clear();
        task.getPredecessors().clear();

        //task project percent recalculate
        if (task.getProject().getPercent() == null) {
            task.getProject().setPercent(0f);
        } else {
            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
            if (settings == null || !settings.isAutomatic()) {
                Float averageProjectTasks;
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                    averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompletedNewLogic();
                } else {
                    averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompleted();
                }
                task.getProject().setPercent(averageProjectTasks);
            } else {
                task.getProject().setPercent(projectManager.getProjectActualPercentCompleted(task.getProject().getObjectID()));
            }

        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, task, user);
        workflowEvent.setEntityType(RelationItem.TYPE_TASK);
        //update project to solr
        try {
//            solrManager.indexAddProject(task.getProject(), task.getProject().getCompany().getObjectID());
            projectSolrComponent.index(task.getProject());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        //

        return PermissionConstants.ALLOW;
    }

    @Override
    public String deleteTasks(ArrayList<Integer> taskIds, String context) {
        boolean t = false;
        for (Integer taskId : taskIds) {
            String sts = deleteTask(taskId, context);
            if (PermissionConstants.ALLOW.equals(sts) && !t) {
                t = true;
            }
        }
        if (t) {
            return PermissionConstants.ALLOW;
        } else {
            return PermissionConstants.DENY;
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HistoryListItem[] getTaskNotes(Integer taskID) {
        EdsTask task = taskManager.get(taskID);
        HistoryListItem[] taskNotes;
        if (task != null) {
            EdsNoteHistory[] taskNote = noteHistoryManager.getNoteList(new ListingFilterParameter()).toArray(new EdsNoteHistory[]{});

            List<EdsNoteHistory> histrItems = new LinkedList<>();
            for (EdsNoteHistory noteHistr : taskNote) {
                if ((EdsNoteHistory.TASK == noteHistr.getRelatedTo() && noteHistr.getRelatedId() != null) &&
                        (noteHistr.getRelatedId().intValue() == task.getObjectID().intValue())) {
                    histrItems.add(noteHistr);
                }
            }

            EdsUser user = employeeManager.getUser();
            taskNotes = new HistoryListItem[histrItems.size()];
            for (int i = 0; i < histrItems.size(); i++) {
                EdsNoteHistory notes = histrItems.get(i);
                HistoryListItem items = new HistoryListItem();
                items.setObjectID(notes.getObjectID());
                items.setEmployee(notes.getEmployee() != null ? notes.getEmployee().getName() : null);
                items.setSubject(notes.getSubject());
                items.setComment(notes.getComment());
                items.setVisibility(notes.isVisibility());
                items.setEventDate(notes.getEventDate() != null ? new Date(notes.getEventDate().getTime()) : null);
                items.setEditable(user.equals(notes.getEmployee()));
                NewsComment[] noteComments = getTaskNoteComments(notes.getObjectID());
                if (noteComments.length > 0) {
                    items.setNotesComments(noteComments);
                } else {
                    items.setNotesComments(new NewsComment[0]);
                }
                taskNotes[i] = items;
            }
            return taskNotes;
        }
        return null;
    }

    public void deleteNote(Integer id) {
        noteServiceLocal.deleteNote(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewsComment[] getTaskNoteComments(Integer noteID) {
        return commonService.getNotecomments(noteID);
    }

    public NewsComment saveTaskNoteComments(NewsComment data) {
        return commonService.saveNoteComment(data);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WbsItem getFirstLevelWorkstreams(Integer projectId, Integer workStreamID) {
        return wbsService.getFirstLevelWorkstreams(projectId, workStreamID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEditTaskStatusDrop(Integer taskId) {
        return timesheetServiceLocal.getEditTaskStatusDrop(taskId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NumberData generateTaskNumber(Integer projectID, Date startdate, Integer objectID) {
        if (startdate != null) {
            startdate = userManager.getUser().getUserDate(startdate);
        }
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        boolean isUnique = false;
        if (settings != null && settings.getTaskNumberingFormat() != null) {
            isUnique = settings.isUniqueNumber(settings.getTaskNumberingFormat(), WIDGET_DATE_YEAR, WIDGET_UNIQUE_NUMBER_ALL_PROJECT);
        }
        Integer intNumber = taskManager.getProjectTasksLastIntNumber(projectID, isUnique);
        String pojectNumber = "";
        Integer clientId = null;
        if (projectID != null) {
            List<Object[]> objects = projectManager.getProjectNumberById(projectID);
            if (objects != null && !objects.isEmpty()) {
                Object[] tt = projectManager.getProjectNumberById(projectID).get(0);
                pojectNumber = tt[0] != null ? tt[0].toString() : "";
                clientId = tt[1] != null ? (Integer) tt[1] : null;
            }
        }
        String clientCode = null;
        if (clientId != null) {
            clientCode = crmAccountManager.getCrmAccountNumberById(clientId).get(0);
        }
        String savedNumberFormat;
        if (settings != null && settings.getTaskNumberingFormat() != null) {
            if (objectID != null) {
                savedNumberFormat = taskManager.getSavedNumberformat(objectID);
                return settings.parsNumberDataForEdit(intNumber, savedNumberFormat, settings.getTaskNumberingFormat());
            }
            return settings.parseNumberDataForALL(intNumber, settings.getTaskNumberingFormat(), settings.getDelimetrTask(), startdate, clientCode, pojectNumber, "task");
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_TASK_PREFIX/*false*/);
        }
    }

    @Override
    public boolean saveTaskEditCellValue(TaskListItem rowValue, String columnCodeName) {
        EdsCompany company = taskManager.getUser().getCompany();
        try {
            EdsTask edsTask = taskManager.get(rowValue.getObjectID());
            edsTask.clear();
            if (TaskListItem.START_DATE.equals(columnCodeName)) {
                edsTask.setStartDate(rowValue.getStartDate());
                edsTask.setAllDay(rowValue.isAllDay());
                updateTaskDailyLoad(edsTask);
            } else if (TaskListItem.DUE_DATE.equals(columnCodeName)) {
                Date taskOldDueDate = edsTask.getDueDate();
                edsTask.setDueDate(rowValue.getDueDate());
                edsTask.setAllDay(rowValue.isAllDay());
                updateTaskDailyLoad(edsTask);
                shiftAllSuccessors(edsTask, null, taskManager.getUser(), false, 0, taskOldDueDate, availabilityCircularResolver.getUserTimeSlot(userManager.getUser()));
                Set<EdsWorkStream> workStreams = new HashSet<>();
                if (!edsTask.getPredecessors().isEmpty()) {
                    findPredecessorParentWS(edsTask, workStreams);
                }
                if (!edsTask.getSuccessors().isEmpty()) {
                    findSuccessorParentWS(edsTask, workStreams);
                }
                if (edsTask.getParentWS() != null) {
                    workStreams.add(edsTask.getParentWS());
                }
                for (EdsWorkStream ws : workStreams) {
                    updateWorkStreamDateRange(null, ws);
                }
                if (edsTask.getParentWS() == null && edsTask.getParentWS() != null) {
                    updateWorkStreamDateRange(null, edsTask.getParentWS());
                }
            } else if (TaskListItem.BOTH_DATE.equals(columnCodeName)) {
                edsTask.setStartDate(rowValue.getStartDate());
                edsTask.setAllDay(rowValue.isAllDay());

                Date taskOldDueDate = edsTask.getDueDate();
                edsTask.setDueDate(rowValue.getDueDate());
                edsTask.setAllDay(rowValue.isAllDay());
                updateTaskDailyLoad(edsTask);
                shiftAllSuccessors(edsTask, null, taskManager.getUser(), false, 0, taskOldDueDate, availabilityCircularResolver.getUserTimeSlot(userManager.getUser()));
            } else if (TaskListItem.PRIORITY_NAME.equals(columnCodeName)) {
                edsTask.setPriority(referenceManager.get(rowValue.getPriorityId()));
            } else if (TaskListItem.TYPE_NAME.equals(columnCodeName)) {
                edsTask.setType(referenceManager.get(rowValue.getTypeId()));
            } else if (TaskListItem.STATUS_NAME.equals(columnCodeName)) {
                EdsReference edsStatus = referenceManager.get(rowValue.getTaskStatusId());
                log.info("Task assignee status change from listing: " + edsStatus.getName());
                updateTaskCellStatus(edsTask, edsStatus, false, null);
            } else if (TaskListItem.OVERALL_STATUS_NAME.equals(columnCodeName)) {
                EdsReference edsStatus = null;
                if (rowValue.getTaskStatusId() != null) {
                    edsStatus = referenceManager.get(rowValue.getTaskStatusId());
                } else if (StringUtils.isNotBlank(rowValue.getStatusCode())) {
                    edsStatus = referenceManager.findReference(EdsTask.TASK_STATUS, rowValue.getStatusCode());
                }
                log.info("Task overall status change from listing: " + edsStatus.getName());
                updateTaskCellStatus(edsTask, edsStatus, true, null);
            } else if (TaskListItem.COMPLETE.equals(columnCodeName)) {
                if (rowValue.getComplete() != null && !rowValue.getComplete().isEmpty()) {
                    updateTaskCellPercent(edsTask, Float.valueOf(rowValue.getComplete()));
                }
            } else if (TaskListItem.PARENT_WORKSTREAM_NAME.equals(columnCodeName)) {
                EdsTask task = taskManager.get(rowValue.getObjectID());
                if (task != null) {
                    if (rowValue.getParentWorkstreamId() != null) {
                        EdsWorkStream workstream = workStreamManager.get(rowValue.getParentWorkstreamId());
                        task.setParent(workstream);
                    } else {
                        task.setParent(null);
                    }
                }
                task.setLastUpdateTime(new Date());
                taskManager.createOrUpdate(task);
                taskSolrComponent.index(task);
            } else if (TaskListItem.NAME.equals(columnCodeName)) {
                EdsTask task = taskManager.get(rowValue.getObjectID());
                if (task != null) {
                    task.setName(rowValue.getName());
                }
                task.setLastUpdateTime(new Date());
                taskManager.createOrUpdate(task);
                taskSolrComponent.index(task);
            } else {
                EdsTaskCustomFields edsTaskCustomFields = edsTask.getTaskCustomFields();
                if (edsTaskCustomFields == null) {
                    edsTaskCustomFields = new EdsTaskCustomFields();
                    taskCFManager.create(edsTaskCustomFields);
                    edsTask.setTaskCustomFields(edsTaskCustomFields);
                }
                CustomFieldsUtils.setDomenObjectFieldChange(edsTaskCustomFields, rowValue.getCustomFields(), columnCodeName);
            }
            edsTask.setLastUpdateTime(new Date());
            taskSolrComponent.index(edsTask);

            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsTask, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_TASK);

            return true;
        } catch (Exception e) {
            log.error("Task List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }

    /**
     * <h1>... This is method update task status ...</h1>
     * <br/>
     * <h2>... Writer Developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created Date {01:40 27/05/2011} ...</h3>
     *
     * @param task
     * @param edsStatus
     * @param isOveralStatus
     */
    private void updateTaskCellStatus(EdsTask task, EdsReference edsStatus, boolean isOveralStatus, String note) {
        if (edsStatus == null) {
            System.out.println("STATUS IS NULL" + task.getObjectID());
            return;
        }
        final EdsUser user = userManager.getUser();
        List<EdsReference> taskStatuses = new ArrayList<>();
        Set<EdsEmployeeTask> etList = task.getUnDeletedAssignments();

        Date completedDate = null;
        boolean isCompleted = edsStatus.getCode().equals(EdsTask.COMPLETED) || edsStatus.getCode().equals(EdsTask.CLOSED);
        if (isCompleted) {
            completedDate = new Date();
        }

        for (final EdsEmployeeTask etTask : etList) {
            etTask.enableTaskChangeListener(new EdsEmployeeTask.ChangeListener() {
                public void onStatusChange(EdsReference value) {
                    baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, etTask, user);
                }

                public void onPercentChange(Float percent) {
                }
            });

            if (!isOveralStatus) {
                if (user.getObjectID().equals(etTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID())) {
                    etTask.setStatus(edsStatus);
                    if (isCompleted && edsStatus.getCode().equals(EdsTask.COMPLETED)) {
                        etTask.setCompletedDate(completedDate);
                    } else if (isCompleted && edsStatus.getCode().equals(EdsTask.CLOSED)) {
                        etTask.setClosedDate(completedDate);
                    } else {
                        etTask.setCompletedDate(null);
                    }
                } else {
                    etTask.setStatus(edsStatus);
                }
                taskStatuses.add(etTask.getStatus());
            } else {
                etTask.setStatus(edsStatus);
                if (isCompleted && edsStatus.getCode().equals(EdsTask.COMPLETED)) {
                    etTask.setCompletedDate(completedDate);
                } else if (isCompleted && edsStatus.getCode().equals(EdsTask.CLOSED)) {
                    etTask.setClosedDate(completedDate);
                } else {
                    etTask.setCompletedDate(null);
                }
            }
        }
        insertTaskStatusHistory(task, edsStatus, user, note);
        task.setStatus(edsStatus);

        if (!taskStatuses.isEmpty()) {
            task.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, task.getTaskLastStatus(task.getStatus(), taskStatuses)));
        }

        task.setLastUpdateTime(new Date());
        taskRbacManager.addRbacEntries(task);

        if (isCompleted) {
            baseEventPostProcessor.registerEvent(StatusTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
            if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                updateTaskCellPercent(task, 100f);
            }
        }
    }

    /**
     * <h1>... This is method update task percent complate ...</h1>
     * <br/>
     * <h2>... Writer Developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created Date {02:30 27/07/2011} ...</h3>
     *
     * @param task
     * @param complete
     */
    public void updateTaskCellPercent(final EdsTask task, Float complete) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (settings == null || !settings.isAutomatic()) {//if settings is null then project percent calculation mode is manual by default
            final EdsUser user = userManager.getUser();
            Set<EdsEmployeeTask> etList = task.getUnDeletedAssignments();
            for (EdsEmployeeTask etTask : etList) {
                etTask.enableTaskChangeListener(new EdsEmployeeTask.ChangeListener() {
                    public void onStatusChange(EdsReference value) {
                    }

                    public void onPercentChange(Float percent) {
                        Float average;
                        Float averageProjectTasks;
                        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                            average = task.getTaskAveragePercentCompletedNewLogic();
                            averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompletedNewLogic();
                        } else {
                            average = task.getTaskAveragePercentCompleted();
                            averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompleted();
                        }
                        task.setPreviousPercent(task.getPercent());
                        task.setPercent(average);
                        task.getProject().setPercent(averageProjectTasks);
                    }
                });
                etTask.setPercent(complete);
                task.setLastUpdateTime(new Date());
                taskRbacManager.addRbacEntries(task);
            }
        }
    }

    public void importDataFromMPPFile(LinkedList<Task> listTasks, HashMap params) throws NumberExistingException {
        if (listTasks != null && !listTasks.isEmpty()) {
            String[] strings1 = (String[]) params.get(CommandConstants.PROJECT_ID);
            Integer projectId = Integer.valueOf(strings1[0]);
            String[] strings2 = (String[]) params.get(CommandConstants.BILLABLE);
            String bill = strings2[0];
            boolean billable = true;
            if ("false".equals(bill)) {
                billable = false;
            }
            EdsUser user = employeeManager.getUser();
            Map<Integer, SelectItem> parentTasks = new HashMap<>();
            List<SelectItem> succTasks = new ArrayList<>();
            List<SelectItem> parentWorkstreams = new ArrayList<>();
            EdsProject project = projectManager.get(projectId);
            EdsTimeSlotItem[] timeSlotItems = user.getCompany().getDefaultTimeSlot().getItems().toArray(new EdsTimeSlotItem[]{});
            for (EdsTimeSlotItem timeSlotItem : timeSlotItems) {
                if ((timeSlotItem.getEndTime() - timeSlotItem.getStartTime()) != 0) {
                }
            }
            ProjectCalendar projectCalendar = commonServiceLocal.createProjectCalendar(new ProjectFile(), user.getCompany().getDefaultTimeSlot(), "MS Project Calendar");
            Map<String, EdsProjectEmployee> projEmployees = new HashMap<>();
            List<EdsProjectEmployee> projectEmployees = projectEmployeeManager.getProjectEmployees(project);
            for (EdsProjectEmployee projectEmployee1 : projectEmployees) {
                EdsEmployee employee = projectEmployee1.getEmployeeDepartment().getEmployee();
                String key = employee.getFirstName().trim().toLowerCase() + ((employee.getLastName() == null || "".equals(employee.getLastName())) ? "" : employee.getLastName().trim().toLowerCase());
                projEmployees.put(key, projectEmployee1);
            }
            EdsReference mediumStatus = referenceManager.findReference(EdsTask.TASK_PRIORITY, EdsTask.MEDIUM);
            for (int i = 1; i < listTasks.size(); i++) {
                Task task = listTasks.get(i);
                if (task.getStart() != null && task.getFinish() != null) {
                    if (task.getStart().after(task.getFinish())) {

                        continue;
                    }
                    if (task.getChildTasks().isEmpty()) {
                        TaskSingleItem newTask = new TaskSingleItem();
                        newTask.setProjectID(projectId);
                        newTask.setName(task.getName() != null ? task.getName() : "");
                        newTask.setDescription(task.getNotes());
                        newTask.setStartDate(task.getStart());
                        newTask.setDueDate(task.getFinish());
                        newTask.setAllDay(true);
                        newTask.setTaskGanttOrder(task.getID());
                        if (task.getDuration() != null) {
                            BigDecimal duration = new BigDecimal(task.getDuration().getDuration());
                            if (TimeUnit.HOURS.equals(task.getDuration().getUnits())) {
                                duration = BigDecimal.valueOf(60L * duration.intValue());
                            } else if (TimeUnit.DAYS.equals(task.getDuration().getUnits())) {
                                duration = BigDecimal.valueOf(24L * 60 * duration.intValue());
                            } else if (TimeUnit.WEEKS.equals(task.getDuration().getUnits()) || TimeUnit.MONTHS.equals(task.getDuration().getUnits()) || TimeUnit.YEARS.equals(task.getDuration().getUnits())) {
                                duration = BigDecimal.valueOf((long) duration.intValue() * Double.valueOf(projectCalendar.getDuration(task.getStart(), task.getFinish()).getDuration()).intValue() * 24 * 60);
                            }
                            duration = duration.setScale(2, RoundingMode.HALF_DOWN);
                            newTask.setEstimatedTime(duration.intValue());
                        }
                        newTask.setPriorityID(mediumStatus.getObjectID());
                        List<ResourceAssignment> assignments = task.getResourceAssignments();
                        List<IdTime> assEmpl = new ArrayList<>();
                        Float overalPercent = task.getPercentageComplete() != null ? (Float) task.getPercentageComplete().floatValue() : Float.valueOf("0.0");
                        newTask.setPercent(overalPercent);
                        EdsProjectEmployee pemployee = null;
                        for (ResourceAssignment res : assignments) {
                            if (res != null && res.getResource() != null && res.getResource().getName() != null) {
                                String[] fullName = res.getResource().getName().split(" ");
                                String key = "";
                                if (fullName != null && fullName.length == 1) {
                                    key = fullName[0] != null ? fullName[0].toLowerCase() : "";
                                } else if (fullName != null && fullName.length >= 2) {
                                    key = fullName[0] != null ? fullName[0].toLowerCase() : "";
                                    key += fullName[1] != null ? fullName[1].toLowerCase() : "";
                                }
                                if (projEmployees.containsKey(key)) {
                                    pemployee = projEmployees.get(key);
                                }
                                if (pemployee == null && fullName != null && fullName.length >= 2) {
                                    EdsProjectEmployee projectEmployee = projectEmployeeManager.findProjectEmployeeByEmployeeName(projectId, fullName[0], fullName[1]);
                                    if (projectEmployee != null) {
                                        EdsEmployee edsEmployee = projectEmployee.getEmployeeDepartment().getEmployee();
                                        List<EdsEmployee> companyEmployees = employeeManager.getCompanyEmployees();
                                        if (companyEmployees != null && !companyEmployees.isEmpty()) {
                                            for (int p = 0; p < companyEmployees.size(); p++) {
                                                if (companyEmployees.contains(edsEmployee)) {
                                                    pemployee = addMembers(project, edsEmployee);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (pemployee != null) {
                                    Integer estTime = 0;
                                    if (newTask.getEstimatedTime() != null) {
                                        estTime = newTask.getEstimatedTime() / assignments.size();
                                    }
                                    IdTime assEmployee = new IdTime(pemployee.getObjectID(), estTime, overalPercent);
                                    assEmpl.add(assEmployee);
                                }
                                pemployee = null;
                            }
                        }
                        Integer taskId;
                        if (assEmpl.isEmpty()) {
                            EdsEmployee edsEmployee = user.getEmployee();
                            EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(edsEmployee, project);
                            if (projectEmployee == null) {
                                projectEmployee = addMembers(project, edsEmployee);
                            }
                            Integer estimate = newTask.getEstimatedTime() != null ? newTask.getEstimatedTime() : Integer.valueOf(0);
                            IdTime assEmployee = new IdTime(projectEmployee.getObjectID(), estimate, overalPercent);
                            assEmpl.add(assEmployee);
                            taskId = saveImportedTasks(newTask, assEmpl.toArray(new IdTime[]{}), true, billable);
                        } else {
                            Integer estTime = 0;
                            if (newTask.getEstimatedTime() != null) {
                                estTime = newTask.getEstimatedTime() / assignments.size();
                            }
                            int diff = 0;
                            if (estTime * assignments.size() < newTask.getEstimatedTime() || estTime * assignments.size() > newTask.getEstimatedTime()) {
                                diff = newTask.getEstimatedTime() - estTime * assignments.size();
                            }
                            if (diff != 0) {
                                for (int j = 0; j < diff; j++) {
                                    assEmpl.get(j).setTime(assEmpl.get(j).getTime() + (Integer.compare(diff, 0)));
                                }
                            }
                            IdTime[] employees = assEmpl.toArray(new IdTime[]{});
                            newTask.setProjectEmployees(employees);
                            taskId = saveImportedTasks(newTask, assEmpl.toArray(new IdTime[]{}), false, billable);
                        }
                        assEmpl.clear();
                        SelectItem taskItem = new SelectItem(taskId, task.getUniqueID().toString());
                        parentTasks.put(i, taskItem);
                        if (task.getPredecessors() != null) {
                            succTasks.add(taskItem);
                        }
                    } else {
                        WorkstreamSingleItem newWorkStream = new WorkstreamSingleItem();
                        newWorkStream.setNumberData(generateWorkstreamNumber(projectId, task.getStart(), null));
                        newWorkStream.setName(task.getName());
                        newWorkStream.setDescription(task.getNotes());
                        newWorkStream.setStartDate(task.getStart());
                        newWorkStream.setEndDate(task.getFinish());
                        newWorkStream.setProjectID(projectId);
                        newWorkStream.setTaskGanttOrder(task.getID());
                        if (task.getParentTask() != null) {
                            for (SelectItem parentWorkstream : parentWorkstreams) {
                                if (parentWorkstream.getName().equals(task.getParentTask().getUniqueID().toString())) {
                                    EdsWorkStream parentWorkStream = workStreamManager.get(parentWorkstream.getId());
                                    newWorkStream.setParentWSID(parentWorkStream.getObjectID());
                                    break;
                                }
                            }
                        }

                        Integer workstreamId = createWorkstream(newWorkStream, null);
                        SelectItem wsItem = new SelectItem(workstreamId, task.getUniqueID().toString());
                        parentWorkstreams.add(wsItem);
                    }
                }
            }
            if (project != null && listTasks != null && !listTasks.isEmpty()) {
                baseEventPostProcessor.registerEvent(ImportTasksFromMPPFileEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user);
            }
            //------------------------------------------ for checking predecessors, successors ---------------------------------
            for (int m = 1; m < listTasks.size(); m++) {
                Task importedTask = listTasks.get(m);
                EdsTask predTask = new EdsTask();
                if (importedTask.getSuccessors() != null) {
                    if (parentTasks.get(m) != null) {
                        predTask = taskManager.get(parentTasks.get(m).getId());
                    }
                    for (int k = 0; k < importedTask.getSuccessors().size(); k++) {
                        for (SelectItem succTask1 : succTasks) {
                            if (succTask1.getName() != null && importedTask != null && importedTask.getSuccessors().get(k) != null &&
                                    importedTask.getSuccessors().get(k).getTargetTask() != null &&
                                    importedTask.getSuccessors().get(k).getTargetTask().getUniqueID() != null && succTask1.getName().equals(importedTask.getSuccessors().get(k).getTargetTask().getUniqueID().toString())) {
                                EdsTask succTask = taskManager.get(succTask1.getId());
                                if (predTask != null) {
                                    predTask.getSuccessors().add(succTask);
                                    taskManager.update(predTask);
                                    baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, predTask, user);
                                }
                            }
                        }
                    }
                }
                //------------------------------------------ checking tasks for workstreams ------------------------------------
                if (importedTask.getParentTask() != null && importedTask.getChildTasks().isEmpty()) {
                    for (SelectItem parentWorkstream : parentWorkstreams) {
                        if (parentWorkstream.getName().equals(importedTask.getParentTask().getUniqueID().toString())) {
                            EdsWorkStream ws = workStreamManager.get(parentWorkstream.getId());
                            predTask = taskManager.get(parentTasks.get(m).getId());
                            predTask.setParentWS(ws);
                            taskManager.update(predTask);
                            baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, predTask, user);
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("MS Project File successfully imported.");
    }

    private Integer saveImportedTasks(TaskSingleItem newTask, IdTime[] assignees, boolean assigned, boolean billable) {
        EdsUser user = employeeManager.getUser();
        EdsTask task = new EdsTask();
        task.setName(newTask.getName());
        task.setDescription(newTask.getDescription());
        task.setStartAndDueDates(newTask.getStartDate(), newTask.getDueDate());
        task.setBillable(billable);
        task.setPercent(newTask.getPercent());
        task.setEstimatedTime(newTask.getEstimatedTime());
        task.setPriority(referenceManager.get(newTask.getPriorityID()));
        if (newTask.getTypeID() != null) {
            task.setType(referenceManager.get(newTask.getTypeID()));
        }
        task.setAllDay(newTask.isAllDay());
        task.setCreator(userManager.getUser());
        task.setTaskGanttOrder(newTask.getTaskGanttOrder());
        if (newTask.getProjectID() != null) {
            task.setProject(projectManager.get(newTask.getProjectID()));

            NumberData numberData = generateTaskNumber(newTask.getProjectID(), newTask.getStartDate(), null);
            if (numberData != null) {
                task.setIntNumber(numberData.getIntNumber());
                if (numberData.getSavedNumberFormula() != null && !"".equals(numberData.getSavedNumberFormula())) {
                    task.setSavedNumberFormula(numberData.getSavedNumberFormula());
                } else if (numberData.getFirstNumberString() != null && !"".equals(numberData.getFirstNumberString())) {
                    task.setSavedNumberFormula(("".equals(numberData.getFirstNumberString()) ? "null" : numberData.getFirstNumberString()) + SAV_NUM_DEL + ("".equals(numberData.getIntNumber()) ? "null" : numberData.getIntNumber()) + SAV_NUM_DEL + ("".equals(numberData.getLastNumberString()) ? "null" : numberData.getLastNumberString()));
                }
                task.setNumber(numberData.getNumberString());
            }
        }
        if (newTask.getWorkstreamID() != null) {
            task.setParentWS(workStreamManager.get(newTask.getWorkstreamID()));
        }

        taskManager.create(task);

        Integer allTimeSpent = 0;
        Float allPercent = 0.0f;
        for (IdTime assignee : assignees) {
            EdsProjectEmployee projectEmployee = projectEmployeeManager.get(assignee.getId());
            IdTime projectEmployees = new IdTime(projectEmployee.getObjectID(), assignee.getTime(), assignee.getPercent());
            saveImportedTaskAssignees(user, task, projectEmployees, assigned);
            allTimeSpent += assignee.getTime();
            allPercent += assignee.getPercent();
        }

        if (newTask.getPercent().equals(100.0f)) {
            task.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED));
        } else if (newTask.getPercent().equals(0.0f) && allTimeSpent.equals(0) && allPercent.equals(0.0f)) {
            task.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED));
        } else if (allTimeSpent > 0 || allPercent > (0.0f) || newTask.getPercent() > (0.0f)) {
            task.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS));
        }

        task.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, task.getTaskLastStatus()));
        taskRbacManager.addRbacEntries(task);
        baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user);
        System.out.println("Save MS Project Task. Task ID=" + task.getObjectID() + "; Task Name: " + task.getName());
        return task.getObjectID();
    }

    private void saveImportedTaskAssignees(EdsUser user, EdsTask task, IdTime assignees, boolean assigned) {
        if (assignees != null) {
            EdsProjectEmployee pe = projectEmployeeManager.get(assignees.getId());
            EdsEmployeeTask empTask = new EdsEmployeeTask(task, pe);
            task.getAssignments().add(empTask);

            if (assignees.getTime() != null) {
                if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
                    updateDailyEstimates(user, task, assignees, pe, empTask);
                }

                if (assignees.getTime() == 0 && assignees.getPercent().equals(0.0f)) {
                    empTask.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED));
                } else if (assignees.getPercent().equals(100.0f)) {
                    empTask.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED));
                } else {
                    empTask.setStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS));
                }
            }
            empTask.setStartDate(new Date());
            empTask.setTimeSpent(assignees.getActualTime());
            empTask.setEstimatedTime(assignees.getTime());
            if (assignees.getPercent() != null) {
                empTask.setPercent(assignees.getPercent());
            }
            empTask.setNewTask(true);
            empTask.setDeleted(assigned);
            task.setLastUpdateTime(new Date());
        }
    }

    private void updateDailyEstimates(EdsUser user, EdsTask task, IdTime assignees, EdsProjectEmployee pe, EdsEmployeeTask empTask) {
        EdsEmployee edsEmployee = pe.getEmployeeDepartment().getEmployee();
        Set<EdsTimeSlotItem> timeSlotItem = edsEmployee.getTimeSlot().getItems();
        Map<Integer, Integer> available = new HashMap<>();
        for (EdsTimeSlotItem item : timeSlotItem) {
            available.put(item.getDay(), item.getEndTime() - item.getStartTime());
        }
        Calendar startDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();
        startDate.setTime(new Date(task.getStartDate().getTime() + user.getUserTimezone().getRawOffset()));
        dueDate.setTime(new Date(task.getDueDate().getTime() + user.getUserTimezone().getRawOffset()));
        int k = 0;
        ArrayList<Calendar> availableDays = new ArrayList<>();
        while (dueDate.getTime().compareTo(startDate.getTime()) > 0) {
            if (available.containsKey(startDate.get(Calendar.DAY_OF_WEEK) - 1)
                    && available.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) != null && available.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) != 0) {
                k++;
                Calendar nonDate = Calendar.getInstance();
                nonDate.setTime(startDate.getTime());
                ServerUtils.setBeginningOfTheDay(nonDate);
                availableDays.add(nonDate);
            }
            startDate.add(Calendar.DAY_OF_YEAR, 1);
        }
        if (k == 0) {
            k = 1;
        }
        int dailyLoad = assignees.getTime() / k;
        int dailyLoadQ = assignees.getTime() % k;
        empTask.setDailyLoad(dailyLoad);
        //insert timeSheet data with daily estimated time
        if (dailyLoad >= 0 && empTask.getObjectID() != null) {
            availabilityCircularResolver.createOrUpdateTimeSheetDataWithDailyEstimatedTime(edsEmployee, empTask, availableDays, dailyLoad, dailyLoadQ);
        }
    }

    public EdsProjectEmployee addMembers(EdsProject project, EdsEmployee employee) {
        EdsProjectEmployee pe = new EdsProjectEmployee();
        EdsEmployeeDepartment employeeDepartment = employee.getEmployeeTeam();
        if (employeeDepartment != null) {
            pe = new EdsProjectEmployee(employeeDepartment, project);
            EdsUser user = employeeManager.getUser();
            pe.setClientChargeRate(employee.getClientChargeRate());
            pe.setWageRate(employee.getWageRate());
            projectEmployeeManager.create(pe);

            EdsProjectEmployeeWageClientRateHistory history = new EdsProjectEmployeeWageClientRateHistory();
            history.setProjectEmployee(pe);
            history.setChangeDate(new Date());
            history.setWageRate(employee.getWageRate());
            history.setClientChargeRate(employee.getClientChargeRate());
            projectManager.updateEmployeeWageClientRateHistorybyDate(history);

            if (!employee.equals(project.getManager())
                    && !project.isUserBackupManager(employee.getObjectID())
                    && !employee.getAccountStatus().getCode().equals(EMPLOYEE_STATUS_NO_ACCCESS)) {
                baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pe, user);

            }
        }
        return pe;
    }

    /**
     * Will delete all task instances in the recurring series but will leaves the tasks marked as Exception
     * <br><b>Note:</b> Exception tasks will be taken out of recurrence and will become normal non-recurring tasks
     *
     * @param task
     * @param allFollowing
     */
    private void deleteAllInstances(EdsTask task, boolean allFollowing) {
        if (task.getRecurrenceID() != null) {
            EdsRecurrence recurrence = recurrenceManager.get(task.getRecurrenceID());
            if (recurrence != null) {
                List<EdsTask> tasksToRemove;
                if (allFollowing) {
                    tasksToRemove = taskManager.getAllTaskInstancesAfter(recurrence.getObjectID(), task.getFireTime());
                    deleteMultiTask(tasksToRemove);
                    if (recurrence.getEndType().equals(END_BY_DATE)) {
                        Date newEndDate = taskManager.getRecurringTaskFirstOrLastDate(task.getRecurrenceID(), task.getFireTime(), false);
                        recurrence.setEndDate(newEndDate);
                    } else if (recurrence.getEndType().equals(END_AFTER_OCCURRENCES)) {
                        recurrence.setOccurrence(taskManager.getAllTaskInstancesSize(task.getRecurrenceID()).intValue());
                    }
                } else {
                    tasksToRemove = taskManager.getAllTaskInstances(recurrence.getObjectID());
                    deleteMultiTask(tasksToRemove);
                }
            }
        }
    }

    private void deleteMultiTask(List<EdsTask> tasksToRemove) {
        if (tasksToRemove != null && !tasksToRemove.isEmpty()) {
            for (EdsTask taskItem : tasksToRemove) {
                deleteTask(taskItem.getObjectID(), null);
            }
        }
    }

    private void clearOldParentWSCalculatedItemsOfTask(EdsTask task) {
        //clear old Work Stream calculated items of task
        EdsWorkStream oldWorkStream = task.getParentWS();

        EstimateTimeSpentItem estimateTimeSpent = employeeTaskManager.getEstimatedTimeSpent(task.getObjectID());
        oldWorkStream.updateEstimatedTime(estimateTimeSpent.getEstimatedTime() - task.getEstimatedTime());
        oldWorkStream.updateActualTime(estimateTimeSpent.getTimeSpent() - task.getTimespent());

        oldWorkStream.updatePlannedWageAmmount(-task.getPlannedWageAmount());
        oldWorkStream.updatePlannedClientChargeAmmount(-task.getPlannedClientChargeAmount());
        oldWorkStream.updateActualWageAmmount(-task.getActualWageAmount());
        oldWorkStream.updateActualClientChargeAmmount(-task.getActualClientChargeAmount());

        oldWorkStream.updateWageAmmount(-(task.getActualWageAmount() + task.getRemainingWageAmount()));
        oldWorkStream.updateClientChargeAmmount(-(task.getActualClientChargeAmount() + task.getRemainingClientChargeAmount()));

        //update old work stream
        workStreamManager.update(oldWorkStream);

        task.setChangedCalculationFields(true);
        task.setCalculated(false);
        task.setLastUpdateTime(new Date());
    }

    private void clearOldParentWSCalculatedItemsOfWorkstream(EdsWorkStream workstream) {
        //clear old Work Stream calculated items of workstream
        EdsWorkStream oldWorkStream = workstream.getParentWS();

        oldWorkStream.updateEstimatedTime(-workstream.getEstimatedTime());
        oldWorkStream.updateActualTime(-workstream.getActualTime());

        oldWorkStream.updatePlannedWageAmmount(-workstream.getPlannedWageAmount());
        oldWorkStream.updatePlannedClientChargeAmmount(-workstream.getPlannedClientChargeAmount());
        oldWorkStream.updateActualWageAmmount(-workstream.getActualWageAmount());
        oldWorkStream.updateActualClientChargeAmmount(-workstream.getActualClientChargeAmount());

        oldWorkStream.updateWageAmmount(-workstream.getWageAmmount());
        oldWorkStream.updateClientChargeAmmount(-workstream.getClientChargeAmmount());
    }

    public Boolean deleteTask(Integer employeeID, Integer taskID, String deleteType) {

        Boolean isFullRefreshNeeded = false;
        EdsTask task = taskManager.get(taskID);
        if (deleteType == null || Constants.DELETE_THIS_INSTANCE.equals(deleteType)) {
            EdsTask lastTask = null;
            EdsTask firstTask = null;
            if (task.getRecurrenceID() != null) {
                lastTask = taskManager.getFirstOrLastTaskInRecurringSeries(task.getRecurrenceID(), false);
                firstTask = taskManager.getFirstOrLastTaskInRecurringSeries(task.getRecurrenceID(), true);
            }
            deleteTask(task.getObjectID(), null);
            task.setDeleted(true);
            if (lastTask != null && lastTask.getObjectID().intValue() == task.getObjectID().intValue()) {
                EdsRecurrence rec = recurrenceManager.get(task.getRecurrenceID());
                if (rec.getEndType().equals(END_BY_DATE)) {
                    Date newEndDate = taskManager.getRecurringTaskFirstOrLastDate(task.getRecurrenceID(), task.getFireTime(), false);
                    rec.setEndDate(newEndDate);
                } else if (rec.getEndType().equals(END_AFTER_OCCURRENCES)) {
                    rec.setOccurrence(taskManager.getAllTaskInstancesSize(task.getRecurrenceID()).intValue());
                }
                isFullRefreshNeeded = true;
            } else if (firstTask != null && firstTask.getObjectID().intValue() == task.getObjectID().intValue()) {
                EdsRecurrence rec = recurrenceManager.get(task.getRecurrenceID());
                Date newStartDate = taskManager.getRecurringTaskFirstOrLastDate(task.getRecurrenceID(), task.getFireTime(), true);
                rec.setStartDate(newStartDate);
                if (rec.getEndType().equals(END_AFTER_OCCURRENCES)) {
                    rec.setOccurrence(taskManager.getAllTaskInstancesSize(task.getRecurrenceID()).intValue());
                }
                isFullRefreshNeeded = true;
            }
            task.setRecurrenceID(null);
            // delete task overdue reminders and recurrences
            taskReminderManager.deleteTaskReminders(taskID);
            EdsUser user = task.getCreator() != null ? task.getCreator() : userManager.getUser();
            List<EdsRecurrence> recurrenceList = recurrenceManager.getRecurrenceJobList(TASK_OVERDUE_REMINDER, taskID, user.getCompany().getObjectID());
            if (recurrenceList != null && !recurrenceList.isEmpty()) {
                for (EdsRecurrence rec : recurrenceList) {
                    recurrenceService.updateRecurrence(rec, true, true);
                }
            }
            taskManager.update(task);
        } else if (Constants.DELETE_ALL_SERIES.equals(deleteType)) {
            deleteAllInstances(task, false);
            isFullRefreshNeeded = true;
            if (task.getRecurrenceID() != null) {
                EdsRecurrence recurrence = recurrenceManager.get(task.getRecurrenceID());
                recurrenceService.updateRecurrence(recurrence, true, true);
            }
        } else if (Constants.DELETE_ALL_FOLLOWING.equals(deleteType)) {
            deleteAllInstances(task, true);
        }
        return isFullRefreshNeeded;
    }

    public void updateTaskDailyLoad(EdsTask edsTask) {
        updateTaskDailyLoad(edsTask, null);
    }

    public void updateTaskDailyLoad(EdsTask edsTask, IdTime[] projectEmployees) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
            return;
        }
        EdsUser user = edsTask.getCreator();
        if (user == null) {
            System.out.println("Task without creator! " + edsTask.getObjectID());
            return;
        }
        boolean isEnableResourceUtilization = false;
        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);
        if (resourcePlanning != null) {
            isEnableResourceUtilization = true;
        }

        Set<EdsEmployeeTask> empTaskSet = edsTask.getUnDeletedAssignments();
        for (EdsEmployeeTask empTask : empTaskSet) {
            EdsProjectEmployee edsProjectEmployee = empTask.getProjectEmployee();
            EdsEmployee edsEmployee = employeeManager.get(edsProjectEmployee.getEmployeeDepartment().getEmployee().getObjectID());
            Set<EdsTimeSlotItem> timeSlotItem = edsEmployee.getTimeSlot().getItems();
            Map<Integer, Integer> available = new HashMap<>();
            for (EdsTimeSlotItem item : timeSlotItem) {
                available.put(item.getDay(), item.getEndTime() - item.getStartTime());
            }
            if (edsTask.getStartDate() != null && edsTask.getDueDate() != null) {
                Boolean startResourceCalculationForNewAssigneesFromToday = null;
                if (isEnableResourceUtilization && projectEmployees != null && projectEmployees.length > 0) {
                    boolean isTaskOldAssignee = false;
                    //change estimate time
                    for (IdTime pE : projectEmployees) {
                        if (pE.getId().equals(edsProjectEmployee.getObjectID())) {
                            isTaskOldAssignee = true;
                            break;
                        }
                    }
                    //start resource calculation for new employees from today
                    for (IdTime pE : projectEmployees) {
                        if (pE.getId().equals(edsProjectEmployee.getObjectID())) {
                            if (pE.getStartResourceCalculationForNewAssigneesFromToday() != null) {
                                startResourceCalculationForNewAssigneesFromToday = pE.getStartResourceCalculationForNewAssigneesFromToday();
                            }
                            break;
                        }
                    }

                    if (!isTaskOldAssignee) {
                        continue;
                    }
                }

                Calendar startDate = new GregorianCalendar(user.getUserTimezone());
                Calendar dueDate = new GregorianCalendar(user.getUserTimezone());
                long startLongTime = startResourceCalculationForNewAssigneesFromToday != null ? (startResourceCalculationForNewAssigneesFromToday ? new Date().getTime() : edsTask.getStartDate().getTime()) : edsTask.getStartDate().getTime();
                startDate.setTime(new Date(startLongTime + user.getUserTimezone().getRawOffset()));
                dueDate.setTime(new Date(edsTask.getDueDate().getTime() + user.getUserTimezone().getRawOffset()));
                int availableDayCount = 0;

                ArrayList<Holiday> companyHolidays = availabilityCircularResolver.getCompanyHolidayList();
                ArrayList<Holiday> employeeHolidays = availabilityCircularResolver.getHolidaysList(edsEmployee);
                ArrayList<Calendar> employeeLeaves = availabilityCircularResolver.getEmployeeLeaves(startDate.getTime(), dueDate.getTime(), edsEmployee);


                ArrayList<Calendar> availableDays = new ArrayList<>();

                Calendar start = Calendar.getInstance();
                start.setTime(startDate.getTime());
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MILLISECOND, 0);

                while (dueDate.getTime().compareTo(start.getTime()) >= 0) {
                    Integer dayIndex = start.get(Calendar.DAY_OF_WEEK) - 1;

                    if (available.containsKey(dayIndex) && available.get(dayIndex) != null && available.get(dayIndex) != 0
                            && !(availabilityCircularResolver.isHoliday(start, companyHolidays) != null || availabilityCircularResolver.isHoliday(start, employeeHolidays) != null)
                            && !(availabilityCircularResolver.isLrDay(start, employeeLeaves))) {
                        availableDayCount++;
                        Calendar nonDate = Calendar.getInstance();
                        nonDate.setTime(start.getTime());
                        ServerUtils.setBeginningOfTheDay(nonDate);
                        availableDays.add(nonDate);
                    }
                    start.add(Calendar.DAY_OF_MONTH, 1);
                }
                if (availableDays.isEmpty()) {//availableDays yo'q bolsa task end datega estimatelarni set qilishga kelishildi
                    Calendar nonDate = Calendar.getInstance();
                    nonDate.setTime(dueDate.getTime());
                    ServerUtils.setBeginningOfTheDay(nonDate);
                    availableDays.add(nonDate);
                }

                if (availableDayCount == 0) {
                    availableDayCount = 1;
                }
                int estTime = 0;
                if (empTask.getEstimatedTime() != null) {
                    estTime = empTask.getEstimatedTime();
                }
                int dailyLoad = estTime / availableDayCount;
                int dailyLoadQ = estTime % availableDayCount;
                empTask.setDailyLoad(dailyLoad);

                //insert timeSheet data with daily estimated time
                if (dailyLoad >= 0 && isEnableResourceUtilization /*&& availableDays.size() > 0*/) {
                    timeSheetManager.updateDailyEstimatedTime(empTask.getObjectID());
                    availabilityCircularResolver.createOrUpdateTimeSheetDataWithDailyEstimatedTime(edsEmployee, empTask, availableDays, dailyLoad, dailyLoadQ, AvailabilityCircularResolver.FROM_RESOURCE_UTIL);
                }
            }
        }
    }

    @Transactional
    public Integer indexCompanyProjects(SolrReindexRpc solrReindex, Integer start, int limit) {
        List<EdsProject> projects = projectManager.getCompanyProjectsForSolr(solrReindex, start, limit);
        if (projects.isEmpty()) {
            return -1;
        }

        try {
            projectSolrComponent.indexes(projects);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Indexed " + limit + " Projects");
        return projects.get(projects.size() - 1).getObjectID();
    }

    public void calculateTaskBudgets(Integer taskID) {
        if (taskID == null) {
            return;
        }
        EdsTask task = taskManager.get(taskID);
        if (task == null) {
            return;
        }
        calculateTaskBudgets(task);
    }

    public void calculateTaskBudgets(EdsTask task) {
        //Employee task calculation items
        double planedWageAmount;
        double planedClientChargeAmount;

        Integer timeSpent;
        BigDecimal actualWageAmount;
        BigDecimal actualClientChargeAmount;

        //Task calculation items
        Integer taskTimeSpent = 0;
        BigDecimal taskPlanedWageAmount = new BigDecimal(0);
        BigDecimal taskPlanedClientChargeAmount = new BigDecimal(0);
        BigDecimal taskActualWageAmount = new BigDecimal(0);
        BigDecimal taskActualClientChargeAmount = new BigDecimal(0);
        BigDecimal taskRemainingWageAmount = new BigDecimal(0);
        BigDecimal taskRemainingClientChargeAmount = new BigDecimal(0);

        EdsReference approve = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE");
        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);
        boolean isRUEnabled = resourcePlanning != null;

        for (EdsEmployeeTask employeeTask : task.getAssignments()) {
            EdsProjectEmployee projectEmployee = employeeTask.getProjectEmployee();
            List<EdsProjectEmployeeWageClientRateHistory> wagesHistory = projectEmployee.getWageClientRatesHistory();

            planedWageAmount = 0;
            planedClientChargeAmount = 0;

            timeSpent = 0;
            actualWageAmount = new BigDecimal(0);
            actualClientChargeAmount = new BigDecimal(0);
            if (employeeTask.getTimeSheets() != null && !employeeTask.getTimeSheets().isEmpty()) {
                for (EdsTimeSheet timeSheet : employeeTask.getTimeSheets()) {
                    if (timeSheet.getStatus() != null && timeSheet.getStatus().equals(approve)) {
                        //re calculate employee task time spent
                        timeSpent += timeSheet.getTimeSpent();
                        for (int j = 0; j < wagesHistory.size(); j++) {
                            EdsProjectEmployeeWageClientRateHistory current = wagesHistory.get(j);

                            //if timesheet date lays before first salary entry
                            if ((timeSheet.getDate().before(current.getChangeDate()) || timeSheet.getDate().equals(current.getChangeDate())) && j == 0) {
                                actualWageAmount = actualWageAmount.add(BigDecimal.valueOf(current.getWageRate() * ((double) timeSheet.getTimeSpent() / 60))); //should take the difference, correct

                                //if task is billable when calculate "actual client charge cost"
                                if (task.getBillable()) {
                                    actualClientChargeAmount = actualClientChargeAmount.add(BigDecimal.valueOf(current.getClientChargeRate() * ((double) timeSheet.getTimeSpent() / 60)));
                                }

                                break;
                            }

                            EdsProjectEmployeeWageClientRateHistory next = null;

                            if (j != (wagesHistory.size() - 1)) {
                                next = wagesHistory.get(j + 1);
                            }

                            //if timesheet date between salaries change time range, take the lowest salariy change date
                            if ((timeSheet.getDate().after(current.getChangeDate()) || timeSheet.getDate().equals(current.getChangeDate())) && ((next == null || timeSheet.getDate().before(next.getChangeDate())))) {
                                actualWageAmount = actualWageAmount.add(BigDecimal.valueOf(current.getWageRate() * ((double) timeSheet.getTimeSpent() / 60))); //should take the difference, correct

                                //if task is billable when calculate "actual client charge cost"
                                if (task.getBillable()) {
                                    actualClientChargeAmount = actualClientChargeAmount.add(BigDecimal.valueOf(current.getClientChargeRate() * ((double) timeSheet.getTimeSpent() / 60)));
                                }

                                break;
                            }
                        }
                    }
                    if (timeSheet.getDailyEstimatedTime() != null && isRUEnabled && employeeTask.getDeleted() != null && !employeeTask.getDeleted()) {
                        planedWageAmount += timeSheet.getWageRate() * ((double) timeSheet.getDailyEstimatedTime() / 60);
                        if (task.getBillable()) {
                            Double d = timeSheet.getClientChargeRate() != null ? timeSheet.getClientChargeRate() : 0d;
                            planedClientChargeAmount += d * ((double) timeSheet.getDailyEstimatedTime() / 60);
                        }
                    }
                }
            }

            //if task employee is deleted not calculate planed amounts
            if (employeeTask.getDeleted() != null && !employeeTask.getDeleted()) {
                if (!isRUEnabled) {
                    planedWageAmount = ((double) employeeTask.getEstimatedTime() / 60) * projectEmployee.getWageRate();
                }
                taskPlanedWageAmount = taskPlanedWageAmount.add(new BigDecimal(planedWageAmount));

                //if task is billable when calculate "planed client charge cost"
                if (task.getBillable()) {
                    if (!isRUEnabled) {
                        planedClientChargeAmount = ((double) employeeTask.getEstimatedTime() / 60) * projectEmployee.getClientChargeRate();
                    }
                    taskPlanedClientChargeAmount = taskPlanedClientChargeAmount.add(new BigDecimal(planedClientChargeAmount));
                }
            }

            employeeTask.setPlannedWageAmount(planedWageAmount);
            employeeTask.setPlannedClientChargeAmount(planedClientChargeAmount);

            employeeTask.setTimeSpent(timeSpent);
            employeeTask.setActualWageAmmount(actualWageAmount.doubleValue());
            employeeTask.setActualClientChargeAmmount(actualClientChargeAmount.doubleValue());

            taskTimeSpent += timeSpent;
            taskActualWageAmount = taskActualWageAmount.add(actualWageAmount);
            taskActualClientChargeAmount = taskActualClientChargeAmount.add(actualClientChargeAmount);

            //calculate remaining wage amounts for employee task
            double remainingWageAmount = planedWageAmount - projectEmployee.getWageRate() * employeeTask.getTimeSpent() / 60;
            if (remainingWageAmount > 0) {
                taskRemainingWageAmount = taskRemainingWageAmount.add(new BigDecimal(remainingWageAmount));
            }

            //if task is billable when calculate "remaining client charge cost"
            if (task.getBillable()) {
                double remainingClientChargeAmount = planedClientChargeAmount - projectEmployee.getClientChargeRate() * employeeTask.getTimeSpent() / 60;
                if (remainingClientChargeAmount > 0) {
                    taskRemainingClientChargeAmount = taskRemainingClientChargeAmount.add(new BigDecimal(remainingClientChargeAmount));
                }
            }
        }

        task.setTimespent(taskTimeSpent);
        task.setPlannedWageAmount(taskPlanedWageAmount.doubleValue());
        task.setPlannedClientChargeAmount(taskPlanedClientChargeAmount.doubleValue());
        task.setActualWageAmount(taskActualWageAmount.doubleValue());
        task.setActualClientChargeAmount(taskActualClientChargeAmount.doubleValue());
        task.setRemainingWageAmount(taskRemainingWageAmount.doubleValue());
        task.setRemainingClientChargeAmount(taskRemainingClientChargeAmount.doubleValue());

        //changed calculation fields are calculated!
        task.setChangedCalculationFields(false);
        task.setLastUpdateTime(new Date());
    }

    private void calculateRecursiveWSBudgets(WorkstreamSingleItem wsItem, Integer objectID) {
        if (objectID == null) {
            return;
        }

        EdsWorkStream workStream = workStreamManager.get(objectID);

        if (workStream != null) {
            if (workStream.getSubWorkStreams() != null && !workStream.getSubWorkStreams().isEmpty()) {

                //Overall Work Stream calculation budgets by all work stream items
                calculateWorkStreamBudgets(workStream.getObjectID());

                //Overall Work Stream Task Status Statistic lists
                initTaskStatusStatisticList(wsItem, workStream.getObjectID());

                //Overall Work Stream completed
                Object object = workStreamManager.getWSPercent(workStream.getObjectID());
                Integer countOfTask = ((Object[]) object)[0] != null ? ((Long) ((Object[]) object)[0]).intValue() : 0;
                Float percent = ((Object[]) object)[1] != null ? ((Double) ((Object[]) object)[1]).floatValue() : 0f;

                //Overall Work Stream Employee completed
                List<WorkstreamAssigneeItem> wsAssigneeEmployee = getWorkStreamEmployeeStatisticList(employeeTaskManager.getETStatisticByWS(workStream.getObjectID()), false);

                Date startDate = workStreamManager.getWSStartDateByTask(workStream.getObjectID());
                Date endDate = workStreamManager.getWSEndDAteByTask(workStream.getObjectID());

                for (EdsWorkStream sw : workStream.getSubWorkStreams()) {

                    //Sub Work Stream calculation budgets by all work stream items
                    calculateRecursiveWSBudgets(wsItem, sw.getObjectID());

                    percent += sw.getPercent();

                    //Sub Work Stream Task Status Statistic lists
                    //initTaskStatusStatisticList(wsItem, sw.getObjectID(), true);

                    //Sync Work Stream Employees to Sub Work Stream Employees
                    syncWSEmployeeToSWSEmployee(wsAssigneeEmployee, sw.getAssigneeEmployee());

                    Date swStartDate = workStreamManager.getWSStartDateByTask(sw.getObjectID());
                    if (startDate != null && swStartDate != null) {
                        startDate = (startDate.compareTo(swStartDate) >= 0) ? swStartDate : startDate;
                    }

                    Date swEndDate = workStreamManager.getWSEndDAteByTask(sw.getObjectID());
                    if (endDate != null && swEndDate != null) {
                        endDate = (endDate.compareTo(swEndDate) <= 0) ? swEndDate : endDate;
                    }
                }

                //Set Overall Work Stream completed
                Integer countOfWSItems = countOfTask + workStream.getSubWorkStreams().size();
                countOfWSItems = countOfWSItems != 0 ? countOfWSItems : 1;
                workStream.setPercent(percent / countOfWSItems);

                //Set Overall Work Stream assignees
                for (WorkstreamAssigneeItem wae : wsAssigneeEmployee) {
                    wae.setPercent(wae.getPercent() / (wae.getCountOfTask() != 0 ? wae.getCountOfTask() : 1));
                }
                workStream.setAssigneeEmployee(wsAssigneeEmployee);
                wsItem.setAssignees(wsAssigneeEmployee.toArray(new WorkstreamAssigneeItem[]{}));

                if (startDate != null) {
                    workStream.updateStartDate(startDate);
                }
                if (endDate != null) {
                    workStream.updateEndDate(endDate);
                }
                workStreamManager.update(workStream);
            } else {
                //Work Stream calculation budgets by all work stream items
                calculateWorkStreamBudgets(workStream.getObjectID());

                //Overall Work Stream completed
                Object obj = workStreamManager.getWSPercent(workStream.getObjectID());
                Integer countOfTask = ((Object[]) obj)[0] != null ? ((Long) ((Object[]) obj)[0]).intValue() : 0;
                Float percent = ((Object[]) obj)[1] != null ? ((Double) ((Object[]) obj)[1]).floatValue() : 0f;

                countOfTask = countOfTask != 0 ? countOfTask : 1;
                workStream.setPercent(percent / countOfTask);

                //Work Stream Employee completed
                List<WorkstreamAssigneeItem> wsAssigneeEmployee = getWorkStreamEmployeeStatisticList(employeeTaskManager.getETStatisticByWS(workStream.getObjectID()), true);
                workStream.setAssigneeEmployee(wsAssigneeEmployee);
                wsItem.setAssignees(wsAssigneeEmployee.toArray(new WorkstreamAssigneeItem[]{}));

                //Work Stream Task Status Statistic lists
                initTaskStatusStatisticList(wsItem, workStream.getObjectID());

                Date startDate = workStreamManager.getWSStartDateByTask(workStream.getObjectID());
                Date endDate = workStreamManager.getWSEndDAteByTask(workStream.getObjectID());

                if (startDate != null) {
                    workStream.updateStartDate(startDate);
                }
                if (endDate != null) {
                    workStream.updateEndDate(endDate);
                }
                workStreamManager.update(workStream);
            }
        }
    }

    private void initTaskStatusStatisticList(WorkstreamSingleItem wsItem, Integer parentID) {
        List<Object[]> tsStatistics = taskManager.getTasksStatisticByWS(parentID);
        if (tsStatistics != null && !tsStatistics.isEmpty()) {
            for (Object[] object : tsStatistics) {
                if (object[0] != null) {
                    EdsReference status = referenceManager.get((Integer) object[0]);
                    switch (status.getCode()) {
                        case EdsTask.NOT_STARTED -> wsItem.updateNotStartedTasksCount(((Long) object[1]).intValue());
                        case EdsTask.IN_PROGRESS -> wsItem.updateInProgressTasksCount(((Long) object[1]).intValue());
                        case EdsTask.COMPLETED -> wsItem.updateCompletedTasksCount(((Long) object[1]).intValue());
                        case EdsTask.CANCELLED -> wsItem.updateCancelledTasksCount(((Long) object[1]).intValue());
                        case EdsTask.CLOSED -> wsItem.updateClosedTasksCount(((Long) object[1]).intValue());
                        case EdsTask.WAITING_FOR_SOMEONE_ELSE ->
                                wsItem.updateWaitingForTasksCount(((Long) object[1]).intValue());
                    }
                }
            }
        }
    }

    private List<WorkstreamAssigneeItem> getWorkStreamEmployeeStatisticList(List<Object> items, boolean isSubWS) {
        List<WorkstreamAssigneeItem> assigneeItems = new ArrayList<>();

        Map<Integer, WorkstreamAssigneeItem> itemMap = new HashMap<>();

        if (items != null && !items.isEmpty()) {
            for (Object object : items) {

                Object[] item = (Object[]) object;

                EdsProjectEmployee pEmployee = projectEmployeeManager.get((Integer) item[0]);

                WorkstreamAssigneeItem assigneeItem = new WorkstreamAssigneeItem();
                assigneeItem.setId(pEmployee.getObjectID());
                assigneeItem.setName(pEmployee.getName());
                assigneeItem.setTime(item[1] != null ? ((Long) item[1]).intValue() : 0);
                assigneeItem.setActualSpentTime(item[2] != null ? ((Long) item[2]).intValue() : 0);
                assigneeItem.setPercent(item[3] != null ? ((Double) item[3]).floatValue() : 0);
                assigneeItem.setCountOfTask(((Long) item[4]).intValue());
                assigneeItem.setEmployeeID(pEmployee.getEmployeeDepartment().getEmployee().getObjectID());


                if (isSubWS) {
                    assigneeItem.setPercent(assigneeItem.getPercent() / (assigneeItem.getCountOfTask() != 0 ? assigneeItem.getCountOfTask() : 1));
                }

                if (itemMap.get(assigneeItem.getEmployeeID()) != null) {
                    WorkstreamAssigneeItem _exisItem = itemMap.get(assigneeItem.getEmployeeID());
                    _exisItem.setTime(_exisItem.getTime() + assigneeItem.getTime());
                    _exisItem.setActualSpentTime(_exisItem.getActualSpentTime() + assigneeItem.getActualSpentTime());
                    _exisItem.setPercent(_exisItem.getPercent() + assigneeItem.getPercent());
                    _exisItem.setDoubleEmployeeCount(_exisItem.getDoubleEmployeeCount() + 1);

                } else {
                    itemMap.put(assigneeItem.getEmployeeID(), assigneeItem);
                }
            }

            for (WorkstreamAssigneeItem item : itemMap.values()) {
                item.setPercent(item.getPercent() / item.getDoubleEmployeeCount());
            }

            assigneeItems.addAll(itemMap.values());
        }

        return assigneeItems;
    }

    private void syncWSEmployeeToSWSEmployee(List<WorkstreamAssigneeItem> wsEmployees, List<WorkstreamAssigneeItem> swsEmployees) {
        if (wsEmployees == null) {
            wsEmployees = new ArrayList<>();
        }

        if (swsEmployees != null && !swsEmployees.isEmpty()) {
            for (WorkstreamAssigneeItem swEmployee : swsEmployees) {

                boolean hasItem = false;
                for (WorkstreamAssigneeItem wEmployee : wsEmployees) {

                    if (wEmployee.getId().equals(swEmployee.getId())) {
                        wEmployee.setTime(wEmployee.getTime() + swEmployee.getTime());
                        wEmployee.setActualSpentTime(wEmployee.getActualSpentTime() + swEmployee.getActualSpentTime());

                        wEmployee.setPercent(wEmployee.getPercent() + swEmployee.getPercent());
                        wEmployee.setCountOfTask(wEmployee.getCountOfTask() + 1);

                        hasItem = true;
                    }
                }

                if (!hasItem) {
                    wsEmployees.add(swEmployee);
                }
            }
        }
    }

    public void calculateWorkStreamBudgets(Integer objectID) {
        //objectID can not be null
        if (objectID == null) {
            return;
        }

        Date startPCCTime = new Date();
        System.out.println("=== Start WorkStream Cost Calculation ===");

        EdsCompany company = taskManager.getUser().getCompany();

        int listLimit = 100; //list counter
        int listIndex = 1;
        List<EdsTask> workStreamTaskList;
        do {
            int listStart = (listIndex - 1) * listLimit;
            workStreamTaskList = taskManager.getWorkStreamTasksByInterval(objectID, listStart, listLimit);

            if (workStreamTaskList != null && !workStreamTaskList.isEmpty()) {
                for (EdsTask task : workStreamTaskList) {
                    //parent => Work Stream of task
                    EdsWorkStream parent = task.getParentWS();

                    if (task.isChangedCalculationFields()) {
                        double oldPlanedWageAmount = task.getPlannedWageAmount();
                        double oldPlanedClientChargeAmount = task.getPlannedClientChargeAmount();
                        double oldActualWageAmount = task.getActualWageAmount();
                        double oldActualClientChargeAmount = task.getActualClientChargeAmount();

                        double oldWageAmount = task.getActualWageAmount() + task.getRemainingWageAmount();
                        double oldClientChargeAmount = task.getActualClientChargeAmount() + task.getRemainingClientChargeAmount();

                        //calculate task budgets by changed calculation fields
                        calculateTaskBudgets(task);

                        //before clear old planed/actual amount value from task parent(WorkStream)
                        if (parent != null && !task.isCalculated()) {
                            parent.updateEstimatedTime(task.getEstimatedTime());
                            parent.updateActualTime(task.getTimespent());

                            parent.updatePlannedWageAmmount(task.getPlannedWageAmount());
                            parent.updatePlannedClientChargeAmmount(task.getPlannedClientChargeAmount());
                            parent.updateActualWageAmmount(task.getActualWageAmount());
                            parent.updateActualClientChargeAmmount(task.getActualClientChargeAmount());

                            parent.updateWageAmmount(task.getActualWageAmount() + task.getRemainingWageAmount());
                            parent.updateClientChargeAmmount(task.getActualClientChargeAmount() + task.getRemainingClientChargeAmount());

                        } else if (parent != null && task.isCalculated()) {
                            EstimateTimeSpentItem estimateTimeSpent = employeeTaskManager.getEstimatedTimeSpent(task.getObjectID());
                            parent.updateEstimatedTime(estimateTimeSpent.getEstimatedTime());
                            parent.updateActualTime(estimateTimeSpent.getTimeSpent());

                            parent.updatePlannedWageAmmount(task.getPlannedWageAmount() - oldPlanedWageAmount);
                            parent.updatePlannedClientChargeAmmount(task.getPlannedClientChargeAmount() - oldPlanedClientChargeAmount);
                            parent.updateActualWageAmmount(task.getActualWageAmount() - oldActualWageAmount);
                            parent.updateActualClientChargeAmmount(task.getActualClientChargeAmount() - oldActualClientChargeAmount);

                            parent.updateWageAmmount(task.getActualWageAmount() + task.getRemainingWageAmount() - oldWageAmount);
                            parent.updateClientChargeAmmount(task.getActualClientChargeAmount() + task.getRemainingClientChargeAmount() - oldClientChargeAmount);
                        }

                        employeeTaskManager.deleteEmployeeTaskHistory(task.getObjectID());

                        //this task is calculated
                        task.setCalculated(true);

                        //update task by calculation changes
                        taskManager.update(task);

                        try {
                            taskSolrComponent.index(task);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        taskManager.flush();
                    }
                }
            }

            listIndex++;
        } while (workStreamTaskList != null && !workStreamTaskList.isEmpty());

        Date endPCCTime = new Date();
        System.out.println("=== End WorksTream Cost Calculation ===");
        System.out.println("=== Time Spend for WCC : " + (endPCCTime.getTime() - startPCCTime.getTime()) + " sec");

    }

    public Boolean projectStartedAlready(Integer projectID) {
        EdsProject project = projectManager.get(projectID);
        return !project.getStatus().getCode().equalsIgnoreCase(EdsProject.NOT_STARTED);
    }

    @Override
    public void removeDeletedEmployeeRbacks(Integer employeeID) {
        taskRbacManager.removeTaskEntriesForDeletedEmployee(employeeID);
        EdsUser user = userManager.getUser();
        Integer companyID = user.getCompany().getObjectID();

        try {
            solrManager.removeEmployeeAllRbacRecord(companyID, employeeID);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    //Add New Task Assignees
    public void addAssigneesToTask(ArrayList<Integer> taskIDs, ArrayList<IdTime> assignees) {
        EdsUser user = userManager.getUser();

        boolean isEnableResourceUtilization = false;
        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);
        if (resourcePlanning != null) {
            isEnableResourceUtilization = true;
        }

        ArrayList<EdsTask> taskList = new ArrayList<>();
        if (taskIDs != null && assignees != null && !assignees.isEmpty()) {
            for (Integer taskID : taskIDs) {
                long begin = System.currentTimeMillis();
                EdsTask task = taskManager.get(taskID);
                Integer taskEstimatedTime = 0;
                ArrayList<IdTime> newProjectAssigneesToTask = new ArrayList<>();
                for (IdTime employeeIdTime : assignees) {
                    EdsEmployee employee = employeeManager.get(employeeIdTime.getId());
                    EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeTask(taskID, employee.getObjectID(), true);
                    if (employeeTask == null) {
                        employeeTask = new EdsEmployeeTask();
                        EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(employee, task.getProject());
                        if (projectEmployee == null) {
                            projectEmployee = addMembers(task.getProject(), employee);
                        }
                        employeeTask.setProjectEmployee(projectEmployee);
                        employeeTask.setTask(task);
                        // set new project employee idTime
                        IdTime idTime = new IdTime();
                        idTime.setId(projectEmployee.getObjectID());
                        idTime.setTime(employeeIdTime.getTime());
                        idTime.setChangeEstimateTime(employeeIdTime.getChangeEstimateTime());
                        idTime.setStartResourceCalculationForNewAssigneesFromToday(employeeIdTime.getStartResourceCalculationForNewAssigneesFromToday());
                        newProjectAssigneesToTask.add(idTime);

                        UpdateEmployeeTaskAttributes(idTime, employeeTask, true);
                        employeeTask.setDeleted(false);
                        employeeTaskManager.create(employeeTask);
                        task.getAssignments().add(employeeTask);
                        baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, employeeTask, user);
                    } else {
                        employeeTask.setDeleted(false);
                    }
                    if (employeeIdTime.getTime() != null) {
                        taskEstimatedTime += employeeIdTime.getTime();
                    }
                }
                task.setEstimatedTime(taskEstimatedTime);
                long begin1 = System.currentTimeMillis();
                if (isEnableResourceUtilization) {
                    updateTaskDailyLoad(task, newProjectAssigneesToTask.toArray(new IdTime[]{}));
                }
                System.out.println(">>>Update Daily Load - " + (System.currentTimeMillis() - begin1));
                taskRbacManager.addRbacEntries(task);
                baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, task, user);
                System.out.println("Time spent - " + (System.currentTimeMillis() - begin));
            }
        }
    }

    @Override
    public SelectItem[] getTaskLookUpItems(ListingFilterParameter filterParameter) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_TASK_CORE);
        QueryResponse resp = null;
        SolrQuery query = new SolrQuery();
        EdsUser edsUser = userManager.getUser();
        query.setQuery(getTaskFacetQuery(filterParameter, edsUser));
        query.setStart(0);
        query.setParam(CommonParams.ROWS, "10");
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            log.error("Error getting task lookup items", e);
        }
        SolrDocumentList solrDocumentList = resp != null ? resp.getResults() : new SolrDocumentList();
        List<SelectItem> items = com.google.common.collect.Lists.newArrayList();
        for (SolrDocument relevantDoc : solrDocumentList) {
            Integer taskID = Integer.valueOf(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_ID));
            String number = (String) SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_NUMBER);
            String name = (String) SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_NAME);
            items.add(new SelectItem(taskID, (number != null && !"".equals(number.trim()) ? number + SolrProjectListRepresenter.ARROW : "") + name, number));
        }

        return items.toArray(new SelectItem[]{});
    }

    public Date getLastModifiedTask() {
        EdsUser user = userManager.getUser();
        if (user != null) {
            return taskManager.getLastModifiedTaskDateByEmployee(user.getObjectID());
        }
        return null;
    }

    @Override
    public LogHistoryItem[] getAllStatusHistories(Integer id) {
        List<EdsTaskStatusHistory> histories = statusHistoryManager.getTaskStatusHistories(id);
        EdsUser user = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        List<LogHistoryItem> statusHistoryItems = new ArrayList<>();
        for (EdsTaskStatusHistory history : histories) {
            LogHistoryItem item = new LogHistoryItem();
            item.setModifiedDate(history.getModifiedDate());
            item.setComment(history.getComment() != null ? history.getComment() : "");
            if (history.getStatus() != null && history.getStatus().getLocale() != null) {
                item.setStatus(getTaskStatus(userSettings.getInternationalization(), history.getStatus().getLocale()));
            } else {
                item.setStatus(history.getStatus() != null ? history.getStatus().getLocalizedName() : "N/A");
            }
            item.setModifier(history.getModifier() != null ? history.getModifier().getFullName() : "N/A");
            statusHistoryItems.add(item);
        }
        return statusHistoryItems.toArray(new LogHistoryItem[]{});
    }

    @Override
    public LogHistoryItem[] getAllLogHistories(Integer id) {
        return null;
    }

    private String getTaskStatus(String userLocale, EdsReferenceLocale locale) {
        String status = switch (userLocale) {
            case "en" -> locale.getEnglish();
            case "ar" -> locale.getArabic();
            case "ru" -> locale.getRussian();
            case "uz" -> locale.getUzbek();
            default -> null;
        };
        return status;
    }
}
