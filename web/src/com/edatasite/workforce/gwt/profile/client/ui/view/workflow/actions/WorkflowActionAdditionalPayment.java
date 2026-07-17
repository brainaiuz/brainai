package com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAction;
import com.google.gwt.user.client.ui.HTML;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.WorkflowActionConstants.ADDITIONAL_PAYMENT;

/**
 * Created by shohruh on 26-Mar-17.
 */
public class WorkflowActionAdditionalPayment extends AbstractWorkflowAction implements Constants.WorkflowActionConstants.AdditionalPayment {
    private boolean isPayment = true;
    int r;

    public WorkflowActionAdditionalPayment(WorkflowAction action) {
        super();
        setCellSpacing(10);
        setCellPadding(10);
        getElement().setAttribute("style", "border-spacing:10px;border-collapse:separate");

        this.action = action;
        isPayment = ADDITIONAL_PAYMENT == action.getActionType();
        fieldsMap = action.getFieldsAsMap();
        SelectItem[] fields = getColumnsAsReferenceItems(action.getFields());

        map = action.getItemsAsMap();

        r = 0;
        setWidget(r, 0, new HTML(""));
        setWidget(r, 1, new HTML(wfmStrings.customFields()));
        setWidget(r, 2, new HTML(wfmStrings.defaultValue()));
        addWidget(wfmStrings.reference(), fields, FIELD_REFERENCE, map.get(FIELD_REFERENCE), TEXT, true);
        addWidget(wfmStrings.category(), fields, FIELD_CATEGORY, map.get(FIELD_CATEGORY), isPayment ? PAYROLL_CATEGORY_PAYMENT : PAYROLL_CATEGORY_DEDUCTION, true);
        addWidget(wfmStrings.month(), fields, FIELD_MONTH, map.get(FIELD_MONTH), TEXT, true);
        addWidget(wfmStrings.year(), fields, FIELD_YEAR, map.get(FIELD_YEAR), TEXT, true);
        addWidget(wfmStrings.approver(), fields, FIELD_APPROVER, map.get(FIELD_APPROVER), APPROVER_LOOKUP, true, PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP);
        addWidget(wfmStrings.status(), fields, FIELD_STATUS, map.get(FIELD_STATUS), TEXT, true);
        addWidget(wfmStrings.paymentAmount(), fields, FIELD_PAYMENT_AMOUNT, map.get(FIELD_PAYMENT_AMOUNT), NUMERIC, true);
    }

    public boolean validate() {
        return true;
    }

    public String getActionName() {
        return isPayment ? wfmStrings.additionalPayment() : payrollStrings.additionalDeduction();
    }
}
