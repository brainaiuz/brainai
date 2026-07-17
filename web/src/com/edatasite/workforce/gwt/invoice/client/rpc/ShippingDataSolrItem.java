package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShippingDataSolrItem implements IsSerializable {

    private Integer objectId;
    private String shippingDataNumber;
    private String quoteNumber;
    private Date shippingDate;
    private SelectItem client;
    private SelectItem currency;
    private String shippingDataStatusName;
    private SelectItem creator;
    private Integer creatorLocationId;
    private Date creationDate;
    private Boolean isGdn;
    private Integer quoteId;
    private List<Integer> warehouseIds = new ArrayList<>();

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getShippingDataNumber() {
        return shippingDataNumber;
    }

    public void setShippingDataNumber(String shippingDataNumber) {
        this.shippingDataNumber = shippingDataNumber;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public String getShippingDataStatusName() {
        return shippingDataStatusName;
    }

    public void setShippingDataStatusName(String shippingDataStatusName) {
        this.shippingDataStatusName = shippingDataStatusName;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public Integer getCreatorLocationId() {
        return creatorLocationId;
    }

    public void setCreatorLocationId(Integer creatorLocationId) {
        this.creatorLocationId = creatorLocationId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getGdn() {
        return isGdn;
    }

    public void setGdn(Boolean gdn) {
        isGdn = gdn;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public List<Integer> getWarehouseIds() {
        return warehouseIds;
    }

    public void setWarehouseIds(List<Integer> warehouseIds) {
        this.warehouseIds = warehouseIds;
    }
}
