package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPaymentItem;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

public class SinglePayrunPaymentQuickSummary extends KpiSideNavBox implements Constants {

    interface SinglePayrunPaymentQuickSummaryUiBinder extends UiBinder<HTMLPanel, SinglePayrunPaymentQuickSummary> {}

    private static final SinglePayrunPaymentQuickSummaryUiBinder uiBinder = GWT.create(SinglePayrunPaymentQuickSummaryUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HTMLPanel container;
    @UiField
    FormGroup paymentDateField;
    @UiField
    FormGroup referenceField;
    @UiField
    FormGroup fromAccountField;
    @UiField
    FormGroup toAccountField;
    @UiField
    FormGroup bankAccountField;
    @UiField
    FormGroup detailsField;
    @UiField
    FormGroup amountField;

    private HTML header, paymentDate, reference, fromAccount, toAccount, bankAccount, details, amount;

    private WfmButton2 closeButton;

    private PayrunPaymentItem paymentItem;
    private final Integer paymentId;

    private String debugId = "single_payrun_payment_quick_summary_";

    public SinglePayrunPaymentQuickSummary(Integer paymentId) {
        this.paymentId = paymentId;
        uiBinder.createAndBindUi(this);
        addOpeningHandler(event -> loadData());
        show();
    }

    private void loadData() {
        PayrollService.App.get().getPayrunPaymentItem(paymentId, new AsyncCallback<PayrunPaymentItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(PayrunPaymentItem result) {
                paymentItem = result;
                initInternal();
                setValues();
            }
        });
    }

    private void initInternal() {
        //header
        header = new HTML();
        addHeader(header);

        paymentDate = new HTML();
        paymentDateField.setLabel(wfmStrings.paymentDate());
        paymentDateField.setContent(paymentDate);

        reference = new HTML();
        reference.ensureDebugId(debugId + "reference");
        referenceField.setLabel(wfmStrings.reference());
        referenceField.setContent(reference);

        fromAccount =  new HTML();
        fromAccount.ensureDebugId(debugId + "fromAccount");
        fromAccountField.setLabel(wfmStrings.paidFrom());
        fromAccountField.setContent(fromAccount);

        toAccount = new HTML();
        toAccount.ensureDebugId(debugId + "toAccount");
        toAccountField.setLabel(wfmStrings.paidTo());
        toAccountField.setContent(toAccount);

        bankAccount = new HTML();
        bankAccount.ensureDebugId(debugId + "bankAccount");
        bankAccountField.setLabel(wfmStrings.bankAccount());
        bankAccountField.setContent(bankAccount);

        details = new HTML();
        details.ensureDebugId(debugId + "details");
        detailsField.setLabel(wfmStrings.details());
        detailsField.setContent(details);

        amount = new HTML();
        amount.ensureDebugId(debugId + "amount");
        amountField.setLabel(wfmStrings.amount());
        amountField.setContent(amount);

        //body
        addBody(container);

        closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_PRIMARY);
        closeButton.ensureDebugId(debugId + "close_button");
        closeButton.addClickHandler(event -> remove());

        //footer
        addFooter(closeButton);
    }

    private void setValues() {
        header.setHTML(paymentItem.getEmployee());
        paymentDate.setHTML(DateUtils.format(paymentItem.getPaymentDate().getNonConvertedDate()));
        reference.setHTML(paymentItem.getReference());
        if (paymentItem.getPaidFromAccount() != null) {
            fromAccount.setHTML(paymentItem.getPaidFromAccount().getName());
        }
        if (paymentItem.getPaidToAccount() != null) {
            toAccount.setHTML(paymentItem.getPaidToAccount().getName());
        }
        bankAccount.setHTML(paymentItem.getBankAccount());
        details.setHTML(paymentItem.getDetails());
        amount.setHTML(PayrollClientUtils.format(paymentItem.getPaymentAmount()));
    }
}
