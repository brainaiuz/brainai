package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.rpc.AttendanceItem;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public interface SickRequestManager extends Manager<EdsSickRequest> {

    List<EdsSickRequest> getList(ListingFilterParameter fp);

    List<EdsSickRequest> getRequestListByStartDate(EdsCrmAccount client, EdsProject project,
                                                   EdsDepartment department, EdsEmployee employee, Integer viewAsId, String groupByName, Date from, Date to);

    List<EdsSickRequest> getCalendarSickRequests(List<Integer> employeeIDs, Date start, Date end);

    //List getEmployeeAttendanceReport(ListingFilterParameter fp);

    ListResult<Object> getEmployeeAttendanceReport(ListingFilterParameter fp);

    //Integer getEmployeeAttendanceReportCount(ListingFilterParameter fp);

    List<Object> getEmployeeCalendarItems(Integer int_employeeID, Date startDateT, Date endDateT);

    List<EdsSickRequest> getSickRequestByEmployeeAndPeriod(EdsEmployee employee, Date from, Date to);

    LinkedHashMap<Integer, List<StatisticsLeaveRequest>> getSickRequestByEmployeeAndPeriod(Date from, Date to);

    List<EdsSickRequest> getNonPaidLeaveRequests(ListingFilterParameter fp);

    List<EdsSickRequest> getLeaveRequestList(ListingFilterParameter fp);

    List<EdsDepartment> getSickRequestDepartments(ListingFilterParameter fp);

    Integer getLeaveRequestListCount(ListingFilterParameter fp);

    EdsSickRequest getEmployeeLastApprovedLeaveRequest(Integer employeeId, String statusCode);

    List<EdsSickRequest> findApprovedLeaveRequestsByUserId(Integer userId, Date startDate, Date endDate);

    List<EdsSickRequest> findSameLeaveRequests(Integer leaveId, Integer userId, Date startDate, Date endDate, Integer reasonId);

    List<Integer> getLeaveRequestListForSolr(SolrReindexRpc solrReindex);

    List<Integer> getRequestIdsByIds(String ids);

    List<Integer> getIdsWithLimit(int startat, int limit);

    List<AttendanceItem> getEmployeeDurationItems(Integer objectID, Date time, Date time1);

    List<EdsSickRequest> getLeaveRequestListByEmployee(Integer employeeID);

    List<EdsSickRequest> getDailyLeaveRequests(Date startDate, Date endDate);

    Integer getLeaveRequestLastIntNumber();

    Boolean getLeaveRequestByCode(String code, Integer objectID);

    List<EdsSickRequest> getLeaveRequestByParentId(Integer parentID);

    List<EdsSickRequest> findApprovedLeavesByExcludeSick(Integer objectID, int userId, Date startDate, Date endDate);

    Object getLeaveStats(String startDate, String endDate);

    List<EdsSickRequest> getLeaveRequestListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);
}
