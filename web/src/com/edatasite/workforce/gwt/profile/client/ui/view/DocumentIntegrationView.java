package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.ui.SettingsLogoBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class DocumentIntegrationView extends CustomForm implements CustomFormConstants, Constants, AccountingConstants, CommandConstants, Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private final SettingsLogoBundle settingsLogoBundle = GWT.create(SettingsLogoBundle.class);

    private TextBox didoxInn;
    private TextBox didoxPassword;

    public DocumentIntegrationView() {
        super("document", wfmStrings.documents());
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        didoxInn = new TextBox();
        didoxInn.ensureDebugId("didoxInn");

        didoxPassword = new TextBox();
        didoxPassword.ensureDebugId("didoxPassword");

        Image logoForPayPal = new Image(settingsLogoBundle.logoForPayPal());
        logoForPayPal.setPixelSize(100, 50);

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(sender -> {
            if (validateDidox()) {
                save();
            } else {
                Info.warn(wfmStrings.fillAllRequiredFields());
            }
        });

        addTitleField(PAYMENT_GATEWAYS, settingsStrings.onlinePaymentDetails());
        addField("DIDOX_INN", didoxInn, "Inn");
        addField("DIDOX_PASSWORD", didoxPassword, "Password");
        addField("DIDOX_SAVE_BUTTON", saveButton);

    }

    @Override
    protected void addButtons() {
    }

    @Override
    protected void getDataToFillFields() {
        ProfileService.App.get().getDidoxInn(new AbstractAsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                didoxInn.setText(result);
            }
        });
    }

    private boolean validateDidox() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(didoxInn)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(didoxPassword)) {
            errors++;
        }
        return errors == 0;
    }

    private void save() {

        ProfileService.App.get().updateDidoxCredentials(didoxInn.getText(), didoxPassword.getText(), new AbstractAsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(String result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.integrationSettings()), Info.Type.INFO);
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.DOCUMENT_INTEGRATION_FORM;
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
