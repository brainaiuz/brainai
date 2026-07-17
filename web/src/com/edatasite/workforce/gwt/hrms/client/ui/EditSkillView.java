package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.assessment.client.ui.view.AddSkillView;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Abror Abdukadirov
 * Date: 10.07.2017 17:26
 */
public class EditSkillView extends AddSkillView implements Colapse {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    public EditSkillView(Integer objectId, boolean isEdit) {
        super("editskill", hrmsStrings.editSkill());
        this.objectId = objectId;
        this.isEdit = isEdit;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.COMPETENCY_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void getDataToFillFields() {
        super.getDataToFillFields();
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
