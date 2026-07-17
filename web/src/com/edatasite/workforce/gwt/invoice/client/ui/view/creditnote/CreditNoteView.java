package com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.KsaPlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.PlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.UaePlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.NotePaymentMeansCodeEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
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
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CrmAccountWidgets;
import com.edatasite.workforce.gwt.invoice.client.ui.view.IntroductionPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PdfTemplatePanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.historyNote.InvoiceNoteHistoryWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.CANCELLATION_OF_SUPPLIES;
import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.CHANGES_TO_SELLER_OR_BUYER_INFO;
import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.CHANGES_TO_VAT_DUE;
import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.CHANGES_VALUE_OF_SUPPLY;
import static com.edatasite.workforce.gwt.core.client.enums.NoteInstructionType.GOOD_SERVICES_REFUND;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/16/12
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreditNoteView extends FooteredView implements Colapse, Constants, AccountingConstants, AccountingCustomFormConstants, FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private final Date currentDate = new Date();

    private Integer objectID;
    private final String noteType;

    private CrmAccountLookUp crmAccountLookUp;
    private TextBox numberTxtBox;
    private TextBox quoteNumberTxtBox;
    private DataListBox priceLevel;
    private FormGroup priceLevelField;
    private TextBox poNumberTxtBox;
    private TextBox referenceTxtBox;
    private DatePicker creditNoteDate;
    private DatePicker dueDate;
    private ProjectLookUp relatedProject;
    private DataListBox taxCalcTypeListBox;
    private Date conversionDate;

    private CrmAccountWidgets crmAccountWidgets;
    private CurrencyWidget currencyWidget;
    private IntroductionPanel introductionPanel;
    private ProductsTable productsTable;

    private InvoiceNumberData invoiceNumberData;

    private WfmButton2 saveButton, submitButton /*, pdfVersionButton*/;
    private SplitButton approveButton, pdfVersionButton;

    private Params formParameters;
    private HashMap<String, Widget> widgetsMap;
    private CreditNoteFormPresenter formPresenter;
    private HTMLPanel htmlPanel;
    private InvoiceNoteHistoryWidget noteHistoryWidget;
    private FooterUploadPanel uploadPanel;
    private SimpleLink viewBankAccountDetails;
    private DataListBox bankAccounts;
    private AccountsReceivablePayableLookUp receivablePayableLookUp;

    private final String creditNoteView = "cridet_note_view_";

    private PdfTemplatePanel pdfTemplatePanel;
    private MaterialLink supplierBalanceLink;
    private InvoiceAdvancedOptions advancedOptions;
    private MaterialLink showMoreLink;
    private PlaceOfSupplyWidget placeOfSupplyWidget;
    private KpiCheckBox reverseChargeBox;
    private DataListBox reason;
    private DataListBox paymentType;

    private ChosenApproversWidget approver;

    private boolean hasApproval;

    public CreditNoteView(String[] params, String type) {
        super(RECEIVABLE.equals(type) ? "receivablecreditnoteadd" : "payablecreditnoteadd", RECEIVABLE.equals(type) ? accountingStrings.addCreditNote() : accountingStrings.addDebitNote());
        this.noteType = type;
        initFormParameters(params);
    }

    public CreditNoteView(Integer objectID, String type) {
        super("edit", RECEIVABLE.equals(type) ? accountingStrings.editCreditNote() : accountingStrings.editDebitNote());
        this.objectID = objectID;
        this.noteType = type;
        initFormParameters(null);
    }

    private void initFormParameters(String[] params) {
        formParameters = new Params();
        formParameters.setType(noteType);
        formParameters.setObjectID(objectID);
        if (params != null && params.length > 2) {
            if (params[1].equals("fromInvoice")) {
                formParameters.setExternalFormID(COPY_INVOICE_TO_CREDITNOTE);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params[1].equals("copyFromExistingData")) {
                formParameters.setExternalFormID(COPY_FROM_EXISTING_DATA);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            }
        }
    }

    protected Widget onInitialize() {

        crmAccountLookUp = new CrmAccountLookUp(RECEIVABLE.equals(noteType) ? CrmAccountLookUp.CUSTOMER : CrmAccountLookUp.SUPPLIER, true);
        crmAccountLookUp.ensureDebugId(creditNoteView + "crmAccountLookUp");
        crmAccountWidgets = new CrmAccountWidgets(crmAccountLookUp, noteType, (RECEIVABLE.equals(noteType) ? RECEIVABLE_CREDIT_NOTE : PAYABLE_CREDIT_NOTE), formParameters.isEditForm());

        relatedProject = new ProjectLookUp(noteType, RECEIVABLE.equals(noteType) ? crmAccountLookUp : null);
        relatedProject.ensureDebugId(creditNoteView + "relatedProject");

        currencyWidget = new CurrencyWidget(!formParameters.isEditForm());

        if (formParameters.isExternalForm(COPY_INVOICE_TO_CREDITNOTE)) {
            currencyWidget.setEnabled(false);
        }

        creditNoteDate = new DatePicker(currentDate, true);
        creditNoteDate.ensureDebugId(creditNoteView + "creditNoteDate");

        currencyWidget.setDatePicker(creditNoteDate);

        numberTxtBox = new TextBox();
        numberTxtBox.ensureDebugId(creditNoteView + "numberTxtBox");

        referenceTxtBox = new TextBox();
        referenceTxtBox.ensureDebugId(creditNoteView + "referenceTxtBox");

        poNumberTxtBox = new TextBox();
        poNumberTxtBox.ensureDebugId(creditNoteView + "poNumberTxtBox");

        quoteNumberTxtBox = new TextBox();
        quoteNumberTxtBox.ensureDebugId(creditNoteView + "quoteNumberTxtBox");

        priceLevel = new DataListBox();
        priceLevel.ensureDebugId(creditNoteView + "priceLevel");
        priceLevelField = new FormGroup(accountingStrings.relatedPriceLevel(), priceLevel);

        dueDate = new DatePicker(true);
        dueDate.ensureDebugId(creditNoteView + "dueDate");

        taxCalcTypeListBox = new DataListBox();
        crmAccountLookUp.ensureDebugId(creditNoteView + "crmAccountLookUp");
        taxCalcTypeListBox.setWithoutNullLabel(true);
        taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(TAX_CALCULATION_EXCLUSIVE));
        taxCalcTypeListBox.addValueChangeHandler(event -> {
            //This is for changing tax calculation type and recalculating
            productsTable.onTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), true);
        });

        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            if (Utils.isSaudiCompany()) {
                placeOfSupplyWidget = new KsaPlaceOfSupplyWidget(onPlaceOfSupplyChange(), () -> productsTable.onReverseChargeChange(), noteType);
            } else if (Utils.isUAECompany()) {
                placeOfSupplyWidget = new UaePlaceOfSupplyWidget(onPlaceOfSupplyChange(), () -> productsTable.onReverseChargeChange(), noteType);
            }
        }
        if (RECEIVABLE.equals(this.noteType)) {
            introductionPanel = new IntroductionPanel();
            introductionPanel.getIntroduction().addStyleName("keepDropDownOpen");
        }
        productsTable = new ProductsTable(noteType, RECEIVABLE.equals(noteType) ? RECEIVABLE_CREDIT_NOTE : PAYABLE_CREDIT_NOTE, formParameters);

        if (RECEIVABLE.equals(noteType)) {
            productsTable.setCrmAccountLookUp(crmAccountLookUp);
        }
        productsTable.setCurrencyWidget(currencyWidget);
        productsTable.setReverseChargeBox(placeOfSupplyWidget != null ? placeOfSupplyWidget.getReverseChargeBox() : null);
        productsTable.setPlaceOfSupplyBox(placeOfSupplyWidget != null ? placeOfSupplyWidget.getPlaceOfSupplyBox() : null);

        if (Utils.isUKVATRegistered()) {
            reverseChargeBox = new KpiCheckBox();
            reverseChargeBox.setText("This transaction is applicable for reverse charge");
            reverseChargeBox.addValueChangeHandler((x) -> productsTable.onReverseChargeChange());
            productsTable.setReverseChargeBox(reverseChargeBox);
        }

        supplierBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        supplierBalanceLink.setHref("javaScript:void(0)");
        supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        saveButton = new WfmButton2(wfmStrings.draft(), BTN_DEFAULT_OUTLINE);
        saveButton.ensureDebugId(creditNoteView + "saveButton");

        approveButton = new SplitButton(120, BTN_PRIMARY);


        submitButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitButton.ensureDebugId(creditNoteView + "submitButton");
        submitButton.setVisible(false);

        pdfVersionButton = new SplitButton(97, WfmButton2.BTN_WHITE_OUTLINE);
        pdfVersionButton.ensureDebugId(creditNoteView + "pdfVersionButton");

        saveButton.setVisible(false);

        widgetsMap = new HashMap<>();

        noteHistoryWidget = new InvoiceNoteHistoryWidget();
        uploadPanel = new FooterUploadPanel(F_SALE_INV, objectID, true, wfmStrings.attachments());
        bankAccounts = new DataListBox();
        viewBankAccountDetails = new SimpleLink(accountingStrings.viewAllDetails(), "");
        viewBankAccountDetails.setVisible(true);

        receivablePayableLookUp = new AccountsReceivablePayableLookUp(noteType);
        receivablePayableLookUp.getElement().getStyle().setMarginTop(20, Style.Unit.PX);

        advancedOptions = createAdvancedOptions();
        advancedOptions.addToAddressBodyContainer(crmAccountWidgets.getBillAddress());
        advancedOptions.addToMailAddressBodyContainer(crmAccountWidgets.getMailAddress());
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addStyleName("btn-flat CrediitNoteView");
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));
        if (RECEIVABLE.equals(noteType)) {
            approver = new ChosenApproversWidget(RelationItem.TYPE_CREDIT_NOTE, objectID);
        } else {
            approver = new ChosenApproversWidget(RelationItem.TYPE_DEBIT_NOTE, objectID);
        }

        initApproverLoadHandler();

        this.formPresenter = generateFormPresenter();
        this.formPresenter.bindUI();

        return null;
    }

    private void initWidgetsMap(NewInvoice data) {

        creditNoteDate.setStyleName(STYLE_DATE_PICKER);
        dueDate.setStyleName(STYLE_DATE_PICKER);
        productsTable.getPaymentInstruction().setStyleName(STYLE_TERMS_INSTRUCTION);

        FormGroup crmAccountField = new FormGroup(crmAccountLookUp);

        Div supplierFieldLabel = crmAccountField.getGroupLabel();
        supplierFieldLabel.addStyleName("label-group");

        supplierFieldLabel.add(new Span(RECEIVABLE.equals(noteType) ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier())));

        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(supplierBalanceLink);
        supplierFieldLabel.add(balance);

        widgetsMap.put(INPUT_CRM_ACCOUNT, crmAccountField);

        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");
        widgetsMap.put(INPUT_SHOW_MORE, showMoreField);

        FormGroup currencyField = new FormGroup(wfmStrings.currency(), currencyWidget);
        currencyField.ensureDebugId(InvoiceFormFields.CURRENCY);
        widgetsMap.put(INPUT_CURRENCY_LIST_BOX, currencyField);

        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.invoiceDate(), creditNoteDate));
        widgetsMap.put(INPUT_DUE_DATE, new FormGroup(wfmStrings.dueDate(), dueDate));

        if (data.isApprover()) {
            GWT.log("Galdi");
            widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.manager(), approver)));

            if (formParameters.isEditForm() && !data.isApproverSaved() && data.getPaidAmount() == null) {
                if (RECEIVABLE.equals(noteType)) {
                    approver.reloadApproverWidgets(RelationItem.TYPE_CREDIT_NOTE, null);
                } else {
                    approver.reloadApproverWidgets(RelationItem.TYPE_DEBIT_NOTE, null);
                }
            }
        }

        FormGroup numberField = new FormGroup(wfmStrings.invoiceNumber(), numberTxtBox);
        numberField.ensureDebugId(InvoiceFormFields.NUMBER);
        widgetsMap.put(INPUT_NUMBER, numberField);

        FormGroup referenceField = new FormGroup(wfmStrings.reference(), referenceTxtBox);
        referenceField.ensureDebugId(InvoiceFormFields.REFERENCE);
        widgetsMap.put(INPUT_REFERENCE, referenceField);

        FormGroup taxCalField = new FormGroup(accountingStrings.amounts(), taxCalcTypeListBox);
        taxCalField.ensureDebugId(InvoiceFormFields.TAX_CALC);
        widgetsMap.put(INPUT_TAX_CALC_TYPE, taxCalField);

        if ((Utils.isUAECompany() || Utils.isSaudiCompany()) && Utils.isVatRegistered()) {
            widgetsMap.put(INPUT_PLACE_OF_SUPPLY, placeOfSupplyWidget);
        }
        if (Utils.isUKVATRegistered()) {
            widgetsMap.put(INPUT_PLACE_OF_SUPPLY, new GRow(new GColumn(new FormGroup("&nbsp;", reverseChargeBox))));
        }
        widgetsMap.put(INPUT_ITEM_TABLE, productsTable.getItemsTable());
        widgetsMap.put(INPUT_TOTALS_TABLE, productsTable.getTotalsTable());

        widgetsMap.put(INPUT_INSTRUCTION_LIST, productsTable.getPaymentTermsConditionsListBox());
        widgetsMap.put(INPUT_INSTRUCTION, new FormGroup(wfmStrings.paymentInstructions(), productsTable.getPaymentInstruction()));

    }

    private LinkedHashMap<String, ColumnConfig> drawColumns(NewInvoice result) {
        LinkedHashMap<String, ColumnConfig> columnsMap = new LinkedHashMap<>();
        boolean deparmentMandatory = AccountingUtils.get().isEnableAccountingDepartmentRelation() && !Utils.hasPermission(PermissionConstants.SKIP_DEPARTMENT_ITEM_VALIDATION);
        if (result.getCustomItemColumns() != null && result.getCustomItemColumns().length > 0) {

            ColumnConfig columnConfig;
            for (ColumnConfigs column : result.getCustomItemColumns()) {
                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                switch (column.getCode()) {
                    case ProductsTable.PRODUCT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.PRODUCT, Utils.getColumnWidth(column.getWidth(), 155), column.isRequired());
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
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.QTY, "", Utils.getColumnWidth(column.getWidth(), 80), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.QTY, columnConfig);
                        break;
                    case ProductsTable.MEASUREMENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.MEASUREMENT, "", Utils.getColumnWidth(column.getWidth(), 60), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.MEASUREMENT, columnConfig);
                        break;
                    case ProductsTable.UNITPRICE:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.UNITPRICE, "", Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
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
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.DISCOUNT_AMT, "", Utils.getColumnWidth(column.getWidth(), 80), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
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
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.NET_AMT, "", Utils.getColumnWidth(column.getWidth(), 90), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.NET_AMT, columnConfig);
                        break;
                    case ProductsTable.TAX_LIST:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.TAX_LIST, Utils.getColumnWidth(column.getWidth(), 105), GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered() || column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.TAX_LIST, columnConfig);
                        break;
                    case ProductsTable.DOUBLE_TAX_LIST:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.DOUBLE_TAX_LIST, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
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
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.TOTAL_AMT, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
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
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.DEPARTMENT, Utils.getColumnWidth(column.getWidth(), 100), deparmentMandatory);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setRequired(column.isRequired());
                        columnsMap.put(ProductsTable.DEPARTMENT, columnConfig);
                        break;
                    case ProductsTable.CLIENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.CLIENT, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.CLIENT, columnConfig);
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
            columnsMap.put(ProductsTable.PRODUCT, new ColumnConfig(LookUpCell.class, ProductsTable.PRODUCT, 200, true));
            columnsMap.put(ProductsTable.DESCRIPTION, new ColumnConfig(CustomCell.class, ProductsTable.DESCRIPTION, 250, false));
            columnsMap.put(ProductsTable.QTY, new ColumnConfig(CustomCell.class, ProductsTable.QTY, 75, true, Constants.RIGHT_ALIGN_CELL));
            if (Utils.isMultiWarehouseEnabled()) {
                columnsMap.put(ProductsTable.WAREHOUSE, new ColumnConfig(LookUpCell.class, ProductsTable.WAREHOUSE, 100, true));
            }
            columnsMap.put(ProductsTable.UNITPRICE, new ColumnConfig(CustomCell.class, ProductsTable.UNITPRICE, 75, true, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.DISCOUNT_AMT, new ColumnConfig(CustomCell.class, ProductsTable.DISCOUNT_AMT, 75, false, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.ACCOUNT, new ColumnConfig(LookUpCell.class, ProductsTable.ACCOUNT, 100, true));
            columnsMap.put(ProductsTable.NET_AMT, new ColumnConfig(CustomCell.class, ProductsTable.NET_AMT, 100, false, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.TAX_LIST, new ColumnConfig(LookUpCell.class, ProductsTable.TAX_LIST, 100, GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()));
        }
        return columnsMap;
    }

    private void initApproverLoadHandler() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, CreditNoteView.this, (sender, args) -> {
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

    private CreditNoteFormPresenter generateFormPresenter() {
        return new CreditNoteFormPresenter(new CreditNoteViewInterface() {
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
            public CurrencyWidget getCurrencyWidget() {
                return currencyWidget;
            }

            @Override
            public DatePicker getDatePicker() {
                return creditNoteDate;
            }

            @Override
            public DatePicker getDueDatePicker() {
                return dueDate;
            }

            @Override
            public TextBox getNumberTxtBox() {
                return numberTxtBox;
            }

            @Override
            public DataListBox getTaxCalcListBox() {
                return taxCalcTypeListBox;
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
            public Date getConversionDate() {
                return conversionDate;
            }

            @Override
            public void setConversionDate(Date date) {
                conversionDate = date;
            }

            @Override
            public View getView() {
                return CreditNoteView.this;
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
                initCreditNoteCustomFields(result);
            }

            @Override
            public void initSystemCustomFields(NewInvoice result) {
                systemCustomFields = result.getSystemCustomFields();
            }

            @Override
            public void generateForm(String layoutHTML) {
                htmlPanel = new WftHTMLPanel(layoutHTML, widgetsMap).getContainer();
                htmlPanel.setStyleName("add-form");
                htmlPanel.add(createFooter());
                add(htmlPanel);
            }

            @Override
            public NewInvoice getFormData(String invoiceStatus, boolean calculate) {
                return getCreditNoteData(invoiceStatus);
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
            public SplitButton getSplitButtonPdf() {
                return pdfVersionButton;
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
            public SplitButton getApproveSplitButton() {
                return approveButton;
            }

            @Override
            public TextArea getIntroduction() {
                return introductionPanel.getIntroduction();
            }

            @Override
            public boolean validateCustomFields() {
                boolean validate = advancedOptions.validateCustomFieldRequiredFields();

                //if there are not validate fields in the custom fields
                //then we should open the advanced pop-up
                if (!validate) {
                    advancedOptions.getCustomFieldContainer().setActive(0);

                    showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
                }
                return validate;
            }

            @Override
            public boolean validateProjectMandatory() {
                if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY)) {
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
                return CreditNoteView.this.validateSystemCustomFields();
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
            public PdfTemplatePanel getPdfTemplateBox() {
                return pdfTemplatePanel;
            }

            @Override
            public void initPdfTemplates(NewInvoice result) {
//                initCreditNotePdfTemplates(result);
            }

            @Override
            public PlaceOfSupplyWidget getPlaceOfSupplyWidget() {
                return placeOfSupplyWidget;
            }

            @Override
            public MaterialLink getSupplierBalanceLink() {
                return supplierBalanceLink;
            }

            @Override
            public SimpleLink getBankAccountDetailLink() {
                return viewBankAccountDetails;
            }

            @Override
            public DataListBox getBankAccountListBox() {
                return bankAccounts;
            }

            @Override
            public AccountsReceivablePayableLookUp getAccountsReceivablePayableLookUp() {
                return receivablePayableLookUp;
            }

            @Override
            public FormGroup getReverseChargeField() {
                return (FormGroup) widgetsMap.get(INPUT_REVERSE_CHARGE);
            }

            @Override
            public FormGroup getPriceLevel() {
                return priceLevelField;
            }

            @Override
            public Integer validateReason() {
                boolean isSaudiVatRegistered = Utils.isSaudiCompany() && Utils.isVatRegistered();
                int errors = 0;

                if (isSaudiVatRegistered) {
                    boolean reasonInvalid = !Validation.validateDataListBoxRequired(reason);
                    boolean paymentTypeInvalid = !Validation.validateDataListBoxRequired(paymentType);

                    if (reasonInvalid || paymentTypeInvalid) {
                        showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
                        errors += (reasonInvalid ? 1 : 0) + (paymentTypeInvalid ? 1 : 0);
                    }
                }
                return errors;
            }


            @Override
            public Integer validatePaymentTypeCode() {
                if ((Utils.isSaudiCompany() && Utils.isVatRegistered()) && !Validation.validateDataListBoxRequired(paymentType)) {
                    showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
                    return 1;
                }
                return 0;
            }

            @Override
            public DataListBox getPriceLevelDropdown() {
                return priceLevel;
            }

            @Override
            public ChosenApproversWidget getApproverLookUp() {
                return approver;
            }
        });
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {

            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();

                //bank details field
                FormGroup bankField = new FormGroup(wfmStrings.bankDetails(), bankAccounts);
                result.add(bankField);


                if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                    FormGroup projectFields = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), relatedProject, Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY));
                    projectFields.ensureDebugId(InvoiceFormFields.PROJECT);
                    result.add(projectFields);
                }

                GRow formRow = new GRow();
                GColumn poNumberCol = new GColumn(GColumnEnum.COL_6);
                GColumn quoteNumberCol = new GColumn(GColumnEnum.COL_6);

                poNumberCol.add(new FormGroup(accountingStrings.po(), poNumberTxtBox));
                formRow.add(poNumberCol);
                result.add(formRow);

                quoteNumberCol.add(new FormGroup(accountingStrings.sq(), quoteNumberTxtBox));
                formRow.add(quoteNumberCol);
                result.add(formRow);

                result.add(new FormGroup(RECEIVABLE.equals(noteType) ? wfmStrings.accountsReceivable() : wfmStrings.accountsPayable(), receivablePayableLookUp));
                if (Utils.isSaudiCompany() && Utils.isVatRegistered()) {
                    result.add(new FormGroup(wfmStrings.reason(), createResonDropDown()));
                    result.add(new FormGroup(wfmStrings.paymentType(), createPaymentTypeCodeDropDown()));
                }
                result.add(priceLevelField);
                return result;
            }
        });
    }

    private Widget createPaymentTypeCodeDropDown() {
        paymentType = new DataListBox();
        paymentType.setItems(getPaymentTypeCodeItems());
        return paymentType;
    }

    private SelectItem[] getPaymentTypeCodeItems() {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.IN_CASH.getPaymentCode(), NotePaymentMeansCodeEnum.IN_CASH.getName(), String.valueOf(NotePaymentMeansCodeEnum.IN_CASH.getPaymentCode())));
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.CREDIT.getPaymentCode(), NotePaymentMeansCodeEnum.CREDIT.getName(), String.valueOf(NotePaymentMeansCodeEnum.CREDIT.getPaymentCode())));
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.PAYMENT_TO_BANK_ACCOUNT.getPaymentCode(), NotePaymentMeansCodeEnum.PAYMENT_TO_BANK_ACCOUNT.getName(), String.valueOf(NotePaymentMeansCodeEnum.PAYMENT_TO_BANK_ACCOUNT.getPaymentCode())));
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.BANK_CARD.getPaymentCode(), NotePaymentMeansCodeEnum.BANK_CARD.getName(), String.valueOf(NotePaymentMeansCodeEnum.BANK_CARD.getPaymentCode())));
        selectItems.add(new SelectItem(NotePaymentMeansCodeEnum.INSTRUMENT_NOT_DEFINED.getPaymentCode(), NotePaymentMeansCodeEnum.INSTRUMENT_NOT_DEFINED.getName(), String.valueOf(NotePaymentMeansCodeEnum.INSTRUMENT_NOT_DEFINED.getPaymentCode())));
        return selectItems.toArray(new SelectItem[]{});
    }

    private Widget createResonDropDown() {
        reason = new DataListBox();
        reason.setItems(getReasonItems());
        return reason;
    }

    private Widget createPaymentTypeDropDown() {
        reason = new DataListBox();
        reason.setItems(getPaymentTypeItems());
        return reason;
    }

    private SelectItem[] getPaymentTypeItems() {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        selectItems.add(new SelectItem(1, CANCELLATION_OF_SUPPLIES.getValue(), CANCELLATION_OF_SUPPLIES.name()));
        selectItems.add(new SelectItem(2, CHANGES_TO_VAT_DUE.getValue(), CHANGES_TO_VAT_DUE.name()));
        selectItems.add(new SelectItem(3, CHANGES_VALUE_OF_SUPPLY.getValue(), CHANGES_VALUE_OF_SUPPLY.name()));
        selectItems.add(new SelectItem(4, GOOD_SERVICES_REFUND.getValue(), GOOD_SERVICES_REFUND.name()));
        selectItems.add(new SelectItem(5, CHANGES_TO_SELLER_OR_BUYER_INFO.getValue(), CHANGES_TO_SELLER_OR_BUYER_INFO.name()));
        return selectItems.toArray(new SelectItem[]{});
    }

    private SelectItem[] getReasonItems() {
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        selectItems.add(new SelectItem(1, CANCELLATION_OF_SUPPLIES.getValue(), CANCELLATION_OF_SUPPLIES.name()));
        selectItems.add(new SelectItem(2, CHANGES_TO_VAT_DUE.getValue(), CHANGES_TO_VAT_DUE.name()));
        selectItems.add(new SelectItem(3, CHANGES_VALUE_OF_SUPPLY.getValue(), CHANGES_VALUE_OF_SUPPLY.name()));
        selectItems.add(new SelectItem(4, GOOD_SERVICES_REFUND.getValue(), GOOD_SERVICES_REFUND.name()));
        selectItems.add(new SelectItem(5, CHANGES_TO_SELLER_OR_BUYER_INFO.getValue(), CHANGES_TO_SELLER_OR_BUYER_INFO.name()));
        return selectItems.toArray(new SelectItem[]{});
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return CreditNoteView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return CreditNoteView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.addClickHandler(clickEvent -> noteHistoryWidget.setLoadData(callback -> {
            if (objectID == null) {
                return;
            }
            InvoiceService.App.get().loadInvoiceHistoryNote(objectID, RECEIVABLE.equals(noteType) ? RECEIVABLE_CREDIT_NOTE : PAYABLE_CREDIT_NOTE, true, callback);
        }));
        FooterInformer footerIntroduction = null;
        informer.setInitialClasses("informer-item history-notes-container");


        leftSideWidgets.add(informer);
        if (RECEIVABLE.equals(this.noteType)) {
            footerIntroduction = new FooterInformer(SvgEnum.docTitle, wfmStrings.introduction(), introductionPanel.getIntroduction());
            footerIntroduction.setInitialClasses("informer-item");
            leftSideWidgets.add(footerIntroduction);
        }
        leftSideWidgets.add(uploadPanel);

        return leftSideWidgets;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightWidgets = new ArrayList<>();
        Div draftButtonWrapper = new Div();
        draftButtonWrapper.add(saveButton);
        rightWidgets.add(draftButtonWrapper);


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
        return rightWidgets;
    }

    private void initCreditNoteCustomFields(NewInvoice invoice) {
        if (invoice.getCustomFieldItems() != null && invoice.getCustomFieldItems().size() > 0) {
            advancedOptions.createAndAppendInvoiceCustomFieldsView(RECEIVABLE.equals(noteType) ? ViewAddFiledsCodeName.SaleInvoiceAdd : ViewAddFiledsCodeName.PurchaseInvoiceAdd, invoice);
        }
    }

    public void fillForm(NewInvoice result) {
        currencyWidget.setCurrency(result.getCurrencyID(), result.getExchageRate());
        crmAccountWidgets.setCurrencyWidget(currencyWidget);
        productsTable.getBaseTotalLabel().setHTML(accountingMessages.dynamicTotal(currencyWidget.getBaseCurrencyName()));

        if (objectID != null) {
            setValues(result);
        } else if (formParameters.isExternalForm(COPY_INVOICE_TO_CREDITNOTE) || formParameters.isExternalForm(COPY_FROM_EXISTING_DATA)) {
            setValues(result);
        } else {
            applyTaxCalculationTypeChanges(result.getTaxCalculationType());
            if (result.getTypeItem() != null) {
                if (result.getTypeItem().getSupplierCustomerBalance() >= 0) {
                    supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    supplierBalanceLink.setText(AccountingUtils.get().formatPrice(result.getTypeItem().getSupplierCustomerBalance()));
                } else {
                    supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    supplierBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * result.getTypeItem().getSupplierCustomerBalance()) + ")");
                }
            }
            creditNoteDate.setDate(new Date());
            if (result.getDueDays() != null) {
                dueDate.setDate(DateUtil.addDays(currentDate, result.getDueDays() != null ? result.getDueDays() : 0));
            }
        }
        if (result.getAccountsReceivablePayable() != null) {
            receivablePayableLookUp.addAccountItem(result.getAccountsReceivablePayable());
        }
        referenceTxtBox.setText(result.getReference());
        poNumberTxtBox.setText(result.getPoNumber());
        quoteNumberTxtBox.setText(result.getQuoteNumber());
        if (Utils.isSaudiCompany() && Utils.isVatRegistered()) {
            if (result.getPaymentTypeCode() != null) {
                paymentType.setSelectedByDescription(String.valueOf(result.getPaymentTypeCode()));
            }
            if (!Utils.isNullOrEmpty(result.getNoteReason())) {
                reason.setSelectedByDescription(result.getNoteReason());
            }
        }
    }


    private void initProductTable(NewInvoice result) {
        productsTable.setColumnsMap(drawColumns(result));
        productsTable.initItemTable(result.isProgressInvoicing() && Utils.hasGenericAccess(GenericSettingsEnum.EDIT_SALE_INVOICE_CONVERTED_FROM_SQ_SO));
        productsTable.setDefaultAccount(result.getDefaultAccountItem());
        productsTable.setDefaultDiscount(result.getDefaultDiscountItem());
        productsTable.setDefaultTax(result.getDefaultTaxItem());

    }

    public NewInvoice getCreditNoteData(String invoiceStatus) {
        productsTable.calculate(true);

        NewInvoice invoice = new NewInvoice();
        invoice.setClientID(crmAccountLookUp.getSelectedItemID());
        invoice.setBillAddressID(crmAccountWidgets.getBillAddressID());
        invoice.setMailAddressID(crmAccountWidgets.getMailAddressID());
        invoice.setCurrencyID(currencyWidget.getCurrencyID());
        invoice.setExchageRate(productsTable.getExchangeRateValue());
        invoice.setRelatedProject(relatedProject.getSelectedItem());
        invoice.setShippingMethodID(productsTable.getShippingMethod() != null ? productsTable.getShippingMethod().getId() : null);
        if (placeOfSupplyWidget != null) {
            invoice.setPlaceOfSupply(placeOfSupplyWidget.getSelectedPlaceOfSupply());

            if (placeOfSupplyWidget.getReverseChargeBox().isAttached())
                invoice.setReversechargeApplicable(placeOfSupplyWidget.getReverseChargeBox().getValue());
        }
        if (reverseChargeBox != null) {
            invoice.setReversechargeApplicable(reverseChargeBox.getValue());
        }
        if (RECEIVABLE.equals(noteType)) {
            Integer fourDigitNumber = invoiceNumberData.parseFourDigitNumber(numberTxtBox.getText());
            if (fourDigitNumber != null) {
                invoice.setFourDigitNumber(fourDigitNumber.toString());
            }
        } else if (invoiceNumberData != null && invoiceNumberData.getFourDigitNumber() != null && PAYABLE.equals(noteType)) {
            invoice.setFourDigitNumber(invoiceNumberData.getFourDigitNumber());
        }

        invoice.setInvoiceNumber(numberTxtBox.getText());

        invoice.setInvoiceDate(new DateNonConvertable(DateUtil.resetTime(creditNoteDate.getDate())));
        invoice.setDueDate(new DateNonConvertable(DateUtil.getDayLastTime(dueDate.getDate())));
        if (RECEIVABLE.equals(this.noteType)) {
            invoice.setIntroduction(introductionPanel.getIntroduction().getText());
            invoice.setPaymentInstruction(productsTable.getPaymentInstruction().getText());
        }
        if (PAYABLE.equals(this.noteType)) {
            invoice.setPaymentInstruction(productsTable.getPaymentInstruction().getText());
        }
        invoice.setSubtotal(productsTable.getSubTotal());
        invoice.setTotalDiscount(productsTable.getTotalDiscount());
        invoice.setShippingPrice(productsTable.getShippingTotal());
        invoice.setTotalInInvoiceCurrency(productsTable.getTotalInInvoiceCurrency());
        invoice.setTotal(productsTable.getTotalInBaseCurrency());
        invoice.setTotalTaxes(productsTable.getTotalTaxAmountByCurrency());
        invoice.setTotalTaxes(invoice.getTotalTaxes().add(productsTable.getShippingTaxAmount()));
        invoice.setTotalTaxItems(productsTable.getTotalTaxItems());
        invoice.setType(noteType);
        invoice.setStatusCode(invoiceStatus);
        if (pdfTemplatePanel != null) {
            invoice.setPdfTemplateID(pdfTemplatePanel.getSelectedTemplateID());
        }
        invoice.setCustomFieldItems(advancedOptions.getCustomFieldsData());
        invoice.setTaxCalculationType(taxCalcTypeListBox.getSelectedId());
        invoice.setItems(productsTable.getDataItems(invoiceStatus));

        //simplifying to filter for transactions
        if (relatedProject != null && !Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            for (NewInvoiceItem item : invoice.getItems()) {
                item.setProject(relatedProject.getSelectedItem());
            }
        }
        invoice.setCreditedInvoiceAmount(productsTable.getCreditedInvoiceAmount());
        invoice.setCreditedInvoiceID(formParameters.isExternalForm(COPY_INVOICE_TO_CREDITNOTE) ? formParameters.getExternalObjectID() : null);
        invoice.setReference(referenceTxtBox.getText());
        invoice.setPoNumber(poNumberTxtBox.getText());
        invoice.setQuoteNumberCN(quoteNumberTxtBox.getText());
        invoice.setPriceLevel(priceLevel.getSelectedItem());
        invoice.setBankAccount(bankAccounts.getSelectedItem());
        invoice.setAccountsReceivablePayable(receivablePayableLookUp.getSelectedData());
        invoice.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
        invoice.setAttachments(uploadPanel.getAttachedFiles());
        if (Utils.isSaudiCompany() && Utils.isVatRegistered()) {
            invoice.setNoteReason(reason.getSelectedItem() != null ? reason.getSelectedItem().getDescription() : null);
            invoice.setPaymentTypeCode(paymentType.getSelectedItem() != null ? paymentType.getSelectedItem().getId() : null);
        }
        invoice.setApprovers(approver.getChosenApprovers());
        return invoice;
    }

    public void setValues(NewInvoice creditNote) {
        crmAccountLookUp.addItem(creditNote.getTypeItem());

        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            formPresenter.configurePlaceOfSupply(creditNote.getTypeItem(), creditNote.getPlaceOfSupply(), placeOfSupplyWidget);

            if (PAYABLE.equalsIgnoreCase(noteType)) {
                placeOfSupplyWidget.getReverseChargeBox().setValue(creditNote.isReversechargeApplicable());
            }
        }
        if (reverseChargeBox != null) {
            reverseChargeBox.setValue(creditNote.isReversechargeApplicable());
            productsTable.clearSelectedTaxFromItems(creditNote.isReversechargeApplicable());
        }
        if (creditNote.getTypeItem().getSupplierCustomerBalance() >= 0) {
            supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
            supplierBalanceLink.setText(AccountingUtils.get().formatPrice(creditNote.getTypeItem().getSupplierCustomerBalance()));
        } else {
            supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
            supplierBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * creditNote.getTypeItem().getSupplierCustomerBalance()) + ")");
        }

        if (creditNote.getRelatedProject() != null) {
            relatedProject.addItem(creditNote.getRelatedProject());
        }

        crmAccountLookUp.getTextBox().setEnabled(!formParameters.isExternalForm(COPY_INVOICE_TO_CREDITNOTE));
        if (formParameters.isEditForm() || formParameters.isExternalForm(COPY_INVOICE_TO_CREDITNOTE)) {
            currencyWidget.setCurrency(creditNote.getCurrencyID(), creditNote.getExchageRate());
        } else {
            currencyWidget.setCurrency(creditNote.getCurrencyID());
        }

        if (formParameters.isEditForm()) {
            if (RECEIVABLE.equals(noteType)) {
                invoiceNumberData = creditNote.getNumberData();
                String dateString = DateTimeFormat.getFormat("yyyyMMdd").format(creditNoteDate.getDate());
                invoiceNumberData.setWithDate(creditNote.getInvoiceNumber().contains(dateString));
                invoiceNumberData.setDate(invoiceNumberData.isWithDate() ? dateString : "");
            }
            numberTxtBox.setText(creditNote.getInvoiceNumber());
        }

        creditNoteDate.setDate(creditNote.getInvoiceDate().getNonConvertedDate());
        dueDate.setDate(creditNote.getDueDate().getNonConvertedDate());

        applyTaxCalculationTypeChanges(creditNote.getTaxCalculationType());

        productsTable.setExchangeRateValue(creditNote.getExchageRate());

        if (RECEIVABLE.equals(this.noteType)) {
            introductionPanel.getIntroduction().setText(creditNote.getIntroduction());
            productsTable.getPaymentInstruction().setText(creditNote.getPaymentInstruction());

            /*if (creditNote.getShippingMethodID() != null && creditNote.getShippingMethodName() != null) {
                shippingMethod.getShippingMethodLookUp().addItems(new ShippingMethod[]{creditNote.getShippingMethod()});
                shippingMethod.getShippingMethodLookUp().setSelected(creditNote.getShippingMethod());
                formPresenter.onShippingMethodChange();
            }*/
        }
        if (PAYABLE.equals(this.noteType)) {
            productsTable.getPaymentInstruction().setText(creditNote.getPaymentInstruction());
        }

        if (formParameters.isExternalForm(COPY_INVOICE_TO_CREDITNOTE)) {
            //Setting Invoice Number And Total (in invoice currency) As Credit Note refund
            if (RECEIVABLE.equals(noteType)) {
                productsTable.setCreditPaymentParams(creditNote.getNumberData().getInvoiceNumber(), creditNote.getTotalInInvoiceCurrency());
            } else {
                productsTable.setCreditPaymentParams(creditNote.getInvoiceNumber(), creditNote.getTotalInInvoiceCurrency());
            }
            BigDecimal payments = ZERO;
            if (creditNote.getPaymentItems() != null) {
                creditNote.getPaymentItems();
                for (PaymentItem pi : creditNote.getPaymentItems()) {
                    if (pi.getAmount() != null) {
                        payments = payments.add(pi.getAmount());
                    }
                }
            }
            formParameters.setMaximalAmount(creditNote.getTotalInInvoiceCurrency().subtract(payments));
        }
        productsTable.setExternalFormType(formParameters.getExternalFormID());
        productsTable.setValues(creditNote);
    }

    private void applyTaxCalculationTypeChanges(Integer taxCalculationType) {
        taxCalcTypeListBox.setSelected(taxCalculationType != null ? AccountingUtils.getTaxCalcType(taxCalculationType) : AccountingUtils.getTaxCalcType(TAX_CALCULATION_EXCLUSIVE));
        productsTable.onTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), false);
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

    Command onPlaceOfSupplyChange() {
        return () -> {
            if (placeOfSupplyWidget == null) {
                return;
            }
            String treatment = placeOfSupplyWidget.getTaxTreatment() != null ? placeOfSupplyWidget.getTaxTreatment().getCode() : null;
            boolean disableTaxField = NON_VAT_REGISTERED.equals(treatment) || NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(treatment);

            if (disableTaxField) {
                productsTable.clearSelectedTaxFromItems(true);
                return;
            }

            if (NON_GCC.equals(treatment) || GCC_VAT_REGISTERED.equals(treatment) || GCC_NON_VAT_REGISTERED.equals(treatment)) {

                if (Utils.isSaudiCompany()) {
                    if (placeOfSupplyWidget.getReverseChargeBox().isAttached() && placeOfSupplyWidget.getReverseChargeBox().isAttached()) {
                        productsTable.clearSelectedTaxFromItems(!placeOfSupplyWidget.getReverseChargeBox().getValue());
                    } else {
                        productsTable.clearSelectedTaxFromItems(!GCC_VAT_REGISTERED.equals(treatment));
                    }
                    return;
                }
            }
            productsTable.clearSelectedTaxFromItems(false);
        };
    }
}
