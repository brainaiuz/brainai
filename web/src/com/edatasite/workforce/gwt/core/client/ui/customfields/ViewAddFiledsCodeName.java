package com.edatasite.workforce.gwt.core.client.ui.customfields;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 19-Nov-2010
 * Time: 16:29:52
 * <p/>
 * Each code name order equals default add view order
 */
public enum ViewAddFiledsCodeName {
    TaskAdd(ViewName.Task, "PROJECT", "TASK_NUMBER", "TASK_NAME", "DESCRIPTION", "STARTDATE", "DUEDATE", "ASSIGNEES",
            "BILLABLE", "PRIORITY", "STATUS", "TIME_SPENT", "ATTACHMENT"),
    MultiTaskAdd(ViewName.Task, "TASK_NAME", "DESCRIPTION", "ASSIGNEES", "STARTDATE", "DUEDATE", "PRIORITY", "BILLABLE", "ATTACHMENT"),
    ProjectAdd(ViewName.Project, "PROJECT_NUMBER", "PARENT_PROJECT", "PROJECT_NAME", "DESCRIPTION", "STARTDATE", "DUEDATE", "ASSIGNEES",
            "MANAGER", "BACKUP-MANAGER", "CLIENT", "STATUS", "ATTACHMENTS"),
    ContactAdd(ViewName.Contact, "FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "OTHER_NAME", "DATE_OF_BRITH",
            "TITLE", "COMPANY_NAME", "JOB_TITLE", "DEPARTMENT", "POSITION",
            "ORGANITION_TYPE", "ANNUAL_REVINUE", "INDUSTRY", "NUMBER_OF_EMPLOYEES",
            "EMAIL", "PHONE_NUMBERS", "IM_ADDRESS", "WEB_ADDRESS", "ADDRESS",
            "CATEGORY", "RELATIONSHIP", "REPORTS_TO", "CONTACT_OWNER",
            "ACCOUNT", "COMPAIGN", "EMAIL_OPT_OUT", "SUBSCRIPTION_LIST", "ATTACHMENT"),
    ProductCategoryAdd(ViewName.ProductCategory),
    //    ProductServiceAdd(ViewName.ProductServiceView),
    SaleInvoiceAdd(ViewName.SaleInvoice),
    PurchaseInvoiceAdd(ViewName.PurchaseInvoice),
    SaleQuoteAdd(ViewName.SaleQuote),
    SaleOrderAdd(ViewName.SaleOrder),
    PurchaseOrderAdd(ViewName.PurchaseOrder),
    ExpenseReportAdd(ViewName.ExpenceReportView),
    LogACallAdd(ViewName.LogACall),
    ActivityAdd(ViewName.Activity),
    BatchInvoicePaymentAdd(ViewName.BatchInvoicePaymentView),
    MeetingMInutesView(ViewName.MeetingMInutesView),
    BatchPayBillAdd(ViewName.BatchPayBillView),
    BankTransferAdd(ViewName.BankTransferList),
    SinglePayrunView(ViewName.SinglePayrun),
    RequestForQuoteAdd(ViewName.RequestForQuote),
    RequestForPurchaseAdd(ViewName.RequestForPurchase);

    ViewAddFiledsCodeName(ViewName viewName, String... fields) {
        this.viewName = viewName;
        this.fields = fields;
    }

    private ViewName viewName;
    private String[] fields;

    public ViewName getViewName() {
        return viewName;
    }

    public String[] getFields() {
        return fields;
    }
}
