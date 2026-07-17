package com.edatasite.workforce.gwt.backend.client;


import com.edatasite.workforce.gwt.backend.client.ui.view.AddEditDynamicLoginView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class AddEditDynamicLoginSinksContainer extends SinksContainer {
    public AddEditDynamicLoginSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        String hostname = params.length > 1 ? params[1] : null;
        addView(new AddEditDynamicLoginView(hostname));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
