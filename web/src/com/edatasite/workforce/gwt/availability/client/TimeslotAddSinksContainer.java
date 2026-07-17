package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddTimeslotView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class TimeslotAddSinksContainer extends SinksContainer {

    public TimeslotAddSinksContainer(String id, String name, String description) {
        super(id, name);
    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_ADD_TIMESLOT)) {
            addView(new AddTimeslotView());
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}