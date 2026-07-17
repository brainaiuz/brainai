package com.edatasite.workforce.core.kafka.data;

import java.io.Serializable;

public class KafkaData<T> implements Serializable {

    private T dataMQ;
    private String sessionID;
    private String companyId;
    private String clusterType;

    public KafkaData(T dataMQ, String sessionID, String companyId, String clusterType) {
        this.dataMQ = dataMQ;
        this.sessionID = sessionID;
        this.companyId = companyId;
        this.clusterType = clusterType;
    }

    public T getDataMQ() {
        return dataMQ;
    }

    public void setDataMQ(T dataMQ) {
        this.dataMQ = dataMQ;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }
}
