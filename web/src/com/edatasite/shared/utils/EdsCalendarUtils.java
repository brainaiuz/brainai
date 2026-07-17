package com.edatasite.shared.utils;

import com.edatasite.workforce.gwt.payroll.client.rpc.DateRange;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Aug 11, 2010
 * Time: 4:13:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class EdsCalendarUtils {

    public static Calendar getEndOfMonth(Calendar calendar) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        return new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
    }

    public static Calendar getEndOfMonth(Date date) {
        final Calendar endOfMonth = Calendar.getInstance();
        if (date != null) {
            endOfMonth.setTime(date);
        }
        return getEndOfMonth(endOfMonth);
    }

    public static Calendar getStartOfMonth(Date date) {
        final Calendar startOfMonth = Calendar.getInstance();
        if (date != null) {
            startOfMonth.setTime(date);
        }
        return new GregorianCalendar(startOfMonth.get(Calendar.YEAR), startOfMonth.get(Calendar.MONTH), 1, 0, 0, 0);
    }

    public static Calendar getStartDateOfWeek(Calendar calendar, int numberOfWeeks) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        final int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear - (dayOfYear % (7 * numberOfWeeks)) + 1);
        /*set hours, minutes & seconds to 0 (zero)*/
        calendar.roll(Calendar.HOUR, -calendar.get(Calendar.HOUR));
        calendar.roll(Calendar.MINUTE, -calendar.get(Calendar.MINUTE));
        calendar.roll(Calendar.SECOND, -calendar.get(Calendar.SECOND));
        return calendar;
    }

    public static Calendar getStartDateOfWeek(Date date, int numberOfWeeks) {
        final Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        return getStartDateOfWeek(calendar, numberOfWeeks);
    }

    public static Calendar getEndDateOfWeek(Calendar cal, int numberOfWeeks) {
        final Calendar calendar = getStartDateOfWeek(cal, numberOfWeeks);
        calendar.add(Calendar.DAY_OF_YEAR, 7 * numberOfWeeks - 1);
        calendar.roll(Calendar.HOUR, 23);
        calendar.roll(Calendar.MINUTE, 59);
        calendar.roll(Calendar.SECOND, 59);
        return calendar;
    }

    public static Calendar getEndDateOfWeek(Date date, int numberOfWeeks) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getEndDateOfWeek(calendar, numberOfWeeks);
    }

    public static int countDays(Calendar start, Calendar end) {
        final int startYear = start.get(Calendar.YEAR);
        final int endYear = end.get(Calendar.YEAR);
        if (startYear == endYear) {
            return end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR) + 1;
        } else if (endYear > startYear) {
            int days = 0;
            for (int year = startYear; year <= endYear; year++) {
                if (year == startYear) {
                    days += countDays(start, getCalendarYearEnd(year));
                } else if (year == endYear) {
                    days += countDays(getCalendarYearStart(year), end);
                } else {
                    days += getCalendarYearStart(year).getActualMaximum(Calendar.DAY_OF_YEAR);
                }
            }
            return days;
        }
        return -1;
    }

    public static int countDays(Date startDate, Date endDate) {
        final Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        final Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        return countDays(start, end);
    }

    public static int countMonths(Date start, Date end, int incompleteMonthRoundingMode) {
        if (start == null || end == null) {
            return -1;
        }
        final Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        final Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        return countMonths(startCal, endCal, incompleteMonthRoundingMode);
    }

    public static int countMonths(Calendar start, Calendar end, int incompleteMonthRoundingMode) {
        final int startYear = start.get(Calendar.YEAR);
        final int endYear = end.get(Calendar.YEAR);
        if (start.after(end)) {
            return -1;
        } else {
            if (startYear == endYear) {
                int months = 0;
                final int startMonth = start.get(Calendar.MONTH);
                final int endMonth = end.get(Calendar.MONTH);
                /*
                number of whole calendar months in relevant period, i.e. between start & end months
                */
                months += (endMonth - startMonth - 1);
                /*
                If there are not a whole number of calendar months in the relevant period round to a whole number as follows:
                30 and 31 day months: 15 days or less round down, 16 days or more round up
                28 and 29 day months: 14 days or less round down, 15 days or more round up.
                */
                if (incompleteMonthRoundingMode == ROUND_HALF_DOWN) {
                    if (start.get(Calendar.DAY_OF_MONTH) <= start.getActualMaximum(Calendar.DAY_OF_MONTH) / 2) {
                        months++;
                    }
                    if (end.get(Calendar.DAY_OF_MONTH) * 2 > end.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        months++;
                    }
                } else if (incompleteMonthRoundingMode == ROUND_UP) {
                    /*add two months for start & end months, without checking whether they are complete or incomplete months*/
                    months += 2;
                }
                return months;
            } else { //endYear > startYear
                int months = 0;
                for (int year = startYear; year <= endYear; year++) {
                    if (year == startYear) {
                        months += countMonths(start, getCalendarYearEnd(year), incompleteMonthRoundingMode);
                    } else if (year == endYear) {
                        months += countMonths(getCalendarYearStart(year), end, incompleteMonthRoundingMode);
                    } else {
                        months += 12;
                    }
                }
                return months;
            }
        }
    }

    public static int[] countWeeks(Calendar start, Calendar end) {
        if (start.after(end)) {
            return new int[]{0, 0};
        } else {
            final int days = countDays(start, end);
            return new int[]{days / 7, days % 7};
        }
    }

    public static Calendar getCalendarYearStart(int year) {
        return new GregorianCalendar(year, Calendar.JANUARY, 1, 0, 0, 0);
    }

    public static Calendar getCalendarYearEnd(int year) {
        return new GregorianCalendar(year, Calendar.DECEMBER, 31, 23, 59, 59);
    }

    public static Calendar getCalendar(Date date, int addDays) {
        return getCalendar(date.getTime(), addDays);
    }

    public static Calendar getCalendar(long milliSeconds, int addDays) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        if (addDays != 0) {
            calendar.add(Calendar.DAY_OF_YEAR, addDays);
        }
        return calendar;
    }

    public static Date max(Date date1, Date date2) {
        if (date1 == null) {
            return date2;
        } else if (date2 == null) {
            return date1;
        } else {
            return date1.compareTo(date2) > 0 ? date1 : date2;
        }
    }

    public static Date min(Date date1, Date date2) {
        if (date1 == null) {
            return date2;
        } else if (date2 == null) {
            return date1;
        } else {
            return date1.compareTo(date2) < 0 ? date1 : date2;
        }
    }

    public static DateRange getWeeklyDateRange(Date startDate) {
        return new DateRange(startDate, getCalendar(startDate, 6).getTime());
    }

    public static DateRange getNextDateRange(DateRange dateRange) {
        return new DateRange(getCalendar(dateRange.getEndDate(), 1).getTime(), getCalendar(dateRange.getEndDate(), countDays(dateRange.getStartDate(), dateRange.getEndDate())).getTime());
    }

    public static Date getLastWorkingDay(Date date) {
        return switch (EdsCalendarUtils.getCalendar(date, 0).get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY -> EdsCalendarUtils.getCalendar(date, -2).getTime();
            case Calendar.SATURDAY -> EdsCalendarUtils.getCalendar(date, -1).getTime();
            default -> date;
        };
    }

    public static final int ROUND_UP = BigDecimal.ROUND_UP;
    public static final int ROUND_DOWN = BigDecimal.ROUND_DOWN;
    public static final int ROUND_HALF_DOWN = BigDecimal.ROUND_HALF_DOWN;
}
