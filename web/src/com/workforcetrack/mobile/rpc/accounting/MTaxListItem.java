package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 5/23/11
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "taxListItem")
public class MTaxListItem implements Serializable {

    private Integer objectID;
    private String name;
    private Double percent;
    private Integer permissionType;

    public MTaxListItem() {
    }

    public MTaxListItem(TaxListItem taxListItem) {
        this.objectID = taxListItem.getObjectID();
        this.name = taxListItem.getName();
        this.percent = taxListItem.getPercent().doubleValue();
        this.permissionType = taxListItem.getPermissionType();
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

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }
}