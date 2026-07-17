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

import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.agenda.AgendaView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview.DayView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.monthview.MonthView;

public class Calendar extends CalendarWidget {

    private CalendarViews view;

    /**
     * The component to manage the presentation of appointments as a list.
     */
    private AgendaView agendaView = null;

    /**
     * The component to manage the presentation of appointments in a single day
     * layout.
     */
    private DayView dayView = null;

    /**
     * The component to manage the presentation of appointments in a month.
     */
    private MonthView monthView = null;

    /**
     * width of calendar grid
     */
    private String width = null;

    /**
     * Constructs a <code>Calendar</code> with the DayView currently
     * displayed.
     */
    public Calendar() {
        this(CalendarViews.DAY);
    }

    /**
     * Constructs a <code>Calendar</code> with the given
     * CalendarView displayed by default.
     */
    public Calendar(CalendarViews view) {
        setView(view);
    }

    /**
     * Constructs a <code>Calendar</code> with the given
     * CalendarView displayed by default.
     */
    public Calendar(CalendarViews view, boolean isPublic) {
        this.isPublic = isPublic;
        setView(view);
    }

    /**
     * Constructs a <code>Calendar</code> with the given
     * CalendarView displayed by default.
     */
    public Calendar(CalendarViews view, boolean isPublic, boolean isBookable) {
        this.isPublic = isPublic;
        this.isBookable = isBookable;
        setView(view);
    }

    /**
     * Constructs a <code>Calendar</code> with the given
     * CalendarView displayed by default.
     */
    public Calendar(CalendarViews view, boolean isPublic, boolean isBookable, String width) {
        this.isPublic = isPublic;
        this.isBookable = isBookable;
        this.width = width;
        setView(view);
    }

    /**
     * Constructs a <code>Calendar</code> with the a user-defined
     * CalendarView displayed by default.
     */
    public Calendar(CalendarView view) {
        setView(view);
    }

    /**
     * Constructs a <code>Calendar</code> with the a user-defined
     * CalendarView displayed by default.
     */
    public Calendar(CalendarView view, boolean isPublic) {
        this.isPublic = isPublic;
        setView(view);
    }

    /**
     * Constructs a <code>Calendar</code> with the a user-defined
     * CalendarView displayed by default.
     */
    public Calendar(CalendarView view, boolean isPublic, boolean isBookable) {
        this.isPublic = isPublic;
        this.isBookable = isBookable;
        setView(view);
    }


    public CalendarViews getView() {
        return view;
    }

    /**
     * Sets the CalendarView that should be used by the Calendar to display
     * the list of appointments.
     *
     * @param view
     */
    public void setView(CalendarViews view) {
        setView(view, getDays());
    }

    /**
     * Sets the current view of this calendar.
     *
     * @param view The ID of a view used to visualize the appointments managed
     *             by the calendar
     * @param days The number of days to display in the view, which can be
     *             ignored by some views.
     */
    public void setView(CalendarViews view, int days) {
        this.view = view;

        switch (view) {
            case DAY: {
                if (dayView == null) {
                    dayView = new DayView();
                }
                dayView.setDisplayedDays(days);
                dayView.setPublic(isPublic);
                dayView.setBookable(isBookable);
                setView(dayView);
                break;
            }
            case AGENDA: {
                agendaView = new AgendaView();
                agendaView.setPublic(isPublic);
                agendaView.setBookable(isBookable);
                agendaView.setWidth(width);
                setView(agendaView);
                break;
            }
            case MONTH: {
                if (monthView == null) {
                    monthView = new MonthView();
                    monthView.setPublic(isPublic);
                    monthView.setBookable(isBookable);
                }
                setView(monthView);
                break;
            }
        }
    }
}