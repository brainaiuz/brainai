package com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.BotActivationService;
import com.edatasite.workforce.gwt.core.client.rpc.WhatsAppService;
import com.edatasite.workforce.gwt.core.client.rpc.WhatsAppServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.WhatsappCredentialsItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.SettingsLogoBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MessengerSettingsView extends CustomForm {
    private static SettingStrings settingsStrings = SettingStrings.App.get();
    private static final WhatsAppServiceAsync whatsappService = WhatsAppService.App.get();
    private static SettingsLogoBundle settingsLogoBundle = GWT.create(SettingsLogoBundle.class);
    private static WfmStrings wfmStrings = WfmStrings.App.get();
    private TextBox phoneNumber;
    private TextBox keyBox = new TextBox();

    public MessengerSettingsView() {
        super("messengerSettings", "Messengers");
    }



    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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

    }

    @Override
    protected void getDataToFillFields() {

    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        drawTelegramConfigurations();
        drawHelperConfigurations();
        drawWhatsAppConfiguration();
        return null;
    }


    private void drawTelegramConfigurations() {
        Image telegramLogo = new Image(settingsLogoBundle.logoTelegram());
        telegramLogo.setPixelSize(80, 80);

        HTML telegramText = new HTML(settingsStrings.telegramViewText());


        WfmButton2 configureButton = new WfmButton2(settingsStrings.configure(), WfmButton2.BTN_PRIMARY);
        configureButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("integrationsSettings|telegramSettingsList"));

        addField(TELEGRAM_CONFIGURE_BUTTON, configureButton);
        addField(TELEGRAM_LOGO, telegramLogo);
        addField(TELEGRAM_VIEW_TEXT, telegramText);
    }

    private void drawHelperConfigurations() {
        Image helperLogo = new Image(settingsLogoBundle.logoHelperBot());
        helperLogo.setPixelSize(80, 80);

        HTML telegramText = new HTML(settingsStrings.helperBotText());

        WfmButton2 refreshButton = new WfmButton2(wfmStrings.refresh(), WfmButton2.BTN_PRIMARY);
        refreshButton.addClickHandler(event -> refreshHelperKey());

        addField(TELEGRAM_HELPER_LOGO, helperLogo);
        addField(TELEGRAM_HELPER_TEXT, telegramText);
        addField(TELEGRAM_HELPER_REFRESH_BUTTON, refreshButton);
        addField(TELEGRAM_HELPER_KEY, this.keyBox);

        fillKeyBox();
    }

    private void drawWhatsAppConfiguration() {
        Image whatsAppLogo = new Image(settingsLogoBundle.whatsappLogo());
        whatsAppLogo.setPixelSize(80, 80);
        HTML whatsAppText = new HTML("");
        phoneNumber = new TextBox();
        WfmButton2 configureButton = new WfmButton2(settingsStrings.configure(), WfmButton2.BTN_PRIMARY);
        configureButton.addClickHandler(event -> {
            if (!Validation.validateTextBoxRequired(phoneNumber)) {
                Info.warn(wfmStrings.pleasefillintherequiredfields());
            } else {
                whatsappService.saveWhatsappCredentials(new WhatsappCredentialsItem(phoneNumber.getValue()), new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(Void result) {
                        Info.show(wfmStrings.messSuccessfullySaved());
                    }
                });

            }
        });

        addField(WHATSAPP_LOGO, whatsAppLogo);
        addField(WHATSAPP_TEXT, whatsAppText);
        addField(WHATSAPP_BUTTON, configureButton);
        addField(WHATSAPP_NUMBER, phoneNumber, wfmStrings.phone());

        getWhatsappCredentials();
    }


    private void fillKeyBox() {
        keyBox.addClickHandler(clickEvent -> {
            Utils.copyToClipBoard(keyBox.getValue());
            Info.show(wfmStrings.copiedSuccessfully());
        });
        BotActivationService.App.get().getActivationKey(new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {

            }

            public void onSuccess(String key) {
                keyBox.setValue(key);
            }
        });
    }

    private void refreshHelperKey() {
        BotActivationService.App.get().updateActivationKeyForUser(new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {

            }

            public void onSuccess(String key) {
                keyBox.setValue(key);
            }
        });
    }

    private void  getWhatsappCredentials(){
        whatsappService.getWhatsappCredentials(new AsyncCallback<WhatsappCredentialsItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(WhatsappCredentialsItem result) {
                phoneNumber.setText(result.getPhoneNumber());
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.MESSENGER_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }





}

