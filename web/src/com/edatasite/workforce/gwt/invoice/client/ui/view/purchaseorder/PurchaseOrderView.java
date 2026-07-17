package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditFixedAssetForm;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.KsaPlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.PlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.UaePlaceOfSupplyWidget;
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
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceQuoteLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingEmployeeLookUp;
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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_ORDER_HISTORY_NOTES;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LINKS;

/**
 * User: Sherzod
 * Date: 2/3/12
 * Time: 5:05 PM
 */
public class PurchaseOrderView extends FooteredView implements Colapse, FittedContent, Constants, AccountingConstants, AccountingCustomFormConstants, HasLinksInterface {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final Date currentDate = new Date();
    public DataListBox taxCalcTypeListBox;
    private Integer objectID;
    private Date conversionDate;
    private CrmAccountLookUp crmAccountLookUp;
    private TextBox numberTxtBox;
    private DatePicker purchaseOrderDate;
    private TermsAndDuePanel termsAndShipDatePanel;
    private HorizontalPanel dueAndTearmsPanel;
    private ProjectLookUp projectLookUp;
    private WfmDropdown paymentTypeDropdown;
    private InvoiceQuoteLookUp saleQuoteLookUp;
    private TextBox referenceTxtBox;
    private TextBox paymentTerms;
    private TextBox shippingTerms;
    //Custom Fields
    private FormGroup managerBox;
    private AccountingEmployeeLookUp managerLookUp;
    private DatePicker cancelDate;

    private CrmAccountWidgets crmAccountWidgets;
    private CurrencyWidget currencyWidget;
    private ProductsTable productsTable;

    private CrmAccountLookUp dropShipToCustomerLookUp;

    private FixedAssetItem fixedAssetItem;

    private InvoiceNumberData orderNumberData;

    private FooterUploadPanel uploadPanel;

    private Params formParameters;
    private NoteHistoryWidget noteHistoryWidget;

    private DataListBox priceLevel;

    private FormGroup priceLevelField;

    private WfmButton2 saveButton, submitToManagerButton/*, pdfVersionButton*/;
    private SplitButton approveButton, pdfVersionButton;

    private HashMap<String, Widget> widgetsMap;
    private PurchaseOrderFormPresenter formPresenter;
    private HTMLPanel htmlPanel;
    private final String purchaseOrderView = "purchase_order_view_";

    private PdfTemplatePanel pdfTemplatePanel;

    private MaterialLink supplierBalanceLink;
    private InvoiceAdvancedOptions advancedOptions;
    private MaterialLink showMoreLink;
    private PlaceOfSupplyWidget placeOfSupplyWidget;
    private ChosenApproversWidget approver;
    private boolean hasApproval;
    private FooterInformer link;
    private Integer relationID;
    private String relationType;
    private String relationName;

