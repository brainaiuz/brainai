package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.SettingsEmployeeSummaryView;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 17.03.2010
 * Time: 15:25:45
 */
public class ProfileSettingsSinksContainer extends SinksContainer {

    public ProfileSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        super.addView(new SettingsEmployeeSummaryView());
    }
}
