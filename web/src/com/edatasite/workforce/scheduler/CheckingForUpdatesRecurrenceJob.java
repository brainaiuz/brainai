package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.outfromlisten.RecurrenceServiceLocal;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: May 7, 2010
 * Time: 8:56:36 PM
 * To change this template use File | Settings | File Templates.
 */

public class CheckingForUpdatesRecurrenceJob extends BaseRecurrenceJob {

    private RecurrenceServiceLocal recurrenceServiceLocal = (RecurrenceServiceLocal) ApplicationContextProvider.applicationContext.getBean("recurrenceService");

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        recurrenceServiceLocal.checkForUpdate();
    }
}

