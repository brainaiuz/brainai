package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Abdurakhmonov Farrukh on 6/12/2017.
 */
public class OwnerTO extends ResponseData {
    private Integer id;
    private String name;
    private String avatar;
    private String department;

    public OwnerTO() {
    }

    public OwnerTO(Integer id, String name, String avatar, String department) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.department = department;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
