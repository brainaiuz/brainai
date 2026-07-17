package com.edatasite.workforce.gwt.invoice.server.app.multiquoteconverter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 6/10/2020 8:28 PM
 */
public class ConvertedQuotesDto implements Serializable {
    private String methodKey;
    private String progInvoiceType;
    private List<Integer> quoteIds;

    public ConvertedQuotesDto() {
    }

    public ConvertedQuotesDto(String methodKey) {
        this.methodKey = methodKey;
    }

    public ConvertedQuotesDto(String methodKey, String progInvoiceType, List<Integer> quoteIds) {
        this.methodKey = methodKey;
        this.progInvoiceType = progInvoiceType;
        this.quoteIds = quoteIds;
    }

    public String getMethodKey() {
        return methodKey;
    }

    public void setMethodKey(String methodKey) {
        this.methodKey = methodKey;
    }

    public String getProgInvoiceType() {
        return progInvoiceType;
    }

    public void setProgInvoiceType(String progInvoiceType) {
        this.progInvoiceType = progInvoiceType;
    }

    public List<Integer> getQuoteIds() {
        return quoteIds;
    }

    public void setQuoteIds(List<Integer> quoteIds) {
        this.quoteIds = quoteIds;
    }
}
