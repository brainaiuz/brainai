package com.edatasite.workforce.gwt.availability.server.app;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeLeaveStatusListItem;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.availability.client.rpc.InOutPairDto;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveBalanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestComment;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.availability.client.rpc.NewLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.core.client.rpc.CalendarItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.InOutItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.LeaveRequestChartRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: Sherali
 * Date: 02.12.2008
 * Time: 14:53:45
 */
public interface AvailabilityServiceLocal {

    LeaveRequestComment[] getComments(Integer leaveRequestID);

    SelectItem[] getTimeslotList();

    TimeslotItem getEmpTimeslot(Integer employeeID);

    List<TimeslotItem> getEmpShiftTimeslotList();

    TimeslotItem getTimeslot(Integer objectID);

    void createRecurringHoliday();

    String[] getLeaveRequestStats(EdsSickRequest request);

    InOutItem getDailyInTimePerUser(Integer int_employeeID, Date startDate, Date endDate);

    List<SelectItem> getLeaveRequrestTypes();

    void updateLRHours(EdsEmployee employee, Date updateLRFrom, Date to);

    void updateEmployeeLeaveDuration(EdsUser user);

    int getEmployeeWorkDaysCountInMonth(Integer employeeId, Date startDate, Date endDate);

    SelectItem getCurrentUser(Integer employeeID);

    void createEmployeeAttendanceData(Integer companyID, Calendar from, Calendar to, Integer employeeId);

    void createAttendaceRawDataRecords(Integer employeeID, Integer yearDiff);

    Integer getLeaveRequestListCount(ListingFilterParameter fp);

    SelectItem[] getCompanyEmployeesAsAdmin();

    SelectItem[] getSickStatusList();

    EdsTimeSlotItem[] getTimeslotMinutesArray(EdsTimeSlot timelot);

    EmployeeLeaveStatusListItem getTotalAndLeftRequest(Integer employeeID, Integer benefitID, DateNonConvertable date);

    BenefitRequestItem getBenefitRequests(Integer objectID);

    void deleteBenefitRequest(Integer objectID);

    Integer saveBenefitRequest(BenefitRequestItem item);

    Integer changeBenefitRequestStatus(Integer objectID, String status, String note, Double requestedQuantity);

    ListResult<BenefitRequestItem> getBenefitRequestList(ListingFilterParameter fp);

    ListResult<HolidayItem> getHolidays(ListingFilterParameter filterParameter);

    void copyLastYearLeaveAllowanceMinutes(Integer currentYear);

    SelectItem[] getReasons(Integer userId);

    SelectItem[] getReasons(Integer userId, Integer year);

    EmployeeLeaveStatusListItem getEmployeeLeaveBalanceBase(Integer employeeID, String reasonCode, Date startDate, Date endDate);

    Integer createLeaveRequest(NewLeaveRequest leaveRequest);

    StatisticsLeaveRequest getLeaveRequest(Integer requestID);

    void setComment(Integer leaveRequestID, String comment);

    void deleteLeaveRequestComment(Integer commentID);

    void deleteRequest(Integer id);

    void updateApprove(String approve, Integer requestID, boolean approvalForAll);

    String hasAccessInsertRequest(Integer employeeId, NewLeaveRequest leaveRequest, DateNonConvertable startDate, DateNonConvertable endDate, boolean recalculate);

    String hasAccessInsertSickRequestsByPeriod(Integer employeeId, NewLeaveRequest leaveRequest, boolean recalculate);

    CalendarItems getCalendarItems(Integer employeeID, String reasonCode, DateNonConvertable displayFirstDay, DateNonConvertable displayLastDay);

    ListResult<LeaveRequestLisItem> getLeaveRequestList(ListingFilterParameter fp);

    LeaveRequestChartRpc getLeaveRequestChartData(ListingFilterParameter fp);

    void createOrUpdateLeaveAllowance(Integer id);

    void createOrUpdateLeaveAllowance(Integer id, EdsLeaveReason reason, Date effectiveDate);

    void createOrUpdateLeaveAllowanceWithDays(Integer id, EdsLeaveReason reason, Date effectiveDate, Double leaveDays);

    String getLeaveRequestSolrQuery(ListingFilterParameter fp, EdsUser edsUser);

    ListResult<LeaveBalanceReport> getAnnnualLeaveBalanceReportProrataBased(ListingFilterParameter fp);

    void indexLeaveRequests(SolrReindexRpc rpc);

    void createTimeslot(TimeslotItem item);

    Double getLeaveDays(ListingFilterParameter fp, DateNonConvertable date, DateNonConvertable enddate);

    void restartLeaveRequestNumber();

    void leaveRequestCorrection(String reasonShortName);

    void updateOrRemoveLabourPeriod(Integer objectID);

    String getLeaveDaysCount(ListingFilterParameter fp, DateNonConvertable date, DateNonConvertable enddate);

    List<BackupEmployeeItem> getBackupEmployeesForLeaveRequest(Integer sickRequestId);

    List<InOutPairDto> getEmployeeInOutRecords(Integer employeeID, DateNonConvertable fromDate, DateNonConvertable toDate, Boolean includeSnapshots);

    List<InOutPairDto> getEmployeeInOutDailySummary(Integer employeeID, DateNonConvertable fromDate, DateNonConvertable toDate, Boolean includeSnapshots);
}
