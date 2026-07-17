package com.edatasite.workforce.gwt.core.client.twilio;

import com.google.gwt.core.client.JavaScriptObject;

public final class SoundsConfiguration extends JavaScriptObject {
    protected SoundsConfiguration() {
    }

    public native void setIncomingEnabled(boolean enabled)
        /*-{
            $wnd.Twilio.Device.sounds.incoming(enabled);
        }-*/;

    public native void setOutgoingEnabled(boolean enabled)
        /*-{
            $wnd.Twilio.Device.sounds.outgoing(enabled);
        }-*/;

    public native void setDisconnectEnabled(boolean enabled)
        /*-{
            $wnd.Twilio.Device.sounds.disconnect(enabled);
        }-*/;

    public native boolean getIncomingEnabled()
        /*-{
            return $wnd.Twilio.Device.sounds.incoming();
        }-*/;

    public native boolean getOutgoingEnabled()
        /*-{
            return $wnd.Twilio.Device.sounds.outgoing();
        }-*/;

    public native boolean getDisconnectEnabled()
        /*-{
            return $wnd.Twilio.Device.sounds.disconnect();
        }-*/;
}