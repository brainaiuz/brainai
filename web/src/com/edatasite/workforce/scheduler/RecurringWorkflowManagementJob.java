package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by Administrator on 13.06.14.
 */

public class RecurringWorkflowManagementJob extends BaseRecurrenceJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer workflowRuleID = (Integer) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_ID);
        Integer companyID = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        Integer userId = (Integer) jobExecutionContext.getMergedJobDataMap().get(USER_ID);
        if (companyID != null) {
            setCompanyAndDatabase(companyID);
            SecurityContext.getInstance().setStaticUserID(userId);
            allInOneService.runRecurringWorkflow(workflowRuleID, companyID, userId);
        }
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("RecurringWorkflowJob ended:" + new Date());
    }
}
