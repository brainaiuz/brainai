package com.edatasite.workforce.gwt.assessment.client;

import com.edatasite.workforce.gwt.assessment.client.ui.view.AddTemplateView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class TemplateSinksContainer extends SinksContainer {

    public TemplateSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    public TemplateSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {

        AddTemplateView assessViewPanel;
        if (params.length > 1) {
            if ("copyTemplate".equals(params[0])) {
                addView(new AddTemplateView(Integer.valueOf(params[1]), true));
            } else {
                assessViewPanel = new AddTemplateView(id);
                addView(assessViewPanel);
            }
        } else {
            assessViewPanel = new AddTemplateView(id);
            addView(assessViewPanel);
        }
    }
}