package com.edatasite.workforce.gwt.invoice.client.ui.view.payment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.Option;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.IntroductionPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.combobox.MaterialComboBox;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddPaymentRefund extends CustomForm2 implements Colapse, FittedContent, Constants, AccountingConstants {

    public static AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private final Option PREPAYMENT;
    private final Option CREDIT_NOTE;

    private final boolean isReceivable;
    private CrmAccountLookUp crmAccountLookUp;
    private DatePicker date;
    private TextBox reference;

     /*
      Everything related to BankAccount and BankAccountLookUp has been removed due to the fact that
       customer refund view had issues with getting all accounts, and it was only projecting Bank accounts.
      */

    // private BankAccountLookUp bankAccountLookUp;
    private final LinkedHashMap<String, PaymentData> refundItems = new LinkedHashMap<>();
    private CurrencyWidget currencyWidget;
    private MaterialComboBox<Option> paymentTarget;
    private DynamicTable dynamicTable;
    private ScrollPanel scrollDynamicTable;
    private CurrencyItem baseCurrency;
    private CurrencyItem[] currencyItems;

    private TotalTable totalTable;
    private HTML totalAmountValue, totalRefundAmountHTML;
    private BigDecimal totalRefundAmount = BigDecimal.ZERO;
    private BigDecimal totalCloseAmount = BigDecimal.ZERO;
    private WfmButton2 saveButton;
    private IntroductionPanel description;
    private TextBox number;
    private Integer objectID;
    private Integer prepaymentId;
    // private Integer bankAccountId;
    private Integer accountId;
    private ReceivePaymentData data;
    private PaymentAccountsLookUp paymentAccountsLookUp;
    private FormGroup closedAccountItem;
    private FormGroup closedAmountItem;
    private PaymentAccountsLookUp closedAccountLookUp;
    private HTML closedAmountTxtBox;


    public AddPaymentRefund(boolean isReceivable, String[] params) {
        super((isReceivable ? "customerRefund" : "supplierRefund"), (isReceivable ? accountingStrings.customerRefund() : accountingStrings.supplierRefund()));
        this.isReceivable = isReceivable;
        initParams(params);

        PREPAYMENT = new Option(isReceivable ? accountingStrings.customerPrepayment() : Property.get(Constants.SUPPLIER_LIST, accountingStrings.supplierPrepayment(), wfmStrings.supplier()), AccountingConstants.PREPAYMENT);
        CREDIT_NOTE = new Option(isReceivable ? accountingStrings.creditNote() : accountingStrings.debitNote(), Constants.CREDIT_NOTE);
    }

    private void initParams(String[] params) {
        if (params != null && params.length > 0) {
            if (params.length == 3 && "Prepayment".equals(params[1])) {
                prepaymentId = Integer.valueOf(params[2]);

            } else if (params.length == 3 && "Account".equals(params[1])) {
                accountId = Integer.valueOf(params[2]);
            }
        }
    }


    @Override
    protected void registerFields() {

    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        CurrencyService.App.get().getBaseCurrency(new AbstractAsyncCallback<CurrencyItem>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                initialize();
            }

            @Override
            public void success(CurrencyItem result) {
                baseCurrency = result;
                initialize();
            }
        });


        return null;
    }

    private void initialize() {

        boolean hasPermissonCustomerQuick = Utils.hasPermission(ACCOUNTING_CUSTOMER_QUICK_ADD);
        boolean hasPermissonCustomerAdd = Utils.hasPermission(ACCOUNTING_CUSTOMER_ADD);

        boolean hasPermissonSupplierQuick = Utils.hasPermission(ACCOUNTING_SUPPLIER_QUICK_ADD);
        boolean hasPermissonSupplierAdd = Utils.hasPermission(ACCOUNTING_SUPPLIER_ADD);
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
        }, false, isReceivable ? hasPermissonCustomerQuick || hasPermissonCustomerAdd : hasPermissonSupplierQuick || hasPermissonSupplierAdd);
        crmAccountLookUp.ensureDebugId("prepayment_customer_look");
        crmAccountLookUp.setAutocompleteOff();
        crmAccountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            loadClientSupplierData();
        });
        crmAccountLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);

        date = new DatePicker(new Date(), true);
        date.addStyleName(DEFAULT_WIDTH);

        number = new TextBox(true);
        number.addStyleName(DEFAULT_WIDTH);

        reference = new TextBox(true);
        reference.addStyleName(DEFAULT_WIDTH);


