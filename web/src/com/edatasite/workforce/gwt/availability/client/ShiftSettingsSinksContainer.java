package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddShiftSettingsView;
import com.edatasite.workforce.gwt.availability.client.ui.view.ShiftSettingsSummaryView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ShiftSettingsSinksContainer extends SinksContainer {

    public ShiftSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_EDIT_TIMESLOT)) {
            addView(new ShiftSettingsSummaryView(id));
            addView(new AddShiftSettingsView(id));
        }
    }
}