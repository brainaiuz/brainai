package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.ui.view.WorkflowDateSelecter;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by Azazello on 8/7/15.
 */
public class WorkflowEmployeeStepView extends KpiModal implements Constants {
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    private Integer objectID;
    private Integer workflowID;
    private EmployeeStepItem step;
    private WfmForm main;
    private WfmForm.Field onboardingStepField;
    private WfmForm.Field statusField;
    private DataListBox onboardingStep;
    private DataListBox status;
    private KpiCheckBox workflowTimeBasedAction;
    private WorkflowDateSelecter workflowTimeBasedActionDate;
    private WfmButton2 btnSave;
    private WfmButton2 btnCancel;

    public WorkflowEmployeeStepView(Integer objectID, Integer workflowID) {
        this.objectID = objectID;
        this.workflowID = workflowID;
        init();
        addButtons();
        fillFields();
    }

    private void addButtons() {
        btnSave = new WfmButton2(objectID == null ? wfmStrings.save() : wfmStrings.update());
        btnSave.ensureDebugId("save_button");
        btnSave.addClickHandler(sender -> save());
        btnCancel = new WfmButton2(wfmStrings.cancel());
        btnCancel.ensureDebugId("save_button");
        btnCancel.addClickHandler(sender -> close());
        main.addButton(btnSave);
        main.addButton(btnCancel);
    }

    private void save() {
        if (!validate()) {
            return;
        }
        getValues();
        LoadingPanel.loading(true);
        profileService.saveWorkflowStep(step, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                close();
                Info.show(wfmMessages.workflowStepSuccesfully(objectID == null ? wfmStrings.saved() : Property.get(Constants.TASK, wfmStrings.updated(), wfmStrings.task())), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_EMPLOYEE_STEP_UPDATE, result, WorkflowEmployeeStepView.this);
            }
        });
    }

    private void getValues() {
        step.setWorkflowID(workflowID);
        step.setStepID(onboardingStep.getSelectedId());
        step.setStatusID(status.getSelectedId());
        step.setWorkflowActionTimeBased(workflowTimeBasedAction.getValue());
        step.setWorkflowActionStartTime(workflowTimeBasedActionDate.getWorkflowStartDate());
        step.setWorkflowActionStartTimeUnit(workflowTimeBasedActionDate.getWorkflowDueDateUnit());
        step.setWorkflowActionStartTimeGranularity(workflowTimeBasedActionDate.getWorkflowDueDateGranularity());
    }

    public boolean validate() {
        int error = 0;
        if (!Validation.validateListBoxRequired(onboardingStep, onboardingStepField, "")) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            onboardingStep.addStyleName(ERROR_FORM_STYLE);
            error++;
        }
//        if (!Validation.validateListBoxRequired(status, statusField, "")) {
//            Info.show("", wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
//            status.addStyleName(ERROR_FORM_STYLE);
//            error++;
//        }
        return error == 0;
    }

    private void fillFields() {
        LoadingPanel.loading(true);
        profileService.getWorkflowStep(objectID, workflowID, new AbstractAsyncCallback<EmployeeStepItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(EmployeeStepItem result) {
                LoadingPanel.loading(false);
                step = result;
                setValues();
            }
        });
    }

    private void setValues() {
        onboardingStep.setItems(step.getOnboardingSteps());
        status.setItems(step.getStatuses());
        if (step.getStepID() != null) {
            onboardingStep.setSelected(step.getStepID());
        }
        if(step.getStatusID() != null){
            status.setSelected(step.getStatusID());
        }
        workflowTimeBasedAction.setValue(step.isWorkflowActionTimeBased(), true);
        workflowTimeBasedActionDate.setStartDate(step.getWorkflowActionStartTime());
        workflowTimeBasedActionDate.setDueDate(step.getWorkflowActionStartTimeUnit());
        workflowTimeBasedActionDate.setDueDateGranularity(step.getWorkflowActionStartTimeGranularity());
    }

    private void init() {
        onboardingStep = new DataListBox();
        onboardingStep.addStyleName(DEFAULT_WIDTH);
        onboardingStep.addValueChangeHandler(changeEvent -> {
            if (onboardingStep.getSelectedItem() == null) {
                status.clear();
            } else {
                onboardingStep.removeStyleName(ERROR_FORM_STYLE);
                fillStatuses(onboardingStep.getSelectedId());
            }
        });
        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.addValueChangeHandler(changeEvent -> status.removeStyleName(ERROR_FORM_STYLE));
        workflowTimeBasedAction = new KpiCheckBox(wfmStrings.executionTime());
        workflowTimeBasedActionDate = new WorkflowDateSelecter(false, true);
        workflowTimeBasedActionDate.setVisible(false);
        workflowTimeBasedActionDate.setWidth("500px");
        workflowTimeBasedAction.addValueChangeHandler(booleanValueChangeEvent -> workflowTimeBasedActionDate.setVisible(booleanValueChangeEvent.getValue()));
        main = new WfmForm();
        onboardingStepField = main.addField("<span>" + wfmStrings.onboardingStep() + "</span>", onboardingStep, true);
        statusField = main.addField("<span>" + wfmStrings.status() + "</span>", status, true);
        VerticalPanel vPanel = new VerticalPanel();
        vPanel.add(main);
        vPanel.add(Utils.getInHorizontalPanel(5, 100, false, workflowTimeBasedAction, workflowTimeBasedActionDate));
        add(vPanel);
        setTitle(wfmStrings.addOnboardingStepWorkflow());
        center();
        open();
    }

    private void fillStatuses(Integer stepID) {
        LoadingPanel.loading(true);
        profileService.getStepStatuses(stepID, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    status.setItems(result);
                }
            }
        });
    }
}
