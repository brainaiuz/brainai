package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.GwtEvent;

public class DeviceReadyEvent extends GwtEvent<DeviceReadyHandler> {
    private static Type<DeviceReadyHandler> TYPE;

    public static Type<DeviceReadyHandler> getType() {
        if (TYPE == null)
            TYPE = new Type<DeviceReadyHandler>();
        return TYPE;
    }

    public @Override
    Type<DeviceReadyHandler> getAssociatedType() {
        return TYPE;
    }

    public @Override
    void dispatch(DeviceReadyHandler handler) {
        handler.onDeviceReady(this);
    }
}