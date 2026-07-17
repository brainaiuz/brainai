package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.crm.server.app.MassMailServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Ilxom Lutfullaev
 * Date: 01.04.11
 * Time: 20:35
 */

public abstract class BaseRecurrenceJob implements Job, SchedulerConstant {

    protected static CoreService coreService = (CoreService) ApplicationContextProvider.applicationContext.getBean("reportingCoreService");
    protected static TaskServiceLocal taskService = (TaskServiceLocal) ApplicationContextProvider.applicationContext.getBean("taskService");
    protected static InvoiceServiceLocal invoiceServiceLocal = (InvoiceServiceLocal) ApplicationContextProvider.applicationContext.getBean("invoiceService");
    protected static MassMailServiceLocal massMailServiceLocal = (MassMailServiceLocal) ApplicationContextProvider.applicationContext.getBean("massMailService");
    protected static BackendServiceLocal backendService = (BackendServiceLocal) ApplicationContextProvider.applicationContext.getBean("backendService");
    protected static ProjectServiceLocal projectService = (ProjectServiceLocal) ApplicationContextProvider.applicationContext.getBean("projectService");
    protected static RecurrenceService recurrenceService = (RecurrenceService) ApplicationContextProvider.applicationContext.getBean("recurrenceService");
    protected static AvailabilityServiceLocal availabilityService = (AvailabilityServiceLocal) ApplicationContextProvider.applicationContext.getBean("availabilityService");
    protected static GoogleCalendarServiceLocal googleCalendarService = (GoogleCalendarServiceLocal) ApplicationContextProvider.applicationContext.getBean("googleCalendarService");
    protected static HrmsServiceLocal hrmsServiceLocal = (HrmsServiceLocal) ApplicationContextProvider.applicationContext.getBean("hrmsService");
    protected static AllInOneServiceLocal allInOneService = (AllInOneServiceLocal) ApplicationContextProvider.applicationContext.getBean("allInOneService");
    protected static CompanyManager companyManager = (CompanyManager) ApplicationContextProvider.applicationContext.getBean("companyManager");
    protected static RecurrenceManager recurrenceManager = (RecurrenceManager) ApplicationContextProvider.applicationContext.getBean("recurrenceManager");
    protected static GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = (GlobalAuthJdbcSpringManager) ApplicationContextProvider.applicationContext.getBean("globalAuthJdbcSpringManager");
    protected static AccountingServiceLocal accountingServiceLocal = (AccountingServiceLocal) ApplicationContextProvider.applicationContext.getBean("accountingService");
    protected static RecurringJobsManager recurringJobsManager = (RecurringJobsManager) ApplicationContextProvider.applicationContext.getBean("recurringJobsManager");
    protected static Scheduler scheduler = recurringJobsManager.getScheduler();

    public BaseRecurrenceJob() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ServerSecurityContext.getInstance().setDatabase(getDataBaseType());

        Integer companyID = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);

        if (companyID != null) {
            EdsCompany company = companyManager.get(companyID);

            if (company != null && !company.getActive()) {
                Integer recurrencyID = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);

                if (recurrencyID != null) {
                    EdsRecurrence recurrence = recurrenceManager.get(recurrencyID);
                    recurrence.setChanged(true);
                    recurrence.setDeleted(true);
                    recurrenceManager.update(recurrence);
                }
            }
        }
    }

    protected Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Bu metod bir marta bajariladigan recurrence job lar uchun ishlatiladi.
     * Masalan: task overdue reminder;
     * Faqat recurrence history yaratib ketadi.
     *
     * @param jobDataMap - JobDataMap
     */
    protected void updateRecurrenceHistory(JobDataMap jobDataMap) {
        String triggerName = jobDataMap.getString(TRIGGER_NAME);
        Trigger trigger = null;
        try {
//            trigger = scheduler.getTrigger(triggerName, TRIGGER_GROUP);
            trigger = scheduler.getTrigger(new TriggerKey(triggerName, TRIGGER_GROUP));
        } catch (SchedulerException e) {
            System.out.println("Unable to get trigger from scheduler with name: " + triggerName);
            e.printStackTrace();
        }
        if (trigger != null && trigger.getJobDataMap().get(RECURRENCE_HISTORY_ID) != null) {
            Integer recurrenceHistoryID = (Integer) trigger.getJobDataMap().get(RECURRENCE_HISTORY_ID);
            if (recurrenceHistoryID != null) {
                recurrenceService.updateRecurrenceHistory(recurrenceHistoryID);
            }
        }
    }

    protected void blockRecurrence(Integer recurrenceID) {
        recurrenceService.setRecurrenceStatus(recurrenceID, IN_PROGRESS);
    }

    protected void unBlockRecurrence(Integer recurrenceID, boolean isSuccess) {
        if (isSuccess) {
            recurrenceService.setRecurrenceStatus(recurrenceID, SUCCESS);
        } else {
            recurrenceService.setRecurrenceStatus(recurrenceID, FAIL);
        }
    }

    protected String getDataBaseType() {
        return SpringPropertiesUtil.getProperty("bg_databaseType");
    }

    protected String getDataBaseType(Integer companyID) {
        return globalAuthJdbcSpringManager.getCompanyClusterType(companyID);
    }

    protected void setCompanyAndDatabase(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        ServerSecurityContext.getInstance().setDatabase(getDataBaseType());
    }
}
