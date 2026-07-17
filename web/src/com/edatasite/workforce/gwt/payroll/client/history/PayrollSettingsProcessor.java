package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.StarterViewSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;

/**
 * Created by IntelliJ IDEA.
 * User: xushnud
 * Date: 01-May-2010
 * Time: 14:38:39
 * To change this template use File | Settings | File Templates.
 */
public class PayrollSettingsProcessor implements HistoryProcessor {
     private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    public SinksContainer process(String containerName, String[] strings) {
        return new StarterViewSinksContainer(containerName + strings[0], payrollStrings.addExistingEmployee() + (strings.length > 1 && strings[1] != null ? " " + strings[1] : ""), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
