package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.TimeTO;

public class TaskAssigneeTO  extends ResponseData{
    private Integer id;
    private String name;
    private String avatar_image;
    private IdNameTO department;
    private TimeTO estimate;

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

    public String getAvatar_image() {
        return avatar_image;
    }

    public void setAvatar_image(String avatar_image) {
        this.avatar_image = avatar_image;
    }

    public IdNameTO getDepartment() {
        return department;
    }

    public void setDepartment(IdNameTO department) {
        this.department = department;
    }

    public TimeTO getEstimate() {
        return estimate;
    }

    public void setEstimate(TimeTO estimate) {
        this.estimate = estimate;
    }
}
