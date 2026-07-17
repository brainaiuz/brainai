package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.AsteriskSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.MyCallsSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.SipuniSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.TwilioSettingsListView;

import java.util.LinkedList;

/**
 * User: Humoyun Hayitov
 * Date: 26.07.2020
 */
public class TelephonySettingsListSinksContainer extends SinksContainer {

    public TelephonySettingsListSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }


    @Override
    protected void initViews() {
        addView(new TwilioSettingsListView());
        addView(new AsteriskSettingsListView());
        addView(new SipuniSettingsListView());
        addView(new MyCallsSettingsListView());
    }
}
