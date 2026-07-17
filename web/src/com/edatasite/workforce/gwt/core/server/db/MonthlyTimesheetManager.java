package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMonthlyTimesheet;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.MonthlyOvertimeData;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.MonthlyOvertimeDataWithRates;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 */
public interface MonthlyTimesheetManager extends Manager<EdsMonthlyTimesheet> {
    String getMonthYear(Date date);

    Map<Integer, EdsMonthlyTimesheet> getMonthlyTimesheetItems(Integer projectID, Integer employeeID, DateNonConvertable selectedDate);

    void deleteByProjectIDandMonth(Integer projectId, Integer projectEmployeeID, String monthYear);

    List<MonthlyOvertimeData> getMonthlyTimesheetDataForPayroll(ListingFilterParameter lfp);

    MonthlyOvertimeDataWithRates getMonthlyTimesheetDataWithOvertimeRatesForPayroll(ListingFilterParameter lfp);

    List<EdsMonthlyTimesheet> getEmployeeOtherProjectTimeEntiries(Integer employeeID, String monthYear, Integer currentProjectID);

    List<MonthlyOvertimeData> getPrevMonthRemainingTimes(ListingFilterParameter fp, boolean currentMonth);
}
