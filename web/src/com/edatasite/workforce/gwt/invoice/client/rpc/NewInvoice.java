package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductKitItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.REPORTED_ZATCA_STATUS;

/**
 * User: Ruslan Muhammadov
 * Date: 05.03.2009
 * Time: 15:10:59
 */
public class NewInvoice extends Relational implements Serializable, ListingCustomFields {

    private static final String PDF_TEMPLATE_ID = "pdfTemplateID";
    private static final String EMAIL_TEMPLATE_ID = "emailTemplateID";
    private static final String ID = "id";
    private static final String OBJECT_KEY = "objectKey";
    private static final String CLIENT_ID = "clientID";
    private static final String SUPPLIER_ID = "supplierID";
    private static final String BILL_ADDRESS_ID = "billAddressID";
    private static final String MAIL_ADDRESS_ID = "mailAddressID";
    private static final String CLIENT_CONTACT_ID = "clientContactID";
    private static final String CURRENCY_ID = "currencyID";
    private static final String DUE_DAYS = "dueDays";
    private static final String RECURRENCE_PATTERN_ID = "recurrencePatternId";
    private static final String RECURRENCE_NUMBER = "recurrenceNumber";
    private static final String COMPANY_ID = "companyID";
    private static final String USER_ID = "userID";
    private static final String PICK_LIST_ID = "pickListID";
    private static final String OPPORTUNITY_ID = "opportunityID";
    private static final String OPPORTUNITY_NUMBER = "opportunityNumber";
    private static final String CREDITED_INVOICE_ID = "creditedInvoiceID";
    private static final String EXPENSE_ALLOCATION_TYPE = "expenseAllocationType";
    private static final String SHIPPING_METHOD_ID = "shippingMethodID";
    private static final String CLIENT_NAME = "clientName";
    private static final String SUPPLIER_NAME = "supplierName";
    private static final String CLIENT_NUMBER = "clientNumber";
    private static final String CLIENT_CONTACT_EMAIL = "clientContactEmail";
    private static final String PAYMENT_INSTRUCTION = "paymentInstruction";
    private static final String CLIENT_MESSAGE = "clientMessage";
    private static final String CURRENCY_NAME = "currencyName";
    private static final String BASE_CURRENCY_NAME = "baseCurrencyName";
    private static final String CURRENCY_SYMBOL = "currencySymbol";
    private static final String INVOICE_NUMBER = "invoiceNumber";
    private static final String PO_NUMBER = "poNumber";
    private static final String QUOTE_NUMBER = "quoteNumber";
    private static final String QUOTE_NUMBER_CN = "quoteNumberCN";
    private static final String REFERENCE = "reference";
    private static final String BILL_ADDRESS_AS_HTML = "billAddressAsHTML";
    private static final String MAIL_ADDRESS_AS_HTML = "mailAddressAsHTML";
    private static final String COMPANY_MAIL_ADDRESS_AS_HTML = "companyMailAddressAsHTML";
    private static final String TYPE = "type";
    private static final String STATUS_ID = "statusID";
    private static final String STATUS = "status";
    private static final String STATUS_SORDER = "statusSorder";
    private static final String STATUS_CODE = "statusCode";
    private static final String FOUR_DIGIT_NUMBER = "fourDigitNumber";
    private static final String RECURRENCE_PATTERN = "recurrencePattern";
    private static final String INVOICE_CUSTOM_TYPE = "invoiceCustomType";
    private static final String INTRODUCTION = "introduction";
    private static final String SHIPPING_METHOD_NAME = "shippingMethodName";
    private static final String RELATED_PROJECT_NAME = "relatedProjectName";
    private static final String RELATED_PROJECT_NUMBER = "relatedProjectNumber";
    private static final String PROJECT_STATUS_CODE = "PROJECT_STATUS_CODE";
    private static final String ENCRYPTED_LINK = "encryptedLink";
    private static final String DUE_DATE_AS_STRING = "dueDateAsString";
    private static final String TOTAL_AS_STRING = "totalAsString";
    private static final String INVOICE_DATE_AS_STRING = "invoiceDateAsString";
    private static final String TOTAL_IN_INVOICE_AS_STRING = "totalInInvoiceAsString";
    private static final String PAYMENTS_AS_STRING = "paymentsAsString";
    private static final String CREATOR_NAME = "creatorName";
    private static final String NIMBLE_UNIQUE_ID = "nimbleUniqueID";
    private static final String INVENTORY_ITEM_INCLUDED = "inventoryItemIncluded";
    private static final String BOOKKEEP = "bookkeep";
    private static final String IS_CLIENT = "isClient";
    private static final String IS_RECURRING_INVOICE = "isRecurringInvoice";
    private static final String IS_DEBIT_NOTE = "isDebitNote";
    private static final String IS_CREDIT_NOTE = "isCreditNote";
    private static final String IS_PROJECT_BASED_INVOICE = "isProjectBasedInvoice";
    private static final String IS_PURCHASE_CLIENT_ENABLED = "isPurchaseClientEnabled";
    private static final String IS_DOUBLE_APPROVAL_ENABLED = "isDoubleApprovalEnabled";
    private static final String IS_NON_CONVERTED_ITEMS_EXISTS = "isNonConvertedItemsExists";
    private static final String IS_CANCEL_DATE_ENABLED = "isCancelDateEnabled";
    private static final String PO_RELATED_EXPENSE_EXIST = "poRelatedExpenseExist";
    private static final String IS_LUMP_SUM_ENABLED = "isLumpSumEnabled";
    //    private static final String IS_MULTI_CURRENCY_ENABLED = "isMultiCurrencyEnabled";
    private static final String IS_FIXED_ASSET_RELATED = "isFixedAssetRelated";
    private static final String FORCE_VALID_NUMBER_GENERATE = "forceValidNumberGenerate";
    private static final String IS_PRODUCT_SERIALS_ENABLED = "isProductSerialsEnabled";
    private static final String IS_QUOTE_COMISSION_ENABLED = "isQuoteComissionEnabled";
    private static final String IS_RECEIVE_QTY_ACTION = "isReceiveQtyAction";
    private static final String IS_CANCEL_REMAINING_QTY_ENABLED = "isCancelRemainingQtyEnabled";
    private static final String IS_CUSTOM_EXCEL_ENABLED = "isCustomExcelEnabled";
    private static final String IS_MULTI_QUOTE_CONVERT_ENABLED = "isMultiQuoteConvertEnabled";
    private static final String PAYMENT_METHOD_ID = "paymentMethodID";//PO
    private static final String INVOICE_TYPE = "invoiceType";// Product Invoice = 0, Service Invoice = 1
    private static final String PAYMENT_METHOD = "paymentMethod";//PO
    private static final String PAYMENT_TERMS = "paymentTerms";//PO
    private static final String SHIPPING_TERMS = "shippingTerms";//PO
    private static final String CONVERTED_TO_INVOICE = "convertedToInvoice";//SQ, PO
    private static final String CONVERTED_TO_PROJECT = "convertedToProject";//SQ, PO
    private static final String PROGRESS_INVOICING = "progressInvoicing";//SQ
    private static final String EXPAND_PRODUCT_GROUP = "expandProductGroup";
    private static final String TAX_CALCULATION_TYPE = "taxCalculationType";// This is for calculating taxes  NO TAX = 0, TAX INCLUSIVE = 1, TAX EXCLUSIVE = 2
    private static final String PAYMENT_INSTRUCTION_ID = "paymentInstructionID";
    private static final String CONVERTED_ITEM_ID = "convertedItemID";//SQ.ID, PO.ID
    private static final String TARGET_PURCHASE_ORDER_ID = "targetPurchaseOrderID";//PO.ID
    private static final String INVOICE_URL = "invoiceURL";//Invoices url link for overdue invoices
    private static final String FORCE_SAVE = "forceSave";//Default parameter false
    private static final String FROM_SAASU = "fromSaasu";
    private static final String HAS_EXISTING_INVOICES_QUOTES = "hasExistingInvoicesQuotes";
    private static final String FROM_API = "fromApi";
    private static final String INVOICED_ITEM_EXISTS = "isConvertedItemExists";
    private static final String TARGET_GRN_ID = "targetGrnId";//EdsShippingData id
    private static final String PROGRESS_INVOICING_TYPE = "progInvoiceType";
    private static final String ORDER_BASEINVOICE_ORDER_IDS = "orderBaseInvoiceOrderIds";
    private HashMap<String, String> valueMap = null;
    private String customerName;
    private TypeItem typeItem;
    private TypeItem clientItem;//PI, PO
    private SelectItem[] recurrencePatterns;//SI
    private SelectItem relatedProject;//PO,PI
    private SelectItem priceLevel;
    private SelectItem clientDiscount;
    private CurrencyItem[] currencies;//All
    private CurrencyItem baseCurrency;//All
    private SelectItem bankAccount;
    private BankAccount bankAccountItem;
    private SelectItem requisitionedBy;//PO
    private String layoutHTML;
    private DateNonConvertable periodStart;
    private DateNonConvertable periodEnd;
    private DateNonConvertable invoiceDate;
    private DateNonConvertable dueDate;
    private DateNonConvertable shipDate;
    private DateNonConvertable cancelDate;
    private DateNonConvertable currentTime;
    private DateNonConvertable receiveDate;
    private InvoiceTermsItem invoiceTermsItem;
    private BigDecimal subtotal;
    private BigDecimal total;
    private BigDecimal totalDiscount;
    private BigDecimal totalInInvoiceCurrency;
    private BigDecimal comissionAmount;
    private BigDecimal totalTaxes;
    private BigDecimal totalTaxesInInvoiceCurrency;
    private BigDecimal netAmount;
    private BigDecimal netAmountTotal;
    private BigDecimal invoicedAmount;
    private BigDecimal percentage;
    private BigDecimal exchageRate;
    private BigDecimal paidAmount;
    private BigDecimal duePayments;
    private BigDecimal convertedPercent;//SQ
    private BigDecimal convertedAmount;//SQ
    private BigDecimal shippingPrice;
    private BigDecimal previosBalance;
    private BigDecimal paymentsReceived;
    private BigDecimal creditedInvoiceAmount;
    private BigDecimal totalAllocatedAmount;
    private SelectItem creator; //SQ
    private SelectItem purchaseOrderManager;//PO
    private SelectItem currentApproverSelectItem;//SQ, PO
    private FileItem[] attachments;
    private NewInvoiceItem[] items;
    private NewInvoice[] invoicedItems;
    private PaymentItem[] paymentItems;
    private NewInvoiceItem[] purchaseInvoiceItems; //PI
    private HistoryListItem[] historyList;
    private TotalTaxItem[] totalTaxItems;
    private TaxItem shippingTaxItem;
    private RecurrenceJobItem recurrenceJobItem;
    private Integer[] projectIDs;
    private InvoiceNumberData numberData;
    private AccountItem defaultAccountItem;
    private TaxItem defaultTaxItem;
    private DiscountItem defaultDiscountItem;
    private FixedAssetItem fixedAssetItem;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;
    private ArrayList<CompanyCustomFieldItem> systemCustomFields;
    private ArrayList<AllocateComissionItem> allocateComissionItems;
    private HashMap<String, Object> customFieldMap;
    private HashMap<Integer, BigDecimal> allocatedExpenses;
    private Boolean isConvertInvoiceBtnShow;
    private Boolean isPurchaseInvoiceNumberingShow;
    private Boolean isClientApproved;
    private ArrayList<Integer> relatedRFPIDs;
    private ArrayList<Integer> convertedQuoteIDs;
    private boolean salesQuoteTermCopyToSalesInvoice;
    private boolean salesQuoteTermCopyToSalesOrder;
    private boolean salesOrderTermCopyToSalesInvoice;
    private ArrayList<ProductKitItem> productKitItems;
    private ArrayList<AssemblyItem> assemblyItems;
    private String opportunity;
    private String oldStatus;
    private ShippingMethod shippingMethod;
    private ArrayList<NewInvoice> convertedInvoices = new ArrayList<>();
    private BigDecimal billableExpenseAmount;
    private BigDecimal billableExpenseTaxAmount;
    private boolean hasBillableExpense = false;
    private SelectItem markupAccount;
    private BigDecimal markupAmount;
    private ArrayList<BillableExpenseItem> expenses;
    private boolean isPercent;
    private boolean isDeleted;
    private boolean isRevisionHistoryEnabled;
    private RevisionHistoryItem[] revisionHistoryItems;
    private String saasuGUID;
    private Date saasuLastUpdateDate;
    private String saasuLastUpdatedUid;
    private boolean isRoundingModeDisabled;
    private boolean isDoubleTaxEnabled;
    private boolean isDoubleDiscountEnabled;
    private boolean fromQuickbooks;
    private boolean isDeleteAndAddDsiabled;
    private String quickbookInvoiceID;
    private String quickbookEditSequence;
    private Integer quoteId;
    private Integer progressiveInvoiceQuoteId;
    private boolean changedNumber = false;
    private ColumnConfigs[] customItemColumns;
    private PdfTemplateItemList pdfTemplateList;
    private SelectItem[] clientPdfTemplateList;
    private PdfTemplateItemList htmlTemplateList;
    private Integer htmlTemplateId;
    private boolean isSalesOrder;
    private boolean interCompanySales;
    private BigDecimal creditNoteInvoiceTotal;
    private BigDecimal creditNoteInvoiceSubTotal;
    private String relatedInvoiceNumber;
    private Date relatedInvoiceDate;
    private Boolean registeredInterCompanyTransaction;
    private Integer dueDateType;
    private Integer convertPurchaseInvoiceDateType;
    private Date lastUpdateDate;
    private String lastUpdater;
    private AccountItem accountsReceivablePayable;
    private Address billAddress;
    private Address mailAddress;
    private Boolean inTarget;
    private String targetId;
    private String fromQuoteNumber;
    private BigDecimal customerBalance;
    private Integer discountType;
    private BigDecimal discountAmount;
    private Date creationDate;
    private DateNonConvertable quotationDate;
    private ArrayList<NewInvoice> sameProjectInvoices;
    private ArrayList<PaymentItem> projectPrepayments;
    private boolean fromWorkflow;
    private boolean copySOIntroduction;
    private BankTransferNumberData grnNumberData;
    private String shippingLabel;
    private String shippingFourDigitNumber;
    private String shippingNumber;
    //Approvers Mechanism
    private ApproverItemMini currentApprover;
    private ApproverItemMini prevApprover;
    private ReferenceItem overallStatus;
    private ArrayList<ApproverItemMini> approvers = new ArrayList<>();
    private ReceivePaymentData paymentData;
    private ArrayList<SelectItem> paymentMethods;
    private boolean hasAnyPayment;
    private boolean hasAccess = false;
    public ArrayList<NewInvoiceItem> invoiceItemList = new ArrayList<>();
    private HashMap<Integer, ArrayList<ProductSerialItem>> productSerialItems;
    private Integer grnCount;
    private SelectItem placeOfSupply;
    private Integer billOfEntryId;
    private boolean reversechargeApplicable;
    private Long zapierordernumber;
    private String clientVatNumber;
    private boolean isApprover;
    private boolean isApproverSaved;
    private boolean approveForAll;
    private boolean allGdnInvoiced;
    private boolean hasGDN;
    private String clientTrnNumber;
    private Integer typeId;
    private Integer journalId;
    private String name;

