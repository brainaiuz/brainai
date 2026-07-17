package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by User on 1/4/2017.
 */
public class SignupCompanySettingsViewUIBinder implements Constants {

    @UiField
    SpanElement welcome;
    @UiField
    Label companyNameLabel;
    @UiField
    TextBox companyName;
    @UiField
    Label languageLabel;
    @UiField
    DataListBox language;
    @UiField
    WfmButton2 saveButton;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private HTMLPanel rootElement;

    interface ISignupCompanySettingsViewUIBinder extends UiBinder<HTMLPanel, SignupCompanySettingsViewUIBinder> {
    }

    private static ISignupCompanySettingsViewUIBinder ourUiBinder = GWT.create(ISignupCompanySettingsViewUIBinder.class);

    private final ProfileServiceAsync profileService = ProfileService.App.get();

    public SignupCompanySettingsViewUIBinder(SignupCompanySettingsViewInterface signupCompanySettingsViewInterface) {
        rootElement = ourUiBinder.createAndBindUi(this);
        init();
    }

    private void init() {

        welcome.setInnerText(settingsStrings.welcomeToKpiCom());
        companyNameLabel.setText(wfmStrings.companyName() + ":");
        languageLabel.setText(settingsStrings.systemLanguage() + ":");
        //User has already company name. So I'm setting it there.
        // Because there is a request from cookie dev :
        //After I register a user with email and specify company during registration, on web it asks me for a company name
        if (Utils.getCompanyName() != null && !"".equals(Utils.getCompanyName())) {
            companyName.setText(Utils.getCompanyName());
        } else {
            companyName.getElement().setAttribute("placeholder", settingsStrings.enterYourCompanyNameHere());
        }

        language.setWithoutNullLabel(true);
        language.setItems(UiSettings.LANGUAGES);
        language.setSelected(0);

        saveButton.setStyleName(BTN_PRIMARY);
        saveButton.setText(settingsStrings.proceedToKpiCom());
        saveButton.addClickHandler(clickEvent -> save());
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(companyName)) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void save() {
        if (!validate()) {
            return;
        }

        SettingsData dataForUpdate = getDataForSave();
        LoadingPanel.loading(true);
        profileService.updateSignUpCompanyInfo(dataForUpdate, new AbstractAsyncCallback() {

            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.companyDetailsP()), Info.Type.INFO);
                Utils.redirect(GWT.getHostPageBaseURL() + DEFAULT_SECTION + ".html");
            }
        });
    }

    private SettingsData getDataForSave() {
        SettingsData dataForUpdate = new SettingsData();
        final String localeName = language.getSelectedItem().getDescription();
        dataForUpdate.setInternationalization(localeName);
        dataForUpdate.setCompanyName(companyName.getText());
        return dataForUpdate;
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }

}
