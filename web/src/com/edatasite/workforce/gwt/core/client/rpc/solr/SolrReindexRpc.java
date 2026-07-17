package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 14/04/12
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class SolrReindexRpc implements IsSerializable{

    private Integer companyId;
    private Date lastUpdateTime;
    private Date lastUpdateEndTime;
    private boolean allReindex = true;
    private String formID;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUpdateEndTime() {
        return lastUpdateEndTime;
    }

    public void setLastUpdateEndTime(Date lastUpdateEndTime) {
        this.lastUpdateEndTime = lastUpdateEndTime;
    }

    public boolean isAllReindex() {
        return allReindex;
    }

    public void setAllReindex(boolean allReindex) {
        this.allReindex = allReindex;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }
}
