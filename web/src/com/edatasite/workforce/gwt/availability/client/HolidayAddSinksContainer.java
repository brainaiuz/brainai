package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddEditHolidayView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class HolidayAddSinksContainer extends SinksContainer {

    public HolidayAddSinksContainer(String id, String name, String description) {
        super(id, name);
    }

    protected void initViews() {
        AddEditHolidayView holidayView = new AddEditHolidayView();
        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_ADD_HOLIDAY)) {
            addView(holidayView);
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
