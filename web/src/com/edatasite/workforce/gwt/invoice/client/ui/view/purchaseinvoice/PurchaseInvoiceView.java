package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditFixedAssetForm;
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
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
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
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CrmAccountWidgets;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PdfTemplatePanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.historyNote.InvoiceNoteHistoryWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDuePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_HISTORY_NOTES;

/**
 * User: Sherzod
 * Date: 2/3/12
 * Time: 1:44 PM
 */
public class PurchaseInvoiceView extends FooteredView implements Colapse, FittedContent, Constants, AccountingConstants, AccountingCustomFormConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final DateTimeFormat dateFormatForParse = DateTimeFormat.getFormat("MM.dd.yyyy");
    private final Date currentDate = new Date();

    private Integer objectID;

    private CrmAccountLookUp crmAccountLookUp;

    private ProjectLookUp relatedProject;
    private TextBox invoiceNumber;
    private TextBox poNumber;
    private TextBox referenceTxtBox;
    private DatePicker invoiceDate;
    private TermsAndDuePanel termsAndDueDatePanel;
    private HorizontalPanel dueAndTearmsPanel;
    private Date conversionDate;
    private DataListBox taxCalcTypeListBox;
    private AccountsReceivablePayableLookUp receivablePayableLookUp;
    private InvoiceNumberData invoiceNumberData;
    private RecurringWidget recurringWidget;

    //Custom Fields
    private DatePicker cancelDate;

    private CrmAccountWidgets crmAccountWidgets;
    private CurrencyWidget currencyWidget;
    private ProductsTable productsTable;

    private CrmAccountLookUp dropShipToCustomerLookUp;

    private FooterUploadPanel uploadPanel;
    private NoteHistoryWidget noteHistoryWidget;

    private DataListBox priceLevel;
    private FormGroup priceLevelField;

    private boolean isFixedAssetRelated;
    private FixedAssetItem fixedAssetItem;

    //    private boolean isExRateKept = false;
    private final BigDecimal exRateFromOrder = BigDecimal.ONE;

    private Params formParameters;

    private WfmButton2 saveButton, saveAndApproveButton, submitButton;
    private SplitButton pdfVersionButton;

    private HashMap<String, Widget> widgetsMap;
    private PurchaseInvoiceFormPresenter formPresenter;
    private HTMLPanel htmlPanel;
    private final String purchaseInvoiceView = "purchase_Invoice_View";

    private PdfTemplatePanel pdfTemplatePanel;
    private MaterialLink supplierBalanceLink;
    private InvoiceAdvancedOptions advancedOptions;
    private MaterialLink showMoreLink;
    private boolean isRecurringBill;
    private PlaceOfSupplyWidget placeOfSupplyWidget;
    private ChosenApproversWidget approver;
    private boolean hasApproval;
    private Integer relationID;
    private String relationType;
    private String relationName;
    private KpiCheckBox reverseChargeBox;

    public PurchaseInvoiceView(final String[] params) {
        super("purchaseinvoiceadd");
        this.property = new Property(this.getPropertyCode());
        this.setDescription(this.property.getSingular(PurchaseInvoiceView.wfmStrings.addMess(), PurchaseInvoiceView.wfmStrings.purchaseinvoice()));
        this.initFormParameters(params);
    }

    public PurchaseInvoiceView(final String[] params, final boolean isRecurringBill) {
        super((isRecurringBill ? "recurringbilladd" : "purchaseinvoiceadd"));
        this.isRecurringBill = isRecurringBill;
        this.property = new Property(this.getPropertyCode());
        this.setDescription((isRecurringBill ? this.property.getSingular(PurchaseInvoiceView.wfmStrings.addMess(), PurchaseInvoiceView.accountingStrings.recurringBill()) : this.property.getSingular(PurchaseInvoiceView.wfmStrings.addMess(), PurchaseInvoiceView.wfmStrings.purchaseinvoice())));
        this.initFormParameters(params);
        this.formParameters.setRecurringInvoice(isRecurringBill);
    }

    public PurchaseInvoiceView(final Integer objectID, final String[] params, final boolean isRecurringBill) {
        super("edit");
        this.isRecurringBill = isRecurringBill;
        this.objectID = objectID;
        this.property = new Property(this.getPropertyCode());
        this.setDescription((isRecurringBill ? this.property.getSingular(PurchaseInvoiceView.accountingStrings.editRecurringBill(), PurchaseInvoiceView.accountingStrings.recurringBill()) : this.property.getSingular(PurchaseInvoiceView.accountingStrings.editPurchaseInvoice(), PurchaseInvoiceView.wfmStrings.purchaseinvoice())));
        this.initFormParameters(params);
        this.formParameters.setRecurringInvoice(isRecurringBill);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void initFormParameters(final String[] params) {
        this.formParameters = new Params();
        this.formParameters.setType(Constants.PAYABLE);
        this.formParameters.setFormType(Constants.PURCHASE_INVOICE);
        this.formParameters.setObjectID(this.objectID);
        if (params != null) {
            if (params.length > 2 && params[1].equals("opportunity")) {
                this.formParameters.setCrmFormName(params[1]);
                this.formParameters.setOpportunityID(Integer.valueOf(params[2]));
                relationID = Integer.valueOf(params[2]);
                relationType = RelationItem.TYPE_OPPORTUNITY;
            } else if (params.length > 2 && params[1].equals("copyFromExistingData")) {
                this.formParameters.setExternalFormID(AccountingConstants.COPY_FROM_EXISTING_DATA);
                this.formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && (params[1].equals("fromSupplierList") || params[1].equals("supplier"))) {
                this.formParameters.setExternalFormID(AccountingConstants.COPY_FROM_CLIENT_SUPPLIER);
                if (params[1].equals("supplier")) {
                    this.formParameters.setCrmFormName(params[1]);
                }
                this.formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && params[1].equals("convertToInvoice")) {
                this.formParameters.setExternalFormID(AccountingConstants.CONVERT_TO_INVOICE);
                this.formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && params[1].equals("convertFromGrn")) {
                this.formParameters.setExternalFormID(AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN);
                this.formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 1 && "relatedProject".equals(params[1])) {
                this.formParameters.setExternalFormID(AccountingConstants.COPY_FROM_CLIENT_SUPPLIER);
                if (params[2] != null) {
                    this.formParameters.setRelatedProjectID(Integer.valueOf(params[2]));
                }
            } else if (params.length > 1 && "copyFromFixedAsset".equals(params[1])) {
                this.formParameters.setExternalFormID(AccountingConstants.COPY_FROM_FIXED_ASSET);
            } else if (params.length > 1 && "copyFromPO".equals(params[1])) {
                this.formParameters.setExternalFormID(AccountingConstants.COPY_PO_TO_PI);
                this.formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && "copySalesInvoice".equals(params[1])) {
                this.formParameters.setExternalFormID(AccountingConstants.COPY_FROM_SI_TO_PI);
                this.formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && "CONVERT".equals(params[0])) {
                this.formParameters.setConvertFormType(params[1]);
                if (params[2] != null && params[2].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                    this.formParameters.setConvertFormId(Integer.parseInt(params[2]));
                }
            } else {
                if (params.length > 1) {
                    try {
                        this.formParameters.setMaximalAmount(AccountingUtils.get().parseToBigDecimal(params[1]));
                    } catch (final Exception e) {
                        this.formParameters.setMaximalAmount(null);
                    }
                }
                if (params.length > 2) {
                    this.formParameters.setConversionDate(this.dateFormatForParse.parse(params[2]));
                }
                this.formParameters.setFromGettingStarted(this.formParameters.getMaximalAmount() != null && this.formParameters.getConversionDate() != null);
            }
        }
    }

    @Override
    protected Widget onInitialize() {

        boolean hasPermissonSupplierQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_QUICK_ADD);
        boolean hasPermissonSupplierAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_ADD);

        this.crmAccountLookUp = new SmartCrmAccountLookup(CrmConstants.SUPPLIER, true, () -> {
            if (hasPermissonSupplierQuick) {
                new CusSuppQuickAddView(CrmConstants.SUPPLIER, this.crmAccountLookUp.getLastValueBeforeClick());
            } else if (hasPermissonSupplierAdd) {
                SinksContainerFactory.entryPoint.onHistoryChanged("supplier|add/add");
            }
        }, false, hasPermissonSupplierQuick || hasPermissonSupplierAdd);
        this.crmAccountLookUp.ensureDebugId(this.purchaseInvoiceView + "crmAccountLookUp");
        this.crmAccountLookUp.setEnsureSuggestBox(this.purchaseInvoiceView + "crmAccountLookUp");
        this.crmAccountLookUp.setAutocompleteOff();
        this.crmAccountWidgets = new CrmAccountWidgets(this.crmAccountLookUp, Constants.PAYABLE, Constants.PURCHASE_INVOICE, this.formParameters.isEditForm());

        if (AccountingConstants.CONVERT_TO_INVOICE.equals(this.formParameters.getExternalFormID()) || AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN.equals(this.formParameters.getExternalFormID())) {
            this.crmAccountLookUp.setEnabled(false);
        }
        this.relatedProject = new ProjectLookUp(Constants.PAYABLE);
        this.relatedProject.ensureDebugId(this.purchaseInvoiceView + "relatedProject");
        this.relatedProject.setEnsureSuggestBox(this.purchaseInvoiceView + "relatedProject");

        this.priceLevel = new DataListBox();
        this.priceLevel.ensureDebugId(this.purchaseInvoiceView + "priceLevel");

        this.priceLevelField = new FormGroup(PurchaseInvoiceView.accountingStrings.relatedPriceLevel(), this.priceLevel);
        this.priceLevelField.ensureDebugId("inv_price_level");

        this.currencyWidget = new CurrencyWidget(!this.formParameters.isEditForm() && !AccountingConstants.COPY_FROM_FIXED_ASSET.equals(this.formParameters.getExternalFormID()));

        this.dropShipToCustomerLookUp = new CrmAccountLookUp(CrmConstants.CUSTOMER, true);
        this.dropShipToCustomerLookUp.ensureDebugId(this.purchaseInvoiceView + "customerLookUp");
        this.dropShipToCustomerLookUp.setEnsureSuggestBox(this.purchaseInvoiceView + "customerLookUp");
        this.crmAccountWidgets.setDropShipToCustomerLookUp(this.dropShipToCustomerLookUp);

        this.invoiceNumber = new TextBox();
        this.invoiceNumber.ensureDebugId(this.purchaseInvoiceView + "invoiceNumber");
        this.poNumber = new TextBox();
        this.poNumber.ensureDebugId(this.purchaseInvoiceView + "poNumber");
        this.referenceTxtBox = new TextBox(true);
        this.referenceTxtBox.ensureDebugId(this.purchaseInvoiceView + "referenceTxtBox");
        this.invoiceDate = new DatePicker(this.currentDate, true);
        this.invoiceDate.ensureDebugId(this.purchaseInvoiceView + "invoiceDate");

        this.termsAndDueDatePanel = new TermsAndDuePanel(View.wfmStrings.dueDate());
        this.termsAndDueDatePanel.ensureDebugId(this.purchaseInvoiceView + "termsAndDueDatePanel");

        this.taxCalcTypeListBox = new DataListBox();
        this.taxCalcTypeListBox.ensureDebugId("tax-list");
        this.taxCalcTypeListBox.setWithoutNullLabel(true);
        this.taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        this.taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE));
        this.taxCalcTypeListBox.addValueChangeHandler(event -> {
            //This is for changing tax calculation type and recalculating
            this.productsTable.onTaxCalculationTypeChange(this.taxCalcTypeListBox.getSelectedId(), true);
        });
        if (AccountingConstants.COPY_FROM_FIXED_ASSET.equals(this.formParameters.getExternalFormID())) {
            this.taxCalcTypeListBox.setEnabled(false);
        }

        this.receivablePayableLookUp = new AccountsReceivablePayableLookUp(Constants.PAYABLE);
        this.receivablePayableLookUp.getElement().getStyle().setMarginTop(20, Style.Unit.PX);

        this.cancelDate = new DatePicker();

        if (relationID != null && relationType != null) {
            getRelationName(relationID, relationType);
        }

        if (this.formParameters.isRecurringInvoice()) {
            this.recurringWidget = new RecurringWidget(SchedulerConstant.RECURRING_BILL_FORM);
            this.recurringWidget.ensureDebugId(this.purchaseInvoiceView + "recurringFormView");
        }

        if (Constants.GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            if (Utils.isUAECompany()) {
                this.placeOfSupplyWidget = new UaePlaceOfSupplyWidget(this.onPlaceOfSupplyChange(), () -> this.productsTable.onReverseChargeChange());
            } else if (Utils.isSaudiCompany()) {
                this.placeOfSupplyWidget = new KsaPlaceOfSupplyWidget(this.onPlaceOfSupplyChange(), () -> this.productsTable.onReverseChargeChange());
            }
        }

        this.productsTable = new ProductsTable(Constants.PAYABLE, Constants.PURCHASE_INVOICE, this.formParameters);
        this.productsTable.setCurrencyWidget(currencyWidget);
        this.currencyWidget.setDatePicker(this.invoiceDate);
        this.productsTable.setReverseChargeBox(this.placeOfSupplyWidget != null ? this.placeOfSupplyWidget.getReverseChargeBox() : null);
        this.productsTable.setPlaceOfSupplyBox(this.placeOfSupplyWidget != null ? this.placeOfSupplyWidget.getPlaceOfSupplyBox() : null);

        if (Utils.isUKVATRegistered()) {
            reverseChargeBox = new KpiCheckBox();
            reverseChargeBox.setText("This transaction is applicable for reverse charge");
            reverseChargeBox.addValueChangeHandler((x) -> productsTable.onReverseChargeChange());
            productsTable.setReverseChargeBox(reverseChargeBox);
        }
        this.uploadPanel = new FooterUploadPanel(Constants.F_PUR_INV, this.objectID, true, PurchaseInvoiceView.wfmStrings.attachments());
        this.noteHistoryWidget = new InvoiceNoteHistoryWidget();

        this.advancedOptions = this.createAdvancedOptions();
        this.advancedOptions.addToAddressBodyContainer(this.crmAccountWidgets.getBillAddress());
        this.advancedOptions.addToMailAddressBodyContainer(this.crmAccountWidgets.getMailAddress());
        this.showMoreLink = new MaterialLink(View.wfmStrings.showAdditionalFields());
        this.showMoreLink.addClickHandler(ch -> this.showAdvancedOptions(View.wfmStrings.additionalFields(), this.advancedOptions));

        this.supplierBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        this.supplierBalanceLink.setHref("javaScript:void(0)");
        this.supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        this.approver = new ChosenApproversWidget(RelationItem.TYPE_PURCHASE_INVOICE, this.objectID);

        this.submitButton = new WfmButton2(View.wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        this.submitButton.ensureDebugId(this.purchaseInvoiceView + "submitButton");
        this.submitButton.setVisible(false);

        this.saveButton = new WfmButton2(PurchaseInvoiceView.wfmStrings.draft(), Constants.BTN_DEFAULT_OUTLINE);
        this.saveButton.ensureDebugId(this.purchaseInvoiceView + "saveButton");

        this.saveAndApproveButton = new WfmButton2(this.objectID != null ? PurchaseInvoiceView.accountingStrings.updateAndApprove() : PurchaseInvoiceView.wfmStrings.saveAndApprove(), WfmButton2.BTN_PRIMARY);
        this.saveAndApproveButton.ensureDebugId(this.purchaseInvoiceView + "saveAndApproveButton");

        this.pdfVersionButton = new SplitButton(97, WfmButton2.BTN_WHITE_OUTLINE);
        this.pdfVersionButton.ensureDebugId(this.purchaseInvoiceView + "pdfVersionButton");

        this.saveButton.setVisible(false);
        this.saveAndApproveButton.setVisible(false);

        this.widgetsMap = new HashMap<>();

        this.initApproverLoadHandler();

        formPresenter = this.generateFormPresenter();
        formPresenter.bindUI();

        return null;
    }

    private void initApproverLoadHandler() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, this, (sender, args) -> {
            if (this.approver.getFirstApproverLookUp() != null) {
                this.approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    final SelectItem item = this.approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        saveAndApproveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        saveAndApproveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                });
                if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        saveAndApproveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        saveAndApproveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            }
        });
    }

    private void initWidgetsMap(final NewInvoice data) {

        this.invoiceDate.setStyleName(AccountingCustomFormConstants.STYLE_DATE_PICKER);
        this.productsTable.getPaymentInstruction().setStyleName(AccountingCustomFormConstants.STYLE_TERMS_INSTRUCTION);

        //supplier field
        final FormGroup supplierField = new FormGroup(this.crmAccountLookUp);
        if (this.formParameters.isEditForm() && Constants.PAID.equals(data.getStatusCode()) || (data.getPaymentItems() != null && data.getPaymentItems().length > 0)) {
            this.crmAccountLookUp.setEnabled(false);
        }

        final Div supplierFieldLabel = supplierField.getGroupLabel();
        supplierFieldLabel.addStyleName("label-group");

        supplierFieldLabel.add(new Span(Property.get(Constants.SUPPLIER_LIST, PurchaseInvoiceView.wfmStrings.supplier())));

        final Span balance = new Span(PurchaseInvoiceView.wfmStrings.balance() + ": ");
        balance.add(this.supplierBalanceLink);
        supplierFieldLabel.add(balance);
        this.widgetsMap.put(AccountingCustomFormConstants.INPUT_CRM_ACCOUNT, supplierField);

        //drop ship field
        final FormGroup dropShipField = new FormGroup(Property.get(Constants.CLIENT_LIST, PurchaseInvoiceView.wfmStrings.customer()), this.dropShipToCustomerLookUp);
        dropShipField.ensureDebugId("inv_drop_ship");
        this.widgetsMap.put(AccountingCustomFormConstants.INPUT_CUSTOMER, dropShipField);

        if (this.formParameters.isEditForm() && data.getConvertedItemID() == null &&
                (Constants.PAID.equals(data.getStatusCode()) ||
                        (data.getPaidAmount() != null && data.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) ||
                        (data.getPaymentItems() != null && data.getPaymentItems().length > 0))) {
            this.currencyWidget.setEnabled(false);
        }
        currencyWidget.ensureDebugId(InvoiceFormFields.CURRENCY);
        systemCustomFieldsMap.put(AccountingCustomFormConstants.INPUT_CURRENCY_LIST_BOX, currencyWidget);

        this.widgetsMap.put(AccountingCustomFormConstants.INPUT_DATE, new FormGroup(PurchaseInvoiceView.wfmStrings.date(), this.invoiceDate));
        this.widgetsMap.put(AccountingCustomFormConstants.INPUT_DUE_DATE, this.termsAndDueDatePanel.getTermsDueAsField());

        referenceTxtBox.ensureDebugId(InvoiceFormFields.REFERENCE);
        systemCustomFieldsMap.put(AccountingCustomFormConstants.INPUT_REFERENCE, referenceTxtBox);

        taxCalcTypeListBox.ensureDebugId(InvoiceFormFields.TAX_CALC);
        this.systemCustomFieldsMap.put(AccountingCustomFormConstants.INPUT_TAX_CALC_TYPE, taxCalcTypeListBox);

        this.systemCustomFieldsMap.put(AccountingCustomFormConstants.INPUT_SHOW_MORE, showMoreLink);

        addSystemCustomFields(widgetsMap);

        if (this.formParameters.isRecurringInvoice() && this.recurringWidget != null) {
            this.widgetsMap.put(AccountingCustomFormConstants.INPUT_RECURRING_VIEW, this.recurringWidget);
        }

        if (!this.formParameters.isRecurringInvoice()) {
            final FormGroup numberField = new FormGroup(this.property.getShortForNumber(PurchaseInvoiceView.wfmStrings.invoiceNumber()), this.wrapWidgetToFormControl(this.invoiceNumber));
            this.widgetsMap.put(AccountingCustomFormConstants.INPUT_NUMBER, numberField);
        }
        if ((Utils.isUAECompany() || Utils.isSaudiCompany()) && Utils.isVatRegistered()) {
            this.widgetsMap.put(AccountingCustomFormConstants.INPUT_PLACE_OF_SUPPLY, this.placeOfSupplyWidget);
        }
        if (Utils.isUKVATRegistered()) {
            widgetsMap.put(INPUT_PLACE_OF_SUPPLY, new GRow(new GColumn(new FormGroup("&nbsp;", reverseChargeBox))));
        }

        this.widgetsMap.put(AccountingCustomFormConstants.INPUT_ITEM_TABLE, this.productsTable.getItemsTable());
        this.widgetsMap.put(AccountingCustomFormConstants.INPUT_TOTALS_TABLE, this.productsTable.getTotalsTable());

        final DataListBox paymentTermsConditionsListBox = this.productsTable.getPaymentTermsConditionsListBox();
        paymentTermsConditionsListBox.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        paymentTermsConditionsListBox.getElement().getStyle().setFloat(Style.Float.LEFT);
        this.widgetsMap.put(AccountingCustomFormConstants.INPUT_INSTRUCTION, new FormGroup(PurchaseInvoiceView.wfmStrings.paymentInstructions(), this.productsTable.getPaymentInstruction()));

        this.widgetsMap.put(AccountingCustomFormConstants.SAVE_AS_DRAFT_BUTTON, this.saveButton);
        this.widgetsMap.put(AccountingCustomFormConstants.SUBMIT_TO_MANAGER_BUTTON, this.submitButton);
        this.widgetsMap.put(AccountingCustomFormConstants.SAVE_AND_APPROVE_BUTTON, this.saveAndApproveButton);
        this.widgetsMap.put(AccountingCustomFormConstants.PDF_VERSION, this.pdfVersionButton);

        if (data.isApprover()) {
            this.widgetsMap.put(AccountingCustomFormConstants.INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(View.wfmStrings.manager(), this.approver)));

            if (this.formParameters.isEditForm() && !data.isApproverSaved() && data.getPaidAmount() == null) {
                this.approver.reloadApproverWidgets(RelationItem.TYPE_PURCHASE_INVOICE, null);
            }
        }
    }

    private void getAndSetFixedAssetData() {
        if (AddEditFixedAssetForm.dataForSend != null) {
            this.fixedAssetItem = AddEditFixedAssetForm.dataForSend;
            if (this.fixedAssetItem.getTaxCalculationType() != null) {
                this.taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(this.fixedAssetItem.getTaxCalculationType()));
                this.productsTable.onTaxCalculationTypeChange(this.taxCalcTypeListBox.getSelectedId(), true);
            }
            final NewInvoice invoice = new NewInvoice();
            final NewInvoiceItem item = new NewInvoiceItem();
            item.setFullItemName(this.fixedAssetItem.getName());
            item.setDescription(this.fixedAssetItem.getDescription());
            item.setQuantity(this.fixedAssetItem.getQuantity());
            item.setUnitPrice(this.fixedAssetItem.getCost());
            item.setAccountItem(this.fixedAssetItem.getAccount());
            item.setTaxItem(this.fixedAssetItem.getTaxItem());
            invoice.setItems(new NewInvoiceItem[]{item});
            this.productsTable.setValues(invoice);

            invoice.setOpportunityID(this.formParameters.getOpportunityID());

            this.saveButton.setVisible(false);
            this.saveAndApproveButton.setText(PurchaseInvoiceView.wfmStrings.saveAndApprove());

            this.disableFixedAssetRelatedFields();
        }
    }

    private void disableFixedAssetRelatedFields() {
        this.currencyWidget.setEnabled(false);
        this.productsTable.setEnabled(false, false, this.formParameters.getExternalFormID());
    }


    public void fillForm(final NewInvoice result) {
        this.crmAccountWidgets.setCurrencyWidget(this.currencyWidget);

        if (this.formParameters.isEditForm()) {
            this.formParameters.setRecurringInvoice(result.isRecurringInvoice());
        }
        if (result.isPurchaseClientEnabled() && this.widgetsMap.get(AccountingCustomFormConstants.INPUT_CUSTOMER) != null) {
            this.widgetsMap.get(AccountingCustomFormConstants.INPUT_CUSTOMER).setVisible(true);
        } else if (this.widgetsMap.get(AccountingCustomFormConstants.INPUT_CUSTOMER) != null) {

            this.widgetsMap.get(AccountingCustomFormConstants.INPUT_CUSTOMER).setVisible(false);
        }
        if (Constants.OPPORTUNITY.equals(this.formParameters.getCrmFormName())) {
            this.setValues(result);
        }
        if (result.isCancelDateEnabled() && this.widgetsMap.get(AccountingCustomFormConstants.INPUT_CANCELDATE) != null) {
            this.widgetsMap.get(AccountingCustomFormConstants.INPUT_CANCELDATE).setVisible(true);
        } else if (this.widgetsMap.get(AccountingCustomFormConstants.INPUT_CANCELDATE) != null) {
            this.widgetsMap.get(AccountingCustomFormConstants.INPUT_CANCELDATE).setVisible(false);
        }
        if (result.getAccountsReceivablePayable() != null) {
            this.receivablePayableLookUp.addAccountItem(result.getAccountsReceivablePayable());
        } else if (result.getTypeItem() != null && result.getTypeItem().getAccountsReceivablePayable() != null) {
            this.receivablePayableLookUp.addAccountItem(result.getTypeItem().getAccountsReceivablePayable());
        }

        if (this.objectID != null) {//From Edit Form
            this.setValues(result);

            if (result.getConvertedItemID() != null) {
                this.disableCalculatableFields();
            }
        } else if (this.formParameters.isExternalForm(AccountingConstants.COPY_FROM_EXISTING_DATA) ||
                this.formParameters.isExternalForm(AccountingConstants.COPY_PO_TO_PI)) {//From Copy To New Link...
            //this one won't change existing exchange rate
            this.currencyWidget.setAutoReload(false);
            this.setValues(result);
        } else if (formParameters.isExternalForm(AccountingConstants.COPY_FROM_SI_TO_PI)) {
            productsTable.setExternalFormType(formParameters.getExternalFormID());
            this.setValues(result);
        } else if (this.formParameters.isExternalForm(AccountingConstants.CONVERT_TO_INVOICE) || this.formParameters.isExternalForm(AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN)) {//Convert to invoice...
            //this one won't change existing exchange rate
            this.currencyWidget.setAutoReload(false);
            //display only received qty is not null items
            final ArrayList<NewInvoiceItem> items = new ArrayList<>();
            for (final NewInvoiceItem item : result.getItems()) {
                if (item.getNonConvertedQty().compareTo(AccountingConstants.ZERO) != 0) {
                    items.add(item);
                } else if (item.getNonConvertedAmount().compareTo(AccountingConstants.ZERO) != 0) {
                    items.add(item);
                }
            }
            result.setItems(items.toArray(new NewInvoiceItem[items.size()]));
            this.productsTable.setValues(result);
            this.setValues(result);
            this.disableCalculatableFields();
            /*if (!items.isEmpty()) {
                productsTable.setEnabled(false, false);
            }*/

            this.isFixedAssetRelated = result.isFixedAssetRelated();
        } else {//From Add Form And Client Supplier List
            this.applyTaxCalculationTypeChanges(result.getTaxCalculationType());
            this.productsTable.setDefaultAccount(result.getDefaultAccountItem());
            productsTable.setDefaultDiscount(result.getDefaultDiscountItem());
            productsTable.setDefaultTax(result.getDefaultTaxItem());



            if (this.formParameters.isExternalForm(AccountingConstants.COPY_FROM_CLIENT_SUPPLIER) && result.getTypeItem() != null) {
                this.crmAccountLookUp.addItem(result.getTypeItem());
                this.crmAccountWidgets.presenter.initContactAddress(result.getTypeItem(), true, Address.EntityType.CrmAccount);

                if (result.getRelatedProject() != null) {
                    this.relatedProject.setSelected(result.getRelatedProject());
                }
                if (result.getTypeItem().getSupplierCustomerBalance() >= 0) {
                    this.supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    this.supplierBalanceLink.setText(AccountingUtils.get().formatPrice(result.getTypeItem().getSupplierCustomerBalance()));
                } else {
                    this.supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    this.supplierBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * result.getTypeItem().getSupplierCustomerBalance()) + ")");
                }
            } else if (this.formParameters.getExternalFormID() != null && this.formParameters.getExternalFormID().equals(AccountingConstants.COPY_FROM_CLIENT_SUPPLIER) && this.formParameters.getRelatedProjectID() != null) {
                this.relatedProject.setSelected(result.getRelatedProject());
            }
            if (this.formParameters.isFromGettingStarted()) {
                this.invoiceDate.setDate(result.getInvoiceDate().getNonConvertedDate());
            } else {
                this.invoiceDate.setDate(this.currentDate);
            }
            if (result.getInvoiceTermsItem() != null) {
                this.termsAndDueDatePanel.applyCustomerTerms(result.getInvoiceTermsItem());
            } else if (AccountingConstants.TERMS_TYPE.equals(result.getDueDateType())) {
                this.termsAndDueDatePanel.setData(AccountingConstants.TERMS_TYPE, (result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null), result.getInvoiceTermsItem());
            }
            this.crmAccountWidgets.presenter.setCustomerMailAddressData(result.getMailAddressID(), result.getClientItem());
            if (AccountingConstants.COPY_FROM_FIXED_ASSET.equals(this.formParameters.getExternalFormID())) {
                this.getAndSetFixedAssetData();
            }

            if (this.formParameters.getConvertFormType() != null && result.getTypeItem() != null) {
                this.crmAccountLookUp.addItem(result.getTypeItem());
                this.productsTable.setExchangeRateValue(result.getExchageRate());
                this.productsTable.setCurrencyId(result.getCurrencyID());
                this.productsTable.setValues(result);
                if (result.getInvoiceDate() != null) {
                    this.invoiceDate.setDate(result.getInvoiceDate().getNonConvertedDate());
                }
                if (result.getInvoiceTermsItem() != null) {
                    this.termsAndDueDatePanel.setData(AccountingConstants.TERMS_TYPE, (result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null), result.getInvoiceTermsItem());
                } else {
                    this.termsAndDueDatePanel.setData(AccountingConstants.DUE_TYPE, result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null, null);
                }
                this.setValues(result);
                this.currencyWidget.setCurrency(result.getCurrencyID(), result.getExchageRate());
                if (result.getInvoiceNumber() != null && !"".equals(result.getInvoiceNumber())) {
                    this.invoiceNumber.setText(result.getInvoiceNumber());
                }
            }
        }
        this.productsTable.setCurrencyId(result.getCurrencyID());
        this.productsTable.getBaseTotalLabel().setHTML(PurchaseInvoiceView.accountingMessages.dynamicTotal(this.currencyWidget.getBaseCurrencyName()));
        this.currencyWidget.setCurrency(result.getCurrencyID(), result.getExchageRate());

        this.renderButtons(result);
    }

    private void initProductTable(final NewInvoice result) {
        this.productsTable.setColumnsMap(this.drawColumns(result));
        this.productsTable.initItemTable(false);
    }

    private void renderButtons(final NewInvoice result) {
        if (!this.formParameters.isFromGettingStarted()) {
            final boolean editButtonPermission = Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_AND_PI_DRAFT);
            if (this.formParameters.isEditForm()) {
                final String statusCode = result.getStatusCode();
                if (editButtonPermission) {
                    if (Constants.INVOICE_STATUS_PENDING.equals(statusCode) || Constants.DRAFT.equals(statusCode)) {
                        this.saveButton.setVisible(true);
                    }
                }
                if (result.isApprover()) {
                    if (approver != null && approver.getFirstApproverLookUp() != null
                            && approver.getFirstApproverLookUp().getSelectedItem() != null
                            && !approver.getFirstApproverLookUp().getSelectedItem().getId().equals(Utils.getUserID())) {
                        this.submitButton.setVisible(true);
                    } else {
                        this.saveAndApproveButton.setVisible(true);
                    }
                } else if (Constants.DRAFT.equals(statusCode)
                        || Constants.APPROVE.equals(statusCode)
                        || Constants.PAID.equals(statusCode)
                        || Constants.OVER_DUE.equals(statusCode)) {
                    this.saveAndApproveButton.setVisible(true);
                }
            } else {
                if (editButtonPermission) {
                    this.saveButton.setVisible(true);
                }

                if (result.isApprover()) {
                    if (this.approver.getFirstApproverLookUp().getSelectedItem() != null) {
                        final SelectItem item = this.approver.getFirstApproverLookUp().getSelectedItem();
                        if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                            saveAndApproveButton.setVisible(true);
                            submitButton.setVisible(false);
                        } else {
                            saveAndApproveButton.setVisible(false);
                            submitButton.setVisible(true);
                        }
                    }
                } else {
                    this.saveAndApproveButton.setVisible(true);
                }
            }
        } else {
            this.saveAndApproveButton.setVisible(true);
        }
    }

    private void disableCalculatableFields() {
        this.crmAccountLookUp.setEnabled(false);
        this.currencyWidget.setEnabled(false);
        this.taxCalcTypeListBox.setEnabled(false);
        this.productsTable.setEnabled(false, false, this.formParameters.getExternalFormID());
    }

    private Integer getWarehouseDefaultLocation(final Map<Integer, String> map) {
        final Integer[] keys = map.keySet().toArray(new Integer[]{});
        Integer min = null;
        if (map.size() > 1) {
            min = keys[0] != null ? keys[0] : keys[1];
            for (final Map.Entry<Integer, String> entry : map.entrySet()) {
                if (entry.getKey() != null) {
                    if (entry.getKey() < min) {
                        min = entry.getKey();
                    }
                }
            }
        }
        return min;
    }

    private LinkedHashMap<String, ColumnConfig> drawColumns(final NewInvoice result) {
        final LinkedHashMap<String, ColumnConfig> columnsMap = new LinkedHashMap<>();
        final boolean deparmentMandatory = AccountingUtils.get().isEnableAccountingDepartmentRelation() && !Utils.hasPermission(PermissionConstants.SKIP_DEPARTMENT_ITEM_VALIDATION);
        if (result.getCustomItemColumns() != null && result.getCustomItemColumns().length > 0) {

            ColumnConfig columnConfig;

            for (final ColumnConfigs column : result.getCustomItemColumns()) {

                final boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);

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
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.QTY, "", Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL);
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
                        if (Constants.GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                            isRequired = true;
                        }
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.TAX_LIST, Utils.getColumnWidth(column.getWidth(), 100), isRequired);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnConfig.setTitle(column.getTitle());
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
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
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
            columnsMap.put(ProductsTable.UNITPRICE, new ColumnConfig(CustomCell.class, ProductsTable.UNITPRICE, 75, true, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.DISCOUNT_AMT, new ColumnConfig(CustomCell.class, ProductsTable.DISCOUNT_AMT, 75, false, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.NET_AMT, new ColumnConfig(CustomCell.class, ProductsTable.NET_AMT, 100, false, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.TAX_LIST, new ColumnConfig(LookUpCell.class, ProductsTable.TAX_LIST, 100, Constants.GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()));
            columnsMap.put(ProductsTable.ACCOUNT, new ColumnConfig(LookUpCell.class, ProductsTable.ACCOUNT, 100, true));
            columnsMap.put(ProductsTable.FAI_CATEGORY, new ColumnConfig(LookUpCell.class, ProductsTable.FAI_CATEGORY, 100, false));

            if (Utils.isMultiWarehouseEnabled()) {
                columnsMap.put(ProductsTable.WAREHOUSE, new ColumnConfig(LookUpCell.class, ProductsTable.WAREHOUSE, 100, true));
            }
        }
        return columnsMap;
    }

    private PurchaseInvoiceFormPresenter generateFormPresenter() {
        return new PurchaseInvoiceFormPresenter(new PurchaseInvoiceViewInterface() {
            @Override
            public boolean isEditForm() {
                return PurchaseInvoiceView.this.formParameters.isEditForm();
            }

            @Override
            public Integer getDefaultLocationsId(final Map<Integer, String> map) {
                return PurchaseInvoiceView.this.getWarehouseDefaultLocation(map);
            }

            @Override
            public Params getFormParameters() {
                return PurchaseInvoiceView.this.formParameters;
            }

            @Override
            public Integer getObjectID() {
                return PurchaseInvoiceView.this.objectID;
            }

            @Override
            public void setObjectID(final Integer result) {
                PurchaseInvoiceView.this.objectID = result;
            }

            @Override
            public Date getConversionDate() {
                return PurchaseInvoiceView.this.conversionDate;
            }

            @Override
            public void setConversionDate(final Date date) {
                PurchaseInvoiceView.this.conversionDate = date;
            }

            @Override
            public NewInvoice getFormData(final String invoiceStatus, final boolean calculate) {
                return PurchaseInvoiceView.this.getInvoiceData(invoiceStatus);
            }

            @Override
            public LookUp getCrmAccountLookUp() {
                return PurchaseInvoiceView.this.crmAccountLookUp;
            }

            @Override
            public CrmAccountWidgets getCrmAccountWidgets() {
                return PurchaseInvoiceView.this.crmAccountWidgets;
            }

            @Override
            public ProjectLookUp getProjectLookUp() {
                return PurchaseInvoiceView.this.relatedProject;
            }

            @Override
            public CurrencyWidget getCurrencyWidget() {
                return PurchaseInvoiceView.this.currencyWidget;
            }

            @Override
            public DatePicker getDatePicker() {
                return PurchaseInvoiceView.this.invoiceDate;
            }

            @Override
            public DatePicker getDueDatePicker() {
                return null; //dueDate;
            }

            @Override
            public HorizontalPanel getTermsAndDueDateLabel() {
                return PurchaseInvoiceView.this.dueAndTearmsPanel;
            }

            @Override
            public TermsAndDuePanel getTermsAndDueDatePanel() {
                return PurchaseInvoiceView.this.termsAndDueDatePanel;
            }

            @Override
            public TextBox getNumberTxtBox() {
                return PurchaseInvoiceView.this.invoiceNumber;
            }

            @Override
            public DataListBox getTaxCalcListBox() {
                return PurchaseInvoiceView.this.taxCalcTypeListBox;
            }

            @Override
            public ReceiptTable getTotalTable() {
                return PurchaseInvoiceView.this.productsTable.getTotalsTable();
            }

            @Override
            public ProductsTable getProductTable() {
                return PurchaseInvoiceView.this.productsTable;
            }

            @Override
            public View getView() {
                return PurchaseInvoiceView.this;
            }

            @Override
            public void setEditValues(final NewInvoice result) {
                PurchaseInvoiceView.this.setValues(result);
            }

            @Override
            public void setFormData(final NewInvoice result) {
                PurchaseInvoiceView.this.fillForm(result);
            }

            @Override
            public void initCustomFields(final NewInvoice result) {
                PurchaseInvoiceView.this.initInvoiceCustomFields(result);
                if (PurchaseInvoiceView.this.formParameters.isRecurringInvoice() && PurchaseInvoiceView.this.recurringWidget != null && result.getRecurrenceJobItem() != null) {
                    PurchaseInvoiceView.this.recurringWidget.setData(result.getRecurrenceJobItem());
                }
            }

            @Override
            public void initSystemCustomFields(NewInvoice result) {
                systemCustomFields = result.getSystemCustomFields();
            }

            @Override
            public boolean isReccuringInvoice() {
                return PurchaseInvoiceView.this.formParameters.isRecurringInvoice();
            }

            @Override
            public RecurringWidget getRecurringWidget() {
                return PurchaseInvoiceView.this.recurringWidget;
            }

            @Override
            public void generateForm(final String layoutHTML) {
                PurchaseInvoiceView.this.htmlPanel = new WftHTMLPanel(layoutHTML, PurchaseInvoiceView.this.widgetsMap).getContainer();
                PurchaseInvoiceView.this.htmlPanel.setStyleName("add-form");
                PurchaseInvoiceView.this.htmlPanel.add(PurchaseInvoiceView.this.createFooter());

                PurchaseInvoiceView.this.add(PurchaseInvoiceView.this.htmlPanel);
            }

            @Override
            public Date getCurrentDate() {
                return PurchaseInvoiceView.this.currentDate;
            }

            @Override
            public InvoiceNumberData getNumberData() {
                return PurchaseInvoiceView.this.invoiceNumberData;
            }

            @Override
            public void setNumberData(final InvoiceNumberData numberData) {
                PurchaseInvoiceView.this.invoiceNumberData = numberData;
            }

            @Override
            public SplitButton getSplitButtonPdf() {
                return PurchaseInvoiceView.this.pdfVersionButton;
            }

            @Override
            public WfmButton2 getSaveButton() {
                return PurchaseInvoiceView.this.saveButton;
            }

            @Override
            public WfmButton2 getSubmitButton() {
                return PurchaseInvoiceView.this.submitButton;
            }

            @Override
            public WfmButton2 getApproveButton() {
                return PurchaseInvoiceView.this.saveAndApproveButton;
            }

            @Override
            public WfmButton2 getManagerApproveButton() {
                return null;
            }

            @Override
            public WfmButton2 getApproveAndSendButton() {
                return null;
            }

            @Override
            public boolean validateCustomFields() {
                final boolean validate = PurchaseInvoiceView.this.advancedOptions.validateCustomFieldRequiredFields();

                //if there are not validate fields in the custom fields
                //then we should open the advanced pop-up
                if (!validate) {
                    PurchaseInvoiceView.this.advancedOptions.getCustomFieldContainer().setActive(0);

                    PurchaseInvoiceView.this.showAdvancedOptions(View.wfmStrings.additionalFields(), PurchaseInvoiceView.this.advancedOptions);
                }
                return validate;
            }

            @Override
            public boolean validateProjectMandatory() {
                if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && !Utils.isProjectInLineItemEnable() && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY)) {
                    if (relatedProject.getSelectedItemID() == null) {
                        showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
                        relatedProject.addStyleName(ERROR_FORM_STYLE);
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean validateSystemCustomFields() {
                return PurchaseInvoiceView.this.validateSystemCustomFields();
            }

            @Override
            public HTMLPanel getHTMLPanel() {
                return PurchaseInvoiceView.this.htmlPanel;
            }

            @Override
            public void initProductsTableData(final NewInvoice result) {
                PurchaseInvoiceView.this.initProductTable(result);
            }

            @Override
            public void initWidgetMap() {
            }

            @Override
            public void initWidgetMap(final NewInvoice data) {
                PurchaseInvoiceView.this.hasApproval = data.isApprover();
                PurchaseInvoiceView.this.initWidgetsMap(data);
            }

            @Override
            public PdfTemplatePanel getPdfTemplateBox() {
                return PurchaseInvoiceView.this.pdfTemplatePanel;
            }

            @Override
            public void initPdfTemplates(final NewInvoice result) {
//                initInvoicePdfTemplates(result);
            }

            public PlaceOfSupplyWidget getPlaceOfSupplyWidget() {
                return PurchaseInvoiceView.this.placeOfSupplyWidget;
            }

            @Override
            public MaterialLink getSupplierBalanceLink() {
                return PurchaseInvoiceView.this.supplierBalanceLink;
            }

            @Override
            public AccountsReceivablePayableLookUp getAccountsReceivablePayableLookUp() {
                return PurchaseInvoiceView.this.receivablePayableLookUp;
            }

            @Override
            public FormGroup getPriceLevel() {
                return PurchaseInvoiceView.this.priceLevelField;
            }

            @Override
            public DataListBox getPriceLevelDropdown() {
                return PurchaseInvoiceView.this.priceLevel;
            }

            @Override
            public Property getProperty() {
                return PurchaseInvoiceView.this.property;
            }

            @Override
            public ChosenApproversWidget getApproverLookUp() {
                return PurchaseInvoiceView.this.approver;
            }
        });
    }


    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return PurchaseInvoiceView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return PurchaseInvoiceView.this.getFooterRightSideWidgets();
            }
        });
    }


    private List<Widget> getFooterRightSideWidgets() {
        final List<Widget> items = new ArrayList<>();

        if (this.widgetsMap.get(AccountingCustomFormConstants.SAVE_AS_DRAFT_BUTTON) != null) {
            items.add(this.wrapToDiv(this.widgetsMap.get(AccountingCustomFormConstants.SAVE_AS_DRAFT_BUTTON)));
        }
        if (this.widgetsMap.get(AccountingCustomFormConstants.PDF_VERSION) != null) {
            items.add(this.wrapToDiv(this.widgetsMap.get(AccountingCustomFormConstants.PDF_VERSION)));
        }
        if (this.hasApproval) {
            items.add(this.wrapToDiv(this.widgetsMap.get(AccountingCustomFormConstants.SUBMIT_TO_MANAGER_BUTTON)));
        }
        if (this.widgetsMap.get(AccountingCustomFormConstants.SAVE_AND_APPROVE_BUTTON) != null) {
            items.add(this.wrapToDiv(this.widgetsMap.get(AccountingCustomFormConstants.SAVE_AND_APPROVE_BUTTON)));
        }
        return items;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        final List<Widget> leftSideWidgets = new ArrayList<>();

        final FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, View.wfmStrings.historyAndNotes(), this.noteHistoryWidget);
        informer.addClickHandler(click -> noteHistoryWidget.setLoadData(callback -> {
            if (objectID == null) {
                return;
            }
            InvoiceService.App.get().loadInvoiceHistoryNote(objectID, Constants.PURCHASE_INVOICE, true, callback);
        }));
        informer.setInitialClasses("informer-item history-notes-container");
        if (Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_HISTORY_NOTES)) {
            leftSideWidgets.add(informer);
        }
        leftSideWidgets.add(this.uploadPanel);

        return leftSideWidgets;
    }

    private void initInvoiceCustomFields(final NewInvoice invoice) {
        if (!this.formParameters.isRecurringInvoice() && invoice.getCustomFieldItems() != null && invoice.getCustomFieldItems().size() > 0) {
            this.advancedOptions.createAndAppendInvoiceCustomFieldsView(ViewAddFiledsCodeName.PurchaseInvoiceAdd, invoice);
        }
    }

    public NewInvoice getInvoiceData(final String invoiceStatus) {
        this.productsTable.calculate(true);

        final NewInvoice invoice = new NewInvoice();
        invoice.setClientID(this.crmAccountLookUp.getSelectedItemID());
        invoice.setClientName(this.crmAccountLookUp.getSelectedItem() != null ? this.crmAccountLookUp.getSelectedItem().getName() : null);
        invoice.setBillAddressID(this.crmAccountWidgets.getBillAddressID());
        invoice.setMailAddressID(this.crmAccountWidgets.getMailAddressID());
        invoice.setPriceLevel(this.priceLevel.getSelectedItem());

        if (this.placeOfSupplyWidget != null) {
            invoice.setPlaceOfSupply(this.placeOfSupplyWidget.getSelectedPlaceOfSupply());

            if (this.placeOfSupplyWidget.getReverseChargeBox().isAttached())
                invoice.setReversechargeApplicable(this.placeOfSupplyWidget.getReverseChargeBox().getValue());
        }
        if (reverseChargeBox != null) {
            invoice.setReversechargeApplicable(reverseChargeBox.getValue());
        }
        if (this.dropShipToCustomerLookUp.getSelectedItemID() != null) {
            final TypeItem clientItem = new TypeItem();
            clientItem.setId(this.dropShipToCustomerLookUp.getSelectedItemID());
            clientItem.setMailAddressID(this.crmAccountWidgets.getMailAddressID());
            invoice.setClientItem(clientItem);
        }
        invoice.setCurrencyID(this.currencyWidget.getCurrencyID());
        invoice.setExchageRate(this.productsTable.getExchangeRateValue());
        invoice.setRelatedProject(this.relatedProject.getSelectedItem());
        invoice.setInvoiceDate(new DateNonConvertable(this.invoiceDate.getDate()));
        invoice.setPoNumber(this.poNumber.getText());
        invoice.setReference(this.referenceTxtBox.getText());
        invoice.setAccountsReceivablePayable(this.receivablePayableLookUp.getSelectedData());

        invoice.setRecurringInvoice(this.formParameters.isRecurringInvoice());
        if (this.formParameters.isRecurringInvoice()) {
            invoice.setRecurrenceJobItem(this.recurringWidget != null ? this.recurringWidget.getData() : null);
        } else {
            invoice.setInvoiceNumber(this.invoiceNumber.getText());
            invoice.setNumberData(this.invoiceNumberData);
        }

        if (this.termsAndDueDatePanel.isDueTypeSelected()) {
            invoice.setDueDate(new DateNonConvertable(this.termsAndDueDatePanel.getDueDate()));
        } else {
            invoice.setDueDate(new DateNonConvertable(this.termsAndDueDatePanel.getDueDate()));
            invoice.setInvoiceTermsItem(this.termsAndDueDatePanel.getInvoiceTerms());
        }
        invoice.setSubtotal(this.productsTable.getSubTotalHighScale());
        invoice.setTotalDiscount(this.productsTable.getTotalDiscountHighScale());
        invoice.setTotal(this.productsTable.getTotalInBaseCurrencyHighScale());
        invoice.setTotalInInvoiceCurrency(this.productsTable.getTotalInInvoiceCurrencyHighScale());
        invoice.setTotalTaxes(productsTable.getTotalTaxAmountByCurrency());
        invoice.setTotalTaxItems(this.productsTable.getTotalTaxItems());
        invoice.setType(Constants.PAYABLE);
        invoice.setStatusCode(invoiceStatus);
        invoice.setHistoryList(this.noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
        invoice.setPaymentInstructionID(this.productsTable.getPaymentTermsConditionsListBox().getSelectedId());
        invoice.setPaymentInstruction(this.productsTable.getPaymentInstruction().getText());
        invoice.setTaxCalculationType(this.taxCalcTypeListBox.getSelectedId());
        if (this.pdfTemplatePanel != null) {
            invoice.setPdfTemplateID(this.pdfTemplatePanel.getSelectedTemplateID());
        }
        if (this.cancelDate.getDate() != null) {
            invoice.setCancelDate(new DateNonConvertable(this.cancelDate.getDate()));
        }
        invoice.setItems(this.productsTable.getDataItems(invoiceStatus));

        //simplifying to filter for transactions
        if (this.relatedProject != null && !Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            for (final NewInvoiceItem item : invoice.getItems()) {
                item.setProject(this.relatedProject.getSelectedItem());
            }
        }
        invoice.setBookkeep(this.getBookKeep());

        invoice.setConvertedItemID(this.formParameters.isExternalForm(AccountingConstants.CONVERT_TO_INVOICE) ? this.formParameters.getExternalObjectID() : null);
        invoice.setTargetPurchaseOrderID(this.formParameters.isExternalForm(AccountingConstants.COPY_PO_TO_PI) ? this.formParameters.getExternalObjectID() : null);
        invoice.setTargetGrnId(this.formParameters.isExternalForm(AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN) ? this.formParameters.getExternalObjectID() : null);

        if (this.fixedAssetItem != null || this.isFixedAssetRelated) {
            invoice.setFixedAssetItem(this.fixedAssetItem);
            invoice.setFixedAssetRelated(true);
        }
        invoice.setCustomFieldItems(this.advancedOptions.getCustomFieldsData());
        invoice.setAttachments(this.uploadPanel.getAttachedFiles());

        if (this.productsTable.getOverallDiscount().isEnabled()) {
            invoice.setDiscountType(this.productsTable.getOverallDiscount().getType());
            invoice.setDiscountAmount(this.productsTable.getOverallDiscount().getValue());
        }
        invoice.setApprovers(this.approver.getChosenApprovers());
        invoice.setOpportunityID(formParameters.getOpportunityID());
        if (relationID != null && relationType != null) {
            ArrayList<RelationItem> relations = new ArrayList<>();
            relations.add(new RelationItem(null, relationID, relationType, relationName, null, RelationItem.TYPE_PURCHASE_ORDER, null));
            invoice.setRelations(relations);
        }
        return invoice;
    }

    public boolean getBookKeep() {
        return !this.formParameters.isFromGettingStarted();
    }

    public void setValues(final NewInvoice invoice) {
        if (invoice.getTypeItem() != null) {
            this.crmAccountLookUp.addItem(invoice.getTypeItem());
        }

        if (Constants.GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered() && placeOfSupplyWidget != null && invoice.getTypeItem() != null) {
            this.formPresenter.configurePlaceOfSupply(invoice.getTypeItem(), invoice.getPlaceOfSupply(), this.placeOfSupplyWidget);
            this.placeOfSupplyWidget.getReverseChargeBox().setValue(invoice.isReversechargeApplicable());
        }
        if (reverseChargeBox != null) {
            reverseChargeBox.setValue(invoice.isReversechargeApplicable());
            productsTable.clearSelectedTaxFromItems(invoice.isReversechargeApplicable());
        }

        if (invoice.getTypeItem() != null) {
            if (invoice.getTypeItem().getSupplierCustomerBalance() >= 0) {
                this.supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                this.supplierBalanceLink.setText(AccountingUtils.get().formatPrice(invoice.getTypeItem().getSupplierCustomerBalance()));
            } else {
                this.supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                this.supplierBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * invoice.getTypeItem().getSupplierCustomerBalance()) + ")");
            }
        }
        if (invoice.getTypeItem() != null) {
            this.crmAccountWidgets.presenter.initContactAddress(invoice.getTypeItem(),
                    !(this.formParameters.isEditForm() || this.formParameters.isExternalForm(AccountingConstants.CONVERT_TO_INVOICE)),
                    Address.EntityType.CrmAccount);
            this.crmAccountWidgets.presenter.setCustomerMailAddressData(invoice.getMailAddressID(), invoice.getClientItem());
        }

        if (invoice.getRelatedProject() != null) {
            this.relatedProject.setSelected(invoice.getRelatedProject());
        }

        if (this.formParameters.isEditForm()) {
            this.currencyWidget.setCurrency(invoice.getCurrencyID(), invoice.getExchageRate());
            if (invoice.getInvoiceNumber() != null && !"".equals(invoice.getInvoiceNumber())) {
                this.invoiceNumber.setText(invoice.getInvoiceNumber());
            }

        } else if (this.formParameters.isExternalForm(AccountingConstants.COPY_FROM_EXISTING_DATA) || this.formParameters.isExternalForm(AccountingConstants.COPY_PO_TO_PI)) {
            this.currencyWidget.setCurrency(invoice.getCurrencyID());
        } else {
            if (AccountingConstants.CONVERT_TO_INVOICE.equals(this.formParameters.getExternalFormID()) || AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN.equals(this.formParameters.getExternalFormID())) {
                this.currencyWidget.setAutoReload(false);
                this.currencyWidget.setCurrency(invoice.getCurrencyID(), invoice.getExchageRate());
            } else {
                this.currencyWidget.setCurrency(invoice.getCurrencyID());
            }
        }
        this.poNumber.setText(invoice.getPoNumber());
        this.referenceTxtBox.setText(invoice.getReference());
        if (this.objectID != null || this.formParameters.isExternalForm(AccountingConstants.COPY_FROM_EXISTING_DATA)
                || this.formParameters.isExternalForm(AccountingConstants.COPY_PO_TO_PI)
                || this.formParameters.isExternalForm(AccountingConstants.CONVERT_TO_INVOICE)
                || this.formParameters.isExternalForm(AccountingConstants.COPY_FROM_SI_TO_PI)) {
            if (this.formParameters.isExternalForm(AccountingConstants.COPY_FROM_EXISTING_DATA)) {
                this.invoiceDate.setDate(this.currentDate);
                if (invoice.getInvoiceTermsItem() != null) {
                    this.termsAndDueDatePanel.setData(AccountingConstants.TERMS_TYPE, (invoice.getDueDate() != null ? invoice.getDueDate().getNonConvertedDate() : null), invoice.getInvoiceTermsItem());
                } else {
                    this.termsAndDueDatePanel.setData(AccountingConstants.DUE_TYPE, (invoice.getDueDate() != null ? invoice.getDueDate().getNonConvertedDate() : null), null);
                }
            } else {
                if (invoice.getConvertPurchaseInvoiceDateType() != null && invoice.getConvertPurchaseInvoiceDateType().equals(AccountingConstants.RECEIVE_DATE_TYPE) && invoice.getReceiveDate() != null) {
                    this.invoiceDate.setDate(invoice.getReceiveDate().getNonConvertedDate());
                } else {
                    if (!AccountingConstants.CONVERT_TO_INVOICE.equals(this.formParameters.getExternalFormID())) {
                        this.invoiceDate.setDate(invoice.getInvoiceDate().getNonConvertedDate());
                    }
                }
                if (invoice.getInvoiceTermsItem() != null) {
                    this.termsAndDueDatePanel.setData(AccountingConstants.TERMS_TYPE, (invoice.getDueDate() != null ? invoice.getDueDate().getNonConvertedDate() : null), invoice.getInvoiceTermsItem());
                } else {
                    this.termsAndDueDatePanel.setData(AccountingConstants.DUE_TYPE, (invoice.getDueDate() != null && !AccountingConstants.CONVERT_TO_INVOICE.equals(this.formParameters.getExternalFormID()) ? invoice.getDueDate().getNonConvertedDate() : null), null);
                }
                //dueDate.setDate(invoice.getDueDate().getNonConvertedDate());
            }
        } else {
            this.invoiceDate.setDate(this.currentDate);
            if (invoice.getInvoiceTermsItem() != null) {
                this.termsAndDueDatePanel.setData(AccountingConstants.TERMS_TYPE, (invoice.getDueDate() != null ? invoice.getDueDate().getNonConvertedDate() : null), invoice.getInvoiceTermsItem());
            } else {
                this.termsAndDueDatePanel.setData(AccountingConstants.DUE_TYPE, (invoice.getDueDate() != null ? invoice.getDueDate().getNonConvertedDate() : null), null);
            }
        }
        if (invoice.getCancelDate() != null) {
            this.cancelDate.setDate(invoice.getCancelDate().getNonConvertedDate());
        }

        this.applyTaxCalculationTypeChanges(invoice.getTaxCalculationType());
        if (invoice.getOpportunityID() != null) {
            this.formParameters.setOpportunityID(invoice.getOpportunityID());
        }
        this.productsTable.setExchangeRateValue(invoice.getExchageRate());
        this.productsTable.setCurrencyId(invoice.getCurrencyID());
        this.productsTable.getPaymentInstruction().setText(invoice.getPaymentInstruction());
        this.productsTable.setValues(invoice);

        if (invoice.isFixedAssetRelated()) {
            this.disableFixedAssetRelatedFields();
        }
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {

            @Override
            public List<Widget> getOptionWidgets() {
                final List<Widget> result = new ArrayList<>();

                if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                    final FormGroup projectField = new FormGroup(Property.get(Constants.PROJECT, PurchaseInvoiceView.wfmStrings.project()), PurchaseInvoiceView.this.relatedProject, Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY));
                    projectField.ensureDebugId(InvoiceFormFields.PROJECT);
                    result.add(projectField);
                }

                if (!PurchaseInvoiceView.this.formParameters.isRecurringInvoice()) {
                    final FormGroup poNumberField = new FormGroup(PurchaseInvoiceView.wfmStrings.poNumber(), PurchaseInvoiceView.this.poNumber);
                    poNumberField.ensureDebugId(InvoiceFormFields.PO_NUMBER);
                    result.add(poNumberField);
                }

                result.add(new FormGroup(View.wfmStrings.accountsPayable(), PurchaseInvoiceView.this.receivablePayableLookUp));

                result.add(PurchaseInvoiceView.this.priceLevelField);

                return result;
            }
        });
    }

    private void applyTaxCalculationTypeChanges(final Integer taxCalculationType) {
        this.taxCalcTypeListBox.setSelected(taxCalculationType != null ? AccountingUtils.getTaxCalcType(taxCalculationType) : AccountingUtils.getTaxCalcType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE));
        this.productsTable.onTaxCalculationTypeChange(this.taxCalcTypeListBox.getSelectedId(), false);
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(final Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(PurchaseInvoiceView.this.onInitialize());
            }
        });
    }


    public String getPropertyCode() {
        if (this.isRecurringBill) {
            return Constants.RECURRING_BILL;
        } else {
            return Constants.PURCHASE_INVOICE;
        }
    }

    Command onPlaceOfSupplyChange() {
        return () -> {
            if (Arrays.asList(AccountingConstants.COPY_FROM_EXISTING_DATA, AccountingConstants.CONVERT_TO_INVOICE, AccountingConstants.CONVERT_TO_INVOICE_FROM_GRN).contains(this.formParameters.getExternalFormID())) {
                return;
            }
            if (this.placeOfSupplyWidget == null) {
                return;
            }
            final String treatment = this.placeOfSupplyWidget.getTaxTreatment() != null ? this.placeOfSupplyWidget.getTaxTreatment().getCode() : null;
            final boolean disableTaxField = Constants.NON_VAT_REGISTERED.equals(treatment) || Constants.NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(treatment);

            if (disableTaxField) {
                this.productsTable.clearSelectedTaxFromItems(true);
                return;
            }

            if (Constants.NON_GCC.equals(treatment) || Constants.GCC_VAT_REGISTERED.equals(treatment) || Constants.GCC_NON_VAT_REGISTERED.equals(treatment)) {

                if (Utils.isSaudiCompany()) {
                    if (this.placeOfSupplyWidget.getReverseChargeBox().isAttached() && this.placeOfSupplyWidget.getReverseChargeBox().isAttached()) {
                        this.productsTable.clearSelectedTaxFromItems(!this.placeOfSupplyWidget.getReverseChargeBox().getValue());
                    } else {
                        this.productsTable.clearSelectedTaxFromItems(!GCC_VAT_REGISTERED.equals(treatment));
                    }
                    return;
                }
            }
            this.productsTable.clearSelectedTaxFromItems(false);
        };
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
}
