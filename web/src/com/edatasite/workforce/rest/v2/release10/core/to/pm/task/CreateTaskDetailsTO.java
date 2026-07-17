package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.link.LinkTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.ShareWithTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.TimeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.WhenTO;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 4/10/2018.
 */
public class CreateTaskDetailsTO extends ResponseData {

    private Integer id;
    private Integer project;
    private String name;
    private String description;
    private WhenTO when;
    private String priority;
    private Integer status;
    private Boolean billable;
    private ArrayList<LinkTO> links;
    private ShareWithTO assignees;
    private Integer parent_workstream;
    private ArrayList<TimeTO> reminders;
    private ArrayList<LinkTO> predecessor_tasks;
    private ArrayList<LinkTO> successor_tasks;
    private ArrayList<Object> custom_fields;

    public CreateTaskDetailsTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WhenTO getWhen() {
        return when;
    }

    public void setWhen(WhenTO when) {
        this.when = when;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public ArrayList<LinkTO> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LinkTO> links) {
        this.links = links;
    }

    public ShareWithTO getAssignees() {
        return assignees;
    }

    public void setAssignees(ShareWithTO assignees) {
        this.assignees = assignees;
    }

    public Integer getParent_workstream() {
        return parent_workstream;
    }

    public void setParent_workstream(Integer parent_workstream) {
        this.parent_workstream = parent_workstream;
    }

    public ArrayList<TimeTO> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<TimeTO> reminders) {
        this.reminders = reminders;
    }

    public ArrayList<LinkTO> getPredecessor_tasks() {
        return predecessor_tasks;
    }

    public void setPredecessor_tasks(ArrayList<LinkTO> predecessor_tasks) {
        this.predecessor_tasks = predecessor_tasks;
    }

    public ArrayList<LinkTO> getSuccessor_tasks() {
        return successor_tasks;
    }

    public void setSuccessor_tasks(ArrayList<LinkTO> successor_tasks) {
        this.successor_tasks = successor_tasks;
    }

    public ArrayList<Object> getCustom_fields() {
        return custom_fields != null ? custom_fields : new ArrayList<>();
    }

    public void setCustom_fields(ArrayList<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
