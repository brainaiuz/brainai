package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 24.06.2010
 * Time: 20:25:56
 * To change this template use File | Settings | File Templates.
 */
public class TaxListItem implements IsSerializable {

    public static String ACTION = "action";
    public static String NAME = "name";
    public static String TAXRATE = "taxrate";

    private Integer objectID;
    private String name;
    private BigDecimal percent;
    private Integer permissionType;
    private Integer type;

    private boolean selectedByDefault;


    public boolean isSelectedByDefault() {
        return selectedByDefault;
    }

    public void setSelectedByDefault(boolean selectedByDefault) {
        this.selectedByDefault = selectedByDefault;
    }


    public TaxListItem() {
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

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
