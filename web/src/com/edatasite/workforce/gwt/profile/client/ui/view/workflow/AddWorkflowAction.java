package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAction;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions.AbstractWorkflowAction;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions.WorkflowActionAdditionalPayment;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions.WorkflowActionCashAdvance;
import com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions.WorkflowActionCertificate;

/**
 * Created by shohruh on 22-Mar-17.
 */
public class AddWorkflowAction extends KpiModal implements Constants.WorkflowActionConstants {
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private final Integer objectId;
    private final Integer workflowId;
    private WorkflowAction action;
    private WfmButton2 saveButton;
    private AbstractWorkflowAction table;
    private final Integer type;

    public AddWorkflowAction(Integer objectId, Integer workflowId, Integer type) {
        this.objectId = objectId;
        this.workflowId = workflowId;
        this.type = type;
        setTitle(getActionTitle());
        getDataToFillFields();
    }

    private String getActionTitle() {
        String header = null;
        switch (type) {
            case CASH_ADVANCE:
                header = Property.get(Constants.CASH_ADVANCE_LIST, wfmStrings.cashAdvance());
                break;
            case ADDITIONAL_PAYMENT:
                header = Property.get(Constants.ADDITIONAL_PAYMENT_LIST, wfmStrings.additionalPayment());
                break;
            case ADDITIONAL_DEDUCTION:
                header = payrollStrings.additionalDeduction();
                break;
            case CERTIFICATE:
                header = wfmStrings.certificate();
                break;
        }
        return !Utils.isNullOrEmpty(header) ? header : wfmStrings.action();
    }

    private void initialize() {
        action.setActionType(type);
        switch (type) {
            case CASH_ADVANCE:
                table = new WorkflowActionCashAdvance(action);
                break;
            case ADDITIONAL_PAYMENT:
                table = new WorkflowActionAdditionalPayment(action);
                break;
            case ADDITIONAL_DEDUCTION:
                table = new WorkflowActionAdditionalPayment(action);
                break;
            case CERTIFICATE:
                table = new WorkflowActionCertificate(action);
                break;
        }
        add(table);

        saveButton = new WfmButton2(wfmStrings.save(),WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, clickEvent -> close()));
        addButton(saveButton);
        open();
    }

    private void getDataToFillFields() {
        profileService.editWorkflowAction(objectId, workflowId, new AbstractAsyncCallback<WorkflowAction>() {
            @Override
            public void onFailure(Throwable caught) {
                initialize();
            }

            @Override
            public void onSuccess(WorkflowAction action) {
                AddWorkflowAction.this.action = action;
                initialize();
            }
        });
    }

    private void save() {
        if (table.validate()) {
            profileService.saveWorkflowAction(table.getRPC(), new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(Integer result) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_ACTIONS_UPDATE, action, AddWorkflowAction.this);
                    close();
                }
            });
        }
    }
}
