package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.FixedAssetDisposeDialogBox;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.KsaPlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.PlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.UaePlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.MultiQuoteConvertItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CrmAccountWidgets;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ExpenseMarkupPopup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.IntroductionPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PdfTemplatePanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.historyNote.InvoiceNoteHistoryWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ItemQtyPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_INVOICE_HISTORY_NOTES;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_INVOICE_UPLOAD_FILES;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/23/12
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesInvoiceView extends FooteredView implements Colapse, FittedContent, AccountingCustomFormConstants, Constants, AccountingConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final String salesInvoiceView = "sales_invoice_view_";
    private static String invoiceCustomType;
    private final DateTimeFormat dateFormatForParse = DateTimeFormat.getFormat("MM.dd.yyyy");
    private final Date currentDate = new Date();
    public DataListBox taxCalcTypeListBox;
    protected WfmButton2 assignSerialsButton;
    private Integer objectID;
    private Date conversionDate;
    private InvoiceNumberData invoiceNumberData;
    private DataListBox invoiceTypeListBox;
    private CrmAccountLookUp crmAccountLookUp;
    private TextBox numberTxtBox;
    private TextBox quoteNumberTxtBox;
    private TextBox poNumberTxtBox;
    private TextBox referenceTxtBox;
    private DatePicker invoiceDate;
    private TermsAndDuePanel termsAndDuePanel;
    private ProjectLookUp relatedProject;
    private DataListBox priceLevel;
    private WfmDropdown clientDiscount;
    //Custom Fields
    private DatePicker periodStart;
    private DatePicker periodEnd;
    private TextBox previousBalance;
    private TextBox paymentsReceived;
    private HTML paymentAdjustmentLabel;
    private CrmAccountWidgets crmAccountWidgets;
    private CurrencyWidget currencyWidget;
    private RecurringWidget recurringWidget;
    private IntroductionPanel introductionPanel;
    private ProductsTable productsTable;
    private ShippingLabelDialogBox shippingLabelDialogBox;
    private DataListBox bankAccounts;
    private AccountsReceivablePayableLookUp receivablePayableLookUp;
    private MaterialLink customerBalanceLink;

    //fields for progress invoicing
    private BigDecimal quotePercent;
    private BigDecimal quoteAmount;
    private HashMap<Integer, BigDecimal> convertMap;
    private String progressInvoicingType;
    private boolean isSaleOrder = false;
    private FooterUploadPanel footerUploadPanel;
    private InvoiceNoteHistoryWidget notesWidget;
    private WfmButton2 saveButton, submitButton/*, addBillableExpense*/;
    private FooterInformer addBillableExpense;
    private SplitButton pdfVersionButton;
    private SplitButton approveButton;
    private final ArrayList<RelationItem> relations = new ArrayList<>();
    private Integer relationID;
    private String relationType;
    private String relationName;
    private Params formParameters;
    private HashMap<String, Widget> widgetsMap;
    private SalesInvoiceFormPresenter formPresenter;
    private HTMLPanel htmlPanel;
    private ExpenseMarkupPopup expenseMarkupPopup;
    private MaterialLink showMoreLink;
    private InvoiceAdvancedOptions advancedOptions;
    private FormGroup priceLevelField;
    private HorizontalPanel dueAndTearmsPanel;
    private FixedAssetItem fixedAssetItem;
    private ChosenApproversWidget approver;
    private boolean hasApproval;

    private PdfTemplatePanel pdfTemplatePanel;
    private String invoiceTypeCode;
    private String[] params;

    private Command onProjectBaseInvoiceInit;
    private Integer[] projectIDs;
    private final boolean isAlmadarSerials = Utils.hasGenericAccess(GenericSettingsEnum.ALMADAR_PRODUCT_SERIAL_ENABLED);
    private PlaceOfSupplyWidget placeOfSupplyWidget;
    private StringBuilder orderIds;
    private KpiCheckBox reverseChargeBox;

    public SalesInvoiceView(String[] params, String invoiceType) {
        super(RECURRING_INVOICE.equals(invoiceType) ? "recurringinvoiceadd" : "saleinvoiceadd");
        setDescription(RECURRING_INVOICE.equals(invoiceType) ? Property.get(Constants.RECURRING_INVOICE, wfmStrings.addMess(), accountingStrings.recurringInvoice()) : getInvoiceDescription(invoiceType, property));
        this.invoiceTypeCode = invoiceType;
        this.params = params;
        initFormParameters(params);
        formParameters.setRecurringInvoice(RECURRING_INVOICE.equals(invoiceType));
    }

    public SalesInvoiceView(Integer objectID, String[] params, boolean isRecurringInvoice) {
        super("edit");
        setDescription(property.getSingular(accountingStrings.editSalesInvoice(), wfmStrings.salesInvoice()));
        this.objectID = objectID;
        if (!isRecurringInvoice && AccountingUtils.get().enableInvoiceCustomTypes() && params.length > 1) {
            invoiceCustomType = params[1];
        }
        if (isRecurringInvoice) {
            this.invoiceTypeCode = RECURRING_INVOICE;
        }
        initFormParameters(params);
        formParameters.setRecurringInvoice(isRecurringInvoice);
    }

    public SalesInvoiceView(boolean isProjectBasedInvoice) {
        this(null, SALE_INVOICE);
        formParameters.setProjectBasedInvoice(isProjectBasedInvoice);
    }

    private static String getInvoiceDescription(String invoiceType, Property property) {
        if (!AccountingUtils.get().enableInvoiceCustomTypes()) {
            return property.getSingular(wfmStrings.addMess(), wfmStrings.salesInvoice());
        }

        if (DOLLAR_INVOICE.equals(invoiceType)) {
            invoiceCustomType = DOLLAR_INVOICE;
            return accountingStrings.addDollarInvoice();
        } else if (SERVICE_INVOICE.equals(invoiceType)) {
            invoiceCustomType = SERVICE_INVOICE;
            return accountingStrings.addServiceInvoice();
        } else {
            invoiceCustomType = PRODUCT_INVOICE;
            return accountingStrings.addProductInvoice();
        }
    }

    private void initFormParameters(String[] params) {
        formParameters = new Params();
        formParameters.setObjectID(objectID);
        formParameters.setType(RECEIVABLE);
        formParameters.setFormType(SALE_INVOICE);
        formParameters.setInvoiceCustomType(invoiceCustomType);
        if (params != null) {
            if (params.length == 3 && "CONVERT".equals(params[0])) {
                formParameters.setConvertFormType(params[1]);
                if (params[2] != null && params[2].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                    formParameters.setConvertFormId(Integer.parseInt(params[2]));
                }
            } else if (params.length > 2 && (params[1].equals("lead") || params[1].equals("account") || params[1].equals("contact") || params[1].equals("opportunity"))) {
                formParameters.setExternalFormID(COPY_FROM_CRM_ACCOUNT);
                formParameters.setCrmFormName(params[1]);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
                if ("opportunity".equals(params[1])) {
                    formParameters.setOpportunityID(Integer.valueOf(params[2]));
                    relationID = Integer.valueOf(params[2]);
                    relationType = RelationItem.TYPE_OPPORTUNITY;
                }
                if ("lead".equals(params[1]) || (params.length > 4 && RelationItem.TYPE_LEAD.equals(params[4]))) {
                    relationID = Integer.parseInt(params["lead".equals(params[1]) ? 2 : 3]);
                    relationType = RelationItem.TYPE_LEAD;
                } else if (params.length > 3) {
                    relationID = Integer.parseInt(params[3]);
                    relationType = RelationItem.TYPE_CONTACT;
                }
            } else if (params.length > 2 && params[1].equals("copyFromExistingData")) {
                formParameters.setExternalFormID(COPY_FROM_EXISTING_DATA);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && params[1].equals("copyToSaleInvoice")) {
                formParameters.setConvertFormType(params[1]);
                formParameters.setExternalFormID(COPY_FROM_PI_TO_SI);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && params[1].equals("fromClientList")) {
                formParameters.setExternalFormID(COPY_FROM_CLIENT_SUPPLIER);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
                if (params.length > 4 && params[3].equals("reservationID")) {
                    formParameters.setReservationID(Integer.valueOf(params[4]));
                }
            } else if (params.length > 2 && "relatedProject".equals(params[1])) {
                if (params[2] != null && params[3] != null) {
                    formParameters.setExternalFormID(COPY_FROM_CLIENT_SUPPLIER);
                    formParameters.setExternalObjectID(Integer.valueOf(params[3]));
                    formParameters.setRelatedProjectID(Integer.valueOf(params[2]));
                }
            } else if (params.length > 2 && "progressInvoicing".equals(params[1])) {
                formParameters.setExternalFormID(PROGRESS_INVOICING);
                if ("byPercent".equals(params[2])) {
                    formParameters.setExternalObjectID(Integer.valueOf(params[3]));
                    quotePercent = AccountingUtils.get().parseToBigDecimal(params[4]);
                    progressInvoicingType = BY_PERCENTAGE;
                } else if ("byAmount".equals(params[2])) {
                    formParameters.setExternalObjectID(Integer.valueOf(params[3]));
                    quoteAmount = AccountingUtils.get().parseToBigDecimal(params[4]);
                    progressInvoicingType = BY_AMOUNT;
                } else if (params.length > 4) {
                    formParameters.setExternalObjectID(Integer.valueOf(params[3]));
                    progressInvoicingType = BY_ITEM;
                    String[] keyValues = params[4].split(";");
                    convertMap = new HashMap<>();
                    for (String kv : keyValues) {
                        String[] idQty = kv.split(",");
                        convertMap.put(Integer.parseInt(idQty[0]), AccountingUtils.get().parseToBigDecimal(idQty[1]).setScale(10, RoundingMode.HALF_UP));
                    }
                }
                formParameters.setProgressiveInvoiceType(progressInvoicingType);
                if (params.length > 5 && params[5] != null && !"".equals(params[5]) && params[5].equals("saleOrder")) {
                    isSaleOrder = true;
                }
            } else if (params.length > 2 && "copyFromFixedAsset".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_FIXED_ASSET);
                formParameters.setExternalObjectID(Integer.parseInt(params[2]));
                fixedAssetItem = FixedAssetDisposeDialogBox.fixedAssetItem;
            } else if (params.length > 2 && "convertToInvoice".equals(params[1])) {
                formParameters.setExternalFormID(CONVERT_TO_INVOICE);
                formParameters.setExternalObjectID(Integer.parseInt(params[2]));
            } else if (params.length > 2 && "convertToInvoiceFromRentalOrder".equals(params[1])) {
                formParameters.setExternalFormID(CONVERT_TO_INVOICE_FROM_RENTAL_ORDER);
                formParameters.setExternalObjectID(Integer.parseInt(params[2]));
            } else if (params.length > 2 && "convertFromGrn".equals(params[1])) {
                formParameters.setExternalFormID(CONVERT_TO_INVOICE_FROM_GRN);
                formParameters.setExternalObjectID(Integer.parseInt(params[2]));
            } else if (params.length > 2 && "multiQuoteConvert".equals(params[1])) {
                MultiQuoteConvertItem convertItem = new MultiQuoteConvertItem();
                orderIds = new StringBuilder();
                formParameters.setExternalFormID(CONVERT_MULTI_QUOTE_TO_INVOICE);
                String[] quotes = params[2].split(";");
                orderIds.append(quotes);
                String[] idBool;
                for (String str : quotes) {
                    idBool = str.split(",");
                    convertItem.getQuoteIds().add(Integer.parseInt(idBool[0]));
                    if ("true".equals(idBool[1])) {
                        convertItem.setSelectedProjectQuoteId(Integer.parseInt(idBool[0]));
                    }
                }
                formParameters.setMultiQuoteConvertItem(convertItem);
            } else {
                if (params.length > 1) {
                    if ("projectbased".equals(params[1])) {
                        formParameters.setProjectBasedInvoice(true);
                    } else {
                        try {
                            formParameters.setMaximalAmount(AccountingUtils.get().parseToBigDecimal(params[1]));
                        } catch (Exception e) {
                            formParameters.setMaximalAmount(null);
                        }
                    }
                }
                if (params.length > 2 && !"projectbased".equals(params[1])) {
                    formParameters.setConversionDate(dateFormatForParse.parse(params[2]));
                }
                formParameters.setFromGettingStarted(formParameters.getMaximalAmount() != null && formParameters.getConversionDate() != null);
            }
        }
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);

        invoiceTypeListBox = new DataListBox();
        invoiceTypeListBox.ensureDebugId(salesInvoiceView + "invoiceTypeListBox");
        invoiceTypeListBox.setWithoutNullLabel(true);
        invoiceTypeListBox.setItems(new SelectItem[]{
                new SelectItem(PRODUCT_INVOICE_TYPE, accountingStrings.productInvoice()),
                new SelectItem(SERVICE_INVOICE_TYPE, accountingStrings.serviceInvoice())
        });
        invoiceTypeListBox.setSelected(1);
        invoiceTypeListBox.addValueChangeHandler(ch -> onInvoiceTypeChange());

        if (relationID != null && relationType != null) {
            getRelationName(relationID, relationType);
        }


        boolean hasPermissonCustomerQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_QUICK_ADD);
        boolean hasPermissonCustomerAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_ADD);

        crmAccountLookUp = new SmartCrmAccountLookup(CrmAccountLookUp.CUSTOMER, true, () -> {
            if (hasPermissonCustomerQuick) {
                new CusSuppQuickAddView(CrmAccountLookUp.CUSTOMER, crmAccountLookUp.getLastValueBeforeClick());
            } else if (hasPermissonCustomerAdd) {
                SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add");
            }
        }, false, hasPermissonCustomerQuick || hasPermissonCustomerAdd, true);
        crmAccountLookUp.ensureDebugId(salesInvoiceView + "crmAccountLookUp");
        crmAccountLookUp.setAutocompleteOff();

        crmAccountWidgets = new CrmAccountWidgets(crmAccountLookUp, RECEIVABLE, SALE_INVOICE, formParameters.isEditForm());
        if (formParameters.isExternalForm(CONVERT_TO_INVOICE) || formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_GRN)|| formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_RENTAL_ORDER) || PROGRESS_INVOICING.equals(formParameters.getExternalFormID())) {
            crmAccountLookUp.setEnabled(false);
        }

        currencyWidget = new CurrencyWidget(!formParameters.isEditForm() &&
                !formParameters.isExternalForm(PROGRESS_INVOICING) &&
                !formParameters.isExternalForm(CONVERT_TO_INVOICE) &&
                !formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_GRN) &&
                !formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_RENTAL_ORDER));

        relatedProject = new ProjectLookUp(RECEIVABLE, crmAccountLookUp);
        relatedProject.ensureDebugId(salesInvoiceView + "relatedProject");
        relatedProject.setEnsureDebugId(salesInvoiceView + "relatedProject");

        previousBalance = new TextBox();
        previousBalance.ensureDebugId(salesInvoiceView + "previousBalance");

        paymentsReceived = new TextBox();
        paymentsReceived.ensureDebugId(salesInvoiceView + "paymentsReceived");
        previousBalance.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        paymentsReceived.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        paymentAdjustmentLabel = new HTML();

        periodStart = new DatePicker();
        periodStart.ensureDebugId(salesInvoiceView + "periodStart");

        periodEnd = new DatePicker();
        periodEnd.ensureDebugId(salesInvoiceView + "periodEnd");

        invoiceDate = new DatePicker(currentDate, true);
        invoiceDate.ensureDebugId(salesInvoiceView + "invoiceDate");

        termsAndDuePanel = new TermsAndDuePanel(wfmStrings.dueDate());
        termsAndDuePanel.ensureDebugId(salesInvoiceView + "termsAndDuePanel");

        numberTxtBox = new TextBox();
        numberTxtBox.ensureDebugId(salesInvoiceView + "numberTxtBox");

        quoteNumberTxtBox = new TextBox();
        quoteNumberTxtBox.ensureDebugId(salesInvoiceView + "quoteNumberTxtBox");

        poNumberTxtBox = new TextBox();
        poNumberTxtBox.ensureDebugId(salesInvoiceView + "poNumberTxtBox");

        referenceTxtBox = new TextBox();
        referenceTxtBox.ensureDebugId(salesInvoiceView + "referenceTxtBox");

        taxCalcTypeListBox = new DataListBox();
        taxCalcTypeListBox.ensureDebugId(salesInvoiceView + "taxCalcTypeListBox");
        taxCalcTypeListBox.setWithoutNullLabel(true);
        taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(TAX_CALCULATION_EXCLUSIVE));
        if (formParameters.isExternalForm(CONVERT_TO_INVOICE)) {
            taxCalcTypeListBox.setEnabled(false);
        }

        priceLevel = new DataListBox();
        priceLevel.ensureDebugId(salesInvoiceView + "priceLevel");

        priceLevelField = new FormGroup(accountingStrings.relatedPriceLevel(), priceLevel);

        clientDiscount = new WfmDropdown();
        clientDiscount.ensureDebugId(salesInvoiceView + "clientDiscount");

        if (formParameters.isRecurringInvoice()) {
            recurringWidget = new RecurringWidget(SchedulerConstant.INVOICE_ADD_FORM);
            recurringWidget.ensureDebugId(salesInvoiceView + "recurringWidget");
        }
        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            if (Utils.isSaudiCompany()) {
                placeOfSupplyWidget = new KsaPlaceOfSupplyWidget(null, null, Constants.RECEIVABLE);
            } else if (Utils.isUAECompany()) {
                placeOfSupplyWidget = new UaePlaceOfSupplyWidget(null, null, Constants.RECEIVABLE);
            }
        }

        introductionPanel = new IntroductionPanel();
        introductionPanel.getIntroduction().addStyleName("keepDropDownOpen");

        productsTable = new ProductsTable(RECEIVABLE, SALE_INVOICE, formParameters);
        productsTable.setCrmAccountLookUp(crmAccountLookUp);
        productsTable.setCurrencyWidget(currencyWidget);

        if (Utils.isUKVATRegistered()) {
            reverseChargeBox = new KpiCheckBox();
            reverseChargeBox.setText("This transaction is applicable for reverse charge");
            reverseChargeBox.addValueChangeHandler((x) -> productsTable.onReverseChargeChange());
            productsTable.setReverseChargeBox(reverseChargeBox);
        }
        currencyWidget.setDatePicker(invoiceDate);

        initBankAccountWidgets();

        notesWidget = new InvoiceNoteHistoryWidget();

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        addBillableExpense = new FooterInformer(SvgEnum.wallet, accountingStrings.addBillableExp(), null);
        addBillableExpense.getIconContainer().addStyleName("has-expense");
        addBillableExpense.ensureDebugId(salesInvoiceView + "addBillableExpense");
        addBillableExpense.setVisible(false);

        advancedOptions = createAdvancedOptions();
        advancedOptions.addToAddressBodyContainer(crmAccountWidgets.getBillAddress());
        advancedOptions.addToMailAddressBodyContainer(crmAccountWidgets.getMailAddress());
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));
        showMoreLink.addStyleName("btn-flat"); //https://prnt.sc/r8iwmk

        approver = new ChosenApproversWidget(SALE_INVOICE, objectID);

        saveButton = new WfmButton2(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE);
        saveButton.ensureDebugId(salesInvoiceView + "saveButton");
        saveButton.setVisible(false);

        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.ensureDebugId(salesInvoiceView + "submitButton");
        submitButton.setVisible(false);

        pdfVersionButton = new SplitButton(97, WfmButton2.BTN_WHITE_OUTLINE);
        pdfVersionButton.ensureDebugId("save-pdfVersion");

        approveButton = new SplitButton(97, BTN_PRIMARY);
        approveButton.ensureDebugId("saveAndApprove");

        assignSerialsButton = new WfmButton2(accountingStrings.assignSerials(), WfmButton2.BTN_WHITE);
        assignSerialsButton.ensureDebugId("assign-serials");
        assignSerialsButton.setVisible(false);

        widgetsMap = new HashMap<>();

        initApproverLoadHandler();

        this.formPresenter = generateFormPresenter();
        this.formPresenter.bindUI();

        return null;
    }

    private void initApproverLoadHandler() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, SalesInvoiceView.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                });
                if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            }
        });
    }

    private FooterUploadPanel createUploadPanel() {
        FooterUploadPanel result;
        if (params != null && params.length > 2 && "convertToInvoice".equals(params[1])) {
            formParameters.setExternalFormID(CONVERT_TO_INVOICE);
            formParameters.setExternalObjectID(Integer.parseInt(params[2]));
            result = new FooterUploadPanel(F_SALE_INV, objectID, F_SALE_QUOTE, formParameters.getExternalObjectID(), true, true, wfmStrings.attachments());
        } else {
            result = new FooterUploadPanel(F_SALE_INV, objectID, true, wfmStrings.attachments());
        }
        return result;
    }

    private void initWidgetsMap(NewInvoice data) {

        //Customer field
        {
            FormGroup clientField = new FormGroup(crmAccountLookUp);
            if (formParameters.isEditForm() && (PAID.equals(data.getStatusCode()) || (data.getPaymentItems() != null && data.getPaymentItems().length > 0) || data.getConvertedItemID() != null)) {
                crmAccountLookUp.setEnabled(false);
            }
            clientField.ensureDebugId(InvoiceFormFields.CUSTOMER);

            Div clientFieldLabel = clientField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");
            Span spanElement = new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
            spanElement.setId("customer-label");
            clientFieldLabel.add(spanElement);

            if (!RECURRING_INVOICE.equals(invoiceTypeCode)) {
                Span balance = new Span(wfmStrings.balance() + ": ");
                balance.add(customerBalanceLink);
                clientFieldLabel.add(balance);
            }

            widgetsMap.put(INPUT_CRM_ACCOUNT, clientField);
        }

        //Currency field
        {
            if (formParameters.isEditForm() && data.getConvertedItemID() == null &&
                    (PAID.equals(data.getStatusCode()) ||
                            (data.getPaidAmount() != null && data.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) ||
                            (data.getPaymentItems() != null && data.getPaymentItems().length > 0))) {
                currencyWidget.setEnabled(false);
            }
            systemCustomFieldsMap.put(INPUT_CURRENCY_LIST_BOX, currencyWidget);
        }

        FormGroup typeField = new FormGroup(wfmStrings.type(), invoiceTypeListBox);
        typeField.ensureDebugId(InvoiceFormFields.TYPE);

        widgetsMap.put(INPUT_INVOICE_TYPE, typeField);
        widgetsMap.put(INPUT_DATE, new FormGroup(formParameters.isRecurringInvoice() ? accountingStrings.nextInvoiceDate() : wfmStrings.invoiceDate(), invoiceDate));
        widgetsMap.put(INPUT_DUE_DATE, termsAndDuePanel.getTermsDueAsField());
        if (data.isApprover()) {
            widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.manager(), approver)));

            if (formParameters.isEditForm() && !data.isApproverSaved() && (data.getPaidAmount() == null || data.getPaidAmount().compareTo(BigDecimal.ZERO) == 0 )) {
                approver.reloadApproverWidgets(RelationItem.TYPE_SALEINVOICE, null);
            }
        }
        //client discount field {customization}
        FormGroup clientDiscountField = new FormGroup(accountingStrings.clientDiscount(), clientDiscount);
        widgetsMap.put(INPUT_CLIENT_DISCOUNT, clientDiscountField);

        //don't know when this item works
        if (AccountingUtils.get().enablePaymentPeriod()) {
            HTML periodLabel = new HTML(wfmStrings.period());
            periodLabel.setStyleName(STYLE_LABEL);
            widgetsMap.put(LABEL_PERIOD_LABEL, periodLabel);
            widgetsMap.put(INPUT_PERIOD_START, periodStart);
            widgetsMap.put(INPUT_PERIOD_END, periodEnd);
        }

        if (!formParameters.isRecurringInvoice()) {
            FormGroup numberField = new FormGroup(property.getShortForNumber(wfmStrings.invoiceNumber()), numberTxtBox);
            numberField.ensureDebugId(InvoiceFormFields.NUMBER);
            widgetsMap.put(INPUT_NUMBER, numberField);
        }

        systemCustomFieldsMap.put(INPUT_REFERENCE, referenceTxtBox);

        systemCustomFieldsMap.put(INPUT_SHOW_MORE, showMoreLink);

        taxCalcTypeListBox.ensureDebugId(InvoiceFormFields.TAX_CALC);
        systemCustomFieldsMap.put(INPUT_TAX_CALC_TYPE, taxCalcTypeListBox);

        if (formParameters.isRecurringInvoice() && recurringWidget != null) {
            widgetsMap.put(INPUT_RECURRING_VIEW, recurringWidget);
        }
        if ((Utils.isUAECompany() || Utils.isSaudiCompany()) && Utils.isVatRegistered() && placeOfSupplyWidget != null) {
            widgetsMap.put(INPUT_PLACE_OF_SUPPLY, placeOfSupplyWidget);
        }
        if (Utils.isUKVATRegistered()) {
            widgetsMap.put(INPUT_PLACE_OF_SUPPLY, new GRow(new GColumn(new FormGroup("&nbsp;", reverseChargeBox))));
        }
        widgetsMap.put(INPUT_ITEM_TABLE, productsTable.getItemsTable());
        widgetsMap.put(INPUT_TOTALS_TABLE, productsTable.getTotalsTable());
        // if is converted invoice, add info message
        if (data.isDeleteAndAddDsiabled()) {
            HTML message = new HTML(accountingStrings.cannotMakeChangesConvertedInvoice());
            message.getElement().setAttribute("style", "background-color: rgb(255, 243, 205); color: rgb(133, 100, 4); padding: 10px; border: 1px solid rgb(255, 238, 186); border-radius: 5px; display: inline-block;");
            message.setVisible(false);
            widgetsMap.put(INPUT_MESSAGE_TO_USER, message);
        }
        addSystemCustomFields(widgetsMap);

        //Payment instruction field
        {
            FormGroup instructionField = new FormGroup(productsTable.getPaymentInstruction());
            if (Utils.hasRole(CLIENT)) {
                productsTable.getPaymentInstruction().setEnabled(false);
            }
            Div instructionLabel = instructionField.getGroupLabel();
            instructionLabel.addStyleName("label-group");
            instructionLabel.add(new Span(wfmStrings.paymentInstructions()));
            instructionLabel.add(productsTable.getPaymentTermsConditionsListBox());
            widgetsMap.put(INPUT_INSTRUCTION, instructionField);
        }
    }

    private void fillForm(NewInvoice result) {
        if (SALE_INVOICE.equals(this.invoiceTypeCode)) {
            invoiceTypeListBox.setSelected(result.getInvoiceType());
            onInvoiceTypeChange();
        }
        if (formParameters.isEditForm()) {
            formParameters.setRecurringInvoice(result.isRecurringInvoice());
        }
        if (OPPORTUNITY.equals(formParameters.getCrmFormName())) {
            setValues(result);
        }
        crmAccountWidgets.setCurrencyWidget(currencyWidget);
        if (result.getAccountsReceivablePayable() != null) {
            receivablePayableLookUp.addAccountItem(result.getAccountsReceivablePayable());
        }
        productsTable.getBaseTotalLabel().setHTML(accountingMessages.dynamicTotal(currencyWidget.getBaseCurrencyName()));
        productsTable.getBillableExpenseAmountLabel().setHTML(accountingStrings.billableExpenseAmount());
        productsTable.getBillableExpenseTaxLabel().setHTML("Billable Exp. Tax Total");
        currencyWidget.setCurrency(result.getCurrencyID(), result.getExchageRate());
        //set currency id to product table
        productsTable.setCurrencyId(result.getCurrencyID());

        if (reverseChargeBox != null) {
            reverseChargeBox.setValue(result.isReversechargeApplicable());
            productsTable.clearSelectedTaxFromItems(result.isReversechargeApplicable());
        }

        int days = result.getDueDays() != null ? result.getDueDays() : 0;
        if (result.getInvoiceTermsItem() != null) {
            days = result.getInvoiceTermsItem().getDays();
        }
        if (objectID == null) {
            if (TERMS_TYPE.equals(result.getDueDateType()) || result.getInvoiceTermsItem() != null) {
                termsAndDuePanel.setData(TERMS_TYPE, (result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null), result.getInvoiceTermsItem());
            } else {
                termsAndDuePanel.setData(DUE_TYPE, DateUtil.addDays(currentDate, days), null);
            }
        } else {
            if (result.getInvoiceTermsItem() != null) {
                termsAndDuePanel.setData(TERMS_TYPE, (result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null), result.getInvoiceTermsItem());
            } else {
                termsAndDuePanel.setData(DUE_TYPE, DateUtil.addDays(currentDate, days), null);
            }
        }
        //}
        if (objectID != null) {//From Edit Form
            setValues(result);
            if (result.isFromGdn()) {
                productsTable.setEnabled(false, true, CONVERT_TO_INVOICE_FROM_GRN);
            }
            formPresenter.onChangeClientHandler(true);
        } else if (formParameters.isExternalForm(COPY_FROM_EXISTING_DATA) || CONVERT_MULTI_QUOTE_TO_INVOICE.equals(formParameters.getExternalFormID())) {
            setValues(result);

            if (CONVERT_MULTI_QUOTE_TO_INVOICE.equals(formParameters.getExternalFormID())) {
                productsTable.setEnabled(false, true, formParameters.getExternalFormID());
            }
        } else if (formParameters.isExternalForm(PROGRESS_INVOICING)) {
            applyQuoteConvertionAndProgressInvoicingData(result, formParameters.isExternalForm(PROGRESS_INVOICING));

        } else if (formParameters.isExternalForm(CONVERT_TO_INVOICE) || formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_GRN) || formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_RENTAL_ORDER) || formParameters.isExternalForm(COPY_FROM_PI_TO_SI)) {
            List<NewInvoiceItem> items = new ArrayList<>();

            for (NewInvoiceItem item : result.getItems()) {

                if (item.getQuantity().compareTo(ZERO) != 0) {
                    items.add(item);
                }
            }
            result.setItems(items.toArray(new NewInvoiceItem[0]));
            productsTable.setValues(result);
            setValues(result);
            if (!items.isEmpty()) {
                productsTable.setEnabled(false, false, formParameters.getExternalFormID());
            } else {
                formParameters.setExternalFormID(null);
                formParameters.setExternalObjectID(null);
            }
        } else {//From Add Form / Client Supplier List / Crm Account / Fixed Asset

            applyTaxCalculationTypeChanges(result.getTaxCalculationType());

            if (result.getTypeItem() != null && (COPY_FROM_CLIENT_SUPPLIER.equals(formParameters.getExternalFormID())
                    || COPY_FROM_CRM_ACCOUNT.equals(formParameters.getExternalFormID()) || COPY_FROM_FIXED_ASSET.equals(formParameters.getExternalFormID()))) {
                crmAccountLookUp.addItem(result.getTypeItem());
                crmAccountWidgets.presenter.initContactAddress(result.getTypeItem(), true, Address.EntityType.CrmAccount);

                if (result.getRelatedProject() != null) {
                    relatedProject.setSelected(result.getRelatedProject());
                    if (result.getNumberData() != null && result.getNumberData().isWithProject() && COPY_FROM_CLIENT_SUPPLIER.equals(formParameters.getExternalFormID())) {
                        invoiceNumberData.setProjectCode(result.getRelatedProject().getDescription());
                        numberTxtBox.setText(invoiceNumberData.getInvoiceNumber());
                    }
                }
                if (result.getTypeItem().getSupplierCustomerBalance() >= 0) {
                    customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    customerBalanceLink.setText(AccountingUtils.get().formatPrice(result.getTypeItem().getSupplierCustomerBalance()));
                } else {
                    customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * result.getTypeItem().getSupplierCustomerBalance()) + ")");
                }

                if (COPY_FROM_FIXED_ASSET.equals(formParameters.getExternalFormID())) {
                    NewInvoiceItem invoiceItem = new NewInvoiceItem();
                    invoiceItem.setFullItemName(fixedAssetItem.getName());
                    invoiceItem.setDescription(fixedAssetItem.getDescription());
                    invoiceItem.setQuantity(BigDecimal.ONE);
                    invoiceItem.setUnitPrice(BigDecimal.ZERO);
                    invoiceItem.setDiscountAmount(BigDecimal.ZERO);
                    invoiceItem.setAccountItem(fixedAssetItem.getAccount());
                    result.setItems(new NewInvoiceItem[]{invoiceItem});
                    productsTable.setValues(result);

                    EditableTable itemsTable = productsTable.getItemsTable();
                    for (int i = 0; i < itemsTable.getGrid().getRowCount(); i++) {
                        AccountsLookUp account = (AccountsLookUp) itemsTable.getColumnById(i, ProductsTable.ACCOUNT);
                        ItemQtyPanel qtyPanel = (ItemQtyPanel) itemsTable.getColumnById(i, ProductsTable.QTY);
                        if (account != null) {
                            account.setEnabled(false);
                        }
                        if (qtyPanel != null) {
                            qtyPanel.setEnabled(false);
                        }
                    }
                }
            }

            if (result.getItems() != null) {

                if (productsTable.getExchangeRateValue() == null) {
                    productsTable.setExchangeRateValue(new BigDecimal("1.0000"));
                }
            }

            invoiceDate.setDate(formParameters.isFromGettingStarted() ? result.getInvoiceDate().getNonConvertedDate() : currentDate);

            if (formParameters.getConvertFormType() != null) {
                if (result.getTypeItem() != null) {
                    crmAccountLookUp.addItem(result.getTypeItem());
                }
                productsTable.setExchangeRateValue(result.getExchageRate());
                productsTable.setValues(result);
                if (result.getInvoiceDate() != null) {
                    invoiceDate.setDate(result.getInvoiceDate().getNonConvertedDate());
                }
                if (result.getInvoiceTermsItem() != null) {
                    termsAndDuePanel.setData(TERMS_TYPE, (result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null), result.getInvoiceTermsItem());
                } else {
                    termsAndDuePanel.setData(DUE_TYPE, result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null, null);
                }
                setValues(result);
            }
        }

        if (RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
            currencyWidget.setEnabled(false);
            currencyWidget.getCurrencyListBox().setEnabled(false);
            taxCalcTypeListBox.setEnabled(false);
            crmAccountLookUp.setEnabled(false);
        }
    }

    private void initProductTable(NewInvoice result) {
        productsTable.setNewInvoice(result);
        productsTable.setColumnsMap(drawColumns(result));
        productsTable.initItemTable(result.isProgressInvoicing() && Utils.hasGenericAccess(GenericSettingsEnum.EDIT_SALE_INVOICE_CONVERTED_FROM_SQ_SO));
        productsTable.setDefaultAccount(result.getDefaultAccountItem());
        productsTable.setDefaultTax(result.getDefaultTaxItem());
        productsTable.setDefaultDiscount(result.getDefaultDiscountItem());
    }

    private void applyQuoteConvertionAndProgressInvoicingData(NewInvoice result, boolean isProgressInvoicing) {
        if (isProgressInvoicing) {
            if ("byItem".equals(progressInvoicingType)) {
                List<NewInvoiceItem> items = new ArrayList<>();
                BigDecimal totalDiscount = BigDecimal.ZERO;
                for (NewInvoiceItem invItem : result.getItems()) {

                    if (convertMap.containsKey(invItem.getID())) {
                        invItem.setConvertedQty(invItem.getConvertedQty() != null ? invItem.getConvertedQty().add(convertMap.get(invItem.getID())) : convertMap.get(invItem.getID()));
                        invItem.setDiscountAmount(convertMap.get(invItem.getID()).multiply(invItem.getDiscountAmount() != null ? invItem.getDiscountAmount() : BigDecimal.ZERO).divide(invItem.getQuantity(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                        invItem.setQuantity(convertMap.get(invItem.getID()));
                        invItem.setQtyWithHighScale(convertMap.get(invItem.getID()));
                        invItem.setQuoteItemId(invItem.getID());
                        items.add(invItem);

                        invItem.setID(null);
                        totalDiscount = totalDiscount.add(invItem.getDiscountAmount());

                        if (invItem.getCustomFieldItems() != null && !invItem.getCustomFieldItems().isEmpty()) {
                            for (CompanyCustomFieldItem ccfItem : invItem.getCustomFieldItems()) {
                                ccfItem.setObjectId(null);
                            }
                        }

                    }
                }
                result.setItems(items.toArray(new NewInvoiceItem[]{}));
                result.setProgressInvoicingType(progressInvoicingType);

                if (Constants.ONE_OFF_FIXED_AMOUNT.equals(result.getDiscountType())) {
                    if (quotePercent != null) {
                        result.setDiscountAmount(result.getDiscountAmount().multiply(quotePercent).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                    } else if (quoteAmount != null) {
                        BigDecimal percent = quoteAmount.multiply(HUNDRED).divide(result.getTotal(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                        result.setDiscountAmount(result.getDiscountAmount().multiply(percent).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                    }
                }
            } else {
                NewInvoiceItem[] invItems = result.getItems();
                for (NewInvoiceItem invItem : invItems) {
                    if (quotePercent != null) {
                        invItem.setQtyWithHighScale(invItem.getQuantity().multiply(quotePercent).divide(HUNDRED, 10, RoundingMode.HALF_UP));
                        invItem.setQuantity(invItem.getQuantity().multiply(quotePercent).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));

                        if (Constants.ONE_OFF_FIXED_AMOUNT.equals(invItem.getDiscountItemStaticType())) {
                            invItem.setDiscountAmount(invItem.getDiscountAmount().multiply(quotePercent).divide(HUNDRED, 10, RoundingMode.HALF_UP));
                        }
                    } else if (quoteAmount != null) {
                        invItem.setQtyWithHighScale(invItem.getQuantity().multiply(quoteAmount).divide(result.getTotalInInvoiceCurrency(), 10, RoundingMode.HALF_UP)); //Let's remove progress invoicing If you don't like it
                        invItem.setQuantity(invItem.getQuantity().multiply(quoteAmount).divide(result.getTotalInInvoiceCurrency(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));

                        if (Constants.ONE_OFF_FIXED_AMOUNT.equals(invItem.getDiscountItemStaticType())) {
                            invItem.setDiscountAmount(invItem.getDiscountAmount().multiply(quoteAmount).divide(result.getTotalInInvoiceCurrency(), 10, RoundingMode.HALF_UP));
                        }
                    }
                }
                result.setItems(invItems);
                result.setProgressInvoicingType(progressInvoicingType);

                if (Constants.ONE_OFF_FIXED_AMOUNT.equals(result.getDiscountType())) {
                    if (quotePercent != null) {
                        result.setDiscountAmount(result.getDiscountAmount().multiply(quotePercent).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                    } else if (quoteAmount != null) {
                        BigDecimal percent = quoteAmount.multiply(HUNDRED).divide(result.getTotal(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
                        result.setDiscountAmount(result.getDiscountAmount().multiply(percent).divide(HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
                    }
                }
            }
            result.setProgressInvoicing(true);
        }

        result.setSubtotal(null);
        result.setTotalDiscount(null);
        result.setComissionAmount(null);
        result.setTotalInInvoiceCurrency(null);
        result.setTotal(null);
        if (PS_CLOSED.equals(result.getProjectStatusCode())) {
            result.setRelatedProject(null);
            result.setRelatedProjectID(null);
        }

        setValues(result);
        quoteNumberTxtBox.setText(result.getInvoiceNumber());
    }

    private void disableProgressInvoicingFields() {
        currencyWidget.setEnabled(false);
        taxCalcTypeListBox.setEnabled(false);
        if (!Utils.hasGenericAccess(GenericSettingsEnum.EDIT_SALE_INVOICE_CONVERTED_FROM_SQ_SO)) {
            productsTable.setEnabled(false, true, formParameters.getExternalFormID());
        }
    }

    public LinkedHashMap<String, ColumnConfig> drawColumns(NewInvoice result) {
        LinkedHashMap<String, ColumnConfig> columnsMap = new LinkedHashMap<>();
        boolean deparmentMandatory = AccountingUtils.get().isEnableAccountingDepartmentRelation() && !Utils.hasPermission(PermissionConstants.SKIP_DEPARTMENT_ITEM_VALIDATION);

        if (!formParameters.isProjectBasedInvoice() && result.getCustomItemColumns() != null && result.getCustomItemColumns().length > 0) {

            ColumnConfig columnConfig;

            for (ColumnConfigs column : result.getCustomItemColumns()) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);

                switch (column.getCode()) {
                    case ProductsTable.PRODUCT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.PRODUCT, Utils.getColumnWidth(column.getWidth(), 200), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.PRODUCT, columnConfig);
                        break;
                    case ProductsTable.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.DESCRIPTION, Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.DESCRIPTION, columnConfig);
                        break;
                    case ProductsTable.QTY:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.QTY, column.getTitle(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.QTY, columnConfig);
                        break;
                    case ProductsTable.MEASUREMENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.MEASUREMENT, column.getTitle(), Utils.getColumnWidth(column.getWidth(), 60), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.MEASUREMENT, columnConfig);
                        break;
                    case ProductsTable.UNITPRICE:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.UNITPRICE, column.getTitle(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL, isPixel);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.UNITPRICE, columnConfig);
                        break;
                    case ProductsTable.DISCOUNT_LIST:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.DISCOUNT_LIST, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.DISCOUNT_LIST, columnConfig);
                        break;
                    case ProductsTable.DISCOUNT_AMT:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.DISCOUNT_AMT, column.getTitle(), Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.DISCOUNT_AMT, columnConfig);
                        break;
                    case ProductsTable.ACCOUNT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.ACCOUNT, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.ACCOUNT, columnConfig);
                        break;
                    case ProductsTable.NET_AMT:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.NET_AMT, "", Utils.getColumnWidth(column.getWidth(), 80), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.NET_AMT, columnConfig);
                        break;
                    case ProductsTable.TAX_LIST:
                        boolean isRequired = column.isRequired();
                        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                            isRequired = true;
                        }
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.TAX_LIST, Utils.getColumnWidth(column.getWidth(), 100), isRequired);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.TAX_LIST, columnConfig);
                        break;
                    case ProductsTable.DOUBLE_TAX_LIST:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.DOUBLE_TAX_LIST, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.DOUBLE_TAX_LIST, columnConfig);
                        break;
                    case ProductsTable.WAREHOUSE:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.WAREHOUSE, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.WAREHOUSE, columnConfig);
                        break;
                    case ProductsTable.TOTAL_AMT:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.TOTAL_AMT, column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.TOTAL_AMT, columnConfig);
                        break;
                    case ProductsTable.PROJECT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.PROJECT, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.PROJECT, columnConfig);
                        break;
                    case ProductsTable.DEPARTMENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.DEPARTMENT, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.DEPARTMENT, columnConfig);
                        break;
                    case ProductsTable.FROM_DATE:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.FROM_DATE, Utils.getColumnWidth(column.getWidth(), 100), false);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.FROM_DATE, columnConfig);
                        break;
                    case ProductsTable.TO_DATE:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.TO_DATE, Utils.getColumnWidth(column.getWidth(), 100), false);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.TO_DATE, columnConfig);
                        break;
                    case ProductsTable.FAI_CATEGORY:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.FAI_CATEGORY, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.FAI_CATEGORY, columnConfig);
                        break;
                    default:
                        if (column.getCode() != null && column.getCode().contains("date_value")) {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 165), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setDisabled(column.isDisabled());
                            columnsMap.put(column.getCode(), columnConfig);
                        } else {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), DATA_TYPE_NUMBER.equals(column.getDataType()) ? RIGHT_ALIGN_CELL : null);
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setDisabled(column.isDisabled());
                            columnsMap.put(column.getCode(), columnConfig);

                            if (UI_TYPE_TEXTAREA.equalsIgnoreCase(column.getUiType())) {
                                columnConfig.setCustomStyleName("product-description-cell");
                            }
                        }
                        break;
                }
            }
        } else {
            if (formParameters.isProjectBasedInvoice()) {
                columnsMap.put(ProductsTable.PRODUCT, new ColumnConfig(CustomCell.class, ProductsTable.PRODUCT, 200, true));
            } else {
                columnsMap.put(ProductsTable.PRODUCT, new ColumnConfig(LookUpCell.class, ProductsTable.PRODUCT, 200, true));
            }
            columnsMap.put(ProductsTable.DESCRIPTION, new ColumnConfig(CustomCell.class, ProductsTable.DESCRIPTION, 250, false));
            columnsMap.put(ProductsTable.QTY, new ColumnConfig(CustomCell.class, ProductsTable.QTY, "", 75, true, Constants.RIGHT_ALIGN_CELL, true));
            columnsMap.put(ProductsTable.UNITPRICE, new ColumnConfig(CustomCell.class, ProductsTable.UNITPRICE, "", 75, true, Constants.RIGHT_ALIGN_CELL, true));
            columnsMap.put(ProductsTable.DISCOUNT_AMT, new ColumnConfig(CustomCell.class, ProductsTable.DISCOUNT_AMT, "", 75, false, Constants.RIGHT_ALIGN_CELL, true));
            columnsMap.put(ProductsTable.NET_AMT, new ColumnConfig(CustomCell.class, ProductsTable.NET_AMT, "", 100, false, Constants.RIGHT_ALIGN_CELL, true));
            columnsMap.put(ProductsTable.TAX_LIST, new ColumnConfig(LookUpCell.class, ProductsTable.TAX_LIST, 100, GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()));
            columnsMap.put(ProductsTable.ACCOUNT, new ColumnConfig(LookUpCell.class, ProductsTable.ACCOUNT, 100, true));
            if (Utils.hasGenericAccess(GenericSettingsEnum.FAI_INTEGRATION_ENABLED)) {
                columnsMap.put(ProductsTable.FAI_CATEGORY, new ColumnConfig(LookUpCell.class, ProductsTable.FAI_CATEGORY, 100, GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()));
            }
            if (formParameters.isProjectBasedInvoice() && Utils.isProjectInLineItemEnable()) {
                columnsMap.put(ProductsTable.PROJECT, new ColumnConfig(LookUpCell.class, ProductsTable.PROJECT, 100, false));
            }

            if (Utils.isMultiWarehouseEnabled() && !formParameters.isProjectBasedInvoice()) {
                columnsMap.put(ProductsTable.WAREHOUSE, new ColumnConfig(LookUpCell.class, ProductsTable.WAREHOUSE, 100, true));
            }
        }
        return columnsMap;
    }

    private void initBankAccountWidgets() {
        bankAccounts = new DataListBox();
        receivablePayableLookUp = new AccountsReceivablePayableLookUp(Constants.RECEIVABLE);
    }

    private SalesInvoiceFormPresenter generateFormPresenter() {
        return new SalesInvoiceFormPresenter(new SalesInvoiceViewInterface() {
            @Override
            public boolean isEditForm() {
                return formParameters.isEditForm();
            }

            @Override
            public Params getFormParameters() {
                return formParameters;
            }

            @Override
            public Integer getObjectID() {
                return objectID;
            }

            @Override
            public void setObjectID(Integer result) {
                objectID = result;
            }

            @Override
            public BigDecimal getTotalInBaseCurrency() {
                return productsTable.getTotalInBaseCurrency();
            }

            @Override
            public Date getConversionDate() {
                return conversionDate;
            }

            @Override
            public void setConversionDate(Date date) {
                conversionDate = date;
            }

            @Override
            public NewInvoice getFormData(String invoiceStatus, boolean calculate) {
                return getInvoiceData(invoiceStatus, calculate);
            }

            @Override
            public LookUp getCrmAccountLookUp() {
                return crmAccountLookUp;
            }

            @Override
            public CrmAccountWidgets getCrmAccountWidgets() {
                return crmAccountWidgets;
            }

            @Override
            public ProjectLookUp getProjectLookUp() {
                return relatedProject;
            }

            @Override
            public FormGroup getPriceLevel() {
                return priceLevelField;
            }

            @Override
            public DataListBox getPriceLevelDropdown() {
                return priceLevel;
            }

            @Override
            public FormGroup getClientDiscountField() {
                return (FormGroup) widgetsMap.get(INPUT_CLIENT_DISCOUNT);
            }

            @Override
            public WfmDropdown getClientDiscountDropdown() {
                return clientDiscount;
            }

            @Override
            public TextBox getPreviousBalance() {
                return previousBalance;
            }

            @Override
            public TextBox getPaymentsReceived() {
                return paymentsReceived;
            }

            @Override
            public HTML getPayAdjusmentLabel() {
                return paymentAdjustmentLabel;
            }

            @Override
            public HTML getMessageToUser() {
                return (HTML) widgetsMap.get(INPUT_MESSAGE_TO_USER);
            }
            @Override
            public CurrencyWidget getCurrencyWidget() {
                return currencyWidget;
            }

            @Override
            public DatePicker getDatePicker() {
                return invoiceDate;
            }

            @Override
            public DatePicker getDueDatePicker() {
                return null/*dueDate*/;//instead used due date and terms panel
            }

            @Override
            public TextBox getNumberTxtBox() {
                return numberTxtBox;
            }

            @Override
            public TextArea2 getPaymentInstruction() {
                return productsTable.getPaymentInstruction();
            }

            @Override
            public DataListBox getTaxCalcListBox() {
                return taxCalcTypeListBox;
            }

            @Override
            public TextArea getIntroduction() {
                return introductionPanel.getIntroduction();
            }

            @Override
            public ReceiptTable getTotalTable() {
                return productsTable.getTotalsTable();
            }

            @Override
            public ProductsTable getProductTable() {
                return productsTable;
            }

            @Override
            public View getView() {
                return SalesInvoiceView.this;
            }

            @Override
            public void setEditValues(NewInvoice result) {
                setValues(result);
            }

            @Override
            public void setFormData(NewInvoice result) {
                fillForm(result);
            }

            @Override
            public void initCustomFields(NewInvoice result) {
                initInvoiceCustomFields(result);

                if (formParameters.isRecurringInvoice() && recurringWidget != null && (objectID != null || formParameters.getExternalObjectID() != null)) {
                    recurringWidget.setData(result.getRecurrenceJobItem());
                }
            }

            @Override
            public void initSystemCustomFields(NewInvoice result) {
                systemCustomFields = result.getSystemCustomFields();
            }

            @Override
            public void generateForm(String layoutHTML) {
                htmlPanel = new WftHTMLPanel(layoutHTML, widgetsMap).getContainer();
                htmlPanel.setStyleName("add-form invoice-form");
                htmlPanel.add(new ViewFooter(new IFooteredView() {
                    @Override
                    public List<Widget> getFooterLeftSideWidgets() {
                        return SalesInvoiceView.this.getFooterLeftSideWidgets();
                    }

                    @Override
                    public List<Widget> getFooterRightSideWidgets() {
                        return SalesInvoiceView.this.getFooterRightSideWidgets();
                    }
                }));
                add(htmlPanel);
            }

            @Override
            public Date getCurrentDate() {
                return currentDate;
            }

            @Override
            public InvoiceNumberData getNumberData() {
                return invoiceNumberData;
            }

            @Override
            public void setNumberData(InvoiceNumberData numberData) {
                invoiceNumberData = numberData;
            }

            @Override
            public DataListBox getBankAccountListBox() {
                return bankAccounts;
            }

            @Override
            public SplitButton getSplitButtonPdf() {
                return pdfVersionButton;
            }

            @Override
            public SplitButton getApproveSplitButton() {
                return approveButton;
            }

            @Override
            public String getInvoiceType() {
                return invoiceTypeCode;
            }


            @Override
            public boolean isReccuringInvoice() {
                return formParameters.isRecurringInvoice();
            }

            @Override
            public PdfTemplatePanel getPdfTemplateBox() {
                return pdfTemplatePanel;
            }

            @Override
            public WfmButton2 getSaveButton() {
                return saveButton;
            }

            @Override
            public WfmButton2 getSubmitButton() {
                return submitButton;
            }

            @Override
            public WfmButton2 getApproveButton() {
                return null;
            }

            @Override
            public WfmButton2 getApproveAndSendButton() {
                return null;
            }

            @Override
            public boolean validateCustomFields() {
                boolean validate = advancedOptions.validateCustomFieldRequiredFields();

                //if there are not validate fields in the custom fields
                //then we should open the advanced pop-up
                if (!validate) {
                    advancedOptions.getCustomFieldContainer().setActive(0); // to show sidebad popup

                    showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
                }
                return validate;
            }

            @Override
            public boolean validateProjectMandatory() {
                if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && !Utils.isProjectInLineItemEnable() && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY)) {
                    if (relatedProject.getSelectedProjectCode() == null) {
                        showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
                        relatedProject.addStyleName(ERROR_FORM_STYLE);
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean validateSystemCustomFields() {
                return SalesInvoiceView.this.validateSystemCustomFields();
            }

            @Override
            public HTMLPanel getHTMLPanel() {
                return htmlPanel;
            }

            @Override
            public void initProductsTableData(NewInvoice result) {
                initProductTable(result);
            }

            @Override
            public void initWidgetMap() {
            }

            @Override
            public void initWidgetMap(NewInvoice data) {
                hasApproval = data.isApprover();
                initWidgetsMap(data);
            }

            @Override
            public void initPdfTemplates(NewInvoice result) {
//                initInvoicePdfTemplates(result);
            }

            @Override
            public PlaceOfSupplyWidget getPlaceOfSupplyWidget() {
                return placeOfSupplyWidget;
            }

            /*@Override
            public WfmDropdown getPlaceOfSupplyBox() {
                return placeOfSupplyBox;
            }

            @Override
            public FormGroup getPlaceOfSupplyField() {
                return (FormGroup) widgetsMap.get(INPUT_PLACE_OF_SUPPLY);
            }*/

            @Override
            public void setQuotePercent(BigDecimal value) {
                quotePercent = value;
            }

            @Override
            public void setQuoteAmount(BigDecimal value) {
                quoteAmount = value;
            }

            @Override
            public void setProgressInvoiceDialogBoxType(String type) {
                if (type != null && type.equals(BY_ITEM) || type.equals(BY_AMOUNT) || type.equals(BY_PERCENTAGE)) {
                    progressInvoicingType = type;
                }
            }

            @Override
            public void applyProgressInvoicingParameters(NewInvoice result) {
                applyQuoteConvertionAndProgressInvoicingData(result, true);
            }

            @Override
            public void setProgressInvoicingByItem(boolean value) {
                if (value) {
                    progressInvoicingType = BY_ITEM;
                }
            }

            @Override
            public void setProgressInvoiciningMap(HashMap<Integer, BigDecimal> valuesMap) {
                convertMap = valuesMap;
            }

            @Override
            public FooterInformer getBillableExpenseButton() {
                return addBillableExpense;
            }

            @Override
            public ExpenseMarkupPopup getExpenseMarkupPopup() {

                if (expenseMarkupPopup != null) {
                    return expenseMarkupPopup;
                } else {
                    return expenseMarkupPopup = new ExpenseMarkupPopup();
                }
            }

            @Override
            public ShippingLabelDialogBox getShippingLabelDialogBox() {
                return shippingLabelDialogBox;
            }

            @Override
            public void setShippingLabelDialogBox(ShippingLabelDialogBox dialogBox) {
                shippingLabelDialogBox = dialogBox;
            }

            @Override
            public RecurringWidget getRecurringWidget() {
                return recurringWidget;
            }

            @Override
            public DatePicker getPeriodStart() {
                return periodStart;
            }

            @Override
            public DatePicker getPeriodEnd() {
                return periodEnd;
            }

            @Override
            public HorizontalPanel getTermsAndDueDateLabel() {
                return dueAndTearmsPanel;
            }

            @Override
            public TermsAndDuePanel getTermsAndDuePanel() {
                return termsAndDuePanel;
            }

            @Override
            public MaterialLink getCustomerBalanceLink() {
                return customerBalanceLink;
            }

            @Override
            public AccountsReceivablePayableLookUp getAccountsReceivablePayableLookUp() {
                return receivablePayableLookUp;
            }

            @Override
            public Command onProjectBaseInvoiceInit() {
                return onProjectBaseInvoiceInit;
            }

            @Override
            public Property getProperty() {
                return property;
            }

            @Override
            public ChosenApproversWidget getApproverLookUp() {
                return approver;
            }
        });
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(() -> {
            List<Widget> result = new ArrayList<>();
            result.add(new FormGroup(wfmStrings.bankDetails(), bankAccounts));

            if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                result.add(new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), relatedProject, Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY)));
            }
            GRow formRow = new GRow();
