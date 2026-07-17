package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.AttendanceTerminalListView;
import com.edatasite.workforce.gwt.availability.client.ui.view.FingerPrintListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddSwitchvoxView;
import com.edatasite.workforce.gwt.profile.client.ui.view.DocumentIntegrationView;
import com.edatasite.workforce.gwt.profile.client.ui.view.GoogleIntegrationView;
import com.edatasite.workforce.gwt.profile.client.ui.view.IntegrationSettingsView;
import com.edatasite.workforce.gwt.profile.client.ui.view.MicrosoftIntegrationView;
import com.edatasite.workforce.gwt.profile.client.ui.view.PaypalPaymentView;
import com.edatasite.workforce.gwt.profile.client.ui.view.RecruitmentIntegrationView;
import com.edatasite.workforce.gwt.profile.client.ui.view.ShopifyIntegrationView;
import com.edatasite.workforce.gwt.profile.client.ui.view.SynchronizeWithMagentoView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.AsteriskSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.MessengerSettingsView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.MyCallsSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.SMSSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.SipuniSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.TelegramSettingsListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.TelephonySettingsView;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 14.05.2019 19:20
 */
public class IntegrationSinksContainer extends SinksContainer {

    public IntegrationSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        //Collaboration
        addView(new GoogleIntegrationView());
        addView(new MicrosoftIntegrationView());
        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
            addView(new IntegrationSettingsView());
        }
        //Payment Processing
        addView(new PaypalPaymentView());
        addView(new RecruitmentIntegrationView());
        addView(new DocumentIntegrationView());
//        addView(new StripePaymentView());
        //Communication
        addView(new TelephonySettingsView());
        addView(new AsteriskSettingsListView());
        addView(new SipuniSettingsListView());
        addView(new MyCallsSettingsListView());

        addView(new AddSwitchvoxView());
        addView(new SMSSettingsListView());
        addView(new MessengerSettingsView());
        addView(new TelegramSettingsListView());
        addView(new AttendanceTerminalListView());
        //E-Commerce
        if (Utils.hasGenericAccess(GenericSettingsEnum.MAGENTO_INTEGRATION_ENABLE)) {
            addView(new SynchronizeWithMagentoView());
            addView(new ShopifyIntegrationView());
        }
        //Time Management
        addView(new FingerPrintListView());
    }
}
