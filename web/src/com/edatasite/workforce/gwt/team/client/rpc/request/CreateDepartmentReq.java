package com.edatasite.workforce.gwt.team.client.rpc.request;

import com.edatasite.workforce.gwt.core.client.enums.ChildOrientation;

public class CreateDepartmentReq {

    private String name;
    private String description;
    private String shortDescription;
    private ChildOrientation orientation;
    private String color;
    private Integer parentId;

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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public ChildOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(ChildOrientation orientation) {
        this.orientation = orientation;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

}

