package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 15.05.2009
 * Time: 20:31:09
 * To change this template use File | Settings | File Templates.
 */
public class ProjectEmployeeValue implements IsSerializable {

    private ArrayList<SelectItem> tasks;

    private Integer hourSpent = 0;

    private Double clientChargeRate;

    private String timesheetDescription;

    private Integer[] entryIds;

    public ProjectEmployeeValue() {
    }

    public ProjectEmployeeValue(ArrayList<SelectItem> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<SelectItem> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<SelectItem> tasks) {
        this.tasks = tasks;
    }

    public void add(SelectItem item) {
        getTasks().add(item);
    }

    public Integer getHourSpent() {
        return hourSpent;
    }

    public void setHourSpent(Integer hourSpent) {
        this.hourSpent = hourSpent;
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        ProjectEmployeeValue that = (ProjectEmployeeValue) o;
        return tasks != null ? tasks.equals(that.tasks) : that.tasks == null;
    }

    public int hashCode() {
        int result = tasks != null ? tasks.hashCode() : 0;
        result = 31 * result + (hourSpent != null ? hourSpent.hashCode() : 0);
        return result;
    }

    public Integer[] getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(Integer[] entryIds) {
        this.entryIds = entryIds;
    }

    public String getTimesheetDescription() {
        return timesheetDescription;
    }

    public void setTimesheetDescription(String timesheetDescription) {
        this.timesheetDescription = timesheetDescription;
    }
}
