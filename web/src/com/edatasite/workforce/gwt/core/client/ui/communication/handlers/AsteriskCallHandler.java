package com.edatasite.workforce.gwt.core.client.ui.communication.handlers;

import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.communication.CallState;
import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;
import com.edatasite.workforce.gwt.core.client.ui.communication.widgets.CallModal;
import com.google.gwt.core.client.GWT;

import java.util.List;


public class AsteriskCallHandler implements CallCommand {

    private String phoneNumber;
    private ContactDetailsItem contactDetailsItem;
    private CallModal callModal;
    private CallModal.Command incommingCallCommand;

    public AsteriskCallHandler(List<AsteriskSettings> asteriskSettings, String displayName) {

        for (AsteriskSettings asterisks : asteriskSettings) {
            initAsterisk(asterisks.getAsteriskUsername(), asterisks.getAsteriskPassword(), displayName, asterisks.getAsteriskHost(), asterisks.getAsteriskPort());
        }

    }

    public void setIncommingCallCommand(CallModal.Command incommingCallCommand) {
        this.incommingCallCommand = incommingCallCommand;
    }

    private native void initAsterisk(String username, String password, String displayName, String host, String wsport) /*-{

        var self = this;
        $wnd.setupAsterisk(username, password, displayName, host, wsport,
            $entry(
                function (username) {
                    self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler::onConnected(Ljava/lang/String;)(username);
                }
            ),
            $entry(
                function (username) {
                    self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler::onDisconnected(Ljava/lang/String;)(username);
                }
            ),
            $entry(
                function (username) {
                    self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler::onRegistered(Ljava/lang/String;)(username);
                }
            ),
            $entry(
                function (username) {
                    self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler::onUnregistered(Ljava/lang/String;)(username);
                }
            ),
            $entry(
                function (username, incomingNumber) {
                    self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler::onCallReceived(Ljava/lang/String;Ljava/lang/String;)(username, incomingNumber);
                }
            ),
            $entry(
                function (username) {
                    self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler::onCallAnswered(Ljava/lang/String;)(username);
                }
            ),
            $entry(
                function (username) {
                    self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler::onCallHangup(Ljava/lang/String;)(username);
                }
            ),
            $entry(
                function (username) {
                    self.@com.edatasite.workforce.gwt.core.client.ui.communication.handlers.AsteriskCallHandler::onCallHold(Ljava/lang/String;)(username);
                }
            )
        );
    }-*/;

    //When connects to Asterisk Server after connect it should register
    public void onConnected(String username) {
        GWT.log("Asterisk " + username + " Connected !!!");
    }

    public void onDisconnected(String username) {
        GWT.log("Asterisk Disconnected !!!");
    }

    public void onRegistered(String username) {
        GWT.log("Asterisk " + username + " Registered !!!");
    }

    public void onUnregistered(String username) {
        GWT.log("Asterisk " + username + " Unregistered !!!");
    }

    public void setIncomingCallerDetails(ContactDetailsItem incomingCallerDetails) {
        if (callModal != null) {
            callModal.setIncomingCallerDetails(incomingCallerDetails);
        }
    }

    public void onCallReceived(String username, String incomingNumber) {

        GWT.log("Incoming Call into " + username + " From : " + incomingNumber);

        if (callModal != null) {
            callModal.close();
        }
        phoneNumber = incomingNumber;
        callModal = new CallModal(username, null, incomingNumber, this);
        callModal.changeState(username, CallState.INCOMING, false);

        ContactDetailsItem incomingCallerDetails = new ContactDetailsItem();
        incomingCallerDetails.setMobile(incomingNumber);
        callModal.setIncomingCallerDetails(incomingCallerDetails);

        if (incommingCallCommand != null) {
            incommingCallCommand.execute(incomingNumber);
        }


        /*answerAsterisk();
        hangupAsterisk();*/
    }

    public void onCallAnswered(String username) {
        callModal.changeState(username, CallState.CONVERSATION, false);
    }

    private void onCallHangup(String username) {
//        callModal.createCallLog(Appointment.CALL_LOG, null);
        callModal.changeState(username, CallState.FINISHED, false);
    }

    private void onCallHold(String username) {

    }


/////////////////////////////

