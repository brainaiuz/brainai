package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 10/11/12
 * Time: 2:02 PM
 */
public class EditIncidentForm extends AddIncidentView {

    public EditIncidentForm(Integer int_objectID) {
        super("edit", hrmsStrings.editIncident(), "edit_incident_view_", int_objectID);
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
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    protected void fillFormWithData() {
        super.fillFormWithData();
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void initializeForms() {
        super.initializeForms();
    }

    @Override
    protected boolean isIncident() {
        return super.isIncident();
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