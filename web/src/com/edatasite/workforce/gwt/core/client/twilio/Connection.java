package com.edatasite.workforce.gwt.core.client.twilio;

import com.edatasite.workforce.gwt.core.client.twilio.event.*;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public final class Connection implements HasConnectionHandlers {
    public enum Status {PENDING, CONNECTING, OPEN, CLOSED}

    private ConnectionJSO jso;
    private HandlerManager handlerManager;

    public Connection(JavaScriptObject jso) {
        this.jso = jso.cast();
    }

    public void accept() {
        jso.accept();
    }

//    public void volume() {
//        jso.volume();
//    }

    public void reject() {
        jso.reject();
    }

    public void disconnect() {
        jso.disconnect();
    }

    public boolean isMuted() {
        return jso.isMuted();
    }

    public void mute() {
        jso.mute();
    }

    public void unmute() {
        jso.unmute();
    }

    public void sendDigits(String digits) {
        jso.sendDigits(digits);
    }

    public Status getStatus() {
        return Status.valueOf(jso.getStatus().toUpperCase());
    }

    public CallParameters getCallParameters() {
        return jso.getCallParameters();
    }

    private HandlerManager ensureHandlers() {
        if (handlerManager != null)
            return handlerManager;
        registerEvents(jso);
        return handlerManager = new HandlerManager(this);
    }

    private native void registerEvents(ConnectionJSO jso)
        /*-{
            var self = this;
            jso.disconnect(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Connection::fireDisconnectEvent()();
            });
            jso.accept(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Connection::fireAcceptEvent()();
            });
            jso.volume(function (in_, out_) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Connection::fireVolumeEvent(DD)(in_,out_);
            });
            jso.error(function (err) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Connection::fireErrorEvent(Ljava/lang/String;ILcom/google/gwt/core/client/JavaScriptObject;)
                (err.message.message, err.code, err.info);
            });
        }-*/;

    private void fireDisconnectEvent() {
        fireEvent(new DisconnectEvent(this));
    }

    private void fireAcceptEvent() {
        fireEvent(new AcceptEvent(this));
    }

    private void fireVolumeEvent(double in, double out) {
        fireEvent(new VolumeEvent(this, in, out));
    }

    private void fireErrorEvent(String message, int code, JavaScriptObject info) {
        fireEvent(new ErrorEvent(message, code, info, this));
    }

    public HandlerRegistration addDisconnectHandler(DisconnectHandler handler) {
        return addHandler(DisconnectEvent.getType(), handler);
    }

    public HandlerRegistration addAcceptHandler(AcceptHandler handler) {
        return addHandler(AcceptEvent.getType(), handler);
    }

    public HandlerRegistration addErrorHandler(ErrorHandler handler) {
        return addHandler(ErrorEvent.getType(), handler);
    }

    public HandlerRegistration addVolumeHandler(VolumeHandler handler) {
        return addHandler(VolumeEvent.getType(), handler);
    }

    public void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) handlerManager.fireEvent(event);
    }

    private <H extends EventHandler> HandlerRegistration
    addHandler(GwtEvent.Type<H> type, H handler) {
        return ensureHandlers().addHandler(type, handler);
    }

    private static final class ConnectionJSO extends JavaScriptObject {
        protected ConnectionJSO() {
        }

        private native void accept() /*-{
            this.accept();
        }-*/;

        private native void reject() /*-{
            this.reject();
        }-*/;

        private native void disconnect() /*-{
            this.disconnect();
        }-*/;

        private native void mute() /*-{
            this.mute(true);
        }-*/;

        private native void unmute() /*-{
            this.mute(false);
        }-*/;

        private native boolean isMuted() /*-{
            return this.isMuted();
        }-*/;

        private native void sendDigits(String digits) /*-{
            this.sendDigits(digits);
        }-*/;

        private native void volume(double in_, double out_) /*-{
            this.volume(in_, out_);
        }-*/;

        private native String getStatus() /*-{
            return this.status();
        }-*/;

        private native CallParameters getCallParameters()
            /*-{
                if (this.parameters == undefined)
                    return null;
                return this.parameters;
            }-*/;
    }
}