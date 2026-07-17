package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsGoogleCalendarReminder;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarReminderManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 21, 2010
 * Time: 5:28:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("reminderManager")
public class GoogleCalendarReminderManagerImpl extends BaseManager<EdsGoogleCalendarReminder> implements GoogleCalendarReminderManager {

    public GoogleCalendarReminderManagerImpl() {
        super(EdsGoogleCalendarReminder.class);
    }

    @Override
    public ArrayList<CalendarEventReminder> getReminders(Integer eventID) {
        return (ArrayList<CalendarEventReminder>) find("select new com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder(rem.reminderType, rem.minutes) from EdsGoogleCalendarReminder rem where rem.event.objectID = ?", eventID);
    }

    public void deleteEventReminders(Integer eventId) {
        update("delete from EdsGoogleCalendarReminder rem where rem.event.objectID = ?", eventId);
    }

    public void deleteEventRemindersByEventIDs(List<Integer> eventIDs) {
		if (eventIDs != null && !eventIDs.isEmpty()) {
			update("delete from EdsGoogleCalendarReminder rem where rem.event.objectID in (" + ServerUtils.getAsCommoDelimited(eventIDs, "0", ",") + ")");
		}
	}

    public Integer getEventReminderRecurrenceID(Integer eventID) {
        return (Integer) findSingle("select r.recurrenceID from EdsGoogleCalendarReminder r where r.event.objectID=?", eventID);
    }

    public EdsGoogleCalendarReminder getNextEventReminder(Integer eventID) {
        return (EdsGoogleCalendarReminder) findSingle("select r from EdsGoogleCalendarReminder r where r.event.objectID > ? order by r.event.objectID asc", eventID);
    }
}
