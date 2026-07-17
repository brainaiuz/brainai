package com.edatasite.workforce.gwt.core.client.ui.resourceUtil;

import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;


/**
 * @autor:Dilshod
 */
public class MonthDay {

    private static DateTimeFormat format = DateTimeFormat.getFormat("MMM yyyy");
    private static DateTimeFormat format1 = DateTimeFormat.getFormat("MMMM");
    private static DateTimeFormat format2 = DateTimeFormat.getFormat("MMMM, yyyy");
    private static DateTimeFormat format3 = DateTimeFormat.getFormat("MM/yyyy");

    private int step = 0;
    private Date date;
    private Date currentDate = new Date();
    private int year;
    private int month;
    private int day;
    private int currentDay;
    private int maxMonthDay;

    private String monthName;
    private String monthNameWithYear;
    private String monthWithYear;
    private String topMonthName;

    public MonthDay(Date date) {
        this.date = date;
        dateGenerate();
    }


    public MonthDay(int step) {
        this.step = step;
        dateGenerate(step);
    }

    public void dateGenerate(int step) {
        setStep(step);
        date = getClickDate();
        year = date.getYear();
        month = date.getMonth();
        day = date.getDate();
        maxMonthDay = getDaysInMonth(year, month);

        monthName = format1.format(date);
        monthNameWithYear = format2.format(date);
        monthWithYear = format3.format(date);
        topMonthName = format.format(date);
        if (step == 0) {
            currentDay = day;
        }
    }

    private void dateGenerate() {
        year = date.getYear();
        month = date.getMonth();
        day = date.getDate();
        maxMonthDay = getDaysInMonth(year, month);

        monthName = format1.format(date);
        monthNameWithYear = format2.format(date);
        monthWithYear = format3.format(date);
        topMonthName = format.format(date);
        if (step == 0) {
            currentDay = day;
        }
    }

    /* method clicked prev or next return clicked date */

    private Date getClickDate() {
        Date clickDate = new Date();
        if (step == -1 && year != 1900) {
            if (month == 0) {
                year--;
                month = 12;
            }
            month--;
            clickDate = new Date(year, month, day);
        } else if (step == 1 && year != 2100) {
            if (month == 11) {
                year++;
                month = -1;
            }
            month++;
            clickDate = new Date(year, month, day);
        }
        return clickDate;
    }

    /*this method get in year and month  month numbers*/

    private int getDaysInMonth(int year, int month) {
        switch (month) {
            case 1:
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    return 29; // leap year
                } else {
                    return 28;
                }
            case 3:
                return 30;
            case 5:
                return 30;
            case 8:
                return 30;
            case 10:
                return 30;
            default:
                return 31;
        }
    }

    public Date getStartDate() {
        return new Date(year, month, 1);
    }

    public Date getEndDate() {
        return new Date(year, month, maxMonthDay, 23, 59, 59);
    }

    public int getCurrentDate() {
        if (format.format(date).equals(format.format(currentDate))) {
            return currentDay;
        }
        return -1;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMaxMonthDay() {
        return maxMonthDay;
    }

    public void setMaxMonthDay(int maxMonthDay) {
        this.maxMonthDay = maxMonthDay;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthWithYear() {
        return monthWithYear;
    }

    public String getMonthNameWithYear() {
        return monthNameWithYear;
    }

    public void setMonthNameWithYear(String monthNameWithYear) {
        this.monthNameWithYear = monthNameWithYear;
    }

    public String getTopMonthName() {
        return topMonthName;
    }

    public void setTopMonthName(String topMonthName) {
        this.topMonthName = topMonthName;
    }

    public int getCurrentDay() {
        return currentDay;
    }
}
