package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.contact.client.ui.NewGlobalEmployeeSummaryView;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Hurshid on 12/11/2015.
 */
public class InstructorSummaryView extends NewGlobalEmployeeSummaryView implements Colapse {
    private static final TCStrings tcStrings = TCStrings.App.get();
    FormHasCustomField customFieldUtil;

    public InstructorSummaryView(Integer objectID) {
        super(objectID, "summary", tcStrings.instructorSummaryView(), NewGlobalEmployeeSummaryView.FROM_TC_INSTRUCTOR_VIEW);
    }



    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        super.registerFields();
        drawEmployeeInformation();
        drawContactDetails();
        drawCustomFields();
        drawAddressInformation();
        drawAttachments();
        show();
    }
    @Override
    public String getIconStyle() {
        return super.getIconStyle();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.INSTRUCTOR_FORM;
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

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (this.customFieldUtil == null) {
            this.customFieldUtil = new FormHasCustomField();
        }
        return this.customFieldUtil;
    }
}
