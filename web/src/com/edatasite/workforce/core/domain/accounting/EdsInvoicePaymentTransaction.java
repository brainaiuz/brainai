package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 30.03.2009
 * Time: 18:11:43
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoicePaymentsTransaction")
public class EdsInvoicePaymentTransaction extends EdsPaymentTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoicePaymentId")
    private EdsInvoicePayment invoicePayment;

    public EdsInvoicePayment getInvoicePayment() {
        return invoicePayment;
    }

    public void setInvoicePayment(EdsInvoicePayment invoicePayment) {
        this.invoicePayment = invoicePayment;

        setCurrencyID(invoicePayment.getCurrencyID());
        setExchangeRate(invoicePayment.getExchangeRate());
    }

    public Integer getKeyId() {
        return getInvoicePayment() != null ? getInvoicePayment().getObjectID() : null;
    }

    public String getKeyType() {
        EdsInvoicePayment invPayment = (invoicePayment != null ? invoicePayment : ((EdsInvoicePaymentTransaction) getReversalTransaction()).getInvoicePayment());
        return (invPayment.getInvoice() != null && invPayment.getCreditNote() != null) ? CREDITEDINVOICE_TRANSACTION : INVOICEPAYMENT_TRANSACTION;
    }
}
