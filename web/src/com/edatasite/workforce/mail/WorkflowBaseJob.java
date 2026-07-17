package com.edatasite.workforce.mail;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by Azazello on 6/24/2017.
 */
public class WorkflowBaseJob implements Job {

    @Autowired
    @Qualifier("workflowMailer")
    private IBaseJob workflowMailer;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ServerSecurityContext.getInstance().setDatabase(SpringPropertiesUtil.getProperty("bg_databaseType"));
        workflowMailer.execute();
    }
}
