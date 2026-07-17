package com.edatasite.workforce.gwt.core.client.rpc.websocket;

import com.google.gwt.core.client.JavaScriptObject;

final public class WebSocketContactObject extends JavaScriptObject {
    protected WebSocketContactObject() {

    }
//    private CrmAccountTO company;

    public native String getPhone() /*-{
        return this.phone;
    }-*/;
    public native String getName() /*-{
        return this.name;
    }-*/;
    public native String getFirst_name() /*-{
        return this.first_name;
    }-*/;

    public native String getContactType() /*-{
        return this.contact_type;
    }-*/;
    public native int getItem_id() /*-{
        if(this.item_id) {
            return this.item_id;
        } else return 0;
    }-*/;
    public native String getAvatar_image() /*-{
        return this.avatar_image;
    }-*/;
    public native String getCompanyName() /*-{
        if(this.company!=null) {
            this.company.name;
        }
        return '';
    }-*/;

}
