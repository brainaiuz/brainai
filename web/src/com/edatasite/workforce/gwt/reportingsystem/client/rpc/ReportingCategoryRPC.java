package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Virus on 8/23/14.
 */
public class ReportingCategoryRPC implements IsSerializable, Serializable {
    private Integer id;
    private String name;

    public ReportingCategoryRPC() {
    }

    public ReportingCategoryRPC(Integer id, String name) {
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
