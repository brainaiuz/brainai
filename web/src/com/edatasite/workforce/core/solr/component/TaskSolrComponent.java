package com.edatasite.workforce.core.solr.component;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.permission.EdsTaskPermission;
import com.edatasite.workforce.core.solr.document.TaskSolrDoc;
import com.edatasite.workforce.core.solr.facet.SolrFacetFilterComponent;
import com.edatasite.workforce.core.solr.repository.TaskSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.rpc.TaskPermissionItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.GroupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_TASK_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:32.
 */
@Component
public class TaskSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(TaskSolrComponent.class);

    @Autowired
    private TaskSolrDocRepository taskSolrDocRepository;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private SolrFacetFilterComponent solrFacetFilterComponent;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsTask edsTask) throws InterruptedException {
        this.indexes(Arrays.asList(edsTask));
    }

    @Transactional
    public void indexes(List<EdsTask> edsTaskList) throws InterruptedException {
        Integer companyID = SecurityContext.getCompanyID();
        List<TaskSolrDoc> taskSolrDocs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(edsTaskList)) {
            List<Integer> tids = edsTaskList.stream().map(EdsTask::getObjectID).toList();
            Map<Integer, List<EdsTaskRbac>> edsTaskRbacEntriesMap = taskRbacManager.getTaskRbacEntries(tids);
            Map<Integer, List<String>> edsAssigneeUserListMap = taskManager.getTaskAssigneeUserList(tids);
            LinkedHashMap<Integer, List<EdsRelation>> edsRelationListMap = relationManager.getAllRelationsMapByObjectId(EdsRelation.TYPE_TASK, tids);
            for (EdsTask edsTask : edsTaskList) {
                try {
                    List<EdsRelation> edsRelationList =edsRelationListMap.get(edsTask.getObjectID())== null? new ArrayList<>() : edsRelationListMap.get(edsTask.getObjectID());
                    taskSolrDocs.addAll(indexToSolr(edsTask, edsTaskRbacEntriesMap.get(edsTask.getObjectID()), edsAssigneeUserListMap.get(edsTask.getObjectID()), companyID, edsRelationList));
                    log.info("Indexed Task Core CID - {}, objId - {}", companyID, edsTask.getObjectID());
                } catch (Exception e) {
                    log.error("********************* Error on EdsTask with id {}, and error message {} **********************", edsTask.getObjectID(), e.getMessage());
                }
            }
        }

        if (!taskSolrDocs.isEmpty()) {
            try {
                log.info("========= Create Task solr docs for company {} with size {} =========", companyID, taskSolrDocs.size());
                taskSolrDocRepository.saveAll(taskSolrDocs);
            } catch (Exception e) {
                log.error("Error on saving Task list", e);
            }
        }
    }

    public void deleteByTaskId(Integer taskId) {
        taskSolrDocRepository.deleteByTaskId(String.valueOf(taskId));
        log.info("========= Delete Task solr doc by id {} =========", taskId);
    }

    @Transactional
    public void indexConcurrently(List<EdsTask> edsTaskList) throws InterruptedException {
        Integer companyID = SecurityContext.getCompanyID();
        if (!CollectionUtils.isEmpty(edsTaskList)) {
            ConcurrentLinkedQueue<TaskSolrDoc> taskSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();
            List<Integer> tids = edsTaskList.stream().map(EdsTask::getObjectID).toList();
            Map<Integer, List<EdsTaskRbac>> edsTaskRbacEntriesMap = taskRbacManager.getTaskRbacEntries(tids);
            Map<Integer, List<String>> edsAssigneeUserListMap = taskManager.getTaskAssigneeUserList(tids);
            LinkedHashMap<Integer, List<EdsRelation>> edsRelationListMap = relationManager.getAllRelationsMapByObjectId(EdsRelation.TYPE_TASK, tids);

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsTask edsTask : edsTaskList) {
                Callable<Void> task = () -> {
                    try {
                        ServerSecurityContext.getInstance().setDatabase(dataBase);
                        ServerSecurityContext.getInstance().setCompanyId(companId);
                        List<EdsRelation> relations = edsRelationListMap.get(edsTask.getObjectID());
                        if(relations == null){
                            relations = new ArrayList<>();
                        }
                        synchronized (this) {
                            taskSolrDocs.addAll(indexToSolr(edsTask, edsTaskRbacEntriesMap.get(edsTask.getObjectID()), edsAssigneeUserListMap.get(edsTask.getObjectID()), companyID, relations));
                            log.info(" Indexed Task Core CID - {}, objId - {} ", companId, edsTask.getObjectID());
                        }
                    } catch (Exception e) {
                        log.error("Error on EdsTask with id CID - {}, objId - {}, and error message {} ********************** ", companyID, edsTask.getObjectID(), e.getMessage());
                    }
                    return null;
                };
                tasks.add(task);
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
                log.error("Error on loading Task list. Eror message is : {}", e.getMessage());
            }

            if (!taskSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Task solr docs for company {} with size {} =========", companyID, taskSolrDocs.size());
                    taskSolrDocRepository.saveAll(taskSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Task list", e);
                }
            }
        }
    }

    public Collection<TaskSolrDoc> indexToSolr(EdsTask edsTask, List<EdsTaskRbac> taskRbacEntries, List<String> assigneeUserList, Integer companyId, List<EdsRelation> edsRelationList) {
        Map<String, TaskSolrDoc> solrDocMap = new HashMap<>();
        Map<String, Integer> ranks = new HashMap<>();
        List<EdsTaskPermission> permissions = new ArrayList<>();
        List<String> permStrings = new ArrayList<>();

        for (EdsTaskRbac tRbac : taskRbacEntries) {
            permissions.add(tRbac.getTaskPermission());
        }

        if (!permissions.isEmpty()) {
            EdsTaskPermission permission = permissions.get(0);
            TaskPermissionItem tPermission = permission.getAsTaskPermissionItem();
            tPermission = permission.getMergedPermissions(tPermission, permissions);
            permStrings = tPermission.getPemissionAsStringList();
        }

        for (EdsTaskRbac tRbac : taskRbacEntries) {
            String compositID = "";

            if (EdsTrusteeType.USER.equals(tRbac.getTrusteeType())) {
                compositID = companyId + "_" + edsTask.getObjectID() + "_" + tRbac.getUser().getObjectID() + "_" + tRbac.getTrusteeType() + "_" + tRbac.getRelationship();
            } else if (EdsTrusteeType.GROUP.equals(tRbac.getTrusteeType())) {
                compositID = companyId + "_" + edsTask.getObjectID() + "_" + tRbac.getGroup().getObjectID() + "_" + tRbac.getTrusteeType();
            }

            TaskSolrDoc doc = solrDocMap.get(compositID);
            Integer currentRelationRank;
            if (doc == null) {
                currentRelationRank = tRbac.getRelationRank();
                ranks.put(compositID, currentRelationRank);
                doc = new TaskSolrDoc();
                solrDocMap.put(compositID, doc);

                doc.setOid(compositID);
                doc.setCompanyId(companyId);

                doc.setRank(currentRelationRank);

                doc.setTaskId(edsTask.getObjectID());
                doc.setTaskNumber(edsTask.getNumber());
                doc.setTaskName(edsTask.getName());
                doc.setTaskDescription(edsTask.getDescription());

                if (edsTask.getProject() != null) {
                    EdsProject project = edsTask.getProject();
                    doc.setTaskProjectId(project.getObjectID());
                    doc.setTaskProjectName(project.getName());
                    doc.setTaskProjectNumber(project.getNumber());
                    doc.setTaskProjectIdName(SolrUtils.getIdName(project.getObjectID(), project.getName()));

                    if (project.getClients() != null && !project.getClients().isEmpty()) {
                        for (EdsCrmAccount client : project.getClients()) {
                            doc.getTaskProjectMultiClientId().add(client.getObjectID());
                            doc.getTaskProjectMultiClientName().add(client.getName());
                            doc.getTaskProjectMultiClientIdName().add(SolrUtils.getIdName(client.getObjectID(), client.getName()));
                        }
                    }
                    if (edsTask.getProject().getManager() != null) {
                        EdsEmployee manager = edsTask.getProject().getManager();
                        doc.setTaskProjectManagerId(manager.getObjectID());
                        doc.setTaskProjectManagerName(manager.getName());
                        doc.setTaskProjectManagerIdName(SolrUtils.getIdName(manager.getObjectID(), manager.getName()));
                    }
                }

                if (edsTask.getParentWS() != null) {
                    EdsWorkStream workStream = edsTask.getParentWS();
                    doc.setTaskWorkstreamId(workStream.getObjectID());
                    doc.setTaskWorkstreamName(workStream.getName());
                    doc.setTaskWorkstreamIdName(SolrUtils.getIdName(workStream.getObjectID(), workStream.getName()));
                }


                if (tRbac.getClient() != null) {
                    EdsCrmAccount client = tRbac.getClient();
                    doc.setTaskProjectClientName(client.getName());
                    doc.setTaskProjectClientId(client.getObjectID());
                    doc.setTaskProjectClientIdName(SolrUtils.getIdName(client.getObjectID(), client.getName()));
                }

                if (tRbac.getDepartment() != null) {
                    EdsDepartment department = tRbac.getDepartment();
                    doc.setTaskUserDepartmentId(department.getObjectID());
                    doc.setTaskUserDepartmentName(department.getName());
                    doc.setTaskUserDepartmentIdName(SolrUtils.getIdName(department.getObjectID(), department.getName()));
                }

                if (edsTask.getCreator() != null) {
                    EdsUser creator = edsTask.getCreator();
                    doc.setTaskCreatorId(creator.getObjectID());
                    doc.setTaskCreator(creator.getName());
                }

                if (edsTask.getUpdater() != null) {
                    doc.setLastModifiedBy(edsTask.getUpdater().getName());
                }

                if (EdsTrusteeType.USER.equals(tRbac.getTrusteeType()) && !tRbac.getUser().getDeleted()) {
                    if (!EdsRelationship.TASK_NOT_ASSIGNEE.equals(tRbac.getRelationship())) {
                        doc.setUserId(tRbac.getUser().getObjectID());
                        EdsEmployee edsEmployee = tRbac.getUser().getEmployee();
                        String employeeNumber = edsEmployee != null && edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null ? edsEmployee.getProfile().getEmployeeCode() + " " : "";
                        doc.setUserIdName(tRbac.getUser().getObjectID() + SolrTaskRepresenter.SPLIT + employeeNumber + tRbac.getUser().getName());
                    }
                    for (EdsTaskRbac rbac : taskRbacEntries) {
                        if (EdsTrusteeType.USER.equals(rbac.getTrusteeType()) && rbac.getUser() != null) {
                            doc.getViewers().add(SolrUtils.getIdName(rbac.getUser().getObjectID(), SolrTaskRepresenter.FIELD_USER_ID));
                        } else if (EdsTrusteeType.GROUP.equals(rbac.getTrusteeType()) && rbac.getUser() != null) {
                            doc.getViewers().add(SolrUtils.getIdName(rbac.getUser().getObjectID(), SolrTaskRepresenter.FIELD_GROUP_ID));
                        }
                    }
                } else if (EdsTrusteeType.GROUP.equals(tRbac.getTrusteeType())) {
                    doc.setGroupId(tRbac.getGroup().getObjectID());
                    for (EdsTaskRbac rbac : taskRbacEntries) {
                        if (EdsTrusteeType.USER.equals(rbac.getTrusteeType()) && rbac.getUser() != null) {
                            doc.getViewers().add(SolrUtils.getIdName(rbac.getUser().getObjectID(), SolrTaskRepresenter.FIELD_USER_ID));
                        } else if (EdsTrusteeType.GROUP.equals(rbac.getTrusteeType()) && rbac.getUser() != null) {
                            doc.getViewers().add(SolrUtils.getIdName(rbac.getUser().getObjectID(), SolrTaskRepresenter.FIELD_GROUP_ID));
                        }
                    }
                }
                if (assigneeUserList != null && !assigneeUserList.isEmpty()) {
                    doc.getAssigneeNames().addAll(assigneeUserList);
                }
                if ((EdsRelationship.TASK_ASSIGNEE.equals(tRbac.getRelationship()) || EdsRelationship.TASK_NOT_ASSIGNEE.equals(tRbac.getRelationship()))
                        && tRbac.getUser() != null && !tRbac.getUser().getDeleted()) {
                    doc.setAssigneeId(tRbac.getUser().getObjectID());
                    if (tRbac.getStatus() != null) {
                        EdsReference status = tRbac.getStatus();
                        doc.setTaskAssigneeStatusId(status.getObjectID());
                        doc.setTaskAssigneeStatus(status.getName());
                        doc.setTaskAssigneeStatusCode(status.getCode());
                        doc.setTaskAssigneeStatusIdCode(SolrUtils.getIdName(status.getObjectID(), status.getCode()));
                        doc.setTaskAssigneeStatusIdCodeName(SolrUtils.getIdCodeName(status.getObjectID(), status.getCode(), status.getName()));
                    }
                }
                //Overall status. This status is shown to admin group. This status represents task overall status!
                if (edsTask.getStatus() != null) {
                    EdsReference status = edsTask.getStatus();
                    doc.setTaskStatus(status.getName());
                    doc.setTaskStatusId(status.getObjectID());
                    doc.setTaskStatusCode(status.getCode());
                    doc.setTaskStatusIdCode(SolrUtils.getIdName(status.getObjectID(), status.getCode()));
                    doc.setTaskStatusIdCodeName(SolrUtils.getIdCodeName(status.getObjectID(), status.getCode(), status.getName()));
                    doc.setTaskStatusSorder(status.getSorder());
                }
                if (edsTask.getPredecessors() != null && !edsTask.getPredecessors().isEmpty()) {
                    String predTaskStatus = EdsTask.COMPLETED;
                    for (EdsTask predTask : edsTask.getPredecessors()) {
                        String lastStatus = predTask.getTaskLastStatus();

                        if (EdsTask.IN_PROGRESS.equals(lastStatus)) {
                            predTaskStatus = lastStatus;
                            break;
                        }
                        if (!EdsTask.COMPLETED.equals(lastStatus)) {
                            predTaskStatus = lastStatus;
                        }
                    }
                    doc.setPredecessorTaskStatus(predTaskStatus);
                }

                doc.setTrusteeType(tRbac.getTrusteeType());

                doc.setTaskPercentCompleted(edsTask.getPercent());
                doc.setEstimatedTime(tRbac.getEstimatedTime());

                if (edsTask.getTaskAmount() != null) {
                    doc.setTaskAmount(edsTask.getTaskAmount().doubleValue());
                }

                doc.setDueDate(edsTask.getDueDate());
                doc.setStartDate(edsTask.getStartDate());
                doc.setActualStartDate(edsTask.getActualStartDate());
                doc.setEndDate(edsTask.getActualEndDate());
                doc.setCreationDate(edsTask.getCreationTime());
                doc.setLastUpdateDate(edsTask.getLastUpdateTime());
                doc.setKanbanOrder(edsTask.getKanbanOrder());

                if (edsTask.getType() != null) {
                    doc.setTaskTypeId(edsTask.getType().getObjectID());
                    doc.setTaskTypeCode(edsTask.getType().getCode());
                    doc.setTaskTypeIdCodeName(SolrUtils.getIdCodeName(edsTask.getType().getObjectID(), edsTask.getType().getCode(), edsTask.getType().getName()));
                    doc.setTaskType(edsTask.getType().getName());
                }

                if (edsTask.getPriority() != null) {
                    doc.setTaskPriority(edsTask.getPriority().getName());
                    doc.setTaskPriorityId(edsTask.getPriority().getObjectID());
                    doc.setTaskPriorityCode(edsTask.getPriority().getCode());
                    doc.setTaskPriorityIdCode(SolrUtils.getIdName(edsTask.getPriority().getObjectID(), edsTask.getPriority().getCode()));
                    doc.setTaskTypeIdCodeName(SolrUtils.getIdCodeName(edsTask.getPriority().getObjectID(), edsTask.getPriority().getCode(), edsTask.getPriority().getName()));
                    doc.setTaskPrioritySorder(edsTask.getPriority().getSorder());
                }

                CustomFieldsUtils.setSolrDocDynamicFields(doc, edsTask.getTaskCustomFields());
            } else {
                currentRelationRank = ranks.get(compositID);
            }

            if (currentRelationRank < tRbac.getRelationRank()) {  // highest rank is assigned to assignee then to creator then to PM and so on according to relationship
                // we should write exact status of task where he is assignee
                doc.setRank(currentRelationRank);
                doc.setTaskPercentCompleted(tRbac.getPercent() != null ? tRbac.getPercent() : edsTask.getPercent());
                doc.setEstimatedTime(tRbac.getEstimatedTime());
            }
            SolrRelationUtils.addToRelationBaseSolrDoc(doc, edsRelationList, EdsRelation.TYPE_TASK);
            doc.getPermissions().addAll(permStrings);
            doc.getRelationships().add(tRbac.getRelationship());
        }
        return solrDocMap.values();
    }

    @Transactional
    public QueryResponse getList(ListingFilterParameter filterParameter, String solrQuery) {

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setHighlightSnippets(20);
        query.setParam(CommonParams.ROWS, filterParameter.getLimit() > 0 ? String.valueOf(filterParameter.getLimit()) : "50");
        query.setParam(GroupParams.GROUP, true);
        query.setParam(GroupParams.GROUP_LIMIT, "7");
        query.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
        query.setParam(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_TASK_ID);
        query.setSort(SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE, SolrQuery.ORDER.desc);
        if (!filterParameter.isSearchButton()) {
            if (StringUtils.isNotBlank(filterParameter.getSortField())) {
                SolrQuery.ORDER sortDirection = filterParameter.isAscending() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc;
                switch (filterParameter.getSortField()) {
                    case TaskListItem.NUMBER ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_NUMBER, sortDirection);
                    case TaskListItem.NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_NAME, sortDirection);
                    case TaskListItem.DESCRIPTION ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_DESCRIPTION, sortDirection);
                    case TaskListItem.LAST_MODIFIED ->
                            query.setSort(SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE, sortDirection);
                    case TaskListItem.PRIORITY_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_TASK_PRIORITY, sortDirection);
                    case TaskListItem.PROJECT_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_PROJECT_NAME, sortDirection);
                    case TaskListItem.PROJECT_NUMBER ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_PROJECT_NUMBER, sortDirection);
                    case TaskListItem.PROJECT_MANAGER_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_PROJECT_MANAGER_NAME, sortDirection);
                    case TaskListItem.CLIENT ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_PROJECT_CLIENT_NAME, sortDirection);
                    case TaskListItem.DUE_DATE -> query.setSort(SolrTaskRepresenter.FIELD_DUE_DATE, sortDirection);
                    case TaskListItem.COMPLETE ->
                            query.setSort(SolrTaskRepresenter.FILED_TASK_PERCENT_COMPLETED, sortDirection);
                    case TaskListItem.STATUS_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_TASK_ASSIGNEE_STATUS, sortDirection);
                    case TaskListItem.START_DATE -> query.setSort(SolrTaskRepresenter.FIELD_START_DATE, sortDirection);
                    case TaskListItem.END_DATE -> query.setSort(SolrTaskRepresenter.FIELD_END_DATE, sortDirection);
                    case TaskListItem.ESTIMATED ->
                            query.setSort(SolrTaskRepresenter.FIELD_ESTIMATED_TIME, sortDirection);
                    case TaskListItem.PARENT_WORKSTREAM_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_SORTABLE_TASK_WORKSTREAM_NAME, sortDirection);
                    case TaskListItem.OVERALL_STATUS_NAME ->
                            query.setSort(SolrTaskRepresenter.FIELD_TASK_STATUS_SORDER, sortDirection);
                    case TaskListItem.ID -> query.setSort(SolrTaskRepresenter.FIELD_TASK_ID, sortDirection);
                    case TaskListItem.KANBAN_ORDER -> query.setSort(SolrTaskRepresenter.KANBAN_ORDER, sortDirection);
                    case TaskListItem.ACTUAL_START_DATE ->
                            query.setSort(SolrTaskRepresenter.FIELD_ACTUAL_START_DATE, sortDirection);
                    default ->
                            CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(filterParameter.getSortField(), !filterParameter.isAscending(), true);
                }
                ;
            }
        }
        return getTaskListResponse(query);
    }

    private QueryResponse getTaskListResponse(SolrQuery query) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_TASK_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);

        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        return resp;
    }

}
