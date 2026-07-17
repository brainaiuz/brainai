package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLabourPeriod;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public interface LabourPeriodManager extends Manager<EdsLabourPeriod> {

    List<EdsLabourPeriod> periodList(ListingFilterParameter param);

    List<EdsLabourPeriod> periodListByEmployeeId(Integer employeeID);

    Boolean isUsedEmployeeLabourPeriod(Integer employeeID);

    EdsLabourPeriod getById(Integer periodID);

    void updatePeriods(Double allowance);

    void clearEmployeeLabourPeriod(Integer employeeID);

    List<EdsLabourPeriod> sickRequestPeriods(Integer sickRequestID, boolean orderByDesc);

    List<EdsSickRequest> getSickRequestByPeriods(Integer labourPeriodId);

    LinkedHashMap<Integer, Double> getSickDaysByPeriod(Integer sickRequestID);

    Double getTotalTakenLeaveDaysByPeriodId(Integer periodId, boolean isApproved);

    Double getTotalTakenLeaveDaysByPeriodId(Integer periodId, Integer leaveId);

    List<Object[]> getPeriodLeavesData(Integer periodId, boolean isApproved);

    List<Object[]> getDayTypesByPeriod(Integer requestID);

    EdsLabourPeriod getByEmployeeIdAndStartDate(Integer objectID, String startDate);

    Double getLeaveDaysByPeriodIdAndExcludeSick(Integer objectID, boolean isApproved, boolean isRecalculate, Integer requestID);

    EdsLabourPeriod getPeriodByEmployeeIdAndDate(Integer employeeID, Date startDate, Date endDate);

    List<EdsLabourPeriod> periodListByEmployee(Integer employeeID);
}
