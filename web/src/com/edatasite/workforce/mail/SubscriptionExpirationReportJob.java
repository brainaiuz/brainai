package com.edatasite.workforce.mail;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.scheduler.BaseRecurrenceJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Ilhombek
 * Date: 4/3/13
 * Time: 3:15 PM
 */

public class SubscriptionExpirationReportJob extends BaseRecurrenceJob {

    private CommonServiceLocal commonServiceLocal = (CommonServiceLocal) ApplicationContextProvider.applicationContext.getBean("commonService");

    @Transactional
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        try {
            //
            commonServiceLocal.executeSubscriptionExpirationReport();
            //
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
