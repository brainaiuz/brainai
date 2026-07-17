package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddLeadViewPopup;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.KsaPlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.PlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.UaePlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
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
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.*;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.*;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.historyNote.InvoiceNoteHistoryWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDuePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.*;
import static com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName.SaleOrder;
import static com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName.SaleQuote;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/1/12
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */

public class SalesQuoteView extends FooteredView implements Colapse, Constants, AccountingConstants, AccountingCustomFormConstants, FeatureConstants, FittedContent, HasLinksInterface {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    private final Date currentDate = new Date();
    public DataListBox taxCalcTypeListBox;
    private Integer objectID;
    private boolean isSalesOrder;
    private Date conversionDate;
    private InvoiceNumberData quoteNumberData;
    private CrmAccountLookUp crmAccountLookUp;
    private CrmAccountLookUp supplierLookUp;
    private LookUp leadLookUp;
    private HorizontalPanel lookUpPanel;
    private HorizontalPanel lookUpSwitchPanel;
    private MaterialLink lookUpActionButton;
    private TextBox numberTxtBox;
    private TextBox poNumberTxtBox;
    private TextBox referenceTxtBox;
    private DatePicker quoteDate;
    private TermsAndDuePanel termsAndDuePanel;
    private HorizontalPanel dueAndTearmsPanel;
    private ProjectLookUp projectLookUp;
    private HTML comissionAmount;
    private QuoteAllocationSplitView quoteAllocationSplitView;
    private DataListBox priceLevel;

    private ChosenApproversWidget approver;
    private boolean hasApproval;
    private KpiCheckBox progressInvoicing;

    private CrmAccountWidgets crmAccountWidgets;
    private CurrencyWidget currencyWidget;

    private IntroductionPanel introductionPanel;
    private ProductsTable productsTable;

    private InvoiceNoteHistoryWidget noteHistoryWidget;
    private FooterInformer link;
    private WfmButton2 saveSaleOrderButton, saveButton, submitToManagerButton, pdfVersionButton;
    private SplitButton approveButton, printPdfSplitButton;
    private AddLeadViewPopup addLeadViewPopup;
    private SimpleLink viewBankAccountDetails;
    private DataListBox bankAccounts;
    private MaterialLink customerBalanceLink;

    private Double customerBalance;
    private Integer relationID;
    private String relationType;
    private String relationName;
    private final ArrayList<RelationItem> relations = new ArrayList<>();
    private Params formParameters;
    private HashMap<String, Widget> widgetsMap;
    private SalesQuoteFormPresenter formPresenter;
    private HTMLPanel htmlPanel;
    private final String salesQuoteView = "sales_quote_view";
    private boolean progressinvoicingCheck = false;

    private PdfTemplatePanel pdfTemplatePanel;
    private FooterUploadPanel footerUploadPanel;
    private MaterialLink showMoreLink;
    private InvoiceAdvancedOptions advancedOptions;
    private FormGroup priceLevelField;
    private PlaceOfSupplyWidget placeOfSupplyWidget;
    private Integer defaultTaxCalc;

