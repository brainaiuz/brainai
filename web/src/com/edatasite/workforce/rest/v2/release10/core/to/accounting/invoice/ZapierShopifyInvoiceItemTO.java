package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class ZapierShopifyInvoiceItemTO extends ResponseData {
    private String item_name;
    private String item_number;
    private String product_type;
    private String item_description;
    @Schema(required = true)
    private BigDecimal item_quantity;
    @Schema(required = true)
    private BigDecimal item_price;
    private BigDecimal item_net_amount;
    private BigDecimal item_discount_amount;
    private BigDecimal item_tax_amount;
    private String item_tax_name;
    @Schema(required = true)
    private InvoiceSalesAccountTO item_sales_account;
    @Schema(required = true)
    private WarehouseTO item_warehouse;
    private Integer department;

    public ZapierShopifyInvoiceItemTO() {
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public BigDecimal getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(BigDecimal item_quantity) {
        this.item_quantity = item_quantity;
    }

    public BigDecimal getItem_price() {
        return item_price;
    }

    public void setItem_price(BigDecimal item_price) {
        this.item_price = item_price;
    }

    public BigDecimal getItem_net_amount() {
        return item_net_amount;
    }

    public void setItem_net_amount(BigDecimal item_net_amount) {
        this.item_net_amount = item_net_amount;
    }

    public BigDecimal getItem_discount_amount() {
        return item_discount_amount;
    }

    public void setItem_discount_amount(BigDecimal item_discount_amount) {
        this.item_discount_amount = item_discount_amount;
    }

    public BigDecimal getItem_tax_amount() {
        return item_tax_amount;
    }

    public void setItem_tax_amount(BigDecimal item_tax_amount) {
        this.item_tax_amount = item_tax_amount;
    }

    public String getItem_tax_name() {
        return item_tax_name;
    }

    public void setItem_tax_name(String item_tax_name) {
        this.item_tax_name = item_tax_name;
    }

    public InvoiceSalesAccountTO getItem_sales_account() {
        return item_sales_account;
    }

    public void setItem_sales_account(InvoiceSalesAccountTO item_sales_account) {
        this.item_sales_account = item_sales_account;
    }

    public WarehouseTO getItem_warehouse() {
        return item_warehouse;
    }

    public void setItem_warehouse(WarehouseTO item_warehouse) {
        this.item_warehouse = item_warehouse;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }
}
