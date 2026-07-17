package com.edatasite.workforce.listeners;
/**
 * Created by IntelliJ IDEA.
 * User: sherali
 * Date: 12/27/10
 * Time: 1:16 AM
 * To change this template use File | Settings | File Templates.
 */

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.scheduler.RecurringJobsManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RecurringJobsListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(RecurringJobsListener.class);

    public RecurringJobsListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        boolean enabled = Boolean.valueOf(SpringPropertiesUtil.getProperty("bg_recurringJobsManager_enabled"));
        log.info("Staring RecurringJobsListener ......");
        RecurringJobsManager recurringJobsManager = (RecurringJobsManager) ApplicationContextProvider.applicationContext.getBean("recurringJobsManager");
        RecurrenceService recurrenceService = (RecurrenceService) ApplicationContextProvider.applicationContext.getBean("recurrenceService");
        recurringJobsManager.setRecurrenceService(recurrenceService);
        ServerSecurityContext.getInstance().setDatabase(SpringPropertiesUtil.getProperty("bg_databaseType"));
        if (recurringJobsManager.getInitEnabled()) {
            recurringJobsManager.initManager();
        }
        if (enabled) {
            recurrenceService.createRecurrenceLog();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