    public SalesQuoteView(Integer objectID, String from) {
        super("salequoteadd");
        property = new Property(propertyCode());
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.salesQuote()));
        formParameters = new Params();
        formParameters.setType(RECEIVABLE);
        formParameters.setFormType(SALE_QUOTE);
        formParameters.setViewName(SaleQuote);
        formParameters.setCrmFormName(from);
        relationID = objectID;

        if ("copyFromExistingData".equals(from)) {
            formParameters.setExternalFormID(COPY_FROM_EXISTING_DATA);
            formParameters.setExternalObjectID(objectID);
        } else if ("fromClientList".equals(from)) {
            formParameters.setExternalFormID(COPY_FROM_CLIENT_SUPPLIER);
            formParameters.setExternalObjectID(objectID);
        } else if ("copyFromPO".equals(from)) {
            formParameters.setExternalFormID(COPY_PO_TO_SQ);
            formParameters.setExternalObjectID(objectID);
        } else if ("copyFromSOData".equals(from)) {
            formParameters.setExternalFormID(COPY_FROM_SO_TO_SQ);
            formParameters.setExternalObjectID(objectID);
        } else {
            if (RelationItem.TYPE_LEAD.equals(from)) {
                relationType = RelationItem.TYPE_LEAD;
            } else if (OPPORTUNITY.equals(from)) {
                formParameters.setOpportunityID(objectID);
                relationType = RelationItem.TYPE_OPPORTUNITY;
            } else {
                formParameters.setExternalFormID(COPY_FROM_CRM_ACCOUNT);
                formParameters.setExternalObjectID(objectID);
            }
        }

        this.progressinvoicingCheck = true;
    }

    public SalesQuoteView(String[] params) {
        super("salequoteadd");
        property = new Property(propertyCode());
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.salesQuote()));
        initFormParameters(params);
    }

    public SalesQuoteView(Integer objectID, boolean isSalesOreder) {
        super("edit");
        this.objectID = objectID;
        this.isSalesOrder = isSalesOreder;
        property = new Property(propertyCode());
        setDescription(wfmStrings.edit() + " " + property.getSingular(wfmStrings.salesQuote()));
        initFormParameters(null);
    }

    public SalesQuoteView(boolean isSalesOrder, Integer objectID, String from) {
        super("saleorderadd");
        this.isSalesOrder = isSalesOrder;
        property = new Property(propertyCode());
        setDescription(property.getSingular(wfmStrings.addMess(), accountingStrings.salesOrder()));
        formParameters = new Params();
        formParameters.setType(RECEIVABLE);
        if (isSalesOrder) {
            formParameters.setInvoiceCustomType(SALE_ORDER);
            formParameters.setFormType(SALE_ORDER);
            formParameters.setViewName(SaleOrder);
        }
        formParameters.setCrmFormName(from);
        if ("copyFromExistingData".equals(from)) {
            formParameters.setExternalFormID(COPY_FROM_EXISTING_DATA);
            formParameters.setExternalObjectID(objectID);

        } else if ("fromClientList".equals(from)) {
            formParameters.setExternalFormID(COPY_FROM_CLIENT_SUPPLIER);
            formParameters.setExternalObjectID(objectID);
        } else {
            if (RelationItem.TYPE_LEAD.equals(from)) {
                relationType = RelationItem.TYPE_LEAD;
                relationID = objectID;
            }
            if (OPPORTUNITY.equals(from)) {
                formParameters.setOpportunityID(objectID);
                relationType = RelationItem.TYPE_OPPORTUNITY;
                relationID = objectID;
            } else {
                formParameters.setExternalFormID(COPY_FROM_CRM_ACCOUNT);
                formParameters.setExternalObjectID(objectID);
            }
        }


        this.progressinvoicingCheck = true;
    }

    public SalesQuoteView(boolean isSalesOrder, String[] params) {
        super("saleorderadd");
        this.isSalesOrder = isSalesOrder;
        property = new Property(propertyCode());
        setDescription(property.getSingular(wfmStrings.addMess(), accountingStrings.salesOrder()));
        initFormParameters(params);
    }

    private void initFormParameters(String[] params) {
        formParameters = new Params();
        formParameters.setType(RECEIVABLE);
        if (isSalesOrder) {
            formParameters.setInvoiceCustomType(SALE_ORDER);
            formParameters.setFormType(SALE_ORDER);
            formParameters.setViewName(SaleOrder);
        } else {
            formParameters.setFormType(SALE_QUOTE);
            formParameters.setViewName(SaleQuote);
        }
        formParameters.setObjectID(objectID);
        if (params != null) {
            //Getting parameters from CRM in following format: viewName/accountID/contactID or viewName/leadID or viewName/OpportunityID
            if (params.length > 2 && ("lead".equals(params[1]) || "account".equals(params[1]) || "contact".equals(params[1]) || OPPORTUNITY.equals(params[1]))) {
                formParameters.setCrmFormName(params[1]);

                if (OPPORTUNITY.equals(params[1])) {
                    formParameters.setOpportunityID(Integer.valueOf(params[2]));
                    relationID = Integer.parseInt(params[2]);
                    relationType = RelationItem.TYPE_OPPORTUNITY;
                } else {
                    formParameters.setExternalFormID(COPY_FROM_CRM_ACCOUNT);
                    formParameters.setExternalObjectID(Integer.valueOf(params[2]));
                }

                if ("lead".equals(params[1]) || (params.length > 4 && RelationItem.TYPE_LEAD.equals(params[4]))) {
                    relationID = Integer.parseInt(params[3]);
                    relationType = RelationItem.TYPE_LEAD;
                } else if (params.length > 3) {
                    relationID = Integer.parseInt(params[3]);
                    relationType = RelationItem.TYPE_CONTACT;
                }
            } else if (params.length > 2 && "copyFromExistingData".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_EXISTING_DATA);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && "fromClientList".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_CLIENT_SUPPLIER);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && "fromProductList".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_PRODUCT_LIST);
                String[] idsArray = params[2].split(",");
                ArrayList<Integer> idList = new ArrayList<>();
                for (String id : idsArray) {
                    idList.add(Integer.parseInt(id));
                }
                formParameters.setExternalObjectIDList(idList);
            } else if (params.length > 2 && "relatedProject".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_CLIENT_SUPPLIER);
                formParameters.setExternalObjectID(Integer.valueOf(params[3]));
                formParameters.setRelatedProjectID(Integer.valueOf(params[2]));
                this.progressinvoicingCheck = true;
            } else if (params.length > 2 && "copyFromPO".equals(params[1])) {
                formParameters.setExternalFormID(COPY_PO_TO_SQ);
                formParameters.setExternalObjectID(Integer.valueOf(params[2]));
            } else if (params.length > 2 && "copyFromSOData".equals(params[1])) {
                formParameters.setExternalFormID(COPY_FROM_SO_TO_SQ);
                formParameters.setExternalObjectID(objectID);
            } else if (params.length > 2 && "CONVERT".equals(params[0])) {
                formParameters.setConvertFormType(params[1]);
                if (params[2] != null && params[2].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                    formParameters.setConvertFormId(Integer.parseInt(params[2]));
                }
            }
        }

    }

    protected Widget onInitialize() {

        boolean hasPermissonCustomerQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_QUICK_ADD);
        boolean hasPermissonCustomerAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_ADD);

        crmAccountLookUp = new SmartCrmAccountLookup(CrmAccountLookUp.CUSTOMER, true, () -> {
            if (hasPermissonCustomerQuick) {
                new CusSuppQuickAddView(CrmAccountLookUp.CUSTOMER, crmAccountLookUp.getLastValueBeforeClick());
            } else if (hasPermissonCustomerAdd) {
                SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add");
            }
        }, false, hasPermissonCustomerQuick || hasPermissonCustomerAdd);
        crmAccountLookUp.ensureDebugId(salesQuoteView + "crmAccountLookUp");
        crmAccountLookUp.setAutocompleteOff();


        supplierLookUp = new SmartCrmAccountLookup(CrmAccountLookUp.SUPPLIER, true, () -> new CusSuppQuickAddView(CrmAccountLookUp.SUPPLIER, crmAccountLookUp.getLastValueBeforeClick()));
        supplierLookUp.ensureDebugId(salesQuoteView + "supplierLookUp");
        supplierLookUp.setAutocompleteOff();

        /**
         * This part is for a create sale quote to LEAD
         */
        {
            leadLookUp = new SmartCrmAccountLookup(CrmAccountLookUp.LEAD, true, () -> {
                relationType = RelationItem.TYPE_LEAD;
                formParameters.setCrmFormName("lead");
                addLeadViewPopup = new AddLeadViewPopup();
                addLeadViewPopup.center();
            });

            lookUpActionButton = new MaterialLink(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
            lookUpActionButton.setStyleName("btn btn--lightgrey hasicon--right");
            Icon icon = new Icon();
            icon.setClass("ficon--keyboard-arrow-down");
            lookUpActionButton.add(icon);

            MaterialDropDown lookUpMenuBar = new MaterialDropDown(lookUpActionButton);
            lookUpMenuBar.setBelowOrigin(true);
            MaterialLink customerItem = new MaterialLink(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
            MaterialLink leadItem = new MaterialLink(wfmStrings.lead());
            lookUpMenuBar.add(customerItem);
            lookUpMenuBar.add(leadItem);
            lookUpActionButton.add(lookUpMenuBar);

            lookUpSwitchPanel = new HorizontalPanel();
            lookUpSwitchPanel.add(crmAccountLookUp);

            lookUpPanel = new HorizontalPanel();
            lookUpPanel.setStyleName("gwt-dropdown-lookup");
            lookUpPanel.add(lookUpActionButton);
            lookUpPanel.add(lookUpSwitchPanel);

            if (Utils.hasGenericAccess(GenericSettingsEnum.LEAD_TO_SALES_QOUTE_ENABLED)) {
                customerItem.addClickHandler(ch -> onChangeLookUp(true));
                leadItem.addClickHandler(ch -> onChangeLookUp(false));
            }
        }
        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            if (Utils.isSaudiCompany()) {
                placeOfSupplyWidget = new KsaPlaceOfSupplyWidget(null, null, Constants.RECEIVABLE);
            } else if (Utils.isUAECompany()) {
                placeOfSupplyWidget = new UaePlaceOfSupplyWidget(null, null, Constants.RECEIVABLE);
            }
        }
        crmAccountWidgets = new CrmAccountWidgets(crmAccountLookUp, RECEIVABLE, SALE_QUOTE, formParameters.isEditForm());

        if (relationID != null && relationType != null) {
            getRelationName(relationID, relationType);
        }

        projectLookUp = new ProjectLookUp(RECEIVABLE, crmAccountLookUp);
        projectLookUp.setEnsureDebugId(salesQuoteView + "relatedProject");
        projectLookUp.ensureDebugId(salesQuoteView + "relatedProject");

        priceLevel = new DataListBox();
        priceLevel.ensureDebugId(salesQuoteView + "priceLevel");

        priceLevelField = new FormGroup(accountingStrings.relatedPriceLevel(), priceLevel);
        priceLevelField.ensureDebugId("inv_price_level");

        approver = new ChosenApproversWidget(SALE_QUOTE, objectID);

        taxCalcTypeListBox = new DataListBox();
        taxCalcTypeListBox.ensureDebugId(salesQuoteView + "taxCalcTypeListBox");
        taxCalcTypeListBox.setWithoutNullLabel(true);
        taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        getDefaultTaxCalcType();

        currencyWidget = new CurrencyWidget();

        progressInvoicing = new KpiCheckBox(wfmStrings.progressInvoicing());
        progressInvoicing.ensureDebugId(salesQuoteView + "progressInvoicing");

        numberTxtBox = new TextBox();
        numberTxtBox.ensureDebugId(salesQuoteView + "numberTxtBox");

        poNumberTxtBox = new TextBox();
        poNumberTxtBox.ensureDebugId(salesQuoteView + "poNumberTxtBox");

        referenceTxtBox = new TextBox(true);
        referenceTxtBox.ensureDebugId(salesQuoteView + "referenceTxtBox");

        quoteDate = new DatePicker(currentDate, true);
        quoteDate.ensureDebugId(salesQuoteView + "quoteDate");

        noteHistoryWidget = new InvoiceNoteHistoryWidget();

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);

        termsAndDuePanel = new TermsAndDuePanel(accountingStrings.validDate());
        termsAndDuePanel.ensureDebugId(salesQuoteView + "termsAndDuePanel");

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        introductionPanel = new IntroductionPanel();
        introductionPanel.getIntroduction().addStyleName("keepDropDownOpen");

        productsTable = new ProductsTable(RECEIVABLE, SALE_QUOTE, formParameters);
        productsTable.setCrmAccountLookUp(crmAccountLookUp);
        productsTable.setCurrencyWidget(currencyWidget);

        currencyWidget.setDatePicker(quoteDate);

        initBankAccountWidgets();

        footerUploadPanel = new FooterUploadPanel(F_SALE_QUOTE, objectID, true, wfmStrings.attachments());

        comissionAmount = new HTML(AccountingUtils.ZERO.toString());

        advancedOptions = createAdvancedOptions();
        advancedOptions.addToAddressBodyContainer(crmAccountWidgets.getBillAddress());
        advancedOptions.addToMailAddressBodyContainer(crmAccountWidgets.getMailAddress());
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addStyleName("btn-flat SalesQueteView");
        showMoreLink.addClickHandler(ch -> {
            showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
        });

        saveSaleOrderButton = new WfmButton2(wfmStrings.save(), BTN_DEFAULT_OUTLINE);
        saveSaleOrderButton.ensureDebugId(salesQuoteView + "saveSaleOrderButton");

        saveButton = new WfmButton2(wfmStrings.draft(), BTN_DEFAULT_OUTLINE);
        saveButton.ensureDebugId(salesQuoteView + "saveButton");

        if (Utils.hasPermission(PermissionConstants.SALES_ORDER_SUBMIT_AND_EMAIL_SEND) && isSalesOrder) {
            submitToManagerButton = new WfmButton2(wfmStrings.submitForApprovalAndEmail(), WfmButton2.BTN_PRIMARY);
        } else {
            submitToManagerButton = new WfmButton2(wfmStrings.submitForApproval(), WfmButton2.BTN_PRIMARY);
        }

        submitToManagerButton.ensureDebugId(salesQuoteView + "submitToManagerButton");

        approveButton = new SplitButton(120, BTN_PRIMARY);
        approveButton.ensureDebugId(salesQuoteView + "saveAndApprove");

        pdfVersionButton = new WfmButton2(wfmStrings.pdfVersion(), BTN_DEFAULT_OUTLINE);
        pdfVersionButton.ensureDebugId(salesQuoteView + "pdfVersionButton");

        printPdfSplitButton = new SplitButton(97, BTN_DEFAULT_OUTLINE);
        printPdfSplitButton.ensureDebugId(salesQuoteView + "pdfVersionButton");

        saveSaleOrderButton.setVisible(false);
        saveButton.setVisible(false);
        submitToManagerButton.setVisible(false);

        widgetsMap = new HashMap<>();

        this.formPresenter = generateFormPresenter();
        this.formPresenter.bindUI();
        return null;
    }

    private void initWidgetsMap(NewInvoice data) {
        FormGroup customerField = null;

        if (Utils.hasGenericAccess(GenericSettingsEnum.LEAD_TO_SALES_QOUTE_ENABLED) && !isSalesOrder) {
            customerField = new FormGroup(lookUpPanel);
            customerField.ensureDebugId(InvoiceFormFields.CUSTOMER);

            Div customerFieldLabel = customerField.getGroupLabel();
            customerFieldLabel.addStyleName("label-group");
            customerFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));

            Span balance = new Span(wfmStrings.balance() + ": ");
            balance.add(customerBalanceLink);
            customerFieldLabel.add(balance);

            widgetsMap.put(INPUT_CRM_ACCOUNT, customerField);
        } else {
            customerField = new FormGroup(crmAccountLookUp);

            Div customerFieldLabel = customerField.getGroupLabel();
            customerFieldLabel.addStyleName("label-group");
            customerFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));

            Span balance = new Span(wfmStrings.balance() + ": ");
            balance.add(customerBalanceLink);
            customerFieldLabel.add(balance);

            widgetsMap.put(INPUT_CRM_ACCOUNT, customerField);
        }

        currencyWidget.ensureDebugId(InvoiceFormFields.CURRENCY);
        systemCustomFieldsMap.put(INPUT_CURRENCY_LIST_BOX, currencyWidget);

        referenceTxtBox.ensureDebugId(InvoiceFormFields.REFERENCE);
        systemCustomFieldsMap.put(INPUT_REFERENCE, referenceTxtBox);

        systemCustomFieldsMap.put(INPUT_PROGRESS_INVOICING, progressInvoicing);

        taxCalcTypeListBox.ensureDebugId(InvoiceFormFields.TAX_CALC);
        systemCustomFieldsMap.put(INPUT_TAX_CALC_TYPE, taxCalcTypeListBox);

        systemCustomFieldsMap.put(INPUT_SHOW_MORE, showMoreLink);

        addSystemCustomFields(widgetsMap);

        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), quoteDate));
        widgetsMap.put(INPUT_DUE_DATE, termsAndDuePanel.getTermsDueAsField());

        if (!isSalesOrder) {
            widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.manager(), approver)));

            if (formParameters.isEditForm() && !data.isApproverSaved()) {
                approver.reloadApproverWidgets(RelationItem.TYPE_SALEQUOTE, null);
            } else {
                approver.reloadApproverWidgets(RelationItem.TYPE_SALEQUOTE, objectID);
            }

        } else if (isSalesOrder && data.isApprover()) {
            widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.manager(), approver)));

            if (formParameters.isEditForm() && !data.isApproverSaved()) {
                approver.reloadApproverWidgets(RelationItem.TYPE_SALEORDER, null);
            } else {
                approver.reloadApproverWidgets(RelationItem.TYPE_SALEORDER, objectID);
            }
        }

        if (productsTable.isQuoteComissionEnabled()) {
            widgetsMap.put(INPUT_COMISSION_SPLIT, quoteAllocationSplitView);
        }
        FormGroup numberField = new FormGroup(isSalesOrder ? (property.getShortForNumber(wfmStrings.orderNumber())) :
                (property.getShortForNumber(wfmStrings.quoteNumber())), numberTxtBox);
        if (Utils.hasRole(CLIENT) || !Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_NUMBER_CHANGE_ACCESS_SO_SQ)) {
            numberTxtBox.setEnabled(false);
        }
        numberField.ensureDebugId(InvoiceFormFields.NUMBER);
        widgetsMap.put(INPUT_NUMBER, numberField);

        if ((Utils.isUAECompany() || Utils.isSaudiCompany()) && Utils.isVatRegistered()) {
            widgetsMap.put(INPUT_PLACE_OF_SUPPLY, placeOfSupplyWidget);
        }
        widgetsMap.put(INPUT_ITEM_TABLE, productsTable.getItemsTable());
        widgetsMap.put(INPUT_TOTALS_TABLE, productsTable.getTotalsTable());

        //Payment instruction field
        {
            FormGroup instructionField = new FormGroup(productsTable.getPaymentInstruction());
            if (Utils.hasRole(CLIENT)) {
                productsTable.getPaymentInstruction().setEnabled(false);
            }
            productsTable.getPaymentInstruction().ensureDebugId("payment_instruction");
            Div instructionLabel = instructionField.getGroupLabel();
            instructionLabel.addStyleName("label-group");
            instructionLabel.add(new Span(isSalesOrder ? property.getSingular(accountingStrings.salesOrderInstructions(), accountingStrings.salesOrder())
                    : property.getSingular(accountingStrings.salesOrderInstructions(), wfmStrings.salesQuote())));
            instructionLabel.add(productsTable.getPaymentTermsConditionsListBox());
            widgetsMap.put(INPUT_INSTRUCTION, instructionField);
        }

        this.initApproverLoadHandler();
    }

    private void initApproverLoadHandler() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, SalesQuoteView.this, (sender, args) -> {
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

    public void fillForm(NewInvoice result) {

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_REFERENCE_IN_SUM)) {
            currencyWidget.setModalCompleteListener(() -> {
                referenceTxtBox.setEnabled(false);
                // This will be called when the modal completes its logic
                referenceTxtBox.setValue(currencyWidget.getExRateInSum() != null ? currencyWidget.getExRateInSum().toString() : "");
            });
        }
        crmAccountWidgets.setCurrencyWidget(currencyWidget);

        if (this.progressinvoicingCheck) {
            progressInvoicing.setValue(result.isProgressInvoicing());
        }
        productsTable.getBaseTotalLabel().setHTML(accountingMessages.dynamicTotal(currencyWidget.getBaseCurrencyName()));
        currencyWidget.setCurrency(result.getCurrencyID(), result.getExchageRate());

        //set currency id to product table
        productsTable.setCurrencyId(result.getCurrencyID());
        applyTaxCalculationTypeChanges(result.getTaxCalculationType());
        if (formParameters.isEditForm()) {//From Edit Form
            productsTable.getTotalLabel().setHTML(accountingMessages.dynamicTotal(result.getCurrencyName()));
            setValues(result);
            if (isSalesOrder && result.getLastGrnDate() != null) {
                quoteDate.addChangeHandler(event -> {
                    if (quoteDate.getDate().after(result.getLastGrnDate().getNonConvertedDate())) {
                        quoteDate.setDate(result.getInvoiceDate().getNonConvertedDate());
                        Info.warn(wfmStrings.dateCannotBeAfterGrnDate());
                    }
                });
            }
        } else if (formParameters.isExternalForm(COPY_FROM_EXISTING_DATA) || formParameters.isExternalForm(COPY_FROM_SO_TO_SQ) || COPY_FROM_PRODUCT_LIST.equals(formParameters.getExternalFormID()) || COPY_PO_TO_SQ.equals(formParameters.getExternalFormID())) {//From Copy To New Link..., From Product List
            setValues(result);
        } else if (OPPORTUNITY.equals(formParameters.getCrmFormName()) || formParameters.isExternalForm(COPY_FROM_CRM_ACCOUNT)) {//create Sale Quote
            crmAccountWidgets.presenter.initContactAddress(result.getTypeItem(), !OPPORTUNITY.equals(formParameters.getCrmFormName()), Address.EntityType.CrmAccount);
            quoteDate.setDate(currentDate);
            if (DUE_TYPE.equals(result.getDueDateType())) {
                termsAndDuePanel.setData(DUE_TYPE, DateUtil.addDays(currentDate, (result.getDueDays() != null ? result.getDueDays() : 0)), null);
            } else {
                termsAndDuePanel.setData(TERMS_TYPE, (result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null), result.getInvoiceTermsItem());
            }
            if (OPPORTUNITY.equals(formParameters.getCrmFormName())) {
                setValues(result);
            }
        } else {//From Add Form And Client Supplier List

            productsTable.setDefaultAccount(result.getDefaultAccountItem());
            productsTable.setDefaultTax(result.getDefaultTaxItem());
            productsTable.setDefaultDiscount(result.getDefaultDiscountItem());

            if (formParameters.isExternalForm(COPY_FROM_CLIENT_SUPPLIER) && result.getTypeItem() != null) {
                crmAccountLookUp.addItem(result.getTypeItem());
                crmAccountWidgets.presenter.initContactAddress(result.getTypeItem(), true, Address.EntityType.CrmAccount);
                if (formParameters.getRelatedProjectID() != null) {
                    projectLookUp.setSelected(result.getRelatedProject());
                }
                if ((customerBalance = result.getTypeItem().getSupplierCustomerBalance()) >= 0) {
                    customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    customerBalanceLink.setText(AccountingUtils.get().formatPrice(result.getTypeItem().getSupplierCustomerBalance()));
                } else {
                    customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                    customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * result.getTypeItem().getSupplierCustomerBalance()) + ")");
                }
            }
            quoteDate.setDate(currentDate);
            termsAndDuePanel.setData(DUE_TYPE, DateUtil.addDays(currentDate, (result.getDueDays() != null ? result.getDueDays() : 0)), null);

            if (formParameters.getConvertFormType() != null && result.getTypeItem() != null) {
                crmAccountLookUp.addItem(result.getTypeItem());
                productsTable.setExchangeRateValue(result.getExchageRate());
                productsTable.setValues(result);
                if (result.getInvoiceDate() != null) {
                    quoteDate.setDate(result.getInvoiceDate().getNonConvertedDate());
                }
                if (result.getInvoiceTermsItem() != null) {
                    termsAndDuePanel.setData(TERMS_TYPE, (result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null), result.getInvoiceTermsItem());
                } else {
                    termsAndDuePanel.setData(DUE_TYPE, result.getDueDate() != null ? result.getDueDate().getNonConvertedDate() : null, null);
                }
                setValues(result);
            }
        }
        if (isSalesOrder && result.isHasGDN() && formParameters.isEditForm()) {
            // If sales order has gdn, line items needs to be disabled
            productsTable.setEnabled(false, true, formParameters.getExternalFormID());
            // If sales order has gdn, progressive invoicing should be disabled
            progressInvoicing.setVisible(false);
        }

        if (isSalesOrder && RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
            currencyWidget.setEnabled(false);
            currencyWidget.getCurrencyListBox().setEnabled(false);
            taxCalcTypeListBox.setEnabled(false);
            crmAccountLookUp.setEnabled(false);
        }

    }

    private void initBankAccountWidgets() {
        bankAccounts = new DataListBox();
        viewBankAccountDetails = new SimpleLink(accountingStrings.viewAllDetails(), "");
        viewBankAccountDetails.setVisible(true);
    }

    private void initProductTable(NewInvoice result) {
        // Need to disable delete and add icons when it has gdn
        if (isSalesOrder && result.isHasGDN() && formParameters.isEditForm()) {
            productsTable.setIsDeleteAndAddDsiabled(true);
        }
        productsTable.setColumnsMap(drawColumns(result));
        productsTable.initItemTable(false);
        productsTable.setDefaultAccount(result.getDefaultAccountItem());
        productsTable.setDefaultTax(result.getDefaultTaxItem());
        productsTable.setDefaultDiscount(result.getDefaultDiscountItem());

    }


    /**
     * Recent survey shows adults' desicion for their education in many fields and their tuition fees
     *
     * @return
     */
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
                        columnConfig.setMinValue(column.getMinValue());
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
                        columnConfig.setMinValue(column.getMinValue());
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
                    case ProductsTable.DOUBLE_DISCOUNT_LIST:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.DOUBLE_DISCOUNT_LIST, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.DOUBLE_DISCOUNT_LIST, columnConfig);
                        break;
                    case ProductsTable.DOUBLE_DISCOUNT_AMT:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.DOUBLE_DISCOUNT_AMT, "", Utils.getColumnWidth(column.getWidth(), 75), column.isRequired(), Constants.RIGHT_ALIGN_CELL, true);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.DOUBLE_DISCOUNT_AMT, columnConfig);
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
                    case ProductsTable.ATTACHMENT:
                        columnConfig = new ColumnConfig(CustomCell.class, ProductsTable.ATTACHMENT, Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setTitle(column.getTitle());
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setDisabled(column.isDisabled());
                        columnsMap.put(ProductsTable.ATTACHMENT, columnConfig);
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
        } else {
            columnsMap.put(ProductsTable.PRODUCT, new ColumnConfig(LookUpCell.class, ProductsTable.PRODUCT, 200, true));
            columnsMap.put(ProductsTable.DESCRIPTION, new ColumnConfig(CustomCell.class, ProductsTable.DESCRIPTION, 250, false));
            columnsMap.put(ProductsTable.QTY, new ColumnConfig(CustomCell.class, ProductsTable.QTY, "", 75, true, Constants.RIGHT_ALIGN_CELL, true));
            columnsMap.put(ProductsTable.UNITPRICE, new ColumnConfig(CustomCell.class, ProductsTable.UNITPRICE, "", 75, true, Constants.RIGHT_ALIGN_CELL, true));
            columnsMap.put(ProductsTable.DISCOUNT_AMT, new ColumnConfig(CustomCell.class, ProductsTable.DISCOUNT_AMT, "", 75, false, Constants.RIGHT_ALIGN_CELL, true));
            columnsMap.put(ProductsTable.NET_AMT, new ColumnConfig(CustomCell.class, ProductsTable.NET_AMT, 100, false, Constants.RIGHT_ALIGN_CELL));
            columnsMap.put(ProductsTable.NET_AMT, new ColumnConfig(CustomCell.class, ProductsTable.NET_AMT, "", 100, false, Constants.RIGHT_ALIGN_CELL, true));
            columnsMap.put(ProductsTable.TAX_LIST, new ColumnConfig(LookUpCell.class, ProductsTable.TAX_LIST, 100, GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()));
        }

        return columnsMap;
    }

    private SalesQuoteFormPresenter generateFormPresenter() {
        return new SalesQuoteFormPresenter(new SalesQuoteViewInterface() {
            @Override
            public boolean isEditForm() {
                return formParameters.isEditForm();
            }

            @Override
            public boolean isSalesOrder() {
                return isSalesOrder;
            }

            @Override
            public ChosenApproversWidget getApproverLookUp() {
                return approver;
            }

            @Override
            public WfmButton2 getSubmitToManagerButton() {
                return submitToManagerButton;
            }

            @Override
            public WfmButton2 getSalesOrderButton() {
                return saveSaleOrderButton;
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
            public Date getConversionDate() {
                return conversionDate;
            }

            @Override
            public void setConversionDate(Date date) {
                conversionDate = date;
            }

            @Override
            public NewInvoice getFormData(String invoiceStatus, boolean calculate) {
                return getQuoteData(invoiceStatus);
            }

            @Override
            public LookUp getCrmAccountLookUp() {
                return crmAccountLookUp;
            }

            @Override
            public LookUp getSupplierLookUp() {
                return supplierLookUp;
            }

            @Override
            public KpiCheckBox progressInvoicing() {
                return progressInvoicing;
            }

            @Override
            public HasLinks getLinkingUtils() {
                return getLinkingUtil();
            }

            @Override
            public FooterInformer getLinkWidget() {
                return link;
            }

            @Override
            public LookUp getLeadLookUp() {
                return leadLookUp;
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
            public FormGroup getPriceLevel() {
                return priceLevelField;
            }

            @Override
            public DataListBox getPriceLevelDropdown() {
                return priceLevel;
            }

            @Override
            public CurrencyWidget getCurrencyWidget() {
                return currencyWidget;
            }

            @Override
            public DatePicker getDatePicker() {
                return quoteDate;
            }

            @Override
            public DatePicker getDueDatePicker() {
                return null;
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
            public TextBox getNumberTxtBox() {
                return numberTxtBox;
            }

            @Override
            public TextBox getPONumberTxtBox() {
                return poNumberTxtBox;
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
                return SalesQuoteView.this;
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
                initQuoteCustomFields(result);
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

                add(htmlPanel);
            }

            @Override
            public Date getCurrentDate() {
                return currentDate;
            }

            @Override
            public InvoiceNumberData getNumberData() {
                return quoteNumberData;
            }

            @Override
            public void setNumberData(InvoiceNumberData numberData) {
                quoteNumberData = numberData;
            }

            @Override
            public SplitButton getSplitButtonPdf() {
                return printPdfSplitButton;
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
                    showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
                }
                return validate;
            }

            @Override
            public boolean validateProjectMandatory() {
                if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && !Utils.isProjectInLineItemEnable() && Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY)) {
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
                return SalesQuoteView.this.validateSystemCustomFields();
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
//                initQuotePdfTemplates(result);
            }

            @Override
            public PlaceOfSupplyWidget getPlaceOfSupplyWidget() {
                return placeOfSupplyWidget;
            }

            @Override
            public void createComissionAllocateItem() {
                createComissionAllocateLink();
            }

            @Override
            public SimpleLink getBankAccountDetailLink() {
                return viewBankAccountDetails;
            }

            @Override
            public MaterialLink getCustomerBalanceLink() {
                return customerBalanceLink;
            }

            @Override
            public Double getCustomerBalance() {
                return customerBalance;
            }

            @Override
            public void setCustomerBalance(Double balance) {
                customerBalance = balance;
            }

            @Override
            public DataListBox getBankAccountListBox() {
                return bankAccounts;
            }

            @Override
            public Property getProperty() {
                return property;
            }

        });
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {

            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();
                if (!Utils.hasRole(CLIENT)) {
                    result.add(new FormGroup(wfmStrings.bankDetails(), bankAccounts));
                }
                if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                    result.add(new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp, Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_MANDATORY)));
                }
