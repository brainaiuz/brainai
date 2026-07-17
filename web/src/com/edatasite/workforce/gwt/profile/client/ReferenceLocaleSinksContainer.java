package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.ReferenceLocaleView;

import java.util.LinkedList;

public class ReferenceLocaleSinksContainer extends SinksContainer {
    public ReferenceLocaleSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new ReferenceLocaleView(id));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
