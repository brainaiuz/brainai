package com.edatasite.workforce.gwt.accounting.client.rpc.itemBatch;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ItemBatchServiceAsync {

    void getAllBatchesHistory(ListingFilterParameter fp, AsyncCallback<ListResult<ProductTrackBatchItem>> async);

    void validateBatchSerialsOnHand(Integer entityId, AsyncCallback<SelectItem[]> async);
}
