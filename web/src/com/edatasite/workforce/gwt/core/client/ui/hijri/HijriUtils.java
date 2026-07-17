package com.edatasite.workforce.gwt.core.client.ui.hijri;

import com.edatasite.workforce.gwt.core.client.Utils;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 12.09.14
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */
public class HijriUtils {

    public static SimpleHijriDate getHijriDate(Date date) {
        if (Utils.isAlternativeCalendar() && date != null) {
            return HijriCalc.toHijri(date);
        }
        return null;
    }

    public static String getHijriDateForAttendance(Date date){
        if (Utils.isAlternativeCalendar() && date != null) {
            return getHijriDate(date).getDatePickerCurrentDateAttendanceShortFormat();
        }
        return "";
    }

    public static String getCalendarWeeklyHijriPeriod(Date current,Date firstDayOfWeek, Date lastDayOfWeek) {
        if(Utils.isAlternativeCalendar() && firstDayOfWeek !=null && lastDayOfWeek !=null){
            SimpleHijriDate currnetHijriDate = getHijriDate(current);
            SimpleHijriDate firstHijriDate = getHijriDate(firstDayOfWeek);
            SimpleHijriDate secondHijriDate = getHijriDate(lastDayOfWeek);
            return " (" + currnetHijriDate.getMonthName() + " " + firstHijriDate.getDayOfMonth() + " - " + secondHijriDate.getDayOfMonth() + ", " + currnetHijriDate.getYear() + ")";
        }
        return "";
    }

    public static String getCalendarDaylylyHijriPeriod(Date date){
        if (Utils.isAlternativeCalendar() && date != null) {
            return getHijriDate(date).getCalendarHeaderDayDateFormat();
        }
        return "";
    }

    public static String getCalendarMonthlyHijriPeriod(Date date){
        if (Utils.isAlternativeCalendar() && date != null) {
            return getHijriDate(date).getCalendarHeaderMonthDateFormat();
        }
        return "";
    }

    public static String getHijriDayFromat(Date date) {
        if (Utils.isAlternativeCalendar() && date != null) {
            SimpleHijriDate dayHijriDate = getHijriDate(date);
            return " (" + (dayHijriDate.getMonth() + 1) + "/" + dayHijriDate.getDayOfMonth() + ")";
        }
        return "";
    }

    public static String getCalendarHijriDate(Date date) {
        if (Utils.isAlternativeCalendar() && date != null) {
            SimpleHijriDate calendarHijriDate = getHijriDate(date);
            return " (" + calendarHijriDate.getDayOfMonth() + ")";
        }
        return "";
    }
}
