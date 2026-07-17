package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.NewGlobalEmployeeSummaryView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;

/**
 * Created by Hurshid on 12/8/2015.
 */
public class NewHrmsEmployeeSummaryForm extends NewGlobalEmployeeSummaryView {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private final Integer employeeId;

    public NewHrmsEmployeeSummaryForm(Integer employeeId, boolean isEmployeeList, String type) {
        super(employeeId, EMPLOYEE_PROFILE_VIEW, wfmStrings.employeeProfile(), FROM_HRMS_EMPLOYEE_VIEW);
        this.employeeId = employeeId;
        this.isEmployeeList = isEmployeeList;
        filterParameter.setObjectId(employeeId);
        filterParameter.setViewType(type);
        filterParameter.setHasAccessToChange(Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_EDIT : PermissionConstants.HRMS_EDIT_PROFILE));
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
        return LayoutRPC.VIEW;
    }

    protected void registerFields() {
        super.registerFields();
        if (Utils.isSettings()) {
            drawEmployeeInformation();
            drawContactDetails();
            String showEmployementInf = Utils.getUserID().equals(employeeId) ? PermissionConstants.HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION : PermissionConstants.HRMS_SHOW_EMPLOYMENT_INFORMATION;
            drawEmploymentInformation();
//            if (Utils.hasPermission(showEmployementInf)) {
//            }
            drawAccountInformation();
        } else {
            //employee information
            drawEmployeeInformation();
            //contact details
            drawContactDetails();
            //address information
            drawAddressInformation();
            //employment information
            String showEmployementInf = Utils.getUserID().equals(employeeId) ? PermissionConstants.HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION : PermissionConstants.HRMS_SHOW_EMPLOYMENT_INFORMATION;
            if (Utils.hasPermission(showEmployementInf)) {
                drawEmploymentInformation();
            }
            //bank information
            if (Utils.getUserID().equals(employeeId) || Utils.hasPermission(SHOW_EMPLOYEE_BANK_DETAILS)) {
                drawBankInformation();
            }
            drawAccountInformation();

            if (Utils.getUserID().equals(employeeId) || Utils.hasPermission(PermissionConstants.SHOW_EMPLOYEE_PERSONAL_INFORMATION)) {
                drawPersonalIdentityInformation();
            }

            if (Utils.getUserID().equals(employeeId) || Utils.hasPermission(PermissionConstants.HRMS_SHOW_EMPLOYEE_ATTACHMENT)) {
                drawAttachments();
            }
            drawPaymentDeductionCategoryTable();

            drawCustomFields();

            drawItemTable();
            drawExperienceTable();
        }

        show();
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}