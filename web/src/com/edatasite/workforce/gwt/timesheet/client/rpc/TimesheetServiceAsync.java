package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReportResult;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.MonthlyTimesheetItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface TimesheetServiceAsync {

    void getProjectsAndWorkstreams(Integer weekOffset, ListingFilterParameter fp, AsyncCallback<TimesheetFilterData> async);

    void getData(DateNonConvertable clientsDate, int weekOffset, ListingFilterParameter fp, AsyncCallback<TimesheetData> async);

    void applyUpdates(TimesheetDataItem update, Integer synchItemId, AsyncCallback<Integer> async);

    void applyUpdates(TimesheetDataItem update, AsyncCallback<Integer> async);

    void updateStatus(TaskStatus status, AsyncCallback<Void> async);

    void getWeekOffset(DateNonConvertable clientsCurrenDate, DateNonConvertable choosenDate, AsyncCallback<Integer> async);

    void getProjects(AsyncCallback<TimesheetProjectItem[]> async);

    void getEntries(DateNonConvertable fromDate, DateNonConvertable endDate, ArrayList<Integer> projectIDs, LinkedHashMap<String, String> projectTasks, AsyncCallback<TimeSheetEntriesPerPeriod> async);

    void submitTimesheetForApproval(TimeSheetEntriesPerPeriod timesheetForApproval, AsyncCallback<Void> async);

    Request getTimeSheetApprovalSessionList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<TimeSheetApprovalListItem>> async);

    void getTimeSheetApprovalSingleListItems(Integer timeSheetApprovalItem, AsyncCallback<TimeSheetApprovalSingleItemsList> async);

    void saveTimeSheetApprovalSessionListItem(TimeSheetApprovalSingleItemsList item, AsyncCallback<Boolean> async);

    void updatePercentCompleted(Integer employeeTaskId, float percentCompleted, boolean solrUpdate, AsyncCallback<Float> async);

    void getTimesheetSettings(AsyncCallback<TimesheetSettings> callback);

    void getTimesheetCommentSuggestion(Integer userId, Integer taskId, AsyncCallback<ArrayList<SuggestionResponseDTO>> callback);

    void getTimesheetWeeklyDates(DateNonConvertable clientsCurrentDate, int weekOffset, AsyncCallback<DateNonConvertable[]> async);

    void getProjectsAndClients(Integer employeeId, Integer formType, Boolean oldProjects, AsyncCallback<TimesheetData> callback);

    void approveRejectTimesheetHours(TaskTimeSheetEntry[] items, ArrayList<Integer> employeeIDs, AsyncCallback<Boolean> callback);

    void timesheetBatchApproveOrReject(ArrayList<Integer> itemIds, String comment, boolean isApproved, AsyncCallback<Void> callback);

    void getEmployeesList(Integer clientId, Integer projectId, Integer formType, AsyncCallback<SelectItem[]> callback);

    void getProjectsList(Integer clientId, Integer employeeId, AsyncCallback<SelectItem[]> callback);

    void getTimesheetReport(ArrayList<Integer> clientIDs, ArrayList<Integer> projectIDs, ArrayList<Integer> employeeId, Integer viewAsId, Integer statusId, String groupByName,
            DateNonConvertable from, DateNonConvertable to, boolean showClient, boolean showProject, boolean showDepartment, boolean showEmployee, boolean showTask,
            boolean showDate, boolean showComment, boolean showDescription, boolean showPercentCompleted, boolean showApprovedHours, boolean showStatus, boolean showTimesheetStatus, Integer formType, AsyncCallback<ReportResult[]> callback);

    void getFastTimesheetData(DateNonConvertable selectedDate, DateNonConvertable clientsDate, Integer weekOffset, ListingFilterParameter filterParameters, AsyncCallback<FastTimesheetData> callback);

    void getFastTimesheetData(DateNonConvertable selectedDate, DateNonConvertable clientsDate, Integer weekOffset, LinkedHashMap<String, String> items, ListingFilterParameter filterParameters, AsyncCallback<FastTimesheetData> callback);

    void getMonthlyTimesheetData(DateNonConvertable dateNonConvertable, Integer projectId, Integer employeeID, Boolean value, AsyncCallback<ArrayList<MonthlyTimesheetItem>> async);

    void saveMonthlyTimesheetData(ArrayList<MonthlyTimesheetItem> items, DateNonConvertable selectedDate, Integer projectId, AsyncCallback<Void> async);

    void importMonthlyTimesheetData(Integer importingFileID, DateNonConvertable selectedDate, Integer projectId, AsyncCallback<Void> async);

    void getEmployeeContractedHours(DateNonConvertable dateNonConvertable, Integer projectId, Integer employeeId, AsyncCallback<HashMap<Integer, Integer[]>> async);

    void updateTimesheetDate(Integer objectID, DateNonConvertable changedDate, AsyncCallback<Void> async);

    void getTimesheetComment(Integer taskId, Integer employeed, DateNonConvertable timesheetDate, AsyncCallback<String> callback);

    void submitTimesheetAutomaticForApproval(DateNonConvertable fromDate, Integer weekOffSet, LinkedHashMap<String, String> projectTasks, Integer employeeId, AsyncCallback<Void> async);
}
