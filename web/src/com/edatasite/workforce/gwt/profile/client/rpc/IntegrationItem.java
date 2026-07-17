package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * User: Abror Abdukadirov
 * Date: 15.05.2019 16:18
 */
public class IntegrationItem implements IsSerializable {
    private Integer objectID;

    private String payPalMerchant;
    private SelectItem payPalBankAccount;

    private String stripeUserId;
    private SelectItem stripeBankAccount;

    private String googleCheckoutMerchant;
    private SelectItem googleCheckoutBankAccount;

    private boolean isMastercardPaymentEnabled;
    private String mastercardMerchantID;
    private String mastercardAccessCode;
    private String mastercardSecretKey;
    private SelectItem mastercardBankAccount;

    private boolean isElavonPaymentEnabled;
    private String elavonMerchantID;
    private String elavonUserID;
    private String elavonPIN;
    private SelectItem elavonBankAccount;

    private RecurrenceJobItem recurrenceItem;
    private boolean userOffice365Linked;
    private boolean userOffice365AccessToken;
    private boolean userOffice365Validated;
    private boolean userGoogleValidated;
    private boolean isPayPal;

    private String payMeMerchantId;
    private SelectItem paymeBankAccount;
    private SelectItem paymeExpenseAccount;
    private String paymeServiceId;
    private BigDecimal paymeServiceFee;

    private String clickMerchantId;
    private SelectItem clickBankAccount;
    private String clickServiceId;

    private String revolutEmail;
    private SelectItem revolutBankAccount;
    private SelectItem revolutExpenseAccount;
    private String revolutSecretApiKey;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getPayPalMerchant() {
        return payPalMerchant;
    }

    public void setPayPalMerchant(String payPalMerchant) {
        this.payPalMerchant = payPalMerchant;
    }

    public SelectItem getPayPalBankAccount() {
        return payPalBankAccount;
    }

    public void setPayPalBankAccount(SelectItem payPalBankAccount) {
        this.payPalBankAccount = payPalBankAccount;
    }

    public String getStripeUserId() {
        return stripeUserId;
    }

    public void setStripeUserId(String stripeUserId) {
        this.stripeUserId = stripeUserId;
    }

    public SelectItem getStripeBankAccount() {
        return stripeBankAccount;
    }

    public void setStripeBankAccount(SelectItem stripeBankAccount) {
        this.stripeBankAccount = stripeBankAccount;
    }

    public String getGoogleCheckoutMerchant() {
        return googleCheckoutMerchant;
    }

    public void setGoogleCheckoutMerchant(String googleCheckoutMerchant) {
        this.googleCheckoutMerchant = googleCheckoutMerchant;
    }

    public SelectItem getGoogleCheckoutBankAccount() {
        return googleCheckoutBankAccount;
    }

    public void setGoogleCheckoutBankAccount(SelectItem googleCheckoutBankAccount) {
        this.googleCheckoutBankAccount = googleCheckoutBankAccount;
    }

    public boolean isMastercardPaymentEnabled() {
        return isMastercardPaymentEnabled;
    }

    public void setMastercardPaymentEnabled(boolean mastercardPaymentEnabled) {
        isMastercardPaymentEnabled = mastercardPaymentEnabled;
    }

    public String getMastercardMerchantID() {
        return mastercardMerchantID;
    }

    public void setMastercardMerchantID(String mastercardMerchantID) {
        this.mastercardMerchantID = mastercardMerchantID;
    }

    public String getMastercardAccessCode() {
        return mastercardAccessCode;
    }

    public void setMastercardAccessCode(String mastercardAccessCode) {
        this.mastercardAccessCode = mastercardAccessCode;
    }

    public String getMastercardSecretKey() {
        return mastercardSecretKey;
    }

    public void setMastercardSecretKey(String mastercardSecretKey) {
        this.mastercardSecretKey = mastercardSecretKey;
    }

    public SelectItem getMastercardBankAccount() {
        return mastercardBankAccount;
    }

    public void setMastercardBankAccount(SelectItem mastercardBankAccount) {
        this.mastercardBankAccount = mastercardBankAccount;
    }

    public boolean isElavonPaymentEnabled() {
        return isElavonPaymentEnabled;
    }

    public void setElavonPaymentEnabled(boolean elavonPaymentEnabled) {
        isElavonPaymentEnabled = elavonPaymentEnabled;
    }

    public String getElavonMerchantID() {
        return elavonMerchantID;
    }

