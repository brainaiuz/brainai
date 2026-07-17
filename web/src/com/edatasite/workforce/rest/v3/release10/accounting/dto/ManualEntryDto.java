package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ManualEntryDto extends DynamicDto {

    private Integer id;
    @NotNull(message = "Narration is required")
    private String narration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = DateDeserializer.class)
    @NotNull(message = "Date is required")
    private Date date;
    private String number;
    private String reference;
    private String currencyCode;
    private BigDecimal exchangeRate;
    @Pattern(regexp = "DRAFT|POST|SS_APROVED|SUBMITTED",
            message = "type must be one of DRAFT/POST/SS_APROVED/SUBMITTED")
    private String status;
    @NotNull(message = "items are required")
    @NotEmpty(message = "items cannot be empty")
    @Valid
    private List<ManualEntryLineItemDto> items;

    public ManualEntryDto() {
    }

    public ManualEntryDto(Integer id, String narration, Date date, String number, String reference, String currencyCode, BigDecimal exchangeRate, String status, List<ManualEntryLineItemDto> items) {
        this.id = id;
        this.narration = narration;
        this.date = date;
        this.number = number;
        this.reference = reference;
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
        this.status = status;
        this.items = items;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ManualEntryLineItemDto> getItems() {
        return items;
    }

    public void setItems(List<ManualEntryLineItemDto> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ManualEntryDto)) return false;

        ManualEntryDto that = (ManualEntryDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (narration != null ? !narration.equals(that.narration) : that.narration != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (reference != null ? !reference.equals(that.reference) : that.reference != null) return false;
        if (currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null) return false;
        if (exchangeRate != null ? !exchangeRate.equals(that.exchangeRate) : that.exchangeRate != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (items != null ? !items.equals(that.items) : that.items != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (narration != null ? narration.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (exchangeRate != null ? exchangeRate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ManualEntryDto{" +
                "id=" + id +
                ", narration='" + narration + '\'' +
                ", date=" + date +
                ", number='" + number + '\'' +
                ", reference='" + reference + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", exchangeRate=" + exchangeRate +
                ", status='" + status + '\'' +
                ", items=" + items +
                '}';
    }
}
