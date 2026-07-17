package com.edatasite.workforce.gwt.availability.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsProjectEmployeeWageClientRateHistory;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTaskEstimateTimeSpentHistory;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.server.pojo.Holiday;
import com.edatasite.workforce.gwt.availability.server.pojo.HolidayIndicator;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.HolidayManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Ilhombek
 * Date: 6/14/12
 * Time: 4:41 PM
 */
@Transactional
@Service("availabilityCircularResolver")
public class AvailabilityCircularResolverImpl implements AvailabilityCircularResolver {
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private HolidayManager holidayManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;

    public ArrayList<Holiday> getCompanyHolidayList() {
        EdsUser user = employeeManager.getUser();
        if (user == null) {
            return new ArrayList<>();
        }
        return getHolidaysList(user.getCompany(), null);
    }

    public ArrayList<Holiday> getCompanyHolidayList(EdsUser user) {
        return getHolidaysList(user.getCompany(), null);
    }

    public ArrayList<Holiday> getHolidaysList(EdsEmployee emp) {
        return getHolidaysList(emp.getCompany(), emp.getLocation());
    }

    @Override
    public ArrayList<Calendar> getEmployeeLeaves(Date tstartDate, Date tdueDate, EdsEmployee emp) {
        List<Date> lrDates = attendanceRawDataManager.getLeaveDates(tstartDate, tdueDate, emp.getObjectID());

        ArrayList<Calendar> lrDays = new ArrayList<>();
        for (Date lrDate : lrDates) {
            Calendar tmpDate = new GregorianCalendar();
            tmpDate.setTime(lrDate);
            lrDays.add(tmpDate);
        }
        return lrDays;
    }

    private static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

    private ArrayList<Holiday> getHolidaysList(EdsCompany company, EdsLocation location) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowHolidays(true);//
        List<EdsHoliday> holidays = holidayManager.getHolidays(location, fp);