    private Integer rentalOrderId;
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private Integer accountId;
    private String projectName;
    private ArrayList<ShippingData> convertedShippingDataList;
    private PdfTemplateItemList progressInvoicePDFTemplateList;
    private ArrayList<RelationItem> convertedRelations;
    private String fromName;
    private boolean fromGdn;
    private BigDecimal amount;
    private String zatcaStatus;
    private String noteReason;
    private Integer paymentTypeCode;
    private String revolutUrl;
    private DateNonConvertable lastGrnDate;
    private String customCrmAccountName;
    private Integer customCrmAccountId;
    private BigDecimal orderDueAmount;
    private Boolean isConverted;
    private Boolean payment;
    private String amazonLink;
    private String shortLink;
    private List<SelectItem> clientOwners;
    private List<Integer> multiProjectId = new ArrayList<>();
    private List<String> multiProjectName = new ArrayList<>();
    private List<String> multiProjectNumber = new ArrayList<>();
    private List<String> multiProjectIdName = new ArrayList<>();
    private List<String> multiProjectNumberName = new ArrayList<>();
    private Double quotePercent;
    Integer calcScale;

    public BigDecimal getOrderDueAmount() {
        return orderDueAmount;
    }

    public void setOrderDueAmount(BigDecimal orderDueAmount) {
        this.orderDueAmount = orderDueAmount;
    }

    public String getNoteReason() {
        return noteReason;
    }

