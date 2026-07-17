package com.edatasite.workforce.mail;

import com.edatasite.workforce.scheduler.BaseRecurrenceJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class DailyReportService extends BaseRecurrenceJob {

    @Autowired
    @Qualifier("baseDailyReport")
    private BaseDailyReport baseDailyReport;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
        baseDailyReport.execute();
    }
}
