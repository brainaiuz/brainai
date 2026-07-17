package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class GroupGoalITem extends HasApprovers implements IsSerializable {

    public static final String GROUP_GOAL_EMPLOYEE = "employee";
    public static final String GROUP_GOAL_STATUS = "status";
    public static final String GROUP_GOAL_APPROVER = "approver";
    public static final String VALIDITY_PERIOD = "validityPeriod";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    public static final String ACTION = "action";

    private Integer objectId;
    private SelectItem employee;
    private SelectItem status;
    private SelectItem approver;
    private boolean selfApprover;
    private ValidityPeriodItem validityPeriod;
    private DateNonConvertable fromDate;
    private DateNonConvertable toDate;
    private ArrayList<GoalItem> goalItems;


    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public ArrayList<GoalItem> getGoalItems() {
        return goalItems == null ? new ArrayList<>() : goalItems;
    }

    public void setGoalItems(ArrayList<GoalItem> goalItems) {
        this.goalItems = goalItems;
    }

    public boolean isSelfApprover() {
        return selfApprover;
    }

    public void setSelfApprover(boolean selfApprover) {
        this.selfApprover = selfApprover;
    }

    public ValidityPeriodItem getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(ValidityPeriodItem validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public DateNonConvertable getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateNonConvertable fromDate) {
        this.fromDate = fromDate;
    }

    public DateNonConvertable getToDate() {
        return toDate;
    }

    public void setToDate(DateNonConvertable toDate) {
        this.toDate = toDate;
    }
}
