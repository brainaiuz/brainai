package com.edatasite.workforce.gwt.invoice.client.ui.view.payment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.Option;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentAddEditData;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.IntroductionPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Dilshod Madrahimov on 10/19/15 3:04 PM
 */
public class BatchPaymentAddView extends FooteredView implements Constants, FittedContent, AccountingCustomFormConstants, AccountingConstants, Colapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/yyyy");
    private static final InvoiceServiceAsync invoiceService = InvoiceService.App.get();

    private static final Option INVOICE = new Option(accountingStrings.invoice(), AccountingConstants.PAYMENT_TARGET_INVOICE);
    private static final Option MANUAL_ENTRY = new Option(wfmStrings.manualEntry(), AccountingConstants.PAYMENT_TARGET_MANUAL_JOURNAL);
    private static final Option EXPENSE = new Option(wfmStrings.expense(), AccountingConstants.PAYMENT_TARGET_EXPENSE);

    private CrmAccountLookUp crmAccountLookUp;
    private ProjectLookUp projectLookUp;
    private IntroductionPanel description;
    private MaterialComboBox<Option> paymentTarget;

    private CurrencyWidget currencyWidget;
    private PaymentAccountsLookUp paymentAccountLookUp;
    private PaymentAccountsLookUp underpaymentAccountLookUp;
    private PaymentAccountsLookUp overpaymentAccountLookUp;
    private DatePicker paymentDatePicker;
    private AccountsReceivablePayableLookUp accountsReceivablePayableLookUp;

    private TextBox amountTxtBox;
    private TextBox overpaymentAmountTxtBox;
    private TextBox referenceTxtBox;
    private DataListBox paymentTypeListBox;
    private TextBox numberTxtBox;
    private DepartmentLookUp departmentLookUp;

    private FormGroup departmentItem;
    private FormGroup customerField;
    private FormGroup overpaymentAccountItem;
    private FormGroup overpaymentAmountItem;

    private final LinkedHashMap<String, PaymentData> editData = new LinkedHashMap<>();
    private final FindMatchFilterData filterData = new FindMatchFilterData();

    private DynamicTable dynamicTable;
    private ScrollPanel scrollDynamicTable;

    private TotalTable totalTable;
    private HTML totalAmountValue, totalPaymentAmountValue;

    private InvoiceAdvancedOptions advancedOptions;
    private MaterialLink showMoreLink;

    private WfmButton2 saveButton;

    private Integer objectID;
    private final boolean isReceivable;
    private boolean copy = false;
    private HashMap<String, Widget> widgetsMap;
    private BatchPaymentAddEditData data;
    private BankTransferNumberData numberData;
    private FooterUploadPanel footerUploadPanel;
    private InvoiceCustomFieldsView customFieldsView;
    private KpiCheckBox showSubAccountTransaction;
    private int countCheckedCloseButton = 0;
    private MaterialLink customerBalanceLink;

    private String sortDirection = DESC_STR;
    private String sortField = AccountingConstants.DATE_COLUMN;

    private boolean isAll = true;
    private final ArrayList<PaymentData> objPaymentData = new ArrayList<>();

    BigDecimal foreignAccExRate = null;


    public BatchPaymentAddView(boolean isReceivable) {
        super("receivepayment");
        setDescription(isReceivable ? accountingStrings.receivePayment() : property.getSingular(accountingStrings.payBill()));
        this.isReceivable = isReceivable;
    }

    public BatchPaymentAddView(Integer objectID, boolean isReceivable) {
        super("edit");
        setDescription(isReceivable ? accountingStrings.receivePayment() : property.getSingular(accountingStrings.payBill()));
        this.objectID = objectID;
        this.isReceivable = isReceivable;
    }

    public BatchPaymentAddView(Integer copyFromId, boolean isReceivable, boolean copy) {
        super("edit");
        setDescription(isReceivable ? accountingStrings.receivePayment() : property.getSingular(accountingStrings.payBill()));
        this.objectID = copyFromId;
        this.isReceivable = isReceivable;
        this.copy = copy;
    }

    @Override
    protected Widget onInitialize() {
        initForm();
        loadFormData();
        return null;
    }

    private void loadFormData() {
        LoadingPanel.loading(true);
        invoiceService.getBatchPaymentAddEditData(objectID, isReceivable, new AsyncCallback<BatchPaymentAddEditData>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(BatchPaymentAddEditData batchPaymentAddEditData) {
                LoadingPanel.loading(false);
                data = batchPaymentAddEditData;
                setFormData();
                initCustomFields();
                HTMLPanel htmlPanel = new WftHTMLPanel(batchPaymentAddEditData.getData().getLayoutHtml(), widgetsMap).getContainer();
                htmlPanel.add(createFooter());
                htmlPanel.setStyleName("add-form");
                add(htmlPanel);
            }
        });
    }

    private void initForm() {
        widgetsMap = new HashMap<>();
        createHeaderPanel();
        createSearchPanel();
        createDynamicTable();
        createFooterPanel();
        initWidgetsMap();
        showOverpaymentPanel(false);
    }

    private void setFormData() {
        paymentTypeListBox.setItems(data.getPaymentMethods());
        numberData = data.getNumberData();
        paymentTarget.setItems(getPaymentTargetItems());
        numberTxtBox.setText(numberData.getTransferNumber());


        ReceivePaymentData paymentData = data.getData();
        systemCustomFields = paymentData.getSystemCustomFields();
        if (objectID != null) {

            setPaymentTarget(paymentData.getPaymentTarget());
            paymentTarget.setEnabled(false);

            if (paymentData.getCrmAccount() != null) {
                crmAccountLookUp.addItem(paymentData.getCrmAccount());
            }

            if (!copy) {
                crmAccountLookUp.setEnabled(false);
                showSubAccountTransaction.setEnabled(false);
            }
            showSubAccountTransaction.setValue(paymentData.isIncludeSubAccountTransaction());

            paymentDatePicker.setDate(paymentData.getDate() != null ? paymentData.getDate().getDate() : null);
            amountTxtBox.setText(AccountingUtils.get().formatPrice(paymentData.getTotalAmount()));
            if (paymentData.getPaymentMethod() != null) {
                paymentTypeListBox.setSelected(paymentData.getPaymentMethod());
            }
            description.getIntroduction().setText(paymentData.getDescription());
            referenceTxtBox.setText(paymentData.getReference());

            if (paymentData.getCurrency() != null) {
                currencyWidget.setCurrency(paymentData.getCurrency().getId(), paymentData.getExRate());
                if (!copy) {
                    currencyWidget.setEnabled(false);
                    paymentAccountLookUp.setEnabled(false);
                }
            }

            paymentAccountLookUp.addItem(paymentData.getAccount());

            if (paymentData.getOverPayment() != null) {
                overpaymentAccountLookUp.setSelected(paymentData.getOverPayment().getOverPaymentAccount());
                overpaymentAmountTxtBox.setText(AccountingUtils.get().formatPrice(paymentData.getOverPayment().getOverPaymentAmount()));
                showOverpaymentPanel(true);
            }

            reGenerateDynamicTableColumns();
            dynamicTable.clear();
            objPaymentData.clear();
            for (PaymentData data : paymentData.getPayments()) {
                dynamicTable.addRow(getWidgets(data));
                objPaymentData.add(data);
            }
            for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
                putCurrentRowDataToMap(rowID);
            }
            calculate();

//            Validation.validateLookUpRequired(overpaymentAccountLookUp);
            if (copy) {
                numberTxtBox.setText(numberData.getTransferNumber());
            } else {
                numberTxtBox.setText(data.getData().getNumber());
            }

        }
        if (data.isEnableDepartment()) {
            departmentItem.setVisible(true);
        }
    }

    private void initCustomFields() {
        if (data.getData().getCustomFieldItems() != null && !data.getData().getCustomFieldItems().isEmpty()) {
            advancedOptions.createAndAppendBatchPaymentCustomFieldsView(isReceivable ? ViewAddFiledsCodeName.BatchInvoicePaymentAdd : ViewAddFiledsCodeName.BatchPayBillAdd, data);
            customFieldsView = advancedOptions.getCustomFieldsView();
        }
        addSystemCustomFields(widgetsMap);
    }

    private void initWidgetsMap() {
        footerUploadPanel = new FooterUploadPanel(F_BATCH_PAYMENT, null);

        widgetsMap.put(LABEL_TITLE, new HTML(isReceivable ? accountingStrings.receivePayment() : property.getSingular(accountingStrings.payBill())));
        widgetsMap.put(LABEL_ACCOUNTS_RECEIVABLE_PAYABLE, new HTML(isReceivable ? wfmStrings.accountsReceivable() : wfmStrings.accountsPayable()));
        widgetsMap.put(INPUT_CUSTOMER, customerField);
        widgetsMap.put(INPUT_PAYMENT_DATE, new FormGroup(wfmStrings.paymentDate(), paymentDatePicker, true));
        widgetsMap.put(INPUT_INVOICE_TYPE, new FormGroup(wfmStrings.type(), paymentTarget));
        widgetsMap.put(INPUT_NUMBER, new FormGroup(wfmStrings.number(), numberTxtBox));
        widgetsMap.put(INPUT_ACCOUNT, new FormGroup(wfmStrings.accountForFinece(), paymentAccountLookUp, true));
        widgetsMap.put(INPUT_AMOUNT, new FormGroup(wfmStrings.amount(), amountTxtBox, true));
        widgetsMap.put(INPUT_UNDERPAYMENT_ACCOUNT, new FormGroup(accountingStrings.bankFeeDiscountAccount(), underpaymentAccountLookUp));
        widgetsMap.put(INPUT_DEPARTMENT, departmentItem);
        widgetsMap.put(INPUT_OVERPAYMENT_ACCOUNT, new FormGroup(overpaymentAccountItem));
        widgetsMap.put(INPUT_OVERPAYMENT_AMOUNT, new FormGroup(overpaymentAmountItem));
        widgetsMap.put(INPUT_ACCOUNTS_RECEIVABLE_PAYABLE, accountsReceivablePayableLookUp);
        widgetsMap.put(INPUT_ITEM_TABLE, scrollDynamicTable);
        widgetsMap.put(INPUT_TOTALS_TABLE, totalTable);
        widgetsMap.put(SAVE_AS_DRAFT_BUTTON, saveButton);

        currencyWidget.ensureDebugId(InvoiceFormFields.CURRENCY);
        systemCustomFieldsMap.put(AccountingCustomFormConstants.INPUT_EXCHANGE_RATE, currencyWidget);

        referenceTxtBox.ensureDebugId(InvoiceFormFields.REFERENCE);
        systemCustomFieldsMap.put(AccountingCustomFormConstants.INPUT_REFERENCE, referenceTxtBox);

        advancedOptions = createAdvancedOptions();
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addStyleName("btn-flat BatchPaymentAddView");
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));

        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");
        widgetsMap.put(INPUT_SHOW_MORE, showMoreField);

    }

    private void createHeaderPanel() {

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");


        boolean hasPermissonCustomerQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_QUICK_ADD);
        boolean hasPermissonCustomerAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_ADD);

        boolean hasPermissonSupplierQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_QUICK_ADD);
        boolean hasPermissonSupplierAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_ADD);

        crmAccountLookUp = new SmartCrmAccountLookup(isReceivable ? CrmAccountLookUp.CUSTOMER : CrmAccountLookUp.SUPPLIER, true, () -> {
            if (isReceivable) {
                if (hasPermissonCustomerQuick) {
                    new CusSuppQuickAddView(CrmAccountLookUp.CUSTOMER, crmAccountLookUp.getLastValueBeforeClick());
                } else if (hasPermissonCustomerAdd) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add");
                }
            } else {
                if (hasPermissonSupplierQuick) {
                    new CusSuppQuickAddView(CrmAccountLookUp.SUPPLIER, crmAccountLookUp.getLastValueBeforeClick());
                } else if (hasPermissonSupplierAdd) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("supplier|add/add");
                }
            }
        }, true, isReceivable ? hasPermissonCustomerQuick || hasPermissonCustomerAdd : hasPermissonSupplierQuick || hasPermissonSupplierAdd);

        addFormListeners();
        crmAccountLookUp.ensureDebugId("receive_payment_custtomerLookUp");
        crmAccountLookUp.setAutocompleteOff();
        crmAccountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            onChangeLookUp();
        });

        crmAccountLookUp.getSuggestBox().addSelectionHandler(sh -> {
            if (crmAccountLookUp.getSelectedItemID() != null) {
            }
        });

