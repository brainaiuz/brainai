package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 02-May-2018
 * Time: 18:52:19
 * To change this template use File | Settings | File Templates.
 */
public class OpportunitiesList<T> extends ListResult<T> {

    private Double totalAmount = 0d;

    public OpportunitiesList() {
    }

    public OpportunitiesList(ArrayList<T> item, int total) {
        super(item, total);
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}