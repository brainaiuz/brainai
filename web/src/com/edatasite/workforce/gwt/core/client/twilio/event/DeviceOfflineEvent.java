package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.google.gwt.event.shared.GwtEvent;

public class DeviceOfflineEvent extends GwtEvent<DeviceOfflineHandler> {
    private static Type<DeviceOfflineHandler> TYPE;

    public static Type<DeviceOfflineHandler> getType() {
        if (TYPE == null)
            TYPE = new Type<DeviceOfflineHandler>();
        return TYPE;
    }

    public @Override
    Type<DeviceOfflineHandler> getAssociatedType() {
        return TYPE;
    }

    public @Override
    void dispatch(DeviceOfflineHandler handler) {
        handler.onDeviceOffline(this);
    }
}