package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ClasTaxCat {
    @SerializedName("ID")
    String id;

    @SerializedName("Percent")
    BigDecimal percent;

    @SerializedName("TaxExemptionReasonCd")
    String taxExemptionReasonCd;

    @SerializedName("TaxExemptionReason")
    String taxExemptionReason;

    @SerializedName("ID.AR")
    String idAr;

    @SerializedName("Percent.AR")
    String percentAr;

    @SerializedName("TaxExemptionReasonCd.AR")
    String taxExemptionReasonCdAr;

    @SerializedName("TaxExemptionReason.AR")
    String taxExemptionReasonAr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public String getTaxExemptionReasonCd() {
        return taxExemptionReasonCd;
    }

    public void setTaxExemptionReasonCd(String taxExemptionReasonCd) {
        this.taxExemptionReasonCd = taxExemptionReasonCd;
    }

    public String getTaxExemptionReason() {
        return taxExemptionReason;
    }

    public void setTaxExemptionReason(String taxExemptionReason) {
        this.taxExemptionReason = taxExemptionReason;
    }
}
