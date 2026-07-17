package com.edatasite.workforce.gwt.core.client.ui.notification;

import com.edatasite.workforce.gwt.core.client.rpc.EventServiceRemoteService;
import de.novanic.eventservice.client.event.service.EventServiceAsync;
import de.novanic.eventservice.client.event.service.creator.EventServiceCreator;

/**
 * Created by dilsh0d on 25.07.15.
 */
public class KpiEventServiceCreator implements EventServiceCreator {
    @Override
    public EventServiceAsync createEventService() {
        return EventServiceRemoteService.App.get();
    }
}
