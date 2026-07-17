package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Created by Dilsh0d on 11/3/2017.
 */
public class WarehouseTO extends ResponseData {
    @Schema(required = true)
    private Integer warehouse_id;
    private String warehouse_name;

    public WarehouseTO() {
    }

    public WarehouseTO(Integer warehouse_id, String warehouse_name) {
        this.warehouse_id = warehouse_id;
        this.warehouse_name = warehouse_name;
    }

    public Integer getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Integer warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getWarehouse_name() {
        return warehouse_name;
    }

    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }
}
