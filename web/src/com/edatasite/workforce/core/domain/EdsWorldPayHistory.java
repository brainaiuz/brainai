package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Murad Satimov
 * Date: 5/29/15 11:35 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "worldpay_history")
public class EdsWorldPayHistory extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String transId;
    private String transStatus;
    private Date transTime;
    private String authAmount;
    private String authCost;
    private String authCurrency;
    private String authAmountString;
    private String rawAuthMessage;
    private String rawAuthCode;
    private String callbackPW;
    private String cardType;
    private String countryMatch;
    private String AVS;
    private String wafMerchMessage;
    private String authentication;
    private String ipAddress;
    private String region;
    private String tel;
    private String address1;
    private String address2;
    private String cartId;
    private String address3;
    private String amountString;
    private String currency;
    private String amount;
    private String countryString;
    private String displayAddress;
    private String name;
    private String fax;
    private String compName;
    private String futurePayId;
    private String postcode;
    private String cost;
    private String country;
    private String email;
    private String address;
    private String town;
    private String authMode;
    private Boolean verified;
    private Integer subsId;
    private String customType;
    private Integer storefrontId;
    private String custom;
    private String payment_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usagepPlanId")
    private EdsUsagePlan usagepPlan;
    private String itemNumber;
    private String futurePayStatusChange;
    private String txn_type;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getAuthAmount() {
        return authAmount;
    }

    public void setAuthAmount(String authAmount) {
        this.authAmount = authAmount;
    }

    public String getAuthCost() {
        return authCost;
    }

    public void setAuthCost(String authCost) {
        this.authCost = authCost;
    }

    public String getAuthCurrency() {
        return authCurrency;
    }

    public void setAuthCurrency(String authCurrency) {
        this.authCurrency = authCurrency;
    }

    public String getAuthAmountString() {
        return authAmountString;
    }

    public void setAuthAmountString(String authAmountString) {
        this.authAmountString = authAmountString;
    }

    public String getRawAuthMessage() {
        return rawAuthMessage;
    }

    public void setRawAuthMessage(String rawAuthMessage) {
        this.rawAuthMessage = rawAuthMessage;
    }

    public String getRawAuthCode() {
        return rawAuthCode;
    }

    public void setRawAuthCode(String rawAuthCode) {
        this.rawAuthCode = rawAuthCode;
    }

    public String getCallbackPW() {
        return callbackPW;
    }

    public void setCallbackPW(String callbackPW) {
        this.callbackPW = callbackPW;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCountryMatch() {
        return countryMatch;
    }

    public void setCountryMatch(String countryMatch) {
        this.countryMatch = countryMatch;
    }

    public String getAVS() {
        return AVS;
    }

    public void setAVS(String AVS) {
        this.AVS = AVS;
    }

    public String getWafMerchMessage() {
        return wafMerchMessage;
    }

    public void setWafMerchMessage(String wafMerchMessage) {
        this.wafMerchMessage = wafMerchMessage;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCountryString() {
        return countryString;
    }

    public void setCountryString(String countryString) {
        this.countryString = countryString;
    }

    public String getDisplayAddress() {
        return displayAddress;
    }

    public void setDisplayAddress(String displayAddress) {
        this.displayAddress = displayAddress;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getFuturePayId() {
        return futurePayId;
    }

    public void setFuturePayId(String futurePayId) {
        this.futurePayId = futurePayId;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
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

    public EdsUsagePlan getUsagepPlan() {
        return usagepPlan;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getCustom() {
        return custom;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public void setUsagepPlan(EdsUsagePlan usagepPlan) {
        this.usagepPlan = usagepPlan;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public String getFuturePayStatusChange() {
        return futurePayStatusChange;
    }

    public void setFuturePayStatusChange(String futurePayStatusChange) {
        this.futurePayStatusChange = futurePayStatusChange;
    }

    public String getTxn_type() {
        return txn_type;
    }

    public void setTxn_type(String txn_type) {
        this.txn_type = txn_type;
    }
}
