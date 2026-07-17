package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

public class AddTelegramBotSettingsView extends FooteredCustomForm implements Colapse, FittedContent {

    private static final TelegramChatServiceAsync service = TelegramChatService.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private Integer objectId;
    private TextBox botNameBox;
    private TextBox tokenBox;

    public AddTelegramBotSettingsView(Integer objectId) {
        super("telegramSettings", settingsStrings.addTelegramBot());
        this.objectId = objectId;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        botNameBox = new TextBox();
        tokenBox = new TextBox();

        addField("TOKEN", new FormGroup(wfmStrings.accessToken(), tokenBox, true));
        addField("BOT_NAME", new FormGroup("Bot Name", botNameBox, true));

        show();
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();
        WfmButton2 saveAndRegister = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());

        Div wrapper = new Div();
        wrapper.add(saveAndRegister);
        result.add(wrapper);
        return result;
    }

    private void save() {
        if (!validate()) {
            return;
        }
        TelegramSettingsItem settingsItem = new TelegramSettingsItem();
        settingsItem.setId(objectId);
        settingsItem.setToken(tokenBox.getText().trim());
        settingsItem.setBotName(botNameBox.getText().trim());
        LoadingPanel.loading(true);
        service.saveTelegramSettingsItem(settingsItem, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (result == 1) {
                    closeTab();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.telegramBot()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TELEGRAM_SETTINGS_ADD_EDIT, true, AddTelegramBotSettingsView.this);
                } else if (result == 2) {
                    Info.warn("Bot with the same Access Token or Bot Name already exists");
                } else if (result == 3) {
                    Info.show("Failed To Sync with telegram! Access token invalid", Info.Type.WARNING);
                }
            }
        });
    }

    private boolean validate() {
        boolean error = false;
        if (!Validation.validateTextBoxRequired(tokenBox)) {
            error = true;
        }

        if (!Validation.validateTextBoxRequired(botNameBox)) {
            error = true;
        }
        return !error;
    }

    @Override
    protected void addButtons() {

    }

    @Override
    protected void getDataToFillFields() {
        if (objectId != null) {
            LoadingPanel.loading(true);
            service.getTelegramSettingsItem(objectId, new AsyncCallback<TelegramSettingsItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(TelegramSettingsItem telegramSettingsItem) {
                    LoadingPanel.loading(false);
                    if (telegramSettingsItem != null) {
                        tokenBox.setText(telegramSettingsItem.getToken());
                        botNameBox.setText(telegramSettingsItem.getBotName());
                    }
                }
            });
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TELEGRAM_BOT_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
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
}
