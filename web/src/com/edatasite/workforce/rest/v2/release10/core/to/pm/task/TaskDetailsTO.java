package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.OwnerTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class TaskDetailsTO extends ResponseData {
    private Integer id;
    private String title;
    private String number;
    private CategoryTO status;
    private String description;
    private String start_date;
    private String due_date;
    private String priority;
    private ArrayList<OwnerTO> assignees;
    private String project;
    private Boolean billable;
    private String parent_workstream;
    private Integer remind_by_email;
    private String recurrence_type;
    private Integer repeats;
    private String end_date;
    private Integer occurence;
    private ArrayList<TaskLinksTO> links;
    private ArrayList<AttachmentTO> attachments;
    private ArrayList<CustomFieldsTO> custom_fields;

    public TaskDetailsTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CategoryTO getStatus() {
        return status;
    }

    public void setStatus(CategoryTO status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public ArrayList<OwnerTO> getAssignees() {
        return assignees;
    }

    public void setAssignees(ArrayList<OwnerTO> assignees) {
        this.assignees = assignees;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public String getParent_workstream() {
        return parent_workstream;
    }

    public void setParent_workstream(String parent_workstream) {
        this.parent_workstream = parent_workstream;
    }

    public Integer getRemind_by_email() {
        return remind_by_email;
    }

    public void setRemind_by_email(Integer remind_by_email) {
        this.remind_by_email = remind_by_email;
    }

    public String getRecurrence_type() {
        return recurrence_type;
    }

    public void setRecurrence_type(String recurrence_type) {
        this.recurrence_type = recurrence_type;
    }

    public Integer getRepeats() {
        return repeats;
    }

    public void setRepeats(Integer repeats) {
        this.repeats = repeats;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }

    public ArrayList<TaskLinksTO> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<TaskLinksTO> links) {
        this.links = links;
    }

    public ArrayList<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public ArrayList<CustomFieldsTO> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<CustomFieldsTO> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
