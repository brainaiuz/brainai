package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiveReaderDto {
    @JsonProperty("vendor_name")
    private String vendor;

    @JsonProperty("tax")
    private String vat;

    @JsonProperty("line_items")
    private List<ReceiveReaderItemDto> lineItems;

    public ReceiveReaderDto() {
    }

    public ReceiveReaderDto(String vendor, String vat, List<ReceiveReaderItemDto> lineItems) {
        this.vendor = vendor;
        this.vat = vat;
        this.lineItems = lineItems;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public List<ReceiveReaderItemDto> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<ReceiveReaderItemDto> lineItems) {
        this.lineItems = lineItems;
    }
}
