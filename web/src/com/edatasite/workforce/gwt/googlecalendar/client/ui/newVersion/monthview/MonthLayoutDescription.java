package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.monthview;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Describes the layout for all appointments in all the weeks displayed in a
 * <code>MonthView</code>. This class is responsible for the distribution of the
 * appointments over the multiple weeks they possibly span.
 */
public class MonthLayoutDescription {

    private Date calendarFirstDay = null;

    private WeekLayoutDescription[] weeks = new WeekLayoutDescription[6];

    public MonthLayoutDescription(Date calendarFirstDay, ArrayList<Appointment> appointments) {
        this.calendarFirstDay = calendarFirstDay;
        placeAppointments(appointments);
    }

    private void initWeek(int weekIndex) {
        if (weeks[weekIndex] == null) {
            weeks[weekIndex] = new WeekLayoutDescription(calendarFirstDay);
        }
    }

    private void placeAppointments(ArrayList<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            if (appointment.isVisible()) {
                int startWeek = calculateWeekFor(appointment.getStartDate(), calendarFirstDay);
                int endWeek = calculateWeekFor(appointment.getEndDate(), calendarFirstDay);

                // Needed to put this in because if appointment appears in prior month, we get a negative number,
                // which causes an index out of bounds exception. We also have to show it when startWeek returns
                // negative value, therefore we need extra condition to check and force to show it.
                if (startWeek >= 0 && startWeek < weeks.length) {
                    initWeek(startWeek);

                    if (appointment.isMultiDayAppointment()/* || appointment.isAllDayAppointment()*/) {
                        positionMultidayAppointment(startWeek, appointment);
                    } else {
                        weeks[startWeek].addAppointment(appointment);
                    }
                } else if (startWeek < 0 && endWeek >= 0) {//This means that appointment is laid in two months(current and previous months).
                    initWeek(startWeek = 0);

                    if (appointment.isMultiDayAppointment()) {
                        positionMultidayAppointment(startWeek, appointment);
                    } else {
                        weeks[startWeek].addAppointment(appointment);
                    }
                }
            }
        }
    }

    private boolean isMultiWeekAppointment(int startWeek, int endWeek) {
        return startWeek != endWeek;
    }

    private void positionMultidayAppointment(int startWeek, Appointment appointment) {
        int endWeek = calculateWeekFor(appointment.getEndDate(), calendarFirstDay);

        //need to account for an appointment that spans
        // multiple months
        if (endWeek >= weeks.length) {
            endWeek = weeks.length - 1;
        }

        initWeek(endWeek);

        if (isMultiWeekAppointment(startWeek, endWeek)) {
            distributeOverWeeks(startWeek, endWeek, appointment);
        } else {
            weeks[startWeek].addMultiDayAppointment(appointment);
        }
    }

    private void distributeOverWeeks(int startWeek, int endWeek, Appointment appointment) {
        weeks[startWeek].addMultiWeekAppointment(appointment, AppointmentWidgetParts.FIRST_WEEK);
        for (int week = startWeek + 1; week < endWeek; week++) {
            initWeek(week);
            weeks[week].addMultiWeekAppointment(appointment, AppointmentWidgetParts.IN_BETWEEN);
        }
        if (startWeek < endWeek) {
            initWeek(endWeek);
            weeks[endWeek].addMultiWeekAppointment(appointment, AppointmentWidgetParts.LAST_WEEK);
        }
    }

    private int calculateWeekFor(Date testDate, Date calendarFirstDate) {
        return (int) Math.floor(DateUtil.differenceInDays(testDate, calendarFirstDate) / 7d);
    }

    public WeekLayoutDescription[] getWeekDescriptions() {
        return weeks;
    }
}