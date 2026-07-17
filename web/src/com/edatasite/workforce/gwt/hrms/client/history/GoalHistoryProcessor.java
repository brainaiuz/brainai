package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GoalAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GoalsSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 23, 2009
 * Time: 4:54:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoalHistoryProcessor implements HistoryProcessor {
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new GoalsSinksContainer(containerName + strings[0], hrmsStrings.goalView(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new GoalAddSinksContainer("goaladd", hrmsStrings.addPersonalGoal(), params);
    }

}
