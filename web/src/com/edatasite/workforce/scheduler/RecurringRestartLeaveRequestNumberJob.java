package com.edatasite.workforce.scheduler;

import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public class RecurringRestartLeaveRequestNumberJob extends BaseRecurrenceJob {

    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer companyId = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get(COMPANY_ID);
        getLogger().info("RecuringRestartLeaveRequestNumberJob started: " + new Date() + "; CompanyID=" + companyId);
        setCompanyAndDatabase(companyId);
        availabilityService.restartLeaveRequestNumber();
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("RecuringRestartLeaveRequestNumberJob ended:" + new Date());
    }
}
