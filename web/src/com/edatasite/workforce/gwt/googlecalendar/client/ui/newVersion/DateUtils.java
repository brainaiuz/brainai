package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion;

import java.util.Date;

/**
 * Contains utility methods involving dates. This class should remain GWT-API
 * independent.
 */
@SuppressWarnings("deprecation")
public class DateUtils {

    public static final int MILLIS_PER_SECOND = 1000;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;

    public static final long MILLIS_IN_A_DAY = MILLIS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR * HOURS_PER_DAY;

    /**
     * Provides a <code>null</code>-safe way to return the number of millisecons
     * on a <code>date</code>.
     *
     * @param date The date whose value in milliseconds will be returned
     * @return The number of milliseconds in <code>date</code>, <code>0</code>
     *         (zero) if <code>date</code> is <code>null</code>.
     */
    private static long safeInMillis(Date date) {
        return date != null ? date.getTime() : 0;
    }

    /**
     * Returns the number of days between the passed dates.
     *
     * @param endDate   The upper limit of the date range
     * @param startDate The lower limit of the date range
     * @return The number of days between <code>endDate</code> and <code>startDate</code> (inclusive)
     */
    public static int differenceInDays(Date end, Date start) {
//        long startMillis = getTime(start);
//        long endMillis = getTime(end);
//        return (int) Math.ceil((double) (endMillis - startMillis) / (double) MILLIS_IN_A_DAY);
        int days = 0;
        if (compare(start, end)) {
            return -1;
        }

        while (!areOnTheSameDay(start, end)) {
            start = addDays(start, 1);
            days++;
        }

        return days;
    }

    /**
     * Resets the date to have no time modifiers (hours, minutes, seconds.)
     *
     * @param date The date to reset
     */
    public static Date resetTime(Date date) {
        long msec = safeInMillis(date);
        msec = (msec / 1000) * 1000;
        date.setTime(msec);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        return date;
    }

    public static boolean areOnTheSameDay(Date date1, Date date2) {
        return date1.getDate() == date2.getDate() && date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear();
    }

    public static boolean areOnTheSameYear(Date date1, Date date2) {
        return date1.getYear() == date2.getYear();
    }

    public static boolean isMoreThanOneDay(Date start, Date end) {
        start = safeDate(start);
        end = safeDate(end);
        return MILLIS_IN_A_DAY < (end.getTime() - start.getTime());
    }

    public static boolean isOneDay(Date start, Date end) {
        long difference = getTime(end) - getTime(start);
        return difference == DateUtils.MILLIS_IN_A_DAY;
    }

    public static boolean compare(Date start, Date end) {
        start = safeDate(start);
        end = safeDate(end);
        return start.getTime() >= end.getTime();
    }

    public static Date getDateWithZeroMinutes(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), 00, 00);
    }

    public static Date addTime(Date date, int hours, int minutes) {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), date.getHours() + hours, date.getMinutes() + minutes, 0);
    }

    /**
     * Returns new Date instance with same time, but only with added days.
     * Even seconds will be exactly with given date's seconds.
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        return new Date(date.getYear(), date.getMonth(), date.getDate() + days, date.getHours(), date.getMinutes(), date.getSeconds());
    }

    public static Date safeDate(Date date) {
        Date safeDate = new Date(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes()/*, date.getSeconds()*/);
        return new Date(safeDate.getTime());
    }

    public static long getTime(Date date) {
        return date.getTime() - ((long) date.getTimezoneOffset() * SECONDS_PER_MINUTE * MILLIS_PER_SECOND);
    }

    public static int calculateDateInMinutes(Date date) {
        return date.getHours() * MINUTES_PER_HOUR + date.getMinutes();
    }
}
