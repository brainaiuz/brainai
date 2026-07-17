package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddWageRateView;

import java.util.LinkedList;

/**
 * User : Akhror on 13/03/2024
 */
public class AddWageRateSinksContainer extends SinksContainer {
    public AddWageRateSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new AddWageRateView(id));
    }
}