    public PurchaseOrderView(String[] params) {
        super("purchaseorderadd");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.purchaseorder()));
        initFormParameters(params);
    }

    public PurchaseOrderView(Integer objectID) {
        super("edit");
        setDescription(property.getSingular(accountingStrings.editPurchaseOrder(), wfmStrings.purchaseorder()));
        this.objectID = objectID;
        initFormParameters(null);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void initFormParameters(String[] params) {
        formParameters = new Params();
        formParameters.setType(PAYABLE);
        formParameters.setFormType(PURCHASE_ORDER);
        formParameters.setObjectID(objectID);
        formParameters.setViewName(ViewName.PurchaseOrderSystem);

        if (params != null) {
            if (params.length > 2 && params[1].equals("copyFromExistingData")) {
                formParameters.setExternalFormID(COPY_FROM_EXISTING_DATA);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && (params[1].equals("fromSupplierList") || params[1].equals("supplier"))) {
                formParameters.setExternalFormID(COPY_FROM_CLIENT_SUPPLIER);
                if (params[1].equals("supplier")) {
                    formParameters.setCrmFormName(params[1]);
                }
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && (params[1].equals("copyFromSalesQuote") || params[1].equals("copyFromSalesOrder"))) {
                formParameters.setExternalFormID(COPY_FROM_SQ_SO_TO_PO);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && params[1].equals("copyFromSalesInvoice")) {
                formParameters.setExternalFormID(COPY_FROM_SI_TO_PO);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 1 && "copyFromFixedAsset".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_FIXED_ASSET);
            } else if (params.length > 2 && "fromProductList".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_PRODUCT_LIST);
                String[] idsArray = params[2].split(",");
                ArrayList<Integer> idList = new ArrayList<>();
                for (String id : idsArray) {
                    idList.add(Integer.parseInt(id));
                }
                formParameters.setExternalObjectIDList(idList);
            } else if (params.length > 1 && "projectLookUp".equals(params[1])) {
                if (params[2] != null) {
                    formParameters.setRelatedProjectID(Integer.valueOf(params[2]));
                }
            } else if (params.length > 2 && "converFromRFP".equals(params[1])) {
                formParameters.setExternalFormID(CONVERT_RFP_TO_PO);
                String[] idsArray = params[2].split(",");
                ArrayList<Integer> idList = new ArrayList<>();
                for (String id : idsArray) {
                    idList.add(Integer.parseInt(id));
                }
                formParameters.setExternalObjectIDList(idList);
                if (idList != null && !idList.isEmpty()) {
                    relationType = RelationItem.REQUEST_FOR_PURCHASE;
                    relationID = idList.get(0);
                }
            } else if (params.length > 2 && "relatedProject".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_CLIENT_SUPPLIER);
                if (params[2] != null) {
                    formParameters.setRelatedProjectID(Integer.valueOf(params[2]));
                }
            } else if (params.length >= 3 && "opportunity".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_OPPORTUNITY_TO_PO);
                formParameters.setOpportunityID(Integer.valueOf(params[2]));
                relationID = Integer.valueOf(params[2]);
                relationType = RelationItem.TYPE_OPPORTUNITY;
            } else if (params.length > 2 && "CONVERT".equals(params[0])) {
                formParameters.setConvertFormType(params[1]);
                if (params[2] != null && params[2].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                    formParameters.setConvertFormId(Integer.parseInt(params[2]));
                }
            }
        }
    }

    @Override
    protected Widget onInitialize() {

        boolean hasPermissonSupplierQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_QUICK_ADD);
        boolean hasPermissonSupplierAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_ADD);

        crmAccountLookUp = new SmartCrmAccountLookup(CrmAccountLookUp.SUPPLIER, true, () -> {
            if (hasPermissonSupplierQuick) {
                new CusSuppQuickAddView(CrmAccountLookUp.SUPPLIER, crmAccountLookUp.getLastValueBeforeClick());
            } else if (hasPermissonSupplierAdd) {
                SinksContainerFactory.entryPoint.onHistoryChanged("supplier|add/add");
            }
        });
        crmAccountLookUp.ensureDebugId(purchaseOrderView + "crmAccountLookUp");
        crmAccountLookUp.setEnsureSuggestBox(purchaseOrderView + "crmAccountLookUp");
        crmAccountLookUp.setAutocompleteOff();
        crmAccountLookUp.getSuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
            @Override
            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> selectionEvent) {
                InvoiceService.App.get().getClientOrSupplier(crmAccountLookUp.getSelectedItemID(), null, new AsyncCallback<TypeItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(TypeItem typeItem) {
                        productsTable.setCustomerSupplierItem(typeItem);
//                        productsTable.setReverseChargeApplicable(typeItem.isReverseChargeApplicable() & Utils.hasGenericAccess(GenericSettingsEnum.ACCOUNTING_IS_REVERSE_CHARGE));
                        productsTable.calculate();

                    }
                });

            }
        });
        crmAccountWidgets = new CrmAccountWidgets(crmAccountLookUp, PAYABLE, PURCHASE_ORDER, formParameters.isEditForm());

        dropShipToCustomerLookUp = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
        dropShipToCustomerLookUp.ensureDebugId(purchaseOrderView + "customerLookUp");
        dropShipToCustomerLookUp.setEnsureSuggestBox(purchaseOrderView + "customerLookUp");
        crmAccountWidgets.setDropShipToCustomerLookUp(dropShipToCustomerLookUp);

        projectLookUp = new ProjectLookUp(PAYABLE);
        projectLookUp.ensureDebugId(purchaseOrderView + "projectLookUp");
        projectLookUp.setEnsureSuggestBox(purchaseOrderView + "projectLookUp");

        priceLevel = new DataListBox();
        priceLevel.ensureDebugId(purchaseOrderView + "priceLevel");

        priceLevelField = new FormGroup(wfmStrings.priceLevel(), priceLevel);
        priceLevelField.ensureDebugId("inv_price_level");

        taxCalcTypeListBox = new DataListBox();
        taxCalcTypeListBox.ensureDebugId(purchaseOrderView + "taxCalcTypeListBox");
        taxCalcTypeListBox.setWithoutNullLabel(true);
        taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(TAX_CALCULATION_EXCLUSIVE));


        managerLookUp = new AccountingEmployeeLookUp();
        managerLookUp.setProjectLookUp(projectLookUp);
        managerLookUp.setFormType(PURCHASE_ORDER);
        managerLookUp.setAutocompleteOff();
        managerLookUp.ensureDebugId(purchaseOrderView + "managerLookUp");

        numberTxtBox = new TextBox();
        numberTxtBox.ensureDebugId(purchaseOrderView + "numberTxtBox");

        purchaseOrderDate = new DatePicker(currentDate, true);
        purchaseOrderDate.ensureDebugId(purchaseOrderView + "purchaseOrderDate");

        termsAndShipDatePanel = new TermsAndDuePanel(accountingStrings.validDate());
        termsAndShipDatePanel.ensureDebugId(purchaseOrderView + "termsAndShipDatePanel");

        referenceTxtBox = new TextBox(true);
        referenceTxtBox.ensureDebugId(purchaseOrderView + "referenceTxtBox");

        currencyWidget = new CurrencyWidget(!formParameters.isEditForm() && !COPY_FROM_FIXED_ASSET.equals(formParameters.getExternalFormID()));

        paymentTypeDropdown = new WfmDropdown();
        paymentTypeDropdown.ensureDebugId(purchaseOrderView + "paymentTypeDropdown");

        saleQuoteLookUp = new InvoiceQuoteLookUp(Constants.SALE_QUOTE);
        saleQuoteLookUp.ensureDebugId(purchaseOrderView + "saleQuoteLookUp");
        if (formParameters.getExternalFormID() != null && formParameters.getExternalFormID().equals(COPY_FROM_SQ_SO_TO_PO)) {
            QuoteService.App.get().getQuoteSummaryData(formParameters.getExternalObjectID(), new AsyncCallback<NewInvoice>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(NewInvoice newInvoice) {
                    saleQuoteLookUp.setSelected(new SelectItem(formParameters.getExternalObjectID(), newInvoice.getInvoiceNumber()));
                }
            });
        }

        if (relationID != null && relationType != null) {
            getRelationName(relationID, relationType);
        }

        paymentTerms = new TextBox();
        paymentTerms.ensureDebugId(purchaseOrderView + "paymentTerms");

        shippingTerms = new TextBox();
        shippingTerms.ensureDebugId(purchaseOrderView + "shippingTerms");

        cancelDate = new DatePicker();
        cancelDate.ensureDebugId(purchaseOrderView + "cancelDate");

        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            if (Utils.isSaudiCompany()) {
                placeOfSupplyWidget = new KsaPlaceOfSupplyWidget(onPlaceOfSupplyChange(), () -> productsTable.onReverseChargeChange());
            } else if (Utils.isUAECompany()) {
                placeOfSupplyWidget = new UaePlaceOfSupplyWidget(onPlaceOfSupplyChange(), () -> productsTable.onReverseChargeChange());
            }
        }

        productsTable = new ProductsTable(PAYABLE, PURCHASE_ORDER, formParameters);
        productsTable.setCurrencyWidget(currencyWidget);
        currencyWidget.setDatePicker(purchaseOrderDate);
        productsTable.setReverseChargeBox(placeOfSupplyWidget != null ? placeOfSupplyWidget.getReverseChargeBox() : null);
        productsTable.setPlaceOfSupplyBox(placeOfSupplyWidget != null ? placeOfSupplyWidget.getPlaceOfSupplyBox() : null);

        uploadPanel = new FooterUploadPanel(F_PUR_ORDER, objectID, true, wfmStrings.attachments());

        noteHistoryWidget = new InvoiceNoteHistoryWidget();
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);

        advancedOptions = createAdvancedOptions();
        advancedOptions.addToAddressBodyContainer(crmAccountWidgets.getBillAddress());
        advancedOptions.addToMailAddressBodyContainer(crmAccountWidgets.getMailAddress());
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));
        showMoreLink.addStyleName("btn-flat PurchaseOrderView"); //https://prnt.sc/r8iwmk

        supplierBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        supplierBalanceLink.setHref("javaScript:void(0)");
        supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        approver = new ChosenApproversWidget(RelationItem.TYPE_PURCHASE_ORDER, objectID);

        saveButton = new WfmButton2(wfmStrings.draft(), BTN_DEFAULT_OUTLINE);
        saveButton.ensureDebugId(purchaseOrderView + "saveButton");

        approveButton = new SplitButton(120, BTN_PRIMARY);
        approveButton.ensureDebugId("saveAndApprove-button");

        submitToManagerButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        submitToManagerButton.ensureDebugId(purchaseOrderView + "submitForApprovalButton");
        submitToManagerButton.setVisible(false);

