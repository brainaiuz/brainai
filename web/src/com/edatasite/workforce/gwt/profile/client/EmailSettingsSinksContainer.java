package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.CompanyEmailSettings;
import com.edatasite.workforce.gwt.profile.client.ui.view.EmailTemplatesListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.SMSTemplatesListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.SignatureListView;
import com.edatasite.workforce.gwt.profile.client.ui.view.pdf.SettingsPdfTemplateListView;

import java.util.LinkedList;

/**
 * User: Admin
 * Date: 15.03.2010
 * Time: 12:37:47
 */
public class EmailSettingsSinksContainer extends SinksContainer implements PermissionConstants {

    public EmailSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(SETTINGS_COMPANY_EMAL_SETTINGS)) {
            super.addView(new CompanyEmailSettings());
        }
        if (Utils.hasPermission(SETTINGS_EMAIL_TEMPALTE_LIST)) {
            super.addView(new EmailTemplatesListView());
        }
        super.addView(new SignatureListView());
        if (Utils.hasPermission(SETTINGS_EMAIL_TEMPALTE_LIST)) {
            super.addView(new SMSTemplatesListView());
        }

        super.addView(new SettingsPdfTemplateListView());

        /*if (Utils.hasRole(Constants.DR) || Utils.hasRole(ADMIN)) {
            super.addView(new GoogleAnalyticsSettingsView());
        }*/
    }
}
