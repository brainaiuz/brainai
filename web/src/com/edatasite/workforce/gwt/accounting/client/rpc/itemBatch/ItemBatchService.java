package com.edatasite.workforce.gwt.accounting.client.rpc.itemBatch;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface ItemBatchService extends RemoteService {

    ListResult<ProductTrackBatchItem> getAllBatchesHistory(ListingFilterParameter fp);

    SelectItem[] validateBatchSerialsOnHand(Integer entityId);

    class App {
        public static ItemBatchServiceAsync get() {
            ServiceDefTarget target = GWT.create(ItemBatchService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/itemBatches");
            return (ItemBatchServiceAsync) target;
        }
    }
}
