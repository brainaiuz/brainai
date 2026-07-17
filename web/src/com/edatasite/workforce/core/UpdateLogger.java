package com.edatasite.workforce.core;

import com.edatasite.workforce.core.tools.ConsoleColors;
import com.edatasite.workforce.core.tools.SchemaUpdateLog;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UpdateLogger {
    static ConcurrentHashMap<String, SchemaUpdateLog> clusterSchemaLog = new ConcurrentHashMap<>();
    static ConcurrentHashMap<Integer, String> errors = new ConcurrentHashMap<>();

    public static SchemaUpdateLog getLogForCluster(String cluster) {
        return clusterSchemaLog.computeIfAbsent(cluster, key -> {
            SchemaUpdateLog sLog = new SchemaUpdateLog();
            sLog.setCluster(cluster);
            return sLog;
        });
    }

    public static void conclude() {
        if (!errors.isEmpty()) {
            log(ConsoleColors.RED_BRIGHT, "Error occured when run scripts:");
            for (String msg : errors.values()) {
                log(ConsoleColors.RED, msg);
            }
        }

        for (String cluster : clusterSchemaLog.keySet()) {
            log(ConsoleColors.BLUE, "Cluster:", cluster);
            SchemaUpdateLog schemaUpdateLog = clusterSchemaLog.get(cluster);
            Map<Short, Map<String, List<String>>> scriptLog = schemaUpdateLog.getScriptLog();
            for (Short runMode : scriptLog.keySet()) {
                log("\t", ConsoleColors.YELLOW, "Run Mode:", ConsoleColors.GREEN, getModeName(runMode));
                for (String fileName : scriptLog.get(runMode).keySet()) {
                    log("\t\t", ConsoleColors.PURPLE, fileName, ConsoleColors.RED, String.join(", ", scriptLog.get(runMode).get(fileName)));
                }
            }
            if (!schemaUpdateLog.getHibernateSchemaList().isEmpty()) {
                log("\t", ConsoleColors.GREEN, "Schema Update Errors");
                log("\t\t", ConsoleColors.RED, String.join(", ", schemaUpdateLog.getHibernateSchemaList()));
            }
        }
        clusterSchemaLog = new ConcurrentHashMap<>();
        errors = new ConcurrentHashMap<>();
    }

    private static String getModeName(short runMode) {
        if (runMode == 1) {
            return "BEFORE";
        } else if (runMode == 2) {
            return "AFTER";
        } else if (runMode == 3) {
            return "SCHEMA";
        }
        return "UNKNOWN_MODE";
    }

    public static void log(String... appendables) {
        boolean first = true;
        if (appendables != null && appendables.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (String appendable : appendables) {
                if (!first) {
                    sb.append(" ");
                }
                sb.append(appendable);
                first = false;
            }
            sb.append(ConsoleColors.RESET);
            System.out.println(sb);
        }
    }

    public static void putIntoHibernateLog(String cluster, String schema) {
        getLogForCluster(cluster).putIntoHibernateSchemaList(schema);
    }

    public static void putIntoScriptLog(String cluster, Short runMode, String fileName, String schema) {
        getLogForCluster(cluster).putIntoScriptLog(runMode, fileName, schema);
    }

    public static void putRunScriptErrors(String fileName, String errorMsg) {
        StringBuilder builder = new StringBuilder("File name: ")
                .append(fileName)
                .append("\n")
                .append(errorMsg);

        errors.put(builder.toString().hashCode(), builder.toString());
    }
}
