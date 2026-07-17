package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.ProfileMessages;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.CredentialsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class UserCredentials extends CustomForm implements CustomFormConstants, Colapse {

    // -------------------------------------------------------------------------
    // Constants & static fields
    // -------------------------------------------------------------------------

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileMessages profileMessages = ProfileMessages.App.get();

    // -------------------------------------------------------------------------
    // Instance fields
    // -------------------------------------------------------------------------

    private TextBox loginBox;
    private TextBox emailBox;

    private HTML passwordStrength;

    private PasswordTextBox currentPassBox;
    private PasswordTextBox newPassBox;
    private PasswordTextBox reEntertPassBox;

    private DataListBox countryLB;
    private DataListBox timeZone;
    private DataListBox startPage;
    private final DataListBox languageList = new DataListBox();

    private HTMLPanel currentPasswordLabelWrapper;

    private boolean languageChanged = false;
    private final boolean isShowLanguage = true;
    private boolean advancedPasswordEnabled = false;

    private CredentialsItem credentialsItem;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public UserCredentials() {
        super("credentials", settingsStrings.userCredentials());
    }

    // -------------------------------------------------------------------------
    // Lifecycle / initialization
    // -------------------------------------------------------------------------

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {

        loginBox = new TextBox();
        loginBox.setReadOnly(true);
        loginBox.setEnabled(false);

        currentPassBox = new PasswordTextBox();
        currentPassBox.setWidth("100%");

        currentPassBox.addKeyUpHandler(event -> {
            boolean hasCurrentPassword = !currentPassBox.getText().trim().isEmpty();
            newPassBox.setEnabled(hasCurrentPassword);
            reEntertPassBox.setEnabled(hasCurrentPassword);
            if (!hasCurrentPassword) {
                newPassBox.setText("");
                reEntertPassBox.setText("");
                passwordStrength.setHTML("");
            }
        });

        passwordStrength = new HTML();

        newPassBox = new PasswordTextBox();
        newPassBox.setWidth("100%");
        newPassBox.setEnabled(false);
        newPassBox.addKeyUpHandler(keyUpEvent -> {
            if (!"".equals(newPassBox.getText())) {
                passwordStrength.setHTML(settingsStrings.passwordStrength() + " " + getPasswordStrengthTextFromCode(checkPass(newPassBox.getText(), advancedPasswordEnabled), advancedPasswordEnabled));
            } else {
                passwordStrength.setHTML("");
            }
        });

        reEntertPassBox = new PasswordTextBox();
        reEntertPassBox.setWidth("100%");
        reEntertPassBox.setEnabled(false);

        emailBox = new TextBox();

        languageList.setWithoutNullLabel(true);
        languageList.setItems(UiSettings.LANGUAGES);

        countryLB = new DataListBox();
        countryLB.ensureDebugId("User_Credentials_countries");
        countryLB.addValueChangeHandler(sender -> {
            if (countryLB.isSomethingSelected()) {
                ProfileService.App.get().getCountryTimezone(countryLB.getSelectedItem().getId(),
                        new AbstractAsyncCallback<SelectItem[]>() {
                            public void failure(Throwable caught) {
                            }

                            public void success(SelectItem[] items) {
                                timeZone.setItems(items);
                            }
                        });
            } else {
                timeZone.clear();
            }
        });

        timeZone = new DataListBox();
        timeZone.ensureDebugId("User_Credentials_timeZone");

        startPage = new DataListBox();
        startPage.ensureDebugId("User_Credentials_startPage");

        drawForm();
    }

    private void drawForm() {
        addTitleField(USER_CREDENTIALS.CREDENTIAL_INFORMATIONS, settingsStrings.credentialInformations());
        addField(USER_CREDENTIALS.LOGIN, loginBox, getTitle(wfmStrings.login()));

        final HTMLPanel currentPasswordPanel = new HTMLPanel("");
        currentPasswordPanel.setStyleName("form-group");
        currentPasswordLabelWrapper = new HTMLPanel("<span class=\"gwt-HTML\">" + getTitle(settingsStrings.currentPass()) + "</span>");
        currentPasswordLabelWrapper.setStyleName("form-group__label");
        currentPasswordPanel.add(currentPasswordLabelWrapper);
        final HTMLPanel currentPasswordInputWrapper = new HTMLPanel("");
        currentPasswordInputWrapper.setStyleName("form-group__content");
        currentPasswordInputWrapper.add(currentPassBox);
        currentPasswordPanel.add(currentPasswordInputWrapper);
        addField(USER_CREDENTIALS.CURRENT_PASSWORD, currentPasswordPanel, null, true);

        FlowPanel panel = new FlowPanel();
        panel.add(newPassBox);
        panel.add(passwordStrength);

        addField(USER_CREDENTIALS.NEW_PASSWORD, panel, getTitle(wfmStrings.addNewPassword()));
        addField(USER_CREDENTIALS.REENTER_PASSWORD, reEntertPassBox, getTitle(settingsStrings.reEnterPass()));

        addField(EMAIL, emailBox, getTitle(wfmStrings.email()));

        if (isShowLanguage)
            addField(LANGUAGE, languageList, getTitle(wfmStrings.language()));

        addField(COUNTRY, countryLB, wfmStrings.country());
        addField(USER_CREDENTIALS.TIMEZONE, timeZone, settingsStrings.timezone());
        addField(USER_CREDENTIALS.START_PAGE, startPage, settingsStrings.startPage());

        show();
    }

    // -------------------------------------------------------------------------
    // Data loading
    // -------------------------------------------------------------------------

    @Override
    protected void getDataToFillFields() {
        ProfileService.App.get().getCredentials(new AbstractAsyncCallback<CredentialsItem>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(final CredentialsItem credentialsItem) {
                MainLayout.get().removeTabsContainerFromParent();
                UserCredentials.this.credentialsItem = credentialsItem;

                loginBox.setText(credentialsItem.getLogin());
                emailBox.setText(credentialsItem.getEmail());
                languageList.setSelectedByDescription(credentialsItem.getInternationalization());

                countryLB.setItems(credentialsItem.getCountry());
                if (credentialsItem.getCountryID() != null) {
                    countryLB.setSelected(credentialsItem.getCountryID());
                    ProfileService.App.get().getCountryTimezone(credentialsItem.getCountryID(),
                            new AbstractAsyncCallback<SelectItem[]>() {
                                @Override
                                public void failure(Throwable caught) {
                                }

                                @Override
                                public void success(SelectItem[] items) {
                                    timeZone.setItems(items);
                                    if (credentialsItem.getTimeZoneId() != null)
                                        timeZone.setSelected(credentialsItem.getTimeZoneId());
                                }
                            });
                }

                startPage.setItems(credentialsItem.getStartPageLists());
                if (credentialsItem.getStartPage() != null)
                    startPage.setSelectedByValue(credentialsItem.getStartPage());

                languageList.addValueChangeHandler(changeEvent -> {
                    if (!languageList.getSelectedItem().getDescription().equals(credentialsItem.getInternationalization()))
                        languageChanged = true;
                });
                advancedPasswordEnabled = credentialsItem.getAdvancedPasswordEnabled();
            }
        });
    }

    // -------------------------------------------------------------------------
    // Save / action
    // -------------------------------------------------------------------------

    private void saveCredentials() {
        if (!validate()) return;

        credentialsItem = new CredentialsItem();
        credentialsItem.setEmail(emailBox.getText());
        credentialsItem.setNewPass(newPassBox.getText());
        credentialsItem.setCurrentPass(currentPassBox.getText());
        credentialsItem.setInternationalization(languageList.getSelectedItem().getDescription());
        credentialsItem.setTimeZoneId(timeZone.getSelectedId());
        credentialsItem.setStartPage(startPage.getDisplayValue());

        LoadingPanel.loading(true);

        ProfileService.App.get().saveCredentials(credentialsItem, new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Boolean result) {
                LoadingPanel.loading(false);
                if (result != null && !result) {
                    Info.show(settingsStrings.wrongCurrentPassword(), Info.Type.WARNING);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), settingsStrings.userCredentials()), Info.Type.INFO);
                    if (languageChanged) {
                        Window.open(Utils.getPathName() + "?locale=" + languageList.getSelectedItem().getDescription(), "_self", "");
                    }
                }
            }
        });
    }

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------

    private boolean validate() {
        int errors = 0;

        if (!Validation.validateEmailRequired(emailBox)) errors++;

        if (countryLB.isSomethingSelected() && (timeZone == null || timeZone.getSelectedItem() == null)) errors++;

        if (isNotNull(currentPassBox)) {
            if (!isNotNull(newPassBox) || !isNotNull(reEntertPassBox)) {
                errors++;
                Validation.validateTextBoxRequired(newPassBox);
                Validation.validateTextBoxRequired(reEntertPassBox);
                Info.show(settingsStrings.enterAndConfirmPassword(), Info.Type.WARNING);
            } else {
                if (!newPassBox.getText().equals(reEntertPassBox.getText())) {
                    errors++;
                    Info.show(settingsStrings.plsEnterSamePass(), Info.Type.WARNING);
                }
                if ("WEAK".equals(checkPass(newPassBox.getText(), advancedPasswordEnabled))) {
                    errors++;
                    Info.show(settingsStrings.passwordIsWeak(), Info.Type.WARNING);
                }
            }
        } else {
            boolean emailHasChanged = !emailBox.getText().equals(credentialsItem.getEmail());
            if (isNotNull(newPassBox) || isNotNull(reEntertPassBox) || emailHasChanged) {
                Info.show(settingsStrings.wrongCurrentPassword(), Info.Type.WARNING);
                errors++;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private String getPasswordStrengthTextFromCode(String code, boolean adPassEnabled) {
        int minLen = adPassEnabled ? 15 : 8;
        if ("SHORT".equals(code))
            return "<font color='red'><b>" + settingsStrings.tooshort() + "</b></font>";

        if ("WEAK".equals(code))
            return "<font color='red'><b>" + settingsStrings.weak() + "</b> " + profileMessages.weakPasswordWarning(String.valueOf(minLen)) + "</font>";

        if ("STRONG".equals(code))
            return "<font color='green'><b>" + settingsStrings.strong() + "</b></font>";

        if ("MEDIUM".equals(code))
            return "<font color='blue'><b>" + wfmStrings.medium() + "</b></font>";

        return "<font color='red'><b>" + settingsStrings.weak() + "</b></font>";
    }

    private static native String checkPass(String pass, boolean advancedPasswordEnabled) /*-{
        if (advancedPasswordEnabled)
            return $wnd.passStrength(pass, 15, 20, 1, 1, 1, 1, 11);
        else
            return $wnd.passStrength(pass, 8, 12, 1, 1, 1, 1, 4);
    }-*/;

    private boolean isNotNull(PasswordTextBox widget) {
        return (widget.getText() != null) && (!"".equals(widget.getText()));
    }

    // -------------------------------------------------------------------------
    // Overrides / metadata
    // -------------------------------------------------------------------------

    @Override
    protected void addButtons() {
        WfmButton2 save = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        save.getElement().setId("User_Credentials_Update_button");
        save.addClickHandler(sender -> saveCredentials());
        addButton(save);
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.USER_CREDENTIALS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return "icon-settings-user-credentials";
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

}