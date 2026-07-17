package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioService;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioSettings;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
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
public class AddTwilioSettingView extends CustomForm2 implements Colapse {
    private static final TwilioServiceAsync service = TwilioService.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final String FIELD_KEY = "Key";
    private static final String FIELD_VALUE = "Value";
    private static final String[] OUR_SMS_ERROR_CODES = {"101", "102", "103", "104", "111", "113", "114", "115", "116", "117"};
    private static final Set<String> OUR_SMS_ERROR_CODES_SET = new HashSet<>(Arrays.asList(OUR_SMS_ERROR_CODES));

    private Integer objectID;
    private TwilioSettings item = new TwilioSettings();
    private TextBox number;
    private TextBox accountSID;
    private TextBox authToken;
    private TextBox applicationSID;
    private KpiSwitcher record;
    private HTML link;

    public AddTwilioSettingView(Integer objectID) {
        super("twilioSettings", settingsStrings.addTwilioAccount());
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
        number.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                getLinkForTwilio();
            }
        });
        accountSID = new TextBox();
        accountSID.addStyleName(Constants.DEFAULT_WIDTH);
        authToken = new TextBox();
        authToken.addStyleName(Constants.DEFAULT_WIDTH);
        applicationSID = new TextBox();
        applicationSID.addStyleName(Constants.DEFAULT_WIDTH);
        record = new KpiSwitcher();
        link = new HTML();
    }

    private void addFields() {
        addTitleField(BACKEND.TWILIO.PROVIDER_INFORMATION, settingsStrings.providerInformation());
        addField(BACKEND.TWILIO.NUMBER, new AdvancedInputGroup(new HTML("+"), number, null, true, false), wfmStrings.number() + "<em class=\"redTitle\">*</em>:");
        addField(BACKEND.TWILIO.ACCOUNT_SID, accountSID, settingsStrings.accountSID() + "<em class=\"redTitle\">*</em>:");
        addField(BACKEND.TWILIO.AUTH_TOKEN, authToken, wfmStrings.accessToken() + "<em class=\"redTitle\">*</em>:");
        addField(BACKEND.TWILIO.APPLICATION_SID, applicationSID, settingsStrings.applicationSID() + "<em class=\"redTitle\">*</em>:");
        addField(BACKEND.TWILIO.RECORD, record, settingsStrings.recordCalls());
        addField(BACKEND.TWILIO.LINK, link, wfmStrings.links());
        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), (ClickHandler) event -> save());
//        addButton(wfmStrings.close(), WfmButton2.BTN_DEFAULT, (ClickHandler) event -> closeTab());
    }

    @Override
    protected void getDataToFillFields() {
        service.get(objectID, new AsyncCallback<TwilioSettings>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(TwilioSettings result) {
                item = result == null ? item : result;
                fillWithData();
            }
        });
    }

    private void fillWithData() {
        number.setText(item.getNumberWithoutPlus());
        accountSID.setText(item.getAccountSid());
        authToken.setText(item.getAuthToken());
        applicationSID.setText(item.getApplicationSid());
        record.setValue(item.isRecord());
        getLinkForTwilio();
        LoadingPanel.loading(false);
    }

    private void save() {
        if (!validate(false)) {
            return;
        }
        item.setNumber("+"+number.getText());
        item.setAccountSid(accountSID.getText());
        item.setAuthToken(authToken.getText());
        item.setApplicationSid(applicationSID.getText());
        item.setRecord(record.getValue() != null &&record.getValue());
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
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), settingsStrings.twilioAccounts()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TWILIO_SETTINGS_ADD_EDIT, result, AddTwilioSettingView.this);
            }
        });
    }

    private boolean validate(boolean forCheckLimit) {
        int errors = 0;
        if (isEmpty(number.getText())) {
            number.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(accountSID.getText())) {
            accountSID.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(applicationSID.getText())) {
            applicationSID.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(authToken.getText())) {
            authToken.addStyleName(Constants.ERROR_FORM_STYLE);
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
        return LayoutRPC.TWILIO_SETTINGS_FORM;
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

    public void getLinkForTwilio() {
        service.encrypt(number.getValue(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(String result) {
                link.setHTML(result);
            }
        });
    }
}
