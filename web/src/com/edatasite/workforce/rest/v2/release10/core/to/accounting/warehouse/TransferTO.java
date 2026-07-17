package com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse;

import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.WarehouseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anvar Akramov on 3/4/2018.
 */
public class TransferTO extends ResponseData {

    private Integer line_item_id;
    private Integer product_id;
    private String productname;
    private WarehouseTO warehouse;
    private Integer account_id;
    private ArrayList<ProductTrackBatchItem> batchItems;

    public TransferTO() {
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

    public WarehouseTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTO warehouse) {
        this.warehouse = warehouse;
    }

    public Integer getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public ArrayList<ProductTrackBatchItem> getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(ArrayList<ProductTrackBatchItem> batchItems) {
        this.batchItems = batchItems;
    }
}
