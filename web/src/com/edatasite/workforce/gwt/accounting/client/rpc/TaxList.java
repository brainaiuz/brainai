package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jul 29, 2009
 * Time: 4:19:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaxList implements Serializable {

    private TaxItem[] taxItems;
    private Integer ukTaxID;

    public TaxItem[] getTaxItems() {
        return taxItems;
    }

    public void setTaxItems(TaxItem[] taxItems) {
        this.taxItems = taxItems;
    }

    public Integer getUKTaxID() {
        return ukTaxID;
    }

    public void setUKTaxID(Integer ukTaxID) {
        this.ukTaxID = ukTaxID;
    }
}
