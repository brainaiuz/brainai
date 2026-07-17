package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.pm.NumberingSettingsView;
import com.edatasite.workforce.gwt.profile.client.ui.view.pm.TimesheetSettingsView;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 12.07.2010
 * Time: 13:11:23
 */
public class PMSettingsSinksContainer extends SinksContainer {

    public PMSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new NumberingSettingsView());
        addView(new TimesheetSettingsView());
    }
}
