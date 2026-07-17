package com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAction;
import com.google.gwt.user.client.ui.HTML;

/**
 * Created by shohruh on 25-Mar-17.
 */
public class WorkflowActionCashAdvance extends AbstractWorkflowAction implements Constants.WorkflowActionConstants.CashAdvance {
    int r;

    public WorkflowActionCashAdvance(WorkflowAction action) {
        super();
        setCellSpacing(10);
        setCellPadding(10);
        getElement().setAttribute("style", "border-spacing:10px;border-collapse:separate");

        this.action = action;
        fieldsMap = action.getFieldsAsMap();
        SelectItem[] fields = getColumnsAsReferenceItems(action.getFields());

        map = action.getItemsAsMap();

        r = 0;
        setWidget(r, 0, new HTML(""));
        setWidget(r, 1, new HTML(wfmStrings.customFields()));
        setWidget(r, 2, new HTML(wfmStrings.defaultValue()));
        addWidget(wfmStrings.date(), fields, FIELD_DATE, map.get(FIELD_DATE), DATE, true);
        addWidget(wfmStrings.status(), fields, FIELD_STATUS, map.get(FIELD_STATUS), TEXT, true);
        addWidget(wfmStrings.category(), fields, FIELD_CATEGORY, map.get(FIELD_CATEGORY), CASH_ADVANCE_CATEGORY, true);
        addWidget(wfmStrings.requestedAmount(), fields, FIELD_REQUESTED_AMOUNT, map.get(FIELD_REQUESTED_AMOUNT), NUMERIC, true);
        addWidget(wfmStrings.paymentTerms(), fields, FIELD_PAYMENT_TERMS, map.get(FIELD_PAYMENT_TERMS), TEXT, true);
        addWidget(wfmStrings.paymentAmount(), fields, FIELD_PAYMENT_AMOUNT, map.get(FIELD_PAYMENT_AMOUNT), NUMERIC, true);
        addWidget(wfmStrings.paymentMethod(), fields, FIELD_PAYMENT_METHOD, map.get(FIELD_PAYMENT_METHOD), TEXT, true);
        addWidget(wfmStrings.approver(), fields, FIELD_APPROVER, map.get(FIELD_APPROVER), APPROVER_LOOKUP, true, PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP);
        addWidget(wfmStrings.purpose(), fields, FIELD_PURPOSE, map.get(FIELD_PURPOSE), TEXT, false);
        addWidget(wfmStrings.paidFrom(), fields, FIELD_FROM_ACCOUNT, map.get(FIELD_FROM_ACCOUNT), ACCOUNT_LOOKUP, true);
        addWidget(Property.get(Constants.CASH_ADVANCE_LIST, wfmStrings.cashAdvanceAccount(), wfmStrings.cashAdvance()), fields, FIELD_ACCOUNT, map.get(FIELD_ACCOUNT), ACCOUNT_LOOKUP, true);
    }

    public boolean validate() {
        return true;
    }

    public String getActionName() {
        return wfmStrings.cashAdvance();
    }
}
