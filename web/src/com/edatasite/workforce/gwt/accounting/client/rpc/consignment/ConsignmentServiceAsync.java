package com.edatasite.workforce.gwt.accounting.client.rpc.consignment;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;

/**
 * Created by Normurod on 6/15/15.
 */
public interface ConsignmentServiceAsync {
    void deleteConsignment(Integer objectID, AsyncCallback<Boolean> async);

    void getConsignmentData(Integer objectID, AsyncCallback<Consignment> async);

    void save(Consignment Consignment, AsyncCallback<Integer> async);

    Request getConsignmentList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<Consignment>> asyncCallback);

    void getAvailableStock(Integer clientID, Integer productID, Integer consignmentID, AsyncCallback<BigDecimal> asyncCallback);
}
