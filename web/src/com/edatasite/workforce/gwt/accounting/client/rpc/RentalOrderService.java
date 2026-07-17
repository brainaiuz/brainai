package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface RentalOrderService extends RemoteService {

    RentalOrderData getRentalOrderData(Integer objectID, boolean copy);

    Boolean deleteRentalOrder(Integer rentalOrderId);

    SelectItem saveRentalOrder(RentalOrderData data);

    Integer saveRentalOrderHistory(Integer rentalOrderId, HistoryListItem hisItem);

    List<HistoryNote> loadRentalOrderHistory(Integer rentalOrderId);

    void deleteRentalOrderComment(Integer commentID);

    void saveProductForRentItemToRentalOrder(Integer rentalOrderId, HashMap<Integer, Integer> rentalProductsIds);

    void updateStatusRentalOrder(Integer rentalOrderId, String statusCode, SelectItem invoiceItem);

    ListResult<RentalOrderData> getRentalOrderList(ListingFilterParameter filterParametrs);

    BigDecimal calculateRentalMinPrice(Integer productId, DateNonConvertable startDate, DateNonConvertable endDate);

    class App {
        public static RentalOrderServiceAsync get() {
            ServiceDefTarget target = GWT.create(RentalOrderService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/rentalOrder");
            return (RentalOrderServiceAsync) target;
        }
    }

}
