package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.profile.client.EmployeeBenefitSinksContainer;

/**
 * Created by Djuraev on 8/5/15.
 */
public class EmployeeBenefitHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new EmployeeBenefitSinksContainer(containerName + strings[0], hrmsStrings.employeeBenefitAllowance(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new EmployeeBenefitSinksContainer("employeeBenefitView", hrmsStrings.employeeBenefitAllowance(), params);
    }
}