//            GColumn pnumberCol = new GColumn(GColumnEnum.COL_6);
            GColumn snumberCol = new GColumn(GColumnEnum.COL_6);

//            pnumberCol.add(new FormGroup(accountingStrings.po(), poNumberTxtBox));
//            formRow.add(pnumberCol);
            result.add(formRow);

            if (!formParameters.isRecurringInvoice() && Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_LIST)) {
                snumberCol.add(new FormGroup(accountingStrings.sq(), quoteNumberTxtBox));
                formRow.add(snumberCol);
                result.add(formRow);
            }
            result.add(new FormGroup(wfmStrings.accountsReceivable(), receivablePayableLookUp));
            result.add(priceLevelField);
            return result;
        });
    }

    /*private void initInvoicePdfTemplates(NewInvoice invoice) {
        if (invoice.getPdfTemplateList() != null && invoice.getPdfTemplateList().getItems() != null && invoice.getPdfTemplateList().getItems().length > 0) {
            pdfTemplatePanel = new PdfTemplatePanel(invoice);
            pdfTemplatePanel.ensureDebugId("pdf-tmplateList");

            FormGroup pdfTemplateField = new FormGroup(accountingStrings.choosePdfTemplate(), pdfTemplatePanel);
            pdfTemplateField.ensureDebugId("inv_pdf_template");
            advancedOptions.addToBodyContainer(pdfTemplateField);
        }
    }*/

    private void initInvoiceCustomFields(NewInvoice invoice) {

        if (!formParameters.isRecurringInvoice() && invoice.getCustomFieldItems() != null && invoice.getCustomFieldItems().size() > 0) {
            advancedOptions.createAndAppendInvoiceCustomFieldsView(ViewAddFiledsCodeName.SaleInvoiceAdd, invoice);
        }
    }

    public NewInvoice getInvoiceData(String invoiceStatus, boolean calculate) {
        if (calculate) {
            productsTable.calculate(true);
        }

        NewInvoice invoice = new NewInvoice();
        invoice.setClientID(crmAccountLookUp.getSelectedItemID());
        invoice.setBillAddressID(crmAccountWidgets.getBillAddressID());
        invoice.setMailAddressID(crmAccountWidgets.getMailAddressID());
        invoice.setCurrencyID(currencyWidget.getCurrencyID());
        invoice.setExchageRate(productsTable.getExchangeRateValue());
        invoice.setRelatedProject(relatedProject.getSelectedItem());
        invoice.setShippingMethodID(productsTable.getShippingMethod() != null ? productsTable.getShippingMethod().getId() : null);

        //set opportunity ID
        invoice.setOpportunityID(formParameters.getOpportunityID());

        if (placeOfSupplyWidget != null) {
            invoice.setPlaceOfSupply(placeOfSupplyWidget.getSelectedPlaceOfSupply());
        }

        if (previousBalance.getText() != null && !"".equals(previousBalance.getText().trim())) {
            invoice.setPreviosBalance(AccountingUtils.get().parseToBigDecimal(previousBalance.getText().trim()));
        }
        if (paymentsReceived.getText() != null && !"".equals(paymentsReceived.getText().trim())) {
            invoice.setPaymentsReceived(AccountingUtils.get().parseToBigDecimal(paymentsReceived.getText().trim()));
        }

        invoice.setProjectBasedInvoice(formParameters.isProjectBasedInvoice());
        invoice.setRecurringInvoice(formParameters.isRecurringInvoice());
        if (formParameters.isRecurringInvoice()) {
            invoice.setRecurrenceJobItem(recurringWidget != null ? recurringWidget.getData() : null);
        } else {
            Integer fourDigitNumber = invoiceNumberData.parseFourDigitNumber(numberTxtBox.getText());
            if (fourDigitNumber != null) {
                invoice.setFourDigitNumber(fourDigitNumber.toString());
            }
            invoice.setInvoiceNumber(numberTxtBox.getText().trim());
            invoice.setQuoteNumber(quoteNumberTxtBox.getText());
        }
        invoice.setOpportunityID(formParameters.getOpportunityID());

        invoice.setInvoiceType(invoiceTypeListBox.getSelectedId());

        invoice.setReference(referenceTxtBox.getText());
        invoice.setPoNumber(poNumberTxtBox.getText());
        if (periodStart.getDate() != null) {
            invoice.setPeriodStart(new DateNonConvertable(DateUtil.resetTime(periodStart.getDate())));
        }
        if (periodEnd.getDate() != null) {
            invoice.setPeriodEnd(new DateNonConvertable(DateUtil.getDayLastTime(periodEnd.getDate())));
        }

        invoice.setInvoiceDate(new DateNonConvertable(DateUtil.resetTime(invoiceDate.getDate())));

        if (termsAndDuePanel.isDueTypeSelected()) {
            invoice.setDueDate(new DateNonConvertable(termsAndDuePanel.getDueDate()));
        } else {
            invoice.setDueDate(new DateNonConvertable(termsAndDuePanel.getDueDate()));
            invoice.setInvoiceTermsItem(termsAndDuePanel.getInvoiceTerms());
        }

        invoice.setIntroduction(introductionPanel.getIntroduction().getText());
        invoice.setSubtotal(productsTable.getSubTotalHighScale());
        invoice.setTotalDiscount(productsTable.getTotalDiscountHighScale());
        invoice.setShippingPrice(productsTable.getShippingTotal());
        invoice.setTotalInInvoiceCurrency(productsTable.getTotalInInvoiceCurrencyHighScale());
        invoice.setTotal(productsTable.getTotalInBaseCurrencyHighScale());
        invoice.setTotalTaxes(productsTable.getTotalTaxAmountByCurrency());
        invoice.setTotalTaxes(invoice.getTotalTaxes().add(productsTable.getShippingTaxAmount()));
        invoice.setTotalTaxItems(productsTable.getTotalTaxItems());
        invoice.setType(RECEIVABLE);
        invoice.setInvoiceCustomType(invoiceCustomType);
        invoice.setStatusCode(invoiceStatus);
        invoice.setHistoryList(notesWidget.getNotes().toArray(new HistoryListItem[]{}));
        invoice.setPaymentInstructionID(productsTable.getPaymentTermsConditionsListBox().getSelectedId());
        invoice.setPaymentInstruction(productsTable.getPaymentInstruction().getText());
        invoice.setTaxCalculationType(taxCalcTypeListBox.getSelectedId());
        invoice.setItems(productsTable.getDataItems(invoiceStatus));
        if (formParameters.isExternalForm(PROGRESS_INVOICING)) {
            invoice.setProgressiveInvoiceQuoteId(formParameters.getExternalObjectID());
        }
        if (formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_RENTAL_ORDER)) {
            invoice.setRentalOrderId(formParameters.getExternalObjectID());
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED)) {
            HashMap<Integer, ArrayList<ProductSerialItem>> productSerialItems = new HashMap<>();
            for (NewInvoiceItem item : invoice.getItems()) {
                if (item.getAssignedSerials() != null && item.getAssignedSerials().length > 0) {
                    if (productSerialItems.containsKey(item.getItemID())) {
                        ArrayList<ProductSerialItem> productSerialItemsList = new ArrayList<>(productSerialItems.get(item.getItemID()));
                        productSerialItemsList.addAll(Arrays.asList(item.getAssignedSerials()));
                        productSerialItems.put(item.getItemID(), productSerialItemsList);
                    } else {
                        productSerialItems.put(item.getItemID(), new ArrayList<>(Arrays.asList(item.getAssignedSerials())));
                    }
                }
            }
            invoice.setProductSerialItems(productSerialItems);
        }
        //simplifying to filter for transactions
        if (relatedProject != null && !Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            for (NewInvoiceItem item : invoice.getItems()) {
                if (item != null) {
                    item.setProject(relatedProject.getSelectedItem());
                }
            }
        }
        invoice.setBookkeep(getBookKeep());
        invoice.setProjectIDs(projectIDs);
        invoice.setBankAccount(bankAccounts.getSelectedItem());
        invoice.setAccountsReceivablePayable(receivablePayableLookUp.getSelectedData());
        if (pdfTemplatePanel != null) {
            invoice.setPdfTemplateID(pdfTemplatePanel.getSelectedTemplateID());
        }

        /*billable expense data*/
        invoice.setBillableExpenseAmount(expenseMarkupPopup.getTotalWithMarkup());
        invoice.setBillableExpenseTaxAmount(expenseMarkupPopup.getTaxTotal());
        // it sets data to expense
        ArrayList<NewInvoiceItem> setExpense = expenseMarkupPopup.getExpanseItemsAsLineItems();
        invoice.setExpenses(expenseMarkupPopup.getExpenses());
        if (expenseMarkupPopup.markupAccount != null && expenseMarkupPopup.markupAccount.getSelectedItem() != null) {
            invoice.setMarkupAccount(expenseMarkupPopup.markupAccount.getSelectedItem());
        }
        if (reverseChargeBox != null) {
            invoice.setReversechargeApplicable(reverseChargeBox.getValue());
        }
        invoice.setCustomFieldItems(advancedOptions.getCustomFieldsData());

        invoice.setPriceLevel(priceLevel.getSelectedItem());
        invoice.setClientDiscount(clientDiscount.getSelectedItem());
        invoice.setAttachments(footerUploadPanel.getAttachedFiles());
        invoice.setConvertedItemID((formParameters.isExternalForm(PROGRESS_INVOICING) || formParameters.isExternalForm(CONVERT_TO_INVOICE)) ? formParameters.getExternalObjectID() : null);
        invoice.setTargetGrnId(formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_GRN) ? formParameters.getExternalObjectID() : null);
        invoice.setConvertedPercent(quotePercent);
        invoice.setConvertedAmount(quoteAmount);
        invoice.setProgressInvoicing(formParameters.isExternalForm(PROGRESS_INVOICING));
        invoice.setProgressInvoicingType(progressInvoicingType);
        invoice.setConvertedQuoteIDs(productsTable.getConvertedQuoteIds());
        if (orderIds != null) {
            invoice.setOrderBaseinvoiceOrderIds(orderIds.toString());
        }

        if (pdfTemplatePanel != null) {
            invoice.setPdfTemplateID(pdfTemplatePanel.getSelectedTemplateID());
        }

        if (fixedAssetItem != null) {
            invoice.setFixedAssetItem(fixedAssetItem);
            invoice.setFixedAssetRelated(true);
        }
        if (relationID != null && relationType != null) {
            relations.add(new RelationItem(null, relationID, relationType, relationName, null, RelationItem.TYPE_SALEINVOICE, null));
            invoice.setRelations(relations);
        }
        invoice.setSalesOrder(isSaleOrder);
        if (productsTable.getOverallDiscount().isEnabled()) {
            invoice.setDiscountType(productsTable.getOverallDiscount().getType());
            invoice.setDiscountAmount(productsTable.getOverallDiscount().getValue());
        }
        invoice.setApprovers(approver.getChosenApprovers());
        return invoice;
    }

    private boolean getBookKeep() {
        return !formParameters.isFromGettingStarted();
    }

    private void setValues(NewInvoice invoice) {
        if (invoice.getTypeItem() != null) {
            crmAccountLookUp.addItem(invoice.getTypeItem());
        }

        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            formPresenter.configurePlaceOfSupply(invoice.getTypeItem(), invoice.getPlaceOfSupply(), placeOfSupplyWidget);
        }

        if (invoice.getTypeItem() != null) {
            if (invoice.getTypeItem().getSupplierCustomerBalance() >= 0) {
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                customerBalanceLink.setText(AccountingUtils.get().formatPrice(invoice.getTypeItem().getSupplierCustomerBalance()));
            } else {
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * invoice.getTypeItem().getSupplierCustomerBalance()) + ")");
            }
        }
        if (formParameters.isExternalForm(CONVERT_TO_INVOICE)) {
            crmAccountWidgets.presenter.initContactAddress(invoice.getTypeItem(), !(formParameters.isEditForm() || formParameters.isExternalForm(PROGRESS_INVOICING)
                    || formParameters.isExternalForm(CONVERT_TO_INVOICE)), invoice.getMailAddressID());
        } else if (invoice.getTypeItem() != null) {
            crmAccountWidgets.presenter.initContactAddress(invoice.getTypeItem(), !(formParameters.isEditForm() || formParameters.isExternalForm(PROGRESS_INVOICING)
                    || formParameters.isExternalForm(CONVERT_TO_INVOICE) || formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_GRN) || formParameters.isExternalForm(CONVERT_TO_INVOICE_FROM_RENTAL_ORDER) || formParameters.isExternalForm(COPY_FROM_EXISTING_DATA)), Address.EntityType.CrmAccount);
        }

        if (invoice.getBankAccountItem() != null && !invoice.getBankAccountItem().isActive()) {
            bankAccounts.setEnabled(false);
        }

        if (invoice.getRelatedProject() != null) {
            relatedProject.setSelected(invoice.getRelatedProject());
        }

        if (!formParameters.isEditForm() || formParameters.isExternalForm(COPY_FROM_EXISTING_DATA)) {
            currencyWidget.setCurrency(invoice.getCurrencyID(), invoice.getExchageRate());

            if (formParameters.isExternalForm(COPY_FROM_EXISTING_DATA)) {
                productsTable.calculate();
            }
        }
        if (invoice.getPreviosBalance() != null) {
            previousBalance.setText(AccountingUtils.get().formatPrice(invoice.getPreviosBalance()));
        }
        if (invoice.getPaymentsReceived() != null) {
            paymentsReceived.setText(AccountingUtils.get().formatPrice(invoice.getPaymentsReceived()));
        }
        if (invoice.getInvoiceType() != null) {
            invoiceTypeListBox.setSelected(invoice.getInvoiceType());
        }

        if (!formParameters.isRecurringInvoice()) {
            if (formParameters.isEditForm()) {
                invoiceNumberData = invoice.getNumberData();
                String dateString = DateTimeFormat.getFormat("yyyyMMdd").format(invoiceDate.getDate());
                invoiceNumberData.setWithDate(invoice.getInvoiceNumber().contains(dateString));
                invoiceNumberData.setDate(invoiceNumberData.isWithDate() ? dateString : "");
                numberTxtBox.setText(invoice.getInvoiceNumber());
            }
            quoteNumberTxtBox.setText(invoice.getQuoteNumber());
        }
        poNumberTxtBox.setText(invoice.getPoNumber());
        referenceTxtBox.setText(invoice.getReference());
        if (invoice.getPeriodStart() != null) {
            periodStart.setDate(invoice.getPeriodStart().getNonConvertedDate());
        }
        if (invoice.getPeriodEnd() != null) {
            periodEnd.setDate(invoice.getPeriodEnd().getNonConvertedDate());
        }

        if (objectID != null || formParameters.isExternalForm(COPY_FROM_EXISTING_DATA) || formParameters.isExternalForm(CONVERT_TO_INVOICE) || formParameters.isExternalForm(COPY_FROM_PI_TO_SI)) {
            if (formParameters.isExternalForm(COPY_FROM_EXISTING_DATA)) {
                invoiceDate.setDate(currentDate);
                if (invoice.getInvoiceTermsItem() != null) {
                    termsAndDuePanel.setData(TERMS_TYPE, (invoice.getDueDate() != null ? invoice.getDueDate().getNonConvertedDate() : null), invoice.getInvoiceTermsItem());
                } else if (invoice.getTypeItem() != null && invoice.getTypeItem().getTermsItem() != null && invoice.getTypeItem().getTermsItem().getDays() != null) {
                    termsAndDuePanel.setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(currentDate, invoice.getTypeItem().getTermsItem().getDays()), null);
                } else if (invoice.getDueDays() != null) {
                    termsAndDuePanel.setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(currentDate, invoice.getDueDays()), null);
                }
            } else {
                invoiceDate.setDate(invoice.getInvoiceDate().getNonConvertedDate());
                if (invoice.getInvoiceTermsItem() != null) {
                    termsAndDuePanel.setData(TermsAndDuePanel.TERMS_TYPE, (invoice.getDueDate() != null ? invoice.getDueDate().getNonConvertedDate() : null), invoice.getInvoiceTermsItem());
                } else {
                    termsAndDuePanel.setData(TermsAndDuePanel.DUE_TYPE, (invoice.getDueDate() != null ? invoice.getDueDate().getNonConvertedDate() : null), null);
                }
            }
        } else {
            invoiceDate.setDate(currentDate);
        }

        applyTaxCalculationTypeChanges(invoice.getTaxCalculationType());

        introductionPanel.getIntroduction().setText(invoice.getIntroduction());

        productsTable.setExchangeRateValue(invoice.getExchageRate());
        productsTable.setValues(invoice);

        if (!CONVERT_TO_INVOICE.equals(formParameters.getExternalFormID()) && !PROGRESS_INVOICING.equals(formParameters.getExternalFormID())) {
            productsTable.getPaymentInstruction().setText(invoice.getPaymentInstruction());
        }

        if (invoice.isProgressInvoicing() && !COPY_FROM_EXISTING_DATA.equals(formParameters.getExternalFormID())) {
            disableProgressInvoicingFields();

            if (formParameters.isEditForm()) {
                quotePercent = invoice.getConvertedPercent();
                quoteAmount = invoice.getConvertedAmount();
                formParameters.setExternalFormID(PROGRESS_INVOICING);
                formParameters.setExternalObjectID(invoice.getConvertedItemID());
            }
        } else if (invoice.isDeleteAndAddDsiabled()) {
            disableProgressInvoicingFields();
        }
    }

    private void applyTaxCalculationTypeChanges(Integer taxCalculationType) {
        taxCalcTypeListBox.setSelected(taxCalculationType != null ? AccountingUtils.getTaxCalcType(taxCalculationType) : AccountingUtils.getTaxCalcType(TAX_CALCULATION_EXCLUSIVE));
        productsTable.onTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), false);
    }

    public void setPeriod(Date start, Date end) {
        if (start != null) {
            periodStart.setDate(start);
        }
        if (end != null) {
            periodEnd.setDate(end);
        }
    }

    private void getRelationName(final Integer relationID, final String relType) {
        AllInOneService.App.get().getRelationName(relationID, relType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    relationName = result;
                }
            }
        });
    }

    public void setOnProjectBaseInvoiceInit(Command onProjectBaseInvoiceInit) {
        this.onProjectBaseInvoiceInit = onProjectBaseInvoiceInit;
    }

    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public void setProjectIDs(Integer[] projectIDs) {
        this.projectIDs = projectIDs;
    }

    public ProductsTable getProductsTable() {
        return productsTable;
    }

    public SalesInvoiceFormPresenter getFormPresenter() {
        return formPresenter;
    }

    public Params getFormParams() {
        return formParameters;
    }

    private void onInvoiceTypeChange() {

        if (SERVICE_INVOICE_TYPE.equals(invoiceTypeListBox.getSelectedId())) {

            if (widgetsMap.get(INPUT_MAIL_ADDRESS) != null) {
                widgetsMap.get(INPUT_MAIL_ADDRESS).setVisible(false);
            }
            if (widgetsMap.get(INPUT_SHIPPING_METHOD) != null) {
                widgetsMap.get(INPUT_SHIPPING_METHOD).setVisible(false);
            }
        } else {
            if (widgetsMap.get(INPUT_MAIL_ADDRESS) != null) {
                widgetsMap.get(INPUT_MAIL_ADDRESS).setVisible(true);
            }
            if (widgetsMap.get(INPUT_SHIPPING_METHOD) != null) {
                widgetsMap.get(INPUT_SHIPPING_METHOD).setVisible(true);
            }
        }
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();
        FooterInformer informer = new FooterInformer(SvgEnum.docHistory, wfmStrings.historyAndNotes(), notesWidget);
        informer.addClickHandler(click -> notesWidget.setLoadData(callback -> {
            if (objectID == null) {
                return;
            }
            InvoiceService.App.get().loadInvoiceHistoryNote(objectID, SALE_INVOICE, true, callback);
        }));
        FooterInformer footerIntroduction = new FooterInformer(SvgEnum.docTitle, wfmStrings.introduction(), introductionPanel.getIntroduction());

        footerUploadPanel = createUploadPanel();

        informer.setInitialClasses("informer-item history-notes-container");
        footerIntroduction.setInitialClasses("informer-item");
        if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_HISTORY_NOTES)) {
            leftSideWidgets.add(informer);
        }
        if (!Utils.hasRole(CLIENT)) {
            leftSideWidgets.add(footerIntroduction);
        }
        if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_UPLOAD_FILES)) {
            leftSideWidgets.add(footerUploadPanel);
        }

        //add billable expese to footer
        leftSideWidgets.add(addBillableExpense);
        return leftSideWidgets;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = new ArrayList<>();
        boolean editButtonPermission = Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_AND_PI_DRAFT);
        if (editButtonPermission) {
            Div draftButtonWrapper = new Div();
            draftButtonWrapper.add(saveButton);
            rightWidgets.add(draftButtonWrapper);
        }

        boolean letUnsavedInvoicePDF = objectID != null || Utils.hasPermission(PermissionConstants.ACCOUNTING_UNSAVED_SALES_INVOICE_PDF);
        boolean printPDFPermission = Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_PDF) ||
                Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_PDF);

        if (letUnsavedInvoicePDF && printPDFPermission) {
            Div pdfWrapper = new Div();
            pdfWrapper.add(pdfVersionButton);
            rightWidgets.add(pdfWrapper);
        }
        if (hasApproval) {
            Div submitButtonWrapper = new Div();
            submitButtonWrapper.add(submitButton);
            rightWidgets.add(submitButtonWrapper);
        }
        Div approveWrapper = new Div();
        approveWrapper.add(approveButton);
        rightWidgets.add(approveWrapper);
        if (!isAlmadarSerials) {
            Div serialWrapper = new Div();
            serialWrapper.add(assignSerialsButton);
            rightWidgets.add(serialWrapper);
        }
        return rightWidgets;
    }

    @Override
    public String getPropertyCode() {
        return Constants.SALE_INVOICE;
    }
}
