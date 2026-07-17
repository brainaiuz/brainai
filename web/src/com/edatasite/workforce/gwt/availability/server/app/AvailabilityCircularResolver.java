package com.edatasite.workforce.gwt.availability.server.app;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.server.pojo.Holiday;
import com.edatasite.workforce.gwt.availability.server.pojo.HolidayIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 6/14/12
 * Time: 4:41 PM
 */
public interface AvailabilityCircularResolver {

    String FROM_RESOURCE_UTIL = "FROM_RESOURCE_UTIL";

    ArrayList<Holiday> getCompanyHolidayList();

    ArrayList<Holiday> getCompanyHolidayList(EdsUser user);

    ArrayList<Holiday> getHolidaysList(EdsEmployee emp);

    ArrayList<Calendar> getEmployeeLeaves(Date tstartDate, Date tdueDate, EdsEmployee emp);

    ArrayList<Holiday> getHolidaysListByLocation(EdsLocation location, Date start, Date end);

    Calendar getCustomDateG(Date d);

    Holiday isHoliday(Calendar d, ArrayList<Holiday> holidays);

    boolean isLrDay(Calendar d, ArrayList<Calendar> lrDays);

    HolidayIndicator[] getMonthlyHoliday(Map<Integer, Integer> comTimeSlot,
            Date startDate, int daysInMonth, ArrayList<Holiday> companyHolidayDays, boolean isShift);

    HashMap<String, Integer> getMonthHolidaysByPeriod(Map<Integer, Integer> comTimeSlot,
                                                                           Date startDate, Date endDate, int daysInMonth, ArrayList<Holiday> companyHolidayDays);

    Map<Integer, Integer> getCompanyTimeSlot();

    //
    void createOrUpdateTimeSheetDataForEmployeeAndEmployeeTask(EdsEmployee employee, EdsEmployeeTask empTask, EdsEmployeeTask oldEmployeeTask);

    void createOrUpdateTimeSheetDataWithDailyEstimatedTime(EdsEmployee employee, EdsEmployeeTask empTask, ArrayList<Calendar> availableDays, Integer dailyEstimatedTime, Integer dailyLoadQ);

    void createOrUpdateTimeSheetDataWithDailyEstimatedTime(EdsEmployee employee, EdsEmployeeTask empTask, ArrayList<Calendar> availableDays, Integer dailyEstimatedTime, Integer dailyLoadQ, String from);

    void createOrUpdateTimeSheetData(EdsEmployee employee, EdsEmployeeTask empTask, Calendar availDay, Integer dailyEstimatedTime, String from);

    void copyEmployeeTaskDailyLoadToTimeSheet();

    void copyEmployeeTaskDailyLoadToTimeSheet(Integer companyID);

    void updateEmployeeTaskEstimatedTime(EdsEmployeeTask employeeTask, Integer totallyEmployeeTaskEstimatedTime);

    Map<Integer, Integer> getUserTimeSlot(EdsUser user);

}