package com.edatasite.workforce.gwt.payroll.client.rpc.statutorypay;

import com.edatasite.workforce.gwt.payroll.client.rpc.DateRange;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 5, 2010
 * Time: 8:07:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeSMPSettings implements IsSerializable {

    private Integer objectID;
    private Integer employee;
    private Date babyDueDate;
    private Date babyBornDate;
    private Boolean stillBornBaby;
    private Boolean medicalEvidence;
    private Date notifiedLeaveStartDate;
    private Date pregnancyRelatedIllnessStartDate;
    private Date returnToWorkDate;
    private Double maternityPaidAllowanceAmount;
    private Date dateOfLeaving;
    private String reason;
    private Date qwSun;
    private DateRange relevantPeriodForAvgEarnings;

    public EmployeeSMPSettings() {
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

    public Boolean isMedicalEvidence() {
        return medicalEvidence;
    }

    public Boolean getMedicalEvidence() {
        return medicalEvidence;
    }

    public void setMedicalEvidence(Boolean medicalEvidence) {
        this.medicalEvidence = medicalEvidence;
    }

    public Date getNotifiedLeaveStartDate() {
        return notifiedLeaveStartDate;
    }

    public void setNotifiedLeaveStartDate(Date notifiedLeaveStartDate) {
        this.notifiedLeaveStartDate = notifiedLeaveStartDate;
    }

    public Date getPregnancyRelatedIllnessStartDate() {
        return pregnancyRelatedIllnessStartDate;
    }

    public void setPregnancyRelatedIllnessStartDate(Date pregnancyRelatedIllnessStartDate) {
        this.pregnancyRelatedIllnessStartDate = pregnancyRelatedIllnessStartDate;
    }

    public Date getReturnToWorkDate() {
        return returnToWorkDate;
    }

    public void setReturnToWorkDate(Date returnToWorkDate) {
        this.returnToWorkDate = returnToWorkDate;
    }

    public Double getMaternityPaidAllowanceAmount() {
        return maternityPaidAllowanceAmount;
    }

    public void setMaternityPaidAllowanceAmount(Double maternityPaidAllowanceAmount) {
        this.maternityPaidAllowanceAmount = maternityPaidAllowanceAmount;
    }

    public Date getDateOfLeaving() {
        return dateOfLeaving;
    }

    public void setDateOfLeaving(Date dateOfLeaving) {
        this.dateOfLeaving = dateOfLeaving;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getQwSun() {
        return qwSun;
    }

    public void setQwSun(Date qwSun) {
        this.qwSun = qwSun;
    }

    public DateRange getRelevantPeriodForAvgEarnings() {
        return relevantPeriodForAvgEarnings;
    }

    public void setRelevantPeriodForAvgEarnings(DateRange relevantPeriodForAvgEarnings) {
        this.relevantPeriodForAvgEarnings = relevantPeriodForAvgEarnings;
    }
}
