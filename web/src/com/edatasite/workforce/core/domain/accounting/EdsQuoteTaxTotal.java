package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 09.04.2010
 * Time: 19:30:08
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "quoteTaxTotal")
public class EdsQuoteTaxTotal extends EdsBaseInvoiceTaxTotal {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private EdsQuote quote;

    public EdsQuote getQuote() {
        return quote;
    }

    public void setQuote(EdsQuote quote) {
        this.quote = quote;
    }
}
