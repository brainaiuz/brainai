package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsGoogleCalendarEventGuests;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarEventGuestsManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/26/11
 * Time: 8:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class CalendarEventGuestCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsGoogleCalendarEventGuests> TYPE = new WfmType<>(EventTypes.calendarEventGuestCustomEventListener);
    public static String CALENDAR_EVENT_GUEST_STATUS_CHANGE = "CALENDAR_EVENT_GUEST_STATUS_CHANGE";

    @Autowired
    private GoogleCalendarEventGuestsManager eventGuestsManager;
    @Autowired
    private MyUpdateManager myUpdateManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (CALENDAR_EVENT_GUEST_STATUS_CHANGE.equalsIgnoreCase(event.getEventType())) {
            EdsGoogleCalendarEventGuests eventGuest = eventGuestsManager.get(event.getEntityID());
            try {
                if (eventGuest != null && eventGuest.getEvent() != null && eventGuest.getEvent().getOwner() != null) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerCalendarEventGuestsStatusChangeUpdate(eventGuest, eventGuest.getEvent().getOwner(), event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
    }
}
