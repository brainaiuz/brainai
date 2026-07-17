package com.edatasite.workforce.gwt.project.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Dilsh0d
 * Date: 19-May-2010
 * Time: 12:00:39
 */
public class ProjectCostAllDataItem implements IsSerializable {

    private Integer costPeriodId;
    private Integer projectId;
    private Integer taskId;
    private Integer resourceTypeId;
    private Date from;
    private Date to;

    private ProjectCostItem[] resources;
    private ProjectCostItem[] otherOverHeads;

    private boolean estemitedCost = true;

    public ProjectCostAllDataItem() {

    }

    public ProjectCostAllDataItem(Integer projectId, Integer taskId, Integer resourceTypeId, Date from, Date to) {
        this.projectId = projectId;
        this.taskId = taskId;
        this.resourceTypeId = resourceTypeId;
        this.from = from;
        this.to = to;
    }

    public ProjectCostAllDataItem(Integer projectId, Integer taskId, Integer resourceTypeId, Date from) {
        this.projectId = projectId;
        this.taskId = taskId;
        this.resourceTypeId = resourceTypeId;
        this.from = from;
    }

    public Integer getCostPeriodId() {
        return costPeriodId;
    }

    public void setCostPeriodId(Integer costPeriodId) {
        this.costPeriodId = costPeriodId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public ProjectCostItem[] getResources() {
        return resources;
    }

    public void setResources(ProjectCostItem[] resources) {
        this.resources = resources;
    }

    public ProjectCostItem[] getOtherOverHeads() {
        return otherOverHeads;
    }

    public void setOtherOverHeads(ProjectCostItem[] otherOverHeads) {
        this.otherOverHeads = otherOverHeads;
    }

    public boolean isEstemitedCost() {
        return estemitedCost;
    }

    public void setEstemitedCost(boolean estemitedCost) {
        this.estemitedCost = estemitedCost;
    }
}
