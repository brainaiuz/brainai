package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.ui.view.AddPerformanceNoteView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Sherzod
 * Date: May 25, 2009
 * Time: 2:20:17 PM
 */
public class AddIncidentView extends AddPerformanceNoteView implements Constants, Colapse {

    private GeneralFileUpload uploadForm;

    public AddIncidentView() {
        this(null);
    }

    public AddIncidentView(Integer int_employeeID) {
        super("add", wfmStrings.add(), "add_incident_view_", null, int_employeeID);
    }

    public AddIncidentView(String name, String description, String test_code_ID_name, Integer int_objectID) {
        super(name, description, test_code_ID_name, int_objectID);
        this.int_objectID = int_objectID;
    }

    @Override
    public String getIconStyle() {
        return "hrms hrms-edit";
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
    protected void registerFields() {
        super.registerFields();
    }


    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        isIncindent = true;
        super.onInitialize();
        return null;
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
        uploadForm.ensureDebugId("add_incident_view_attachment");
    }

    @Override
    protected void initializeForms() {
        ////////////////////////////////////////////////////////////////////
        //
        FlexTable showToTab = new FlexTable();
        showToTab.setWidget(0, 0, performance_note_visibility_private);
        showToTab.setWidget(0, 1, performance_note_visibility_public);

        FlexTable periodTab = new FlexTable();
        KpiDatePicker startDatePicker = dateTime.getStartDatePicker();
        KpiDatePicker dueDatePicker = dateTime.getDueDatePicker();
        periodTab.setWidget(0, 1, startDatePicker);
        periodTab.setWidget(0, 3, dueDatePicker);

        //add field items

        //incident details -> 1
        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, performance_note_name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(),
                    formPropertyMap.get(CustomFormConstants.NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NAME).isInformation());
            performance_note_name.setEnabled(!formPropertyMap.get(CustomFormConstants.NAME).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NAME).isInformation()){
                new KpiToolTip(performance_note_name,formPropertyMap.get(CustomFormConstants.NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NAME, performance_note_name, getTitle(wfmStrings.name(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, performance_note_description, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(),
                    formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation());
            performance_note_description.setEnabled(!formPropertyMap.get(CustomFormConstants.DESCRIPTION).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation()){
                new KpiToolTip(performance_note_description,formPropertyMap.get(CustomFormConstants.DESCRIPTION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.DESCRIPTION, performance_note_description, wfmStrings.description());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES) != null) {
            addField(CustomFormConstants.RELATED_EMPLOYEES, performance_note_related_employee, getTitle(formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).isChanged() ? formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).getTitle() : wfmStrings.relatedEmployee(),
                    formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).isInformation());
            performance_note_related_employee.setEnabled(!formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).isInformation()){
                new KpiToolTip(performance_note_related_employee,formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).getInformationText());
            }
        } else {
            addField(CustomFormConstants.RELATED_EMPLOYEES, performance_note_related_employee, wfmStrings.relatedEmployee());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VISIBILITY) != null) {
            addField(CustomFormConstants.VISIBILITY, showToTab, getTitle(formPropertyMap.get(CustomFormConstants.VISIBILITY).isChanged() ? formPropertyMap.get(CustomFormConstants.VISIBILITY).getTitle() : wfmStrings.visibility(),
                    formPropertyMap.get(CustomFormConstants.VISIBILITY).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.VISIBILITY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.VISIBILITY).isInformation()){
                new KpiToolTip(showToTab,formPropertyMap.get(CustomFormConstants.VISIBILITY).getInformationText());
            }

        } else {
            addField(CustomFormConstants.VISIBILITY, showToTab, getTitle(wfmStrings.visibility(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PERIOD) != null) {
            addField(CustomFormConstants.PERIOD, periodTab, getTitle(formPropertyMap.get(CustomFormConstants.PERIOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PERIOD).getTitle() : wfmStrings.period(),
                    formPropertyMap.get(CustomFormConstants.PERIOD).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.PERIOD).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PERIOD).isInformation()){
                new KpiToolTip(periodTab,formPropertyMap.get(CustomFormConstants.PERIOD).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PERIOD, periodTab, getTitle(wfmStrings.period(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, performance_note_status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status(),
                    formPropertyMap.get(CustomFormConstants.STATUS).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.STATUS).isInformation());
            performance_note_status.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.STATUS).isInformation()){
                new KpiToolTip(performance_note_status,formPropertyMap.get(CustomFormConstants.STATUS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.STATUS, performance_note_status, getTitle(wfmStrings.status(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null) {
            addField(CustomFormConstants.PRIORITY, priorities, getTitle(formPropertyMap.get(CustomFormConstants.PRIORITY).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIORITY).getTitle() : wfmStrings.priority(),
                    formPropertyMap.get(CustomFormConstants.PRIORITY).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.PRIORITY).isInformation());
            priorities.setEnabled(!formPropertyMap.get(CustomFormConstants.PRIORITY).isDisabled());
            if(formPropertyMap.get(CustomFormConstants.PRIORITY).isInformation()){
                new KpiToolTip(priorities,formPropertyMap.get(CustomFormConstants.PRIORITY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PRIORITY, priorities, getTitle(wfmStrings.priority(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null) {
            addField(CustomFormConstants.REPORTED_BY, performance_note_reported_by, getTitle(formPropertyMap.get(CustomFormConstants.REPORTED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.REPORTED_BY).getTitle() : wfmStrings.reportedBy(),
                    formPropertyMap.get(CustomFormConstants.REPORTED_BY).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.REPORTED_BY).isInformation());
            performance_note_reported_by.setEnabled(!formPropertyMap.get(CustomFormConstants.REPORTED_BY).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.REPORTED_BY).isInformation()){
                new KpiToolTip(performance_note_reported_by,formPropertyMap.get(CustomFormConstants.REPORTED_BY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.REPORTED_BY, performance_note_reported_by, getTitle(wfmStrings.reportedBy()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null) {
            addField(CustomFormConstants.RESOLVER, performance_note_resolver, getTitle(formPropertyMap.get(CustomFormConstants.RESOLVER).isChanged() ? formPropertyMap.get(CustomFormConstants.RESOLVER).getTitle() : wfmStrings.resolverOwner(),
                    formPropertyMap.get(CustomFormConstants.RESOLVER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.RESOLVER).isInformation());
            performance_note_resolver.setEnabled(!formPropertyMap.get(CustomFormConstants.RESOLVER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.RESOLVER).isInformation()){
                new KpiToolTip(performance_note_resolver,formPropertyMap.get(CustomFormConstants.RESOLVER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.RESOLVER, performance_note_resolver, getTitle(wfmStrings.resolverOwner()));
        }
        addField(CustomFormConstants.ATTACHMENTS, uploadForm, null, true);

        addCustomFields();
        show();
    }

    @Override
    protected boolean isIncident() {
        return true;
    }

    @Override
    protected void save(final boolean closeTabT) {
        info_error_message = wfmStrings.errorOccurredSavingChanges();
        info_success_message = Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.incident());

        super.save(closeTabT);
    }

    @Override
    protected void setValues() {
        super.setValues();
        performance_note_item.setAttachments(uploadForm.getAttachedFiles());
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
