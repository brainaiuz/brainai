package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.EventHandler;

public interface ErrorHandler extends EventHandler {
    void onError(ErrorEvent evt);
}