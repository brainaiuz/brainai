package com.edatasite.workforce.gwt.availability.client.ui.view.customTabs;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialPanel;

/**
 * User: Ilhombek
 * Date: 11/29/11
 * Time: 7:02 PM
 */
public class TimeSlotTabWidget extends Composite {

    private Integer int_employeeID;
    private String tabName;
    private static WfmStrings wfmStrings = WfmStrings.App.get();
    private HTML html;
    private MaterialPanel panel = new MaterialPanel("pg_leave__timeslot box-bg--1 box-radius");
    private MaterialPanel body = new MaterialPanel("pg_leave__timeslot-content");

    public TimeSlotTabWidget(String tabName, Integer employeeID) {
        this.int_employeeID = employeeID;
        this.tabName = tabName;
        viewShow();
    }

    public void viewShow() {
        html = new HTML(tabName);
        html.addStyleName("pg_leave__timeslot-title");

        final FlexTable flexTable = new FlexTable();

        AvailabilityService.App.get().getEmpTimeslot(int_employeeID, new AbstractAsyncCallback<TimeslotItem>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(TimeslotItem timeSlot) {
                if (timeSlot.getName() != null) {
                    html.setHTML(timeSlot.getName());
                }
                flexTable.setHTML(0, 0, "&nbsp;");
                flexTable.setHTML(0, 1, wfmStrings.startTime());
                flexTable.getCellFormatter().addStyleName(0, 1, "blueBoldTitle");
                flexTable.setHTML(0, 2, wfmStrings.endTime());
                flexTable.getCellFormatter().addStyleName(0, 2, "blueBoldTitle");
                drawTable(timeSlot, flexTable);
            }
        });
        body.add(flexTable);

        panel.add(html);
        panel.add(body);
        initWidget(panel);
    }

    private void drawTable(TimeslotItem timeSlot, FlexTable flexTable) {
        int weekStartDate = Integer.valueOf(Utils.userSettings.get(Constants.OVERALL_DATE_PICKER_WEEK_START));
        if (weekStartDate == 1) {
            drawInitializeTable(flexTable, 1, wfmStrings.sunday(), timeSlot.getSunday());
            drawInitializeTable(flexTable, 2, wfmStrings.monday(), timeSlot.getMonday());
            drawInitializeTable(flexTable, 3, wfmStrings.tuesday(), timeSlot.getTuesday());
            drawInitializeTable(flexTable, 4, wfmStrings.wednesday(), timeSlot.getWednesday());
            drawInitializeTable(flexTable, 5, wfmStrings.thursday(), timeSlot.getThursday());
            drawInitializeTable(flexTable, 6, wfmStrings.friday(), timeSlot.getFriday());
            drawInitializeTable(flexTable, 7, wfmStrings.saturday(), timeSlot.getSaturday());
        } else if (weekStartDate == 2) {
            drawInitializeTable(flexTable, 1, wfmStrings.monday(), timeSlot.getMonday());
            drawInitializeTable(flexTable, 2, wfmStrings.tuesday(), timeSlot.getTuesday());
            drawInitializeTable(flexTable, 3, wfmStrings.wednesday(), timeSlot.getWednesday());
            drawInitializeTable(flexTable, 4, wfmStrings.thursday(), timeSlot.getThursday());
            drawInitializeTable(flexTable, 5, wfmStrings.friday(), timeSlot.getFriday());
            drawInitializeTable(flexTable, 6, wfmStrings.saturday(), timeSlot.getSaturday());
            drawInitializeTable(flexTable, 7, wfmStrings.sunday(), timeSlot.getSunday());
        } else {
            drawInitializeTable(flexTable, 1, wfmStrings.saturday(), timeSlot.getSaturday());
            drawInitializeTable(flexTable, 2, wfmStrings.sunday(), timeSlot.getSunday());
            drawInitializeTable(flexTable, 3, wfmStrings.monday(), timeSlot.getMonday());
            drawInitializeTable(flexTable, 4, wfmStrings.tuesday(), timeSlot.getTuesday());
            drawInitializeTable(flexTable, 5, wfmStrings.wednesday(), timeSlot.getWednesday());
            drawInitializeTable(flexTable, 6, wfmStrings.thursday(), timeSlot.getThursday());
            drawInitializeTable(flexTable, 7, wfmStrings.friday(), timeSlot.getFriday());
        }
    }

    private void drawInitializeTable(FlexTable flexTable, int row, String weekDayName, int[] weekDay) {
        flexTable.setHTML(row, 0, weekDayName + ":");
        flexTable.setText(row, 1, getTimeSlot(weekDay[0] / 60) + ":" + getTimeSlot(weekDay[0] % 60));
        flexTable.setText(row, 2, getTimeSlot(weekDay[1] / 60) + ":" + getTimeSlot(weekDay[1] % 60));
        flexTable.getCellFormatter().addStyleName(row, 0, "blueBoldTitle");
        flexTable.getCellFormatter().addStyleName(row, 1, "add-font");
        flexTable.getCellFormatter().addStyleName(row, 2, "add-font");

        flexTable.getCellFormatter().addStyleName(row, 1, "text-center");
        flexTable.getCellFormatter().addStyleName(row, 2, "text-center");
        if(((weekDay[0] / 60) == 0) && ((weekDay[0] % 60) == 0) && ((weekDay[1] / 60) == 0) && ((weekDay[1] % 60) == 0)){
            flexTable.getCellFormatter().addStyleName(row, 1, "text-red");
            flexTable.getCellFormatter().addStyleName(row, 2, "text-red");
        }
    }

    private String getTimeSlot(int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }
        if (String.valueOf(time).length() == 1) {
            return "00";
        }
        return String.valueOf(time);
    }
}