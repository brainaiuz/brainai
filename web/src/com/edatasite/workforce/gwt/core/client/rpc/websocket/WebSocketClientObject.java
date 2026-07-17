package com.edatasite.workforce.gwt.core.client.rpc.websocket;

import com.google.gwt.core.client.JavaScriptObject;

final public class WebSocketClientObject extends JavaScriptObject {
    protected WebSocketClientObject() {

    }
    public native int getEventType() /*-{
        return this.eventType;
    }-*/;
    public native String getData() /*-{
        return this.data;
    }-*/;
}
