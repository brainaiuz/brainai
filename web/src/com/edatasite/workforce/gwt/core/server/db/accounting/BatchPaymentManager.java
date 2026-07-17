package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by Sherzod on 7/6/2015.
 */
public interface BatchPaymentManager extends Manager<EdsBatchPayment> {
    ListResult<EdsBatchPayment> getBatchPayments(ListingFilterParameter filterParametrs);

    Integer getBatchPaymentsCount(ListingFilterParameter filterParametrs);

    boolean isNumberExists(String number, Integer objectID, String type);

    Integer getLastIntNumber(String type);

    EdsBatchPayment getPayment(Integer id);
}
