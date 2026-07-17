package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/5/12
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class RevisionHistoryItem implements IsSerializable {
    private Integer objectID;
    private String updaterName;
    private Date updatedDate;
    private String number;

    public RevisionHistoryItem() {
    }

    public RevisionHistoryItem(Integer objectID, String updaterName, Date updatedDate, String number) {
        this.objectID = objectID;
        this.updaterName = updaterName;
        this.updatedDate = updatedDate;
        this.number = number;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
