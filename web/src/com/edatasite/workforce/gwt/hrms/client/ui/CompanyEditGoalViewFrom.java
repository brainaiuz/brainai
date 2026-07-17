package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: romeo
 * Date: 5/25/12
 * Time: 8:50 PM
 */
public class CompanyEditGoalViewFrom extends CompanyGoalAddEditView2 implements Colapse, Constants {

    public CompanyEditGoalViewFrom(Integer objectId) {
        super("editcompanygoal", hrmsStrings.editCompanyGoal());
        this.type = CustomFormConstants.COMPANY_GOAL;
        setDescription(hrmsStrings.editCompanyGoal());
        this.objectId = objectId;
        this.viewName = hrmsStrings.companyGoal();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    public void initialize() {
        super.initialize();
        LoadingPanel.loading(true);
    }

    @Override
    public void fillFieldWithValue() {
        super.fillFieldWithValue();
    }

    public void addFieldsToForm() {
        super.addFieldsToForm();
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
