package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.edatasite.workforce.gwt.core.client.twilio.Connection;
import com.google.gwt.event.shared.GwtEvent;

public class AcceptEvent extends GwtEvent<AcceptHandler> {
    private final Connection connection;

    public AcceptEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    private static Type<AcceptHandler> TYPE;

    public static Type<AcceptHandler> getType() {
        if (TYPE == null)
            TYPE = new Type<AcceptHandler>();
        return TYPE;
    }

    public @Override
    Type<AcceptHandler> getAssociatedType() {
        return TYPE;
    }

    public @Override
    void dispatch(AcceptHandler handler) {
        handler.onAccept(this);
    }
}