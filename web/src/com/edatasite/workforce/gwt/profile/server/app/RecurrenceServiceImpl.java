package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsRecurrenceHistory;
import com.edatasite.workforce.core.domain.EdsRecurrenceJob;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsServerHistory;
import com.edatasite.workforce.core.domain.EdsTemporaryRecurrence;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringBill;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringInvoice;
import com.edatasite.workforce.core.domain.settings.EdsOverdueInvoiceReminderSettings;
import com.edatasite.workforce.gwt.backend.client.rpc.RecurrenceLogItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceJobManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.ServerHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TemporaryRecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.settings.OverdueInvoiceReminderSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.mail.MassMaillerSendMessage;
import com.edatasite.workforce.outfromlisten.RecurrenceServiceLocal;
import com.edatasite.workforce.scheduler.CalendarEventRecurrenceJob;
import com.edatasite.workforce.scheduler.ContractOverdueReminderRecurrenceJob;
import com.edatasite.workforce.scheduler.DailyAutoFillTimesheetRecurrenceJob;
import com.edatasite.workforce.scheduler.DailyHrReminderProcedureJob;
import com.edatasite.workforce.scheduler.DeferredTransactionsRecurrenceJob;
import com.edatasite.workforce.scheduler.EmployeeVisaExpirationDateRecurrenceJob;
import com.edatasite.workforce.scheduler.ProjectOverdueReminderRecurrenceJob;
import com.edatasite.workforce.scheduler.RecuringRestartProjectNumberJob;
import com.edatasite.workforce.scheduler.RecurringBillJob;
import com.edatasite.workforce.scheduler.RecurringJobsManager;
import com.edatasite.workforce.scheduler.RecurringManualJournalJob;
import com.edatasite.workforce.scheduler.RecurringReportJob;
import com.edatasite.workforce.scheduler.RecurringRestartLeaveRequestNumberJob;
import com.edatasite.workforce.scheduler.RecurringSaleInvoiceJob;
import com.edatasite.workforce.scheduler.RecurringWorkflowManagementJob;
import com.edatasite.workforce.scheduler.SynchronizeMagentoCatalogRecurrenceJob;
import com.edatasite.workforce.scheduler.SynchronizeWithGoogleCalendarRecurrenceJob;
import com.edatasite.workforce.scheduler.SynchronizeWithGoogleContactsRecurrenceJob;
import com.edatasite.workforce.scheduler.SynchronizeWithOfficeCalendarRecurrenceJob;
import com.edatasite.workforce.scheduler.TaskOverdueReminderRecurrenceJob;
import com.edatasite.workforce.scheduler.TimesheetRecurrenceJob;
import com.edatasite.workforce.scheduler.WorkflowManagementRecurrenceJob;
import com.finnetlimited.reportservice.core.server.db.schema.TelegramReportingRecurrenceManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsTelegramReportingScheduleRule;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.TriggerUtils;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.AbstractTrigger;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * User: Ilxom Lutfullaev
 * Date: 01.05.2010
 * Time: 16:13:34
 */

@Transactional
@Service("recurrenceService")
public class RecurrenceServiceImpl implements RecurrenceService, RecurrenceServiceLocal, SchedulerConstant, Constants {
    private static final Logger logger = LoggerFactory.getLogger(RecurrenceServiceImpl.class);
    private final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    @Autowired
    private RecurrenceJobManager recurrenceJobManager;
    @Autowired
    private ServerHistoryManager serverHistoryManager;
    @Autowired
    private RecurrenceHistoryManager recurrenceHistoryManager;
    @Autowired
    private RecurringJobsManager recurringJobsManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private TemporaryRecurrenceManager temporaryRecurrenceManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private OverdueInvoiceReminderSettingsManager overdueInvoiceReminderSettingsManager;
    @Autowired
    private TelegramReportingRecurrenceManager telegramReportingRecurrenceManager;
    @Autowired
    private ScheduledCourseManager scheduledCourseManager;

