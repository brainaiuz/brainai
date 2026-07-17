package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.datePicker.util.DateLocale;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormatInfo;
import com.google.gwt.i18n.client.impl.cldr.DateTimeFormatInfoImpl_en;

import java.util.Date;

@SuppressWarnings("deprecation")
public class DateUtil implements Constants {
    public static final int MILLIS_PER_SECOND = 1000;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;
    public static final long MILLIS_IN_A_DAY = MILLIS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR * HOURS_PER_DAY;
    private static final Integer secondsInDay = 86400000;

    /**
     * Add days to the Date object.
     *
     * @param date The Date to modify
     * @param days Number of day to add
     * @return The modified Date object
     */
//    public static Date addDays(Date date, int days) {
//        return new Date(date.getYear(), date.getMonth(), date.getDate() + days);
//    }

    /**
     * Add months to the Date object.
     *
     * @param date   The Date to modify
     * @param months Number of month to add
     * @return The modified Date object
     */
    public static Date addMonths(Date date, int months) {
        return new Date(date.getYear(), date.getMonth() + months, date.getDate() > 28 ? 1 : date.getDate());
    }

    public static Date addMonths(Date date, int months, int beginningDay) {
        return new Date(date.getYear(), date.getMonth() + months, beginningDay <= date.getDate() ? beginningDay : 0);
    }

