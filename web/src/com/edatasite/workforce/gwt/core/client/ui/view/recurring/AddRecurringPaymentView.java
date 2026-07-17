package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.user.client.Command;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

public class AddRecurringPaymentView extends AbstractRecurringPayDeductionView {

    public AddRecurringPaymentView() {
        super("add");
        setDescription(property.getSingular(payrollStrings.recurringPaymentCategory()));
    }

    public AddRecurringPaymentView(Integer objectId) {
        super("edit");
        this.objectID = objectId;
        setDescription(property.getSingular(payrollStrings.recurringPaymentCategory()));
    }

    @Override
    protected void initAdditionalFields() {
        categoryLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
        categoryLookUp.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    protected void setAdditionalData() {
    }

    @Override
    protected SelectItem[] getTerms() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.fixed()),
                new SelectItem(1, wfmStrings.basicOfPersentage())
//                new SelectItem(3, payrollStrings.minimumWage())
        };
    }

    @Override
    protected Command onChangeTerms() {
        return () -> {
            termsFormGroup.getGroupLabel().clear();
            if (terms.getSelectedItem().getId() == 0) {
                paymentAmount.setPlaceHolder(wfmStrings.amount());
            } else {
                paymentAmount.setPlaceHolder("%");
            }
        };
    }

    @Override
    protected void getAdditionalData(RecurringPayDeductItem item) {

    }

    @Override
    protected PayType getPayType() {
        return PayType.PAYMENT;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_RECURRING_PAYMENT_FORM;
    }
}
