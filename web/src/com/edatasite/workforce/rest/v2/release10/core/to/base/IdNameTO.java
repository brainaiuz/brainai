package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d Madrahimov on 01/10/2018.
 */
public class IdNameTO extends ResponseData {

    private Integer id;
    private String name;

    public IdNameTO() {
    }

    public IdNameTO(Integer id, String name) {
        this.id = id;
        this.name = name;
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
}
