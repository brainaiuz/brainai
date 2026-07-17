package com.edatasite.workforce.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Sep 12, 2010
 * Time: 8:01:48 PM
 * To change this template use File | Settings | File Templates.
 */

public class RecurringEventRecurrenceJob extends BaseRecurrenceJob {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("RecurringEventRecurrenceJob started: " + new Date());
        googleCalendarService.createRecurringEvent();
        getLogger().info("RecurringEventRecurrenceJob ended: " + new Date());
    }
}
