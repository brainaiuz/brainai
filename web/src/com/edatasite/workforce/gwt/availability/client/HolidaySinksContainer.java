package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AddEditHolidayView;
import com.edatasite.workforce.gwt.availability.client.ui.view.HolidaySummaryView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class HolidaySinksContainer extends SinksContainer {

    public HolidaySinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        HolidaySummaryView holidaySummaryView = new HolidaySummaryView(id);
        addView(holidaySummaryView);
        AddEditHolidayView holidayView = new AddEditHolidayView(id);
        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_EDIT_HOLIDAY)) {
            addView(holidayView);
        }
    }

}
