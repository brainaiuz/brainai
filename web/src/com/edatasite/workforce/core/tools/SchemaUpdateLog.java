package com.edatasite.workforce.core.tools;

import java.util.*;

public class SchemaUpdateLog {
    private String cluster;
    private SortedSet<String> hibernateSchemaList = new TreeSet<>();
    private Map<Short, Map<String, List<String>>> scriptLog = new HashMap<>(); // Map<runMode, Map<sqlFile, List<schemaList>>>

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public Map<Short, Map<String, List<String>>> getScriptLog() {
        return scriptLog;
    }

    public void setScriptLog(Map<Short, Map<String, List<String>>> scriptLog) {
        this.scriptLog = scriptLog;
    }

    public SortedSet<String> getHibernateSchemaList() {
        return hibernateSchemaList;
    }

    public void setHibernateSchemaList(SortedSet<String> hibernateSchemaList) {
        this.hibernateSchemaList = hibernateSchemaList;
    }

    private Map<String, List<String>> getPatchSchemaListForKey(short runMode) {
        return scriptLog.computeIfAbsent(runMode, key -> new HashMap<>());
    }

    public void putIntoHibernateSchemaList(String schema) {
        hibernateSchemaList.add(schema);
    }

    public void putIntoScriptLog(Short runMode, String fileName, String schema) {
        getPatchSchemaListForKey(runMode)
                .computeIfAbsent(fileName, k -> new ArrayList<>())
                .add(schema);
    }

}
