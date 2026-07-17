package com.edatasite.workforce.gwt.core.client.rpc.websocket;

import com.google.gwt.core.client.JavaScriptObject;

final public class WebSocketMessageObject extends JavaScriptObject {
    protected WebSocketMessageObject() {

    }
    public native String getObjectId() /*-{
        return this.objectId;
    }-*/;

    public native String getContactId() /*-{
        return this.contactId;
    }-*/;

    public native String getMessage() /*-{
        return this.message;
    }-*/;

    public native String getContactFullName() /*-{
        return this.contactFullName;
    }-*/;

    public native String getCreatedDate() /*-{
        return this.createdDate;
    }-*/;

    public native boolean isCompanyMessage() /*-{
        return this.companyMessage;
    }-*/;

    public native String getDate() /*-{
        return this.date;
    }-*/;

    public native String getPhoneNumber() /*-{
        return this.phoneNumber;
    }-*/;


}
