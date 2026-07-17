package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollPaymentAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollPaymentViewSinksContainer;

public class PayrollPaymentHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PayrollPaymentViewSinksContainer(containerName + strings[0], wfmStrings.payment(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new PayrollPaymentAddSinksContainer("payrollPaymentAdd", wfmStrings.payment(), params);
    }
}
