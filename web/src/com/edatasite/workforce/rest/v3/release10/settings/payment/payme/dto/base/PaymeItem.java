package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class PaymeItem {
    private String code;
    private String title;
    private Long price;
    private Integer count;
    @SerializedName("package_code")
    private String packageCode;
    @SerializedName("vat_percent")
    private double vatPercent;
    private BigDecimal discount;

    public PaymeItem() {

    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public Long getPrice() {
        return price;
    }

    public Integer getCount() {
        return count;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public double getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(double vatPercent) {
        this.vatPercent = vatPercent;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
