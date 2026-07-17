package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrunPaymentListSinksContainer;
import com.google.gwt.core.client.GWT;

public class PayrunPaymentListHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = GWT.create(WfmStrings.class);

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PayrunPaymentListSinksContainer(containerName + strings[0], wfmStrings.payments(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
