package com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 6:49:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PriceLevelService extends RemoteService {

    PriceLevelItem getPriceLevelData(ListingFilterParameter filterParameter);

    SelectItem[] getClientList(ListingFilterParameter filterParametrs);

    Integer save(PriceLevelItem priceLevelItem);

    Boolean deletePriceLevel(Integer objectID);

    ListResult<PriceLevelItem> getPriceLevelList(ListingFilterParameter filterParameter);

//    void updatePriceLevelByQB(PriceLevelItem priceLevelItem, Integer synchItemType);
    ListResult<PriceLevelItem> getPriceLevelListForListing(ListingFilterParameter filterParameter);

    void updatePriceLevelByQB(PriceLevelItem priceLevelItem, Integer synchItemType);

    PriceLevelPPItem getPriceLevelPPItem(Integer productId, Integer priceLevelId);

    Double getCustomPriceFromPriceLevel(Integer productId, Integer priceLevelId);

    void setCustomPriceFromPriceLevel(Integer productId, Integer priceLevelId, Double customPrice);

    void setCustomPriceFromPriceLevels(Integer productId, PriceLevelPPItem[] priceLevelPPItems);

    HashMap<PriceLevelItem, PriceLevelPPItem> getPriceLevelPPItems(Integer productId);

    ArrayList<PriceLevelPPItem> getPriceLevelPPItemList(Integer priceLevelId, String searchKey);

    void savePriceLevelPPItems(Integer priceLevelId, PriceLevelPPItem[] ppItems);

    void savePriceLevelPBItems(Integer priceLevelId, PriceLevelBBItem[] bbItems);

    void savePriceLevelPPItem(PriceLevelPPItem item);

    void deletePriceLevelPPItem(Integer ppItemId);

    class App {
        public static PriceLevelServiceAsync get() {
            ServiceDefTarget target = GWT.create(PriceLevelService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/priceLevel");
            return (PriceLevelServiceAsync) target;
        }
    }
}
