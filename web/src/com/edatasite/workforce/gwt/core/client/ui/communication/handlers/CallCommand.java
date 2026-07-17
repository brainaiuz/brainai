package com.edatasite.workforce.gwt.core.client.ui.communication.handlers;

import com.edatasite.workforce.gwt.core.client.ui.communication.ContactDetailsItem;

public interface CallCommand {

    void call(String username, String phoneNumber, ContactDetailsItem contactDetailsItem);
    void forwardCall(String username, String phoneNumber, ContactDetailsItem contactDetailsItem);

    void onCallReceived(String username, String incomingNumber);

    boolean mute(String username);

    void disconnect(String username);

    void reject(String username);

    void accept(String username);

    void sendDigits(String username, String digits);

}
