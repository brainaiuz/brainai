package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WorkflowWebHookListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 28-Feb-2014
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowAddSinksContainer extends SinksContainer {

    public WorkflowAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (id == null && params != null && params.length > 2) {
            id = Integer.parseInt(params[1]);
        }
        String relationType = params.length > 2 && !"null".equals(params[2]) ? params[2] : null;
        boolean reccurrence = (params.length > 3 && !"null".equals(params[3]) && WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_RECURRENCE.name().equals(params[3])) ||
                                (id == null && params.length > 1 && !"null".equals(params[1]) &&WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_RECURRENCE.name().equals(params[1]));
        boolean notForStep = WorkflowRule._WORKFLOW_MODULE_CASE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_LEAD.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_CONTACT.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_CS_STUDENT.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_SCHEDULED_COURSE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_SALEQUOTE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_PURCHASE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_OPPORTUNITY.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_SICK_REQUEST.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_EXPENSE_CLAIM.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_PROJECT.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_ADDITIONAL_PAYMENT.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_PRODUCT.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_GDN.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_PICKLIST.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_ACCOUNT.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_TASK.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_QUOTE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_PURCHASE_INVOICE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_SALEORDER.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_VACANCY.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_RECEIVE_PAYMENT.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_PAY_INVOICE.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_ROTATION.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_GROUP_PLACEMENT.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_PRODUCT_CATEGORY.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_EMPLOYEE_DOCUMENTS.equals(relationType) ||
                WorkflowRule._WORKFLOW_MODULE_PLACEMENT.equals(relationType);
        if (id != null) {
            addView(new EditWorkflowRule(id, reccurrence));
            if (!WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(relationType)) {
                addView(new WorkflowTaskList(id, RelationItem.TYPE_WORKFLOW));
            }
            if (!WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(relationType) && !WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(relationType) && !WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(relationType)) {
                addView(new WorkflowEventListView(id, RelationItem.TYPE_WORKFLOW));
            }
            addView(new WorkflowAlertListView(id));
            addView(new WorkflowSMSAlertListView(id));
            addView(new WorkflowTelegramAlertListView(id));
            if (!WorkflowRule._WORKFLOW_MODULE_CERTIFICATE.equals(relationType)) {
                if (!WorkflowRule._WORKFLOW_MODULE_PRODUCT.equals(relationType)) {
                    addView(new WorkflowPushNotificationsListView(id, relationType));
                }
                addView(new WorkflowUpdateFieldListView(id));
                addView(new WorkflowActionsListView(id));
            }
            if (!notForStep && !WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE.equals(relationType)) {
                addView(new WorkflowEmployeeListView(this.id));
            }
            if (WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(relationType)) {
                addView(new WorkflowInvoiceListView(this.id));
            }
            addView(new WorkflowWebHookListView(id));
        } else {
            addView(new AddWorkflowRule(reccurrence));
        }
    }
}
