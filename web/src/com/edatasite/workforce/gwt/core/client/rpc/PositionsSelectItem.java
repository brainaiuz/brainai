package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;

import java.util.Date;

/**
 * User: Admin
 * Date: 14.11.2008
 * Time: 17:09:38
 */
public class PositionsSelectItem extends SelectItem implements UserGrant {

    private Integer positionId;
    private Integer statusId;
    private String statusName;
    private String positionName = "";
    private Integer time;
    private Integer timeSpent;
    private Integer actualTime;
    private int permission;
    private Float percent;
    private Integer departmentId;
    private String departmentName;
    private Integer employeeId;//pls note that this is project employee id. So I had to add exactEmployeeId to avoid side effect
    private Integer exactEmployeeId;
    private String employeeNumber;
    private boolean isAssignee = false;
    private boolean isMyself = false;
    private boolean isNew = false;
    private SelectItem department;
    private SelectItem location;
    private String plannedPlaceCount;
    private String headCount;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private DateNonConvertable startDate;
    private DateNonConvertable endDate;
    private Boolean isDeleted;
    private Integer fullPartTime;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private ProjectPosition projectPosition;

    private String label;

    public PositionsSelectItem() {
    }

    public PositionsSelectItem(Integer id, String name, Integer positionId, String positionName) {
        super(id, name);
        this.positionId = positionId;
        if (positionName != null) {
            this.positionName = positionName;
        }
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    public boolean isAssignee() {
        return isAssignee;
    }

    public void setAssignee(boolean assignee) {
        isAssignee = assignee;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getExactEmployeeId() {
        return exactEmployeeId;
    }

    public void setExactEmployeeId(Integer exactEmployeeId) {
        this.exactEmployeeId = exactEmployeeId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        if (positionName == null) {
            return;
        }
        this.positionName = positionName;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getFullPartTime() {
        return fullPartTime;
    }

    public void setFullPartTime(Integer fullPartTime) {
        this.fullPartTime = fullPartTime;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public ProjectPosition getProjectPosition() {
        return projectPosition;
    }

    public void setProjectPosition(ProjectPosition projectPosition) {
        this.projectPosition = projectPosition;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public String getPlannedPlaceCount() {
        return plannedPlaceCount;
    }

    public void setPlannedPlaceCount(String plannedPlaceCount) {
        this.plannedPlaceCount = plannedPlaceCount;
    }

    public String getHeadCount() {
        return headCount;
    }

    public void setHeadCount(String headCount) {
        this.headCount = headCount;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}