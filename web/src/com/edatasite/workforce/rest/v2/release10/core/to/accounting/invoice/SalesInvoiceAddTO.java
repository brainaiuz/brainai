package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.PaymentTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class SalesInvoiceAddTO extends InvoiceListItemTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Schema(required = true)
    private String invoice_due_date;
    private String reference;
    @Schema(required = true)
    private InvoiceCustomerTO customer;
    private BigDecimal exchange_rate;
    private String introduction;
    private Integer payment_account_id;

    @Schema(required = true)
    private ArrayList<InvoiceItemTO> invoice_items;
    private ArrayList<PaymentTO> payments;

    private ArrayList<ZapierShopifyTaxItemTO> tax_line_items;
    private BigDecimal tax_total;
    private BigDecimal discount_total;

    private List<Object> custom_fields;
    @Pattern(regexp = "NO_TAX|TAX_INCLUSIVE|TAX_EXCLUSIVE", message = "taxCalculationType must be one of NO_TAX/TAX_INCLUSIVE/TAX_EXCLUSIVE")
    private String tax_calculation_type;
    private Integer bank_account_id;

    public SalesInvoiceAddTO() {
    }

    public String getInvoice_due_date() {
        return invoice_due_date;
    }

    public void setInvoice_due_date(String invoice_due_date) {
        this.invoice_due_date = invoice_due_date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getPayment_account_id() {
        return payment_account_id;
    }

    public void setPayment_account_id(Integer payment_account_id) {
        this.payment_account_id = payment_account_id;
    }

    public ArrayList<InvoiceItemTO> getInvoice_items() {
        return invoice_items;
    }

    public void setInvoice_items(ArrayList<InvoiceItemTO> invoice_items) {
        this.invoice_items = invoice_items;
    }

    public ArrayList<PaymentTO> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<PaymentTO> payments) {
        this.payments = payments;
    }

    public ArrayList<ZapierShopifyTaxItemTO> getTax_line_items() {
        return tax_line_items;
    }

    public void setTax_line_items(ArrayList<ZapierShopifyTaxItemTO> tax_line_items) {
        this.tax_line_items = tax_line_items;
    }

    public BigDecimal getTax_total() {
        return tax_total;
    }

    public void setTax_total(BigDecimal tax_total) {
        this.tax_total = tax_total;
    }

    public BigDecimal getDiscount_total() {
        return discount_total;
    }

    public void setDiscount_total(BigDecimal discount_total) {
        this.discount_total = discount_total;
    }

    public List<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(List<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public String getTax_calculation_type() {
        return tax_calculation_type;
    }

    public void setTax_calculation_type(String tax_calculation_type) {
        this.tax_calculation_type = tax_calculation_type;
    }

    public Integer getBank_account_id() {
        return bank_account_id;
    }

    public void setBank_account_id(Integer bank_account_id) {
        this.bank_account_id = bank_account_id;
    }
}
