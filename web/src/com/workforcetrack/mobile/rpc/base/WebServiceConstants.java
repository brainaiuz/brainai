package com.workforcetrack.mobile.rpc.base;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/8/11
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public interface WebServiceConstants {

    String HOST_LIVE_KPI = "app.kpi.com";

    String WFT_WEB_SERVICE = "wftWebService";
    String WFT_JAXB_CONTEXT_LOADER = "wftjaxbContextLoader";
    String SESSION_ID = "sessionID";
    String WFT_REQUESTS_TAG = "<WFTRequests>";

    boolean isValidSessionID = false;

    // OUTLOOK ACTIONS
    int VIEW = 1;
    int ADD = 2;
    int ADD_WITH_PARAMS = 3;
    int EDIT = 4;
    int ADD_NOTE = 5;
    int ADD_EVENT = 6;
    int ADD_TASK = 7;
    int ADD_LOG_EVENT = 8;

    //CRUD methods
    String GET = "get";
    String GET_LIST = "getList";
    String DELETE = "delete";
    String SAVE = "save";
    String SAVE_WITH_RETURN_ID = "saveWithReturnID";

    //Other methods
    String SYNCHRONIZE_CONTACTS = "synchronizeContacts";
    String SYNCHRONIZE_CONTACTS_WITH_OUTLOOK = "synchronizeContactsWithOutlook";

    //ContactService CRUD methods name
    String GET_CONTACT = "getContact";
    String GET_CONTACT_LIST = "getContactList";
    String DELETE_CONTACT = "deleteContact";
    String SAVE_CONTACT = "saveContact";

    //Calendar service methods
    String SAVE_EVENT = "saveEvent";
    String SAVE_TASK = "saveTask";


    //ServiceName
    String CONTACT_SERVICE = "contactService";
    String CONTACT_WEB_SERVICE = "contactWebService";
    String CRM_SERVICE = "crmService";
    String CRM_WEB_SERVICE = "crmWebService";
    String TASK_SERVICE = "taskService";
    String TASK_WEB_SERVICE = "taskWebService";
    String CALENDAR_SERVICE = "calendarService";
    String CALENDAR_WEB_SERVICE = "calendarWebService";
    String LOGIN_SERVICE = "loginService";
    String LOGIN_WEB_SERVICE = "loginWebService";
    String SIGNUP_WEB_SERVICE = "signUpWebService";
    String SIGNUP_SERVICE = "signUpService";
    String PROJECT_SERVICE = "projectService";
    String PROJECT_WEB_SERVICE = "projectWebService";
    String TIMESHEET_SERVICE = "timesheetService";
    String TIMESHEET_WEB_SERVICE = "timesheetWebService";
    String ACCOUNTING_SERVICE = "accountingService";
    String ACCOUNTING_WEB_SERVICE = "accountingWebService";
    String INVOICE_SERVICE = "invoiceService";
    String INVOICE_WEB_SERVICE = "invoiceWebService";
    String PRODUCT_SERVICE = "productService";
    String PRODUCT_WEB_SERVICE = "productWebService";
    String CLIENT_SERVICE = "clientService";
    String CLIENT_WEB_SERVICE = "clientWebService";
    String EXPENSE_WEB_SERVICE = "expenseWebService";
    String EXPENSE_SERVICE = "expenseService";
    String STATUS_WEB_SERVICE = "statusWebService";
    String STATUS_SERVICE = "statusService";
    String BUG_REPORT_SERVICE = "bugReportService";
    String BUG_REPORT_WEB_SERVICE = "bugReportWebService";
    String AVAILABILITY_SERVICE = "availabilityService";
    String MESSAGE_CENTER_WEB_SERVICE = "messageCenterWebService";
    String MESSAGE_CENTER_SERVICE = "messageCenterService";
    String UPLOAD_WEB_SERVICE = "uploadWebService";
    String UPLOAD_SERVICE = "uploadService";


    //WFP Service names
    String NEWS_SERVICE = "newsService";
    String NEWS_WEB_SERVICE = "newsWebService";
    String WFP_PRODUCT_WEB_SERVICE = "wfpProductWebService";
    String FEEDBACK_SERVICE = "feedbackService";
    String FEEDBACK_WEB_SERVICE = "feedbackWebService";
    String DIRECTORY_SERVICE = "directoryService";
    String DIRECTORY_WEB_SERVICE = "directoryWebService";

    String USERNAME_SERVICE = "wfd";
    String PASSWORD_SERVICE = "wfdTest";

    //Response class types
    String MCONTACT_LIST_ITEM = "MContactListItem";
    String MCONTACT_LIST = "MContactList";
    String UserSignUPSessionID = "UserSignUPSessionID";
    String MCOMPANY_LIST = "MCompanyList";
    String MUSER_INFO = "MUserInfo";
    String MCOMPANY_NAME_LIST = "MCompanyNameList";
    String MCONTACT_CATEGORY_LIST = "MContactCategoryList";
    String MCOUNTRY_LIST = "MCountryList";
    String MSTATE_LIST = "MStateList";
    String MCOUNTRY_STATES_LIST = "MCountryStates";
    String MAPPOINTMENT_LIST = "MAppointmentList";
    String MAPPOINTMENT = "MAppointment";
    String MUSER_COMPANY_DTO = "MUserCompanyDTO";
    String MOPPORTUNITY_LIST = "MOpportunityList";
    String MOPPORTUNITY_LIST_ITEM = "MOpportunityListItem";
    String MHISTORY_LIST = "MHistoryList";
    String MCASE_LIST = "MCaseList";
    String MCASE_ITEM = "MCaseItem";
    String MEMAIL_LIST = "MEmailList";
    String MCRM_FILTER_DATA = "MCrmFilterData";

    String MCONTACT_COMPANY_INFO = "MContactCompanyInfo";

    String MINTEGER_LIST = "MIntegerList";
    String MSTRING_LIST = "MStringList";

    String MCREATED_COMPANY = "MCreatedCompany";
    String MFILTER_DATA = "MFilterData";


    //ProjectWebService
    String MPROJECT_LIST = "MProjectList";
    String MPROJECT_LIST_ITEM = "MProjectListItem";
    String MPROJECT_STATUS_ITEM_LIST = "MProjectStatusItemList";
    String MCLIENT_LIST = "MClientList";
    String MPROJECT_MEMBER_LIST = "MProjectMemberList";
    String MPROJECT_STATUS_LIST = "MProjectStatusList";


    //TaskWebService
    String MTASK_LIST_ITEM = "MTaskListItem";
    String MTASK_LIST = "MTaskList";
    String MPOSITION_LIST = "MPositionList";
    String MPRIORITY_LIST = "MPriorityList";
    String MPROJECT_ITEM_LIST = "MProjectItemList";
    String MTASK_STATUS_LIST = "MTaskStatusList";
    String MTASK_FILTER_DATA = "MTaskFilterData";

    //TimesheetWebService
    String MPROJECT_TREE_LIST = "MProjectTreeList";
    String MTIMESHEET_DATA = "MTimesheetData";

    //ProductWebService
    String MPRODUCT_LIST_ITEM = "MProductListItem";
    String MPRODUCT_LIST = "MProductList";

    //InvoiceWebService
    String MINVOICE_LIST_ITEM = "MInvoiceListItem";
    String MINVOICE_LIST = "MInvoiceList";
    String MSELECT_ITEM = "MSelectItem";
    // public static final String MCURRENCY_LIST = "MCurrencyList";
    String MINVOICE_NUMBER_DATA = "MInvoiceNumberData";
    String MTYPE_ITEM_LIST = "MTypeItemList";
    String MTAX_LIST = "MTaxList";
    String MPRODUCTS_BY_TYPE_LIST = "MProductsByTypeList";
    String MCLIENT_SUPPLIER_ADDRESS_DATA = "MClientSupplierAddressData";

    //ClientWebService
    String MCLIENT_LIST_ITEM = "MClientListItem";
    String MNEW_CLIENT_LIST = "MNewClientList";
    String MBILLING_DATA = "MBillingData";
    String MCLIENT_CONTACT_LIST = "MClientContactList";
    String MCLIENT_CONTACT_LIST_ITEM = "MClientContactListItem";
    String MLIST_ARRAY = "MListArray";
    // public static final String MCOUNTRY_LIST = "MCountryList";
    String MREGION_LIST = "MRegionList";
    String MCONTACT_ITEM = "MContactItem";
    String MCONTACT_ITEM_LIST = "MContactItemList";

    //CrmWebService
    String MCRM_ACCOUNT_LIST = "MCrmAccountList";
    String MCRM_ACCOUNT_LIST_ITEM = "MCrmAccountListItem";

    //ExpenseWebService
    String MCURRENCY_ITEM = "MCurrencyItem";
    String MAPPROVER_LIST = "MApproverList";
    String MCURRENCY_LIST = "MCurrencyList";
    String MEXPENSE_LIST = "MExpenseList";
    String MREPORT_DATA = "MReportData";
    String MRELATED_PROJECT_LIST = "MRelatedProjectList";
    String MEXPENSE_REPORTS_LIST = "MExpenseReportsList";
    String MEXPENSE_REPORTS_LIST_ITEM = "MExpenseReportsListItem";
    String MEMAIL_TEMPLATE_LIST = "MEmailTemplateList";
    String MEMAIL_TEMPLATE_ITEM = "MEmailTemplateItem";

    //AccountingWebService
    String MACCOUNT_LIST = "MAccountList";
    String MACCOUNTS_BY_CATEGORY = "MAccountsByCategory";
    String MBUG_REPORT_ITEM = "MBugReportItem";
    String MPRODUCT_CATEGORY_LIST = "MProductCategoryList";
    String MPRODUCTCATEGORY_ITEM = "MProductCategoryListItem";
    String MFIXED_ASSET_ITEM_LIST = "MFixedAssetItemList";
    String MFIXED_ASSET_GROUP_ITEM_LIST = "MFixedAssetGroupItemList";
    String MNUMBER_DATA = "MNumberData";

    //MessageCenterWebService
    String MUSER_MAIL_MESSAGE_LIST = "MUserMailMessageList";
    String MMESSAGE_TRACKER_NAME_LIST = "MMessageTrackerNameList";
    String MTRACKER_NAME_LIST = "MTrackerNameList";
    String MSELECT_ITEM_LIST = "MSelectItemList";
}
