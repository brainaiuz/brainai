package com.edatasite.workforce.rest.v3.release10.pm.service;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.wfmTimer.client.rpc.ClockService;
import com.edatasite.workforce.rest.v3.release10.pm.dto.TaskTimerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.PM_TASK;

/**
 * User: Mukhammadrizo
 * Date: 06.09.2023
 */
@Service
public class TaskTimerService {

    private final ClockWidgetService taskTimerService;
    private final TaskManager taskManager;
    private final ProjectManager projectManager;
    private final LoginServiceLocal loginServiceLocal;
    private final ClockService clockService;
    private final WfmMessageSource wfmLocalizer;

    @Autowired
    public TaskTimerService(ClockWidgetService timesheetService, TaskManager taskManager, ProjectManager projectManager, LoginServiceLocal loginServiceLocal, RolePermissionService rolePermissionService, ClockService clockService,
                            WfmMessageSource wfmLocalizer) {
        this.taskTimerService = timesheetService;
        this.taskManager = taskManager;
        this.projectManager = projectManager;
        this.loginServiceLocal = loginServiceLocal;
        this.clockService = clockService;
        this.wfmLocalizer = wfmLocalizer;
    }


    public void startTimer(TaskTimerDto timerDto) throws Exception {
        if (taskTimerService.getHistoryClockItem() != null) {
            throw new RuntimeException("User has active timer");
        }
        ClockItem clockItem = new ClockItem();
        clockItem.setRelation(PM_TASK);
        clockItem.setStartDate(new Date());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 7);
        clockItem.setEndDate(calendar.getTime());
        clockItem.setOverrideAnotherTimerInstance(false);

        setDtoValuesToClockItem(timerDto, clockItem);


        try {
            taskTimerService.startTimer(clockItem);
        } catch (Exception ex) {
            throw new Exception();
        }
    }

    public void stopTaskTimer(TaskTimerDto timerDto) throws Exception {
        ClockItem clockItem = new ClockItem();
        setDtoValuesToClockItem(timerDto, clockItem);
        clockItem.setStarted(false);
        try {
            taskTimerService.stopTimer(clockItem);
        } catch (Exception ex) {
            throw new Exception();
        }
        UserSignUPSessionID loginedUser = loginServiceLocal.getSignedUser();
        if (loginedUser.isSaveTimerIntoTimesheetAutomatically()) {
//            clockItem.setReset(false);
//            taskTimerService.applyTime(clockItem);
        }

    }

    public void resetTaskTimer(TaskTimerDto timerDto) throws Exception {
        ClockItem clockItem = new ClockItem();
        setDtoValuesToClockItem(timerDto, clockItem);
        clockItem.setReset(true);
        try {
            taskTimerService.stopTimer(clockItem);
        } catch (Exception ex) {
            throw new Exception();
        }

    }

    private void setDtoValuesToClockItem(TaskTimerDto timerDto, ClockItem clockItem) throws Exception {
        clockItem.setRelation(PM_TASK);
        clockItem.setTaskID(timerDto.getTaskId());
        clockItem.setProjectID(timerDto.getProjectId());
        clockItem.setObjectId(timerDto.getObjectId());

        clockItem.setComment(timerDto.getComment());
        clockItem.setBusObjectId(timerDto.getTaskId());
        clockItem.setObjectId(timerDto.getObjectId());
        clockItem.setReset(timerDto.isReset());
        clockItem.setTodaysTime(timerDto.getTodaysTime() != null ? timerDto.getTodaysTime() : 0);

        clockItem.setElapsedTime(0);
        clockItem.setRemainingTime(0);

        if (timerDto.getTaskId() != null) {
            EdsTask task = taskManager.get(timerDto.getTaskId());
            if (task == null) {
                throw new Exception("Task not found");
            }
            clockItem.setTaskName(task.getName());
        }

        if (timerDto.getProjectId() != null) {
            EdsProject project = projectManager.get(timerDto.getProjectId());
            if (project == null) {
                throw new Exception("Project not found");
            }
            clockItem.setProjectName(project.getName());
            clockItem.setRelation(PM_TASK);
            clockItem.setTodaysTime(timerDto.getTodaysTime());
        }
    }

    public TaskTimerDto getActiveTimer() {
        ClockItem clockItem = taskTimerService.getHistoryClockItem();
        if (clockItem == null) return null;
        TaskTimerDto timerDto = new TaskTimerDto();
        timerDto.setBusObjectId(clockItem.getBusObjectId());
        timerDto.setObjectId(clockItem.getObjectId());
        timerDto.setTaskId(clockItem.getTaskID());
        timerDto.setProjectId(clockItem.getProjectID());
        timerDto.setProjectName(clockItem.getProjectName());
        timerDto.setStartDate(clockItem.getStartDate());
        timerDto.setEndDate(clockItem.getEndDate());
        timerDto.setComment(clockItem.getComment());
        timerDto.setStarted(clockItem.isStarted());
        timerDto.setTodaysTime(clockItem.getTodaysTime());
        timerDto.setHaveAStoppedTime(clockItem.isHaveStoppedTime());
        timerDto.setTaskName(clockItem.getTaskName());
        return timerDto;
    }

    public void logTime(TaskTimerDto timerDto) throws Exception {
        UserSignUPSessionID signedUser = loginServiceLocal.getSignedUser();
        if (signedUser.isValidateMaximumHours()) {
            ClockItem item = clockService.getClockItem(timerDto.getTaskId(), PM_TASK, new DateNonConvertable());
            AttendanceStats attendanceStats = item.getTaskMap().get(timerDto.getTaskId());
            Integer lastMinutes = timerDto.getTodaysTime();
            if (signedUser.isValidateTimeslot() && attendanceStats.getTimeslotMinutes() - attendanceStats.getTimesheetMinutes() < lastMinutes) {
                throw new RuntimeException(wfmLocalizer.localize("timeslotValidationMessage").replace("&nbsp;", " ") + attendanceStats.getTimeslotMinutes() + wfmLocalizer.localize("hours"));
            }
            int maxHoursAllowed = signedUser.getMaximumHours() != null ? signedUser.getMaximumHours() : 0;
            int remainingMinutes = (maxHoursAllowed * 60) - attendanceStats.getTimesheetMinutes();
            if (!signedUser.isValidateTimeslot() && remainingMinutes < lastMinutes) {
                throw new RuntimeException(wfmLocalizer.localize("timeslotValidationMessage").replace("&nbsp;", " ") + maxHoursAllowed + wfmLocalizer.localize("hours"));
            }
        }

        ClockItem clockItem = new ClockItem();
        setDtoValuesToClockItem(timerDto, clockItem);
        clockItem.setReset(Boolean.TRUE);
        taskTimerService.applyTime(clockItem);
    }
}
