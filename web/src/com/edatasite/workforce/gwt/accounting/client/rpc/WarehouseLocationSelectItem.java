package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 26, 2010
 * Time: 11:00:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseLocationSelectItem extends SelectItem {
    private String pickingNumber;
    private Integer warehouseID;

    public WarehouseLocationSelectItem() {
        super();
    }

    public WarehouseLocationSelectItem(Integer id, String name, String pickingNumber) {
        super(id, name);
        this.pickingNumber = pickingNumber;
    }

    public WarehouseLocationSelectItem(Integer id, String name, String pickingNumber, Integer warehouseID) {
        super(id, name);
        this.pickingNumber = pickingNumber;
        this.warehouseID = warehouseID;
    }

    public String getPickingNumber() {
        return pickingNumber;
    }

    public void setPickingNumber(String pickingNumber) {
        this.pickingNumber = pickingNumber;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }
}
