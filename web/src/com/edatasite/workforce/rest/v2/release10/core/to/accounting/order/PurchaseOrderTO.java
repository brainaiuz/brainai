package com.edatasite.workforce.rest.v2.release10.core.to.accounting.order;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.SupplierItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 3/16/18.
 */
public class PurchaseOrderTO extends ResponseData {

    private Integer id;
    private String invoice_number;
    @Schema(description = "Date Format (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String invoice_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Schema(required = true)
    private String due_date;
    private CurrencyTO currency;
    private SupplierItemTO supplier;
    private InvoiceStatusTO status;
    private String quote_number;
    private String reference;
    private String introduction;
    private String payment_terms;
    private String shipping_terms;
    private Integer tax_calculation_type;
    private IdNameTO related_project;
    private IdNameTO creator;
    private IdNameTO purchase_order_manager;
    private IdNameTO price_level;
    private IdNameTO shipping_method;

    @Schema(required = true)
    private ArrayList<InvoiceItemTO> items;

    private BigDecimal total_in_base_currency; //total amount in base currency
    private BigDecimal total_taxes_in_purchase_currency; // total amount of taxes in purchaseorder's currency
    private BigDecimal total_in_purchase_currency;  //total amount in purchaseorder's currency
    private BigDecimal comission;//
    private BigDecimal due_payments;//
    private BigDecimal subtotal;
    private BigDecimal total_taxes_in_base_currency;
    private BigDecimal total_discount;
    private BigDecimal shipping_price;
    private BigDecimal exchange_rate;//Currency's exchange rate

    public PurchaseOrderTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public SupplierItemTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierItemTO supplier) {
        this.supplier = supplier;
    }

    public InvoiceStatusTO getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatusTO status) {
        this.status = status;
    }

    public String getQuote_number() {
        return quote_number;
    }

    public void setQuote_number(String quote_number) {
        this.quote_number = quote_number;
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

    public IdNameTO getRelated_project() {
        return related_project;
    }

    public void setRelated_project(IdNameTO related_project) {
        this.related_project = related_project;
    }

    public IdNameTO getCreator() {
        return creator;
    }

    public void setCreator(IdNameTO creator) {
        this.creator = creator;
    }

    public IdNameTO getPurchase_order_manager() {
        return purchase_order_manager;
    }

    public void setPurchase_order_manager(IdNameTO purchase_order_manager) {
        this.purchase_order_manager = purchase_order_manager;
    }

    public IdNameTO getPrice_level() {
        return price_level;
    }

    public void setPrice_level(IdNameTO price_level) {
        this.price_level = price_level;
    }

    public IdNameTO getShipping_method() {
        return shipping_method;
    }

    public void setShipping_method(IdNameTO shipping_method) {
        this.shipping_method = shipping_method;
    }

    public BigDecimal getTotal_in_base_currency() {
        return total_in_base_currency;
    }

    public void setTotal_in_base_currency(BigDecimal total_in_base_currency) {
        this.total_in_base_currency = total_in_base_currency;
    }

    public ArrayList<InvoiceItemTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<InvoiceItemTO> items) {
        this.items = items;
    }

    public BigDecimal getTotal_taxes_in_purchase_currency() {
        return total_taxes_in_purchase_currency;
    }

    public void setTotal_taxes_in_purchase_currency(BigDecimal total_taxes_in_purchase_currency) {
        this.total_taxes_in_purchase_currency = total_taxes_in_purchase_currency;
    }

    public BigDecimal getTotal_in_purchase_currency() {
        return total_in_purchase_currency;
    }

    public void setTotal_in_purchase_currency(BigDecimal total_in_purchase_currency) {
        this.total_in_purchase_currency = total_in_purchase_currency;
    }

    public BigDecimal getDue_payments() {
        return due_payments;
    }

    public void setDue_payments(BigDecimal due_payments) {
        this.due_payments = due_payments;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal_taxes_in_base_currency() {
        return total_taxes_in_base_currency;
    }

    public void setTotal_taxes_in_base_currency(BigDecimal total_taxes_in_base_currency) {
        this.total_taxes_in_base_currency = total_taxes_in_base_currency;
    }

    public BigDecimal getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(BigDecimal exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public BigDecimal getComission() {
        return comission;
    }

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

    public String getPayment_terms() {
        return payment_terms;
    }

    public void setPayment_terms(String payment_terms) {
        this.payment_terms = payment_terms;
    }

    public String getShipping_terms() {
        return shipping_terms;
    }

    public void setShipping_terms(String shipping_terms) {
        this.shipping_terms = shipping_terms;
    }

    public BigDecimal getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(BigDecimal total_discount) {
        this.total_discount = total_discount;
    }

    public BigDecimal getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(BigDecimal shipping_price) {
        this.shipping_price = shipping_price;
    }

    public Integer getTax_calculation_type() {
        return tax_calculation_type;
    }

    public void setTax_calculation_type(Integer tax_calculation_type) {
        this.tax_calculation_type = tax_calculation_type;
    }
}
