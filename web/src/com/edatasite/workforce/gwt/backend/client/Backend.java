package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public class Backend extends WorkforceEntryPoint {

    public interface BackendResourse extends ClientBundle {
        @CssResource.NotStrict
        @Source("com/edatasite/workforce/gwt/backend/public/backend.css")
        CssResource backend();
    }

    public static BackendResourse resourse = GWT.create(BackendResourse.class);

    public void initSinksContainerFactory() {
        containerFactory = new BackendSinksContainerFactory(this);
        resourse.backend().ensureInjected();
    }

}
