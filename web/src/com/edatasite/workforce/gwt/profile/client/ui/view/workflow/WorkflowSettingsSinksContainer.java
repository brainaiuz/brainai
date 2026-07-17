package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.ApproversView;
import com.edatasite.workforce.gwt.profile.client.ui.view.TimesheetReminderView;

import java.util.LinkedList;

/**
 * User: Hayot
 * Date: 28.02.2014
 * Time: 15:45:56
 */
public class WorkflowSettingsSinksContainer extends SinksContainer {

    public WorkflowSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new WorkflowRuleListView());
        addView(new ApproversView());
        addView(new TimesheetReminderView(true));
        addView(new TimesheetReminderView());
    }
}
