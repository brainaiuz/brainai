package com.edatasite.workforce.gwt.accounting.client.rpc.consignment;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.math.BigDecimal;

/**
 * Created by Normurod on 6/15/15.
 */
public interface ConsignmentService extends RemoteService {
    ListResult<Consignment> getConsignmentList(ListingFilterParameter filterParameter);

    Consignment getConsignmentData(Integer objectID);

    Integer save(Consignment consignment);

    Boolean deleteConsignment(Integer objectID);

    BigDecimal getAvailableStock(Integer clientID, Integer productID, Integer consignmentID);

    class App {
        public static ConsignmentServiceAsync get() {
            ServiceDefTarget target = GWT.create(ConsignmentService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/consignment");
            return (ConsignmentServiceAsync) target;
        }
    }
}
