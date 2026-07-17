package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 17, 2010
 * Time: 12:06:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class UnitMeasurementItem implements IsSerializable {
    public static String ACTION = "action";
    public static String NAME = "name";
    public static String DESCRIPTION = "description";
    private Integer objectID;
    private String name;
    private String description;
    private Integer companyID;

    public UnitMeasurementItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }
}
