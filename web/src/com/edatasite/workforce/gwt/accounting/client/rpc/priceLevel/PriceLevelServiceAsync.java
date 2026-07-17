package com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

public interface PriceLevelServiceAsync {
    void deletePriceLevel(Integer objectID, AsyncCallback<Boolean> async);

    void getPriceLevelData(ListingFilterParameter filterParameter, AsyncCallback<PriceLevelItem> async);

    void save(PriceLevelItem priceLevelItem, AsyncCallback<Integer> async);

    void getClientList(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void getPriceLevelList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PriceLevelItem>> async);

    void getPriceLevelListForListing(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PriceLevelItem>> async);

    void updatePriceLevelByQB(PriceLevelItem priceLevelItem, Integer synchItemType, AsyncCallback<Void> async);

    void getPriceLevelPPItem(Integer productId, Integer priceLevelId, AsyncCallback<PriceLevelPPItem> async);

    void getCustomPriceFromPriceLevel(Integer productId, Integer priceLevelId, AsyncCallback<Double> asyncCallback);

    void setCustomPriceFromPriceLevel(Integer productId, Integer priceLevelId, Double customPrice, AsyncCallback<Void> asyncCallback);

    void setCustomPriceFromPriceLevels(Integer productId, PriceLevelPPItem[] priceLevelPPItems, AsyncCallback<Void> asyncCallback);

    void getPriceLevelPPItems(Integer productId, AsyncCallback<HashMap<PriceLevelItem, PriceLevelPPItem>> asyncCallback);

    void getPriceLevelPPItemList(Integer priceLevelId, String searchKey, AsyncCallback<ArrayList<PriceLevelPPItem>> async);

    void savePriceLevelPPItems(Integer priceLevelId, PriceLevelPPItem[] ppItems, AsyncCallback<Void> async);

    void savePriceLevelPBItems(Integer priceLevelId, PriceLevelBBItem[] bbItems, AsyncCallback<Void> async);

    void savePriceLevelPPItem(PriceLevelPPItem item, AsyncCallback<Void> async);

    void deletePriceLevelPPItem(Integer ppItemId, AsyncCallback<Void> async);
}
