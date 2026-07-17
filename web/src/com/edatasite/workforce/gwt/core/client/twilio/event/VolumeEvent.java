package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.edatasite.workforce.gwt.core.client.twilio.Connection;
import com.google.gwt.event.shared.GwtEvent;

public class VolumeEvent extends GwtEvent<VolumeHandler> {
    private final Connection connection;
    private final double in;
    private final double out;


    public VolumeEvent(Connection connection, double in, double out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    public Connection getConnection() {
        return connection;
    }

    public double getIn() {
        return in;
    }

    public double getOut() {
        return out;
    }

    private static Type<VolumeHandler> TYPE;

    public static Type<VolumeHandler> getType() {
        if (TYPE == null)
            TYPE = new Type<VolumeHandler>();
        return TYPE;
    }

    public @Override
    Type<VolumeHandler> getAssociatedType() {
        return TYPE;
    }

    public @Override
    void dispatch(VolumeHandler handler) {
        handler.onVolume(this);
    }
}