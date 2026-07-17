package com.edatasite.workforce.gwt.pm.client.factory;

import com.edatasite.workforce.gwt.client.client.history.ClientEditHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.ClientHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.history.PMClientImportHistoryProcessor;
import com.edatasite.workforce.gwt.client.client.ui.view.NewClientListView;
import com.edatasite.workforce.gwt.core.client.DynamicSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.form.AddCustomFormItemView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemView;
import com.edatasite.workforce.gwt.core.client.history.CustomFormItemHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.SearchHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.WorkflowWebHookEditHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.history.WorkflowWebHookHistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.resourceUtil.ResourceUtilizationView;
import com.edatasite.workforce.gwt.crm.client.history.CaseHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.ContactEditHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.ContactHistoryProcessor;
import com.edatasite.workforce.gwt.crm.client.history.OpportunityHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.ImportEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.EmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.EmployeeImportHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.history.SingleEmployeeHistoryProcessor;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.employee.client.ui.view.quickadd.EmployeeQuickAdd;
import com.edatasite.workforce.gwt.expenses.client.history.ExpensePaymentViewHistoryProcessor;
import com.edatasite.workforce.gwt.expenses.client.history.ExpenseReportHistoryProcessor;
import com.edatasite.workforce.gwt.expenses.client.ui.view.ExpenseListView;
import com.edatasite.workforce.gwt.googlecalendar.client.history.EventHistoryProcessor;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.expense.ProjectBaseExpenseHistoryProcessor;
import com.edatasite.workforce.gwt.invoice.client.history.rfp.RequestForPurchaseHistoryProcessor;
import com.edatasite.workforce.gwt.issue.client.history.IssueHistoryProcessor;
import com.edatasite.workforce.gwt.issue.client.ui.IssueListView;
import com.edatasite.workforce.gwt.location.client.history.LocationHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailHistoryProcessor;
import com.edatasite.workforce.gwt.pm.client.PMDashboardSinksContainer;
import com.edatasite.workforce.gwt.pm.client.history.GettingStartedGuideHistoryProcessor;
import com.edatasite.workforce.gwt.profile.client.history.CustomizationSettingsHistoryProcessor;
import com.edatasite.workforce.gwt.project.client.history.*;
import com.edatasite.workforce.gwt.project.client.ui.BookingItemsListView;
import com.edatasite.workforce.gwt.project.client.ui.ContractListView;
import com.edatasite.workforce.gwt.project.client.ui.ProjectListView;
import com.edatasite.workforce.gwt.task.client.history.MultiTaskHistoryProcessor;
import com.edatasite.workforce.gwt.task.client.history.TaskHistoryProcessor;
import com.edatasite.workforce.gwt.task.client.history.WorkstreamHistoryProcessor;
import com.edatasite.workforce.gwt.task.client.ui.TaskListView;
import com.edatasite.workforce.gwt.team.client.history.TeamHistoryProcessor;
import com.edatasite.workforce.gwt.timesheet.client.history.TimesheetApprovalHistoryProcessor;
import com.edatasite.workforce.gwt.timesheet.client.history.TimesheetHistoryProcessor;
import com.edatasite.workforce.gwt.timesheet.client.ui.TimesheetApprovalListView;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.FastTimesheet;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.FastTimesheetCustom;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.MonthlyTimesheetView;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PM_CONTEXT;

/**
 * @author Admin
 */
public class PMSinksContainerFactory extends SinksContainerFactory {
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private boolean isFirstContainer = true;
    private SinksContainer sinksContainer;

