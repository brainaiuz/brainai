package com.edatasite.workforce.gwt.core.server.db.eventdispatcher;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.Inducer;

import java.util.List;

/**
 * User: Abdulaziz
 * Date: Dec 14, 2009
 * Time: 5:35:24 PM
 */
public interface BusinessEventDispatcherManager {
    <P extends Inducer> EdsBusinessEvent registerEvent(P sourceID, Integer entityID, String entityType, String eventType, String processorName, Boolean sendNotification);

    <P extends Inducer, F extends EdsObject> EdsBusinessEvent registerEvent(P sourceID, F additionalSourceID, Integer entityID, String entityType, String eventType, String processorName, Boolean sendNotification);

    EdsBusinessEvent registerCustomEvent(Integer sourceID, Integer additionalSourceID, Integer entityID, String entityType, String eventType, String processorName, Boolean sendNotification);

    void removeEvent(EdsBusinessEvent event);

    void removeEventNative(EdsBusinessEvent event);

    void updateEvent(EdsBusinessEvent event);

    List<EdsBusinessEvent> getUnprocessedEvents();

    EdsBusinessEvent getEvent(Integer eventID);

    List<EdsBusinessEvent> getUserSyntGoogleContactEvents(String eventType, Integer userID);

    <P extends Inducer> EdsBusinessEvent getUnprocessedEvent(P sourceID, Integer objectID, String eventType, String stringValue);

    List<EdsBusinessEvent> getEvents(List<Integer> Ids);
}