//        showSubAccountTransaction = new KpiCheckBox(accountingStrings.includeSubAccountTransactions());
//        showSubAccountTransaction.ensureDebugId("receive_payment-includeSubAccountTransactions");
//        showSubAccountTransaction.addValueChangeHandler(valueChangeEvent -> {
//            if (!Validation.validateLookUpRequired(crmAccountLookUp)) {
//                return;
//            }
//            reGenerateDynamicTableColumns();
//            loadClientSupplierData(false);
//        });

        description = new IntroductionPanel();
        description.getIntroduction().addStyleName("keepDropDownOpen");

        paymentTarget = new MaterialComboBox<>();
        paymentTarget.setMultiple(true);
        paymentTarget.setCloseOnSelect(false);
        paymentTarget.ensureDebugId("receive_payment-paymentTarget");
        paymentTarget.addValueChangeHandler(c -> {
            projectLookUp.clear();
            editData.clear();
            dynamicTable.clear();
            clearFilterData();
            showOverpaymentPanel(false);
            loadClientSupplierData(false);
        });

        currencyWidget = new CurrencyWidget(objectID == null);
        currencyWidget.setShowExchangePopUpAlways(true);
        currencyWidget.addListener(this::onExchangeRateChange);
        currencyWidget.setDatePicker(paymentDatePicker);
        currencyWidget.ensureDebugId("receive_payment-currency");
//        currencyWidget.setEnabled(false);

        paymentAccountLookUp = new PaymentAccountsLookUp(true);
        paymentAccountLookUp.ensureDebugId("receive_payment-paymentLookUp");
        paymentAccountLookUp.setAutocompleteOff();
        paymentAccountLookUp.getSuggestBox().addSelectionHandler(event -> {
            currencyWidget.setShowOnlyAccountCurrenyWithBase(paymentAccountLookUp.getSelectedItem().getRowId());
            loadClientSupplierData(true);
//            currencyWidget.setCurrency(paymentAccountLookUp.getSelectedItem().getRowId());
        });

        if (Utils.hasGenericAccess(GenericSettingsEnum.TRANSACTION_UNIQUE_NUMBERING)) {
            paymentAccountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {

                if (paymentAccountLookUp.getSelectedItemID() != null) {
                    LoadingPanel.loading(true);
                    invoiceService.generateBatchPaymentNumberData(isReceivable, paymentAccountLookUp.getSelectedItemID(), new AsyncCallback<BankTransferNumberData>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(BankTransferNumberData bankTransferNumberData) {
                            LoadingPanel.loading(false);
                            numberData = bankTransferNumberData;
                            data.setNumberData(numberData);
                            applyNumberDate();
                        }
                    });
                }
            });
        }

        customerField = new FormGroup(crmAccountLookUp);
        customerField.ensureDebugId(isReceivable ? InvoiceFormFields.CUSTOMER : InvoiceFormFields.SUPPLIER);

        Div clientFieldLabel = customerField.getGroupLabel();
        clientFieldLabel.addStyleName("label-group");


        Span inputLabel = new Span(isReceivable ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()));
        Span balance = new Span(wfmStrings.balance() + ": ");

        inputLabel.addStyleName("form-label--required");
        balance.add(customerBalanceLink);

        clientFieldLabel.add(inputLabel);
        clientFieldLabel.add(balance);

        underpaymentAccountLookUp = new PaymentAccountsLookUp(true);
        underpaymentAccountLookUp.setAutocompleteOff();
        underpaymentAccountLookUp.ensureDebugId("receive_payment-bank_fee/discount_account");
        overpaymentAccountLookUp = new PaymentAccountsLookUp(true, true);
        overpaymentAccountLookUp.setAutocompleteOff();

        paymentDatePicker = new DatePicker(new Date(), true);
        paymentDatePicker.ensureDebugId("receive_payment-paymentdate");
