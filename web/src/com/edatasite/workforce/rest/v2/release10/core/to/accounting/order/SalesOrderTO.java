package com.edatasite.workforce.rest.v2.release10.core.to.accounting.order;


import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceCustomerTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceItemTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d on 09/09/2019.
 */
public class SalesOrderTO extends SalesOrderListTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Schema(required = true)
    private String due_date;

    @Schema(required = true)
    private InvoiceCustomerTO customer;
    private BigDecimal exchange_rate;
    private String reference;
    private String introduction;
    private Integer tax_calculation_type;

    @Schema(required = true)
    private ArrayList<InvoiceItemTO> items;

    private List<Object> custom_fields;

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public InvoiceCustomerTO getCustomer() {
        return customer;
    }

    public void setCustomer(InvoiceCustomerTO customer) {
        this.customer = customer;
    }

    public BigDecimal getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(BigDecimal exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public ArrayList<InvoiceItemTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<InvoiceItemTO> items) {
        this.items = items;
    }

    public Integer getTax_calculation_type() {
        return tax_calculation_type;
    }

    public void setTax_calculation_type(Integer tax_calculation_type) {
        this.tax_calculation_type = tax_calculation_type;
    }

    public List<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(List<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }
}
