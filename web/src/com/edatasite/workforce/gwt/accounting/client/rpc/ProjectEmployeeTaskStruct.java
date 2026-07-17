package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 15.05.2009
 * Time: 20:32:36
 * To change this template use File | Settings | File Templates.
 */
public class ProjectEmployeeTaskStruct extends ProjectEmployeeStruct {

    private SelectItem task;

    public ProjectEmployeeTaskStruct() {

    }

    public ProjectEmployeeTaskStruct(SelectItem project, SelectItem employee, SelectItem task) {
        super(project, employee);
        this.task = task;
    }

    public SelectItem getTask() {
        return task;
    }

    public void setTask(SelectItem task) {
        this.task = task;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ProjectEmployeeTaskStruct that = (ProjectEmployeeTaskStruct) o;
        return task != null ? task.equals(that.task) : that.task == null;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (task != null ? task.hashCode() : 0);
        return result;
    }
}