package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class ContractSingleItem implements IsSerializable {

    private String number;
    private ProjectPosition[] projectPositions;
    private int clientId;
    private Boolean accomudation;
    private Boolean food;
    private ArrayList<HistoryListItem> notes;
    private FileItem[] attachments;
    private ArrayList<CalendarEventReminder> reminder = new ArrayList<>();
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private DateNonConvertable startDate;
    private DateNonConvertable dueDate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ProjectPosition[] getProjectPositions() {
        return projectPositions;
    }

    public void setProjectPositions(ProjectPosition[] projectPositions) {
        this.projectPositions = projectPositions;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setAccomudation(Boolean accomudation) {
        this.accomudation = accomudation;
    }

    public Boolean getAccomudation() {
        return accomudation;
    }

    public void setFood(Boolean food) {
        this.food = food;
    }

    public Boolean getFood() {
        return food;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public ArrayList<CalendarEventReminder> getReminder() {
        return reminder;
    }

    public void setReminder(ArrayList<CalendarEventReminder> reminder) {
        this.reminder = reminder;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateNonConvertable dueDate) {
        this.dueDate = dueDate;
    }
}