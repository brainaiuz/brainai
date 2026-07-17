package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.HelpTextPanel;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.MaterialFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.upload.AttachmentStrategy;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.SettingsLogoBundle;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.InstructionData;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 7/11/15.
 */

public class InvoiceSettingsForm extends CustomForm implements Constants, AccountingConstants, CommandConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    public static final String INSTRUCTION = "INSTRUCTION";

    private final InvoiceServiceAsync invoiceService = InvoiceService.App.get();

    SettingsLogoBundle settingsLogoBundle = GWT.create(SettingsLogoBundle.class);

    private Integer objectId;

    //Invoice Form
    TextBox paymentDue;
    TextBox salesQuotes;
    TextBox purchaseOrders;
    DataListBox companyPdfLocale;
    DataListBox dwInvoiceCustomType;

    //Tax Calculation Type
    DataListBox taxCalculationTypeListBox;

    //Bank Form
    DataListBox bankAccounts;
    PaymentAccountsLookUp defaultPaymentAccounts;

    FlexTable invoicePDFNamingTable;
    FlexTable customerPDFNamingTable;
    FlexTable supplierPDFNamingTable;
    FlexTable salesReceiptPDFNamingTable;
    FlexTable salesOrderPDFNamingTable;
    FlexTable purchaseOrderPDFNamingTable;

    MultiTableNewUI salesInvoiceTermsConditions;
    MultiTableNewUI quoteTermsConditions;
    MultiTableNewUI saleOrderTermsConditions;
    //Purchase Order Terms and Conditions
    MultiTableNewUI purchaseOrderTermsAndConditions;
    //Purchase Invoice Payment Instructions
    MultiTableNewUI purchaseInvoicePaymentInstructions;
    //RFQ Instructions
    MultiTableNewUI rfqInstructions;

    MultiTable introductionSaleInvoice;

    MultiTable introductionSaleQuote;

    MultiTable introductionSaleOrder;

    MultiTable introductionRFQ;

    VerticalPanelDiv quoteTermsConditionsPanel;

    KpiSwitcher salesQuoteTermCopyToSalesInvoice;
    KpiSwitcher salesQuoteTermCopyToSalesOrder;
    KpiCheckBox copySQIntroduction;
    KpiCheckBox copySOIntroduction;
    KpiSwitcher salesOrderTermCopyToSalesInvoice;
    VerticalPanelDiv saleOrderTermsConditionsPanel;
    HorizontalPanel purchaseOrderTermsConditionsPanel;
    HorizontalPanel purchaseInvoicePaymentInstructionsPanel;
    HorizontalPanel rfqInstructionsPanel;

    WfmForm logoForm;

    KpiSwitcher salesQuoteProgressInvoicing;
    KpiSwitcher expandProductGroup;
    KpiSwitcher isConvertInvoiceBtnShow;
    KpiSwitcher isShowPurchaseInvoiceAndPICreditNoteNumbering;
    DataListBox dueDateOption;

    DataListBox salesInvoiceInvoiceType;       // Product Invoice = 0, Service Invoice = 1
    DataListBox convertPurchaseInvoiceDateType;
    SettingsData settingsData;
    String invoiceSettings = "invoice_settings_";

    public InvoiceSettingsForm() {
        super("invoiceSettings", settingsStrings.invoiceSettings());
    }

    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        drawInvoiceForm();
        drawBankAndTaxCalcForm();
        drawPdfNamingForm();
        drawIntroductionForm();
        drawTermsPaymentIntroductionForm();
        drawLogoForm();
        initListeners();
        getDataToFillFields();
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.INVOICE_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.getElement().setId("Invoice_setting_save_button");
        saveButton.addClickHandler(sender -> save());
        addButton(saveButton);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        invoiceService.getInvoiceSettings(new AbstractAsyncCallback<SettingsData>() {
            public void failure(Throwable caught) {
                caught.printStackTrace();
                LoadingPanel.loading(false);
            }

            public void success(SettingsData result) {
                settingsData = result;
                drawDisabledForm();
                fillFormWithData();
                LoadingPanel.loading(false);
            }
        });
    }

    private void drawDisabledForm() {
        if (settingsData.isEnableInvoiceCustomTypes()) {
            addField(CONVERT_SQ_TO, dwInvoiceCustomType, settingsStrings.saleQuoteConvertTo());
        }
    }

    private void fillFormWithData() {
        objectId = settingsData.getObjectID();

        if (settingsData.getPaymentDue() != null) {
            paymentDue.setText(settingsData.getPaymentDue().toString());
        }
        if (settingsData.getSalesQuoteDue() != null) {
            salesQuotes.setText(settingsData.getSalesQuoteDue().toString());
        }
        if (settingsData.getPurchaseOrderDue() != null) {
            purchaseOrders.setText(settingsData.getPurchaseOrderDue().toString());
        }
        if (settingsData.getTaxCalculationType() != null) {
            taxCalculationTypeListBox.setSelected(settingsData.getTaxCalculationType());
        }
        if (settingsData.getSalesQuoteProgressInvoicing() != null) {
            salesQuoteProgressInvoicing.setValue(settingsData.getSalesQuoteProgressInvoicing());
        }
//        if (settingsData.getSalesInvoiceInvoiceType() != null) {
//            salesInvoiceInvoiceType.setSelected(settingsData.getSalesInvoiceInvoiceType());
//        }
        if (isConvertInvoiceBtnShow != null) {
            isConvertInvoiceBtnShow.setValue(settingsData.isConvertInvoiceBtnShow());
        }
        if (isShowPurchaseInvoiceAndPICreditNoteNumbering != null) {
            isShowPurchaseInvoiceAndPICreditNoteNumbering.setValue(settingsData.getIsShowPurchaseInvoiceAndPICreditNoteNumbering());
        }

        if (settingsData.getPaymentDue() != null) {
            paymentDue.setText(settingsData.getPaymentDue().toString());
        }
        if (settingsData.getSalesQuoteDue() != null) {
            salesQuotes.setText(settingsData.getSalesQuoteDue().toString());
        }
        if (settingsData.getPurchaseOrderDue() != null) {
            purchaseOrders.setText(settingsData.getPurchaseOrderDue().toString());
        }
        if (settingsData.getQuoteConvertToInvoiceCustomType() != null) {
            dwInvoiceCustomType.setSelected(settingsData.getQuoteConvertToInvoiceCustomType());
        }
        if (settingsData.getDueDateType() != null) {
            dueDateOption.setSelected(settingsData.getDueDateType());
        }
        if (settingsData.getConvertPurchaseInvoiceDateType() != null) {
            convertPurchaseInvoiceDateType.setSelected(settingsData.getConvertPurchaseInvoiceDateType());
        }

        expandProductGroup.setValue(settingsData.isExpandProductGroup());
        setInvoicePDFNamingFormat(settingsData.getPdfNamingFormat());
        setCustomerSupplierPDFNamingFormat(settingsData.getCustomerPdfNamingFormat(), true);
        setCustomerSupplierPDFNamingFormat(settingsData.getSupplierPdfNamingFormat(), false);

        setSalesReceiptPDFNamingFormat(settingsData.getSalesReceiptPdfNamingFormat());
        setSalesOrderPDFNamingFormat(settingsData.getSalesOrderPdfNamingFormat());
        setPurchaseOrderPDFNamingFormat(settingsData.getPurchaseOrderPdfNamingFormat());

        if (settingsData.getInstructions() != null && settingsData.getInstructions().length > 0) {
            salesInvoiceTermsConditions.removeAllRows();
            for (InstructionData instData : settingsData.getInstructions()) {
                salesInvoiceTermsConditions.addWidgets(getWidgetsMap(instData, Constants.SALE_INVOICE));
            }
        }
        if (settingsData.getQuoteTermsConditions() != null && settingsData.getQuoteTermsConditions().length > 0) {
            quoteTermsConditions.removeAllRows();
            for (InstructionData tcData : settingsData.getQuoteTermsConditions()) {
                quoteTermsConditions.addWidgets(getWidgetsMap(tcData, Constants.SALE_QUOTE));
            }
        }

        if (settingsData.getSaleOrderTermsConditions() != null && settingsData.getSaleOrderTermsConditions().length > 0) {
            saleOrderTermsConditions.removeAllRows();
            for (InstructionData tcData : settingsData.getSaleOrderTermsConditions()) {
                saleOrderTermsConditions.addWidgets(getWidgetsMap(tcData, Constants.SALE_ORDER_CODE));
            }
        }

        //Purchase Order Terms and Conditions
        if (settingsData.getPurchaseOrderTermsAndConditions() != null && settingsData.getPurchaseOrderTermsAndConditions().length > 0) {
            purchaseOrderTermsAndConditions.removeAllRows();
            for (InstructionData purOrTermsAndCon : settingsData.getPurchaseOrderTermsAndConditions()) {
                purchaseOrderTermsAndConditions.addWidgets(getWidgetsMap(purOrTermsAndCon, Constants.PURCHASE_ORDER));
            }
        }
        salesQuoteTermCopyToSalesInvoice.setValue(settingsData.isSalesQuoteTermCopyToSalesInvoice());
        salesQuoteTermCopyToSalesOrder.setValue(settingsData.isSalesQuoteTermCopyToSalesOrder());
        copySQIntroduction.setValue(settingsData.isCopySQIntroduction());
        copySOIntroduction.setValue(settingsData.isCopySOIntroduction());
        salesOrderTermCopyToSalesInvoice.setValue(settingsData.isSalesOrderTermCopyToSalesInvoice());
        //Purchase Invoice Payment Instructions
        if (settingsData.getPurchaseInvoicePaymentInstructions() != null && settingsData.getPurchaseInvoicePaymentInstructions().length > 0) {
            purchaseInvoicePaymentInstructions.removeAllRows();
            for (InstructionData purInvPayInst : settingsData.getPurchaseInvoicePaymentInstructions()) {
                purchaseInvoicePaymentInstructions.addWidgets(getWidgetsMap(purInvPayInst, Constants.PURCHASE_INVOICE));
            }
        }
        //Rfq Instructions
        if (settingsData.getRfqInstructions() != null && settingsData.getRfqInstructions().length > 0) {
            rfqInstructions.removeAllRows();
            for (InstructionData rfqInst : settingsData.getRfqInstructions()) {
                rfqInstructions.addWidgets(getWidgetsMap(rfqInst, Constants.REQUEST_FOR_QUOTE));
            }
        }
        //Sales Invoice Introduction
        if (settingsData.getSaleInvoiceIntroduction() != null && settingsData.getSaleInvoiceIntroduction().length > 0) {
            introductionSaleInvoice.removeAllRows();
            introductionSaleInvoice.setStyleName("hasMultiTable");
            for (InstructionData saleInvIntr : settingsData.getSaleInvoiceIntroduction()) {
                introductionSaleInvoice.addWidgets(getWidgetsMap(saleInvIntr, Constants.SALE_INVOICE_INTR));
            }
        }
        //Sales Quote Introduction
        if (settingsData.getSaleQuoteIntroduction() != null && settingsData.getSaleQuoteIntroduction().length > 0) {
            introductionSaleQuote.removeAllRows();
            for (InstructionData saleQt : settingsData.getSaleQuoteIntroduction()) {
                introductionSaleQuote.addWidgets(getWidgetsMap(saleQt, Constants.SALE_QUOTE));
            }
        }
        //Sales Order Introduction
        if (settingsData.getSaleOrderIntroduction() != null && settingsData.getSaleOrderIntroduction().length > 0) {
            introductionSaleOrder.removeAllRows();
            for (InstructionData saleOrdr : settingsData.getSaleOrderIntroduction()) {
                introductionSaleOrder.addWidgets(getWidgetsMap(saleOrdr, Constants.SALE_ORDER_CODE));
            }
        }
        if (settingsData.getRequestForQuoteIntroduction() != null && settingsData.getRequestForQuoteIntroduction().length > 0) {
            introductionRFQ.removeAllRows();
            for (InstructionData rfq : settingsData.getRequestForQuoteIntroduction()) {
                introductionRFQ.addWidgets(getWidgetsMap(rfq, Constants.REQUEST_FOR_QUOTE_INTR));
            }
        }

        if (settingsData.getBankAccounts() != null && settingsData.getBankAccounts().length > 0) {
            bankAccounts.setItems(settingsData.getBankAccounts());
            if (settingsData.getBankID() != null && bankAccounts.getItemsById() != null && bankAccounts.getItemsById().containsKey(settingsData.getBankID())) {
                bankAccounts.setSelected(settingsData.getBankID());
            }
        }

        if (settingsData.getDefaultPaymentAccount() != null) {
            defaultPaymentAccounts.setSelected(settingsData.getDefaultPaymentAccount());
        }

        if (settingsData.getCompanyPdfLocaleItems() != null && settingsData.getCompanyPdfLocaleItems().length > 0) {
            companyPdfLocale.setItems(settingsData.getCompanyPdfLocaleItems());
            if (settingsData != null && settingsData.getLocaleID() != null) {
                companyPdfLocale.setSelected(settingsData.getLocaleID());
            }
        }
        if (settingsData.getInvoiceCustomTypes() != null && settingsData.getInvoiceCustomTypes().length > 0) {
            dwInvoiceCustomType.setItems(settingsData.getInvoiceCustomTypes());
            if (settingsData.getQuoteConvertToInvoiceCustomType() != null) {
                dwInvoiceCustomType.setSelected(settingsData.getQuoteConvertToInvoiceCustomType());
            }
        }
    }

    private void drawInvoiceForm() {
        paymentDue = new TextBox();
        paymentDue.ensureDebugId(invoiceSettings + "paymentDue");

        salesQuotes = new TextBox();
        salesQuotes.ensureDebugId(invoiceSettings + "salesQuotes");

        purchaseOrders = new TextBox();
        purchaseOrders.ensureDebugId(invoiceSettings + "purchaseOrders");

        companyPdfLocale = new DataListBox();
        companyPdfLocale.ensureDebugId(invoiceSettings + "companyPdfLocale");
        companyPdfLocale.setNullLabel("English");//settingsStrings.defaultSaleQuoteLocale());

        salesQuoteProgressInvoicing = new KpiSwitcher();
        salesQuoteProgressInvoicing.ensureDebugId("progress-invoicing-checkBox");
        expandProductGroup = new KpiSwitcher();
        expandProductGroup.ensureDebugId("expandProduct-groupcheckBox");

//        salesInvoiceInvoiceType = new DataListBox();
//        salesInvoiceInvoiceType.ensureDebugId("invooiceType-listBox");
//        salesInvoiceInvoiceType.setWithoutNullLabel(true);
//        salesInvoiceInvoiceType.setItems(new SelectItem[]{
//                new SelectItem(PRODUCT_INVOICE_TYPE, accountingStrings.productInvoice()),
//                new SelectItem(SERVICE_INVOICE_TYPE, accountingStrings.serviceInvoice())
//        });

        dwInvoiceCustomType = new DataListBox();
        dwInvoiceCustomType.ensureDebugId(invoiceSettings + "dwInvoiceCustomType");
        dwInvoiceCustomType.setWithoutNullLabel(true);
        dwInvoiceCustomType.setAllowFirstItem(false);

        isConvertInvoiceBtnShow = new KpiSwitcher();
        isShowPurchaseInvoiceAndPICreditNoteNumbering = new KpiSwitcher();
        isShowPurchaseInvoiceAndPICreditNoteNumbering.ensureDebugId("purchaseInvoice-creditNote-checkBox");


        dueDateOption = new DataListBox();
        dueDateOption.ensureDebugId(invoiceSettings + "dueDateOption");
//        dueDateOption.addStyleName(DEFAULT_WIDTH);
        dueDateOption.setStyleName("form-control file--InvoiceSettingsForm");
        dueDateOption.setWithoutNullLabel(true);
        dueDateOption.setItems(new SelectItem[]{
                new SelectItem(DUE_TYPE, wfmStrings.dueDate()),
                new SelectItem(TERMS_TYPE, wfmStrings.terms())
        });
        dueDateOption.setSelected(DUE_TYPE);

        convertPurchaseInvoiceDateType = new DataListBox();
        convertPurchaseInvoiceDateType.ensureDebugId(invoiceSettings + "convertPurchaseInvoiceDateType");
        convertPurchaseInvoiceDateType.addStyleName(DEFAULT_WIDTH);
        convertPurchaseInvoiceDateType.setWithoutNullLabel(true);
        convertPurchaseInvoiceDateType.setItems(new SelectItem[]{
                new SelectItem(PO_DATE_TYPE, accountingStrings.purchaseOrderDate()),
                new SelectItem(RECEIVE_DATE_TYPE, accountingStrings.receiveDate())
        });
        convertPurchaseInvoiceDateType.setSelected(PO_DATE_TYPE);

        addTitleField(INVOICE_DETAILS, wfmStrings.invoiceDetails());
        addField(PAYMENT_DUE, new AdvancedInputGroup(paymentDue, new Span(wfmStrings.days())), settingsStrings.paymentDueOrCreditTerms());
        addField(SQ_VALID, new AdvancedInputGroup(salesQuotes, new Span(wfmStrings.days())), settingsStrings.salesQuotesValidfor());
        addField(PO_VALID, new AdvancedInputGroup(purchaseOrders, new Span(wfmStrings.days())), settingsStrings.purchaseOrdersValidfor());
        addField(PDF_EXCEL_LOCALIZATION, companyPdfLocale, settingsStrings.pdfExcelLocalization());
        addField(CustomFormConstants.PROGRESS_INVOICING, salesQuoteProgressInvoicing, wfmStrings.progressInvoicing());
        addField(CustomFormConstants.EXPAND_PRODUCT_GROUP, expandProductGroup, settingsStrings.expandProductGroup());
//        addField(INVOICE_TYPE, salesInvoiceInvoiceType, settingsStrings.salesInvoiceInvoiceType());

        if (Utils.hasRole(ADMIN) || Utils.hasRole(DR) || Utils.hasRole(ACCOUNTANT)) {
            String convertToSQ = Property.get(Constants.SALE_QUOTE, wfmStrings.convertToo(), wfmStrings.salesQuote());
            String toSI = Property.get(Constants.SALE_INVOICE, accountingStrings.toSalesInvoice(), wfmStrings.salesInvoice());
            addField(CONVERT_SQ_TO_SI, isConvertInvoiceBtnShow, convertToSQ + " " + toSI);
            addField(PI_AND_CN_NUMBERING, isShowPurchaseInvoiceAndPICreditNoteNumbering, settingsStrings.purchaseInvoiceAndCreditNoteNumbering());
        }
        addField(CustomFormConstants.DUE_DATE_OPTIONS, dueDateOption, settingsStrings.defaultDueDateOption());
        addField(CONVERT_PURCHASE_INVOICE_DATE_TYPE, convertPurchaseInvoiceDateType, settingsStrings.purchaseInvoiceDate());

    }

    private void drawBankAndTaxCalcForm() {
        taxCalculationTypeListBox = new DataListBox();
        taxCalculationTypeListBox.ensureDebugId(invoiceSettings + "taxCalculationType");
        taxCalculationTypeListBox.addStyleName(DEFAULT_WIDTH);
        taxCalculationTypeListBox.setWithoutNullLabel(true);
        taxCalculationTypeListBox.setAllowFirstItem(false);
        taxCalculationTypeListBox.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.noTax()),//NO TAX
                new SelectItem(1, wfmStrings.taxInclusive()),//TAX INCLUSIVE
                new SelectItem(2, wfmStrings.taxExclusive())});//TAX EXCLUSIVE
        taxCalculationTypeListBox.setSelected(2);

        bankAccounts = new DataListBox();
        bankAccounts.ensureDebugId(invoiceSettings + "bankAccounts");
        bankAccounts.addStyleName(DEFAULT_WIDTH);
        bankAccounts.setAllowFirstItem(true);

        defaultPaymentAccounts = new PaymentAccountsLookUp(true);
        defaultPaymentAccounts.ensureDebugId(invoiceSettings + "defaultPaymentAccounts");
        defaultPaymentAccounts.addStyleName(DEFAULT_WIDTH);

        addTitleField(BANK_ACCOUNT_INFORMATION, Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountInformation(), wfmStrings.bankAccount()));
        addField(ACCOUNTS, taxCalculationTypeListBox, accountingStrings.amounts());
        addField(BANK_ACCOUNT, bankAccounts, Property.getPluralWithObjectCode(Constants.BANKACCOUNT, wfmStrings.bankAccounts()));
        addField(DEFAULT_PAYMENT_ACCOUNT, defaultPaymentAccounts, accountingStrings.paymentAccount());
    }

    private void drawPdfNamingForm() {
        // INVOICE PDF NAMING
        HTML prefixTitle = new HTML(wfmStrings.prefix());
        prefixTitle.ensureDebugId(invoiceSettings + "prefixTitle");

        TextBox prefix = new TextBox();
        prefix.ensureDebugId(invoiceSettings + "prefix");
        prefix.setWidth("190px");

        KpiCheckBox type = new KpiCheckBox(wfmStrings.type());
        type.ensureDebugId(invoiceSettings + "type");

        KpiCheckBox companyName = new KpiCheckBox(wfmStrings.companyName());
        companyName.ensureDebugId(invoiceSettings + "companyName");

        KpiCheckBox client = new KpiCheckBox(accountingStrings.clientSupplier());
        client.ensureDebugId(invoiceSettings + "clientSupplier");

        KpiCheckBox clientCode = new KpiCheckBox(accountingStrings.clientSupplierCode());
        clientCode.ensureDebugId(invoiceSettings + "clientSupplierCode");

        KpiCheckBox number = new KpiCheckBox(wfmStrings.number());
        number.ensureDebugId(invoiceSettings + "number");

        KpiCheckBox date = new KpiCheckBox(wfmStrings.date());
        date.ensureDebugId(invoiceSettings + "date");

        KpiCheckBox userName = new KpiCheckBox(wfmStrings.username());
        userName.ensureDebugId(invoiceSettings + "clientUserName");

        invoicePDFNamingTable = new FlexTable();
        invoicePDFNamingTable.setStyleName("mod_table--auto mod_table--cellpadding-right mod-table--aligned");
        invoicePDFNamingTable.setWidget(0, 0, prefix);
        invoicePDFNamingTable.setWidget(0, 1, prefixTitle);
        invoicePDFNamingTable.setWidget(0, 2, companyName);
        invoicePDFNamingTable.setWidget(0, 3, userName);
        invoicePDFNamingTable.setWidget(0, 4, number);
        invoicePDFNamingTable.setWidget(0, 5, client);
        invoicePDFNamingTable.setWidget(0, 6, date);
        invoicePDFNamingTable.setWidget(0, 7, type);

        addTitleField(PDF_NAMING, settingsStrings.invoicePdfNaming());
        addField(INVOICE_PDF_NAMING_TABLE, invoicePDFNamingTable, settingsStrings.invoicePdfNaming());

        //SALES RECEIPT PDF NAMING
        HTML receiptPrefixTitle = new HTML(wfmStrings.prefix());
        receiptPrefixTitle.ensureDebugId(invoiceSettings + "receiptPrefixTitle");

        TextBox receiptPrefix = new TextBox();
        receiptPrefix.ensureDebugId(invoiceSettings + "receiptPrefix");
        receiptPrefix.setWidth("190px");

        KpiCheckBox receiptType = new KpiCheckBox(wfmStrings.type());
        receiptType.ensureDebugId(invoiceSettings + "receiptType");

        KpiCheckBox receiptCompanyName = new KpiCheckBox(wfmStrings.companyName());
        receiptCompanyName.ensureDebugId(invoiceSettings + "receiptCompanyName");

        KpiCheckBox receiptClient = new KpiCheckBox(accountingStrings.clientSupplier());
        receiptClient.ensureDebugId(invoiceSettings + "receiptClient");

        KpiCheckBox receiptClientCode = new KpiCheckBox(accountingStrings.clientSupplierCode());
        receiptClientCode.ensureDebugId(invoiceSettings + "receiptClientCode");

        KpiCheckBox receiptNumber = new KpiCheckBox(wfmStrings.number());
        receiptNumber.ensureDebugId(invoiceSettings + "receiptNumber");

        KpiCheckBox receiptDate = new KpiCheckBox(wfmStrings.date());
        receiptDate.ensureDebugId(invoiceSettings + "receiptDate");

        KpiCheckBox receiptUserName = new KpiCheckBox(wfmStrings.username());
        receiptUserName.ensureDebugId(invoiceSettings + "receiptUserName");

        salesReceiptPDFNamingTable = new FlexTable();
        salesReceiptPDFNamingTable.setStyleName("mod_table--auto mod_table--cellpadding-right mod-table--aligned");
        salesReceiptPDFNamingTable.setWidget(0, 0, receiptPrefix);
        salesReceiptPDFNamingTable.setWidget(0, 1, receiptPrefixTitle);
        salesReceiptPDFNamingTable.setWidget(0, 2, receiptCompanyName);
        salesReceiptPDFNamingTable.setWidget(0, 3, receiptUserName);
        salesReceiptPDFNamingTable.setWidget(0, 4, receiptNumber);
        salesReceiptPDFNamingTable.setWidget(0, 5, receiptClient);
        salesReceiptPDFNamingTable.setWidget(0, 6, receiptDate);
        salesReceiptPDFNamingTable.setWidget(0, 7, receiptType);
        salesReceiptPDFNamingTable.setCellSpacing(10);

        addField(SALES_RECEIPT_PDF_NAMING_TABLE, salesReceiptPDFNamingTable, accountingStrings.salesReceipt() + " " + settingsStrings.pdfNaming());

        //SALES ORDER PDF NAMING
        HTML salesOrderPrefixTitle = new HTML(wfmStrings.prefix());
        salesOrderPrefixTitle.ensureDebugId(invoiceSettings + "salesOrderPrefixTitle");

        TextBox salesOrderPrefix = new TextBox();
        salesOrderPrefix.ensureDebugId(invoiceSettings + "salesOrderPrefix");
        salesOrderPrefix.setWidth("190px");

        KpiCheckBox salesOrderType = new KpiCheckBox(wfmStrings.type());
        salesOrderType.ensureDebugId(invoiceSettings + "salesOrderType");

        KpiCheckBox salesOrderCompanyName = new KpiCheckBox(wfmStrings.companyName());
        salesOrderCompanyName.ensureDebugId(invoiceSettings + "salesOrderCompanyName");

        KpiCheckBox salesOrderClient = new KpiCheckBox(accountingStrings.clientSupplier());
        salesOrderClient.ensureDebugId(invoiceSettings + "salesOrderClient");

        KpiCheckBox salesOrderClientCode = new KpiCheckBox(accountingStrings.clientSupplierCode());
        salesOrderClientCode.ensureDebugId(invoiceSettings + "salesOrderClientCode");

        KpiCheckBox salesOrderNumber = new KpiCheckBox(wfmStrings.number());
        salesOrderNumber.ensureDebugId(invoiceSettings + "salesOrderNumber");

        KpiCheckBox salesOrderDate = new KpiCheckBox(wfmStrings.date());
        salesOrderDate.ensureDebugId(invoiceSettings + "salesOrderDate");

        KpiCheckBox salesOrderUserName = new KpiCheckBox(wfmStrings.username());
        salesOrderUserName.ensureDebugId(invoiceSettings + "salesOrderUserName");

        salesOrderPDFNamingTable = new FlexTable();
        salesOrderPDFNamingTable.setStyleName("mod_table--auto mod_table--cellpadding-right mod-table--aligned");

        salesOrderPDFNamingTable.setWidget(0, 0, salesOrderPrefix);
        salesOrderPDFNamingTable.setWidget(0, 1, salesOrderPrefixTitle);
        salesOrderPDFNamingTable.setWidget(0, 2, salesOrderCompanyName);
        salesOrderPDFNamingTable.setWidget(0, 3, salesOrderUserName);
        salesOrderPDFNamingTable.setWidget(0, 4, salesOrderNumber);
        salesOrderPDFNamingTable.setWidget(0, 5, salesOrderClient);
        salesOrderPDFNamingTable.setWidget(0, 6, salesOrderDate);
        salesOrderPDFNamingTable.setWidget(0, 7, salesOrderType);
        salesOrderPDFNamingTable.setCellSpacing(10);

        addField(SALES_ORDER_PDF_NAMING_TABLE, salesOrderPDFNamingTable, accountingStrings.salesOrder() + " " + settingsStrings.pdfNaming());

        //PURCHASE ORDER PDF NAMING
        HTML purchaseOrderPrefixTitle = new HTML(wfmStrings.prefix());
        purchaseOrderPrefixTitle.ensureDebugId(invoiceSettings + "purchaseOrderPrefixTitle");

        TextBox purchaseOrderPrefix = new TextBox();
        purchaseOrderPrefix.setWidth("190px");
        purchaseOrderPrefix.ensureDebugId(invoiceSettings + "purchaseOrderPrefix");

        KpiCheckBox purchaseOrderType = new KpiCheckBox(wfmStrings.type());
        purchaseOrderType.ensureDebugId(invoiceSettings + "purchaseOrderType");

        KpiCheckBox purchaseOrderCompanyName = new KpiCheckBox(wfmStrings.companyName());
        purchaseOrderCompanyName.ensureDebugId(invoiceSettings + "purchaseOrderCompanyName");

        KpiCheckBox purchaseOrderClient = new KpiCheckBox(accountingStrings.clientSupplier());
        purchaseOrderClient.ensureDebugId(invoiceSettings + "purchaseOrderClient");

        KpiCheckBox purchaseOrderClientCode = new KpiCheckBox(accountingStrings.clientSupplierCode());
        purchaseOrderClientCode.ensureDebugId(invoiceSettings + "purchaseOrderClientCode");

        KpiCheckBox purchaseOrderNumber = new KpiCheckBox(wfmStrings.number());
        purchaseOrderNumber.ensureDebugId(invoiceSettings + "receiptNumber");

        KpiCheckBox purchaseOrderDate = new KpiCheckBox(wfmStrings.date());
        purchaseOrderDate.ensureDebugId(invoiceSettings + "purchaseOrderDate");

        KpiCheckBox purchaseOrderUserName = new KpiCheckBox(wfmStrings.username());
        purchaseOrderUserName.ensureDebugId(invoiceSettings + "purchaseOrderUserName");

        purchaseOrderPDFNamingTable = new FlexTable();
        purchaseOrderPDFNamingTable.setStyleName("mod_table--auto mod_table--cellpadding-right mod-table--aligned");
        purchaseOrderPDFNamingTable.setWidget(0, 0, purchaseOrderPrefix);
        purchaseOrderPDFNamingTable.setWidget(0, 1, purchaseOrderPrefixTitle);
        purchaseOrderPDFNamingTable.setWidget(0, 2, purchaseOrderCompanyName);
        purchaseOrderPDFNamingTable.setWidget(0, 3, purchaseOrderUserName);
        purchaseOrderPDFNamingTable.setWidget(0, 4, purchaseOrderNumber);
        purchaseOrderPDFNamingTable.setWidget(0, 5, purchaseOrderClient);
        purchaseOrderPDFNamingTable.setWidget(0, 6, purchaseOrderDate);
        purchaseOrderPDFNamingTable.setWidget(0, 7, purchaseOrderType);
        purchaseOrderPDFNamingTable.setCellSpacing(10);

        addField(PURCHASE_ORDER_PDF_NAMING_TABLE, purchaseOrderPDFNamingTable, wfmStrings.purchaseorder() + " " + settingsStrings.pdfNaming());

        // CUSTOMER PDF NAMING
        HTML customerPrefixTitle = new HTML(wfmStrings.prefix());
        customerPrefixTitle.ensureDebugId(invoiceSettings + "customerPrefixTitle");

        TextBox customerPrefix = new TextBox();
        customerPrefix.ensureDebugId(invoiceSettings + "customerPrefix");
        customerPrefix.setWidth("190px");

        KpiCheckBox customerCompanyName = new KpiCheckBox(wfmStrings.companyName());
        customerCompanyName.ensureDebugId(invoiceSettings + "customerCompanyName");

        KpiCheckBox customerCode = new KpiCheckBox(wfmStrings.customerCode());
        customerCode.ensureDebugId(invoiceSettings + "customerCode");

        KpiCheckBox customerNumber = new KpiCheckBox(wfmStrings.number());
        customerNumber.ensureDebugId(invoiceSettings + "customerNumber");

        KpiCheckBox customerDate = new KpiCheckBox(wfmStrings.generatedDate());
        customerDate.ensureDebugId(invoiceSettings + "customerDate");

        KpiCheckBox customerUserName = new KpiCheckBox(wfmStrings.username());
        userName.ensureDebugId(invoiceSettings + "customerUserName");

        customerPDFNamingTable = new FlexTable();
        customerPDFNamingTable.setStyleName("mod_table--auto mod_table--cellpadding-right mod-table--aligned");
        customerPDFNamingTable.setWidget(0, 0, customerPrefix);
        customerPDFNamingTable.setWidget(0, 1, customerPrefixTitle);
        customerPDFNamingTable.setWidget(0, 2, customerCompanyName);
        customerPDFNamingTable.setWidget(0, 3, customerUserName);
        customerPDFNamingTable.setWidget(0, 4, customerNumber);
        customerPDFNamingTable.setWidget(0, 5, customerDate);
        customerPDFNamingTable.setWidget(0, 6, customerCode);

        addField(CUSTOMER_PDF_NAMING_TABLE, customerPDFNamingTable, settingsStrings.clientPdfNaming());

        // SUPPLIER PDF NAMING
        HTML supplierPrefixTitle = new HTML(wfmStrings.prefix());
        supplierPrefixTitle.ensureDebugId(invoiceSettings + "supplierPrefixTitle");

        TextBox supplierPrefix = new TextBox();
        supplierPrefix.ensureDebugId(invoiceSettings + "supplierPrefix");
        supplierPrefix.setWidth("190px");

        KpiCheckBox supplierCompanyName = new KpiCheckBox(wfmStrings.companyName());
        supplierCompanyName.ensureDebugId(invoiceSettings + "supplierCompanyName");

        KpiCheckBox supplierCode = new KpiCheckBox(settingsStrings.supllierPdfNaming());
        supplierCode.ensureDebugId(invoiceSettings + "supplierCode");

        KpiCheckBox supplierNumber = new KpiCheckBox(wfmStrings.number());
        supplierNumber.ensureDebugId(invoiceSettings + "supplierNumber");

        KpiCheckBox supplierDate = new KpiCheckBox(wfmStrings.generatedDate());
        supplierDate.ensureDebugId(invoiceSettings + "supplierDate");

        KpiCheckBox supplierUserName = new KpiCheckBox(wfmStrings.username());
        supplierUserName.ensureDebugId(invoiceSettings + "supplierUserName");

        supplierPDFNamingTable = new FlexTable();
        supplierPDFNamingTable.setStyleName("mod_table--auto mod_table--cellpadding-right mod-table--aligned");
        supplierPDFNamingTable.setWidget(0, 0, supplierPrefix);
        supplierPDFNamingTable.setWidget(0, 1, supplierPrefixTitle);
        supplierPDFNamingTable.setWidget(0, 2, supplierCompanyName);
        supplierPDFNamingTable.setWidget(0, 3, supplierUserName);
        supplierPDFNamingTable.setWidget(0, 4, supplierNumber);
        supplierPDFNamingTable.setWidget(0, 5, supplierDate);
        supplierPDFNamingTable.setWidget(0, 6, supplierCode);
        supplierPDFNamingTable.setCellSpacing(10);

        addField(SUPPLIER_PDF_NAMING_TABLE, supplierPDFNamingTable, settingsStrings.supllierPdfNaming());
    }

    private void drawIntroductionForm() {
        introductionSaleInvoice = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.SALE_INVOICE_INTR);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });

        introductionSaleQuote = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.SALE_QUOTE);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });

        introductionSaleOrder = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.SALE_ORDER_CODE);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });

        introductionRFQ = new MultiTable(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.REQUEST_FOR_QUOTE_INTR);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });

        HorizontalPanel SIIntroductionPanel = new HorizontalPanel();
        SIIntroductionPanel.add(introductionSaleInvoice);

        HorizontalPanel SQIntroductionPanel = new HorizontalPanel();
        copySQIntroduction = new KpiCheckBox();
        copySQIntroduction.ensureDebugId("salesQuoteIntroduction-checkBox");
        SQIntroductionPanel.add(introductionSaleQuote);
        SQIntroductionPanel.add(copySQIntroduction);
        SQIntroductionPanel.getElement().setClassName("check-delete-table");

        HorizontalPanel SOIntroductionPanel = new HorizontalPanel();
        copySOIntroduction = new KpiCheckBox();
        copySOIntroduction.ensureDebugId("salesOrder-introduction-checkBox");
        SOIntroductionPanel.add(introductionSaleOrder);
        SOIntroductionPanel.add(copySOIntroduction);
        SOIntroductionPanel.getElement().setClassName("check-delete-table");

        HorizontalPanel RfQIntroductionPanel = new HorizontalPanel();
        RfQIntroductionPanel.add(introductionRFQ);

        addTitleField(CustomFormConstants.INTRODUCTION, wfmStrings.introduction());
        addField(SI_INTRODUCTION, SIIntroductionPanel, settingsStrings.salesInvoiceIntroduction());
        addField(SQ_INTRODUCTION, SQIntroductionPanel, settingsStrings.salesQuoteIntroduction());
        addField(SO_INTRODUCTION, SOIntroductionPanel, settingsStrings.salesOrderIntroduction());
        addField(RFQ_INTRODUCTION, RfQIntroductionPanel, settingsStrings.requestForQuoteIntroduction());
    }

    private void drawTermsPaymentIntroductionForm() {

        salesInvoiceTermsConditions = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.SALE_INVOICE);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, false);
        quoteTermsConditions = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.SALE_QUOTE);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, false);

        saleOrderTermsConditions = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.SALE_ORDER_CODE);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, false);

        purchaseOrderTermsAndConditions = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.PURCHASE_ORDER);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, false);

        purchaseInvoicePaymentInstructions = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.PURCHASE_INVOICE);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, false);
        rfqInstructions = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgetsMap(null, Constants.REQUEST_FOR_QUOTE);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, false);

        quoteTermsConditionsPanel = new VerticalPanelDiv();
        quoteTermsConditionsPanel.addStyleName("quote-terms-switchers");

        salesQuoteTermCopyToSalesInvoice = new KpiSwitcher();
        salesQuoteTermCopyToSalesInvoice.setOffLabel(accountingStrings.copyToSale());
        salesQuoteTermCopyToSalesInvoice.ensureDebugId("salesQuoteTermsAnd-conditions-ChBox1");

        salesQuoteTermCopyToSalesOrder = new KpiSwitcher();
        salesQuoteTermCopyToSalesOrder.setOffLabel(accountingStrings.copyTermsToOrder());
        salesQuoteTermCopyToSalesOrder.ensureDebugId("salesQuoteTermsAnd-conditions-ChBox2");


