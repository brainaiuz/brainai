package com.edatasite.workforce.rest.v2.release10.core.to.status;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class ColorTO extends ResponseData {

    private Integer id;
    private String hex;
    private String name;

    public ColorTO() {

    }

    public ColorTO(Integer id, String hex, String name) {
        this.id = id;
        this.hex = hex;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
