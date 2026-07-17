package com.edatasite.workforce.scheduler;

import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 01.05.2010
 * Time: 17:33:18
 * To change this template use File | Settings | File Templates.
 */
public class RecurringSaleInvoiceJob extends BaseRecurrenceJob {
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer recurringInvoiceID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        getLogger().info("RecurringSaleInvoiceJob started: " + new Date() + "; CompanyID=" + companyId + "; InvoiceID=" + recurringInvoiceID);
        setCompanyAndDatabase(companyId);
        Integer newInvoiceID = invoiceServiceLocal.createInvoiceFromRecurringInvoice(recurringInvoiceID);
        if (newInvoiceID == -1) {
            getLogger().info("RecurringInvoiceID:" + recurringInvoiceID + " not found or already deleted! ");
        } else {
            getLogger().info("Recurring Invoice saved. RecurringInvoiceID:" + recurringInvoiceID + "; NewInvoiceID:" + newInvoiceID);
            invoiceServiceLocal.createSalesInvoiceTransactionAndSendMessage(newInvoiceID, recurringInvoiceID);
        }
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("RecurringSaleInvoiceJob ended: " + new Date());
    }
}
