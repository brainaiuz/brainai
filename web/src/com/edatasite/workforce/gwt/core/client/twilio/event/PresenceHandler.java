package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.EventHandler;

public interface PresenceHandler extends EventHandler {
    void onPresenceChanged(PresenceEvent evt);
}