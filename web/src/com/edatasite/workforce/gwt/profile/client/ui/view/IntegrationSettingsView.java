package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.IntegrationSettingsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Shohruh on 01-Feb-17.
 */
public class IntegrationSettingsView extends CustomForm2 implements CustomFormConstants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();


    // TARGET ERP settings
    private TextBox url;
    private TextBox username;
    private PasswordTextBox password;
    private TextBox controller;
    private boolean isPassChanged = false;

    public IntegrationSettingsView() {
        super("integrationSettings", wfmStrings.targetIntegration());
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        return null;
    }

    protected void registerFields() {
        url = new TextBox();
        url.setWidth("400px");
        username = new TextBox();
        username.setWidth("400px");
        password = new PasswordTextBox();
        password.setWidth("400px");
        password.addChangeHandler(changeEvent -> isPassChanged = true);
        controller = new TextBox();
        controller.setWidth("400px");

        addTitleField(INTEGRATION_SETTINGS.TARGET_INTEGRATION, settingsStrings.targetIntegrationSettings());

        addField(INTEGRATION_SETTINGS.TARGET_URL, url, getTitle(settingsStrings.serviceUrl()));
        addField(INTEGRATION_SETTINGS.TARGET_USERNAME, username, getTitle(wfmStrings.username()));
        addField(INTEGRATION_SETTINGS.TARGET_PASSWORD, password, getTitle(wfmStrings.password()));
        addField(INTEGRATION_SETTINGS.TARGET_CONTROLLER, controller, getTitle(settingsStrings.controller()));

        getDataToFillFields();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (INTEGRATION_SETTINGS.TARGET_INTEGRATION.equals(fieldID)) {
            return settingsStrings.targetIntegrationSettings();
        } else if (INTEGRATION_SETTINGS.TARGET_URL.equals(fieldID)) {
            return settingsStrings.serviceUrl();
        } else if (INTEGRATION_SETTINGS.TARGET_USERNAME.equals(fieldID)) {
            return wfmStrings.username();
        } else if (INTEGRATION_SETTINGS.TARGET_PASSWORD.equals(fieldID)) {
            return wfmStrings.password();
        } else if (INTEGRATION_SETTINGS.TARGET_CONTROLLER.equals(fieldID)) {
            return settingsStrings.controller();
        }
        return null;
    }

    @Override
    public String getIconStyle() {
        return "accountMark  ac-type-num-settings";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, wfmStrings.save(), wfmStrings.save(), clickEvent -> save());
    }

    private void save() {
        if (!validation()) return;
        IntegrationSettingsItem settingsItem = new IntegrationSettingsItem();
        settingsItem.setTgUrl(url.getText());
        settingsItem.setTgUsername(username.getText());
        if (isPassChanged) settingsItem.setTgPassword(password.getText());
        settingsItem.setTgController(controller.getText());

        ProfileService.App.get().saveIntegrationSettins(settingsItem, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    private boolean validation() {
        int error = 0;
        if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
            if (!Validation.validateTextBoxRequired(url) || !url.getText().endsWith(".wsdl")) error++;
            if (!Validation.validateTextBoxRequired(username)) error++;
            if (!Validation.validateTextBoxRequired(password)) error++;
            if (!Validation.validateTextBoxRequired(controller)) error++;
        }
        if (error > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected void getDataToFillFields() {
        ProfileService.App.get().getIntegrationSettings(new AsyncCallback<IntegrationSettingsItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(IntegrationSettingsItem settingsItem) {
                url.setText(settingsItem.getTgUrl());
                username.setText(settingsItem.getTgUsername());
                password.setText(settingsItem.getTgPassword());
                controller.setText(settingsItem.getTgController());
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.INTEGRATION_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }
}
