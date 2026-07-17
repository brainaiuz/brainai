package com.edatasite.workforce.gwt.accounting.client.rpc.discount;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DiscountServiceAsync {
    Request getDiscountList(ListingFilterParameter filterParametrs, ListLoadConfig config, AsyncCallback<DiscountList> async);

    void deleteDiscount(Integer objectID, AsyncCallback<Boolean> async);

    void getDiscountData(Integer objectID, AsyncCallback<DiscountItem> async);

    void save(DiscountItem discountItem, AsyncCallback<Integer> async);

    Request getDiscountList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<DiscountItem>> asyncCallback);

    void getDiscountListAsSelectItem(AsyncCallback<DiscountItem[]> abstractAsyncCallback);
}
