package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 10/9/12
 * Time: 5:08 PM
 */
public class ViewPerformanceNoteForm extends AddPerformanceNoteView implements Colapse {

    private final Integer int_objectID;

    protected HTML performance_note_name, performance_note_related_to, performance_note_period, performance_note_status, priority, performance_note_reported_by, performance_note_resolver, performance_note_visibility;

    private String test_code_ID_name = "summary_performance_note_view_";


    public ViewPerformanceNoteForm(Integer int_objectID) {
        super("summary", "Performance Notes Summary", "summary_performance_note_view_", int_objectID);
        this.int_objectID = int_objectID;
    }

    public ViewPerformanceNoteForm(String name, String description, String test_code_ID_name, Integer int_objectID) {
        super(name, description, test_code_ID_name, int_objectID);
        this.test_code_ID_name = test_code_ID_name;
        this.int_objectID = int_objectID;
    }

    @Override
    public String getIconStyle() {
        return "issues issue-list";
    }

    @Override
    protected void addButtons() {

        //delete button
        addRemoveButton().addClickHandler(event -> {
            //register delete logic
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmStrings.sureYouWantToDelete());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    HrmsService.App.get().deletePerformanceNote(performance_note_item.getObjectID(), new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INCIDENT_DELETE, result, ViewPerformanceNoteForm.this);
                            closeTab();
                            Info.show((isIncident() ? wfmStrings.incident() : Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.performanceNote())), Info.Type.INFO);
                        }
                    });
                }
            });
            messageBox.open();
        });

        //edit Button
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_INCIDENT)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> goTo("incident|edit/" + int_objectID, performance_note_item.getName()));
        }
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
        return LayoutRPC.VIEW;
    }

    @Override
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    protected void fillFormWithData() {
        performance_note_name.setHTML(performance_note_item.getName() != null ? performance_note_item.getName() : "");
        //performance note description
        performance_note_description.setText(performance_note_item.getDescription() != null ? performance_note_item.getDescription() : "");
        //performance note related To
        if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE)) {
            performance_note_related_to.setHTML("<a href=\"#employeeProfile%7CemployeeProfileView/" + performance_note_item.getRelatedToID() + "\">" + performance_note_item.getRelatedToName() + "</a>");
        } else {
            performance_note_related_to.setHTML(performance_note_item.getRelatedToName() != null ? performance_note_item.getRelatedToName() : "");
        }
        //performance note reported by
        if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE)) {
            performance_note_reported_by.setHTML("<a href=\"#employeeProfile%7CemployeeProfileView/" + performance_note_item.getReportedByID() + "\">" + performance_note_item.getReportedByName() + "</a>");
        } else {
            performance_note_reported_by.setHTML(performance_note_item.getReportedByName() != null ? performance_note_item.getReportedByName() : "");
        }
        //performance note resolver
        performance_note_resolver.setHTML(performance_note_item.getResolverName() != null ? performance_note_item.getResolverName() : "");
        //performance note status
        performance_note_status.setHTML(performance_note_item.getStatusName() != null ? performance_note_item.getStatusName() : "");
        priority.setHTML(performance_note_item.getPriorityName() != null ? performance_note_item.getPriorityName() : "");
        //performance note period
        performance_note_period.setHTML(DateUtils.format(performance_note_item.getStartDate()) + " - " + DateUtils.format(performance_note_item.getEndDate()));
        //performance note visibility
        performance_note_visibility.setHTML(performance_note_item.isPublic() ? wfmStrings.pub() : wfmStrings.priv());
    }

    @Override
    protected void initialize() {
        //performance note name
        performance_note_name = new HTML();
        performance_note_name.ensureDebugId(test_code_ID_name + "name");
        //performance note description
        performance_note_description = new TextArea2(3000);
        performance_note_description.setReadOnly(true);
        performance_note_description.hideCharacterLimitPanel();
        performance_note_description.ensureDebugId(test_code_ID_name + "description");
        //performance note related to employee
        performance_note_related_to = new HTML();
        performance_note_related_to.ensureDebugId(test_code_ID_name + "related_employee");
        //performance note visibility
        performance_note_visibility = new HTML(wfmStrings.pub());
        performance_note_visibility.ensureDebugId(test_code_ID_name + "visibility");
        //performance note period date From/To
        performance_note_period = new HTML();
        performance_note_period.ensureDebugId(test_code_ID_name + "period");
        //performance note status
        performance_note_status = new HTML();
        performance_note_status.ensureDebugId(test_code_ID_name + "status");

        priority = new HTML();
        priority.ensureDebugId(test_code_ID_name + "priority");

        //performance note reporter
        performance_note_reported_by = new HTML();
        performance_note_reported_by.ensureDebugId(test_code_ID_name + "reporter");
        //performance note resolver/owner
        performance_note_resolver = new HTML();
        performance_note_resolver.ensureDebugId(test_code_ID_name + "resolver");
    }

    @Override
    protected void initializeForms() {
        ////////////////////////////////////////////////////////////////////
        //add field items

        //performance note details -> 1
        addTitleField(CustomFormConstants.DETAILS, "Performance Note Details");
        addField(CustomFormConstants.NAME, performance_note_name, getTitle(hrmsStrings.noteName()));
        addField(CustomFormConstants.DESCRIPTION, performance_note_description, getTitle(hrmsStrings.noteDescription()));
        addField(CustomFormConstants.RELATED_EMPLOYEES, performance_note_related_to, getTitle(wfmStrings.relatedEmployee()));
        addField(CustomFormConstants.VISIBILITY, performance_note_visibility, getTitle(wfmStrings.visibility()));
        addField(CustomFormConstants.PERIOD, performance_note_period, getTitle(hrmsStrings.notePeriod()));
        addField(CustomFormConstants.STATUS, performance_note_status, getTitle(hrmsStrings.noteStatus()));
        addField(CustomFormConstants.REPORTED_BY, performance_note_reported_by, getTitle(wfmStrings.reportedBy()));
        addField(CustomFormConstants.RESOLVER, performance_note_resolver, getTitle(wfmStrings.resolverOwner()));

        show();
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
