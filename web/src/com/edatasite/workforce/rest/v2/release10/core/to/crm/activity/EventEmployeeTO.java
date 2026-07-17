package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
/**
 * Created by Abdurakhmonov Farrukh on 03/17/2018.
 */
public class EventEmployeeTO extends ResponseData {
    private Integer id;
    private String name;
    private String avatar_image;
    private CategoryTO department;

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

    public CategoryTO getDepartment() {
        return department;
    }

    public void setDepartment(CategoryTO department) {
        this.department = department;
    }
}
