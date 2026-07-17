package com.edatasite.workforce.mail;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TcScheduledTaskBaseJob implements Job{
    @Autowired
    @Qualifier("tcScheduledTasker")
    private IBaseJob tcScheduledTasker;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ServerSecurityContext.getInstance().setDatabase(SpringPropertiesUtil.getProperty("bg_databaseType"));
        tcScheduledTasker.execute();
    }
}
