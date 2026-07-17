package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 7, 2010
 * Time: 3:52:36 PM
 * To change this template use File | Settings | File Templates.
 */

public class SynchronizeWithGoogleCalendarRecurrenceJob extends BaseRecurrenceJob {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer companyID = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        Integer userID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer recurrenceID = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        if (companyID != null && userID != null) {
            setCompanyAndDatabase(companyID);
            ServerSecurityContext.getInstance().setStaticUserID(userID);
            Date startDate = new Date();
            startDate.setDate(1);
            Date endDate = (Date) startDate.clone();
            endDate.setMonth(startDate.getMonth() + 3);
            getLogger().info("SynchronizeWithGoogleCalendarRecurrenceJob started: " + new Date());
            EdsRecurrence recurrence = recurrenceManager.get(recurrenceID);
            if (recurrence != null && !IN_PROGRESS.equals(recurrence.getStatus())) {
                try {
                    blockRecurrence(recurrenceID);
                    googleCalendarService.synchronizeEvents(userID, startDate, endDate);
                    unBlockRecurrence(recurrenceID, true);
                } catch (Exception e) {
                    unBlockRecurrence(recurrenceID, false);
                    e.printStackTrace();
                }
            }
            ServerSecurityContext.getInstance().removeCompanyId();
            getLogger().info("SynchronizeWithGoogleCalendarRecurrenceJob ended: " + new Date());
        }
    }
}
