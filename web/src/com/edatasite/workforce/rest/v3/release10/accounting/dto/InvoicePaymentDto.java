package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Normurod Buriev.
 * Date: 11/19/2020 5:48 PM
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"properties"})
public class InvoicePaymentDto {
    @NotNull(message = "Payment is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;
    @NotNull(message = "Payment amount is required")
    @Min(value = 0, message = "Payment amount could not be less that ZERO.")
    private BigDecimal amount;
    @JsonAlias({"currency", "currencyCode", "currency_code"})
    private String currency;
    @JsonAlias({"exchangeRate", "exchange_rate", "exchange"})
    private BigDecimal exchangeRate = BigDecimal.ONE;
    private ItemDto method;
    @NotNull(message = "Payment account is required.")
    private IdCode account;
    private String reference;

    /**
     * This one's for additional fields if needed
     */
    @JsonIgnore
    private Map<String, String> properties;

    public InvoicePaymentDto() {
    }

    @JsonAnySetter
    public void addProperty(String key, String value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, String> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public ItemDto getMethod() {
        return method;
    }

    public void setMethod(ItemDto method) {
        this.method = method;
    }

    public IdCode getAccount() {
        return account;
    }

    public void setAccount(IdCode account) {
        this.account = account;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
