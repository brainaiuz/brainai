package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 30.03.2009
 * Time: 18:13:18
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoiceTransaction")
public class EdsInvoiceTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceid")
    private EdsInvoice invoice;

    public EdsInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(EdsInvoice invoice) {
        this.invoice = invoice;

        setCurrencyID(invoice.getCurrency() != null ? invoice.getCurrency().getObjectID() : null);
        setExchangeRate(invoice.getExchangeRate());
    }

    public Integer getKeyId() {
        return getInvoice() != null ? getInvoice().getObjectID() : ((EdsInvoiceTransaction) getReversalTransaction()).getInvoice().getObjectID();
    }

    public String getKeyType() {
        return INVOICE_TRANSACTION;
    }
}
