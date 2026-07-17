package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListSearchData;

public class ProductRequestListTO extends RequestListSearchData {

    private Integer warehouse_id;
    private boolean avoid_zero;
    private boolean includeBatches;
    private Integer brand_id;

    public Integer getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Integer warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public boolean isAvoid_zero() {
        return avoid_zero;
    }

    public void setAvoid_zero(boolean avoid_zero) {
        this.avoid_zero = avoid_zero;
    }

    public boolean isIncludeBatches() {
        return includeBatches;
    }

    public void setIncludeBatches(boolean includeBatches) {
        this.includeBatches = includeBatches;
    }

    public Integer getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Integer brand_id) {
        this.brand_id = brand_id;
    }
}
