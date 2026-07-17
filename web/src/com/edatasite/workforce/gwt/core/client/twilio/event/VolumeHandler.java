package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.EventHandler;

public interface VolumeHandler extends EventHandler {
    void onVolume(VolumeEvent evt);
}