//        paymentDatePicker.addChangeHandler(c -> applyNumberDate());
        amountTxtBox = new TextBox(true);
        amountTxtBox.ensureDebugId("receive_payment-ammount");
        amountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(amountTxtBox, AccountingUtils.getPriceScale());
        amountTxtBox.addChangeHandler(c -> {
            amountTxtBox.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(amountTxtBox.getText())));
        });

        overpaymentAmountTxtBox = new TextBox(true);
        overpaymentAmountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(overpaymentAmountTxtBox, AccountingUtils.getPriceScale());

        overpaymentAccountItem = new FormGroup("Overpayment Account", overpaymentAccountLookUp);
        overpaymentAmountItem = new FormGroup(wfmStrings.amount(), overpaymentAmountTxtBox);

        referenceTxtBox = new TextBox(true);
        referenceTxtBox.ensureDebugId("receive_payment-referrence");
        paymentTypeListBox = new DataListBox();
        paymentTypeListBox.ensureDebugId("receive_payment-paymentType");

        numberTxtBox = new TextBox(true);
        numberTxtBox.ensureDebugId("receive_payment-number");
        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.setAutocompleteOff();
        departmentLookUp.ensureDebugId("receive_payment-department");

        departmentItem = new FormGroup(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentLookUp);
        departmentItem.setVisible(false);

        accountsReceivablePayableLookUp = new AccountsReceivablePayableLookUp(isReceivable ? RECEIVABLE : PAYABLE);
        accountsReceivablePayableLookUp.ensureDebugId("receive_payment-accountLookUp");
        accountsReceivablePayableLookUp.getSuggestBox().setSize("180px", "18px");
        accountsReceivablePayableLookUp.setAutocompleteOff();
        accountsReceivablePayableLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> loadClientSupplierData(false));
    }

    private void onChangeLookUp() {
        if (!Validation.validateLookUpRequired(crmAccountLookUp)) {
            return;
        }
        clearFilterData();
        projectLookUp.clear();
        loadClientSupplierData(true);
        invoiceService.getClientOrSupplier(crmAccountLookUp.getSelectedItemID(), isReceivable ? RECEIVABLE : PAYABLE, new AsyncCallback<TypeItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(TypeItem typeItem) {
                if (typeItem.getSupplierCustomerBalance() >= 0) {
                    customerBalanceLink.setText(AccountingUtils.get().formatPrice(typeItem.getSupplierCustomerBalance()));
                } else {
                    customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * typeItem.getSupplierCustomerBalance()) + ")");
                }
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + crmAccountLookUp.getSelectedItemID() + "/" + CrmAccountItem.CUSTOMER));
            }
        });
        paymentTarget.setSingleValue(INVOICE);

        if (paymentTarget.getSingleValue() != null) {
            projectLookUp.clear();
            editData.clear();
            dynamicTable.clear();
            clearFilterData();
            showOverpaymentPanel(false);
            loadClientSupplierData(false);
        }
    }

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_EDIT, BatchPaymentAddView.this, (sender, args) -> setSupplierClientData((Integer) args, false, Constants.RECEIVABLE));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_ADD, BatchPaymentAddView.this, (sender, args) -> setSupplierClientData((Integer) args, true, Constants.RECEIVABLE));

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_EDIT, BatchPaymentAddView.this, (sender, args) -> setSupplierClientData((Integer) args, false, Constants.PAYABLE));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_ADD, BatchPaymentAddView.this, (sender, args) -> setSupplierClientData((Integer) args, true, Constants.PAYABLE));
    }

    private void setSupplierClientData(Integer args, boolean b, String type) {
        InvoiceService.App.get().getClientOrSupplier(args, type, new AbstractAsyncCallback<TypeItem>() {
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(TypeItem typeItem) {
                crmAccountLookUp.setSelected(typeItem);

                if (currencyWidget != null && typeItem.getCurrencyID() != null) {
                    currencyWidget.setCurrency(typeItem.getCurrencyID());
                }
                onChangeLookUp();
            }
        });
    }

    private void createSearchPanel() {
        projectLookUp = new ProjectLookUp(RECEIVABLE, crmAccountLookUp);
        projectLookUp.ensureDebugId("receive_payment-projectLookUp");
        projectLookUp.setVisible(isReceivable);
        projectLookUp.setAutocompleteOff();

        projectLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            editData.clear();
            dynamicTable.clear();
            clearFilterData();
            loadClientSupplierData(false);
        });

    }

    private void createDynamicTable() {
        dynamicTable = new DynamicTable(getColumns(), false);
        dynamicTable.ensureDebugId("Payment_Table");
        scrollDynamicTable = new ScrollPanel();
        scrollDynamicTable.add(dynamicTable);
    }

    private void reGenerateDynamicTableColumns() {
        dynamicTable = new DynamicTable(getColumns(), false);
        dynamicTable.addStyleName("payInvoiceTable file--BatchPaymentAddView");
        dynamicTable.ensureDebugId("Payment_Table");
        scrollDynamicTable.clear();
        scrollDynamicTable.add(dynamicTable);
    }

    private DynamicTableColumn[] getColumns() {
        ArrayList<DynamicTableColumn> columns = new ArrayList<>();

        columns.add(new DynamicTableColumn(wfmStrings.selectAll(), ENABLE_COLUMN, 20, onExchangeSelectAll -> onExchangeSelectAll()));

        if (showSubAccountTransaction != null && showSubAccountTransaction.getValue()) {
            columns.add(new DynamicTableColumn(isReceivable ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), CLIENT_SUPPLIER, 200));
        }
        columns.add(new DynamicTableColumn(wfmStrings.description(), DESCRIPTION_COLUMN, 200, sortDirection -> sortItemTable(DESCRIPTION_COLUMN, sortDirection)));
        columns.add(new DynamicTableColumn(wfmStrings.reference(), REFERENCE_COLUMN, 290));
        columns.add(new DynamicTableColumn(wfmStrings.date(), DATE_COLUMN, 120, sortDirection -> sortItemTable(DATE_COLUMN, sortDirection)));

        columns.add(new DynamicTableColumn(wfmStrings.dueAmount(), AMOUNT_COLUMN, 120, Constants.RIGHT_ALIGN_CELL, sortDirection -> sortItemTable(AMOUNT_COLUMN, sortDirection)));
        columns.add(new DynamicTableColumn(wfmStrings.paymentAmount(), PAYMENT_AMOUNT, 120, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(accountingStrings.bankFeeDiscount(), UNDER_PAYMENT_AMOUNT, 120, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(accountingStrings.bankFeeTaxRate(), BANK_FEE_TAX_RATE_COLUMN, 80, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(accountingStrings.bankFeeTaxAmount(), BANK_FEE_TAX_AMOUNT_COLUMN, 100, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(wfmStrings.close(), CLOSE_COLUMN, 50));
        return columns.toArray(new DynamicTableColumn[columns.size()]);
    }

    private void sortItemTable(String sortField, String sortDirection) {
        this.sortField = sortField;
        this.sortDirection = sortDirection;
        initFilterData();
        loadClientSupplierData(false);
    }

    private void createFooterPanel() {
        HTML totalAmountLabel = new HTML(wfmStrings.totalAmount());
        HTML totalPaymentAmountLabel = new HTML(wfmStrings.paymentAmount());
        totalAmountLabel.setStyleName(STYLE_TOTAL_LABEL);
        totalPaymentAmountLabel.setStyleName(STYLE_TOTAL_LABEL);

        totalAmountValue = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalAmountValue.ensureDebugId("receive_payment-totalAmmoun");
        totalPaymentAmountValue = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalPaymentAmountValue.ensureDebugId("receive_payment-totalPaymentAmount");
        totalAmountValue.setStyleName(STYLE_TOTAL_VALUE);
        totalPaymentAmountValue.setStyleName(STYLE_TOTAL_VALUE);

        totalTable = new TotalTable();
        totalTable.addItem(totalAmountLabel, totalAmountValue);
        totalTable.addItem(totalPaymentAmountLabel, totalPaymentAmountValue);
        saveButton = new WfmButton2(objectID != null ? wfmStrings.save() : wfmStrings.addPayment(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId("receive_payment-saveButton");
        saveButton.addClickHandler(clickEvent -> savePaymentData(true, false));
    }

    private List<Option> getPaymentTargetItems() {
        List<Option> items = new ArrayList<>();
        items.add(INVOICE);
        items.add(MANUAL_ENTRY);
        if (!isReceivable) {
            items.add(EXPENSE);
        }

        return items;
    }

    private void setPaymentTarget(String code) {
        if (code == null) {
            return;
        }
        Set<Option> items = new HashSet<>();
        if (code.contains(AccountingConstants.PAYMENT_TARGET_INVOICE)) {
            items.add(INVOICE);
        }
        if (code.contains(AccountingConstants.PAYMENT_TARGET_MANUAL_JOURNAL)) {
            items.add(MANUAL_ENTRY);
        }
        if (code.contains(AccountingConstants.PAYMENT_TARGET_EXPENSE)) {
            items.add(EXPENSE);
        }

        // --> for old payments
        if (code.contains(AccountingConstants.PAYMENT_TARGET_INVOICE_AND_MANUAL_JOURNAL)) {
            items.add(INVOICE);
            items.add(MANUAL_ENTRY);
        }
        if (code.contains(Constants.EXPENSES)) {
            items.add(EXPENSE);
        }
        // -->
        paymentTarget.setValues(new ArrayList<>(items));
    }

    private String getPaymentTarget() {
        return paymentTarget.getSelectedValues().stream().map(Option::getCode).collect(Collectors.joining(","));
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return BatchPaymentAddView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return BatchPaymentAddView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterLeftSideWidgets() {
        List<Widget> result = new ArrayList<>();
        result.add(footerUploadPanel);
        FooterInformer footerIntroduction = new FooterInformer(SvgEnum.docTitle, wfmStrings.description(), description.getIntroduction());
        footerIntroduction.setInitialClasses("informer-item");
        result.add(footerIntroduction);
        return result;
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();

        Div saveWrapper = new Div();
        Div closeWrapper = new Div();

        saveWrapper.add(saveButton);

        result.add(saveWrapper);
        result.add(closeWrapper);
        return result;
    }

    private void clearFilterData() {
        filterData.setSearchKey("");
        filterData.setStartAmount(null);
        filterData.setEndAmount(null);
        filterData.setStartDate(null);
        filterData.setEndDate(null);
        filterData.setSortField(null);
        filterData.setSortDirection(null);
        totalAmountValue.setHTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalPaymentAmountValue.setHTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
    }

    private void initFilterData() {
        filterData.setReceivablePayableID(accountsReceivablePayableLookUp.getSelectedItemID());
        filterData.setSortField(this.sortField);
        filterData.setSortDirection(this.sortDirection);
    }

    private void loadClientSupplierData(final boolean isCrmAccountChange) {
        if (paymentTarget.getSelectedValues().isEmpty()) {
            return;
        }
        saveButton.setEnabled(false);
        LoadingPanel.loading(true);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCrmAccountId(crmAccountLookUp.getSelectedItemID());
        fp.setCurrencyID(isCrmAccountChange ? null : currencyWidget.getCurrencyID());
        fp.setExchangeRate(isCrmAccountChange ? null : currencyWidget.getExchangeRate());
        fp.setReceivable(isReceivable);
        fp.setOptions(paymentTarget.getSelectedValues().stream().map(Option::getCode).collect(Collectors.toCollection(ArrayList::new)));
        fp.setProjectId(projectLookUp.getSelectedItemID());
        fp.setShowSubAccountTransaction(showSubAccountTransaction.getValue());

        invoiceService.getReceivePaymentData(fp, filterData, new AbstractAsyncCallback<ReceivePaymentData>() {
            @Override
            public void failure(Throwable throwable) {
                saveButton.setEnabled(true);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ReceivePaymentData paymentData) {

                if (isCrmAccountChange) {
                    editData.clear();
                    currencyWidget.setCurrency(paymentData.getCurrency().getId());
                }

                dynamicTable.clear();
                objPaymentData.clear();
                if (objectID == null) {
                    for (PaymentData data : paymentData.getPayments()) {
                        dynamicTable.addRow(getWidgets(data));
                        objPaymentData.add(data);
                    }
                } else {
                    for (PaymentData data : editData.values()) {
                        if (data != null) {
                            dynamicTable.addRow(getWidgets(data));
                            objPaymentData.add(data);
                        }
                    }

//                    for (PaymentData data : paymentData.getPayments()) {
//                        if (data != null && !editData.containsKey(data.getKeyForMap())) {
//                            dynamicTable.addRow(getWidgets(data));
//                            objPaymentData.add(data);
//                        }
//                    }
                }
                calculate();
                saveButton.setEnabled(true);
                LoadingPanel.loading(false);
            }
        });
    }

    private Widget[] getWidgets(final PaymentData data) {
        //checkbox column
        ExtendedCheckBox enableCheckBox = new ExtendedCheckBox(data);
        //customer column
        Label customerSupplier = new Label();
        //description column
        Label description = new Label();
        //reference column
        TextBox txtReference = new TextBox();
        //Invoice date column
        Label invoiceDate = new Label();
        //Amount column
        Label invoiceAmount = new Label();
        //Payment Amount column
        CustomCellTextBox paymentAmount = new CustomCellTextBox();
        //BankFee/Discount Amount column
        CustomCellTextBox underpaymentAmount = new CustomCellTextBox();
        //Bank Fee Tax Rate column
        CustomCellTextBox bankFeeTaxRateTxtBox = new CustomCellTextBox();
        //Bank Fee Tax Amount column (readonly, auto-calculated)
        Label bankFeeTaxAmountLabel = new Label(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        //Close Column
        CloseCheckBox closeCheckBox = new CloseCheckBox(data);


        description.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        description.getElement().getStyle().setColor("#2C5FE1");
        description.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        txtReference.setEnabled(false);


        Validation.addNumericKeyboardListener(paymentAmount, AccountingUtils.calculationScale);
        Validation.checkToFocusTextBox(paymentAmount, AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        paymentAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        paymentAmount.setEnabled(false);
        Validation.addNumericKeyboardListener(paymentAmount, AccountingUtils.getPriceScale());
        paymentAmount.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));

        Validation.addNumericKeyboardListener(underpaymentAmount, AccountingUtils.calculationScale);
        Validation.checkToFocusTextBox(underpaymentAmount, AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        underpaymentAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        underpaymentAmount.setEnabled(false);
        Validation.addNumericKeyboardListener(underpaymentAmount, AccountingUtils.getPriceScale());
        underpaymentAmount.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));

        Validation.addNumericKeyboardListener(bankFeeTaxRateTxtBox, AccountingUtils.getPriceScale());
        bankFeeTaxRateTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        bankFeeTaxRateTxtBox.setEnabled(false);

        final Runnable recalcBankFeeTax = () -> {
            BigDecimal bankFeeAmt = AccountingUtils.get().parseToBigDecimal(underpaymentAmount.getValue());
            String rateText = bankFeeTaxRateTxtBox.getText();
            if (!Utils.isNullOrEmpty(rateText)) {
                BigDecimal rate = AccountingUtils.get().parseToBigDecimal(rateText);
                BigDecimal taxAmt = bankFeeAmt.multiply(rate).divide(new BigDecimal("100"), AccountingUtils.getPriceScale(), RoundingMode.HALF_UP);
                bankFeeTaxAmountLabel.setText(AccountingUtils.get().formatPrice(taxAmt));
            } else {
                bankFeeTaxAmountLabel.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
            }
        };

        bankFeeTaxRateTxtBox.addKeyUpHandler(keyUpEvent -> recalcBankFeeTax.run());

        underpaymentAmount.addKeyUpHandler(keyUpEvent -> {
            countCheckedCloseButton++;
            BigDecimal paymentAmount_ = AccountingUtils.get().parseToBigDecimal(paymentAmount.getValue());
            BigDecimal underPaymentAmount_ = AccountingUtils.get().parseToBigDecimal(underpaymentAmount.getValue());
            BigDecimal dataTotal = data.getTotal().setScale(2, RoundingMode.HALF_UP);
            BigDecimal sum = paymentAmount_.add(underPaymentAmount_).setScale(2, RoundingMode.HALF_UP);
            if (dataTotal.compareTo(sum) == 0) {
                underpaymentAmount.setEnabled(false);
                closeCheckBox.setValue(true);
            }
            recalcBankFeeTax.run();
        });

        closeCheckBox.addStyleName("closeCheckBox");
        closeCheckBox.setEnabled(false);

        paymentAmount.addKeyUpHandler(keyUpEvent -> calculateEnteredPayments());
        paymentAmount.addChangeHandler(changeEvent -> {
            if (data != null
                    && (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(data.getType()) || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(data.getType()))
                    && data.getAppliedAmount() != null) {
                BigDecimal pAmount = AccountingUtils.get().parseToBigDecimal(paymentAmount.getText());

                if (data.getAppliedAmount().compareTo(pAmount) > 0) {
                    Info.show(accountingStrings.totalPrepaymentLessThanAmountAppliedErrorMessage(), Info.Type.WARNING);
                    paymentAmount.setText(AccountingUtils.get().format(data.getPaymentAmount()));
                }
            }
        });

        closeCheckBox.addValueChangeHandler(valueChangeEvent -> {
            underpaymentAmount.setEnabled(!closeCheckBox.getValue());
            if (!enableCheckBox.getValue()) {
                return;
            }
            if (closeCheckBox.getValue()) {
                countCheckedCloseButton++;
                if (data.isPaymentDiffCurrency()) {
                    BigDecimal exchangeRate = data.getExchangeRate();
                    BigDecimal underPaymentAmount_ = AccountingUtils.get().parseToBigDecimal(underpaymentAmount.getText());
                    if (currencyWidget.getBaseCurrency().getId().equals(currencyWidget.getCurrencyID())) {
                        data.setUnderPaymentAmountInInvoiceCurrency(underPaymentAmount_.multiply(exchangeRate));
                    } else {
                        data.setUnderPaymentAmountInInvoiceCurrency(underPaymentAmount_.divide(exchangeRate, AccountingUtils.calculationScale, RoundingMode.HALF_UP));
                    }
                }
                BigDecimal paymentAmount_ = AccountingUtils.get().parseToBigDecimal(paymentAmount.getValue());
                BigDecimal dueAmount = data.getTotal().subtract(paymentAmount_);
                if (dueAmount.compareTo(BigDecimal.ZERO) > 0) {
                    underpaymentAmount.setValue(AccountingUtils.get().formatPrice(dueAmount));
                }
                recalcBankFeeTax.run();
            } else {
                underpaymentAmount.setValue(AccountingUtils.getZero());
                bankFeeTaxAmountLabel.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
                countCheckedCloseButton--;
            }
        });

        enableCheckBox.addValueChangeHandler(booleanValueChangeEvent -> {
            onCheckBoxValueChange(data, enableCheckBox, txtReference, paymentAmount, underpaymentAmount, closeCheckBox);
            bankFeeTaxRateTxtBox.setEnabled(enableCheckBox.getValue());
            if (!enableCheckBox.getValue()) {
                bankFeeTaxAmountLabel.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
            }
        });
        if (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(data.getType()) || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(data.getType())) {
            enableCheckBox.setEnabled(false);
        }

        if (data.getInvoiceNumber() != null) {
            description.setText(data.getInvoiceNumber());
            description.addClickHandler(clickEvent -> {
                String url;
                if (data.isExpensePayment()) {
                    url = "expenseReports|previewReport/" + data.getInvoiceID() + "/EXPENSE_VIEW/ACCOUNTING";
                    SinksContainerFactory.entryPoint.onHistoryChanged(url, data.getInvoiceNumber());
                } else if (data.isManualJournal()) {
                    url = "manual|summary/" + data.getInvoiceID();
                    SinksContainerFactory.entryPoint.onHistoryChanged(url, data.getInvoiceNumber());
                } else if (data.isOpeningBalance()) {
                    //no link
                } else if (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(data.getType()) || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(data.getType())) {
                    url = "invoicepayment|paymentView/" + data.getObjectID();
                    SinksContainerFactory.entryPoint.onHistoryChanged(url);
                } else {
                    url = (isReceivable ? SALE_INVOICE : PURCHASE_INVOICE) + "|summary/" + data.getInvoiceID();
                    SinksContainerFactory.entryPoint.onHistoryChanged(url, data.getInvoiceNumber());
                }
            });
        }
        if (data.getInvoiceDate() != null) {
            invoiceDate.setText(DateUtils.format(data.getInvoiceDate()));
        }
        if (data.getTotal() != null) {
            invoiceAmount.setText(AccountingUtils.get().formatPrice(data.getTotal()));

            if (data.isPaymentDiffCurrency()) {
                invoiceAmount.setStyleName("asLink");
            }
        }
        if (data.getCrmAccount() != null) {
            customerSupplier.setText(data.getCrmAccount().getName());
        }
        if (data.getReferenceNumber() != null) {
            txtReference.setText(data.getReferenceNumber());
        }
        if (data.getPaymentAmount() != null && data.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
            paymentAmount.setText(AccountingUtils.get().formatPrice(data.getPaymentAmountInInvoiceCurrency()!= null? data.getPaymentAmountInInvoiceCurrency() : data.getPaymentAmount()));
            enableCheckBox.setValue(true);
            paymentAmount.setEnabled(true);
            txtReference.setEnabled(true);
        }
        if (data.getUnderPaymentAccount() != null && data.getUnderPaymentAmount() != null) {
            BigDecimal paymentAmount_ = AccountingUtils.get().parseToBigDecimal(paymentAmount.getValue());
            BigDecimal underPaymentAmount_ = data.getUnderPaymentAmountInInvoiceCurrency() != null ? data.getUnderPaymentAmountInInvoiceCurrency() : data.getUnderPaymentAmount();
            BigDecimal dataTotal = data.getTotal().setScale(2, RoundingMode.HALF_UP);
            BigDecimal sum = paymentAmount_.add(underPaymentAmount_).setScale(2, RoundingMode.HALF_UP);
            if (dataTotal.compareTo(sum) == 0) {
                closeCheckBox.setValue(true);
            }
            underpaymentAccountLookUp.setSelected(data.getUnderPaymentAccount());
            underpaymentAmount.setValue(AccountingUtils.get().formatPrice(data.getUnderPaymentAmountInInvoiceCurrency() != null ? data.getUnderPaymentAmountInInvoiceCurrency() : data.getUnderPaymentAmount()));
            countCheckedCloseButton++;
        }
        if (data.getUnderPaymentTaxRate() != null) {
            bankFeeTaxRateTxtBox.setText(AccountingUtils.get().formatQty(data.getUnderPaymentTaxRate()));
            bankFeeTaxRateTxtBox.setEnabled(true);
            if (data.getUnderPaymentTaxAmount() != null) {
                bankFeeTaxAmountLabel.setText(AccountingUtils.get().formatPrice(data.getUnderPaymentTaxAmount()));
            }
        }

        if (showSubAccountTransaction.getValue()) {
            return new Widget[]{enableCheckBox, customerSupplier, description, txtReference, invoiceDate, invoiceAmount, paymentAmount, underpaymentAmount, bankFeeTaxRateTxtBox, bankFeeTaxAmountLabel, closeCheckBox};
        } else {
            return new Widget[]{enableCheckBox, description, txtReference, invoiceDate, invoiceAmount, paymentAmount, underpaymentAmount, bankFeeTaxRateTxtBox, bankFeeTaxAmountLabel, closeCheckBox};
        }
    }

    private void onExchangeRateChange() {
        for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
            DynamicTableItem item = dynamicTable.getItem(rowID);
            ExtendedCheckBox checkBox = (ExtendedCheckBox) item.getColumnById(ENABLE_COLUMN);

            if (!currencyWidget.getBaseCurrency().getId().equals(currencyWidget.getCurrencyID()) &&
                    currencyWidget.getExchangeRate().compareTo(checkBox.getPaymentData().getExchangeRate()) != 0) {
                Label invoiceAmount = (Label) item.getColumnById(AMOUNT_COLUMN);
                TextBox paymentAmount = (TextBox) item.getColumnById(PAYMENT_AMOUNT);

                BigDecimal total = AccountingUtils.get().parseToBigDecimal(invoiceAmount.getText());
                BigDecimal payment = AccountingUtils.get().parseToBigDecimal(paymentAmount.getText());

                total = total.divide(checkBox.getPaymentData().getExchangeRate(), AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                total = total.multiply(currencyWidget.getExchangeRate());
                payment = payment.divide(checkBox.getPaymentData().getExchangeRate(), AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                payment = payment.multiply(currencyWidget.getExchangeRate());

                invoiceAmount.setText(AccountingUtils.get().formatPrice(total));
                paymentAmount.setText(AccountingUtils.get().formatPrice(payment));
            }
        }
        paymentAccountLookUp.setCurrencyID(currencyWidget.getCurrencyID());
        loadClientSupplierData(false);
    }

    private void onExchangeSelectAll() {

        for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
            DynamicTableItem item = dynamicTable.getItem(rowID);
            PaymentData data = objPaymentData.get(rowID);
            ExtendedCheckBox enableColumn = (ExtendedCheckBox) item.getColumnById(ENABLE_COLUMN);
            TextBox refColumn = (TextBox) item.getColumnById(REFERENCE_COLUMN);
            CustomCellTextBox payColumn = (CustomCellTextBox) item.getColumnById(PAYMENT_AMOUNT);
            CustomCellTextBox bankFeeColumn = (CustomCellTextBox) item.getColumnById(UNDER_PAYMENT_AMOUNT);
            CloseCheckBox closeColumn = (CloseCheckBox) item.getColumnById(CLOSE_COLUMN);
            TextBox taxRateColumn = (TextBox) item.getColumnById(BANK_FEE_TAX_RATE_COLUMN);
            Label taxAmountColumn = (Label) item.getColumnById(BANK_FEE_TAX_AMOUNT_COLUMN);
            enableColumn.setValue(isAll);
            onCheckBoxValueChange(data, enableColumn, refColumn, payColumn, bankFeeColumn, closeColumn);
            if (taxRateColumn != null) {
                taxRateColumn.setEnabled(isAll);
                if (!isAll) {
                    taxAmountColumn.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
                }
            }
        }
        isAll = !isAll;
    }

    private void onCheckBoxValueChange(PaymentData data, ExtendedCheckBox enableCheckBox, TextBox txtReference, CustomCellTextBox paymentAmount, CustomCellTextBox underpaymentAmountTextBox, CloseCheckBox closeCheckBox) {
        boolean enabled = enableCheckBox.getValue();
        txtReference.setEnabled(enabled);

        if (enabled && Utils.isNullOrEmpty(txtReference.getText())) {
            txtReference.setText(referenceTxtBox.getText());
        }
        paymentAmount.setEnabled(enableCheckBox.getValue());
        paymentAmount.setItemFocus(true);
        closeCheckBox.setEnabled(enabled);
        underpaymentAmountTextBox.setEnabled(!closeCheckBox.getValue());
        BigDecimal dueAmount = data.getTotal();

        allocateEnteredAmount(paymentAmount, enableCheckBox.getObjectID(), dueAmount);
        if (!enabled) {
            underpaymentAmountTextBox.setValue(AccountingUtils.getZero());
            paymentAmount.setText(AccountingUtils.getZero());
            if (closeCheckBox.getValue()) {
                countCheckedCloseButton--;
            }
            closeCheckBox.setValue(false);
            editData.remove(data.getKeyForMap());
        }
        calculate();
    }

    private void applyNumberDate() {
        if (numberData != null) {
            data.setNumberData(numberData);
            numberTxtBox.setText(numberData.getTransferNumber());
            if (numberData.isWithDate()) {
                numberData.setDate(paymentDatePicker.getDate() != null ? dateFormat.format(paymentDatePicker.getDate()) : "");
            }
        }
    }

    public static class ExtendedCheckBox extends KpiCheckBox {
        private final PaymentData paymentData;

        ExtendedCheckBox(PaymentData data) {
            this.paymentData = data;
        }

        public Integer getObjectID() {
            return paymentData.getInvoiceID();
        }

        public PaymentData getPaymentData() {
            return paymentData;
        }
    }

    private static class CloseCheckBox extends KpiCheckBox {
        private final PaymentData paymentData;

        private CloseCheckBox(PaymentData data) {
            this.paymentData = data;
        }

        public Integer getObjectID() {
            return paymentData.getInvoiceID();
        }

        private Integer getUnderPaymentObjectID() {
            return paymentData.getUnderPaymentID();
        }

        public PaymentData getPaymentData() {
            return paymentData;
        }
    }

    private void allocateEnteredAmount(TextBox currentPaymentTxtBox, Integer objectID, BigDecimal dueAmount) {
        BigDecimal totalAllocatedAmount = (amountTxtBox.getText() != null && !amountTxtBox.getText().trim().isEmpty()) ? AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()) : BigDecimal.ZERO;
        if (totalAllocatedAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal amountsAllocatedToItems = BigDecimal.ZERO;
            for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
                DynamicTableItem item = dynamicTable.getItem(rowID);
                ExtendedCheckBox checkBox = (ExtendedCheckBox) item.getColumnById(ENABLE_COLUMN);
                if (checkBox.getObjectID().equals(objectID)) {
                    putCurrentRowDataToMap(rowID);
                }
                if (checkBox.getValue() && !checkBox.getObjectID().equals(objectID)) {
                    TextBox paymentAmount = (TextBox) item.getColumnById(PAYMENT_AMOUNT);
                    if (paymentAmount.getText() != null && !paymentAmount.getText().trim().isEmpty()) {
                        amountsAllocatedToItems = amountsAllocatedToItems.add(AccountingUtils.get().parseToBigDecimal(paymentAmount.getText().trim()));
                    }
                }
            }
            BigDecimal unallocatedAmount = totalAllocatedAmount.subtract(amountsAllocatedToItems);
            if (unallocatedAmount.compareTo(dueAmount) > 0) {
                unallocatedAmount = dueAmount;
            }
            currentPaymentTxtBox.setText(AccountingUtils.get().formatPrice(unallocatedAmount));
        }
    }

    private void putCurrentRowDataToMap(int rowID) {
        DynamicTableItem item = dynamicTable.getItem(rowID);
        ExtendedCheckBox checkBox = (ExtendedCheckBox) item.getColumnById(ENABLE_COLUMN);
        Label description = (Label) item.getColumnById(DESCRIPTION_COLUMN);
        Label invoiceAmountTextBox = (Label) item.getColumnById(AMOUNT_COLUMN);
        TextBox paymentAmountTextBox = (TextBox) item.getColumnById(PAYMENT_AMOUNT);
        TextBox txtReference = (TextBox) item.getColumnById(REFERENCE_COLUMN);
        TextBox underpaymentAmount = (TextBox) item.getColumnById(UNDER_PAYMENT_AMOUNT);
        TextBox bankFeeTaxRate = (TextBox) item.getColumnById(BANK_FEE_TAX_RATE_COLUMN);
        Label bankFeeTaxAmount = (Label) item.getColumnById(BANK_FEE_TAX_AMOUNT_COLUMN);
        CloseCheckBox closeCheckBox = (CloseCheckBox) item.getColumnById(CLOSE_COLUMN);

        if (checkBox.getValue()) {
            BigDecimal invoiceAmount = AccountingUtils.get().parseToBigDecimal(invoiceAmountTextBox.getText());
            BigDecimal paymentAmount = AccountingUtils.get().parseToBigDecimal(paymentAmountTextBox.getText());

            if (closeCheckBox.getValue()) {
                underpaymentAmount.setValue(AccountingUtils.get().formatPrice(invoiceAmount.subtract(paymentAmount)));
                if (invoiceAmount.subtract(paymentAmount).compareTo(BigDecimal.ZERO) < 0) {
                    paymentAmountTextBox.setStyleName(ERROR_FORM_STYLE);
                    paymentAmountTextBox.setTitle("Payment amount can not be more then invoice due amount!");
                } else {
                    paymentAmountTextBox.removeStyleName(ERROR_FORM_STYLE);
                    paymentAmountTextBox.setTitle("");
                }
            }
            PaymentData data = new PaymentData();
            data.setObjectID(checkBox.getPaymentData().getObjectID());
            data.setInvoiceID(checkBox.getObjectID());
            data.setInvoiceNumber(description.getText());
            data.setInvoiceDate(checkBox.getPaymentData().getInvoiceDate());
            data.setPaymentDiffCurrency(checkBox.getPaymentData().isPaymentDiffCurrency());
            data.setOpeningBalance(checkBox.getPaymentData().isOpeningBalance());
            data.setManualJournal(checkBox.getPaymentData().isManualJournal());
            data.setExpensePayment(checkBox.getPaymentData().isExpensePayment());
            data.setDate(new DateNonConvertable(paymentDatePicker.getDate()));
            data.setPaymentAccount(paymentAccountLookUp.getSelectedItem());
            data.setReferenceNumber(txtReference.getText() != null ? txtReference.getText() : referenceTxtBox.getText());
            data.setPaymentTypeID(paymentTypeListBox.getSelectedId());
            data.setTotal(invoiceAmount);
            data.setTotalInInvoiceCurrency(data.getTotal());

            if (!(AccountingConstants.RECEIVABLE_PREPAYMENT.equals(checkBox.getPaymentData().getType())
                    || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(checkBox.getPaymentData().getType()))) {
                data.setPaymentAmount(paymentAmount.compareTo(data.getTotal()) > 0 ? data.getTotal() : paymentAmount);
            } else {
                data.setPaymentAmount(paymentAmount);
            }
            data.setPaymentAmountInInvoiceCurrency(data.getPaymentAmount());
            if (closeCheckBox.getValue() || !"".equals(underpaymentAmount.getText())) {
                data.setUnderPaymentAmountInInvoiceCurrency(AccountingUtils.get().parseToBigDecimal(underpaymentAmount.getText()));
            }
            data.setExchangeRate(currencyWidget.getExchangeRate());
            data.setCurrency(new SelectItem(currencyWidget.getCurrencyID()));
            data.setAccountItem(checkBox.getPaymentData().getAccountItem());
            data.setCrmAccount(checkBox.getPaymentData().getCrmAccount());
            data.setType(checkBox.getPaymentData().getType());
            data.setNumber(checkBox.getPaymentData().getNumber());
            data.setIntNumber(checkBox.getPaymentData().getIntNumber());
            data.setPaymentStatus(checkBox.getPaymentData().getPaymentStatus());
            if (data.getTotal().setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)
                    .compareTo(data.getPaymentAmount().setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)) == 0) {
                data.setFullPaid(true);
            }
            if (closeCheckBox.getValue() || !"".equals(underpaymentAmount.getText())) {
                data.setUnderPaymentID(closeCheckBox.getUnderPaymentObjectID());
                data.setUnderPaymentAccount(underpaymentAccountLookUp.getSelectedItem());
                data.setUnderPaymentAmount(AccountingUtils.get().parseToBigDecimal(underpaymentAmount.getText()));
            }
            if (bankFeeTaxRate != null && !Utils.isNullOrEmpty(bankFeeTaxRate.getText())) {
                data.setUnderPaymentTaxRate(AccountingUtils.get().parseToBigDecimal(bankFeeTaxRate.getText()));
                data.setUnderPaymentTaxAmount(AccountingUtils.get().parseToBigDecimal(bankFeeTaxAmount.getText()));
            }
            editData.put(checkBox.getPaymentData().getKeyForMap() + rowID, data);
        } else {
            editData.remove(checkBox.getPaymentData().getKeyForMap() + rowID);
        }
    }

    private void putPrePaymentDataToMap(BigDecimal prePaymentAmount) {
        PaymentData data = new PaymentData();
        data.setInvoiceNumber(description.getIntroduction().getText());
        data.setPaymentAmount(prePaymentAmount);
        data.setPaymentAmountInInvoiceCurrency(data.getPaymentAmount());
        data.setDate(new DateNonConvertable(paymentDatePicker.getDate()));
        data.setPaymentAccount(paymentAccountLookUp.getSelectedItem());
        data.setReferenceNumber(!referenceTxtBox.getText().isEmpty() ? referenceTxtBox.getText() : "Overpayment added as credit to be used later.");
        data.setPaymentTypeID(paymentTypeListBox.getSelectedId());
        data.setExchangeRate(currencyWidget.getExchangeRate());
        data.setCurrency(new SelectItem(currencyWidget.getCurrencyID()));
        data.setCrmAccount(crmAccountLookUp.getSelectedItem());
        data.setType(isReceivable ? AccountingConstants.RECEIVABLE_PREPAYMENT : AccountingConstants.PAYABLE_SUPPLIER_CREDIT);
        data.setPaymentStatus(AccountingConstants.PRE_PAYMENT_OPEN_STATUS);
        editData.put("prePayment", data);
    }

    private void savePaymentData(boolean checkExistingReference, boolean isValid) {
        saveButton.setEnabled(false);
        if (overpaymentAccountLookUp.isVisible()) {
            if (Utils.isNullOrEmpty(overpaymentAmountTxtBox.getText()) || overpaymentAccountLookUp.getSelectedItem() == null) {
                saveButton.setEnabled(true);
                return;
            }
        } else {
            if (!isValid && !validate()) {
                saveButton.setEnabled(true);
                return;
            }
        }

        for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
            putCurrentRowDataToMap(rowID);
        }
        if (editData.isEmpty()) {
            if (amountTxtBox.getText().isEmpty() || AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()).doubleValue() <= 0) {
                Validation.validateTextBoxRequired(amountTxtBox);
                Info.show("Please select an invoice/manual transaction to save payment.", Info.Type.WARNING);
                saveButton.setEnabled(true);
                return;
            } else {
                Info.show(accountingMessages.pleaseCheckItemsForSave(), Info.Type.WARNING);
                saveButton.setEnabled(true);
                return;
            }
        }
        if (currencyWidget.isAccountIsForeign() && currencyWidget.getExchangeRate().compareTo(BigDecimal.ONE) == 0) {
            invoiceService.checkExchangeRate(paymentAccountLookUp.getSelectedItemID(), new DateNonConvertable(paymentDatePicker.getDate()), new AbstractAsyncCallback<CurrencyListItem>() {
                @Override
                public void failure(Throwable throwable) {
                    saveButton.setEnabled(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(CurrencyListItem currencyListItem) {
                    approveExchangeRate(saveButton, currencyListItem, checkExistingReference);
                }
            });

        } else {
            savePayment(checkExistingReference);
        }
    }

    private void savePayment(boolean checkExistingReference) {
        ReceivePaymentData paymentData = new ReceivePaymentData();
        paymentData.setObjectID(!copy ? objectID : null);
        paymentData.setBatchPayment(true);

        Integer intNumber = data.getNumberData().parseNumber(numberTxtBox.getText());
        if (intNumber != null) {
            paymentData.setIntNumber(intNumber);
        }

        paymentData.setNumber(numberTxtBox.getText());
        paymentData.setDescription(description.getIntroduction().getText());
        paymentData.setDepartment(departmentLookUp.getSelectedItem());

        paymentData.setCrmAccount(crmAccountLookUp.getSelectedItem());
        paymentData.setAccount(paymentAccountLookUp.getSelectedItem());
        paymentData.setProject(projectLookUp.getSelectedItem());
        paymentData.setExRate(currencyWidget.getExchangeRate());
        paymentData.setCurrency(new CurrencyItem(currencyWidget.getCurrencyID(), null, null));
        paymentData.setReference(referenceTxtBox.getText());
        paymentData.setDate(new DateNonConvertable(paymentDatePicker.getDate()));
        paymentData.setPaymentMethod(paymentTypeListBox.getSelectedItem());
        paymentData.setTotalAmount(AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()));
        paymentData.setPayments(editData.values().toArray(new PaymentData[]{}));
        paymentData.setForeignAccExRate(foreignAccExRate);
        paymentData.setValidateReferences(checkExistingReference);
        paymentData.setAccountsReceivablePayable(accountsReceivablePayableLookUp.getSelectedItem());
        paymentData.setType(isReceivable ? RECEIVABLE : PAYABLE);
        paymentData.setIncludeSubAccountTransaction(showSubAccountTransaction.getValue());

        if (footerUploadPanel != null && footerUploadPanel.getAttachedFiles() != null && footerUploadPanel.getAttachedFiles().length > 0) {
            paymentData.setAttachments(footerUploadPanel.getAttachedFiles());
        }
        paymentData.setPaymentTarget(getPaymentTarget());
        if (customFieldsView != null) {
            paymentData.setCustomFieldItems(customFieldsView.getData());
        }

        if (overpaymentAccountLookUp.getSelectedItem() != null && !Utils.isNullOrEmpty(overpaymentAmountTxtBox.getText())) {
            PaymentData overPaymentData = null;
            if (objectID != null) {
                overPaymentData = data.getData().getOverPayment();
            }
            if (overPaymentData == null) {
                overPaymentData = new PaymentData();
            }
            overPaymentData.setBatchPaymentID(!copy ? objectID : null);
            overPaymentData.setPaymentAccount(paymentAccountLookUp.getSelectedItem());
            overPaymentData.setOverPaymentAccount(overpaymentAccountLookUp.getSelectedItem());
            overPaymentData.setOverPaymentAmount(AccountingUtils.get().parseToBigDecimal(overpaymentAmountTxtBox.getText()));
            overPaymentData.setDate(new DateNonConvertable(paymentDatePicker.getDate()));
            overPaymentData.setReferenceNumber(!Utils.isNullOrEmpty(referenceTxtBox.getText()) ? referenceTxtBox.getText() : wfmStrings.overpayment());
            overPaymentData.setPaymentTypeID(paymentTypeListBox.getSelectedId());
            overPaymentData.setExchangeRate(currencyWidget.getExchangeRate());
            overPaymentData.setCurrency(new SelectItem(currencyWidget.getCurrencyID()));
            overPaymentData.setPaymentDiffCurrency(!currencyWidget.getBaseCurrency().getId().equals(currencyWidget.getCurrencyID()));
            overPaymentData.setCrmAccount(crmAccountLookUp.getSelectedItem());
            overPaymentData.setType(isReceivable ? AccountingConstants.RECEIVABLE_OVERPAYMENT : AccountingConstants.PAYABLE_OVERPAYMENT);

            paymentData.setOverPayment(overPaymentData);
        }

        LoadingPanel.loading(true);

        invoiceService.saveReceivePaymentData(paymentData, isReceivable, new AbstractAsyncCallback<BatchPaymentResult>() {
            @Override
            public void failure(Throwable throwable) {
                saveButton.setEnabled(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(BatchPaymentResult result) {
                saveButton.setEnabled(true);
                LoadingPanel.loading(false);
                if (result.getResult() != null && result.getResult().equals(-1)) {
                    Info.show(wfmStrings.numberAlreadyExist(), Info.Type.WARNING);
                    return;
                }
                if (result.getDuplicatedReferences() != null && result.getDuplicatedReferences().length > 0) {
                    StringBuilder references = new StringBuilder();
                    for (int i = 0; i < result.getDuplicatedReferences().length; i++) {
                        if (i != 0) {
                            references.append(", ");
                        }
                        references.append(result.getDuplicatedReferences()[i]);
                    }
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.setMessage(accountingMessages.referenceWithNumberExists(references.toString()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            savePaymentData(false, false);
                        }
                    });
                    messageBox.setWidth("300px");
                    messageBox.open();
                    return;
                }

                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, null, BatchPaymentAddView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, null, BatchPaymentAddView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, null, BatchPaymentAddView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_TO_BANK_ACCOUNT, null, BatchPaymentAddView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_TRANSFER_LIST_UPDATE, result, BatchPaymentAddView.this);

                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.payment()), Info.Type.INFO);
                closeTab();
            }
        });
    }

    private void approveExchangeRate(WfmButton2 saveButton, CurrencyListItem currencyListItem, boolean checkExistingReference) {
        KpiModal dialogBox = new KpiModal();
        dialogBox.setWidth(600);
        dialogBox.setTitle(wfmStrings.setExchangeRate());

        HTML infoText = new HTML("<p>" + accountingStrings.approveThisExRate() + "</p>");
        dialogBox.add(infoText);

        HorizontalPanelDiv manualPanel = new HorizontalPanelDiv();
        manualPanel.getElement().getStyle().setMarginLeft(101, Style.Unit.PX);
        manualPanel.addStyleName("mt-2 mb-1");

        manualPanel.add(new HTML(currencyListItem.getBaseCurrency().getName() + " = "));

        TextBox exchnageRate = new TextBox();
        exchnageRate.setWidth("200px");
        exchnageRate.setText(currencyListItem.getExchangeRate().toString());
        Validation.addNumericKeyboardListener(exchnageRate, AccountingUtils.calculationScale);
        exchnageRate.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        manualPanel.add(exchnageRate);

        manualPanel.add(new HTML(" " + currencyListItem.getCurrency().getName()));

        dialogBox.add(manualPanel);

        WfmButton2 saveBtn = new WfmButton2(wfmStrings.continueOnly(), WfmButton2.BTN_PRIMARY);
        saveBtn.addClickHandler(click -> {
            saveBtn.setEnabled(false);
            foreignAccExRate = new BigDecimal(exchnageRate.getText());
            savePayment(checkExistingReference);
        });
        dialogBox.addButton(saveBtn);

        WfmButton2 closeBtn = new WfmButton2(wfmStrings.cancel(), BTN_DEFAULT_OUTLINE);
        closeBtn.addClickHandler(click -> {
            saveButton.setEnabled(true);
            dialogBox.close();
        });
        dialogBox.addButton(closeBtn);

        dialogBox.open();
    }

    private void calculate() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalPaymentAmount = BigDecimal.ZERO;
        for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
            DynamicTableItem item = dynamicTable.getItem(rowID);
            ExtendedCheckBox checkBox = (ExtendedCheckBox) item.getColumnById(ENABLE_COLUMN);
            if (checkBox.getValue()) {
                Label invoiceAmount = (Label) item.getColumnById(AMOUNT_COLUMN);
                TextBox paymentAmount = (TextBox) item.getColumnById(PAYMENT_AMOUNT);
                if (invoiceAmount.getText() != null && !invoiceAmount.getText().trim().isEmpty()) {
                    totalAmount = totalAmount.add(AccountingUtils.get().parseToBigDecimal(invoiceAmount.getText()));
                }
                if (paymentAmount.getText() != null && !paymentAmount.getText().trim().isEmpty()) {
                    totalPaymentAmount = totalPaymentAmount.add(AccountingUtils.get().parseToBigDecimal(paymentAmount.getText()));
                }
            }
        }

        totalAmountValue.setHTML(AccountingUtils.get().formatPrice(totalAmount));
        totalPaymentAmountValue.setHTML(AccountingUtils.get().formatPrice(totalPaymentAmount));
    }

    private void calculateEnteredPayments() {
        BigDecimal amountsAllocatedToItems = BigDecimal.ZERO;
        for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
            DynamicTableItem item = dynamicTable.getItem(rowID);
            ExtendedCheckBox checkBox = (ExtendedCheckBox) item.getColumnById(ENABLE_COLUMN);

            if (checkBox.getValue()) {
                TextBox paymentAmount = (TextBox) item.getColumnById(PAYMENT_AMOUNT);
                if (!Utils.isNullOrEmpty(paymentAmount.getText())) {
                    putCurrentRowDataToMap(rowID);
                    amountsAllocatedToItems = amountsAllocatedToItems.add(AccountingUtils.get().parseToBigDecimal(paymentAmount.getText().trim()));
                }
            }
        }
        totalPaymentAmountValue.setHTML(AccountingUtils.get().formatPrice(amountsAllocatedToItems));
        amountTxtBox.setText(AccountingUtils.get().formatPrice(amountsAllocatedToItems));
    }

    private void showOverpaymentPanel(boolean isVisible) {
        overpaymentAccountLookUp.setVisible(isVisible);
        if (!isVisible) {
            overpaymentAccountLookUp.clear();
        }

        overpaymentAmountTxtBox.setVisible(isVisible);
        if (!isVisible) {
            overpaymentAmountTxtBox.setText("");
        }
        overpaymentAccountItem.setVisible(isVisible);
        overpaymentAmountItem.setVisible(isVisible);
    }

    private boolean validate() {
        StringBuilder numbers = new StringBuilder();
        int errors = 0, dateValidationErrors = 0;
        int amountValidationErrors = 0;
        boolean validateCrmAccount = getPaymentTarget().contains(AccountingConstants.PAYMENT_TARGET_INVOICE) || getPaymentTarget().contains(AccountingConstants.PAYMENT_TARGET_MANUAL_JOURNAL);
        BigDecimal totalPaymentAmount = BigDecimal.ZERO;
        if (paymentDatePicker.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(paymentDatePicker.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(isReceivable ? accountingStrings.receivePayment() : property.getSingular(accountingStrings.payBill()), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        if (!Validation.validateTextBoxRequired(numberTxtBox)) {
            errors++;
        }
        errors += validateSystemCustomFields() ? 0 : 1;
        if (validateCrmAccount && !Validation.validateLookUpRequired(crmAccountLookUp)) {
            errors++;
        }
        if (paymentTarget.getSelectedValue().isEmpty()) {
            errors++;
        }
        if (countCheckedCloseButton > 0 && !Validation.validateLookUpRequired(underpaymentAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(paymentAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateDate(paymentDatePicker)) {
            errors++;
        }
        if (customFieldsView != null && !customFieldsView.validateRequiredFields()) {
            advancedOptions.getCustomFieldContainer().setActive(0);
            showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions);
            errors++;
        }
        boolean hasItems = false;
        for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
            DynamicTableItem item = dynamicTable.getItem(rowID);

            ExtendedCheckBox checkBox = (ExtendedCheckBox) item.getColumnById(ENABLE_COLUMN);
            Label invoiceAmount = (Label) item.getColumnById(AMOUNT_COLUMN);
            TextBox paymentAmount = (TextBox) item.getColumnById(PAYMENT_AMOUNT);

            if (checkBox.getValue()) {

                hasItems = true;

                if (!Validation.validateTextBoxRequired(paymentAmount)) {
                    errors++;
                } else if (!(AccountingConstants.RECEIVABLE_PREPAYMENT.equals(checkBox.getPaymentData().getType())
                        || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(checkBox.getPaymentData().getType())) &&
                        AccountingUtils.get().parseToBigDecimal(paymentAmount.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)
                                .compareTo(AccountingUtils.get().parseToBigDecimal(invoiceAmount.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)) > 0) {
                    amountValidationErrors++;
                }

                if (paymentDatePicker.getDate() != null && !(AccountingConstants.RECEIVABLE_PREPAYMENT.equals(checkBox.getPaymentData().getType())
                        || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(checkBox.getPaymentData().getType()))) {
                    DateUtil.resetTime(paymentDatePicker.getDate());

                    if (checkBox.getPaymentData().getInvoiceDate() != null && paymentDatePicker.getDate().compareTo(DateUtil.resetTime(checkBox.getPaymentData().getInvoiceDate().getNonConvertedDate())) < 0) {
                        dateValidationErrors++;

                        if (!numbers.toString().isEmpty()) {
                            numbers.append(", ");
                        }
                        numbers.append(checkBox.getPaymentData().getInvoiceNumber());
                    }
                }

                if (!(AccountingConstants.RECEIVABLE_PREPAYMENT.equals(checkBox.getPaymentData().getType())
                        || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(checkBox.getPaymentData().getType()))) {

                    BigDecimal invoiceTotal = AccountingUtils.get().parseToBigDecimal(invoiceAmount.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    BigDecimal invoicePaymentTotal = AccountingUtils.get().parseToBigDecimal(paymentAmount.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    totalPaymentAmount = totalPaymentAmount.add(invoicePaymentTotal.compareTo(invoiceTotal) > 0 ? invoiceTotal : invoicePaymentTotal);
                } else {
                    totalPaymentAmount = totalPaymentAmount.add(AccountingUtils.get().parseToBigDecimal(paymentAmount.getText()));
                }
            }
        }

        if (!hasItems) {
            if (!Validation.validateTextBoxRequired(amountTxtBox)) {
                errors++;
            }
        }

        BigDecimal amount = AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText());
        totalPaymentAmount = totalPaymentAmount.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
        int comparison = amount.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(totalPaymentAmount);
        if ((!validateCrmAccount && comparison != 0) || (validateCrmAccount && comparison < 0)) {
            Info.show(accountingMessages.paymentAmountMustMatch(), Info.Type.WARNING);
            return false;
        } else if (comparison > 0) {
            amountValidationErrors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.fillRequiredField());
            return false;
        }
        if (amountValidationErrors > 0) {
            final BigDecimal prePaymentAmount = amount.subtract(totalPaymentAmount).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
            KpiModal dialogBox = new KpiModal();
            dialogBox.setWidth(400);

            HTML center = new HTML("<table align='center' cellspacing='5' cellpadding='0'><tr><td rowspan='2'></td><td>" + accountingMessages.overpaymant(currencyWidget.getCurrencyName(), AccountingUtils.get().format(prePaymentAmount)) + "</td></tr></table>");
            dialogBox.add(center);

            WfmButton2 prePaymentBtn = new WfmButton2(accountingStrings.prepayment());
            WfmButton2 overPaymentBtn = new WfmButton2(wfmStrings.overpayment());
            WfmButton2 cancelBtn = new WfmButton2(wfmStrings.cancel());
            HorizontalPanel buttonPanel = new HorizontalPanel();
            buttonPanel.add(prePaymentBtn);
            buttonPanel.add(overPaymentBtn);
            buttonPanel.add(cancelBtn);
            buttonPanel.setSpacing(4);

            dialogBox.addButton(buttonPanel);
            dialogBox.open();

            prePaymentBtn.addClickHandler(clickEvent -> {
                dialogBox.close();
                putPrePaymentDataToMap(prePaymentAmount);
                savePaymentData(false, true);
            });
            overPaymentBtn.addClickHandler(clickEvent -> {
                dialogBox.close();
                showOverpaymentPanel(true);
                Validation.validateLookUpRequired(overpaymentAccountLookUp);
                overpaymentAmountTxtBox.setText(AccountingUtils.get().formatPrice(prePaymentAmount));
            });
            cancelBtn.addClickHandler(clickEvent -> dialogBox.close());
            return false;
        }

        if (dateValidationErrors > 0) {
            Info.show(accountingMessages.datePaidMessage(numbers.toString()), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(() -> {
            List<Widget> result = new ArrayList<>();

            if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) { //Utils.isProjectInLineItemEnable() shu shartni olib tashladim Komronaka request
                FormGroup project = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp);
                result.add(project);
            }
            FormGroup paymentType = new FormGroup(wfmStrings.paymentType(), paymentTypeListBox);
            result.add(paymentType);

            showSubAccountTransaction = new KpiCheckBox(accountingStrings.includeSubAccountTransactions());
            showSubAccountTransaction.ensureDebugId("receive_payment-includeSubAccountTransactions");
            showSubAccountTransaction.addValueChangeHandler(valueChangeEvent -> {
                if (!Validation.validateLookUpRequired(crmAccountLookUp)) {
                    return;
                }
                reGenerateDynamicTableColumns();
                loadClientSupplierData(false);
            });

            result.add(showSubAccountTransaction);
            return result;
        }, false);
    }

    @Override
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

    @Override
    public String getPropertyCode() {
        return Constants.PAYBILLS_LIST;
    }
}
