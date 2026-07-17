package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.edatasite.workforce.gwt.core.client.twilio.Connection;
import com.google.gwt.event.shared.GwtEvent;

public class IncomingConnectionEvent extends GwtEvent<IncomingConnectionHandler> {
    private final Connection connection;

    public IncomingConnectionEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    private static Type<IncomingConnectionHandler> TYPE;

    public static Type<IncomingConnectionHandler> getType() {
        if (TYPE == null)
            TYPE = new Type<IncomingConnectionHandler>();
        return TYPE;
    }

    public @Override
    Type<IncomingConnectionHandler> getAssociatedType() {
        return TYPE;
    }

    public @Override
    void dispatch(IncomingConnectionHandler handler) {
        handler.onIncomingConnection(this);
    }
}