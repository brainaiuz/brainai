package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 24.02.2010
 * Time: 15:51:36
 * To change this template use File | Settings | File Templates.
 */
public class WorkspaceHomeUnavailableEmployeesRpc implements IsSerializable {

    private Integer objectID;
    private Integer employeeID;
    private String employeeName;
    private long fromDate;
    private long toDate;
    private String fromSDate;
    private String toSDate;
    private String employeePhotoURL;
    private String leaveType;

    private Boolean isLinkable;

    public Boolean getLinkable() {
        return isLinkable;
    }

    public void setLinkable(Boolean linkable) {
        isLinkable = linkable;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public long getFromDate() {
        return fromDate;
    }

    public void setFromDate(long fromDate) {
        this.fromDate = fromDate;
    }

    public long getToDate() {
        return toDate;
    }

    public void setToDate(long toDate) {
        this.toDate = toDate;
    }

    public String getEmployeePhotoURL() {
        return employeePhotoURL;
    }

    public void setEmployeePhotoURL(String employeePhotoURL) {
        this.employeePhotoURL = employeePhotoURL;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getFromSDate() {
        /*if (fromSDate.length() > 16)
            return fromSDate.substring(0, 16);*/
        return fromSDate;
    }

    public void setFromSDate(String fromSDate) {
        this.fromSDate = fromSDate;
    }

    public String getToSDate() {
        /*if (toSDate.length() > 16)
            return toSDate.substring(0, 16);*/
        return toSDate;
    }

    public void setToSDate(String toSDate) {
        this.toSDate = toSDate;
    }
}
