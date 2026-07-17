package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: romeo
 * Date: 5/24/12
 * Time: 2:33 AM
 */
public class EditGoalForm extends GoalAddEditView2 implements Colapse, Constants {

    public EditGoalForm(Integer objectId, String[] params) {
        super("editgoal", hrmsStrings.editGoal());
        this.type = params[1];
        if (PERSONAL_GOAL.equals(type)) {
            isPersonGoal = true;
            this.viewName = Property.get(Constants.PERSONAL_GOAL, hrmsStrings.personalGoal());
            folderType = F_PERS_GOAL;
        } else if (PROJECT_GOAL.equals(type)) {
            isProjectGoal = true;
            this.viewName = Property.get(PROJECT_GOAL, hrmsStrings.projectgoal());
            folderType = F_PROJ_GOAL;
        } else if (BUSINESS_GOAL.equals(type)) {
            isBusinessGoal = true;
            this.viewName = hrmsStrings.businessGoal();
            folderType = F_BUSS_GOAL;
        }
        setDescription(wfmStrings.edit() + "&nbsp" + viewName);
        this.objectId = objectId;
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
