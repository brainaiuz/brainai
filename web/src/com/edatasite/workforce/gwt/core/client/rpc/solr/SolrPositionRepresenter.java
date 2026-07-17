package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SolrPositionRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPOSITE = "composite";

    public static final String FIELD_POSITION_ID = "positionId";
    public static final String FIELD_NUMBER = "number";
    public static final String FIELD_NAME = "name";

    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME = "statusName";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_STATUS_ID_NAME = "statusIdName";
    public static final String FIELD_STATUS = "status_";
    public static final String FIELD_STATUS_EN = "statusEn";
    public static final String FIELD_STATUS_RU = "statusRu";
    public static final String FIELD_STATUS_UZ = "statusUz";
    public static final String FIELD_STATUS_AR = "statusAr";

    public static final String FIELD_EMPLOYEE_COUNT = "employeeCount";
    public static final String FIELD_LOCATION_ID = "locationId";
    public static final String FIELD_LOCATION_NAME = "locationName";
    public static final String FIELD_LOCATION_ID_NAME = "locationIdName";

    public static final String FIELD_DEPARTMENT_ID = "departmentId";
    public static final String FIELD_DEPARTMENT_NAME = "departmentName";
    public static final String FIELD_DEPARTMENT_ID_NAME = "departmentIdName";

    public static final String FIELD_DEPARTMENT_NAME_UZ = "departmentNameUz";
    public static final String FIELD_DEPARTMENT_NAME_RU = "departmentNameRu";
    public static final String FIELD_DEPARTMENT_NAME_AR = "departmentNameAr";
    public static final String FIELD_DEPARTMENT_NAME_EN = "departmentNameEn";

    public static final String DEPARTMENT_NAME_BASE = "departmentName_";




    public static final String FIELD_VACANT_COUNT = "vacantCount";
    public static final String FIELD_CREATED_BY_ID = "createdById";
    public static final String FIELD_CREATED_BY_NAME = "createdByName";
    public static final String FIELD_CREATED_BY_ID_NAME = "createdByIdName";
    public static final String FIELD_CREATED_DATE = "createdDate";

    public static final String FIELD_MODIFIED_BY_ID = "modifiedById";
    public static final String FIELD_MODIFIED_BY_NAME = "modifiedByName";
    public static final String FIELD_MODIFIED_BY_ID_NAME = "modifiedByIdName";
    public static final String FIELD_MODIFIED_DATE = "modifiedDate";

    public static final String TYPE_NAME_BASE = "typeName_";


    public static final String FIELD_TYPE_ID = "typeId";
    public static final String FIELD_TYPE_NAME = "typeName";
    public static final String FIELD_TYPE_NAME_UZ = "typeNameUz";
    public static final String FIELD_TYPE_NAME_RU = "typeNameRu";
    public static final String FIELD_TYPE_NAME_AR = "typeNameAr";
    public static final String FIELD_TYPE_NAME_EN = "typeNameEn";
    public static final String FIELD_TYPE_CODE = "typeCode";
    public static final String FIELD_TYPE_ID_NAME = "typeIdName";

    public static final String FIELD_NAME_EN = "nameEn";
    public static final String FIELD_NAME_RU = "nameRu";
    public static final String FIELD_NAME_AR = "nameAr";
    public static final String FIELD_NAME_UZ = "nameUz";
    public static final String NAME_BASE = "name_";

    public static final String SORTABLE_NUMBER = "sortableNumber";
    public static final String SORTABLE_NAME = "sortableName";
    public static final String SORTABLE_STATUS_NAME = "sortableStatusName";
    public static final String SORTABLE_EMPLOYEE_COUNT = "sortableEmployeeCount";
    public static final String SORTABLE_LOCATION_NAME = "sortableLocationName";
    public static final String SORTABLE_DEPARTMENT_NAME = "sortableDepartmentName";
    public static final String SORTABLE_VACANT_COUNT = "sortableVacantCount";
    public static final String SORTABLE_CREATED_DATE = "sortableCreatedDate";
    public static final String SORTABLE_CREATED_BY_NAME = "sortableCreatedByName";
    public static final String SORTABLE_MODIFIED_BY_NAME = "sortableModifiedByName";
    public static final String SORTABLE_MODIFIED_DATE = "sortableModifiedDate";
    public static final String SORTABLE_TYPE_NAME = "sortableTypeName";
}
