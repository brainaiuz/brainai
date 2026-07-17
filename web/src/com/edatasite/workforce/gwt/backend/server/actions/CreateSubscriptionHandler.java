package com.edatasite.workforce.gwt.backend.server.actions;

import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;


public class CreateSubscriptionHandler extends WfmCommandHandler {

    private BackendServiceLocal backendService;

    public BackendServiceLocal getBackendService() {
        return backendService;
    }

    public void setBackendService(BackendServiceLocal backendService) {
        this.backendService = backendService;
    }


    public void execute(Object command) throws Throwable {
        CreateSubscriptionCommand c = (CreateSubscriptionCommand) command;
        backendService.createSubscription(c);
    }

}
