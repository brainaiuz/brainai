package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddSwitchvoxView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.AsteriskSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.MyCallsSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.SMSSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.SipuniSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.TelegramSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.TwilioSettingsListView;

import java.util.LinkedList;

/**
 * User: Dilshod Madrahimov
 * Date: 2019-06-20 14:24
 */
public class CommunicationSettingsSinksContainer extends SinksContainer {

    public CommunicationSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
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
        addView(new AddSwitchvoxView());
        addView(new SMSSettingsListView());
        addView(new TelegramSettingsListView());
    }
}
