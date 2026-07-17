package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.LinksTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.RecurrenceTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.TimeTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh 02/23/2018.
 */
public class TaskInformationTO extends ResponseData {
    private String start_date;
    private String end_date;
    private Boolean all_day;
    private String number;
    private CategoryTO project;
    private boolean billable;
    private CategoryTO parent_workstream;
    private ArrayList<TimeTO> reminders;
    private RecurrenceTO recurrence;
    private ArrayList<LinksTO> links;
    private ArrayList<LinksTO> predecessor_tasks;
    private ArrayList<LinksTO> successor_tasks;

    public TaskInformationTO() {
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Boolean getAll_day() {
        return all_day;
    }

    public void setAll_day(Boolean all_day) {
        this.all_day = all_day;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CategoryTO getProject() {
        return project;
    }

    public void setProject(CategoryTO project) {
        this.project = project;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public CategoryTO getParent_workstream() {
        return parent_workstream;
    }

    public void setParent_workstream(CategoryTO parent_workstream) {
        this.parent_workstream = parent_workstream;
    }

    public ArrayList<TimeTO> getReminders() {
        return reminders;
    }

    public void setReminders(ArrayList<TimeTO> reminders) {
        this.reminders = reminders;
    }

    public RecurrenceTO getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(RecurrenceTO recurrence) {
        this.recurrence = recurrence;
    }

    public ArrayList<LinksTO> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<LinksTO> links) {
        this.links = links;
    }

    public ArrayList<LinksTO> getPredecessor_tasks() {
        return predecessor_tasks;
    }

    public void setPredecessor_tasks(ArrayList<LinksTO> predecessor_tasks) {
        this.predecessor_tasks = predecessor_tasks;
    }

    public ArrayList<LinksTO> getSuccessor_tasks() {
        return successor_tasks;
    }

    public void setSuccessor_tasks(ArrayList<LinksTO> successor_tasks) {
        this.successor_tasks = successor_tasks;
    }
}