//                result.add(new FormGroup(accountingStrings.po(), poNumberTxtBox));
                result.add(priceLevelField);
                return result;
            }
        });
    }

    public void createComissionAllocateLink() {
        quoteAllocationSplitView = new QuoteAllocationSplitView(productsTable.getNewInvoice().getAllocateComissionItems(), true, formParameters.isEditForm());
        quoteAllocationSplitView.setListener(() -> quoteAllocationSplitView.setComissionAmount(productsTable.getComissionAmount()));

        productsTable.setComissionListener(() -> {
            comissionAmount.setHTML(AccountingUtils.get().formatPrice(productsTable.getComissionAmount()));
            quoteAllocationSplitView.setVisible(productsTable.getComissionAmount().compareTo(ZERO) != 0);
        });
    }


    /*private void initQuotePdfTemplates(NewInvoice quote) {
        if (quote.getPdfTemplateList() != null && quote.getPdfTemplateList().getItems() != null && quote.getPdfTemplateList().getItems().length > 0) {
            pdfTemplatePanel = new PdfTemplatePanel(quote);
            pdfTemplatePanel.ensureDebugId("pdf-tmplateList");

            FormGroup pdfTemplateField = new FormGroup(accountingStrings.choosePdfTemplate(), pdfTemplatePanel);
            pdfTemplateField.ensureDebugId("inv_pdf_template");

            advancedOptions.addToBodyContainer(pdfTemplateField);
        }
    }*/

    private void initQuoteCustomFields(NewInvoice invoice) {
        if (invoice.getCustomFieldItems() != null && invoice.getCustomFieldItems().size() > 0) {
            if (isSalesOrder) {
                advancedOptions.createAndAppendInvoiceCustomFieldsView(ViewAddFiledsCodeName.SaleOrderAdd, invoice);
            } else {
                advancedOptions.createAndAppendInvoiceCustomFieldsView(ViewAddFiledsCodeName.SaleQuoteAdd, invoice);
            }
        }
    }

    public NewInvoice getQuoteData(String invoiceStatus) {
        productsTable.calculate(true);

        NewInvoice quote = new NewInvoice();
        quote.setClientID(crmAccountLookUp.getSelectedItemID());
        quote.setBillAddressID(crmAccountWidgets.getBillAddressID());
        quote.setMailAddressID(crmAccountWidgets.getMailAddressID());
        quote.setCurrencyID(currencyWidget.getCurrencyID());
        quote.setExchageRate(productsTable.getExchangeRateValue());
        quote.setRelatedProject(projectLookUp.getSelectedItem());
        quote.setPriceLevel(priceLevel.getSelectedItem());
        quote.setReference(referenceTxtBox.getText());
        quote.setApprovers(approver.getChosenApprovers());
        quote.setProgressInvoicing(progressInvoicing.getValue());

        if (placeOfSupplyWidget != null) {
            quote.setPlaceOfSupply(placeOfSupplyWidget.getSelectedPlaceOfSupply());
        }

        //set opportunity ID
        quote.setOpportunityID(formParameters.getOpportunityID());

        Integer fourDigitNumber = quoteNumberData.parseFourDigitNumber(numberTxtBox.getText());
        if (fourDigitNumber != null) {
            quote.setFourDigitNumber(fourDigitNumber.toString());
        }
        quote.setInvoiceNumber(numberTxtBox.getText());
        if (productsTable.getNewInvoice() != null && !numberTxtBox.getText().equals(productsTable.getNewInvoice().getInvoiceNumber())) {
            quote.setChangedNumber(true);
        }
        quote.setPoNumber(poNumberTxtBox.getText());
        quote.setInvoiceDate(new DateNonConvertable(DateUtil.resetTime(quoteDate.getDate())));

        if (termsAndDuePanel.isDueTypeSelected()) {
            quote.setDueDate(new DateNonConvertable(termsAndDuePanel.getDueDate()));
        } else {
            quote.setDueDate(new DateNonConvertable(termsAndDuePanel.getDueDate()));
            quote.setInvoiceTermsItem(termsAndDuePanel.getInvoiceTerms());
        }
        quote.setShippingMethodID(productsTable.getShippingMethod() != null ? productsTable.getShippingMethod().getId() : null);
        quote.setIntroduction(introductionPanel.getIntroduction().getText());
        quote.setSubtotal(productsTable.getSubTotal());
        quote.setTotal(productsTable.getTotalInBaseCurrency());
        quote.setTotalDiscount(productsTable.getTotalDiscount());
        quote.setShippingPrice(productsTable.getShippingTotal());
        quote.setNetAmountTotal(productsTable.getNetAmountTotal());
        quote.setTotalInInvoiceCurrency(productsTable.getTotalInInvoiceCurrency());
        quote.setTotalTaxes(productsTable.getTotalTaxAmountByCurrency());
        quote.setTotalTaxes(quote.getTotalTaxes().add(productsTable.getShippingTaxAmount()));
        quote.setTotalTaxItems(productsTable.getTotalTaxItems());
        quote.setType(RECEIVABLE);
        quote.setStatusCode(invoiceStatus);
        quote.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));
        quote.setPaymentInstructionID(productsTable.getPaymentTermsConditionsListBox().getSelectedId());
        quote.setPaymentInstruction(productsTable.getPaymentInstruction().getText());
        quote.setTaxCalculationType(taxCalcTypeListBox.getSelectedId());
        quote.setItems(productsTable.getDataItems(invoiceStatus));
        if (supplierLookUp.getSelectedItemID() != null) {
            quote.setSupplierID(supplierLookUp.getSelectedItemID());
        }

        //simplifying to filter for transactions
        if (projectLookUp != null && !Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            for (NewInvoiceItem item : quote.getItems()) {
                if (item == null) {
                    continue;
                }
                item.setProject(projectLookUp.getSelectedItem());
            }
        }
        quote.setComissionAmount(productsTable.getComissionAmount());
        quote.setBankAccount(bankAccounts.getSelectedItem());
        if (pdfTemplatePanel != null) {
            quote.setPdfTemplateID(pdfTemplatePanel.getSelectedTemplateID());
        }
        if (productsTable.isQuoteComissionEnabled()) {
            quote.setAllocateComissionItems(quoteAllocationSplitView.getAllocateComissionItems());
        }
        quote.setCustomFieldItems(advancedOptions.getCustomFieldsData());
        /*if (customFieldsView != null) {
            quote.setCustomFieldItems(customFieldsView.getData());
        }*/

        quote.setAttachments(footerUploadPanel.getAttachedFiles());

        if (relationID != null && relationType != null) {
            relations.add(new RelationItem(null, relationID, relationType, relationName, null, isSalesOrder ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE, null));
            quote.setRelations(relations);
        }
        if (relationID != null) {
            quote.setClientContactID(relationID);
        }
        if (productsTable.getOverallDiscount().isEnabled()) {
            quote.setDiscountType(productsTable.getOverallDiscount().getType());
            quote.setDiscountAmount(productsTable.getOverallDiscount().getValue());
        }
        quote.setSalesOrder(isSalesOrder);
        quote.setTypeId(formParameters.getExternalFormID());
        if (isSalesOrder && (invoiceStatus.equals(RECEIVED) || invoiceStatus.equals(PARTIAL_RECEIVED))) {
            if (fullReceived(quote)) {
                quote.setStatusCode(RECEIVED);
            } else {
                quote.setStatusCode(PARTIAL_RECEIVED);
            }
        }
        return quote;
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

                List<Widget> result = new ArrayList<>();

                FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
                informer.addClickHandler(click -> noteHistoryWidget.setLoadData(callback -> {
                    if (objectID == null) {
                        return;
                    }
                    InvoiceService.App.get().loadInvoiceHistoryNote(objectID, isSalesOrder ? SALE_ORDER : SALE_QUOTE, false, callback);
                }));
                informer.setInitialClasses("informer-item history-notes-container");

                FooterInformer introduction = new FooterInformer(SvgEnum.docTitle, wfmStrings.introduction(), introductionPanel.getIntroduction());
                introduction.setInitialClasses("informer-item history-notes-container");

                link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);

                if (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_HISTORY_NOTES)) {
                    result.add(informer);
                }
                if (!Utils.hasRole(CLIENT)) {
                    result.add(introduction);
                }
                if (Utils.hasPermission(isSalesOrder ? ACCOUNTING_SALES_ORDER_UPLOAD_FILES : ACCOUNTING_SALES_QUOTE_UPLOAD_FILES)) {
                    result.add(footerUploadPanel);
                }
                if (objectID != null && Utils.hasPermission(isSalesOrder ? ACCOUNTING_SALES_ORDER_LINKS : ACCOUNTING_SALES_QUOTE_LINKS)) {
                    result.add(link);
                }

                return result;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {

                List<Widget> rightWidgets = new ArrayList<>();

                Div approveButtonWrapper = new Div();
                approveButtonWrapper.add(approveButton);

                Div saveSaleOrderButtonWrapper = new Div();
                saveSaleOrderButtonWrapper.add(saveSaleOrderButton);

                Div submitButtonWrapper = new Div();
                submitButtonWrapper.add(submitToManagerButton);

                rightWidgets.add(saveSaleOrderButtonWrapper);
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SO_AND_SQ_DRAFT)) {
                    Div saveButtonWrapper = new Div();
                    saveButtonWrapper.add(saveButton);
                    rightWidgets.add(saveButtonWrapper);
                }

                if ((isSalesOrder && (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_PDF) || Utils.hasPermission(PermissionConstants.CRM_SALES_ORDER_PDF))) ||
                        (!isSalesOrder && (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_PDF) || Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_PDF)))) {
                    Div pdfButtonWrapper = new Div();
                    pdfButtonWrapper.add(printPdfSplitButton);
                    rightWidgets.add(pdfButtonWrapper);
                }
                if (!isSalesOrder || hasApproval) {
                    rightWidgets.add(submitButtonWrapper);
                }
                rightWidgets.add(approveButtonWrapper);
                return rightWidgets;
            }
        });
    }


    public void setValues(NewInvoice quote) {
        if (quote.getTypeItem() != null) {
            if (!COPY_PO_TO_SQ.equals(formParameters.getExternalFormID())) {
                crmAccountLookUp.addItem(quote.getTypeItem());
                crmAccountWidgets.presenter.initContactAddress(quote.getTypeItem(), !formParameters.isEditForm() && !OPPORTUNITY.equals(formParameters.getCrmFormName()) && !formParameters.isExternalForm(COPY_FROM_EXISTING_DATA) && formParameters.getConvertFormType() == null, Address.EntityType.CrmAccount);
            }
        }
        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            formPresenter.configurePlaceOfSupply(quote.getTypeItem(), quote.getPlaceOfSupply(), placeOfSupplyWidget);
        }
        if (quote.getRelatedProject() != null) {
            projectLookUp.setSelected(quote.getRelatedProject());
        }
        if (formParameters.isEditForm() || formParameters.isExternalForm(COPY_FROM_EXISTING_DATA)) {
            currencyWidget.setCurrency(quote.getCurrencyID(), quote.getExchageRate());
        } else {
            currencyWidget.setCurrency(quote.getCurrencyID());
        }

        if (quote.getReference() != null) {
            referenceTxtBox.setText(quote.getReference());
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_REFERENCE_IN_SUM)) {
                referenceTxtBox.setEnabled(false);
            }
        }
