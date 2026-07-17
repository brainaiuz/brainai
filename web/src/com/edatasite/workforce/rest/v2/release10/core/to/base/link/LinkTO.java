package com.edatasite.workforce.rest.v2.release10.core.to.base.link;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d Madrahimov on 3/17/2018.
 */
public class LinkTO extends ResponseData {

    private Integer item_id;
    private String name;
    private String link_type;

    public LinkTO() {
    }

    public LinkTO(Integer item_id, String name, String link_type) {
        this.item_id = item_id;
        this.name = name;
        this.link_type = link_type;
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

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }
}
