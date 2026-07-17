package com.edatasite.workforce.gwt.core.client.rpc.solr;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 27-Apr-2011
 * Time: 18:28:07
 */
public interface SolrCaseRepresenter {
    String SPLIT = "@";
    String COMPANY_ID = "companyId";
    String COMPOSITE_ID = "oid";
    String COMPOSITE = "composite";
    String COMPOSITE_CASE_SUBJECT = "compositeCaseSubject";
    String CASE_ID = "caseId";
    String CASE_EMAIL = "caseEmail";
    String CASE_EMAIL_ID = "caseEmailId";
    String CASE_TRACKER_ID = "caseTrackerId";
    String CASE_PHONE = "casePhone";
    String CASE_SUBJECT = "caseSubject";
    String CASE_NUMBER = "caseNumber";
    String CASE_ASSIGNEE = "caseAssignee";
    String CASE_ASSIGNEE_ID = "caseAssigneeId";
    String CASE_ASSIGNEE_ID_NAME = "caseAssigneeIdName";
    String CASE_DEPARTMENT = "caseDepartment";
    String CASE_DEPARTMENT_ID = "caseDepartmentId";
    String CASE_DEPARTMENT_ID_NAME = "caseDepartmentIdName";
    String CASE_ORIGIN_ID = "caseOriginId";
    String CASE_ORIGIN_NAME = "caseOriginName";
    String CASE_ORIGIN_CODE = "caseOriginCode";
    String CASE_ORIGIN_ID_NAME = "caseOriginIdName";
    String CASE_ORIGIN_ID_CODE_NAME = "caseOriginIdCodeName";
    String CASE_TYPE_ID = "caseTypeId";
    String CASE_TYPE_NAME = "caseTypeName";
    String CASE_TYPE_CODE = "caseTypeCode";
    String CASE_TYPE_ID_NAME = "caseTypeIdName";
    String CASE_TYPE_ID_CODE_NAME = "caseTypeIdCodeName";
    String CASE_REASON_NAME = "caseReasonName";
    String CASE_REASON_ID = "caseReasonId";
    String CASE_REASON_ID_NAME = "caseReasonIdName";
    String ENTITY_ID = "entityId";
    String OPPORTUNITY_ID = "opportunityId";
    String ACCOUNT_ID = "accountId";
    String LEAD_ID = "leadId";
    String RELEATED_TO_ID = "relatedToId";
    String PRIORITY_ID = "priorityId";
    String PRIORITY_NAME = "priorityName";
    String PRIORITY_CODE = "priorityCode";
    String PRIORITY_COLOR = "priorityColor";
    String PRIORITY_ID_NAME = "priorityIdName";
    String PRIORITY_ID_CODE_NAME = "priorityIdCodeName";
    String PRIORITY_SORDER = "prioritySorder";
    String STATUS_ID = "statusId";
    String STATUS_NAME = "statusName";
    String STATUS_ID_NAME = "statusIdName";
    String STATUS_ID_CODE_NAME = "statusIdCodeName";
    String STATUS_CODE = "statusCode";
    String STATUS_SORDER = "statusSorder";
    String RESOLVER_ID = "resolverId";
    String RESOLVER_NAME = "resolverName";
    String RESOLVER_ID_NAME = "resolverIdName";
    String REPORTED_BY = "reportedBy";
    String IN_TRASH = "inTrash";
    String HAS_ATTACHMENT = "hasAttachment";
    String CREATE_DATE = "createDate";
    String LAST_UPDATE_DATE = "lastUpdatedDate";
    String LAST_REPORTED_DATE = "lastReportedDate";
    String FIELD_DYN_STRING_COMPOSITE = "dynStringComposite";
    String BILLABLE = "billable";
    String INTERNAL_UPDATED_DATE = "internalUpdatedDate";
    String INTERNAL_STATUS_ID = "internalStatusId";
    String INTERNAL_STATUS_NAME = "internalStatusName";
    String INTERNAL_STATUS_ID_NAME = "internalStatusIdName";
    String INTERNAL_STATUS_SORDER = "internalStatusSorder";
    String KANBAN_ORDER = "kanbanOrder";

    // Solr sortable fields
    String SORTABLE_CASE_SUBJECT = "sortableCaseSubject";
    String SORTABLE_REPORTED_BY = "sortableReportedBy";
    String SORTABLE_CASE_ASSIGNEE = "sortableCaseAssignee";

    //related to fileds
    String DYNAMIC_FIELD_RELATED_ID = "relatedId_*";
    String DYNAMIC_FIELD_RELATED_NAME = "relatedName_*";
    String DYNAMIC_FIELD_RELATED_ID_NAME = "relatedIdName_*";
}