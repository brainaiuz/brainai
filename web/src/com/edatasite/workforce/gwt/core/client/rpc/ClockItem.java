package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Aug 11, 2010
 * Time: 8:01:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClockItem implements IsSerializable, Key {

    private Integer objectId;
    private Integer busObjectId;
    private Integer relation;
    private Date startDate;
    private Date endDate;
    private Integer estimateTime;
    private Integer todaysTime = 0;
    private Integer actualTime = 0;
    private Integer elapsedTime;
    private Integer remainingTime;
    private Float percent;
    private String comment;
    private boolean started;
    private String companyDateTimeFormat;
    private Integer ownerId;
    private String ownerName;
    private boolean reset;
    private boolean overrideAnotherTimerInstance;
    private boolean approvedForToday;
    private boolean sentToApproveForToday;

    // PM_TASK related fields
    private SelectItem[] projects;
    private SelectItem[] tasks;
    private SelectItem[] cases;
    private SelectItem[] issues;
    private HashMap<Integer, AttendanceStats> taskMap;

    private Integer projectID;
    private String projectName;

    private Integer taskID;
    private String taskName;

    private Integer caseID;
    private String caseName;

    private Integer issueID;
    private String issueName;

    private boolean haveStoppedTime;

    public ClockItem() {

    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getBusObjectId() {
        return busObjectId;
    }

    public void setBusObjectId(Integer busObjectId) {
        this.busObjectId = busObjectId;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
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

    public Integer getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(Integer estimateTime) {
        this.estimateTime = estimateTime;
    }

    public Integer getTodaysTime() {
        return todaysTime;
    }

    public void setTodaysTime(Integer todaysTime) {
        this.todaysTime = todaysTime;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public Integer getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Integer getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Integer remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public String getCompanyDateTimeFormat() {
        return companyDateTimeFormat;
    }

    public void setCompanyDateTimeFormat(String companyDateTimeFormat) {
        this.companyDateTimeFormat = companyDateTimeFormat;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public SelectItem[] getProjects() {
        return projects;
    }

    public void setProjects(SelectItem[] projects) {
        this.projects = projects;
    }

    public SelectItem[] getTasks() {
        return tasks;
    }

    public void setTasks(SelectItem[] tasks) {
        this.tasks = tasks;
    }

    public SelectItem[] getCases() {
        return cases;
    }

    public void setCases(SelectItem[] cases) {
        this.cases = cases;
    }

    public SelectItem[] getIssues() {
        return issues;
    }

    public void setIssues(SelectItem[] issues) {
        this.issues = issues;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isOverrideAnotherTimerInstance() {
        return overrideAnotherTimerInstance;
    }

    public void setOverrideAnotherTimerInstance(boolean overrideAnotherTimerInstance) {
        this.overrideAnotherTimerInstance = overrideAnotherTimerInstance;
    }

    public boolean isApprovedForToday() {
        return approvedForToday;
    }

    public void setApprovedForToday(boolean approvedForToday) {
        this.approvedForToday = approvedForToday;
    }

    public boolean isSentToApproveForToday() {
        return sentToApproveForToday;
    }

    public void setSentToApproveForToday(boolean sentToApproveForToday) {
        this.sentToApproveForToday = sentToApproveForToday;
    }

    public HashMap<Integer, AttendanceStats> getTaskMap() {
        return taskMap;
    }

    public void setTaskMap(HashMap<Integer, AttendanceStats> taskMap) {
        this.taskMap = taskMap;
    }

    @Override
    public String getKey() {
        return "" + getObjectId();
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseName() {
        return caseName;
    }

    public boolean isHaveStoppedTime() {
        return haveStoppedTime;
    }

    public void setHaveStoppedTime(boolean haveStoppedTime) {
        this.haveStoppedTime = haveStoppedTime;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getCaseID() {
        return caseID;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    public Integer getIssueID() {
        return issueID;
    }

    public void setIssueID(Integer issueID) {
        this.issueID = issueID;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }
}
