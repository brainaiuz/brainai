package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;

/**
 * Created by Anvar Akramov on 27/3/2018.
 */
public class AssemblyItemTO extends ResponseData {

    private Integer assembly_item_id;
    private IdNameTO product;
    private Integer product_type;
    private String description;
    private BigDecimal quantity;
    private BigDecimal cost_price;
    private BigDecimal total;
    /*private BigDecimal product_price;
    private BigDecimal items_in_stock;
    private String date;
    private Integer warehouse_id;*/

    public AssemblyItemTO() {
    }

    public Integer getAssembly_item_id() {
        return assembly_item_id;
    }

    public void setAssembly_item_id(Integer assembly_item_id) {
        this.assembly_item_id = assembly_item_id;
    }

    public IdNameTO getProduct() {
        return product;
    }

    public void setProduct(IdNameTO product) {
        this.product = product;
    }

    public Integer getProduct_type() {
        return product_type;
    }

    public void setProduct_type(Integer product_type) {
        this.product_type = product_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost_price() {
        return cost_price;
    }

    public void setCost_price(BigDecimal cost_price) {
        this.cost_price = cost_price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

/*    public BigDecimal getProduct_price() {
        return product_price;
    }

    public void setProduct_price(BigDecimal product_price) {
        this.product_price = product_price;
    }

    public BigDecimal getItems_in_stock() {
        return items_in_stock;
    }

    public void setItems_in_stock(BigDecimal items_in_stock) {
        this.items_in_stock = items_in_stock;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(Integer warehouse_id) {
        this.warehouse_id = warehouse_id;
    }*/

}
