package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * User: Ilxom Lutfullaev  Date: Apr 27, 2010
 */

public class TimesheetRecurrenceJob extends BaseRecurrenceJob {

    private static ProfileServiceLocal profileServiceLocal = (ProfileServiceLocal) ApplicationContextProvider.applicationContext.getBean("profileService");

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer employeeId = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        String when = (String) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_PARAM);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        Integer type = (Integer) jobExecutionContext.getMergedJobDataMap().get(TYPE);
        Integer recurrenceID = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        getLogger().info("TimesheetRecurrenceJob started: " + new Date() + "; CompanyID=" + companyId + "; EmployeeID=" + employeeId + "; Type=" + type);
        setCompanyAndDatabase(companyId);
        profileServiceLocal.sendTimeSheetReminder(recurrenceID, employeeId, type, when);
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("TimesheetRecurrenceJob ended: " + new Date());
    }
}
