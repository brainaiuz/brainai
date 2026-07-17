package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface PaymentMethodManager extends Manager<EdsPaymentMethod> {

    List<EdsPaymentMethod> list();

    List<EdsPaymentMethod> getPaymentMethods(ListingFilterParameter fp);

    Integer getCount(ListingFilterParameter fp);

    EdsPaymentMethod getByName(String methodName);
}
