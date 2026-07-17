package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.GeneralEmployeeEditForm;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;

/**
 * User: Ilhombek
 * Date: 7/30/11
 * Time: 3:21 PM
 */
public class PMEmployeeEditForm extends GeneralEmployeeEditForm {

    public PMEmployeeEditForm(Integer employeeId) {
        super(employeeId, "edit", wfmStrings.editEmployee(), "employee_edit_view_", FROM_PM);
    }

    @Override
    public String getIconStyle() {
        return "bgMark employee-edit";
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
        return LayoutRPC.PM_EMPLOYEE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected void registerFields() {
        super.registerFields();

        if (container != null) {
            setCollapse(true);
        }
        //employee information
        drawEmployeeInformation();
        //contact details
        drawContactDetails();
        //address information
        drawAddressInformation();
        //employment information
        String showEmployementInf = Utils.getUserID().equals(employeeID) ? PermissionConstants.PM_SHOW_OWN_EMPLOYMENT_INFORMATION : PermissionConstants.PM_SHOW_EMPLOYMENT_INFORMATION;
        if (Utils.hasPermission(showEmployementInf)) {
            drawEmploymentInformation();
        }
        //account information
        if (Utils.hasPermission(PermissionConstants.PM_SHOW_EMPLOYEE_ROLE_WIDGET)) {
            drawAccountInformation();
        }
        show();
    }

    @Override
    protected String getWikiCode() {
        return null;
    }
}