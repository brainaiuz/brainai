package com.edatasite.workforce.scheduler;

import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: May 3, 2010
 * Time: 2:55:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class CalendarEventRecurrenceJob extends BaseRecurrenceJob {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer eventId = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        String reminderType = (String) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_PARAM);
        getLogger().info("CalendarEventRecurrenceJob started: " + new Date() + "; CompanyID=" + companyId + "; EventID=" + eventId);
		setCompanyAndDatabase(companyId);
        googleCalendarService.sendEventNotification(eventId, Integer.valueOf(reminderType));
        updateRecurrenceHistory(jobExecutionContext.getMergedJobDataMap());
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("CalendarEventRecurrenceJob ended: " + new Date());
    }
}
