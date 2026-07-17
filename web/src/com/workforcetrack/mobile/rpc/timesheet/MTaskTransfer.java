package com.workforcetrack.mobile.rpc.timesheet;

import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskTransfer;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * User: Abdulaziz
 * Date: 7/16/11
 * Time: 7:04 PM
 */
@XmlRootElement
public class MTaskTransfer implements Serializable{
    private Integer emplTaskId;
    private Integer taskId;
    private Integer projectId;
    private String emplTaskName;
    private String projectName;
    private String clientName;
    private MTaskStatus taskStatus;
    private Date startDate;
    private float percentCompleted;
    private Integer estimatedTime;
    private Date endDate;
    private Integer totalMinutes;
    private boolean isIssue = false;

    public MTaskTransfer(){

    }
    public MTaskTransfer(TaskTransfer taskTransfer){
        this.emplTaskId = taskTransfer.getEmplTaskId();
        this.taskId = taskTransfer.getTaskId();
        this.projectId = taskTransfer.getProjectId();
        this.emplTaskName = taskTransfer.getEmplTaskName();
        this.projectName = taskTransfer.getProjectName();
        this.clientName = taskTransfer.getClientName();
        this.taskStatus = new MTaskStatus(taskTransfer.getTaskStatus());
        this.startDate = taskTransfer.getStartDate();
        this.percentCompleted = taskTransfer.getPercentCompleted();
        this.estimatedTime = taskTransfer.getEstimatedTime();
        this.endDate = taskTransfer.getEndDate();
        this.totalMinutes = taskTransfer.getTotalMinutes();
        this.isIssue = taskTransfer.isIssue();
    }

    public static TaskTransfer convertFromMobile(MTaskTransfer mTransfer){
        TaskTransfer transfer = new TaskTransfer();
        transfer.setEmplTaskId(mTransfer.getEmplTaskId());
        transfer.setTaskId(mTransfer.getTaskId());
        transfer.setProjectId(mTransfer.getProjectId());
        transfer.setEmplTaskName(mTransfer.getEmplTaskName());
        transfer.setProjectName(mTransfer.getProjectName());
        transfer.setClientName(mTransfer.getClientName());
        transfer.setTaskStatus(MTaskStatus.convertFromMobile(mTransfer.getTaskStatus()));
        transfer.setStartDate(mTransfer.getStartDate());
        transfer.setPercentCompleted(mTransfer.getPercentCompleted());
        transfer.setEstimatedTime(mTransfer.getEstimatedTime());
        transfer.setEndDate(mTransfer.getEndDate());
        transfer.setTotalMinutes(mTransfer.getTotalMinutes());
        transfer.setIsIssue(mTransfer.isIssue);
        return transfer;
    }

    public Integer getEmplTaskId() {
        return emplTaskId;
    }

    public void setEmplTaskId(Integer emplTaskId) {
        this.emplTaskId = emplTaskId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getEmplTaskName() {
        return emplTaskName;
    }

    public void setEmplTaskName(String emplTaskName) {
        this.emplTaskName = emplTaskName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public MTaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(MTaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public float getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(float percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public boolean isIssue() {
        return isIssue;
    }

    public void setIssue(boolean issue) {
        isIssue = issue;
    }
}
