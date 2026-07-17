package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Umidbek on 16.02.2015.
 */
public class WorkStreamTO implements IsSerializable {
    private Integer id;
    private String name;

    public WorkStreamTO() {
    }

    public WorkStreamTO(Integer id, String name) {
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
