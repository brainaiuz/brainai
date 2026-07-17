package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioService;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 12/12/12
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddAsteriskSettingView extends CustomForm2 implements Colapse {
    private static final TwilioServiceAsync service = TwilioService.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final String FIELD_KEY = "Key";
    private static final String FIELD_VALUE = "Value";
    private static final String[] OUR_SMS_ERROR_CODES = {"101", "102", "103", "104", "111", "113", "114", "115", "116", "117"};
    private static final Set<String> OUR_SMS_ERROR_CODES_SET = new HashSet<>(Arrays.asList(OUR_SMS_ERROR_CODES));

    private final Integer objectID;
    private AsteriskSettings item = new AsteriskSettings();
    private TextBox number;
    private TextBox asteriskHost;
    private TextBox asteriskPort;

    public AddAsteriskSettingView(Integer objectID) {
        super("asteriskSettings", settingsStrings.addAsteriskAccount());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        initialize();
        addFields();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initialize() {
        number = new TextBox();
        number.addStyleName(Constants.DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(number);

        asteriskHost = new TextBox();
        asteriskHost.addStyleName(Constants.DEFAULT_WIDTH);
        asteriskPort = new TextBox();
        asteriskPort.addStyleName(Constants.DEFAULT_WIDTH);
        number = new TextBox();
        number.addStyleName(Constants.DEFAULT_WIDTH);

    }

    private void addFields() {
        addTitleField(BACKEND.TWILIO.PROVIDER_INFORMATION, settingsStrings.providerInformation());
        addField(BACKEND.ASTERISK.NUMBER, new AdvancedInputGroup(new HTML("+"), number, null, true, false), wfmStrings.number() + "<em class=\"redTitle\">*</em>:");
        addField(BACKEND.ASTERISK.ASTERISK_HOST, asteriskHost, settingsStrings.asteriskHost() + "<em class=\"redTitle\">*</em>:");
        addField(BACKEND.ASTERISK.ASTERISK_PORT, asteriskPort, settingsStrings.asteriskPort() + "<em class=\"redTitle\">*</em>:");
        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), event -> save());
//        addButton(wfmStrings.close(), WfmButton2.BTN_DEFAULT, (ClickHandler) event -> closeTab());
    }

    @Override
    protected void getDataToFillFields() {
        service.getAsteriskSettings(objectID, new AsyncCallback<AsteriskSettings>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(AsteriskSettings result) {
                item = result == null ? item : result;
                fillWithData();
            }
        });
    }

    private void fillWithData() {
        number.setText(item.getAsteriskNumber());
        asteriskHost.setText(item.getAsteriskHost());
        asteriskPort.setText(item.getAsteriskPort());

        LoadingPanel.loading(false);
    }

    private void save() {
        if (!validate(false)) {
            return;
        }
        item.setAsteriskNumber(number.getText());
        item.setAsteriskHost(asteriskHost.getText());
        item.setAsteriskPort(asteriskPort.getText());

        LoadingPanel.loading(true);
        service.save(item, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                closeTab();
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), settingsStrings.asteriskAccounts()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ASTERISK_SETTINGS_ADD_EDIT, result, AddAsteriskSettingView.this);
            }
        });
    }

    private boolean validate(boolean forCheckLimit) {
        int errors = 0;
        if (isEmpty(number.getText())) {
            number.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(asteriskHost.getText())) {
            asteriskHost.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(asteriskPort.getText())) {
            asteriskPort.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(number.getText())) {
            number.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ASTERISK_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
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
