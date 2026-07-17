package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 12/12/12
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddAsteriskEmployeeView extends CustomForm2 implements Colapse {
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final String FIELD_KEY = "Key";
    private static final String FIELD_VALUE = "Value";
    private static final String[] OUR_SMS_ERROR_CODES = {"101", "102", "103", "104", "111", "113", "114", "115", "116", "117"};
    private static final Set<String> OUR_SMS_ERROR_CODES_SET = new HashSet<>(Arrays.asList(OUR_SMS_ERROR_CODES));

    private Integer objectID;
    private Integer employeeId;
    private Integer asteriskSettingsId;
    private AsteriskSettings item = new AsteriskSettings();

    private HTML employeeCode, firstname, lastname, email, phone, department, position;
    private TextBox asteriskUsername;
    private TextBox asteriskPassword;

    public AddAsteriskEmployeeView(Integer employeeId, Integer asteriskSettingsId) {
        super("asteriskEmployeeSettings", settingsStrings.addAsteriskAccount());
        this.employeeId = employeeId;
        this.asteriskSettingsId = asteriskSettingsId;
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

        employeeCode = new HTML();
        employeeCode.addStyleName(DEFAULT_WIDTH);
        employeeCode.ensureDebugId("asteriskEmplyee_employeeCode");

        firstname = new HTML();
        firstname.addStyleName(DEFAULT_WIDTH);
        firstname.ensureDebugId("asteriskEmplyee_firstname");

        lastname = new HTML();
        lastname.addStyleName(DEFAULT_WIDTH);
        lastname.ensureDebugId("asteriskEmplyee_lastname");

        email = new HTML();
        email.addStyleName(DEFAULT_WIDTH);
        email.ensureDebugId("asteriskEmplyee_email");

        phone = new HTML();
        phone.addStyleName(DEFAULT_WIDTH);
        phone.ensureDebugId("asteriskEmplyee_phone");

        department = new HTML();
        department.addStyleName(DEFAULT_WIDTH);
        department.ensureDebugId("asteriskEmployee_department");

        position = new HTML();
        position.addStyleName(DEFAULT_WIDTH);
        position.ensureDebugId("asteriskEmployee_position");

        asteriskUsername = new TextBox();
        asteriskUsername.addStyleName(DEFAULT_WIDTH);
        asteriskPassword = new TextBox();
        asteriskPassword.addStyleName(DEFAULT_WIDTH);

    }

    private void addFields() {

        addTitleField(CustomFormConstants.EMPLOYEE_INFORMATION, wfmStrings.personalInformation());
        addField(CustomFormConstants.EMPLOYEE_CODE, employeeCode, getTitle(wfmStrings.employeeCode()));
        addField(CustomFormConstants.FIRST_NAME, firstname, getTitle(wfmStrings.firstName()));
        addField(CustomFormConstants.LAST_NAME, lastname, getTitle(wfmStrings.lastName()));
        addField(CustomFormConstants.EMAIL, email, getTitle(wfmStrings.email()));
        addField(CustomFormConstants.PHONE, phone, getTitle(wfmStrings.phone()));
        addField(CustomFormConstants.DEPARTMENT, department, getTitle(wfmStrings.department()));
        addField(CustomFormConstants.POSITION, position, getTitle(wfmStrings.position()));


        addTitleField(CustomFormConstants.ASTERISK_SETTINGS, Property.get(Constants.Asterisk, wfmStrings.contactDetails(), wfmStrings.asterisk()));
        addField(CustomFormConstants.ASTERISK_USERNAME, asteriskUsername, "Username:");// <em class=\"redTitle\">*</em>
        addField(CustomFormConstants.ASTERISK_PASSWORD, asteriskPassword, "Password:");/* <em class=\"redTitle\">*</em>*/
        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), (ClickHandler) event -> save());
//        addButton(wfmStrings.close(), WfmButton2.BTN_DEFAULT, (ClickHandler) event -> closeTab());
    }

    @Override
    protected void getDataToFillFields() {
        profileService.getAsteriskSettings(employeeId, asteriskSettingsId, new AsyncCallback<AsteriskSettings>() {
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

        firstname.setHTML(item.getUser().getFirstName());
        lastname.setHTML(item.getUser().getLastName());
        employeeCode.setHTML("" + item.getUser().getEmployeeNumber());
        email.setHTML(item.getUser().getEmail());
        phone.setHTML(item.getUser().getPhoneNumber());
        department.setHTML(item.getUser().getDepartment());
        position.setHTML(item.getUser().getPosition());

        asteriskUsername.setText(item.getAsteriskUsername());
        asteriskPassword.setText(item.getAsteriskPassword());

        LoadingPanel.loading(false);
    }

    private void save() {
        /*if (!validate(false)) {
            return;
        }*/
        item.setUserId(employeeId);
        item.setAsteriskSettingsId(asteriskSettingsId);
        item.setAsteriskUsername(asteriskUsername.getValue());
        item.setAsteriskPassword(asteriskPassword.getValue());

        LoadingPanel.loading(true);
        profileService.saveEmployeeAsteriskSettings(item, true, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                closeTab("asteriskEmployeeList/" + asteriskSettingsId);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.asterisk()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ASTERISK_EMPLOYEE_ADD_EDIT, result, AddAsteriskEmployeeView.this);
            }
        });
    }

    private boolean validate(boolean forCheckLimit) {
        int errors = 0;

        if (isEmpty(asteriskUsername.getText())) {
            asteriskUsername.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (isEmpty(asteriskPassword.getText())) {
            asteriskPassword.addStyleName(Constants.ERROR_FORM_STYLE);
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
        return LayoutRPC.ASTERISK_EMPLOYEE_FORM;
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
