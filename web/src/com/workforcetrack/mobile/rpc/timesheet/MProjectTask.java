package com.workforcetrack.mobile.rpc.timesheet;

import com.workforcetrack.mobile.rpc.opportunity.MNumberData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/14/11
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class MProjectTask {

    private String name;
    private Integer timesheetID;
    private Integer employeeTaskID;
    private Integer dailyWorkedMinutes;
    private Integer totalWorkedMinutes;
    private Date date;
    //private boolean editable = true;
    private int status;
    private Integer oldEmployeeTaskID;
    private String comment;
    private Integer taskStatusID;
    private List<Integer> oldEmployeeTaskIDList = new ArrayList<>();
    private MNumberData numberData;
    private Date startDate;
    private Date endDate;
    private float percentCompleted;


    public MProjectTask() {
    }

    public MProjectTask(ProjectTaskForMobile projectTaskForMobile) {
        if (projectTaskForMobile != null) {
            this.name = projectTaskForMobile.getName();
            this.timesheetID = projectTaskForMobile.getTimesheetID();
            this.employeeTaskID = projectTaskForMobile.getEmployeeTaskID();
            this.dailyWorkedMinutes = projectTaskForMobile.getDailyWorkedMinutes();
            this.totalWorkedMinutes = projectTaskForMobile.getTotalWorkedMinutes();
            this.date = projectTaskForMobile.getDate();
            this.status = projectTaskForMobile.getStatus();
            this.oldEmployeeTaskID = projectTaskForMobile.getOldEmployeeTaskID();
            this.comment = projectTaskForMobile.getComment();
            this.taskStatusID = projectTaskForMobile.getTaskStatusID();
            this.oldEmployeeTaskIDList = projectTaskForMobile.getOldEmployeeTaskIDList();
            this.numberData = projectTaskForMobile.getNumberData();
            this.startDate = projectTaskForMobile.getStartDate();
            this.endDate =  projectTaskForMobile.getEndDate();
            this.percentCompleted = projectTaskForMobile.getPercentCompleted();
        }
    }


    public static Boolean convert(MProjectTask mProjectTask, ProjectTaskForMobile projectTaskForMobile, boolean toProjectTaskForMobile) {
        if (mProjectTask == null || projectTaskForMobile == null) {
            return null;
        }
        try {
            if (toProjectTaskForMobile) {
                projectTaskForMobile.setName(mProjectTask.getName());
                projectTaskForMobile.setTimesheetID(mProjectTask.getTimesheetID());
                projectTaskForMobile.setEmployeeTaskID(mProjectTask.getEmployeeTaskID());
                projectTaskForMobile.setDailyWorkedMinutes(mProjectTask.getDailyWorkedMinutes());
                projectTaskForMobile.setTotalWorkedMinutes(mProjectTask.getTotalWorkedMinutes());
                projectTaskForMobile.setDate(mProjectTask.getDate());
                projectTaskForMobile.setStatus(mProjectTask.getStatus());
                projectTaskForMobile.setOldEmployeeTaskID(mProjectTask.getOldEmployeeTaskID());
                projectTaskForMobile.setOldEmployeeTaskIDList(mProjectTask.getOldEmployeeTaskIDList());
                projectTaskForMobile.setComment(mProjectTask.getComment());
                projectTaskForMobile.setStartDate(mProjectTask.getStartDate());
                projectTaskForMobile.setEndDate(mProjectTask.getEndDate());
                projectTaskForMobile.setPercentCompleted(mProjectTask.getPercentCompleted());

            } else {
                mProjectTask.setName(projectTaskForMobile.getName());
                mProjectTask.setTimesheetID(projectTaskForMobile.getTimesheetID());
                mProjectTask.setEmployeeTaskID(projectTaskForMobile.getEmployeeTaskID());
                mProjectTask.setDailyWorkedMinutes(projectTaskForMobile.getDailyWorkedMinutes());
                mProjectTask.setTotalWorkedMinutes(projectTaskForMobile.getTotalWorkedMinutes());
                mProjectTask.setDate(projectTaskForMobile.getDate());
                mProjectTask.setStatus(projectTaskForMobile.getStatus());
                mProjectTask.setOldEmployeeTaskID(projectTaskForMobile.getOldEmployeeTaskID());
                mProjectTask.setOldEmployeeTaskIDList(projectTaskForMobile.getOldEmployeeTaskIDList());
                mProjectTask.setComment(projectTaskForMobile.getComment());
                mProjectTask.setStartDate(projectTaskForMobile.getStartDate());
                mProjectTask.setEndDate(projectTaskForMobile.getEndDate());
                mProjectTask.setPercentCompleted(projectTaskForMobile.getPercentCompleted());
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
