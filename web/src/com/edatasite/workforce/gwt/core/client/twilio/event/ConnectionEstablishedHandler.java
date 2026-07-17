package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.EventHandler;

public interface ConnectionEstablishedHandler extends EventHandler {
    void onConnectionEstablished(ConnectionEstablishedEvent evt);
}