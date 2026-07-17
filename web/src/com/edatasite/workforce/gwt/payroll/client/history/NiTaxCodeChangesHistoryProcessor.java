package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.NiTaxCodeChangesSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 31, 2009
 * Time: 7:19:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiTaxCodeChangesHistoryProcessor implements HistoryProcessor {
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    public SinksContainer process(String containerName, String[] strings) {
        return new NiTaxCodeChangesSinksContainer(containerName + strings[0], payrollStrings.changesView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
