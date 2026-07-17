package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReportResult;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.MonthlyTimesheetItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface TimesheetService extends RemoteService {

    TimesheetFilterData getProjectsAndWorkstreams(Integer weekOffset, ListingFilterParameter fp);

    TimesheetData getData(DateNonConvertable clientsDate, int weekOffset, ListingFilterParameter fp);

    DateNonConvertable[] getTimesheetWeeklyDates(DateNonConvertable clientsCurrentDate, int weekOffset);

    Integer applyUpdates(TimesheetDataItem update, Integer synchItemId);

    Integer applyUpdates(TimesheetDataItem update);

    void updateStatus(TaskStatus status);

    Integer getWeekOffset(DateNonConvertable clientsCurrenDate, DateNonConvertable choosenDate);

    TimesheetProjectItem[] getProjects();

    TimeSheetEntriesPerPeriod getEntries(DateNonConvertable fromDate, DateNonConvertable endDate, ArrayList<Integer> projectIDs, LinkedHashMap<String, String> projectTasks);

    void submitTimesheetForApproval(TimeSheetEntriesPerPeriod timesheetForApproval);

    ListResult<TimeSheetApprovalListItem> getTimeSheetApprovalSessionList(ListingFilterParameter filterParametrs);

    TimeSheetApprovalSingleItemsList getTimeSheetApprovalSingleListItems(Integer timeSheetApprovalItem);

    Boolean saveTimeSheetApprovalSessionListItem(TimeSheetApprovalSingleItemsList item);

    Float updatePercentCompleted(Integer employeeTaskId, float percentCompleted, boolean solrUpdate);

    TimesheetSettings getTimesheetSettings();

    ArrayList<SuggestionResponseDTO> getTimesheetCommentSuggestion(Integer userId,Integer taskId);

    TimesheetData getProjectsAndClients(Integer employeeId, Integer formType, Boolean oldProjects);

    Boolean approveRejectTimesheetHours(TaskTimeSheetEntry[] items, ArrayList<Integer> employeeIDs);

    void timesheetBatchApproveOrReject(ArrayList<Integer> itemIds, String comment, boolean isApproved);

    SelectItem[] getEmployeesList(Integer clientId, Integer projectId, Integer formType);

    SelectItem[] getProjectsList(Integer clientId, Integer employeeId);

    ReportResult[] getTimesheetReport(ArrayList<Integer> clientIDs, ArrayList<Integer> projectIDs,
                                      ArrayList<Integer> employeeId, Integer viewAsId, Integer statusId, String groupByName,
                                      DateNonConvertable from, DateNonConvertable to, boolean showClient, boolean showProject,
                                      boolean showDepartment, boolean showEmployee, boolean showTask,
                                      boolean showDate, boolean showComment, boolean showDescription, boolean showPercentCompleted, boolean showApprovedHours, boolean showStatus, boolean showTimesheetStatus, Integer formType);

    FastTimesheetData getFastTimesheetData(DateNonConvertable selectedDate, DateNonConvertable clientsDate, Integer weekOffset, ListingFilterParameter filterParameters);

    FastTimesheetData getFastTimesheetData(DateNonConvertable selectedDate, DateNonConvertable clientsDate, Integer weekOffset, LinkedHashMap<String, String> items, ListingFilterParameter filterParameters);

    ArrayList<MonthlyTimesheetItem> getMonthlyTimesheetData(DateNonConvertable dateNonConvertable, Integer projectId, Integer employeeID, Boolean value);

    void saveMonthlyTimesheetData(ArrayList<MonthlyTimesheetItem> items, DateNonConvertable selectedDate, Integer projectId);

    void importMonthlyTimesheetData(Integer importingFileID, DateNonConvertable selectedDate, Integer projectId);

    HashMap<Integer, Integer[]> getEmployeeContractedHours(DateNonConvertable dateNonConvertable, Integer projectId, Integer employeeId);

    void updateTimesheetDate(Integer objectID, DateNonConvertable changedDate);

    String getTimesheetComment(Integer taskId, Integer employeed, DateNonConvertable timesheetDate);

    void submitTimesheetAutomaticForApproval(DateNonConvertable fromDate, Integer weekOffSet, LinkedHashMap<String, String> projectTasks, Integer employeeId);

    class App {
        public static TimesheetServiceAsync get() {
            ServiceDefTarget target = GWT.create(TimesheetService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/timesheet");
            return (TimesheetServiceAsync) target;
        }
    }
}