//        GColumn qCopyToInvColumn = new GColumn(GColumnEnum.COL_4, new FormGroup(salesQuoteTermCopyToSalesInvoice));
//        GColumn qCopyToSOColumn = new GColumn(GColumnEnum.COL_4, new FormGroup(salesQuoteTermCopyToSalesOrder));

        Div qCopyToInvColumn = new Div("col-4");
        qCopyToInvColumn.add(salesQuoteTermCopyToSalesInvoice);

        Div qCopyToSOColumn = new Div("col-4");
        qCopyToSOColumn.add(salesQuoteTermCopyToSalesOrder);

        Div quoteTermsConditionsCheckboxes = new Div("grid-row");
        quoteTermsConditionsCheckboxes.add(qCopyToInvColumn);
        quoteTermsConditionsCheckboxes.add(qCopyToSOColumn);

        Div quoteTermsConditionsCheckboxesWrapper = new Div("mb-3");
        quoteTermsConditionsCheckboxesWrapper.add(quoteTermsConditionsCheckboxes);

        quoteTermsConditionsPanel.add(quoteTermsConditionsCheckboxesWrapper);
        quoteTermsConditionsPanel.add(quoteTermsConditions);
//        quoteTermsConditionsPanel.add(salesQuoteTermCopyToSalesInvoice);
//        quoteTermsConditionsPanel.add(salesQuoteTermCopyToSalesOrder);

        salesOrderTermCopyToSalesInvoice = new KpiSwitcher();
        salesOrderTermCopyToSalesInvoice.setOffLabel(accountingStrings.copyToSale());
        salesOrderTermCopyToSalesInvoice.ensureDebugId("salesOrderTermsAnd-conditions-ChBox2");
        salesOrderTermCopyToSalesInvoice.addStyleName("mb-3");

        saleOrderTermsConditionsPanel = new VerticalPanelDiv();
        saleOrderTermsConditionsPanel.add(salesOrderTermCopyToSalesInvoice);
        saleOrderTermsConditionsPanel.add(saleOrderTermsConditions);

        purchaseOrderTermsConditionsPanel = new HorizontalPanel();
        purchaseOrderTermsConditionsPanel.add(purchaseOrderTermsAndConditions);
        purchaseInvoicePaymentInstructionsPanel = new HorizontalPanel();
        purchaseInvoicePaymentInstructionsPanel.add(purchaseInvoicePaymentInstructions);
        rfqInstructionsPanel = new HorizontalPanel();
        rfqInstructionsPanel.add(rfqInstructions);

        addTitleField(CustomFormConstants.TERMS_PAYMENT_INTRODUCTION, settingsStrings.termsAndPaymentInstructions());
        addField(SI_TERMS_INTRODUCTION, salesInvoiceTermsConditions, settingsStrings.salesInvoicePaymentInstruction());
        addField(SQ_TERMS_INTRODUCTION, quoteTermsConditionsPanel, settingsStrings.salesQuoteTermsConditions());
        addField(SO_TERMS_INTRODUCTION, saleOrderTermsConditionsPanel, settingsStrings.salesOrderTermsConditions());
        addField(PO_TERMS_INTRODUCTION, purchaseOrderTermsConditionsPanel, settingsStrings.purchaseOrderTermsAndConditions());
        addField(PI_TERMS_INTRODUCTION, purchaseInvoicePaymentInstructionsPanel, settingsStrings.purchaseInvoicePaymentInstructions());
        addField(RFQ_TERMS_INTRODUCTION, rfqInstructionsPanel, settingsStrings.rfqInstructions());

    }

    static int sch = 0;

    private WidgetsMap getWidgetsMap(InstructionData data, String type) {
        WidgetsMap widgetsMap = new WidgetsMap();

        final ExtendedTextArea instruction = new ExtendedTextArea();
        instruction.setWidth("100%");
        instruction.ensureDebugId(invoiceSettings + type + (sch++));

        if (data != null) {
            instruction.setInstructionID(data.getObjectID());
            instruction.setText(data.getText());
        } else if (Constants.SALE_INVOICE.equals(type)) {
            instruction.setText(PaymentTermsConditionsUtil.getSettingsTemplate(SALE_INVOICE));
        }

        widgetsMap.addWidgetToMap(INSTRUCTION, instruction);
        widgetsMap.addWidgets(instruction);
        return widgetsMap;
    }

    public class ExtendedTextArea extends TextArea {
        private Integer instructionID;

        public Integer getInstructionID() {
            return instructionID;
        }

        public void setInstructionID(Integer instructionID) {
            this.instructionID = instructionID;
        }
    }

    private String getInvoicePDFNamingFormat() {
        TextBox prefix = (TextBox) invoicePDFNamingTable.getWidget(0, 0);
        KpiCheckBox companyName = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 2);
        KpiCheckBox userName = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 3);
        KpiCheckBox number = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 4);
        KpiCheckBox client = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 5);
