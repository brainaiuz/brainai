package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsPaymentBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/30/11
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "customerPayment")
public class EdsCustomerSupplierPayment extends EdsPaymentBase {

    public static final Integer CUSTOMER_PAYMENT = 1;
    public static final Integer SUPPLIER_PAYMENT = 2;
    public static final Integer JOURNAL_AR_PAYMENT = 3;
    public static final Integer JOURNAL_AP_PAYMENT = 4;


    private Integer customerSupplierID;
    private Integer manualJournalId;
    private Integer type; //CUSTOMER_PAYMENT = 1; SUPPLIER_PAYMENT = 2;
    private Integer batchPaymentID;
    private Integer underPaymentID;
    @Column(precision = 25, scale = 5)
    private BigDecimal amountInEntityCurrency;

    /**
     * Ar - Account Receivable
     * Ap - Account Payable
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_Ar_Ap")
    private EdsAccount accountArAp;

    public Integer getCustomerSupplierID() {
        return customerSupplierID;
    }

    public void setCustomerSupplierID(Integer customerSupplierID) {
        this.customerSupplierID = customerSupplierID;
    }

    public Integer getManualJournalId() {
        return manualJournalId;
    }

    public void setManualJournalId(Integer manualJournalId) {
        this.manualJournalId = manualJournalId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBatchPaymentID() {
        return batchPaymentID;
    }

    public void setBatchPaymentID(Integer batchPaymentID) {
        this.batchPaymentID = batchPaymentID;
    }

    public BigDecimal getAmountInEntityCurrency() {
        return amountInEntityCurrency;
    }

    public void setAmountInEntityCurrency(BigDecimal amountInEntityCurrency) {
        this.amountInEntityCurrency = amountInEntityCurrency;
    }

    public EdsAccount getAccountArAp() {
        return accountArAp;
    }

    public void setAccountArAp(EdsAccount accountArAp) {
        this.accountArAp = accountArAp;
    }

    public Integer getUnderPaymentID() {
        return underPaymentID;
    }

    public void setUnderPaymentID(Integer underPaymentID) {
        this.underPaymentID = underPaymentID;
    }
}
