package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollCategoryV2AddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollCategoryV2ViewSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;

public class PayrollCategoryV2HistoryProcessor implements HistoryProcessor {
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PayrollCategoryV2ViewSinksContainer(containerName + strings[0], payrollStrings.payrollCategory(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new PayrollCategoryV2AddSinksContainer("payrollCategoryadd", payrollStrings.payrollCategory(), params);
    }
}
