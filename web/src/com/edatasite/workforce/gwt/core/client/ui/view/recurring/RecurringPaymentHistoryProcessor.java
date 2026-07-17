package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECURRING_PAY_DEDUCTION_LIST;

public class RecurringPaymentHistoryProcessor implements HistoryProcessor {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new RecurringPaymentViewSinksContainer(containerName + strings[0], Property.get(RECURRING_PAY_DEDUCTION_LIST, payrollStrings.recurringPaymentCategory()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new RecurringPaymentAddSinksContainer("recurringPaymentadd", Property.get(RECURRING_PAY_DEDUCTION_LIST, payrollStrings.recurringPaymentCategory()), params);
    }
}
