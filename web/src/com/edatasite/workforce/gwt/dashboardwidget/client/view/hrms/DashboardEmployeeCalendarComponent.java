package com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms;

import com.edatasite.workforce.gwt.core.client.ui.view.CalendarTabWidget;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import gwt.material.design.client.ui.html.Div;

/**
 * User: Abror Abdukadirov
 * Date: 30.11.2018 16:14
 */
public class DashboardEmployeeCalendarComponent extends DashboardBaseWidget {

    private CalendarTabWidget calendarTabWidget;
    private Integer employeeId = Utils.getUserID();

    public DashboardEmployeeCalendarComponent(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void initInternal() {
        setTitle(wfmStrings.calendar());

        Div wrapperDiv = new Div("pg_leave__second-sidebar box-bg--1 box-radius");
        Div calendarDiv = new Div("pg_leave__calendar box-bg--1 box-radius");

        calendarTabWidget = new CalendarTabWidget(employeeId, null, this::reloadWidgets);
        calendarDiv.add(calendarTabWidget);

        wrapperDiv.add(calendarDiv);

        contentPanel.add(wrapperDiv);

        resetPanel.setVisible(false);
    }

    @Override
    protected void getData() {

    }

    @Override
    protected void getSampleData(boolean nodata) {

    }

    private void reloadWidgets(Object object) {
    }

    @Override
    public String getCode() {
        return DASHBOARD_WIDGET_CODE.HRMS_EMPLOYEE_CALENDAR;
    }
}
