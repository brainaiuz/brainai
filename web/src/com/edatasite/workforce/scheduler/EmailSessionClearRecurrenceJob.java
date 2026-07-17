package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.gwt.core.server.utils.EmailSessionCache;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Jul-2011
 * Time: 18:25:44
 */
public class EmailSessionClearRecurrenceJob extends BaseRecurrenceJob {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        getLogger().info("EmailSessionClearRecurrenceJob started: " + new Date());
        for (String key : EmailSessionCache.keySet()) {
            String storeType = key.split("_")[1];
            Session session = EmailSessionCache.get(key);
            try {
                Store store = session.getStore(storeType);
                if (!store.isConnected()) {
                    EmailSessionCache.remove(key);
                }
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
        getLogger().info("EmailSessionClearRecurrenceJob ended: " + new Date());
    }
}
