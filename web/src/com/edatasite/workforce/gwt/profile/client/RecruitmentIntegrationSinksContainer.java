package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.RecruitmentIntegrationView;

import java.util.LinkedList;

public class RecruitmentIntegrationSinksContainer extends SinksContainer {
    public RecruitmentIntegrationSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews() {
        addView(new RecruitmentIntegrationView());
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
