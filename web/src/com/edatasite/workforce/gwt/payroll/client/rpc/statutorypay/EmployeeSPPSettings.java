package com.edatasite.workforce.gwt.payroll.client.rpc.statutorypay;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 10, 2010
 * Time: 8:50:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeSPPSettings implements IsSerializable {

    private Integer objectID;
    private Integer employee;
    private Date babyDueDate;
    private Date babyBornDate;
    private Boolean stillBornBaby;
    private Date notifiedLeaveStartDate;
    private Date returnToWorkDate;
    private Date dateOfLeaving;
    private Date firstAbscence;
    private Date mwSun;

    public EmployeeSPPSettings() {
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

    public Date getBabyDueDate() {
        return babyDueDate;
    }

    public void setBabyDueDate(Date babyDueDate) {
        this.babyDueDate = babyDueDate;
    }

    public Date getBabyBornDate() {
        return babyBornDate;
    }

    public void setBabyBornDate(Date babyBornDate) {
        this.babyBornDate = babyBornDate;
    }

    public Boolean isStillBornBaby() {
        return stillBornBaby;
    }

    public Boolean getStillBornBaby() {
        return stillBornBaby;
    }

    public void setStillBornBaby(Boolean stillBornBaby) {
        this.stillBornBaby = stillBornBaby;
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
