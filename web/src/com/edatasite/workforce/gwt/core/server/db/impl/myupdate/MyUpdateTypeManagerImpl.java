package com.edatasite.workforce.gwt.core.server.db.impl.myupdate;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsGoogleCalendarEventGuests;
import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsNewsComment;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheck;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsRFP;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.EdsSolution;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdateType;
import com.edatasite.workforce.core.domain.network.EdsNetwork;
import com.edatasite.workforce.core.domain.network.EdsNetworkContact;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.core.domain.workflow.EdsWebHookResponse;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarEventGuestsManager;
import com.edatasite.workforce.gwt.core.server.db.HolidayManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.MailListManager;
import com.edatasite.workforce.gwt.core.server.db.NewsCommentManager;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PlacementManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.SolutionManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetApprovalSessionManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.WebHookResponseManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankCheckManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BatchPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.FixedAssetManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.db.network.NetworkContactManager;
import com.edatasite.workforce.gwt.core.server.db.network.NetworkManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.webforms.WebFormManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Set;

import static com.edatasite.workforce.core.domain.accounting.EdsAccountType.PREPAYMENT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SALE_ORDER_CODE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SALE_QUOTE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SUPPLIER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.defaultSupportName;

/**
 * User: Abdulaziz
 * Date: Jan 7, 2010
 * Time: 12:31:24 PM
 */

@Repository("myUpdateTypeManager")
public class MyUpdateTypeManagerImpl extends BaseManager<EdsMyUpdateType> implements MyUpdateTypeManager {
    private static final DecimalFormat numberFormat = new DecimalFormat(",##0.00");
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Autowired
    ManualJournalManager manualJournalManager;
    @Autowired
    RFQManager rfqManager;
    @Autowired
    RFPManager rfpManager;
    @Autowired
    ExpensePaymentManager expensePaymentManager;
    @Autowired
    SpendReceiveMoneyManager spendReceiveMoneyManager;
    @Autowired
    BatchPaymentManager batchPaymentManager;
    @Autowired
    PayslipTableItemManager payslipTableItemManager;
    @Autowired
    CashAdvanceManager cashAdvanceManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private TimeSheetApprovalSessionManager timeSheetApprovalSessionManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private NetworkManager networkManager;
    @Autowired
    private NewsManager newsManager;
    @Autowired
    private NewsCommentManager newsCommentManager;
    @Autowired
    private NetworkContactManager networkContactManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private GoogleCalendarEventGuestsManager eventGuestsManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private FixedAssetManager fixedAssetManager;
    @Autowired
    private BankCheckManager bankCheckManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private BankAccountManager bankAccountManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private MailListManager mailListManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private WebFormManager webFormManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private SolutionManager solutionManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Qualifier("companyCFSettingsManager")
    @Autowired
    private CompanyCustomFieldsManager companyCFManager;
    @Autowired
    @Qualifier("myActivityLocalizer")
    private WfmMessageSource activityWfmMessageSource;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private ExpenseReportManager reportManager;
    @Autowired
    private HolidayManager holidayManager;
    @Autowired
    private ListPanelSettingsManager listPanelSettingsManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private CustomFormManager customFormManager;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private AdditionalPaymentManager additionalPaymentManager;
    @Autowired
    private PlacementManager placementManager;
    @Autowired
    private WebHookResponseManager webHookResponseManager;

    public MyUpdateTypeManagerImpl() {
        super(EdsMyUpdateType.class);
    }

    public EdsMyUpdateType getType(String code, String parentCode) {
        return (EdsMyUpdateType) findSingle("SELECT ut FROM EdsMyUpdateType ut WHERE ut.code = ? AND ut.parent.code = ? ", code, parentCode);

    }

