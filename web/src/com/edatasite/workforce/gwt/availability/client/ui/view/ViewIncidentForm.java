package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.ui.view.ViewPerformanceNoteForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 10/11/12
 * Time: 2:03 PM
 */
public class ViewIncidentForm extends ViewPerformanceNoteForm implements Colapse {
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final Integer int_objectID;
    private GeneralFileUpload uploadForm;

    public ViewIncidentForm(Integer int_objectID) {
        super("summary", "Incident Summary", "summary_incident_view_", int_objectID);
        this.int_objectID = int_objectID;
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
        return LayoutRPC.INCIDENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
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
        //incident attachment
        uploadForm = new GeneralFileUpload(F_INCIDENT, int_objectID, int_objectID);
        uploadForm.ensureDebugId("summary_incident_view__attachment");
    }

    @Override
    protected void initializeForms() {
        ////////////////////////////////////////////////////////////////////
        //add field items

        //incident details -> 1
        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.incidentDetails());
        addField(CustomFormConstants.NAME, performance_note_name, getTitle(wfmStrings.name()));
        addField(CustomFormConstants.DESCRIPTION, performance_note_description, getTitle(wfmStrings.description()));
        addField(CustomFormConstants.RELATED_EMPLOYEES, performance_note_related_to, getTitle(wfmStrings.relatedEmployee()));
        addField(CustomFormConstants.VISIBILITY, performance_note_visibility, getTitle(wfmStrings.visibility()));
        addField(CustomFormConstants.PERIOD, performance_note_period, getTitle(wfmStrings.period()));
        addField(CustomFormConstants.STATUS, performance_note_status, getTitle(wfmStrings.status()));
        addField(CustomFormConstants.PRIORITY, priority, getTitle(wfmStrings.priority()));
        addField(CustomFormConstants.REPORTED_BY, performance_note_reported_by, getTitle(wfmStrings.reportedBy()));
        addField(CustomFormConstants.RESOLVER, performance_note_resolver, getTitle(wfmStrings.resolverOwner()));
        //attachments -> 2
        addField(CustomFormConstants.ATTACHMENTS, uploadForm, wfmStrings.attachments(), true);
        show();
    }

    @Override
    protected boolean isIncident() {
        return true;
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
