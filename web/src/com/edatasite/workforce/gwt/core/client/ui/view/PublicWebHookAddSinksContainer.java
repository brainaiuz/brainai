package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class PublicWebHookAddSinksContainer extends SinksContainer {
    public PublicWebHookAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (id == null && params != null && params.length > 1 && params[1] != null && !params[1].isEmpty()) {
            try {
                id = Integer.parseInt(params[1]);
            } catch (NumberFormatException e) {
                e.fillInStackTrace();
            }
        }
        addView(new AddWorkflowWebHook(id));
    }
}
