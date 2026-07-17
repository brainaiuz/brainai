package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.gwt.availability.client.rpc.AttendanceStatusDto;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.DailyOvertimeData;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Farhod
 * Date: 03/03/12
 * Time: 11:01
 */
public interface AttendanceRawDataManager extends Manager<EdsAttendanceRawData> {

    Date getLastEnteredDateForEmployee(Integer employeeID);

    List<EdsAttendanceRawData> getAttendanceRawDataByDates(Date from, Date to, Integer employeeID);

    List<Date> getLeaveDates(Date from, Date to, Integer employeeID);

    Map<Integer, Integer> getPlannedDaysForEmployeeBatch(Date startDate, Date endDate, Set<Integer> employeeIds);

    Map<Integer, Integer> getLeaveDatesCountBatch(Date from, Date to, Set<Integer> employeeIds);

    EdsAttendanceRawData getAttendanceRawDataByDate(Date from, Integer employeeID);

    Double[] getLeaveRequestMinutes(ListingFilterParameter fp);

    Integer getMonthlyPlanned(Integer year, Integer month, Integer employeeID);

    List<Date> getHolidayDays(ListingFilterParameter fp);

    Date getLastDateOfLR(Integer employeeID);

    List<Object> getDailyInTimes(Integer int_employeeID, Date start_date, Date end_date);

    List<Date> getAllDaysWithIntervel(ListingFilterParameter fp);

    List<Date> getWorkingDays(ListingFilterParameter fp, String datesType);

    List<Date> getWorkingDays(ListingFilterParameter fp);

    List<DailyOvertimeData> getDailyOvertimeData(ListingFilterParameter lfp);

    void updateHolidays(Date from, Date to, List<Integer> locationId, boolean b, boolean takenFromAllowance);

    void updateHolidays(EdsHoliday holiday, Integer employeeId, boolean isHoliday, boolean holidayfromannualleave);

    Date getEndDateForLeaveRequest(ListingFilterParameter fp);

    void updateAttendanceRawDataByEmployeeAndTimeslotId(Integer employeeID, Date from, Date to);

    Object[] getWorkingDate(Integer employeeID, Date date);

    void restoreAttendanceRawData(Integer employeeID, Date start, Date end);

    long getPlannedHoursForEmployee(Date startDate, Date endDate, Integer employeeId);

    int getPlannedDaysForEmployee(Date startDate, Date endDate, Integer employeeId);

    Map<Date, BigDecimal> getWorkingHours(ListingFilterParameter fp, String datesType);

    Set<Date> getLeaveDays(ListingFilterParameter fp, String datesType);

    List<AttendanceStatusDto> getEmployeeDailyLeaveStatus(Integer userId, Date fromDate, Date toDate);
}