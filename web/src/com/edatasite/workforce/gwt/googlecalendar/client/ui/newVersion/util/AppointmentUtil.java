package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Utility class for several operations involving time and {@link Appointment}
 * objects.
 */
public class AppointmentUtil {

    public static ArrayList filterListByDateRange(ArrayList<Appointment> fullList, Date date, int days) {
        ArrayList<Appointment> group = new ArrayList<>();
        Date startDate = DateUtil.resetTime(date);
        Date endDate = DateUtil.addDays(DateUtil.resetTime(date), days);

        for (Appointment appointment : fullList) {
            if ((appointment.isMultiDay() || appointment.isAllDay()) &&
                    rangeContains(appointment, startDate, endDate) && appointment.isVisible()) {
                group.add(appointment);
            }
        }

        return group;
    }

    public static boolean rangeContains(Appointment appointment, Date date) {
        Date rangeEnd = DateUtil.addDays(DateUtil.resetTime((Date) date.clone()), 1);
        return rangeContains(appointment, date, rangeEnd)/* && isInAllDayAppointmentRange(appointment, date)*/;
    }

    /**
     * For all day appointments we have two days but the same time,
     * therefore appointment's start date is enough for showing it.
     *
     * @param appointment
     * @param date
     * @return
     */
    /*private static boolean isInAllDayAppointmentRange(Appointment appointment, Date date) {
        if (appointment.isAllDay()) {
            return DateUtil.areOnTheSameDay(appointment.getStart(), date);
        }
        return true;
    }*/

    /**
     * Indicates whether the specified <code>appointment</code> falls within the
     * date range defined by <code>rangeStart</code> and <code>rangeEnd</code>.
     *
     * @param appointment The appointment to test
     * @param rangeStart  The range lower limit
     * @param rangeEnd    The range upper limit
     * @return <code>true</code> if the appointment's date falls within the range, <code>false</code> otherwise.
     */
    public static boolean rangeContains(Appointment appointment, Date rangeStart, Date rangeEnd) {
        long apptStartMillis = appointment.getStartDate().getTime();
        long apptEndMillis = appointment.getEndDate().getTime();
        long rangeStartMillis = rangeStart.getTime();
        long rangeEndMillis = rangeEnd.getTime();

        return apptStartMillis >= rangeStartMillis && apptStartMillis < rangeEndMillis
                || apptStartMillis <= rangeStartMillis && apptEndMillis >= rangeStartMillis;
    }

    /**
     * Filters a list of appointments and returns only appointments with a start
     * date equal to the date provided. FYI - I hate everything about this
     * method and am pissed off I have to use it. May be able to avoid it in the
     * future
     *
     * @param fullList
     * @param startDate
     * @return
     */
    public static ArrayList<Appointment> filterListByDate(ArrayList<Appointment> fullList, Date startDate) {
        ArrayList<Appointment> group = new ArrayList<>();
        startDate = DateUtil.resetTime(startDate);
        Date endDate = DateUtil.addDays(startDate, 1);

        for (Appointment appointment : fullList) {
            if (appointment.isVisible() && !appointment.isMultiDay() && !appointment.isAllDay() &&
                    DateUtil.compareWithHours(appointment.getStartDate(), startDate) && DateUtil.compareWithHours(endDate, appointment.getEndDate())) {
                group.add(appointment);
            }
        }

        return group;
    }

    public static ArrayList<Appointment> getListByDateRange(ArrayList<Appointment> appointments, Date startDate) {
        ArrayList<Appointment> group = new ArrayList<>();
        startDate = DateUtil.resetTime(startDate);

        for (Appointment appointment : appointments) {
            if (DateUtil.compareByDate(startDate, appointment.getStartDate()) &&
                DateUtil.compareByDate(appointment.getEndDate(), startDate) && appointment.isVisible()) {
                group.add(appointment);
            }
        }

        return group;
    }

}
