package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddShiftSettingsView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ShiftSettingsAddSinksContainer extends SinksContainer {

    public ShiftSettingsAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_ADD_TIMESLOT)) {
            addView(new AddShiftSettingsView(id));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}