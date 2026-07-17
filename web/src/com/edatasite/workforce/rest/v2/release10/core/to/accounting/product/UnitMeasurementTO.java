package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class UnitMeasurementTO extends ResponseData {
    private Integer measurement_id;
    private String measurement_name;

    public UnitMeasurementTO() {
    }

    public UnitMeasurementTO(Integer measurement_id, String measurement_name) {
        this.measurement_id = measurement_id;
        this.measurement_name = measurement_name;
    }

    public Integer getMeasurement_id() {
        return measurement_id;
    }

    public void setMeasurement_id(Integer measurement_id) {
        this.measurement_id = measurement_id;
    }

    public String getMeasurement_name() {
        return measurement_name;
    }

    public void setMeasurement_name(String measurement_name) {
        this.measurement_name = measurement_name;
    }
}
