package com.edatasite.workforce.core.tools;

import com.edatasite.workforce.core.UpdateLogger;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gson.Gson;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class BackendRunSchemaUpdate extends EdsSchemaUpdater {

    static {
        //try retrieve data from file]
        props = new Properties();
        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        if (StringUtil.isEmpty(profile)) {
            System.out.println("--------------------------");
            System.out.println("Please set spring.profiles.active!!!");
            System.out.println("VM options add this -Dspring.profiles.active=local or app");
            System.out.println("--------------------------");
            System.exit(0);
        }
        InputStream stream0 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/hibernate.properties");
        InputStream stream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/application.properties");
        if (stream0 != null) {
            try {
                props.load(stream0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (stream1 != null) {
            try {
                props.load(stream1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void runSchemaUpdate(String args[]) {
        patchDirectory = readPatchDirectory();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<String> clusterList = new Gson().fromJson(props.getProperty("tenant.datasource.cluster.update", ""), List.class);

        runGlobalSQLPatcher();

        for (String cluster : clusterList) {
            runSqlPatcher(SqlScriptExecutor.BEFORE_SCHEMA_UPDATE, cluster, args);
        }

        boolean forAllSchema = Arrays.stream(args).anyMatch("all"::equalsIgnoreCase);

        for (String cluster : clusterList) {
            log(ConsoleColors.GREEN_BRIGHT, "SCHEMA_UPDATE for " + cluster + " is started by generated SQL statements...");
            if (forAllSchema) {
                setDbUrl(getClusterUrl(cluster));
                runSqlPatcherForSchemaUpdate(getDefaultSchema(args), cluster);
            } else {
                runHibernateSchemaUpdate(Arrays.asList(args), cluster);
            }
            runSqlPatcher(SqlScriptExecutor.AFTER_SCHEMA_UPDATE, cluster, args);
        }

        stopWatch.stop();
        System.out.println("=== SchemaUpdate: failed schemas: " + errorCompaniesList);
        System.out.println("=== Failed patches: TODO [filename] ==> failed schemas");
        System.out.println("=== Overall execution time: " + stopWatch.getTotalTimeMillis());

        UpdateLogger.conclude();
        SqlScriptExecutor.shutdownExecutorService();
    }
}