package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.EditTimeslotView;
import com.edatasite.workforce.gwt.availability.client.ui.view.TimeslotSummaryView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class TimeslotSinksContainer extends SinksContainer {

    public TimeslotSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_EDIT_TIMESLOT)) {
            addView(new TimeslotSummaryView(id));
            addView(new EditTimeslotView(id));
        }
    }
}