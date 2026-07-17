package com.edatasite.workforce.rest.v2.release10.core.to.crm.email;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Farrukh Abdurakhmonov on 4/14/2018.
 */
public class EmailSenderTO extends ResponseData {
    private Integer id;
    private String name;
    private String email;
    private String avatar;
    private Boolean isDefault;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean isisDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isisDefault) {
        this.isDefault = isisDefault;
    }
}
