package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: May 29, 2009
 * Time: 5:02:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class BugsPerEmployeesListItem implements IsSerializable {

    public static final String EMPLOYEE = "employee";
    public static final String SECTION = "section";
    public static final String STATUS_NEW = "neww";
    public static final String STATUS_RESOLVED = "resolved";
    public static final String STATUS_UNDER_INVESTIGATION = "under_invest";
    public static final String STATUS_IN_PROGRESS = "in_progress";
    public static final String STATUS_IGNORED = "ignored";
    public static final String STATUS_DONE = "done";
    public static final String TOTAL_BUG = "total";


    private String employee;
    private String section;
    private String bugNew;
    //status
    private Integer statusNew;
    private Integer resolved;
    private Integer underInvest;
    private Integer inProgress;
    private Integer ignored;
    private Integer done;
    private Integer total;

    private Integer objectID;
    private Integer newStatusID;
    private String newStatusName;

    public BugsPerEmployeesListItem() {
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBugNew() {
        return bugNew;
    }

    public void setBugNew(String bugNew) {
        this.bugNew = bugNew;
    }

    public Integer getStatusNew() {
        return statusNew;
    }

    public void setStatusNew(Integer statusNew) {
        this.statusNew = statusNew;
    }

    public Integer getResolved() {
        return resolved;
    }

    public void setResolved(Integer resolved) {
        this.resolved = resolved;
    }

    public Integer getUnderInvest() {
        return underInvest;
    }

    public void setUnderInvest(Integer underInvest) {
        this.underInvest = underInvest;
    }

    public Integer getInProgress() {
        return inProgress;
    }

    public void setInProgress(Integer inProgress) {
        this.inProgress = inProgress;
    }

    public Integer getIgnored() {
        return ignored;
    }

    public void setIgnored(Integer ignored) {
        this.ignored = ignored;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getNewStatusID() {
        return newStatusID;
    }

    public void setNewStatusID(Integer newStatusID) {
        this.newStatusID = newStatusID;
    }

    public String getNewStatusName() {
        return newStatusName;
    }

    public void setNewStatusName(String newStatusName) {
        this.newStatusName = newStatusName;
    }
}
