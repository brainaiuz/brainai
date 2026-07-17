package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d Madrahimov on 01/10/2018.
 */
public class SearchPeopleTO extends ResponseData {

    private String item_type;
    private Integer item_id;
    private String name;
    private String email;
    private String mobile;

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
