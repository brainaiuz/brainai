package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: Tulqin
 * Date: 2/6/13
 * Time: 5:29 PM
 */
public class CreateSchemaRecurrenceJob extends BaseRecurrenceJob {

    private static int schemaCountNeedToCreate = 10;

    private static final int awsDefaultFreeSchemasCount = 20;
    private static Integer awsCurrentFreeSchemasCount = 0;

    private static final int appDefaultFreeSchemasCount = 100;
    private static Integer appCurrentFreeSchemasCount = 0;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String databaseType = getDataBaseType();
        String hostName = SpringPropertiesUtil.getProperty("bg_hostName");
        if (databaseType.equals(TenantContextHolder.FREE_DB)) {
            getLogger().info("---->> Begin create schema process ...");
            getLogger().info("Database type :" + databaseType);
            if ((hostName.equals("aws.kpi.com") || hostName.equals("newui.kpi.com")) && companyManager.getCurrentFreeSchemasCount() != null) {
                awsCurrentFreeSchemasCount = companyManager.getCurrentFreeSchemasCount();
                schemaCountNeedToCreate = awsDefaultFreeSchemasCount - awsCurrentFreeSchemasCount;

            } else if (hostName.contains("app.") && companyManager.getCurrentFreeSchemasCount() != null) {
                appCurrentFreeSchemasCount = companyManager.getCurrentFreeSchemasCount();
                schemaCountNeedToCreate = appDefaultFreeSchemasCount - appCurrentFreeSchemasCount;

            }

            getLogger().info(" Begin create schema for " + hostName);
            if (schemaCountNeedToCreate > 0 && schemaCountNeedToCreate < 200) {
                backendService.createSchemas(schemaCountNeedToCreate);
                getLogger().info(schemaCountNeedToCreate + " schemas successfully created ");
            }
        }
    }
}
