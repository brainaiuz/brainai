package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class DepartmentListItemTO extends ResponseData {

    private Integer id;
    private String name;
    private Integer employees_count;

    public DepartmentListItemTO() {
    }

    public DepartmentListItemTO(Integer id, String name, Integer employees_count) {
        this.id = id;
        this.name = name;
        this.employees_count = employees_count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEmployees_count() {
        return employees_count;
    }

    public void setEmployees_count(Integer employees_count) {
        this.employees_count = employees_count;
    }
}