    public Integer getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(Integer paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public void setNoteReason(String noteReason) {
        this.noteReason = noteReason;
    }

    public String getZatcaStatus() {
        return zatcaStatus;
    }

    public void setZatcaStatus(String zatcaStatus) {
        this.zatcaStatus = zatcaStatus;
    }

    public boolean isZatcaReported() {
        boolean taxillaReported = REPORTED_ZATCA_STATUS.equals(getZatcaStatus());
        boolean faiSuccess = Optional.ofNullable(getZatcaStatus())
                .map(String::toLowerCase)
                .filter(s -> s.contains("success"))
                .isPresent();
        return taxillaReported || faiSuccess;
    }

    public HashMap<String, String> getInstance() {
        return valueMap = valueMap == null ? new HashMap<>() : valueMap;
    }

    public String getLayoutHTML() {
        return layoutHTML;
    }

    public void setLayoutHTML(String layoutHTML) {
        this.layoutHTML = layoutHTML;
    }

    public CurrencyItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(CurrencyItem[] currencies) {
        this.currencies = currencies;
    }

    public SelectItem[] getRecurrencePatterns() {
        return recurrencePatterns;
    }

    public void setRecurrencePatterns(SelectItem[] recurrencePatterns) {
        this.recurrencePatterns = recurrencePatterns;
    }

    public SelectItem getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(SelectItem relatedProject) {
        this.relatedProject = relatedProject;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getInvoiceURL() {
        return getString(INVOICE_URL);
    }

    public void setInvoiceURL(String invoiceURL) {
        addString(INVOICE_URL, invoiceURL);
    }

    public Integer getPdfTemplateID() {
        return getInteger(PDF_TEMPLATE_ID);
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        addInteger(PDF_TEMPLATE_ID, pdfTemplateID);
    }

    public Integer getEmailTemplateID() {
        return getInteger(EMAIL_TEMPLATE_ID);
    }

    public void setEmailTemplateID(Integer emailTemplateID) {
        addInteger(EMAIL_TEMPLATE_ID, emailTemplateID);
    }


    public Integer getID() {
        return getInteger(ID);
    }

    public void setID(Integer id) {
        addInteger(ID, id);
    }

    public String getObjectKey() {
        return getString(OBJECT_KEY);
    }

    public void setObjectKey(String objectKey) {
        addString(OBJECT_KEY, objectKey);
    }

    public Integer getClientID() {
        return getInteger(CLIENT_ID);
    }

    public void setClientID(Integer clientID) {
        addInteger(CLIENT_ID, clientID);
    }

    public Integer getSupplierID() {
        return getInteger(SUPPLIER_ID);
    }

    public void setSupplierID(Integer supplierID) {
        addInteger(SUPPLIER_ID, supplierID);
    }

    public Integer getBillAddressID() {
        return getInteger(BILL_ADDRESS_ID);
    }

    public void setBillAddressID(Integer billAddressID) {
        addInteger(BILL_ADDRESS_ID, billAddressID);
    }

    public String getOrderBaseinvoicedOrderIds() {
        return getString(ORDER_BASEINVOICE_ORDER_IDS);
    }

    public void setOrderBaseinvoiceOrderIds(String orderIds) {
        addString(ORDER_BASEINVOICE_ORDER_IDS, orderIds);
    }

    public Integer getMailAddressID() {
        return getInteger(MAIL_ADDRESS_ID);
    }

    public void setMailAddressID(Integer mailAddressID) {
        addInteger(MAIL_ADDRESS_ID, mailAddressID);
    }

    public Integer getClientContactID() {
        return getInteger(CLIENT_CONTACT_ID);
    }

    public void setClientContactID(Integer clientContactID) {
        addInteger(CLIENT_CONTACT_ID, clientContactID);
    }

    public String getClientName() {
        return getString(CLIENT_NAME);
    }

    public void setClientName(String clientName) {
        addString(CLIENT_NAME, clientName);
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }


    public String getSupplierName() {
        return getString(SUPPLIER_NAME);
    }

    public void setSupplierName(String suppName) {
        addString(SUPPLIER_NAME, suppName);
    }

    public String getClientNumber() {
        return getString(CLIENT_NUMBER);
    }

    public void setClientNumber(String clientNumber) {
        addString(CLIENT_NUMBER, clientNumber);
    }

    public String getClientContactEmail() {
        return getString(CLIENT_CONTACT_EMAIL);
    }

    public void setClientContactEmail(String clientContactEmail) {
        addString(CLIENT_CONTACT_EMAIL, clientContactEmail);
    }

    public String getBillAddressAsHTML() {
        return getString(BILL_ADDRESS_AS_HTML);
    }

    public void setBillAddressAsHTML(String billAddressAsHTML) {
        addString(BILL_ADDRESS_AS_HTML, billAddressAsHTML);
    }

    public String getMailAddressAsHTML() {
        return getString(MAIL_ADDRESS_AS_HTML);
    }

    public void setMailAddressAsHTML(String mailAddressAsHTML) {
        addString(MAIL_ADDRESS_AS_HTML, mailAddressAsHTML);
    }

    public String getCompanyMailAddressAsHTML() {
        return getString(COMPANY_MAIL_ADDRESS_AS_HTML);
    }

    public void setCompanyMailAddressAsHTML(String companyMailAddressAsHTML) {
        addString(COMPANY_MAIL_ADDRESS_AS_HTML, companyMailAddressAsHTML);
    }

    public Integer getCurrencyID() {
        return getInteger(CURRENCY_ID);
    }

    public void setCurrencyID(Integer currencyID) {
        addInteger(CURRENCY_ID, currencyID);
    }

    public String getCurrencyName() {
        return getString(CURRENCY_NAME);
    }

    public void setCurrencyName(String currencyName) {
        addString(CURRENCY_NAME, currencyName);
    }

    public String getBaseCurrencyName() {
        return getString(BASE_CURRENCY_NAME);
    }

    public void setBaseCurrencyName(String baseCurrencyName) {
        addString(BASE_CURRENCY_NAME, baseCurrencyName);
    }

    public String getCurrencySymbol() {
        return getString(CURRENCY_SYMBOL);
    }

    public void setCurrencySymbol(String currencySymbol) {
        addString(CURRENCY_SYMBOL, currencySymbol);
    }

    public String getInvoiceNumber() {
        return getString(INVOICE_NUMBER);
    }

    public void setInvoiceNumber(String invoiceNumber) {
        addString(INVOICE_NUMBER, invoiceNumber);
    }

    public String getPoNumber() {
        return getString(PO_NUMBER);
    }

    public void setPoNumber(String poNumber) {
        addString(PO_NUMBER, poNumber);
    }

    public String getQuoteNumber() {
        return getString(QUOTE_NUMBER);
    }

    public void setQuoteNumber(String quoteNumber) {
        addString(QUOTE_NUMBER, quoteNumber);
    }

    public String getQuoteNumberCN() {
        return getString(QUOTE_NUMBER_CN);
    }

    public void setQuoteNumberCN(String quoteNumberCN) {
        addString(QUOTE_NUMBER_CN, quoteNumberCN);
    }

    public String getReference() {
        return getString(REFERENCE);
    }

    public void setReference(String reference) {
        addString(REFERENCE, reference);
    }

    public DateNonConvertable getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(DateNonConvertable periodStart) {
        this.periodStart = periodStart;
    }

    public DateNonConvertable getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(DateNonConvertable periodEnd) {
        this.periodEnd = periodEnd;
    }

    public DateNonConvertable getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(DateNonConvertable invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }

    public DateNonConvertable getShipDate() {
        return shipDate;
    }

    public void setShipDate(DateNonConvertable shipDate) {
        this.shipDate = shipDate;
    }

    public DateNonConvertable getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(DateNonConvertable cancelDate) {
        this.cancelDate = cancelDate;
    }

    public DateNonConvertable getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(DateNonConvertable currentTime) {
        this.currentTime = currentTime;
    }

    public DateNonConvertable getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(DateNonConvertable receiveDate) {
        this.receiveDate = receiveDate;
    }

    public InvoiceTermsItem getInvoiceTermsItem() {
        return invoiceTermsItem;
    }

    public void setInvoiceTermsItem(InvoiceTermsItem invoiceTermsItem) {
        this.invoiceTermsItem = invoiceTermsItem;
    }

    public Integer getDueDays() {
        return getInteger(DUE_DAYS);
    }

    public void setDueDays(Integer dueDays) {
        addInteger(DUE_DAYS, dueDays);
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount != null ? totalDiscount : BigDecimal.ZERO;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getTotalInInvoiceCurrency() {
        return totalInInvoiceCurrency;
    }

    public void setTotalInInvoiceCurrency(BigDecimal totalInInvoiceCurrency) {
        this.totalInInvoiceCurrency = totalInInvoiceCurrency;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getType() {
        return getString(TYPE);
    }

    public void setType(String type) {
        addString(TYPE, type);
    }

    public Integer getStatusID() {
        return getInteger(STATUS_ID);
    }

    public void setStatusID(Integer statusID) {
        addInteger(STATUS_ID, statusID);
    }

    public String getStatus() {
        return getString(STATUS);
    }

    public void setStatus(String status) {
        addString(STATUS, status);
    }

    public String getStatusCode() {
        return getString(STATUS_CODE);
    }

    public void setStatusSorder(String status) {
        addString(STATUS_SORDER, status);
    }

    public String getStatusSorder() {
        return getString(STATUS_SORDER);
    }

    public void setStatusCode(String statusCode) {
        addString(STATUS_CODE, statusCode);
    }

    public BigDecimal getExchageRate() {
        return exchageRate;
    }

    public void setExchageRate(BigDecimal exchageRate) {
        this.exchageRate = exchageRate;
    }

    public String getPaymentInstruction() {
        return getString(PAYMENT_INSTRUCTION);
    }

    public void setPaymentInstruction(String paymentInstruction) {
        addString(PAYMENT_INSTRUCTION, paymentInstruction);
    }

    public Integer getPaymentInstructionID() {
        return getInteger(PAYMENT_INSTRUCTION_ID);
    }

    public void setPaymentInstructionID(Integer paymentInstructionID) {
        addInteger(PAYMENT_INSTRUCTION_ID, paymentInstructionID);
    }

    public String getClientMessage() {
        return getString(CLIENT_MESSAGE);
    }

    public void setClientMessage(String clientMessage) {
        addString(CLIENT_MESSAGE, clientMessage);
    }

    public NewInvoiceItem[] getItems() {
        return items;
    }

    public void setItems(NewInvoiceItem[] items) {
        this.items = items;
    }

    public NewInvoice[] getInvoicedItems() {
        return invoicedItems;
    }

    public void addItem(NewInvoiceItem item) {
        invoiceItemList.add(item);
    }

    public void setInvoicedItems(NewInvoice[] invoicedItems) {
        this.invoicedItems = invoicedItems;
    }

    public HashMap<Integer, ArrayList<ProductSerialItem>> getProductSerialItems() {
        return productSerialItems;
    }

    public void setProductSerialItems(HashMap<Integer, ArrayList<ProductSerialItem>> productSerialItems) {
        this.productSerialItems = productSerialItems;
    }

    public PaymentItem[] getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(PaymentItem[] paymentItems) {
        this.paymentItems = paymentItems;
    }

    public NewInvoiceItem[] getPurchaseInvoiceItems() {
        return purchaseInvoiceItems;
    }

    public void setPurchaseInvoiceItems(NewInvoiceItem[] purchaseInvoiceItems) {
        this.purchaseInvoiceItems = purchaseInvoiceItems;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getDuePayments() {
        return duePayments;
    }

    public void setDuePayments(BigDecimal duePayments) {
        this.duePayments = duePayments;
    }

    public boolean isInventoryItemIncluded() {
        return getBool(INVENTORY_ITEM_INCLUDED);
    }

    public void setInventoryItemIncluded(Boolean inventoryItemIncluded) {
        addBoolean(INVENTORY_ITEM_INCLUDED, inventoryItemIncluded);
    }

    public boolean isConvertedToInvoice() {
        return getBool(CONVERTED_TO_INVOICE);
    }

    public void setConvertedToInvoice(Boolean convertedToInvoice) {
        addBoolean(CONVERTED_TO_INVOICE, convertedToInvoice);
    }

    public boolean isConvertedToProject() {
        return getBool(CONVERTED_TO_PROJECT);
    }

    public void setConvertedToProject(Boolean convertedToProject) {
        addBoolean(CONVERTED_TO_PROJECT, convertedToProject);
    }

    public boolean isProgressInvoicing() {
        return getBool(PROGRESS_INVOICING);
    }

    public void setProgressInvoicing(Boolean progressInvoicing) {
        addBoolean(PROGRESS_INVOICING, progressInvoicing);
    }

    public boolean isExpandProductGroup() {
        return getBool(EXPAND_PRODUCT_GROUP);
    }

    public void setExpandProductGroup(Boolean expandProductGroup) {
        addBoolean(EXPAND_PRODUCT_GROUP, expandProductGroup);
    }

    public Integer getPaymentMethodID() {
        return getInteger(PAYMENT_METHOD_ID);
    }

    public void setPaymentMethodID(Integer paymentMethodID) {
        addInteger(PAYMENT_METHOD_ID, paymentMethodID);
    }

    public String getPaymentMethod() {
        return getString(PAYMENT_METHOD);
    }

    public void setPaymentMethod(String paymentMethod) {
        addString(PAYMENT_METHOD, paymentMethod);
    }

    public String getPaymentTerms() {
        return getString(PAYMENT_TERMS);
    }

    public void setPaymentTerms(String paymentTerms) {
        addString(PAYMENT_TERMS, paymentTerms);
    }

    public String getShippingTerms() {
        return getString(SHIPPING_TERMS);
    }

    public void setShippingTerms(String shippingTerms) {
        addString(SHIPPING_TERMS, shippingTerms);
    }

    public SelectItem getRequisitionedBy() {
        return requisitionedBy;
    }

    public void setRequisitionedBy(SelectItem requisitionedBy) {
        this.requisitionedBy = requisitionedBy;
    }

    public boolean isBookkeep() {
        return getBool(BOOKKEEP);
    }

    public void setBookkeep(Boolean bookkeep) {
        addBoolean(BOOKKEEP, bookkeep);
    }

    public String getFourDigitNumber() {
        return getString(FOUR_DIGIT_NUMBER);
    }

    public void setFourDigitNumber(String fourDigitNumber) {
        addString(FOUR_DIGIT_NUMBER, fourDigitNumber);
    }

    public String getShippingFourDigitNumber() {
        return shippingFourDigitNumber;
    }

    public void setShippingFourDigitNumber(String fourDigitNumber) {
        shippingFourDigitNumber = fourDigitNumber;
    }


    public boolean isClient() {
        return getBool(IS_CLIENT);
    }

    public void setClient(Boolean client) {
        addBoolean(IS_CLIENT, client);
    }

    public HistoryListItem[] getHistoryList() {
        return historyList;
    }

    public void setHistoryList(HistoryListItem[] historyList) {
        this.historyList = historyList;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public Integer[] getProjectIDs() {
        return projectIDs;
    }

    public void setProjectIDs(Integer[] projectIDs) {
        this.projectIDs = projectIDs;
    }

    public Integer getRecurrencePatternId() {
        return getInteger(RECURRENCE_PATTERN_ID);
    }

    public void setRecurrencePatternId(Integer recurrencePatternId) {
        addInteger(RECURRENCE_PATTERN_ID, recurrencePatternId);
    }

    public Integer getRecurrenceNumber() {
        return getInteger(RECURRENCE_NUMBER);
    }

    public void setRecurrenceNumber(Integer recurrenceNumber) {
        addInteger(RECURRENCE_NUMBER, recurrenceNumber);
    }

    public String getRecurrencePattern() {
        return getString(RECURRENCE_PATTERN);
    }

    public void setRecurrencePattern(String recurrencePattern) {
        addString(RECURRENCE_PATTERN, recurrencePattern);
    }

    public InvoiceNumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(InvoiceNumberData numberData) {
        this.numberData = numberData;
    }

    public SelectItem getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(SelectItem bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getInvoiceType() {
        return getInteger(INVOICE_TYPE);
    }

    public void setInvoiceType(Integer invoiceType) {
        addInteger(INVOICE_TYPE, invoiceType);
    }

    public Integer getTaxCalculationType() {
        return getInteger(TAX_CALCULATION_TYPE);
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        addInteger(TAX_CALCULATION_TYPE, taxCalculationType);
    }

    public TotalTaxItem[] getTotalTaxItems() {
        return totalTaxItems;
    }

    public void setTotalTaxItems(TotalTaxItem[] totalTaxItems) {
        this.totalTaxItems = totalTaxItems;
    }

    public TaxItem getShippingTaxItem() {
        return shippingTaxItem;
    }

    public void setShippingTaxItem(TaxItem shippingTaxItem) {
        this.shippingTaxItem = shippingTaxItem;
    }

    public Boolean isConvertInvoiceBtnShow() {
        return isConvertInvoiceBtnShow;
    }

    public void setConvertInvoiceBtnShow(Boolean isConvertInvoiceBtnShow) {
        this.isConvertInvoiceBtnShow = isConvertInvoiceBtnShow;
    }

    public Boolean isPurchaseInvoiceNumberingShow() {
        return isPurchaseInvoiceNumberingShow != null ? isPurchaseInvoiceNumberingShow : false;
    }

    public void setPurchaseInvoiceNumberingShow(Boolean isPurchaseInvoiceNumberingShow) {
        this.isPurchaseInvoiceNumberingShow = isPurchaseInvoiceNumberingShow;
    }

    public Boolean isClientApproved() {
        return isClientApproved;
    }

    public void setClientApproved(Boolean isClientApproved) {
        this.isClientApproved = isClientApproved;
    }

    public String getInvoiceDateAsString() {
        return getString(INVOICE_DATE_AS_STRING);
    }

    public void setInvoiceDateAsString(String invoiceDateAsString) {
        addString(INVOICE_DATE_AS_STRING, invoiceDateAsString);
    }

    public String getDueDateAsString() {
        return getString(DUE_DATE_AS_STRING);
    }

    public void setDueDateAsString(String dueDateAsString) {
        addString(DUE_DATE_AS_STRING, dueDateAsString);
    }

    public String getTotalAsString() {
        return getString(TOTAL_AS_STRING);
    }

    public void setTotalAsString(String totalAsString) {
        addString(TOTAL_AS_STRING, totalAsString);
    }

    public String getTotalInInvoiceAsString() {
        return getString(TOTAL_IN_INVOICE_AS_STRING);
    }

    public void setTotalInInvoiceAsString(String totalInInvoiceAsString) {
        addString(TOTAL_IN_INVOICE_AS_STRING, totalInInvoiceAsString);
    }

    public String getPaymentsAsString() {
        return getString(PAYMENTS_AS_STRING);
    }

    public void setPaymentsAsString(String paymentsAsString) {
        addString(PAYMENTS_AS_STRING, paymentsAsString);
    }

    public Integer getPickListID() {
        return getInteger(PICK_LIST_ID);
    }

    public void setPickListID(Integer pickListID) {
        addInteger(PICK_LIST_ID, pickListID);
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }

    public Integer getCompanyID() {
        return getInteger(COMPANY_ID);
    }

    public void setCompanyID(Integer companyID) {
        addInteger(COMPANY_ID, companyID);
    }

    public Integer getUserID() {
        return getInteger(USER_ID);
    }

    public void setUserID(Integer userID) {
        addInteger(USER_ID, userID);
    }

    public boolean isRecurringInvoice() {
        return getBool(IS_RECURRING_INVOICE);
    }

    public void setRecurringInvoice(Boolean recurringInvoice) {
        addBoolean(IS_RECURRING_INVOICE, recurringInvoice);
    }

    public boolean isCreditNote() {
        return getBool(IS_CREDIT_NOTE);
    }

    public void setCreditNote(Boolean creditNote) {
        addBoolean(IS_CREDIT_NOTE, creditNote);
    }

    public boolean isDebitNote() {
        return getBool(IS_DEBIT_NOTE);
    }

    public void setDebitNote(Boolean debitNote) {
        addBoolean(IS_DEBIT_NOTE, debitNote);
    }

    public BigDecimal getCreditedInvoiceAmount() {
        return creditedInvoiceAmount;
    }

    public void setCreditedInvoiceAmount(BigDecimal creditedInvoiceAmount) {
        this.creditedInvoiceAmount = creditedInvoiceAmount;
    }

    public Integer getCreditedInvoiceID() {
        return getInteger(CREDITED_INVOICE_ID);
    }

    public void setCreditedInvoiceID(Integer creditedInvoiceID) {
        addInteger(CREDITED_INVOICE_ID, creditedInvoiceID);
    }

    public Integer getExpenseAllocationType() {
        return getInteger(EXPENSE_ALLOCATION_TYPE);
    }

    public void setExpenseAllocationType(Integer expenseAllocationType) {
        addInteger(EXPENSE_ALLOCATION_TYPE, expenseAllocationType);
    }

    public Integer getShippingMethodID() {
        return getInteger(SHIPPING_METHOD_ID);
    }

    public void setShippingMethodID(Integer shippingMethodID) {
        addInteger(SHIPPING_METHOD_ID, shippingMethodID);
    }

    public String getShippingMethodName() {
        return getString(SHIPPING_METHOD_NAME);
    }

    public void setShippingMethodName(String shippingMethodName) {
        addString(SHIPPING_METHOD_NAME, shippingMethodName);
    }

    public BigDecimal getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(BigDecimal shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public boolean isProjectBasedInvoice() {
        return getBool(IS_PROJECT_BASED_INVOICE);
    }

    public void setProjectBasedInvoice(Boolean projectBasedInvoice) {
        addBoolean(IS_PROJECT_BASED_INVOICE, projectBasedInvoice);
    }

    public String getRelatedProjectName() {
        return getString(RELATED_PROJECT_NAME);
    }

    public void setRelatedProjectName(String relatedProjectName) {
        addString(RELATED_PROJECT_NAME, relatedProjectName);
    }

    public String getRelatedProjectNumber() {
        return getString(RELATED_PROJECT_NUMBER);
    }

    public void setRelatedProjectNumber(String relatedProjectNumber) {
        addString(RELATED_PROJECT_NUMBER, relatedProjectNumber);
    }

    public String getProjectStatusCode() {
        return getString(PROJECT_STATUS_CODE);
    }

    public void setProjectStatusCode(String projectStatusCode) {
        addString(PROJECT_STATUS_CODE, projectStatusCode);
    }

    public TypeItem getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(TypeItem typeItem) {
        this.typeItem = typeItem;
    }

    public TypeItem getClientItem() {
        return clientItem;
    }

    public void setClientItem(TypeItem clientItem) {
        this.clientItem = clientItem;
    }

    public boolean isPurchaseClientEnabled() {
        return getBool(IS_PURCHASE_CLIENT_ENABLED);
    }

    public void setPurchaseClientEnabled(Boolean purchaseClientEnabled) {
        addBoolean(IS_PURCHASE_CLIENT_ENABLED, purchaseClientEnabled);
    }

//    public Integer getId() {
//        return id;
//    }

//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getIntroduction() {
        return getString(INTRODUCTION);
    }

    public void setIntroduction(String introduction) {
        addString(INTRODUCTION, introduction);
    }

    public BigDecimal getDueAmount() {
        if (getTotalInInvoiceCurrency() == null) {
            return BigDecimal.ZERO;
        }
        return getTotalInInvoiceCurrency()
                .subtract(getPaidAmount() == null ? BigDecimal.ZERO : getPaidAmount())
                .setScale(getCalcScale() != null ? getCalcScale() : AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
    }

    public String getEncryptedLink() {
        return getString(ENCRYPTED_LINK);
    }

    public void setEncryptedLink(String s) {
        addString(ENCRYPTED_LINK, s);
    }

    public AccountItem getDefaultAccountItem() {
        return defaultAccountItem;
    }

    public void setDefaultAccountItem(AccountItem defaultAccountItem) {
        this.defaultAccountItem = defaultAccountItem;
    }

    public SelectItem getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(SelectItem priceLevel) {
        this.priceLevel = priceLevel;
    }

    public SelectItem getClientDiscount() {
        return clientDiscount;
    }

    public void setClientDiscount(SelectItem clientDiscount) {
        this.clientDiscount = clientDiscount;
    }

    public Integer getOpportunityID() {
        return getInteger(OPPORTUNITY_ID);
    }

    public void setOpportunityID(Integer opportunityID) {
        addInteger(OPPORTUNITY_ID, opportunityID);
    }

    public String getOpportunityNumber() {
        return getString(OPPORTUNITY_NUMBER);
    }

    public void setOpportunityNumber(String opportunityNumber) {
        addString(OPPORTUNITY_NUMBER, opportunityNumber);
    }

    public BigDecimal getConvertedPercent() {
        return convertedPercent;
    }

    public void setConvertedPercent(BigDecimal convertedPercent) {
        this.convertedPercent = convertedPercent;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public Integer getConvertedItemID() {
        return getInteger(CONVERTED_ITEM_ID);
    }

    public void setConvertedItemID(Integer convertedItemID) {
        addInteger(CONVERTED_ITEM_ID, convertedItemID);
    }

    public Integer getTargetPurchaseOrderID() {
        return getInteger(TARGET_PURCHASE_ORDER_ID);
    }

    public void setTargetPurchaseOrderID(Integer targetPurchaseOrderID) {
        addInteger(TARGET_PURCHASE_ORDER_ID, targetPurchaseOrderID);
    }

    public void setTargetGrnId(Integer targetGrnId) {
        this.addInteger(TARGET_GRN_ID, targetGrnId);
    }

    public Integer getTargetGrnId() {
        return this.getInteger(TARGET_GRN_ID);
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

//    public SelectItem getManager() {
//        return manager;
//    }
//
//    public void setManager(SelectItem manager) {
//        this.manager = manager;
//    }


    public SelectItem getPurchaseOrderManager() {
        return purchaseOrderManager;
    }

    public void setPurchaseOrderManager(SelectItem purchaseOrderManager) {
        this.purchaseOrderManager = purchaseOrderManager;
    }

    public SelectItem getCurrentApproverSelectItem() {
        return currentApproverSelectItem;
    }

    public void setCurrentApproverSelectItem(SelectItem currentApproverSelectItem) {
        this.currentApproverSelectItem = currentApproverSelectItem;
    }

    public String getCreatorName() {
        return getString(CREATOR_NAME);
    }

    public void setCreatorName(String creatorName) {
        addString(CREATOR_NAME, creatorName);
    }

    public boolean isDoubleApprovalEnabled() {
        return getBool(IS_DOUBLE_APPROVAL_ENABLED);
    }

    public void setDoubleApprovalEnabled(Boolean doubleApprovalEnabled) {
        addBoolean(IS_DOUBLE_APPROVAL_ENABLED, doubleApprovalEnabled);
    }

    public TypeItem getAsTypeItem() {
        TypeItem item = new TypeItem();
        item.setId(getID());
        item.setName(getInvoiceNumber());
        item.setDescription(getStatus());
        item.setCurrency(getCurrencyName());
        item.setEncryptedLink(getEncryptedLink());
        item.setDueDate(getDueDate());
        item.setDueAmount(getDueAmount().doubleValue());
        item.setStatus(getStatus());
        return item;
    }

    public boolean containsOneOfProducts() {
        for (NewInvoiceItem item : items) {
            if (item.getItemID() == null || item.getItemID() == 0) {
                return true;
            }
        }
        return false;
    }

    public BigDecimal getBaseTotalWithoutTaxes(boolean isAgencyFees) {
        if (isAgencyFees) {
            return getTotal();
        }
        return getTotal().subtract(getTotalTaxes() != null ? getTotalTaxes() : BigDecimal.ZERO);
    }

    public BigDecimal getComissionAmount() {
        return comissionAmount;
    }

    public void setComissionAmount(BigDecimal comissionAmount) {
        this.comissionAmount = comissionAmount;
    }

    public Integer getRelatedProjectID() {
        return relatedProject != null ? relatedProject.getId() : null;
    }

    public void setRelatedProjectID(Integer id) {
        if (id != null) {
            this.relatedProject = new SelectItem(id);
        }
    }

    public boolean isNonConvertedItemsExists() {
        return getBool(IS_NON_CONVERTED_ITEMS_EXISTS);
    }

    public void setNonConvertedItemsExists(Boolean nonConvertedItemsExists) {
        addBoolean(IS_NON_CONVERTED_ITEMS_EXISTS, nonConvertedItemsExists);
    }

    public BigDecimal getPreviosBalance() {
        return previosBalance;
    }

    public void setPreviosBalance(BigDecimal previosBalance) {
        this.previosBalance = previosBalance;
    }

    public BigDecimal getPaymentsReceived() {
        return paymentsReceived;
    }

    public void setPaymentsReceived(BigDecimal paymentsReceived) {
        this.paymentsReceived = paymentsReceived;
    }

    public boolean isCancelDateEnabled() {
        return getBool(IS_CANCEL_DATE_ENABLED);
    }

    public void setCancelDateEnabled(Boolean cancelDateEnabled) {
        addBoolean(IS_CANCEL_DATE_ENABLED, cancelDateEnabled);
    }

    public Boolean isSubmitter(Integer userID) {
        return creator != null && creator.getId() != null && creator.getId().equals(userID);
    }

//    public Boolean isCurrentApprover(Integer userID) {
//        return manager != null && manager.getId() != null && manager.getId().equals(userID);
//    }

    public Boolean isCurrentApprover(Integer userID) {
        return currentApproverSelectItem != null && currentApproverSelectItem.getId() != null && currentApproverSelectItem.getId().equals(userID);
    }

    public String getInvoiceCustomType() {
        return getString(INVOICE_CUSTOM_TYPE);
    }

    public void setInvoiceCustomType(String invoiceCustomType) {
        addString(INVOICE_CUSTOM_TYPE, invoiceCustomType);
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldMap != null ? customFieldMap.get(columnCodeKey) : null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldMap.put(columnCodeKey, cellValue);
    }

    public HashMap<String, Object> getCustomFieldMap() {
        return customFieldMap;
    }

    public void setCustomFieldMap(HashMap<String, Object> customFieldMap) {
        this.customFieldMap = customFieldMap;
    }

    public HashMap<Integer, BigDecimal> getAllocatedExpenses() {
        return allocatedExpenses;
    }

    public void setAllocatedExpenses(HashMap<Integer, BigDecimal> allocatedExpenses) {
        this.allocatedExpenses = allocatedExpenses;
    }

    public BigDecimal getTotalAllocatedAmount() {
        return totalAllocatedAmount != null ? totalAllocatedAmount : BigDecimal.ZERO;
    }

    public void setTotalAllocatedAmount(BigDecimal totalAllocatedAmount) {
        this.totalAllocatedAmount = totalAllocatedAmount;
    }

    public boolean isPoRelatedExpenseExist() {
        return getBool(PO_RELATED_EXPENSE_EXIST);
    }

    public void setPoRelatedExpenseExist(Boolean poRelatedExpenseExist) {
        addBoolean(PO_RELATED_EXPENSE_EXIST, poRelatedExpenseExist);
    }

    public boolean isLumpSumEnabled() {
        return getBool(IS_LUMP_SUM_ENABLED);
    }

    public void setLumpSumEnabled(Boolean lumpSumEnabled) {
        addBoolean(IS_LUMP_SUM_ENABLED, lumpSumEnabled);
    }

    /*public boolean isMultiCurrencyEnabled() {
        return getBool(IS_MULTI_CURRENCY_ENABLED);
    }

    public void setMultiCurrencyEnabled(Boolean multiCurrencyEnabled) {
        addBoolean(IS_MULTI_CURRENCY_ENABLED, multiCurrencyEnabled);
    }*/

    public boolean isFixedAssetRelated() {
        return getBool(IS_FIXED_ASSET_RELATED);
    }

    public void setFixedAssetRelated(Boolean fixedAssetRelated) {
        addBoolean(IS_FIXED_ASSET_RELATED, fixedAssetRelated);
    }

    public FixedAssetItem getFixedAssetItem() {
        return fixedAssetItem;
    }

    public void setFixedAssetItem(FixedAssetItem fixedAssetItem) {
        this.fixedAssetItem = fixedAssetItem;
    }

    public BigDecimal getBillableExpenseAmount() {
        return billableExpenseAmount;
    }

    public void setBillableExpenseAmount(BigDecimal billableExpenseAmount) {
        this.billableExpenseAmount = billableExpenseAmount;
    }

    public BigDecimal getBillableExpenseTaxAmount() {
        return billableExpenseTaxAmount;
    }

    public void setBillableExpenseTaxAmount(BigDecimal billableExpenseTaxAmount) {
        this.billableExpenseTaxAmount = billableExpenseTaxAmount;
    }

    public boolean isHasBillableExpense() {
        return hasBillableExpense;
    }

    public void setHasBillableExpense(boolean hasBillableExpense) {
        this.hasBillableExpense = hasBillableExpense;
    }

    public SelectItem getMarkupAccount() {
        return markupAccount;
    }

    public void setMarkupAccount(SelectItem markupAccount) {
        this.markupAccount = markupAccount;
    }

    public BigDecimal getMarkupAmount() {
        return markupAmount;
    }

    public void setMarkupAmount(BigDecimal markupAmount) {
        this.markupAmount = markupAmount;
    }

    public ArrayList<BillableExpenseItem> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<BillableExpenseItem> expenses) {
        this.expenses = expenses;
    }

    public boolean isPercent() {
        return isPercent;
    }

    public void setPercent(boolean percent) {
        isPercent = percent;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isForceSave() {
        return getBool(FORCE_SAVE);
    }

    public void setForceSave(Boolean forceSave) {
        addBoolean(FORCE_SAVE, forceSave);
    }

    public boolean isForceValidNumberGenerate() {
        return getBool(FORCE_VALID_NUMBER_GENERATE);
    }

    public void setForceValidNumberGenerate(Boolean forceValidNumberGenerate) {
        addBoolean(FORCE_VALID_NUMBER_GENERATE, forceValidNumberGenerate);
    }

    public boolean isProductSerialsEnabled() {
        return getBool(IS_PRODUCT_SERIALS_ENABLED);
    }

    public void setProductSerialsEnabled(Boolean productSerialsEnabled) {
        addBoolean(IS_PRODUCT_SERIALS_ENABLED, productSerialsEnabled);
    }

    public boolean isRevisionHistoryEnabled() {
        return isRevisionHistoryEnabled;
    }

    public void setRevisionHistoryEnabled(boolean revisionHistoryEnabled) {
        isRevisionHistoryEnabled = revisionHistoryEnabled;
    }

    public RevisionHistoryItem[] getRevisionHistoryItems() {
        return revisionHistoryItems;
    }

    public void setRevisionHistoryItems(RevisionHistoryItem[] revisionHistoryItems) {
        this.revisionHistoryItems = revisionHistoryItems;
    }

    public boolean isRoundingModeDisabled() {
        return isRoundingModeDisabled;
    }

    public void setRoundingModeDisabled(boolean roundingModeDisabled) {
        isRoundingModeDisabled = roundingModeDisabled;
    }

    public String getSaasuGUID() {
        return saasuGUID;
    }

    public void setSaasuGUID(String saasuGUID) {
        this.saasuGUID = saasuGUID;
    }

    public Date getSaasuLastUpdateDate() {
        return saasuLastUpdateDate;
    }

    public void setSaasuLastUpdateDate(Date saasuLastUpdateDate) {
        this.saasuLastUpdateDate = saasuLastUpdateDate;
    }

    public String getSaasuLastUpdatedUid() {
        return saasuLastUpdatedUid;
    }

    public void setSaasuLastUpdatedUid(String saasuLastUpdatedUid) {
        this.saasuLastUpdatedUid = saasuLastUpdatedUid;
    }

    public boolean isFromSaasu() {
        return getBool(FROM_SAASU);
    }

    public void setFromSaasu(boolean fromSaasu) {
        addBoolean(FROM_SAASU, fromSaasu);
    }

    public boolean isFromApi() {
        return getBool(FROM_API);
    }

    public void setFromApi(boolean fromApi) {
        addBoolean(FROM_API, fromApi);
    }

    public boolean isConvertingFromQuote() {
        return getConvertedItemID() != null && getID() == null;
    }

    public boolean isDoubleTaxEnabled() {
        return isDoubleTaxEnabled;
    }

    public void setDoubleTaxEnabled(boolean doubleTaxEnabled) {
        isDoubleTaxEnabled = doubleTaxEnabled;
    }

    public boolean isDoubleDiscountEnabled() {
        return isDoubleDiscountEnabled;
    }

    public void setDoubleDiscountEnabled(boolean isDoubleDiscountEnabled) {
        this.isDoubleDiscountEnabled = isDoubleDiscountEnabled;
    }

    public String getNimbleUniqueID() {
        return getString(NIMBLE_UNIQUE_ID);
    }

    public void setNimbleUniqueID(String nimbleUniqueID) {
        addString(NIMBLE_UNIQUE_ID, nimbleUniqueID);
    }

    public boolean isFromQuickbooks() {
        return fromQuickbooks;
    }

    public void setFromQuickbooks(boolean fromQuickbooks) {
        this.fromQuickbooks = fromQuickbooks;
    }

    public String getQuickbookInvoiceID() {
        return quickbookInvoiceID;
    }

    public void setQuickbookInvoiceID(String quickbookInvoiceID) {
        this.quickbookInvoiceID = quickbookInvoiceID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public boolean isChangedNumber() {
        return changedNumber;
    }

    public void setChangedNumber(boolean changedNumber) {
        this.changedNumber = changedNumber;
    }

    public ColumnConfigs[] getCustomItemColumns() {
        return customItemColumns;
    }

    public void setCustomItemColumns(ColumnConfigs[] customItemColumns) {
        this.customItemColumns = customItemColumns;
    }

    public ArrayList<AllocateComissionItem> getAllocateComissionItems() {
        return allocateComissionItems;
    }

    public void setAllocateComissionItems(ArrayList<AllocateComissionItem> allocateComissionItems) {
        this.allocateComissionItems = allocateComissionItems;
    }

    public boolean isQuoteComissionEnabled() {
        return getBool(IS_QUOTE_COMISSION_ENABLED);
    }

    public void setQuoteComissionEnabled(Boolean quoteComissionEnabled) {
        addBoolean(IS_QUOTE_COMISSION_ENABLED, quoteComissionEnabled);
    }

    public boolean isReceiveQtyAction() {
        return getBool(IS_RECEIVE_QTY_ACTION);
    }

    public void setReceiveQtyAction(Boolean receiveQtyAction) {
        addBoolean(IS_RECEIVE_QTY_ACTION, receiveQtyAction);
    }

    public boolean isCancelRemainingQtyEnabled() {
        return getBool(IS_CANCEL_REMAINING_QTY_ENABLED);
    }

    public void setCancelRemainingQtyEnabled(Boolean cancelRemainingQtyEnabled) {
        addBoolean(IS_CANCEL_REMAINING_QTY_ENABLED, cancelRemainingQtyEnabled);
    }

    public PdfTemplateItemList getPdfTemplateList() {
        return pdfTemplateList;
    }

    public void setPdfTemplateList(PdfTemplateItemList pdfTemplateList) {
        this.pdfTemplateList = pdfTemplateList;
    }

    public SelectItem[] getClientPdfTemplateList() {
        return clientPdfTemplateList;
    }

    public void setClientPdfTemplateList(SelectItem[] clientPdfTemplateList) {
        this.clientPdfTemplateList = clientPdfTemplateList;
    }

    public PdfTemplateItemList getHtmlTemplateList() {
        return htmlTemplateList;
    }

    public void setHtmlTemplateList(PdfTemplateItemList htmlTemplateList) {
        this.htmlTemplateList = htmlTemplateList;
    }

    public ArrayList<Integer> getRelatedRFPIDs() {
        return relatedRFPIDs;
    }

    public void setRelatedRFPIDs(ArrayList<Integer> relatedRFPIDs) {
        this.relatedRFPIDs = relatedRFPIDs;
    }

    public Boolean isCustomExcelEnabled() {
        return getBoolean(IS_CUSTOM_EXCEL_ENABLED);
    }

    public void setCustomExcelEnabled(Boolean isCustomExcelEnabled) {
        addBoolean(IS_CUSTOM_EXCEL_ENABLED, isCustomExcelEnabled);
    }

    public boolean isSalesOrder() {
        return isSalesOrder;
    }

    public void setSalesOrder(boolean salesOrder) {
        isSalesOrder = salesOrder;
    }

    public BigDecimal getCreditNoteInvoiceTotal() {
        return creditNoteInvoiceTotal;
    }

    public void setCreditNoteInvoiceTotal(BigDecimal creditNoteInvoiceTotal) {
        this.creditNoteInvoiceTotal = creditNoteInvoiceTotal;
    }

    public String getRelatedInvoiceNumber() {
        return relatedInvoiceNumber;
    }

    public BigDecimal getCreditNoteInvoiceSubTotal() {
        return creditNoteInvoiceSubTotal;
    }

    public void setCreditNoteInvoiceSubTotal(BigDecimal creditNoteInvoiceSubTotal) {
        this.creditNoteInvoiceSubTotal = creditNoteInvoiceSubTotal;
    }

    public void setRelatedInvoiceNumber(String relatedInvoiceNumber) {
        this.relatedInvoiceNumber = relatedInvoiceNumber;
    }

    public boolean isMultiQuoteConvertEnabled() {
        return getBool(IS_MULTI_QUOTE_CONVERT_ENABLED);
    }

    public void setMultiQuoteConvertEnabled(Boolean multiQuoteConvertEnabled) {
        addBoolean(IS_MULTI_QUOTE_CONVERT_ENABLED, multiQuoteConvertEnabled);
    }

    public ArrayList<Integer> getConvertedQuoteIDs() {
        return convertedQuoteIDs;
    }

    public void setConvertedQuoteIDs(ArrayList<Integer> convertedQuoteIDs) {
        this.convertedQuoteIDs = convertedQuoteIDs;
    }

    /*public boolean isHasExistingInvoicesQuotes() {
        return getBool(HAS_EXISTING_INVOICES_QUOTES);
    }

    public void setHasExistingInvoicesQuotes(boolean hasExistingInvoicesQuotes) {
        addBoolean(HAS_EXISTING_INVOICES_QUOTES, hasExistingInvoicesQuotes);
    }*/

    public BankAccount getBankAccountItem() {
        return bankAccountItem;
    }

    public void setBankAccountItem(BankAccount bankAccountItem) {
        this.bankAccountItem = bankAccountItem;
    }

    public BigDecimal getTotalTaxesInInvoiceCurrency() {
        return totalTaxesInInvoiceCurrency;
    }

    public void setTotalTaxesInInvoiceCurrency(BigDecimal totalTaxesInInvoiceCurrency) {
        this.totalTaxesInInvoiceCurrency = totalTaxesInInvoiceCurrency;
    }

    @Override
    public Integer getRelationID() {
        return getID();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_SALEQUOTE;
    }

    @Override
    public String getRelationName() {
        return getQuoteNumber();
    }

    public boolean isSalesQuoteTermCopyToSalesInvoice() {
        return salesQuoteTermCopyToSalesInvoice;
    }

    public void setSalesQuoteTermCopyToSalesInvoice(boolean salesQuoteTermCopyToSalesInvoice) {
        this.salesQuoteTermCopyToSalesInvoice = salesQuoteTermCopyToSalesInvoice;
    }

    public boolean isSalesQuoteTermCopyToSalesOrder() {
        return salesQuoteTermCopyToSalesOrder;
    }

    public void setSalesQuoteTermCopyToSalesOrder(boolean salesQuoteTermCopyToSalesOrder) {
        this.salesQuoteTermCopyToSalesOrder = salesQuoteTermCopyToSalesOrder;
    }

    public boolean isSalesOrderTermCopyToSalesInvoice() {
        return salesOrderTermCopyToSalesInvoice;
    }

    public void setSalesOrderTermCopyToSalesInvoice(boolean salesOrderTermCopyToSalesInvoice) {
        this.salesOrderTermCopyToSalesInvoice = salesOrderTermCopyToSalesInvoice;
    }

    public BigDecimal getNetAmountTotal() {
        return netAmountTotal;
    }

    public void setNetAmountTotal(BigDecimal netAmountTotal) {
        this.netAmountTotal = netAmountTotal;
    }

    public ArrayList<AssemblyItem> getAssemblyItems() {
        return assemblyItems;
    }

    public void setAssemblyItems(ArrayList<AssemblyItem> assemblyItems) {
        this.assemblyItems = assemblyItems;
    }

    public ArrayList<ProductKitItem> getProductKitItems() {
        return productKitItems;
    }

    public void setProductKitItems(ArrayList<ProductKitItem> productKitItems) {
        this.productKitItems = productKitItems;
    }

    public boolean isInterCompanySales() {
        return interCompanySales;
    }

    public void setInterCompanySales(boolean interCompanySales) {
        this.interCompanySales = interCompanySales;
    }

    public Boolean isRegisteredInterCompanyTransaction() {
        return registeredInterCompanyTransaction;
    }

    public void setRegisteredInterCompanyTransaction(boolean registeredInterCompanyTransaction) {
        this.registeredInterCompanyTransaction = registeredInterCompanyTransaction;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getDueDateType() {
        return dueDateType;
    }

    public void setDueDateType(Integer dueDateType) {
        this.dueDateType = dueDateType;
    }

    public String getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(String opportunity) {
        this.opportunity = opportunity;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getLastUpdater() {
        return lastUpdater;
    }

    public void setLastUpdater(String lastUpdater) {
        this.lastUpdater = lastUpdater;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public ArrayList<NewInvoice> getConvertedInvoices() {
        return convertedInvoices;
    }

    public void setConvertedInvoices(ArrayList<NewInvoice> convertedInvoices) {
        this.convertedInvoices = convertedInvoices;
    }

    public AccountItem getAccountsReceivablePayable() {
        return accountsReceivablePayable;
    }

    public void setAccountsReceivablePayable(AccountItem accountsReceivablePayable) {
        this.accountsReceivablePayable = accountsReceivablePayable;
    }

    public Address getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(Address billAddress) {
        this.billAddress = billAddress;
    }

    public Address getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(Address mailAddress) {
        this.mailAddress = mailAddress;
    }

    public boolean isInTarget() {
        return inTarget != null && inTarget;
    }

    public void setInTarget(Boolean inTarget) {
        this.inTarget = inTarget;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public boolean isFromWorkflow() {
        return fromWorkflow;
    }

    public void setFromWorkflow(boolean fromWorkflow) {
        this.fromWorkflow = fromWorkflow;
    }
    //Approvers Mechanism


    public boolean isCopySOIntroduction() {
        return copySOIntroduction;
    }

    public void setCopySOIntroduction(boolean copySOIntroduction) {
        this.copySOIntroduction = copySOIntroduction;
    }

    public ArrayList<ApproverItemMini> getApprovers() {
        if (approvers == null) {
            approvers = new ArrayList<>();
        }
        return approvers;
    }

    public void setApprovers(ArrayList<ApproverItemMini> approvers) {
        this.approvers = approvers;
    }

    public ApproverItemMini getCurrentApprover() {
        return currentApprover;
    }

    public ReferenceItem getCurrentStatus() {
        if (getCurrentApprover() != null) {
            return getCurrentApprover().getStatus();
        }
        return null;
    }

    public void setCurrentApprover(ApproverItemMini currentApprover) {
        this.currentApprover = currentApprover;
    }

    public ApproverItemMini getPrevApprover() {
        return prevApprover;
    }

    public void setPrevApprover(ApproverItemMini prevApprover) {
        this.prevApprover = prevApprover;
    }

    public ReferenceItem getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(ReferenceItem overallStatus) {
        this.overallStatus = overallStatus;
    }

    public BigDecimal getCustomerBalance() {
        return customerBalance;
    }

    public void setCustomerBalance(BigDecimal customerBalance) {
        this.customerBalance = customerBalance;
    }

    public Integer getConvertPurchaseInvoiceDateType() {
        return convertPurchaseInvoiceDateType;
    }

    public void setConvertPurchaseInvoiceDateType(Integer convertPurchaseInvoiceDateType) {
        this.convertPurchaseInvoiceDateType = convertPurchaseInvoiceDateType;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getFromQuoteNumber() {
        return fromQuoteNumber;
    }

    public void setFromQuoteNumber(String fromQuoteNumber) {
        this.fromQuoteNumber = fromQuoteNumber;
    }

    public DateNonConvertable getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(DateNonConvertable quotationDate) {
        this.quotationDate = quotationDate;
    }

    public ArrayList<NewInvoice> getSameProjectInvoices() {
        if (sameProjectInvoices == null) {
            sameProjectInvoices = new ArrayList<>();
        }
        return sameProjectInvoices;
    }

    public void setSameProjectInvoices(ArrayList<NewInvoice> sameProjectInvoices) {
        this.sameProjectInvoices = sameProjectInvoices;
    }

    public ArrayList<PaymentItem> getProjectPrepayments() {
        if (projectPrepayments == null) {
            projectPrepayments = new ArrayList<>();
        }
        return projectPrepayments;
    }

    public void setProjectPrepayments(ArrayList<PaymentItem> projectPrepayments) {
        this.projectPrepayments = projectPrepayments;
    }

    public boolean getInvoicedItemsExist() {
        return getBool(INVOICED_ITEM_EXISTS);
    }

    public void setInvoicedItemsExist(Boolean invoicedItemsExist) {
        addBoolean(INVOICED_ITEM_EXISTS, invoicedItemsExist);
    }

    public BankTransferNumberData getGrnNumberData() {
        return grnNumberData;
    }

    public void setGrnNumberData(BankTransferNumberData grnNumberData) {
        this.grnNumberData = grnNumberData;
    }

    public void setShippingLabel(String shippingLabel) {
        this.shippingLabel = shippingLabel;
    }

    public String getShippingLabel() {
        return shippingLabel;
    }

    public void setShippingNumber(String shippingNumber) {
        this.shippingNumber = shippingNumber;
    }

    public String getShippingNumber() {
        return shippingNumber;
    }

    public Integer getHtmlTemplateId() {
        return htmlTemplateId;
    }

    public void setHtmlTemplateId(Integer htmlTemplateId) {
        this.htmlTemplateId = htmlTemplateId;
    }

    public void setPaymentData(ReceivePaymentData paymentData) {
        this.paymentData = paymentData;
    }

    public ReceivePaymentData getPaymentData() {
        return paymentData;
    }

    public ArrayList<SelectItem> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(ArrayList<SelectItem> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public void setAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public void setProgressInvoicingType(String type) {
        addString(PROGRESS_INVOICING_TYPE, type);
    }

    public String getProgressInvoicingType() {
        return getString(PROGRESS_INVOICING_TYPE);
    }

    public boolean hasAnyPayment() {
        return hasAnyPayment;
    }

    public void setAnyPaymentExists(boolean hasAnyPayment) {
        this.hasAnyPayment = hasAnyPayment;
    }

    public Integer getGrnCount() {
        return grnCount;
    }

    public void setGrnCount(Integer grnCount) {
        this.grnCount = grnCount;
    }

    public SelectItem getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(SelectItem placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public Integer getBillOfEntryId() {
        return billOfEntryId;
    }

    public void setBillOfEntryId(Integer billOfEntryId) {
        this.billOfEntryId = billOfEntryId;
    }

    public boolean isReversechargeApplicable() {
        return reversechargeApplicable;
    }

    public void setReversechargeApplicable(boolean reversechargeApplicable) {
        this.reversechargeApplicable = reversechargeApplicable;
    }

    public Long getZapierordernumber() {
        return zapierordernumber;
    }

    public void setZapierordernumber(Long zapierordernumber) {
        this.zapierordernumber = zapierordernumber;
    }

    public String getClientVatNumber() {
        return clientVatNumber;
    }

    public void setClientVatNumber(String clientVatNumber) {
        this.clientVatNumber = clientVatNumber;
    }

    public boolean isApprover() {
        return isApprover;
    }

    public void setApprover(boolean approver) {
        isApprover = approver;
    }

    public boolean isApproverSaved() {
        return isApproverSaved;
    }

    public void setApproverSaved(boolean approverSaved) {
        isApproverSaved = approverSaved;
    }

    public boolean isApproveForAll() {
        return approveForAll;
    }

    public void setApproveForAll(boolean approveForAll) {
        this.approveForAll = approveForAll;
    }

    public boolean isAllGdnInvoiced() {
        return this.allGdnInvoiced;
    }

    public void setAllGdnInvoiced(final boolean allGdnInvoiced) {
        this.allGdnInvoiced = allGdnInvoiced;
    }

    public boolean isHasGDN() {
        return hasGDN;
    }

    public void setHasGDN(boolean hasGDN) {
        this.hasGDN = hasGDN;
    }


    public String getClientTrnNumber() {
        return clientTrnNumber;
    }

    public void setClientTrnNumber(String clientTrnNumber) {
        this.clientTrnNumber = clientTrnNumber;
    }

    public Integer getTypeId() {
        return this.typeId;
    }

    public void setTypeId(final Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getJournalId() {
        return this.journalId;
    }

    public void setJournalId(final Integer journalId) {
        this.journalId = journalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ArrayList<ShippingData> getConvertedShippingDataList() {
        return this.convertedShippingDataList;
    }

    public void setConvertedShippingDataList(final ArrayList<ShippingData> convertedShippingDataIds) {
        this.convertedShippingDataList = convertedShippingDataIds;
    }

    public PdfTemplateItemList getProgressInvoicePDFTemplateList() {
        return progressInvoicePDFTemplateList;
    }

    public void setProgressInvoicePDFTemplateList(PdfTemplateItemList progressInvoicePDFTemplateList) {
        this.progressInvoicePDFTemplateList = progressInvoicePDFTemplateList;
    }

    public ArrayList<RelationItem> getConvertedRelations() {
        return this.convertedRelations;
    }

    public void setConvertedRelations(final ArrayList<RelationItem> convertedRelations) {
        this.convertedRelations = convertedRelations;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromName(final String fromName) {
        this.fromName = fromName;
    }

    public boolean isFromGdn() {
        return this.fromGdn;
    }

    public void setFromGdn(final boolean fromGdn) {
        this.fromGdn = fromGdn;
    }

    public ArrayList<CompanyCustomFieldItem> getSystemCustomFields() {
        return systemCustomFields;
    }

    public void setSystemCustomFields(ArrayList<CompanyCustomFieldItem> systemCustomFields) {
        this.systemCustomFields = systemCustomFields;
    }

    public Date getRelatedInvoiceDate() {
        return relatedInvoiceDate;
    }

    public void setRelatedInvoiceDate(Date relatedInvoiceDate) {
        this.relatedInvoiceDate = relatedInvoiceDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRevolutUrl() {
        return revolutUrl;
    }

    public void setRevolutUrl(String revolutUrl) {
        this.revolutUrl = revolutUrl;
    }

    public DateNonConvertable getLastGrnDate() {
        return lastGrnDate;
    }

    public void setLastGrnDate(DateNonConvertable lastGrnDate) {
        this.lastGrnDate = lastGrnDate;
    }

    public String getCustomCrmAccountName() {
        return customCrmAccountName;
    }

    public void setCustomCrmAccountName(String customCrmAccountName) {
        this.customCrmAccountName = customCrmAccountName;
    }

    public Integer getCustomCrmAccountId() {
        return customCrmAccountId;
    }

    public void setCustomCrmAccountId(Integer customCrmAccountId) {
        this.customCrmAccountId = customCrmAccountId;
    }

    public DiscountItem getDefaultDiscountItem() {
        return defaultDiscountItem;
    }

    public void setDefaultDiscountItem(DiscountItem defaultDiscountItem) {
        this.defaultDiscountItem = defaultDiscountItem;
    }

    public void setIsDeleteAndAddDsiabled(boolean isDeleteAndAddDsiabled) {
        this.isDeleteAndAddDsiabled = isDeleteAndAddDsiabled;
    }

    public boolean isDeleteAndAddDsiabled() {
        return isDeleteAndAddDsiabled;
    }

    public Integer getProgressiveInvoiceQuoteId() {
        return progressiveInvoiceQuoteId;
    }

    public void setProgressiveInvoiceQuoteId(Integer progressiveInvoiceQuoteId) {
        this.progressiveInvoiceQuoteId = progressiveInvoiceQuoteId;
    }

    public Boolean getConverted() {
        return isConverted;
    }

    public void setConverted(Boolean converted) {
        isConverted = converted;
    }

    public String getAmazonLink() {
        return amazonLink;
    }

    public void setAmazonLink(String amazonLink) {
        this.amazonLink = amazonLink;
    }

    public Integer getCalcScale() {
        return calcScale;
    }

    public void setCalcScale(Integer calcScale) {
        this.calcScale = calcScale;
    }

    public TaxItem getDefaultTaxItem() {
        return defaultTaxItem;
    }

    public void setDefaultTaxItem(TaxItem defaultTaxItem) {
        this.defaultTaxItem = defaultTaxItem;
    }

    public Integer getRentalOrderId() {
        return rentalOrderId;
    }

    public void setRentalOrderId(Integer rentalOrderId) {
        this.rentalOrderId = rentalOrderId;
    }

    public List<SelectItem> getClientOwners() {
        return clientOwners;
    }

    public void setClientOwners(List<SelectItem> clientOwners) {
        this.clientOwners = clientOwners;
    }

    public List<Integer> getMultiProjectId() {
        return multiProjectId;
    }

    public void setMultiProjectId(List<Integer> multiProjectId) {
        this.multiProjectId = multiProjectId;
    }

    public List<String> getMultiProjectName() {
        return multiProjectName;
    }

    public void setMultiProjectName(List<String> multiProjectName) {
        this.multiProjectName = multiProjectName;
    }

    public List<String> getMultiProjectNumber() {
        return multiProjectNumber;
    }

    public void setMultiProjectNumber(List<String> multiProjectNumber) {
        this.multiProjectNumber = multiProjectNumber;
    }

    public List<String> getMultiProjectIdName() {
        return multiProjectIdName;
    }

    public void setMultiProjectIdName(List<String> multiProjectIdName) {
        this.multiProjectIdName = multiProjectIdName;
    }

    public List<String> getMultiProjectNumberName() {
        return multiProjectNumberName;
    }

    public void setMultiProjectNumberName(List<String> multiProjectNumberName) {
        this.multiProjectNumberName = multiProjectNumberName;
    }

    public Double getQuotePercent() {
        return quotePercent;
    }

    public void setQuotePercent(Double quotePercent) {
        this.quotePercent = quotePercent;
    }

    public Boolean hasPayment() {
        return payment != null && payment;
    }

    public void setHasPayment(Boolean payment) {
        this.payment = payment;
    }
}