    public String getUpdatesLink(EdsMyUpdate myUpdate) {
        String companyId = EncryptionHelper.encryptURL(myUpdate.getCompanyID().toString());
        String linkK = //"&" + Constants.USER + "=" + EncryptionHelper.encryptURL(getUser().getObjectID().toString()) +
                "&cid=" + companyId;
        String link = "";
        String eventType = myUpdate.getEventType();
        String code = getParentTypeCodeByCode(myUpdate.getTypeCode());
        if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.TASK)) {
            if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.TASK_FILE_UPLOAD)) {
                EdsFileHeader edsFileHeader = fileHeaderManager.get(myUpdate.getAffectedID());
                if (edsFileHeader != null) {
                    link = getEncryptEncodedLink("task|document/" + edsFileHeader.getEntityId().toString()) + linkK;
                }
            } else {
                link = getEncryptEncodedLink("task|summary/" + myUpdate.getAffectedID().toString()) + linkK;
            }
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.PROJECT)) {
            if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.PROJECT_FILE_UPLOAD)) {
                EdsFileHeader edsFileHeader = fileHeaderManager.get(myUpdate.getAffectedID());
                if (edsFileHeader != null) {
                    link = getEncryptEncodedLink("project|document/" + edsFileHeader.getEntityId().toString()) + linkK;
                }
            } else {
                link = getEncryptEncodedLink("project|summary/" + myUpdate.getAffectedID().toString()) + linkK;
            }
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.DEPARTMENT)) {
            link = getEncryptEncodedLink("department|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.USER_EMPLOYEE_ADD)) {
            link = getEncryptEncodedLink("employee|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.USER_EMPLOYEE_EDIT)) {
            link = getEncryptEncodedLink("employee|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.CLIENT)) {
            link = getEncryptEncodedLink("client|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.USER_CLIENT_CONTACT_ADD)) {
            Integer clientId = clientContactManager.getClientIdByClientContact(myUpdate.getAffectedID());
            link = clientId != null ? (getEncryptEncodedLink("client|contacts/" + clientId) + linkK) : "";
        } else if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.USER_CLIENT_CONTACT_EDIT)) {
            Integer clientId = clientContactManager.getClientIdByClientContact(myUpdate.getAffectedID());
            link = clientId != null ? (getEncryptEncodedLink("client|contacts/" + clientId) + linkK) : "";
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.ISSUE)) {
            if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.ISSUE_FILE_UPLOAD)) {
                EdsFileHeader edsFileHeader = fileHeaderManager.get(myUpdate.getAffectedID());
                if (edsFileHeader != null) {
                    link = getEncryptEncodedLink("issue|summary/" + edsFileHeader.getEntityId().toString()) + linkK;
                }
            } else {
                link = getEncryptEncodedLink("issue|summary/" + myUpdate.getAffectedID().toString()) + linkK;
            }
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.LOCATION)) {
            link = getEncryptEncodedLink("location|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.LEAD)) {
            link = getEncryptEncodedLink("lead|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.ACCOUNT)) {
            link = getEncryptEncodedLink("account|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.SOLUTION)) {
            link = getEncryptEncodedLink("solution|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.WEB_FORM)) {
            link = getEncryptEncodedLink("webform|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.CASE)) {
            link = getEncryptEncodedLink("case|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.TIMESLOT)) {
            link = getEncryptEncodedLink("timeslot|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.HOLIDAY)) {
            link = getEncryptEncodedLink("holiday|view/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && (code.equals(MyUpdateTypeManager.CONTACT) ||
                (code.equals(MyUpdateTypeManager.CONTACT_NOTE)) || (code.equals(MyUpdateTypeManager.LEAD_NOTE)))) {
            link = getEncryptEncodedLink("contact|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.OPPORTUNITY)) {
            link = getEncryptEncodedLink("opportunity|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.CAMPAIGN)) {
            link = getEncryptEncodedLink("campaign|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.TIMESHEET_STATUS_WAITING_FOR_MANAGER)) {
            link = getEncryptEncodedLink("myworkspace|timesheetApproval") + linkK;
        } else if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.TIMESHEET_STATUS_APPROVED)) {
            link = getEncryptEncodedLink("myworkspace|timesheetApproval") + linkK;
        } else if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.TIMESHEET_STATUS_APPROVED_FOR_EMPLOYEE)) {
            link = getEncryptEncodedLink("myworkspace|timesheet") + linkK;
        } else if (isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.SALES_INVOICE) &&
                !eventType.equals(EdsMyUpdate.DELETE) && invoiceManager.get(myUpdate.getAffectedID()) != null) {
            link = getEncryptEncodedLink("saleinvoice|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.RECURRING_INVOICE) &&
                !eventType.equals(EdsMyUpdate.DELETE) && invoiceManager.get(myUpdate.getAffectedID()) != null) {
            link = getEncryptEncodedLink("recurringinvoice|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) &&
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.SALES_QUOTE) && quoteManager.get(myUpdate.getAffectedID()) != null) {
            String homeURL = "salequote";
            if (myUpdate.getTypeCode().equals(MyUpdateTypeManager.SALES_QUOTE_CONVERT_TO_SALE_ORDER)) {
                homeURL = "saleorder";
            }
            link = getEncryptEncodedLink(homeURL + "|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) &&
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.SALES_ORDER) &&
                quoteManager.get(myUpdate.getAffectedID()) != null) {
            String homeURL = "saleorder";
            link = getEncryptEncodedLink(homeURL + "|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) &&
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.PURCHASE_ORDER) && quoteManager.get(myUpdate.getAffectedID()) != null) {
            link = getEncryptEncodedLink("purchaseorder|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) &&
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.PURCHASE_INVOICE) && invoiceManager.get(myUpdate.getAffectedID()) != null) {
            link = getEncryptEncodedLink("purchaseinvoice|summary/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.PRODUCT)) {
            EdsItem item = itemManager.get(myUpdate.getAffectedID());
            if (item != null) {
                link = getEncryptEncodedLink("product|summary/" + myUpdate.getAffectedID().toString() + "/" + item.getTypeName() + "/FROM_MY_UPDATE") + linkK;
            }
        } else if (!eventType.equals(EdsMyUpdate.DELETE) &&
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.FIXED_ASSET) && fixedAssetManager.get(myUpdate.getAffectedID()) != null) {
            link = getEncryptEncodedLink("fixedasset|summary/" + myUpdate.getAffectedID().toString() + "/FROM_MY_UPDATE") + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) &&
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CHECK) && bankCheckManager.get(myUpdate.getAffectedID()) != null) {
            link = getEncryptEncodedLink("check|summary/" + myUpdate.getAffectedID().toString() + "/FROM_MY_UPDATE") + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) &&
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CHART_OF_ACCOUNT) && accountingManager.get(myUpdate.getAffectedID()) != null) {
            link = getEncryptEncodedLink("chartOfAccount|chartOfAccountSummary/" + myUpdate.getAffectedID().toString() + "/FROM_MY_UPDATE") + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.EXPENSE_REPORT) &&
                reportManager.getExpenseReport(myUpdate.getAffectedID()) != null) {
            link = getEncryptEncodedLink("expenseReports|previewReport/" + myUpdate.getAffectedID().toString() + "/" + Constants.EXPENSE_VIEW + linkK);
        } else if (isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CALENDAR_EVENT_GUEST)) {
            link = "calendar";
        } else if (isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.BANK_TRANSFER)) {
            EdsBankTransfer bankTransfer = spendReceiveMoneyManager.get(myUpdate.getAffectedID());
            Integer transferType = null;
            if (bankTransfer != null && bankTransfer.getTransferType() != null) {
                transferType = bankTransfer.getTransferType();
            }
            String itemType = "";
            if (transferType != null) {
                if (AccountingConstants.RECEIVE_MONEY.equals(transferType)) {
                    itemType = "/CASH_RECEIPT";
                } else if (AccountingConstants.SPEND_MONEY.equals(transferType)) {
                    itemType = "/CASH_PAYMENT";
                } else if (AccountingConstants.CASH_RECEIPT.equals(transferType)) {
                    itemType = "/RECEIVE_MONEY";
                } else if (AccountingConstants.CASH_PAYMENT.equals(transferType)) {
                    itemType = "/SPEND_MONEY";
                }
            }
            link = getEncryptEncodedLink("spendreceivemoney|summary/" + myUpdate.getAffectedID().toString() + itemType + linkK);
        } else if (isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.INVOICE_PAYMENT)) {
            link = getEncryptEncodedLink("invoicepayment|paymentView/" + myUpdate.getAffectedID().toString()) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.EVENT)) {
            String affectedId = myUpdate.getAffectedID() != null ? myUpdate.getAffectedID().toString() : "";
            link = getEncryptEncodedLink("event|summary/" + affectedId) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.BATCH_PAYMENT)) {
            String affectedId = myUpdate.getAffectedID() != null ? myUpdate.getAffectedID().toString() : "";
            link = getEncryptEncodedLink("receivepayment|summary/" + affectedId) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.ACCOUNTING_MANUAL_JOURNAL)) {
            String affectedId = myUpdate.getAffectedID() != null ? myUpdate.getAffectedID().toString() : "";
            link = getEncryptEncodedLink("manual|summary/" + affectedId) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_QUOTE)) {
            String affectedId = myUpdate.getAffectedID() != null ? myUpdate.getAffectedID().toString() : "";
            link = getEncryptEncodedLink("requestforquote|summary/" + affectedId) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_PURCHASE)) {
            String affectedId = myUpdate.getAffectedID() != null ? myUpdate.getAffectedID().toString() : "";
            link = getEncryptEncodedLink("requestforpurchase|summary/" + affectedId) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.SINGLE_PAYRUN)) {
            String affectedId = myUpdate.getAffectedID() != null ? myUpdate.getAffectedID().toString() : "";
            link = getEncryptEncodedLink("singlePayrun|viewPayslip/" + affectedId) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.GROUP_PAYRUN)) {
            String affectedId = myUpdate.getAffectedID() != null ? myUpdate.getAffectedID().toString() : "";
            link = getEncryptEncodedLink("payslipTable|summary/" + affectedId) + linkK;
        } else if (!eventType.equals(EdsMyUpdate.DELETE) && code.equals(MyUpdateTypeManager.CASH_ADVANCE)) {
            String affectedId = myUpdate.getAffectedID() != null ? myUpdate.getAffectedID().toString() : "";
            link = getEncryptEncodedLink("cashAdvance|summary/" + affectedId) + linkK;
        } else if (eventType.equals(WEBHOOK_ADD)) {
            String webhookId = myUpdate.getRelationID() != null ? myUpdate.getRelationID().toString() : "";
            link = getEncryptEncodedLink("");
        }

        return link;
    }

    private boolean isParentCodeEqualsTo(EdsMyUpdate myUpdate, String code) {
        return getParentTypeCodeByCode(myUpdate.getTypeCode()).equals(code);
    }

    public String getSectionURL(EdsMyUpdate myUpdate) {

        String sectionURL = "";
        if (getParentTypeCodeByCode(myUpdate.getTypeCode()).equals(MyUpdateTypeManager.SALES_QUOTE) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.SALES_INVOICE) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.RECURRING_INVOICE) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CLIENT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.EXPENSE_REPORT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.PURCHASE_ORDER) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.PURCHASE_INVOICE) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.SALES_ORDER) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.PRODUCT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.FIXED_ASSET) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CHECK) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.BANK_ACCOUNT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.BANK_TRANSFER) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.INVOICE_PAYMENT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.BATCH_PAYMENT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CHART_OF_ACCOUNT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.ACCOUNTING_MANUAL_JOURNAL) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_QUOTE) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_PURCHASE)) {

            sectionURL = MyUpdateItem.ACCOUNTING_SECTION_URL;

        } else if (getParentTypeCodeByCode(myUpdate.getTypeCode()).equals(MyUpdateTypeManager.TASK) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.TIMESHEET) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.PROJECT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.DEPARTMENT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.LOCATION) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.USER) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.ISSUE) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.WORKSPACE_NOTE)) {

            sectionURL = MyUpdateItem.PM_SECTION_URL;
        } else if (getParentTypeCodeByCode(myUpdate.getTypeCode()).equals(MyUpdateTypeManager.OPPORTUNITY) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CALENDAR_EVENT_GUEST) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.LEAD) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CONTACT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CONTACT_NOTE) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.LEAD_NOTE) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CAMPAIGN) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.ACCOUNT) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.WEB_FORM) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CASE) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.SOLUTION) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.EVENT)) {
            sectionURL = MyUpdateItem.CRM_URL;
        } else if (getParentTypeCodeByCode(myUpdate.getTypeCode()).equals(MyUpdateTypeManager.SINGLE_PAYRUN) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.GROUP_PAYRUN) ||
                isParentCodeEqualsTo(myUpdate, MyUpdateTypeManager.CASH_ADVANCE)) {
            sectionURL = MyUpdateItem.PAYROLL_URL;
        }
        return sectionURL;
    }


    private String getEncryptEncodedLink(String plainText) {
        return EncryptionHelper.encodeURL(EncryptionHelper.encryptURL(plainText));
    }

    public void getMyUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        if (item == null || type == null || type.getParent() == null || type.getParent().getCode() == null) {
            return;
        }
        String code = type.getParent().getCode();
        if (EdsTrusteeType.USER.equals(myUpdate.getReceiverType())) {
            switch (code) {
                case TASK -> getTaskUpdateMessage(myUpdate, item);
                case PROJECT -> getProjectUpdateMessage(myUpdate, item);
                case USER -> getUserUpdateMessage(myUpdate, item);
                case DEPARTMENT -> getDepartmentUpdateMessage(myUpdate, item);
                case CLIENT -> getClientUpdateMessage(myUpdate, item);
                case ISSUE -> getIssueUpdateMessage(myUpdate, item);
                case LOCATION -> getLocationUpdateMessage(myUpdate, item);
                case TIMESHEET -> getTimesheetUpdateMessage(myUpdate, item, broadMessage);
                case WORKSPACE_NOTE -> getNoteUpdateMessage(myUpdate, item);
                case CONTACT_NOTE -> getContactNoteUpdateMessage(myUpdate, item);
                case LEAD_NOTE -> getLeadNoteUpdateMessage(myUpdate, item);
                case SALES_INVOICE -> getSalesInvoiceUpdateMessage(myUpdate, item, false);
                case RECURRING_INVOICE -> getRecurringInvoiceUpdateMessage(myUpdate, item);
                case SALES_QUOTE -> getSaleQuoteUpdateMessage(myUpdate, item);
                case SALES_ORDER -> getSaleOrderUpdateMessage(myUpdate, item);
                case EXPENSE_REPORT -> getExpenseReportUpdateMessage(myUpdate, item);
                case PURCHASE_ORDER -> getPurchaseOrderUpdateMessage(myUpdate, item);
                case PURCHASE_INVOICE -> getPurchaseInvoiceUpdateMessage(myUpdate, item);
                case PRODUCT -> getProductUpdateMessage(myUpdate, item);
                case FIXED_ASSET -> getFixedAssetUpdateMessage(myUpdate, item);
                case CHECK -> getCheckUpdateMessage(myUpdate, item);
                case BANK_ACCOUNT -> getBankAccountUpdateMessage(myUpdate, item);
                case BANK_TRANSFER -> getBankTransferUpdateMessage(myUpdate, item);
                case ACCOUNTING_MANUAL_JOURNAL -> getManualJournalUpdateMessage(myUpdate, item);
                case ACCOUNTING_REQUEST_FOR_QUOTE -> getRfqUpdateMessage(myUpdate, item);
                case ACCOUNTING_REQUEST_FOR_PURCHASE -> getRfpUpdateMessage(myUpdate, item);
                case CHART_OF_ACCOUNT -> getChartOfAccountUpdateMessage(myUpdate, item);
                case CALENDAR_EVENT_GUEST -> getCalendarEventGuestUpdate(myUpdate, item);
                case LEAVE_REQUEST -> getLeaveRequestUpdateMessage(myUpdate, item);
                case LEAD, CONTACT -> getLeadUpdateMessage(myUpdate, item);
                case OPPORTUNITY -> getOpportunityUpdateMessage(myUpdate, item);
                case CATEGORY -> getContactCategoryUpdateMessage(myUpdate, item);
                case MAILING -> getMailingListUpdateMessage(myUpdate, item);
                case CAMPAIGN -> getCampaignUpdateMessage(myUpdate, item);
                case ACCOUNT -> getAccountUpdateMessage(myUpdate, item);
                case WEB_FORM -> getWebFormUpdateMessage(myUpdate, item);
                case CASE -> getCaseUpdateMessage(myUpdate, item);
                case SOLUTION -> getSolutionUpdateMessage(myUpdate, item);
                case COMPANY_CUSTOM_FIELD -> getCustomFieldUpdateMessage(myUpdate, item);
                case TIMESLOT -> getTimeSlotUpdateMessage(myUpdate, item);
                case HOLIDAY -> getHolidayUpdateMessage(myUpdate, item);
                case PANEL_SETTING -> getPanelSettingsUpdateMessage(myUpdate, item, broadMessage);
                case INVOICE_PAYMENT -> getInvoicePaymentUpdateMessage(myUpdate, item);
                case EVENT -> getEventUpdateMessage(myUpdate, item);
                case BATCH_PAYMENT -> getBatchPaymentUpdateMessage(myUpdate, item);
                case SINGLE_PAYRUN -> getSinglePayrunUpdateMessage(myUpdate, item);
                case GROUP_PAYRUN -> getGroupPayrunUpdateMessage(myUpdate, item);
                case CASH_ADVANCE -> getCashAdvanceUpdateMessage(myUpdate, item);
                case ATTACHMENT -> getAttachmentUpdateMessage(myUpdate, item);
                case CUSTOM_FORM_ITEM -> getCustomFormItemUpdateMessage(myUpdate, item);
                case ADDITIONAL_PAYMENT -> getAdditionalPaymentUpdateMessage(myUpdate, item);
                case HRMS_PLACEMENT -> getPlacementUpdateMessage(myUpdate, item);
            }

        }
    }

    public void getMyUpdateMessageAllHistory(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage, boolean showAllHistory) { //for Accounting
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        if (item == null || type == null || type.getParent() == null) {
            return;
        }
        if (EdsTrusteeType.USER.equals(myUpdate.getReceiverType())) {
            if (myUpdate.getEventType().equals(WEBHOOK_ADD)) {
                EdsUser user = userManager.get(myUpdate.getReceiver());
                String userName = getUserName(myUpdate, user);

                item.setUserName(userName);
                EdsWebHookResponse response = webHookResponseManager.get(myUpdate.getRelationID());
                if (myUpdate.getStatusCode() != null) {
                    if (myUpdate.getStatusCode().startsWith("20") || myUpdate.getStatusCode().startsWith("30")) {
                        item.setMessage("Webhook succeeded: " + response.getWebHook().getName());
                        item.setSubType(MyUpdateItem.SUCCESS);
                    } else {
                        item.setMessage("Webhook failed: " + response.getWebHook().getName());
                        item.setSubType(MyUpdateItem.FAIL);
                    }
                }
            } else {
                String code = type.getParent().getCode();
                switch (code) {
                    case SALES_INVOICE ->
                            getSalesInvoiceUpdateMessage(myUpdate, item, showAllHistory);   // When user create Invoice, only for him, not for others.
                    case RECURRING_INVOICE -> getRecurringInvoiceUpdateMessage(myUpdate, item);
                    case SALES_QUOTE -> getSaleQuoteUpdateMessage(myUpdate, item);
                    case SALES_ORDER -> getSaleOrderUpdateMessage(myUpdate, item);
                    case PURCHASE_ORDER -> getPurchaseOrderUpdateMessage(myUpdate, item);
                    case PURCHASE_INVOICE -> getPurchaseInvoiceUpdateMessage(myUpdate, item);
                    case FIXED_ASSET -> getFixedAssetUpdateMessage(myUpdate, item);
                    case ACCOUNTING_MANUAL_JOURNAL, MANUAL_JOURNAL_APPLIED ->
                            getManualJournalUpdateMessage(myUpdate, item);
                    case ACCOUNTING_REQUEST_FOR_QUOTE -> getRfqUpdateMessage(myUpdate, item);
                    case ACCOUNTING_REQUEST_FOR_PURCHASE -> getRfpUpdateMessage(myUpdate, item);
                    case ACCOUNTING_STOCK_TRANSFER -> getStockTransferUpdateMessage(myUpdate, item);
                    case ACCOUNTING_STOCK_ADJUSTMENT -> getStockAdjustmentUpdateMessage(myUpdate, item);
                    case EXPENSE_REPORT -> getExpensClaimsUpdateMessage(myUpdate, item);
                    case EXPENSE_PAYMENT -> getExpensePaymentUpdateMessage(myUpdate, item);
                    case BANK_TRANSFER, BANK_TRANSFER_APPLIED -> getBankTransferUpdateMessage(myUpdate, item);
                    case BATCH_PAYMENT -> getBatchPaymentUpdateMessage(myUpdate, item);
                    case INVOICE_PAYMENT -> getInvoicePaymentUpdateMessage(myUpdate, item);
                    case ADDITIONAL_PAYMENT -> getAdditionalPaymentUpdateMessage(myUpdate, item);
                    case HRMS_PLACEMENT -> getPlacementUpdateMessage(myUpdate, item);
                }
            }
        }
    }

    private void getLeadUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsCrmContact lead = crmContactManager.get(myUpdate.getAffectedID());
        if (lead == null) {
            return;
        }
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        if (lead.getContactType().equals(EdsCrmContact.LEAD_CONTACT) || lead.getConvertedFrom().equals(EdsCrmContact.LEAD_CONTACT)) {
            if (LEAD_ADD.equals(type.getCode())) {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(LEAD_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_LEAD_BY, userName, lead.getFirstName() + " " + lead.getLastName()));
            } else if (LEAD_EDIT.equals(type.getCode())) {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(LEAD_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_LEAD_BY, userName, lead.getFirstName() + " " + lead.getLastName()));
            } else if (LEAD_DELETE.equals(type.getCode())) {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(LEAD_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_LEAD_BY, userName, lead.getFirstName() + " " + lead.getLastName()));
            }

        } else if (lead.getContactType().equals(EdsCrmContact.CRM_CONTACT)) {
            if (CONTACT_ADD.equals(type.getCode())) {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(CONTACT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_CONTACT_BY, userName, lead.getFirstName() + " " + lead.getLastName()));
            } else if (CONTACT_EDIT.equals(type.getCode())) {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(CONTACT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_CONTACT_BY, userName, lead.getFirstName() + " " + lead.getLastName()));
            } else if (CONTACT_DELETE.equals(type.getCode())) {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(CONTACT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_CONTACT_BY, userName, lead.getFirstName() + " " + lead.getLastName()));
            }
        }
    }

    private void getSolutionUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsSolution solution = solutionManager.get(myUpdate.getAffectedID());
        if (solution == null) {
            return;
        }
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());

        switch (type.getCode()) {
            case SOLUTION_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(SOLUTION_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_SOLUTION_BY, userName, solution.getTitle()));
            }
            case SOLUTION_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(SOLUTION_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_SOLUTION_BY, userName, solution.getTitle()));
            }
            case SOLUTION_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(SOLUTION_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_SOLUTION_BY, userName, solution.getTitle()));
            }
        }
    }

    private void getCaseUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsCase edsCase = caseManager.get(myUpdate.getAffectedID());
        if (edsCase == null) {
            return;
        }
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());

        switch (type.getCode()) {
            case CASE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(CASE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_CASE_BY, userName, edsCase.getSubject()));
            }
            case CASE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(CASE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_CASE_BY, userName, edsCase.getSubject()));
            }
            case CASE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(CASE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_CASE_BY, userName, edsCase.getSubject()));
            }
        }
    }

    private void getTimeSlotUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsTimeSlot edsTimeSlot = timeSlotManager.get(myUpdate.getAffectedID());
        if (edsTimeSlot == null) {
            return;
        }
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        if (TIMESLOT_ADD.equals(type.getCode())) {
            item.setSubType(MyUpdateItem.ADD);
            item.setTitle(activityWfmMessageSource.localize(TIMESLOT_ADD));
            item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_TIMESLOT_BY, userName, edsTimeSlot.getName()));
        } else if (TIMESLOT_EDIT.equals(type.getCode())) {
            item.setSubType(MyUpdateItem.EDIT);
            item.setTitle(activityWfmMessageSource.localize(TIMESLOT_EDIT));
            item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_TIMESLOT_BY, userName, edsTimeSlot.getName()));
        } else if (TIMESLOT_DELETE.equals(type.getCode())) {
            item.setSubType(MyUpdateItem.DELETE);
            item.setTitle(activityWfmMessageSource.localize(TIMESLOT_DELETE));
            item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_TIMESLOT_BY, edsTimeSlot.getName()));
        }
    }

    private void getHolidayUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsHoliday edsHoliday = holidayManager.get(myUpdate.getAffectedID());
        if (edsHoliday == null) {
            return;
        }
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());

        switch (type.getCode()) {
            case HOLIDAY_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(HOLIDAY_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_HOLIDAY_BY, userName, edsHoliday.getName()));
            }
            case HOLIDAY_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(HOLIDAY_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_HOLIDAY_BY, userName, edsHoliday.getName()));
            }
            case HOLIDAY_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(HOLIDAY_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_HOLIDAY_BY, userName, edsHoliday.getName()));
            }
        }
    }

    private void getPanelSettingsUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        EdsListPanelSettings listPanelSettings = listPanelSettingsManager.get(myUpdate.getAffectedID());
        if (listPanelSettings == null) {
            return;
        }
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        if (PANEL_SETTING_EDIT.equals(type.getCode())) {
            item.setSubType(MyUpdateItem.EDIT);
            item.setTitle(activityWfmMessageSource.localize(PANEL_SETTINGS_EDIT));
            if (broadMessage) {
                item.setMessage(activityWfmMessageSource.localizeWithParam(PANEL_SETTING_EDIT_BY, "SaleInvoiceListPanel".equals(listPanelSettings.getPanelType()) ? "SalesInvoiceListPanel" : listPanelSettings.getPanelType()));
            } else {
                item.setMessage(activityWfmMessageSource.localizeWithParam(PANEL_SETTING_EDIT, "SaleInvoiceListPanel".equals(listPanelSettings.getPanelType()) ? "SalesInvoiceListPanel" : listPanelSettings.getPanelType()));
            }
        }
    }

    private void getWebFormUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsWebForm webForm = webFormManager.get(myUpdate.getAffectedID());
        if (webForm == null) {
            return;
        }
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());

        switch (type.getCode()) {
            case WEB_FORM_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(WEB_FORM_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_WEB_FORM_BY, userName, webForm.getTitle()));
            }
            case WEB_FORM_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(WEB_FORM_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_WEB_FORM_BY, userName, webForm.getTitle()));
            }
            case WEB_FORM_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(WEB_FORM_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_WEB_FORM_BY, userName, webForm.getTitle()));
            }
        }
    }

    private void getAccountUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsCrmAccount account = crmAccountManager.get(myUpdate.getAffectedID());
        if (account == null) {
            return;
        }
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        String accountType = "";
        if (account.isClient()) {
            accountType = activityWfmMessageSource.localize(CRM_CLIENT);
        } else if (account.isSupplier()) {
            accountType = activityWfmMessageSource.localize(CRM_SUPPLIER);
        } else {
            accountType = activityWfmMessageSource.localize(CRM_ACCOUNT);
        }
        String accountName = account.getName() != null ? account.getName() : "";

        switch (type.getCode()) {
            case ACCOUNT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_ACCOUNT_BY, userName, accountName, accountType));
            }
            case ACCOUNT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_ACCOUNT_BY, userName, accountName, accountType));
            }
            case ACCOUNT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_ACCOUNT_BY, userName, accountName, accountType));
            }
        }
    }

    private void getCampaignUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsCampaign campaign = campaignManager.get(myUpdate.getAffectedID());
        if (campaign == null) {
            return;
        }
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        switch (type.getCode()) {
            case CAMPAIGN_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(CAMPAIGN_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_CAMPAIGN_BY, userName, campaign.getName()));
            }
            case CAMPAIGN_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(CAMPAIGN_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_CAMPAIGN_BY, userName, campaign.getName()));
            }
            case CAMPAIGN_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(CAMPAIGN_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_CAMPAIGN_BY, userName, campaign.getName()));
            }
        }
    }

    private void getOpportunityUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsOpportunity opportunity = opportunityManager.get(myUpdate.getAffectedID());
        if (opportunity == null) {
            return;
        }
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        switch (type.getCode()) {
            case OPPORTUNITY_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(OPPORTUNITY_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_OPPORTUNITY_BY, userName, opportunity.getName()));
            }
            case OPPORTUNITY_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(OPPORTUNITY_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_OPPORTUNITY_BY, userName, opportunity.getName()));
            }
            case OPPORTUNITY_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(OPPORTUNITY_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_OPPORTUNITY_BY, userName, opportunity.getName()));
            }
        }
    }

    private void getMailingListUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsMailList mailList = mailListManager.get(myUpdate.getAffectedID());
        if (mailList == null) {
            return;
        }
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        if (MAILING_ADD.equals(type.getCode())) {
            item.setSubType(MyUpdateItem.ADD);
            item.setTitle(activityWfmMessageSource.localize(MAILING_ADD));
            item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_MAILING_BY, userName, mailList.getName()));
        } else if (MAILING_EDIT.equals(type.getCode())) {
            item.setSubType(MyUpdateItem.EDIT);
            item.setTitle(activityWfmMessageSource.localize(MAILING_EDIT));
            item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_MAILING_BY, userName, mailList.getName()));
        }
    }

    private void getContactCategoryUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsContactCategory category = contactCategoryManager.get(myUpdate.getAffectedID());
        if (category == null) {
            return;
        }
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        if (CATEGORY_ADD.equals(type.getCode())) {
            item.setSubType(MyUpdateItem.ADD);
            item.setTitle(activityWfmMessageSource.localize(CATEGORY_ADD));
            item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_CATEGORY_BY, userName, category.getName()));
        } else if (CATEGORY_EDIT.equals(type.getCode())) {
            item.setSubType(MyUpdateItem.EDIT);
            item.setTitle(activityWfmMessageSource.localize(CATEGORY_EDIT));
            item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_CATEGORY_BY, userName, category.getName()));
        }
    }

    public void getMyNetworkUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        EdsMyUpdateType type = getMyUpdateTypeByCode(myUpdate.getTypeCode());
        if (EdsTrusteeType.USER.equals(myUpdate.getReceiverType())) {
            if (Constants.NETWORK_ADD.equals(type.getCode()) || Constants.NETWORK_EDIT.equals(type.getCode()) || Constants.NETWORK_DELETE.equals(type.getCode())) {
                getNetworkUpdateMessage(myUpdate, item, broadMessage);
            } else if (Constants.NETWORK_BLOG_ADD.equals(type.getCode()) || Constants.NETWORK_BLOG_EDIT.equals(type.getCode()) || Constants.NETWORK_BLOG_DELETE.equals(type.getCode())) {
                getNetworkBlogUpdateMessage(myUpdate, item, broadMessage);
            } else if (Constants.NETWORK_CONTACT_ADD.equals(type.getCode()) || Constants.NETWORK_CONTACT_DELETE.equals(type.getCode())) {
                getNetworkContactUpdateMessage(myUpdate, item, broadMessage);
            } else if (Constants.NETWORK_BLOG_COMMENTED.equals(type.getCode()) || Constants.NETWORK_BLOG_RATED.equals(type.getCode())) {
                getNetworkComment(myUpdate, item, broadMessage);
            } else if (Constants.NETWORK_CONTACT_JOIN.equals(type.getCode())) {
                getNetworkJoinUpdateMessage(myUpdate, item, broadMessage);
            } else if (Constants.NETWORK_EDIT_CONFIRM.equals(type.getCode())) {
                getNetworkConfirmOrRejectMessage(myUpdate, item, broadMessage);
            }
        }
        if (WORKSPACE_NOTE.equals(type.getParent() != null ? type.getParent().getCode() : "")) {
            getNoteUpdateMessage(myUpdate, item);
        }
    }

    private void getNoteUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsNoteHistory note = noteHistoryManager.get(myUpdate.getAffectedID());
        String subject = "";
        String creator = "";
        if (note != null) {
            subject = note.getSubject();
            creator = getUserName(myUpdate, note.getEmployee());
        }

        switch (typeCode) {
            case WORKSPACE_NOTE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(WORKSPACE_NOTE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_NOTE, creator, subject));
            }
            case WORKSPACE_NOTE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(WORKSPACE_NOTE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_NOTE, creator, subject));
            }
            case WORKSPACE_NOTE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(WORKSPACE_NOTE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_NOTE, creator, subject));
            }
            default -> {
                item.setSubType(MyUpdateItem.NOTE);
                item.setTitle(activityWfmMessageSource.localize(WORKSPACE_NOTE));
            }
        }
    }

    private void getContactNoteUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsCrmContact contact = crmContactManager.get(myUpdate.getAffectedID());
        String creator = "";
        String contactName = "";
        if (contact != null) {
            contactName = contact.getFullName();
            creator = getUserName(myUpdate, contact.getCreator());
        }

        switch (typeCode) {
            case CONTACT_NOTE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(CONTACT_NOTE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_CONTACT_NOTE, creator, contactName));
            }
            case CONTACT_NOTE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(CONTACT_NOTE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_CONTACT_NOTE, creator, contactName));
            }
            case CONTACT_NOTE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(CONTACT_NOTE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_CONTACT_NOTE, creator, contactName));
            }
            default -> {
                item.setSubType(MyUpdateItem.NOTE);
                item.setTitle(activityWfmMessageSource.localize(CONTACT_NOTE));
            }
        }
    }

    private void getLeadNoteUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsCrmContact contact = crmContactManager.get(myUpdate.getAffectedID());

        String creator = "";
        String contactName = "";
        if (contact != null) {
            contactName = contact.getFullName();
            creator = getUserName(myUpdate, contact.getCreator());
        }

        switch (typeCode) {
            case LEAD_NOTE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(LEAD_NOTE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_LEAD_NOTE, creator, contactName));
            }
            case LEAD_NOTE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(LEAD_NOTE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_LEAD_NOTE, creator, contactName));
            }
            case LEAD_NOTE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(LEAD_NOTE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_LEAD_NOTE, creator, contactName));
            }
            default -> {
                item.setSubType(MyUpdateItem.NOTE);
                item.setTitle(activityWfmMessageSource.localize(LEAD_NOTE));
            }
        }
    }

    private void getTimesheetUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        String typeCode = myUpdate.getTypeCode();
        EdsTimeSheetApprovalSession timeSheetApprovalSession = timeSheetApprovalSessionManager.get(myUpdate.getAffectedID());
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case TIMESHEET_STATUS_WAITING -> {
                item.setSubType(MyUpdateItem.STATUS_WAITING);
                item.setTitle(activityWfmMessageSource.localize(TIMESHEET_STATUS_WAITING));
                item.setMessage(activityWfmMessageSource.localize(SUBMIT_TIMESHEET_FOR_APPROVAL, userName));
            }
            case TIMESHEET_STATUS_WAITING_FOR_MANAGER -> {
                item.setSubType(MyUpdateItem.STATUS_WAITING);/*(after if has link, changed) Not link  -  link*/
                item.setTitle(activityWfmMessageSource.localize(TIMESHEET_STATUS_WAITING_FOR_MANAGER));
                item.setMessage(timeSheetApprovalSession.getEmployee().getName() + " " + activityWfmMessageSource.localize(SUBMITED_TIMESHEET_ENTRIES_FOR_YOUR_APPROVAL, userName));
            }
            case TIMESHEET_STATUS_APPROVED -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(TIMESHEET_STATUS_APPROVED));
                if (broadMessage) {              /*send manager*/
                    item.setMessage(activityWfmMessageSource.localizeWithParam(REVIEWED, userName, timeSheetApprovalSession.getEmployee().getName(), timeSheetApprovalSession.getProject().getName()));
                } else {/*Timesheet Approval List  -  link*/
                    item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_APPROVED, userName, timeSheetApprovalSession.getEmployee().getName()));
                }
            }
            case TIMESHEET_STATUS_REJECTED -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(TIMESHEET_STATUS_REJECTED));
                if (broadMessage) {              /*send manager*/
                    item.setMessage(activityWfmMessageSource.localizeWithParam(REVIEWED, userName, timeSheetApprovalSession.getEmployee().getName(), timeSheetApprovalSession.getProject().getName()));
                } else {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_REJECTED, userName, timeSheetApprovalSession.getEmployee().getName()));
                }
            }
            case TIMESHEET_STATUS_APPROVED_FOR_EMPLOYEE -> {/*send employee*/
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(TIMESHEET_STATUS_APPROVED_FOR_EMPLOYEE));
                EdsUser inducer = userManager.get(myUpdate.getInducerID());

                if (user != null && inducer.getObjectID().equals(user.getObjectID())) {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_APPROVED_YOUR_TIMESHEET_ENTRIES, userName, userName));
                } else {/*Timesheet Approval List  -  link*/ /*Not link -> Timesheet List*/
                    item.setMessage(activityWfmMessageSource.localizeWithParam(YOUR_TIMESHEET_ENTRIES_HAVE_BEEN_APPROVED_BY, userName, inducer.getName()));
                }
            }
            case TIMESHEET_STATUS_REJECTED_FOR_EMPLOYEE -> {/*send employee*/
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(TIMESHEET_STATUS_REJECTED_FOR_EMPLOYEE));
                EdsUser inducer = userManager.get(myUpdate.getInducerID());
                user = userManager.get(myUpdate.getReceiver());
                if (inducer.getObjectID().equals(user.getObjectID())) {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_REJECTED_YOUR, userName, userName));
                } else {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(YOUR_TIMESHEET_ENTRIES_HAVE_BEEN_REJECTED_BY, userName, inducer.getName()));
                }
            }
        }
    }

    private void getLocationUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsLocation location = locationManager.get(myUpdate.getAffectedID());
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case LOCATION_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(LOCATION_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_LOCATION, userName, location.getCity()));
            }
            case LOCATION_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(LOCATION_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_LOCATION, userName, location.getCity()));
            }
            case LOCATION_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(LOCATION_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETE_LOCATION, userName, location.getCity()));
            }
        }
    }

    private void getSalesInvoiceUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean showAllHistory) {

        String typeCode = myUpdate.getTypeCode();
        EdsBaseInvoice invoice = invoiceManager.get(myUpdate.getAffectedID());

        String clientName = "";
        String invoiceNumber = "";
        String invoiceTotal = "";
        String totalHistory = null;
        String clientOrManagerName = "";
        if (invoice == null) {
            invoiceNumber = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (invoiceNumber == null) {
                return;
            }
        } else {
            invoiceNumber = invoice.getNumber() != null ? invoice.getNumber() : "";
            invoiceTotal = invoice.getTotal() != null ? numberFormat.format(invoice.getTotal()) : "";
            totalHistory = myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : invoiceTotal;
            switch (typeCode) {
                case SALES_INVOICE_MANAGER_APPROVE, SALES_INVOICE_MANAGER_REJECT ->
                        clientOrManagerName = getUserName(myUpdate, userManager.get(myUpdate.getInducerID()));
                case SALES_INVOICE_SUBMITTED_TO_MANAGER -> clientOrManagerName = invoice.getCurrentApprover() != null &&
                        invoice.getCurrentApprover().getExactEmployee() != null
                        ? getUserName(myUpdate, invoice.getCurrentApprover().getExactEmployee())
                        : "";
                default -> {
                }
            }
        }

        EdsInvoicePayment invoicePayment = invoicePaymentManager.get(myUpdate.getRelationID());
        String paymentAmount = invoicePayment != null ? numberFormat.format(invoicePayment.getAmount()) : "";
        String paymentAmountHistory = myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : paymentAmount;
        if (invoice != null && invoice.getClientOrSupplier() != null && invoice.getClientOrSupplier().getName() != null) {
            clientName = invoice.getClientOrSupplier().getName();
        }

        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);

        item.setUserName(userName);
        switch (typeCode) {
            case SALES_INVOICE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_ADD));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(ADDED) + " " + invoiceNumber + " " + activityWfmMessageSource.localize(OF_AMOUNT) + " " + totalHistory);
            }
            case SALES_INVOICE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_EDIT));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(EDITED) + " " + invoiceNumber + " " + activityWfmMessageSource.localize(OF_AMOUNT) + " " + totalHistory);
            }
            case SALES_INVOICE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_DELETE));
                if (!showAllHistory) {
                    item.setMessage(userName + " " + activityWfmMessageSource.localize(DELETED) + " " + invoiceNumber + " " + activityWfmMessageSource.localize(OF_AMOUNT) + " " + totalHistory);
                }
            }
            case SALES_INVOICE_SEND_TO_CLIENT -> {
                item.setSubType(MyUpdateItem.STATUS_SENT);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_SEND_TO_CLIENT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SENT_SALES_INVOICE, userName, invoiceNumber, clientName));
            }
            case SALES_INVOICE_PAYMENT_RECEIVE -> {
                item.setSubType(MyUpdateItem.STATUS_RECEIVED);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_PAYMENT_RECEIVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(PAYMENT_RECEIVED, paymentAmountHistory, userName));
            }
            case SALES_INVOICE_CONVERTED_FROM_SALES_QUOTE -> {
                item.setSubType(MyUpdateItem.CONVERTED);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_CONVERTED_FROM_SALES_QUOTE));
                String quoteNumber = (myUpdate.getItemName() != null) ? myUpdate.getItemName() : "";
                item.setMessage(activityWfmMessageSource.localizeWithParam(BY_CONVERTING_SALES_QUOTE, userName, invoiceNumber, quoteNumber));
            }
            case SALES_INVOICE_PAYMENT_VOID -> {
                item.setSubType(MyUpdateItem.STATUS_WAITING);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_PAYMENT_VOID));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SALES_INVOICE_PAYMENT_VOID, userName, invoiceNumber));
            }
            case SALES_INVOICE_PAYMENT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_PAYMENT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SALES_INVOICE_PAYMENT_DELETE, userName, paymentAmountHistory));
            }
            case SALES_INVOICE_ADD_CREDIT_NOTE -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_ADD_CREDIT_NOTE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_HAVE_SALES_INVOICE_ADD_CREDIT_NOTE, userName, invoiceNumber));
            }
            case SALES_INVOICE_REFUND -> {
                item.setSubType(MyUpdateItem.STATUS_REFUNDED);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_REFUND));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_SALES_INVOICE_ADD_CREDIT_NOTE, invoiceNumber, paymentAmountHistory));
            }
            case SALES_INVOICE_SUBMITTED_TO_MANAGER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_SUBMITTED_TO_MANAGER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SUBMIT_SALES_INVOICE_TO_MANAGER, userName, invoiceNumber, clientOrManagerName));
            }
            case SALES_INVOICE_MANAGER_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_MANAGER_APPROVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(CLIENT_APPROVED_SALES_INVOICE, clientOrManagerName, invoiceNumber));
            }
            case SALES_INVOICE_MANAGER_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_MANAGER_REJECT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(REJECTED_SALES_INVOICE, clientOrManagerName, invoiceNumber));
            }
        }
    }

    private void getAdditionalPaymentUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(myUpdate.getAffectedID());
        if (additionalPayment == null) {
            return;
        }

