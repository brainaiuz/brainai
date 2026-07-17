package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.CompanyGoalAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.CompanyGoalSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 23, 2009
 * Time: 4:54:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyGoalHistoryProcessor implements HistoryProcessor {
    private HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CompanyGoalSinksContainer(containerName + strings[0], hrmsStrings.companyGoalView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new CompanyGoalAddSinksContainer("companygoaladd", hrmsStrings.addCompanyGoal(), params);
    }

}