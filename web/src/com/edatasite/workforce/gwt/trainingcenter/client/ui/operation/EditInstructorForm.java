package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 8/6/12
 * Time: 6:02 PM
 */
public class EditInstructorForm extends AddInstructorFrom implements Colapse {


    public EditInstructorForm(Integer objectID) {
        super("editInstructor", tcStrings.editInstructorView(), "instructor_edit_view_", objectID);
    }

    @Override
    public String getIconStyle() {
        return super.getIconStyle();
    }

    @Override
    protected void addButtons() {
        //update
        saveAndCloseButton = addButton(wfmStrings.update(), null, "instructor_edit_view_update_button", (ClickHandler) event -> {
            //update logic
            save(true);
        });
    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected void registerFields() {
        super.registerFields();
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