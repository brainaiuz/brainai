package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.RecruitmentIntegrationItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RecruitmentIntegrationView extends CustomForm implements CustomFormConstants, Constants, AccountingConstants, CommandConstants, Colapse {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private TextBox hhClientId;
    private TextBox hhClientSecret;
    private TextBox zoomClientId;
    private TextBox zoomClientSecret;
    private TextBox botToken;
    private TextBox botUsername;
    private TextBox linkedInClientId;
    private TextBox linkedInClientSecret;
    private TextBox mehnatUzClientId;
    private TextBox mehnatUzClientSecret;
    private RecruitmentIntegrationItem item;

    public RecruitmentIntegrationView() {
        super("recruitment", settingsStrings.recruitment());
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        hhClientId = new TextBox();
        hhClientSecret = new TextBox();
        WfmButton2 hhSaveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        hhSaveButton.addClickHandler(event -> {
            if (!Validation.validateTextBoxRequired(hhClientId) || !Validation.validateTextBoxRequired(hhClientSecret)) {
                Info.warn(wfmStrings.pleasefillintherequiredfields());
            } else {
                item = new RecruitmentIntegrationItem();
                item.setHhClientId(hhClientId.getText());
                item.setHhClientSecret(hhClientSecret.getText());
                ProfileService.App.get().saveRecruitmentIntegrationItem(item, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        Window.open("https://hh.ru/oauth/authorize?\n" +
                                "response_type=code&\n" +
                                "client_id=" + hhClientId.getText(), null, commonParamForUrl);
                    }
                });
            }
        });

        addField(HH_CLIENT_ID, hhClientId, settingsStrings.clientId());
        addField(HH_CLIENT_SECRET, hhClientSecret, settingsStrings.clientSecret());
        addField(HH_SAVE_BUTTON, hhSaveButton);

        zoomClientId = new TextBox();
        zoomClientSecret = new TextBox();
        WfmButton2 zoomSaveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        zoomSaveButton.addClickHandler(event -> {
            if (!Validation.validateTextBoxRequired(zoomClientId) || !Validation.validateTextBoxRequired(zoomClientSecret)) {
                Info.warn(wfmStrings.pleasefillintherequiredfields());
            } else {
                item = new RecruitmentIntegrationItem();
                item.setZoomClientId(zoomClientId.getText());
                item.setZoomClientSecret(zoomClientSecret.getText());
                ProfileService.App.get().saveRecruitmentIntegrationItem(item, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        Window.open("https://zoom.us/oauth/authorize?\n" +
                                "response_type=code&\n" +
                                "client_id=" + zoomClientId.getText() + "&redirect_uri=" + GWT.getHostPageBaseURL() + "/common/headHunterServlet?integration_type=zoom", null, commonParamForUrl);
                    }
                });
            }
        });

        addField(ZOOM_CLIENT_ID, zoomClientId, settingsStrings.clientId());
        addField(ZOOM_CLIENT_SECRET, zoomClientSecret, settingsStrings.clientSecret());
        addField(ZOOM_SAVE_BUTTON, zoomSaveButton);


        botToken = new TextBox();
        botUsername = new TextBox();
        WfmButton2 botSaveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        botSaveButton.addClickHandler(event -> {
            if (!Validation.validateTextBoxRequired(botToken) || !Validation.validateTextBoxRequired(botUsername)) {
                Info.warn(wfmStrings.pleasefillintherequiredfields());
            } else {
                item = new RecruitmentIntegrationItem();
                item.setBotToken(botToken.getText());
                item.setBotUsername(botUsername.getText());
                ProfileService.App.get().saveRecruitmentIntegrationItem(item, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, true);
                        messageBox.setTitle("Telgegram Bot Synced");
                        messageBox.setMessage("@"+botUsername.getText());
                        messageBox.open();
                    }
                });
            }
        });

        addField(TELEGRAM_BOT_TOKEN, botToken, wfmStrings.accessToken());
        addField(TELEGRAM_BOT_USERNAME, botUsername, wfmStrings.username());
        addField(TELEGRAM_BOT_SAVE_BUTTON, botSaveButton);




        linkedInClientId = new TextBox();
        linkedInClientSecret = new TextBox();
        WfmButton2 linkedInSaveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        addField(LINKEDIN_CLIENT_ID, linkedInClientId, settingsStrings.clientId());
        addField(LINKEDIN_CLIENT_SECRET, linkedInClientSecret, settingsStrings.clientSecret());
        addField(LINKEDIN_SAVE_BUTTON, linkedInSaveButton);

        mehnatUzClientId = new TextBox();
        mehnatUzClientSecret = new TextBox();
        WfmButton2 mehnatUzSaveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        addField(MEHNAT_UZ_CLIENT_ID, mehnatUzClientId, settingsStrings.clientId());
        addField(MEHNAT_UZ_CLIENT_SECRET, mehnatUzClientSecret, settingsStrings.clientSecret());
        addField(MEHNAT_UZ_SAVE_BUTTON, mehnatUzSaveButton);

        show();
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
        ProfileService.App.get().getRecruitmentIntegrationItem(new AbstractAsyncCallback<RecruitmentIntegrationItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(RecruitmentIntegrationItem result) {
                if (result != null) {
                    hhClientId.setText(result.getHhClientId());
                    hhClientSecret.setText(result.getHhClientSecret());
                    zoomClientSecret.setText(result.getZoomClientSecret());
                    zoomClientId.setText(result.getZoomClientId());
                    botToken.setText(result.getBotToken());
                    botUsername.setText(result.getBotUsername());
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.RECRUITMENT_INTEGRATION_FORM;
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
