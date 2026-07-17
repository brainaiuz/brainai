package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface RentalOrderServiceAsync {

    void saveRentalOrder(RentalOrderData data, AsyncCallback<SelectItem> async);

    void getRentalOrderData(Integer objectID, boolean copy, AsyncCallback<RentalOrderData> async);

    void deleteRentalOrder(Integer rentalOrderId, AsyncCallback<Boolean> async);

    void saveRentalOrderHistory(Integer rentalOrderId, HistoryListItem hisItem, AsyncCallback<Integer> async);

    void loadRentalOrderHistory(Integer objectID, AsyncCallback<List<HistoryNote>> async);

    void deleteRentalOrderComment(Integer commentID, AsyncCallback<Void> async);

    void saveProductForRentItemToRentalOrder(Integer rentalOrderId, HashMap<Integer, Integer> rentalProductsIds, AsyncCallback<Void> async);

    void updateStatusRentalOrder(Integer rentalOrderId, String statusCode, SelectItem invoiceItem, AsyncCallback<Void> async);

    void getRentalOrderList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<RentalOrderData>> listResultAsyncCallback);

    void calculateRentalMinPrice(Integer productId, DateNonConvertable startDate, DateNonConvertable endDate, AsyncCallback<BigDecimal> async);
}
