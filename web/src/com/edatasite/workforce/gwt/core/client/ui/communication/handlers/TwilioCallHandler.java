package com.edatasite.workforce.gwt.core.client.ui.communication.handlers;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioService;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.twilio.Connection;
import com.edatasite.workforce.gwt.core.client.twilio.TwilioError;
import com.edatasite.workforce.gwt.core.client.ui.communication.CallState;
import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.widgets.CallModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class TwilioCallHandler implements CallCommand {

    private String phoneNumber;
    private CallModal callModal;
    private Connection connection;
    private static boolean ready;
    private SelectItem twilioSetting;

    public TwilioCallHandler(SelectItem twilioSetting) {
        this.twilioSetting = twilioSetting;
        registerEvents();
    }

    @Override
    public void call(String username, String phoneNumber, ContactDetailsItem contactDetailsItem) {
        this.phoneNumber = phoneNumber;
        callModal = new CallModal(username, contactDetailsItem, phoneNumber, this);
        if (!ready) {
            TwilioService.App.get().getTwilioToken(twilioSetting.getId(), new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(String token) {
                    setup(token);
                }
            });
        } else {
            makeCall();
        }
    }

    @Override
    public void forwardCall(String username, String phoneNumber, ContactDetailsItem contactDetailsItem) {

    }

    @Override
    public void onCallReceived(String username, String incomingNumber) {

    }

    @Override
    public boolean mute(String username) {
        if (connection != null) {
            if (!connection.isMuted()) {
                connection.mute();
                return true;
            } else {
                connection.unmute();
                return false;
            }
        }
        return false;
    }

    @Override
    public void disconnect(String username) {
        disconnectAll();
    }

    @Override
    public void reject(String username) {

    }

    @Override
    public void accept(String username) {

    }

    @Override
    public void sendDigits(String username, String digits) {
        connection.sendDigits(digits);
    }

    private void makeCall() {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("To", new JSONString(phoneNumber));
            jsonObject.put("record", new JSONString("record-on-answer"));
            connection = new Connection(connect(jsonObject.getJavaScriptObject()));
        }
    }

    private void onReady() {
        ready = true;
        makeCall();
    }

    private void onConnect(JavaScriptObject javaScriptObject) {
        connection = new Connection(javaScriptObject);
        connection.addErrorHandler(evt -> GWT.log(evt.getMessage()));
        if (callModal != null) {
            callModal.changeState(null, CallState.CONVERSATION, true);
        }
    }

    private void onDisconnect(JavaScriptObject javaScriptObject) {
        connection = new Connection(javaScriptObject);
        connection.addErrorHandler(evt -> GWT.log(evt.getMessage()));
        if (callModal != null) {
            callModal.createCallLog(Appointment.CALL_LOG, connection.getCallParameters() != null ? connection.getCallParameters().getCallSid() : null);
            callModal.changeState(null, CallState.FINISHED, true);
        }
    }

    private void onCancel() {
        connection.disconnect();
        if (callModal != null) {
            callModal.createCallLog(Appointment.CALL_LOG, connection.getCallParameters() != null ? connection.getCallParameters().getCallSid() : null);
            callModal.changeState(null, CallState.FINISHED, true);
        }
    }

    private void onError(String message, int code, JavaScriptObject info, JavaScriptObject connection) {
        if (code == 31205) {
            ready = false;
            Info.warn("Please retry.");
        } else if (TwilioError.getInstance().hasError(code)) {
            Info.warn(TwilioError.getInstance().getError(code));
        }
        Utils.log("Twilio Error + " + code);
    }

    private void onOffline() {
        Info.warn("Your device is offline");
    }

    private native JavaScriptObject connect(JavaScriptObject params)
        /*-{
            return $wnd.Twilio.Device.connect(params);
        }-*/;

    public native void disconnectAll()
        /*-{
            $wnd.Twilio.Device.disconnectAll();
        }-*/;

    private native void registerEvents()
        /*-{
            var self = this;
            $wnd.Twilio.Device.ready(function (device) {
                self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.TwilioCallHandler::onReady()();
            });
            $wnd.Twilio.Device.offline(function (device) {
                self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.TwilioCallHandler::onOffline()();
            });
            $wnd.Twilio.Device.incoming(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.twilio.Device::fireIncomingConnectionEvent(Lcom/google/gwt/core/client/JavaScriptObject;)(connection);
            });
            $wnd.Twilio.Device.cancel(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.TwilioCallHandler::onCancel()();
            });
            $wnd.Twilio.Device.connect(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.TwilioCallHandler::onConnect(Lcom/google/gwt/core/client/JavaScriptObject;)(connection);
            });
            $wnd.Twilio.Device.disconnect(function (connection) {
                self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.TwilioCallHandler::onDisconnect(Lcom/google/gwt/core/client/JavaScriptObject;)(connection);
            });
            $wnd.Twilio.Device.error(function (err) {
                self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.TwilioCallHandler::onError(Ljava/lang/String;ILcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)
                (err.message.message, err.code, err.info, err.connection);
            });
        }-*/;

    public native void setup(String token)
        /*-{
            $wnd.Twilio.Device.setup(token);
        }-*/;
}
