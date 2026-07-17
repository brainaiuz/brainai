/*
 * This file is part of gwt-cal
 * Copyright (C) 2009  Scottsdale Software LLC
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

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

/**
 * Abstract base class defining the operations to render a calendar and
 * user-input dispatching methods. <p/> <p></p>Subclasses will provide the
 * details of rendering the calendar to visualize by day (Day View), monthly
 * (month view), agenda (list view) and the logic implementing the user-input
 * event processing.
 */
public abstract class CalendarView {

    /**
     * Calendar widget bound to the view.
     *
     * @see CalendarWidget
     */
    protected CalendarWidget calendarWidget = null;

    /**
     * Number of days the calendar should display at a given time, 3 by
     * default.
     */
    private int displayedDays = 3;

    /**
     * Height of the view, it determines the height and sets value to CalendarWidget during settings view.
     */
    private String height;

    private String width = (Window.getClientWidth() - 195) + "px";

    private boolean wideScreen = false;
    /**
     * Public access to appoinments (View/Book)
     */
    private boolean isPublic = false;

    /**
     * is bookable
     */
    private boolean isBookable = true;

    /**
     * Attaches this view to the provided {@link CalendarWidget}.
     *
     * @param calendarWidget The interactive widget containing the calendar
     */
    public void attach(CalendarWidget calendarWidget) {
        this.calendarWidget = calendarWidget;
    }

    /**
     * Detaches this view from the currently associated {@link CalendarWidget}.
     * CalendarView, is that correct??
     */
    public void detatch() {
        calendarWidget = null;
    }

    /**
     * Returns the CSS style name of this calendar view.
     *
     * @return The CSS style that should be used when rendering this calendar view
     */
    public abstract String getStyleName();

    public void doSizing() {

    }

    public abstract void doLayout();

    /**
     * Returns the configured number of days the calendar should display at a
     * given time.
     *
     * @return The number of days this calendar view should display at a given time
     */
    public int getDisplayedDays() {
        return displayedDays;
    }

    /**
     * Sets the configured number of days the calendar should display at a given time.
     *
     * @param displayedDays The number of days this calendar view should display at a given time
     */
    public void setDisplayedDays(int displayedDays) {
        this.displayedDays = displayedDays;
    }

    /* on clicks */

    public abstract void onDoubleClick(Element element, Event event);

    public abstract void onSingleClick(Element element, Event event);

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DELETE}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DELETE}
     * keystrokes.
     */
    public void onDeleteKeyPressed() {

    }

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_UP}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_UP} keystrokes.
     */
    public void onUpArrowKeyPressed() {

    }

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DOWN}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DOWN}
     * keystrokes.
     */
    public void onDownArrowKeyPressed() {

    }

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT}
     * keystrokes.
     */
    public void onLeftArrowKeyPressed() {

    }

    /**
     * Processes user {@link com.google.gwt.event.dom.client.KeyCodes.KEY_RIGHT}
     * keystrokes. The <code>CalendarView</code> implementation is empty so that
     * subclasses are not forced to implement it if no specific logic is needed
     * for {@link com.google.gwt.event.dom.client.KeyCodes.KEY_RIGHT}
     * keystrokes.
     */
    public void onRightArrowKeyPressed() {

    }

    /* appointment */

    public abstract void onAppointmentSelected(Appointment appt);

    public final void selectAppointment(Appointment appt) {
        calendarWidget.setSelectedAppointment(appt, true);
    }

    public final void selectNextAppointment() {
        calendarWidget.selectNextAppointment();
    }

    public final void selectPreviousAppointment() {
        calendarWidget.selectPreviousAppointment();
    }

    public final void updateAppointment(Appointment toAppt) {
        calendarWidget.fireUpdateEvent(toAppt);
    }

    public final void deleteAppointment(Appointment appt) {
        calendarWidget.fireDeleteEvent(appt);
    }

    public final void openAppointment(Appointment appt) {
        calendarWidget.fireOpenEvent(appt);
    }

    public void scrollToHour(int hour) {

    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
        calendarWidget.setHeight(height);
    }

    public String getWidthCalendar() {
        return width;
    }

    public void setWidthCalendar(String width) {
        this.width = width;
        calendarWidget.setWidth(width);
    }

    public boolean isWideScreen() {
        return wideScreen;
    }

    public void setWideScreen(boolean wideScreen) {
        this.wideScreen = wideScreen;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isBookable() {
        return isBookable;
    }

    public void setBookable(boolean bookable) {
        isBookable = bookable;
    }
}
