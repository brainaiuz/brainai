package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.gwt.core.server.app.MagentoService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by Shohruh on 19 Dec 2016.
 */
@DisallowConcurrentExecution
public class SynchronizeMagentoCatalogRecurrenceJob extends BaseRecurrenceJob {

    private MagentoService magentoService = (MagentoService) ApplicationContextProvider.applicationContext.getBean("magentoService");
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        Integer recurrenceId = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        getLogger().info("SynchronizeMagentoCatalogRecurrenceJob started: " + new Date() + " CompanyID=" + companyId);
        setCompanyAndDatabase(companyId);
        EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
        if (recurrence != null && !IN_PROGRESS.equals(recurrence.getStatus())) {
            try {
                blockRecurrence(recurrenceId);
                magentoService.synchronizeWithMagentoCatalog();
                unBlockRecurrence(recurrenceId, true);
            } catch (Exception e) {
                unBlockRecurrence(recurrenceId, false);
                e.printStackTrace();
            }
        }
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("SynchronizeMagentoCatalogRecurrenceJob ended: " + new Date());
    }
}
