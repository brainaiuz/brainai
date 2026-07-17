package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Shohruh on 27 Oct 2016.
 */
public class SolrAdditionalPaymentPresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";
    public static final String FIELD_ADDITIONAL_PAYMENT_ID = "additionalPaymentId";
    public static final String FIELD_REFERENCE = "reference";

    public static final String FIELD_CREATOR_ID = "creatorId";
    public static final String FIELD_CREATOR_NAME = "creatorName";
    public static final String FIELD_CREATOR_ID_NAME = "creatorIdName";
    public static final String FIELD_APPROVER_ID = "approverId";
    public static final String FIELD_APPROVER_NAME = "approverName";
    public static final String FIELD_APPROVER_ID_NAME = "approverIdName";
    public static final String FIELD_UPDATER_ID = "updaterId";
    public static final String FIELD_UPDATER_NAME = "updaterName";
    public static final String FIELD_UPDATER_ID_NAME = "updaterIdName";

    public static final String FIELD_PAYROLL_BATCH_ID = "payrollGroupId";
    public static final String FIELD_PAYROLL_BATCH_NAME = "payrollGroupName";

    public static final String FIELD_PAYROLL_DEPARTMENT_ID = "payrollDepartmentId";
    public static final String FIELD_PAYROLL_DEPARTMENT_NAME = "payrollDepartmentName";

    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_APPROVED_DATE = "approvedDate";
    public static final String FIELD_TOTAL_AMOUNT = "totalAmount";

    public static final String FIELD_TYPE = "type";
    public static final String FIELD_ENTITY_TYPE = "entityType";
    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME = "statusName";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_STATUS_ID_NAME = "statusIdName";

    public static final String FIELD_MONTH_ID = "monthId";
    public static final String FIELD_MONTH_NAME = "monthName";
    public static final String FIELD_MONTH_ID_NAME = "monthIdName";

    public static final String FIELD_YEAR = "year";
    public static final String FIELD_YEAR_ID_NAME = "yearIdName";

    public static final String FIELD_DELETED = "deleted";

    public static final String FIELD_LAST_UPDATE = "lastUpdate";
    public static final String FIELD_PDF_TEMPLATE_ID = "pdfTemplateId";

    public static final String SORTABLE_REFERENCE = "sortableReference";
    public static final String SORTABLE_CREATOR_NAME = "sortableCreatorName";
    public static final String SORTABLE_APPROVER_NAME = "sortableApproverName";

    public static final String FIELD_PAYMENT_CATEGORY = "paymentCategory";
    public static final String FIELD_PAYMENT_TYPE = "paymentType";

    public static final String FIELD_CATEGORY_LOOKUP_ID = "categoryLookupId";
    public static final String FIELD_CATEGORY_LOOKUP_NAME = "categoryLookupName";
    public static final String FIELD_CATEGORY_LOOKUP_ID_NAME = "categoryLookupIdName";
}
