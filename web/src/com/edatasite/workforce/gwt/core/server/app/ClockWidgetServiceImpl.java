package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsClock;
import com.edatasite.workforce.core.domain.EdsClockHistory;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AttendanceStats;
import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.rpc.ClockWidgetService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TimesheetSummary;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ClockHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ClockManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskStatus;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 09.04.14
 * Time: 19:56
 */
@Transactional
@Service("clockWidgetService")
public class ClockWidgetServiceImpl implements ClockWidgetService, Constants {

    @Autowired
    private ClockManager clockManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private TimesheetService timesheetService;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ClockHistoryManager clockHistoryManager;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private IssueService issueService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TimeSheetManager timesheetManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    @Override
    public Integer startTimer(ClockItem item) {
        EdsClock clock;
        EdsUser user = clockManager.getUser();
        Integer objectID = item.getBusObjectId();
        if (objectID == null && !"".equals(item.getTaskName())) {
            TaskSingleItem newTask = new TaskSingleItem();
            newTask.setProjectID(item.getProjectID());
            newTask.setName(item.getTaskName());
            newTask.setDescription(item.getComment());
            newTask.setStartDate(item.getStartDate());
            newTask.setDueDate(item.getEndDate());
            newTask.setAllDay(true);
            newTask.setBillable(true);
            NumberData numberData = taskService.generateTaskNumber(item.getProjectID(), item.getStartDate(), null);
            newTask.setNumberData(numberData);
            EdsReference mediumStatus = referenceManager.findReference(EdsTask.TASK_PRIORITY, EdsTask.MEDIUM);
            newTask.setPriorityID(mediumStatus.getObjectID());
            Integer[] taskIDs = new Integer[0];
            try {
                taskIDs = taskService.saveTask(newTask);
            } catch (NumberExistingException e) {
                e.printStackTrace();
            }
            objectID = taskIDs[1]; // 1 - taskID
        }
        EdsClock existingClock = clockManager.getClockItem(user.getObjectID(), objectID, item.getRelation());
        if (existingClock == null) {
            clock = new EdsClock();
            clock.setBusObjectId(objectID);
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
            EdsReference inProgressStatus = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS);
            EdsTask task = taskManager.get(clock.getBusObjectId());
            EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeRelatedTask(task, user.getEmployee());
            TaskStatus taskStatus = new TaskStatus();
            if (employeeTask != null) {
                taskStatus.setEmployeeTaskId(employeeTask.getObjectID());
            }
            taskStatus.setTaskId(task.getObjectID());
            taskStatus.setStatus(inProgressStatus.getObjectID());
            timesheetService.updateStatus(taskStatus);
        }
        return objectID;
    }

    @Override
    public ClockItem getClockItem(Integer busObjectId, Integer type, DateNonConvertable clientsCurrentDate) {
        ClockItem clockItem = new ClockItem();
        EdsUser user = clockManager.getUser();
        EdsClock clock = null;
        if (busObjectId != null) {
            clock = clockManager.getClockItem(user.getObjectID(), busObjectId, type);
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
                clockItem.setHaveStoppedTime(!clock.isStarted());
            } else {
                ClockItem stoppedItem = getHistoryClockItem();
                if (stoppedItem != null) {
                    clockItem.setHaveStoppedTime(stoppedItem.isHaveStoppedTime());
                }
            }
        } else {
            ClockItem stoppedItem = getHistoryClockItem();
            if (stoppedItem != null) {
                clockItem.setHaveStoppedTime(stoppedItem.isHaveStoppedTime());

            }
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
                if (busObjectId != null) {
                    if (type == PM_TASK) {
                        task = taskManager.get(busObjectId);
                    } else {
                        task = taskManager.getTaskByIssueId(busObjectId);
                    }
                }
                if (task != null) {
                    clockItem.setProjectID(task.getProject().getObjectID());
                    clockItem.setProjectName(task.getProject().getName());
                    getTasks(clockItem, user, clockItem.getProjectID(), type, clientsCurrentDate);
                    if (type == PM_TASK) {
                        clockItem.setTaskID(task.getObjectID());
                        clockItem.setTaskName((task.getNumber() != null ? task.getNumber() + " - " : "") + task.getName());
                    } else {
                        clockItem.setIssueID(task.getObjectID());
                        clockItem.setIssueName(task.getName());
                    }

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
                            if (!ServerUtils.isNullOrEmpty(timeSheet.getComment())) {
                                clockItem.setComment(timeSheet.getComment());
                            }
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
                        clockItem.setCaseID(cases.getObjectID());
                        clockItem.setCaseName(cases.getSubject());
                    }
                }
            }
        }
        return clockItem;
    }

    @Override
    public Integer[] applyTime(ClockItem item) {
        Integer actual = item.getTodaysTime();
        Integer todays = null;
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
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
                            } catch (InterruptedException e) {
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
            List<EdsClockHistory> result = clockHistoryManager.getClockItems(item.getBusObjectId(), item.getRelation());
            boolean isNew = true;
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            for (EdsClockHistory history : result) {
                if (dateFormat.format(history.getDate()).equals(dateFormat.format(new Date())) && history.getOwner().equals(user)) {
                    history.setComment(history.getComment() + ". " + item.getComment());
                    history.setCumulativeTime(history.getCumulativeTime() + cumulative);
                    clockHistoryManager.createOrUpdate(history);
                    isNew = false;
                }

            }
            if (isNew) {
                EdsClockHistory history = new EdsClockHistory();
                history.setBusObjectId(item.getBusObjectId());
                history.setComment(item.getComment());
                history.setCumulativeTime(cumulative);
                history.setDate(new Date());
                history.setOwner(user);
                history.setRelation(item.getRelation());
                clockHistoryManager.create(history);
            }
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

    @Override
    public void stopTimer(ClockItem item) {
        EdsUser user = clockManager.getUser();
        if (item.getBusObjectId() == null || item.getRelation() == null) {
            ClockItem historyItem = getHistoryClockItem();
            item.setBusObjectId(historyItem.getBusObjectId());
            item.setRelation(historyItem.getRelation());
        }
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
            clock.setComment(item.getComment());
            clockManager.update(clock);
        }
    }

    @Override
    public ClockItem getProjectTasks(Integer projectID, Integer type, DateNonConvertable clientsCurrentDate) {
        ClockItem clockItem = new ClockItem();
        getTasks(clockItem, userManager.getUser(), projectID, type, clientsCurrentDate);
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
            EdsAttendanceRawData attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(clientsCurrentDate != null ? clientsCurrentDate.getDate() : null, employee.getObjectID());
            for (EdsEmployeeTask employeeTask : taskList) {
                EdsTask task = employeeTask.getTask();
                tasks.add(task.getAsSelectItem());

                AttendanceStats attendanceStats = new AttendanceStats();
                attendanceStats.setTaskStart(task.getStartDate());
                attendanceStats.setTaskEnd(task.getDueDate());
                if (attendanceRawData != null) {
                    attendanceStats.setHoliday(attendanceRawData.getHoliday());
                    attendanceStats.setDayOff(attendanceRawData.getDayOff());
                    attendanceStats.setTimeslotMinutes(attendanceRawData.getTimeSlot());
                    attendanceStats.setTimesheetMinutes(attendanceRawData.getTimeSheet() + attendanceRawData.getTimeSheetPending());
                    attendanceStats.setLeaveMinutes(attendanceRawData.getLeave());
                }
                taskMap.put(task.getObjectID(), attendanceStats);
            }
            clockItem.setTasks(tasks.toArray(new SelectItem[]{}));
            clockItem.setTaskMap(taskMap);
            return;
        }
        clockItem.setTasks(new SelectItem[]{});
        clockItem.setTaskMap(new HashMap<>());
    }

    @Override
    public ClockItem getHistoryClockItem() {
        EdsUser user = clockManager.getUser();
        List<EdsClock> clocks = clockManager.getClockItemsByUser(user);
        ClockItem result = null;
        EdsClock clock = null;
        if (clocks != null && !clocks.isEmpty()) {
            clock = clocks.get(0);
            result = new ClockItem();
            result.setBusObjectId(clock.getBusObjectId());
            result.setRelation(clock.getRelation());
            result.setHaveStoppedTime(false);
        } else {
            List<EdsClock> stopedClocks = clockManager.getStopedClockItemsByUser(user);
            if (stopedClocks != null && !stopedClocks.isEmpty()) {
                clock = stopedClocks.get(0);
                result = new ClockItem();
                result.setBusObjectId(clock.getBusObjectId());
                result.setRelation(clock.getRelation());
                result.setHaveStoppedTime(true);
            }
        }
        if (clock != null && result != null) {
            int lastTime = 0;
            if (clock.isStarted()) {
                int cumulativeTime = clock.getCumulativeTime() != null ? clock.getCumulativeTime() : 0;
                lastTime = (int) (((new Date().getTime() - clock.getStartDate().getTime()) / 1000) % (60 * 60 * 24)) + cumulativeTime;
            } else if (!clock.isReset()) {
                lastTime = clock.getCumulativeTime() != null ? clock.getCumulativeTime() : 0;
            }
            result.setObjectId(clock.getObjectID());
            result.setComment(clock.getComment());
            result.setTodaysTime(lastTime);
            result.setStartDate(clock.getStartDate());
            result.setEndDate(clock.getEndDate());
            result.setStarted(clock.isStarted());
            result.setReset(clock.isReset());
            EdsTask task = taskManager.get(clock.getBusObjectId());
            if (task != null) {
                result.setTaskID(task.getObjectID());
                result.setProjectID(task.getProject() != null ? task.getProject().getObjectID() : null);
                result.setProjectName(task.getProject() != null ? task.getProject().getName() : null);
                result.setTaskName(task.getName());
            }
        }
        return result;
    }

    @Override
    public ClockItem getHistoryMultiClockItem(Integer type) {
        EdsUser user = clockManager.getUser();
        List<EdsClock> clocks = null;
        if (type != null) {
            clocks = clockManager.getClockItemsByUserAndType(user, type);
        } else {
            clocks = clockManager.getClockItemsByUser(user);
        }
        if (clocks != null && clocks.size() > 0) {
            ClockItem item = new ClockItem();
            item.setBusObjectId(clocks.get(0).getBusObjectId());
            item.setRelation(clocks.get(0).getRelation());
            item.setHaveStoppedTime(false);
            return item;
        } else {
            List<EdsClock> stopedClocks = clockManager.getStopedClockItemsByUser(user);
            if (stopedClocks != null && stopedClocks.size() > 0) {
                ClockItem item = new ClockItem();
                item.setBusObjectId(stopedClocks.get(0).getBusObjectId());
                item.setRelation(stopedClocks.get(0).getRelation());
                item.setHaveStoppedTime(true);
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean getPMNumberingSettings() {
        EdsNumberingSettings edsSettings = numberingSettingsManager.getNumberingSetting();
        if (edsSettings != null) {
            return edsSettings.getEnableMultipleTimerInstances();
        }
        return true;
    }

    /**
     * @return Current day timesheet entries of loggedin user
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<ClockItem> getCurrentUserEntriesByDay(DateNonConvertable date) {
        ArrayList<ClockItem> result = new ArrayList<>();

        EdsEmployee employee = (EdsEmployee) taskManager.getUser();

        Calendar todayCal = new GregorianCalendar();

        todayCal.setTime(date.getNonConvertedDate());
        todayCal.set(Calendar.AM_PM, 0);
        todayCal.set(Calendar.HOUR, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        Calendar endCal = new GregorianCalendar();
        endCal.setTime(date.getNonConvertedDate());
        endCal.set(Calendar.AM_PM, 0);
        endCal.add(Calendar.DAY_OF_YEAR, 0);
        endCal.set(Calendar.HOUR, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);

        List<EdsTimeSheet> timesheets = timesheetManager.list(employee, todayCal.getTime(), endCal.getTime());
        HashMap<String, TimesheetSummary> timesheetSummaryMap = new HashMap<>();
        for (EdsTimeSheet timesheet : timesheets) {
            if(timesheet.getTimeSpent()!=null && timesheet.getTimeSpent()>0) {
                ClockItem entry = new ClockItem();
                entry.setStartDate(new Date(timesheet.getDate().getTime()));  // Non Convertable date is being used inside the TimeSheetDataItem
                entry.setObjectId(timesheet.getObjectID());
                entry.setComment(timesheet.getComment());
                entry.setActualTime(timesheet.getTimeSpent());
                entry.setProjectID(timesheet.getProjectID());
                entry.setTaskID(timesheet.getTaskID());
                if (timesheet.getProjectID() != null) {
                    EdsProject project = projectManager.get(timesheet.getProjectID());
                    if (project != null) {
                        entry.setProjectName(project.getName());
                    }
                }
                if (timesheet.getTaskID() != null) {
                    EdsTask task = taskManager.get(timesheet.getTaskID());
                    if (task != null) {
                        entry.setTaskName( (task.getNumber()!=null ? task.getNumber() + " - " : "") + task.getName());
                    }
                }
            /*
            FastTaskTransfer taskTransfer = resultMap.get(timesheet.getTaskID());
            if(taskTransfer==null) {
                taskTransfer = new FastTaskTransfer();
                if(timesheet.getProjectID()!=null) {
                    EdsProject project = projectManager.get(timesheet.getProjectID());
                    if(project!=null) {
                        taskTransfer.setProjectId(project.getObjectID());
                        taskTransfer.setProjectName(project.getName());
                    }
                }
                if(timesheet.getTaskID()!=null) {
                    EdsTask task = taskManager.get(timesheet.getTaskID());
                    if(task!=null) {
                        taskTransfer.setTaskId(task.getObjectID());
                        taskTransfer.setEmplTaskName(task.getName());
                    }
                }
            }*/

                result.add(entry);
            }
        }
        return result;
    }
}
