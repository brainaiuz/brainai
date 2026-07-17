package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ReportData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PrePaymentLinkProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class ExpensePrePaymentAndCreditLink extends SimpleLink {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private KpiModal creditDialogBox;

    private final ExpenseReportsListItem expenseReportsListItem;
    private PaymentData[] paymentsCredits;
    private FlexTable creditTable;
    private final PrePaymentLinkProvider creditLinkProvider;
    private boolean bussy = false;

    public ExpensePrePaymentAndCreditLink(ReportData reportData, PrePaymentLinkProvider creditLinkProvider) {
        super(accountingStrings.applyCredit());
        this.expenseReportsListItem = reportData.getReport();
        this.creditLinkProvider = creditLinkProvider;
        setVisible(false);
        addClickHandler(clickEvent -> {

            if (creditDialogBox == null) {
                initializePrepaymentDialogBox();
            }
            creditDialogBox.open();
        });
    }


    private void initializePrepaymentDialogBox() {
        creditDialogBox = new KpiModal();
        creditDialogBox.setTitle(accountingStrings.applyCredit());
        creditDialogBox.setWidth("1030px");

        Div divPanel = new Div();

        creditTable = new FlexTable();
        creditTable.setStyleName("table");
        creditTable.setCellSpacing(5);
        divPanel.add(creditTable);

        creditTable.setText(0, 0, "");
        creditTable.setText(0, 1, wfmStrings.amount() + " (" + expenseReportsListItem.getExpenseCurrency().getName() + ")");
        creditTable.setText(0, 2, wfmStrings.remaining() + " " + wfmStrings.amount());
        creditTable.setText(0, 3, wfmStrings.number());
        creditTable.setText(0, 4, wfmStrings.paymentDate());
        creditTable.setText(0, 5, wfmStrings.reference());
        creditTable.setText(0, 6, Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()));

        creditTable.setText(0, 7, wfmStrings.date());
        creditTable.getRowFormatter().setStyleName(0, "thead");

        HTMLTable.ColumnFormatter cf = creditTable.getColumnFormatter();
        cf.setWidth(0, "40px");
        cf.setWidth(1, "125px");
        cf.setWidth(2, "150px");
        cf.setWidth(3, "110px");
        cf.setWidth(4, "140px");
        cf.setWidth(5, "130px");
        cf.setWidth(6, "150px");
        cf.setWidth(7, "150px");

        int i = 1;
        for (PaymentData pc : paymentsCredits) {
            BigDecimal amountForPay = pc.getPaymentAmount().multiply(getPaymentExchangeRate()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);

            final AmountTextBox amountTxtBox = new AmountTextBox(pc.getPaymentAmount());
            final CreditBalanceCheckBox checkBox = new CreditBalanceCheckBox(pc, amountForPay);
            final DatePicker datePicker = new DatePicker();
            datePicker.setWidth("120px");

            amountTxtBox.setWidth("100px");
            Validation.addNumericKeyboardListener(amountTxtBox, AccountingUtils.calculationScale);
            amountTxtBox.setText(AccountingUtils.get().formatPrice(amountForPay));

            checkBox.addClickHandler(clickEvent -> {
                amountTxtBox.setEnabled(checkBox.getValue());
                datePicker.setEnabled(checkBox.getValue());
            });

            amountTxtBox.addKeyUpHandler(keyUpEvent -> {
                BigDecimal amountPay = AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText());
                amountTxtBox.setAmountInBaseCurrency(amountPay.divide(getPaymentExchangeRate(), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
            });

            StringBuilder info = new StringBuilder();
            if (pc.isCrmAccountCredit()) {
                info.append(wfmStrings.openingBalance() + ". ");
            }

            info.append(" ");
            info.append(wfmStrings.amount() + " : " + AccountingUtils.get().formatPrice(pc.getPaymentAmount()) + " " + expenseReportsListItem.getBaseCurrency().getName() + "<br/>");
            HTML infoLabel = new HTML(info.toString());

            amountTxtBox.setEnabled(false);
            datePicker.setEnabled(false);
            creditTable.setWidget(i, 0, checkBox);
            creditTable.setWidget(i, 1, amountTxtBox);
            creditTable.setWidget(i, 2, infoLabel);
            FlexTable referencePanel = new FlexTable();

            //Info
            Span tooltipWrapper = new Span();

            Icon iInfo = new Icon();
            iInfo.setClass("ficon--info");
            MaterialLink iconLink = new MaterialLink();
            iconLink.add(iInfo);
            String activation = "infoDropDown";
            iconLink.setActivates(activation);

            MaterialDropDown dropDown = new MaterialDropDown(activation);
            dropDown.addStyleName("dropdown-content dropdown-content-tooltip");
            dropDown.setHover(true);

            tooltipWrapper.add(iconLink);
            tooltipWrapper.add(dropDown);

            if (pc.getManualJournalID() != null) {
                dropDown.getElement().setInnerHTML(wfmStrings.manualTransactions());
            } else if (pc.getBankTransferID() != null) {
                dropDown.getElement().setInnerHTML(accountingStrings.bankTransfers());
            } else if (pc.getBankCheckItem() != null) {
                dropDown.getElement().setInnerHTML(wfmStrings.checks());
            } else {
                wfmStrings.prepayments();
            }

            String number;
            HTML reference;
            MaterialLink numberLink;

            if (pc.isManualJournal()) {
                number = pc.getInvoiceNumber();
                reference = new HTML(pc.getReferenceNumber());
                numberLink = new MaterialLink(number, "manual|summary/" + pc.getManualJournalID());
            } else if (pc.isPrepayment()) {
                number = pc.getNumber();
                reference = new HTML(pc.getReference());
                numberLink = new MaterialLink(number, "invoicepayment|paymentView/" + pc.getObjectID());
            } else if (pc.isBankTransafer()) {
                String type;
                number = pc.getNumber() != null ? pc.getNumber() : "";
                if (number.startsWith("CP")) {
                    type = "CASH_PAYMENT";
                } else if (number.startsWith("CR")) {
                    type = "CASH_RECEIPT";
                } else if (number.startsWith("REM")) {
                    type = "RECEIVE_MONEY";
                } else {
                    type = "SPEND_MONEY";
                }
                reference = new HTML(pc.getReferenceNumber());
                numberLink = new MaterialLink(number, "spendreceivemoney|summary/" + pc.getBankTransferID() + "/" + type);
            } else {
                number = pc.getNumber();
                reference = new HTML(pc.getReferenceNumber());
                numberLink = new MaterialLink(number, "check|summary/" + pc.getObjectID());
            }
            creditTable.setWidget(i, 3, numberLink);

            DatePicker paymentDate = new DatePicker();
            paymentDate.setEnabled(false);
            if (pc.getDate() != null) {
                paymentDate.setDate(pc.getDate().getNonConvertedDate());
            }

            creditTable.setWidget(i, 4, paymentDate);

            referencePanel.setWidget(0, 0, reference);
            referencePanel.setWidget(0, 1, tooltipWrapper);

            creditTable.setWidget(i, 5, referencePanel);
//            if (RECEIVABLE.equals(invoiceData.getType())) {
//                HTML safeHtml = new HTML("");
//                if (pc.getSaleQuoteItem() != null) {
//                    safeHtml.setHTML(pc.getSaleQuoteItem().getName());
//                }
//                creditTable.setWidget(i, 6, safeHtml);
//            } else {
            HTML safeHtml = new HTML("");
            if (pc.getPurchaseOrderItem() != null) {
                safeHtml.setHTML(pc.getPurchaseOrderItem().getName());
            }
            creditTable.setWidget(i, 6, safeHtml);
//            }

            creditTable.setWidget(i, 7, datePicker);
            i++;
        }

        FlexTable mainTable = new FlexTable();
        mainTable.setWidth("100%");
        int row = 0;

        if (!expenseReportsListItem.getExpenseCurrency().getId().equals(expenseReportsListItem.getBaseCurrency().getId())) {
            mainTable.setWidget(row++, 0, new HTML(1 + " " + expenseReportsListItem.getBaseCurrency().getName() + " = " + getPaymentExchangeRate() + " " + expenseReportsListItem.getExpenseCurrency().getName()));
        }

        mainTable.setWidget(row++, 0, divPanel);

        WfmButton2 apply = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_WHITE_OUTLINE);
        apply.addClickHandler(clickEvent -> {
            if (!bussy) {
                bussy = true;
                applyCreditsToExpense(creditTable);
            }
        });
        cancel.addClickHandler(clickEvent -> creditDialogBox.close());


        creditDialogBox.addButton(cancel);
        creditDialogBox.addButton(apply);
        /*HorizontalPanel exportPanel = new HorizontalPanel();
        exportPanel.setStyleName("workforce");
        exportPanel.setSpacing(5);
        exportPanel.add(apply);
        exportPanel.add(cancel);
        mainTable.setWidget(row, 0, exportPanel);
        mainTable.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);*/

        creditDialogBox.add(mainTable);
    }

    public class CreditBalanceCheckBox extends KpiCheckBox {
        private final BigDecimal maximalPayableAmount;
        private final PaymentData credit;

        public CreditBalanceCheckBox(PaymentData credit, BigDecimal maximalPayableAmount) {
            this.maximalPayableAmount = maximalPayableAmount;
            this.credit = credit;
        }

        public BigDecimal getMaximalPayableAmount() {
            return maximalPayableAmount;
        }

        public SelectItem getCrmAccount() {
            return credit.getCrmAccount();
        }

        public SelectItem getBankCheckItem() {
            return credit.getBankCheckItem();
        }

        public Integer getProjectID() {
            return credit.getRelatedObjectID();
        }

        public boolean isCrmAccountCredit() {
            return credit.isCrmAccountCredit();
        }

        public boolean isPrepayment() {
            return credit.isPrepayment();
        }

        public boolean isManualJournal() {
            return credit.isManualJournal();
        }

        public Integer getManualJournalID() {
            return credit.getManualJournalID();
        }

        public boolean isBankTransfer() {
            return credit.isBankTransafer();
        }

        public Integer getBankTransferID() {
            return credit.getBankTransferID();
        }

        public SelectItem getPaymentAccount() {
            return credit.getPaymentAccount();
        }

        public Date getDate() {
            return credit.getDate() != null ? DateUtil.resetTime(credit.getDate().getNonConvertedDate()) : null;
        }

        public Integer getObjectID() {
            return credit.getObjectID();
        }

        public BigDecimal getCreditAmount() {
            return credit.getPaymentAmount();
        }

        public AccountItem getReceivablePayable() {
            return credit.getReceivablePayable();
        }
    }

    public class AmountTextBox extends TextBox {
        private BigDecimal amountInBaseCurrency;

        public AmountTextBox(BigDecimal amountInBaseCurrency) {
            this.amountInBaseCurrency = amountInBaseCurrency;
        }

        public BigDecimal getAmountInBaseCurrency() {
            return amountInBaseCurrency;
        }

        public void setAmountInBaseCurrency(BigDecimal amountInBaseCurrency) {
            this.amountInBaseCurrency = amountInBaseCurrency;
        }
    }

    private void applyCreditsToExpense(FlexTable creditTable) {
        int errors = 0, dateValidateErrors = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (int i = 1; i < creditTable.getRowCount(); i++) {
            CreditBalanceCheckBox checkBox = (CreditBalanceCheckBox) creditTable.getWidget(i, 0);
            if (checkBox.getValue()) {
                final TextBox amountTxtBox = (TextBox) creditTable.getWidget(i, 1);
                if (!Validation.validateTextBoxRequired(amountTxtBox)) {
                    errors++;
                } else {
                    BigDecimal amount = AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    totalAmount = totalAmount.add(amount);
                    String errorMessage = null;
                    if (amount.compareTo(BigDecimal.ZERO) == 0) {
                        errorMessage = accountingStrings.amountShouldNotBeZero();
                    } else if (amount.compareTo(checkBox.getMaximalPayableAmount().setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)) > 0) {
                        errorMessage = accountingStrings.amountShouldNotBeMoreThan() + " " + AccountingUtils.get().formatPrice(checkBox.getMaximalPayableAmount());
                    }
                    if (errorMessage != null) {
                        amountTxtBox.setStyleName("x-form-invalid");
                        amountTxtBox.addKeyDownHandler(keyDownEvent -> {
                            if (!"".equals(amountTxtBox.getStyleName()))
                                amountTxtBox.removeStyleName(amountTxtBox.getStyleName());
                        });
                        errors++;
                    }
                }

                DatePicker datePicker = (DatePicker) creditTable.getWidget(i, 7);
                if (datePicker.getDate() == null) {
                    Validation.validateDate(datePicker, new HTML(), false);
                    errors++;
                }

                if (datePicker.getDate() != null) {
                    DateUtil.resetTime(datePicker.getDate());
                    if (datePicker.getDate().before(expenseReportsListItem.getStartDate().getNonConvertedDate())) {
                        dateValidateErrors++;
                        datePicker.addStyleName(Constants.ERROR_FORM_STYLE);
                        Utils.scrollIntoView(datePicker.getElement());
                    } else {
                        datePicker.removeStyleName(Constants.ERROR_FORM_STYLE);
                    }

                    if (checkBox.getDate() != null && datePicker.getDate().before(checkBox.getDate())) {
                        datePicker.addStyleName(Constants.ERROR_FORM_STYLE);
                        Utils.scrollIntoView(datePicker.getElement());

                        bussy = false;
                        Info.show(accountingMessages.paymentCreditDateMessage(DateUtil.getDateTimeFormat().format(checkBox.getDate())), Info.Type.WARNING);
                        return;
                    }
                }
            }
        }
        if (errors > 0) {
            bussy = false;
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return;
        }
        if (totalAmount.compareTo(creditLinkProvider.getDueAmount()) > 0) {
            bussy = false;
            Info.show(accountingStrings.amountNotMoreDueAmount(), Info.Type.WARNING);
            return;
        }

        if (dateValidateErrors > 0) {
            bussy = false;
            Info.show(accountingStrings.datePaidMessage(), Info.Type.WARNING);
            return;
        }

        List<PaymentData> appliedCreditList = new LinkedList<>();
        for (int i = 1; i < creditTable.getRowCount(); i++) {
            CreditBalanceCheckBox checkBox = (CreditBalanceCheckBox) creditTable.getWidget(i, 0);
            if (checkBox.getValue()) {
                AmountTextBox amountTxtBox = (AmountTextBox) creditTable.getWidget(i, 1);
                DatePicker datePicker = (DatePicker) creditTable.getWidget(i, 7);

                PaymentData paymentData = new PaymentData();
                paymentData.setObjectID(checkBox.getObjectID());
                paymentData.setRelatedObjectID(checkBox.getProjectID());
                paymentData.setExpenseId(expenseReportsListItem.getId());
                paymentData.setCrmAccount(checkBox.getCrmAccount());
                paymentData.setBankCheckItem(checkBox.getBankCheckItem());
                paymentData.setCrmAccountCredit(checkBox.isCrmAccountCredit());
                paymentData.setPrepayment(checkBox.isPrepayment());
                paymentData.setReceivablePayable(checkBox.getReceivablePayable());

                BigDecimal creditAmountInInvoiceCurrency = checkBox.getCreditAmount().multiply(getPaymentExchangeRate());

                if (AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(creditAmountInInvoiceCurrency.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)) == 0) {
                    paymentData.setPaymentAmount(creditAmountInInvoiceCurrency);
                    paymentData.setBaseAmount(checkBox.getCreditAmount());
                } else {
                    paymentData.setPaymentAmount(AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()));
                    paymentData.setBaseAmount(amountTxtBox.getAmountInBaseCurrency());
                }
                paymentData.setDate(new DateNonConvertable(datePicker.getDate()));
                paymentData.setManualJournalID(checkBox.getManualJournalID());
                paymentData.setBankTransferID(checkBox.getBankTransferID());
                paymentData.setPaymentAccount(checkBox.getPaymentAccount());
                paymentData.setExchangeRate(getPaymentExchangeRate());
                paymentData.setCurrency(new SelectItem(expenseReportsListItem.getExpenseCurrency().getId()));


                if (checkBox.isCrmAccountCredit()) {
                    paymentData.setType(AccountingConstants.PAYABLE_CRM_ACCOUNT_CREDIT);
                } else if (checkBox.isManualJournal()) {
                    paymentData.setType(AccountingConstants.PAYABLE_MANUAL_CREDIT);
                } else if (checkBox.isPrepayment()) {
                    paymentData.setType(AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE);
                } else if (checkBox.isBankTransfer()) {
                    paymentData.setType(AccountingConstants.PAYABLE_BANKTRANSFER_CREDIT);
                } else {
                    paymentData.setType(AccountingConstants.PAYABLE_BANK_CHECK_SHARE);
                }

                paymentData.setTotal(totalAmount);
                appliedCreditList.add(paymentData);
            }
        }

        if (appliedCreditList.size() == 0) {
            bussy = false;
            Info.show(accountingStrings.thereIsNoCreditToApply(), Info.Type.INFO);
            return;
        }

        if (BigDecimal.ZERO.compareTo(creditLinkProvider.getDueAmount()) > 0) {
            bussy = false;
            Info.show(accountingStrings.amountNotMoreDueAmount(), Info.Type.INFO);
            return;
        }

        LoadingPanel.loading(true);
        ReceivePaymentData appliedCreditData = new ReceivePaymentData();
        appliedCreditData.setPayments(appliedCreditList.toArray(new PaymentData[]{}));

        BigDecimal payAmount = BigDecimal.ZERO, baseAmount = BigDecimal.ZERO;
        for (PaymentData bcipData : appliedCreditList) {
            if (AccountingConstants.PAYABLE_BANK_CHECK_SHARE.equals(bcipData.getType())) {
                payAmount = payAmount.add(bcipData.getPaymentAmount());
                baseAmount = baseAmount.add(bcipData.getBaseAmount());
            }
        }

        if (payAmount.compareTo(BigDecimal.ZERO) > 0 && baseAmount.compareTo(BigDecimal.ZERO) > 0) {
            PaymentData bankCheckTotalData = new PaymentData();
            bankCheckTotalData.setType(AccountingConstants.PAYABLE_BANK_CHECK_SHARE);
            bankCheckTotalData.setExpenseId(expenseReportsListItem.getId());
            if (expenseReportsListItem.getSupplier() != null) {
                bankCheckTotalData.setCrmAccount(expenseReportsListItem.getSupplier());
            }
            bankCheckTotalData.setTotal(totalAmount);
            bankCheckTotalData.setDate(new DateNonConvertable(new Date()));
            bankCheckTotalData.setExchangeRate(getPaymentExchangeRate());

            bankCheckTotalData.setPaymentAmount(payAmount);
            bankCheckTotalData.setBaseAmount(baseAmount);
            appliedCreditData.setBankCheckTotalData(bankCheckTotalData);
        }

        InvoiceService.App.get().applySupplierCreditData(appliedCreditData, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                bussy = false;
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                bussy = false;
                LoadingPanel.loading(false);
                creditLinkProvider.fireInvoicePaymentChange(Constants.PAYABLE);
                Info.show(accountingStrings.creditAppliedSuccessfully(), Info.Type.INFO);
                ExpensePrePaymentAndCreditLink.this.setVisible(false);
                creditDialogBox.close();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_PAYMENT_DELETE, "EXPENSE_PAYMENT"
                        , ExpensePrePaymentAndCreditLink.this);
            }
        });
    }

    /**
     * For the Pre payment we have to use invoice exchange rate not the payment exchange rate
     *
     * @return
     */
    private BigDecimal getPaymentExchangeRate() {
        return expenseReportsListItem.getExchangeRate().setScale(AccountingUtils.get().getExRateScale(), RoundingMode.HALF_UP);

        /*if (!"".equals(creditLinkProvider.getPaymentExRateTxtBox().getText())) {
            return AccountingUtils.get().parseToBigDecimal(creditLinkProvider.getPaymentExRateTxtBox().getText());
        } else {
            return new BigDecimal("1.00");
        }*/
    }

    public void applyCreditData(PaymentData[] paymentsCredits) {
        creditDialogBox = null;
        this.paymentsCredits = paymentsCredits;
        //setVisible(paymentsCredits != null && paymentsCredits.length > 0);
    }

    public void hideDialogBoxIfOpen() {
        if (creditDialogBox != null && creditDialogBox.isShowing()) {
            creditDialogBox.close();
        }
    }
}
