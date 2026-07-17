package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class PhoneEmailTO extends ResponseData {
    private String name;
    private String type;
    private Boolean isPrimary;

    public PhoneEmailTO() {
    }

    public PhoneEmailTO(String name, String type, Boolean isPrimary) {
        this.name = name;
        this.type = type;
        this.isPrimary = isPrimary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }
}
