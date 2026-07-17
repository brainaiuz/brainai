package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by User on 20.04.2016.
 */
public class ContractOverdueReminderRecurrenceJob extends BaseRecurrenceJob {
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer contractId = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer recurrenceId = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);

        getLogger().info("ContractOverdueReminderRecurrenceJob started: " + new Date() + "; CompanyID=" + companyId + "; ContractID=" + contractId + "; RecurrenceID=" + recurrenceId);

        setCompanyAndDatabase(companyId);
        EdsRecurrence recurrence = recurrenceService.getRecurrence(recurrenceId);

        projectService.sendContractOverDueEmailNotification(contractId, companyId,recurrence);
        recurrenceService.updateRecurrence(recurrence, true, true);
        recurrenceService.setRecurrenceStatus(recurrenceId, SUCCESS);
        updateRecurrenceHistory(jobExecutionContext.getMergedJobDataMap());
        ServerSecurityContext.getInstance().removeCompanyId();

        getLogger().info("ContractOverdueReminderRecurrenceJob ended :" + new Date());
    }
}
