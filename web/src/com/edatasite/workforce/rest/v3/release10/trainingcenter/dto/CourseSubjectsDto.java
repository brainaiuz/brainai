package com.edatasite.workforce.rest.v3.release10.trainingcenter.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.IdName;

public class CourseSubjectsDto {
    private Integer id;
    private String name;
    private String description;
    private IdName parent;

    public CourseSubjectsDto() {

    }

    public CourseSubjectsDto(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IdName getParent() {
        return parent;
    }

    public void setParent(IdName parent) {
        this.parent = parent;
    }
}
