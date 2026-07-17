package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar
 * Date: Mar 4, 2010
 * Time: 2:20:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class OverdueInvoiceRecurrenceJob extends BaseRecurrenceJob {
    InvoiceServiceLocal invoiceServiceLocal = (InvoiceServiceLocal) ApplicationContextProvider.applicationContext.getBean("invoiceService");

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer employeeId = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        getLogger().info("OverdueInvoiceRecurrenceJob started: " + new Date() + "; CompanyID=" + companyId + "; EmployeeID=" + employeeId);
        setCompanyAndDatabase(companyId);
        try {
            invoiceServiceLocal.sendOverDueInvoiceReminders(employeeId, companyId, (Boolean) jobExecutionContext.getMergedJobDataMap().get("toClient"), (Integer) jobExecutionContext.getMergedJobDataMap().get("recObjectId"));
        } catch (Exception e) {
            getLogger().error("Error occured while executing OverdueInvoiceRecurrenceJob \n");
            e.printStackTrace();
        }
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("OverdueInvoiceRecurrenceJob ended:" + new Date());
    }
}
