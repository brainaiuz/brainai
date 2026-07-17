package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 13/10/2017.
 */
public class PeopleTO extends ResponseData {

    private Integer people_id;
    private String people_name;
    private String people_type;
    private String people_avatar;
    private Boolean people_is_active;

    public PeopleTO() {
    }

    public Integer getPeople_id() {
        return people_id;
    }

    public void setPeople_id(Integer people_id) {
        this.people_id = people_id;
    }

    public String getPeople_name() {
        return people_name;
    }

    public void setPeople_name(String people_name) {
        this.people_name = people_name;
    }

    public String getPeople_type() {
        return people_type;
    }

    public void setPeople_type(String people_type) {
        this.people_type = people_type;
    }

    public String getPeople_avatar() {
        return people_avatar;
    }

    public void setPeople_avatar(String people_avatar) {
        this.people_avatar = people_avatar;
    }

    public Boolean getPeople_is_active() {
        return people_is_active;
    }

    public void setPeople_is_active(Boolean people_is_active) {
        this.people_is_active = people_is_active;
    }
}
