package com.edatasite.workforce.gwt.core.client.rpc.notification;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * Created by dilsh0d on 06.08.15.
 */
public class PushNotificationRemoteListener implements RemoteEventListener {
    @Override
    public void apply(Event anEvent) {
        if (anEvent instanceof NotificationReloadEvent) {
            reloadNotificationToolBar((NotificationReloadEvent)anEvent);
        }
    }

    public void reloadNotificationToolBar(NotificationReloadEvent reloadEvent) {

    }
}
