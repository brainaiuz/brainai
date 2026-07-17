package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 09.04.2010
 * Time: 19:29:31
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoiceTaxTotal")
public class EdsInvoiceTaxTotal extends EdsBaseInvoiceTaxTotal {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private EdsInvoice invoice;

    public EdsInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(EdsInvoice invoice) {
        this.invoice = invoice;
    }
}
