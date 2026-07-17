package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 10/27/2017.
 */
public class OwnerTO extends ResponseData {
    private Integer owner_id;
    private String owner_name;

    public OwnerTO() {
    }

    public OwnerTO(Integer owner_id, String owner_name) {
        this.owner_id = owner_id;
        this.owner_name = owner_name;
    }

    public Integer getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Integer owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }
}
