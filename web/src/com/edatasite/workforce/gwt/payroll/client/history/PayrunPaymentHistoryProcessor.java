package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrunPaymentAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrunPaymentViewSinksContainer;

public class PayrunPaymentHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PayrunPaymentViewSinksContainer(containerName + strings[0], wfmStrings.payment(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new PayrunPaymentAddSinksContainer("payrunPaymentAdd", wfmStrings.payment(), params);
    }
}
