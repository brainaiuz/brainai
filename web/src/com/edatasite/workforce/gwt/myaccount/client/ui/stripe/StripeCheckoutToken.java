package com.edatasite.workforce.gwt.myaccount.client.ui.stripe;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Created by Anvar Akramov on 9/26/17.
 */
public class StripeCheckoutToken extends JavaScriptObject {

    protected StripeCheckoutToken() {
    }

    public final native String getId() /*-{
        return this.id;
    }-*/;

    public final native int getCreated() /*-{
        return this.created;
    }-*/;

    public final native boolean getLiveMode() /*-{
        return this.livemode;
    }-*/;

    public final native String getType() /*-{
        return this.type;
    }-*/;

    public final native String getObject() /*-{
        return this.object;
    }-*/;

    public final native boolean getUsed() /*-{
        return this.used;
    }-*/;

    public final native Card getCard() /*-{
        return this.card;
    }-*/;
}
