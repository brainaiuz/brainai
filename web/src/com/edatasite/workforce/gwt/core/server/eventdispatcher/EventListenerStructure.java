package com.edatasite.workforce.gwt.core.server.eventdispatcher;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;

/**
 * User: Anvarbek
 * Date: Dec 28, 2009
 * Time: 7:44:11 PM
 */
public class EventListenerStructure {

    public EventListenerStructure(String eventListenerName, String eventType, Integer entityID) {
        this.eventListenerName = eventListenerName;
        this.eventType = eventType;
        this.entityID = entityID;
    }

    public EventListenerStructure(String eventListenerName, String eventType, Integer entityID, Integer sourceID) {
        this.eventListenerName = eventListenerName;
        this.eventType = eventType;
        this.entityID = entityID;
        this.sourceID = sourceID;
    }

    public String eventListenerName;
    public Integer entityID;
    public Integer sourceID;
    public String eventType;
    public EdsBusinessEvent businessEvent;
}
