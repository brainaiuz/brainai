package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.GoalsListView;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.resourceUtil.ResourceUtilizationView;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfp.RequestForPurchaseListView;
import com.edatasite.workforce.gwt.issue.client.ui.IssueListView;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailListView;
import com.edatasite.workforce.gwt.project.client.ui.ProjectListView;
import com.edatasite.workforce.gwt.project.client.ui.view.BillOfMaterialView;
import com.edatasite.workforce.gwt.project.client.ui.view.CostRateListView;
import com.edatasite.workforce.gwt.project.client.ui.view.NewProjectBudgetSheetView;
import com.edatasite.workforce.gwt.project.client.ui.view.NewProjectSummaryView;
import com.edatasite.workforce.gwt.project.client.ui.view.ProjectEditView;
import com.edatasite.workforce.gwt.project.client.ui.view.ProjectExpenseClaimsListView;
import com.edatasite.workforce.gwt.project.client.ui.view.ProjectGwtGanttChartView;
import com.edatasite.workforce.gwt.project.client.ui.view.ProjectInvoicesListView;
import com.edatasite.workforce.gwt.project.client.ui.view.ProjectPurchaseInvoiceListView;
import com.edatasite.workforce.gwt.project.client.ui.view.ProjectPurchaseOrderListView;
import com.edatasite.workforce.gwt.project.client.ui.view.ProjectSaleQuoteListView;
import com.edatasite.workforce.gwt.project.client.ui.view.ProjectTaskListView;
import com.edatasite.workforce.gwt.project.client.ui.view.projectbudget.NewProjectBudgetView;
import com.edatasite.workforce.gwt.task.client.ui.view.ProjectWBSView;

import java.util.LinkedList;

public class ProjectViewSinksContainer extends SinksContainer {

    public ProjectViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    protected void initViews() {
        //boolean isParentNullProject = !(params.length > 1 && params[1] != null && !params[1].isEmpty() && !"null".equals(params[1]));// project parent is null or sub prject checked
        boolean hasAccessToChange = params.length <= 2 || Boolean.parseBoolean(params[2]);

        NewProjectSummaryView projectSummaryView = new NewProjectSummaryView(this.id, this);
        projectSummaryView.ensureDebugId("projectSummaryView");
        super.addView(projectSummaryView);

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_EDIT) && (hasAccessToChange)) {
            ProjectEditView projectEditView = new ProjectEditView(id);
            projectEditView.ensureDebugId("projectEditView");
            super.addView(projectEditView);
        }

        if (Utils.hasPermission(PermissionConstants.PM_TASKS_LIST)) {
            ProjectTaskListView taskListView = new ProjectTaskListView(id, hasAccessToChange);
            taskListView.ensureDebugId("taskListView");
            super.addView(taskListView);
        }

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_WORK_BREAKDOWN_STRUCTURE)) {
            ProjectWBSView projectWBS = new ProjectWBSView(id, hasAccessToChange);
            projectWBS.ensureDebugId("projectWBS");
            super.addView(projectWBS);
        }

        //        if ("true".equals(Utils.userSettings.get(RESOURCE_UTILIZATION_ENABLED)) && (Utils.hasPermission(PermissionConstants.PM_PROJECT_RESOURCE_WORK_LOAD))) {
