package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusItemTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh 02/23/2018.
 */
public class TaskDetailsItemTO extends ResponseData {
    private TaskBaseInfoTO base_info;
    private FilteredStatusItemTO status;
    private String share_link;
    private ArrayList<TaskAssigneeTO> assignees;
    private TaskInformationTO information;
    private ArrayList<Object> custom_fields;
    private boolean can_edit;

    public TaskDetailsItemTO() {
    }

    public TaskBaseInfoTO getBase_info() {
        return base_info;
    }

    public void setBase_info(TaskBaseInfoTO base_info) {
        this.base_info = base_info;
    }

    public FilteredStatusItemTO getStatus() {
        return status;
    }

    public void setStatus(FilteredStatusItemTO status) {
        this.status = status;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    public ArrayList<TaskAssigneeTO> getAssignees() {
        return assignees;
    }

    public void setAssignees(ArrayList<TaskAssigneeTO> assignees) {
        this.assignees = assignees;
    }

    public TaskInformationTO getInformation() {
        return information;
    }

    public void setInformation(TaskInformationTO information) {
        this.information = information;
    }

    public ArrayList<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public boolean isCan_edit() {
        return can_edit;
    }

    public void setCan_edit(boolean can_edit) {
        this.can_edit = can_edit;
    }
}
