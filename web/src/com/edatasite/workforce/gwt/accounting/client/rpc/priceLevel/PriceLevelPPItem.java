package com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 6:38:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceLevelPPItem implements IsSerializable {

    private Integer objectId;
    private Integer productID;
    public Integer priceLevelID;// Hayot: Why I'm doing it public? I use it only once! As others are working with getter/setters they will notice this change will not use it.
    private String productName;
    private Double customPrice;
    private Double standarPrice;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Double getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(Double customPrice) {
        this.customPrice = customPrice;
    }

    public Double getStandarPrice() {
        return standarPrice;
    }

    public void setStandarPrice(Double standarPrice) {
        this.standarPrice = standarPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
