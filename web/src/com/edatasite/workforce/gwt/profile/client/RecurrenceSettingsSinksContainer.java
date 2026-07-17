package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.TimesheetReminderView;

import java.util.LinkedList;

/**
 * User: Ilxom Lutfullaev
 * Date: Apr 26, 2010
 * Time: 8:07:00 PM
 */

public class RecurrenceSettingsSinksContainer extends SinksContainer implements SchedulerConstant {

    public RecurrenceSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new TimesheetReminderView());
        addView(new TimesheetReminderView(true));

    }
}
