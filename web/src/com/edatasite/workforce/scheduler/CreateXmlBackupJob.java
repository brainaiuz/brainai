package com.edatasite.workforce.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * User: Faxriddin Taslimov
 * Date: 07/26/2019
 */
public class CreateXmlBackupJob extends BaseRecurrenceJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("CRETE_XML_BACKUP_START");
        coreService.createXmlBackupFile();
        getLogger().info("CRETE_XML_BACKUP_TASKER_END");

    }

}
