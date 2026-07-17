package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by Faxriddin on 1/30/2016.
 */
public class CountrySettingsSinksContainer extends SinksContainer {

    public CountrySettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        /*if (id != null) {
            addView(new AddCountrySettingsView(id));
        } else {
            addView(new AddCountrySettingsView());
        }*/
    }
}
