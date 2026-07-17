package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 15.05.2009
 * Time: 20:32:36
 * To change this template use File | Settings | File Templates.
 */
public class ProjectEmployeeStruct implements IsSerializable {

    private SelectItem employee;
    private SelectItem project;

    public ProjectEmployeeStruct() {

    }

    public ProjectEmployeeStruct(SelectItem project, SelectItem employee) {
        this.employee = employee;
        this.project = project;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        ProjectEmployeeStruct that = (ProjectEmployeeStruct) o;
        if (employee != null ? !employee.equals(that.employee) : that.employee != null) {
            return false;
        }
        return project != null ? project.equals(that.project) : that.project == null;
    }


    public int hashCode() {
        int result = employee != null ? employee.hashCode() : 0;
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }
}
