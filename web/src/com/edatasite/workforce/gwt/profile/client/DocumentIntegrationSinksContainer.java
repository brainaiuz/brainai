package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.DocumentIntegrationView;

import java.util.LinkedList;

public class DocumentIntegrationSinksContainer extends SinksContainer {
    public DocumentIntegrationSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews() {
        addView(new DocumentIntegrationView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
