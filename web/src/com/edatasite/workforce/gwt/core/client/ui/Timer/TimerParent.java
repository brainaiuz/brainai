package com.edatasite.workforce.gwt.core.client.ui.Timer;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AttendanceStats;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

import java.util.Date;

/**
 * User: Eminem
 * Date: 19/03/2016 14:43
 */

public class TimerParent extends PopupPanel implements Constants {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final DateTimeFormat timerFormat = DateTimeFormat.getFormat("HH:mm:ss");
    protected Label hoursLabel;

    public TimerParent(boolean autoHide) {
        super(autoHide);
    }

    protected boolean validateAgainstTimesheetSettimgs(AttendanceStats attendanceStats) {
        Date clientToday = new Date();
        DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat());
        Integer lastMinutes = (int) Math.ceil((double) parseSecond(hoursLabel.getText()) / 60);
        if ("true".equals(Utils.userSettings.get(VALIDATE_TASK_START))) {
            if ((clientToday.getYear() == attendanceStats.getTaskStart().getYear() && clientToday.getMonth() == attendanceStats.getTaskStart().getMonth() && clientToday.getDate() < attendanceStats.getTaskStart().getDate()) ||
                (clientToday.getYear() == attendanceStats.getTaskStart().getYear() && clientToday.getMonth() < attendanceStats.getTaskStart().getMonth()) ||
                clientToday.getYear() < attendanceStats.getTaskStart().getYear()) {
                Info.show(wfmStrings.taskStartDateValidationMessage() + format.format(attendanceStats.getTaskStart()), Info.Type.WARNING);
                return false;
            }
        }
        if ("true".equals(Utils.userSettings.get(VALIDATE_TASK_END))) {
            if ((clientToday.getYear() == attendanceStats.getTaskEnd().getYear() && clientToday.getMonth() == attendanceStats.getTaskEnd().getMonth() && clientToday.getDate() > attendanceStats.getTaskEnd().getDate()) ||
                (clientToday.getYear() == attendanceStats.getTaskEnd().getYear() && clientToday.getMonth() > attendanceStats.getTaskEnd().getMonth()) ||
                clientToday.getYear() > attendanceStats.getTaskEnd().getYear()) {
                Info.show(wfmStrings.taskEndDateValidationMessage() + format.format(attendanceStats.getTaskEnd()), Info.Type.WARNING);
                return false;
            }
        }
        if ("true".equals(Utils.userSettings.get(VALIDATE_HOLIDAY)) && attendanceStats.isHoliday()) {
            Info.show(wfmStrings.holidayValidationMessage(), Info.Type.WARNING);
            return false;
        }
        //check for the weekend, weekend is when the timeslot equals to ZERO
        if ("true".equals(Utils.userSettings.get(VALIDATE_DAY_OFF)) && attendanceStats.isDayOff()) {
            Info.show(wfmStrings.dayOffValidationMessage(), Info.Type.WARNING);
            return false;
        }
        if ("true".equals(Utils.userSettings.get(VALIDATE_lEAVE_REQUEST)) && (attendanceStats.getLeaveMinutes() > 0)) {
            if (attendanceStats.getTimeslotMinutes() - attendanceStats.getLeaveMinutes() == 0) {
                Info.show(wfmStrings.dailyLeaveRequestValidationMessage(), Info.Type.WARNING);
                return false;
            }
            int maxMinutesAllowed = 0;
            if ("true".equals(Utils.userSettings.get(VALIDATE_MAXIMUM_HOURS)) && "false".equals(Utils.userSettings.get(VALIDATE_TIMESLOT))) {
                if (Utils.userSettings.get(MAXIMUM_HOURS) != null && !Utils.userSettings.get(MAXIMUM_HOURS).equals("")) {
                    maxMinutesAllowed = Integer.valueOf(Utils.userSettings.get(MAXIMUM_HOURS)) * 60;
                }
            } else {
                maxMinutesAllowed = attendanceStats.getTimeslotMinutes();
            }

            if (maxMinutesAllowed - attendanceStats.getLeaveMinutes() - attendanceStats.getTimesheetMinutes() < lastMinutes) {
                Info.show(wfmStrings.hourlyLeaveRequestValidationMessage() + Utils.formatMinutes(maxMinutesAllowed - attendanceStats.getLeaveMinutes()) + " " + wfmStrings.hours(), Info.Type.WARNING);
                return false;
            }
        }
        if ("true".equals(Utils.userSettings.get(VALIDATE_MAXIMUM_HOURS))) {
            if ("true".equals(Utils.userSettings.get(VALIDATE_TIMESLOT))) {
                if (attendanceStats.getTimeslotMinutes() - attendanceStats.getTimesheetMinutes() < lastMinutes) {
                    Info.show(wfmStrings.timeslotValidationMessage() + Utils.formatMinutes(attendanceStats.getTimeslotMinutes()) + " " + wfmStrings.hours(), Info.Type.WARNING);
                    return false;
                }
            } else {
                int maxHoursAllowed = 0;
                if (Utils.userSettings.get(MAXIMUM_HOURS) != null && !Utils.userSettings.get(MAXIMUM_HOURS).equals("")) {
                    maxHoursAllowed = Integer.valueOf(Utils.userSettings.get(MAXIMUM_HOURS));
                }
                if ((maxHoursAllowed * 60) - attendanceStats.getTimesheetMinutes() < lastMinutes) {
                    Info.show(wfmStrings.timeslotValidationMessage() + maxHoursAllowed + " " + wfmStrings.hours(), Info.Type.WARNING);
                    return false;
                }
            }
        }
        return true;
    }

    protected static Integer parseSecond(String minutes) throws NumberFormatException, StringIndexOutOfBoundsException {
        if (minutes == null || minutes.equals("")) {
            return 0;
        }
        String[] parts = minutes.split(String.valueOf(":"));
        int h = 0;
        int m = 0;
        int s = 0;
        h = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        s = Integer.parseInt(parts[2]);

        return h * 3600 + m * 60 + s;
    }
}
