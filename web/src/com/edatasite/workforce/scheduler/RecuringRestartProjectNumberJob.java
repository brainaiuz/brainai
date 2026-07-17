package com.edatasite.workforce.scheduler;

import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: JavaZone
 * Date: 6/25/12
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecuringRestartProjectNumberJob extends BaseRecurrenceJob {
    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer companyId = (Integer) jobExecutionContext.getJobDetail().getJobDataMap().get(COMPANY_ID);
        getLogger().info("RecuringRestartProjectNumberJob started: " + new Date() + "; CompanyID=" + companyId);
        setCompanyAndDatabase(companyId);
        projectService.restartProectNumber();
        ServerSecurityContext.getInstance().removeCompanyId();
        getLogger().info("RecuringRestartProjectNumberJob ended:" + new Date());
    }

}
