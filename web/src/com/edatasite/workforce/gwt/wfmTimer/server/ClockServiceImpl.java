package com.edatasite.workforce.gwt.wfmTimer.server;

import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsClock;
import com.edatasite.workforce.core.domain.EdsClockHistory;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AttendanceStats;
import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ClockHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ClockManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceImpl;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskStatus;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.wfmTimer.client.rpc.ClockService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Aug 11, 2010
 * Time: 7:15:29 PM
 * To change this template use File | Settings | File Templates.
 */

@Transactional

@Service("clockService")
public class ClockServiceImpl extends RemoteServiceServlet implements ClockService, Constants {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private ClockManager clockManager;
    @Autowired
    private ClockHistoryManager clockHistoryManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private TimesheetService timesheetService;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    public void startTimer(ClockItem item) {
        EdsClock clock;
        EdsUser user = clockManager.getUser();
        EdsClock existingClock = clockManager.getClockItem(user.getObjectID(), item.getBusObjectId(), item.getRelation());
        if (existingClock == null) {
            clock = new EdsClock();
            clock.setBusObjectId(item.getBusObjectId());
            clock.setRelation(item.getRelation());
        } else {
            clock = existingClock;
        }
        clock.setOwner(user);
        if (item.getEstimateTime() != null) {
            clock.setEstimate(item.getEstimateTime());
        }
        if (clock.isNew() && item.getStartDate() != null) {
            clock.setStartDate(item.getStartDate());
        } else {
            clock.setStartDate(new Date());
        }
        clock.setReset(false);
        if (item.isOverrideAnotherTimerInstance()) {
            clock.setCumulativeTime(item.getTodaysTime());
        }
        clock.setComment(item.getComment());
        clock.setStarted(true);
        if (clock.getObjectID() == null) {
            clockManager.create(clock);
        } else {
            clockManager.update(clock);
        }
        // running additional actions
        if (clock.getRelation() == PM_TASK) {// setting task status to In Progress while starting clock timer
            log.info("Timer change status. Task id:" + clock.getBusObjectId());
            EdsReference inProgressStatus = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS);
            EdsTask task = taskManager.get(clock.getBusObjectId());
            EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeRelatedTask(task, user.getEmployee());
            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setEmployeeTaskId(employeeTask.getObjectID());
            taskStatus.setTaskId(task.getObjectID());
            taskStatus.setStatus(inProgressStatus.getObjectID());
            timesheetService.updateStatus(taskStatus);
        }
    }

    public ListResult<ClockItem> getCaseClocks(Integer busObjectId, Integer type) {
        List<EdsClockHistory> result = clockHistoryManager.getClockItems(busObjectId, type);
        ArrayList<ClockItem> items = new ArrayList<>();
        if (result != null && result.size() > 0) {
            int i = 0;
            for (EdsClockHistory item : result) {
                i++;
                if (i <= 10) {
                    ClockItem clock = new ClockItem();
                    clock.setComment(item.getComment());
                    clock.setStartDate(item.getDate());
                    Integer lastTime = 0;
                    clock.setElapsedTime(item.getCumulativeTime() != null ? item.getCumulativeTime() : 0);
                    clock.setOwnerName(item.getOwner().getFullName());
                    items.add(clock);
                } else {
                    break;
                }
            }
        }
        return new ListResult<>(items, items.size());
    }

    public ClockItem getClockItem(Integer busObjectId, Integer type, DateNonConvertable clientsCurrentDate) {
        ClockItem clockItem = new ClockItem();
        EdsUser user = clockManager.getUser();
        EdsClock clock = clockManager.getClockItem(user.getObjectID(), busObjectId, type);
        if (clock != null) {
            clockItem.setObjectId(clock.getObjectID());
            clockItem.setBusObjectId(clock.getBusObjectId());
            clockItem.setOwnerId(clock.getOwner().getObjectID());
            clockItem.setRelation(clock.getRelation());
            clockItem.setActualTime(0);
            clockItem.setStartDate(clock.getStartDate());
            clockItem.setEndDate(clock.getEndDate());
            clockItem.setReset(clock.isReset());
            Integer lastTime = 0;
            if (clock.isStarted()) {
                int cumulativeTime = clock.getCumulativeTime() != null ? clock.getCumulativeTime() : 0;
                lastTime = (int) (((new Date().getTime() - clock.getStartDate().getTime()) / 1000) % (60 * 60 * 24)) + cumulativeTime;
            } else if (!clock.isReset()) {
                lastTime = clock.getCumulativeTime() != null ? clock.getCumulativeTime() : 0;
            }
            clockItem.setElapsedTime(lastTime);
            clockItem.setRemainingTime(0);
            clockItem.setStarted(clock.isStarted());
            clockItem.setComment(clock.getComment());
        }
        if (user.getCompany() != null && user.getCompany().getCompanySettings() != null &&
                user.getCompany().getCompanySettings().getLongDateFormat() != null) {
            clockItem.setCompanyDateTimeFormat(user.getCompany().getCompanySettings().getLongDateFormat());
        }
        clockItem.setApprovedForToday(false);
        clockItem.setSentToApproveForToday(false);

        switch (type) {
            case PM_TASK, PM_ISSUE_TIMER -> {
                EdsTask task = null;
                if (type == PM_TASK) {
                    task = taskManager.get(busObjectId);
                } else {
                    task = taskManager.getTaskByIssueId(busObjectId);
                }
                if (task != null) {
//                    ArrayList<SelectItem> projects = projectManager.getEmployeeNotStartedOnGoingProjectsAsSelectItem(user.getEmployee());
//                    clockItem.setProjects(projects.toArray(new SelectItem[]{}));
                    clockItem.setProjectID(task.getProject().getObjectID());
                    clockItem.setProjectName(task.getProject().getName());
                    getTasks(clockItem, user, clockItem.getProjectID(), type, clientsCurrentDate);
                    EdsEmployeeTask employeeTask = taskManager.getEmployeeTask(user.getObjectID(), task.getObjectID());
                    if (employeeTask != null) {
                        clockItem.setEstimateTime(employeeTask.getEstimatedTime());
                        clockItem.setPercent(employeeTask.getPercent());
                        Date currentDate = new Date();
                        currentDate.setMinutes(currentDate.getMinutes() + user.getUserTimezone().getRawOffset() / 60000);
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(currentDate);
                        ServerUtils.setBeginningOfTheDay(calendar);
                        EdsTimeSheet timeSheet = timeSheetManager.getTimeshetForMobile(employeeTask, calendar.getTime());
                        if (timeSheet != null) {
                            clockItem.setComment(timeSheet.getComment());
                            int diff = 0;
                            if (clock != null) {
                                diff = clock.isStarted() ? Long.valueOf((new Date().getTime() - clock.getStartDate().getTime()) / 60000).intValue() : 0;
                            }
                            clockItem.setTodaysTime(timeSheet.getTimeSpent() + diff);
                            if (timeSheet.getStatus() != null && timeSheet.getStatus().getCode().equals(EdsTimeSheet._APPROVE)) {
                                clockItem.setApprovedForToday(true);
                            }
                            if (timeSheet.getStatus() != null && timeSheet.getStatus().getCode().equals(EdsTimeSheet._WAITING)) {
                                clockItem.setSentToApproveForToday(true);
                            }
                        }
                        Integer totalTimeSheets = timeSheetManager.getEmployeeTaskTotalTimeSheet(employeeTask.getTask().getObjectID().toString(), user.getObjectID(), null);
                        clockItem.setActualTime(totalTimeSheets);
                    }
                }
            }
            case CRM_CASE -> {
                if (busObjectId != null) {
                    EdsCase cases = caseManager.get(busObjectId);
                    if (cases != null) {
                        clockItem.setCaseName(cases.getSubject());
                    }
                }
            }
        }
        return clockItem;
    }

    private void getTasks(ClockItem clockItem, EdsUser user, Integer projectID, Integer type, DateNonConvertable clientsCurrentDate) {
        int weekOffset = timesheetService.getWeekOffset(clientsCurrentDate, clientsCurrentDate);
        DateNonConvertable[] dates = timesheetService.getTimesheetWeeklyDates(clientsCurrentDate, weekOffset);
        Calendar startDate = new GregorianCalendar();
        startDate.setTime(dates[0].getNonConvertedDate());
        startDate.set(Calendar.AM_PM, 0);
        startDate.set(Calendar.HOUR, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = new GregorianCalendar();
        endDate.setTime(dates[6].getNonConvertedDate());
        endDate.set(Calendar.AM_PM, 0);
        endDate.set(Calendar.HOUR, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        endDate.set(Calendar.MILLISECOND, 0);

        EdsEmployee employee = (EdsEmployee) taskManager.getUser();
        ListingFilterParameter parametrs = new ListingFilterParameter();
        parametrs.setProjectId(projectID);
        if (type == PM_ISSUE_TIMER) {
            parametrs.setOnlyIssueTasks(true);
        } else {
            parametrs.setOnlyIssueTasks(false);
        }
        List<EdsEmployeeTask> taskList = employeeTaskManager.listDueTasks(employee, startDate.getTime(), endDate.getTime(), parametrs);
        if (taskList != null) {
            ArrayList<SelectItem> tasks = new ArrayList<>();
            HashMap<Integer, AttendanceStats> taskMap = new HashMap<>();
            EdsAttendanceRawData attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(clientsCurrentDate.getDate(), employee.getObjectID());
            for (EdsEmployeeTask employeeTask : taskList) {
                EdsTask task = employeeTask.getTask();
                tasks.add(task.getAsSelectItem());

                AttendanceStats attendanceStats = new AttendanceStats();
                attendanceStats.setTaskStart(task.getStartDate());
                attendanceStats.setTaskEnd(task.getDueDate());
                attendanceStats.setHoliday(attendanceRawData.getHoliday());
                attendanceStats.setDayOff(attendanceRawData.getDayOff());
                attendanceStats.setTimeslotMinutes(attendanceRawData.getTimeSlot());
                attendanceStats.setTimesheetMinutes(attendanceRawData.getTimeSheet() + attendanceRawData.getTimeSheetPending());
                attendanceStats.setLeaveMinutes(attendanceRawData.getLeave());

                taskMap.put(task.getObjectID(), attendanceStats);
            }
            clockItem.setTasks(tasks.toArray(new SelectItem[]{}));
            clockItem.setTaskMap(taskMap);
            return;
        }
        clockItem.setTasks(new SelectItem[]{});
        clockItem.setTaskMap(new HashMap<>());
    }

    public Integer[] applyTime(ClockItem item) {
        Integer actual = item.getTodaysTime();
        Integer todays = null;
        EdsUser user = userManager.getUser();
        EdsClock clock = clockManager.getClockItem(user.getObjectID(), item.getBusObjectId(), item.getRelation());
        int cumulative = 0;
        if (clock == null) {
            clock = new EdsClock();
            Date startDate = new Date();
            startDate.setMinutes(startDate.getMinutes() - actual);
            clock.setStartDate(startDate);
        }
        if (clock.getCumulativeTime() != null) {
            cumulative = clock.getCumulativeTime();
        }
        clock.setBusObjectId(item.getBusObjectId());
        clock.setRelation(item.getRelation());
        clock.setOwner(user);
        clock.setCumulativeTime(0);
        clock.setStartDate(null);
        clock.setEndDate(null);
        clock.setComment(item.getComment());
        clock.setStarted(false);
        if (clock.getActualTime() != null && clock.getActualTime() > 0) {
            actual += clock.getActualTime();
        }
        clock.setActual(actual);
        clock.setReset(item.isReset());
        // set data to entity
        switch (item.getRelation()) {
            case PM_TASK, PM_ISSUE_TIMER -> {
                EdsTask task = null;
                if (item.getRelation() == PM_TASK) {
                    task = taskManager.get(item.getBusObjectId());
                } else {
                    task = taskManager.getTaskByIssueId(item.getBusObjectId());
                }
                Set<EdsEmployeeTask> employeeTasks = task.getUnDeletedAssignments();
                if (employeeTasks != null && !employeeTasks.isEmpty()) {
                    for (EdsEmployeeTask employeeTask : employeeTasks) {
                        if (employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().equals(clock.getOwner())) {
                            Date currentDate = new Date();
                            currentDate.setMinutes(currentDate.getMinutes() + user.getUserTimezone().getRawOffset() / 60000);
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(currentDate);
                            ServerUtils.setBeginningOfTheDay(calendar);
                            TimesheetDataItem timesheetItem = new TimesheetDataItem();
                            todays = (int) Math.ceil((double) item.getTodaysTime() / 60);//this is in seconds, so convert it to minutes
                            timesheetItem.setDifference(todays);
                            List<EdsTimeSheet> timeSheetList = timeSheetManager.getTimeSheets(employeeTask.getTask().getObjectID(), user.getObjectID(), calendar.getTime());
                            Integer sumEmployeeSpentToTaskInterval = 0;
                            for (EdsTimeSheet timeSheet : timeSheetList) {
                                sumEmployeeSpentToTaskInterval += timeSheet.getTimeSpent() != null ? timeSheet.getTimeSpent() : 0;
                                if (timeSheet.getEmployeeTask().getDeleted() && timeSheet.getTimeSpent() > 0) {
                                    timesheetItem.getOldEmployeeTaskIDList().add(timeSheet.getEmployeeTask().getObjectID());
                                }
                            }
                            todays += sumEmployeeSpentToTaskInterval;
                            timesheetItem.setEmployeeTaskID(employeeTask.getObjectID());
                            timesheetItem.setDate(calendar.getTime());
                            timesheetItem.setMinutes(todays);
                            timesheetItem.setComment(item.getComment());
                            timesheetService.applyUpdates(timesheetItem, null);
                            employeeTask.setEstimatedTime(item.getEstimateTime());
                            EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();//if settings is null then project percent calculation mode is manual by default
                            if (numberingSettings == null || !numberingSettings.isAutomatic()) {
                                employeeTask.setPercent(item.getPercent());
                                Float average;
                                Float averageProjectTasks;
                                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                                    average = task.getTaskAveragePercentCompletedNewLogic();
                                    averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompletedNewLogic();
                                } else {
                                    average = task.getTaskAveragePercentCompleted();
                                    averageProjectTasks = task.getProject().getProjectTasksAveragePercentCompleted();
                                }
                                task.setPreviousPercent(task.getPercent());
                                task.setPercent(average);
                                task.getProject().setPercent(averageProjectTasks);
                                task.setLastUpdateTime(new Date());
                            }
                            taskRbacManager.addRbacEntries(task);
                            try {
                                taskSolrComponent.index(task);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }
        clockManager.createOrUpdate(clock);
        if (item.getRelation() == CRM_CASE) {
            EdsClockHistory history = new EdsClockHistory();
            history.setBusObjectId(item.getBusObjectId());
            history.setComment(item.getComment());
            history.setCumulativeTime(cumulative);
            history.setDate(new Date());
            history.setOwner(user);
            history.setRelation(item.getRelation());
            clockHistoryManager.create(history);
        }
        if (item.getRelation() != CRM_CASE) {
            EdsEmployeeTask employeeTask = taskManager.getEmployeeTask(user.getObjectID(), item.getBusObjectId());
            Integer[] response = new Integer[2];
            response[0] = timeSheetManager.getEmployeeTaskTotalTimeSheet(employeeTask.getTask().getObjectID().toString(), user.getObjectID(), null);
            response[1] = todays;//todays timespent is needed for timesheet max hours validation.
            return response;
        }
        return new Integer[0];
    }

    public void stopTimer(ClockItem item) {
        EdsUser user = clockManager.getUser();
        EdsClock clock = clockManager.getClockItem(user.getObjectID(), item.getBusObjectId(), item.getRelation());
        if (clock != null) {
            clock.setStarted(false);
            clock.setReset(item.isReset());
            if (item.isReset()) {
                clock.setCumulativeTime(0);
                clock.setStartDate(null);
                clock.setEndDate(null);
            } else {
                clock.setCumulativeTime(item.getTodaysTime());
                clock.setEndDate(new Date());
            }
            clockManager.update(clock);
        }
    }

    public ClockItem getProjectTasks(Integer projectID, Integer type, DateNonConvertable clientsCurrentDate) {
        ClockItem clockItem = new ClockItem();
        getTasks(clockItem, userManager.getUser(), projectID, type, clientsCurrentDate);
        return clockItem;
    }

}
