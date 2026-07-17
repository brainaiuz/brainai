package com.edatasite.workforce.gwt.hrms.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GroupGoalAddSinksContainer;
import com.edatasite.workforce.gwt.hrms.client.GroupGoalViewSinksContainer;

public class GroupGoalHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new GroupGoalViewSinksContainer(containerName + strings[0], hrmStrings.groupGoal(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new GroupGoalAddSinksContainer("groupgoaladd", hrmStrings.addGroupGoal(), params);
    }
}
