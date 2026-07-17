package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import de.novanic.eventservice.client.event.service.EventService;

/**
 * Created by dilsh0d on 24.07.15.
 */
public interface EventServiceRemoteService extends RemoteService, EventService {

    class App {
        public static EventServiceRemoteServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/gwteventservice");
            return (EventServiceRemoteServiceAsync) target;
        }
    }
}
