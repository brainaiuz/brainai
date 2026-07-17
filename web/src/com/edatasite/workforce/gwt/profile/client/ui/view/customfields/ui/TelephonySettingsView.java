package com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.SettingsLogoBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SETTINGS_ASTERISK_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SETTINGS_TWILIO_LIST;


/**
 * User: Humoyun Hayitov
 * Date: 08.6.2020
 */
public class TelephonySettingsView extends View implements CustomFormConstants, FittedContent {

    interface TelephonySettingsViewUiBinder extends UiBinder<HTMLPanel, TelephonySettingsView> {

    }
    @UiField
    HTMLPanel mainDiv;
    @UiField
    HTMLPanel twilioPanel;
    @UiField
    HTMLPanel twilioLogo;
    @UiField
    HTMLPanel twilioText;
    @UiField
    HTMLPanel twilioButton;
    @UiField
    HTMLPanel asteriskPanel;
    @UiField
    HTMLPanel asteriskLogo;
    @UiField
    HTMLPanel asteriskText;
    @UiField
    HTMLPanel asteriskButton;
    @UiField
    HTMLPanel sipuniPanel;
    @UiField
    HTMLPanel sipuniLogo;
    @UiField
    HTMLPanel sipuniText;
    @UiField
    HTMLPanel sipuniButton;
    @UiField
    HTMLPanel myCallsPanel;
    @UiField
    HTMLPanel myCallsLogo;
    @UiField
    HTMLPanel myCallsText;
    @UiField
    HTMLPanel myCallsButton;


    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected SettingsLogoBundle settingsLogoBundle = GWT.create(SettingsLogoBundle.class);

    public TelephonySettingsView() {
        super("telephonySettings", settingsStrings.telephony());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected Widget onInitialize() {
        TelephonySettingsView.TelephonySettingsViewUiBinder ourUiBinder = GWT.create(TelephonySettingsView.TelephonySettingsViewUiBinder.class);
        add(ourUiBinder.createAndBindUi(this));
        initialize();
        return null;
    }

    protected void initialize() {
        mainDiv.setVisible(true);
        mainDiv.getElement().getStyle().setPaddingTop(0, Style.Unit.PX);
        mainDiv.getElement().getStyle().setPaddingBottom(0, Style.Unit.PX);
        mainDiv.getElement().getStyle().setPaddingRight(10, Style.Unit.PX);
        mainDiv.getElement().getStyle().setPaddingLeft(10, Style.Unit.PX);
        mainDiv.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        if (Utils.hasPermission(SETTINGS_TWILIO_LIST)) {
            Image logo = new Image(settingsLogoBundle.logoTwilio());
            twilioLogo.add(logo);
            twilioText.add(new HTML(settingsStrings.twilioViewText()));
            WfmButton2 configureButton = new WfmButton2(settingsStrings.configure(), WfmButton2.BTN_PRIMARY);
            configureButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("telephonySettingsList|twilioSettingsList/" + settingsStrings.twilioAccounts()));
            twilioButton.add(configureButton);
        } else {
            twilioPanel.removeFromParent();
        }
        if (Utils.hasPermission(SETTINGS_ASTERISK_LIST)) {
            asteriskPanel.setVisible(true);
            Image logo = new Image(settingsLogoBundle.logoAsterisk());
            asteriskLogo.add(logo);
            asteriskText.add(new HTML(settingsStrings.asteriskViewText()));
            WfmButton2 configureButton = new WfmButton2(settingsStrings.configure(), WfmButton2.BTN_PRIMARY);
            configureButton.addClickHandler(event -> Utils.openURLCurrentTab("Settings.html#integrationsSettings|asteriskSettingsList"));
            asteriskButton.add(configureButton);
        } else {
            asteriskPanel.removeFromParent();
        }

        if (Utils.hasPermission(SETTINGS_ASTERISK_LIST)) {
            sipuniPanel.setVisible(true);
            Image logo = new Image(settingsLogoBundle.logoSipuni());
            sipuniLogo.add(logo);
            sipuniText.add(new HTML(settingsStrings.sipuniViewText()));
            WfmButton2 configureButton = new WfmButton2(settingsStrings.configure(), WfmButton2.BTN_PRIMARY);
            configureButton.addClickHandler(event -> Utils.openURLCurrentTab("Settings.html#integrationsSettings|sipuniSettingsList"));
            sipuniButton.add(configureButton);
        } else {
            sipuniPanel.removeFromParent();
        }

        if (Utils.hasPermission(SETTINGS_ASTERISK_LIST)) {
            myCallsPanel.setVisible(true);
            Image logo = new Image(settingsLogoBundle.logoMyCalls());
            myCallsLogo.add(logo);
            myCallsText.add(new HTML(settingsStrings.myCallsViewText()));
            WfmButton2 configureButton = new WfmButton2(settingsStrings.configure(), WfmButton2.BTN_PRIMARY);
            configureButton.addClickHandler(event -> Utils.openURLCurrentTab("Settings.html#integrationsSettings|myCallsSettingsList"));
            myCallsButton.add(configureButton);
        } else {
            myCallsPanel.removeFromParent();
        }

    }


}
