package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.edatasite.workforce.gwt.core.client.twilio.Connection;
import com.google.gwt.event.shared.GwtEvent;

public class ConnectionEstablishedEvent extends GwtEvent<ConnectionEstablishedHandler> {
    private final Connection connection;

    public ConnectionEstablishedEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    private static Type<ConnectionEstablishedHandler> TYPE;

    public static Type<ConnectionEstablishedHandler> getType() {
        if (TYPE == null)
            TYPE = new Type<ConnectionEstablishedHandler>();
        return TYPE;
    }

    public @Override
    Type<ConnectionEstablishedHandler> getAssociatedType() {
        return TYPE;
    }

    public @Override
    void dispatch(ConnectionEstablishedHandler handler) {
        handler.onConnectionEstablished(this);
    }
}