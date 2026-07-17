package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.EventHandler;

public interface IncomingConnectionHandler extends EventHandler {
    void onIncomingConnection(IncomingConnectionEvent evt);
}