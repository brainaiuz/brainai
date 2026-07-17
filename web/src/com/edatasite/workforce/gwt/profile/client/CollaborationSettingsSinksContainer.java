package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.GoogleIntegrationView;
import com.edatasite.workforce.gwt.profile.client.ui.view.IntegrationSettingsView;
import com.edatasite.workforce.gwt.profile.client.ui.view.MicrosoftIntegrationView;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: 2019-06-20 14:24
 */
public class CollaborationSettingsSinksContainer extends SinksContainer {

    public CollaborationSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new GoogleIntegrationView());
        addView(new MicrosoftIntegrationView());
        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
            addView(new IntegrationSettingsView());
        }
    }
}
