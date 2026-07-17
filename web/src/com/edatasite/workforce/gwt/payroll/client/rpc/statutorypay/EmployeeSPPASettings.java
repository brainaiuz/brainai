package com.edatasite.workforce.gwt.payroll.client.rpc.statutorypay;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 20, 2010
 * Time: 6:54:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeSPPASettings implements IsSerializable {

    private Integer objectID;
    private Integer employee;
    private Date dateMatchNotified;
    private Date childExpectedDate;
    private Date childPlacedDate;
    private Date notifiedLeaveStartDate;
    private Date returnToWorkDate;
    private Date dateOfLeaving;
    private Date firstAbscence;
    private Date mwSun;

    public EmployeeSPPASettings() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEmployee() {
        return employee;
    }

    public void setEmployee(Integer employee) {
        this.employee = employee;
    }

    public Date getDateMatchNotified() {
        return dateMatchNotified;
    }

    public void setDateMatchNotified(Date dateMatchNotified) {
        this.dateMatchNotified = dateMatchNotified;
    }

    public Date getChildExpectedDate() {
        return childExpectedDate;
    }

    public void setChildExpectedDate(Date childExpectedDate) {
        this.childExpectedDate = childExpectedDate;
    }

    public Date getChildPlacedDate() {
        return childPlacedDate;
    }

    public void setChildPlacedDate(Date childPlacedDate) {
        this.childPlacedDate = childPlacedDate;
    }

    public Date getNotifiedLeaveStartDate() {
        return notifiedLeaveStartDate;
    }

    public void setNotifiedLeaveStartDate(Date notifiedLeaveStartDate) {
        this.notifiedLeaveStartDate = notifiedLeaveStartDate;
    }

    public Date getReturnToWorkDate() {
        return returnToWorkDate;
    }

    public void setReturnToWorkDate(Date returnToWorkDate) {
        this.returnToWorkDate = returnToWorkDate;
    }

    public Date getDateOfLeaving() {
        return dateOfLeaving;
    }

    public void setDateOfLeaving(Date dateOfLeaving) {
        this.dateOfLeaving = dateOfLeaving;
    }

    public Date getFirstAbscence() {
        return firstAbscence;
    }

    public void setFirstAbscence(Date firstAbscence) {
        this.firstAbscence = firstAbscence;
    }

    public Date getMwSun() {
        return mwSun;
    }

    public void setMwSun(Date mwSun) {
        this.mwSun = mwSun;
    }
}
