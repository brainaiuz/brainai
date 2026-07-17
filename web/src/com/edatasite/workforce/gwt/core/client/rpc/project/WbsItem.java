package com.edatasite.workforce.gwt.core.client.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;

import java.util.Date;


public class WbsItem extends WfmTreeItem {

    public static String NAME = "NAME";
    public static String START_DATE = "START_DATE";
    public static String END_DATE = "END_DATE";

    private String statusName;
    private String priorityName;
    private String lastModifiedBy;
    private Date lastModified;
    private Date startDate;
    private Date endDate;
    private String[] assignees;
    public static final int WORKSTREAM = 1000;
    public static final int TASK = 1;
    private String numberData;
    private Integer statusId;
    private String projectStatus;
    private Float taskPercent;
    private Integer estimated;
    private String timeSpent;

    public WbsItem() {
        super();
    }

    public WbsItem(Integer id, String name, int nodeType) {
        super(id, name);
        this.nodeType = nodeType;
    }

    private int nodeType;

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public int getNodeType() {
        return nodeType;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
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

    public String[] getAssignees() {
        return assignees;
    }

    public void setAssignees(String[] assignees) {
        this.assignees = assignees;
    }

    public String getNumberData() {
        return numberData;
    }

    public void setNumberData(String numberData) {
        this.numberData = numberData;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Float getTaskPercent() {
        return taskPercent;
    }

    public void setTaskPercent(Float taskPercent) {
        this.taskPercent = taskPercent;
    }

    public Integer getEstimated() {
        return estimated;
    }

    public void setEstimated(Integer estimated) {
        this.estimated = estimated;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WbsItem)) {
            return false;
        }
        WbsItem other = (WbsItem) o;
        if (getNodeType() != other.getNodeType()) {
            return false;
        }
        return getId().intValue() == other.getId().intValue();
    }


    public int hashCode() {
        return getId() + nodeType;
    }
}