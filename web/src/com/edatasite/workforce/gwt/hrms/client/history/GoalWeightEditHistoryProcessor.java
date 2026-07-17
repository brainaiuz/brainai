package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GoalWeightEditSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: Aziz
 * Date: Nov 12, 2009
 * Time: 1:11:35 PM
 */
public class GoalWeightEditHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new GoalWeightEditSinksContainer(containerName + strings[0], hrmsStrings.editGoalWeights(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}