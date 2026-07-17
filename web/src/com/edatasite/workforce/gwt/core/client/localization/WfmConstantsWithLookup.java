package com.edatasite.workforce.gwt.core.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * User: Ilhombek
 * Date: 10/15/11
 * Time: 7:34 PM
 */
public interface WfmConstantsWithLookup extends ConstantsWithLookup {

    String Task();

    String Project();

    String Department();

    String Employee();

    String MessageCenter();

    String Lead();

    String Schemas();

    String Contact();

    String CrmAccount();

    String MailListMember();

    String SolrInconsistency();

    String Poll();

    String Survey();

    String BmtListValues();

    String ServerLog();

    String RecurrenceLog();

    String News();

    String Client();

    String Supplier();

    String SaleInvoice();

    String SaleQuote();

    String Location();

    String SaleOrder();

    String CustomFields();

    String ReportingSystem();

    String Document();

    String Event();

    String PurchaseOrder();

    String Holiday();

    String SalaryGrade();

    String _TASK_PRIORITY_HIGH();

    String _TASK_PRIORITY_MEDIUM();

    String _TASK_PRIORITY_LOW();

    String ON_HOLD();

    String NOT_STARTED();

    String IN_PROGRESS();

    String COMPLETED();

    String WAITING_FOR_SOMEONE_ELSE();

    String CLOSED();

    String CANCELLED();

    String BrandsListView();

    String WareHousesLocations();

    String ProductComments();

    String ManualTransaction();

    String ProjectInvoicesListView();

    String HrmsExpenceReportView();

    String Positions();

    String EmployeesGoal();

    String Incient();

    String PersonalGoal();

    String CompanyGoal();

    String ProjectGoal();

    String BusinessGoal();

    String PerformanceNote();

    String AppraisalTemplate();

    String AssessmentArchive();

    String Competency();

    String Mail();

    String Message();

    String QueuedMessages();

    String SentMessage();

    String Solution();

    String ProducktServiceStockView();

    String ReportingDashlet();

    String ReportingDashboard();

    String ReportingDashoboardDownloadLink();

    String AttendanceTrackingListView();

    String Departments();

    String Directory();

    String BankAccountTransaction();

    String AEO();

    String Activity();

    String Blog();

    String Website();

    String Tag();

    String PageBlock();

    String PageLayoutBlock();

    String WebsiteLayoutBlock();

    String WebSiteLayout();

    String Block();

    String PageLayout();

    String QuickbookSynchHistory();

    String PAYETax();

    String WebsiteMenu();

    String BugsListSummary();

    String BouncedMessage();

    String Unsubbed();

    String MessageTrack();

    String ProductCategoryStoreFront();

    String CustomPage();

    String StoreFront();

    String WebForm();

    String Campain();

    String VatReports();

    String PurchaseInvoice();

    String TimeSheetApproval();

    String TaxRates();

    String WareHouses();

    String BankAccounts();

    String CustomEntity();

    String CustomForms();

    String ReportingXMLTemplates();

    String BackendAccessLogListPanel();

    String BackendCustomisedPDFTemplatesListView();

    String PaymentDeductionListPanel();

    String PunishmentsPromotionsList();

    String EmployeePunishmentsPromotionsList();

    String PremiumRecommendationsList();

    String TenderCardList();

    String Estimate();

    String Terms();

    String Timeslot();

    String Issues();

    String BookingItemsView();

    String Contract();

    String AnnualAllowanceListView();

    String ValidityPeriodListView();

    String WorkspaceCategory();

    String ReferenceView();

    String UsageHistory();

    String CountrySettingsList();

    String BenefitList();

    String EmployeeHistoryList();

    String Role();

    String Workflow();

    String ImportLogsView();

    String WorkflowActivities();

    String WorkflowAlert();

    String ProductServiceView();

    String ExpenceReportView();

    String FixedAsset();

    String EmployeePayslipList();

    String ProjectManagement();

    String HRMS();

    String CRM();

    String Payroll();

    String Custom();

    String Folders();

    String Vacancy();

    String RecurringInvoice();

    String ProjectExpenseClaimsListView();

    String ProjectPurchaseOrderListView();

    String MeasurementsList();

    String Discount();

    String MeetingMInutesView();

    String WorkflowUpdateField();

    String WorkflowPushNotification();

    String WorkflowActions();

    String WorkflowSMSAlert();

    String Signature();

    String SMSTemplates();

    String EmailTemplates();

    String ShortList();

    String Placement();

    String Candidate();

    String WorkspaceNews();

    String EmployeeLeave();

    String PeriodAppraisalsListView();

    String CompanyDocumentList();

    String BenefitRequestList();

    String Certificates();

    String EmployeeBonuses();

    String CashAdvancePaymentList();

    String SMSSetting();

    String EmailFilter();

    String MessagesView();

    String EmployeeDocumentList();

    String PastEmploymentListView();

    String DependentListView();

    String SinglePayrun();

    String EmailAccount();

    String MessageClickTrack();

    String ConsignmentList();

    String AccountTransactions();

    String InventoryItemsView();

    String RequestForQuote();

    String RequestForPurchase();

    String RecurringBill();

    String ReservationView();

    String StockAdjustments();

    String InvoiceTemplatesListView();

    String BankTransferList();

    String Prepayment();

    String CheckList();

    String BatchInvoicePaymentView();

    String BatchPayBillView();

    String CurrencyList();

    String PaymentMethodList();

    String AccountingView();

    String EmployeeTemplateList();

    String SALE_INVOICE_ITEM();

    String SALE_QUOTE_ITEM();

    String PURCHASE_INVOICE_ITEM();

    String PURCHASE_ORDER_ITEM();

    String OPPORTUNITY_SUB_ITEM();

    String RFQ_ITEM();

    String RFP_ITEM();

    String CLIENT_ITEM();

    String SUPPLIER_ITEM();

    String BILL_OF_MATERIALS_ITEM();

    String EXPENSE_CLAIM_ITEM();

    String CUSTOM_FORM();

    String ShippingData();

    String MANUAL_JOURNAL_ITEM();

    String BANK_RECEIPT_ITEM();

    String BANK_PAYMENT_ITEM();

    String CASH_RECEIPT_ITEM();

    String CASH_PAYMENT_ITEM();


    class App {
        private static WfmConstantsWithLookup instance;

        public static WfmConstantsWithLookup get() {
            if (instance == null) {
                instance = GWT.create(WfmConstantsWithLookup.class);
            }
            return instance;
        }
    }
}