package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.AnnualLeaveItem;
import com.edatasite.workforce.gwt.core.client.rpc.CalendarItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ShiftSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.laborPeriod.MultiLeaveDTO;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public interface AvailabilityServiceAsync {

    void createLeaveRequest(NewLeaveRequest leaveRequest, AsyncCallback<Integer> async);

    void getReasons(Integer userId, AsyncCallback<SelectItem[]> async);

    void getReasons(Integer userId, Integer year, AsyncCallback<SelectItem[]> async);

    void getLeaveRequest(Integer requestID, AsyncCallback<StatisticsLeaveRequest> async);

    void updateMultipleRequests(String code, ArrayList<Integer> selectItems, String note, AsyncCallback<Void> callback);

    void updateApprove(String approve, Integer requestID, boolean approvalForAll, AsyncCallback<Void> callback);

    void updateApprove(String approve, Integer requestID, boolean approvalForAll, String rejectionReason, AsyncCallback<Void> callback);

    void updateApprove(String approve, Integer requestID, boolean approvalForAll, String rejectionReason, boolean fromApi, AsyncCallback<Void> callback);

    Request getTimeslots(ListingFilterParameter fp, AsyncCallback<ListResult<TimeslotItem>> async);

    void getTimeslot(Integer objectID, AsyncCallback<TimeslotItem> async);

    void createTimeslot(TimeslotItem item, AsyncCallback<Void> async);

    void updateTimeslot(TimeslotItem item, AsyncCallback<Void> async);

    void deleteTimeslot(Integer objectID, AsyncCallback<Void> async);

    void getTimeSlotHistories(Integer timeSlotID, boolean isShift, AsyncCallback<TimeSlotHistoryList[]> callback);

    void deleteTimeslot(Integer objectID, Integer toTimeslot, AsyncCallback<Void> async);

    void getEmpTimeslot(Integer employeeID, AsyncCallback<TimeslotItem> async);

    void createOrUpdateHoliday(HolidayItem newHoliday, AsyncCallback<Void> async);

    void getHolidayHistories(Integer holidayId, AsyncCallback<HolidayHistoryList[]> callback);

    Request getHolidays(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<HolidayItem>> async);

    void getHoliday(Integer holidayID, AsyncCallback<HolidayItem> async);

    void deleteHoliday(Integer holidayID, AsyncCallback<Void> async);

    void getTimeslotList(AsyncCallback<SelectItem[]> async);

    void getLeaveRequestSettingsData(AsyncCallback<LeaveSettingsItem> async);

    void updateEmpBenefitAllowance(AnnualLeaveItem item, AsyncCallback<Void> async);

    void deleteRequest(Integer id, AsyncCallback<Void> async);

    void getCalendarItems(Integer employeeID, String reasonCode, DateNonConvertable displayFirstDay, DateNonConvertable displayLastDay, AsyncCallback<CalendarItems> async);

    void getTeamsList(AsyncCallback<SelectItem[]> async);

    void getEmployeesAndTimeslot(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<TimeslotSetting>> async);

    void getEmployeeBenefitList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<AnnualLeaveItem>> async);

    void getEmployee(Integer objectID, AsyncCallback<EmployeeViewItem> async);

    void setComment(Integer leaveRequestID, String comment, AsyncCallback<Void> async);

    void deleteLeaveRequestComment(Integer commentID, AsyncCallback<Void> callback);

    void getComments(Integer leaveRequestID, AsyncCallback<LeaveRequestComment[]> async);

    void getEmployeeAttendanceReport(ListingFilterParameter fp, int daysInMonth, AsyncCallback<EmployeeAttendanceReport> async);

    void hasAccessInsertRequest(Integer employeeId, NewLeaveRequest leaveRequest, DateNonConvertable startDate, DateNonConvertable endDate, boolean recalculate, AsyncCallback<String> async);

    void getEmployeeTimeSlot(Integer employeeId, DateNonConvertable date, AsyncCallback<TimeSlot> async);

    void getLeaveDaysCount(ListingFilterParameter fp, DateNonConvertable startDate, DateNonConvertable endDate, AsyncCallback<String> async);

    void getLeaveDays(ListingFilterParameter fp, DateNonConvertable startDate, DateNonConvertable endDate, AsyncCallback<Double> async);

    void getEmployeesByTeamsList(Integer id, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> callback);

    Request getTeamMates(ListingFilterParameter filterParameter, AsyncCallback<ListResult<TeammatesAvailability>> callback);

    void getLeaveRequestRPC(Integer employeeID, AsyncCallback<NewLeaveRequest> callback);

    void createAttendaceRawDataRecords(Integer employeeID, Integer yearDiff, AsyncCallback<Void> callback);

    void validateAllowanceLimit(NewLeaveRequest leaveRequest, AsyncCallback<NewLeaveRequest> async);

    void getBenefitRequests(Integer objectID, AsyncCallback<BenefitRequestItem> async);

    void saveBenefitRequest(BenefitRequestItem item, AsyncCallback<Integer> async);

    void getTotalAndLeftRequest(Integer employeeID, Integer benefitID, DateNonConvertable date, AsyncCallback<EmployeeLeaveStatusListItem> callback);

    void getBenefitRequestList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<BenefitRequestItem>> abstractAsyncCallback);

    void deleteBenefitRequest(Integer objectID, AsyncCallback<Void> abstractAsyncCallback);

    void getBenefitRequestsFacetFilterData(FacetFilterRpc facetFilterBenefitRequests, AsyncCallback<FacetFilterRpc> async);

    void changeBenefitRequestStatus(Integer objectID, String status, String note, Double requestedQuantity, AsyncCallback<Integer> async);

    void getCurrentUser(Integer employeeID, AsyncCallback<SelectItem> async);

    void getLRYear(Integer employeeID, Integer currentYear, AsyncCallback<Integer> abstractAsyncCallback);

    void getEmployeesMap(ListingFilterParameter filterParametrs, AsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>> abstractAsyncCallback);

    void getAnnnualLeaveBalanceReport(ListingFilterParameter filterParameter, AsyncCallback<ListResult<LeaveBalanceReport>> abstractAsyncCallback);

    void getAnnnualLeaveBalanceReportProrataBased(ListingFilterParameter filterParameter, AsyncCallback<ListResult<LeaveBalanceReport>> abstractAsyncCallback);

    void getEmployeeLeaveBalanceReport(ListingFilterParameter filterParameter, AsyncCallback<ListResult<LeaveBalanceReport>> abstractAsyncCallback);

    void saveLeaveBalanceReportData(LeaveBalanceReport leaveBalanceReport, AsyncCallback<TestRPC> callback);

    void getBenefitListAsSelectItems(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);

    void getLeaveRequestList(ListingFilterParameter fp, AsyncCallback<ListResult<LeaveRequestLisItem>> callback);

    void loadLeaveRequestHistory(Integer objectId, AsyncCallback<List<HistoryNote>> callback);

    void createLeaveRequestHistory(Integer leaveRequestId, HistoryListItem hisItem, AsyncCallback<Integer> async);

    void getEndDateForLeaveRequest(ListingFilterParameter fp, AsyncCallback<Date> async);

    void getPeriodList(Integer employeeID, Integer requestID, String reasonCode, boolean isSummary, boolean isRecalculate, AsyncCallback<ArrayList<LaborPeriodRequest>> async);

    void getTakenDaysByPeriod(Integer periodID, AsyncCallback<ArrayList<LaborPeriodRequest>> async);

    void getEndDate(Integer employeeID, Date startDate, Double overAllLeaveDays, String reasonCode, AsyncCallback<Date> async);

    void deleteSickRequestListByParent(Integer parentID, AsyncCallback<Void> async);

    void setStartDueDates(ArrayList<MultiLeaveDTO> list, Date startDate, Integer employeeID, String reasonCode, AsyncCallback<ArrayList<MultiLeaveDTO>> async);

    void hasAccessInsertSickRequestsByPeriod(Integer employeeId, NewLeaveRequest leaveRequest, boolean recalculate, AsyncCallback<String> async);

    void restoreLeave(Integer employeeID, Integer sickID, DateNonConvertable start, DateNonConvertable end, String reasonCode, AsyncCallback<Void> async);

    void saveBackupEmployeesFromSummary(Integer leaveRequestId, ArrayList<BackupEmployeeItem> backupEmployeeItemList, AsyncCallback<Void> async);

    void saveLREditCellValue(LeaveRequestLisItem rowValue, String columnCodeName, AsyncCallback<Boolean> async);

    void getReasons(Integer userId, boolean withDrafts, AsyncCallback<SelectItem[]> async);

    void getTimeSlotShortNameForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void getBrigadasForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void saveShiftSettings(ShiftSettingsItem item, AsyncCallback<Void> async);

    void getShiftSettings(Integer objectID, AsyncCallback<ShiftSettingsItem> async);

    void getTimeSlotInterval(Integer id, AsyncCallback<String[]> async);

    void deleteShiftSettings(Integer objectID, AsyncCallback<Void> async);

    void generateGoalNumber(String categoryType, AsyncCallback<NumberData> async);

    void loadGoalHistory(Integer objectId, AsyncCallback<List<HistoryNote>> callback);

    void createGoalHistory(Integer objectId, HistoryListItem hisItem, AsyncCallback<Integer> async);

    void deleteGoalHistory(Integer commentID, AsyncCallback<Void> async);

    void getProject(Integer objectId, AsyncCallback<ProjectSingleItem> async);

    void getReasonsWithLimit(Integer userId, boolean withDrafts, String searchText, Integer limit, AsyncCallback<SelectItem[]> async);

    void getEmployeeAdditonalAllowances(Integer employeeID, Integer sickRequestID, Date startDate, Boolean isSummary, Boolean isRecalculate, AsyncCallback<ArrayList<LaborPeriodRequest>> dateAsyncCallback);

    void getEmployeeInOutRecords(Integer employeeID, DateNonConvertable date, AsyncCallback<List<InOutPairDto>> callback);

    void getLateAndEarlyPercentage(DateNonConvertable fromDate, Integer employeeID, AsyncCallback<Double[]> async);

    void getAttendanceMarkList(ListingFilterParameter fp, AsyncCallback<ListResult<AttendanceMarkListItem>> callback);

    void getAttendanceMarkFacetData(com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc fp, AsyncCallback<com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc> callback);

    void approveAttendanceMarks(ArrayList<Integer> fingerprintIds, AsyncCallback<Void> callback);

}
