package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Faxriddin on 4/27/15.
 */
public class AnnualLeaveItem implements IsSerializable {

    public static final String EMPLOYEE_NAME = "EMPLOYEE_NAME";
    public static final String DEPARTMENT_NAME = "DEPARTMENT_NAME";

    private Integer objectID;
    private Double annualallowancedays;
    private Integer allowanceYear;
    private Boolean addPrevious;
    private Integer positionId;
    private Integer reasonId;
    private String reasonName;
    private String reasonCode;

    private Double lastAllowanceDays;
    private Integer employeeId;
    private String employeeName;
    private String departmentName;
    private String benefitType;
    private HashMap<Integer, Double> allowanceByBenefit;
    private ArrayList<LaborPeriodRequest> requestList;

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public ArrayList<LaborPeriodRequest> getRequestList() {
        return requestList;
    }

    public void setRequestList(ArrayList<LaborPeriodRequest> requestList) {
        this.requestList = requestList;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Double getAnnualallowancedays() {
        if (annualallowancedays == null) {
            return 0d;
        }
        return annualallowancedays;
    }

    public void setAnnualallowancedays(Double annualallowancedays) {
        this.annualallowancedays = annualallowancedays;
    }

    public Integer getAllowanceYear() {
        return allowanceYear;
    }

    public void setAllowanceYear(Integer allowanceYear) {
        this.allowanceYear = allowanceYear;
    }

    public Boolean getAddPrevious() {
        return addPrevious;
    }

    public void setAddPrevious(Boolean addPrevious) {
        this.addPrevious = addPrevious;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getBenefitType() {
        return benefitType;
    }

    public void setBenefitType(String benefitType) {
        this.benefitType = benefitType;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public HashMap<Integer, Double> getAllowanceByBenefit() {
        return allowanceByBenefit;
    }

    public void setAllowanceByBenefit(HashMap<Integer, Double> allowanceByBenefit) {
        this.allowanceByBenefit = allowanceByBenefit;
    }

    public Double getLastAllowanceDays() {
        return lastAllowanceDays;
    }

    public void setLastAllowanceDays(Double lastAllowanceDays) {
        this.lastAllowanceDays = lastAllowanceDays;
    }
}