//        bankAccountLookUp = new BankAccountLookUp();
//        bankAccountLookUp.getSuggestBox().addSelectionHandler(su -> {
//            if (bankAccountLookUp.getSelectedData() != null && bankAccountLookUp.getSelectedData().getCurrency() != null) {
//                if (baseCurrency.getId() == bankAccountLookUp.getSelectedData().getCurrency().getId()) {
//                    currencyWidget.setCurrencies(currencyItems);
//                } else {
//                    currencyWidget.setCurrencies(new CurrencyItem[]{baseCurrency, bankAccountLookUp.getSelectedData().getCurrency()});
//                }
//                currencyWidget.setCurrency(bankAccountLookUp.getSelectedData().getCurrency());
//            }
//            loadClientSupplierData();
//        });

        paymentAccountsLookUp = new PaymentAccountsLookUp();

        paymentAccountsLookUp.getSuggestBox().addSelectionHandler(su -> {
            CurrencyService.App.get().getCurrency(paymentAccountsLookUp.getSelectedItem().getRowId(), new AbstractAsyncCallback<CurrencyItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(CurrencyItem result) {
                        if (baseCurrency.getId().equals(result.getId())) {
                            currencyWidget.setCurrencies(currencyItems);
                        } else {
                            currencyWidget.setCurrencies(new CurrencyItem[]{baseCurrency, result});
                        }
                        currencyWidget.setCurrency(result);

                    loadClientSupplierData();
                }
            });
        });

        currencyWidget = new CurrencyWidget(true);
        currencyWidget.getCurrencyListBox().addValueChangeHandler(value -> {
            loadClientSupplierData();
        });
        currencyWidget.addStyleName(DEFAULT_WIDTH);
        currencyWidget.setDatePicker(date);

        paymentTarget = new MaterialComboBox<>();
        paymentTarget.setMultiple(true);
        paymentTarget.setCloseOnSelect(false);
        paymentTarget.ensureDebugId("receive_payment-paymentTarget");
        paymentTarget.setItems(getPaymentTargetItems());
        paymentTarget.setSingleValue(PREPAYMENT);
        paymentTarget.addValueChangeHandler(c -> {
            loadClientSupplierData();
        });

        closedAccountLookUp = new PaymentAccountsLookUp(true, true);
        closedAccountLookUp.setAutocompleteOff();

        closedAmountTxtBox = new HTML();

        closedAccountItem = new FormGroup(wfmStrings.account(), closedAccountLookUp);
        closedAccountItem.setVisible(false);
        closedAmountItem = new FormGroup(accountingStrings.closeAmount(), closedAmountTxtBox);
        closedAmountItem.setVisible(false);

        createDynamicTable();


        totalAmountValue = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalAmountValue.ensureDebugId("receive_payment-totalAmmoun");
        totalRefundAmountHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalRefundAmountHTML.ensureDebugId("receive_payment-totalPaymentAmount");
        totalAmountValue.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        totalRefundAmountHTML.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);

        HTML totalAmountLabel = new HTML(wfmStrings.totalAmount());
        HTML totalPaymentAmountLabel = new HTML(accountingStrings.refundAmount());
        totalAmountLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        totalPaymentAmountLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        totalTable = new TotalTable();
        totalTable.addItem(totalAmountLabel, totalAmountValue);
        totalTable.addItem(totalPaymentAmountLabel, totalRefundAmountHTML);


        FlowPanel fp = new FlowPanel();
        fp.add(scrollDynamicTable);
        GRow row = new GRow();
        row.getElement().setAttribute("style", "padding-top: 40px");
        row.add(new GColumn(GColumnEnum.COL_4, closedAmountItem));
        row.add(new GColumn(GColumnEnum.COL_4, closedAccountItem));

        row.add(new GColumn(GColumnEnum.COL_4, totalTable));
        fp.add(row);


        addTitleField(PREPAYMENT_FORM_TITLE, isReceivable ? accountingStrings.customerRefund() : accountingStrings.supplierRefund());
        addField(CRM_ACCOUNT_LOOKUP, crmAccountLookUp, getTitle(isReceivable ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), true));
        addField(DATE_FIELD, date, getTitle(wfmStrings.date(), true));
        addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number()));
        addField(REFERENCE, reference, getTitle(wfmStrings.reference()));
        addField(CustomFormConstants.CURRENCY, currencyWidget, getTitle(wfmStrings.currency(), true));
       // addField(AccountingConstants.BANK_ACCOUNT, bankAccountLookUp, getTitle(isReceivable ? wfmStrings.paidFrom() : wfmStrings.paidTo(), true));
        addField(AccountingConstants.BANK_ACCOUNT, paymentAccountsLookUp, getTitle(isReceivable ? wfmStrings.paidFrom() : wfmStrings.paidTo(), true));
        addField(TYPE, paymentTarget, getTitle(wfmStrings.type()));
        addField(AccountingCustomFormConstants.ITEMS_TABLE, fp, null);
        show();
    }


    private void createDynamicTable() {
        dynamicTable = new DynamicTable(getColumns(), false);
        dynamicTable.ensureDebugId("Payment_Table");
        scrollDynamicTable = new ScrollPanel();
        scrollDynamicTable.add(dynamicTable);
    }


    private DynamicTableColumn[] getColumns() {
        ArrayList<DynamicTableColumn> columns = new ArrayList<>();
        columns.add(new DynamicTableColumn("", ENABLE_COLUMN, 20));

        columns.add(new DynamicTableColumn(wfmStrings.number(), CustomFormConstants.NUMBER, 150));
        columns.add(new DynamicTableColumn(wfmStrings.date(), DATE_COLUMN, 150));

        columns.add(new DynamicTableColumn(wfmStrings.amount(), AMOUNT_COLUMN, 150, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(wfmStrings.remainingAmount(), DUE_AMOUNT_COLUMN, 150, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(accountingStrings.refundAmount(), REFUND_AMOUNT, 150, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(accountingStrings.closeAmount(), CLOSE_AMOUNT, 150, Constants.RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(wfmStrings.close(), CLOSE_COLUMN, 150));
        return columns.toArray(new DynamicTableColumn[columns.size()]);
    }


    private Widget[] getWidgets(final PaymentData data) {
        if (data.getInvoiceNumber() != null) {
            refundItems.put(data.getInvoiceNumber(), data);
        }
        //checkbox column
        KpiCheckBox enableCheckBox = new KpiCheckBox();

        //description column
        Label description = new Label();
        description.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        description.getElement().getStyle().setColor("#2C5FE1");
        description.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        //Invoice date column
        Label invoiceDate = new Label();
        //Amount column
        Label invoiceAmount = new Label();

        Label remainingAmount = new Label();

        //Payment Amount column
        CustomCellTextBox refundAmount = new CustomCellTextBox();
//        Validation.checkToFocusTextBox(refundAmount, AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        refundAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        refundAmount.setEnabled(false);
        Validation.addNumericKeyboardListener(refundAmount, AccountingUtils.getPriceScale());
        refundAmount.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));

        Label closedAmount = new Label();

        KpiCheckBox enableCloseCheckBox = new KpiCheckBox();

        refundAmount.addKeyUpHandler(changeEvent -> {
            calculate();
        });

        enableCheckBox.addValueChangeHandler(booleanValueChangeEvent -> onCheckBoxValueChange(data, enableCheckBox, refundAmount));
        enableCloseCheckBox.addValueChangeHandler(booleanValueChangeEvent -> calculate());

        if (data.getInvoiceNumber() != null) {
            description.setText(data.getInvoiceNumber());
            description.addClickHandler(clickEvent -> {
                if (AccountingConstants.RECEIVABLE_PREPAYMENT_REFUND.equals(data.getType()) || AccountingConstants.PAYABLE_PREPAYMENT_REFUND.equals(data.getType())) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|paymentView/" + data.getInvoiceID(), data.getInvoiceNumber(), data.getInvoiceNumber());
                } else if (AccountingConstants.RECEIVABLE_CREDIT_REFUND.equals(data.getType()) || AccountingConstants.PAYABLE_DEBIT_REFUND.equals(data.getType())) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|summary/" + data.getInvoiceID(), data.getInvoiceNumber(), data.getInvoiceNumber());
                }
            });
        }
        if (data.getInvoiceDate() != null) {
            invoiceDate.setText(DateUtils.format(data.getInvoiceDate()));
        }
        if (data.getTotal() != null) {
            invoiceAmount.setText(AccountingUtils.get().formatPrice(data.getTotal()));
        }
        if (data.getRemainingAmount() != null) {
            remainingAmount.setText(AccountingUtils.get().formatPrice(data.getRemainingAmount()));
        }
        closedAmount.setText(AccountingUtils.getZero());

        if (data.getPaymentAmount() != null && data.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
            refundAmount.setText(AccountingUtils.get().formatPrice(data.getPaymentAmount()));
            enableCheckBox.setValue(true);
            refundAmount.setEnabled(true);
        }

        return new Widget[]{enableCheckBox, description, invoiceDate, invoiceAmount, remainingAmount, refundAmount, closedAmount, enableCloseCheckBox};

    }

    private void onCheckBoxValueChange(PaymentData data, KpiCheckBox enableCheckBox, CustomCellTextBox refundPayment) {
        boolean enabled = enableCheckBox.getValue();

        refundPayment.setEnabled(enableCheckBox.getValue());

//        allocateEnteredAmount(refundPayment, enableCheckBox.getObjectID(), dueAmount);
        if (!enabled) {
            refundPayment.setText(AccountingUtils.getZero());
        }
        calculate();
    }

    private void calculate() {
        BigDecimal totalRemainingAmount = BigDecimal.ZERO;
        BigDecimal totalRefundAmounts = BigDecimal.ZERO;
        BigDecimal totalCloseAmounts = BigDecimal.ZERO;
        for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
            DynamicTableItem item = dynamicTable.getItem(rowID);
            KpiCheckBox checkBox = (KpiCheckBox) item.getColumnById(ENABLE_COLUMN);
            if (checkBox.getValue()) {
                Label invoiceAmount = (Label) item.getColumnById(DUE_AMOUNT_COLUMN);
                TextBox paymentAmount = (TextBox) item.getColumnById(REFUND_AMOUNT);
                Label closeAmount = (Label) item.getColumnById(CLOSE_AMOUNT);
                KpiCheckBox closeCheckBox = (KpiCheckBox) item.getColumnById(CLOSE_COLUMN);
                if (invoiceAmount.getText() != null && !invoiceAmount.getText().trim().isEmpty()) {
                    totalRemainingAmount = totalRemainingAmount.add(AccountingUtils.get().parseToBigDecimal(invoiceAmount.getText()));
                }
                if (paymentAmount.getText() != null && !paymentAmount.getText().trim().isEmpty()) {
                    totalRefundAmounts = totalRefundAmounts.add(AccountingUtils.get().parseToBigDecimal(paymentAmount.getText()));
                }
                if (closeCheckBox.getValue() && invoiceAmount.getText() != null && !invoiceAmount.getText().trim().isEmpty() && paymentAmount.getText() != null && !paymentAmount.getText().trim().isEmpty()) {
                    BigDecimal closedAmount = AccountingUtils.get().parseToBigDecimal(invoiceAmount.getText()).subtract(AccountingUtils.get().parseToBigDecimal(paymentAmount.getText()));
                    closeAmount.setText(AccountingUtils.get().formatPrice(closedAmount));
                    totalCloseAmounts = totalCloseAmounts.add(closedAmount);
                } else {
                    closeAmount.setText(AccountingUtils.getZero());
                }
            }
        }

        totalAmountValue.setHTML(AccountingUtils.get().formatPrice(totalRemainingAmount));
        totalRefundAmountHTML.setHTML(AccountingUtils.get().formatPrice(totalRefundAmounts));
        totalRefundAmount = totalRefundAmounts;
        totalCloseAmount = totalCloseAmounts;
    }

    private void loadClientSupplierData() {

        LoadingPanel.loading(true);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCrmAccountId(crmAccountLookUp.getSelectedItemID());
        fp.setCurrencyID(currencyWidget.getCurrencyID());
        fp.setExchangeRate(currencyWidget.getExchangeRate());
        fp.setReceivable(isReceivable);
        fp.setOptions(paymentTarget.getSelectedValues().isEmpty() ? null : paymentTarget.getSelectedValues().stream().map(Option::getCode).collect(Collectors.toCollection(ArrayList::new)));

        InvoiceService.App.get().getPaymentRefundItemData(fp, new AbstractAsyncCallback<ReceivePaymentData>() {
            @Override
            public void failure(Throwable throwable) {
                saveButton.setEnabled(true);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ReceivePaymentData paymentData) {

                dynamicTable.clear();
                refundItems.clear();
                for (PaymentData data : paymentData.getPayments()) {
                    dynamicTable.addRow(getWidgets(data));
                }
                calculate();
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    protected void addButtons() {

        description = new IntroductionPanel();
        description.getIntroduction().addStyleName("keepDropDownOpen");

        FooterInformer footerIntroduction = new FooterInformer(SvgEnum.docTitle, wfmStrings.description(), description.getIntroduction());
        footerIntroduction.setInitialClasses("informer-item");
        footer.addToLeftSide(footerIntroduction);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveButton);
    }

    @Override
    protected void getDataToFillFields() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(objectID);
        filterParameter.setReceivable(isReceivable);
        filterParameter.setEntityID(prepaymentId);
        filterParameter.setAccountID(accountId);
        InvoiceService.App.get().getPaymentRefund(filterParameter, new AbstractAsyncCallback<ReceivePaymentData>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ReceivePaymentData result) {
                data = result;
                currencyItems = result.getEnabledCurrencies();
                number.setText(result.getNumberData().getTransferNumber());
                if (prepaymentId != null) {
                    if (result.getBankAccountCurrency() != null && !baseCurrency.equals(result.getBankAccountCurrency())){
                        currencyWidget.setCurrencies(new CurrencyItem[] {baseCurrency, result.getBankAccountCurrency()});
                    } else {
                        currencyWidget.setCurrency(result.getCurrency());
                        currencyWidget.setCurrencies(currencyItems);
                    }
//                     bankAccountLookUp.setSelected(result.getBankAccount());
                    paymentAccountsLookUp.setSelected(result.getAccount());
                    crmAccountLookUp.setSelected(result.getCrmAccount());
                    crmAccountLookUp.setEnabled(false);
                    reference.setText(result.getReference());
                    loadClientSupplierData();
                } else if (accountId != null) {
                    paymentAccountsLookUp.setSelected(result.getAccount());
                    if (result.getCurrency() != null)
                        currencyWidget.setCurrency(result.getCurrency());

                    if (result.getBankAccountCurrency() != null && !baseCurrency.equals(result.getBankAccountCurrency())){
                        currencyWidget.setCurrencies(new CurrencyItem[] {baseCurrency, result.getBankAccountCurrency()});
                    } else {
                        currencyWidget.setCurrencies(currencyItems);
                    }
                    loadClientSupplierData();
                }
            }
        });

    }

    @Override
    protected String getFormID() {
        return isReceivable ? LayoutRPC.CUSTOMER_REFUND : LayoutRPC.SUPPLIER_REFUND;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
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

    private List<Option> getPaymentTargetItems() {
        List<Option> items = new ArrayList<>();
        items.add(PREPAYMENT);
//        items.add(CREDIT_NOTE);

        return items;
    }

    private String getPaymentTarget() {
        return paymentTarget.getSelectedValues().stream().map(Option::getCode).collect(Collectors.joining(","));
    }

    private void setPaymentTarget(String code) {
        if (code == null) {
            return;
        }
        Set<Option> items = new HashSet<>();
        if (code.contains(AccountingConstants.PREPAYMENT)) {
            items.add(PREPAYMENT);
        }
        if (code.contains(Constants.CREDIT_NOTE)) {
            items.add(CREDIT_NOTE);
        }
        // -->
        paymentTarget.setValues(new ArrayList<>(items));
    }

    private void save() {
        saveButton.setEnabled(false);
        if (!validate()) {
            saveButton.setEnabled(true);
            return;
        }


        ReceivePaymentData paymentData = new ReceivePaymentData();
        paymentData.setObjectID(null);

        Integer intNumber = data.getNumberData().parseNumber(number.getText());
        if (intNumber != null) {
            paymentData.setIntNumber(intNumber);
        }

        paymentData.setNumber(number.getText());
        paymentData.setDescription(description.getIntroduction().getText());

        paymentData.setCrmAccount(crmAccountLookUp.getSelectedItem());
        paymentData.setAccount(paymentAccountsLookUp.getSelectedItem());
        paymentData.setExRate(currencyWidget.getExchangeRate());
        paymentData.setCurrency(new CurrencyItem(currencyWidget.getCurrencyID(), null, null));
        paymentData.setReference(reference.getText());
        paymentData.setDate(new DateNonConvertable(date.getDate()));
        paymentData.setTotalAmount(totalRefundAmount);
        paymentData.setPayments(getRefundTableData());
        paymentData.setType(isReceivable ? RECEIVABLE : PAYABLE);
        paymentData.setPaymentTarget(getPaymentTarget());
        if (closedAccountLookUp != null && closedAccountLookUp.getSelectedItemID() != null) {
            paymentData.setCloseAccount(closedAccountLookUp.getSelectedItem());
        }
        if (closedAmountTxtBox.getText() != null) {
            paymentData.setCloseAmount(AccountingUtils.parsePriceToBigDecimal(closedAmountTxtBox.getText()));
        }
        LoadingPanel.loading(true);

        InvoiceService.App.get().savePaymentRefundData(paymentData, new AbstractAsyncCallback<BatchPaymentResult>() {
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

                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PREPAYMENT_SAVE, null, AddPaymentRefund.this);

                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.payment()), Info.Type.INFO);
                closeTab();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (date.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(date.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(isReceivable ? accountingStrings.customerRefund() : accountingStrings.supplierRefund(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            date.addStyleName(ERROR_FORM_STYLE);
            return false;
        }
        if (!Validation.validateLookUpRequired(crmAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(paymentAccountsLookUp)) {
            errors++;
        }
        if (!Validation.validateDate(date)) {
            errors++;
        }

        if (paymentTarget.getSelectedValues().isEmpty()) {
            paymentTarget.addStyleName("x-form-invalid");
            errors++;
        }
        if (totalCloseAmount.compareTo(BigDecimal.ZERO) <= 0 && totalRefundAmount.compareTo(BigDecimal.ZERO) <= 0 && dynamicTable.getRowNumber() > 0) {
            if (dynamicTable.getItem(0).getColumnById(REFUND_AMOUNT) != null) {
                dynamicTable.getItem(0).getColumnById(REFUND_AMOUNT).addStyleName("x-form-invalid");
            }
            errors++;
        }

        for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
            DynamicTableItem item = dynamicTable.getItem(rowID);
            KpiCheckBox checkBox = (KpiCheckBox) item.getColumnById(ENABLE_COLUMN);
            if (checkBox.getValue()) {
                Label invoiceAmount = (Label) item.getColumnById(DUE_AMOUNT_COLUMN);
                TextBox paymentAmount = (TextBox) item.getColumnById(REFUND_AMOUNT);
                if (invoiceAmount.getText() != null && !invoiceAmount.getText().trim().isEmpty() && paymentAmount.getText() != null && !paymentAmount.getText().trim().isEmpty()) {
                    BigDecimal dueAmount = AccountingUtils.parsePriceToBigDecimal(invoiceAmount.getText());
                    BigDecimal refundAmount = AccountingUtils.parsePriceToBigDecimal(paymentAmount.getText());
                    if (dueAmount != null && refundAmount != null) {
                        if (dueAmount.compareTo(refundAmount) < 0) {
                            paymentAmount.addStyleName("x-form-invalid");
                            errors++;
                        }
                    }
                }
            }
        }

        if (errors <= 0) {
            BigDecimal closeAmounts = BigDecimal.ZERO;
            for (int rowID = 0; rowID < dynamicTable.getRowNumber(); rowID++) {
                DynamicTableItem item = dynamicTable.getItem(rowID);
                KpiCheckBox checkBox = (KpiCheckBox) item.getColumnById(ENABLE_COLUMN);
                if (checkBox.getValue()) {
                    TextBox paymentAmount = (TextBox) item.getColumnById(REFUND_AMOUNT);
                    Label closedAmount = (Label) item.getColumnById(CLOSE_AMOUNT);
                    if (closedAmount.getText() != null && paymentAmount.getText() != null) {
                        BigDecimal closeAmountDecimal = AccountingUtils.parsePriceToBigDecimal(closedAmount.getText());
                        if (closedAmount != null) {
                            closeAmounts = closeAmounts.add(closeAmountDecimal);
                        }
                    }
                }
            }
            if (closeAmounts != null && closeAmounts.compareTo(BigDecimal.ZERO) > 0) {
                showClosePaymentPanel(true);
                closedAmountTxtBox.setText(AccountingUtils.get().formatPrice(closeAmounts));
                if (!Validation.validateLookUpRequired(closedAccountLookUp)) {
                    errors++;
                }
            }
        }

        return errors <= 0;
    }

    private PaymentData[] getRefundTableData() {
        LinkedList<PaymentData> items = new LinkedList<>();
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem item = dynamicTable.getItem(i);
            KpiCheckBox enableCheckBox = (KpiCheckBox) item.getColumnById(ENABLE_COLUMN);
            if (enableCheckBox.getValue()) {
                PaymentData refundItem = null;
                Label number = (Label) item.getColumnById(CustomFormConstants.NUMBER);
                CustomCellTextBox refundAmount = (CustomCellTextBox) item.getColumnById(REFUND_AMOUNT);
                KpiCheckBox closeCheckBox = (KpiCheckBox) item.getColumnById(CLOSE_COLUMN);
                Label closedAmount = (Label) item.getColumnById(CLOSE_AMOUNT);
                refundItem = refundItems.get(number.getText());

                if (refundItem == null) {
                    refundItem = new PaymentData();
                }
                if (refundAmount != null) {
                    refundItem.setRefundAmount(AccountingUtils.get().parseToBigDecimal(refundAmount.getText()));
                }
                if (closeCheckBox != null && closeCheckBox.getValue() && closedAmount != null) {
                    refundItem.setClosePrepayment(closeCheckBox.getValue());
                    refundItem.setClosedAmount(AccountingUtils.get().parseToBigDecimal(closedAmount.getText()));

                }
                if (refundItem.getRefundAmount().compareTo(BigDecimal.ZERO) > 0 || (refundItem.getClosedAmount() != null && refundItem.getClosedAmount().compareTo(BigDecimal.ZERO) > 0)) {
                    items.add(refundItem);
                }
            }

        }
        return items.toArray(new PaymentData[]{});
    }

    private void showClosePaymentPanel(boolean isVisible) {
        closedAccountLookUp.setVisible(isVisible);
        if (!isVisible) {
            closedAccountLookUp.clear();
        }

        closedAmountTxtBox.setVisible(isVisible);
        if (!isVisible) {
            closedAmountTxtBox.setText("");
        }
        closedAccountItem.setVisible(isVisible);
        closedAmountItem.setVisible(isVisible);
    }
}
