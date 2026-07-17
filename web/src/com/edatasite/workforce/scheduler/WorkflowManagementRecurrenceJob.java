package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by Administrator on 13.06.14.
 */

public class WorkflowManagementRecurrenceJob extends BaseRecurrenceJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
        Integer companyID = (Integer) jobExecutionContext.getMergedJobDataMap().get(COMPANY_ID);
        SecurityContext.setCompanyID(companyID);
        if (companyID != null) {
            String database = globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID);
            SecurityContext.getInstance().setDatabase(database);
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }
        String busObjectParams = (String) jobExecutionContext.getMergedJobDataMap().get(BUS_OBJECT_PARAM);
        if (busObjectParams != null && !"".equals(busObjectParams)) {
            String[] params = busObjectParams.split(",");
            Date actionRunTime = new Date(Long.valueOf(params[0]));
            String actionRelationType = params[1];
            Integer actionObjectID = Integer.valueOf(params[2]);
            String relationType = params[3];
            Integer relationID = Integer.valueOf(params[4]);
            Integer userID = params.length > 5 ? Integer.valueOf(params[5]) : null;
            allInOneService.runRecurrenceForWorkflowAction(actionRelationType, actionObjectID, relationType, relationID, userID);
        }
    }
}
