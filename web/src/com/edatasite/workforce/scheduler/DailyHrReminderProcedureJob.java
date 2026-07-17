package com.edatasite.workforce.scheduler;

import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Oct 6, 2010
 * Time: 5:35:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class DailyHrReminderProcedureJob extends BaseRecurrenceJob {
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer typeID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        Integer userId = (Integer) jobExecutionContext.getMergedJobDataMap().get(USER_ID);
        getLogger().info("DailyHrReminderProcedureJob started: " + new Date() + "; CompanyID=" + companyId + "; UserID=" + userId);
        setCompanyAndDatabase(companyId);
        ServerSecurityContext.getInstance().setStaticUserID(userId);
        if (userId != null) {
            hrmsServiceLocal.sendHrReminders(typeID, userId, companyId);
        }
//        updateAndCreateRecurrenceHistory(jobExecutionContext.getJobDetail());
        ServerSecurityContext.getInstance().setStaticUserID(null);
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("DailyHrReminderProcedureJob ended:" + new Date() + "; CompanyID=" + companyId + "; UserID=" + userId);
    }
}
