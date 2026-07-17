package com.edatasite.workforce.gwt.dashboardwidget.client.utils;

import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.GettingStartedItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.emailAccount.EmailAccountQuickAdd;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.*;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.accounting.AgingPayableReceivableComponent;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.accounting.IncomeVsExpenseComponent;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.accounting.SalesAndPurchaseComponent;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.accounting.TopExpensesComponent;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.documents.DashboardExpiredDocuments;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.dynamicwidget.DynamicComponent;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.hrms.*;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.pm.DashboardProjectByDate;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.pm.DashboardProjectOverview;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.pm.DashboardTasksDueToday;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.quickadd.CompanySetupQuickAdd;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.quickadd.DataImportQuickView;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.quickadd.EmployeeProfileQuickAdd;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.quickadd.EmployeesQuickAddForm;

public class DashboardUtils implements Constants {

    public static DashboardBaseWidget generateComponent(DashboardComponentItem componentConf, boolean isCustomize, Integer dashboardId) {

        if (componentConf == null) {
            return null;
        }

        DashboardBaseWidget component = null;
        switch (componentConf.getComponentCode()) {
            case DASHBOARD_WIDGET_CODE.TODO_LIST:
                component = new DashboardToDoListComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.MY_UPDATES:
                component = new DashboardMyUpdatesComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.MY_CONTACTS:
                component = new DashboardMyContactsComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.INCOME_VS_EXPENSE:
                component = new IncomeVsExpenseComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.TOP_EXPENSES:
                component = new TopExpensesComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.EMPLOYEE_TOP_EXPENSES:
                component = new EmployeeExpensesComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.SALE_PURCHASE_TRANSACTIONS:
                component = new SalesAndPurchaseComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.AGED_REPORTS:
                component = new AgingPayableReceivableComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.PROJET_OVERVIEW:
                component = new DashboardProjectOverview(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.PROJECT_DUE_THIS_MONTH:
                component = new DashboardProjectByDate(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.TASKS_DUE_TODAY:
                component = new DashboardTasksDueToday(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.UNAVAILABLE_EMPLOYEES_SUPERVISION:
                component = new UnavailableEmployeesSupervisorWidget(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.MY_CALENDAR:
                component = new DashboardMyCalendarComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.GENDER_RATIO:
                component = new GenderRatioChart(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.EXPIRED_DOCUMENTS:
                component = new DashboardExpiredDocuments(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.EXPIRY_DOCUMENTS:
                component = new DashboardExpiredDocuments(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.HOLIDAY:
                component = new PublicHolidaysWidget(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.PAYROLL_YTD:
                component = new PaymentsAndDeductionsWidget(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.PAYROLL_EMPLOYEE_YTD:
                component = new EmployeePaymentsAndDeductionsWidget(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.COMBO:
                component = new DashboardComboComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.LEAVE_REASON_STATUS:
                component = new EmployeeLeaveReasonStatus(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.HRMS_MY_FILES:
                component = new HrmsMyFiles(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.TIMESLOT:
                component = new TimeSlotWidget(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.WAITING_FOR_APPROVAL:
                component = new WaitingForApprovalWidget(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.HRMS_EMPLOYEE_CALENDAR:
                component = new DashboardEmployeeCalendarComponent(componentConf);
                component.init();
                break;
            case DASHBOARD_WIDGET_CODE.MY_FAVOURITE_REPORTS:
                component = new DashboardMyFavouriteReportsComponent(componentConf);
                component.init();
                break;
            default:

                if (componentConf.getReportId() != null && componentConf.getReportId() > 0) {
                    component = new DynamicComponent(componentConf, isCustomize, dashboardId);
                    component.init();
                }
                break;

        }
        return component;
    }

    public static KpiSideNavBox generateGettingStartedComponent(GettingStartedItem item) {

        if (item == null) {
            return null;
        }

        KpiSideNavBox quickAdd = null;
        switch (item.getType()) {
            case DASHBOARD_GETTING_STARTED.COMPANY_SETUP:
                quickAdd = new CompanySetupQuickAdd();
                break;
            case DASHBOARD_GETTING_STARTED.INVITE_USER:
                quickAdd = new EmployeesQuickAddForm();
                break;
            case DASHBOARD_GETTING_STARTED.USER_PROFILE:
                quickAdd = new EmployeeProfileQuickAdd();
                break;
            case DASHBOARD_GETTING_STARTED.DATA_MIGRATION:
                quickAdd = new DataImportQuickView();
                break;
            case DASHBOARD_GETTING_STARTED.CONFIGURE_EMAIL:
                quickAdd = new EmailAccountQuickAdd(true);
                break;
        }

        return quickAdd;
    }
}
