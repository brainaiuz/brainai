package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 4/23/15 1:54 PM
 */
public class TimerTO implements IsSerializable {
    Integer id;
    SelectItemTO project;
    SelectItemTO task;
    SelectItemTO issue;
    SelectItemTO cases;
    Integer estimatedTime;
    Integer totalTime;
    Integer cumulativeTime;
    Integer elapsedTime;
    Float completed;
    String comment;
    Boolean isStarted;
    Boolean isReset;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItemTO getProject() {
        return project;
    }

    public void setProject(SelectItemTO project) {
        this.project = project;
    }

    public SelectItemTO getTask() {
        return task;
    }

    public void setTask(SelectItemTO task) {
        this.task = task;
    }

    public SelectItemTO getIssue() {
        return issue;
    }

    public void setIssue(SelectItemTO issue) {
        this.issue = issue;
    }

    public SelectItemTO getCases() {
        return cases;
    }

    public void setCases(SelectItemTO cases) {
        this.cases = cases;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Float getCompleted() {
        return completed;
    }

    public void setCompleted(Float completed) {
        this.completed = completed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCumulativeTime() {
        return cumulativeTime;
    }

    public void setCumulativeTime(Integer cumulativeTime) {
        this.cumulativeTime = cumulativeTime;
    }

    public Integer getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Integer elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Boolean getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(Boolean isStarted) {
        this.isStarted = isStarted;
    }

    public Boolean getIsReset() {
        return isReset;
    }

    public void setIsReset(Boolean isReset) {
        this.isReset = isReset;
    }

    public TimerTO() {
    }

    public TimerTO(ClockItem clockItem) {
        this.project = new SelectItemTO(clockItem.getProjectID(), clockItem.getProjectName());
        this.task = new SelectItemTO(clockItem.getTaskID(), clockItem.getTaskName());
        this.issue = new SelectItemTO(clockItem.getIssueID(), clockItem.getIssueName());
        this.cases = new SelectItemTO(clockItem.getCaseID(), clockItem.getCaseName());
        this.cumulativeTime = clockItem.getTodaysTime();
        this.estimatedTime = clockItem.getEstimateTime();
        this.elapsedTime = clockItem.getElapsedTime();
        this.totalTime = clockItem.getActualTime();
        this.completed = clockItem.getPercent();
        this.comment = clockItem.getComment();
        this.isReset = clockItem.isReset();
        this.isStarted = clockItem.isStarted();
    }

    public ClockItem wrap(TimerTO timerTO) {
        ClockItem clockItem = new ClockItem();
        clockItem.setObjectId(timerTO.getId());
        clockItem.setComment(timerTO.getComment());
        clockItem.setPercent(timerTO.getCompleted());
        clockItem.setTodaysTime(timerTO.getCumulativeTime());
        clockItem.setElapsedTime(timerTO.getElapsedTime());
        clockItem.setEstimateTime(timerTO.getEstimatedTime());
        Integer relationId = null;
        Integer relationType = null;
        if (timerTO.getTask() != null && timerTO.getTask().getId() != null) {
            relationId = timerTO.getTask().getId();
            relationType = Constants.PM_TASK;
        }
        if (timerTO.getIssue() != null && timerTO.getIssue().getId() != null) {
            relationId = timerTO.getIssue().getId();
            relationType = Constants.PM_ISSUE_TIMER;
        }
        if (timerTO.getCases() != null && timerTO.getCases().getId() != null) {
            relationId = timerTO.getCases().getId();
            relationType = Constants.CRM_CASE;
        }
        clockItem.setBusObjectId(relationId);
        clockItem.setRelation(relationType);

        return clockItem;
    }
}
