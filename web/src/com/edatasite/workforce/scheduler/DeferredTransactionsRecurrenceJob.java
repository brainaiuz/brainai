package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsDeferredTransactionItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.DeferredTransactionManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 6/11/2021 7:00 PM
 */
public class DeferredTransactionsRecurrenceJob extends BaseRecurrenceJob {
    private static DeferredTransactionManager deferredTransactionManager = (DeferredTransactionManager) ApplicationContextProvider.applicationContext.getBean("deferredTransactionManager");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer companyID = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        EdsCompany company = companyManager.getCompany(companyID);

        LocalDate companyDate = LocalDate.now(company.getTimeZone().toZoneId());
        if (!companyDate.equals(companyDate.withDayOfMonth(1))) {
            return;
        }


        Date date = Date.from(companyDate.minusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        List<EdsDeferredTransactionItem> items = deferredTransactionManager.getItems(ServerUtils.getMonthStartDate(date), ServerUtils.getMonthEndDate(date));

        if (!CollectionUtils.isEmpty(items)) {
            LocalDate startDate = LocalDate.now().withDayOfMonth(1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

            for (EdsDeferredTransactionItem item : items) {
                LocalDate fromLocalDate = Instant.ofEpochMilli(item.getFromDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate toLocalDate = Instant.ofEpochMilli(item.getToDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

                if (!startDate.isBefore(fromLocalDate) && startDate.isBefore(toLocalDate)) {
                    fromLocalDate = startDate;
                } else if (!(startDate.isBefore(fromLocalDate) && fromLocalDate.isBefore(endDate))) {
                    continue;
                }

                if (!endDate.isAfter(toLocalDate) && endDate.isAfter(fromLocalDate)) {
                    toLocalDate = endDate;
                } else if (!(endDate.isAfter(toLocalDate) && toLocalDate.isAfter(startDate))) {
                    continue;
                }

                item.setDayCount(Period.between(fromLocalDate, toLocalDate).getDays() + 1);
                item.setJournalDate(Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                accountingServiceLocal.createTransactionForDeferredObject(item);
            }
        }
    }
}
