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
 * User: Admin
 * Date: 08.11.2008
 * Time: 13:54:27
 * To change this template use File | Settings | File Templates.
 */
public class PrefBaseJob implements Job {

    @Autowired
    @Qualifier("prefMailer")
    private IBaseJob prefMailer;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ServerSecurityContext.getInstance().setDatabase(SpringPropertiesUtil.getProperty("bg_databaseType"));
        prefMailer.execute();
    }
}
