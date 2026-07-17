package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BotActivationServiceAsync {
    void updateActivationKeyForUser(AsyncCallback<String> activation);

    void getActivationKey(AsyncCallback<String> activation);
}
