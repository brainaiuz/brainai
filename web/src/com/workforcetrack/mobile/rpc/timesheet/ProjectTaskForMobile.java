package com.workforcetrack.mobile.rpc.timesheet;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 1, 2010
 * Time: 4:04:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTaskForMobile implements IsSerializable {

    private String name;
    private Integer timesheetID;
    private Integer employeeTaskID;
    private Integer dailyWorkedMinutes;
    private Integer totalWorkedMinutes;
    private Date date;
    private boolean editable = true;
    private int status;
    private Integer oldEmployeeTaskID;
    private String comment;
    private Integer taskStatusID;
    private List<Integer> oldEmployeeTaskIDList = new ArrayList<>();
    private MNumberData numberData;
    private Date startDate;
    private Date endDate;
    private float percentCompleted;

    public ProjectTaskForMobile() {

    }

    public ProjectTaskForMobile(Integer timesheetID, Integer employeeTaskID, String name, Integer taskStatusID) {
        this.name = name;
        this.timesheetID = timesheetID;
        this.employeeTaskID = employeeTaskID;
        this.taskStatusID = taskStatusID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimesheetID() {
        return timesheetID;
    }

    public void setTimesheetID(Integer timesheetID) {
        this.timesheetID = timesheetID;
    }

    public Integer getEmployeeTaskID() {
        return employeeTaskID;
    }

    public void setEmployeeTaskID(Integer employeeTaskID) {
        this.employeeTaskID = employeeTaskID;
    }

    public Integer getDailyWorkedMinutes() {
        return dailyWorkedMinutes;
    }

    public void setDailyWorkedMinutes(Integer dailyWorkedMinutes) {
        this.dailyWorkedMinutes = dailyWorkedMinutes;
    }

    public Integer getTotalWorkedMinutes() {
        return totalWorkedMinutes;
    }

    public void setTotalWorkedMinutes(Integer totalWorkedMinutes) {
        this.totalWorkedMinutes = totalWorkedMinutes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getOldEmployeeTaskID() {
        return oldEmployeeTaskID;
    }

    public void setOldEmployeeTaskID(Integer oldEmployeeTaskID) {
        this.oldEmployeeTaskID = oldEmployeeTaskID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getTaskStatusID() {
        return taskStatusID;
    }

    public void setTaskStatusID(Integer taskStatusID) {
        this.taskStatusID = taskStatusID;
    }

    public List<Integer> getOldEmployeeTaskIDList() {
        return oldEmployeeTaskIDList;
    }

    public void setOldEmployeeTaskIDList(List<Integer> oldEmployeeTaskIDList) {
        this.oldEmployeeTaskIDList = oldEmployeeTaskIDList;
    }

    public MNumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(MNumberData numberData) {
        this.numberData = numberData;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public float getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(float percentCompleted) {
        this.percentCompleted = percentCompleted;
    }
}
