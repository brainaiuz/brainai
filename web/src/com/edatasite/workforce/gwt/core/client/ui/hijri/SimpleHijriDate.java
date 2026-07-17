package com.edatasite.workforce.gwt.core.client.ui.hijri;

import com.edatasite.workforce.gwt.core.client.localization.HijriStrings;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 09.09.14
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class SimpleHijriDate {

    private static HijriStrings hijriStrings = HijriStrings.App.get();

/* This class uses Java convention so that month number is starting from 0 instead of 1.
	 * But the original calculator uses 1-based month number.
	 */

    /** Before Hijri. */
    public static final int ERA_BH = -1;
    /** After Hijri (anno-hegirae). */
    public static final int ERA_AH = 0;

    private int year;
    private int month;
    private int dayOfMonth;
    private int dayOfWeek;
    private int monthLength;
    private int era;


    SimpleHijriDate(HijriCalculator.sDate d) {
        this.year = d.year;
        this.month = d.month - 1;
        this.dayOfMonth = d.day;
        this.dayOfWeek = d.weekday;
        this.monthLength = d.to_numdays;
        if (HijriCalculator.HIJRI_BH.equals(d.units))
            era = ERA_BH;
        else
            era = ERA_AH;
    }

    /**
     * Start from 1.
     * @return
     */
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Start from 0 for Ahad (Sunday).
     * @return
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Start from 0 for Muharram.
     * @return
     */
    public int getMonth() {
        return month;
    }

    /**
     * The year.
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     * Number of days in this month.
     * @return
     */
    public int getMonthLength() {
        return monthLength;
    }

    /**
     * Could be {@link #ERA_AH} or {@link #ERA_BH}.
     * @return
     */
    public int getEra() {
        return era;
    }

    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getDayOfWeekName() {
        return hijriStrings.getString("hijriDay" + dayOfWeek);
    }

    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getDayOfWeekShortName() {
        return hijriStrings.getString("hijriDayShort" + dayOfWeek);
    }

    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getMonthName() {
        return hijriStrings.getString("hijriMonth" + month);
    }

    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getMonthShortName() {
        return hijriStrings.getString("hijriMonthShort" + month);
    }

    /**
     * Short cut for {@link HijriNames}.
     * @param locale
     * @return
     */
    public String getEraName() {
        if (era == SimpleHijriDate.ERA_BH){
			return hijriStrings.getString("eraBH");
        }else if (era == SimpleHijriDate.ERA_AH){
			return hijriStrings.getString("eraAH");
        }
        return "";
    }

    public String getDatePickerCurrentMonthFormat(){
        return getMonthName()+" "+getYear();
    }

    public String getDatePickerCurrentDateShortFormat(){
        return " (" + getDayOfMonth() + " " + getMonthShortName() + ", " + getYear() + ")";
    }

    public String getDatePickerCurrentDateAttendanceShortFormat(){
        return " (" + getMonthShortName() + " " + getDayOfMonth() + "," + getYear() + ")";
    }

    public String getStandartDateFormat(){
        return " (" + getDayOfMonth() + "-" + (getMonth() + 1) + "-" + getYear() + ")";
    }

    public String getCalendarHeaderDayDateFormat(){
        return " (" +getMonthName() + " " + getDayOfMonth() + ", " + getYear() + ")";
    }

    public String getCalendarHeaderMonthDateFormat(){
        return " (" + getMonthName() + ", " +  getYear() + ")";
    }

}
