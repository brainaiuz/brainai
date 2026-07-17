package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.Markedable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 4, 2010
 * Time: 5:59:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SchemaListItem implements IsSerializable, Markedable {

    public static final String OBJECT_ID = "objectid";
    public static final String NAME = "objectid";
    public static final String COMPANY_NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String FREE = "free";
    public static final String MAINTENANCE = "ismaitenance";

    private Integer objectID;
    private String name;
    private String description;
    private Boolean free;
    private String isMaintenance;

    public SchemaListItem() {
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

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public Boolean isMarked() {
        return free;
    }

    public void setMarked(Boolean marked) {
        free = marked;
    }

    public String getMaintenance() {
        return isMaintenance;
    }

    public void setMaintenance(String maintenance) {
        isMaintenance = maintenance;
    }
}
