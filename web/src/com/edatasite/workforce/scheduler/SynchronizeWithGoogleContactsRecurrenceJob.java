package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 04.07.2010
 * Time: 15:26:25
 * To change this template use File | Settings | File Templates.
 */

public class SynchronizeWithGoogleContactsRecurrenceJob extends BaseRecurrenceJob {
    ContactService contactService = (ContactService) ApplicationContextProvider.applicationContext.getBean("contactService");

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		super.execute(jobExecutionContext);
        Integer employeeId = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        Integer recurrenceId = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        getLogger().info("SynchronizeWithGoogleContactsRecurrenceJob started: " + new Date() + "CompanyID=" + companyId + "; EmployeeID=" + employeeId);
        setCompanyAndDatabase(companyId);
		EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
		if (recurrence != null && !IN_PROGRESS.equals(recurrence.getStatus())) {
			try {
				blockRecurrence(recurrenceId);
				contactService.recurringSyncContactsWithGoogle(employeeId);
				unBlockRecurrence(recurrenceId, true);
			} catch (Exception e) {
				unBlockRecurrence(recurrenceId, false);
				e.printStackTrace();
			}
		}
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("SynchronizeWithGoogleContactsRecurrenceJob ended: " + new Date());
    }
}