//        pdfVersionButton = new WfmButton2(accountingStrings.pdfVersion(), BTN_DEFAULT_OUTLINE);
        pdfVersionButton = new SplitButton(97, WfmButton2.BTN_WHITE_OUTLINE);
        pdfVersionButton.ensureDebugId(purchaseOrderView + "pdfVersionButton");

        saveButton.setVisible(false);
        widgetsMap = new HashMap<>();

        this.initApproverLoadHandler();

        this.formPresenter = generateFormPresenter();
        this.formPresenter.bindUI();

        return null;
    }

    private void initApproverLoadHandler() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, PurchaseOrderView.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitToManagerButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitToManagerButton.setVisible(true);
                    }
                });
                if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitToManagerButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitToManagerButton.setVisible(true);
                    }
                }
            }
        });
    }

    private void initWidgetsMap(NewInvoice data) {

        productsTable.getPaymentInstruction().setStyleName(STYLE_TERMS_INSTRUCTION);

        //supplier field

        FormGroup supplierField = new FormGroup(crmAccountLookUp);

        Div supplierFieldLabel = supplierField.getGroupLabel();
        supplierFieldLabel.addStyleName("label-group");

        supplierFieldLabel.add(new Span(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier())));

        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(supplierBalanceLink);
        supplierFieldLabel.add(balance);

        widgetsMap.put(INPUT_CRM_ACCOUNT, supplierField);

        //drop ship field
        FormGroup dropShipField = new FormGroup(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), dropShipToCustomerLookUp);
        dropShipField.ensureDebugId("inv_drop_ship");
        widgetsMap.put(INPUT_CUSTOMER, dropShipField);

        FormGroup currencyField = new FormGroup(wfmStrings.currency(), currencyWidget);
        currencyField.ensureDebugId(InvoiceFormFields.CURRENCY);
        widgetsMap.put(INPUT_CURRENCY_LIST_BOX, currencyField);

        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), purchaseOrderDate));

        widgetsMap.put(INPUT_DUE_DATE, termsAndShipDatePanel.getTermsDueAsField());

        FormGroup numberField = new FormGroup(property.getShortForNumber(wfmStrings.poNumber()), numberTxtBox);
        numberField.ensureDebugId(InvoiceFormFields.NUMBER);
        widgetsMap.put(INPUT_NUMBER, numberField);
        if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_NUMBER_CHANGE_ACCESS_PO)) {
            numberTxtBox.setEnabled(false);
        }

