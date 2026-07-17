package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface BotActivationService extends RemoteService {
    String updateActivationKeyForUser();

    String getActivationKey();

    class App {
        public static BotActivationServiceAsync get() {
            ServiceDefTarget target = GWT.create(BotActivationService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/botActivationService");
            return (BotActivationServiceAsync) target;
        }
    }
}
