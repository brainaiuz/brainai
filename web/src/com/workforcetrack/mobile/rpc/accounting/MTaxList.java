package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jul 29, 2009
 * Time: 4:19:03 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "taxList")
public class MTaxList {

    private List<MTaxItem> taxItems = new ArrayList<>();
    private Integer ukTaxID;

    public MTaxList() {
    }

    public MTaxList(TaxList taxList) {
        if (taxList != null) {
            taxItems = new ArrayList<>();
            for (TaxItem taxItem:taxList.getTaxItems()){
                this.taxItems.add(new MTaxItem(taxItem));
            }
            this.ukTaxID = taxList.getUKTaxID();
        }
    }

    public List<MTaxItem> getTaxItems() {
        return taxItems;
    }

    public void setTaxItems(List<MTaxItem> taxItems) {
        this.taxItems = taxItems;
    }

    public Integer getUKTaxID() {
        return ukTaxID;
    }

    public void setUKTaxID(Integer ukTaxID) {
        this.ukTaxID = ukTaxID;
    }

    public static TaxList convert(MTaxList mTaxList) {
        TaxList taxList = new TaxList();
        taxList.setUKTaxID(mTaxList.getUKTaxID());

        List<TaxItem> taxItems = new ArrayList<>();
        for (MTaxItem mTaxItem : mTaxList.getTaxItems()){
            taxItems.add(mTaxItem.convertToTaxItem(null));
        }
        taxList.setTaxItems(taxItems.toArray(new TaxItem[0]));
        return taxList;
    }
}