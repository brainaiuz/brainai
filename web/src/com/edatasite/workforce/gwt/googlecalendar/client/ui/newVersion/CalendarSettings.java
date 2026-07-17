/*
 * This file is part of gwt-cal
 * Copyright (C) 2009  Brad Rydzewski
 *
 * gwt-cal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/
 */

package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.i18n.client.DateTimeFormat;

public class CalendarSettings {

    public static CalendarSettings DEFAULT_SETTINGS = new CalendarSettings();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    /**
     * DateTime format used to represent a day.
     */
    public static final DateTimeFormat DEFAULT_DATE_FORMAT = DateTimeFormat.getFormat("EEE, MMM d");

    public static final String[] DAY_LIST_MONDAY = new String[]{wfmStrings.mondayShort(), wfmStrings.tuesdayShort(), wfmStrings.wednesdayShort(), wfmStrings.thursdayShort(), wfmStrings.fridayShort(), wfmStrings.saturdayShort(), wfmStrings.sundayShort()};
    public static final String[] DAY_LIST_SATURDAY = new String[]{wfmStrings.saturdayShort(), wfmStrings.sundayShort(), wfmStrings.mondayShort(), wfmStrings.tuesdayShort(), wfmStrings.wednesdayShort(), wfmStrings.thursdayShort(), wfmStrings.fridayShort()};
    public static final String[] DAY_LIST_SUNDAY = new String[]{wfmStrings.sundayShort(), wfmStrings.mondayShort(), wfmStrings.tuesdayShort(), wfmStrings.wednesdayShort(), wfmStrings.thursdayShort(), wfmStrings.fridayShort(), wfmStrings.saturdayShort()};
    public static final String[] MONTH_LIST = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private int pixelsPerInterval = 17/*30*/; //IE6 cannot be less than 20!!!!!
    private int intervalsPerHour = 4/*2*/;
    private int workingHourStart = 8;
    private int workingHourEnd = 17;
    private int scrollToHour = 8; //default hour that gets scrolled to
    private boolean enableDragDrop = true;
    private boolean offsetHourLabels = false;

    /*
     * Clicks required to fire TimeBlockClickEvent.
     */
    private Click timeBlockClickNumber = Click.Single;

    public int getPixelsPerInterval() {
        return pixelsPerInterval;
    }

    public void setPixelsPerInterval(int px) {
        pixelsPerInterval = px;
    }

    public int getIntervalsPerHour() {
        return intervalsPerHour;
    }

    public void setIntervalsPerHour(int intervals) {
        intervalsPerHour = intervals;
    }

    public int getWorkingHourStart() {
        return workingHourStart;
    }

    public void setWorkingHourStart(int start) {
        workingHourStart = start;
    }

    public int getWorkingHourEnd() {
        return workingHourEnd;
    }

    public void setWorkingHourEnd(int end) {
        workingHourEnd = end;
    }

    public int getScrollToHour() {
        return scrollToHour;
    }

    public void setScrollToHour(int hour) {
        scrollToHour = hour;
    }

    public boolean isEnableDragDrop() {
        return enableDragDrop;
    }

    public void setEnableDragDrop(boolean enableDragDrop) {
        this.enableDragDrop = enableDragDrop;
    }

    public boolean isOffsetHourLabels() {
        return offsetHourLabels;
    }

    public void setOffsetHourLabels(boolean offsetHourLabels) {
        this.offsetHourLabels = offsetHourLabels;
    }

    public Click getTimeBlockClickNumber() {
        return timeBlockClickNumber;
    }

    public void setTimeBlockClickNumber(Click timeBlockClickNumber) {
        this.timeBlockClickNumber = timeBlockClickNumber;
    }

    public enum Click {
        Double, Single
    }
}
