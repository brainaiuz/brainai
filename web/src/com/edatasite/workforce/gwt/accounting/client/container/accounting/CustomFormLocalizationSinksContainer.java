package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.CustomFormLocalizationView;

import java.util.LinkedList;

public class CustomFormLocalizationSinksContainer extends SinksContainer {

    public CustomFormLocalizationSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        addView(new CustomFormLocalizationView(params[0]));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
    }
}
