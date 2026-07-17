package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.GwtEvent;

public class PresenceEvent extends GwtEvent<PresenceHandler> {
    private final String from;
    private final boolean available;

    public PresenceEvent(String from, boolean available) {
        this.from = from;
        this.available = available;
    }

    public String getFrom() {
        return from;
    }

    public boolean isAvailable() {
        return available;
    }

    private static Type<PresenceHandler> TYPE;

    public static Type<PresenceHandler> getType() {
        if (TYPE == null)
            TYPE = new Type<PresenceHandler>();
        return TYPE;
    }

    public @Override
    Type<PresenceHandler> getAssociatedType() {
        return TYPE;
    }

    public @Override
    void dispatch(PresenceHandler handler) {
        handler.onPresenceChanged(this);
    }
}