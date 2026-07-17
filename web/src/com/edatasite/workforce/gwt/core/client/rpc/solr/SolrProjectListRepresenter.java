package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 06-Jan-2011
 * Time: 21:09:36
 */
public class SolrProjectListRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String ARROW = " -> ";

    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_LOOKUP_COMPOSITE = "lookupComposite";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_PROJECT_NUMBER = "projectNumber";
    public static final String FIELD_PROJECT_ID = "projectId";
    public static final String FIELD_PROJECT_NAME = "projectName";
    public static final String FIELD_PARENT_ID = "parentId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_USER_NAME = "userName";
    public static final String FIELD_USER_ID_NAME = "userIdName";

    public static final String FIELD_PROJECT_STATUS_ID = "statusId";
    public static final String FIELD_PROJECT_STATUS_NAME = "statusName";
    //    public static final String FIELD_PROJECT_STATUS_ID_NAME = "STATUS_ID_NAME";
    public static final String FIELD_PROJECT_STATUS_CODE = "statusCode";
    public static final String FIELD_PROJECT_STATUS_ID_CODE = "statusIdCode";
    public static final String FIELD_PROJECT_STATUS_ID_CODE_NAME = "statusIdCodeName";
    public static final String FIELD_PROJECT_STATUS_SORDER = "statusSorder";
    public static final String FIELD_PROJECT_COMPLETED = "completed";
    public static final String FIELD_PROJECT_DESCRIPTION = "description";

    public static final String FIELD_PROJECT_MANAGER_ID = "managerId";
    public static final String FIELD_PROJECT_MANAGER_NAME = "managerName";
    public static final String FIELD_PROJECT_MANAGER_ID_NAME = "managerIdName";
    public static final String FIELD_PROJECT_BACKUP_MANAGER_ID = "backupManagerId";
    public static final String FIELD_PROJECT_BACKUP_MANAGER_NAME = "backupManagerName";
    public static final String FIELD_PROJECT_BACKUP_MANAGER_ID_NAME = "backupManagerIdName";

    public static final String FIELD_PROJECT_MULTI_CLIENT_ID = "projectMultiClientId";
    public static final String FIELD_PROJECT_MULTI_CLIENT_NAME = "projectMultiClientName";
    public static final String FIELD_PROJECT_MULTI_CLIENT_ID_NAME = "projectMultiClientIdName";
    public static final String FIELD_PROJECT_CLIENT_ID = "clientId";
    public static final String FIELD_PROJECT_CLIENT_NAME = "clientName";
    public static final String FIELD_PROJECT_CLIENT_ID_NAME = "clientIdName";
    public static final String FIELD_PROJECT_CLIENT_NAME_SORT = "clientNameSort";
    public static final String FIELD_PROJECT_LOCATION_ID = "locationId";
    public static final String FIELD_PROJECT_LOCATION_NAME = "locationName";
    public static final String FIELD_PROJECT_LOCATION_ID_NAME = "locationIdName";
    public static final String FIELD_USER_LOCATION_ID = "userLocationId";
    public static final String FIELD_PROJECT_TASK_OF_COUNT = "taskOfCount";
    public static final String FIELD_PROJECT_HOUR_SPENT = "hourSpent";
    public static final String FIELD_PROJECT_CREATOR_ID = "projectCreatorId";
    public static final String FIELD_PROJECT_CREATOR = "projectCreator";
    public static final String FIELD_PROJECT_INVOICE = "invoice";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdate";

    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_DUE_DATE = "dueDate";
    public static final String FIELD_END_DATE = "endDate";
    public static final String FIELD_BILLIBLE = "billible";

    public static final String DYNAMIC_STRING_VALUE = "stringValue";
    public static final String DYNAMIC_DATE_VALUE = "dateValue";
    public static final String DYNAMIC_NUMBER_VALUE = "doubleValue";

    // Solr Sortable Columns
    public static final String SORTABLE_PROJECT_NUMBER = "sortableNumber";
    public static final String SORTABLE_PROJECT_NAME = "sortableName";
    public static final String SORTABLE_PROJECT_DESCRIPTION = "sortableDescription";
    public static final String SORTABLE_PROJECT_MANAGER = "sortableManager";
    public static final String SORTABLE_PROJECT_BACKUP_MANAGER = "sortableBackupManager";
    public static final String SORTABLE_PROJECT_CLIENT = "sortableClient";
    public static final String SORTABLE_CLIENT_NAME_SORT = "sortableClientNameSort";
    public static final String SORTABLE_PROJECT_INVOICE = "sortableInvoice";
    public static final String SORTABLE_PROJECT_HOUR_SPENT = "sortableHourSpent";

    //related to fileds
    public static final String DYNAMIC_FIELD_RELATED_ID = "relatedId_*";
    public static final String DYNAMIC_FIELD_RELATED_NAME = "relatedName_*";
    public static final String DYNAMIC_FIELD_RELATED_ID_NAME = "relatedIdName_*";

    //calculation fields
    public static final String FIELD_PLANED_WAGE_AMOUNT = "planedWageAmount";
    public static final String FIELD_PLANED_CLIENT_CHARGE_AMOUNT = "planedClientChargeAmount";
    public static final String FIELD_PLANED_EXPENSES_AMOUNT = "planedExpensesAmount";
    public static final String FIELD_PLANED_INCOME_AMOUNT = "planedIncomeAmount";

    public static final String FIELD_ACTUAL_WAGE_AMOUNT = "actualWageAmount";
    public static final String FIELD_ACTUAL_CLIENT_CHARGE_AMOUNT = "actualClientChargeAmount";
    public static final String FIELD_EXPENSES_AMOUNT = "expensesAmount";
    public static final String FIELD_INCOME_AMOUNT = "incomeAmount";
    public static final String FIELD_DYN_STRING_COMPOSITE = "dynStringComposite";
    public static final String FIELD_PROJECT_NAME_NUMBER_COMPOSITE = "projectNameNumberComposite";
    public static final String FIELD_PROJECT_CREATED_DATE = "projectCreatedDate";

    public static final String FIELD_PROJECT_MODIFIED_BY = "projectModifiedBy";
    public static final String FIELD_PROJECT_MODIFIED_DATE = "projectModifiedDate";

}
