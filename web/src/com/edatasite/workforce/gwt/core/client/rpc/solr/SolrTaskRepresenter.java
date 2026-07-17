package com.edatasite.workforce.gwt.core.client.rpc.solr;

/**
 * User: Abdulaziz
 * Date: Nov 3, 2009
 * Time: 7:41:32 PM
 */
public class SolrTaskRepresenter {
    public static final String SPLIT = "@";
    public static final String FIELD_TASK_ID = "taskId";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_ASSIGNEE_NAMES = "assigneeNames";
    public static final String FIELD_ASSIGNEE_ID = "assigneeId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_GROUP_ID = "groupId";
    public static final String FIELD_TRUSTEE_TYPE = "trusteeType";
    public static final String FIELD_USER_ID_NAME = "userIdName";
    //TASK PREDECESSOR STATUS
    public static final String FIELD_PREDECESSOR_TASK_STATUS = "predecessorTaskStatus";

    //TASK OVERALL STATUS SHOWN ONLY TO ADMIN GROUP
    public static final String FIELD_TASK_STATUS_ID = "taskStatusId";
    public static final String FIELD_TASK_STATUS = "taskStatus";
    public static final String FIELD_TASK_STATUS_CODE = "taskStatusCode";
    public static final String FIELD_TASK_STATUS_ID_CODE = "taskStatusIdCode";
    public static final String FIELD_TASK_STATUS_ID_CODE_NAME = "taskStatusIdCodeName";
    public static final String FIELD_TASK_STATUS_SORDER = "taskStatusSorder";
    //ASSIGNEE STATUS
    public static final String FIELD_TASK_ASSIGNEE_STATUS = "taskAssigneeStatus";
    public static final String FIELD_TASK_ASSIGNEE_STATUS_ID = "taskAssigneeStatusId";
    public static final String FIELD_TASK_ASSIGNEE_STATUS_CODE = "taskAssigneeStatusCode";
    public static final String FIELD_TASK_ASSIGNEE_STATUS_ID_CODE = "taskAssigneeStatusIdCode";
    public static final String FIELD_TASK_ASSIGNEE_STATUS_ID_CODE_NAME = "taskAssigneeStatusIdCodeName";
    public static final String FIELD_TASK_PROJECT_MANAGER_NAME = "taskProjectManagerName";
    public static final String FIELD_TASK_PROJECT_MANAGER_ID = "taskProjectManagerId";
    public static final String FIELD_TASK_PROJECT_MANAGER_ID_NAME = "taskProjectManagerIdName";
    public static final String FIELD_TASK_PRIORITY = "taskPriority";
    public static final String FIELD_TASK_PRIORITY_ID = "taskPriorityId";
    public static final String FIELD_TASK_PRIORITY_CODE = "taskPriorityCode";
    public static final String FIELD_TASK_PRIORITY_ID_CODE = "taskPriorityIdCode";
    public static final String FIELD_TASK_PRIORITY_ID_CODE_NAME = "taskPriorityIdCodeName";
    public static final String FIELD_TASK_PRIORITY_SORDER = "taskPrioritySorder";
    public static final String FIELD_TASK_TYPE = "taskType";
    public static final String FIELD_TASK_TYPE_ID = "taskTypeId";
    public static final String FIELD_TASK_TYPE_CODE = "taskTypeCode";
    public static final String FIELD_TASK_TYPE_ID_CODE_NAME = "taskTypeIdCodeName";
    public static final String FILED_TASK_PERCENT_COMPLETED = "taskPercentCompleted";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_START_DATE = "startDate";// estimeted start date
    public static final String FIELD_DUE_DATE = "dueDate";// estimeted end date
    public static final String FIELD_ACTUAL_START_DATE = "actualStartDate";// actual start date
    public static final String FIELD_END_DATE = "endDate";// actual end date
    public static final String FIELD_ESTIMATED_TIME = "estimatedTime";
    public static final String FIELD_TASK_AMOUNT = "taskAmount";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_RANK = "rank";
    public static final String FIELD_PERMISSIONS = "permissions";
    public static final String FIELD_RELATIONSHIPS = "relationships";
    public static final String FIELD_TASK_NAME = "taskName";
    public static final String FIELD_TASK_DESCRIPTION = "taskDescription";
    public static final String FIELD_TASK_WORKSTREAM_NAME = "taskWorkstreamName";
    public static final String FIELD_TASK_WORKSTREAM_ID_NAME = "taskWorkstreamIdName";
    public static final String FIELD_TASK_WORKSTREAM_ID = "taskWorkstreamId";
    public static final String FIELD_TASK_PROJECT_NAME = "taskProjectName";
    public static final String FIELD_TASK_PROJECT_NUMBER = "taskProjectNumber";
    public static final String FIELD_TASK_PROJECT_ID_NAME = "taskProjectIdName";
    public static final String FIELD_TASK_PROJECT_ID = "taskProjectId";
    public static final String FIELD_TASK_PROJECT_CLIENT_NAME = "taskProjectClientName";
    public static final String FIELD_TASK_PROJECT_CLIENT_ID_NAME = "taskProjectClientIdName";
    public static final String FIELD_TASK_PROJECT_CLIENT_ID = "taskProjectClientId";
    public static final String FIELD_TASK_PROJECT_MULTI_CLIENT_NAME = "taskProjectMultiClientName";
    public static final String FIELD_TASK_PROJECT_MULTI_CLIENT_ID_NAME = "taskProjectMultiClientIdName";
    public static final String FIELD_TASK_PROJECT_MULTI_CLIENT_ID = "taskProjectMultiClientId";
    public static final String FIELD_TASK_USER_DEPARTMENT_NAME = "taskUserDepartmentName";
    public static final String FIELD_TASK_USER_DEPARTMENT_ID = "taskUserDepartmentId";
    public static final String FIELD_TASK_USER_DEPARTMENT_ID_NAME = "taskUserDepartmentIdName";
    public static final String FIELD_VIEWERS = "viewers";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_TASK_NUMBER = "taskNumber";
    public static final String FIELD_TASK_LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String FIELD_TASK_CREATOR_ID = "taskCreatorId";
    public static final String FIELD_TASK_CREATOR = "taskCreator";
    public static final String KANBAN_ORDER = "kanbanOrder";

