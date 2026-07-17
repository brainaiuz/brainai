package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;

/**
 * User: Administrator
 * Date: May 31, 2010
 * Time: 2:49:29 PM
 */
public class HrmsEmployeeEditForm extends GeneralEmployeeEditForm {

    public HrmsEmployeeEditForm(Integer employeeId) {
        super(employeeId);
    }

    @Override
    protected void addButtons() {
        super.addButtons();
    }

    @Override
    protected void fillFormWithData() {
        super.fillFormWithData();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.HRMS_EMPLOYEE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    protected void registerFields() {

        super.registerFields();

        if (Utils.isSettings()) {
            drawEmployeeInformation();
            drawContactDetails();
            drawEmploymentInformation();
            if (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET)) {
                drawAccountInformation();
            }
        } else {
            //employee information
            drawEmployeeInformation();
            //contact details
            drawContactDetails();
            //address information
            drawAddressInformation();
            //employment information
            String showEmployementInf = Utils.getUserID().equals(employeeID) ? PermissionConstants.HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION : PermissionConstants.HRMS_SHOW_EMPLOYMENT_INFORMATION;
            if (Utils.hasPermission(showEmployementInf)) {
                drawEmploymentInformation();
            }
            //bank information

            if ((employeeID != null && employeeID.equals(Utils.getUserID())) || Utils.hasPermission(PermissionConstants.SHOW_EMPLOYEE_BANK_DETAILS)) {
                drawBankInformation();
            }
            //account information
            if (Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ROLE_WIDGET)) {
                drawAccountInformation();
            }
            //Personal Identity Information
            if ((employeeID != null && employeeID.equals(Utils.getUserID())) || Utils.hasPermission(PermissionConstants.SHOW_EMPLOYEE_PERSONAL_INFORMATION)) {
                drawPersonalIdentityInformation();
            }
            //attachments
            if ((employeeID != null && employeeID.equals(Utils.getUserID())) || Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ATTACHMENT)) {
                drawAttachments();
            }
            drawPaymentDeductionCategoryTable();
            //custom fields
//        drawCustomFields();
        }
        show();
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}