//        if (!DRAFT.equals(quote.getStatusCode()) && quote.isProgressInvoicing()) {
//            taxCalcTypeListBox.setEnabled(false);
//        }
        progressInvoicing.setValue(quote.isProgressInvoicing());
        if (formParameters.isEditForm()) {
            quoteNumberData = quote.getNumberData();
            String dateString = DateTimeFormat.getFormat("yyyyMMdd").format(quote.getInvoiceDate().getNonConvertedDate());
            quoteNumberData.setWithDate(quote.getInvoiceNumber().contains(dateString));
            quoteNumberData.setDate(quoteNumberData.isWithDate() ? dateString : "");
            numberTxtBox.setText(quote.getInvoiceNumber());
        }
        if (quote.getPoNumber() != null) {
            poNumberTxtBox.setText(quote.getPoNumber());
        }

        if (quote.getSupplierID() != null) {
            supplierLookUp.setSelected(new SelectItem(quote.getSupplierID(), quote.getSupplierName()));
        }

        int days = quote.getDueDays() != null ? quote.getDueDays() : 0;

        if (formParameters.isEditForm()) {
            quoteDate.setDate(quote.getInvoiceDate().getNonConvertedDate());
            if (quote.getInvoiceTermsItem() != null) {
                termsAndDuePanel.setData(TermsAndDuePanel.TERMS_TYPE, (quote.getDueDate() != null ? quote.getDueDate().getNonConvertedDate() : null), quote.getInvoiceTermsItem());
            } else {
                termsAndDuePanel.setData(TermsAndDuePanel.DUE_TYPE, (quote.getDueDate() != null ? quote.getDueDate().getNonConvertedDate() : null), null);
            }
        } else if (formParameters.isExternalForm(COPY_FROM_EXISTING_DATA)) {
            quoteDate.setDate(currentDate);
            if (quote.getInvoiceTermsItem() != null) {
                termsAndDuePanel.setData(TERMS_TYPE, (quote.getDueDate() != null ? quote.getDueDate().getNonConvertedDate() : null), quote.getInvoiceTermsItem());
            } else if (quote.getTypeItem() != null && quote.getTypeItem().getTermsItem() != null && quote.getTypeItem().getTermsItem().getDays() != null) {
                termsAndDuePanel.setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(currentDate, quote.getTypeItem().getTermsItem().getDays()), null);
            } else {
                termsAndDuePanel.setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(currentDate, days), null);
            }
        } else {
            quoteDate.setDate(currentDate);
            if (formParameters.getExternalFormID() != null) {
                if (quote.getInvoiceTermsItem() != null || TERMS_TYPE.equals(quote.getDueDateType())) {
                    termsAndDuePanel.setData(TERMS_TYPE, (quote.getDueDate() != null ? quote.getDueDate().getNonConvertedDate() : null), quote.getInvoiceTermsItem());
                } else {
                    termsAndDuePanel.setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(currentDate, days), null);
                }
            } else if (DUE_TYPE.equals(quote.getDueDateType())) {
                termsAndDuePanel.setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(currentDate, days), null);
            } else {
                termsAndDuePanel.setData(TERMS_TYPE, (quote.getDueDate() != null ? quote.getDueDate().getNonConvertedDate() : null), quote.getInvoiceTermsItem());
            }
        }
        if (!COPY_FROM_PRODUCT_LIST.equals(formParameters.getExternalFormID())) {
            if ((customerBalance = quote.getTypeItem().getSupplierCustomerBalance()) >= 0) {
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                customerBalanceLink.setText(AccountingUtils.get().formatPrice(quote.getTypeItem().getSupplierCustomerBalance()));
            } else {
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * quote.getTypeItem().getSupplierCustomerBalance()) + ")");
            }
        }
        applyTaxCalculationTypeChanges(quote.getTaxCalculationType());

        introductionPanel.getIntroduction().setText(quote.getIntroduction());

        productsTable.setExchangeRateValue(quote.getExchageRate());
        productsTable.setValues(quote);

        productsTable.getPaymentInstruction().setText(quote.getPaymentInstruction());


        if (formParameters.isEditForm() || formParameters.isExternalForm(COPY_FROM_EXISTING_DATA)) {
            currencyWidget.setCurrency(quote.getCurrencyID(), quote.getExchageRate());
        } else {
            currencyWidget.setCurrency(quote.getCurrencyID());
        }
    }

    private void applyTaxCalculationTypeChanges(Integer taxCalculationType) {
        taxCalcTypeListBox.setSelected(taxCalculationType != null ? AccountingUtils.getTaxCalcType(taxCalculationType) : defaultTaxCalc != null ? AccountingUtils.getTaxCalcType(defaultTaxCalc) : AccountingUtils.getTaxCalcType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE));
        productsTable.onTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), false);
    }

    private void getRelationName(final Integer relationID, final String relType) {
        allInOneService.getRelationName(relationID, relType, new AsyncCallback<String>() {
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

    private void getDefaultTaxCalcType() {
        allInOneService.getTaxCalcTypeForInvoice(new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Integer integer) {
                defaultTaxCalc = integer;
                taxCalcTypeListBox.setSelected(integer != null ? AccountingUtils.getTaxCalcType(integer) : AccountingUtils.getTaxCalcType(TAX_CALCULATION_EXCLUSIVE));
            }
        });
    }

    private void onChangeLookUp(boolean isCustomerLookUp) {
        lookUpSwitchPanel.clear();
        if (isCustomerLookUp) {
            lookUpSwitchPanel.add(crmAccountLookUp);
            lookUpActionButton.setText(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
            crmAccountLookUp.clear();
        } else {
            lookUpSwitchPanel.add(leadLookUp);
            lookUpActionButton.setText(wfmStrings.lead());
            leadLookUp.clear();
        }
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

    public String propertyCode() {
        if (isSalesOrder) {
            return Constants.SALE_ORDER_CODE;
        }
        return Constants.SALE_QUOTE;
    }


    private HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(SalesQuoteView.this) {
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
                    return isSalesOrder ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE;
                }

                @Override
                public String getRelationName() {
                    return quoteNumberData != null ? quoteNumberData.getInvoiceNumber() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }
}
