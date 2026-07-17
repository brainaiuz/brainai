package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsPaymentBase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * User: Dilsh0d Madrahimov
 * Date: 7/31/17
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "overPayment")
public class EdsOverPayment extends EdsPaymentBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batchPaymentID")
    private EdsBatchPayment batchPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccountID")
    private EdsCrmAccount crmAccount; //Client or Supplier

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overPaymentAccountID")
    private EdsAccount overPaymentAccount;

    @Column(precision = 25, scale = 5)
    private BigDecimal amountInEntityCurrency;

    private String type;

    private Integer invoiceID;
    private Integer manualJournalID;
    private Integer customerSupplierID;

    public EdsBatchPayment getBatchPayment() {
        return batchPayment;
    }

    public void setBatchPayment(EdsBatchPayment batchPayment) {
        this.batchPayment = batchPayment;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        this.crmAccount = crmAccount;
    }

    public EdsAccount getOverPaymentAccount() {
        return overPaymentAccount;
    }

    public void setOverPaymentAccount(EdsAccount overPaymentAccount) {
        this.overPaymentAccount = overPaymentAccount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmountInEntityCurrency() {
        return amountInEntityCurrency;
    }

    public void setAmountInEntityCurrency(BigDecimal amountInEntityCurrency) {
        this.amountInEntityCurrency = amountInEntityCurrency;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Integer getManualJournalID() {
        return manualJournalID;
    }

    public void setManualJournalId(Integer manualJournalID) {
        this.manualJournalID = manualJournalID;
    }

    public Integer getCustomerSupplierID() {
        return customerSupplierID;
    }

    public void setCustomerSupplierID(Integer customerSupplierID) {
        this.customerSupplierID = customerSupplierID;
    }
}
