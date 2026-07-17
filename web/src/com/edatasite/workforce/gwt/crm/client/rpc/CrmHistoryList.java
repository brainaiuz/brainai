package com.edatasite.workforce.gwt.crm.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: May 1, 2010
 * Time: 6:56:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmHistoryList implements IsSerializable {

    private Integer objectID;
    private Integer updaterID;
    private String updater;
    private String message;
    private Date creationTime;
    private String updaterImageURL;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getUpdaterID() {
        return updaterID;
    }

    public void setUpdaterID(Integer updaterID) {
        this.updaterID = updaterID;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getUpdaterImageURL() {
        return updaterImageURL;
    }

    public void setUpdaterImageURL(String updaterImageURL) {
        this.updaterImageURL = updaterImageURL;
    }
}
