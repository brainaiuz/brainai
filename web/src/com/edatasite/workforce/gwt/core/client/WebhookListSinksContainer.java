package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WorkflowWebHookListView;

import java.util.LinkedList;

public class WebhookListSinksContainer extends SinksContainer {
    public WebhookListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params != null && params.length > 1) {
            addView(new WorkflowWebHookListView(params[0], params[1]));
        } else {
            addView(new WorkflowWebHookListView(params[0]));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
    }
}
