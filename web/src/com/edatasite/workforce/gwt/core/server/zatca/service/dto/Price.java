package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Price {
    @SerializedName("PriceAmt")
    BigDecimal priceAmt;

    @SerializedName("BaseQty")
    String baseQty;

    @SerializedName("BaseQtyUoM")
    String baseQtyUoM;

    @SerializedName("BaseQtyUoM.AR")
    String baseQtyUoMAr;

    public BigDecimal getPriceAmt() {
        return priceAmt;
    }

    public void setPriceAmt(BigDecimal priceAmt) {
        this.priceAmt = priceAmt;
    }

    public String getBaseQty() {
        return baseQty;
    }

    public void setBaseQty(String baseQty) {
        this.baseQty = baseQty;
    }

    public String getBaseQtyUoM() {
        return baseQtyUoM;
    }

    public void setBaseQtyUoM(String baseQtyUoM) {
        this.baseQtyUoM = baseQtyUoM;
    }
}
