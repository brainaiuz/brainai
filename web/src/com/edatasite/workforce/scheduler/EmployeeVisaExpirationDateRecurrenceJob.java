package com.edatasite.workforce.scheduler;

import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * User: Ilhombek
 * Date: 3/9/13
 * Time: 3:50 PM
 */
@Transactional
public class EmployeeVisaExpirationDateRecurrenceJob extends BaseRecurrenceJob {

    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer employeeProfileID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer recurrenceID = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        Integer companyID = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);

        getLogger().info("EmployeeVisaExpirationDateRecurrenceJob started: " + new Date() + "; CompanyID=" + companyID + "; EmployeeProfileID=" + employeeProfileID + "; RecurrenceID=" + recurrenceID);

		setCompanyAndDatabase(companyID);
        hrmsServiceLocal.sendEmployeeVisaExpirationDateEmailNotification(recurrenceID, employeeProfileID);
        recurrenceService.updateRecurrence(recurrenceID, true, true);
        updateRecurrenceHistory(jobExecutionContext.getMergedJobDataMap());
        ServerSecurityContext.getInstance().removeCompanyId();

        getLogger().info("EmployeeVisaExpirationDateRecurrenceJob ended :" + new Date());
    }
}