    public PMSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
        setDefaultContainer(MYWORKSPACE);
    }

    private SinksContainer selectedContener;

    public void initDefaultContainers() {
        addMainSinksContainer();
    }

    private void addMainSinksContainer() {
        if (com.edatasite.workforce.gwt.pm.client.PM.dashboards.size() > 0) {
            PMDashboardSinksContainer dashboardContainer = new PMDashboardSinksContainer();
            dashboardContainer.setPreparedView("dashboard_" + dashboardContainer.normalizeName(com.edatasite.workforce.gwt.pm.client.PM.defaultDashboardName));
            putContainer(dashboardContainer);
            setDefaultContainer(dashboardContainer.getName());
            setDashboardContainer(dashboardContainer);
        }

        /*selectedContener = new PMSinksContainer(MYWORKSPACE, getModuleParam("tabName") != null ? getModuleParam("tabName") : Property.getPluralWithObjectCode(Constants.PROJECT, wfmStrings.projects()), true);
        selectedContener.setPreparedView(TASK_LIST);
        setSinksContainer(selectedContener);
        setSelection(selectedContener);*/

        if (Utils.getPropertyListingMap() != null && !Utils.getPropertyListingMap().isEmpty()) {
            setPMPropertyListingsMap(Utils.getPropertyListingMap());
        }

        //As Munir asked we need to open second container if dashboard has only one view
        if(com.edatasite.workforce.gwt.pm.client.PM.dashboards.size() == 1) {
            openSecondContainer();
        }
    }

    public void finishGettingStarted() {
        Utils.userSettings.put(PM_IS_SETUP, "true");
        addMainSinksContainer();
        setSelection(selectedContener);
    }
    // For the module by launch include the necessary processors to the corresponding Sinks containers!

    public void registerProcessors() {
        registerHistoryProcessor(SEARCH, new SearchHistoryProcessor());// History processor for search tab
        registerHistoryProcessor(TASK, new TaskHistoryProcessor());// the task is becoming the container name
        registerHistoryProcessor(WORKSTREAM, new WorkstreamHistoryProcessor());
        registerHistoryProcessor(PROJECT, new ProjectHistoryProcessor());
        registerHistoryProcessor("contract", new ContractHistoryProcessor());
        registerHistoryProcessor("client", new ClientHistoryProcessor(Constants.PROJECT_MANAGEMENT_PAGE));
        registerHistoryProcessor("importclient", new PMClientImportHistoryProcessor());
        registerHistoryProcessor("employee", new EmployeeHistoryProcessor());
        registerHistoryProcessor("importemployees", new EmployeeImportHistoryProcessor());
        registerHistoryProcessor("importemployee", new ImportEmployeeHistoryProcessor());
        registerHistoryProcessor("singleemployee", new SingleEmployeeHistoryProcessor());
        registerHistoryProcessor("issue", new IssueHistoryProcessor());
        registerHistoryProcessor(DEPARTMENT, new TeamHistoryProcessor());
        registerHistoryProcessor("multitask", new MultiTaskHistoryProcessor());
        registerHistoryProcessor("location", new LocationHistoryProcessor());
        registerHistoryProcessor(GETTING_STARTED, new GettingStartedGuideHistoryProcessor());
        registerHistoryProcessor("clientedit", new ClientEditHistoryProcessor());
        registerHistoryProcessor("projectcostestimate", new ProjectCostEstimateHistoryProcessor());
        registerHistoryProcessor("projectcostactual", new ProjectCostActualHistoryProcessor());
        registerHistoryProcessor("timesheet", new TimesheetHistoryProcessor());
        registerHistoryProcessor("timesheetapproval", new TimesheetApprovalHistoryProcessor());
        registerHistoryProcessor("contact", new ContactHistoryProcessor());
        registerHistoryProcessor("contactedit", new ContactEditHistoryProcessor());
        registerHistoryProcessor("messagecenter", new EmailHistoryProcessor());
        registerHistoryProcessor("projectbudget", new ProjectBudgetSheetHistoryProcessor());
        registerHistoryProcessor("case", new CaseHistoryProcessor());
        registerHistoryProcessor("email", new EmailHistoryProcessor());
        registerHistoryProcessor("emailcompose", new EmailComposeHistoryProcessor());
        registerHistoryProcessor("opportunity", new OpportunityHistoryProcessor());
        registerHistoryProcessor("importproject", new ImportProjectHistoryProcessor());
        registerHistoryProcessor("expenseReports", new ExpenseReportHistoryProcessor());
        registerHistoryProcessor("expensepayment", new ExpensePaymentViewHistoryProcessor());
        registerHistoryProcessor("projectBaseExpense", new ProjectBaseExpenseHistoryProcessor());
        registerHistoryProcessor("event", new EventHistoryProcessor());
        registerHistoryProcessor("bookingitems", new BookingItemsHistoryProcessor());
        registerHistoryProcessor("bookingitemsreservation", new BookingItemsReservationHistoryProcessor());
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
        registerHistoryProcessor(ITEM_LIST, new CustomFormItemHistoryProcessor());
        registerHistoryProcessor("customizationSettings", new CustomizationSettingsHistoryProcessor());
        registerHistoryProcessor("requestforpurchase", new RequestForPurchaseHistoryProcessor());
        registerHistoryProcessor("webhook", new WorkflowWebHookHistoryProcessor());
        registerHistoryProcessor("webhookEdit", new WorkflowWebHookEditHistoryProcessor());
    }

    public void registerMenuItems() {
        if (Utils.hasPermission(PermissionConstants.PM_TASKS_ADD)) {
            addNewMenuItem(Property.get(TASK, wfmStrings.task()), "task|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.PM_TASKS_ADD_MULTI)) {
            addNewMenuItem(Property.get(TASK, wfmStrings.multiTask(), wfmStrings.task()), "multitask|add/add");
        }

        if (Utils.hasPermission(PermissionConstants.PM_ISSUE_ADD)) {
            addNewMenuItem(Property.get(Constants.ISSUE, wfmStrings.issue()), "issue|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_WORKSTREAM_ADD)) {
            addNewMenuItem(wfmStrings.workStream(), "workstream|add/add");
        }

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_ADD)) {
            addNewMenuItem(Property.get(PROJECT, wfmStrings.project()), "project|add/add");
        }

        if (Utils.hasPermission(PermissionConstants.PM_CUSTOMER_ADD_CLIENT)) {
            addNewMenuItem(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), "client|add/add");
        }
        if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ADD)) {
            addNewMenuItem(wfmStrings.employee(), clickEvent -> {
                EmployeeQuickAdd quickAddBox = new EmployeeQuickAdd();

                quickAddBox.setCommand(() -> {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADD, null, quickAddBox);
                    quickAddBox.remove();
                });
            });
        }
        if (Utils.isEmployeeAssignmentEnable() && Utils.hasPermission(PermissionConstants.PM_CONTRACT_ADD_EDIT)) {
            addNewMenuItem(wfmStrings.contract(), "contract|add/add");
        }
    }

    private void setPMPropertyListingsMap(LinkedHashMap<SelectItem, LinkedList<PropertyItem>> propertyListingsMap) {
        for (SelectItem selectItem : propertyListingsMap.keySet()) {
            LinkedList<View> viewList = new LinkedList<>();
            if (selectItem.getDescription().contains(ModuleEnum.PM.getCode())) {
                LinkedList<PropertyItem> propertyItemList = propertyListingsMap.get(selectItem);
                for (PropertyItem propertyItem : propertyItemList) {
                    if (propertyItem != null) {
                        switch (propertyItem.getObjectName()) {
                            case TASK:
                                viewList.add(new TaskListView());
                                break;
                            case ISSUE:
                                viewList.add(new IssueListView());
                                break;
                            case TIMESHEET:
                                if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_PROJECT_TO_TIMESHEET)) {
                                    viewList.add(new FastTimesheetCustom());
                                } else {
                                    viewList.add(new FastTimesheet());
                                }
                                break;
                            case MONTHLYTIMESHEET:
                                boolean isMonthlyTimesheetEnabled = Boolean.valueOf(Utils.userSettings.get(Constants.MONTHLY_TIMESHEET));
                                if (isMonthlyTimesheetEnabled && Utils.hasPermission(PermissionConstants.MONTHLY_TIMESHEET)) {
                                    viewList.add(new MonthlyTimesheetView());
                                }
                                break;
                            case TIMESHEET_APPROVAL_LIST:
                                viewList.add(new TimesheetApprovalListView());
                                break;
                            case PROJECT:
                                viewList.add(new ProjectListView());
                                break;
                            case CLIENT_LIST:
                                viewList.add(new NewClientListView(false));
                                break;
                            case EMLOYEE_LIST:
                                if (!CompanyConstants.C22240.equals(Utils.getEncryptedCompanyID())) {
                                    viewList.add(new EmployeeListView(EmployeeListView.FROM_PM));
                                }
                                break;
                            case BOOKINGITEMS_LIST:
                                viewList.add(new BookingItemsListView(PM_CONTEXT));
                                break;
                            case RESOURCE_UTIL:
                                viewList.add(new ResourceUtilizationView(null));
                                break;
                            case CONTRACT_LIST:
                                viewList.add(new ContractListView());
                                break;
                            case EXPENSES_CLAIM:
                                viewList.add(new ExpenseListView(false));
                                break;

                            default:
                                if (propertyItem.isCustom()) {
                                    if (Constants.PAGE.equals(propertyItem.getType())) {
                                        if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_SUMMARY_" + Utils.getCompanyID())) {
                                            viewList.add(new CustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        } else if (propertyItem.getSelectedItemID() != null && Utils.hasPermission(propertyItem.getFormID() + "_EDIT_" + Utils.getCompanyID()) || Utils.hasPermission(propertyItem.getFormID() + "_ADD_" + Utils.getCompanyID())) {
                                            viewList.add(new AddCustomFormItemView(propertyItem.getSelectedItemID(), propertyItem.getfID(), propertyItem.getFormID(), getLocalizedPlural(propertyItem), true));
                                        }
                                    } else {
                                        viewList.add(new CustomFormItemListView(propertyItem.getfID(), getLocalizedPlural(propertyItem), propertyItem.getFormID()));
                                    }
                                }
                        }
                    }
                }
            }
            DynamicSinksContainer dynamicSC = new DynamicSinksContainer(selectItem.getCode(), selectItem.getName(), viewList);
            dynamicSC.setPreparedView(selectItem.getCategory());
            if (isFirstContainer) {
                sinksContainer = dynamicSC;
                setSelection(sinksContainer);
                isFirstContainer = false;
            }
            setSinksContainer(dynamicSC);
        }
    }
    private String getLocalizedPlural(PropertyItem propertyItem) {
        if (propertyItem.getlPlural() != null) {
            switch (Utils.getUserLanguage()) {
                case "en":
                    return propertyItem.getlPlural().getEnglishName();
                case "ar":
                    return propertyItem.getlPlural().getArabicName();
                case "ru":
                    return propertyItem.getlPlural().getRussianName();
                case "uz":
                    return propertyItem.getlPlural().getUzbekName();
            }
        }
        return propertyItem.getPlural();
    }
}
