package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.monthview;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;

import java.util.Date;

/**
 * Describes the layout of days (single, all and multiday) within a single
 * week that is visualized in the <code>MonthView</code>. A
 * <code>WeekLayoutDescription</code> is not aware of any other thing than
 * placing an appointment <em>horizontally</em>, i.e., without considering the
 * exact week the appointment belongs to. It is the <code>MonthLayoutDescription</code>
 * responsibility to allocate the month necessary <code>weeks</code> and
 * distributing appointments over them.
 *
 * @see MonthView
 * @see MonthLayoutDescription
 */
public class WeekLayoutDescription {

    private AppointmentStackingManager topAppointmentsManager = new AppointmentStackingManager();

    private DayLayoutDescription[] days = null;

    private Date calendarFirstDay = null;

    public WeekLayoutDescription(Date calendarFirstDay) {
        this.calendarFirstDay = calendarFirstDay;
        days = new DayLayoutDescription[7];
    }

    private void assertValidDayIndex(int day) {
        if (day < 0 || day > days.length) {
            throw new IllegalArgumentException("Invalid day index (" + day + ")");
        }
    }

    private DayLayoutDescription initDay(int day) {
        assertValidDayIndex(day);
        if (days[day] == null) {
            days[day] = new DayLayoutDescription(day);
        }
        return days[day];
    }

    public boolean areThereAppointmentsOnDay(int day) {
        assertValidDayIndex(day);
        return days[day] != null || topAppointmentsManager.areThereAppointmentsOn(day);
    }

    public DayLayoutDescription getDayLayoutDescription(int day) {
        assertValidDayIndex(day);
        if (!areThereAppointmentsOnDay(day)) {
            return null;
        }
        return days[day];
    }

    public void addAppointment(Appointment appointment) {
        int dayOfWeek = dayInWeek(appointment.getStartDate());
        /*if (appointment.isAllDay()) {
            topAppointmentsManager.assignLayer(new AppointmentLayoutDescription(dayOfWeek, appointment));
        } else {*/
        initDay(dayOfWeek).addAppointment(appointment);
//        }
    }

    public int currentStackOrderInDay(int dayIndex) {
        return topAppointmentsManager.singleDayLowestOrder(dayIndex);
    }

    public void addMultiDayAppointment(Appointment appointment) {
        int weekStartDay = dayInWeek(appointment.getStartDate());
        int weekEndDay = dayInWeek(appointment.getEndDate());
        topAppointmentsManager.assignLayer(new AppointmentLayoutDescription(weekStartDay, weekEndDay, appointment));
    }

    public void addMultiWeekAppointment(Appointment appointment, AppointmentWidgetParts presenceInMonth) {
        switch (presenceInMonth) {
            case FIRST_WEEK:
                int weekStartDay = dayInWeek(appointment.getStartDate());
                if (weekStartDay < 0) {
                    weekStartDay = 0;
                }
                topAppointmentsManager.assignLayer(new AppointmentLayoutDescription(weekStartDay, 6, appointment));
                break;
            case IN_BETWEEN:
                topAppointmentsManager.assignLayer(new AppointmentLayoutDescription(0, 6, appointment));
                break;
            case LAST_WEEK:
                int weekEndDay = dayInWeek(appointment.getEndDate());
                topAppointmentsManager.assignLayer(new AppointmentLayoutDescription(0, weekEndDay, appointment));
                break;
        }
    }

    private int dayInWeek(Date date) {
        int day = (int) Math.floor(DateUtil.differenceInDays(date, calendarFirstDay) % 7d);
        if (day < 0) {
            day = 0;
        }
        return day;
    }

    public AppointmentStackingManager getTopAppointmentsManager() {
        return topAppointmentsManager;
    }
}