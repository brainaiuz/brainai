package com.edatasite.workforce.scheduler;

import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * User: Fatxulla
 * Date: 28.05.2010
 * Time: 13:25:03
 */
public class ProjectOverdueReminderRecurrenceJob extends BaseRecurrenceJob {
    @Override
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer projectID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer recurrenceId = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        setCompanyAndDatabase(companyId);
        boolean isComplited = coreService.isComplate("PROJECT", projectID);
        if (!isComplited) {
            getLogger().info("ProjectOverdueReminderRecurrenceJob started: " + new Date() + "; CompanyID=" + companyId + "; ProjectID=" + projectID + "; RecurrenceID=" + recurrenceId);

            projectService.sendEmailNotification(projectID, companyId);
            recurrenceService.updateRecurrence(recurrenceId, true, true);
            updateRecurrenceHistory(jobExecutionContext.getMergedJobDataMap());

            getLogger().info("ProjectOverdueReminderRecurrenceJob ended :" + new Date());
        }
        ServerSecurityContext.getInstance().removeCompanyId();
    }
}
