package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.ModuleDashboardListView;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 14:54
 */
public class ModuleDashboardSinksContainer extends SinksContainer {

    public ModuleDashboardSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ModuleDashboardListView());
    }
}
