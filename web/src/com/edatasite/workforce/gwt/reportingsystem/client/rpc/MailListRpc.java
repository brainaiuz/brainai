package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 5/23/12
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class MailListRpc implements IsSerializable {

    private Integer objectid;
    private String name;
    private String description;
    private String mailListType;
    private boolean active;

    public void setObjectid(Integer objectid) {
        this.objectid = objectid;
    }

    public Integer getObjectid() {
        return objectid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getMailListType() {
        return mailListType;
    }

    public void setMailListType(String mailListType) {
        this.mailListType = mailListType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