//        KpiCheckBox clientCode = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 5);
        KpiCheckBox date = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 6);
        KpiCheckBox type = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 7);

        StringBuffer s = new StringBuffer();
        addNamingFormat(prefix, PDF_PREFIX, s);
        addNamingFormat(type, PDF_TYPE, s);
        addNamingFormat(companyName, PDF_COMPANY_NAME, s);
        addNamingFormat(client, PDF_CLIENT, s);
//        addNamingFormat(clientCode, PDF_CLIENT_CODE, s);
        addNamingFormat(number, PDF_NUMBER, s);
        addNamingFormat(date, PDF_GENERATED_DATE, s);
        addNamingFormat(userName, PDF_USER_NAME, s);
        return s.toString();
    }

    private void addNamingFormat(Widget widget, String code, StringBuffer s) {
        if (widget instanceof KpiCheckBox) {
            if (((KpiCheckBox) widget).getValue()) {
                s.append(s.length() > 0 ? "_" : "");
                s.append(code);
            }
        } else if (widget instanceof TextBox) {
            if (((TextBox) widget).getValue() != null && !"".equals(((TextBox) widget).getValue())) {
                s.append(s.length() > 0 ? "_" : "");
                s.append(code);
            }
        }
    }

    private void setInvoicePDFNamingFormat(String format) {
        if (format != null) {
            TextBox prefix = (TextBox) invoicePDFNamingTable.getWidget(0, 0);
            KpiCheckBox companyName = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 2);
            KpiCheckBox userName = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 3);
            KpiCheckBox number = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 4);
            KpiCheckBox client = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 5);
            KpiCheckBox date = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 6);
            KpiCheckBox type = (KpiCheckBox) invoicePDFNamingTable.getWidget(0, 7);

            applyNamingFormat(prefix, PDF_PREFIX, format);
            applyNamingFormat(type, PDF_TYPE, format);
            applyNamingFormat(companyName, PDF_COMPANY_NAME, format);
            applyNamingFormat(client, PDF_CLIENT, format);
