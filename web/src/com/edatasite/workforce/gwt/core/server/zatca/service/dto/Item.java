package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("Name")
    String name = "";

    @SerializedName("SellersItemID")
    String sellersItemID = "";

    @SerializedName("BuyerItemID")
    String buyerItemID = "";

    @SerializedName("StdItemID")
    String stdItemID = "";

    @SerializedName("Name.AR")
    String nameAr = "";

    @SerializedName("SellersItemID.AR")
    String sellersItemIDAr = "";

    @SerializedName("BuyerItemID.AR")
    String buyerItemIDAr = "";

    @SerializedName("StdItemID.AR")
    String stdItemIDAr = "";

    @SerializedName("ClasTaxCat")
    ClasTaxCat clasTaxCat;

    @SerializedName("Price")
    Price price;

    @SerializedName("AlwChg")
    AlwChg itemAlwChg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellersItemID() {
        return sellersItemID;
    }

    public void setSellersItemID(String sellersItemID) {
        this.sellersItemID = sellersItemID;
    }

    public String getBuyerItemID() {
        return buyerItemID;
    }

    public void setBuyerItemID(String buyerItemID) {
        this.buyerItemID = buyerItemID;
    }

    public String getStdItemID() {
        return stdItemID;
    }

    public void setStdItemID(String stdItemID) {
        this.stdItemID = stdItemID;
    }

    public ClasTaxCat getClasTaxCat() {
        return clasTaxCat;
    }

    public void setClasTaxCat(ClasTaxCat clasTaxCat) {
        this.clasTaxCat = clasTaxCat;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public AlwChg getAlwChg() {
        return itemAlwChg;
    }

    public void setAlwChg(AlwChg alwChg) {
        this.itemAlwChg = alwChg;
    }
}
