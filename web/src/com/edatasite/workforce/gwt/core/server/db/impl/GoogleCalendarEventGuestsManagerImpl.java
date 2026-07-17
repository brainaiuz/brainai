package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsGoogleCalendarEventGuests;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarEventGuestsManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Jan 29, 2011
 * Time: 12:28:27 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("calendarEventGuestsManager")
public class GoogleCalendarEventGuestsManagerImpl extends BaseManager<EdsGoogleCalendarEventGuests> implements GoogleCalendarEventGuestsManager {

    public GoogleCalendarEventGuestsManagerImpl() {
        super(EdsGoogleCalendarEventGuests.class);
    }

    public List<EdsGoogleCalendarEventGuests> getEventGuests(Integer eventId) {
        if (eventId != null && eventId > 0) {
            return find("select guest from EdsGoogleCalendarEventGuests guest where guest.event.objectID = ?", eventId);
        }
        return null;
    }

    public void deleteCalendarEventGuest(Integer objectID, String guestEmail) {
        if (objectID != null && objectID > 0) {
            update("delete from EdsGoogleCalendarEventGuests guest where guest.event.objectID=? and guest.email=?", objectID, guestEmail);
        }
    }

    public void deleteCalendarEventGuests(List<Integer> eventIDs) {
        if (eventIDs != null && !eventIDs.isEmpty()) {
            update("delete from EdsGoogleCalendarEventGuests guest where guest.event.objectID in (" + ServerUtils.getAsCommoDelimited(eventIDs, "0", ",") + ")");
        }
    }

    @Transactional
    public void updateEventGuestStatus(Integer eventId, String email, String status) {
        update("update EdsGoogleCalendarEventGuests guest set status=? where guest.event.objectID=? and guest.email=?", status, eventId, email);
    }

    public List<EdsGoogleCalendarEventGuests> getEventGuestsByEmail(Integer eventID, String guestsEmail) {
        return find("select guest from EdsGoogleCalendarEventGuests guest where guest.event.objectID=? and guest.email=?", eventID, guestsEmail);
    }

    @Override
    public String getEventGuestsEmailsByEvent(Integer eventID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT array_to_string(array_agg(g.email),',') FROM ").append(getCompanyId()).append(".googlecalendareventguests g ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".event e ON e.id = g.eventId ");
        sql.append("WHERE e.id = ").append(eventID);
        return (String) findNativeSingle(sql.toString());
    }

    @Override
    public String getEventGuestsByEvent(Integer eventID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT array_to_string(array_agg(CASE WHEN c is null THEN g.email ELSE c.firstname||' '||c.lastname END),',') FROM ").append(getCompanyId()).append(".googlecalendareventguests g ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".event e ON e.id = g.eventId ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmContact c ON c.primaryEmail = g.email ");
        sql.append("WHERE c.deleted <> true AND e.id = ").append(eventID);
        return (String) findNativeSingle(sql.toString());
    }
}
