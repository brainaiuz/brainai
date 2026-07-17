package com.edatasite.workforce.gwt.invoice.client.ui.view.components;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.ui.ItemUploadForm;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentAndPrePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PrePaymentAndCreditLink;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PrePaymentLinkProvider;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class InvoicePaymentWidget extends Composite {
    interface InvoicePaymentWidgetUiBinder extends UiBinder<HTMLPanel, InvoicePaymentWidget> {
    }

    public interface SavePaymentCommand {
        void execute(boolean checkExistingReference, boolean isValid, boolean isOverpayment);
    }

    private static final InvoicePaymentWidgetUiBinder ourUiBinder = GWT.create(InvoicePaymentWidgetUiBinder.class);
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    @UiField
    Div amountToPayContainer;
    @UiField
    Div paymentDateContainer;
    @UiField
    Div paymentAccountContainer;
    @UiField
    Div referenceNumberContainer;
    @UiField
    Div buttonContainer;
    @UiField
    Div overpaymentAccountContainer;
    @UiField
    Div overpaymentAmountContainer;

    //private DataListBox paymentMethod;
    /**
     * Amount to pay or refound in purchase cases
     */
    private TextBox amountToPay;
    /**
     * Payment transaction date
     */
    private DatePicker paymentDate;
    /**
     * Pay to or pay from account
     */
    private PaymentAccountsLookUp paymentAccountLookUp;

    private PaymentAccountsLookUp overpaymentAccountLookUp;
    private TextBox overpaymentAmountTxtBox;
    private FormGroup overpaymentAccountItem;
    private FormGroup overpaymentAmountItem;
    /**
     * Reference number related to the payment transaction
     */
    private TextBox referenceNumber;
    /**
     * Attachemnts for payment transaction
     */
    private ItemUploadForm paymentUploadForm;
    /**
     * If the client/supplier has already paid amount
     * then future will be shown up
     */
    private PrePaymentAndCreditLink applyCreditLink;
    /**
     * Run receive payment or refound payment functionality
     */
    private WfmButton2 paymentButton;

    /**
     * this one will use for debugging purpose
     */
    private final String debuggingCode = "Invoice_Payment_Widget_";

    private final NewInvoice invoiceData;
    private BigDecimal paymentExchangeRate = BigDecimal.ONE;
    private final SavePaymentCommand saveCommand;
    private Command afterAppliedCreditCommand;

    public InvoicePaymentWidget(NewInvoice invoiceData, SavePaymentCommand saveCommand) {
        this.invoiceData = invoiceData;
        this.saveCommand = saveCommand;
        initWidget(ourUiBinder.createAndBindUi(this));
        initialize();
    }

    private void initialize() {
        amountToPay = new TextBox();
        amountToPay.ensureDebugId(debuggingCode + "amount_to_pay");
        amountToPay.setValue(invoiceData.getDueAmount().toString());
        if (invoiceData.isProgressInvoicing()) {
            amountToPay.setValue(invoiceData.getDueAmount().toString());
        }
        new KpiToolTip(amountToPay, String.valueOf(invoiceData.getDueAmount()));
        Validation.addNumericKeyboardListener(amountToPay, AccountingUtils.getPriceScale());
        amountToPay.addValueChangeHandler(c -> {
            showOverpaymentPanel(false);
        });

        paymentDate = new DatePicker(true);
        paymentDate.ensureDebugId(debuggingCode + "payment_date");
        paymentDate.setDate(DateUtil.resetTime(new Date()));
        paymentDate.addChangeHandler(changeEvent -> getPaymentExchangeRate());
        paymentDate.setEnabled(!Utils.hasGenericAccess(GenericSettingsEnum.PAYMENT_DATE_SHOULD_BE_EQUAL_TO_TODAY) || Utils.isAdmin());

        if (invoiceData.getExchageRate() != null) {
            paymentExchangeRate = invoiceData.getExchageRate();
        } else {
            getPaymentExchangeRate();
        }
        paymentAccountLookUp = new PaymentAccountsLookUp(true, false, false);
        paymentAccountLookUp.setCurrencyID(invoiceData.getCurrencyID());
        paymentAccountLookUp.ensureDebugId(debuggingCode + "payment_account");

        overpaymentAccountLookUp = new PaymentAccountsLookUp(true, true);
        overpaymentAccountLookUp.setAutocompleteOff();
        overpaymentAccountLookUp.setVisible(false);
        overpaymentAmountTxtBox = new TextBox(true);
        overpaymentAmountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        overpaymentAmountTxtBox.setVisible(false);
        Validation.addNumericKeyboardListener(overpaymentAmountTxtBox, AccountingUtils.getPriceScale());

        overpaymentAccountItem = new FormGroup("Overpayment Account", overpaymentAccountLookUp);
        overpaymentAmountItem = new FormGroup(wfmStrings.amount(), overpaymentAmountTxtBox);

        referenceNumber = new TextBox();
        referenceNumber.ensureDebugId(debuggingCode + "reference_number");
        referenceNumber.setText(invoiceData.getInvoiceNumber());

        paymentUploadForm = new ItemUploadForm(Constants.F_EXP, true);

        paymentButton = new WfmButton2(invoiceData.isCreditNote() ? accountingStrings.addRefund() : (Constants.RECEIVABLE.equals(invoiceData.getType()) ? accountingStrings.receive() : accountingStrings.pay()), WfmButton2.BTN_PRIMARY);
        paymentButton.addClickHandler(ch -> {
            enablePaymentButton(false);

            if (overpaymentAccountLookUp.isVisible()) {
                if (!Validation.validateLookUpRequired(paymentAccountLookUp)) {
                    enablePaymentButton(true);
                    return;
                }
                if (validate() && saveCommand != null) {
                    saveCommand.execute(true, false, true);
                } else {
                    enablePaymentButton(true);
                }
            } else if (validate() && saveCommand != null) {
                saveCommand.execute(true, false, false);
            } else {
                enablePaymentButton(true);
            }
        });

        applyCreditLink = new PrePaymentAndCreditLink(invoiceData, new PrePaymentLinkProvider() {
            @Override
            public BigDecimal getPaymentExRate() {
                return paymentExchangeRate;
            }

            @Override
            public BigDecimal getDueAmount() {
                return invoiceData.getDueAmount();
            }

            @Override
            public void fireInvoicePaymentChange(String type) {

                if (afterAppliedCreditCommand != null) {
                    afterAppliedCreditCommand.execute();
                }

                if (Constants.PAYABLE.equals(type)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_CHECK_SAVED, null, InvoicePaymentWidget.this);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, Constants.RECEIVABLE.equals(invoiceData.getType()) ? InvoiceSummaryView.ADD_SALEINVOICE : InvoiceSummaryView.ADD_PURCHASEINVOICE, InvoicePaymentWidget.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, null, InvoicePaymentWidget.this);
            }
        });
        applyCreditLink.setVisible(false);
        applyCreditLink.hideDialogBoxIfOpen();
        loadAccountCredits();
        initWidgetsAsField();
    }

    private void initWidgetsAsField() {

        //AMOUNT TO PAY field
        {
            FormGroup fieldAmountToPay = new FormGroup(amountToPay);
            fieldAmountToPay.getGroupLabel().addStyleName("label-group");
            StringBuilder label = new StringBuilder();
            label.append(wfmStrings.amount())
                    .append(" (")
                    .append(invoiceData.getCurrencyName())
                    .append(")");
            fieldAmountToPay.getGroupLabel().add(new Span(label.toString()));
            fieldAmountToPay.getGroupLabel().add(applyCreditLink);
            amountToPayContainer.add(fieldAmountToPay);
        }

        //PAYMENT DATE field
        {
            paymentDateContainer.add(new FormGroup(wfmStrings.paidDate(), paymentDate));
        }

        //PAY TO ACCOUNT field
        {
            FormGroup fieldPayTo = null;

            if (Constants.RECEIVABLE.equals(invoiceData.getType())) {
                fieldPayTo = new FormGroup(invoiceData.isCreditNote() ? wfmStrings.paidFrom() : wfmStrings.paidTo(), paymentAccountLookUp);
            } else {
                fieldPayTo = new FormGroup(invoiceData.isCreditNote() ? wfmStrings.paidTo() : wfmStrings.paidFrom(), paymentAccountLookUp);
            }
            paymentAccountContainer.add(fieldPayTo);
        }

        //OVERPAYMENT widget
        {
            FormGroup fieldPayTo = new FormGroup();
            overpaymentAmountItem.setVisible(false);
            overpaymentAccountItem.setVisible(false);
            overpaymentAmountContainer.add(overpaymentAmountItem);
            overpaymentAccountContainer.add(overpaymentAccountItem);
        }

        //REFERENCE NUMBER field
        {
            Div inputGroup = new Div("input-group");
            Div prepend = new Div("input-group-prepend");
            prepend.getElement().setAttribute("style", "height:30px");
            inputGroup.add(referenceNumber);
            inputGroup.add(prepend);

            Div prependedContent = new Div("input-group-text");
            prependedContent.add(paymentUploadForm);
            prepend.add(prependedContent);

            FormGroup fieldReferenceNumber = new FormGroup(accountingStrings.refOrChequeNumber(), inputGroup);
            referenceNumberContainer.add(fieldReferenceNumber);
        }
        FormGroup fieldPayButton = new FormGroup(paymentButton);
        fieldPayButton.setLabel("&nbsp;");
        buttonContainer.add(fieldPayButton);
    }

    /**
     * Load exchange rate by selected payment date for the payment transaction
     */
    private void getPaymentExchangeRate() {
        CurrencyService.App.get().getCurrencyRateByDate(invoiceData.getCurrencyID(), new DateNonConvertable(paymentDate.getDate()), new AsyncCallback<CurrencyListItem>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(CurrencyListItem result) {
                paymentExchangeRate = BigDecimal.valueOf(result.getExchangeRate());
            }
        });
    }

    private boolean validate() {
        int errors = 0;

        if (!Validation.validateTextBoxRequired(amountToPay)) {
            errors++;
        }

        if (Validation.validateDateOrder(paymentDate.getDate(), invoiceData.getInvoiceDate().getNonConvertedDate())) {
            paymentDate.addStyleName("x-form-invalid");
            Info.show(WfmStrings.App.get().paymentDateCannotBeEarlierThanDate(), Info.Type.WARNING);
            errors++;
        } else {
            paymentDate.removeStyleName("x-form-invalid");
        }
        if (Utils.isSalesLocked() && DateUtils.getTransactionLockDate().after(paymentDate.getDate())) {
            errors++;
            Info.show(AccountingMessages.App.get().dateShouldBeAfterClosedBeforeDate(wfmStrings.paymentDate(), Utils.getTransactionLockDate()), Info.Type.WARNING);
        }

        if (!Validation.validateLookUpRequired(paymentAccountLookUp)) {
            errors++;
        }

        if (!Validation.validateTextBoxRequired(referenceNumber)) {
            errors++;
        }

        if (!"".equals(amountToPay.getText())) {
            BigDecimal amountForPay = AccountingUtils.get().parseToBigDecimal(amountToPay.getText()).subtract(getOverPaymentAmount());
            amountForPay = AccountingUtils.get().parseToBigDecimal(AccountingUtils.get().formatPrice(amountForPay)).setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
            if (invoiceData.isProgressInvoicing()) {
                amountForPay = AccountingUtils.get().parseToBigDecimal(amountToPay.getText()).subtract(getOverPaymentAmount());
            }
            if (amountForPay.compareTo(invoiceData.getDueAmount()) > 0) {

                if (errors == 0) {
                    BigDecimal prePaymentAmount = amountForPay.subtract(invoiceData.getDueAmount()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    if (invoiceData.isProgressInvoicing()) {
                        prePaymentAmount = amountForPay.subtract(invoiceData.getDueAmount());
                    }
                    KpiModal dialogBox = new KpiModal();
                    dialogBox.setWidth(400);

                    HTML center = new HTML("<table align='center' cellspacing='5' cellpadding='0'><tr><td rowspan='2'></td><td>" +
                            accountingMessages.overpaymant(invoiceData.getCurrencyName(), AccountingUtils.get().format(prePaymentAmount)) + "</td></tr></table>");
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
                        if (saveCommand != null) {
                            saveCommand.execute(false, true, false);
                        }
                    });
                    BigDecimal finalPrePaymentAmount = prePaymentAmount;
                    overPaymentBtn.addClickHandler(clickEvent -> {
                        dialogBox.close();
                        showOverpaymentPanel(true);
                        Validation.validateLookUpRequired(overpaymentAccountLookUp);
                        overpaymentAmountTxtBox.setText(AccountingUtils.get().formatPrice(finalPrePaymentAmount));
                    });
                    cancelBtn.addClickHandler(clickEvent -> {
                        dialogBox.close();
                        enablePaymentButton(true);
                        Info.show(accountingStrings.amountNotMoreDueAmount(), Info.Type.WARNING);
                    });
                }
                errors++;
            }
        }

        return errors == 0;
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

    public void setAfterAppliedCreditCommand(Command command) {
        this.afterAppliedCreditCommand = command;
    }

    /**
     * Initialize payment
     *
     * @return
     */
    public PaymentData getPaymentData() {
        PaymentData paymentData = new PaymentData();
        paymentData.setInvoiceID(invoiceData.getID());
        paymentData.setPaymentAmount(AccountingUtils.get().parseToBigDecimal(amountToPay.getText()));
        paymentData.setDate(new DateNonConvertable(paymentDate.getDate()));
        paymentData.setPaymentAccount(paymentAccountLookUp.getSelectedItem());
        paymentData.setReferenceNumber(referenceNumber.getText());
        paymentData.setAttachments(paymentUploadForm.getAttachedFiles());
        paymentData.setCurrency(new SelectItem(invoiceData.getCurrencyID()));
        paymentData.setExchangeRate(paymentExchangeRate);
        paymentData.setType(invoiceData.getType());
        paymentData.setTotal(invoiceData.getTotalInInvoiceCurrency());
        paymentData.setCrmAccount(new SelectItem(invoiceData.getClientID(), invoiceData.getClientName()));
        return paymentData;
    }

    /**
     * load client/supplier prepayments
     * If yes show the apply credit link then
     */
    public void loadAccountCredits() {
        InvoiceService.App.get().getCustomerCreditData(invoiceData.getID(), invoiceData.getClientID(), false, new AsyncCallback<PaymentAndPrePaymentData>() {
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

    public BigDecimal getAmountToPay() {
        return AccountingUtils.get().parseToBigDecimal(amountToPay.getText());
    }

    public BigDecimal getOverPaymentAmount() {
        return AccountingUtils.get().parseToBigDecimal(overpaymentAmountTxtBox.getText());
    }

    public SelectItem getOverPaymentAccount() {
        return overpaymentAccountLookUp.getSelectedItem();
    }

    public void enablePaymentButton(boolean b) {
        paymentButton.setEnabled(b);
    }
}