//        String appruver = additionalPayment.getCurrentApprover() != null &&
//                additionalPayment.getCurrentApprover().getExactEmployee() != null
//                ? getUserName(myUpdate, additionalPayment.getCurrentApprover().getExactEmployee()) : "";


        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);
        item.setUserName(userName);
        switch (typeCode) {
            case ADDITIONAL_PAYMENT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ADDITIONAL_PAYMENT_ADD));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(ADDED_ADDITIONAL_PAYMENT));
            }
            case ADDITIONAL_PAYMENT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ADDITIONAL_PAYMENT_EDIT));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(EDITED_ADDITIONAL_PAYMENT));
            }
            case ADDITIONAL_PAYMENT_REJECTED -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(ADDITIONAL_PAYMENT_REJECTED));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(REJECTED_ADDITIONAL_PAYMENT));
            }
            case ADDITIONAL_PAYMENT_APPROVED -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(ADDITIONAL_PAYMENT_APPROVED));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(APPROVED_ADDITIONAL_PAYMENT));
            }
            case ADDITIONAL_PAYMENT_SUBMITTED_TO_MANAGER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(ADDITIONAL_PAYMENT_SUBMITTED_TO_MANAGER));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(SUBMITTED_TO_MANAGER_ADDITIONAL_PAYMENT));
            }
            case ADDITIONAL_PAYMENT_DRAFT -> {
                item.setSubType(MyUpdateItem.STATUS_DRAFT);
                item.setTitle(activityWfmMessageSource.localize(ADDITIONAL_PAYMENT_DRAFT));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(DRAFT_ADDITIONAL_PAYMENT));
            }
        }
    }

    private void getPlacementUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsPlacement placement = placementManager.get(myUpdate.getAffectedID());
        if (placement == null) {
            return;
        }

        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);
        item.setUserName(userName);
        switch (typeCode) {
            case HRMS_PLACEMENT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(HRMS_PLACEMENT_ADD));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(ADDED_PLACEMENT));
            }
            case HRMS_PLACEMENT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(HRMS_PLACEMENT_EDIT));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(EDITED_PLACEMENT));
            }
            case HRMS_PLACEMENT_DECLINE -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(HRMS_PLACEMENT_DECLINE));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(REJECTED_PLACEMENT));
            }
            case HRMS_PLACEMENT_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(HRMS_PLACEMENT_APPROVE));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(APPROVED_PLACEMENT));
            }
            case HRMS_PLACEMENT_SEND_TO_APPROVER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(HRMS_PLACEMENT_SEND_TO_APPROVER));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(SUBMITTED_TO_MANAGER_PLACEMENT));
            }
        }
    }

    private void getRecurringInvoiceUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {

        String typeCode = myUpdate.getTypeCode();
        EdsBaseInvoice invoice = invoiceManager.get(myUpdate.getAffectedID());

        String invoiceNumber = "";
        String invoiceTotal = "";
        String invoiceTotalHistory = null;
        String invoiceCur = "";
        if (invoice == null) {
            invoiceNumber = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (invoiceNumber == null) {
                return;
            }
        } else {
            invoiceNumber = invoice.getNumber() != null ? invoice.getNumber() : "";
            invoiceTotal = invoice.getTotal() != null ? numberFormat.format(invoice.getTotal()) : "";
            invoiceTotalHistory = myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : invoiceTotal;
            invoiceCur = invoice.getCurrency() != null && invoice.getCurrency().getName() != null ? invoice.getCurrency().getName() : "";
        }

        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case RECURRING_INVOICE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(RECURRING_INVOICE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_RECURRING_INVOICE, userName, invoiceNumber, invoiceTotalHistory, invoiceCur));
            }
            case RECURRING_INVOICE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(RECURRING_INVOICE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_RECURRING_INVOICE, userName, invoiceNumber, invoiceTotalHistory, invoiceCur));
            }
            case RECURRING_INVOICE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(RECURRING_INVOICE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_RECURRING_INVOICE, userName, invoiceNumber, invoiceTotalHistory, invoiceCur));
            }
        }
    }

    private void getManualJournalUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsManualJournal manualJournal = manualJournalManager.get(myUpdate.getAffectedID());
        String journalNumber = manualJournal != null ? manualJournal.getNumber() : "";
        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);
        EdsInvoicePayment invoicePayment = null;
        EdsInvoice invoice = null;
        if (MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE.equals(typeCode)) {
            invoicePayment = invoicePaymentManager.get(myUpdate.getAffectedID());
            invoice = invoiceManager.get(invoicePayment.getInvoice().getObjectID());
        }

        switch (typeCode) {
            case ACCOUNTING_MANUAL_JOURNAL_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_MANUAL_JOURNAL_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_MANUAL_JOURNAL, userName, journalNumber));
            }
            case ACCOUNTING_MANUAL_JOURNAL_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_MANUAL_JOURNAL_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_EDITED_MANUAL_JOURNAL, userName, journalNumber));
            }
            case ACCOUNTING_MANUAL_JOURNAL_VOID -> {
                item.setSubType(MyUpdateItem.STATUS_WAITING);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_MANUAL_JOURNAL_VOID));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_REVERSED_MANUAL_JOURNAL, userName, journalNumber));
            }
            case ACCOUNTING_MANUAL_JOURNAL_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_MANUAL_JOURNAL_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_MANUAL_JOURNAL, userName, journalNumber));
            }
            case MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE -> {
                BigDecimal amount = myUpdate.getAmount() != null ? myUpdate.getAmount() : invoicePayment != null && invoicePayment.getAmount() != null ? invoicePayment.getAmount() : BigDecimal.ZERO;
                String paymentTotal = invoicePayment != null ? invoicePayment.isDeleted() ?
                        amount.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP) + " (Deleted Payment) " :
                        amount.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP).toString() : "";
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE_MESSAGE, invoice != null ? invoice.getNumber() : "", userName, paymentTotal));
            }
        }
    }

    private void getRfqUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsRFQ rfq = rfqManager.get(myUpdate.getAffectedID());
        String journalNumber = rfq != null ? rfq.getNumber() : "";
        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case ACCOUNTING_REQUEST_FOR_QUOTE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_REQUEST_FOR_QUOTE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_RFQ, userName, journalNumber));
            }
            case ACCOUNTING_REQUEST_FOR_QUOTE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_REQUEST_FOR_QUOTE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_EDITED_RFQ, userName, journalNumber));
            }
            case ACCOUNTING_REQUEST_FOR_QUOTE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_REQUEST_FOR_QUOTE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_EDITED_RFQ, userName, journalNumber));
            }
        }
    }

    private void getRfpUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsRFP rfp = rfpManager.get(myUpdate.getAffectedID());
        String rfpNumber = rfp != null ? rfp.getNumber() : "";
        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);
        String clientOrManagerName = "";

        if (rfp != null) {
            switch (typeCode) {
                case RFP_MANAGER_APPROVE, RFP_MANAGER_REJECT ->
                        clientOrManagerName = getUserName(myUpdate, userManager.get(myUpdate.getInducerID()));
                case RFP_SUBMITTED_TO_MANAGER -> clientOrManagerName = rfp.getCurrentApprover() != null &&
                        rfp.getCurrentApprover().getExactEmployee() != null
                        ? getUserName(myUpdate, rfp.getCurrentApprover().getExactEmployee())
                        : "";
                default -> {
                }
            }
        }


        switch (typeCode) {
            case ACCOUNTING_REQUEST_FOR_PURCHASE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_REQUEST_FOR_PURCHASE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_RFP, userName, rfpNumber));
            }
            case ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_EDITED_RFP, userName, rfpNumber));
            }
            case ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_EDITED_RFP, userName, rfpNumber));
            }
            case RFP_SUBMITTED_TO_MANAGER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(RFP_SUBMITTED_TO_MANAGER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SUBMIT_RFP_TO_MANAGER, userName, rfpNumber, clientOrManagerName));
            }
            case RFP_MANAGER_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(RFP_MANAGER_APPROVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(CLIENT_APPROVED_RFP, clientOrManagerName, rfpNumber));
            }
            case RFP_MANAGER_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(RFP_MANAGER_REJECT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(REJECTED_RFP, clientOrManagerName, rfpNumber));
            }
        }
    }

    private void getStockTransferUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsStockTransfer transfer = stockTransferManager.get(myUpdate.getAffectedID());
        String journalNumber = transfer != null ? transfer.getNumber() : "";
        EdsUser user = userManager.get(myUpdate.getReceiver());
        EdsUser currentUser = userManager.getUser();
        String userName = getUserName(myUpdate, user);
        String transferTitle = "";
        String reporterName = userManager.get(myUpdate.getInducerID()).getFullName();
        if (transfer == null) {
            transferTitle = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (transferTitle == null) {
                return;
            }
        } else {
            transferTitle = transfer.getTransferName() != null ? transfer.getTransferName() : "";
        }

        switch (typeCode) {
            case ACCOUNTING_STOCK_TRANSFER_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_TRANSFER_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_STOCK_TRANSFER, userName, journalNumber));
            }
            case ACCOUNTING_STOCK_TRANSFER_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_TRANSFER_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_EDITED_STOCK_TRANSFER, userName, journalNumber));
            }
            case ACCOUNTING_STOCK_TRANSFER_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_TRANSFER_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_STOCK_TRANSFER, userName, journalNumber));
            }
            case ACCOUNTING_STOCK_TRANSFER_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_TRANSFER_APPROVE));
                if (currentUser.getObjectID() == myUpdate.getReceiver()) {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(
                            YOU_HAVE_APPROVED_STOCK_TRANSFER, journalNumber, transferTitle));
                } else {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(
                            ACCOUNTING_STOCK_TRANSFER_HAS_BEEN_APPROVED,
                            userManager.get(myUpdate.getReceiver()).getFullName(), journalNumber, transferTitle));
                }
            }
            case ACCOUNTING_STOCK_TRANSFER_DECLINE -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_TRANSFER_DECLINE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(
                        ACCOUNTING_STOCK_TRANSFER_HAS_BEEN_DECLINED, journalNumber, transferTitle));
            }
            case ACCOUNTING_STOCK_TRANSFER_SEND_TO_APPROVER -> {
                item.setSubType(MyUpdateItem.STATUS_SENT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_TRANSFER_SEND_TO_APPROVER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(
                        YOU_HAVE_SENT_ACCOUNTING_STOCK_TRANSFER_TO,
                        reporterName, journalNumber, transferTitle, userManager.get(myUpdate.getReceiver()).getFullName()));
            }
            case ACCOUNTING_STOCK_TRANSFER_TRANSFERRED -> {
                item.setSubType(MyUpdateItem.STATUS_SENT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_TRANSFER_TRANSFERRED));
                if (currentUser.getObjectID() == myUpdate.getReceiver()) {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(
                            YOU_TRANSFERED_STOCK_TRANSFERD, journalNumber, transferTitle));
                } else {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(
                            ACCOUNTING_STOCK_TRANSFER_HAS_BEEN_TRANSFERRED,
                            userManager.get(myUpdate.getReceiver()).getFullName(), journalNumber, transferTitle));
                }
            }
        }
    }

    private void getStockAdjustmentUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsStockAdjustment adjustment = stockAdjustmentManager.get(myUpdate.getAffectedID());
        String journalNumber = adjustment != null ? adjustment.getNumber() : "";
        EdsUser user = userManager.get(myUpdate.getReceiver());
        EdsUser currentUser = userManager.getUser();
        String userName = getUserName(myUpdate, user);

        String transferTitle = "";
        String reporterName = userManager.get(myUpdate.getInducerID()).getFullName();
        if (adjustment == null) {
            transferTitle = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (transferTitle == null) {
                return;
            }
        } else {
            transferTitle = journalNumber;
        }

        switch (typeCode) {
            case ACCOUNTING_STOCK_ADJUSTMENT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_ADJUSTMENT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_STOCK_ADJUSTMENT, userName, journalNumber));
            }
            case ACCOUNTING_STOCK_ADJUSTMENT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_ADJUSTMENT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_EDITED_STOCK_ADJUSTMENT, userName, journalNumber));
            }
            case ACCOUNTING_STOCK_ADJUSTMENT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_ADJUSTMENT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_STOCK_ADJUSTMENT, userName, journalNumber));
            }
            case ACCOUNTING_STOCK_ADJUSTMENT_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_ADJUSTMENT_APPROVE));
                if (currentUser.getObjectID() == myUpdate.getReceiver()) {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(
                            YOU_HAVE_APPROVED_STOCK_ADJUSTMENT, journalNumber, transferTitle));
                } else {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(
                            ACCOUNTING_STOCK_ADJUSTMENT_HAS_BEEN_APPROVED,
                            userManager.get(myUpdate.getReceiver()).getFullName(), journalNumber, transferTitle));
                }
            }
            case ACCOUNTING_STOCK_ADJUSTMENT_DECLINE -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_ADJUSTMENT_DECLINE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(
                        ACCOUNTING_STOCK_ADJUSTMENT_HAS_BEEN_DECLINED, journalNumber, transferTitle));
            }
            case ACCOUNTING_STOCK_ADJUSTMENT_SEND_TO_APPROVER -> {
                item.setSubType(MyUpdateItem.STATUS_SENT);
                item.setTitle(activityWfmMessageSource.localize(ACCOUNTING_STOCK_ADJUSTMENT_SEND_TO_APPROVER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(
                        YOU_HAVE_SENT_ACCOUNTING_STOCK_ADJUSTMENT_TO,
                        reporterName, journalNumber, transferTitle, userManager.get(myUpdate.getReceiver()).getFullName()));
            }
        }
    }

    private void getExpensClaimsUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsExpenseReport expenseReport = reportManager.getExpenseReport(myUpdate.getAffectedID());
        String expenseNumber = expenseReport != null ? expenseReport.getNumber() : "";

        String expenseReportTitle = "";
        String reporterName = userManager.get(myUpdate.getInducerID()).getFullName();
        if (expenseReport == null) {
            expenseReportTitle = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (expenseReportTitle == null) {
                return;
            }
        } else {
            expenseReportTitle = expenseReport.getTitle() != null ? expenseReport.getTitle() : "";
        }

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        String totalHistory = "";
        String total = "";
        total = expenseReport.getBaseTotal() != null ? numberFormat.format(expenseReport.getBaseTotal()) : "";
        totalHistory = myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : total;

        if (EXPENSE_REPORT_ADD.equals(typeCode)) {
            item.setSubType(MyUpdateItem.ADD);
            item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_ADD));
            item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_EXPENSE_REPORT, userName, expenseNumber, expenseReportTitle, totalHistory));
        } else if (EXPENSE_REPORT_EDIT.equals(typeCode)) {
            item.setSubType(MyUpdateItem.EDIT);
            item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_EDIT));
            item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_EXPENSE_REPORT, userName, expenseNumber, expenseReportTitle, totalHistory));
        } else if (EXPENSE_REPORT_APPROVE.equals(typeCode)) {
            item.setSubType(MyUpdateItem.STATUS_APPROVED);
            item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_APPROVE));
            item.setMessage(activityWfmMessageSource.localizeWithParam(EXPENSE_REPORT_HAS_BEEN_APPROVED, userManager.get(myUpdate.getReceiver()).getFullName(), expenseNumber, expenseReportTitle));
        } else if (EXPENSE_REPORT_DECLINE.equals(typeCode)) {
            item.setSubType(MyUpdateItem.STATUS_REJECT);
            item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_DECLINE));
            item.setMessage(activityWfmMessageSource.localizeWithParam(EXPENSE_REPORT_HAS_BEEN_DECLINED, expenseNumber, expenseReportTitle));
        } else if (EXPENSE_REPORT_GET_FROM_REPORTER.equals(typeCode)) {
            item.setSubType(MyUpdateItem.STATUS_SUBMITED);
            item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_GET_FROM_REPORTER));
            item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SENT_EXPENSE_REPORT_FOR_YOUR_REVIEW, reporterName, expenseNumber, expenseReportTitle));
        } else if (EXPENSE_REPORT_SEND_TO_APPROVER.equals(typeCode)) {
            item.setSubType(MyUpdateItem.STATUS_SENT);
            item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_SEND_TO_APPROVER));
            item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_SENT_EXPENSE_REPORT_TO, userName, expenseNumber, expenseReportTitle, userManager.get(myUpdate.getReceiver()).getFullName()));
        }
    }

    private void getExpensePaymentUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        if (EXPENSE_PAYMENT_ADD.equalsIgnoreCase(typeCode)) {
            EdsExpensePayment expensePayment = expensePaymentManager.get(myUpdate.getAffectedID());
            BigDecimal amount = myUpdate.getAmount() != null ? myUpdate.getAmount() : expensePayment.getAmount();
            String reporterName = getUserName(myUpdate, userManager.get(myUpdate.getInducerID()));
            item.setMessage("<b>" + reporterName + "</b> added payment with amount of " + "<b>" + ServerUtils.decimalPrecision(amount, 2) + "</b>");
        }
    }

    private void getBankTransferUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsBankTransfer bankTransfer = spendReceiveMoneyManager.get(myUpdate.getAffectedID());
        EdsInvoicePayment invoicePayment = null;
        EdsInvoice invoice = null;
        if (BANK_TRANSFER_APPLIED_PAYABLE.equals(typeCode)) {
            invoicePayment = invoicePaymentManager.get(myUpdate.getAffectedID());
            invoice = invoiceManager.get(invoicePayment.getInvoice().getObjectID());

        } else if (BANK_TRANSFER_APPLIED_RECEIVABLE.equals(typeCode)) {
            invoicePayment = invoicePaymentManager.get(myUpdate.getAffectedID());
            invoice = invoiceManager.get(invoicePayment.getInvoice().getObjectID());

        }
        String itemName = "bank transfer";
        Integer transferType = null;
        if (bankTransfer != null && bankTransfer.getTransferType() != null) {
            transferType = bankTransfer.getTransferType();
        }
        if (transferType != null) {
            if (AccountingConstants.RECEIVE_MONEY.equals(transferType)) {
                itemName = "bank receipt";
            } else if (AccountingConstants.SPEND_MONEY.equals(transferType)) {
                itemName = "bank payment";
            } else if (AccountingConstants.CASH_RECEIPT.equals(transferType)) {
                itemName = "cash receipt";
            } else if (AccountingConstants.CASH_PAYMENT.equals(transferType)) {
                itemName = "cash payment";
            }
        }

        String bankTransferName = bankTransfer != null && bankTransfer.getName() != null ? bankTransfer.getName() : "";
        String bankTransferNo = bankTransfer != null && bankTransfer.getNumber() != null ? bankTransfer.getNumber() : "";
        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case BANK_TRANSFER_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(BANK_TRANSFER_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_BANK_TRANSFER, userName, itemName, bankTransferNo, bankTransferName));
            }
            case BANK_TRANSFER_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(BANK_TRANSFER_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_BANK_TRANSFER, userName, itemName, bankTransferNo, bankTransferName));
            }
            case BANK_TRANSFER_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(BANK_TRANSFER_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_BANK_TRANSFER, userName, itemName, bankTransferNo, bankTransferName));
            }
            case BANK_TRANSFER_APPLIED_PAYABLE -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(BANK_TRANSFER_APPLIED_PAYABLE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(BANK_TRANSFER_APPLIED_PAYABLE_MESSAGE, invoice != null ? invoice.getNumber() : "", userName, invoicePayment != null ? invoicePayment.getAmount().setScale(2).toString() : ""));
            }
            case BANK_TRANSFER_APPLIED_RECEIVABLE -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(BANK_TRANSFER_APPLIED_RECEIVABLE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(BANK_TRANSFER_APPLIED_RECEIVABLE_MESSAGE, invoice != null ? invoice.getNumber() : "", userName, invoicePayment != null ? invoicePayment.getAmount().setScale(2, RoundingMode.HALF_UP).toString() : ""));
            }
        }
    }

    private void getBatchPaymentUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsBatchPayment batchPayment = batchPaymentManager.get(myUpdate.getAffectedID());
        EdsUser reporter = userManager.get(myUpdate.getInducerID());
        String reporterName = getUserName(myUpdate, reporter);

        String itemName = "payment ";
        String paymentAmount = "";
        String paymentNumber = "";
        if (batchPayment != null) {
            paymentAmount = batchPayment.getTotalAmount() != null ? numberFormat.format(batchPayment.getTotalAmount()) : "0.00";
            paymentNumber = batchPayment.getNumber() != null ? batchPayment.getNumber() : "";
        }

        switch (typeCode) {
            case BATCH_PAYMENT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(INVOICE_PAYMENT_ADD));
                item.setMessage(reporterName + " " + activityWfmMessageSource.localize(ADDED) + " " + itemName +
                        activityWfmMessageSource.localize(OF_AMOUNT) + " " + paymentAmount);
            }
            case BATCH_PAYMENT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(INVOICE_PAYMENT_EDIT));
                item.setMessage(reporterName + "  " + activityWfmMessageSource.localize(EDITED) + " " + itemName + " " + paymentNumber);
            }
            case BATCH_PAYMENT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(INVOICE_PAYMENT_DELETE));
                item.setMessage(reporterName + " " + activityWfmMessageSource.localize(DELETED) + " " + itemName + " " + paymentNumber);
            }
            case BATCH_PAYMENT_VOID -> {
                item.setSubType(MyUpdateItem.STATUS_CANCELLED);
                item.setTitle(activityWfmMessageSource.localize(BATCH_PAYMENT_VOID));
                item.setMessage(reporterName + " " + activityWfmMessageSource.localize(VOIDED) + " " + itemName + " " + paymentNumber);
            }
        }
    }

    private void getSinglePayrunUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsPayslipTableItem singPayrun = payslipTableItemManager.get(myUpdate.getAffectedID());
        if (singPayrun == null) {
            return;
        }

        EdsUser creator = userManager.get(myUpdate.getInducerID());
        String creatorName = getUserName(myUpdate, creator);

        String total = singPayrun.getTotal() != null ? numberFormat.format(singPayrun.getTotal()) : "";
        String month = singPayrun.getMonth() != null ? singPayrun.getMonth() : "";
        String year = singPayrun.getYear() != null ? singPayrun.getYear().toString() : "";
        String currencyName = singPayrun.getCurrency() != null && singPayrun.getCurrency().getName() != null ?
                singPayrun.getCurrency().getName() :
                "";
        String employeeName = singPayrun.getEmployee() != null && singPayrun.getEmployee().getFullName() != null ?
                singPayrun.getEmployee().getFullName() :
                "";

        switch (typeCode) {
            case SINGLE_PAYRUN_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(SINGLE_PAYRUN_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_SINGLE_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case SINGLE_PAYRUN_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(SINGLE_PAYRUN_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_SINGLE_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case SINGLE_PAYRUN_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(SINGLE_PAYRUN_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_SINGLE_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case SINGLE_PAYRUN_SUBMIT -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(SINGLE_PAYRUN_SUBMIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(SUBMITTED_SINGLE_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case SINGLE_PAYRUN_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(SINGLE_PAYRUN_APPROVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(APPROVED_SINGLE_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case SINGLE_PAYRUN_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(SINGLE_PAYRUN_REJECT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(REJECTED_SINGLE_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
        }

    }

    private void getGroupPayrunUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsPayslipTableItem groupPayrun = payslipTableItemManager.get(myUpdate.getAffectedID());
        if (groupPayrun == null) {
            return;
        }

        EdsUser creator = userManager.get(myUpdate.getInducerID());
        String creatorName = getUserName(myUpdate, creator);

        String total = groupPayrun.getTotal() != null ? numberFormat.format(groupPayrun.getTotal()) : "";
        String month = groupPayrun.getMonth() != null ? groupPayrun.getMonth() : "";
        String year = groupPayrun.getYear() != null ? groupPayrun.getYear().toString() : "";
        String currencyName = groupPayrun.getCurrency() != null && groupPayrun.getCurrency().getName() != null ?
                groupPayrun.getCurrency().getName() :
                "";
        String employeeName = groupPayrun.getEmployee() != null && groupPayrun.getEmployee().getFullName() != null ?
                groupPayrun.getEmployee().getFullName() :
                "";

        switch (typeCode) {
            case GROUP_PAYRUN_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(GROUP_PAYRUN_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_GROUP_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case GROUP_PAYRUN_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(GROUP_PAYRUN_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_GROUP_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case GROUP_PAYRUN_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(GROUP_PAYRUN_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_GROUP_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case GROUP_PAYRUN_SUBMIT -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(GROUP_PAYRUN_SUBMIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(SUBMITTED_GROUP_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case GROUP_PAYRUN_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(GROUP_PAYRUN_APPROVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(APPROVED_GROUP_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
            case GROUP_PAYRUN_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(GROUP_PAYRUN_REJECT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(REJECTED_GROUP_PAYRUN, creatorName, total, currencyName, employeeName, month, year));
            }
        }

    }

    private void getCashAdvanceUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(myUpdate.getAffectedID());
        if (cashAdvance == null) {
            return;
        }

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        String approverName = cashAdvance.getCurrentApprover() != null &&
                cashAdvance.getCurrentApprover().getExactEmployee() != null &&
                cashAdvance.getCurrentApprover().getExactEmployee().getFullName() != null ?
                cashAdvance.getCurrentApprover().getExactEmployee().getFullName() :
                "";
        String requesterName = cashAdvance.getEmployee() != null && cashAdvance.getEmployee().getFullName() != null ?
                cashAdvance.getEmployee().getFullName() :
                "";
        String cashAdvanceNo = cashAdvance.getNumber() != null ? cashAdvance.getNumber() : "";
        String requestedAmount = cashAdvance.getTotalAmount() != null ? numberFormat.format(cashAdvance.getTotalAmount()) : "";
        String currencyName = cashAdvance.getCurrency() != null && cashAdvance.getCurrency().getName() != null ?
                cashAdvance.getCurrency().getName() :
                "";
        String categoryName = cashAdvance.getCategory() != null && cashAdvance.getCategory().getName() != null ?
                cashAdvance.getCategory().getName() :
                "";
        switch (typeCode) {
            case CASH_ADVANCE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(CASH_ADVANCE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_CASH_ADVANCE, userName, categoryName, cashAdvanceNo));
            }
            case CASH_ADVANCE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(CASH_ADVANCE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EDITED_CASH_ADVANCE, userName, categoryName, cashAdvanceNo));
            }
            case CASH_ADVANCE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(CASH_ADVANCE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_CASH_ADVANCE, userName, categoryName, cashAdvanceNo));
            }
            case CASH_ADVANCE_SUBMIT -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(CASH_ADVANCE_SUBMIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(SUBMITTED_CASH_ADVANCE, requesterName, categoryName, cashAdvanceNo, requestedAmount, currencyName));
            }
            case CASH_ADVANCE_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(CASH_ADVANCE_APPROVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(APPROVED_CASH_ADVANCE, approverName, categoryName, cashAdvanceNo, requestedAmount, currencyName));
            }
            case CASH_ADVANCE_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(CASH_ADVANCE_REJECT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(REJECTED_CASH_ADVANCE, approverName, categoryName, cashAdvanceNo, requestedAmount, currencyName));
            }
        }

    }

    private void getAttachmentUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsFileHeader edsFileHeader = fileHeaderManager.get(myUpdate.getAffectedID());

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        String fileName = edsFileHeader != null && edsFileHeader.getName() != null ? edsFileHeader.getName() : "";

        if (ATTACHMENT_ADD.equalsIgnoreCase(typeCode)) {
            item.setSubType(MyUpdateItem.ADD);
            item.setTitle(activityWfmMessageSource.localize(ATTACHMENT_ADD));
            item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_ATTACHMENT, userName, fileName));
        } else if (ATTACHMENT_DELETE.equalsIgnoreCase(typeCode)) {
            item.setSubType(MyUpdateItem.DELETE);
            item.setTitle(activityWfmMessageSource.localize(ATTACHMENT_DELETE));
            item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_ATTACHMENT, userName, fileName));
        }
    }


    private void getCustomFormItemUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsFileHeader edsFileHeader = fileHeaderManager.get(myUpdate.getAffectedID());
        EdsUser enducerUser = userManager.get(myUpdate.getInducerID());

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        String fileName = edsFileHeader != null && edsFileHeader.getName() != null ? edsFileHeader.getName() : "";
        EdsCustomForm customForm = customFormManager.findByFormID(myUpdate.getFormId());

        if (customForm == null) {
            item.setTitle("Custom Form cannot be found!");
            return;
        }
        EdsProperty property = customForm.getProperty();

        switch (myUpdate.getTypeCode()) {
            case MyUpdateTypeManager.CUSTOM_FORM_ITEM_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(property.getSingular() + " " + activityWfmMessageSource.localize(ADDED_CUSTOM_FORM));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(ADDED_CUSTOM_FORM) + " " + property.getShortcut());
            }
            case MyUpdateTypeManager.CUSTOM_FORM_ITEM_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(property.getSingular() + " " + activityWfmMessageSource.localize(EDITED_CUSTOM_FORM));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(EDITED_CUSTOM_FORM) + " " + property.getShortcut());
            }
            case MyUpdateTypeManager.CUSTOM_FORM_ITEM_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(property.getSingular() + " " + activityWfmMessageSource.localize(DELETED_CUSTOM_FORM));
                item.setMessage(userName + " " + activityWfmMessageSource.localize(DELETED_CUSTOM_FORM) + " " + property.getShortcut());
            }
            case MyUpdateTypeManager.CUSTOM_FORM_ITEM_SUBMITTED_TO_MANAGER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(property.getSingular() + " Submitted");
                item.setMessage(userName + " submited " + property.getShortcut() + " to " + enducerUser);
            }
            case MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(property.getSingular() + " Approve");
                item.setMessage(userName + " approve " + property.getShortcut());
            }
            case MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(property.getSingular() + " Rejected");
                item.setMessage(userName + " rejected " + property.getShortcut());
            }
        }

    }

    private void getInvoicePaymentUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsInvoicePayment invoicePayment = invoicePaymentManager.get(myUpdate.getAffectedID());
        String reporterName = getUserName(myUpdate, userManager.get(myUpdate.getInducerID()));
        String itemName = "invoice payment";
        if (invoicePayment != null && invoicePayment.getType() != null) {
            if (invoicePayment.getType().contains(RECEIVABLE) || invoicePayment.getType().contains(PREPAYMENT)) {
                itemName = "Prepayment";
            } else if (invoicePayment.getType().contains(SUPPLIER) || invoicePayment.getType().contains(PAYABLE)) {
                itemName = "Supplier Credit";
            }
        }

        switch (typeCode) {
            case INVOICE_PAYMENT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(INVOICE_PAYMENT_ADD));
                item.setMessage(reporterName + " " + commonLocalizer.localize("addedMessage", "added ") + itemName);
            }
            case INVOICE_PAYMENT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(INVOICE_PAYMENT_EDIT));
                item.setMessage(reporterName + " " + commonLocalizer.localize("updatedMessage", "updated ") + itemName);
            }
            case INVOICE_PAYMENT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(INVOICE_PAYMENT_DELETE));
                item.setMessage(reporterName + " deleted " + itemName);
            }
        }
    }

    private void getEventUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        EdsEvent event = eventManager.get(myUpdate.getAffectedID());
        if (event == null) {
            return;
        }
        String itemName = event.getName();
        String reporterName = getUserName(myUpdate, userManager.get(myUpdate.getInducerID()));
        String typeCode = myUpdate.getTypeCode();

        switch (typeCode) {
            case EVENT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                if (event.getActivityType() == 2) {
                    item.setTitle(activityWfmMessageSource.localize(CALL_LOG_ADD));
                } else {
                    item.setTitle(activityWfmMessageSource.localize(EVENT_ADD));
                }
                item.setMessage(reporterName + " " + commonLocalizer.localize("addedMessage", "added ") + itemName);
            }
            case EVENT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                if (event.getActivityType() == 2) {
                    item.setTitle(activityWfmMessageSource.localize(CALL_LOG_EDIT));
                } else {
                    item.setTitle(activityWfmMessageSource.localize(EVENT_EDIT));
                }
                item.setMessage(reporterName + " " + commonLocalizer.localize("updatedMessage", "edited ") + itemName);
            }
            case EVENT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                if (event.getActivityType() == 2) {
                    item.setTitle(activityWfmMessageSource.localize(CALL_LOG_DELETE));
                } else {
                    item.setTitle(activityWfmMessageSource.localize(EVENT_DELETE));
                }
                item.setMessage(reporterName + " deleted " + itemName);
            }
        }
    }

    private void getSaleQuoteUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {

        String typeCode = myUpdate.getTypeCode();
        EdsSaleQuote saleQuote = quoteManager.getSaleQuote(myUpdate.getAffectedID());

        String quoteNumber = "";
        String clientOrManagerName = "";
        String quoteTotal = "";
        String quoteTotalHistory = null;
        if (saleQuote == null) {
            quoteNumber = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (quoteNumber == null) {
                return;
            }
        } else {
            quoteNumber = saleQuote.getNumber() != null ? saleQuote.getNumber() : "";
            quoteTotal = saleQuote.getTotal() != null ? numberFormat.format(saleQuote.getTotal()) : "";
            quoteTotalHistory = myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : quoteTotal;
            switch (typeCode) {
                case SALES_QUOTE_MANAGER_APPROVE, SALES_QUOTE_MANAGER_REJECT ->
                        clientOrManagerName = getUserName(myUpdate, userManager.get(myUpdate.getInducerID()));
                case SALES_QUOTE_CLIENT_APPROVE, SALES_QUOTE_REJECT, SALES_QUOTE_SEND_TO_CLIENT -> {
                    if (EdsTrusteeType.CONTACT.equals(myUpdate.getInducerType())) {
                        EdsCrmContact contact = crmContactManager.get(myUpdate.getInducerID());
                        if (contact != null) {
                            clientOrManagerName = contact.getName() != null ? contact.getName() : "";
                        }
                    } else if (saleQuote.getClientOrSupplier() != null && saleQuote.getClientOrSupplier().getName() != null) {
                        clientOrManagerName = saleQuote.getClientOrSupplier().getName();
                    }
                }
                case SALES_QUOTE_SUBMITTED_TO_MANAGER -> clientOrManagerName = saleQuote.getCurrentApprover() != null &&
                        saleQuote.getCurrentApprover().getExactEmployee() != null &&
                        saleQuote.getCurrentApprover().getExactEmployee().getName() != null
                        ? getUserName(myUpdate, saleQuote.getCurrentApprover().getExactEmployee())
                        : "";
            }
        }

        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);

        EdsProperty sqProperty = propertManager.findByCode(SALE_QUOTE);
        String salesQuoteSingular = sqProperty != null && sqProperty.getSingular() != null ? sqProperty.getSingular() : commonLocalizer.localize("salesQuote");

        EdsProperty soProperty = propertManager.findByCode(SALE_ORDER_CODE);
        String salesOrderSingular = soProperty != null && soProperty.getSingular() != null ? soProperty.getSingular() : commonLocalizer.localize("salesOrder");

        switch (typeCode) {
            case SALES_QUOTE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_ADD), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_ADDED_SALES_QUOTE, userName, quoteNumber, quoteTotalHistory), salesQuoteSingular));
            }
            case SALES_QUOTE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_EDIT), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_SALES_QUOTE, userName, quoteNumber, quoteTotalHistory), salesQuoteSingular));
            }
            case SALES_QUOTE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_DELETE), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_DELETED_SALES_QUOTE, userName, quoteNumber, quoteTotalHistory), salesQuoteSingular));
            }
            case SALES_QUOTE_CLIENT_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_CLIENT_APPROVE), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(CLIENT_APPROVED_SALES_QUOTE, clientOrManagerName, quoteNumber), salesQuoteSingular));
            }
            case SALES_QUOTE_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_REJECT), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(CLIENT_REJECTED_SALES_QUOTE, clientOrManagerName, quoteNumber), salesQuoteSingular));
            }
            case SALES_QUOTE_CONVERT_TO_SALE_ORDER -> {
                item.setSubType(MyUpdateItem.CONVERTED);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_CONVERT_TO_SALE_ORDER), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_CONVERTED_SALEQUOTE_TO, userName, quoteNumber, saleQuote.getQuoteNumberCN() != null ? saleQuote.getQuoteNumberCN() : null), salesQuoteSingular, salesOrderSingular));
            }
            case SALES_QUOTE_SEND_TO_CLIENT -> {
                item.setSubType(MyUpdateItem.STATUS_SENT);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_SEND_TO_CLIENT), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_SENT_SALES_QUOTE_TO_CLIENT, userName, quoteNumber, clientOrManagerName), salesQuoteSingular));
            }
            case SALES_QUOTE_SUBMITTED_TO_MANAGER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_SUBMITTED_TO_MANAGER), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_SUBMIT_SALES_QUOTE_TO_MANAGER, userName, quoteNumber, clientOrManagerName), salesQuoteSingular));
            }
            case SALES_QUOTE_MANAGER_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_MANAGER_APPROVE), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(MANAGER_APPROVED_SALES_QUOTE, clientOrManagerName, quoteNumber), salesQuoteSingular));
            }
            case SALES_QUOTE_MANAGER_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_MANAGER_REJECT), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(REJECTED_SALES_QUOTE, clientOrManagerName, quoteNumber), salesQuoteSingular));
            }
            case SALES_QUOTE_CLOSED -> {
                item.setSubType(MyUpdateItem.STATUS_CLOSED);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_QUOTE_CLOSED), salesQuoteSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_SALES_QUOTE_CLOSED, userName, quoteNumber), salesQuoteSingular));
            }
        }
    }

    private void getSaleOrderUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {

        String typeCode = myUpdate.getTypeCode();
        EdsSaleQuote saleQuote = quoteManager.getSaleQuote(myUpdate.getAffectedID());

        String quoteNumber = "";
        String quoteTotal = "";
        String quoteTotalHistory = null;
        String clientOrManagerName = "";
        if (saleQuote == null) {
            quoteNumber = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (quoteNumber == null) {
                return;
            }
        } else {
            quoteNumber = saleQuote.getNumber() != null ? saleQuote.getNumber() : "";
            quoteTotal = saleQuote.getTotal() != null ? numberFormat.format(saleQuote.getTotal()) : "";
            quoteTotalHistory = myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : quoteTotal;
            if (SALES_ORDER_MANAGER_APPROVE.equals(typeCode) || SALES_ORDER_MANAGER_REJECT.equals(typeCode)) {
                clientOrManagerName = getUserName(myUpdate, userManager.get(myUpdate.getInducerID()));
            } else if (SALES_ORDER_SUBMITTED_TO_MANAGER.equals(typeCode)) {
                clientOrManagerName = saleQuote.getCurrentApprover() != null &&
                        saleQuote.getCurrentApprover().getExactEmployee() != null
                        ? getUserName(myUpdate, saleQuote.getCurrentApprover().getExactEmployee())
                        : "";
            }
        }

        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);

        EdsProperty soProperty = propertManager.findByCode(SALE_ORDER_CODE);
        String salesOrderSingular = soProperty != null && soProperty.getSingular() != null ? soProperty.getSingular() : commonLocalizer.localize("salesOrder");

        EdsProperty sqProperty = propertManager.findByCode(SALE_QUOTE);
        String salesQuoteSingular = sqProperty != null && sqProperty.getSingular() != null ? sqProperty.getSingular() : commonLocalizer.localize("salesQuote");

        switch (typeCode) {
            case SALES_ORDER_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_ORDER_ADD), salesOrderSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_ADDED_SALES_ORDER, userName, quoteNumber, quoteTotalHistory), salesOrderSingular));
            }
            case SALES_ORDER_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_ORDER_EDIT), salesOrderSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_SALES_ORDER, userName, quoteNumber, quoteTotalHistory), salesOrderSingular));
            }
            case SALES_ORDER_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_ORDER_DELETE), salesOrderSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_DELETED_SALES_ORDER, userName, quoteNumber, quoteTotalHistory), salesOrderSingular));
            }
            case SALES_ORDER_PICKLIST -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_ORDER_PICKLIST), salesOrderSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_PICKLIST_SALES_ORDER, userName, quoteNumber, quoteTotalHistory), salesOrderSingular));
            }
            case SALES_ORDER_CLOSED -> {
                item.setSubType(MyUpdateItem.STATUS_CLOSED);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_ORDER_CLOSED), salesOrderSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_SALES_ORDER_CLOSED, userName, quoteNumber), salesOrderSingular));
            }
            case SALES_ORDER_CONVERT_FROM_SQ -> {
                item.setSubType(MyUpdateItem.CONVERTED);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_ORDER_CONVERT_FROM_SQ), salesOrderSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_SALES_ORDER_CONVERTED_FROM_SQ, userName, quoteNumber, saleQuote.getFromNumber() != null ? saleQuote.getFromNumber() : null), salesOrderSingular, salesQuoteSingular));
            }
            case SALES_ORDER_SUBMITTED_TO_MANAGER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_ORDER_SUBMITTED_TO_MANAGER), salesOrderSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(HAS_SUBMIT_SALES_ORDER_TO_MANAGER, userName, quoteNumber, clientOrManagerName), salesOrderSingular));
            }
            case SALES_ORDER_MANAGER_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_ORDER_MANAGER_APPROVE), salesOrderSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(MANAGER_APPROVED_SALES_ORDER, clientOrManagerName, quoteNumber), salesOrderSingular));
            }
            case SALES_ORDER_MANAGER_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(format(activityWfmMessageSource.localize(SALES_ORDER_MANAGER_REJECT), salesOrderSingular));
                item.setMessage(format(activityWfmMessageSource.localizeWithParam(REJECTED_SALES_ORDER, clientOrManagerName, quoteNumber), salesOrderSingular));
            }
        }
    }

    private void getPurchaseOrderUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(myUpdate.getAffectedID());

        String orderNumber;
        String clientOrManagerName = "";
        String orderTotal = "";
        String orderTotalHistory = null;
        if (purchaseOrder == null) {
            orderNumber = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (orderNumber == null) {
                return;
            }
        } else {
            orderNumber = purchaseOrder.getNumber() != null ? purchaseOrder.getNumber() : "";
            orderTotal = purchaseOrder.getTotal() != null ? numberFormat.format(purchaseOrder.getTotal()) : "";
            orderTotalHistory = myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : orderTotal;
            if (PURCHASE_ORDER_SEND_TO_CLIENT.equals(typeCode)) {
                if (EdsTrusteeType.CONTACT.equals(myUpdate.getInducerType())) {
                    EdsCrmContact contact = crmContactManager.get(myUpdate.getInducerID());
                    if (contact != null) {
                        clientOrManagerName = contact.getName() != null ? contact.getName() : "";
                    }
                } else if (purchaseOrder.getClientOrSupplier() != null && purchaseOrder.getClientOrSupplier().getName() != null) { //clientOrManagerName = userManager.get(myUpdate.getInducerID()).getFullName();
                    if (purchaseOrder.getClientOrSupplier().getPrimaryContact() != null) {
                        clientOrManagerName = purchaseOrder.getClientOrSupplier().getPrimaryContact().getName() != null
                                ? purchaseOrder.getClientOrSupplier().getPrimaryContact().getName()
                                : "";
                    } else {
                        clientOrManagerName = purchaseOrder.getClientOrSupplier().getName() != null
                                ? purchaseOrder.getClientOrSupplier().getName()
                                : "";
                    }
                }
            } else if (PURCHASE_ORDER_SUBMITTED_TO_MANAGER.equals(typeCode)) {
                clientOrManagerName = purchaseOrder.getApprover() != null && purchaseOrder.getApprover().getName() != null
                        ? purchaseOrder.getApprover().getName()
                        : "";
            }

        }

        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case PURCHASE_ORDER_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_ORDER_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_PURCHASE_ORDER, userName, orderNumber, orderTotalHistory));
            }
            case PURCHASE_ORDER_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_ORDER_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_PURCHASE_ORDER, userName, orderNumber, orderTotalHistory));
            }
            case PURCHASE_ORDER_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_ORDER_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_PURCHASE_ORDER, userName, orderNumber));
            }
            case PURCHASE_ORDER_SEND_TO_CLIENT -> {
                item.setSubType(MyUpdateItem.STATUS_SENT);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_ORDER_SEND_TO_CLIENT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SENT_PURCHASE_ORDER_TO_CLIENT, userName, orderNumber, clientOrManagerName));
            }
            case PURCHASE_ORDER_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_ORDER_APPROVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_APPROVED_PURCHASE_ORDER, userName, orderNumber));
            }
            case PURCHASE_ORDER_RECEIVED -> {
                item.setSubType(MyUpdateItem.STATUS_RECEIVED);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_ORDER_RECEIVED));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_PURCHASE_ORDER_RECEIVED, userName, orderNumber));
            }
            case PURCHASE_ORDER_PARTIAL_RECEIVED -> {
                item.setSubType(MyUpdateItem.STATUS_RECEIVED);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_ORDER_PARTIAL_RECEIVED));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_PURCHASE_ORDER_PARTIAL_RECEIVED, userName, orderNumber));
            }
            case PURCHASE_ORDER_SUBMITTED_TO_MANAGER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_ORDER_SUBMITTED_TO_MANAGER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SUBMIT_PURCHASE_ORDER_TO_MANAGER, userName, orderNumber, clientOrManagerName));
            }
            case PURCHASE_ORDER_CLOSED -> {
                item.setSubType(MyUpdateItem.STATUS_CLOSED);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_ORDER_CLOSED));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_PURCHASE_ORDER_CLOSED, userName, orderNumber));
            }
        }
    }

    private void getPurchaseInvoiceUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(myUpdate.getAffectedID());

        String purchaseNumber;
        String clientOrManagerName = "";
        String purchaseTotal = "";
        String purchaseTotalHistory = null;
        if (purchaseInvoice == null) {
            purchaseNumber = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (purchaseNumber == null) {
                return;
            }
        } else {
            purchaseNumber = purchaseInvoice.getNumber() != null ? purchaseInvoice.getNumber() : "";
            purchaseTotal = purchaseInvoice.getTotal() != null ? numberFormat.format(purchaseInvoice.getTotal()) : "";
            purchaseTotalHistory = myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : purchaseTotal;
            switch (typeCode) {
                case PURCHASE_INVOICE_PAYMENT_VOID, PURCHASE_INVOICE_PAYMENT_PAY ->
                        clientOrManagerName = userManager.get(myUpdate.getInducerID()).getFullName();
                case PURCHASE_INVOICE_MANAGER_APPROVE, PURCHASE_INVOICE_MANAGER_REJECT ->
                        clientOrManagerName = getUserName(myUpdate, userManager.get(myUpdate.getInducerID()));
                case PURCHASE_INVOICE_SUBMITTED_TO_MANAGER ->
                        clientOrManagerName = purchaseInvoice.getCurrentApprover() != null &&
                                purchaseInvoice.getCurrentApprover().getExactEmployee() != null
                                ? getUserName(myUpdate, purchaseInvoice.getCurrentApprover().getExactEmployee())
                                : "";
                default -> {
                }
            }
        }
        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);


        EdsInvoicePayment invoicePayment = invoicePaymentManager.get(myUpdate.getRelationID());
        String paymentAmount = invoicePayment != null ? numberFormat.format(invoicePayment.getAmount()) : "";
        String paymentAmountHistory = invoicePayment != null && myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : paymentAmount;

        switch (typeCode) {
            case PURCHASE_INVOICE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_PURCHASE_INVOICE, userName, purchaseNumber, purchaseTotalHistory));
            }
            case PURCHASE_INVOICE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_PURCHASE_INVOICE, userName, purchaseNumber, purchaseTotalHistory));
            }
            case PURCHASE_INVOICE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_PURCHASE_INVOICE, userName, purchaseNumber));
            }
            case PURCHASE_INVOICE_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_APPROVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_PURCHASE_INVOICE_APPROVE, clientOrManagerName, purchaseNumber));
            }
            case PURCHASE_INVOICE_ADD_CREDIT_NOTE -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(SALES_INVOICE_ADD_CREDIT_NOTE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_HAVE_PURCHASE_INVOICE_ADD_CREDIT_NOTE, userName, purchaseNumber));
            }
            case PURCHASE_INVOICE_PAYMENT_VOID -> {
                item.setSubType(MyUpdateItem.STATUS_WAITING);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_PAYMENT_VOID));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_HAVE_PURCHASE_INVOICE_PAYMENT_VOID, userName, paymentAmountHistory));
            }
            case PURCHASE_INVOICE_PAYMENT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_PAYMENT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_HAVE_PURCHASE_INVOICE_PAYMENT_DELETE, userName, paymentAmountHistory));
            }
            case PURCHASE_INVOICE_PAYMENT_PAY -> {
                item.setSubType(MyUpdateItem.STATUS_PAID);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_PAYMENT_PAY));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_HAVE_PURCHASE_INVOICE_PAYMENT_PAY, userName, paymentAmountHistory));
            }
            case PURCHASE_INVOICE_REFUND -> {
                item.setSubType(MyUpdateItem.STATUS_REFUNDED);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_REFUND));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_PURCHASE_INVOICE_ADD_CREDIT_NOTE, purchaseNumber));
            }
            case PURCHASE_INVOICE_SUBMITTED_TO_MANAGER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_SUBMITTED_TO_MANAGER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SUBMIT_PURCHASE_INVOICE_TO_MANAGER, userName, purchaseNumber, clientOrManagerName));
            }
            case PURCHASE_INVOICE_MANAGER_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_MANAGER_APPROVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(MANAGER_APPROVED_PURCHASE_INVOICE, clientOrManagerName, purchaseNumber));
            }
            case PURCHASE_INVOICE_MANAGER_REJECT -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(PURCHASE_INVOICE_MANAGER_REJECT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(REJECTED_PURCHASE_INVOICE, clientOrManagerName, purchaseNumber));
            }
        }
    }

    private void getProductUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsItem product = itemManager.get(myUpdate.getAffectedID());

        String productNumber = "";
        if (product == null) {
            productNumber = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (productNumber == null) {
                return;
            }
        } else {
            productNumber = product.getName() != null ? product.getName() : "";
        }

        EdsUser user = userManager.get(myUpdate.getReceiver());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case PRODUCT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(PRODUCT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_PRODUCT, userName, productNumber));
            }
            case PRODUCT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(PRODUCT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_PRODUCT, userName, productNumber));
            }
            case PRODUCT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(PRODUCT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_PRODUCT, userName, productNumber));
            }
        }

    }


    private void getFixedAssetUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsFixedAsset fixedAsset = fixedAssetManager.get(myUpdate.getAffectedID());
        String user = "";
        String fixedAssetName = "";
        String fixedAssetNumber = "";
        if (fixedAsset == null) {
            fixedAssetName = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (fixedAssetName == null) {
                return;
            }
        } else {
            if ((FIXED_ASSET_OWNER_ADD.equals(typeCode) || FIXED_ASSET_OWNER_EDIT.equals(typeCode)) && myUpdate.getInducerID() != null) {
                EdsUser owner = userManager.get(myUpdate.getInducerID());
                if (owner != null) {
                    user = owner.getFullName();
                }
            }
            fixedAssetName = fixedAsset.getName() != null ? fixedAsset.getName() : "";
            fixedAssetNumber = fixedAsset.getIntNumber() != null ? fixedAsset.getCode() : "";
        }


        EdsUser creator = userManager.get(myUpdate.getReceiver());
        String creatorName = getUserName(myUpdate, creator);


        switch (typeCode) {
            case FIXED_ASSET_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(FIXED_ASSET_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_FIXED_ASSET, creatorName, fixedAssetName, fixedAssetNumber));
            }
            case FIXED_ASSET_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(FIXED_ASSET_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_FIXED_ASSET, creatorName, fixedAssetName, fixedAssetNumber));
            }
            case FIXED_ASSET_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(FIXED_ASSET_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_FIXED_ASSET, creatorName, fixedAssetName, fixedAssetNumber));
            }
            case FIXED_ASSET_DISPOSE -> {
                item.setSubType(MyUpdateItem.STATUS_WAITING);
                item.setTitle(activityWfmMessageSource.localize(FIXED_ASSET_DISPOSE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DISPOSE_FIXED_ASSET, creatorName, fixedAssetName, fixedAssetNumber));
            }
            case FIXED_ASSET_OWNER_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(FIXED_ASSET_OWNER_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_OWNER_FIXED_ASSET, creatorName, user, fixedAssetName, fixedAssetNumber));
            }
            case FIXED_ASSET_OWNER_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(FIXED_ASSET_OWNER_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_EDITED_OWNER_FIXED_ASSET, creatorName, user, fixedAssetName, fixedAssetNumber));
            }
        }

    }


    private void getCheckUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsBankCheck bankCheck = bankCheckManager.get(myUpdate.getAffectedID());
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        String bannkCheckName;
        if (bankCheck == null) {
            bannkCheckName = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (bannkCheckName == null) {
                return;
            }
        } else {
            bannkCheckName = bankCheck.getNumber() != null ? bankCheck.getNumber() : "";
        }

        switch (typeCode) {
            case CHECK_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(CHECK_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_CHECK, userName, bannkCheckName));
            }
            case CHECK_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(CHECK_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_CHECK, userName, bannkCheckName));
            }
            case CHECK_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(CHECK_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_CHECK, userName, bannkCheckName));
            }
        }

    }

    private void getBankAccountUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsBankAccount bankAccount = bankAccountManager.get(myUpdate.getAffectedID());

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        String bankAccountName;
        if (bankAccount == null) {
            bankAccountName = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (bankAccountName == null) {
                return;
            }
        } else {
            bankAccountName = bankAccount.getAccountNumber() != null ? bankAccount.getAccountNumber() : "";
        }

        switch (typeCode) {
            case BANK_ACCOUNT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(BANK_ACCOUNT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_BANK_ACCOUNT, userName, bankAccountName));
            }
            case BANK_ACCOUNT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(BANK_ACCOUNT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_BANK_ACCOUNT, userName, bankAccountName));
            }
            case BANK_ACCOUNT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(BANK_ACCOUNT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_BANK_ACCOUNT, userName, bankAccountName));
            }
        }

    }

    private void getChartOfAccountUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsAccount chartOfAccount = accountingManager.get(myUpdate.getAffectedID());

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        String chartOfAccountName = "";
        if (chartOfAccount == null) {
            chartOfAccountName = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (chartOfAccountName == null) {
                return;
            }
        } else {
            chartOfAccountName = chartOfAccount.getCodeString() != null ? chartOfAccount.getCodeString() : "";
        }

        switch (typeCode) {
            case CHART_OF_ACCOUNT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(CHART_OF_ACCOUNT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_CHART_OF_ACCOUNT, userName, chartOfAccountName));
            }
            case CHART_OF_ACCOUNT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(CHART_OF_ACCOUNT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_CHART_OF_ACCOUNT, userName, chartOfAccountName));
            }
            case CHART_OF_ACCOUNT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(CHART_OF_ACCOUNT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_CHART_OF_ACCOUNT, userName, chartOfAccountName));
            }
        }

    }

    private void getExpenseReportUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsExpenseReport expenseReport = reportManager.getExpenseReport(myUpdate.getAffectedID());
        String expenseNumber = expenseReport != null ? expenseReport.getNumber() : "";

        String expenseReportTitle;
        String reporterName = userManager.get(myUpdate.getInducerID()).getFullName();
        if (expenseReport == null) {
            expenseReportTitle = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (expenseReportTitle == null) {
                return;
            }
        } else {
            expenseReportTitle = expenseReport.getTitle() != null ? expenseReport.getTitle() : "";
        }

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        String totalHistory = "";
        String total = "";
        total = expenseReport.getBaseTotal() != null ? numberFormat.format(expenseReport.getBaseTotal()) : "";
        totalHistory = myUpdate.getAmount() != null ? numberFormat.format(myUpdate.getAmount()) : total;

        switch (typeCode) {
            case EXPENSE_REPORT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_EXPENSE_REPORT, userName, expenseNumber, expenseReportTitle, totalHistory));
            }
            case EXPENSE_REPORT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_EXPENSE_REPORT, userName, expenseNumber, expenseReportTitle, totalHistory));
            }
            case EXPENSE_REPORT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_EXPENSE_REPORT, userName, expenseNumber, expenseReportTitle));
            }
            case EXPENSE_REPORT_APPROVE -> {
                item.setSubType(MyUpdateItem.STATUS_APPROVED);
                item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_APPROVE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EXPENSE_REPORT_HAS_BEEN_APPROVED, userManager.get(myUpdate.getReceiver()).getFullName(), expenseNumber, expenseReportTitle));
            }
            case EXPENSE_REPORT_DECLINE -> {
                item.setSubType(MyUpdateItem.STATUS_REJECT);
                item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_DECLINE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(EXPENSE_REPORT_HAS_BEEN_DECLINED, expenseNumber, expenseReportTitle));
            }
            case EXPENSE_REPORT_GET_FROM_REPORTER -> {
                item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_GET_FROM_REPORTER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_SENT_EXPENSE_REPORT_FOR_YOUR_REVIEW, reporterName, expenseNumber, expenseReportTitle));
            }
            case EXPENSE_REPORT_SEND_TO_APPROVER -> {
                item.setSubType(MyUpdateItem.STATUS_SENT);
                item.setTitle(activityWfmMessageSource.localize(EXPENSE_REPORT_SEND_TO_APPROVER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_SENT_EXPENSE_REPORT_TO, userName, expenseNumber, expenseReportTitle, userManager.get(myUpdate.getReceiver()).getFullName()));
            }
        }

    }

    private void getCalendarEventGuestUpdate(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsGoogleCalendarEventGuests eventGuests = eventGuestsManager.get(myUpdate.getAffectedID());
        EdsEmployee owner = eventGuests.getEvent().getOwner();
        String eventSubject = eventGuests.getEvent().getSubject();
        String guestName = getEventGuestName(eventGuests.getEmail(), owner.getCompany().getObjectID());

        switch (typeCode) {
            case CALENDAR_EVENT_GUEST_STATUS_ACCEPTED -> {
                item.setSubType(MyUpdateItem.CALENDAR_EVENT_GUEST_STATUS_ACCEPTED);
                item.setTitle(activityWfmMessageSource.localize(CALENDAR_EVENT_GUEST_STATUS_ACCEPTED));
                item.setMessage(activityWfmMessageSource.localizeWithParam(INVITATION_ACCEPTED, guestName, eventSubject));
            }
            case CALENDAR_EVENT_GUEST_STATUS_TENTATIVELY -> {
                item.setSubType(MyUpdateItem.CALENDAR_EVENT_GUEST_STATUS_TENTATIVELY);
                item.setTitle(activityWfmMessageSource.localize(CALENDAR_EVENT_GUEST_STATUS_TENTATIVELY));
                item.setMessage(activityWfmMessageSource.localizeWithParam(TENTATIVELY_ACCEPTED, guestName, eventSubject));
            }
            case CALENDAR_EVENT_GUEST_STATUS_DECLINED -> {
                item.setSubType(MyUpdateItem.CALENDAR_EVENT_GUEST_STATUS_DECLINED);
                item.setTitle(activityWfmMessageSource.localize(CALENDAR_EVENT_GUEST_STATUS_DECLINED));
                item.setMessage(activityWfmMessageSource.localizeWithParam(INVITATION_DECLINED, guestName, eventSubject));
            }
        }
    }

    private String getEventGuestName(String guestsEmail, Integer companyID) {
        String userFullName = "";
        EdsCrmContact contact = crmContactManager.getContactByEmail(guestsEmail, companyID);
        if (contact != null) {
            userFullName = contact.getName();
        }
        return !"".equals(userFullName) ? userFullName : guestsEmail;
    }

    private void getIssueUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsIssue issue = issueManager.get(myUpdate.getAffectedID());
        String issueName = issue != null ? issue.getName() : activityWfmMessageSource.localize(UNNAMED);
        String issueNumber = issue != null ? issue.getNumber() : activityWfmMessageSource.localize(UNNAMED);
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case ISSUE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_ADDED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_UPDATED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_EMPLOYEE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_ADDED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_EMPLOYEE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_UPDATED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_TASK_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_ADDED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_TASK_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_UPDATED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_DEPARTMENT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_ADDED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_DEPARTMENT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_UPDATED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_CLIENT_CUSTOMER_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_ADDED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_CLIENT_CUSTOMER_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_UPDATED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_PROJECT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_ADDED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_TYPE_PROJECT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_UPDATED_ISSUE, userName, issueNumber, issueName));
            }
            case ISSUE_FILE_UPLOAD -> {
                item.setSubType(MyUpdateItem.FILE_UPLOAD);
                item.setTitle(activityWfmMessageSource.localize(ISSUE_FILE_UPLOAD));
                EdsFileHeader fileHeader = fileHeaderManager.get(myUpdate.getAffectedID());
                if (fileHeader != null && fileHeader.getEntityId() != null) {
                    String fileName = fileHeader.getName();
                    item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_DOCUMENT_TO_ISSUE, userName, fileName, issueNumber, issueName));
                }
            }
        }
    }

    private void getClientUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsCrmAccount client = clientManager.get(myUpdate.getAffectedID());
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        String clientName = client != null && client.getName() != null ? client.getName() : activityWfmMessageSource.localize(UNNAMED);

        switch (typeCode) {
            case CLIENT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(CLIENT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_CLIENT, userName, clientName));
            }
            case CLIENT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(CLIENT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_CLIENT, userName, clientName));
            }
            case CLIENT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(CLIENT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_CLIENT, userName, clientName));
            }
        }
    }


    private void getDepartmentUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);
        EdsDepartment department = departmentManager.get(myUpdate.getAffectedID());

        switch (typeCode) {
            case DEPARTMENT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(DEPARTMENT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_DEPARTAMENT, userName, department.getName()));
            }
            case DEPARTMENT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(DEPARTMENT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_DEPARTMENT, userName, department.getName()));
            }
            case DEPARTMENT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(DEPARTMENT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETE_DEPARTMENT, userName, department.getName()));
            }
            case DEPARTMENT_EMPLOYEE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(DEPARTMENT_EMPLOYEE_ADD));
                EdsUser receiverMySelf = userManager.get(myUpdate.getReceiver());
                EdsUser inducer = userManager.get(myUpdate.getInducerID());
                if (department.getLeader() != null && receiverMySelf.getObjectID().equals(department.getLeader().getObjectID())) {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_BEEN_ASSIGNED_AS_A_DEPARTMENT_LEADER, inducer.getFullName(), department.getName(), userName));
                } else {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_BEEN_ASSIGNED_TO_DEPARTMENT, inducer.getFullName(), department.getName(), userName));
                }
            }
            case DEPARTMENT_EMPLOYEE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(DEPARTMENT_EMPLOYEE_DELETE));
                EdsUser inducer = userManager.get(myUpdate.getInducerID());
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_BEEN_UNASSIGNED_FROM_DEPARTMENT, inducer.getFullName(), department.getName(), userName));
            }
            case DEPARTMENT_EDIT_FOR_LEADER -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(DEPARTMENT_EDIT_FOR_LEADER));
                EdsUser inducer = userManager.get(myUpdate.getInducerID());
                EdsUser receiverMySelf = userManager.get(myUpdate.getReceiver());
                if (receiverMySelf.getObjectID().equals(inducer.getObjectID())) {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_DEPARTMENT, userName, department.getName()));
                } else {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_DEPARTMENT, inducer.getName(), department.getName()));
                }
            }
            case DEPARTMENT_DELETE_FOR_LEADER -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(DEPARTMENT_DELETE_FOR_LEADER));
                EdsUser inducer = userManager.get(myUpdate.getInducerID());
                EdsUser receiverMySelf = userManager.get(myUpdate.getReceiver());
                if (receiverMySelf.getObjectID().equals(inducer.getObjectID())) {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_DEPARTMENT, userName, department.getName()));
                } else {
                    item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_DEPARTMENT, inducer.getName(), department.getName()));
                }
            }
        }
    }

    private void getUserUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        EdsUser emplClient = userManager.get(myUpdate.getAffectedID());
        String emplClientName = getUserName(myUpdate, emplClient);

        switch (typeCode) {
            case USER_CLIENT_CONTACT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(USER_CLIENT_CONTACT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_AS_A_CLIENT_CONTACT, userName, emplClientName));
            }
            case USER_CLIENT_CONTACT_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(USER_CLIENT_CONTACT_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_CONTACTS_OF_CLIENT, userName, emplClientName));
            }
            case USER_CLIENT_CONTACT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(USER_CLIENT_CONTACT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_CLIENT_CONTACT, userName, emplClientName));
            }
            case USER_EMPLOYEE_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(USER_EMPLOYEE_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_A_NEW_EMPLOYEE, userName, emplClientName));
            }
            case USER_EMPLOYEE_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(USER_EMPLOYEE_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_THE_EMPLOYEE, userName, emplClientName));
            }
            case USER_EMPLOYEE_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(USER_EMPLOYEE_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_THE_EMPLOYEE, userName, emplClientName));
            }
            case USER_EMPLOYEE_TERMINATE -> {
                item.setSubType(MyUpdateItem.STATUS_TERMINATED);
                item.setTitle(activityWfmMessageSource.localize(USER_EMPLOYEE_TERMINATE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(TERMINATED_THE_EMPLOYEE, userName, emplClientName));
            }
        }
    }

    private void getProjectUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsProject project = projectManager.get(myUpdate.getAffectedID());
        String projectName = project != null && project.getName() != null ? project.getName() : activityWfmMessageSource.localize(UNNAMED);
        String projectNumber = project != null && project.getNumber() != null ? project.getNumber() : activityWfmMessageSource.localize(UNNAMED);

        EdsUser assigned = userManager.get(myUpdate.getReceiver());
        String assignedName = assigned != null ? assigned.getFullName() : "";

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case PROJECT_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_PROJECT, userName, projectNumber, projectName));
            }
            case PROJECT_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_PROJECT, userName, projectNumber, projectName));
            }
            case PROJECT_DELETE_FOR_MANAGERS -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_DELETE_FOR_MANAGERS));
                item.setMessage(activityWfmMessageSource.localizeWithParam(PROJECT_HAS_BEEN_DELETED, userName, assignedName, projectNumber, projectName));
            }
            case PROJECT_DELETE_FOR_MEMBERS ->
                    item.setMessage(activityWfmMessageSource.localizeWithParam(PROJECT_HAS_BEEN_DELETED, userName, assignedName, projectNumber, projectName));
            case PROJECT_MANAGER_ASSIGN -> {
                item.setSubType(MyUpdateItem.ASSIGN);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_MANAGER_ASSIGN));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_BEEN_ASSIGNED_AS_A_PROJECT_MANAGER_TO_PROJECT, assignedName, projectNumber, projectName, userName));
            }
            case PROJECT_BACKUP_MANAGER_ASSIGN -> {
                item.setSubType(MyUpdateItem.ASSIGN);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_BACKUP_MANAGER_ASSIGN));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_BEEN_ASSIGNED_AS_A_BACKUPMANAGE_TO_PROJECT, assignedName, projectNumber, projectName, userName));
            }
            case PROJECT_MEMBER_ASSIGN -> {
                item.setSubType(MyUpdateItem.ASSIGN);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_MEMBER_ASSIGN));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_BEEN_ASSIGNED_AS_A_MEMBER_TO_PROJECT, assignedName, projectNumber, projectName, userName));
            }
            case PROJECT_UPDATE -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_UPDATE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_PROJECT, userName, projectNumber, projectName));
            }
            case PROJECT_STATUS_COMPLETED -> {
                item.setSubType(MyUpdateItem.STATUS_COMPELETED);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_STATUS_COMPLETED));
                item.setMessage(activityWfmMessageSource.localizeWithParam(COMPLETED_PROJECT, userName, projectNumber, projectName));
            }
            case PROJECT_MEMBER_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_MEMBER_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_BEEN_UNASSIGNED_FROM_PROJECT, assignedName, projectNumber, projectName, userName));
            }
            case PROJECT_MEMBER_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_MEMBER_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_PROJECT, assignedName, projectNumber, projectName));
            }
            case PROJECT_UPDATE_FOR_MANAGER -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_UPDATE_FOR_MANAGER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_PROJECT, assignedName, projectNumber, projectName));
            }
            case PROJECT_UPDATE_FOR_BACKUP_MANAGER -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_UPDATE_FOR_BACKUP_MANAGER));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_PROJECT, assigned, projectNumber, projectName));
            }
            case PROJECT_IMPORT_TASKS_FROM_MS_PROJECT -> {
                item.setSubType(MyUpdateItem.IMPORTED);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_IMPORT_TASKS_FROM_MS_PROJECT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(IMPORTED_TASKS_TO_PROJECT, userName, projectNumber, projectName));
            }
            case PROJECT_FILE_UPLOAD -> {
                item.setSubType(MyUpdateItem.FILE_UPLOAD);
                item.setTitle(activityWfmMessageSource.localize(PROJECT_FILE_UPLOAD));
                EdsFileHeader fileHeader = fileHeaderManager.get(myUpdate.getAffectedID());
                if (fileHeader != null) {
                    projectName = fileHeader.getFolder() != null ? fileHeader.getFolder().getName() : activityWfmMessageSource.localize(UNNAMED);
                    String fileName = fileHeader.getName();
                    item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_DOCUMENT_TO_PROJECT, userName, fileName, projectNumber, projectName));
                }
            }
        }
    }

    private void getTaskUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsTask task = taskManager.get(myUpdate.getAffectedID());
        String taskName = task != null ? task.getName() : activityWfmMessageSource.localize(UNNAMED);
        String taskNumber = task != null ? task.getNumber() : activityWfmMessageSource.localize(UNNAMED);

        EdsUser assigned = userManager.get(myUpdate.getReceiver());
        String assignedName = assigned != null ? assigned.getFullName() : "";

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case TASK_STATUS_CANCELLED -> {
                item.setSubType(MyUpdateItem.STATUS_CANCELLED);
                item.setTitle(activityWfmMessageSource.localize(TASK_STATUS_CANCELLED));
                item.setMessage(activityWfmMessageSource.localizeWithParam(CANCELLED_TASK, userName, taskNumber, taskName));
            }
            case TASK_STATUS_CLOSED -> {
                item.setTitle(activityWfmMessageSource.localize(TASK_STATUS_CLOSED));
                item.setSubType(MyUpdateItem.STATUS_CLOSED);
                item.setMessage(activityWfmMessageSource.localizeWithParam(CLOSED_TASK, userName, taskNumber, taskName));
            }
            case TASK_STATUS_COMPELETED -> {
                item.setTitle(activityWfmMessageSource.localize(TASK_STATUS_COMPELETED));
                item.setSubType(MyUpdateItem.STATUS_COMPELETED);
                item.setMessage(activityWfmMessageSource.localizeWithParam(COMPLETED_TASK, userName, taskNumber, taskName));
            }
            case TASK_ADD -> {
                item.setTitle(activityWfmMessageSource.localize(TASK_ADD));
                item.setSubType(MyUpdateItem.ADD);
                item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_TASK, userName, taskNumber, taskName));
            }
            case TASK_UPDATE -> {
                item.setTitle(activityWfmMessageSource.localize(TASK_UPDATE));
                item.setSubType(MyUpdateItem.EDIT);
                item.setMessage(activityWfmMessageSource.localizeWithParam(UPDATED_TASK, userName, taskNumber, taskName));
            }
            case TASK_DELETE -> {
                item.setTitle(activityWfmMessageSource.localize(TASK_DELETE));
                item.setSubType(MyUpdateItem.DELETE);
                item.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_TASK, userName, taskNumber, taskName));
            }
            case TASK_ASSIGN -> {
                item.setTitle(activityWfmMessageSource.localize(TASK_ASSIGN));
                item.setSubType(MyUpdateItem.ASSIGN);
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_BEEN_ASSIGNED_TO_TASK, userName, taskNumber, taskName, assignedName));
            }
            case TASK_ASSIGNEE_DELETE -> {
                item.setTitle(activityWfmMessageSource.localize(TASK_ASSIGNEE_DELETE));
                item.setSubType(MyUpdateItem.DELETE);
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_BEEN_UNASSIGNED_FROM_TASK, userName, taskNumber, taskName, assignedName));
            }
            case TASK_FILE_UPLOAD -> {
                item.setTitle(activityWfmMessageSource.localize(TASK_FILE_UPLOAD));
                item.setSubType(MyUpdateItem.FILE_UPLOAD);
                EdsFileHeader fileHeader = fileHeaderManager.get(myUpdate.getAffectedID());
                if (fileHeader != null && fileHeader.getEntityId() != null) {
                    EdsTask edsTask = taskManager.get(fileHeader.getEntityId());
                    String tName = edsTask != null ? edsTask.getName() : activityWfmMessageSource.localize(UNNAMED);
                    String tNumber = edsTask != null ? edsTask.getNumber() : activityWfmMessageSource.localize(UNNAMED);
                    String fileName = fileHeader.getName();
                    item.setMessage(activityWfmMessageSource.localizeWithParam(ADDED_DOCUMENT_TO_TASK, userName, fileName, tNumber, tName));
                }
            }
        }
    }

    private void getLeaveRequestUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem myUpdateItem) {
        String typeCode = myUpdate.getTypeCode();
        EdsSickRequest sickRequest = sickRequestManager.get(myUpdate.getAffectedID());
        String deletedRequestString = "";
        if (sickRequest == null) {
            deletedRequestString = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (myUpdateItem == null || deletedRequestString == null) {
                return;
            }
        }

        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        if (LEAVE_REQUEST_DELETE.equals(typeCode)) {
            myUpdateItem.setSubType(MyUpdateItem.DELETE);
            myUpdateItem.setTitle(activityWfmMessageSource.localize(LEAVE_REQUEST_DELETE));
            myUpdateItem.setMessage(activityWfmMessageSource.localizeWithParam(DELETED_LEAVE_REQUEST, userName, deletedRequestString));
        }
    }


    private void getNetworkUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        String typeCode = myUpdate.getTypeCode();
        EdsNetwork network = networkManager.get(myUpdate.getAffectedID());
        String networkName, networkType, creator;
        boolean canView = true;
        if (network != null) {
            networkName = network.getName();
            creator = getUserName(myUpdate, network.getCreator());
            networkType = Constants.PRIVATE_INVITATION_TO_JOIN.equals(network.getType()) ? " private" : "";
            if (network.isDeleted() != null && network.isDeleted()) {
                canView = false;
            }
            if (network.getType() != null && network.getType().equals("Private (Invitation to Join)")) {
                if (network.getCreator() != null && !network.getCreator().equals(userManager.getUser())) {
                    canView = false;
                }
            }
            if (item != null && canView) {
                if (!"".equals(creator)) {
                    if (Constants.NETWORK_ADD.equals(typeCode)) {
                        item.setSubType(Constants.NETWORK_ADD);
                        if (broadMessage) {
                            item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_CREATED_NETWORK, "<b>" + creator + "</b>", "<b>" + networkName + "</b>", networkType));
                        } else {
                            item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_CREATED_NETWORK, "<b>" + networkName + "</b>", networkType));
                        }
                    } else if (Constants.NETWORK_EDIT.equals(typeCode)) {
                        item.setSubType(Constants.NETWORK_EDIT);
                        if (broadMessage) {
                            item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_NETWORK, "<b>" + creator + "</b>", "<b>" + networkName + "</b>", networkType));
                        } else {
                            item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_UPDATED_NETWORK, "<b>" + networkName + "</b>", networkType));
                        }
                    } else if (Constants.NETWORK_DELETE.equals(typeCode)) {
                        item.setSubType(Constants.NETWORK_DELETE);
                        if (broadMessage) {
                            item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_NETWORK, "<b>" + creator + "</b>", "<b>" + networkName + "</b>", networkType));
                        } else {
                            item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_DELETED_NETWORK, "<b>" + networkName + "</b>", networkType));
                        }
                    }
                }
            }
        }
    }

    private void getNetworkBlogUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        String typeCode = myUpdate.getTypeCode();
        EdsNews networkNews = newsManager.get(myUpdate.getAffectedID());
        String subject = "", creator = "";
        String newsOrDiscussion = activityWfmMessageSource.localize(NEWS);
        StringBuilder networkslist = new StringBuilder();
        boolean canView = true;
        if (networkNews != null) {
            newsOrDiscussion = networkNews.getBlog() ? activityWfmMessageSource.localize(DISCUSSION) : activityWfmMessageSource.localize(NEWS);
            subject = networkNews.getSubject();
            creator = getUserName(myUpdate, networkNews.getUser());
            if (networkNews.getAnonym() != null && networkNews.getAnonym()) {
                creator = activityWfmMessageSource.localize(ANONYMOUS);
            }
            if (networkNews.getNetworks() != null && networkNews.getNetworks().size() != 0) {
                if (networkNews.getNetworks().size() == 1) {
                    for (EdsNetwork tempNetwork : networkNews.getNetworks()) {
                        if (tempNetwork.getName() != null) {
                            networkslist.append(activityWfmMessageSource.localizeWithParam(NETWORK, "<b>" + tempNetwork.getName() + "</b>"));
                            break;
                        }
                    }
                } else {
                    int i = 0;
                    for (EdsNetwork tempNetwork : networkNews.getNetworks()) {
                        if (i == 0) {
                            if (tempNetwork.getName() != null) {
                                if (tempNetwork.isDeleted() != null) {
                                    if (tempNetwork.isDeleted()) {
                                        i--;
                                    } else {
                                        networkslist.append(activityWfmMessageSource.localizeWithParam(NETWORK, "<b>" + tempNetwork.getName() + "</b>"));
                                    }
                                } else {
                                    networkslist.append(activityWfmMessageSource.localizeWithParam(NETWORK, "<b>" + tempNetwork.getName() + "</b>"));
                                }
                            }
                        } else {
                            if (tempNetwork.getName() != null) {
                                networkslist.append(",<b>...</b>");
                                break;
                            }
                        }
                        i++;
                    }
                }

            }
        }
        if (item != null && item.getSection() != null && item.getSection().equals("NETWORK")) {
            if (networkNews != null && networkNews.getNetworks() != null) {
                canView = userCanViewNetworksUpdates(networkNews.getNetworks(), userManager.getUser());
            }
        }
        if (networkNews != null && networkNews.getDeleted() != null && networkNews.getDeleted()) {
            canView = false;
        }
        if (item != null && canView) {
            if (!subject.equals("") && !creator.equals("")) {
                if (Constants.NETWORK_BLOG_ADD.equals(typeCode)) {
                    item.setSubType(Constants.NETWORK_BLOG_ADD);
                    if (broadMessage) {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_OF, "<b>" + creator + "</b>", "<b>" + subject + "</b>", newsOrDiscussion, networkslist.toString()));
                    } else {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_ADDED_OF, "<b>" + subject + "</b> ", newsOrDiscussion, networkslist.toString()));
                    }
                } else if (Constants.NETWORK_BLOG_EDIT.equals(typeCode)) {
                    item.setSubType(Constants.NETWORK_BLOG_EDIT);
                    if (broadMessage) {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_OF, "<b>" + creator + "</b>", "<b>" + subject + "</b>", newsOrDiscussion, networkslist.toString()));
                    } else {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_UPDATED_OF, "<b>" + subject + "</b>", newsOrDiscussion, networkslist.toString()));
                    }
                } else if (Constants.NETWORK_BLOG_DELETE.equals(typeCode)) {
                    item.setSubType(Constants.NETWORK_BLOG_DELETE);
                    if (broadMessage) {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_OF, "<b>" + creator + "</b>", "<b>" + subject + "</b>", newsOrDiscussion, networkslist.toString()));
                    } else {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_DELETED_OF, "<b>" + subject + "</b>", newsOrDiscussion, networkslist.toString()));
                    }
                }
            }
        }
    }

    private void getNetworkComment(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        String typeCode = myUpdate.getTypeCode();
        EdsNewsComment networkNewsView = newsCommentManager.get(myUpdate.getAffectedID());
        String news = "";
        String network = "";
        String creator = "";
        String newsOrDiscussion = activityWfmMessageSource.localize(NEWS);
        boolean canView = true;
        if (item != null && item.getSection() != null && item.getSection().equals("NETWORK")) {
            if (networkNewsView != null && networkNewsView.getNews() != null && networkNewsView.getNews().getNetworks() != null) {
                Set<EdsNetwork> networks = networkNewsView.getNews().getNetworks();
                if (networks.size() != 0) {
                    canView = userCanViewNetworksUpdates(networks, userManager.getUser());
                }
            }
        }
        if (canView) {
            if (networkNewsView != null) {
                news = networkNewsView.getNews().getSubject();
                network = "";
                creator = getUserName(myUpdate, networkNewsView.getUser());
                newsOrDiscussion = networkNewsView.getNews().getBlog() ? activityWfmMessageSource.localize(DISCUSSION) : activityWfmMessageSource.localize(NEWS);
            }
            if (item != null && !"".equals(creator) && !"".equals(network)) {
                if (Constants.NETWORK_BLOG_COMMENTED.equals(typeCode)) {
                    item.setSubType(Constants.NETWORK_BLOG_COMMENTED);
                    if (broadMessage) {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_COMMENTED_OF_NETWORK, "<b>" + creator + "</b>", "<b>" + news + "</b>", newsOrDiscussion, "<b>" + network + "</b>"));
                    } else {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_COMMENTED_OF_NETWORK, "<b>" + news + "</b>", newsOrDiscussion, "<b>" + network + "</b>"));
                    }
                } else if (Constants.NETWORK_BLOG_RATED.equals(typeCode)) {
                    item.setSubType(Constants.NETWORK_BLOG_RATED);
                    if (broadMessage) {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_RATED_NETWORK, "<b>" + creator + "</b>", "<b>" + news + "</b>", newsOrDiscussion, "<b>", network));
                    } else {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_RATED_NETWORK, "<b>" + news + "</b>", newsOrDiscussion, "<b>" + network + "</b>"));
                    }
                }
            }
        }
    }

    private void getNetworkConfirmOrRejectMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        EdsNetworkContact contact = networkContactManager.get(myUpdate.getAffectedID());
        String joinUser = "";
        String network = "";
        boolean canView = true;
        if (contact != null) {
            if (item != null && item.getSection() != null && "NETWORK".equals(item.getSection())) {
                if (contact.getNetwork() != null) {
                    canView = userCanViewNetworkUpdates(contact.getNetwork(), userManager.getUser());
                }
            }

            if (canView) {

                joinUser = getUserName(myUpdate, userManager.get(contact.getUserContactID()));
                network = contact.getNetwork().getName();
            }

            if (item != null && !"".equals(joinUser) && !"".equals(network)) {
                item.setSubType(Constants.NETWORK_CONTACT_JOIN);
                if (contact.isConfirmed()) {
                    if (broadMessage) {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_CONFIRMED_NETWORK, "<b>" + joinUser + "</b>", "<b>" + network + "</b>"));
                    } else {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_CONFIRMED_NETWORK, "<b>" + network + "</b>"));
                    }
                } else {
                    if (broadMessage) {
                        if (contact.getUserContactID().equals(userManager.getUser().getObjectID()) || contact.getUser().getObjectID().equals(userManager.getUser().getObjectID())) {
                            item.setMessage(activityWfmMessageSource.localizeWithParam(REQUESTED_TO_JOIN_NETWORK_HAS_BENN_REJECTED, "<b>" + joinUser + "</b>", "<b>" + network + "</b>"));
                        }
                    } else {
                        String text = "";
                        if (!contact.isInvited()) {
                            text = activityWfmMessageSource.localize(YOU_REJECTED_INVITATION_TO_JOIN);
                        } else {
                            text = activityWfmMessageSource.localize(YOU_WERE_REJECTED_TO_JOIN);
                        }
                        item.setMessage(activityWfmMessageSource.localize(NETWORK, text + "<b>" + network + "</b>"));
                    }
                }
            }
        }
    }

    private void getNetworkJoinUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        EdsNetworkContact joinContact = null;
        if (myUpdate != null) {
            joinContact = networkContactManager.get(myUpdate.getAffectedID());
        }
        String joinUser = "";
        String network = "";
        String creator = "";
        boolean canView = true;
        if (item != null && item.getSection() != null && "NETWORK".equals(item.getSection())) {
            if (joinContact != null && joinContact.getNetwork() != null) {
                canView = userCanViewNetworkUpdates(joinContact.getNetwork(), userManager.getUser());
            }
        }
        if (canView) {
            if (joinContact != null) {
                if (joinContact.getUserContactID() != null) {
                    joinUser = userManager.get(joinContact.getUserContactID()).getFullName();
                }
                if (joinContact.getNetwork() != null && joinContact.getNetwork().getName() != null) {
                    network = joinContact.getNetwork().getName();
                    if (joinContact.getNetwork().getCreator() != null && joinContact.getNetwork().getCreator().getName() != null) {
                        creator = getUserName(myUpdate, joinContact.getNetwork().getCreator());
                    }
                }

            }

            if (item != null) {
                item.setSubType(Constants.NETWORK_CONTACT_JOIN);

                if (!joinUser.equals("") && !network.equals("") && !"".equals(creator)) {
                    if (broadMessage) {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_INVITED_TO_NETWORK, "<b>" + creator + "</b>", "<b>" + joinUser + "</b>", "<b>" + network + "</b>"));
                    } else {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_BENN_INVITED_NETWORK, "<b>" + network + "</b>"));
                    }
                }
            }
        }
    }

    private void getNetworkContactUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item, boolean broadMessage) {
        String typeCode = myUpdate.getTypeCode();
        EdsNetworkContact contact = networkContactManager.get(myUpdate.getAffectedID());
        String userContact = "";
        String network = "";
        String creator = "";
        boolean canView = true;
        if (contact != null) {
            if (item != null && item.getSection() != null && item.getSection().equals("NETWORK")) {
                if (contact.getNetwork() != null) {
                    canView = userCanViewNetworkUpdates(contact.getNetwork(), userManager.getUser());
                }
            }
            if (canView) {
                if (EdsNetworkContact.COMPANY_USER.equals(contact.getType())) {
                    creator = getUserName(myUpdate, contact.getUser());
                    userContact = userManager.get(contact.getUserContactID()).getFullName();
                    network = contact.getNetwork().getName();
                } else if (EdsNetworkContact.CRM_CONTACT.equals(contact.getType())) {
                    userContact = crmContactManager.get(contact.getUserContactID()).getName();
                }
            }
            if (item != null && !"".equals(creator) && !"".equals(userContact) && !"".equals(network)) {
                if (Constants.NETWORK_CONTACT_ADD.equals(typeCode)) {
                    item.setSubType(Constants.NETWORK_CONTACT_ADD);
                    if (broadMessage) {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_TO_NETWORK, "<b>" + creator + " </b>", "<b>" + userContact + "</b>", network));
                    } else {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_ADDED_TO_NETWORK, "<b>" + userContact + "</b>", "<b>" + network + "</b>"));
                    }
                } else if (Constants.NETWORK_CONTACT_DELETE.equals(typeCode)) {
                    item.setSubType(Constants.NETWORK_CONTACT_DELETE);
                    if (broadMessage) {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_FROM_NETWORK, "<b>" + creator + "</b>", "<b>" + userContact + "</b>", "<b>" + network + "</b>"));
                    } else {
                        item.setMessage(activityWfmMessageSource.localizeWithParam(YOU_HAVE_DELETED_FROM_NETWORK, "<b>" + userContact + "</b>", "<b>" + network + "</b>"));
                    }
                }
            }
        }
    }

    private void getCustomFieldUpdateMessage(EdsMyUpdate myUpdate, MyUpdateItem item) {
        String typeCode = myUpdate.getTypeCode();
        EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFManager.get(myUpdate.getAffectedID());

        String customFieldName = "";
        if (customFieldsSettings == null) {
            customFieldName = myUpdateManager.getDeletedUpdateName(myUpdate.getAffectedID(), getParentTypeCodeByCode(myUpdate.getTypeCode()));
            if (customFieldName == null) {
                return;
            }
        } else {
            customFieldName = customFieldsSettings.getFieldName() != null ? customFieldsSettings.getFieldName() : "";
        }
        EdsUser user = userManager.get(myUpdate.getInducerID());
        String userName = getUserName(myUpdate, user);

        switch (typeCode) {
            case COMPANY_CUSTOM_FIELD_ADD -> {
                item.setSubType(MyUpdateItem.ADD);
                item.setTitle(activityWfmMessageSource.localize(COMPANY_CUSTOM_FIELD_ADD));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_ADDED_CUSTOM_FIELD, userName, customFieldName));
            }
            case COMPANY_CUSTOM_FIELD_EDIT -> {
                item.setSubType(MyUpdateItem.EDIT);
                item.setTitle(activityWfmMessageSource.localize(COMPANY_CUSTOM_FIELD_EDIT));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_UPDATED_CUSTOM_FIELD, userName, customFieldName));
            }
            case COMPANY_CUSTOM_FIELD_DELETE -> {
                item.setSubType(MyUpdateItem.DELETE);
                item.setTitle(activityWfmMessageSource.localize(COMPANY_CUSTOM_FIELD_DELETE));
                item.setMessage(activityWfmMessageSource.localizeWithParam(HAS_DELETED_CUSTOM_FIELD, userName, customFieldName));
            }
        }

    }

    private boolean userCanViewNetworksUpdates(Set<EdsNetwork> networks, EdsUser user) {
        for (EdsNetwork network : networks) {
            if (network.isDeleted() != null) {
                if (network.isDeleted()) {
                    continue;
                }
            }
            return network.getCreator() != null && network.getCreator().equals(user) || isUserContact(user, network);
        }
        return false;
    }

    private boolean isUserContact(EdsUser user, EdsNetwork network) {
        if (network.getContacts() != null) {
            Set<EdsNetworkContact> contacts = network.getContacts();
            for (EdsNetworkContact contact : contacts) {
                if (contact.getUserContactID().equals(user.getObjectID())) {
                    return true;
                } else if (contact.getUser().getObjectID().equals(user.getObjectID())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean userCanViewNetworkUpdates(EdsNetwork network, EdsUser user) {
        if (network.isDeleted() != null) {
            if (network.isDeleted()) {
                return false;
            }
        }
        return isUserContact(user, network);

    }

    private String getParentTypeCodeByCode(String typeCode) {
        EdsMyUpdateType edsMyUpdateType = getMyUpdateTypeByCode(typeCode);
        return edsMyUpdateType.getParent().getCode();
    }

    private static String getUserName(EdsMyUpdate myUpdate, EdsUser user) {
        if (myUpdate.isSuperUser()) {
            return defaultSupportName;
        }
        return user != null ? user.getFullName() : "";
    }

    private EdsMyUpdateType getMyUpdateTypeByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return new EdsMyUpdateType();
        }
        return (EdsMyUpdateType) findSingle("SELECT ut FROM EdsMyUpdateType ut WHERE ut.code = ? ", code);
    }

    private String format(String format, final String... args) {
        String retVal = format;
        for (final String current : args) {
            retVal = retVal.replaceFirst("[%][s]", current);
        }
        return retVal;
    }
}
