package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;


/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Aug-2010
 * Time: 18:58:14
 */
public interface ListingRequestProvider<T> {
    void getRequest(ListingFilterParameter filterParametrs, ListingCallback<T> callback);
}
