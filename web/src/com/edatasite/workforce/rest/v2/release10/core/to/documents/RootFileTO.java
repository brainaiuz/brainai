package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 10/27/2017.
 */
public class RootFileTO extends ResponseData {
    private Integer root_id;
    private String root_name;
    private String root_type;

    public RootFileTO() {
    }


    public RootFileTO(String root_name, String root_type) {
        this.root_name = root_name;
        this.root_type = root_type;
    }

    public RootFileTO(Integer root_id, String root_name, String root_type) {
        this.root_id = root_id;
        this.root_name = root_name;
        this.root_type = root_type;
    }

    public Integer getRoot_id() {
        return root_id;
    }

    public void setRoot_id(Integer root_id) {
        this.root_id = root_id;
    }

    public String getRoot_type() {
        return root_type;
    }

    public void setRoot_type(String root_type) {
        this.root_type = root_type;
    }

    public String getRoot_name() {
        return root_name;
    }

    public void setRoot_name(String root_name) {
        this.root_name = root_name;
    }
}
