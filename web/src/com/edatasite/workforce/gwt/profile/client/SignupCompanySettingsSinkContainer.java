package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.SignupCompanySettingsView;

import java.util.LinkedList;

/**
 * Created by User on 12/28/2016.
 */
public class SignupCompanySettingsSinkContainer extends SinksContainer {

    public SignupCompanySettingsSinkContainer() {
        super("newsignupcompanysettings", "Company Settings", new String[]{"add"}, NONE);

    }

    public SignupCompanySettingsSinkContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        super.addView(new SignupCompanySettingsView());
    }
}
