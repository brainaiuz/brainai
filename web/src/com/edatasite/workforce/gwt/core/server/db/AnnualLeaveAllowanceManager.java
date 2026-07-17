package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAnnualLeaveAllowance;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveBalanceReport;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Farhod
 * Date: 04/04/12
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public interface AnnualLeaveAllowanceManager extends Manager<EdsAnnualLeaveAllowance> {

    EdsAnnualLeaveAllowance getLeaveAllowanceByReason(Integer year, Integer employeeID, String reasonCode, Date effectiveDate);

    Map<String, Double> getLeaveEmployeeAllowance(Integer year, Integer employeeId);

    Double getLeaveAllowanceCustom(Integer employeeID);

    List<EdsAnnualLeaveAllowance> getLeaveAllowance(Integer year, List<Integer> employees);

    List<EdsAnnualLeaveAllowance> getLeaveAllowancesByReason(Integer year, List<Integer> employeeIDs, String reasonCode);

    List<EdsAnnualLeaveAllowance> getLeaveAllowancesByEmployee(Integer employeeID, String reasonCode);

    HashMap<Integer, EdsAnnualLeaveAllowance> getAllowancesMapByYearAndReason(Integer year, String reasonCode);

    ArrayList<LeaveBalanceReport> getAnnnualLeaveBalanceReport(ListingFilterParameter fp);

    ListResult<LeaveBalanceReport> getAnnnualLeaveBalanceReportProrataBased(ListingFilterParameter fp);

    Integer getLeaveBalanceCount(ListingFilterParameter fp);

    Map<Integer, EdsAnnualLeaveAllowance> getAllowancesMapByYearAndReasonAndEmployee(Integer year,
                                                                                     String reasonCode,
                                                                                     List<Integer> employeeIds);

    Integer getLeaveAllowanceDays(int year, Integer employeeId, String reasonCode);

    EdsAnnualLeaveAllowance getLeaveAllowanceByPeriodStartDate(Integer employeeID, Date periodStartDate, String reasonCode);

    Map<Integer, Integer> getLastYearMinutesMapByYearAndReasonAndEmployee(Integer year, String reasonCode, List<Integer> employeeIds);
}
