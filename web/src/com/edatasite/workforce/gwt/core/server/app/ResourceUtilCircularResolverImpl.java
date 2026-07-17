package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityCircularResolver;
import com.edatasite.workforce.gwt.availability.server.pojo.Holiday;
import com.edatasite.workforce.gwt.availability.server.pojo.HolidayIndicator;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

/**
 * User: Ilhombek
 * Date: 6/29/12
 * Time: 5:42 PM
 */
@Service("resourceUtilCircularResolver")
public class ResourceUtilCircularResolverImpl implements ResourceUtilCircularResolver {

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private AvailabilityCircularResolver availabilityCircularResolver;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    @Qualifier("rolePermissionService")
    private RolePermissionServiceLocal rolePermissionServiceLocal;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonService;


    /**
     * Register daily employee estimated time
     *
     * @param employeeID             - employeeID
     * @param taskID                 - taskID
     * @param isChangeTaskStartTime  - isChangeTaskStartTime
     * @param isChangeTaskEndTime    - isChangeTaskEndTime
     * @param dailyDate              - dailyDate
     * @param lastDailyEstimatedTime - lastDailyEstimated time this employee task
     */
    public void saveResourceUtilDailyEstimatedTime(Integer employeeID, Integer taskID,
            boolean isChangeTaskStartTime, boolean isChangeTaskEndTime,
            DateNonConvertable nonConvertable, Date dailyDate, Integer lastDailyEstimatedTime) {
        //re
        EdsEmployee employee = employeeManager.get(employeeID);
        EdsTask task = taskManager.get(taskID);
        if (employee != null && task != null) {
            EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeRelatedTask(task, employee);
            if (employeeTask != null) {
                if (isChangeTaskStartTime) {
                    Calendar changedTaskStartTime = Calendar.getInstance();
                    changedTaskStartTime.setTime(dailyDate);
                    task.setStartDate(changedTaskStartTime.getTime());
                    updateTask(task.getObjectID(), isChangeTaskStartTime, false, changedTaskStartTime.getTime());
                } else if (isChangeTaskEndTime) {
                    Calendar changedTaskEndTime = Calendar.getInstance();
                    changedTaskEndTime.setTime(dailyDate);
                    ServerUtils.setEndOfTheDay(changedTaskEndTime);
                    updateTask(task.getObjectID(), false, isChangeTaskEndTime, changedTaskEndTime.getTime());
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(nonConvertable.getNonConvertedDate());

                Integer oldTotalEmployeeTaskEstimate = timeSheetManager.getTotalDailyEstimatedTimesTimeSheet(employeeTask);

                availabilityCircularResolver.createOrUpdateTimeSheetData(employee, employeeTask, calendar, lastDailyEstimatedTime, AvailabilityCircularResolver.FROM_RESOURCE_UTIL);

                //register update employee task estimated time
                Integer totallyEmployeeTaskEstimatedTime = timeSheetManager.getTotalDailyEstimatedTimesTimeSheet(employeeTask);
                availabilityCircularResolver.updateEmployeeTaskEstimatedTime(employeeTask, totallyEmployeeTaskEstimatedTime);

                int difference = 0;
                if (oldTotalEmployeeTaskEstimate != null && totallyEmployeeTaskEstimatedTime != null) {
                    difference = totallyEmployeeTaskEstimatedTime - oldTotalEmployeeTaskEstimate;
                }
                // Update employee task estimate
                Integer employeeTaskEstimate = employeeTask.getEstimatedTime();
                employeeTask.setEstimatedTime(employeeTaskEstimate != null ? employeeTaskEstimate + difference : difference);
                employeeTaskManager.update(employeeTask);

                // Update task estimate
                EdsTask edsTask = employeeTask.getTask();
                Integer taskEstimate = edsTask.getEstimatedTime();
                edsTask.setEstimatedTime(taskEstimate != null ? taskEstimate + difference : difference);
                taskManager.updateTask(edsTask);
            }
        }
    }

    private void updateTask(Integer taskID, boolean changeTaskStartTime, boolean changeTaskEndTime, Date time) {
        EditTask taskForEdit = taskServiceLocal.getTaskForEdit(taskID);
        taskForEdit.setCreatedFrom(EditTask.FROM_RESOURCE_UTIL);
        if (changeTaskStartTime) {
            Calendar merjeNewStartDate = Calendar.getInstance();
            merjeNewStartDate.setTime(time);
            if (taskForEdit.getStartDate() != null) {
                Calendar merjeOldStartDate = Calendar.getInstance();
                merjeOldStartDate.setTime(taskForEdit.getStartDate());
                merjeNewStartDate.set(Calendar.AM_PM, merjeOldStartDate.get(Calendar.AM_PM));
                merjeNewStartDate.set(Calendar.HOUR_OF_DAY, merjeOldStartDate.get(Calendar.HOUR_OF_DAY));
                merjeNewStartDate.set(Calendar.MINUTE, merjeOldStartDate.get(Calendar.MINUTE));
                merjeNewStartDate.set(Calendar.SECOND, merjeOldStartDate.get(Calendar.SECOND));
                merjeNewStartDate.set(Calendar.MILLISECOND, merjeOldStartDate.get(Calendar.MILLISECOND));
            }
            taskForEdit.setStartDate(merjeNewStartDate.getTime());
        } else if (changeTaskEndTime) {
            Calendar mejeNewDueDate = Calendar.getInstance();
            mejeNewDueDate.setTime(time);
            if (taskForEdit.getDueDate() != null) {
                Calendar mejeOldDieDate = Calendar.getInstance();
                mejeOldDieDate.setTime(taskForEdit.getDueDate());
                mejeNewDueDate.set(Calendar.AM_PM, mejeOldDieDate.get(Calendar.AM_PM));
                mejeNewDueDate.set(Calendar.HOUR_OF_DAY, mejeOldDieDate.get(Calendar.HOUR_OF_DAY));
                mejeNewDueDate.set(Calendar.MINUTE, mejeOldDieDate.get(Calendar.MINUTE));
                mejeNewDueDate.set(Calendar.SECOND, mejeOldDieDate.get(Calendar.SECOND));
                mejeNewDueDate.set(Calendar.MILLISECOND, mejeOldDieDate.get(Calendar.MILLISECOND));
            }
            taskForEdit.setDueDate(mejeNewDueDate.getTime());
        }
        try {
            taskServiceLocal.updateTask(taskForEdit);
        } catch (NumberExistingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Generate Resource Utilization Report
     *
     * @param departmentID    - department ID
     * @param employeeID      - employee ID
     * @param startDateString - start Date S
     * @param endDateString   - end Date S
     * @param daysInMonth     - days in month
     * @return - resource util item
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ResourceUtilItem getResourceUtilization(ListingFilterParameter fp) {
        Date startDate = new Date(Integer.parseInt(fp.getStartDateNC().split("-")[0]) - 1900, Integer.parseInt(fp.getStartDateNC().split("-")[1]) - 1, Integer.parseInt(fp.getStartDateNC().split("-")[2]), 0, 0, 0);
        Date endDate = new Date(Integer.parseInt(fp.getEndDateNC().split("-")[0]) - 1900, Integer.parseInt(fp.getEndDateNC().split("-")[1]) - 1, Integer.parseInt(fp.getEndDateNC().split("-")[2]),
                Integer.parseInt(fp.getEndDateNC().split("-")[3]), Integer.parseInt(fp.getEndDateNC().split("-")[4]), Integer.parseInt(fp.getEndDateNC().split("-")[5]));

        Long interval = (new Date()).getTime();
        fp.setStartDate(startDate);
        fp.setEndDate(endDate);
        List<EdsEmployee> existingEmployees = null;

        ListingFilterParameter lfp = new ListingFilterParameter();
        // lfp.setViewAsId(fp.getViewAsId());
        lfp.setClientId(fp.getClientId());
        lfp.setProjectId(fp.getProjectId());
        lfp.setDepartmentId(fp.getDepartmentId());
        lfp.setPositionIDs(fp.getPositionIDs());
        lfp.setNoPosition(fp.getNoPosition());
        lfp.setShowActive(fp.isShowActive());

        existingEmployees = employeeManager.list(lfp, true);
        StringBuilder employeeIds = new StringBuilder();
        HashMap<Integer, Map<Date, Object[]>> employeesMap = new HashMap<>();
        for (EdsEmployee empl : existingEmployees) {
            if (fp.getEmployeeId() == null || empl.getObjectID().equals(fp.getEmployeeId())) {
                Object[] obj = new Object[15];
                obj[0] = null;
                obj[1] = empl.getObjectID();
                obj[2] = empl.getFullName();
                obj[11] = empl.getProfile().getEmployeeCode();
                if (empl.getPhoto() != null && empl.getPhoto().getObjectID() != null){
                    obj[12] = (commonService.getImageUrl(empl.getPhoto().getObjectID()));
                }
                if (empl.getPosition() != null) {
                    obj[13] = empl.getPosition().getName();
                }
                Map<Date, Object[]> objectsMap = new HashMap<>();
                objectsMap.put(null, obj);
                employeesMap.put(empl.getObjectID(), objectsMap);
                if (employeeIds.length() == 0) {
                    employeeIds = new StringBuilder(empl.getObjectID().toString());
                } else {
                    employeeIds.append(",").append(empl.getObjectID().toString());
                }
            }
        }

        //if (fp.getProjectId() == null) {
        List employees = employeeManager.getResourceUtilReport(fp);
        List otherDetails = employeeManager.getOtherDetailsResourceUtilReport(fp);

        Integer unauthorizedLeaveId = referenceManager.getByCode("LR_TYPE_UNAUTHORIZED_LEAVE") != null ? referenceManager.getByCode("LR_TYPE_UNAUTHORIZED_LEAVE").getObjectID() : 0;
        for (Object object : employees) {
            Object[] data = (Object[]) object;
            Object[] objectList = new Object[15];
            objectList[0] = data[0];
            objectList[1] = data[1];
            objectList[2] = data[2];
            objectList[3] = data[3];
            objectList[4] = data[4];
            objectList[5] = data[5];
            objectList[6] = data[6];
            objectList[7] = data[7];
            objectList[14] = data[8];
            objectList[8] = data[9];

            Integer empID = (Integer) (data[1] != null ? data[1] : 0);
            if (employeesMap.containsKey(empID)) {
                Date date = (Date) (data[0]);
                if (date != null) {
                    Map<Date, Object[]> objectsMap = employeesMap.get(empID);
                    if (objectsMap.containsKey(date)) {
                        Object[] previousList = objectsMap.get(date);
                        Integer previousReasonId = previousList[7] != null ? (Integer) previousList[7] : 0;
                        Integer currentReason = objectList[7] != null ? (Integer) objectList[7] : 0;
                        if (currentReason.equals(unauthorizedLeaveId) && previousReasonId.equals(unauthorizedLeaveId)) {
                            objectList[7] = unauthorizedLeaveId;
                        } else {
                            objectList[7] = previousReasonId;
                        }
                    }
                    objectsMap.put(date, objectList);
                }
            }
        }

        for (Object object : otherDetails) {
            Object[] data = (Object[]) object;
            Integer empID = (Integer) (data[1] != null ? data[1] : 0);
            if (employeesMap.containsKey(empID)) {
                Date date = (Date) (data[0]);
                if (date != null) {
                    Map<Date, Object[]> objectsMap = employeesMap.get(empID);
                    if (objectsMap.containsKey(date)) {
                        Object[] currentObj = objectsMap.get(date);
                        currentObj[9] = data[3];
                        currentObj[10] = data[4];
                    }
                }
            }
        }

        //}
        System.out.println("Load employees: " + ((new Date()).getTime() - interval) + " ms ");

        interval = (new Date()).getTime();
        ArrayList<Holiday> companyHolidayDays = availabilityCircularResolver.getCompanyHolidayList();
        System.out.println("Load Holidays: " + ((new Date()).getTime() - interval) + " ms ");

        interval = (new Date()).getTime();
        Map<Integer, Integer> companyTimeSlot = availabilityCircularResolver.getCompanyTimeSlot();
        System.out.println("Load Company TimeSlot: " + ((new Date()).getTime() - interval) + " ms ");

        ResourceUtilItem resourceUtilItem = new ResourceUtilItem();

        resourceUtilItem.setEmployeeIds(employeeIds.toString());
        interval = (new Date()).getTime();
        HolidayIndicator[] monthlyHoliday = availabilityCircularResolver.getMonthlyHoliday(companyTimeSlot, startDate, fp.getSelectedMonth(), companyHolidayDays, false);
        System.out.println("Month Holidays: " + ((new Date()).getTime() - interval) + " ms ");

        Map<Integer, EmployeeResourceUtilItem> empResourceUtilMap = new LinkedHashMap<>();
        EmployeeResourceUtilItem employeeResourceUtilItem = null;
        Integer emp_ID = null;

        interval = (new Date()).getTime();

        for (Map.Entry<Integer, Map<Date, Object[]>> entry : employeesMap.entrySet()) {
            Map<Date, Object[]> datas = entry.getValue();
            for (Map.Entry<Date, Object[]> dataList : datas.entrySet()) {
                Object[] data = dataList.getValue();

                Date dailyDATE = (Date) (data[0]);
                Integer empID = (Integer) (data[1] != null ? data[1] : 0);
                String empName = (String) (data[2] != null ? data[2] : "");
                String empCode = (String) (data[11] != null ? data[11] : "");
                String empPhoto = (String) (data[12] != null ? data[12] : "");
                String empPosition = (String) (data[13] != null ? data[13] : "");
                Boolean dayOff = (Boolean) (data[3] != null ? data[3] : Boolean.FALSE);
                Integer timeSlotHour = (Integer) (data[4] != null ? data[4] : 0);
                String shortNameLR = (String) (data[14] != null ? data[14] : "");
                Integer inOutHour = (Integer) (data[8] != null ? ((Double) data[8]).intValue() : 0);
                Boolean isHolidayT = (Boolean) (data[5] != null ? data[5] : Boolean.FALSE);
                Integer approvedLRTime = 0;
                if (data[7] != null && data[7].equals(unauthorizedLeaveId)) {
                    approvedLRTime = (Integer) (data[6] != null ? data[6] : 0);
                    approvedLRTime = approvedLRTime > 0 ? -approvedLRTime : -1;
                } else {
                    approvedLRTime = (Integer) (data[6] != null ? data[6] : 0);
                }
                BigInteger proTasksDailyLoadCount = (BigInteger) (data[9] != null ? data[9] : BigInteger.valueOf(0));
                BigInteger timeSheetHour = (BigInteger) (data[10] != null ? data[10] : BigInteger.valueOf(0));
                if (empID != null && empID != 0) {

                    if (emp_ID == null || !emp_ID.equals(empID)) {
                        emp_ID = empID;
                        employeeResourceUtilItem = new EmployeeResourceUtilItem(empID, empName, empCode, empPhoto, empPosition, (fp.getSelectedMonth() + 1));
                        empResourceUtilMap.put(empID, employeeResourceUtilItem);
                    }

                    Calendar calendar = Calendar.getInstance();

                    if (empResourceUtilMap.containsKey(empID)) {
                        if (dailyDATE != null) {
                            calendar.setTime(dailyDATE);
                            int dayOffMonth = calendar.get(Calendar.DAY_OF_MONTH);
                            int current = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                            if (fp.getSelectedMonth() >= dayOffMonth) {

                                if (timeSlotHour != null) {
                                    employeeResourceUtilItem.getTotalTimeSlotHours()[dayOffMonth] = dayOff ? 0 : timeSlotHour;
                                }
                                if (inOutHour != null) {
                                    employeeResourceUtilItem.getTotalInOutHours()[dayOffMonth] = dayOff ? 0 : inOutHour;
                                }
                                if (timeSheetHour != null) {
                                    employeeResourceUtilItem.getTotalTimeSheetHours()[dayOffMonth] = timeSheetHour.intValue();
                                }

                                if (proTasksDailyLoadCount != null) {
                                    employeeResourceUtilItem.getTotalHours()[dayOffMonth] = proTasksDailyLoadCount.intValue();
                                }
                                if (isHolidayT) {
                                    employeeResourceUtilItem.getWithHoliday_INT()[dayOffMonth] = 1;
                                }
                                if (approvedLRTime != null) {
                                    employeeResourceUtilItem.getWith_LR_INT()[dayOffMonth] = dayOff ? 0 : approvedLRTime;
                                }
                                if (shortNameLR != null) {
                                    employeeResourceUtilItem.getShortNameLR()[dayOffMonth] = shortNameLR;
                                }

                                if (dayOff != null) {
                                    employeeResourceUtilItem.getDayOff()[dayOffMonth] = dayOff;
                                }
                            }
                        }
                    }
                }
            }
        }

        EmployeeResourceUtilItem[] employeeResourceUtilItems = new EmployeeResourceUtilItem[empResourceUtilMap.size()];
        int e = 0;
        for (Integer id : empResourceUtilMap.keySet()) {
            employeeResourceUtilItems[e] = empResourceUtilMap.get(id);
            e++;
        }
        Arrays.sort(employeeResourceUtilItems, Comparator.comparing(EmployeeResourceUtilItem::getEmployee_name));
        resourceUtilItem.setEmployeeResourceUtilItems(employeeResourceUtilItems);

        if (fp.getDepartmentId() != null) {
            EdsDepartment department = departmentManager.get(fp.getDepartmentId());
            resourceUtilItem.setDepartment_id(department.getObjectID());
            resourceUtilItem.setDepartment_name(department.getName());
            resourceUtilItem.setDepartment_description(department.getDescription());
        }
        resourceUtilItem.setMonth_holiday_INT(ServerUtils.extractArrayFromHolidayIndicator(monthlyHoliday));

        System.out.println("Process Time: " + ((new Date()).getTime() - interval) + " ms ");
        return resourceUtilItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectTaskItem[] getEmployeeProjectsResourceUtil(Integer start, String startDateString, String endDateString, ListingFilterParameter filterParameter) {
        Date startDate = new Date(Integer.parseInt(startDateString.split("-")[0]) - 1900, Integer.parseInt(startDateString.split("-")[1]) - 1, Integer.parseInt(startDateString.split("-")[2]), 0, 0, 0);
        Date endDate = new Date(Integer.parseInt(endDateString.split("-")[0]) - 1900, Integer.parseInt(endDateString.split("-")[1]) - 1, Integer.parseInt(endDateString.split("-")[2]),
                Integer.parseInt(endDateString.split("-")[3]), Integer.parseInt(endDateString.split("-")[4]), Integer.parseInt(endDateString.split("-")[5]));
        List projects = projectManager.getResourceUtilProjectReport(startDate, endDate, start, filterParameter);

        int daysInMonth = filterParameter.getDay();
        Map<Integer, Integer> companyTimeSlot = availabilityCircularResolver.getCompanyTimeSlot();

        Map<Integer, ProjectTaskItem> projectResourceUtilMap = new LinkedHashMap<>();
        ProjectTaskItem projectResourceUtilItem = null;
        Integer pro_ID = null;

        for (Object object : projects) {
            Object[] data = (Object[]) object;
            Date dailyDATE = (Date) (data[0]);
            Integer empID = (Integer) (data[1] != null ? data[1] : 0);
            Integer proID = (Integer) (data[2] != null ? data[2] : 0);
            String proName = (String) (data[3] != null ? data[3] : "");
            String proDescription = (String) (data[4] != null ? data[4] : "");
            Date proStartTime = (Date) (data[5]);
            Date proEndTime = (Date) (data[6]);
            BigInteger proTasksDailyLoadCount = (BigInteger) (data[7] != null ? data[7] : BigInteger.valueOf(0));
            Boolean dayOff = (Boolean) (data[8] != null ? data[8] : Boolean.FALSE);
            Integer timeSlotHour = (Integer) (data[9] != null ? data[9] : 0);
            Integer timeSheetHour = (Integer) (data[10] != null ? data[10] : 0);
            Boolean isHolidayT = (Boolean) (data[11] != null ? data[11] : Boolean.FALSE);
            Integer approvedLRTime = (Integer) (data[12] != null ? data[12] : 0);

            if (proID != null && proID != 0) {
                if (pro_ID == null || !pro_ID.equals(proID)) {
                    pro_ID = proID;
                    projectResourceUtilItem = new ProjectTaskItem(proID, proName, (daysInMonth + 1));
                    projectResourceUtilItem.setProject_description(proDescription);
                    projectResourceUtilMap.put(proID, projectResourceUtilItem);
                }
                Calendar calendar = Calendar.getInstance();
                if (projectResourceUtilMap.containsKey(proID)) {
                    if (dailyDATE != null) {
                        calendar.setTime(dailyDATE);
                        int dayOffMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        int current = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        if (daysInMonth >= dayOffMonth) {
                            if (proTasksDailyLoadCount != null) {
                                projectResourceUtilItem.getTotalEstimatedTime()[dayOffMonth] = proTasksDailyLoadCount.intValue();
                            }
                            if (isHolidayT) {
                                projectResourceUtilItem.getWithHoliday_INT()[dayOffMonth] = 1;
                            }
                            if (approvedLRTime != null) {
                                projectResourceUtilItem.getWith_LR_INT()[dayOffMonth] = dayOff ? 0 : approvedLRTime;
                            }
                        }
                    }
                }
            }
        }

        ProjectTaskItem[] projectTaskItems = new ProjectTaskItem[projectResourceUtilMap.size()];
        int p = 0;
        for (Integer project_ID : projectResourceUtilMap.keySet()) {
            projectTaskItems[p] = projectResourceUtilMap.get(project_ID);
            p++;
        }
        Arrays.sort(projectTaskItems, Comparator.comparing(ProjectTaskItem::getProject_name));
        return projectTaskItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TaskItem[] getEmployeeProjectTasksResourceUtil(String startDateString, String endDateString, Integer start, ListingFilterParameter filterParameter) {
        Date startDate = new Date(Integer.parseInt(startDateString.split("-")[0]) - 1900, Integer.parseInt(startDateString.split("-")[1]) - 1, Integer.parseInt(startDateString.split("-")[2]), 0, 0, 0);
        Date endDate = new Date(Integer.parseInt(endDateString.split("-")[0]) - 1900, Integer.parseInt(endDateString.split("-")[1]) - 1, Integer.parseInt(endDateString.split("-")[2]),
                Integer.parseInt(endDateString.split("-")[3]), Integer.parseInt(endDateString.split("-")[4]), Integer.parseInt(endDateString.split("-")[5]));
        List tasks = projectManager.getResourceUtilTaskReport(startDate, endDate, start, filterParameter);

        int daysInMonth = filterParameter.getDay();// returns DAYS IN MONTH
        Map<Integer, Integer> companyTimeSlot = availabilityCircularResolver.getCompanyTimeSlot();

        Map<Integer, TaskItem> taskResourceUtilMap = new LinkedHashMap<>();
        TaskItem taskResourceUtilItem = null;
        Integer task_ID = null;

        for (Object object : tasks) {
            Object[] data = (Object[]) object;
            Date dailyDATE = (Date) (data[0]);
            Integer empID = (Integer) (data[1] != null ? data[1] : 0);
            Integer taskID = (Integer) (data[2] != null ? data[2] : 0);
            String taskName = (String) (data[3] != null ? data[3] : "");
            String taskDescription = (String) (data[4] != null ? data[4] : "");
            Integer workingDay = (Integer) (data[5] != null ? data[5] : -1);//workingDay == -1 dayOff, workingDay == 1 workingDay
            Integer dailyLoad = (Integer) (data[6] != null ? data[6] : 0);
            Integer taskEstimatedTime = (Integer) (data[7] != null ? data[7] : 0);
            Date taskStartTime = (Date) (data[8]);
            Date taskEndTime = (Date) (data[9]);
            Integer timeSheetHour = (Integer) (data[10] != null ? data[10] : 0);
            Boolean dayOff = (Boolean) (data[11] != null ? data[11] : Boolean.FALSE);
            Integer timeSlotHour = (Integer) (data[12] != null ? data[12] : 0);
            Integer overAllTimeSheetHour = (Integer) (data[13] != null ? data[13] : 0);
            Boolean isHolidayT = (Boolean) (data[14] != null ? data[14] : Boolean.FALSE);
            Integer approvedLRTime = (Integer) (data[15] != null ? data[15] : 0);
            Boolean isIssue = (Boolean) (data[16] != null ? data[16] : Boolean.FALSE);

            if (taskID != null && taskID != 0) {

                if ((task_ID == null || !task_ID.equals(taskID)) && !taskResourceUtilMap.containsKey(taskID)) {
                    task_ID = taskID;
                    taskResourceUtilItem = new TaskItem(taskID, taskName, (daysInMonth + 1));
                    taskResourceUtilItem.setTask_description(taskDescription);
                    taskResourceUtilMap.put(taskID, taskResourceUtilItem);
                }
                Calendar calendar = Calendar.getInstance();
                if (taskResourceUtilMap.containsKey(taskID)) {
                    if (dailyDATE != null) {
                        calendar.setTime(dailyDATE);
                        int dayOffMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        int current = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        if (daysInMonth >= dayOffMonth) {
                            if (timeSheetHour != null) {
                                taskResourceUtilItem.getTotalTimeSheetHours()[dayOffMonth] += timeSheetHour;
                            }
                            if (workingDay == 1) {
                                taskResourceUtilItem.getWorkingDay()[dayOffMonth] = true;
                            }
                            if (dailyLoad != null && dailyLoad >= 0) {
                                taskResourceUtilItem.getTotalEstimatedTime()[dayOffMonth] = dailyLoad;
                            }
                            if (isHolidayT) {
                                taskResourceUtilItem.getWithHoliday_INT()[dayOffMonth] = 1;
                            }
                            if (approvedLRTime != null) {
                                taskResourceUtilItem.getWith_LR_INT()[dayOffMonth] = dayOff ? 0 : approvedLRTime;
                            }
                            if (timeSlotHour != null) {
                                taskResourceUtilItem.getTotalTimeSlotHours()[dayOffMonth] = dayOff ? 0 : timeSlotHour;
                            }

                            taskResourceUtilItem.getDailyDate()[dayOffMonth] = new DateNonConvertable(dailyDATE);
                            taskResourceUtilItem.setTask_start_date(taskStartTime);
                            taskResourceUtilItem.setTask_due_date(taskEndTime);
                            taskResourceUtilItem.setIssue(isIssue);
                        }
                    }
                }
            }
        }

        TaskItem[] taskItems = new TaskItem[taskResourceUtilMap.size()];
        int t = 0;
        for (Integer t_ID : taskResourceUtilMap.keySet()) {
            taskItems[t] = taskResourceUtilMap.get(t_ID);
            taskItems[t].setEditable(hasEditableOption(filterParameter.getProjectId()));
            t++;
        }
        Arrays.sort(taskItems, (o1, o2) -> o2.getTask_name().compareTo(o1.getTask_name()));
        return taskItems;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ExportToExcelItem getResourceUtilization(ListingFilterParameter fp, String startDateString, String endDateString, int daysInMonth) {

        Date startDate = new Date(Integer.parseInt(startDateString.split("-")[0]) - 1900, Integer.parseInt(startDateString.split("-")[1]) - 1, Integer.parseInt(startDateString.split("-")[2]), 0, 0, 0);
        Date endDate = new Date(Integer.parseInt(endDateString.split("-")[0]) - 1900, Integer.parseInt(endDateString.split("-")[1]) - 1, Integer.parseInt(endDateString.split("-")[2]),
                Integer.parseInt(endDateString.split("-")[3]), Integer.parseInt(endDateString.split("-")[4]), Integer.parseInt(endDateString.split("-")[5]));

        ListingFilterParameter filterParameter = new ListingFilterParameter();

        ArrayList ids = new ArrayList();
        StringBuilder employeeIds = null;
        StringBuilder projectIds = null;
        if (fp.getDepartmentId() == null && fp.getEmployeeId() != null) {
            Integer employeeId = fp.getEmployeeId();
            employeeIds = new StringBuilder(employeeId != null ? employeeId.toString() : null);

            List<Integer> projects = employeeManager.getProjectIds(fp);
            if (projects != null && projects.size() > 0) {
                for (Integer projectId : projects) {
                    if (projectIds == null) {
                        projectIds = new StringBuilder(projectId.toString());
                    } else {
                        projectIds.append(",").append(projectId);
                    }
                }
            }
        } else if (fp.getDepartmentId() == null && fp.getProjectId() != null) {
            Object projectId = fp.getProjectId();
            projectIds = new StringBuilder(projectId != null ? projectId.toString() : null);

            List<Integer> employees = employeeManager.getEmployeeIds(fp);
            if (employees != null && employees.size() > 0) {
                for (Integer employeeId : employees) {
                    if (employeeIds == null) {
                        employeeIds = new StringBuilder(employeeId.toString());
                    } else {
                        employeeIds.append(",").append(employeeId);
                    }
                }
            }
        } else if (fp.getDepartmentId() != null) {
            List<Integer> employees = employeeManager.getEmployeeIds(fp);
            if (employees != null && employees.size() > 0) {
                for (Integer employeeId : employees) {
                    if (employeeIds == null) {
                        employeeIds = new StringBuilder(employeeId.toString());
                    } else {
                        employeeIds.append(",").append(employeeId);
                    }
                }
            }
            List<Integer> projects = employeeManager.getProjectIds(fp);
            if (projects != null && projects.size() > 0) {
                for (Integer projectId : projects) {
                    if (projectIds == null) {
                        projectIds = new StringBuilder(projectId.toString());
                    } else {
                        projectIds.append(",").append(projectId);
                    }
                }
            }
        }

        boolean projectSelected = fp.getProjectId() != null;
        if (employeeIds != null) {
            filterParameter.setEmployeeIDs(employeeIds.toString());
        }
        if (projectIds != null) {
            filterParameter.setProjectIds(projectIds.toString());
        }
        filterParameter.setStartDate(startDate);
        filterParameter.setEndDate(endDate);
        filterParameter.setDay(daysInMonth);//this is a days count of month

        filterParameter.setPositionIDs(fp.getPositionIDs());
        filterParameter.setNoPosition(fp.getNoPosition());
        filterParameter.setShowFilledCells(fp.isShowFilledCells());
        filterParameter.setShowActive(fp.isShowActive());

        return projectManager.getResourceUtilEmployeeExcel(filterParameter, projectSelected);
    }

    private boolean hasEditableOption(Integer projectID) {
        EdsUser user = employeeManager.getUser();
        if (projectID != null) {
            EdsProject project = projectManager.get(projectID);
            if (project != null) {
                if (project.getManager() != null && user.getObjectID().equals(project.getManager().getObjectID())) {
                    user.addArtificialRole(roleManager.getByCode(Constants.PMOFPR));
                }
                if (project.isUserBackupManager(user.getObjectID())) {
                    user.addArtificialRole(roleManager.getByCode(Constants.BMOFPR));
                }
            }
        }
        return ServerUtils.hasPermission(PermissionConstants.PM_RESOURCE_UTILIZATION_EDITABLE, user);
    }
}
