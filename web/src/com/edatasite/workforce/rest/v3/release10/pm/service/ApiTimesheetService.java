package com.edatasite.workforce.rest.v3.release10.pm.service;

import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetApprovalSessionManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskTimeSheetEntry;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetEntriesPerPeriod;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetEntry;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.pm.dto.SubmitForApprovalDto;
import com.edatasite.workforce.rest.v3.release10.pm.dto.TimesheetDTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.TimesheetFilterDTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.TimesheetSettingsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.SERVER_ERROR;

/**
 * User: Akhror
 * Date: 26.07.2021
 */
@Service
public class ApiTimesheetService {

    private final EmployeeTaskManager employeeTaskManager;
    private final EmployeeManager employeeManager;
    private final TaskManager taskManager;
    private final TimesheetService timesheetService;
    private final TaskService taskService;
    private final ProjectManager projectManager;
    private final TimeSheetManager timeSheetManager;
    private final WfmMessageSource referenceWfmMessageSource;
    private final NumberingSettingsManager numberingSettingsManager;
    private final AttendanceRawDataManager attendanceRawDataManager;
    private final CompanySystemSettingsManager companySystemSettingsManager;

    private final TimeSheetApprovalSessionManager timeSheetApprovalSessionManager;

    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;

    @Autowired
    public ApiTimesheetService(EmployeeTaskManager employeeTaskManager, EmployeeManager employeeManager, TaskManager taskManager, TimesheetService timesheetService, TaskService taskService, ProjectManager projectManager, TimeSheetManager timeSheetManager, @Qualifier("referenceWfmMessageSource") WfmMessageSource referenceWfmMessageSource, NumberingSettingsManager numberingSettingsManager, AttendanceRawDataManager attendanceRawDataManager, CompanySystemSettingsManager companySystemSettingsManager, TimeSheetApprovalSessionManager timeSheetApprovalSessionManager) {
        this.employeeTaskManager = employeeTaskManager;
        this.employeeManager = employeeManager;
        this.taskManager = taskManager;
        this.timesheetService = timesheetService;
        this.taskService = taskService;
        this.projectManager = projectManager;
        this.timeSheetManager = timeSheetManager;
        this.referenceWfmMessageSource = referenceWfmMessageSource;
        this.numberingSettingsManager = numberingSettingsManager;
        this.attendanceRawDataManager = attendanceRawDataManager;
        this.companySystemSettingsManager = companySystemSettingsManager;
        this.timeSheetApprovalSessionManager = timeSheetApprovalSessionManager;
    }

