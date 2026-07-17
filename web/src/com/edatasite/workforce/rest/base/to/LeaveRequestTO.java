package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 5/13/15 8:02 PM
 */
public class LeaveRequestTO implements IsSerializable {
    Integer id;
    String description;

    Long fromDate;
    Long startHour;
    Long startMinut;

    Long toDate;
    Long endHour;
    Long endMinut;

    String duration;
    SelectItemTO type;
    Boolean typeBoolean;
    UserTO manager;
    String department;
    SelectItemTO overallStatus;
    SelectItemTO reason;
    Boolean takenFromAllowance;
    ArrayList<UserTO> approvers;
    ArrayList<SelectItemTO> approverItems;
    UserTO currentApprover;
    UserTO prevApprover;

    UserTO employee;
    String otherReason;
    ArrayList<Integer> employeeList;
    boolean pending;
    Boolean takeByMoney;

    String totalLeaveRequest;
    String totalUsedRequest;
    String totalLeftRequest;
    String totalExceededRequest;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public SelectItemTO getType() {
        return type;
    }

    public void setType(SelectItemTO type) {
        this.type = type;
    }

    public UserTO getManager() {
        return manager;
    }

    public void setManager(UserTO manager) {
        this.manager = manager;
    }

    public SelectItemTO getReason() {
        return reason;
    }

    public void setReason(SelectItemTO reason) {
        this.reason = reason;
    }

    public Boolean getTakenFromAllowance() {
        return takenFromAllowance;
    }

    public void setTakenFromAllowance(Boolean takenFromAllowance) {
        this.takenFromAllowance = takenFromAllowance;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public UserTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserTO employee) {
        this.employee = employee;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public ArrayList<Integer> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(ArrayList<Integer> employeeList) {
        this.employeeList = employeeList;
    }

    public Long getStartHour() {
        return startHour;
    }

    public void setStartHour(Long startHour) {
        this.startHour = startHour;
    }

    public Long getStartMinut() {
        return startMinut;
    }

    public void setStartMinut(Long startMinut) {
        this.startMinut = startMinut;
    }

    public Long getEndHour() {
        return endHour;
    }

    public void setEndHour(Long endHour) {
        this.endHour = endHour;
    }

    public Long getEndMinut() {
        return endMinut;
    }

    public void setEndMinut(Long endMinut) {
        this.endMinut = endMinut;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
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


    public SelectItemTO getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(SelectItemTO overallStatus) {
        this.overallStatus = overallStatus;
    }

    public ArrayList<UserTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<UserTO> approvers) {
        this.approvers = approvers;
    }

    public ArrayList<SelectItemTO> getApproverItems() {
        return approverItems;
    }

    public void setApproverItems(ArrayList<SelectItemTO> approverItems) {
        this.approverItems = approverItems;
    }

    public UserTO getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(UserTO currentApprover) {
        this.currentApprover = currentApprover;
    }

    public UserTO getPrevApprover() {
        return prevApprover;
    }

    public void setPrevApprover(UserTO prevApprover) {
        this.prevApprover = prevApprover;
    }

    public Boolean getTypeBoolean() {
        return typeBoolean;
    }

    public void setTypeBoolean(Boolean typeBoolean) {
        this.typeBoolean = typeBoolean;
    }

    public Boolean getTakeByMoney() {
        return takeByMoney;
    }

    public void setTakeByMoney(Boolean takeByMoney) {
        this.takeByMoney = takeByMoney;
    }

    public LeaveRequestTO() {
    }

    public LeaveRequestTO(LeaveRequestLisItem item) {
        this.id = item.getObjectId();
        this.description = item.getDescription();

        this.fromDate = WrapUtils.dateToLong(item.getStartDate().getNonConvertedDate());
        this.startHour = (long) item.getStartDate().getNonConvertedDate().getHours();
        this.startMinut = (long) item.getStartDate().getNonConvertedDate().getMinutes();

        this.toDate = WrapUtils.dateToLong(item.getEndDate().getNonConvertedDate());
        this.endHour = (long) item.getEndDate().getNonConvertedDate().getHours();
        this.endMinut = (long) item.getEndDate().getNonConvertedDate().getMinutes();

        if (item.getStatusID() != null) {
            this.overallStatus = new SelectItemTO(item.getStatusID(), item.getStatus());
        }
        this.employee = new UserTO(item.getEmployeeId(), item.getEmployeeName());
        this.reason = new SelectItemTO(item.getReasonID(), item.getReason());
        this.duration = item.getPaid();

    }

    public LeaveRequestTO(LeaveRequestLisItem item, boolean isBriefly) {
        this(item);
        this.takenFromAllowance = false;
        this.typeBoolean = false;
        this.pending = item.isPending();
    }
}
