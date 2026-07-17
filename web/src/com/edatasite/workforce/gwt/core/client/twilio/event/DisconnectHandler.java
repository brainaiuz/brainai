package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.EventHandler;

public interface DisconnectHandler extends EventHandler {
    void onDisconnect(DisconnectEvent evt);
}