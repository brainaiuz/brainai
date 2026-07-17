package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.MultiTaskList;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoList;
import com.edatasite.workforce.gwt.submodule.todo.client.TodoListRpc;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dilshod Madrahimov on 1/25/2016.
 */
public interface DashboardWidgetService extends RemoteService {

    void deleteTodoTask(Integer taskID);

    TodoListRpc getTodoListRpc();

    TodoList getTodoLists(Integer userID);

    void saveTodoListOrdering(HashMap<Integer, Integer> taskIdsWithOrdering);

    Integer saveMultipleTask(MultiTaskList multiTaskList);

    ListResult<MyUpdateItem> getMyLatestUpdates(ListingFilterParameter fp, DateNonConvertable startDate);

    ListResult<MyUpdateItem> getMyPeersUpdates(ListingFilterParameter fp, DateNonConvertable startDate);

    ListResult<DashboardContactItem> getMyContacts(ListingFilterParameter fp, ListLoadConfig config);

    ChartData getDynamicComponentData(DashboardComponentItem gridItemConfig);

    void updateContactFavourite(Integer contactId, boolean isFavourited);

    DashboardContactItem getContactData();

    DashboardContactItem getMyContactDetails(Integer contactId);

    ChartData geProjectOverviewData();

    ChartData geProjectTime();

    ChartData geProjectBudget();

    ArrayList<DashboardPMItem> getProjectsByDate(ListingFilterParameter fp);

    ArrayList<DashboardPMItem> getTasksByDate(ListingFilterParameter fp);

    ArrayList<LRPC> getLeaveRequestList(ListingFilterParameter fp);

    SelectItem[] getCustomReasons();

    CompanySettingsItem getCompanySettingsGettingStarted();

    void updateCompanyInfoGettingStarted(CompanySettingsItem data);

    EmployeeProfileItem getEmployeeProfileGettingStarted();

    void saveEmployeeProfileGettingStarted(EmployeeProfileItem data);

    ArrayList<ImportGuideItem> getDataImportGettingStarted();

    ChartData getEmployeesGenderRatio();

    ChartData getTopExpenses(FromToDate fromToDate);

    ChartData getEmployeeTopExpenses(FromToDate fromToDate);

    ChartData getAgingData(DateNonConvertable date);

    ChartData getSalesPurchaseData(DateNonConvertable date);

    ChartData getIncomeExpensesData(DateNonConvertable date);

    HolidayRPC getCompanyHolidaysForUser();

    DashboardMyCalendarCarouselItem getMyCalendarDays(DateNonConvertable currentDate);

    DashboardMyCalendarCarouselItem getMyCalendarSampleDays(DateNonConvertable date);

    ArrayList<DashboardMyCalendarDetailItem> getMyCalendarDetailData(DateNonConvertable day);

    Integer saveMyCalendarItem(DashboardMyCalendarDetailItem item);

    ChartData getCompanyPaymentDeductionData();

    ChartData getCompanyPaymentDeductionSampleData();

    ChartData getEmployeePaymentDeductionData();

    ChartData getEmployeePaymentDeductionSampleData();

    DashboardComboItem getComboData();

    DashboardWeatherItem getWeather(boolean isSample);

    ChartData getLeaveRequestDays(ListingFilterParameter fp);

    DashboardWeatherItem saveWeatherSettings(boolean isFahrenheit);

    TimeslotItem getEmpTimeslot();

    KpiWidgetData getDynamicWidgetComponentData(DashboardComponentItem gridItemConfig);

    ReportRpc getDynamicWidgetTitle(Integer reportId);

    ChartConfItem getDynamicComponentTitle(Integer reportId);

    class App {
        public static DashboardWidgetServiceAsync get() {
            ServiceDefTarget target = GWT.create(DashboardWidgetService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/dashboardWidget");
            return (DashboardWidgetServiceAsync) target;
        }
    }

}