        ArrayList<Holiday> holidayDays = new ArrayList<>();
        for (EdsHoliday holiday : holidays) {

            Date startDateGmt = new DateNonConvertable(holiday.getStartDate()).getNonConvertedDate();
            Date endDateGmt = new DateNonConvertable(holiday.getEndDate()).getNonConvertedDate();
            boolean takenFromAllowance = holiday.isTakeAnnual();
            if (startDateGmt.getHours() > 0) {
                int hour = startDateGmt.getHours();
                if (hour > 12) {
                    hour = 24 - hour;
                    startDateGmt = new Date(startDateGmt.getTime() + (hour * 1000 * 60 * 60));
                    endDateGmt = new Date(endDateGmt.getTime() + (hour * 1000 * 60 * 60));
                } else {
                    startDateGmt = new Date(startDateGmt.getTime() - (hour * 1000 * 60 * 60));
                    endDateGmt = new Date(endDateGmt.getTime() - (hour * 1000 * 60 * 60));
                }
            }

            Calendar startDate = getCustomDateG(startDateGmt);
            Calendar endDate = getCustomDateG(endDateGmt);
            long cntDays = (endDate.getTimeInMillis() - startDate.getTimeInMillis()) / MILLISECONDS_IN_DAY;

            if (cntDays == 0) {
                holidayDays.add(new Holiday(startDate, takenFromAllowance));
            } else {
                if (cntDays > 0) {

                    for (int k = 0; k <= cntDays; k++) {
                        Calendar tmpDate = (Calendar) startDate.clone();
                        tmpDate.add(Calendar.DAY_OF_YEAR, k);
                        holidayDays.add(new Holiday(tmpDate, takenFromAllowance));
                    }
                }
            }
        }
        return holidayDays;
    }

    public ArrayList<Holiday> getHolidaysListByLocation(EdsLocation location, Date start, Date end) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowHolidays(true);//
        List<EdsHoliday> holidays = holidayManager.getCalendarHolidays(location, start, end);

        ArrayList<Holiday> holidayDays = new ArrayList<>();
        for (EdsHoliday holiday : holidays) {

            Date startDateGmt = new DateNonConvertable(holiday.getStartDate()).getNonConvertedDate();
            Date endDateGmt = new DateNonConvertable(holiday.getEndDate()).getNonConvertedDate();
            boolean takenFromAllowance = holiday.isTakeAnnual();
            if (startDateGmt.getHours() > 0) {
                int hour = startDateGmt.getHours();
                if (hour > 12) {
                    hour = 24 - hour;
                    startDateGmt = new Date(startDateGmt.getTime() + (hour * 1000 * 60 * 60));
                    endDateGmt = new Date(endDateGmt.getTime() + (hour * 1000 * 60 * 60));
                } else {
                    startDateGmt = new Date(startDateGmt.getTime() - (hour * 1000 * 60 * 60));
                    endDateGmt = new Date(endDateGmt.getTime() - (hour * 1000 * 60 * 60));
                }
            }

            Calendar startDate;
            if (start.before(startDateGmt)) {
                startDate = getCustomDateG(startDateGmt);
            } else {
                startDate = getCustomDateG(start);
            }
            Calendar endDate;
            if (end.before(endDateGmt)) {
                endDate = getCustomDateG(end);
            } else {
                endDate = getCustomDateG(endDateGmt);
            }

            long cntDays = (endDate.getTimeInMillis() - startDate.getTimeInMillis()) / MILLISECONDS_IN_DAY;

            if (cntDays == 0) {
                holidayDays.add(new Holiday(startDate, takenFromAllowance));
            } else {
                if (cntDays > 0) {

                    for (int k = 0; k <= cntDays; k++) {
                        Calendar tmpDate = (Calendar) startDate.clone();
                        tmpDate.add(Calendar.DAY_OF_YEAR, k);
                        holidayDays.add(new Holiday(tmpDate, takenFromAllowance));
                    }
                }
            }
        }
        return holidayDays;
    }


    public Calendar getCustomDateG(Date d) {
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTime(d);
        tmpCal.set(Calendar.HOUR_OF_DAY, 0);
        tmpCal.set(Calendar.MINUTE, 0);
        tmpCal.set(Calendar.SECOND, 0);
        return tmpCal;
    }

    /**
     * Generate holiday or not holiday days
     *
     * @param d        - current selected day
     * @param holidays - monthly or yearly holidays days
     * @return - selected day isHoliday or NOT
     */
    public Holiday isHoliday(Calendar d, ArrayList<Holiday> holidays) {
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTime(d.getTime());
        tmpCal.set(Calendar.HOUR_OF_DAY, 0);
        tmpCal.set(Calendar.MINUTE, 0);
        tmpCal.set(Calendar.SECOND, 0);
        tmpCal.set(Calendar.MILLISECOND, 0);

        for (Holiday dd : holidays) {
            if (dd.getCalendar().getTimeInMillis() == tmpCal.getTimeInMillis()) {
                return dd;
            }
        }
        return null;
    }

    public boolean isLrDay(Calendar d, ArrayList<Calendar> lrDays) {
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.setTime(d.getTime());
        tmpCal.set(Calendar.HOUR_OF_DAY, 0);
        tmpCal.set(Calendar.MINUTE, 0);
        tmpCal.set(Calendar.SECOND, 0);
        tmpCal.set(Calendar.MILLISECOND, 0);

        for (Calendar dd : lrDays) {
            if (dd.getTimeInMillis() == tmpCal.getTimeInMillis()) {
                return true;
            }
        }
        return false;
    }

    /* get Month holiday */
    public HolidayIndicator[] getMonthlyHoliday(Map<Integer, Integer> comTimeSlot,
            Date startDate, int daysInMonth, ArrayList<Holiday> companyHolidayDays, boolean isShift) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        HolidayIndicator[] month = new HolidayIndicator[daysInMonth + 1];
        for (int i = 1; i <= daysInMonth; i++) {
            if (isShift) {
                month[i] = new HolidayIndicator(null, 0);
            } else {
                int current = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                if (comTimeSlot != null && comTimeSlot.get(current) == 0) {
                    month[i] = new HolidayIndicator(null, 1); //there 1 == day off
                } else {
                    month[i] = new HolidayIndicator(null, 2); //there 2 == working day
                }
                if (companyHolidayDays != null && companyHolidayDays.size() > 0) {
                    Holiday isCHoliday = isHoliday(calendar, companyHolidayDays);
                    if (isCHoliday != null) {
                        month[i] = new HolidayIndicator(isCHoliday, 3);   //there 3 == company holiday
                    }
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (month[i] == null) {
                    month[i] = new HolidayIndicator(null, 0);
                }
            }
        }
        if (month[0] == null) {
            month[0] = new HolidayIndicator(null, 0);
        }
        return month;
    }

    public HashMap<String, Integer> getMonthHolidaysByPeriod(Map<Integer, Integer> comTimeSlot,
                                                                                  Date startDate, Date endDate, int daysInMonth, ArrayList<Holiday> companyHolidayDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        HashMap<String, Integer> month = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        while (!calendar.getTime().after(endDate)) {
            int current = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (comTimeSlot != null && comTimeSlot.get(current) == 0) {
                month.put(sdf.format(calendar.getTime()), 1); //there 1 == day off
            } else {
                month.put(sdf.format(calendar.getTime()), 2); //there 2 == working day
            }

            if (companyHolidayDays != null && !companyHolidayDays.isEmpty()) {
                Holiday isCHoliday = isHoliday(calendar, companyHolidayDays);
                if (isCHoliday != null) {
                    month.put(sdf.format(calendar.getTime()), 3);   //there 3 == company holiday
                }
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return month;
    }

    // get Company default time slot items
    public Map<Integer, Integer> getCompanyTimeSlot() {
//        EdsCompany company = employeeManager.getUser().getCompany();
        Map<Integer, Integer> param = new HashMap<>();
        final EdsTimeSlot mainTimeSlot = getCompanyMainTimeSlot();
        if (mainTimeSlot != null && mainTimeSlot.getDeleted() != null && !mainTimeSlot.getDeleted()) {
            Set<EdsTimeSlotItem> items = mainTimeSlot.getItems();
            for (EdsTimeSlotItem slot : items) {
                param.put(slot.getDay(), Math.abs(slot.getEndTime() - slot.getStartTime()));
            }
            return param;
        } else {
            return null;
        }
    }


    public Map<Integer, Integer> getUserTimeSlot(EdsUser user) {
        Map<Integer, Integer> param = new HashMap<>();
        final EdsTimeSlot mainTimeSlot;
        EdsEmployee employee = employeeManager.get(user.getObjectID());
        if (employee.getTimeSlot() != null) {
            mainTimeSlot = employee.getTimeSlot();
        } else {
            mainTimeSlot = getCompanyMainTimeSlot();
        }
        if (mainTimeSlot != null && mainTimeSlot.getDeleted() != null && !mainTimeSlot.getDeleted()) {
            Set<EdsTimeSlotItem> items = mainTimeSlot.getItems();
            for (EdsTimeSlotItem slot : items) {
                param.put(slot.getDay(), Math.abs(slot.getEndTime() - slot.getStartTime()));
            }
            return param;
        } else {
            return null;
        }
    }

    private EdsTimeSlot getCompanyMainTimeSlot() {
        int maxEmployeeCount = 0;
        EdsTimeSlot mainTimeSlot = null;
        for (EdsTimeSlot ts : timeSlotManager.getTimeslots()) {
            final int employeeCount = employeeManager.getEmpoyeeCountByTimeSlot(ts);
            if (employeeCount >= maxEmployeeCount) {
                maxEmployeeCount = employeeCount;
                mainTimeSlot = ts;
            }
        }
        return mainTimeSlot;
    }


    public void createOrUpdateTimeSheetDataForEmployeeAndEmployeeTask(EdsEmployee employee, EdsEmployeeTask empTask, EdsEmployeeTask oldEmpTask) {
        Set<EdsTimeSlotItem> timeSlotItem = employee.getTimeSlot().getItems();
        Map<Integer, Integer> available = new HashMap<>();
        for (EdsTimeSlotItem item : timeSlotItem) {
            available.put(item.getDay(), item.getEndTime() - item.getStartTime());
        }
        Calendar startDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();
        startDate.setTime(new Date(empTask.getTask().getStartDate().getTime() + employee.getUserTimezone().getRawOffset()));
        dueDate.setTime(new Date(empTask.getTask().getDueDate().getTime() + employee.getUserTimezone().getRawOffset()));

        Map<Date, Integer> oldTaskDailyEstimated = new HashMap<>();
        if (oldEmpTask != null) {
            List<EdsTimeSheet> timeSheetEntries = timeSheetManager.getEmployeeTaskTimeEntries(oldEmpTask);
            for (EdsTimeSheet timeSheet : timeSheetEntries) {
                oldTaskDailyEstimated.put(timeSheet.getDate(), timeSheet.getDailyEstimatedTime());
            }
        }
        while (dueDate.getTime().compareTo(startDate.getTime()) >= 0) {
            if (available.containsKey(startDate.get(Calendar.DAY_OF_WEEK) - 1) && available.get(startDate.get(Calendar.DAY_OF_WEEK) - 1) != null) {
                Calendar nonDate = Calendar.getInstance();
                nonDate.setTime(startDate.getTime());
                ServerUtils.setBeginningOfTheDay(nonDate);
                if (oldEmpTask == null) {
                    createOrUpdateTimeSheetData(employee, empTask, nonDate, empTask.getDailyLoad(), null);
                } else {
                    createOrUpdateTimeSheetData(employee, empTask, nonDate, oldTaskDailyEstimated.get(nonDate.getTime()), null);
                }
            }
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public void createOrUpdateTimeSheetDataWithDailyEstimatedTime(EdsEmployee employee, EdsEmployeeTask empTask, ArrayList<Calendar> availableDays, Integer dailyEstimatedTime, Integer dailyLoadQ) {
        createOrUpdateTimeSheetDataWithDailyEstimatedTime(employee, empTask, availableDays, dailyEstimatedTime, dailyLoadQ, null);
    }

    public void createOrUpdateTimeSheetDataWithDailyEstimatedTime(EdsEmployee employee, EdsEmployeeTask empTask, ArrayList<Calendar> availableDays, Integer dailyEstimatedTime, Integer dailyLoadQ, String from) {
        EdsModule resourcePlanning = moduleManager.getModuleByCode(PermissionConstants.RESOURCE_PLANNING);
        if (resourcePlanning != null) {
            boolean isFirst = true;

            ArrayList<Date> oldTimesheetDates = timeSheetManager.getTimesheetOldDates(employee.getObjectID(), empTask);

            //the first date should contain the remainder
            int dailyEstimatedTimeC = dailyEstimatedTime + (dailyLoadQ != null ? dailyLoadQ : 0);
            if (availableDays.size() > 0) {
                createOrUpdateTimeSheetData(employee, empTask, availableDays.get(0), dailyEstimatedTimeC, from);
                //remove the first date from the list
                availableDays.remove(0);
            }

            ArrayList<Date> newTimesheetDates = ServerUtils.convertToDates(availableDays);
            ArrayList<Date> datesForUpdate = (ArrayList<Date>) ServerUtils.intersect(newTimesheetDates, oldTimesheetDates);
            //UPDATES OLD TIMESHEET DATES
            if (datesForUpdate.size() > 0) {
                timeSheetManager.updateTimeSheetOldDataWithDailyEstimatedTime(empTask.getObjectID(), dailyEstimatedTime, datesForUpdate, FROM_RESOURCE_UTIL.equals(from));
            }

            //CREATE TIMESHEET DATES
            int batchSize = 50;
            int count = 0;
            for (Date date : newTimesheetDates) {
                count++;
                createNewTimesheetWithDailyEstimate(employee, empTask, date, dailyEstimatedTime);
                if (batchSize == count) {
                    timeSheetManager.flushAndClear();
                    count = 0;
                }
            }
        }
    }

    public void createOrUpdateTimeSheetData(EdsEmployee employee, EdsEmployeeTask empTask, Calendar availDay, Integer dailyEstimatedTime, String from) {
        EdsTimeSheet timeSheet = timeSheetManager.getTimeSheet(empTask, availDay.getTime());
        if (timeSheet == null) {
            createNewTimesheetWithDailyEstimate(employee, empTask, availDay.getTime(), dailyEstimatedTime);
        } else {
            if (empTask.getProjectEmployee() != null) {
                EdsProjectEmployeeWageClientRateHistory history = timeSheetManager.getProjectEmployeeWageClientRateByDate(availDay.getTime(), empTask.getProjectEmployee().getObjectID());
                if (history != null) {
                    timeSheet.setWageRate(history.getWageRate());
                    timeSheet.setClientChargeRate(history.getClientChargeRate());
                }
            }
            if (FROM_RESOURCE_UTIL.equals(from) || timeSheet.getDailyEstimatedTime() == null) {
                timeSheet.setDailyEstimatedTime(dailyEstimatedTime);
                if (timeSheet.getTimeSpent() == null) {
                    timeSheet.setTimeSpent(0);
                }
                timeSheetManager.update(timeSheet);
            }
        }
    }

    private void createNewTimesheetWithDailyEstimate(EdsEmployee employee, EdsEmployeeTask empTask, Date availDay, Integer dailyEstimatedTime) {
        EdsTimeSheet timeSheet = new EdsTimeSheet();
        timeSheet.setDate(availDay);
        timeSheet.setEmployeeTask(empTask);
        EdsTask task = empTask.getTask();
        timeSheet.setTaskID(task.getObjectID());
        timeSheet.setProjectID(task.getProject().getObjectID());
        timeSheet.setEmployeeID(employee.getObjectID());
        if (employee.getTeam() != null) {
            timeSheet.setTeamID(employee.getTeam().getObjectID());
        }
        timeSheet.setTimeSpent(0);
        timeSheet.setDailyEstimatedTime(dailyEstimatedTime);
        if (empTask.getProjectEmployee() != null) {
            EdsProjectEmployeeWageClientRateHistory history = timeSheetManager.getProjectEmployeeWageClientRateByDate(availDay, empTask.getProjectEmployee().getObjectID());
            if (history != null) {
                timeSheet.setWageRate(history.getWageRate());
                timeSheet.setClientChargeRate(history.getClientChargeRate());
            }
        }
        timeSheetManager.create(timeSheet);
    }

    /**
     * Update employee task estimated time and recalculate task budgets
     *
     * @param employeeTask                     - current selected employee task
     * @param totallyEmployeeTaskEstimatedTime - total employee task estimated time
     */
    public void updateEmployeeTaskEstimatedTime(EdsEmployeeTask employeeTask, Integer totallyEmployeeTaskEstimatedTime) {

        EdsTaskEstimateTimeSpentHistory estimateTimeSpentHistory = new EdsTaskEstimateTimeSpentHistory();
        EdsTask edsTask = employeeTask.getTask();
        estimateTimeSpentHistory.setTask(edsTask);
        if (employeeTask.getEstimatedTime().compareTo(totallyEmployeeTaskEstimatedTime) != 0) {
            estimateTimeSpentHistory.setOldEstimatedTime(employeeTask.getEstimatedTime());
            estimateTimeSpentHistory.setEstimatedTime(totallyEmployeeTaskEstimatedTime);
        }

        if (estimateTimeSpentHistory.getEstimatedTime().compareTo(estimateTimeSpentHistory.getOldEstimatedTime()) != 0) {
            //this is for the recalculate task budgets
            edsTask.setChangedCalculationFields(true);

            edsTask.getEstimateTimeSpentHistoryList().add(estimateTimeSpentHistory);
        }
        employeeTaskManager.update(employeeTask);

        int totallyTaskEstimatedTime = 0;
        for (EdsEmployeeTask empTask : edsTask.getUnDeletedAssignments()) {
            totallyTaskEstimatedTime += empTask.getEstimatedTime();
        }
        edsTask.setEstimatedTime(totallyTaskEstimatedTime);
    }

    //
    public void copyEmployeeTaskDailyLoadToTimeSheet() {
        List<EdsCompany> companies = companyManager.getCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companies) {
            if (company.hasSchema(schemas)) {
                System.out.println("Start copied company all employee task dailyLoad to timeSheet - (Company Name: " + company.getName() + ")");
                copyEmployeeTaskDailyLoadToTimeSheet(company.getObjectID());
                System.out.println("Stop copied company all employee task dailyLoad to timeSheet - (Company Name: " + company.getName() + ")");
            }
        }
    }

    public void copyEmployeeTaskDailyLoadToTimeSheet(Integer companyID) {
        System.out.println("Start copied company all employee task dailyLoad to timeSheet - (Company ID: " + companyID + ")");
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        List<Integer> employeeIds = employeeManager.getEmployeeIds();
        for (Integer employeeID : employeeIds) {
            EdsEmployee employee = employeeManager.get(employeeID);
            List<EdsEmployeeTask> employeeTasks = employeeTaskManager.getEmployeeTasks(employee);
            for (EdsEmployeeTask employeeTask : employeeTasks) {
                if (employeeTask.getDailyLoad() != null && /*employeeTask.getDailyLoad() != 0*/ employeeTask.getDailyLoad() >= 0) {
                    createOrUpdateTimeSheetDataForEmployeeAndEmployeeTask(employee, employeeTask, null);
                }
                employeeManager.flush();
            }
            employeeManager.flushAndClear();
        }
        System.out.println("Stop copied company all employee task dailyLoad to timeSheet - (Company ID: " + companyID + ")");
    }
}