//        FormGroup refField = new FormGroup(wfmStrings.reference(), referenceTxtBox);
        this.referenceTxtBox.ensureDebugId(InvoiceFormFields.REFERENCE);
        this.systemCustomFieldsMap.put(AccountingCustomFormConstants.INPUT_REFERENCE, referenceTxtBox);
        addSystemCustomFields(widgetsMap);

        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");
        widgetsMap.put(INPUT_SHOW_MORE, showMoreField);

        if ((Utils.isUAECompany() || Utils.isSaudiCompany()) && Utils.isVatRegistered()) {
            widgetsMap.put(INPUT_PLACE_OF_SUPPLY, placeOfSupplyWidget);
        }

        FormGroup taxCalcTypeField = new FormGroup(accountingStrings.amounts(), taxCalcTypeListBox);
        taxCalcTypeField.ensureDebugId(InvoiceFormFields.TAX_CALC);
        widgetsMap.put(INPUT_TAX_CALC_TYPE, taxCalcTypeField);

        if (data.isApprover()) {
            widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.currentApprover(), approver)));

            if (formParameters.isEditForm() && !data.isApproverSaved()) {
                approver.reloadApproverWidgets(RelationItem.TYPE_PURCHASE_ORDER, null);
            } else {
                approver.reloadApproverWidgets(RelationItem.TYPE_PURCHASE_ORDER, objectID);
            }
        }


        FormGroup cancelDateField = new FormGroup(accountingStrings.cancelDate(), cancelDate);
        cancelDateField.ensureDebugId("inv_cancel_date");
        widgetsMap.put(INPUT_CANCELDATE, cancelDateField);

        widgetsMap.put(INPUT_ITEM_TABLE, productsTable.getItemsTable());
        widgetsMap.put(INPUT_TOTALS_TABLE, productsTable.getTotalsTable());

        DataListBox paymentTermsConditionsListBox = productsTable.getPaymentTermsConditionsListBox();
        paymentTermsConditionsListBox.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        paymentTermsConditionsListBox.getElement().getStyle().setFloat(Style.Float.LEFT);
        paymentTermsConditionsListBox.ensureDebugId("termsAndCondition-link");
        widgetsMap.put(INPUT_INSTRUCTION_LIST, paymentTermsConditionsListBox);
        widgetsMap.put(INPUT_INSTRUCTION, new FormGroup(accountingStrings.termAndConditions(), productsTable.getPaymentInstruction()));

        widgetsMap.put(SAVE_AS_DRAFT_BUTTON, saveButton);
        widgetsMap.put(SAVE_AND_APPROVE_BUTTON, approveButton);
        if (hasApproval) {
            // in serice it will just update field not submits/approvers.
            if (data.isDeleteAndAddDsiabled() || data.isConvertedToInvoice()) {
                submitToManagerButton.setText(wfmStrings.update());
            }
            widgetsMap.put(SUBMIT_TO_MANAGER_BUTTON, submitToManagerButton);
        }
        widgetsMap.put(PDF_VERSION, pdfVersionButton);

        // if is converted invoice or grn, add info message
        if (data.isDeleteAndAddDsiabled() || data.isConvertedToInvoice()) {
            HTML message = new HTML(accountingStrings.purchaseOrderCanNotBeChangedBecouseConverted());
            message.getElement().setAttribute("style", "background-color: rgb(255, 243, 205); color: rgb(133, 100, 4); padding: 10px; border: 1px solid rgb(255, 238, 186); border-radius: 5px; display: inline-block;");
            message.setVisible(false);
            widgetsMap.put(INPUT_MESSAGE_TO_USER, message);
        }
    }

    public void fillForm(NewInvoice result) {
        crmAccountWidgets.setCurrencyWidget(currencyWidget);
        if (result.isPurchaseClientEnabled() && widgetsMap.get(INPUT_CUSTOMER) != null) {
            widgetsMap.get(INPUT_CUSTOMER).setVisible(true);
        } else if (widgetsMap.get(INPUT_CUSTOMER) != null) {
            widgetsMap.get(INPUT_CUSTOMER).setVisible(false);
        }

        if (result.isCancelDateEnabled() && widgetsMap.get(INPUT_CANCELDATE) != null) {
            widgetsMap.get(INPUT_CANCELDATE).setVisible(true);
        } else if (widgetsMap.get(INPUT_CANCELDATE) != null) {
            widgetsMap.get(INPUT_CANCELDATE).setVisible(false);
        }

        //Filling Data
        productsTable.setCurrencyId(result.getCurrencyID());
        applyTaxCalculationTypeChanges(result.getTaxCalculationType());
        productsTable.getBaseTotalLabel().setHTML(accountingMessages.dynamicTotal(currencyWidget.getBaseCurrencyName()));

        if (!formParameters.isExternalForm(COPY_FROM_SI_TO_PO)) {
            currencyWidget.setCurrency(result.getCurrencyID(), result.getExchageRate());
        }
        orderNumberData = result.getNumberData();

        if (objectID != null) {//From Edit Form
            setValues(result);
            numberTxtBox.setText(result.getInvoiceNumber());
            if (result.getLastGrnDate() != null) {
                purchaseOrderDate.addChangeHandler(event -> {
                    if (purchaseOrderDate.getDate().after(result.getLastGrnDate().getNonConvertedDate())) {
                        purchaseOrderDate.setDate(result.getInvoiceDate().getNonConvertedDate());
                        Info.warn(wfmStrings.dateCannotBeAfterGrnDate());
                    }
                });
            }
            if (result.getInvoicedItems() != null && result.getInvoicedItems().length > 0) {
                currencyWidget.setEnabled(false);
            }
        } else if (formParameters.isExternalForm(COPY_FROM_EXISTING_DATA) || COPY_FROM_PRODUCT_LIST.equals(formParameters.getExternalFormID())
                || CONVERT_RFP_TO_PO.equals(formParameters.getExternalFormID()) || COPY_FROM_OPPORTUNITY_TO_PO.equals(formParameters.getExternalFormID())) {//From Copy To New Link...
            setValues(result);
            numberTxtBox.setText(orderNumberData.getInvoiceNumber());
            referenceTxtBox.setText(relationName);
        } else {//From Add Form
            productsTable.setDefaultAccount(result.getDefaultAccountItem());
            productsTable.setDefaultTax(result.getDefaultTaxItem());
            productsTable.setDefaultDiscount(result.getDefaultDiscountItem());
            if (formParameters.isExternalForm(COPY_FROM_CLIENT_SUPPLIER) && result.getTypeItem() != null) {
                crmAccountLookUp.addItem(result.getTypeItem());
                crmAccountWidgets.presenter.initContactAddress(result.getTypeItem(), true, Address.EntityType.CrmAccount);

                if (result.getRelatedProject() != null) {
                    projectLookUp.setSelected(result.getRelatedProject());
                }
                if (result.getTypeItem().getSupplierCustomerBalance() >= 0) {
                    supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    supplierBalanceLink.setText(AccountingUtils.get().formatPrice(result.getTypeItem().getSupplierCustomerBalance()));
                } else {
                    supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    supplierBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * result.getTypeItem().getSupplierCustomerBalance()) + ")");
                }
            } else {
                if (formParameters.getExternalFormID() != null && formParameters.getExternalFormID().equals(COPY_FROM_CLIENT_SUPPLIER) && formParameters.getRelatedProjectID() != null) {
                    projectLookUp.setSelected(result.getRelatedProject());
                }
                if (formParameters.isExternalForm(COPY_FROM_SQ_SO_TO_PO) || formParameters.isExternalForm(COPY_FROM_SI_TO_PO)) {
                    productsTable.setExternalFormType(formParameters.getExternalFormID());
                    productsTable.setValues(result);
                    productsTable.calculate();
                    referenceTxtBox.setText(result.getReference());
                }
            }
            numberTxtBox.setText(orderNumberData.getInvoiceNumber());
            if (formParameters.getExternalFormID() != null) {
                if (result.getInvoiceTermsItem() != null) {
                    termsAndShipDatePanel.setData(TERMS_TYPE, (result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null), result.getInvoiceTermsItem());
                } else {
                    termsAndShipDatePanel.setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(currentDate, (result.getDueDays() != null ? result.getDueDays() : 0)), null);
                }
            } else if (result.getInvoiceTermsItem() != null) {
                termsAndShipDatePanel.applyCustomerTerms(result.getInvoiceTermsItem());
            } else {
                termsAndShipDatePanel.setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(currentDate, (result.getDueDays() != null ? result.getDueDays() : 0)), null);
            }
            crmAccountWidgets.presenter.setCustomerMailAddressData(result.getMailAddressID(), result.getClientItem());

            if (COPY_FROM_FIXED_ASSET.equals(formParameters.getExternalFormID())) {
                getAndSetFixedAssetData();
            }

            if (formParameters.getConvertFormType() != null) {
                setValues(result);
                productsTable.setExchangeRateValue(result.getExchageRate());
                productsTable.setValues(result);
                if (result.getInvoiceDate() != null) {
                    purchaseOrderDate.setDate(result.getInvoiceDate().getNonConvertedDate());
                }
                if (result.getInvoiceTermsItem() != null) {
                    termsAndShipDatePanel.setData(TERMS_TYPE, (result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null), result.getInvoiceTermsItem());
                } else {
                    termsAndShipDatePanel.setData(DUE_TYPE, result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null, null);
                }
            }
        }
    }

    private void initProductTable(NewInvoice result) {
        productsTable.setColumnsMap(drawColumns(result));
        productsTable.initItemTable(false);
    }

    private void getAndSetFixedAssetData() {
        if (AddEditFixedAssetForm.dataForSend != null) {
            fixedAssetItem = AddEditFixedAssetForm.dataForSend;
            if (fixedAssetItem.getTaxCalculationType() != null) {
                taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(fixedAssetItem.getTaxCalculationType()));
            }
            NewInvoice invoice = new NewInvoice();
            NewInvoiceItem item = new NewInvoiceItem();
            item.setFullItemName(fixedAssetItem.getName());
            item.setDescription(fixedAssetItem.getDescription());
            item.setQuantity(fixedAssetItem.getQuantity());
            item.setUnitPrice(fixedAssetItem.getCost());
            item.setAccountItem(fixedAssetItem.getAccount());
            item.setTaxItem(fixedAssetItem.getTaxItem());
            invoice.setItems(new NewInvoiceItem[]{item});
            productsTable.setValues(invoice);

            saveButton.setVisible(false);

            disableFixedAssetRelatedFields();
        }
    }

    private void disableFixedAssetRelatedFields() {
        currencyWidget.setEnabled(false);
        productsTable.setEnabled(false, false, formParameters.getExternalFormID());
    }

    private Integer getWarehouseDefaultLocation(Map<Integer, String> map) {
        Integer[] keys = map.keySet().toArray(new Integer[]{});
        Integer min = null;
        if (map.size() > 1) {
            min = keys[0] != null ? keys[0] : keys[1];
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                if (entry.getKey() != null) {
                    if (entry.getKey() < min) {
                        min = entry.getKey();
                    }
                }
            }
        }
        return min;
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
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.QTY, "", Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL, true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.QTY, columnConfig);
                        break;
                    case ProductsTable.MEASUREMENT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.MEASUREMENT, "", Utils.getColumnWidth(column.getWidth(), 60), column.isRequired(), true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.MEASUREMENT, columnConfig);
                        break;
                    case ProductsTable.UNITPRICE:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.UNITPRICE, "", Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL, true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.UNITPRICE, columnConfig);
                        break;
                    case ProductsTable.COMISSION:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.COMISSION, "", Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL, true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.COMISSION, columnConfig);
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
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.DISCOUNT_AMT, "", Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL, true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setForceWidthInPercent(!isPixel);
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
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.NET_AMT, "", Utils.getColumnWidth(column.getWidth(), 80), column.isRequired(), Constants.RIGHT_ALIGN_CELL, true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.NET_AMT, columnConfig);
                        break;
                    case ProductsTable.TAX_LIST:
                        columnConfig = new ColumnConfig(LookUpCell.class, ProductsTable.TAX_LIST, Utils.getColumnWidth(column.getWidth(), 100), GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered() || column.isRequired());
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
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.DOUBLE_TAX_LIST, columnConfig);
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
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setForceWidthInPercent(!isPixel);
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
                    default:
                        if (column.getCode() != null && column.getCode().contains("date_value")) {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 165), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setDisabled(column.isDisabled());
                            columnsMap.put(column.getCode(), columnConfig);
                        } else {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
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

            //Fix Table Width for custom invoice item table
//            if (ProductsTable.MIN_WIDTH > countWidth) {
//                if (columnsMap.containsKey(ProductsTable.DESCRIPTION)) {
//                    columnsMap.get(ProductsTable.DESCRIPTION).setWidth(columnsMap.get(ProductsTable.DESCRIPTION).getWidth() + (ProductsTable.MIN_WIDTH - countWidth));
//                } else {
//                    columnsMap.get(ProductsTable.PRODUCT).setWidth(columnsMap.get(ProductsTable.PRODUCT).getWidth() + (ProductsTable.MIN_WIDTH - countWidth));
//                }
//            }
        } else {
            columnsMap.put(ProductsTable.PRODUCT, new ColumnConfig(LookUpCell.class, ProductsTable.PRODUCT, 200, true));
            columnsMap.put(ProductsTable.DESCRIPTION, new ColumnConfig(CustomCell.class, ProductsTable.DESCRIPTION, 250, false));
            columnsMap.put(ProductsTable.QTY, new ColumnConfig(CustomCell.class, ProductsTable.QTY, 75, true, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.UNITPRICE, new ColumnConfig(CustomCell.class, ProductsTable.UNITPRICE, 75, true, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.DISCOUNT_AMT, new ColumnConfig(CustomCell.class, ProductsTable.DISCOUNT_AMT, 75, false, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.ACCOUNT, new ColumnConfig(LookUpCell.class, ProductsTable.ACCOUNT, 100, true));
            columnsMap.put(ProductsTable.NET_AMT, new ColumnConfig(CustomCell.class, ProductsTable.NET_AMT, 100, false, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.TAX_LIST, new ColumnConfig(LookUpCell.class, ProductsTable.TAX_LIST, 100, GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()));
        }
        return columnsMap;
    }

    private PurchaseOrderFormPresenter generateFormPresenter() {
        return new PurchaseOrderFormPresenter(new PurchaseOrderViewInterface() {

            @Override
            public WfmDropdown getPaymentTypeDropdown() {
                return paymentTypeDropdown;
            }

            @Override
            public Integer getDefaultLocationsId(Map<Integer, String> map) {
                return getWarehouseDefaultLocation(map);
            }

            @Override
            public TextBox getShippingTerms() {
                return shippingTerms;
            }

            @Override
            public TextBox getPaymentTerms() {
                return paymentTerms;
            }

            @Override
            public TextArea2 getTermsConditions() {
                return productsTable.getPaymentInstruction();
            }

            @Override
            public AccountingEmployeeLookUp getManagerLookUp() {
                return managerLookUp;
            }

            @Override
            public Widget getManagerBox() {
                return managerBox;
            }

            @Override
            public WfmButton2 getSubmitToManagerButton() {
                return submitToManagerButton;
            }

            @Override
            public boolean isEditForm() {
                return formParameters.isEditForm();
            }

            @Override
            public HTML getMessageToUser() {
                return (HTML) widgetsMap.get(INPUT_MESSAGE_TO_USER);
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
            public MaterialLink getSupplierBalanceLink() {
                return supplierBalanceLink;
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
                return projectLookUp;
            }

            @Override
            public CurrencyWidget getCurrencyWidget() {
                return currencyWidget;
            }

            @Override
            public DatePicker getDatePicker() {
                return purchaseOrderDate;
            }

            @Override
            public DatePicker getDueDatePicker() {
                //return shipDate;
                return null;
            }

            /*@Override
            public ShippingMethodWidget getShippingMethod() {
                return shippingMethod;
            }*/

            @Override
            public HorizontalPanel getTermsAndDueDateLabel() {
                return dueAndTearmsPanel;
            }

            @Override
            public TermsAndDuePanel getTermsAndDueDatePanel() {
                return termsAndShipDatePanel;
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
                return PurchaseOrderView.this;
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
                initOrderCustomFields(result);
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
            public NewInvoice getFormData(String status, boolean calculate) {
                return getOrderData(status);
            }

            @Override
            public Date getCurrentDate() {
                return currentDate;
            }

            @Override
            public InvoiceNumberData getNumberData() {
                return orderNumberData;
            }

            @Override
            public void setNumberData(InvoiceNumberData numberData) {
                orderNumberData = numberData;
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
            public boolean validateCustomFields() {
                boolean validate = advancedOptions.validateCustomFieldRequiredFields();

                //if there are not validate fields in the custom fields
                //then we should open the advanced pop-up
                if (!validate) {
                    advancedOptions.getCustomFieldContainer().setActive(0);
                    showAdvancedOptions("Purchase Order Advanced Fields", advancedOptions);
                }
                return validate;
            }

            @Override
            public boolean validateProjectMandatory() {
                if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && !Utils.isProjectInLineItemEnable() && (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY) || AccountingUtils.isMandatoryProjectForPurchaseOrders())) {
                    if (projectLookUp.getSelectedProjectCode() == null) {
                        showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
                        projectLookUp.addStyleName(ERROR_FORM_STYLE);
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean validateSystemCustomFields() {
                return PurchaseOrderView.this.validateSystemCustomFields();
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
            public void initWidgetMap(NewInvoice result) {
                hasApproval = result.isApprover();
                initWidgetsMap(result);
            }

            @Override
            public PdfTemplatePanel getPdfTemplateBox() {
                return pdfTemplatePanel;
            }

            @Override
            public void initPdfTemplates(NewInvoice result) {
//                initOrderPdfTemplates(result);
            }

            @Override
            public PlaceOfSupplyWidget getPlaceOfSupplyWidget() {
                return placeOfSupplyWidget;
            }

            /*@Override
            public WfmDropdown getPlaceOfSupplyBox() {
                return null;
            }

            @Override
            public FormGroup getPlaceOfSupplyField() {
                return null;
            }*/

            @Override
            public FormGroup getPriceLevel() {
                return priceLevelField;
            }

            @Override
            public DataListBox getPriceLevelDropdown() {
                return priceLevel;
            }

            /*@Override
            public KpiCheckBox getReverseChargeBox() {
                return null;
            }*/

            @Override
            public FooterInformer getLinkWidget() {
                return link;
            }

            @Override
            public HasLinks getLinkingUtils() {
                return getLinkingUtil();
            }

            @Override
            public FormGroup getReverseChargeField() {
                return (FormGroup) widgetsMap.get(INPUT_REVERSE_CHARGE);
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

    private void initOrderCustomFields(NewInvoice invoice) {
        if (invoice.getCustomFieldItems() != null && invoice.getCustomFieldItems().size() > 0) {
            advancedOptions.createAndAppendInvoiceCustomFieldsView(ViewAddFiledsCodeName.PurchaseOrderAdd, invoice);
        }
    }

    public NewInvoice getOrderData(String invoiceStatus) {
        productsTable.calculate(true);

        NewInvoice order = new NewInvoice();
        order.setClientID(crmAccountLookUp.getSelectedItemID());
        order.setBillAddressID(crmAccountWidgets.getBillAddressID());
        order.setMailAddressID(crmAccountWidgets.getMailAddressID());
        order.setCurrencyID(currencyWidget.getCurrencyID());
        order.setPriceLevel(priceLevel.getSelectedItem());
        order.setShippingMethodID(productsTable.getShippingMethod() != null ? productsTable.getShippingMethod().getId() : null);
        order.setOpportunityID(formParameters.getOpportunityID());

        if (placeOfSupplyWidget != null) {
            order.setPlaceOfSupply(placeOfSupplyWidget.getSelectedPlaceOfSupply());

            if (placeOfSupplyWidget.getReverseChargeBox().isAttached())
                order.setReversechargeApplicable(placeOfSupplyWidget.getReverseChargeBox().getValue());
        }
        if (dropShipToCustomerLookUp.getSelectedItem() != null) {
            TypeItem clientItem = new TypeItem();
            clientItem.setId(dropShipToCustomerLookUp.getSelectedItemID());
            clientItem.setMailAddressID(crmAccountWidgets.getMailAddressID());
            order.setClientItem(clientItem);
        }
        order.setExchageRate(productsTable.getExchangeRateValue());
        order.setRelatedProject(projectLookUp.getSelectedItem());

        Integer fourDigitNumber = orderNumberData.parseFourDigitNumber(numberTxtBox.getText());
        if (fourDigitNumber != null) {
            order.setFourDigitNumber(fourDigitNumber.toString());
        } else {
            order.setFourDigitNumber(orderNumberData.getFourDigitNumber());
        }

        order.setInvoiceNumber(numberTxtBox.getText());
        order.setInvoiceDate(new DateNonConvertable(purchaseOrderDate.getDate()));

        if (termsAndShipDatePanel.isDueTypeSelected()) {
            order.setDueDate(new DateNonConvertable(termsAndShipDatePanel.getDueDate()));
        } else {
            order.setDueDate(new DateNonConvertable(termsAndShipDatePanel.getDueDate()));
            order.setInvoiceTermsItem(termsAndShipDatePanel.getInvoiceTerms());
        }

        order.setSubtotal(productsTable.getSubTotal());
        order.setTotal(productsTable.getTotalInBaseCurrency());
        order.setTotalInInvoiceCurrency(productsTable.getTotalInInvoiceCurrency());
        order.setComissionAmount(productsTable.getComissionAmount());
        order.setTotalDiscount(productsTable.getTotalDiscount());
        order.setShippingPrice(productsTable.getShippingTotal());
        order.setTotalTaxes(productsTable.getTotalTaxAmountByCurrency());
        order.setTotalTaxes(order.getTotalTaxes().add(productsTable.getShippingTaxAmount()));
        order.setTotalTaxItems(productsTable.getTotalTaxItems());
        order.setType(PAYABLE);
        order.setStatusCode(invoiceStatus);
        order.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
        order.setPaymentInstructionID(productsTable.getPaymentTermsConditionsListBox().getSelectedId());
        order.setPaymentInstruction(productsTable.getPaymentInstruction().getText());
        order.setTaxCalculationType(taxCalcTypeListBox.getSelectedId());
        if (pdfTemplatePanel != null) {
            order.setPdfTemplateID(pdfTemplatePanel.getSelectedTemplateID());
        }
        if (cancelDate.getDate() != null) {
            order.setCancelDate(new DateNonConvertable(cancelDate.getDate()));
        }

        order.setItems(productsTable.getDataItems(invoiceStatus));

        if (invoiceStatus.equals(RECEIVED) || invoiceStatus.equals(PARTIAL_RECEIVED) || invoiceStatus.equals(INVOICED)) {
            if (fullReceived(order)) {
                order.setStatusCode(invoiceStatus);
            } else {
                order.setStatusCode(PARTIAL_RECEIVED);
            }
        }

        //simplifying to filter for transactions
        if (projectLookUp != null && !Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            for (NewInvoiceItem item : order.getItems()) {
                item.setProject(projectLookUp.getSelectedItem());
            }
        }
        if (paymentTypeDropdown.isValid()) {
            order.setPaymentMethodID(paymentTypeDropdown.getSelectedId());
        }
        order.setReference(referenceTxtBox.getText());
        order.setPaymentTerms(paymentTerms.getText());
        order.setShippingTerms(shippingTerms.getText());
        if (saleQuoteLookUp != null && saleQuoteLookUp.getSelectedItemID() != null) {
            order.setQuoteId(saleQuoteLookUp.getSelectedItemID());
            order.setQuoteNumber(saleQuoteLookUp.getSelectedItem().getName());
        }


        if (managerLookUp.getSelectedItemID() != null) {
            order.setPurchaseOrderManager(new SelectItem(managerLookUp.getSelectedItemID()));
        }

        if (fixedAssetItem != null) {
            order.setFixedAssetItem(fixedAssetItem);
            order.setFixedAssetRelated(true);
        }

        order.setCustomFieldItems(advancedOptions.getCustomFieldsData());

        order.setAttachments(uploadPanel.getAttachedFiles());

        if (CONVERT_RFP_TO_PO.equals(formParameters.getExternalFormID())) {
            order.setRelatedRFPIDs(formParameters.getExternalObjectIDList());
        }
        if (productsTable.getOverallDiscount().isEnabled()) {
            order.setDiscountType(productsTable.getOverallDiscount().getType());
            order.setDiscountAmount(productsTable.getOverallDiscount().getValue());
        }
        order.setApprovers(approver.getChosenApprovers());

        if (relationID != null && relationType != null) {
            ArrayList<RelationItem> relations = new ArrayList<>();
            relations.add(new RelationItem(null, relationID, relationType, relationName, null, RelationItem.TYPE_PURCHASE_ORDER, null));
            order.setRelations(relations);
        }
        return order;
    }

    private boolean fullReceived(NewInvoice invoiceData) {
        for (NewInvoiceItem item : invoiceData.getItems()) {
            if (ReceiveTypeEnum.RECEIVE_BY_VALUE == item.getReceiveType()) {
                BigDecimal netAmount = item.getNet().multiply(invoiceData.getExchageRate() != null ? invoiceData.getExchageRate() : BigDecimal.ONE);
                BigDecimal res = netAmount.subtract(item.getReceivedAmount() != null ? item.getReceivedAmount() : ZERO).setScale(AccountingUtils.getPriceScale(), RoundingMode.HALF_UP);
                if (res.compareTo(ZERO) > 0) {
                    return false;
                }
            } else {
                BigDecimal res = item.getQuantity().subtract(item.getReceivedQty() != null ? item.getReceivedQty() : ZERO).setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP);
                if (res.compareTo(ZERO) > 0) {
                    return false;
                }
            }

        }
        return true;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return PurchaseOrderView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return PurchaseOrderView.this.getFooterRightSideWidgets();
            }
        });
    }


    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> items = new ArrayList<>();

        if (widgetsMap.get(SAVE_AS_DRAFT_BUTTON) != null) {
            items.add(wrapToDiv(widgetsMap.get(SAVE_AS_DRAFT_BUTTON)));
        }
        if (widgetsMap.get(PDF_VERSION) != null) {
            items.add(wrapToDiv(widgetsMap.get(PDF_VERSION)));
        }
        if (widgetsMap.get(SAVE_AND_APPROVE_BUTTON) != null) {
            items.add(widgetsMap.get(SAVE_AND_APPROVE_BUTTON));
        }
        if (widgetsMap.get(SUBMIT_TO_MANAGER_BUTTON) != null) {
            items.add(wrapToDiv(widgetsMap.get(SUBMIT_TO_MANAGER_BUTTON)));
        }
        return items;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.addClickHandler(click -> noteHistoryWidget.setLoadData(callback -> {
            if (objectID == null) {
                return;
            }
            InvoiceService.App.get().loadInvoiceHistoryNote(objectID, PURCHASE_ORDER, false, callback);
        }));
        informer.setInitialClasses("informer-item history-notes-container");
        if (Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_HISTORY_NOTES)) {
            leftSideWidgets.add(informer);
        }
        leftSideWidgets.add(uploadPanel);

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        if (objectID != null && Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_LINKS)) {
            leftSideWidgets.add(link);
        }

        return leftSideWidgets;
    }

    public void setValues(final NewInvoice order) {
        if (order.getTypeItem() != null) {
            crmAccountLookUp.addItem(order.getTypeItem());
            crmAccountWidgets.presenter.initContactAddress(order.getTypeItem(), !formParameters.isEditForm(), Address.EntityType.CrmAccount);

            if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                formPresenter.configurePlaceOfSupply(order.getTypeItem(), order.getPlaceOfSupply(), placeOfSupplyWidget);
            }
            if (order.getTypeItem().getSupplierCustomerBalance() >= 0) {
                supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                supplierBalanceLink.setText(AccountingUtils.get().formatPrice(order.getTypeItem().getSupplierCustomerBalance()));
            } else {
                supplierBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                supplierBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * order.getTypeItem().getSupplierCustomerBalance()) + ")");
            }
        }
        crmAccountWidgets.presenter.setCustomerMailAddressData(order.getMailAddressID(), order.getClientItem());

        if (formParameters.isEditForm()) {
            orderNumberData = order.getNumberData();
            String dateString = DateTimeFormat.getFormat("yyyyMMdd").format(purchaseOrderDate.getDate());
            orderNumberData.setWithDate(order.getInvoiceNumber().contains(dateString));
            orderNumberData.setDate(orderNumberData.isWithDate() ? dateString : "");
            numberTxtBox.setText(order.getInvoiceNumber());
            crmAccountLookUp.setEnabled(order.getGrnCount() == null || order.getGrnCount() < 1);
        }

        if (order.getRelatedProject() != null) {
            projectLookUp.setSelected(order.getRelatedProject());
        }

        applyTaxCalculationTypeChanges(order.getTaxCalculationType());

        if (!(formParameters.isExternalForm(COPY_FROM_SQ_SO_TO_PO) || COPY_FROM_PRODUCT_LIST.equals(formParameters.getExternalFormID())
                || CONVERT_RFP_TO_PO.equals(formParameters.getExternalFormID()) || COPY_FROM_OPPORTUNITY_TO_PO.equals(formParameters.getExternalFormID()))) {
            currencyWidget.setCurrency(order.getCurrencyID(), order.getExchageRate());
            productsTable.setExchangeRateValue(order.getExchageRate());
        }

        if (objectID != null) {
            purchaseOrderDate.setDate(order.getInvoiceDate().getNonConvertedDate());
            if (order.getInvoiceTermsItem() != null) {
                termsAndShipDatePanel.setData(TermsAndDuePanel.TERMS_TYPE, (order.getDueDate() != null ? order.getDueDate().getNonConvertedDate() : null), order.getInvoiceTermsItem());
            } else {
                termsAndShipDatePanel.setData(TermsAndDuePanel.DUE_TYPE, (order.getDueDate() != null ? order.getDueDate().getNonConvertedDate() : null), null);
            }
        } else if (order.getInvoiceTermsItem() != null) {
            termsAndShipDatePanel.setData(TERMS_TYPE, (order.getDueDate() != null ? order.getDueDate().getNonConvertedDate() : null), order.getInvoiceTermsItem());
        }
        referenceTxtBox.setText(order.getReference());
        paymentTerms.setText(order.getPaymentTerms());
        shippingTerms.setText(order.getShippingTerms());

        if (order.getQuoteId() != null) {
            saleQuoteLookUp.setSelected(new SelectItem(order.getQuoteId(), order.getQuoteNumber()));
        }

        if (order.getCancelDate() != null) {
            cancelDate.setDate(order.getCancelDate().getNonConvertedDate());
        }

        if (order.getPurchaseOrderManager() != null) {
            managerLookUp.setSelected(order.getPurchaseOrderManager());
        }


        productsTable.getPaymentInstruction().setText(order.getPaymentInstruction());

        productsTable.setValues(order);

        if (order.isFixedAssetRelated()) {
            disableFixedAssetRelatedFields();
        }
        if (formParameters.getOpportunityID() != null) {
            dropShipToCustomerLookUp.setSelected(new SelectItem(order.getClientID(), order.getClientName()));
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CLIENT_ADD, order.getClientID(), PurchaseOrderView.this);

            if (order.getItems() != null && order.getItems().length > 0) {
                crmAccountLookUp.setSelected(new SelectItem(order.getItems()[0].getSupplierID(), order.getItems()[0].getSupplierName()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUPPLIER_ADD, order.getItems()[0].getSupplierID(), PurchaseOrderView.this);
            }
            currencyWidget.setCurrency(order.getCurrencyID(), order.getExchageRate());
        }
        if (order.getItems() != null && order.getItems().length > 0) {
            crmAccountLookUp.setSelected(new SelectItem(order.getItems()[0].getSupplierID(), order.getItems()[0].getSupplierName()));
        }
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {

            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();

                if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                    FormGroup projectField = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp, Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY) || AccountingUtils.isMandatoryProjectForPurchaseOrders());
                    projectField.ensureDebugId(InvoiceFormFields.PROJECT);
                    result.add(projectField);
                }

                FormGroup pTypeField = new FormGroup(wfmStrings.paymentType(), paymentTypeDropdown);
                pTypeField.ensureDebugId(InvoiceFormFields.PAYMENT_TYPE);
                result.add(pTypeField);

                FormGroup sqField = new FormGroup(wfmStrings.saleQuoteOrder(), saleQuoteLookUp);
                sqField.ensureDebugId(InvoiceFormFields.SQ_NUMBER);
                result.add(sqField);


                FormGroup payTermsField = new FormGroup(wfmStrings.paymentTerms(), paymentTerms);
                payTermsField.ensureDebugId(InvoiceFormFields.PAYMENT_TERMS);
                result.add(payTermsField);

                FormGroup shipTermsField = new FormGroup(wfmStrings.shippingTerms(), shippingTerms);
                shipTermsField.ensureDebugId(InvoiceFormFields.SHIPPING_TERMS);
                result.add(shipTermsField);

                result.add(priceLevelField);

                return result;
            }
        });
    }

    private void applyTaxCalculationTypeChanges(Integer taxCalculationType) {
        taxCalcTypeListBox.setSelected(taxCalculationType != null ? AccountingUtils.getTaxCalcType(taxCalculationType) : AccountingUtils.getTaxCalcType(TAX_CALCULATION_EXCLUSIVE));
        productsTable.onTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), false);
    }


    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
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

    private HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(PurchaseOrderView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_PURCHASE_ORDER;
                }

                @Override
                public String getRelationName() {
                    return orderNumberData != null ? orderNumberData.getInvoiceNumber() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
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

    @Override
    public String getPropertyCode() {
        return Constants.PURCHASE_ORDER;
    }
}