//            applyNamingFormat(clientCode, PDF_CLIENT_CODE, format);
            applyNamingFormat(number, PDF_NUMBER, format);
            applyNamingFormat(date, PDF_GENERATED_DATE, format);
            applyNamingFormat(userName, PDF_USER_NAME, format);
        }
    }

    private void applyNamingFormat(Widget widget, String code, String values) {
        if (values.contains(code)) {
            if (widget instanceof KpiCheckBox) {
                ((KpiCheckBox) widget).setValue(true);
            } else if (widget instanceof TextBox) {
                ((TextBox) widget).setValue(settingsData.getPdfNamingPrefix());
            }
        }
    }

    private void applyCustomerSupplierNamingFormat(Widget widget, String code, String values, boolean isCustomer) {
        if (values.contains(code)) {
            if (widget instanceof KpiCheckBox) {
                ((KpiCheckBox) widget).setValue(true);
            } else if (widget instanceof TextBox) {
                ((TextBox) widget).setValue(isCustomer ? settingsData.getCustomerPdfNamingPrefix() : settingsData.getSupplierPdfNamingPrefix());
            }
        }
    }

    private void setCustomerSupplierPDFNamingFormat(String format, boolean isCustomer) {
        if (format != null) {
            TextBox prefix = null;
            KpiCheckBox companyName = null;
            KpiCheckBox code = null;
            KpiCheckBox number = null;
            KpiCheckBox date = null;
            KpiCheckBox username = null;
            if (isCustomer) {
                prefix = (TextBox) customerPDFNamingTable.getWidget(0, 0);
                companyName = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 2);
                username = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 3);
                number = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 4);
                date = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 5);
                code = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 6);
            } else {
                prefix = (TextBox) supplierPDFNamingTable.getWidget(0, 0);
                companyName = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 2);
                username = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 3);
                number = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 4);
                date = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 5);
                code = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 6);

            }

            applyCustomerSupplierNamingFormat(prefix, PDF_PREFIX, format, isCustomer);
            applyCustomerSupplierNamingFormat(companyName, PDF_COMPANY_NAME, format, isCustomer);
            applyCustomerSupplierNamingFormat(code, PDF_CLIENT_CODE, format, isCustomer);
            applyCustomerSupplierNamingFormat(number, PDF_NUMBER, format, isCustomer);
            applyCustomerSupplierNamingFormat(date, PDF_GENERATED_DATE, format, isCustomer);
            applyCustomerSupplierNamingFormat(username, PDF_USER_NAME, format, isCustomer);
        }
    }

    // CUSTOMER/SUPPLIER PDF NAMING
    private String getCustomerSupplierPDFNamingFormat(boolean isCustomer) {
        if (isCustomer) {
            TextBox customerPrefix = (TextBox) customerPDFNamingTable.getWidget(0, 0);
            KpiCheckBox customerCompanyName = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 2);
            KpiCheckBox customerName = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 3);
            KpiCheckBox customerNumber = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 4);
            KpiCheckBox customerDate = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 5);
            KpiCheckBox customerCode = (KpiCheckBox) customerPDFNamingTable.getWidget(0, 6);

            StringBuffer s = new StringBuffer();
            addNamingFormat(customerPrefix, PDF_PREFIX, s);
            addNamingFormat(customerCompanyName, PDF_COMPANY_NAME, s);
            addNamingFormat(customerCode, PDF_CLIENT_CODE, s);
            addNamingFormat(customerNumber, PDF_NUMBER, s);
            addNamingFormat(customerDate, PDF_GENERATED_DATE, s);
            addNamingFormat(customerName, PDF_USER_NAME, s);
            return s.toString();
        } else {
            TextBox supplierPrefix = (TextBox) supplierPDFNamingTable.getWidget(0, 0);
            KpiCheckBox supplierCompanyName = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 2);
            KpiCheckBox supplierName = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 3);
            KpiCheckBox supplierNumber = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 4);
            KpiCheckBox supplierDate = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 5);
            KpiCheckBox supplierCode = (KpiCheckBox) supplierPDFNamingTable.getWidget(0, 6);

            StringBuffer s = new StringBuffer();
            addNamingFormat(supplierPrefix, PDF_PREFIX, s);
            addNamingFormat(supplierCompanyName, PDF_COMPANY_NAME, s);
            addNamingFormat(supplierCode, PDF_CLIENT_CODE, s);
            addNamingFormat(supplierNumber, PDF_NUMBER, s);
            addNamingFormat(supplierDate, PDF_GENERATED_DATE, s);
            addNamingFormat(supplierName, PDF_USER_NAME, s);
            return s.toString();
        }
    }

    private void drawLogoForm() {
        logoForm = new WfmForm();
        logoForm.addField(wfmStrings.companyPdfLogo(), new LogoField(new Image(settingsLogoBundle.imageForPDF()), FOR_INVOICEPDF, settingsStrings.chooseLogoYourCompany()));
        addTitleField(COMPANY_LOGO, wfmStrings.companyPdfLogo());
        addField(COMPANY_LOGO, logoForm, "", true);
    }

    private class LogoField extends Composite {

        private AttachmentStrategy attachmentStrategy;
        private WfmFormPanel form;
        private MaterialFileUpload fileUpload;
        private final Image image;
        private Image logoImage;
        private final String logoType;
        private final String helpMessage;

        public LogoField(Image image, String logoType, String helpMessage) {
            this.image = image;
            this.logoType = logoType;
            this.helpMessage = helpMessage;
            show();
        }

        private void initialize() {
            attachmentStrategy = () -> settingsData.getCompanyID();

            form = new WfmFormPanel("/CompanyAttachment");

            logoImage = new Image();
            logoImage.setVisible(false);
            invoiceService.getInvoiceLogoUrl(new AbstractAsyncCallback<String>() {
                public void failure(Throwable caught) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void success(String result) {
                    if (result != null) {
                        logoImage.setUrl(result);
                        logoImage.setVisible(true);
                    }
                }
            });

            fileUpload = new MaterialFileUpload();
            fileUpload.ensureDebugId(invoiceSettings + "fileUpload");
        }

        private void show() {
            initialize();

            FlexTable flexTable = new FlexTable();
            flexTable.setWidget(0, 0, fileUpload);
            TextArea description = new TextArea();
            description.setName(DESCRIPTION_PARAM_NAME);
            description.setVisible(false);
            description.setText("");
            TextBox uploadType = new TextBox();
            uploadType.setName(UPLOAD_TYPE_PARAM_NAME);
            uploadType.setVisible(false);
            uploadType.setText(Utils.getUploadTypeParam());

            TextBox imageWidth = new TextBox();
            imageWidth.setName(IMAGE_WIDTH);
            imageWidth.setValue("471");
            imageWidth.setVisible(false);
            TextBox imageHeight = new TextBox();
            imageHeight.setName(IMAGE_HEIGHT);
            imageHeight.setValue("121");
            imageHeight.setVisible(false);


            FlexTable flex = new FlexTable();
            HTML logoSizeText = new HTML(wfmStrings.logoSize());
            flex.setWidget(0, 0, logoSizeText);
            flex.setWidget(1, 0, new HelpTextPanel(helpMessage));
            flex.getFlexCellFormatter().setColSpan(1, 0, 2);

            DockPanel panel = new DockPanel();
            panel.add(flex, DockPanel.NORTH);
            panel.add(new HTML("<br/><br/><br/>"), DockPanel.CENTER);
            panel.add(logoImage, DockPanel.SOUTH);

            flexTable.setWidget(1, 0, panel);
            flexTable.setWidget(2, 0, description);
            flexTable.setWidget(2, 1, uploadType);
            flexTable.setWidget(3, 0, imageWidth);
            flexTable.setWidget(3, 1, imageHeight);

            fileUpload.getFileUpload().addChangeHandler(c -> {
                if (fileUpload.getFilename() == "") {
                    Info.show(wfmStrings.chooseYourLogoImage(), Info.Type.INFO);
                } else {
                    if (fileUpload.getFilename().toLowerCase().lastIndexOf(".jpg") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".jpeg") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".gif") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".png") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".ico") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".bmp") != -1) {
                        saveLogo();
                    } else {
                        fileUpload.reset();
                        Info.show(wfmStrings.provideImageType(), Info.Type.WARNING);
                    }
                }
            });

            DockPanel dock = new DockPanel();
            dock.add(flexTable, DockPanel.WEST);
            dock.add(new HTML("<div></div>"), DockPanel.CENTER);
            dock.add(image, DockPanel.EAST);

            form.setWidget(dock);
            form.addFormHandler(new FormHandler() {
                public void onSubmit(FormSubmitEvent event) {

                }

                public void onSubmitComplete(FormSubmitCompleteEvent event) {
                    LoadingPanel.loading(false);
                    if (form.isSuccess()) {
                        invoiceService.getInvoiceLogoUrl(new AbstractAsyncCallback<String>() {
                            public void failure(Throwable caught) {
                            }

                            public void success(String result) {
                                if (result != null && !"".equals(result)) {
                                    logoImage.setUrl(result);
                                    logoImage.setVisible(true);
                                } else {
                                    logoImage.setVisible(false);
                                }
                            }
                        });
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.invoiceLogo()));
                    } else {
                        fileUpload.reset();
                        if (form.getErrorString() == null) {
                            Info.warn(wfmStrings.errorOccurredSavingChanges());
                            logoImage.setVisible(false);
                        } else {
                            Info.warn("Your Company logo size should be within 470x120px");
                            logoImage.setVisible(false);
                        }
                    }
                }
            });
            initWidget(form);
        }

        private void saveLogo() {
            form.setParameter("hostId", attachmentStrategy.getHostId().toString());
            form.setParameter("logoType", logoType);
            form.submit();
            LoadingPanel.loading(true);
        }
    }

    private void initListeners() {
        Validation.addNumericKeyboardListener(paymentDue);
        Validation.addNumericKeyboardListener(salesQuotes);
        Validation.addNumericKeyboardListener(purchaseOrders);
    }


    public void save() {
        final SettingsData invSettings = new SettingsData();
        invSettings.setObjectID(objectId);
        if (!"".equals(paymentDue.getText())) {
            invSettings.setPaymentDue(Integer.parseInt(paymentDue.getText()));
        }
        if (!"".equals(salesQuotes.getText())) {
            invSettings.setSalesQuoteDue(Integer.parseInt(salesQuotes.getText()));
        }
        if (!"".equals(purchaseOrders.getText())) {
            invSettings.setPurchaseOrderDue(Integer.parseInt(purchaseOrders.getText()));
        }
        invSettings.setLocaleID(companyPdfLocale.getSelectedId());

        invSettings.setSalesQuoteProgressInvoicing(salesQuoteProgressInvoicing.getValue());
        invSettings.setExpandProductGroup(expandProductGroup.getValue());
//            invSettings.setSalesInvoiceInvoiceType(salesInvoiceInvoiceType.getSelectedId());
        if (isConvertInvoiceBtnShow != null) {
            invSettings.setConvertInvoiceBtnShow(isConvertInvoiceBtnShow.getValue());
        }
        if (isShowPurchaseInvoiceAndPICreditNoteNumbering != null) {
            invSettings.setIsShowPurchaseInvoiceAndPICreditNoteNumbering(isShowPurchaseInvoiceAndPICreditNoteNumbering.getValue());
        }
        invSettings.setDueDateType(dueDateOption.getSelectedId());
        invSettings.setConvertPurchaseInvoiceDateType(convertPurchaseInvoiceDateType.getSelectedId());

        invSettings.setPdfNamingFormat(getInvoicePDFNamingFormat());
        invSettings.setSalesReceiptPdfNamingFormat(getSalesReceiptPDFNamingFormat());
        invSettings.setSalesOrderPdfNamingFormat(getSalesOrderPDFNamingFormat());
        invSettings.setPurchaseOrderPdfNamingFormat(getPurchaseOrderPDFNamingFormat());
        invSettings.setCustomerPdfNamingFormat(getCustomerSupplierPDFNamingFormat(true));
        invSettings.setSupplierPdfNamingFormat(getCustomerSupplierPDFNamingFormat(false));
        invSettings.setPdfNamingPrefix(((TextBox) invoicePDFNamingTable.getWidget(0, 0)).getValue());
        invSettings.setSalesReceiptPdfNamingPrefix(((TextBox) salesReceiptPDFNamingTable.getWidget(0, 0)).getValue());
        invSettings.setSalesOrderPdfNamingPrefix(((TextBox) salesOrderPDFNamingTable.getWidget(0, 0)).getValue());
        invSettings.setPurchaseOrderPdfNamingPrefix(((TextBox) purchaseOrderPDFNamingTable.getWidget(0, 0)).getValue());
        invSettings.setCustomerPdfNamingPrefix(((TextBox) customerPDFNamingTable.getWidget(0, 0)).getValue());
        invSettings.setSupplierPdfNamingPrefix(((TextBox) supplierPDFNamingTable.getWidget(0, 0)).getValue());

        invSettings.setInstructions(getInstructionsOrTermsConditions(salesInvoiceTermsConditions));
        invSettings.setQuoteTermsConditions(getInstructionsOrTermsConditions(quoteTermsConditions));
        invSettings.setSaleOrderTermsConditions(getInstructionsOrTermsConditions(saleOrderTermsConditions));
        invSettings.setPurchaseOrderTermsAndConditions(getInstructionsOrTermsConditions(purchaseOrderTermsAndConditions));
        invSettings.setPurchaseInvoicePaymentInstructions(getInstructionsOrTermsConditions(purchaseInvoicePaymentInstructions));
        invSettings.setRfqInstructions(getInstructionsOrTermsConditions(rfqInstructions));
        invSettings.setQuoteConvertToInvoiceCustomType(dwInvoiceCustomType.getSelectedId());

        /* set intoduction values to settingsdata */
        invSettings.setSaleInvoiceIntroduction(getInstructionsOrTermsConditions(introductionSaleInvoice));
        invSettings.setSaleQuoteIntroduction(getInstructionsOrTermsConditions(introductionSaleQuote));
        invSettings.setSaleOrderIntroduction(getInstructionsOrTermsConditions(introductionSaleOrder));
        invSettings.setRequestForQuoteIntroduction(getInstructionsOrTermsConditions(introductionRFQ));
        invSettings.setTaxCalculationType(taxCalculationTypeListBox.getSelectedId());
        if (bankAccounts != null) {
            invSettings.setBankID(bankAccounts.getSelectedId());
        }
        if (defaultPaymentAccounts != null && defaultPaymentAccounts.getSelectedItem() != null) {
            invSettings.setDefaultPaymentAccount(defaultPaymentAccounts.getSelectedItem());
        }
        invSettings.setSalesQuoteTermCopyToSalesInvoice(salesQuoteTermCopyToSalesInvoice.getValue());
        invSettings.setSalesQuoteTermCopyToSalesOrder(salesQuoteTermCopyToSalesOrder.getValue());
        invSettings.setSalesOrderTermCopyToSalesInvoice(salesOrderTermCopyToSalesInvoice.getValue());
        invSettings.setCopySQIntroduction(copySQIntroduction.getValue());
        invSettings.setCopySOIntroduction(copySOIntroduction.getValue());
        saveInvoiceSettings(invSettings);
    }

    private InstructionData[] getInstructionsOrTermsConditions(MultiTable table) {
        ArrayList<HashMap<String, Widget>> widgetsList = table.getWidgets();
        List<InstructionData> dataList = new LinkedList<>();
        for (HashMap<String, Widget> widgets : widgetsList) {
            ExtendedTextArea area = (ExtendedTextArea) widgets.get(INSTRUCTION);
            InstructionData data = new InstructionData();
            data.setObjectID(area.getInstructionID());
            data.setText(area.getText());
            dataList.add(data);
        }
        return dataList.toArray(new InstructionData[]{});
    }

    private InstructionData[] getInstructionsOrTermsConditions(MultiTableNewUI table) {
        LinkedList<HashMap<String, Widget>> widgetsList = table.getWidgets();
        List<InstructionData> dataList = new LinkedList<>();
        for (HashMap<String, Widget> widgets : widgetsList) {
            ExtendedTextArea area = (ExtendedTextArea) widgets.get(INSTRUCTION);
            InstructionData data = new InstructionData();
            data.setObjectID(area.getInstructionID());
            data.setText(area.getText());
            dataList.add(data);
        }
        return dataList.toArray(new InstructionData[]{});
    }

    private void saveInvoiceSettings(SettingsData invSettings) {
        invoiceService.saveCompanyInvoiceSettings(invSettings, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(Integer result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), settingsStrings.invoiceSettings()), Info.Type.INFO);
            }

        });

    }

    public String getDescription() {
        return settingsStrings.invoiceSettings();
    }

    public String getIconStyle() {
        return "accountMark  ac-type-num-settings";
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

    private String getSalesReceiptPDFNamingFormat() {
        TextBox prefix = (TextBox) salesReceiptPDFNamingTable.getWidget(0, 0);
        KpiCheckBox companyName = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 2);
        KpiCheckBox userName = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 3);
        KpiCheckBox number = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 4);
        KpiCheckBox client = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 5);
        KpiCheckBox date = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 6);
        KpiCheckBox type = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 7);

        StringBuffer s = new StringBuffer();
        addNamingFormat(prefix, PDF_PREFIX, s);
        addNamingFormat(type, PDF_TYPE, s);
        addNamingFormat(companyName, PDF_COMPANY_NAME, s);
        addNamingFormat(client, PDF_CLIENT, s);
