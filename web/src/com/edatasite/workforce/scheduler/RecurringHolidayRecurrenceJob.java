package com.edatasite.workforce.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 16.03.12
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */

public class RecurringHolidayRecurrenceJob extends BaseRecurrenceJob {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("RecurringHolidayRecurrenceJob started: " + new Date());
        availabilityService.createRecurringHoliday();
        getLogger().info("RecurringHolidayRecurrenceJob ended: " + new Date());
    }
}
