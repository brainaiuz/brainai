package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
/**
 * Created by Abdurakhmonov Farrukh on 03/17/2018.
 */
public class LinksTO extends ResponseData {
    private Integer id;
    private String name;
    private String link_type;

    public LinksTO() {
    }

    public LinksTO(Integer id, String name, String link_type) {
        this.id = id;
        this.name = name;
        this.link_type = link_type;
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

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }
}
