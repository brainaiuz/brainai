package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.util.List;

public class ShiftTeamsItem implements Serializable {
    private SelectItem team;
    private String employeeCode;
    private SelectItem fullName;
    private String position;
    private String department;
    private String label;
    private String additionalPosition;
    private Integer leaveCount;
    private List<StatisticsLeaveRequest> leave;


    public SelectItem getTeam() {
        return team;
    }

    public void setTeam(SelectItem team) {
        this.team = team;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public SelectItem getFullName() {
        return fullName;
    }

    public void setFullName(SelectItem fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAdditionalPosition() {
        return additionalPosition;
    }

    public void setAdditionalPosition(String additionalPosition) {
        this.additionalPosition = additionalPosition;
    }

    public Integer getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(Integer leaveCount) {
        this.leaveCount = leaveCount;
    }

    public List<StatisticsLeaveRequest> getLeave() {
        return leave;
    }

    public void setLeave(List<StatisticsLeaveRequest> leave) {
        this.leave = leave;
    }
}
