package com.edatasite.workforce.gwt.core.server.app;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.Domain;

/**
 * Created by dilsh0d on 01.08.15.
 */
public interface ServerSentEventService {

    void addEvent(Domain aDomain, Event anEvent);

}
