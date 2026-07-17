package com.edatasite.workforce.gwt.project.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class EditContract implements IsSerializable {

    private Integer objectId;
    private String number;
    private String clientName;
    private Integer clientId;
    private Boolean isAccomodation;
    private DateNonConvertable creationTime;
    private Boolean isFoot;
    private ProjectPosition[] projectPositions;
    private ArrayList<CalendarEventReminder> reminder = new ArrayList<>();
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private DateNonConvertable startDate;
    private DateNonConvertable dueDate;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Boolean getIsAccomodation() {
        return isAccomodation;
    }

    public void setIsAccomodation(Boolean isAccomodation) {
        this.isAccomodation = isAccomodation;
    }

    public Boolean getIsFoot() {
        return isFoot;
    }

    public void setIsFoot(Boolean isFoot) {
        this.isFoot = isFoot;
    }

    public ProjectPosition[] getProjectPositions() {
        return projectPositions;
    }

    public void setProjectPositions(ProjectPosition[] projectPositions) {
        this.projectPositions = projectPositions;
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

    public DateNonConvertable getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateNonConvertable creationTime) {
        this.creationTime = creationTime;
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