package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
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
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dilshod Madrahimov on 1/25/2016.
 */
public interface DashboardWidgetServiceAsync {

    void deleteTodoTask(Integer taskID, AsyncCallback<Void> async);

    void getTodoListRpc(AsyncCallback<TodoListRpc> callback);

    void getTodoLists(Integer userID, AsyncCallback<TodoList> callback);

    void saveTodoListOrdering(HashMap<Integer, Integer> taskIdsWithOrdering, AsyncCallback<Void> callback);

    void saveMultipleTask(MultiTaskList multiTaskList, AsyncCallback<Integer> async);

    void getMyLatestUpdates(ListingFilterParameter fp, DateNonConvertable startDate, AsyncCallback<ListResult<MyUpdateItem>> async);

    void getMyPeersUpdates(ListingFilterParameter fp, DateNonConvertable startDate, AsyncCallback<ListResult<MyUpdateItem>> async);

    void getMyContacts(ListingFilterParameter fp, ListLoadConfig config, AsyncCallback<ListResult<DashboardContactItem>> async);

    void getDynamicComponentData(DashboardComponentItem gridItemConfig, AsyncCallback<ChartData> async);

    void updateContactFavourite(Integer contactId, boolean isFavourited, AsyncCallback<Void> async);

    void getContactData(AsyncCallback<DashboardContactItem> async);

    void getMyContactDetails(Integer contactId, AsyncCallback<DashboardContactItem> async);

    void geProjectOverviewData(AsyncCallback<ChartData> asyncCallback);

    void geProjectTime(AsyncCallback<ChartData> asyncCallback);

    void geProjectBudget(AsyncCallback<ChartData> asyncCallback);

    void getProjectsByDate(ListingFilterParameter fp, AsyncCallback<ArrayList<DashboardPMItem>> asyncCallback);

    void getTasksByDate(ListingFilterParameter fp, AsyncCallback<ArrayList<DashboardPMItem>> asyncCallback);

    void getLeaveRequestList(ListingFilterParameter fp, AsyncCallback<ArrayList<LRPC>> async);

    void getCustomReasons(AsyncCallback<SelectItem[]> async);

    void getCompanySettingsGettingStarted(AsyncCallback<CompanySettingsItem> callback);

    void updateCompanyInfoGettingStarted(CompanySettingsItem data, AsyncCallback callback);

    void getEmployeeProfileGettingStarted(AsyncCallback<EmployeeProfileItem> callback);

    void saveEmployeeProfileGettingStarted(EmployeeProfileItem data, AsyncCallback callback);

    void getDataImportGettingStarted(AsyncCallback<ArrayList<ImportGuideItem>> callback);

    void getEmployeesGenderRatio(AsyncCallback<ChartData> async);

    void getTopExpenses(FromToDate fromToDate, AsyncCallback<ChartData> async);

    void getEmployeeTopExpenses(FromToDate fromToDate, AsyncCallback<ChartData> async);

    void getAgingData(DateNonConvertable date, AsyncCallback<ChartData> asyncCallback);

    void getSalesPurchaseData(DateNonConvertable date, AsyncCallback<ChartData> asyncCallback);

    void getIncomeExpensesData(DateNonConvertable date, AsyncCallback<ChartData> asyncCallback);

    void getCompanyHolidaysForUser(AsyncCallback<HolidayRPC> async);

    void getMyCalendarDays(DateNonConvertable currentDate, AsyncCallback<DashboardMyCalendarCarouselItem> asyncCallback);

    void getMyCalendarSampleDays(DateNonConvertable currentDate, AsyncCallback<DashboardMyCalendarCarouselItem> asyncCallback);

    void getMyCalendarDetailData(DateNonConvertable date, AsyncCallback<ArrayList<DashboardMyCalendarDetailItem>> asyncCallback);

    void saveMyCalendarItem(DashboardMyCalendarDetailItem item, AsyncCallback<Integer> callback);

    void getCompanyPaymentDeductionData(AsyncCallback<ChartData> callback);

    void getCompanyPaymentDeductionSampleData(AsyncCallback<ChartData> callback);

    void getEmployeePaymentDeductionData(AsyncCallback<ChartData> callback);

    void getEmployeePaymentDeductionSampleData(AsyncCallback<ChartData> callback);

    void getComboData(AsyncCallback<DashboardComboItem> callback);

    void getWeather(boolean isSample, AsyncCallback<DashboardWeatherItem> callback);

    void getLeaveRequestDays(ListingFilterParameter fp, AsyncCallback<ChartData> async);

    void saveWeatherSettings(boolean isFahrenheit, AsyncCallback<DashboardWeatherItem> async);

    void getEmpTimeslot(AsyncCallback<TimeslotItem> async);

    void getDynamicWidgetComponentData(DashboardComponentItem gridItemConfig, AsyncCallback<KpiWidgetData> callback);

    void getDynamicWidgetTitle(Integer reportId, AsyncCallback<ReportRpc> callback);

    void getDynamicComponentTitle(Integer reportId, AsyncCallback<ChartConfItem> callback);
}
