package com.edatasite.workforce.mail;

import com.edatasite.workforce.scheduler.BaseRecurrenceJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Dec 9, 2009
 * Time: 7:08:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeeklyReportService extends BaseRecurrenceJob {

    @Autowired
    @Qualifier("weeklySubscriptionReportJob")
    private IBaseJob baseWeeklyReport;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        baseWeeklyReport.execute();
    }
}
