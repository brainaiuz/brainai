package com.edatasite.workforce.mail;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 1:26 PM
 */
public class TCScheduledTaskerService implements IBaseJob {

    private static Logger log = LoggerFactory.getLogger(TCScheduledTaskerService.class);

    @Autowired
    private TCService tcService;

    @Override
    public void execute() {
        String databaseType = SpringPropertiesUtil.getProperty("bg_databaseType");
        log.info("TC_SCHEDULED_TASKER_START");
        ServerSecurityContext.getInstance().setDatabase(databaseType);
        tcService.executeScheduledTasker();
        log.info("TC_SCHEDULED_TASKER_END");
    }

}
