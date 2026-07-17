package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsGoogleCalendarEventGuests;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Jan 29, 2011
 * Time: 12:27:04 PM
 * To change this template use File | Settings | File Templates.
 */

public interface GoogleCalendarEventGuestsManager extends Manager<EdsGoogleCalendarEventGuests> {
    List<EdsGoogleCalendarEventGuests> getEventGuests(Integer eventId);

    void deleteCalendarEventGuest(Integer objectID, String guestEmail);

    void deleteCalendarEventGuests(List<Integer> eventIDs);

    void updateEventGuestStatus(Integer eventId, String email, String status);

    List<EdsGoogleCalendarEventGuests> getEventGuestsByEmail(Integer eventID, String guestsEmail);

    String getEventGuestsEmailsByEvent(Integer eventID);

    String getEventGuestsByEvent(Integer eventID);
}
