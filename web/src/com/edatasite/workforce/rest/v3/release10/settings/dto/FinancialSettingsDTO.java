package com.edatasite.workforce.rest.v3.release10.settings.dto;

import com.edatasite.workforce.rest.base.to.CurrencyTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class FinancialSettingsDTO implements Serializable {
    private CurrencyTO baseCurrency;
    private Integer calculationScale;
    private Integer roundingExchRate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date conversionDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date financialYearEnd;
    private boolean enableIT;


    public Integer getCalculationScale() {
        return calculationScale;
    }

    public void setCalculationScale(Integer calculationScale) {
        this.calculationScale = calculationScale;
    }

    public Integer getRoundingExchRate() {
        return roundingExchRate;
    }

    public void setRoundingExchRate(Integer roundingExchRate) {
        this.roundingExchRate = roundingExchRate;
    }

    public Date getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(Date conversionDate) {
        this.conversionDate = conversionDate;
    }

    public Date getFinancialYearEnd() {
        return financialYearEnd;
    }

    public void setFinancialYearEnd(Date financialYearEnd) {
        this.financialYearEnd = financialYearEnd;
    }

    public boolean isEnableIT() {
        return enableIT;
    }

    public void setEnableIT(boolean enableIT) {
        this.enableIT = enableIT;
    }

    public CurrencyTO getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyTO baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
}
