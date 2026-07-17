package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.CompanyGoalSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GoalAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 23, 2009
 * Time: 4:54:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class BusinGoalHistoryProcessor implements HistoryProcessor {
    private HrmsStrings hrmsStrings=HrmsStrings.App.get();
    public SinksContainer process(String containerName, String[] strings) {
        return new CompanyGoalSinksContainer(containerName + strings[0], "Company Goal View", strings);
    }

    public SinksContainer processAdd(String[] params) {
/*        return new CompanyGoalAddSinksContainer("busingoaladd", hrmsStrings.editCompanyGoal(), params);*/
        return new GoalAddSinksContainer("busingoaladd", hrmsStrings.addBusinessGoal(), params);
    }

}