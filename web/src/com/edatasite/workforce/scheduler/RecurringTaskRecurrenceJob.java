package com.edatasite.workforce.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 4/30/12
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class RecurringTaskRecurrenceJob extends BaseRecurrenceJob {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("RecurringTaskRecurrenceJob started: " + new Date());
        taskService.createRecurringTask();
        getLogger().info("RecurringTaskRecurrenceJob ended: " + new Date());
    }
}
