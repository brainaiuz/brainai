package com.workforcetrack.mobile.rpc.timesheet;

import com.edatasite.workforce.gwt.core.client.rpc.Departments;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Abdulaziz
 * Date: 8/25/11
 * Time: 5:40 PM
 */
@XmlRootElement
public class MDepartments implements IsSerializable {
    public MDepartments(){

    }
    public MDepartments(Departments deps){
        this.deptName = deps.getDeptName();
        this.employees = deps.getEmployees();
    }

    public static Departments convertFromMobile(MDepartments mDeps){
        Departments deps = new Departments();
        deps.setDeptName(mDeps.getDeptName());
        deps.setEmployees(mDeps.getEmployees());
        return deps;
    }
    private String deptName;
    private String employees;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEmployees() {
        return employees;
    }

    public void setEmployees(String employees) {
        this.employees = employees;
    }
}