//            ResourceWorkLoad resourceWorkLoad = new ResourceWorkLoad(id);
//            resourceWorkLoad.ensureDebugId("resourceWorkLoad");
//            super.addView(resourceWorkLoad);
//        }
//

        // project sub project list
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SUB_PROJECT) ||
                (Boolean.valueOf(Utils.userSettings.get(Constants.IS_SETUP_SUPPROJECT_TWO_LEVEL)) && ("null".equals(params[1]) || "undefined".equals(params[1])))) {

            ProjectListView subProjectListView = new ProjectListView(this.id);
            subProjectListView.ensureDebugId("subProjectListView");
            super.addView(subProjectListView);
        }

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_BUDGET_SHEET)) {
            NewProjectBudgetSheetView projectBudgetSheetSpecificView = new NewProjectBudgetSheetView(id, hasAccessToChange);
            projectBudgetSheetSpecificView.ensureDebugId("projectBudgetSheetSpecificView");
            super.addView(projectBudgetSheetSpecificView);
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_BUDGET_BETA)) {
                super.addView(new NewProjectBudgetView(id, hasAccessToChange));
            }
        }

        if ((Utils.hasPermission(PermissionConstants.PM_PROJECT_GANTT_CHART) && hasAccessToChange)) {
            ProjectGwtGanttChartView gwtGanttChartView = new ProjectGwtGanttChartView(this.id, this);
            gwtGanttChartView.ensureDebugId("gwtganttChartView");
            super.addView(gwtGanttChartView);
        }

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_DOCUMENTS)) {
            DocumentsView documentsView = new DocumentsView(F_PROJECT, this.id, true, false);
            documentsView.ensureDebugId("documentsView");
            super.addView(documentsView);
        }

        if ((Utils.hasPermission(PermissionConstants.PM_PROJECT_SALES_QUOTE) || Utils.hasPermission(PermissionConstants.PM_SALES_QUOTE_LIST))
                && Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            ProjectSaleQuoteListView quotesView = new ProjectSaleQuoteListView(this.id, false);
            quotesView.ensureDebugId("quotesView");
            super.addView(quotesView);
        }
        if ((Utils.hasPermission(PermissionConstants.PM_PROJECT_SALES_QUOTE) || Utils.hasPermission(PermissionConstants.PM_SALES_ORDER_LIST))
                && Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            ProjectSaleQuoteListView ordersView = new ProjectSaleQuoteListView(this.id, true);
            ordersView.ensureDebugId("ordersView");
            super.addView(ordersView);
        }

        if (Utils.hasPermission(PermissionConstants.PM_SALES_INVOICE_LIST)
                && Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            ProjectInvoicesListView InvoicesView = new ProjectInvoicesListView(this.id);
            InvoicesView.ensureDebugId("InvoicesView");
            super.addView(InvoicesView);
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_REQUEST_FOR_PURCHASE_LIST) && Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            super.addView(new RequestForPurchaseListView(id));
        }

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_PURCHASE_ORDER) && Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            ProjectPurchaseOrderListView projectPurchaseOrderListView = new ProjectPurchaseOrderListView(this.id);
            projectPurchaseOrderListView.ensureDebugId("projectPurchaseOrderListView");
            super.addView(projectPurchaseOrderListView);
        }

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_PURCHASE_INVOICE) && Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            ProjectPurchaseInvoiceListView projectPurchaseInvoiceListView = new ProjectPurchaseInvoiceListView(this.id);
            projectPurchaseInvoiceListView.ensureDebugId("projectPurchaseInvoiceListView");
            super.addView(projectPurchaseInvoiceListView);
        }

        if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EXPENSE_USE_AS_INTERNAL_INVOICE) && Utils.hasPermission(PermissionConstants.PM_PROJECT_EXPENSE_CLAIMS) && Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            ProjectExpenseClaimsListView projectExpenseClaimsListView = new ProjectExpenseClaimsListView(this.id);
            projectExpenseClaimsListView.ensureDebugId("projectExpenseClaimsListView");
            super.addView(projectExpenseClaimsListView);
        }

        if ("true".equals(Utils.userSettings.get(RESOURCE_UTILIZATION_ENABLED)) && Utils.hasPermission(PermissionConstants.PM_RESOURCE_UTILIZATION_LIST)) {
            ResourceUtilizationView resourceUtilizationView = new ResourceUtilizationView(id);
            resourceUtilizationView.ensureDebugId("resourceUtilizationView");
            super.addView(resourceUtilizationView);
        }

        /*if (Utils.hasPermission(PermissionConstants.PM_PROJECT_EMPLOYEES)) {
            ProjectMembersView projectMembersView = new ProjectMembersView(this.id, isParentNullProject, hasAccessToChange);//,canModify);
            projectMembersView.ensureDebugId("projectMembersView");
            super.addView(projectMembersView);
        }*/

        if (Utils.hasGenericAccess(GenericSettingsEnum.BILL_OF_MATERIALS) && Utils.hasPermission(PermissionConstants.BILL_OF_MATERIALS)) {
            BillOfMaterialView billOfMaterialView = new BillOfMaterialView(this.id);
            billOfMaterialView.ensureDebugId("billOfMaterialView");
            super.addView(billOfMaterialView);
        }

        if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_RATE_HISTORY)) {
            CostRateListView projectRateCost = new CostRateListView(this.id);//,canModify);
            projectRateCost.ensureDebugId("projectRateCost");
            super.addView(projectRateCost);
        }

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_ISSUE)) {
            super.addView(new IssueListView(id, RelationItem.TYPE_PROJECT));
        }

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_EMAIL) && Utils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
            EmailListView emailListView = new EmailListView(RelationItem.TYPE_PROJECT, this.id);
            emailListView.ensureDebugId("emailListView");
            super.addView(emailListView);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
            CaseListView caseListView = new CaseListView(id, RelationItem.TYPE_PROJECT);
            caseListView.ensureDebugId("caseListView");
            super.addView(caseListView);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
            super.addView(new EventListView(null, this.id, RelationItem.TYPE_PROJECT));
        }

        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            super.addView(new WebHookResponseListView(this.id, RelationItem.TYPE_PROJECT));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_PROJECT_GOALS)) {
            super.addView(new GoalsListView(Constants.PROJECT_GOAL, id));
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_PERSONAL_GOALS)) {
            super.addView(new GoalsListView(Constants.PERSONAL_GOAL, id));
        }
        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.PROJECT, id);
        }
    }

    @Override
    public void activate(View view) {
        super.activate(view);
        if (view instanceof DocumentsView) {
            DocumentsView.setSingleton((DocumentsView) view);
        }
    }

    @Override
    public void reInit() {
        super.reInit();
        if (getWorkarea() != null && getWorkarea().getCurrentView() != null && getWorkarea().getCurrentView() instanceof DocumentsView) {
            DocumentsView.setSingleton((DocumentsView) getWorkarea().getCurrentView());
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
