package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataItemStatus;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * User: Murad Satimov
 * Date: 1/11/18 7:19 PM
 */
public class ShippingDataItem implements IsSerializable {
    private Integer id;
    private SelectItem item;
    private SelectItem warehouse;
    private BigDecimal amount;
    private BigDecimal net;
    private ReceiveTypeEnum receiveType;
    private ShippingDataItemStatus status;
    private BigDecimal receivedAllocation;
    private BigDecimal numberOfPacks;
    private CompanyCustomFieldItem articleNumberCF;
    private Boolean trackBatchesEnabled;
    private ArrayList<ProductTrackBatchItem> batchItems;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SelectItem getItem() {
        return item;
    }

    public void setItem(SelectItem item) {
        this.item = item;
    }

    public SelectItem getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItem warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getReceivedAllocation() {
        return receivedAllocation;
    }

    public void setReceivedAllocation(BigDecimal receivedAllocation) {
        this.receivedAllocation = receivedAllocation;
    }

    public ReceiveTypeEnum getReceiveType() {
        return Optional.ofNullable(receiveType).orElse(ReceiveTypeEnum.RECEIVE_BY_QTY);
    }

    public void setReceiveType(ReceiveTypeEnum receiveType) {
        this.receiveType = receiveType;
    }

    public ShippingDataItemStatus getStatus() {
        return status;
    }

    public void setStatus(ShippingDataItemStatus status) {
        this.status = status;
    }

    public BigDecimal getNumberOfPacks() {
        return numberOfPacks;
    }

    public void setNumberOfPacks(BigDecimal numberOfPacks) {
        this.numberOfPacks = numberOfPacks;
    }

    public CompanyCustomFieldItem getArticleNumberCF() {
        return articleNumberCF;
    }

    public void setArticleNumberCF(CompanyCustomFieldItem articleNumberCF) {
        this.articleNumberCF = articleNumberCF;
    }

    public Boolean getTrackBatchesEnabled() {
        return trackBatchesEnabled;
    }

    public void setTrackBatchesEnabled(Boolean trackBatchesEnabled) {
        this.trackBatchesEnabled = trackBatchesEnabled;
    }

    public ArrayList<ProductTrackBatchItem> getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(ArrayList<ProductTrackBatchItem> batchItems) {
        this.batchItems = batchItems;
    }
}
