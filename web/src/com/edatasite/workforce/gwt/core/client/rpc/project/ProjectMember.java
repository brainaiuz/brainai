package com.edatasite.workforce.gwt.core.client.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.UserGrant;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class ProjectMember implements IsSerializable, UserGrant {
    //EmployeeId
    private Integer id;
    private Integer projectEmployeeId;
    private Integer departmentId;
    private Integer projectId;
    private String name;
    private String employeeNumber;
    private String teamName;
    private Double wageRate;
    private String posititon;
    private Double clientChargeRate;
    private Float workloadPercentage;
    private Integer defaulDepartmentId;
    private boolean check;
    private int permission;
    private boolean isDeleted;
    private Integer estimatedTime;
    private Integer timeSpent;
    private Integer actualTime;

    private Integer positionId;
    private DateNonConvertable contractStart;
    private DateNonConvertable contractEnd;
    private Date createDate;
    private Integer taskCount;
    private Float percentSum;
    private String unit;

    public ProjectMember() {

    }

    public Integer getProjectEmployeeId() {
        return projectEmployeeId;
    }

    public void setProjectEmployeeId(Integer projectEmployeeId) {
        this.projectEmployeeId = projectEmployeeId;
    }

    public Integer getDefaulDepartmentId() {
        return defaulDepartmentId;
    }

    public void setDefaulDepartmentId(Integer defaulDepartmentId) {
        this.defaulDepartmentId = defaulDepartmentId;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public int getPermission() {
        return permission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ProjectMember(Integer id, String name, String teamName) {
        this.id = id;
        this.name = name;
        this.teamName = teamName;
    }

    public ProjectMember(Integer id, Integer departmentId, String name, String teamName) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
        this.teamName = teamName;
    }

    public ProjectMember(Integer id, Integer departmentId, String name,
                         String teamName, boolean check) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
        this.teamName = teamName;
        this.check = check;
    }

    public int hashCode() {
        return getId();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ProjectMember)) {
            return false;
        }
        return getId().equals(((ProjectMember) obj).getId());
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public Float getWorkloadPercentage() {
        return workloadPercentage;
    }

    public void setWorkloadPercentage(Float workloadPercentage) {
        this.workloadPercentage = workloadPercentage;
    }

    public String getPosititon() {
        return posititon;
    }

    public void setPosititon(String posititon) {
        this.posititon = posititon;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public DateNonConvertable getContractStart() {
        return contractStart;
    }

    public void setContractStart(DateNonConvertable contractStart) {
        this.contractStart = contractStart;
    }

    public DateNonConvertable getContractEnd() {
        return contractEnd;
    }

    public void setContractEnd(DateNonConvertable contractEnd) {
        this.contractEnd = contractEnd;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setPercentSum(Float percentSum) {
        this.percentSum = percentSum;
    }

    public Float getPercentSum() {
        return percentSum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}