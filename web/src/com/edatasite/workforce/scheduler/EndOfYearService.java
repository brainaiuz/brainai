package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.mail.IBaseJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class EndOfYearService extends BaseRecurrenceJob{
    @Autowired
    @Qualifier("endOfYearServiceBean")
    private IBaseJob endOfYearService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
        if (endOfYearService != null) {
            endOfYearService.execute();
        } else {
            System.err.println("--------------------- End Of Year Service is NULL ---------------------");
        }
    }
}
