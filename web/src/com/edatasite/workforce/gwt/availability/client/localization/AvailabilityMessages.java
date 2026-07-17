package com.edatasite.workforce.gwt.availability.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface AvailabilityMessages extends Messages {

    String timeNotValid(String time);

    String yourLeaveRequest(String name);

    String yourLeaveRequest1(String name);

    String noHolidaysMessage(String p0);

    String wantToDeleteHoliday(String p0);

    String holidayDeleted(String p0);

    String formText(String p0, String p1);

    String timeslotSetUpfor(String p0);

    String latestLeaveRequests2(String p0, String p1);

    String wantToDeleteTimeslot(String p0);

    String deletedTimeslot(String p0);

    String listBoxLabelText(String p0);

    String timeslotSetUpforVacation(String p0);

    String yourLeaveRequest1TimeOff(String p0);

    String latestLeaveRequests2TimeOff(String p0, String p1);

    String yourLeaveRequest1Robert(String p0);

    String leaveRequestLimitExceeded(String p0, String p1, String p2);

    String benefitRequestLimitExceeded(String p0, String p1, String p2);

    class App {
        public static AvailabilityMessages get() {
            return (AvailabilityMessages) GWT.create(AvailabilityMessages.class);
        }
    }
}
