package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECURRING_PAY_DEDUCTION_LIST;

public class RecurringDeductionHistoryProcessor implements HistoryProcessor {

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new RecurringDeductionViewSinksContainer(containerName + strings[0], Property.get(RECURRING_PAY_DEDUCTION_LIST, payrollStrings.recurringDeductionCategory()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new RecurringDeductionAddSinksContainer("recurringDeductionadd", Property.get(RECURRING_PAY_DEDUCTION_LIST, payrollStrings.recurringDeductionCategory()), params);
    }
}
