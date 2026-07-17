package com.edatasite.workforce.rest.v3.release10.core.to.pm.project;

import com.edatasite.workforce.rest.base.to.EmployeeTO;

import java.util.List;

public class ProjectEmployeeListDTO {
    private String departmentName;
    private List<EmployeeTO> employees;

    public ProjectEmployeeListDTO() {
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<EmployeeTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeTO> employees) {
        this.employees = employees;
    }
}
