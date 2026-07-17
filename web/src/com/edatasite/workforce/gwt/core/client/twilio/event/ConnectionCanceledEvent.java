package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.edatasite.workforce.gwt.core.client.twilio.Connection;
import com.google.gwt.event.shared.GwtEvent;

public class ConnectionCanceledEvent extends GwtEvent<ConnectionCanceledHandler> {
    private final Connection connection;

    public ConnectionCanceledEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    private static Type<ConnectionCanceledHandler> TYPE;

    public static Type<ConnectionCanceledHandler> getType() {
        if (TYPE == null)
            TYPE = new Type<ConnectionCanceledHandler>();
        return TYPE;
    }

    public @Override
    Type<ConnectionCanceledHandler> getAssociatedType() {
        return TYPE;
    }

    public @Override
    void dispatch(ConnectionCanceledHandler handler) {
        handler.onConnectionCanceled(this);
    }
}