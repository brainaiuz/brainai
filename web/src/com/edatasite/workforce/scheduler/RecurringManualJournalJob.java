package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.accounting.server.app.ManualEntryServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Sherzod on 6/19/2015.
 */
public class RecurringManualJournalJob extends BaseRecurrenceJob {

    private ManualEntryServiceLocal manualEntryServiceLocal = (ManualEntryServiceLocal) ApplicationContextProvider.applicationContext.getBean("manualEntryService");

    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer recurringManualJournalID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer recurrencyID = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        getLogger().info("RecurringManualJournalJob started: " + new Date() + "; CompanyID=" + companyId + "; ManualJournalID=" + recurringManualJournalID);
        setCompanyAndDatabase(companyId);
        Integer newInvoiceID = manualEntryServiceLocal.createManualJournalFromRecurringJob(recurrencyID, recurringManualJournalID);
        getLogger().info("Recurring Manual Journal saved. RecurringManualJournalID:" + recurringManualJournalID + "; NewManualJournalID:" + newInvoiceID);

        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("RecurringManualJournalJob ended: " + new Date());
    }
}
