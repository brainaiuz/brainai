package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 17.08.2009
 * Time: 9:28:02
 * To change this template use File | Settings | File Templates.
 */
public class UserSessionHistoryItem implements IsSerializable {
    private String userSessionID;
    private String lastAccessDate;
    private String accessedSectionName;
    private String moduleLoadedTime;

    public String getUserSessionID() {
        return userSessionID;
    }

    public void setUserSessionID(String userSessionID) {
        this.userSessionID = userSessionID;
    }

    public String getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(String lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String getAccessedSectionName() {
        return accessedSectionName;
    }

    public void setAccessedSectionName(String accessedSectionName) {
        this.accessedSectionName = accessedSectionName;
    }

    public String getModuleLoadedTime() {
        return moduleLoadedTime;
    }

    public void setModuleLoadedTime(String moduleLoadedTime) {
        this.moduleLoadedTime = moduleLoadedTime;
    }
}
