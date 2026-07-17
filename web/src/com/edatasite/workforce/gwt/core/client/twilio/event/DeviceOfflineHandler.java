package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.EventHandler;

public interface DeviceOfflineHandler extends EventHandler {
    void onDeviceOffline(DeviceOfflineEvent evt);
}