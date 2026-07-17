package com.edatasite.workforce.gwt.core.client.rpc.task;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TaskInvolvedMember implements IsSerializable {

    private Integer employeeTaskID;
    private Integer employeeID;
    private Integer employeeTeamID;
    private Integer assignEmployeeID;
    private String employeeTeam;
    private String employee;
    private Integer totalTimeSpent;
    private Integer estimatedTime;
    private Integer actualTime;
    private Integer statusID;
    private String statusName;
    private Float percent;
    private Integer timeSpent;
    private String estimateTimeInString;
    private String employeeNumber;
    private Integer exactEmployeeID;

    public TaskInvolvedMember() {

    }

    public TaskInvolvedMember(Integer employeeTaskID, Integer employeeID, String employee, Long totalTimeSpent) {
        this.employeeTaskID = employeeTaskID;
        this.employeeID = employeeID;
        this.employee = employee;
        this.totalTimeSpent = totalTimeSpent.intValue();
    }

    public TaskInvolvedMember(Integer employeeTaskID, Integer employeeTeamID, Integer employeeID,
                              String employeeTeam, String employee, Long totalTimeSpent) {
        this.employeeTaskID = employeeTaskID;
        this.employeeID = employeeID;
        this.employeeTeamID = employeeTeamID;
        this.employeeTeam = employeeTeam;
        this.employee = employee;
        this.totalTimeSpent = totalTimeSpent.intValue();
    }


    public TaskInvolvedMember(Integer employeeTaskID, Integer employeeTeamID, Integer employeeID,
                              String employeeTeam, String employee) {
        this.employeeTaskID = employeeTaskID;
        this.employeeID = employeeID;
        this.employeeTeamID = employeeTeamID;
        this.employeeTeam = employeeTeam;
        this.employee = employee;
    }

    public TaskInvolvedMember(Integer employeeTaskID, Integer employeeTeamID, Integer employeeID,
                              String employeeTeam, String employee, Integer estimatedTime) {
        this.employeeTaskID = employeeTaskID;
        this.employeeID = employeeID;
        this.employeeTeamID = employeeTeamID;
        this.employeeTeam = employeeTeam;
        this.employee = employee;
        this.estimatedTime = estimatedTime;
    }

    public TaskInvolvedMember(Integer employeeTaskID, Integer employeeID, Integer employeeTeamID,
                              String employeeTeam, String employee, Integer estimatedTime,
                              Integer statusID, String statusName, Float percent, Integer actualTime) {
        this.employeeTaskID = employeeTaskID;
        this.employeeTeamID = employeeTeamID;
        this.employeeID = employeeID;
        this.employeeTeam = employeeTeam;
        this.employee = employee;
        this.estimatedTime = estimatedTime;
        this.statusID = statusID;
        this.statusName = statusName;
        this.percent = percent;
        this.actualTime = actualTime;
    }

    /**
     * This constructor is used in constructor query TimeSheetManagerImpl.getSumTimeSheets()
     *
     * @param employeeTaskID
     * @param employeeID
     * @param employeeTeamID
     * @param employeeTeam
     * @param employee
     * @param estimatedTime
     * @param statusID
     * @param statusName
     * @param percent
     * @param actualTime
     * @param assignEmployeeID
     * @param employeeNumber
     */
    public TaskInvolvedMember(Integer employeeTaskID, Integer employeeID, Integer employeeTeamID,
                              String employeeTeam, String employee, Integer estimatedTime,
                              Integer statusID, String statusName, Float percent, Integer actualTime, Integer assignEmployeeID, String employeeNumber, Integer exactEmployeeID) {
        this.employeeTaskID = employeeTaskID;
        this.employeeTeamID = employeeTeamID;
        this.employeeID = employeeID;//pls note that this not employee id , this is project employee id. So I had to add exactEmployeeID to retrieve employee
        this.employeeTeam = employeeTeam;
        this.employee = employee;
        this.estimatedTime = estimatedTime;
        this.statusID = statusID;
        this.statusName = statusName;
        this.percent = percent;
        this.actualTime = actualTime;
        this.assignEmployeeID = assignEmployeeID;
        this.employeeNumber = employeeNumber;
        this.exactEmployeeID = exactEmployeeID;
    }

    public TaskInvolvedMember(String employee, String statusName, Integer estimatedTime, Float percent, Integer timeSpent) {
        this.employee = employee;
        this.statusName = statusName;
        this.estimatedTime = estimatedTime;
        this.percent = percent;
        this.timeSpent = timeSpent;
    }

    public Integer getEmployeeTaskID() {
        return employeeTaskID;
    }

    public void setEmployeeTaskID(Integer employeeTaskID) {
        this.employeeTaskID = employeeTaskID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeTeam() {
        return employeeTeam;
    }

    public void setEmployeeTeam(String employeeTeam) {
        this.employeeTeam = employeeTeam;
    }

    public Integer getEmployeeTeamID() {
        return employeeTeamID;
    }

    public void setEmployeeTeamID(Integer employeeTeamID) {
        this.employeeTeamID = employeeTeamID;
    }

    public String getEmployee() {
        if (employee == null) {
            return " ";
        } else {
            return employee;
        }
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Integer getTotalTimeSpent() {
        if (totalTimeSpent == null) {
            return 0;
        } else {
            return totalTimeSpent;
        }
    }

    public void setTotalTimeSpent(Integer totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    public Integer getEstimatedTime() {
        if (estimatedTime == null) {
            return 0;
        } else {
            return estimatedTime;
        }
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getActualTime() {
        if (actualTime == null) {
            return 0;
        } else {
            return actualTime;
        }
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Float getPercent() {
        if (percent == null) {
            Integer i = 0;
            percent = i.floatValue();
            return percent;
        } else {
            return percent;
        }
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getTimeSpent() {
        if (timeSpent == null) {
            return 0;
        }
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getAssignEmployeeID() {
        return assignEmployeeID;
    }

    public void setAssignEmployeeID(Integer assignEmployeeID) {
        this.assignEmployeeID = assignEmployeeID;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Integer getExactEmployeeID() {
        return exactEmployeeID;
    }

    public void setExactEmployeeID(Integer exactEmployeeID) {
        this.exactEmployeeID = exactEmployeeID;
    }

    public String getEstimateTimeInString() {
        return estimateTimeInString;
    }

    public void setEstimateTimeInString(String estimateTimeInString) {
        this.estimateTimeInString = estimateTimeInString;
    }
}
