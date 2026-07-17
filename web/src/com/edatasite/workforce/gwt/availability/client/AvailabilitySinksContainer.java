package com.edatasite.workforce.gwt.availability.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AnnualBalanceReport;
import com.edatasite.workforce.gwt.availability.client.ui.view.AnnualLeaveBalanceReportProrataBased;
import com.edatasite.workforce.gwt.availability.client.ui.view.AttendanceReportView2;
import com.edatasite.workforce.gwt.availability.client.ui.view.GlobalEmployeeLeaveRequestView;
import com.edatasite.workforce.gwt.availability.client.ui.view.LeaveRequestListView;
import com.edatasite.workforce.gwt.availability.client.ui.view.TeamAvailabilityView;
import com.edatasite.workforce.gwt.availability.client.ui.view.TerminalAttendance;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.BrigadaListView;
import com.edatasite.workforce.gwt.hrms.client.ui.ShiftListView;

import java.util.LinkedList;

public class AvailabilitySinksContainer extends SinksContainer {


    public AvailabilitySinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    public AvailabilitySinksContainer(String name, String description, String[] strings) {
        super(name, description, strings, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        //my attendance
        if (Utils.hasPermission(PermissionConstants.HRMS_MY_ATTENDANCE)) {
            addView(new GlobalEmployeeLeaveRequestView(null, id, null, "availability", wfmStrings.myAttendance(), GlobalEmployeeLeaveRequestView.FROM_IN_OUT_ATTENDANCE_TRACKING_VIEW));
        }
        //attendance tracking
        if (Utils.hasPermission(PermissionConstants.HRMS_ATTENDANCE_TRACKING)) {
            TeamAvailabilityView teamAvailabilityView = new TeamAvailabilityView();
            addView(teamAvailabilityView);
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_LIVE_REQUEST)) {
            addView(new LeaveRequestListView());
        }

        //attendance report
        if (Utils.hasPermission(PermissionConstants.HRMS_ATTENDANCE_REPORT)) {
            addView(new AttendanceReportView2());
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_TERMINAL_REPORT)) {
            addView(new TerminalAttendance());
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_SHIFT)) {
            addView(new ShiftListView());
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_BRIGADA)) {
            addView(new BrigadaListView());
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_ANNUAL_LEAVE_BALANCE_REPORT)) {
            if (Utils.isEnableProrataBasedAnnualLeave()) {
                addView(new AnnualLeaveBalanceReportProrataBased());
            } else {
                addView(new AnnualBalanceReport());
            }
        }
    }
}