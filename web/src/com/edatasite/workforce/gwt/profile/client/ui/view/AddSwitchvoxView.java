package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 17.05.12
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class AddSwitchvoxView extends CustomForm implements CustomFormConstants {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private TextBox userName;
    private PasswordTextBox password;
    private TextBox serverID;

    public AddSwitchvoxView() {
        super("switchvoxSettings", "Switchvox");
    }

    public Widget onInitialize() {
        super.onInitialize();
        String addSwitch = "add_switchvox_";

        userName = new TextBox();
        userName.addStyleName(Constants.DEFAULT_WIDTH);
        userName.ensureDebugId(addSwitch.concat("userName"));

        password = new PasswordTextBox();
        password.addStyleName("form-control");
        password.addStyleName(Constants.DEFAULT_WIDTH);
        password.ensureDebugId(addSwitch.concat("password"));

        serverID = new TextBox();
        serverID.addStyleName(Constants.DEFAULT_WIDTH);
        serverID.ensureDebugId(addSwitch.concat("serverID"));

        addTitleField(SWITCHVOX_SETTINGS.SWITCHVOX_SETTINGS, settingsStrings.switchvoxSettings());
        addField(SWITCHVOX_SETTINGS.USER_NAME, userName, getTitle(wfmStrings.username(), true));
        addField(SWITCHVOX_SETTINGS.PASSWORD, password, getTitle(wfmStrings.password(), true));
        addField(SWITCHVOX_SETTINGS.SERVER_ID, serverID, getTitle(settingsStrings.serverID(), true));

        show();

        return this;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProfileService.App.get().getCompanyInfoSwitchvox(new AsyncCallback<SettingsData>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SettingsData result) {
                LoadingPanel.loading(false);
                userName.setText(result.getSwitchvoxUserName() != null ? result.getSwitchvoxUserName() : "");
                password.setText(result.getSwitchvoxPassword() != null ? result.getSwitchvoxPassword() : "");
                serverID.setText(result.getSwitchvoxServerID() != null ? result.getSwitchvoxServerID() : "");
            }
        });
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, null, "save", (ClickHandler) clickEvent -> save());
    }

    private void save() {
        if (valadition()) {
            SettingsData data = new SettingsData();
            data.setSwitchvoxUserName(userName.getValue());
            data.setSwitchvoxPassword(password.getValue());
            data.setSwitchvoxServerID(serverID.getValue());
            LoadingPanel.loading(true);
            ProfileService.App.get().updateCompanyInfoSwitchvox(data, new AsyncCallback() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Object result) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.switchCompany()), Info.Type.INFO);
                }
            });
        }
    }

    private boolean valadition() {
        int error = 0;
        if (!Validation.validateTextBoxRequired(userName)) {
            error++;
        }
        if (!Validation.validateTextBoxRequired(password)) {
            error++;
        }
        if (!Validation.validateTextBoxRequired(serverID)) {
            error++;
        }
        return error <= 0;
    }

    public String getIconStyle() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SWITCHVOX_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

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
}
