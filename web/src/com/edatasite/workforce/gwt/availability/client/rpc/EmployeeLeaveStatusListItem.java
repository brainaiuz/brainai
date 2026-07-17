package com.edatasite.workforce.gwt.availability.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: Oct 27, 2009
 * Time: 9:35:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeLeaveStatusListItem implements IsSerializable {

    public static final String ACTION = "Action";
    public static final String EMPLOYEE_NAME = "employeeName";
    public static final String MANAGER_NAME = "managerName";
    public static final String DEPARTMENT_NAME = "departmentName";
    public static final String APPROVED_STATUS = "approvedStatus";
    public static final String DENIED_STATUS = "deniedStatus";
    public static final String PENDING_STATUS = "pendingStatus";

    public static final String ANNUAL_LEAVE_ALLOWANCE_DAYS = "annualLeaveAllowanceDays";
    public static final String TAKEN_STATUTORY_LEAVE_DAYS = "takenStatutoryLeaveDays";
    public static final String LEFT_STATUTORY_LEAVE_DAYS = "leftStatutoryLeaveDays";

    private Integer employeeID;
    private String employeeName;
    private String managerName;
    private String departmentName;
    private String approvedLRequest;
    private String deniedLRequest;
    private String pendingLRequest;

    private String totalLeaveRequest;
    private String totalUsedRequest;
    private String totalPendingRequest;
    private String totalLeftRequest;
    private String totalExceededRequest;
    private String currentPaidDays;
    private String currentNonPaidDays;

    private String qtyType;
    private Integer reasonID;
    private boolean isProrataBased;

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

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getApprovedLRequest() {
        return approvedLRequest;
    }

    public void setApprovedLRequest(String approvedLRequest) {
        this.approvedLRequest = approvedLRequest;
    }

    public String getDeniedLRequest() {
        return deniedLRequest;
    }

    public void setDeniedLRequest(String deniedLRequest) {
        this.deniedLRequest = deniedLRequest;
    }

    public String getPendingLRequest() {
        return pendingLRequest;
    }

    public void setPendingLRequest(String pendingLRequest) {
        this.pendingLRequest = pendingLRequest;
    }

    public String getTotalLeaveRequest() {
        return totalLeaveRequest;
    }

    public void setTotalLeaveRequest(String totalLeaveRequest) {
        this.totalLeaveRequest = totalLeaveRequest;
    }

    public String getTotalUsedRequest() {
        return totalUsedRequest;
    }

    public void setTotalUsedRequest(String totalUsedRequest) {
        this.totalUsedRequest = totalUsedRequest;
    }

    public String getTotalPendingRequest() {
        return totalPendingRequest;
    }

    public void setTotalPendingRequest(String totalPendingRequest) {
        this.totalPendingRequest = totalPendingRequest;
    }

    public String getTotalLeftRequest() {
        return totalLeftRequest;
    }

    public void setTotalLeftRequest(String totalLeftRequest) {
        this.totalLeftRequest = totalLeftRequest;
    }

    public String getTotalExceededRequest() {
        return totalExceededRequest;
    }

    public void setTotalExceededRequest(String totalExceededRequest) {
        this.totalExceededRequest = totalExceededRequest;
    }

    public String getCurrentPaidDays() {
        return currentPaidDays;
    }

    public void setCurrentPaidDays(String currentPaidDays) {
        this.currentPaidDays = currentPaidDays;
    }

    public String getCurrentNonPaidDays() {
        return currentNonPaidDays;
    }

    public void setCurrentNonPaidDays(String currentNonPaidDays) {
        this.currentNonPaidDays = currentNonPaidDays;
    }

    public boolean isProrataBased() {
        return isProrataBased;
    }

    public void setProrataBased(boolean prorataBased) {
        isProrataBased = prorataBased;
    }

    public String getQtyType() {
        return qtyType;
    }

    public void setQtyType(String qtyType) {
        this.qtyType = qtyType;
    }

    public void setReasonID(Integer reasonID) {
        this.reasonID = reasonID;
    }

    public Integer getReasonID() {
        return reasonID;
    }
}
