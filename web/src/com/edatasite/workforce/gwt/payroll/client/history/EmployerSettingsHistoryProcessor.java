package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.EmployerSettingsSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.EmployerSettingsViewSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;

public class EmployerSettingsHistoryProcessor implements HistoryProcessor {
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);


    public SinksContainer process(String containerName, String[] strings) {
        return new EmployerSettingsViewSinksContainer(containerName + strings[0], payrollStrings.employerSettings(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new EmployerSettingsSinksContainer("employersettingsadd", payrollStrings.employerSettings(), params);
    }

}