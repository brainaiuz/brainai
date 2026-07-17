package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.server.gwd.GWDWebService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/22/13
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class GWDSyncRecurrenceJob extends BaseRecurrenceJob {

    private GWDWebService gwdWebService = (GWDWebService) ApplicationContextProvider.applicationContext.getBean("gwdWebService");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("GWD_SYNC_TASKER_START");
        gwdWebService.startSyncData();
        getLogger().info("GWD_SYNC_TASKER_END");

    }

}
