package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddMinimumWageView;

import java.util.LinkedList;

/**
 * User : Akhror on 12/03/2024
 */
public class AddMinimumWageSinksContainer extends SinksContainer {
    public AddMinimumWageSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new AddMinimumWageView(id));
    }
}