//        addNamingFormat(clientCode, PDF_CLIENT_CODE, s);
        addNamingFormat(number, PDF_NUMBER, s);
        addNamingFormat(date, PDF_GENERATED_DATE, s);
        addNamingFormat(userName, PDF_USER_NAME, s);
        return s.toString();
    }

    private void setSalesReceiptPDFNamingFormat(String format) {
        if (format != null) {
            TextBox prefix = (TextBox) salesReceiptPDFNamingTable.getWidget(0, 0);
            KpiCheckBox companyName = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 2);
            KpiCheckBox userName = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 3);
            KpiCheckBox number = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 4);
            KpiCheckBox client = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 5);
            KpiCheckBox date = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 6);
            KpiCheckBox type = (KpiCheckBox) salesReceiptPDFNamingTable.getWidget(0, 7);

            applySalesReceiptNamingFormat(prefix, PDF_PREFIX, format);
            applySalesReceiptNamingFormat(type, PDF_TYPE, format);
            applySalesReceiptNamingFormat(companyName, PDF_COMPANY_NAME, format);
            applySalesReceiptNamingFormat(client, PDF_CLIENT, format);
//            applySalesReceiptNamingFormat(clientCode, PDF_CLIENT_CODE, format);
            applySalesReceiptNamingFormat(number, PDF_NUMBER, format);
            applySalesReceiptNamingFormat(date, PDF_GENERATED_DATE, format);
            applySalesReceiptNamingFormat(userName, PDF_USER_NAME, format);
        }
    }

    private void applySalesReceiptNamingFormat(Widget widget, String code, String values) {
        if (values.contains(code)) {
            if (widget instanceof KpiCheckBox) {
                ((KpiCheckBox) widget).setValue(true);
            } else if (widget instanceof TextBox) {
                ((TextBox) widget).setValue(settingsData.getSalesReceiptPdfNamingPrefix());
            }
        }
    }

    private String getSalesOrderPDFNamingFormat() {
        TextBox prefix = (TextBox) salesOrderPDFNamingTable.getWidget(0, 0);
        KpiCheckBox companyName = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 2);
        KpiCheckBox userName = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 3);
        KpiCheckBox number = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 4);
        KpiCheckBox client = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 5);
        KpiCheckBox date = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 6);
        KpiCheckBox type = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 7);

        StringBuffer s = new StringBuffer();
        addNamingFormat(prefix, PDF_PREFIX, s);
        addNamingFormat(type, PDF_TYPE, s);
        addNamingFormat(companyName, PDF_COMPANY_NAME, s);
        addNamingFormat(client, PDF_CLIENT, s);
        addNamingFormat(number, PDF_NUMBER, s);
        addNamingFormat(date, PDF_GENERATED_DATE, s);
        addNamingFormat(userName, PDF_USER_NAME, s);
        return s.toString();
    }

    private void setSalesOrderPDFNamingFormat(String format) {
        if (format != null) {
            TextBox prefix = (TextBox) salesOrderPDFNamingTable.getWidget(0, 0);
            KpiCheckBox companyName = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 2);
            KpiCheckBox userName = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 3);
            KpiCheckBox number = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 4);
            KpiCheckBox client = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 5);
            KpiCheckBox date = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 6);
            KpiCheckBox type = (KpiCheckBox) salesOrderPDFNamingTable.getWidget(0, 7);

            applySalesOrderNamingFormat(prefix, PDF_PREFIX, format);
            applySalesOrderNamingFormat(type, PDF_TYPE, format);
            applySalesOrderNamingFormat(companyName, PDF_COMPANY_NAME, format);
            applySalesOrderNamingFormat(client, PDF_CLIENT, format);
            applySalesOrderNamingFormat(number, PDF_NUMBER, format);
            applySalesOrderNamingFormat(date, PDF_GENERATED_DATE, format);
            applySalesOrderNamingFormat(userName, PDF_USER_NAME, format);
        }
    }

    private void applySalesOrderNamingFormat(Widget widget, String code, String values) {
        if (values.contains(code)) {
            if (widget instanceof KpiCheckBox) {
                ((KpiCheckBox) widget).setValue(true);
            } else if (widget instanceof TextBox) {
                ((TextBox) widget).setValue(settingsData.getSalesOrderPdfNamingPrefix());
            }
        }
    }

    private String getPurchaseOrderPDFNamingFormat() {
        TextBox prefix = (TextBox) purchaseOrderPDFNamingTable.getWidget(0, 0);
        KpiCheckBox companyName = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 2);
        KpiCheckBox userName = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 3);
        KpiCheckBox number = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 4);
        KpiCheckBox client = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 5);
        KpiCheckBox date = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 6);
        KpiCheckBox type = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 7);

        StringBuffer s = new StringBuffer();
        addNamingFormat(prefix, PDF_PREFIX, s);
        addNamingFormat(type, PDF_TYPE, s);
        addNamingFormat(companyName, PDF_COMPANY_NAME, s);
        addNamingFormat(client, PDF_CLIENT, s);
        addNamingFormat(number, PDF_NUMBER, s);
        addNamingFormat(date, PDF_GENERATED_DATE, s);
        addNamingFormat(userName, PDF_USER_NAME, s);
        return s.toString();
    }

    private void setPurchaseOrderPDFNamingFormat(String format) {
        if (format != null) {
            TextBox prefix = (TextBox) purchaseOrderPDFNamingTable.getWidget(0, 0);
            KpiCheckBox companyName = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 2);
            KpiCheckBox userName = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 3);
            KpiCheckBox number = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 4);
            KpiCheckBox client = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 5);
            KpiCheckBox date = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 6);
            KpiCheckBox type = (KpiCheckBox) purchaseOrderPDFNamingTable.getWidget(0, 7);

            applyPurchaseOrderNamingFormat(prefix, PDF_PREFIX, format);
            applyPurchaseOrderNamingFormat(type, PDF_TYPE, format);
            applyPurchaseOrderNamingFormat(companyName, PDF_COMPANY_NAME, format);
            applyPurchaseOrderNamingFormat(client, PDF_CLIENT, format);
            applyPurchaseOrderNamingFormat(number, PDF_NUMBER, format);
            applyPurchaseOrderNamingFormat(date, PDF_GENERATED_DATE, format);
            applyPurchaseOrderNamingFormat(userName, PDF_USER_NAME, format);
        }
    }

    private void applyPurchaseOrderNamingFormat(Widget widget, String code, String values) {
        if (values.contains(code)) {
            if (widget instanceof KpiCheckBox) {
                ((KpiCheckBox) widget).setValue(true);
            } else if (widget instanceof TextBox) {
                ((TextBox) widget).setValue(settingsData.getPurchaseOrderPdfNamingPrefix());
            }
        }
    }
}
