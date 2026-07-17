package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User : Akhror on 29/10/2021
 */
public class SolrCertificateRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_CERTIFICATE_ID = "certificateId";
    public static final String FIELD_NUMBER = "number";

    public static final String FIELD_EMPLOYEE_ID = "employeeId";
    public static final String FIELD_EMPLOYEE_NAME = "employeeName";
    public static final String FIELD_EMPLOYEE_CODE = "employeeCode";
    public static final String FIELD_EMPLOYEE_ID_NAME = "employeeIdName";

    public static final String FIELD_TYPE_ID = "typeId";
    public static final String FIELD_TYPE_NAME = "typeName";
    public static final String FIELD_TYPE_ID_NAME = "typeIdName";

    public static final String FIELD_CURRENT_APPROVER_ID = "currentApproverId";
    public static final String FIELD_CURRENT_APPROVER_NAME = "currentApproverName";
    public static final String FIELD_CURRENT_APPROVER_ID_NAME = "currentApproverIdName";

    public static final String FIELD_ISSUED_DATE = "issuedDate";
    public static final String FIELD_ISSUED_BY_ID = "issuedById";
    public static final String FIELD_ISSUED_BY_NAME = "issuedByName";
    public static final String FIELD_ISSUED_BY_ID_NAME = "issuedByIdName";

    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_CREATED_BY_ID = "createdById";
    public static final String FIELD_CREATED_BY_NAME = "createdName";
    public static final String FIELD_CREATED_BY_ID_NAME = "createdByIdName";

    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME = "statusName";
    public static final String FIELD_STATUS_ID_NAME = "statusIdName";

    public static final String DYNAMIC_FIELD_CF_STRING = "stringValue";
    public static final String DYNAMIC_FIELD_CF_DOUBLE = "doubleValue";
    public static final String DYNAMIC_FIELD_CF_DATE = "dateValue";

    //For sortable columns
    public static final String SORTABLE_NUMBER = "sortableNumber";
    public static final String SORTABLE_EMPLOYEE_NAME = "sortableEmployeeName";
    public static final String SORTABLE_TYPE_NAME = "sortableTypeName";
    public static final String SORTABLE_CURRENT_APPROVER_NAME = "sortableCurrentApproverName";
    public static final String SORTABLE_ISSUED_DATE = "sortableIssuedDate";
    public static final String SORTABLE_ISSUED_BY_NAME = "sortableIssuedByName";
    public static final String SORTABLE_CREATED_DATE = "sortableCreatedDate";
    public static final String SORTABLE_CREATED_BY_NAME = "sortableIssuedByName";
    public static final String SORTABLE_STATUS_NAME = "sortableStatusName";
    public static final String SORTABLE_EMPLOYEE_CODE = "sortableEmployeeCode";
}
