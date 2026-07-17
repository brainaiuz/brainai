package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.mail.IBaseJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Mar 29, 2010
 * Time: 4:47:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class EfileService extends BaseRecurrenceJob {

    @Autowired
    @Qualifier("efileSubmitterBean")
    private IBaseJob efileSubmitter;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
        if (efileSubmitter != null) {
            efileSubmitter.execute();
        } else {
            System.err.println("---------------------7777777777777777777777---------------------");
        }
    }
}
