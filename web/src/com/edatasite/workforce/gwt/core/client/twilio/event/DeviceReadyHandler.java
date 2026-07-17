package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.EventHandler;

public interface DeviceReadyHandler extends EventHandler {
    void onDeviceReady(DeviceReadyEvent evt);
}