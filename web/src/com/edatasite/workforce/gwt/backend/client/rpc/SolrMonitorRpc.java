package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/03/12
 * Time: 19:42
 * To change this template use File | Settings | File Templates.
 */
public class SolrMonitorRpc implements IsSerializable {

    public static final String ACTION = "action";
    public static final String CORE_NAME = "coreName";
    public static final String NUM_DOCS = "numDocs";
    public static final String CORE_SIZE = "coreSize";
    public static final String START_TIME = "startTime";
    public static final String LAST_MODIFIED = "lastModified";

    private int numDocs;
    private int usesUsersCount;
    private String fileSize;
    private int coreId;
    private String coreName;
    private Date startTime;
    private Date lastModified;

    public int getNumDocs() {
        return numDocs;
    }

    public void setNumDocs(int numDocs) {
        this.numDocs = numDocs;
    }

    public int getUsesUsersCount() {
        return usesUsersCount;
    }

    public void setUsesUsersCount(int usesUsersCount) {
        this.usesUsersCount = usesUsersCount;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getCoreId() {
        return coreId;
    }

    public void setCoreId(int coreId) {
        this.coreId = coreId;
    }

    public String getCoreName() {
        return coreName;
    }

    public void setCoreName(String coreName) {
        this.coreName = coreName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