    public void setElavonMerchantID(String elavonMerchantID) {
        this.elavonMerchantID = elavonMerchantID;
    }

    public String getElavonUserID() {
        return elavonUserID;
    }

    public void setElavonUserID(String elavonUserID) {
        this.elavonUserID = elavonUserID;
    }

    public String getElavonPIN() {
        return elavonPIN;
    }

    public void setElavonPIN(String elavonPIN) {
        this.elavonPIN = elavonPIN;
    }

    public SelectItem getElavonBankAccount() {
        return elavonBankAccount;
    }

    public void setElavonBankAccount(SelectItem elavonBankAccount) {
        this.elavonBankAccount = elavonBankAccount;
    }

    public RecurrenceJobItem getRecurrenceItem() {
        return recurrenceItem;
    }

    public void setRecurrenceItem(RecurrenceJobItem recurrenceItem) {
        this.recurrenceItem = recurrenceItem;
    }

    public boolean isUserOffice365Linked() {
        return userOffice365Linked;
    }

    public void setUserOffice365Linked(boolean userOffice365Linked) {
        this.userOffice365Linked = userOffice365Linked;
    }

    public boolean isUserOffice365AccessToken() {
        return userOffice365AccessToken;
    }

    public void setUserOffice365AccessToken(boolean userOffice365AccessToken) {
        this.userOffice365AccessToken = userOffice365AccessToken;
    }

    public boolean isUserOffice365Validated() {
        return userOffice365Validated;
    }

    public void setUserOffice365Validated(boolean userOffice365Validated) {
        this.userOffice365Validated = userOffice365Validated;
    }

    public boolean isUserGoogleValidated() {
        return userGoogleValidated;
    }

    public void setUserGoogleValidated(boolean userGoogleValidated) {
        this.userGoogleValidated = userGoogleValidated;
    }

    public boolean isPayPal() {
        return isPayPal;
    }

    public void setPayPal(boolean payPal) {
        isPayPal = payPal;
    }

    public String getPayMeMerchantId() {
        return payMeMerchantId;
    }

    public void setPayMeMerchantId(String payMeMerchantId) {
        this.payMeMerchantId = payMeMerchantId;
    }

    public SelectItem getPaymeBankAccount() {
        return paymeBankAccount;
    }

    public SelectItem getClickBankAccount() {
        return clickBankAccount;
    }


    public void setPaymeBankAccount(SelectItem paymeBankAccount) {
        this.paymeBankAccount = paymeBankAccount;
    }

    public String getPaymeServiceId() {
        return paymeServiceId;
    }

    public void setPaymeServiceId(String paymeServiceId) {
        this.paymeServiceId = paymeServiceId;
    }

    public void setClickBankAccount(SelectItem clickBankAccount) {
        this.clickBankAccount = clickBankAccount;
    }

    public String getClickMerchantId() {
        return clickMerchantId;
    }

    public void setClickMerchantId(String clickMerchantId) {
        this.clickMerchantId = clickMerchantId;
    }

    public String getClickServiceId() {
        return clickServiceId;
    }

    public void setClickServiceId(String clickServiceId) {
        this.clickServiceId = clickServiceId;
    }

    public String getRevolutEmail() {
        return revolutEmail;
    }

    public void setRevolutEmail(String revolutEmail) {
        this.revolutEmail = revolutEmail;
    }

    public SelectItem getRevolutBankAccount() {
        return revolutBankAccount;
    }

    public void setRevolutBankAccount(SelectItem revolutBankAccount) {
        this.revolutBankAccount = revolutBankAccount;
    }

    public String getRevolutSecretApiKey() {
        return revolutSecretApiKey;
    }

    public void setRevolutSecretApiKey(String revolutSecretApiKey) {
        this.revolutSecretApiKey = revolutSecretApiKey;
    }

    public SelectItem getRevolutExpenseAccount() {
        return revolutExpenseAccount;
    }

    public void setRevolutExpenseAccount(SelectItem revolutExpenseAccount) {
        this.revolutExpenseAccount = revolutExpenseAccount;
    }

    public SelectItem getPaymeExpenseAccount() {
        return paymeExpenseAccount;
    }

    public void setPaymeExpenseAccount(SelectItem paymeExpenseAccount) {
        this.paymeExpenseAccount = paymeExpenseAccount;
    }

    public BigDecimal getPaymeServiceFee() {
        return paymeServiceFee;
    }

    public void setPaymeServiceFee(BigDecimal paymeServiceFee) {
        this.paymeServiceFee = paymeServiceFee;
    }
}
