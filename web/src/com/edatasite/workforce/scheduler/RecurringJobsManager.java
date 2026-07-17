package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsRecurrenceHistory;
import com.edatasite.workforce.core.domain.EdsServerHistory;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.mail.MassMaillerSendMessage;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.triggers.AbstractTrigger;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Mar 4, 2010
 * Time: 2:12:35 AM
 * To change this template use File | Settings | File Templates.
 */

public class RecurringJobsManager implements SchedulerConstant, Constants {
    private static final Logger log = LoggerFactory.getLogger(RecurringJobsManager.class);
    private StdScheduler scheduler;
    //    public static String hostName;
    private final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    private RecurrenceService recurrenceService;
    private RecurrenceManager recurrenceManager;
    //    private Boolean enabled = false;
    private Boolean initEnabled = false;
//    public static String databaseType;

    // recurrenceService ni RecurringJobsListener class dan set qilinadi.

    public void setRecurrenceService(RecurrenceService recurrenceService) {
        this.recurrenceService = recurrenceService;
    }

    public void setRecurrenceManager(RecurrenceManager recurrenceManager) {
        this.recurrenceManager = recurrenceManager;
    }

    public void setScheduler(StdScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public StdScheduler getScheduler() {
        return scheduler;
    }

//    public Boolean getEnabled() {
//        return enabled;
//    }

    public Boolean getInitEnabled() {
        return initEnabled;
    }

    public void setInitEnabled(Boolean initEnabled) {
        this.initEnabled = initEnabled;
    }

//    public void setEnabled(Boolean enabled) {
//        this.enabled = enabled;
//    }

//    public void setDatabaseType(String databaseType) {
//        this.databaseType = databaseType;
//    }

//    public void setHostName(String hostName) {
//        this.hostName = hostName;
//    }

//    public String getHostName() {
//        return hostName;
//    }

    public void initManager() {
        String hostName = SpringPropertiesUtil.getProperty("bg_hostName");
        boolean enabled = Boolean.valueOf(SpringPropertiesUtil.getProperty("bg_recurringJobsManager_enabled"));
        String databaseType = SpringPropertiesUtil.getProperty("bg_databaseType");
        if (enabled) {
            ServerSecurityContext.getInstance().setDatabase(databaseType);
            try {
                List<EdsRecurrence> recurrenceList = recurrenceManager.getRecurrences(HOST_MAILFORCETRACK.equals(hostName));
//                EdsServerHistory lastServerHistory = recurrenceService.getLastServerHistory();
                Map data = new HashMap();

                if (!scheduler.isStarted()) {
                    scheduler.start();
                    log.info("Scheduler started");
                }
                log.info("RecurringJobsManager.initManager() : EdsContextParams.isMailForceTrack() = " + HOST_MAILFORCETRACK.equals(hostName));
                for (EdsRecurrence recurrence : recurrenceList) {
                    try {
                        if (IN_PROGRESS.equals(recurrence.getStatus())) {
                            recurrenceService.setRecurrenceStatus(recurrence.getObjectID(), null);
                        }
                        Integer companyID = recurrence.getCompanyID();
                        Integer userID = recurrence.getUserID();
                        Integer jobType = recurrence.getJob().getObjectID();
                        JobDetail jobDetail = null;
                        String jobTypeString = "";
                        String id = recurrence.getObjectID().toString();
                        String cronExpression = recurrenceManager.getCronExpression(recurrence);
                        /*if ((HOST_MAILFORCETRACK.equals(hostName) || HOST_AWS.equals(hostName) || HOST_LOCAL.equals(hostName)) && jobType == MASS_MAILING_RECURRENCE) {
                            jobDetail = new JobDetail(JOB_NAME + id, JOB_GROUP, MassMaillerSendMessage.class);
                        } else */
                        if (jobType != null && companyID != null /*&& !HOST_MAILFORCETRACK.equals(hostName)*/) {
                            switch (jobType) {
                                case MASS_MAILING_RECURRENCE -> {
//                                    jobDetail = JobBuilder.newJob(JOB_NAME + id, JOB_GROUP, MassMaillerSendMessage.class);
                                    jobDetail = JobBuilder.newJob(MassMaillerSendMessage.class).withIdentity(JOB_NAME + id, JOB_GROUP).build();
                                }

//                                case OVERDUE_INVOICE_REMINDER: {
////                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, OverdueInvoiceRecurrenceJob.class);
//                                    jobDetail = JobBuilder.newJob(OverdueInvoiceRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
//                                    jobTypeString = "Overdue Invoice Reminder";
//                                    break;
//                                }
                                case TIMESHEET_REMINDER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, TimesheetRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(TimesheetRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Timesheet Reminder";
                                }
                                case CALENDAR_EVENT_REMINDER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, CalendarEventRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(CalendarEventRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Calendar Event Reminder";
                                }
                                case RECURRING_INVOICE_REMINDER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, RecurringSaleInvoiceJob.class);
                                    jobDetail = JobBuilder.newJob(RecurringSaleInvoiceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Recurring Invoice Reminder";
                                }
                                case RECURRING_BILL_REMINDER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, RecurringBillJob.class);
                                    jobDetail = JobBuilder.newJob(RecurringBillJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Recurring Bill Reminder";
                                }
                                case RECURRING_MANUAL_JOURNAL_REMINDER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, RecurringManualJournalJob.class);
                                    jobDetail = JobBuilder.newJob(RecurringManualJournalJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Recurring Manual Journal Reminder";
                                }
                                case TASK_OVERDUE_REMINDER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, TaskOverdueReminderRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(TaskOverdueReminderRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Task Overdue Reminder";
                                }
                                case PROJECT_OVERDUE_REMINDER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, ProjectOverdueReminderRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(ProjectOverdueReminderRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Project Overdue Reminder";
                                }
                                case CONTRACT_OVERDUE_REMINDER -> {
                                    jobDetail = JobBuilder.newJob(ContractOverdueReminderRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Contract Overdue Reminder";
                                }
                                case WORKSTREAM_OVERDUE_REMINDER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, WorkstreamOverdueReminderRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(WorkstreamOverdueReminderRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Workstream Overdue Reminder";
                                }
                                case SYNCHRONIZE_GOOGLE_CONTACT -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, SynchronizeWithGoogleContactsRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(SynchronizeWithGoogleContactsRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Sync with google contact";
                                }
                                case SYNCHRONIZE_GOOGLE_CALENDAR -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, SynchronizeWithGoogleCalendarRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(SynchronizeWithGoogleCalendarRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Sync with google calendar";
                                }
                                case SYNCHRONIZE_OFFICE_CALENDAR -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, SynchronizeWithOfficeCalendarRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(SynchronizeWithOfficeCalendarRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Sync with google calendar";
                                }
                                case RECURRING_REPORT -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, RecurringReportJob.class);
                                    jobDetail = JobBuilder.newJob(RecurringReportJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Recurring Report";
                                }
                                case DAILY_HR_REMINDERP_ROCEDURE_JOB -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, DailyHrReminderProcedureJob.class);
                                    jobDetail = JobBuilder.newJob(DailyHrReminderProcedureJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Daily Hr Reminder Procedure Job";
                                }
                                case RECURRING_PROJECT_NUMBER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, RecuringRestartProjectNumberJob.class);
                                    jobDetail = JobBuilder.newJob(RecuringRestartProjectNumberJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Recurring Project Number";
                                }
                                case RECURRING_LEAVE_REQUEST_NUMBER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, RecuringRestartProjectNumberJob.class);
                                    jobDetail = JobBuilder.newJob(RecurringRestartLeaveRequestNumberJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Recurring Leave Request Number";
                                }
                                case EMPLOYEE_VISA_EXPIRATION_REMINDER -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, EmployeeVisaExpirationDateRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(EmployeeVisaExpirationDateRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Employee Visa Expiry Date Reminder";
                                }
                                case WORKFLOW_RECURRENCE -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, WorkflowManagementRecurrenceJob.class);
                                    jobDetail = JobBuilder.newJob(WorkflowManagementRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Workflow Management Recurrence";
                                }
                                case RECURRING_WORKFLOW -> {
//                                    jobDetail = new JobDetail(JOB_NAME + recurrence.getObjectID(), JOB_GROUP, RecurringWorkflowManagementJob.class);
                                    jobDetail = JobBuilder.newJob(RecurringWorkflowManagementJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Recurring Workflow Management Job";
                                }
                                case DAILY_FILL_TIMESHEET_FROM_RES_UTIL -> {
                                    jobDetail = JobBuilder.newJob(DailyAutoFillTimesheetRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "Daily Auto Fill Timesheet RecurrenceJob Job";
                                }
                                case SYNCHRONIZE_MAGENTO_CATALOG -> {
                                    jobDetail = JobBuilder.newJob(SynchronizeMagentoCatalogRecurrenceJob.class).withIdentity(JOB_NAME + recurrence.getObjectID(), JOB_GROUP).build();
                                    jobTypeString = "SynchronizeMagentoCatalogRecurrenceJob Job";
                                }
                            }
                        }

                        if (jobDetail != null) {
                            Date recEndDate = recurrenceManager.getTriggerEndDate(recurrence);
                            if (recurrence.getStartDate() != null && recEndDate != null && recEndDate.before(recurrence.getStartDate())) {
                                recEndDate = recurrence.getStartDate();
                            }
                            Integer interval = recurrence.getInterval() != null ? recurrence.getInterval() : Integer.valueOf(1);
                            DateBuilder.IntervalUnit intervalUnit = null;
                            if (recurrence.getType() == RECURRENCE_TYPE_DAILY) {
                                intervalUnit = DateBuilder.IntervalUnit.DAY;
                            } else if (recurrence.getType() == RECURRENCE_TYPE_MONTHLY) {
                                intervalUnit = DateBuilder.IntervalUnit.MONTH;
                            } else if (recurrence.getType() == RECURRENCE_TYPE_MINUTELY) {
                                intervalUnit = DateBuilder.IntervalUnit.MINUTE;
                            } else if (recurrence.getType() == RECURRENCE_TYPE_HOURLY) {
                                intervalUnit = DateBuilder.IntervalUnit.HOUR;
                            } else if (recurrence.getType() == RECURRENCE_TYPE_EVEN_DAILY) {
                                if (new Date().getDate() % 2 == 0) {
                                    intervalUnit = DateBuilder.IntervalUnit.DAY;
                                } else {
                                    return;
                                }
                            } else if (recurrence.getType() == RECURRENCE_TYPE_ODD_DAILY) {
                                if (new Date().getDate() % 2 != 0) {
                                    intervalUnit = DateBuilder.IntervalUnit.DAY;
                                } else {
                                    return;
                                }
                            }
                            AbstractTrigger cronTrigger;

                            if ((recurrence.getType() == RECURRENCE_TYPE_DAILY && recurrence.getDailyPatternOptions() == DAILY_PATTERN_OPTION_INTERVAL) ||
                                    recurrence.getType() == RECURRENCE_TYPE_MONTHLY || recurrence.getType() == RECURRENCE_TYPE_MINUTELY || recurrence.getType() == RECURRENCE_TYPE_HOURLY) {
//                                cronTrigger = new DailyTimeIntervalTrigger.(TRIGGER_NAME + id, TRIGGER_GROUP, JOB_NAME + id, JOB_GROUP, recurrence.getStartDate(), recEndDate, intervalUnit, interval);
                                cronTrigger = new CalendarIntervalTriggerImpl(TRIGGER_NAME + id, TRIGGER_GROUP, recurrence.getStartDate(), recEndDate, intervalUnit, interval);
                                cronTrigger.setJobGroup(JOB_GROUP);
                                cronTrigger.setJobName(JOB_NAME + id);
                            } else {
                                //cronTrigger = new CronTrigger(TRIGGER_NAME + id, TRIGGER_GROUP, JOB_NAME + id, JOB_GROUP, recurrence.getStartDate(), recEndDate, cronExpression);
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

                            cronTrigger.setMisfireInstruction(CronTriggerImpl.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
                            data.put(BUS_OBJECT_ID, recurrence.getBusObjectId());
                            data.put(BUS_OBJECT_PARAM, recurrence.getBusObjectParams() != null ? recurrence.getBusObjectParams() : "");
                            data.put(SchedulerConstant.COMPANY_ID, companyID);
                            data.put(SchedulerConstant.USER_ID, userID);
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
//                            jobDetail.setJobDataMap(jobDataMap);
                            cronTrigger.setJobDataMap(jobDataMap);

                            Date date = cronTrigger.getFireTimeAfter(new Date());
                            if (date != null) {
                                if (cronTrigger.getStartTime().before(new Date())) {
                                    cronTrigger.setStartTime(date);
                                }
                                scheduler.scheduleJob(jobDetail, cronTrigger);
                                System.out.println("Successfully loaded Recurrence id: " + id + "; " + jobTypeString + "; CompanyID: " + companyID != null ? companyID : "; Sched date: " + format.format(date) + "; CronExpression: " + cronExpression);
                            } else {
                                System.out.println("No fire time after current date for recurrenceId: " + id + "; " + jobTypeString + "; CompanyID: " + companyID != null ? companyID : "; StartDate: " + format.format(recurrence.getStartDate()) + "; CronExpression: " + cronExpression);
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        System.err.println(e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                System.err.println(e.getMessage());
            }
        }
    }

    private Integer registerRecurrence(EdsServerHistory serverHistory, String jobDetailName, Date normalFireTime, Date lateFireTime, String jobType, String cronExpression, Integer recurrenceID, Integer companyID) {
        EdsRecurrenceHistory recurrenceHistory = new EdsRecurrenceHistory();
        recurrenceHistory.setJobName(jobDetailName);
        recurrenceHistory.setNormalFireTime(normalFireTime);
        recurrenceHistory.setLateFireTime(lateFireTime);
        recurrenceHistory.setJobType(jobType);
        recurrenceHistory.setCronExpression(cronExpression);
        recurrenceHistory.setRecurrenceID(recurrenceID);
        recurrenceHistory.setFired(false);
        recurrenceHistory.setServerHistory(serverHistory);
        recurrenceHistory.setCompanyID(companyID);
        return recurrenceService.createLateRecurrence(recurrenceHistory);
    }
}
