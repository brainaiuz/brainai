package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpensePaymentData;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.expenses.client.rpc.ReportData;
import com.edatasite.workforce.gwt.expenses.client.ui.ItemUploadForm;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentAndPrePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PrePaymentLinkProvider;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class ExpensePaymentPanel extends Div {

    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private TextBox amountForPay;
    private DatePicker datePaid;
    private PaymentAccountsLookUp paymentAccountLookUp;
    private TextBox refChequeNumber;
    private WfmButton2 payButton;
    private final ReportData reportData;
    private BigDecimal dueAmount;
    private CurrencyListItem exchangeCurrency;
    private ILoadTotals loadTotals;
    private ItemUploadForm paymentUploadForm;
    private ExpensePrePaymentAndCreditLink applyCreditLink;
    private Command afterAppliedCreditCommand;

    public ExpensePaymentPanel(ReportData reportData) {
        super("simple-panel");
        this.reportData = reportData;
        initPaymentPanelColumns();
        initHandlers();
        loadAccountCredits();
    }


    public void setLoadTotals(ILoadTotals loadTotals) {
        this.loadTotals = loadTotals;
    }

    private void initHandlers() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXPENSE_PAYMENT_DELETE, ExpensePaymentPanel.this, (sender, args) -> {
            reloadExpensePaymentRelatedFields();
        });
    }

    private void initPaymentPanelColumns() {
        Div row = new Div("grid-row");
        row.add(this::createAmountColumn);
        row.add(this::createPaymentDateColumn);
        row.add(this::createPaymentAccountColumn);
        row.add(this::createReferenceNumberColumn);
        row.add(this::createButtonColumn);
        add(row);
    }

    private Div createButtonColumn() {
        Div buttonColumn = new Div("col-auto");

        payButton = new WfmButton2(wfmStrings.addPayment(), WfmButton2.BTN_PRIMARY);
        payButton.addClickHandler(clickEvent -> savePayment(true));
        FormGroup payButtonFormGroup = new FormGroup("", payButton);
        buttonColumn.add(payButtonFormGroup);
        return buttonColumn;
    }

    private Div createReferenceNumberColumn() {
        Div referenceColumn = new Div("col-auto");
        refChequeNumber = new TextBox();
        refChequeNumber.addStyleName("form-control");
        paymentUploadForm = new ItemUploadForm(Constants.F_EXP_PAYMENT, true);

        Div inputGroup = new Div("input-group");
        Div prepend = new Div("input-group-prepend");
        prepend.getElement().setAttribute("style", "height:30px");
        inputGroup.add(refChequeNumber);
        inputGroup.add(prepend);

        Div prependedContent = new Div("input-group-text");
        prependedContent.add(paymentUploadForm);
        prepend.add(prependedContent);

        FormGroup fieldReferenceNumber = new FormGroup(accountingStrings.refOrChequeNumber(), inputGroup);
        referenceColumn.add(fieldReferenceNumber);

        return referenceColumn;
    }

    private Div createAmountColumn() {
        Div amountColumn = new Div("col-auto");
        amountForPay = new TextBox();
        amountForPay.addStyleName("form-control");
        Validation.addNumericKeyboardListener(amountForPay, AccountingUtils.calculationScale);

        applyCreditLink = new ExpensePrePaymentAndCreditLink(reportData, new PrePaymentLinkProvider() {
            @Override
            public BigDecimal getPaymentExRate() {
                return reportData.getReport() != null && reportData.getReport().getExchangeRate() != null ? reportData.getReport().getExchangeRate() : BigDecimal.ONE;
            }

            @Override
            public BigDecimal getDueAmount() {
                return dueAmount;
            }

            @Override
            public void fireInvoicePaymentChange(String type) {

                if (afterAppliedCreditCommand != null) {
                    afterAppliedCreditCommand.execute();
                }

//                if (Constants.PAYABLE.equals(type)) {
//                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_CHECK_SAVED, null, ExpensePaymentPanel.this);
//                }
//                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, Constants.RECEIVABLE.equals(invoiceData.getType()) ? InvoiceSummaryView.ADD_SALEINVOICE : InvoiceSummaryView.ADD_PURCHASEINVOICE, InvoicePaymentWidget.this);
//                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, null, InvoicePaymentWidget.this);
            }
        });
        applyCreditLink.setVisible(false);
        applyCreditLink.hideDialogBoxIfOpen();

        FormGroup fieldAmountToPay = new FormGroup(amountForPay);
        fieldAmountToPay.getGroupLabel().addStyleName("label-group");
        fieldAmountToPay.getGroupLabel().add(new Span(wfmStrings.amount()));
        fieldAmountToPay.getGroupLabel().add(applyCreditLink);
        amountColumn.add(fieldAmountToPay);

        return amountColumn;
    }

    private Div createPaymentDateColumn() {
        Div dateColumn = new Div("col-auto");
        initDatePicker();
        FormGroup dateFormGroup = new FormGroup(wfmStrings.paidDate(), datePaid);
        dateColumn.add(dateFormGroup);
        return dateColumn;
    }

    private void initDatePicker() {
        datePaid = new DatePicker();
        datePaid.addChangeHandler(changeEvent -> {
            if (datePaid.getStyleName() != null && !datePaid.getStyleName().equals("")) {
                datePaid.removeStyleName(datePaid.getStyleName());
            }
            getPaymentExchangeRate();
        });
        datePaid.setDate(new Date());
        getPaymentExchangeRate();
    }

    private Div createPaymentAccountColumn() {
        ExpenseReportsListItem expenseReportData = reportData.getReport();
        Div paymentAccountColumn = new Div("col");
        paymentAccountLookUp = new PaymentAccountsLookUp(/*true, new String[]{ADD_ACCOUNT, ADD_BANK_ACCOUNT}*/);
        paymentAccountLookUp.setCurrencyID(expenseReportData.getExpenseCurrency().getId());

        FormGroup paymentAccountFormGroup = new FormGroup(wfmStrings.paidFrom(), paymentAccountLookUp);
        paymentAccountColumn.add(paymentAccountFormGroup);
        return paymentAccountColumn;
    }


    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
        amountForPay.setText(AccountingUtils.get().formatPrice(dueAmount));
    }

    private void getPaymentExchangeRate() {
        ExpenseReportsListItem expenseReportData = reportData.getReport();
        CurrencyService.App.get().getCurrencyRateByDate(expenseReportData.getExpenseCurrency().getId(), new DateNonConvertable(datePaid.getDate()), new AsyncCallback<CurrencyListItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(CurrencyListItem result) {
                exchangeCurrency = result;
            }
        });
    }

    private boolean validatePayment() {
        if (datePaid.getDate() != null && Utils.isExpensesLocked() && DateUtils.getTransactionLockDate().after(datePaid.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.expenseClaim(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy/MM/dd");
        ExpenseReportsListItem expenseReportData = reportData.getReport();
        String expenseDateSrt = dateFormat.format(expenseReportData.getStartDate().getNonConvertedDate());
        String paidDateSrt = dateFormat.format(datePaid.getDate());
        Date expenseDate = dateFormat.parse(expenseDateSrt);
        Date paidDate = dateFormat.parse(paidDateSrt);

        int errors = 0;
        if (!Validation.validateDateOrder(expenseDate, paidDate, wfmStrings.canNotBeEarlier(), true)) {
            datePaid.setStyleName("x-form-invalid");
            errors++;
        }
        if (amountForPay.getText().equals("")) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(paymentAccountLookUp)) {
            errors++;
        }
        if (refChequeNumber.getText().equals("")) {
            errors++;
        }
        return errors <= 0;
    }

    private void enabledPayButton(boolean value) {
        if (payButton == null) {
            return;
        }
        payButton.setEnabled(value);
    }

    private void savePayment(boolean checkExistingReference) {
        enabledPayButton(false);
        if (!validatePayment()) {
            enabledPayButton(true);
            return;
        }
        BigDecimal paymentAmount = AccountingUtils.get().parseToBigDecimal(amountForPay.getText()).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        boolean validAmountOfPayment = paymentAmount.compareTo(dueAmount) <= 0;

        if (validAmountOfPayment) {
            ExpensePaymentData epd = new ExpensePaymentData();
            epd.setPaymentAmount(paymentAmount);
            epd.setDate(new DateNonConvertable(datePaid.getDate()));
            epd.setPaymentAccount(paymentAccountLookUp.getSelectedItem());
            epd.setReferenceNumber(refChequeNumber.getText());
            epd.setValidateReference(checkExistingReference);
            epd.setReportId(reportData.getReport().getId());
            if (exchangeCurrency != null && exchangeCurrency.getExchangeRate() != null) {
                epd.setExchangeRate(BigDecimal.valueOf(exchangeCurrency.getExchangeRate()).setScale(AccountingUtils.customExRateScale, RoundingMode.HALF_UP));
            } else {
                epd.setExchangeRate(new BigDecimal("1.00"));
            }
            epd.setAttachments(paymentUploadForm.getAttachedFiles());
            ExpenseService.App.get().savePayment(epd, new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable throwable) {
                    enabledPayButton(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(Integer response) {
                    enabledPayButton(true);
                    if (ExpensePaymentData.REFERENCE_EXIST.equals(response)) {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.information());
                        messageBox.setMessage(accountingMessages.referenceWithNumberExists(refChequeNumber.getText()));
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onSubmit() {
                                savePayment(false);
                            }
                        });
                        messageBox.open();
                    } else {
                        reloadExpensePaymentRelatedFields();
//                        refreshPaymentHistory();
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.payment()), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSEREPORT_SAVED, null, ExpensePaymentPanel.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_TO_BANK_ACCOUNT, null, ExpensePaymentPanel.this);
                    }
                }
            });
        } else {
            Info.show(accountingStrings.validatePayment(), Info.Type.WARNING);
            enabledPayButton(true);
        }
    }

    private void reloadExpensePaymentRelatedFields() {
        paymentAccountLookUp.clear();

        ExpenseService.App.get().getExpensePayments(reportData.getReport().getId(), new AsyncCallback<ExpensePaymentData[]>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ExpensePaymentData[] result) {
                if (loadTotals != null) {
                    loadTotals.loadTotals(result);
                }
            }
        });
    }

    public void addPaymentLookupItem(SelectItem paymentAccount) {
        paymentAccountLookUp.addItem(paymentAccount);
    }

    public void setReferenceNumber(String number) {
        if (("".equals(refChequeNumber.getValue()) || refChequeNumber.getValue() == null)) {
            refChequeNumber.setValue(number);
        }
    }


    protected interface ILoadTotals {
        void loadTotals(ExpensePaymentData[] data);
    }

    public void setAfterAppliedCreditCommand(Command command) {
        this.afterAppliedCreditCommand = command;
    }

    public void loadAccountCredits() {
        if (reportData.getReport() != null && reportData.getReport().getSupplier() != null && reportData.getReport().getSupplier().getId() != null) {
            InvoiceService.App.get().getCustomerCreditData(reportData.getReport().getId(), reportData.getReport().getSupplier().getId(), true, new AsyncCallback<PaymentAndPrePaymentData>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(PaymentAndPrePaymentData paymentAndPrePaymentData) {
                    applyCreditLink.applyCreditData(paymentAndPrePaymentData.getCredits());
                    applyCreditLink.setVisible(paymentAndPrePaymentData.getCredits() != null && paymentAndPrePaymentData.getCredits().length > 0);
                }
            });
        }
    }
}