    public static Date addHours(Date date, int hours) {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), date.getHours() + hours, date.getMinutes());
    }

    public static Date addMinutes(Date date, int minutes) {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes() + minutes);
    }

    /**
     * Test if two Date objects represent the same day. It tests if the days,
     * the months and the years are equals.
     *
     * @param date1 First Date
     * @param date2 Second Date
     * @return true if the days are the same
     */
    public static boolean areEquals(Date date1, Date date2) {
        return date1.getMinutes() == date2.getMinutes() && date1.getHours() == date2.getHours() && date1.getDate() == date2.getDate() && date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear();
    }

    public static boolean equalByMonths(Date date1, Date date2) {
        return date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear();
    }

    /**
     * Tests if two Date objects represent the other days. It compares by milliseconds and get exact result.
     *
     * @param date1 First Date
     * @param date2 Second Date
     * @return true if the date1 is more bigger than date2
     */
    public static boolean compare(Date date1, Date date2) {
        return date1.getTime() > date2.getTime();
    }

    /**
     * Tests if two Date objects represent the other days. It compares the days,
     * the months and the years.
     *
     * @param date1 First Date
     * @param date2 Second Date
     * @return true if the date1 is more bigger than date2
     */
    public static boolean compareByDate(Date date1, Date date2, boolean... firstDateMore) {
        Date first = new Date(date1.getYear(), date1.getMonth(), date1.getDate());
        Date second = new Date(date2.getYear(), date2.getMonth(), date2.getDate());

        if (firstDateMore.length > 0 && firstDateMore[0]) {
            return first.getTime() > second.getTime();
        }
        return first.getTime() >= second.getTime();
    }

    /**
     * Checks if two Date objects represent the other days. It compares with their time which is long
     *
     * @param date1 First Date
     * @param date2 Second Date
     * @return true if the date1 is equal or more bigger than date2
     */
    public static boolean equalAndMore(Date date1, Date date2) {
        return equalAndMore(date1.getTime(), date2.getTime());
    }

    /**
     * It compares two long parametres by their values.
     *
     * @param date1 First Date
     * @param date2 Second Date
     * @return true if the date1 is equal or more bigger than date2
     */
    public static boolean equalAndMore(long date1, long date2) {
        return date1 >= date2;
    }

    /**
     * Return a Date object with represents the first day of a month contained
     * in another Date object.
     *
     * @param date The Date containing the month
     * @return The first day of the month
     */
    public static Date getMonthFirstDay(Date date) {
        Date current = date;
//        while (current.getDate() != 1) {
        current = new Date(current.getYear(), current.getMonth(), 1);
        if (current.getDate() == 30 || current.getDate() == 31) {
            current.setDate(current.getDate() + 1);
        }
//        }
        return current;
    }

    public static Date getMonthLastDate(Date date) {
        int month = date.getMonth();
        Date current = (Date) date.clone();
        while (current.getMonth() == month) {
            current.setDate(current.getDate() + 1);
        }
        current.setDate(current.getDate() - 1);
        return current;
    }

    public static Date getMonthLastDateWithTime(Date date) {
        return getDayLastTime(getMonthLastDate(date));
    }

    public static Date getYearFirstDay(Date date) {
        Date current = date;
        current.setMonth(0);
        current = getMonthFirstDay(current);
        return current;
    }

    public static Date getYearLastDay(Date date) {
        Date current = date;
        current.setMonth(11);
        current = getMonthLastDate(current);
        return current;
    }

    /**
     * Returns the place of the day in the week.
     * Example : sunday = 0, monday = 1 ....
     * Depends on the locale.
     *
     * @param day The day
     * @return The place of the day
     */
    public static int getWeekDayIndex(Date day) {
        DateLocale locale = getDateLocale();
        int[] daysOrder = locale.getDAY_ORDER();
        int dayIndex = day.getDay();
        for (int i = 0; i < 7; i++) {
            if (dayIndex == daysOrder[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the first day of the current week.
     *
     * @return Date pointing to the first day
     */
    public static Date getWeekFirstDay() {
        return getWeekFirstDay(new Date());
    }

    /**
     * Returns the first day of the week containing a Date object.
     *
     * @param date The Date
     * @return The Date pointing to the first day
     */
    public static Date getWeekFirstDay(Date date) {
        DateLocale local = getDateLocale();
        int firstDay = local.getDAY_ORDER()[0];
        return getWeekFirstDay(date, firstDay);
    }

    public static Date getWeekFirstDay(Date date, Integer firstDay) {
        Date current = date;
        while (current.getDay() != firstDay) {
            current = new Date(current.getYear(), current.getMonth(), current.getDate() - 1);
        }
        return current;
    }

    /**
     * Returns the last day of the current week.
     *
     * @return Date pointing to the last day
     */
    public static Date getWeekLastDay() {
        return getWeekLastDay(new Date());
    }

    /**
     * Returns the last day of the week containing a Date object.
     *
     * @param date The Date
     * @return The Date pointing to the last day
     */
    public static Date getWeekLastDay(Date date) {
        DateLocale local = getDateLocale();
        int lastDay = local.getDAY_ORDER()[6];
        return getWeekLastDay(date, lastDay);
    }

    public static Date getWeekLastDay(Date date, int weekLastDay) {
        while (date.getDay() != weekLastDay) {
            date = new Date(date.getYear(), date.getMonth(), date.getDate() + 1);
        }
        return date;
    }

    /**
     * Test if a day is a weekend day.
     *
     * @param day The Date to test
     * @return true if the Date is a weekend day
     */
    public static boolean isInWeekEnd(Date day) {
        int dayIndex = day.getDay();
        return dayIndex == 0 || dayIndex == 6;
    }

    public static boolean isInWeekEndCustom(Date day, String useCustomWeekStart) {
        int dayIndex = day.getDay();
        String overallbirnima = TIMESHEET_WEEK_START.equals(useCustomWeekStart) ? Utils.userSettings.get(TIMESHEET_WEEK_START) : Utils.userSettings.get(OVERALL_DATE_PICKER_WEEK_START);
        if (overallbirnima == null || "".equals(overallbirnima) || "null".equals(overallbirnima)) {
            overallbirnima = "1";
        }
        int weekStart = Integer.valueOf(overallbirnima) - 1;//adjust to zero based
        return dayIndex == (weekStart + 5) % 7 || dayIndex == (weekStart + 6) % 7;
    }

    public static boolean isInOneWeek(Date date1, Date date2) {
        Date weekFirstDay = getWeekFirstDay(date1);
        Date weekLastDay = addDays(weekFirstDay, 6);
        return compareByDate(date2, weekFirstDay) && compareByDate(weekLastDay, date2);
    }

    /**
     * Get the DateTimeFormat corresponding to the locale.
     *
     * @return DateTimeFormat
     */
    public static DateTimeFormat getDateTimeFormat() {
        DateLocale locale = getDateLocale();
        return locale.getDateTimeFormat();
    }

    /**
     * Counts the days between start and end
     *
     * @param start
     * @param end
     * @return
     */
    public static int countDays(Date start, Date end) {
        int days = 1;
        if (areOnTheSameDay(start, end)) {
            return days;
        } else if (compare(start, end)) {
            return -1;
        }

        while (!areOnTheSameDay(start, end)) {
            start = DateUtil.addDays(start, 1);
            days++;
        }

        return days;
    }

    public static int countDays(Date month) {
        return countDays(getMonthFirstDay(month), getMonthLastDate(month));
    }

    public static int getMonthlyDivisor(Date payday_end_rp, Date payday_start_rp) {
        Date temp = payday_start_rp;
        int result = 0;
        while (temp.getYear() <= payday_end_rp.getYear() && temp.getMonth() < payday_end_rp.getMonth()) {
            if (temp.getYear() == payday_start_rp.getYear() && temp.getMonth() == payday_start_rp.getMonth()) {
                Date firstDay = getMonthFirstDay(temp);
                Date lastDay = getMonthLastDate(temp);
                int daysInMonth = getMonthLastDate(temp).getDate() - getMonthFirstDay(temp).getDate() + 1;
                if (daysInMonth >= 30) {
                    if (countDays(temp, lastDay) > 15) {
                        result++;
                    }
                } else {
                    if (countDays(temp, lastDay) > 14) {
                        result++;
                    }
                }
            } else if (temp.getYear() == payday_end_rp.getYear() && temp.getMonth() == payday_end_rp.getMonth()) {
                temp = payday_end_rp;
                Date firstDay = getMonthFirstDay(temp);
                Date lastDay = getMonthLastDate(temp);
                int daysInMonth = getMonthLastDate(temp).getDate() - getMonthFirstDay(temp).getDate() + 1;
                if (daysInMonth >= 30) {
                    if (countDays(firstDay, temp) > 15) {
                        result++;
                    }
                } else {
                    if (countDays(firstDay, temp) > 14) {
                        result++;
                    }
                }
            } else {
                result++;
            }
            temp = addMonths(temp, 1, 0);
        }
        return 0;
    }

    /**
     * Converts given date to local time zone.
     *
     * @param date
     * @return
     */
    public static Date convertToLocalDate(Date date) {
        long convertedTime = date.getTime() + (long) date.getTimezoneOffset() * 60 * 1000;//In milli seconds.
        return new Date(convertedTime);
    }

    /**
     * Resets the date to have no time modifiers (hours, minutes, seconds.)
     *
     * @param date The date to reset
     */
    public static Date resetTime(Date date) {
        if (date != null) {
            long msec = safeInMillis(date);
            msec = (msec / 1000) * 1000;
            date.setTime(msec);
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
        }

        return date;
    }

    public static Date getDayLastTime(Date date) {
        if (date != null) {
            long msec = safeInMillis(date);
            msec = (msec / 1000) * 1000;
            date.setTime(msec);
            date.setHours(23);
            date.setMinutes(59);
            date.setSeconds(59);
        }
        return date;
    }

    private static long safeInMillis(Date date) {
        return date != null ? date.getTime() : 0;
    }

    public static Date getDateWithZeroMinutes(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), 00, 00);
    }

    public static boolean areOnTheSameDay(Date date1, Date date2) {
        return date1.getDate() == date2.getDate() && date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear();
    }

    public static boolean betweenStartDateAndEndDate(Date startDate, Date endDate, Date cursor) {
        return (cursor.getDate() >= startDate.getDate() && cursor.getMonth() >= startDate.getMonth() && cursor.getYear() >= startDate.getYear()) &&
                (cursor.getDate() <= endDate.getDate() && cursor.getMonth() <= endDate.getMonth() && cursor.getYear() <= endDate.getYear());
    }

    public static boolean isMoreThanOneDay(Date start, Date end) {
        start = safeDate(start);
        end = safeDate(end);
        return MILLIS_IN_A_DAY < (end.getTime() - start.getTime());
    }

    public static boolean isOneDay(Date start, Date end) {
        long difference = getTime(end) - getTime(start);
        long diff = MILLIS_IN_A_DAY - 1000;
        return difference == diff || difference == MILLIS_IN_A_DAY;
    }

    public static boolean areOnTheSameYear(Date date1, Date date2) {
        return date1.getYear() == date2.getYear();
    }

    public static boolean compareWithHours(Date start, Date end) {
        start = safeDate(start);
        end = safeDate(end);
        return start.getTime() >= end.getTime();
    }

    public static Date safeDate(Date date) {
        Date safeDate = new Date(date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes()/*, date.getSeconds()*/);
        return new Date(safeDate.getTime());
    }

    public static Date addTime(Date date, int hours, int minutes) {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), date.getHours() + hours, date.getMinutes() + minutes, 0);
    }

    public static Date addTime(Date date, int hours, int minutes, int seconds) {
        return new Date(date.getYear(), date.getMonth(), date.getDate(), date.getHours() + hours, date.getMinutes() + minutes, date.getSeconds() + seconds);
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
        return date == null ? null : new Date(date.getYear(), date.getMonth(), date.getDate() + days, date.getHours(), date.getMinutes(), date.getSeconds());
    }

    public static Date minusDays(Date date, int days) {
        return date == null ? null : new Date(date.getYear(), date.getMonth(), date.getDate() - days, date.getHours(), date.getMinutes(), date.getSeconds());
    }

    public static long getTime(Date date) {
        return date.getTime() - ((long) date.getTimezoneOffset() * SECONDS_PER_MINUTE * MILLIS_PER_SECOND);
    }

    public static int calculateDateInMinutes(Date date) {
        return date.getHours() * MINUTES_PER_HOUR + date.getMinutes();
    }

    /**
     * Returns the number of days between the passed dates.
     *
     * @param end   The upper limit of the date range
     * @param start The lower limit of the date range
     * @return The number of days between <code>endDate</code> and <code>startDate</code> (inclusive)
     */
    public static int differenceInDays(Date end, Date start) {
//        long startMillis = getTime(start);
//        long endMillis = getTime(end);
//        return (int) Math.ceil((double) (endMillis - startMillis) / (double) MILLIS_IN_A_DAY);
        int days = 0;
        if (compareWithHours(start, end)) {
            return -1;
        }

        while (!areOnTheSameDay(start, end)) {
            start = addDays(start, 1);
            days++;
        }

        return days;
    }

    public static Date getDateTime() {
        Date date = new Date();
        String time = DateTimeFormat.getFormat("HH:mm").format(date);
        String[] splittedTime = time.split(":");
        Integer minute = 0;
        Integer timeHour = Integer.valueOf(splittedTime[0]);
        Integer timeMinute = Integer.valueOf(splittedTime[1]);
        if (timeMinute >= 0 && timeMinute < 15) {
            minute = 15;
        } else if (timeMinute >= 15 && timeMinute < 30) {
            minute = 30;
        } else if (timeMinute >= 30 && timeMinute < 45) {
            minute = 45;
        } else if (timeMinute >= 45 && timeMinute <= 59) {
            minute = 0;
            date.setHours(date.getHours() + 1);
        }
        date.setMinutes(minute);
        date.setSeconds(0);
        return date;
    }

    public static int getFirstDateOfWeekend(Integer weekFirstDay) {
        if (weekFirstDay == 0) {
            return 5;
        } else if (weekFirstDay == 1) {
            return 6;
        } else if (weekFirstDay == 6) {
            return 4;
        }
        return 5;
    }

    public static int getLastDayOfWeekend(Integer weekFirstDay) {
        if (weekFirstDay == 0) {
            return 6;
        } else if (weekFirstDay == 1) {
            return 0;
        } else if (weekFirstDay == 6) {
            return 5;
        }
        return 6;
    }

    public static String getTodayInFormat(String format) {
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(format);
        return dateFormat.format(new Date());
    }

    public static int getMinutDiff(Date start, Date end) {
        return calculateDateInMinutes(end) - calculateDateInMinutes(start);
    }

    public static int getDaysCount(Date start, Date end) {
        return getDaysCount(start, end, false);
    }

    public static int getDaysCount(Date start, Date end, boolean ignoreHours) {
        Date sDate = new Date(start.getYear(), start.getMonth(), start.getDate());
        Date eDate = new Date(end.getYear(), end.getMonth(), end.getDate());
        long startOffset = -((long) sDate.getTimezoneOffset() *60*1000);
        long endOffset = - ((long) eDate.getTimezoneOffset() *60*1000);
        Long value = ((eDate.getTime() + endOffset) - (sDate.getTime() + startOffset))/secondsInDay;
        int abs = Math.abs(value.intValue());
        if (!ignoreHours && (end.getTime() - start.getTime()) % secondsInDay >= 0) {
            abs += 1;
        }
        return abs;
    }

    public static Date addYears(Date date, int years) {
        if (getDateInMonth(date.getYear() + years, date.getMonth()) < date.getDate()) {
            return new Date(date.getYear() + years, date.getMonth(), getDateInMonth(date.getYear() + years, date.getMonth()), date.getHours(), date.getMinutes(), date.getSeconds());
        } else {
            return new Date(date.getYear() + years, date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
        }
    }

    /*this method get in year and month numbers*/

    public static int getDateInMonth(int year, int month) {
        switch (month) {
            case 1:
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    return 29; // leap year
                } else {
                    return 28;
                }
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                return 31;
        }
    }

    private static DateLocale getDateLocale() {
        DateLocale locale = new DateLocale() {
            @Override
            public int[] getDAY_ORDER() {
                return new int[]{1, 2, 3, 4, 5, 6, 0};
            }

            @Override
            public DateTimeFormat getDateTimeFormat() {
                return DateTimeFormat.getFormat("dd.MM.yyyy");
            }
        };
        return locale;
    }

    public static class CustomDateTimeFormat extends DateTimeFormat {

        protected CustomDateTimeFormat(String pattern) {
            super(pattern);
        }

        public static DateTimeFormat getFormatByEnglish(String pattern, DateTimeFormatInfo dtfi) {
            return getFormat(pattern, new DateTimeFormatInfoImpl_en());
        }
    }

    public static Integer getYear(Date date) {
        if (date == null) {
            return null;
        }
        return DateUtil.getYear(date.getTime());
    }

    public static Integer getMonth(Date date) {
        if (date == null) {
            return null;
        }
        return DateUtil.getMonth(date.getTime());
    }

    public static Integer getDay(Date date) {
        if (date == null) {
            return null;
        }
        return DateUtil.getDay(date.getTime());
    }

    public static Date getDate(int year, int month, int day) {
        return new Date(((long) getDateTime(year, month, day)));
    }

    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        return isToday(date.getTime());
    }

    public static boolean isYesterday(Date date) {
        if (date == null) {
            return false;
        }
        return isYesterday(date.getTime());
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return isSameDay(date1.getTime(), date2.getTime());
    }

    public static int[] getHoursAndMinutes(Date date) {
        if (date == null) {
            return new int[0];
        }
        return DateUtil.getHoursAndMinutes(date.getTime());
    }

    private static native int getYear(double milliseconds) /*-{
        var date = new Date(milliseconds);
        return date.getFullYear();
    }-*/;

    private static native int getMonth(double milliseconds) /*-{
        var date = new Date(milliseconds);
        return date.getMonth();
    }-*/;

    private static native int getDay(double milliseconds) /*-{
        var date = new Date(milliseconds);
        return date.getDate();
    }-*/;

    private static native double getDateTime(int year, int month, int day) /*-{
        return new Date(year, month, day).getTime();
    }-*/;

    private static native double getFirstDayOfMonth(double timeInMilliseconds) /*-{
        var date = new Date(timeInMilliseconds);
        return new Date(date.getFullYear(), date.getMonth(), 1).getTime();
    }-*/;

    private static native boolean isToday(double milliseconds) /*-{
        var todayDate = new Date();
        var date = new Date(milliseconds);
        return (date.setHours(0,0,0,0) === todayDate.setHours(0,0,0,0));
    }-*/;

    private static native boolean isYesterday(double milliseconds) /*-{
        var todayDate = new Date();
        var date = new Date(milliseconds);
        todayDate.setDate(todayDate.getDate() - 1)
        return (date.setHours(0,0,0,0) === todayDate.setHours(0,0,0,0));
    }-*/;

    private static native boolean isSameDay(double milliseconds1, double milliseconds2) /*-{
        var date1 = new Date(milliseconds1);
        var date2 = new Date(milliseconds2);
        return (date1.setHours(0,0,0,0) === date2.setHours(0,0,0,0));
    }-*/;

    private static native int[] getHoursAndMinutes(double milliseconds) /*-{
        var date = new Date(milliseconds);
        return [date.getHours(), date.getMinutes()];
    }-*/;
}
