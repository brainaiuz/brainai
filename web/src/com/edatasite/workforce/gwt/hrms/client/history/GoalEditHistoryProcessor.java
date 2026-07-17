package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GoalEditSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * Created with IntelliJ IDEA.
 * User: romeo
 * Date: 5/24/12
 * Time: 2:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class GoalEditHistoryProcessor implements HistoryProcessor{
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new GoalEditSinksContainer(containerName, hrmsStrings.editGoal(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
