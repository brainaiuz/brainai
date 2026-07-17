package com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.WarehouseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 3/4/2018.
 */
public class AdjustmentItemTO extends ResponseData {

    private Integer line_item_id;
    private Integer product_id;
    private String name;
    private String description;
    private WarehouseTO warehouse;
    private BigDecimal current_qty;
    private BigDecimal used_qty;
    private BigDecimal new_qty;
    private BigDecimal total_qty;
    private IdNameTO project;

    public AdjustmentItemTO() {
    }

    public Integer getLine_item_id() {
        return line_item_id;
    }

    public void setLine_item_id(Integer line_item_id) {
        this.line_item_id = line_item_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
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

    public WarehouseTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTO warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getCurrent_qty() {
        return current_qty;
    }

    public void setCurrent_qty(BigDecimal current_qty) {
        this.current_qty = current_qty;
    }

    public BigDecimal getUsed_qty() {
        return used_qty;
    }

    public void setUsed_qty(BigDecimal used_qty) {
        this.used_qty = used_qty;
    }

    public BigDecimal getNew_qty() {
        return new_qty;
    }

    public void setNew_qty(BigDecimal new_qty) {
        this.new_qty = new_qty;
    }

    public BigDecimal getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(BigDecimal total_qty) {
        this.total_qty = total_qty;
    }

    public IdNameTO getProject() {
        return project;
    }

    public void setProject(IdNameTO project) {
        this.project = project;
    }
}
