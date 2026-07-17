package com.edatasite.workforce.gwt.core.client.rpc.solr;


import com.google.gwt.user.client.rpc.IsSerializable;

public class SolrDepartmentRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPOSITE = "composite";

    public static final String FIELD_DEPARTMENT_ID = "departmentId";
    public static final String FIELD_NUMBER = "number";
    public static final String FIELD_NAME = "name";

    public static final String FIELD_DEPARTMENT_PARENT_ID = "parentDepartmentId";
    public static final String FIELD_DEPARTMENT_PARENT_NAME = "parentDepartmentName";
    public static final String FIELD_DEPARTMENT_PARENT_ID_NAME = "parentDepartmentIdName";

    public static final String FIELD_DEPARTMENT_PARENT_NAME_EN = "parentDepartmentNameEn";
    public static final String FIELD_DEPARTMENT_PARENT_NAME_RU = "parentDepartmentNameRu";
    public static final String FIELD_DEPARTMENT_PARENT_NAME_AR = "parentDepartmentNameAr";
    public static final String FIELD_DEPARTMENT_PARENT_NAME_UZ = "parentDepartmentNameUz";

    public static final String DEPARTMENT_PARENT_NAME_BASE = "DEPARTMENT_PARENT_NAME_";




    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME = "statusName";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_STATUS_ID_NAME = "statusIdName";

    public static final String FIELD_EMPLOYEE_COUNT = "employeeCount";
    public static final String FIELD_LOCATION_ID = "locationId";
    public static final String FIELD_LOCATION_NAME = "locationName";
    public static final String FIELD_LOCATION_ID_NAME = "locationIdName";
    public static final String FIELD_LEADER_ID="leaderId";
    public static final String FIELD_LEADER_NAME="leaderName";
    public static final String FIELD_LEADER_ID_NAME="leaderIdName";
    public static final String FIELD_LEADER_IS_VACANT="leaderIsVacant";


    public static final String FIELD_HEAD_COUNT = "headCount";
    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_ENCRYPTED_ID = "encryptedId";


    public static final String FIELD_CREATED_BY_ID = "createdById";
    public static final String FIELD_CREATED_BY_NAME = "createdByName";
    public static final String FIELD_CREATED_BY_ID_NAME = "createdByIdName";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_MODIFIED_BY_ID = "modifiedById";
    public static final String FIELD_MODIFIED_BY_NAME = "modifiedByName";
    public static final String FIELD_MODIFIED_BY_ID_NAME = "modifiedByIdName";
    public static final String FIELD_MODIFIED_DATE = "modifiedDate";


    public static final String FIELD_TYPE_ID = "TYPE_ID";
    public static final String FIELD_TYPE_NAME = "TYPE_NAME";
    public static final String FIELD_TYPE_NAME_UZ = "TYPE_NAME_UZ";
    public static final String FIELD_TYPE_NAME_RU = "TYPE_NAME_RU";
    public static final String FIELD_TYPE_NAME_AR = "TYPE_NAME_AR";
    public static final String FIELD_TYPE_NAME_EN = "TYPE_NAME_EN";
    public static final String FIELD_TYPE_CODE = "TYPE_CODE";
    public static final String FIELD_TYPE_ID_NAME = "TYPE_ID_NAME";

    public static final String FIELD_NAME_EN = "nameEn";
    public static final String FIELD_NAME_RU = "nameRu";
    public static final String FIELD_NAME_AR = "nameAr";
    public static final String FIELD_NAME_UZ = "nameUz";
    public static final String NAME_BASE = "name_";

    public static final String SORTABLE_NUMBER = "sortableNumber";
    public static final String SORTABLE_NAME = "sortableName";
    public static final String SORTABLE_STATUS_NAME = "sortableStatusName";
    public static final String SORTABLE_LOCATION_NAME = "sortableLocationName";
    public static final String SORTABLE_DEPARTMENT_NAME = "sortableDepartmentName";
    public static final String SORTABLE_HEAD_COUNT = "sortableHeadCount";
    public static final String SORTABLE_CREATED_DATE = "sortableCreatedDate";
    public static final String SORTABLE_CREATED_BY_NAME = "sortableCreatedByName";
    public static final String SORTABLE_MODIFIED_BY_NAME = "sortableModifiedByName";
    public static final String SORTABLE_MODIFIED_DATE = "sortableModifiedDate";
    public static final String SORTABLE_TYPE_NAME = "sortableTypeName";
}
