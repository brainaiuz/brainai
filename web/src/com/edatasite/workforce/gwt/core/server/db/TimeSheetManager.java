package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployeeWageClientRateHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeSheetSuggestItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface TimeSheetManager extends Manager<EdsTimeSheet> {
    List<EdsTimeSheet> getTimeSheets(EdsEmployeeTask task);

    List<EdsTimeSheet> getTimeSheets(Integer taskID, Integer employeeID);

    List<EdsTimeSheet> getTimeSheetByTaskID(Integer taskID, Integer employeeTaskID);

    List<EdsTimeSheet> getTimeSheets(Integer taskID, Integer employeeID, Date date);

    List<TaskTimeSheetSuggestItem> getTimeSheetsForSuggest(Integer taskID, Integer employeeID);

    List<EdsTimeSheet> getNotFilledTimesheetForToday(Date date);

    Integer getTotalTimeSheets(EdsEmployeeTask employeeTask);

    Integer getTotalDailyEstimatedTimesTimeSheet(EdsEmployeeTask employeeTask);

    List getEmployeeTaskTotalTimeSheets(String emplTaskIds, Integer employeeID);

    Integer getEmployeeTaskTotalTimeSheet(String taskIds, Integer employeeId, Date date);

    List<TaskInvolvedMember> getSumTimeSheets(Integer taskID);

    List<EdsTimeSheet> getTimeEntries(Integer taskID);

    void updateTimeEntries(Integer taskID, Integer projectID);

    void updateDailyEstimatedTimeByEmployeeTask(EdsEmployeeTask employeeTask);

    List<EdsTimeSheet> list(EdsEmployee employee, Date startDate, Date endDate);

    Long getTodayEntered(EdsUser user);

    Long getThisWeekEntered(EdsUser user);

    Long getThisMonthEntered(EdsUser user);

    Long getCompanyEntered(EdsCompany company, String startDateFormat, String endDateFormat);

    List<Object[]> getCompanyTeamsByCompanyId(Integer objectID, String startDateFormat, String endDateFormat);

    List<Integer> getTimeSheetTasksByRegDate(Date sTime, Date eTime);

    List<Integer> getCompaniesByTSDate(Date sTime, Date eTime);

    Integer getCompanyTimeSpentByDate(EdsCompany company, Date sTime, Date eTime);

    List getTimeSheetSummaryData(String range, Integer objectID, String startDateFormat, String endDateFormat);

    List getClientSummaryData(Integer objectID, String startDateFormat, String endDateFormat);

    List<EdsEmployee> getTimesheetEmployeesByProject(EdsProject project, Date sTime, Date eTime);

    List<EdsTimeSheet> getTimeSheetDataByProjectAndEmployee(EdsProject project, EdsEmployee employee, Date sTime, Date eTime);

    List<EdsTimeSheet> getTimesheetEntriesForApproval(List<Integer> projectIDs, EdsEmployee employee, Date sTime, Date eTime, EdsReference waiting, EdsReference approved);

    List<EdsTimeSheet> getTimesheetEntriesForApprovalByProjectAndTaskIds(LinkedHashMap<String, String> projectTasks, EdsEmployee employee, Date sTime, Date eTime, EdsReference waiting, EdsReference approved);

    List<EdsEmployee> getTimeSheetEmployeesByDepartment(EdsDepartment department, Date sTime, Date eTime);

    List<EdsTimeSheet> getTimeSheetDataByDepartmentAndEmployee(EdsDepartment department, EdsEmployee employee, Date sTime, Date eTime);

    Date getTimeSheetMaxDateByTaskID(EdsEmployeeTask task);

    Integer getProjectActualTime(EdsProject project, Integer type);

    Integer getDepartmentActualTime(EdsDepartment department, Integer type);

    List<EdsTimeSheet> getTimeSheets(EdsEmployee employee, EdsProject project, EdsCrmAccount client, EdsDepartment department, Integer viewAsId, Date fromDate, Date toDate);

    List<EdsTimeSheet> getTimeSheetIdIn(String ids);

    List<EdsTimeSheet> getEmployeeTaskTimeEntries(EdsEmployeeTask eTask);

    Integer getSumTimeSpentEmployeeInTheTask(Integer taskID, Integer employeeID);

    String getEmployeeLastDepartment(Integer employeeId);

    Integer getWorkedTimeForMobile(Integer employeeID, Integer taskID, Date date);

    EdsTimeSheet getTimeshetForMobile(EdsEmployeeTask task, Date date);

    EdsTimeSheet getTimeshetForMobile(Integer employeeID, Integer taskID, Date date);

    List<EdsTimeSheet> getTimesheetsByNotExportToQB(Integer limit);

    Date getFirstTimesheetDateForTask(EdsTask task);

    Date getLastTimesheetDateForTask(EdsTask task);

    EdsTimeSheet getTimeSheet(EdsEmployeeTask employeeTask, Date timesheetDate);

    EdsTimeSheet getTimeSheet(Integer employeeID, Integer taskID, Date date);

    Long getMonthlyApprovedTimeSpents(EdsUser user);

    BigDecimal getApprovedTimeSpentInterval(Integer employeeID, Date from, Date to, Integer projectId);

    String getSumEmployeeSpentToTaskInterval(EdsEmployeeTask employeeTask, Date from, Date to);

    Integer getTotalTimeSheetHours(Integer employeeTaskID, Integer statusID);

    EdsProjectEmployeeWageClientRateHistory getProjectEmployeeWageClientRateByDate(Date date, Integer projectEmployeeId);

    Double[] getProjectCostAndTimeSpent(Integer projectID, Integer employeeID);

    HashMap<Integer, Double[]> getCostAndTimeSpentOnProjects(String projectIDs);

    HashMap<Integer, Double> getProjectTimeSpents(String projectIds, String status);

    HashMap<Integer, Double> getTaskTimeSpents(String taskIds, String status);

    Double[] getTimeSpentByEmployee(Integer projectID, Integer employeeId);

    HashMap<String, Double[]> getEmployeeCostAndTimeSpentOnProjects(Integer projectID);

    Integer getEstimatedTime(Integer projectEmployee, Integer prjectId);

    Double[] getTaskCostAndTimeSpent(Integer taskID);

    HashMap<Integer, Double[]> getCostAndTimeSpentOnTasks(String taskIDs);

    void deletePayslipIDsFromTimeSheet(Integer objectID, Integer employeeID);

    List<EdsTimeSheet> getTimeSheetsByPayslipIDs(Integer objectID, Integer objectID1);

    Integer getProjectApprovedTimesheetHours(Integer projectID);

    Integer getTimesheetCount(EdsEmployeeTask employeeTask, Date from, Date to);

    void updateDailyEstimatedTime(Integer id, Date start, Date end, Integer dailyEstimate);

    void updateDailyEstimatedTime(Integer employeeTaskid);

    ArrayList<Date> getTimesheetOldDates(Integer employeeId, EdsEmployeeTask edsEmployeeTask);

    void updateTimeSheetOldDataWithDailyEstimatedTime(Integer employeetaskId, Integer dailyEstimatedTime, List<Date> dates, boolean fromResourceUtil);

    void updateWageRate(Integer empID, Double wageRate, Date applyFrom);

    void updateClientChargeRate(Integer empID, Double clientChargeRate, Date applyFrom);

    List<TaskTimeEntriesItem> getProjectTimeEntiries(Integer projectID);

    boolean isTaskUsedInInvoice(Integer taskID);

    List<EdsTimeSheet> getTimesheetForTimeEntries(ListingFilterParameter fp);

    List<EdsTimeSheet> getDailyTimesheets(Date startDate, Date endDate);

    List<Object[]> getTimesheetForWeeklyRate(ListingFilterParameter fp);

    List<EdsAttendanceRawData> getHoursForPayrun(ListingFilterParameter fp);

    BigDecimal getTimeslotHours(ListingFilterParameter fp);

    List<EdsTimeSheet> getProjectTimsheets(ListingFilterParameter fp);
}
