package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.AnnualLeaveItem;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public interface AvailabilityService extends RemoteService {

    String hasAccessInsertRequest(Integer employeeId, NewLeaveRequest leaveRequest, DateNonConvertable startDate, DateNonConvertable endDate, boolean recalculate);

    String hasAccessInsertSickRequestsByPeriod(Integer employeeId, NewLeaveRequest leaveRequest, boolean recalculate);

    Integer createLeaveRequest(NewLeaveRequest leaveRequest);

    SelectItem[] getReasons(Integer userId);

    SelectItem[] getReasonsWithLimit(Integer userId, boolean withDrafts, String searchText, Integer limit);

    SelectItem[] getReasons(Integer userId, boolean withDrafts);

    SelectItem[] getReasons(Integer userId, Integer year);

    StatisticsLeaveRequest getLeaveRequest(Integer requestID);

    void updateMultipleRequests(String code, ArrayList<Integer> selectItems, String note);

    void updateApprove(String approve, Integer requestID, boolean approvalForAll);

    ListResult<TimeslotItem> getTimeslots(ListingFilterParameter fp);

    TimeslotItem getTimeslot(Integer objectID);

    ShiftSettingsItem getShiftSettings(Integer objectID);

    String[] getTimeSlotInterval(Integer id);

    void createTimeslot(TimeslotItem item);

    void updateTimeslot(TimeslotItem item);

    void saveShiftSettings(ShiftSettingsItem item);

    TimeSlotHistoryList[] getTimeSlotHistories(Integer timeSlotID, boolean isShift);

    HolidayHistoryList[] getHolidayHistories(Integer holidayID);

    void deleteTimeslot(Integer objectID);

    void deleteShiftSettings(Integer objectID);

    void deleteTimeslot(Integer objectID, Integer toTimeslot);

    TimeslotItem getEmpTimeslot(Integer employeeID);

    void createOrUpdateHoliday(HolidayItem newHoliday);

    ListResult<HolidayItem> getHolidays(ListingFilterParameter filterParametrs);

    HolidayItem getHoliday(Integer holidayID);

    void deleteHoliday(Integer holidayID);

    SelectItem[] getTimeslotList();

    LeaveSettingsItem getLeaveRequestSettingsData();

    void updateEmpBenefitAllowance(AnnualLeaveItem item);

    void deleteRequest(Integer id);

    void deleteSickRequestListByParent(Integer parentID);

    CalendarItems getCalendarItems(Integer employeeID, String reasonCode, DateNonConvertable displayFirstDay, DateNonConvertable displayLastDay);

    SelectItem[] getTeamsList();

    ListResult<TimeslotSetting> getEmployeesAndTimeslot(ListingFilterParameter filterParametrs);

    ListResult<AnnualLeaveItem> getEmployeeBenefitList(ListingFilterParameter filterParametrs);

    EmployeeViewItem getEmployee(Integer objectID);

    void setComment(Integer leaveRequestID, String comment);

    LeaveRequestComment[] getComments(Integer leaveRequestID);

    EmployeeAttendanceReport getEmployeeAttendanceReport(ListingFilterParameter fp, int daysInMonth);

    TimeSlot getEmployeeTimeSlot(Integer employeeId, DateNonConvertable date);

    String getLeaveDaysCount(ListingFilterParameter fp, DateNonConvertable date, DateNonConvertable enddate);

    Double getLeaveDays(ListingFilterParameter fp, DateNonConvertable date, DateNonConvertable enddate);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeesByTeamsList(Integer id);

    ListResult<TeammatesAvailability> getTeamMates(ListingFilterParameter filterParameter);

    NewLeaveRequest getLeaveRequestRPC(Integer employeeID);

    ArrayList<LaborPeriodRequest> getPeriodList(Integer employeeID, Integer requestID, String reasonCode, boolean isSummary, boolean isRecalculate);

    ArrayList<LaborPeriodRequest> getTakenDaysByPeriod(Integer periodID);

    void createAttendaceRawDataRecords(Integer employeeID, Integer yearDiff);

    NewLeaveRequest validateAllowanceLimit(NewLeaveRequest leaveRequest);

    BenefitRequestItem getBenefitRequests(Integer objectID);

    Integer saveBenefitRequest(BenefitRequestItem item);

    EmployeeLeaveStatusListItem getTotalAndLeftRequest(Integer employeeID, Integer benefitID, DateNonConvertable date);

    ListResult<BenefitRequestItem> getBenefitRequestList(ListingFilterParameter fp);

    FacetFilterRpc getBenefitRequestsFacetFilterData(FacetFilterRpc benefitRequestsData);

    void deleteBenefitRequest(Integer objectID);

    Integer changeBenefitRequestStatus(Integer objectID, String status, String note, Double requestedQuantity);

    SelectItem getCurrentUser(Integer employeeID);

    Integer getLRYear(Integer employeeID, Integer currentYear);

    HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getEmployeesMap(ListingFilterParameter fp);

    ListResult<LeaveBalanceReport> getAnnnualLeaveBalanceReport(ListingFilterParameter fp);

    ListResult<LeaveBalanceReport> getAnnnualLeaveBalanceReportProrataBased(ListingFilterParameter fp);

    ListResult<LeaveBalanceReport> getEmployeeLeaveBalanceReport(ListingFilterParameter fp);

    TestRPC saveLeaveBalanceReportData(LeaveBalanceReport leaveBalanceReport);

    SelectItem[] getBenefitListAsSelectItems(ListingFilterParameter fp);

    ListResult<LeaveRequestLisItem> getLeaveRequestList(ListingFilterParameter fp);

    List<HistoryNote> loadLeaveRequestHistory(Integer objectId);

    Integer createLeaveRequestHistory(Integer leaveRequestId, HistoryListItem hisItem);

    void deleteLeaveRequestComment(Integer commentID);

    Date getEndDateForLeaveRequest(ListingFilterParameter fp);

    void updateApprove(String approve, Integer requestID, boolean approvalForAll, String rejectionReason);

    void updateApprove(String approve, Integer requestID, boolean approvalForAll, String rejectionReason, boolean fromApi);

    Date getEndDate(Integer employeeID, Date startDate, Double overAllLeaveDays, String reasonCode);

    ArrayList<MultiLeaveDTO> setStartDueDates(ArrayList<MultiLeaveDTO> list, Date startDate, Integer employeeID, String reasonCode);

    void restoreLeave(Integer employeeID, Integer sickID, DateNonConvertable start, DateNonConvertable end, String reasonCode);

    void saveBackupEmployeesFromSummary(Integer leaveRequestId, ArrayList<BackupEmployeeItem> backupEmployeeItemList);

    boolean saveLREditCellValue(LeaveRequestLisItem rowValue, String columnCodeName);

    SelectItem[] getTimeSlotShortNameForLookUp(ListingFilterParameter filterParameter);

    SelectItem[] getBrigadasForLookUp(ListingFilterParameter filterParameter);

    NumberData generateGoalNumber(String categoryType);

    List<HistoryNote> loadGoalHistory(Integer objectId);

    Integer createGoalHistory(Integer objectId, HistoryListItem hisItem);

    void deleteGoalHistory(Integer commentID);

    ProjectSingleItem getProject(Integer objectId);

    ArrayList<LaborPeriodRequest> getEmployeeAdditonalAllowances(Integer employeeID, Integer sickRequestID, Date startDate, Boolean isSummary, Boolean isRecalculate);

    List<InOutPairDto> getEmployeeInOutRecords(Integer employeeID, DateNonConvertable date);

    Double[] getLateAndEarlyPercentage(DateNonConvertable fromDate, Integer employeeID);

    ListResult<AttendanceMarkListItem> getAttendanceMarkList(ListingFilterParameter fp);

    FacetFilterRpc getAttendanceMarkFacetData(FacetFilterRpc fp);

    void approveAttendanceMarks(ArrayList<Integer> fingerprintIds);

    class App {

        public static AvailabilityServiceAsync get() {
            ServiceDefTarget target = GWT.create(AvailabilityService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/availability");
            return (AvailabilityServiceAsync) target;
        }
    }
}
