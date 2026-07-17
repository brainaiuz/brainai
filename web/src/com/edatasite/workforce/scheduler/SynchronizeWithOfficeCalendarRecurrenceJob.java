package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by SuperKomp on 17.08.2016.
 */
public class SynchronizeWithOfficeCalendarRecurrenceJob extends BaseRecurrenceJob {

    private MessageManager messageManager = (MessageManager) ApplicationContextProvider.applicationContext.getBean("messageManager");

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Integer companyID = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        Integer userID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer recurrenceID = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        if (companyID != null && userID != null) {
            ServerSecurityContext.getInstance().setStaticUserID(userID);
            setCompanyAndDatabase(companyID);
            Date startDate = new Date();
            startDate.setDate(1);
            Date endDate = (Date) startDate.clone();
            endDate.setMonth(startDate.getMonth() + 3);
            getLogger().info("SynchronizeWithOfficeCalendarRecurrenceJob started: " + new Date());
            EdsRecurrence recurrence = recurrenceManager.get(recurrenceID);
            if (recurrence != null && !IN_PROGRESS.equals(recurrence.getStatus())) {
                try {
                    blockRecurrence(recurrenceID);
                    googleCalendarService.syncEvents(null, userID, startDate, endDate);
                    unBlockRecurrence(recurrenceID, true);
                } catch (Exception e) {
                    unBlockRecurrence(recurrenceID, false);
                    messageManager.sendMessageToOwnerAutoSync(userID);
                    e.printStackTrace();
                }
            }
            ServerSecurityContext.getInstance().removeCompanyId();
            getLogger().info("SynchronizeWithOfficeCalendarRecurrenceJob ended: " + new Date());
        }
    }
}
