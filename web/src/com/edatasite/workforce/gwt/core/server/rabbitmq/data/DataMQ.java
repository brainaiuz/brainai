package com.edatasite.workforce.gwt.core.server.rabbitmq.data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 25/10/12
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
public class DataMQ<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6884113105287726167L;

    private T dataMQ;
    private Integer userId;
    private Integer companyId;
    private String clusterType;

    public DataMQ(){}

    public DataMQ(T dataMQ, Integer companyId, String clusterType) {
        this.dataMQ = dataMQ;
        this.companyId = companyId;
        this.clusterType = clusterType;
    }

    public DataMQ(T dataMQ, Integer userId, Integer companyId, String clusterType) {
        this.dataMQ = dataMQ;
        this.userId = userId;
        this.companyId = companyId;
        this.clusterType = clusterType;
    }

    public T getDataMQ() {
        return dataMQ;
    }

    public void setDataMQ(T dataMQ) {
        this.dataMQ = dataMQ;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public String toString() {
        if (companyId != null && clusterType != null) {
            return companyId + clusterType;
        }
        return super.toString();
    }
}
