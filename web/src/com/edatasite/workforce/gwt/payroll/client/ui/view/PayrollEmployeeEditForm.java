package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 11/30/12
 * Time: 2:02 PM
 */
public class PayrollEmployeeEditForm extends PayrollEmployeeAddForm  {

    public PayrollEmployeeEditForm(Integer int_employeeID) {
        super("edit", wfmStrings.editEmployee(), "edit_starter_view_", int_employeeID);
    }

    public PayrollEmployeeEditForm(Integer employeeTemplateID, boolean fromTemplate) {
        super("edit", wfmStrings.editEmployee(), "edit_starter_view_", employeeTemplateID, fromTemplate);
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
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return super.getWikiCode();
    }

    @Override
    protected void fillFormWithData() {
        super.fillFormWithData();
    }


    @Override
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}