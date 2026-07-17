package com.edatasite.workforce.gwt.messagecenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailComposeView;

import java.util.LinkedList;

public class EmailComposeSinksContainer extends SinksContainer {

    public EmailComposeSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new EmailComposeView(params));
    }
}
