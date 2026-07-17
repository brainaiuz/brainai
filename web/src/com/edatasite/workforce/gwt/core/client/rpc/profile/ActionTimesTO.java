package com.edatasite.workforce.gwt.core.client.rpc.profile;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Faxriddin on 8/7/15.
 */
public class ActionTimesTO implements IsSerializable {

    private String actiontype;
    private String actionNumber;
    private String actionPeriod;

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(String actionNumber) {
        this.actionNumber = actionNumber;
    }

    public String getActionPeriod() {
        return actionPeriod;
    }

    public void setActionPeriod(String actionPeriod) {
        this.actionPeriod = actionPeriod;
    }
}
