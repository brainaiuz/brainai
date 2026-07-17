package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.List;
import java.util.Objects;

public class DepartmentDto {
    private Integer id;
    private String name;
    private List<SelectItem> employees;

    public DepartmentDto() {
    }

    public DepartmentDto(Integer id, String name, List<SelectItem> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
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

    public List<SelectItem> getEmployees() {
        return employees;
    }

    public void setEmployees(List<SelectItem> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentDto that)) return false;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DepartmentTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
