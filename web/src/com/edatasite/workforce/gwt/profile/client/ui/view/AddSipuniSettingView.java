package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.SipuniSettings;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioService;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddSipuniSettingView  extends CustomForm2 implements Colapse {
    private static final TwilioServiceAsync service = TwilioService.App.get();
    private static final String[] OUR_SMS_ERROR_CODES = {"101", "102", "103", "104", "111", "113", "114", "115", "116", "117"};

    private Integer objectID;
    private SipuniSettings item = new SipuniSettings();

    private TextBox operatorNumber;
    private TextBox sipNumber;
    private TextBox secretKey;
    private EmployeeLookUpWithCode employeeLookUpWithCode;


    public AddSipuniSettingView(Integer objectID) {
        super("sipuniSettings", wfmStrings.add());
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
    operatorNumber = new TextBox();
    sipNumber = new TextBox();
    secretKey = new TextBox();
    employeeLookUpWithCode = new EmployeeLookUpWithCode();


    }

    private void addFields() {
        addTitleField(BACKEND.TWILIO.PROVIDER_INFORMATION, wfmStrings.provider());
        addField("OPERATOR",operatorNumber,"operator number");
        addField("SIP_NUMBER",sipNumber,"sip number");
        addField("SECRET_KEY",secretKey,"secret key");
        addField("USER",employeeLookUpWithCode,wfmStrings.employee());
        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), (ClickHandler) event -> save());
//        addButton(wfmStrings.close(), WfmButton2.BTN_DEFAULT, (ClickHandler) event -> closeTab());
    }

    @Override
    protected void getDataToFillFields() {
        service.getSipuniSettings(objectID, new AsyncCallback<SipuniSettings>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SipuniSettings result) {
                item = result == null ? item : result;
                fillWithData();
            }
        });
    }

    private void fillWithData() {
        operatorNumber.setText(item.getOperatorNumber());
        sipNumber.setText(item.getSipNumber());
        secretKey.setText(item.getSecretKey());
        employeeLookUpWithCode.setSelected(item.getOperator());

        LoadingPanel.loading(false);
    }

    private void save() {
        if (!validate(false)) {
            return;
        }
       item.setOperator(employeeLookUpWithCode.getSelectedItem());
       item.setSipNumber(sipNumber.getValue());
       item.setSecretKey(secretKey.getValue());
       item.setOperatorNumber(operatorNumber.getValue());

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
                Info.show(wfmStrings.messSuccessfullySaved(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ASTERISK_SETTINGS_ADD_EDIT, result, AddSipuniSettingView.this);
            }
        });
    }

    private boolean validate(boolean forCheckLimit) {
        int errors = 0;
        if (isEmpty(operatorNumber.getValue())) {
            operatorNumber.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(sipNumber.getValue())) {
            sipNumber.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(employeeLookUpWithCode.getSelectedItem())) {
            employeeLookUpWithCode.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(secretKey.getValue())) {
            secretKey.addStyleName(Constants.ERROR_FORM_STYLE);
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
        return LayoutRPC.SIPUNI_SETTINGS_FORM;
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
