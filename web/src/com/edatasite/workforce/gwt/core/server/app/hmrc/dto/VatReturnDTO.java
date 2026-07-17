/*
 * Copyright (c) 2023.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.server.app.hmrc.dto;

import java.math.BigDecimal;


public class VatReturnDTO {
    private String periodKey;
    private BigDecimal vatDueSales;
    private BigDecimal vatDueAcquisitions = BigDecimal.ZERO;
    private BigDecimal totalVatDue;
    private BigDecimal vatReclaimedCurrPeriod;
    private BigDecimal netVatDue;
    private BigDecimal totalValueSalesExVAT;
    private BigDecimal totalValuePurchasesExVAT;
    private BigDecimal totalValueGoodsSuppliedExVAT = BigDecimal.ZERO;
    private BigDecimal totalAcquisitionsExVAT = BigDecimal.ZERO;
    private boolean finalised = true;

    public VatReturnDTO() {
    }

    public String getPeriodKey() {
        return periodKey;
    }

    public void setPeriodKey(String periodKey) {
        this.periodKey = periodKey;
    }

    public BigDecimal getVatDueSales() {
        return vatDueSales;
    }

    public void setVatDueSales(BigDecimal vatDueSales) {
        this.vatDueSales = vatDueSales;
    }

    public BigDecimal getVatDueAcquisitions() {
        return vatDueAcquisitions;
    }

    public void setVatDueAcquisitions(BigDecimal vatDueAcquisitions) {
        this.vatDueAcquisitions = vatDueAcquisitions;
    }

    public BigDecimal getTotalVatDue() {
        return totalVatDue;
    }

    public void setTotalVatDue(BigDecimal totalVatDue) {
        this.totalVatDue = totalVatDue;
    }

    public BigDecimal getVatReclaimedCurrPeriod() {
        return vatReclaimedCurrPeriod;
    }

    public void setVatReclaimedCurrPeriod(BigDecimal vatReclaimedCurrPeriod) {
        this.vatReclaimedCurrPeriod = vatReclaimedCurrPeriod;
    }

    public BigDecimal getNetVatDue() {
        return netVatDue;
    }

    public void setNetVatDue(BigDecimal netVatDue) {
        this.netVatDue = netVatDue;
    }

    public BigDecimal getTotalValueSalesExVAT() {
        return totalValueSalesExVAT;
    }

    public void setTotalValueSalesExVAT(BigDecimal totalValueSalesExVAT) {
        this.totalValueSalesExVAT = totalValueSalesExVAT;
    }

    public BigDecimal getTotalValuePurchasesExVAT() {
        return totalValuePurchasesExVAT;
    }

    public void setTotalValuePurchasesExVAT(BigDecimal totalValuePurchasesExVAT) {
        this.totalValuePurchasesExVAT = totalValuePurchasesExVAT;
    }

    public BigDecimal getTotalValueGoodsSuppliedExVAT() {
        return totalValueGoodsSuppliedExVAT;
    }

    public void setTotalValueGoodsSuppliedExVAT(BigDecimal totalValueGoodsSuppliedExVAT) {
        this.totalValueGoodsSuppliedExVAT = totalValueGoodsSuppliedExVAT;
    }

    public BigDecimal getTotalAcquisitionsExVAT() {
        return totalAcquisitionsExVAT;
    }

    public void setTotalAcquisitionsExVAT(BigDecimal totalAcquisitionsExVAT) {
        this.totalAcquisitionsExVAT = totalAcquisitionsExVAT;
    }

    public boolean isFinalised() {
        return finalised;
    }

    public void setFinalised(boolean finalised) {
        this.finalised = finalised;
    }
}
