package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 16, 2010
 * Time: 3:21:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseLocationItem implements IsSerializable {

    private Integer objectID;
    private String aisle;
    private String rack;
    private String shelf;
    private String bin;
    private SelectItem aisleItem;
    private SelectItem rackItem;
    private SelectItem shelfItem;

    private Integer warehouseID;

    public WarehouseLocationItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public SelectItem getAisleItem() {
        return aisleItem;
    }

    public void setAisleItem(SelectItem aisleItem) {
        this.aisleItem = aisleItem;
    }

    public SelectItem getRackItem() {
        return rackItem;
    }

    public void setRackItem(SelectItem rackItem) {
        this.rackItem = rackItem;
    }

    public SelectItem getShelfItem() {
        return shelfItem;
    }

    public void setShelfItem(SelectItem shelfItem) {
        this.shelfItem = shelfItem;
    }

}