    @Override
    public Integer saveRecurrenceJob(RecurrenceJobItem item) {
        EdsRecurrenceJob job = null;
        if (item.getJobType() != null) {
            job = recurrenceJobManager.get(item.getJobType());
        }
        EdsUser user = recurrenceJobManager.getUser();

        boolean defaultTimeSheetReminder = item.getJobType() != null && item.getJobType() == SchedulerConstant.TIMESHEET_REMINDER && item.getDefaultReminder() != null && item.getDefaultReminder();

        EdsRecurrence recurrence = item.getObjectId() != null ? recurrenceManager.get(item.getObjectId()) : new EdsRecurrence();
        if (item.isEnabled()) {
            wrapRecurrenceJobItemToEdsRecurrence(item, recurrence, job);
            //Override wrap method based on specific business logic, BusObjs mostly
            recurrence.setType(item.getType());
            recurrence.setCompanyID(user != null && user.getCompany() != null ? user.getCompany().getObjectID() : Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
            recurrence.setUserID(user != null ? user.getObjectID() : null);
            if (recurrence.getType() == RECURRENCE_TYPE_EVEN_DAILY || recurrence.getType() == RECURRENCE_TYPE_ODD_DAILY) {
                recurrence.setInterval(1);
                recurrence.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
            }
            if (item.getJobType() == TIMESHEET_REMINDER || item.getJobType() == OVERDUE_INVOICE_REMINDER || item.getJobType() == SYNCHRONIZE_MAGENTO_CATALOG ||
                    item.getJobType() == SYNCHRONIZE_GOOGLE_CONTACT || item.getJobType() == SYNCHRONIZE_GOOGLE_CALENDAR || item.getJobType() == SYNCHRONIZE_OFFICE_CALENDAR) {
                if (!defaultTimeSheetReminder) {
                    recurrence.setBusObjectId(user.getObjectID());
                }
            } else {
                if (!defaultTimeSheetReminder) {
                    recurrence.setBusObjectId(item.getBusObjectId());
                }
            }

            if (item.getJobType() == SYNCHRONIZE_GOOGLE_CALENDAR || item.getJobType() == SYNCHRONIZE_OFFICE_CALENDAR) {
                EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(recurrence.getCompanyID());
                if (settings != null) {
                    recurrence.setInterval(settings.getGoogleCalendarAutoSyncInterval());
                } else {
                    recurrence.setInterval(60); // This is default interval = 60 minutes
                }
            }

            Scheduler scheduler = recurringJobsManager.getScheduler();
            if (item.getObjectId() == null) {
                recurrenceManager.create(recurrence);
            } else {
                recurrenceManager.update(recurrence);
                removeTriggerFromScheduler(scheduler, recurrence.getObjectID());
            }
            createRecurrence(recurrence, scheduler);

            return recurrence.getObjectID();
        } else {
            updateRecurrence(recurrence, true, true);
        }
        return null;
    }

    public void reLoadTrigger(EdsRecurrence recurrence) {
        Scheduler scheduler = recurringJobsManager.getScheduler();
        removeTriggerFromScheduler(scheduler, recurrence.getObjectID());
        createRecurrence(recurrence, scheduler);
    }

    public void setRecurrenceStatus(Integer recurrenceID, String status) {
        EdsRecurrence recurrence = recurrenceManager.get(recurrenceID);
        if (recurrence != null) {
            recurrence.setStatus(status);
            if (FAIL.equals(status)) {
                recurrence.setAttempts(recurrence.getAttempts() + 1);
            }
            recurrenceManager.update(recurrence);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void wrapRecurrenceJobItemToEdsRecurrence(RecurrenceJobItem item, EdsRecurrence recurrence, EdsRecurrenceJob job) {
        if (recurrence != null) {
            recurrence.setBusObjectId(item.getBusObjectId());
            recurrence.setType(item.getType());
            recurrence.setBusObjectParams(item.getBusObjectParams());
            recurrence.setUserTimeZone(item.getUserTimeZone());
            recurrence.setJob(job);
            recurrence.setDailyPatternOptions(item.getDailyPatternOptions());
            recurrence.setInterval(item.getInterval());
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(item.getStartDate());
            startCalendar.set(Calendar.SECOND, 0);
            startCalendar.set(Calendar.MILLISECOND, 0);
            recurrence.setStartDate(startCalendar.getTime());
            if (item.getEndDate() != null) {
                Calendar endCalendar = new GregorianCalendar();
                endCalendar.setTime(item.getEndDate());
                endCalendar.set(Calendar.SECOND, 0);
                endCalendar.set(Calendar.MILLISECOND, 0);
                recurrence.setEndDate(endCalendar.getTime());
            } else {
                recurrence.setEndDate(null);
            }
            recurrence.setOccurrence(item.getOccurrence());
            recurrence.setSunday(item.isSunday() != null && item.isSunday());
            recurrence.setMonday(item.isMonday() != null && item.isMonday());
            recurrence.setTuesday(item.isTuesday() != null && item.isTuesday());
            recurrence.setWednesday(item.isWednesday() != null && item.isWednesday());
            recurrence.setThursday(item.isThursday() != null && item.isThursday());
            recurrence.setFriday(item.isFriday() != null && item.isFriday());
            recurrence.setSaturday(item.isSaturday() != null && item.isSaturday());
            recurrence.setEndType(item.getEndType());
            recurrence.setMonthlyOrYearlyDay(item.getMonthlyOrYearlyDay());
            recurrence.setCustomPatternDay(item.getCustomPatternDay());
            recurrence.setDailyPatternOptions(item.getDailyPatternOptions());
            recurrence.setMonthlyOrYearlyPatternOption(item.getMonthlyOrYearlyPatternOption());
            recurrence.setMonthlyOrYearlyDay(item.getMonthlyOrYearlyDay());
            recurrence.setYearlyMonth(item.getYearlyMonth());
            recurrence.setToMe(item.getToMe() != null ? item.getToMe() : false);
            recurrence.setToClient(item.getToClient() != null ? item.getToClient() : false);
            recurrence.setChanged(true);
            recurrence.setAttempts(0);
            recurrence.setStatus(null);
        }
    }

    public void removeTriggerFromScheduler(Integer recurrenceID) {
        removeTriggerFromScheduler(recurringJobsManager.getScheduler(), recurrenceID);
    }

    /**
     * this method used for editing or deleting recurrence from Scheduler (from RAM)
     *
     * @param scheduler    -  list of working scheduler
     * @param recurrenceID -  RecurrenceJobItem item ID (transfer object)
     */
    private void removeTriggerFromScheduler(Scheduler scheduler, Integer recurrenceID) {
        try {
            String jobName = JOB_NAME + recurrenceID.toString();
            scheduler.deleteJob(new JobKey(jobName, JOB_GROUP));
        } catch (SchedulerException e) {
            logger.info("Yedirgan recurrenceID: " + recurrenceID);
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem getJob(Integer jobType) {
        return getJob(jobType, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem getJob(Integer jobType, boolean defaultTimeSheetReminder) {
        EdsUser user = recurrenceManager.getUser();
        EdsRecurrence recurrence;
        if (defaultTimeSheetReminder) {
            recurrence = recurrenceManager.getRecurrenceJob(jobType, user.getCompany().getObjectID());
        } else {
            recurrence = recurrenceManager.getRecurrenceJob(jobType, user.getObjectID(), user.getCompany().getObjectID());
        }
        if (recurrence == null) {
            recurrence = new EdsRecurrence();
        }

        RecurrenceJobItem result = recurrence.createRecurrenceItem(jobType);

        if (recurrence.getCompanyID() != null) {
            List<EdsOverdueInvoiceReminderSettings> settingses = overdueInvoiceReminderSettingsManager.getReminderSettingsByRecurrenceId(recurrence.getCompanyID(), recurrence.getObjectID());
            if (settingses != null && settingses.size() > 0) {
                ArrayList<SelectItem> selectedRoles = new ArrayList<>();
                for (EdsOverdueInvoiceReminderSettings edsSettings : settingses) {
                    if (edsSettings != null && edsSettings.getRole() != null) {
                        SelectItem roleItem = new SelectItem();
                        roleItem.setId(edsSettings.getRole().getObjectID());
                        roleItem.setName(edsSettings.getRole().getName());
                        selectedRoles.add(roleItem);
                    }
                }
                result.setSelectedRoles(selectedRoles);
            }
        }
        List<EdsRole> roles = roleManager.list();
        for (EdsRole role : roles) {
            result.getRoles().add(role.getAsSelectItem());
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem createRecurrenceItemByUser(Integer busObjectId, Integer jobType, EdsUser user) {
        if (jobType != null) {
            EdsRecurrence recurrence = recurrenceManager.getRecurrencesByUser(busObjectId, jobType, user);
            if (recurrence == null) {
                recurrence = new EdsRecurrence();
            }
            return recurrence.createRecurrenceItem(jobType);
        }
        return null;
    }

    @Transactional
    public void updateRecurrence(EdsRecurrence recurrence, boolean changed, boolean deleted) {
        if (recurrence != null && recurrence.getJob() != null) {
            Integer jobType = recurrence.getJob().getObjectID();
            if (jobType == MASS_MAILING_RECURRENCE) {
                recurrence.setChanged(changed);
                recurrence.setDeleted(deleted);
                recurrenceManager.update(recurrence);
            } else if (!HOST_MAILFORCETRACK.equals(SpringPropertiesUtil.getProperty("bg_hostName"))) {
                if (deleted && recurrence.getCompanyID() != null) {
                    if (recurrence.getJob().getObjectID() == RECURRING_EVENT) {
                        eventManager.removeRecurrenceFromEvent(recurrence.getObjectID(), recurrence.getCompanyID());
                    } else if (recurrence.getJob().getObjectID() == RECURRING_TASK) {
                        taskManager.removeRecurrenceFromTask(recurrence.getObjectID(), recurrence.getCompanyID());
                    }else if (recurrence.getJob().getObjectID() == RECURRING_COURSE_SCHEDULE) {
                        scheduledCourseManager.removeRecurrenceFromScheduleCourse(recurrence.getObjectID(), recurrence.getCompanyID());
                    }
                }
                recurrence.setChanged(changed);
                recurrence.setDeleted(deleted);
                recurrenceManager.update(recurrence);
            }
        }
    }

    @Transactional
    public void updateRecurrence(Integer recurrenceId, boolean changed, boolean deleted) {
        EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
        if (recurrence != null) {
            updateRecurrence(recurrence, changed, deleted);
        }
    }

    public void checkForUpdate() {
        List<EdsRecurrence> recList = recurrenceManager.getChangedRecurrences();
        Scheduler scheduler = recurringJobsManager.getScheduler();
        if (recList != null && recList.size() > 0) {
            boolean enabled = Boolean.valueOf(SpringPropertiesUtil.getProperty("bg_recurringJobsManager_enabled"));
            for (EdsRecurrence recurrence : recList) {
                try {
                    removeTriggerFromScheduler(scheduler, recurrence.getObjectID());
                    if (recurrence.isDeleted()) {
                        if (recurrence.getJob().getObjectID() == RECURRING_EVENT) {
                            eventManager.removeRecurrenceFromEvent(recurrence.getObjectID(), recurrence.getCompanyID());
                        } else if (recurrence.getJob().getObjectID() == RECURRING_TASK) {
                            taskManager.removeRecurrenceFromTask(recurrence.getObjectID(), recurrence.getCompanyID());
                        }
                        recurrenceManager.delete(recurrence);
                        logger.info("Deleted recurrence: ID: " + recurrence.getObjectID() + "; Type: " + recurrence.getJob().getName() + "; CompanyID: " + recurrence.getCompanyID());
                    } else {
                        if (enabled) {
                            createRecurrence(recurrence, scheduler);
                        }
                    }
                } catch (Exception e) {
                    logger.info("Yedirgan recurrenceID: " + recurrence.getObjectID() + "; Type: " + recurrence.getJob().getJobName() + "; CompanyID: " + recurrence.getCompanyID());
                    logger.info("Unable to load recurrence. Please check all parameters!");
                    e.printStackTrace();
                }
            }
        }
    }

    private void createRecurrence(EdsRecurrence recurrence, Scheduler scheduler) {
        boolean enabled = Boolean.valueOf(SpringPropertiesUtil.getProperty("bg_recurringJobsManager_enabled"));
        JobDetail jobDetail = null;
        String jobTypeString = "";
        String cronExpression = recurrenceManager.getCronExpression(recurrence);
        Integer companyID = recurrence.getCompanyID();
        Integer userId = recurrence.getUserID();
        Integer jobType = recurrence.getJob() != null ? recurrence.getJob().getObjectID() : null;
        if (jobType != null && companyID != null) {
            switch (jobType) {
                case MASS_MAILING_RECURRENCE -> {
                    jobDetail = JobBuilder.newJob(MassMaillerSendMessage.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Mass Mailing";
                }

//                case OVERDUE_INVOICE_REMINDER: {
//                    jobDetail = JobBuilder.newJob(OverdueInvoiceRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
//                    jobTypeString = "Overdue Invoice Reminder";
//                    break;
//                }
                case TIMESHEET_REMINDER -> {
                    jobDetail = JobBuilder.newJob(TimesheetRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Timesheet Reminder";
                }
                case CALENDAR_EVENT_REMINDER -> {
                    jobDetail = JobBuilder.newJob(CalendarEventRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Calendar Event Reminder";
                }
                case RECURRING_INVOICE_REMINDER -> {
                    jobDetail = JobBuilder.newJob(RecurringSaleInvoiceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Recurring Invoice Reminder";
                }
                case CONTRACT_OVERDUE_REMINDER -> {
                    jobDetail = JobBuilder.newJob(ContractOverdueReminderRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Contract Overdue Reminder";
                }
                case RECURRING_BILL_REMINDER -> {
                    jobDetail = JobBuilder.newJob(RecurringBillJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Recurring Bill Reminder";
                }
                case RECURRING_MANUAL_JOURNAL_REMINDER -> {
                    jobDetail = JobBuilder.newJob(RecurringManualJournalJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Recurring Bill Reminder";
                }
                case TASK_OVERDUE_REMINDER -> {
                    jobDetail = JobBuilder.newJob(TaskOverdueReminderRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Task Overdue Reminder";
                }
                case PROJECT_OVERDUE_REMINDER -> {
                    jobDetail = JobBuilder.newJob(ProjectOverdueReminderRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Project Overdue Reminder";
                }
                case WORKSTREAM_OVERDUE_REMINDER -> {
                    jobTypeString = "Workstream Overdue Reminder";
                }
                case SYNCHRONIZE_GOOGLE_CONTACT -> {
                    jobDetail = JobBuilder.newJob(SynchronizeWithGoogleContactsRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Sync with google contact";
                }
                case SYNCHRONIZE_GOOGLE_CALENDAR -> {
                    jobDetail = JobBuilder.newJob(SynchronizeWithGoogleCalendarRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Sync with google calendar";
                }
                case SYNCHRONIZE_OFFICE_CALENDAR -> {
                    jobDetail = JobBuilder.newJob(SynchronizeWithOfficeCalendarRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Sync with office365 calendar";
                }
                case RECURRING_REPORT -> {
                    jobDetail = JobBuilder.newJob(RecurringReportJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Recurring Report";
                }
                case DAILY_HR_REMINDERP_ROCEDURE_JOB -> {
                    jobDetail = JobBuilder.newJob(DailyHrReminderProcedureJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Daily Hr Reminder Procedure Job";
                }
                case RECURRING_PROJECT_NUMBER -> {
                    jobDetail = JobBuilder.newJob(RecuringRestartProjectNumberJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Recurring Project Number";
                }
                case RECURRING_LEAVE_REQUEST_NUMBER -> {
                    jobDetail = JobBuilder.newJob(RecurringRestartLeaveRequestNumberJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Recurring Leave Request Number";
                }
                case EMPLOYEE_VISA_EXPIRATION_REMINDER -> {
                    jobDetail = JobBuilder.newJob(EmployeeVisaExpirationDateRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Employee Visa Expiry Date Reminder";
                }
                case WORKFLOW_RECURRENCE -> {
                    jobDetail = JobBuilder.newJob(WorkflowManagementRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Workflow Management Recurrence";
                }
                case RECURRING_WORKFLOW -> {
                    jobDetail = JobBuilder.newJob(RecurringWorkflowManagementJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "Recurring Workflow Management Job";
                }
                case DAILY_FILL_TIMESHEET_FROM_RES_UTIL -> {
                    jobDetail = JobBuilder.newJob(DailyAutoFillTimesheetRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "DailyAutoFillTimesheetRecurrenceJob Job";
                }
                case SYNCHRONIZE_MAGENTO_CATALOG -> {
                    jobDetail = JobBuilder.newJob(SynchronizeMagentoCatalogRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "SynchronizeMagentoCatalogRecurrenceJob Job";
                }
                case RECURRING_DEFERRED_TRANSACTIONS -> {
                    jobDetail = JobBuilder.newJob(DeferredTransactionsRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                    jobTypeString = "DeferredTransactionsRecurrenceJob Job";
                }
            }
        }

        if (jobDetail != null) {
            String id = recurrence.getObjectID().toString();
            Date recEndDate = recurrenceManager.getTriggerEndDate(recurrence);
            if (recurrence.getStartDate() != null && recEndDate != null && recEndDate.before(recurrence.getStartDate())) {
                recurrence.setEndDate(recurrence.getStartDate());
            }
            AbstractTrigger cronTrigger;
            Integer interval = recurrence.getInterval() != null && recurrence.getInterval() > 0 ? recurrence.getInterval() : Integer.valueOf(1);
            DateBuilder.IntervalUnit intervalUnit = null;
            if (recurrence.getType() == RECURRENCE_TYPE_DAILY
                    || recurrence.getType() == RECURRENCE_TYPE_EVEN_DAILY
                    || recurrence.getType() == RECURRENCE_TYPE_ODD_DAILY) {
                intervalUnit = DateBuilder.IntervalUnit.DAY;
            } else if (recurrence.getType() == RECURRENCE_TYPE_MONTHLY) {
                intervalUnit = DateBuilder.IntervalUnit.MONTH;
            } else if (recurrence.getType() == RECURRENCE_TYPE_MINUTELY) {
                intervalUnit = DateBuilder.IntervalUnit.MINUTE;
            } else if (recurrence.getType() == RECURRENCE_TYPE_HOURLY) {
                intervalUnit = DateBuilder.IntervalUnit.HOUR;
            }
            if ((recurrence.getType() == RECURRENCE_TYPE_DAILY && recurrence.getDailyPatternOptions() != null &&
                    recurrence.getDailyPatternOptions() == DAILY_PATTERN_OPTION_INTERVAL ||
                    recurrence.getType() == RECURRENCE_TYPE_MONTHLY ||
                    recurrence.getType() == RECURRENCE_TYPE_MINUTELY ||
                    recurrence.getType() == RECURRENCE_TYPE_HOURLY) ||
                    (recurrence.getType() == RECURRENCE_TYPE_EVEN_DAILY || recurrence.getType() == RECURRENCE_TYPE_ODD_DAILY)) {
                cronTrigger = new CalendarIntervalTriggerImpl(TRIGGER_NAME + id, TRIGGER_GROUP, recurrence.getStartDate(), recEndDate, intervalUnit, interval);
                cronTrigger.setJobGroup(JOB_GROUP);
                cronTrigger.setJobName(JOB_NAME + id);
            } else {
                cronTrigger = new CronTriggerImpl();
                cronTrigger.setJobName(JOB_NAME + id);
                cronTrigger.setJobGroup(JOB_GROUP);
                cronTrigger.setName(TRIGGER_NAME + id);
                cronTrigger.setGroup(TRIGGER_GROUP);
                cronTrigger.setStartTime(recurrence.getStartDate());
                cronTrigger.setEndTime(recEndDate);
                CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) cronTrigger;
                try {
                    cronTriggerImpl.setCronExpression(cronExpression);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            cronTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);//fire missed trigger (MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY doesn't work versions < quartz 2.0)
            Map data = new HashMap();
            data.put(BUS_OBJECT_ID, recurrence.getBusObjectId());
            data.put(BUS_OBJECT_PARAM, recurrence.getBusObjectParams() != null ? recurrence.getBusObjectParams() : "");
            data.put(SchedulerConstant.COMPANY_ID, companyID);
            data.put(SchedulerConstant.USER_ID, userId);
            if (jobType == OVERDUE_INVOICE_REMINDER) {
                data.put(TO_ME, recurrence.getToMe());
                data.put(TO_CLIENT, recurrence.getToClient());
            }
            data.put(REC_OBJECT_ID, recurrence.getObjectID());
            data.put(START_DATE, recurrence.getStartDate());
            data.put(END_TYPE, recurrence.getEndType());
            data.put(TYPE, recurrence.getType());
            data.put(JOB_TYPE_STRING, jobTypeString);
            data.put(CRON_EXPRESSION, cronExpression);
            data.put(TRIGGER_NAME, cronTrigger.getName());

            JobDataMap jobDataMap = new JobDataMap(data);
            cronTrigger.setJobDataMap(jobDataMap);
            if (cronTrigger != null) {
                Date date = cronTrigger.getFireTimeAfter(new Date());
                if (date != null) {
                    if (cronTrigger.getStartTime().before(new Date())) {
                        cronTrigger.setStartTime(date);
                    }
                    try {

                        if (enabled) {
                            recurrence.setChanged(false);
                            scheduler.scheduleJob(jobDetail, cronTrigger);
                            logger.info("Successfully loaded Recurrence id: " + id + "; " + jobTypeString + "; CompanyID: " + companyID + "; Sched date: " + format.format(date) + "; CronExpression: " + cronExpression);
                        } else {
                            recurrence.setChanged(true);
                        }
                        recurrence.setDeleted(false);
                        recurrenceManager.update(recurrence);
                    } catch (SchedulerException e) {
                        logger.info("Unable to schedule trigger with key: " + cronTrigger.getKey() + "; Type: " + jobTypeString);
                        e.printStackTrace();
                    }
                } else {
                    if (enabled) {
                        removeTriggerFromScheduler(scheduler, recurrence.getObjectID());
                        logger.info("No fire time after current date for recurrenceId: " + id + "; " + jobTypeString + "; CompanyID: " + companyID + "; StartDate: " + format.format(recurrence.getStartDate()) + "; CronExpression: " + cronExpression);
                        updateRecurrence(recurrence, false, false);
                    } else {
                        recurrence.setChanged(true);
                        recurrence.setDeleted(false);
                        recurrenceManager.update(recurrence);
                    }
                }
            }
        }
    }

    public Integer registerRecurrence(EdsServerHistory serverHistory, String jobDetailName, Date normalFireTime, Date lateFireTime, String jobType, String cronExpression, Integer recurrenceID, Integer companyID, boolean isFired) {
        EdsRecurrenceHistory recurrenceHistory = new EdsRecurrenceHistory();
        recurrenceHistory.setJobName(jobDetailName);
        recurrenceHistory.setNormalFireTime(normalFireTime);
        recurrenceHistory.setLateFireTime(lateFireTime);
        recurrenceHistory.setJobType(jobType);
        recurrenceHistory.setCronExpression(cronExpression);
        if (recurrenceID != null) {
            recurrenceHistory.setRecurrenceID(recurrenceID);
        }
        recurrenceHistory.setCompanyID(companyID);
        recurrenceHistory.setFired(isFired);
        recurrenceHistory.setServerHistory(serverHistory);
        return createLateRecurrence(recurrenceHistory);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem getRecurringInvoiceRecurrenceItem(EdsRecurringInvoice recurringInvoice) {
        EdsCompany company = recurringInvoice.getSender() != null ? recurringInvoice.getSender().getCompany() : recurrenceManager.getUser().getCompany();
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(RECURRING_INVOICE_REMINDER, recurringInvoice.getObjectID(), company.getObjectID());
        if (recurrence == null) {
            recurrence = new EdsRecurrence();
        }
        RecurrenceJobItem jobItem = recurrence.createRecurrenceItem(RECURRING_INVOICE_REMINDER);
        if (recurrence != null) {
            if (recurrence.getType() != null && RECURRENCE_TYPE_YEARLY == recurrence.getType() && recurrence.getMonthlyOrYearlyDay() != null && MONTHLY_OR_YEARLY_PATTERN_CUSTOM == recurrence.getMonthlyOrYearlyPatternOption()) {
                Date tempDate = (Date) recurrence.getStartDate().clone();
                tempDate.setDate(recurrence.getMonthlyOrYearlyDay());
                jobItem.setStartDate(tempDate);
            } else {
                jobItem.setStartDate(recurrence.getStartDate());
            }
        }
        return jobItem;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem getRecurringBillRecurrenceItem(EdsRecurringBill edsRecurringBill) {
        EdsCompany company = edsRecurringBill.getSender() != null ? edsRecurringBill.getSender().getCompany() : recurrenceManager.getUser().getCompany();
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(RECURRING_BILL_REMINDER, edsRecurringBill.getObjectID(), company.getObjectID());
        if (recurrence == null) {
            recurrence = new EdsRecurrence();
        }
        RecurrenceJobItem jobItem = recurrence.createRecurrenceItem(RECURRING_BILL_REMINDER);
        if (recurrence != null) {
            if (recurrence.getType() != null && RECURRENCE_TYPE_YEARLY == recurrence.getType() && recurrence.getMonthlyOrYearlyDay() != null && MONTHLY_OR_YEARLY_PATTERN_CUSTOM == recurrence.getMonthlyOrYearlyPatternOption()) {
                Date tempDate = (Date) recurrence.getStartDate().clone();
                tempDate.setDate(recurrence.getMonthlyOrYearlyDay());
                jobItem.setStartDate(tempDate);
            } else {
                jobItem.setStartDate(recurrence.getStartDate());
            }
        }
        return jobItem;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem getRecurringManualJournalRecurrenceItem(EdsManualJournal edsManualJournal) {
        EdsCompany company = edsManualJournal.getSender() != null ? edsManualJournal.getSender().getCompany() : recurrenceManager.getUser().getCompany();
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(RECURRING_MANUAL_JOURNAL_REMINDER, edsManualJournal.getObjectID(), company.getObjectID());
        if (recurrence == null) {
            recurrence = new EdsRecurrence();
        }
        RecurrenceJobItem jobItem = recurrence.createRecurrenceItem(RECURRING_MANUAL_JOURNAL_REMINDER);
        if (recurrence != null) {
            if (recurrence.getType() != null && RECURRENCE_TYPE_YEARLY == recurrence.getType() && recurrence.getMonthlyOrYearlyDay() != null && MONTHLY_OR_YEARLY_PATTERN_CUSTOM == recurrence.getMonthlyOrYearlyPatternOption()) {
                Date tempDate = (Date) recurrence.getStartDate().clone();
                tempDate.setDate(recurrence.getMonthlyOrYearlyDay());
                jobItem.setStartDate(tempDate);
            } else {
                jobItem.setStartDate(recurrence.getStartDate());
            }
        }
        return jobItem;
    }

    public void deleteInvoiceRecurrenceIfExists(Integer invoiceID) {
        EdsCompany company = recurrenceManager.getUser().getCompany();
        EdsRecurrence recurrenceForDelete = recurrenceManager.getRecurrenceJob(RECURRING_INVOICE_REMINDER, invoiceID, company.getObjectID());
        if (recurrenceForDelete != null) {
            recurrenceManager.delete(recurrenceForDelete);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Map<Integer, Date> getNextFireTimesAsMap(Integer[] busObjectIDArray, Integer jobType) {
        HashMap<Integer, Date> nextDatesMap = new HashMap<>();
        EdsCompany company = recurrenceManager.getUser().getCompany();
        for (Integer aBusObjectIDArray : busObjectIDArray) {
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(jobType, aBusObjectIDArray, company.getObjectID());
            if (recurrence != null) {
                List<Date> dates = getRecurringDates(recurrence);
                if (dates != null && !dates.isEmpty()) {
                    for (Date date : dates) {
                        if (date.after(new Date())) {
                            nextDatesMap.put(aBusObjectIDArray, date);
                            break;
                        }
                    }
                }
            }
        }
        return nextDatesMap;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getRecurrenceTemplateString(Integer busObjectId, Integer jobType) {
        EdsUser user = recurrenceManager.getUser();
        EdsCompany company = user.getCompany();
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(jobType, busObjectId, company.getObjectID());
        StringBuilder repeats = new StringBuilder();
        if (recurrence != null) {
            if (RECURRENCE_TYPE_DAILY == recurrence.getType()) {
                repeats.append("Every " + (recurrence.getDailyPatternOptions() != null && DAILY_PATTERN_OPTION_INTERVAL == recurrence.getDailyPatternOptions() ?
                        (recurrence.getInterval() == 1 ? " day" : recurrence.getInterval() + " days") : "weekday"));
            } else if (RECURRENCE_TYPE_EVEN_DAILY == recurrence.getType()) {
                repeats.append("Every even days");
            } else if (RECURRENCE_TYPE_ODD_DAILY == recurrence.getType()) {
                repeats.append("Every odd days");
            } else if (RECURRENCE_TYPE_WEEKLY == recurrence.getType()) {
                repeats.append("Every " + (recurrence.getInterval() == 1 ? " week " : recurrence.getInterval() + " weeks "));
                List<String> days = new LinkedList<>();
                if (recurrence.isSunday()) {
                    days.add("Sunday");
                }
                if (recurrence.isMonday()) {
                    days.add("Monday");
                }
                if (recurrence.isTuesday()) {
                    days.add("Tuesday");
                }
                if (recurrence.isWednesday()) {
                    days.add("Wednesday");
                }
                if (recurrence.isThursday()) {
                    days.add("Thusrday");
                }
                if (recurrence.isFriday()) {
                    days.add("Friday");
                }
                if (recurrence.isSaturday()) {
                    days.add("Saturday");
                }

                if (days.size() > 0) {
                    repeats.append(" on ");
                    int i = 0;
                    for (String d : days) {
                        repeats.append((i != 0 ? "," : "") + d);
                        i++;
                    }
                }
            } else if (RECURRENCE_TYPE_MONTHLY == recurrence.getType()) {
                if (MONTHLY_OR_YEARLY_PATTERN_CUSTOM == recurrence.getMonthlyOrYearlyPatternOption()) {
                    repeats.append("Day " + recurrence.getMonthlyOrYearlyDay() + " of every " +
                            (recurrence.getInterval() == 1 ? "Month" : recurrence.getInterval() + " Months"));
                } else if (MONTHLY_OR_YEARLY_PATTERN_SIMPLE == recurrence.getMonthlyOrYearlyPatternOption()) {
                    repeats.append("The ");
                    switch (recurrence.getCustomPatternDay()) {
                        case 1 -> repeats.append("first ");
                        case 2 -> repeats.append("second ");
                        case 3 -> repeats.append("third ");
                        case 4 -> repeats.append("fourth ");
                        case 5 -> repeats.append("last ");
                    }
                    switch (recurrence.getMonthlyOrYearlyDay()) {
                        case 1 -> repeats.append("Sunday");
                        case 2 -> repeats.append("Monday");
                        case 3 -> repeats.append("Tuesday");
                        case 4 -> repeats.append("Wednesday");
                        case 5 -> repeats.append("Thursday");
                        case 6 -> repeats.append("Friday");
                        case 7 -> repeats.append("Saturday");
                    }
                    repeats.append(" of every " + (recurrence.getInterval() == 1 ? " Month" : recurrence.getInterval() + " Months"));
                }
            } else if (RECURRENCE_TYPE_YEARLY == recurrence.getType()) {
                if (MONTHLY_OR_YEARLY_PATTERN_CUSTOM == recurrence.getMonthlyOrYearlyPatternOption()) {
                    repeats.append("Every " + (recurrence.getInterval() > 1 ? recurrence.getInterval() + " years " : ""));
                    switch (recurrence.getYearlyMonth()) {
                        case 1 -> repeats.append("January");
                        case 2 -> repeats.append("February");
                        case 3 -> repeats.append("March");
                        case 4 -> repeats.append("April");
                        case 5 -> repeats.append("May");
                        case 6 -> repeats.append("June");
                        case 7 -> repeats.append("July");
                        case 8 -> repeats.append("August");
                        case 9 -> repeats.append("September");
                        case 10 -> repeats.append("October");
                        case 11 -> repeats.append("November");
                        case 12 -> repeats.append("December");
                    }
                    Date tempDate = (Date) recurrence.getStartDate().clone();
                    if (recurrence.getMonthlyOrYearlyDay() != null) {
                        tempDate.setDate(recurrence.getMonthlyOrYearlyDay());
                        tempDate.setMinutes(tempDate.getMinutes() + (user.getUserTimezone().getRawOffset() / 60000));
                    }
                    repeats.append(" " + tempDate.getDate());
                } else if (MONTHLY_OR_YEARLY_PATTERN_SIMPLE == recurrence.getMonthlyOrYearlyPatternOption()) {
                    repeats.append("The ");
                    switch (recurrence.getCustomPatternDay()) {
                        case 1 -> repeats.append("first ");
                        case 2 -> repeats.append("second ");
                        case 3 -> repeats.append("third ");
                        case 4 -> repeats.append("fourth ");
                        case 5 -> repeats.append("last ");
                    }
                    switch (recurrence.getMonthlyOrYearlyDay()) {
                        case 1 -> repeats.append("Sunday");
                        case 2 -> repeats.append("Monday");
                        case 3 -> repeats.append("Tuesday");
                        case 4 -> repeats.append("Wednesday");
                        case 5 -> repeats.append("Thursday");
                        case 6 -> repeats.append("Friday");
                        case 7 -> repeats.append("Saturday");
                    }
                    repeats.append(" of ");
                    switch (recurrence.getYearlyMonth()) {
                        case 1 -> repeats.append("January");
                        case 2 -> repeats.append("February");
                        case 3 -> repeats.append("March");
                        case 4 -> repeats.append("April");
                        case 5 -> repeats.append("May");
                        case 6 -> repeats.append("June");
                        case 7 -> repeats.append("July");
                        case 8 -> repeats.append("August");
                        case 9 -> repeats.append("September");
                        case 10 -> repeats.append("October");
                        case 11 -> repeats.append("November");
                        case 12 -> repeats.append("December");
                    }
                }
            }
        }

        return repeats.toString();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsRecurrence getRecurrence(Integer objectId) {
        if (objectId != null) {
            return recurrenceManager.get(objectId);
        }
        return null;
    }

    @Override
    public EdsRecurrence getRecurrenceByJobId(Integer busObjectID, Integer jobType) {
        EdsCompany company = recurrenceManager.getUser().getCompany();
        return recurrenceManager.getRecurrenceJob(jobType, busObjectID, company.getObjectID());
    }

    @Override
    public RecurrenceJobItem createRecurrenceItemByRule(Integer recurrenceId, int jobType) {
        EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
        if (recurrence == null) {
            recurrence = new EdsRecurrence();
        }
        return recurrence.createRecurrenceItem(jobType);
    }

    @Override
    public ArrayList<String> deleteTelegramRecurrenceRule(Integer id) {
        EdsTelegramReportingScheduleRule rule = telegramReportingRecurrenceManager.get(id);
        Integer recurrenceId = rule.getRecurrenceId();
        boolean isActive = rule.isActive();
        if (isActive) {
            EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
            recurrenceManager.delete(recurrence);
        } else {
            EdsTemporaryRecurrence recurrence = temporaryRecurrenceManager.get(recurrenceId);
            temporaryRecurrenceManager.delete(recurrence);
        }
        telegramReportingRecurrenceManager.delete(rule);
        return telegramReportingRecurrenceManager.getAllRuleNames(rule.getEdsReport().getObjectID());
    }

    @Override
    public void deleteRecurrences(Integer busObjectID, Integer jobType) {
        EdsCompany company = recurrenceManager.getUser().getCompany();
        List<EdsRecurrence> recurrences = recurrenceManager.getRecurrenceJobList(jobType, busObjectID, company.getObjectID());
        if (recurrences != null && !recurrences.isEmpty()) {
            for (EdsRecurrence recurrence : recurrences) {
                removeTriggerFromScheduler(recurrence.getObjectID());
                updateRecurrence(recurrence, true, true);
            }
        } else {
            logger.info("NO_RECURRENCE_SETTINGS_FOR_GIVEN_JOB_AND_ID");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AbstractTrigger getRecurrenceCronTrigger(EdsRecurrence recurrence) {
        String cronExpression = recurrenceManager.getCronExpression(recurrence);
        AbstractTrigger trigger = null;
        Calendar startCalendar = new GregorianCalendar();
        if (recurrence.getStartDate() != null) {
            startCalendar.setTime(recurrence.getStartDate());
        } else {
            startCalendar.setTime(new Date());
        }
        Calendar endCalendar = new GregorianCalendar();
        if (recurrence.getEndDate() != null) {
            endCalendar.setTime(recurrence.getEndDate());
        } else {
            endCalendar.setTime(startCalendar.getTime());
            endCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR) + 2);
        }
        if ((recurrence.getType() == RECURRENCE_TYPE_DAILY && recurrence.getDailyPatternOptions() != null && recurrence.getDailyPatternOptions() == DAILY_PATTERN_OPTION_INTERVAL ||
                recurrence.getType() == RECURRENCE_TYPE_MONTHLY || recurrence.getType() == RECURRENCE_TYPE_YEARLY) ||
                (recurrence.getType() == RECURRENCE_TYPE_EVEN_DAILY || recurrence.getType() == RECURRENCE_TYPE_ODD_DAILY)) {
            DateBuilder.IntervalUnit intervalUnit = null;
            if (recurrence.getType() == RECURRENCE_TYPE_DAILY) {
                intervalUnit = DateBuilder.IntervalUnit.DAY;
            } else if (recurrence.getType() == RECURRENCE_TYPE_EVEN_DAILY || recurrence.getType() == RECURRENCE_TYPE_ODD_DAILY) {
                intervalUnit = DateBuilder.IntervalUnit.DAY;
            } else if (recurrence.getType() == RECURRENCE_TYPE_MONTHLY) {
                intervalUnit = DateBuilder.IntervalUnit.MONTH;
            } else if (recurrence.getType() == RECURRENCE_TYPE_YEARLY) {
                intervalUnit = DateBuilder.IntervalUnit.YEAR;
            } else if (recurrence.getType() == RECURRENCE_TYPE_HOURLY) {
                intervalUnit = DateBuilder.IntervalUnit.HOUR;
            }

            trigger = new CalendarIntervalTriggerImpl();
            trigger.setName("testTrigger");
            trigger.setGroup("testTriggerGroup");
            trigger.setMisfireInstruction(CalendarIntervalTriggerImpl.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
            CalendarIntervalTriggerImpl calendarIntervalTrigger = (CalendarIntervalTriggerImpl) trigger;
            calendarIntervalTrigger.setRepeatInterval(recurrence.getInterval() != null ? recurrence.getInterval() : 1);
            calendarIntervalTrigger.setRepeatIntervalUnit(intervalUnit);
        } else {
            try {
                trigger = new CronTriggerImpl();
                trigger.setName("testTriggerName");
                trigger.setGroup("testTriggerGroup");
                trigger.setMisfireInstruction(CronTriggerImpl.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
                CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) trigger;
                cronTriggerImpl.setCronExpression(cronExpression);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        trigger.setStartTime(startCalendar.getTime());
        if (startCalendar.getTime().getTime() > endCalendar.getTime().getTime()) {
            trigger.setEndTime(startCalendar.getTime());
        } else {
            trigger.setEndTime(endCalendar.getTime());
        }
        return trigger;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Date> getRecurringDates(EdsRecurrence recurrence) {
        Integer occurence = recurrence.getOccurrence();
        Date fromDate = recurrence.getStartDate();
        Date toDate = recurrence.getEndDate();
        if (toDate == null) {
            toDate = recurrenceManager.getTriggerEndDate(recurrence);
        }

        TimeZone tz = TimeZone.getTimeZone(recurrence.getUserTimeZone() != null ? recurrence.getUserTimeZone() : "GMT");
        DateNonConvertable dn = new DateNonConvertable(fromDate);
        dn.setInTimeZoneOffSetMs((-1) * tz.getOffset(fromDate.getTime()));
        dn.setOutTimeZoneOffSetMs(0);

        DateNonConvertable dn2 = new DateNonConvertable(toDate);
        dn2.setInTimeZoneOffSetMs((-1) * tz.getOffset(toDate.getTime()));
        dn2.setOutTimeZoneOffSetMs(0);

        EdsRecurrence tempRecurrence = recurrence.cloneShallow();
        tempRecurrence.setStartDate(dn.getNonConvertedDate());
        tempRecurrence.setEndDate(dn2.getNonConvertedDate());
        AbstractTrigger trigger = getRecurrenceCronTrigger(tempRecurrence);

        if (trigger instanceof CalendarIntervalTriggerImpl) {
            trigger.setStartTime(fromDate);
            trigger.setEndTime(toDate);
            if (occurence != null) {
                return TriggerUtils.computeFireTimes(trigger, null, occurence);
            } else {
                return TriggerUtils.computeFireTimesBetween(trigger, null, fromDate, toDate);
            }
        } else if (trigger instanceof CronTriggerImpl) {
            ((CronTriggerImpl) trigger).setTimeZone(tz);
            trigger.setStartTime(fromDate);
            if (occurence != null) {
                return TriggerUtils.computeFireTimes(trigger, null, occurence);
            } else {
                return TriggerUtils.computeFireTimesBetween(trigger, null, fromDate, dn2.getNonConvertedDate());
            }
        }
        return null;
    }

    @Override
    public void deleteRecurrence(Integer busObjectID, Integer jobType) {
        EdsCompany company = recurrenceManager.getUser().getCompany();
        EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(jobType, busObjectID, company.getObjectID());
        if (recurrence != null) {
            removeTriggerFromScheduler(recurrence.getObjectID());
            updateRecurrence(recurrence, true, true);
        } else {
            logger.info("NO_RECURRENCE_SETTINGS_FOR_GIVEN_JOB_AND_ID");
        }
    }

    public void createRecurrenceLog() {
        EdsServerHistory lastServerHistory = serverHistoryManager.getLastServerHistory();
        if (lastServerHistory != null) {
            lastServerHistory.setDownTimeTo(new Date());
            serverHistoryManager.update(lastServerHistory);
        }

        EdsServerHistory newServerHistory = new EdsServerHistory();
        newServerHistory.setDownTimeFrom(new Date());
        newServerHistory.setCatchUp(false);
        serverHistoryManager.create(newServerHistory);
    }

    public void updateRecurrenceLog() {

    }

    public Integer createLateRecurrence(EdsRecurrenceHistory recurrenceHistory) {
        if (recurrenceHistory != null) {
            recurrenceHistoryManager.create(recurrenceHistory);
            return recurrenceHistory.getObjectID();
        }
        return null;
    }

    public void updateLateRecurrence(String jobName, Date fireTime) {
        EdsRecurrenceHistory recurrenceHistory = recurrenceHistoryManager.getRecurrenceHistory(jobName, fireTime);
        if (recurrenceHistory != null) {
            Long recurrenceCount = recurrenceHistoryManager.getLateRecurrencesInThisSeries(recurrenceHistory.getServerHistory());
            if (recurrenceCount != null && recurrenceCount == 0) {
                EdsServerHistory serverHistory = recurrenceHistory.getServerHistory();
                serverHistory.setCatchUp(true);
                serverHistoryManager.update(serverHistory);
            }
        }
    }

    public void fireLateRecurrences() {
        List<EdsRecurrenceHistory> recurrenceHistories = recurrenceHistoryManager.getLateRecurrences();
        if (recurrenceHistories != null) {
            for (EdsRecurrenceHistory recurrenceHistory : recurrenceHistories) {
                if (recurrenceHistory.getJobName() != null && recurrenceHistory.getLateFireTime() != null) {
                    updateLateRecurrence(recurrenceHistory.getJobName(), recurrenceHistory.getLateFireTime());
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsServerHistory getLastServerHistory() {
        return serverHistoryManager.getLastServerHistory();
    }

    public void updateRecurrenceHistory(Integer recurrenceHistoryID) {
        EdsRecurrenceHistory recurrenceHistory = recurrenceHistoryManager.get(recurrenceHistoryID);
        if (recurrenceHistory != null) {
            recurrenceHistory.setFired(true);
            recurrenceHistoryManager.update(recurrenceHistory);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<RecurrenceLogItem> getRecurrenceJobItems() {
        Set<TriggerKey> triggersInGroup = null;
        ArrayList<RecurrenceLogItem> result = new ArrayList<>();
        Scheduler scheduler = recurringJobsManager.getScheduler();

        try {
            triggersInGroup = scheduler.getTriggerKeys(GroupMatcher.groupContains(TRIGGER_GROUP));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        if (triggersInGroup == null) {
            return result;
        }
        for (TriggerKey triggerKey : triggersInGroup) {
            Trigger trigger;
            RecurrenceLogItem item = new RecurrenceLogItem();
            try {
                trigger = scheduler.getTrigger(triggerKey);
            } catch (SchedulerException e) {
                logger.info("Unable to get trigger from scheduler with name: " + triggerKey.toString());
                e.printStackTrace();
                continue;
            }
            JobDataMap map = trigger.getJobDataMap();
            item.setObjectID(map.getInt(BUS_OBJECT_ID));
            item.setJobType(map.getString(JOB_TYPE_STRING));
            item.setCronExpression(map.getString(CRON_EXPRESSION));
            if (map.getString(LATE_FIRE_TIME) != null && !"".equals(map.getString(LATE_FIRE_TIME))) {
                try {
                    Date lateFireTime = new Date(map.getString(LATE_FIRE_TIME));
                    item.setLateFireTime(lateFireTime);
                } catch (Exception e) {
                    logger.info("Unable create Date with parameter: " + map.getString(LATE_FIRE_TIME));
                    e.printStackTrace();
                }
            }
            item.setNormalFireTime(trigger.getNextFireTime());
            item.setFired(false);
            item.setRecurrenceID(map.getInt(REC_OBJECT_ID));
            item.setCompanyID(map.getInt(SchedulerConstant.COMPANY_ID));
            result.add(item);
        }

        return result;
    }

    public EdsRecurrenceHistory getRecurrenceHistory(Integer recurrenceID, String date) {
        return recurrenceHistoryManager.getRecurrenceHistory(recurrenceID, date);
    }

    @Override
    public void nativelyRemoveRecurrence(Integer recurrenceID) {
        EdsRecurrence recurrence = recurrenceManager.get(recurrenceID);
        if (recurrence.getJob().getObjectID() == RECURRING_EVENT) {
            eventManager.removeRecurrenceFromEvent(recurrence.getObjectID(), recurrence.getCompanyID());
        } else if (recurrence.getJob().getObjectID() == RECURRING_TASK) {
            taskManager.removeRecurrenceFromTask(recurrence.getObjectID(), recurrence.getCompanyID());
        }
        recurrenceManager.nativelyRemoveRecurrence(recurrenceID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem getJob() {
        EdsUser user = recurrenceManager.getUser();
        Integer jobType = SYNCHRONIZE_GOOGLE_CALENDAR;
        EdsRecurrence recurrence;
        recurrence = recurrenceManager.getRecurrenceJob(jobType, user.getObjectID(), user.getCompany().getObjectID());
        if (recurrence == null) {
            jobType = SYNCHRONIZE_OFFICE_CALENDAR;
            recurrence = recurrenceManager.getRecurrenceJob(jobType, user.getObjectID(), user.getCompany().getObjectID());
            if (recurrence == null) {
                recurrence = new EdsRecurrence();
            }
        }
        return recurrence.createRecurrenceItem(jobType);
    }
}
