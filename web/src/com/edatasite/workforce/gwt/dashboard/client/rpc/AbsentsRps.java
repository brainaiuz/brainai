package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 09.11.2009
 * Time: 15:53:49
 * To change this template use File | Settings | File Templates.
 */
public class AbsentsRps implements IsSerializable {
    private String name;
    private long allWorkDay;
    private long leaveRequestDay;
    private int approved;
    private int notapproved;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getNotapproved() {
        return notapproved;
    }

    public void setNotapproved(int notapproved) {
        this.notapproved = notapproved;
    }

    public long getAllWorkDay() {
        return allWorkDay;
    }

    public void setAllWorkDay(long allWorkDay) {
        this.allWorkDay = allWorkDay;
    }

    public long getLeaveRequestDay() {
        return leaveRequestDay;
    }

    public void setLeaveRequestDay(long leaveRequestDay) {
        this.leaveRequestDay = leaveRequestDay;
    }
}
