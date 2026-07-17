package com.edatasite.workforce.gwt.core.client.twilio.event;

import com.edatasite.workforce.gwt.core.client.twilio.Connection;
import com.google.gwt.event.shared.GwtEvent;

public class DisconnectEvent extends GwtEvent<DisconnectHandler> {
    private final Connection connection;

    public DisconnectEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    private static Type<DisconnectHandler> TYPE;

    public static Type<DisconnectHandler> getType() {
        if (TYPE == null)
            TYPE = new Type<DisconnectHandler>();
        return TYPE;
    }

    public @Override
    Type<DisconnectHandler> getAssociatedType() {
        return TYPE;
    }

    public @Override
    void dispatch(DisconnectHandler handler) {
        handler.onDisconnect(this);
    }
}