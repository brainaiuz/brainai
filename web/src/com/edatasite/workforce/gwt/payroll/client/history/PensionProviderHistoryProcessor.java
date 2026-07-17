package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PensionProviderAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PensionProviderViewSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 12, 2009
 * Time: 5:52:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionProviderHistoryProcessor implements HistoryProcessor {
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    public SinksContainer process(String containerName, String[] strings) {
        return new PensionProviderViewSinksContainer(containerName + strings[0], payrollStrings.pensionProvider(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PensionProviderAddSinksContainer("pensionprovideradd", payrollStrings.newPensionProvider(), params);
    }
}
