package com.edatasite.workforce.mail;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 22.08.2012
 * Time: 13:54:27
 * To change this template use File | Settings | File Templates.
 */
public class SmsSenderBaseJob implements Job{

    @Autowired
    @Qualifier("smsSender")
    private IBaseJob smsSender;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ServerSecurityContext.getInstance().setDatabase(SpringPropertiesUtil.getProperty("bg_databaseType"));
        smsSender.execute();
    }
}