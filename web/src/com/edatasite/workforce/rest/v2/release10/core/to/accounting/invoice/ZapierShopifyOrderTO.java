package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anvar Akramov on 16/7/2019.
 */
public class ZapierShopifyOrderTO extends ResponseData {

    String invoice_number;
    Long order_number;
    String status;
    String reference;
    Date due_date;
    BigDecimal subtotal;
    BigDecimal discount_total;
    BigDecimal tax_total;
    BigDecimal due_amount;
    BigDecimal shipping_price;
    private BigDecimal total;
    private String currency_code;

    @Schema(description = "Date Format (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String invoice_date;
    @Schema(description = "Date Format (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String cancel_at;
    private String customer_name;
    private String customer_firstname;
    private String customer_lastname;
    private String customer_email;
    private String customer_phone;

    String ship_to_name;
    String ship_to_type;
    String ship_to_address1;
    String ship_to_address2;
    String ship_to_city;
    String ship_to_country_code;
    String ship_to_state_code;
    String ship_to_postcode;
    Double ship_to_latitude;
    Double ship_to_longitude;

    String bill_to_name;
    String bill_to_type;
    String bill_to_address1;
    String bill_to_address2;
    String bill_to_city;
    String bill_to_country_code;
    String bill_to_state_code;
    String bill_to_postcode;
    Double bill_to_latitude;
    Double bill_to_longitude;

    String introduction;
    String shipping_method;
    String notes;
    /*SelectItemTO terms;
    String tax_type;
    SelectItemTO bank_account;
    SelectItemTO account;
    SelectItemTO email_template;*/
    ArrayList<ZapierShopifyInvoiceItemTO> line_items;
    ArrayList<ZapierShopifyTaxItemTO> tax_line_items;

    public ZapierShopifyOrderTO() {
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public BigDecimal getDiscount_total() {
        return discount_total;
    }

    public void setDiscount_total(BigDecimal discount_total) {
        this.discount_total = discount_total;
    }

    public BigDecimal getTax_total() {
        return tax_total;
    }

    public void setTax_total(BigDecimal tax_total) {
        this.tax_total = tax_total;
    }

    public BigDecimal getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(BigDecimal due_amount) {
        this.due_amount = due_amount;
    }

    public BigDecimal getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(BigDecimal shipping_price) {
        this.shipping_price = shipping_price;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_firstname() {
        return customer_firstname;
    }

    public void setCustomer_firstname(String customer_firstname) {
        this.customer_firstname = customer_firstname;
    }

    public String getCustomer_lastname() {
        return customer_lastname;
    }

    public void setCustomer_lastname(String customer_lastname) {
        this.customer_lastname = customer_lastname;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getShip_to_name() {
        return ship_to_name;
    }

    public void setShip_to_name(String ship_to_name) {
        this.ship_to_name = ship_to_name;
    }

    public String getShip_to_type() {
        return ship_to_type;
    }

    public void setShip_to_type(String ship_to_type) {
        this.ship_to_type = ship_to_type;
    }

    public String getShip_to_address1() {
        return ship_to_address1;
    }

    public void setShip_to_address1(String ship_to_address1) {
        this.ship_to_address1 = ship_to_address1;
    }

    public String getShip_to_address2() {
        return ship_to_address2;
    }

    public void setShip_to_address2(String ship_to_address2) {
        this.ship_to_address2 = ship_to_address2;
    }

    public String getShip_to_city() {
        return ship_to_city;
    }

    public void setShip_to_city(String ship_to_city) {
        this.ship_to_city = ship_to_city;
    }

    public String getShip_to_country_code() {
        return ship_to_country_code;
    }

    public void setShip_to_country_code(String ship_to_country_code) {
        this.ship_to_country_code = ship_to_country_code;
    }

    public String getShip_to_state_code() {
        return ship_to_state_code;
    }

    public void setShip_to_state_code(String ship_to_state_code) {
        this.ship_to_state_code = ship_to_state_code;
    }

    public String getShip_to_postcode() {
        return ship_to_postcode;
    }

    public void setShip_to_postcode(String ship_to_postcode) {
        this.ship_to_postcode = ship_to_postcode;
    }

    public Double getShip_to_latitude() {
        return ship_to_latitude;
    }

    public void setShip_to_latitude(Double ship_to_latitude) {
        this.ship_to_latitude = ship_to_latitude;
    }

    public Double getShip_to_longitude() {
        return ship_to_longitude;
    }

    public void setShip_to_longitude(Double ship_to_longitude) {
        this.ship_to_longitude = ship_to_longitude;
    }

    public String getBill_to_name() {
        return bill_to_name;
    }

    public void setBill_to_name(String bill_to_name) {
        this.bill_to_name = bill_to_name;
    }

    public String getBill_to_type() {
        return bill_to_type;
    }

    public void setBill_to_type(String bill_to_type) {
        this.bill_to_type = bill_to_type;
    }

    public String getBill_to_address1() {
        return bill_to_address1;
    }

    public void setBill_to_address1(String bill_to_address1) {
        this.bill_to_address1 = bill_to_address1;
    }

    public String getBill_to_address2() {
        return bill_to_address2;
    }

    public void setBill_to_address2(String bill_to_address2) {
        this.bill_to_address2 = bill_to_address2;
    }

    public String getBill_to_city() {
        return bill_to_city;
    }

    public void setBill_to_city(String bill_to_city) {
        this.bill_to_city = bill_to_city;
    }

    public String getBill_to_country_code() {
        return bill_to_country_code;
    }

    public void setBill_to_country_code(String bill_to_country_code) {
        this.bill_to_country_code = bill_to_country_code;
    }

    public String getBill_to_state_code() {
        return bill_to_state_code;
    }

    public void setBill_to_state_code(String bill_to_state_code) {
        this.bill_to_state_code = bill_to_state_code;
    }

    public String getBill_to_postcode() {
        return bill_to_postcode;
    }

    public void setBill_to_postcode(String bill_to_postcode) {
        this.bill_to_postcode = bill_to_postcode;
    }

    public Double getBill_to_latitude() {
        return bill_to_latitude;
    }

    public void setBill_to_latitude(Double bill_to_latitude) {
        this.bill_to_latitude = bill_to_latitude;
    }

    public Double getBill_to_longitude() {
        return bill_to_longitude;
    }

    public void setBill_to_longitude(Double bill_to_longitude) {
        this.bill_to_longitude = bill_to_longitude;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getShipping_method() {
        return shipping_method;
    }

    public void setShipping_method(String shipping_method) {
        this.shipping_method = shipping_method;
    }

    public ArrayList<ZapierShopifyInvoiceItemTO> getLine_items() {
        return line_items;
    }

    public void setLine_items(ArrayList<ZapierShopifyInvoiceItemTO> line_items) {
        this.line_items = line_items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public Long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(Long order_number) {
        this.order_number = order_number;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<ZapierShopifyTaxItemTO> getTax_line_items() {
        return tax_line_items;
    }

    public void setTax_line_items(ArrayList<ZapierShopifyTaxItemTO> tax_line_items) {
        this.tax_line_items = tax_line_items;
    }

    public String getCancel_at() {
        return cancel_at;
    }

    public void setCancel_at(String cancel_at) {
        this.cancel_at = cancel_at;
    }
}
