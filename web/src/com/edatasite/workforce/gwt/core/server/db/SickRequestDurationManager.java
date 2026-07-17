package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsSickRequestDuration;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 4/19/13
 * Time: 10:54 AM
 */
public interface SickRequestDurationManager extends Manager<EdsSickRequestDuration> {

    Double[] getLeaveRequestMinutes(ListingFilterParameter fp);

    HashMap<Integer, Double[]> getAllowanceSpent(ListingFilterParameter fp);

    EdsSickRequestDuration getSickRequestDurationT(Date selectedDate, Integer sickRequestID, Integer periodId, String dayType);

    Integer getLeaveMinutes(Integer requestID, Date date);

    Map<Integer, Double[]> getAllowanceSpentByEmployees(ListingFilterParameter fp);

    Map<String, Double[]> getUserTakenDays(ListingFilterParameter fp, List<String> reasons);

    Double getUserSpentPaidAllowance(ListingFilterParameter fp);

    Map<Integer, Double[]> getEmployeesLeaveRequestsDuration(List<EdsSickRequest> requestList);

    Map<Integer, Double[]> getLeaveRequestsDurationByIds(String idList);

    HashMap<Integer, Double> getEmployeeLeaveDurations(ListingFilterParameter fp);

    void restoreDuration(String sickRequestIds, Date startDate, Date endDate);

    void deleteDurationByDateAndEmployeeId(Date startDate, Date endDate, Integer employeeId);
    List<EdsSickRequestDuration> getDurationByDateAndEmployeeId(Date date, EdsSickRequest sickRequest);

    Double getEmployeeLeaveDurationsByMonthAndYear(ListingFilterParameter fp);

    HashMap<Integer, Double> getDurationByDateAndEmployeeId(Date startDate, Date endDate, Integer employeeId);

    void deleteDurationBySickId(Integer sickId);
}
