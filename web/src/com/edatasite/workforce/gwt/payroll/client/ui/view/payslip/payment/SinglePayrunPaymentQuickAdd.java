package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.payment;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPaymentItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.SaveResultTO;
import com.edatasite.workforce.gwt.payroll.client.utils.PayrollClientUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;

public class SinglePayrunPaymentQuickAdd extends KpiSideNavBox implements Constants {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private static final SinglePayrunPaymentQuickAddUiBinder uiBinder = GWT.create(SinglePayrunPaymentQuickAddUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public boolean validateForm() {
        int errors = 0;
        if (!Validation.validateLookUpRequired(fromAccountLookup)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(toAccountLookup)) {
            errors++;
        }
        if (!Validation.validateDate(paymentDate)) {
            errors++;
        }

        if (!Validation.validateTextBoxRequired(amount)) {
            errors++;
        } else {
            BigDecimal paymentAmount = PayrollClientUtils.parseToBigDecimal(amount.getValue());
            if (paymentAmount.compareTo(paymentItem.getDueAmount()) > 0) {
                Info.show(payrollStrings.paymentAmountCannotbeMoreThanDueAmount(), Info.Type.WARNING);
                return false;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

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

    private HTML header;
    private DatePicker paymentDate;
    private TextBox reference;
    private PaymentAccountsLookUp fromAccountLookup;
    private AccountsLookUp toAccountLookup;
    private TextBox bankAccount;
    private TextBox details;
    private TextBox amount;

    private WfmButton2 saveButton;

    private PayrunPaymentItem paymentItem;
    private final Integer singlePayrunId;

    private final String debugId = "single_payrun_payment_quick_add_";

    public SinglePayrunPaymentQuickAdd(Integer singlePayrunId) {
        this.singlePayrunId = singlePayrunId;
        uiBinder.createAndBindUi(this);
        addOpeningHandler(event -> loadData());
        show();
    }

    private void loadData() {
        PayrollService.App.get().initPayrunPaymentItem(singlePayrunId, new AsyncCallback<PayrunPaymentItem>() {
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

        paymentDate = new DatePicker();
        paymentDateField.setLabel(wfmStrings.paymentDate(), true);
        paymentDateField.setContent(paymentDate);

        reference = new TextBox();
        reference.ensureDebugId(debugId + "reference");
        referenceField.setLabel(wfmStrings.reference());
        referenceField.setContent(reference);

        fromAccountLookup =  new PaymentAccountsLookUp(true);
        fromAccountLookup.ensureDebugId(debugId + "fromAccount");
        fromAccountField.setLabel(wfmStrings.paidFrom(), true);
        fromAccountField.setContent(fromAccountLookup);

        toAccountLookup = new AccountsLookUp("CURRENT_LIABILITY");
        toAccountLookup.ensureDebugId(debugId + "toAccount");
        toAccountField.setLabel(wfmStrings.paidTo(), true);
        toAccountField.setContent(toAccountLookup);

        bankAccount = new TextBox();
        bankAccount.ensureDebugId(debugId + "bankAccount");
        bankAccountField.setLabel(wfmStrings.bankAccount());
        bankAccountField.setContent(bankAccount);

        details = new TextBox();
        details.ensureDebugId(debugId + "details");
        detailsField.setLabel(wfmStrings.details());
        detailsField.setContent(details);

        amount = new TextBox();
        amount.ensureDebugId(debugId + "amount");
        Validation.addNumericKeyboardListener(amount);
        amountField.setLabel(wfmStrings.amount(), true);
        amountField.setContent(amount);

        //body
        addBody(container);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId(debugId + "save_button");
        saveButton.addClickHandler(event -> {
            enableButtons(false);
            if (validateForm()) {
                save();
            } else {
                enableButtons(true);
            }
        });

        //footer
        addFooter(saveButton);
    }

    private void setValues() {
        header.setHTML(paymentItem.getEmployee());
        paymentDate.setDate(paymentItem.getDueDate().getNonConvertedDate());
        amount.setValue(PayrollClientUtils.format(paymentItem.getDueAmount()));
    }

    private PayrunPaymentItem getData() {
        paymentItem.setPaidFromAccountID(fromAccountLookup.getSelectedItemID());
        paymentItem.setPaidToAccountID(toAccountLookup.getSelectedItemID());
        paymentItem.setPaymentAmount(PayrollClientUtils.parseToBigDecimal(amount.getValue()));
        paymentItem.setPaymentDate(new DateNonConvertable(paymentDate.getDate()));
        paymentItem.setReference(reference.getValue());
        paymentItem.setDetails(details.getValue());
        paymentItem.setBankAccount(bankAccount.getValue());
        return paymentItem;
    }

    public void save() {
        LoadingPanel.loading(true, container);
        PayrunPaymentItem paymentItem = getData();
        PayrollService.App.get().createPayrunPaymentItem(paymentItem, new AsyncCallback<SaveResultTO<Integer>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false, container);
                enableButtons(true);
                Info.warn(wfmStrings.sorrySomethingWentWrong(), Info.Position.BOTTOM_RIGHT);
            }

            @Override
            public void onSuccess(SaveResultTO<Integer> result) {
                LoadingPanel.loading(false);
                enableButtons(true);
                if (result == null) {
                    Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.WARNING);
                    return;
                }
                if (result.getResult() != null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYRUN_PAYMENT_ADD, null, SinglePayrunPaymentQuickAdd.this);
                    remove();
                } else if (result.getMessage() != null) {
                    Info.show(result.getMessage(), Info.Type.WARNING);
                }
            }
        });
    }

    interface SinglePayrunPaymentQuickAddUiBinder extends UiBinder<HTMLPanel, SinglePayrunPaymentQuickAdd> {
    }

    public void enableButtons(boolean enabled) {
        saveButton.setEnabled(enabled);
    }
}
