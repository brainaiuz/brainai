package com.edatasite.workforce.gwt.client.client;

import com.edatasite.workforce.gwt.client.client.ui.view.ClientDynamicView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ClientDynamicAddSinksContainer extends SinksContainer {

    public ClientDynamicAddSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new ClientDynamicView(null, params));

    }

}
