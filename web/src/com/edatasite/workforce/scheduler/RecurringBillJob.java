package com.edatasite.workforce.scheduler;

import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Sherzod on 6/19/2015.
 */
public class RecurringBillJob extends BaseRecurrenceJob{
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer recurringBillID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        getLogger().info("RecurringBillJob started: " + new Date() + "; CompanyID=" + companyId + "; BillID=" + recurringBillID);
        setCompanyAndDatabase(companyId);
        Integer newInvoiceID = invoiceServiceLocal.createInvoiceFromRecurringBill(recurringBillID);
        getLogger().info("Recurring Bill saved. RecurringBillID:" + recurringBillID + "; NewInvoiceID:" + newInvoiceID);

        invoiceServiceLocal.createPurchaseInvoiceTransaction(newInvoiceID, recurringBillID);
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("RecurringBillJob ended: " + new Date());
    }
}
