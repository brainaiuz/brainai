package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.base.to.AddressTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.ShippingMethodTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.CustomerTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anvar Akramov on 11/2/2019.
 */
public class ZapierInvoiceItemTO extends InvoiceListItemTO {

    String reference;
    Date due_date;
    BigDecimal subtotal;

    BigDecimal base_total;
    BigDecimal base_subtotal;
    BigDecimal discount_total;
    BigDecimal base_discount_total;
    BigDecimal tax_total;
    BigDecimal base_tax_total;
    BigDecimal due_amount;
    BigDecimal exchange_rate;
    BigDecimal shipping_price;
    CustomerTO customer;
    SelectItemTO supplier;
    com.edatasite.workforce.rest.base.to.AddressTO bill_to_address;
    AddressTO ship_to_address;
    String introduction;
    SelectItemTO invoice_type;
    ShippingMethodTO shipping_method;
    SelectItemTO terms;
    SelectItemTO tax_type;
    SelectItemTO bank_account;
    SelectItemTO account;
    SelectItemTO email_template;
    ArrayList<InvoiceItemTO> items;
    String notes;

    public ZapierInvoiceItemTO() {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getBase_total() {
        return base_total;
    }

    public void setBase_total(BigDecimal base_total) {
        this.base_total = base_total;
    }

    public BigDecimal getBase_subtotal() {
        return base_subtotal;
    }

    public void setBase_subtotal(BigDecimal base_subtotal) {
        this.base_subtotal = base_subtotal;
    }

    public BigDecimal getDiscount_total() {
        return discount_total;
    }

    public void setDiscount_total(BigDecimal discount_total) {
        this.discount_total = discount_total;
    }

    public BigDecimal getBase_discount_total() {
        return base_discount_total;
    }

    public void setBase_discount_total(BigDecimal base_discount_total) {
        this.base_discount_total = base_discount_total;
    }

    public BigDecimal getTax_total() {
        return tax_total;
    }

    public void setTax_total(BigDecimal tax_total) {
        this.tax_total = tax_total;
    }

    public BigDecimal getBase_tax_total() {
        return base_tax_total;
    }

    public void setBase_tax_total(BigDecimal base_tax_total) {
        this.base_tax_total = base_tax_total;
    }

    public BigDecimal getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(BigDecimal due_amount) {
        this.due_amount = due_amount;
    }

    public BigDecimal getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(BigDecimal exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public BigDecimal getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(BigDecimal shipping_price) {
        this.shipping_price = shipping_price;
    }

    public CustomerTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerTO customer) {
        this.customer = customer;
    }

    public SelectItemTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SelectItemTO supplier) {
        this.supplier = supplier;
    }

    public AddressTO getBill_to_address() {
        return bill_to_address;
    }

    public void setBill_to_address(AddressTO bill_to_address) {
        this.bill_to_address = bill_to_address;
    }

    public AddressTO getShip_to_address() {
        return ship_to_address;
    }

    public void setShip_to_address(AddressTO ship_to_address) {
        this.ship_to_address = ship_to_address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public SelectItemTO getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(SelectItemTO invoice_type) {
        this.invoice_type = invoice_type;
    }

    public ShippingMethodTO getShipping_method() {
        return shipping_method;
    }

    public void setShipping_method(ShippingMethodTO shipping_method) {
        this.shipping_method = shipping_method;
    }

    public SelectItemTO getTerms() {
        return terms;
    }

    public void setTerms(SelectItemTO terms) {
        this.terms = terms;
    }

    public SelectItemTO getTax_type() {
        return tax_type;
    }

    public void setTax_type(SelectItemTO tax_type) {
        this.tax_type = tax_type;
    }

    public SelectItemTO getBank_account() {
        return bank_account;
    }

    public void setBank_account(SelectItemTO bank_account) {
        this.bank_account = bank_account;
    }

    public SelectItemTO getAccount() {
        return account;
    }

    public void setAccount(SelectItemTO account) {
        this.account = account;
    }

    public SelectItemTO getEmail_template() {
        return email_template;
    }

    public void setEmail_template(SelectItemTO email_template) {
        this.email_template = email_template;
    }

    public ArrayList<InvoiceItemTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<InvoiceItemTO> items) {
        this.items = items;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
