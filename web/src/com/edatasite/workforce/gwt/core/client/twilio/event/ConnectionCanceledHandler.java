package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.EventHandler;

public interface ConnectionCanceledHandler extends EventHandler {
    void onConnectionCanceled(ConnectionCanceledEvent evt);
}