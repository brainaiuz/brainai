package com.edatasite.workforce.gwt.payroll.server.app;

import com.edatasite.shared.utils.EdsCalendarUtils;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: Aug 24, 2009
 * Time: 3:36:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollUtils {

    private static final int PENSION_AGE = 0;
    private static final int COOPS = 1;
    private static final int ANOTHER_JOB = 2;
    private static final int COSR = 3;
    private static final int MARRIED_WIDOW = 4;
    private static final int ANOTHER_JOB2 = 5;
    private static final int MARRIED_WIDOW2 = 6;
    private static final int COMP = 7;
    private static final int ANOTHER_JOB3 = 8;
    private static final int MARRIED_WIDOW3 = 9;

    public static final int TAX_YEAR_START_DAY_OF_MONTH = 6;
    public static final int TOTAL_NUMBER_OF_MONTHS_IN_YEAR = 12;
    public static final int TOTAL_NUMBER_OF_WEEKS_IN_YEAR = 52;

    private static final EmployeeEarning[] employeeEarnings = new EmployeeEarning[10];//one off array space reservation for storing small graph
    public static final int MAX_WEEK = 53;
    public static final int MAX_MONTHS = 12;
    public static final int MAX_DAYS_A_YEAR = 365;
    public static final int FEB_28 = 50;
    public static final int APR_5 = 95;

    public PayrollUtils() {
        EmployeeEarning employeeEarning = new EmployeeEarning();
        employeeEarning.setNoIndex(COOPS);
    }

    static {
        EmployeeEarning employeeEarning = new EmployeeEarning();
        employeeEarning.setNoIndex(COOPS);
        employeeEarning.setYesLetter('C');
        employeeEarnings[PENSION_AGE] = employeeEarning;

        employeeEarning = new EmployeeEarning();
        employeeEarning.setNoIndex(ANOTHER_JOB);
        employeeEarning.setYesIndex(COSR);
        employeeEarnings[COOPS] = employeeEarning;

        employeeEarning = new EmployeeEarning();
        employeeEarning.setYesLetter('J');
        employeeEarning.setNoIndex(MARRIED_WIDOW);
        employeeEarnings[ANOTHER_JOB] = employeeEarning;

        employeeEarning = new EmployeeEarning();
        employeeEarning.setYesLetter('B');
        employeeEarning.setNoLetter('A');
        employeeEarnings[MARRIED_WIDOW] = employeeEarning;

        employeeEarning = new EmployeeEarning();
        employeeEarning.setYesIndex(ANOTHER_JOB2);
        employeeEarning.setNoIndex(COMP);
        employeeEarnings[COSR] = employeeEarning;

        employeeEarning = new EmployeeEarning();
        employeeEarning.setYesLetter('L');
        employeeEarning.setNoIndex(MARRIED_WIDOW2);
        employeeEarnings[ANOTHER_JOB2] = employeeEarning;

        employeeEarning = new EmployeeEarning();
        employeeEarning.setYesLetter('E');
        employeeEarning.setNoLetter('D');
        employeeEarnings[MARRIED_WIDOW2] = employeeEarning;

        employeeEarning = new EmployeeEarning();
        employeeEarning.setYesIndex(ANOTHER_JOB3);
        employeeEarnings[COMP] = employeeEarning;

        employeeEarning = new EmployeeEarning();
        employeeEarning.setYesLetter('S');
        employeeEarning.setNoIndex(MARRIED_WIDOW3);
        employeeEarnings[ANOTHER_JOB3] = employeeEarning;

        employeeEarning = new EmployeeEarning();
        employeeEarning.setYesLetter('G');
        employeeEarning.setNoLetter('F');
        employeeEarnings[MARRIED_WIDOW3] = employeeEarning;
    }


    public static char calculateNITableLetter(Map<Integer, Boolean> valueByIndex) {
        EmployeeEarning earning = employeeEarnings[PENSION_AGE];
        char returningLetter = 0;
        int index = PENSION_AGE;
        while (true) {
            if (valueByIndex.get(index)) {
                if (earning.getYesLetter() != 0) {
                    returningLetter = earning.getYesLetter();
                    break;
                }
                earning = employeeEarnings[index = earning.getYesIndex()];
            } else {
                if (earning.getNoLetter() != 0) {
                    returningLetter = earning.getNoLetter();
                    break;
                }
                earning = employeeEarnings[index = earning.getNoIndex()];
            }
        }
        return returningLetter;
    }

    public static String getTaxYears(Date date) {
        final int taxYearStart = getTaxYearStart(date);
        return taxYearStart + "/" + (taxYearStart + 1);
    }

    public static Integer getTaxYearStart(Date date) {
        final Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        if (cal.get(Calendar.MONTH) < Calendar.APRIL || cal.get(Calendar.MONTH) == Calendar.APRIL && cal.get(Calendar.DAY_OF_MONTH) < TAX_YEAR_START_DAY_OF_MONTH) {
            return cal.get(Calendar.YEAR) - 1;
        } else {
            return cal.get(Calendar.YEAR);
        }
    }

    public static Integer getTaxYearEnd(Date date) {
        return getTaxYearStart(date) + 1;
    }

    public static Calendar getBeginningOfTaxYear(Date date) {
        final Calendar cal = new GregorianCalendar();
        if (date != null) {
            cal.setTime(date);
        }
        cal.set(getTaxYearStart(date), Calendar.APRIL, 6, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar getBeginningOfTaxYearForNonUK(Date date) {
        final Calendar cal = new GregorianCalendar();
        if (date != null) {
            cal.setTime(date);
        }
        cal.set(cal.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar getEndOfTaxYear(Date date) {
        final Calendar cal = new GregorianCalendar();
        if (date != null) {
            cal.setTime(date);
        }
        final int yearDiff = cal.get(Calendar.MONTH) < Calendar.APRIL || cal.get(Calendar.MONTH) == Calendar.APRIL && cal.get(Calendar.DAY_OF_MONTH) <= 5 ? 0 : 1;
        cal.set(cal.get(Calendar.YEAR) + yearDiff, Calendar.APRIL, 5, 23, 59, 59);
        return cal;
    }

    public static Calendar getEndOfTaxYearForNonUK(Date date) {
        final Calendar cal = new GregorianCalendar();
        if (date != null) {
            cal.setTime(date);
        }
        cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER, 31, 23, 59, 59);
        return cal;
    }

    public static Calendar getEndOfCurrentTaxYear() {
        return getEndOfTaxYear(null);
    }

    public static boolean dateIsInCurrentTaxYear(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.after(getBeginningOfTaxYear(date)) && cal.before(getEndOfTaxYear(date));
    }

    public static int getPayPeriodNumber(Date date, Frequency frequency, boolean isUKCompany) {
        final Calendar p11Start = isUKCompany ? getBeginningOfTaxYear(date) : getBeginningOfTaxYearForNonUK(date);
        int weekMonthNum = 1;
        if (frequency.getCycle() == Calendar.WEEK_OF_YEAR) {
            final int days = EdsCalendarUtils.countDays(p11Start.getTime(), date);
            final int weeks = days / 7 + (days % 7 > 0 ? 1 : 0);
            if (frequency.getNumberOfCycles() > 1) {
                return weeks + (frequency.getNumberOfCycles() - weeks % frequency.getNumberOfCycles() < frequency.getNumberOfCycles() ? frequency.getNumberOfCycles() - (weeks % frequency.getNumberOfCycles()) : 0);
            }
            return weeks;
        } else {
            weekMonthNum = 0;
            while (date.after(p11Start.getTime()) || date.equals(p11Start.getTime())) {
                p11Start.set(Calendar.MONTH, p11Start.get(Calendar.MONTH) + 1);
                weekMonthNum++;
            }
        }
        return weekMonthNum;
    }

    public static Frequency getFrequency(Object frequency) {
        if (frequency != null) {
            if (frequency instanceof String) {
                for (Frequency f : Frequency.values()) {
                    if (f.getName().equalsIgnoreCase((String) frequency)) {
                        return f;
                    }
                }
                if ("W".equalsIgnoreCase((String) frequency)) {
                    return Frequency.WEEKLY;
                } else if ("M".equalsIgnoreCase((String) frequency)) {

                    return Frequency.MONTHLY;
                }
            } else if (frequency instanceof Integer) {
                for (Frequency f : Frequency.values()) {
                    if (f.getId() == (Integer) frequency){
                        return f;
                    }
                }
            }
        }
        return Frequency.WEEKLY;
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static Calendar getEndDateOfPeriod(Date date, Frequency frequency) {
        return switch (frequency) {
            case WEEKLY, WEEKLYx2, WEEKLYx4 -> EdsCalendarUtils.getEndDateOfWeek(date, frequency.getNumberOfCycles());
            default -> EdsCalendarUtils.getEndOfMonth(date);
        };
    }

    public static Calendar getStartDateOfPeriod(Date date, Frequency frequency) {
        return switch (frequency) {
            case WEEKLY, WEEKLYx2, WEEKLYx4 -> EdsCalendarUtils.getStartDateOfWeek(date, frequency.getNumberOfCycles());
            default -> EdsCalendarUtils.getStartOfMonth(date);
        };
    }

    public static ArrayList<Integer> getWorkingDays(EdsTimeSlot timeSlot) {
        final ArrayList<Integer> days = new ArrayList<>();
        for (EdsTimeSlotItem timeSlotItem : timeSlot.getItems()) {
            if (timeSlotItem.getStartTime() > 0) {
                days.add(timeSlotItem.getDay() + 1);//in EdsTimeSlotItem SUNDAY starts from 0 while Calendar.SUNDAY = 1
            }
        }
        return days;
    }

    public static BigDecimal getNumberOfHours(Date startDate, Date endDate, Map<Date, BigDecimal> workingHours, Set<Date> excludeDates) {
        assert startDate != null : "startDate cannot be null";
        assert endDate != null : "endDate cannot be null";
        BigDecimal calculatedHours = BigDecimal.ZERO;
        if (workingHours.isEmpty()) {
            return calculatedHours;
        }
        boolean excludedDatesPersists = excludeDates != null && !excludeDates.isEmpty();
        for (Map.Entry<Date, BigDecimal> item : workingHours.entrySet()) {
            Date date = item.getKey();
            BigDecimal hour = item.getValue();
            if (excludedDatesPersists && excludeDates.contains(date)) {
                continue;
            }
            long diff1 = ChronoUnit.DAYS.between(startDate.toInstant(), Instant.ofEpochMilli(date.getTime()));
            long diff2 = ChronoUnit.DAYS.between(Instant.ofEpochMilli(date.getTime()), endDate.toInstant());

            if (diff1 >= 0 && diff2 >= 0) {
                calculatedHours = calculatedHours.add(hour);
            }
        }
        return calculatedHours;
    }
}
