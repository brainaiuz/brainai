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
public class RecurringReportJob extends BaseRecurrenceJob {
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer reportID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer companyId = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        Integer userId = (Integer) jobExecutionContext.getMergedJobDataMap().get(USER_ID);
        String category = (String) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_PARAM);
        Integer recurrenceId = (Integer) jobExecutionContext.getMergedJobDataMap().get(REC_OBJECT_ID);
        getLogger().info("RecurringReportJob started: " + new Date() + "; CompanyID=" + companyId + "; ReportID=" + reportID + "; UserID=" + userId);
        setCompanyAndDatabase(companyId);
        ServerSecurityContext.getInstance().setStaticUserID(userId);
        if (userId != null) {
            coreService.sendToClient(reportID, userId, companyId, category, recurrenceId);
        }
        ServerSecurityContext.getInstance().removeCompanyId();
        ServerSecurityContext.getInstance().setStaticUserID(null);
        getLogger().info("RecurringReportJob ended:" + new Date());
    }
}
