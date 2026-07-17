package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.GeneralEmployeeEditForm;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;

/**
 * User: Ilhombek
 * Date: 8/2/11
 * Time: 12:30 PM
 */
public class SettingsEmployeeEditForm extends GeneralEmployeeEditForm {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    public SettingsEmployeeEditForm(Integer employeeId) {
        super(employeeId, Constants.HRMS_EDIT_PROFILE, wfmStrings.contactprofile(), "profile_settings_edit_view_", FROM_SETTINGS_PROFILE);
    }

    @Override
    protected void addButtons() {
        super.addButtons();
    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SETTINGS_EMPLOYEE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected void registerFields() {
        super.registerFields();
        //employee information
        drawEmployeeInformation();
        //contact details
        drawContactDetails();

        //address information
        drawAddressInformation();
        //bank information
        //attachments
        drawAttachments();

        show();
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}