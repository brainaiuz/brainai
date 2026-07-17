package com.edatasite.workforce.rest.v3.release10.accounting.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Normurod Buriev.
 * Date: 6/1/2021 6:43 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyCurrencyRateRequest {
    @NotNull(message = "Currency is required.")
    private String currency;
    @NotNull(message = "Date is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;
    @JsonAlias({"exchangerate", "exchangeRate", "rate"})
    @NotNull(message = "Currency is required.")
    private Double exchangeRate;

    public DailyCurrencyRateRequest() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
