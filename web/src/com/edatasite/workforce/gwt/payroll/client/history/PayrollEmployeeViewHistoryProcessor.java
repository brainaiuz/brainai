package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PayrollEmployeeViewSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Nov 2, 2009
 * Time: 9:11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollEmployeeViewHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PayrollEmployeeViewSinksContainer(containerName + strings[0], wfmStrings.summaryView() + (strings.length > 1 && strings[1] != null ? " " + strings[1] : ""), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PayrollEmployeeViewSinksContainer("payrollEmployeeView", wfmStrings.summaryView(), params);
    }
}