package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.contact.client.ui.GeneralEmployeeEditForm;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 8/6/12
 * Time: 6:02 PM
 */
public class AddInstructorFrom extends GeneralEmployeeEditForm implements Constants {

    public static final TCStrings tcStrings = TCStrings.App.get();

    private Integer instructorID;

    public AddInstructorFrom() {
        super(null, "addInstructor", tcStrings.addInstructorView(), "instructor_add_view_", FROM_TC_INSTRUCTOR);
    }

    public AddInstructorFrom(String name, String description, String test_code_ID_name, Integer objectID) {
        super(objectID, name, description, test_code_ID_name, FROM_TC_INSTRUCTOR);
        this.instructorID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        super.registerFields();
        //instructor details
        drawEmployeeInformation();
        //contact details
        drawContactDetails();
        //address information
        drawAddressInformation();
        //courses
        drawCourses();
        //attachments
        drawAttachments();
        //custom fields
        drawCustomFields();

        show();
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected void addButtons() {
        //save & close
        saveAndCloseButton = addButton(wfmStrings.save(), null, "instructor_add_view_save_and_close_button", event -> {
            //save & close logic
            save(true);
        });
        //save & new
        saveButton = addButton(wfmStrings.saveAndNew(), null, "instructor_add_view_save_and_new_button", event -> {
            //save & new logic
            save(false);
        });

    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.INSTRUCTOR_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
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