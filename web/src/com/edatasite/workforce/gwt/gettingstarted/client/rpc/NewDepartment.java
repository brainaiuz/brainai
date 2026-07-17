package com.edatasite.workforce.gwt.gettingstarted.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class NewDepartment implements IsSerializable {

    private Integer objectId;
    private String name;
    private Integer[] membersId;
    private Integer leaderId;
    private String description;
    private Date startDate;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer[] getMembersId() {
        return membersId;
    }

    public void setMembersId(Integer[] membersId) {
        this.membersId = membersId;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
