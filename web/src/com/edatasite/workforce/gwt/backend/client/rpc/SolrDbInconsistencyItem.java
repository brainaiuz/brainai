package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.Key;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Abdulaziz
 * Date: Oct 19, 2010
 * Time: 3:21:42 PM
 */
public class SolrDbInconsistencyItem implements IsSerializable, Key {
    private String entityName;
    private String entityType;
    private String status;
    private Integer entityID;
    private Date statisticDate;
    private boolean fixed;
    private Integer companyID;
    private String companyName;
    private String lessInSolr;
    private String moreInSolr;

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Date getStatisticDate() {
        return statisticDate;
    }

    public void setStatisticDate(Date statisticDate) {
        this.statisticDate = statisticDate;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLessInSolr() {
        return lessInSolr;
    }

    public void setLessInSolr(String lessInSolr) {
        this.lessInSolr = lessInSolr;
    }

    public String getMoreInSolr() {
        return moreInSolr;
    }

    public void setMoreInSolr(String moreInSolr) {
        this.moreInSolr = moreInSolr;
    }

    @Override
    public String getKey() {
        return getEntityID() + "_" + getEntityType();
    }
}
