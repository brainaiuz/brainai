package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 11.08.2010
 * Time: 16:57:23
 * To change this template use File | Settings | File Templates.
 */
public class WFTPlaginListItem implements IsSerializable {

    public static final String OBJECT_ID = "objectID";
    public static final String PLUGIN_NAME = "plagin";
    public static final String VERSION = "version";
    public static final String UPDATE_DATE = "date";
    public static final String UPDATER_NAME = "updater";
    private Integer objectID;
    private String plaginName;
    private String plaginVersion;
    private Date updateDate;
    private String updaterName;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getPlaginName() {
        return plaginName;
    }

    public void setPlaginName(String plaginName) {
        this.plaginName = plaginName;
    }

    public String getPlaginVersion() {
        return plaginVersion;
    }

    public void setPlaginVersion(String plaginVersion) {
        this.plaginVersion = plaginVersion;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }
}
