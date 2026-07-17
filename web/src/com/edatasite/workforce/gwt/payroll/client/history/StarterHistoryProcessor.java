package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.StarterAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.StarterViewSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;

public class StarterHistoryProcessor implements HistoryProcessor {

    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new StarterViewSinksContainer(containerName + strings[0], wfmStrings.summaryView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new StarterAddSinksContainer("starteradd", wfmStrings.addEmployee(), params);
    }
}