    public Integer save(final TimesheetDTO dto) throws RestException {
        TimesheetDataItem item = new TimesheetDataItem();
        Date clientToday = new Date();
        EdsEmployee employee = null;
        if (dto.getEmployee() != null) {
            if (dto.getEmployee().getId() != null) {
                employee = employeeManager.get(dto.getEmployee().getId());
            } else if (dto.getEmployee().getCode() != null) {
                employee = employeeManager.getEmployeeByNumber(dto.getEmployee().getCode());
            } else if (dto.getEmployee().getName() != null) {
                employee = employeeManager.getEmployeeByFirstNameViaLastName(dto.getEmployee().getName());
            }
        }
        if (employee == null) {
            employee = (EdsEmployee) employeeManager.getUser();
        }
        EdsTask task = null;
        if (dto.getTask().getId() != null) {
            task = taskManager.get(dto.getTask().getId());
        }
        if (task == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        EdsAttendanceRawData attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(dto.getDate(), employee.getObjectID());
        EdsNumberingSettings edsSettings = null;
        edsSettings = numberingSettingsManager.getNumberingSetting();
        Integer timesheetMinutes = attendanceRawData.getTimeSheet() + attendanceRawData.getTimeSheetPending();
        if (edsSettings != null) {
            if (edsSettings.isValidateTaskStart()) {
                boolean prevDaysCurrentMonth = clientToday.getYear() == task.getStartDate().getYear() && clientToday.getMonth() == task.getStartDate().getMonth() && clientToday.getDate() < task.getStartDate().getDate();
                boolean prevMonthsCurrentYear = clientToday.getYear() == task.getStartDate().getYear() && clientToday.getMonth() < task.getStartDate().getMonth();
                boolean prevYears = clientToday.getYear() < task.getStartDate().getYear();
                if (prevDaysCurrentMonth || prevMonthsCurrentYear || prevYears) {
                    throw new RestException(ERROR_MESSAGE, "You cannot fill timesheet before task start date. The task start date is: " + ServerUtils.formatFilterDate(task.getStartDate()), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);}
            }
            if (edsSettings.isValidateTaskEnd()) {
                boolean prevDaysCurrentMonth = clientToday.getYear() == task.getDueDate().getYear() && clientToday.getMonth() == task.getDueDate().getMonth() && clientToday.getDate() > task.getDueDate().getDate();
                boolean prevMonthsCurrentYear = clientToday.getYear() == task.getDueDate().getYear() && clientToday.getMonth() > task.getDueDate().getMonth();
                boolean prevYears = clientToday.getYear() > task.getDueDate().getYear();
                if (prevDaysCurrentMonth || prevMonthsCurrentYear || prevYears) {
                    throw new RestException(ERROR_MESSAGE, "You cannot fill timesheet after task end date. The task end date is: " + ServerUtils.formatFilterDate(task.getDueDate()), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            if (edsSettings.isValidateHoliday()) {
                if (attendanceRawData != null && attendanceRawData.getHoliday()) {
                    throw new RestException(ERROR_MESSAGE, "You cannot fill timesheet for holiday", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            if (edsSettings.isValidateDayOff()) {
                if (attendanceRawData != null && attendanceRawData.getDayOff()) {
                    throw new RestException(ERROR_MESSAGE, "You cannot fill timesheet for day off", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            if (edsSettings.isValidateLeaveRequest()) {
                if (attendanceRawData != null && attendanceRawData.getLeave() > 0) {
                    if (attendanceRawData.getTimeSlot() - attendanceRawData.getLeave() == 0) {
                        throw new RestException(ERROR_MESSAGE, "You cannot fill timesheet while you have approved Leave Request for this day", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    int maxMinutesAllowed = 0;
                    if (edsSettings.isValidateMaximumHours() && !edsSettings.isValidateTimeslot()) {
                        if (edsSettings.getMaximumHours() != null && !edsSettings.getMaximumHours().equals("")) {
                            maxMinutesAllowed = Integer.valueOf(edsSettings.getMaximumHours()) * 60;
                        }
                    } else {
                        maxMinutesAllowed = attendanceRawData.getTimeSlot();
                    }
                    if (maxMinutesAllowed - attendanceRawData.getLeave() - timesheetMinutes < dto.getMinutes()) {
                        throw new RestException(ERROR_MESSAGE, "Approved hourly leave exists. Total work and leave hours exceed daily limit.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
            if (edsSettings.isValidateMaximumHours()) {
                if (edsSettings.isValidateTimeslot()) {
                    if (attendanceRawData.getTimeSlot() - timesheetMinutes < dto.getMinutes()) {
                        throw new RestException(ERROR_MESSAGE, "Total daily hours cannot exceed the limit.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    int maxHoursAllowed = 0;
                    if (edsSettings.getMaximumHours() != null && !edsSettings.getMaximumHours().equals("")) {
                        maxHoursAllowed = Integer.valueOf(edsSettings.getMaximumHours());
                    }
                    if ((maxHoursAllowed * 60) - timesheetMinutes< dto.getMinutes()) {
                        throw new RestException(ERROR_MESSAGE, "Total daily hours cannot exceed the limit.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
            if (edsSettings.isValidatePastTimesheet()) {
                Integer pastDays = Optional.of(edsSettings).map(EdsNumberingSettings::getPastTimesheetDays).orElse(0);
                if (dto.getDate().getTime() < clientToday.getTime()) {
                    if (pastDays < Math.floor((clientToday.getTime() - dto.getDate().getTime()) / (double) (1000 * 60 * 60 * 24))) {
                        throw new RestException(ERROR_MESSAGE, "You can edit past timesheet hours only within " + pastDays + " days range  from current date", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
            if (edsSettings.isValidateFutureTimesheet()) {
                int futureDays = Optional.of(edsSettings).map(EdsNumberingSettings::getFutureTimesheetDays).orElse(0);
                if (dto.getDate().getTime() > clientToday.getTime()) {
                    if (futureDays < Math.ceil((dto.getDate().getTime() - clientToday.getTime()) / (double) (1000 * 60 * 60 * 24))) {
                        throw new RestException(ERROR_MESSAGE, "You can edit future timesheet hours only within " + futureDays + " days range  from current date", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        }
        EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeRelatedTask(task, employee);
        item.setEmployeeTaskID(employeeTask.getObjectID());
        item.setMinutes(dto.getMinutes());
        item.setComment(dto.getComment());
        FastTaskTransfer transferTask = new FastTaskTransfer();
        EdsProject domainProject = employeeTask.getTask().getProject();
        EdsCrmAccount client = domainProject.getClient();

        transferTask.setTaskNumber(employeeTask.getTask().getNumber());

        if (employeeTask.getTask() instanceof EdsIssue) {
            transferTask.setIsIssue(true);
        } else {
            transferTask.setIsIssue(false);
        }

        transferTask.setEmplTaskId(employeeTask.getObjectID());
        transferTask.setEmplTaskName(employeeTask.getTask().getName());

        if (!domainProject.getClients().isEmpty()) {
            StringBuilder buildClientName = new StringBuilder();

            for (EdsCrmAccount c : domainProject.getClients()) {
                if (!buildClientName.toString().isEmpty()) {
                    buildClientName.append(", ");
                }
                buildClientName.append(c.getName());
            }

            if (transferTask.getClientName() == null) {
                transferTask.setClientName(!buildClientName.toString().isEmpty() ? buildClientName.toString() : "N/A");
            }
        } else if (client != null) {
            transferTask.setClientName(client.getName());
        } else {
            transferTask.setClientName("N/A");
        }

        String number = domainProject.getNumber() != null ? domainProject.getNumber() + " " : "";

        transferTask.setProjectId(domainProject.getObjectID());
        transferTask.setProjectName(number + domainProject.getName());
        transferTask.getTaskStatus().setEmployeeTaskId(employeeTask.getObjectID());
        transferTask.getTaskStatus().setStatus(employeeTask.getStatus().getObjectID());
        transferTask.getTaskStatus().setStatusName(commonLocalizer.localizeRef(employeeTask.getStatus()));
        transferTask.getTaskStatus().setTaskId(employeeTask.getObjectID());
        if (employeeTask.getTask().getPriority() != null) {
            transferTask.getTaskStatus().setPriority(employeeTask.getTask().getPriority().getName());
        }
        transferTask.setEstimatedTime(employeeTask.getEstimatedTime());
        transferTask.setPercentCompleted(employeeTask.getPercent() == null ? 0 : ServerUtils.decimalPrecision(employeeTask.getPercent(), 2));
        transferTask.setTaskId(employeeTask.getTask().getObjectID());

        Calendar emplTaskStart = new GregorianCalendar();
        if (employeeTask.getStartDate() != null) {
            emplTaskStart.setTime(employeeTask.getStartDate());
        } else {
            emplTaskStart.setTime(employeeTask.getTask().getStartDate());
        }

        emplTaskStart.set(Calendar.AM_PM, 0);
        emplTaskStart.set(Calendar.HOUR, 0);
        emplTaskStart.set(Calendar.MINUTE, 0);
        emplTaskStart.set(Calendar.SECOND, 0);
        emplTaskStart.set(Calendar.MILLISECOND, 0);
        transferTask.setStartDate(emplTaskStart.getTime());
        if (employeeTask.getEndDate() != null) {
            Calendar emplTaskEnd = new GregorianCalendar();
            emplTaskEnd.setTime(employeeTask.getEndDate());
            emplTaskEnd.set(Calendar.AM_PM, 0);
            emplTaskEnd.set(Calendar.HOUR, 23);
            emplTaskEnd.set(Calendar.MINUTE, 59);
            emplTaskEnd.set(Calendar.SECOND, 59);
            emplTaskEnd.set(Calendar.MILLISECOND, 0);
            transferTask.setEndDate(emplTaskEnd.getTime());
        }
        if (employeeTask.getTask().getStartDate() != null) {
            transferTask.setTaskStartDate(employeeTask.getTask().getStartDate());
        }
        if (employeeTask.getTask().getDueDate() != null) {
            transferTask.setTaskEndDate(employeeTask.getTask().getDueDate());
        }
        item.setTaskTransfer(transferTask);
        item.setDateNonConvertable(new DateNonConvertable(DateUtils.resetTime(dto.getDate() != null ? dto.getDate() : new Date())));
        item.setCurrentServerDate(new Date());
        item.setMinutes(dto.getMinutes());
        item.setComment(dto.getComment());
        item.setProjectID(domainProject.getObjectID());
        item.setTimesheetMinutes(dto.getMinutes());
        item.setTaskStart(employeeTask.getStartDate());
        item.setTaskEnd(employeeTask.getEndDate());

        return timesheetService.applyUpdates(item, null);
    }

    public List<TimesheetDTO> getTimesheet(TimesheetFilterDTO filter) throws RestException {
        ListingFilterParameter fp = new ListingFilterParameter();
        List<TimesheetDTO> result = new ArrayList<>();
        EdsEmployee employee = null;
        if (filter.getEmployee() != null) {
            if (filter.getEmployee().getId() != null && filter.getEmployee().getId() > 0) {
                employee = employeeManager.get(filter.getEmployee().getId());
            } else if (filter.getEmployee().getCode() != null && !"".equals(filter.getEmployee().getCode())) {
                employee = employeeManager.getEmployeeByNumber(filter.getEmployee().getCode());
            } else if (filter.getEmployee().getName() != null && !"".equals(filter.getEmployee().getName())) {
                employee = employeeManager.getEmployeeByFirstNameViaLastName(filter.getEmployee().getName());
            }
        }
        if (employee != null) {
            fp.setEmployeeId(employee.getObjectID());
        }
        if (filter.getStartDate() != null) {
            fp.setStartDate(filter.getStartDate());
        }
        if (filter.getEndDate() != null) {
            fp.setEndDate(filter.getEndDate());
        }

        if (filter.getTask() != null) {
            EdsTask task = null;
            if (filter.getTask().getId() != null && filter.getTask().getId() > 0) {
                task = taskManager.get(filter.getTask().getId());
            }

            if (task == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "task is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            fp.setTaskID(task.getObjectID());

            ListResult<TaskTimeEntriesItem> entries = taskService.getTaskTimeEntriesList(fp);
            if (entries.getList() != null && entries.getTotal() > 0) {
                for (TaskTimeEntriesItem item : entries.getList()) {
                    TimesheetDTO dto = new TimesheetDTO();
                    dto.setComment(item.getComment());
                    dto.setMinutes(item.getTimeSpent());
                    dto.setDate(item.getDate().getNonConvertedDate());
                    dto.setEmployee(new ItemDto(item.getEmployeeId(), item.getEmloyee(), item.getEmloyeeCode()));
                    dto.setTask(new ItemDto(item.getTaskId(), item.getTaskName()));
                    result.add(dto);
                }
            }
            return result;
        }

        Integer weekOffSet = timesheetService.getWeekOffset(new DateNonConvertable(filter.getStartDate()), new DateNonConvertable(filter.getEndDate()));
        fp.setSelectedDay(filter.getStartDate().getDay());
        fp.setSelectedMonth(filter.getStartDate().getMonth());
        fp.setSelectedYear(filter.getStartDate().getYear());
        TimesheetData timesheetData = timesheetService.getData(new DateNonConvertable(filter.getStartDate()), weekOffSet, fp);
        if (timesheetData.getItems() != null) {
            timesheetData.getItems();
            for (TimesheetDataItem item : timesheetData.getItems()) {
                TimesheetDTO dto = new TimesheetDTO();
                dto.setComment(item.getComment());
                dto.setMinutes(item.getMinutes());
                dto.setDate(item.getDateNonConvertable().getNonConvertedDate());
                EdsEmployee edsEmployee = employeeManager.get(item.getEmployeeID());
                if (edsEmployee != null) {
                    dto.setEmployee(new ItemDto(edsEmployee.getObjectID(), edsEmployee.getFullName(), edsEmployee.getProfile().getEmployeeCode()));
                }
                EdsTask edsTask = taskManager.get(item.getTaskID());
                dto.setTask(new ItemDto(edsTask.getObjectID(), edsTask.getName(), edsTask.getNumber()));
                result.add(dto);
            }
        }
        return result;
    }


    public List<TimesheetDTO> getProjectTimesheets(TimesheetFilterDTO filter) throws RestException {
        ListingFilterParameter fp = new ListingFilterParameter();
        List<TimesheetDTO> result = new ArrayList<>();
        EdsEmployee employee = null;
        fp.setStartDate(filter.getStartDate());
        fp.setEndDate(filter.getEndDate());
        if (filter.getEmployee() != null) {
            if (filter.getEmployee().getId() != null && filter.getEmployee().getId() > 0) {
                employee = employeeManager.get(filter.getEmployee().getId());
            } else if (filter.getEmployee().getCode() != null && !"".equals(filter.getEmployee().getCode())) {
                employee = employeeManager.getEmployeeByNumber(filter.getEmployee().getCode());
            } else if (filter.getEmployee().getName() != null && !"".equals(filter.getEmployee().getName())) {
                employee = employeeManager.getEmployeeByFirstNameViaLastName(filter.getEmployee().getName());
            }
        }
        if (employee != null) {
            fp.setEmployeeId(employee.getObjectID());
        }
        if (filter.getProject() != null) {
            EdsProject project = null;
            if (filter.getProject().getId() != null && filter.getProject().getId() > 0) {
                project = projectManager.get(filter.getProject().getId());
            }

            if (project == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Project is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            fp.setProjectId(project.getObjectID());

            List<EdsTimeSheet> entries = timeSheetManager.getProjectTimsheets(fp);
            if (entries != null && entries.size() > 0) {
                for (EdsTimeSheet item : entries) {
                    TimesheetDTO dto = new TimesheetDTO();
                    dto.setComment(item.getComment());
                    dto.setMinutes(item.getTimeSpent());
                    dto.setDate(item.getDate());
                    dto.setMinutes(item.getTimeSpent());
                    if (item.getStatus() != null) {
                        dto.setStatus(item.getStatus() != null ? referenceWfmMessageSource.localizeRef(item.getStatus()) : "");
                    }
                    if (item.getEmployeeTask() != null && item.getEmployeeTask().getTask() != null) {
                        ItemDto task = new ItemDto();
                        task.setId(item.getEmployeeTask().getTask().getObjectID());
                        task.setName(item.getEmployeeTask().getTask().getName());
                        task.setCode(item.getEmployeeTask().getTask().getDescription());
                        dto.setTask(task);
                    }
                    EdsEmployee timesheetEmployee = employeeManager.get(item.getEmployeeID());
                    if (timesheetEmployee != null) {
                        dto.setEmployee(new ItemDto(timesheetEmployee.getObjectID(), timesheetEmployee.getFullName()));
                    }
                    result.add(dto);
                }
            }
            return result;
        }
        return null;
    }

    public TimesheetSettingsDto getTimesheetSettings() throws RestException {
        EdsNumberingSettings timesheetSettings = numberingSettingsManager.getNumberingSetting();
        if (timesheetSettings != null) {
            TimesheetSettingsDto settingsDto = new TimesheetSettingsDto();
            settingsDto.setAutomatically(timesheetSettings.isAutomaticApproval());
            settingsDto.setSubmitForApproval(timesheetSettings.isWaitingForApproval());
            settingsDto.setManually(!(timesheetSettings.isAutomaticApproval() || timesheetSettings.isWaitingForApproval()));
            settingsDto.setTimesheetWeekStart(timesheetSettings.getTimesheetWeekStart());
            settingsDto.setSortTimesheetByTaskName(timesheetSettings.getSortTimesheetByTaskName());
            settingsDto.setTimesheetDateFormat(timesheetSettings.getTimesheetDateFormat());
            settingsDto.setAutomaticPercent(timesheetSettings.isAutomatic());
            settingsDto.setManualPercent(!timesheetSettings.isAutomatic());
            settingsDto.setShowCompletedTasks(timesheetSettings.getShowCompletedTasks());
            settingsDto.setTimesheetCommentRequired(timesheetSettings.getTimesheetCommentRequired());
            settingsDto.setTimesheetApprovalCommentRequired(timesheetSettings.getTimesheetApprovalCommentRequired());
            settingsDto.setShowTaskRelated(companySystemSettingsManager.showTaskRelated());
            settingsDto.setShowTimesheetHourTypes(timesheetSettings.getShowTimesheetHourTypes());
            settingsDto.setShowToDoListTasks(timesheetSettings.getShowToDoListTasks());
            settingsDto.setEnableMultipleTimerInstances(timesheetSettings.getEnableMultipleTimerInstances());
            settingsDto.setSaveTimerIntoTimesheetAutomatically(timesheetSettings.getSaveTimerIntoTimesheetAutomatically());
            settingsDto.setDailyFillTimesheetFromResUtilRequired(timesheetSettings.getDailyFillTimesheetFromResUtilRequired());
            settingsDto.setValidateTaskStart(timesheetSettings.isValidateTaskStart());
            settingsDto.setValidateTaskEnd(timesheetSettings.isValidateTaskEnd());
            settingsDto.setValidateHoliday(timesheetSettings.isValidateHoliday());
            settingsDto.setValidateLeaveRequest(timesheetSettings.isValidateLeaveRequest());
            settingsDto.setValidateMaximumHours(timesheetSettings.isValidateMaximumHours());
            settingsDto.setValidateTimeslot(timesheetSettings.isValidateTimeslot());
            settingsDto.setValidateHours(!timesheetSettings.isValidateTimeslot());
            settingsDto.setMaximumHours(timesheetSettings.getMaximumHours());
            settingsDto.setValidateDayOff(timesheetSettings.isValidateDayOff());
            settingsDto.setValidateTimesheetEstimate(timesheetSettings.isValidateTimesheetEstimate());
            settingsDto.setValidatePastTimesheet(timesheetSettings.isValidatePastTimesheet());
            settingsDto.setPastTimesheetDays(timesheetSettings.getPastTimesheetDays());
            settingsDto.setValidateFutureTimesheet(timesheetSettings.isValidateFutureTimesheet());
            settingsDto.setFutureTimesheetDays(timesheetSettings.getFutureTimesheetDays());
            return settingsDto;
        }
        throw new RestException(GENERAL_ERROR_MESSAGE, "Timesheet settings not found", NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public String submitForApproval(SubmitForApprovalDto filterDTO) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setProjectId(filterDTO.getProject().getId());
        filterParameter.setEmployeeId(filterDTO.getEmployee().getId());
        filterParameter.setStartDate(filterDTO.getStartDate());
        filterParameter.setEndDate(filterDTO.getEndDate());
        if (filterDTO.getSelectAll()) {
            filterParameter.setObjectsIds(null);
        } else if (filterDTO.getApprovedTimesheetIds() != null) {
            filterParameter.setObjectsIds(ServerUtils.getAsCommoDelimited(filterDTO.getApprovedTimesheetIds(), "0", ","));
        }
        TimeSheetEntriesPerPeriod timeSheetEntriesPerPeriod = getTimesheetEntries(filterParameter);
        timesheetService.submitTimesheetForApproval(timeSheetEntriesPerPeriod);
        return "Sended to successfully";
    }

    private TimeSheetEntriesPerPeriod getTimesheetEntries(ListingFilterParameter fp) {
        HashMap<Integer, List<TimeSheetEntry>> taskTimeEntries = new HashMap();
        ArrayList<EdsEmployeeTask> tasks = new ArrayList();
        ArrayList<Integer> taskIdList = new ArrayList();
        TimeSheetEntriesPerPeriod tentries = new TimeSheetEntriesPerPeriod();
        List<EdsTimeSheet> entries = timeSheetManager.getProjectTimsheets(fp);
        for (EdsTimeSheet ts : entries) {
            if (!taskIdList.contains(ts.getEmployeeTask().getObjectID())) {
                taskIdList.add(ts.getEmployeeTask().getObjectID());
                tasks.add(ts.getEmployeeTask());
            }
            if (!taskTimeEntries.containsKey(ts.getEmployeeTask().getObjectID())) {
                taskTimeEntries.put(ts.getEmployeeTask().getObjectID(), new ArrayList());
            }
            TimeSheetEntry te = new TimeSheetEntry();
            te.setTimeSheetId(ts.getObjectID());
            if (ts.getTimeSpent() != null) {
                te.setTimeSpent(ts.getTimeSpent());
            } else {
                te.setTimeSpent(0);
            }
            taskTimeEntries.get(ts.getEmployeeTask().getObjectID()).add(te);
        }
        TaskTimeSheetEntry[] entry = new TaskTimeSheetEntry[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            EdsEmployeeTask empTask = (EdsEmployeeTask) tasks.get(i);
            Integer taskid = empTask.getObjectID();
            entry[i] = new TaskTimeSheetEntry();
            entry[i].setTaskId(taskid);
            entry[i].setProjectId(empTask.getTask().getProject().getObjectID());
            entry[i].setTaskName(empTask.getTask().getName());
            TimeSheetEntry[] tes = taskTimeEntries.get(taskid).toArray(new TimeSheetEntry[]{});
            entry[i].setEntries(tes);
            int totaltimespent = 0;
            for (TimeSheetEntry te : tes) {
                totaltimespent += te.getTimeSpent();
            }
            entry[i].setTotalTimeSpent(totaltimespent);
        }
        tentries.setEntries(entry);
        tentries.setFromDate(new DateNonConvertable(fp.getStartDate()));
        tentries.setToDate(new DateNonConvertable(fp.getEndDate()));
        tentries.setEmployeeID(fp.getEmployeeId());
        return tentries;
    }
}
