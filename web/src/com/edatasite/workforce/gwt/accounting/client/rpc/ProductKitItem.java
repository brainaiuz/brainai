package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 1, 2010
 * Time: 5:47:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductKitItem extends SelectItem {

    private Integer productKitID;
    
    private ProductSelectItem productItem;
    private BigDecimal quantity;

    private String price;
    private String cost;
    private String tax;
    private String subtotal;
    private SelectItem warehouse;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getProductKitID() {
        return productKitID;
    }

    public void setProductKitID(Integer productKitID) {
        this.productKitID = productKitID;
    }

    public ProductSelectItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductSelectItem productItem) {
        this.productItem = productItem;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public SelectItem getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItem warehouse) {
        this.warehouse = warehouse;
    }
}
