package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.NewGlobalEmployeeSummaryView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Hurshid on 12/11/2015.
 */
public class PMNewEmployeeSummaryView extends NewGlobalEmployeeSummaryView {
    private Integer employeeID;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public PMNewEmployeeSummaryView(Integer employeeId) {
        super(employeeId, "summary", wfmStrings.employeeSummary(), FROM_PM_EMPLOYEE_VIEW);
        if (employeeId != null) {
            this.employeeID = employeeId;
        } else {
            this.employeeID = Utils.getUserID();
        }
    }

    @Override
    public String getIconStyle() {
        return "bgMark icon-employee";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PM_EMPLOYEE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    protected void registerFields() {

        super.registerFields();

        drawEmployeeInformation();
        drawContactDetails();
        drawAddressInformation();
        String showEmployementInf = Utils.getUserID().equals(employeeID) ? PermissionConstants.PM_SHOW_OWN_EMPLOYMENT_INFORMATION : PermissionConstants.PM_SHOW_EMPLOYMENT_INFORMATION;
        if (Utils.hasPermission(showEmployementInf)) {
            drawEmploymentInformation();
        }
        drawAccountInformation();

        show();
    }

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