    @Override
    public void
    call(String username, String phoneNumber, ContactDetailsItem contactDetailsItem) {
        String phone = phoneNumber.trim();
        phone = phone.replace(" ", "");
        if (phone != null && phone.length() > 9) {
            phone = phone.substring(phone.length() - 9);
        }
        phone = phone.replace("+", "");
        this.phoneNumber = phone;
        this.contactDetailsItem = contactDetailsItem;
        callModal = new CallModal(username, contactDetailsItem, phoneNumber, this);
        makeCall(username);
    }

    @Override
    public void forwardCall(String username, String phoneNumber, ContactDetailsItem contactDetailsItem) {
        String phone = phoneNumber.trim();
        phone = phone.replace(" ", "");
        if (phone != null && phone.length() > 9) {
            phone = phone.substring(phone.length() - 9);
        }
        phone = phone.replace("+", "");
        this.phoneNumber = phone;
//        this.contactDetailsItem = contactDetailsItem;
//        callModal = new CallModal(username, contactDetailsItem, phoneNumber, this);
        forwardCallAsterisk(this.phoneNumber, username);
    }

    @Override
    public boolean mute(String username) {
        if (isAsteriskConnected(username)) {
            if (!isAsteriskMuted(username)) {
                asteriskMute(username);
                return true;
            } else {
                asteriskUnmute(username);
                return false;
            }
        }
        return false;
    }

    @Override
    public void disconnect(String username) {
        GWT.log("Asteric call hangup Disconnecting !!!!");
        hangupAsterisk(username);
        GWT.log("Hangup !!!!");
        //Below will be called onHangup
//        callModal.changeState(CallState.FINISHED);
    }

    @Override
    public void reject(String username) {
        GWT.log("Asteric call rejecting !!!!");
        rejectAsterisk(username);
    }

    @Override
    public void accept(String username) {
        answerAsterisk(username);
    }

    @Override
    public void sendDigits(String username, String digits) {
        asteriskSendDTMF(username, digits);
    }

    private void makeCall(String username) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            GWT.log("Calling via SIP to " + phoneNumber);
            callAsterisk(phoneNumber, username);
        }
    }

    public static native void callAsterisk(String phone, String username) /*-{
        //Call
        $wnd.callAsterisk(phone, username);
    }-*/;

    public static native void forwardCallAsterisk(String phone, String username) /*-{
        //Call
        $wnd.forwardAsteriskCall(phone, username);
    }-*/;

    public static native void answerAsterisk(String username) /*-{
        //Answer
        $wnd.answerAsteriskCall(username);
    }-*/;

    public static native void hangupAsterisk(String username) /*-{
        //Answer
        $wnd.hangupAsteriskCall(username);
    }-*/;

    public static native void rejectAsterisk(String username) /*-{
        //Answer
        $wnd.rejectAsteriskCall(username);
    }-*/;

    public static native boolean isAsteriskConnected(String username) /*-{
        //IsConnected
        return $wnd.isAsteriskConnected(username);
    }-*/;

    public static native boolean isAsteriskMuted(String username) /*-{
        //Ismuted
        return $wnd.isAsteriskMuted(username);
    }-*/;

    public static native void asteriskMute(String username) /*-{
        //Mute
        return $wnd.asteriskMute(username);
    }-*/;

    public static native void asteriskUnmute(String username) /*-{
        //Unmute
        return $wnd.asteriskUnmute(username);
    }-*/;

    public static native boolean isAsteriskHeld(String username) /*-{
        //IsHeld
        return $wnd.isAsteriskHeld(username);
    }-*/;

    public static native void asteriskHold(String username) /*-{
        //Hold
        return $wnd.asteriskHold(username);
    }-*/;

    public static native void asteriskUnhold(String username) /*-{
        //Unhold
        return $wnd.asteriskUnhold(username);
    }-*/;

    public static native void asteriskSendDTMF(String username, String tone) /*-{
        //Unhold
        return $wnd.asteriskSendDTMF(username, tone);
    }-*/;

    public static native void asteriskSendMessage(String username, String destination, String message) /*-{
        //Unhold
        return $wnd.asteriskSendMessage(username, destination, message);
    }-*/;
}