    public static final String DYNAMIC_FIELD_CF_STRING = "string_value";
    public static final String DYNAMIC_FIELD_CF_DOUBLE = "double_value";
    public static final String DYNAMIC_FIELD_CF_DATE = "date_value";
    public static final String FIELD_DYN_STRING_COMPOSITE = "dynStringComposite";

    /* Solr Sortable Fields */
    public static final String FIELD_SORTABLE_TASK_NUMBER = "sortableTaskNumber";
    public static final String FIELD_SORTABLE_TASK_PROJECT_NUMBER = "sortableTaskProjectNumber";
    public static final String FIELD_SORTABLE_TASK_NAME = "sortableTaskName";
    public static final String FIELD_SORTABLE_TASK_DESCRIPTION = "sortableTaskDescription";
    public static final String FIELD_SORTABLE_TASK_PROJECT_NAME = "sortableTaskProject";
    public static final String FIELD_SORTABLE_TASK_PROJECT_MANAGER_NAME = "sortableTaskProjectManagerName";
    public static final String FIELD_SORTABLE_TASK_PROJECT_CLIENT_NAME = "sortableTaskClient";
    public static final String FIELD_SORTABLE_TASK_WORKSTREAM_NAME = "sortableTaskWorkstream";

    //related to fileds
    public static final String DYNAMIC_FIELD_RELATED_ID = "relatedId_";
    public static final String DYNAMIC_FIELD_RELATED_NAME = "relatedName_";
    public static final String DYNAMIC_FIELD_RELATED_ID_NAME = "relatedIdName_";
}
