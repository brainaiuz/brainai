package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.ModuleDashboardCustomizeWidgetView;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 16:05
 */
public class ModuleDashboardViewSinksContainer extends SinksContainer {

    public ModuleDashboardViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
//        addView(new ModuleDashboardSummaryView(this.id));
        addView(new ModuleDashboardCustomizeWidgetView(this.id, this.params.length > 1));
    }
}
