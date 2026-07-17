package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.enums.PaymentTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 25.12.2008
 * Time: 14:47:07
 * To change this template use File | Settings | File Templates.
 */

/*
This table name used to be EdsPayPalItem but we renamed it and using to store STRIPE data too.
And it became generic transaction storage. (Anvar Akramov)
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "subscription_payment")
public class EdsSubscriptionPayment extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "address_name")
    private String address_name;
    @Column(name = "subscr_id")
    private String subscr_id;
    @Column(name = "address_city")
    private String address_city;
    @Column(name = "residence_country")
    private String residence_country;
    @Column(name = "test_ipn")
    private String test_ipn;
    @Column(name = "payment_gross")
    private String payment_gross;
    @Column(name = "payment_date")
    private String payment_date;
    @Column(name = "address_zip")
    private String address_zip;
    @Column(name = "address_street")
    private String address_street;
    @Column(name = "protection_eligibility")
    private String protection_eligibility;
    @Column(name = "payer_id")
    private String payer_id;
    @Column(name = "verify_sign")
    private String verify_sign;
    @Column(name = "business")
    private String business;
    @Column(name = "address_country_code")
    private String address_country_code;
    @Column(name = "mc_fee")
    private String mc_fee;
    @Column(name = "address_status")
    private String address_status;
    @Column(name = "transaction_subject")
    private String transaction_subject;
    @Column(name = "notify_version")
    private String notify_version;
    @Column(name = "payment_fee")
    private String payment_fee;
    @Column(name = "item_name")
    private String item_name;
    @Column(name = "item_number")
    private String item_number;
    @Column(name = "payment_status")
    private String payment_status;
    @Column(name = "mc_gross")
    private String mc_gross;
    @Column(name = "mc_currency")
    private String mc_currency;
    @Column(name = "txn_id")
    private String txn_id;
    @Column(name = "txn_type")
    private String txn_type;
    @Column(name = "receiver_email")
    private String receiver_email;
    @Column(name = "payer_email")
    private String payer_email;
    @Column(name = "payer_status")
    private String payer_status;
    @Column(name = "charset")
    private String charset;
    @Column(name = "receiver_id")
    private String receiver_id;
    @Column(name = "address_country")
    private String address_country;
    @Column(name = "custom")
    private String custom;
    @Column(name = "address_state")
    private String address_state;
    @Column(name = "exchange_rate")
    private String exchange_rate;
    @Column(name = "settle_currency")
    private String settle_currency;
    @Column(name = "isVerified")
    private Boolean verified;  //

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usagePlan_id")
    private EdsUsagePlan usagePlan;*/
    //Instead we decided to use guid
    private String usageplan_guid;

    @Column(name = "amount3")
    private String amount3;

    @Column(name = "mc_amount3")
    private String mc_amount3;

    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentTypeEnum paymentType;
    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;
    @Column(name = "stripe_charge_id")
    private String stripeChargeId;
    @Column(name = "api_subscription_id")
    private String apiSubscrId;
    @Column(name = "subscr_payment_status")
    private String subscriptionPaymentStatus;

    private transient Integer subsId;

    private transient Integer storefrontId;

    private transient String customType;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(String exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getSettle_currency() {
        return settle_currency;
    }

    public void setSettle_currency(String settle_currency) {
        this.settle_currency = settle_currency;
    }

    public String getAddress_state() {
        return address_state;
    }

    public void setAddress_state(String address_state) {
        this.address_state = address_state;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getSubscr_id() {
        return subscr_id;
    }

    public void setSubscr_id(String subscr_id) {
        this.subscr_id = subscr_id;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getResidence_country() {
        return residence_country;
    }

    public void setResidence_country(String residence_country) {
        this.residence_country = residence_country;
    }

    public String getTest_ipn() {
        return test_ipn;
    }

    public void setTest_ipn(String test_ipn) {
        this.test_ipn = test_ipn;
    }

    public String getPayment_gross() {
        return payment_gross;
    }

    public void setPayment_gross(String payment_gross) {
        this.payment_gross = payment_gross;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getAddress_zip() {
        return address_zip;
    }

    public void setAddress_zip(String address_zip) {
        this.address_zip = address_zip;
    }

    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(String address_street) {
        this.address_street = address_street;
    }

    public String getProtection_eligibility() {
        return protection_eligibility;
    }

    public void setProtection_eligibility(String protection_eligibility) {
        this.protection_eligibility = protection_eligibility;
    }

    public String getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(String payer_id) {
        this.payer_id = payer_id;
    }

    public String getVerify_sign() {
        return verify_sign;
    }

    public void setVerify_sign(String verify_sign) {
        this.verify_sign = verify_sign;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getAddress_country_code() {
        return address_country_code;
    }

    public void setAddress_country_code(String address_country_code) {
        this.address_country_code = address_country_code;
    }

    public String getMc_fee() {
        return mc_fee;
    }

    public void setMc_fee(String mc_fee) {
        this.mc_fee = mc_fee;
    }

    public String getAddress_status() {
        return address_status;
    }

    public void setAddress_status(String address_status) {
        this.address_status = address_status;
    }

    public String getTransaction_subject() {
        return transaction_subject;
    }

    public void setTransaction_subject(String transaction_subject) {
        this.transaction_subject = transaction_subject;
    }

    public String getNotify_version() {
        return notify_version;
    }

    public void setNotify_version(String notify_version) {
        this.notify_version = notify_version;
    }

    public String getPayment_fee() {
        return payment_fee;
    }

    public void setPayment_fee(String payment_fee) {
        this.payment_fee = payment_fee;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getMc_gross() {
        return mc_gross;
    }

    public void setMc_gross(String mc_gross) {
        this.mc_gross = mc_gross;
    }

    public String getMc_currency() {
        return mc_currency;
    }

    public void setMc_currency(String mc_currency) {
        this.mc_currency = mc_currency;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getReceiver_email() {
        return receiver_email;
    }

    public void setReceiver_email(String receiver_email) {
        this.receiver_email = receiver_email;
    }

    public String getPayer_email() {
        return payer_email;
    }

    public void setPayer_email(String payer_email) {
        this.payer_email = payer_email;
    }

    public String getPayer_status() {
        return payer_status;
    }

    public void setPayer_status(String payer_status) {
        this.payer_status = payer_status;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getAddress_country() {
        return address_country;
    }

    public void setAddress_country(String address_country) {
        this.address_country = address_country;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getUsageplan_guid() {
        return usageplan_guid;
    }

    public void setUsageplan_guid(String usageplan_guid) {
        this.usageplan_guid = usageplan_guid;
    }

    public String getTxn_type() {
        return txn_type;
    }

    public void setTxn_type(String txn_type) {
        this.txn_type = txn_type;
    }

    public String getAmount3() {
        return amount3;
    }

    public void setAmount3(String amount3) {
        this.amount3 = amount3;
    }

    public String getMc_amount3() {
        return mc_amount3;
    }

    public void setMc_amount3(String mc_amount3) {
        this.mc_amount3 = mc_amount3;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getSubsId() {
        return subsId;
    }

    public void setSubsId(Integer subsId) {
        this.subsId = subsId;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public Integer getStorefrontId() {
        return storefrontId;
    }

    public void setStorefrontId(Integer storefrontId) {
        this.storefrontId = storefrontId;
    }

    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeChargeId() {
        return stripeChargeId;
    }

    public void setStripeChargeId(String stripeChargeId) {
        this.stripeChargeId = stripeChargeId;
    }

    public String getApiSubscrId() {
        return apiSubscrId;
    }

    public void setApiSubscrId(String apiSubscrId) {
        this.apiSubscrId = apiSubscrId;
    }

    public String getSubscriptionPaymentStatus() {
        return subscriptionPaymentStatus;
    }

    public void setSubscriptionPaymentStatus(String subscriptionPaymentStatus) {
        this.subscriptionPaymentStatus = subscriptionPaymentStatus;
    }
}
