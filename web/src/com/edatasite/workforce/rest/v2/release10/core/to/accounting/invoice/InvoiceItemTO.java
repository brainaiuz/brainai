package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class InvoiceItemTO extends ResponseData {
    @Schema(required = true)
    private ItemTO item;
    private String item_description;
    @Schema(required = true)
    private BigDecimal item_quantity;
    @Schema(required = true)
    private BigDecimal item_price;
    private BigDecimal item_net_amount;
    private BigDecimal total_discount;
    @Schema(required = true)
    private InvoiceSalesAccountTO item_sales_account;
    @Schema(required = true)
    private WarehouseTO item_warehouse;
    private Integer department;
    private Long zapiervariantid;//were added for zapier/shopify integration
    private ZapierShopifyTaxItemTO tax_item;
    private ArrayList<ProductTrackBatchItem> batchItems;

    public InvoiceItemTO() {
    }

    public ItemTO getItem() {
        return item;
    }

    public void setItem(ItemTO item) {
        this.item = item;
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

    public BigDecimal getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(BigDecimal total_discount) {
        this.total_discount = total_discount;
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

    public Long getZapiervariantid() {
        return zapiervariantid;
    }

    public void setZapiervariantid(Long zapiervariantid) {
        this.zapiervariantid = zapiervariantid;
    }

    public ZapierShopifyTaxItemTO getTax_item() {
        return tax_item;
    }

    public void setTax_item(ZapierShopifyTaxItemTO tax_item) {
        this.tax_item = tax_item;
    }

    public ArrayList<ProductTrackBatchItem> getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(ArrayList<ProductTrackBatchItem> batchItems) {
        this.batchItems = batchItems;
    }
}
