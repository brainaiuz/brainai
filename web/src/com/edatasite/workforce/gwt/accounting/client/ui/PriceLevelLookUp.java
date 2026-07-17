package com.edatasite.workforce.gwt.accounting.client.ui;

import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.PER_PRODUCT;

/**
 * Created with IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: 08/02/18
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceLevelLookUp extends LookUp {
    private Map<Integer, PriceLevelItem> collection = new HashMap<>();

    public Map<Integer, PriceLevelItem> getCollection() {
        return collection;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        LoadingPanel.loading(true);
        filterParametrs.setType(PER_PRODUCT);
        filterParametrs.setFromListing(true);//it's queryied to get only client(operationType) price levels on PriceLevelManagerImpl.java:35 if it's not from listing
        PriceLevelService.App.get().getPriceLevelList(filterParametrs, new AsyncCallback<ListResult<PriceLevelItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ListResult<PriceLevelItem> result) {
                SelectItem[] items = new SelectItem[result.getList().size()];
                int counter = 0;
                for (PriceLevelItem item : result.getList()) {
                    items[counter++] = item;
                    collection.put(item.getId(), item);
                }
                setItems(filterParametrs.getSearchKey(), items);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                PriceLevelLookUp.super.getSuggestBox().showSuggestions(searchKey);
                LoadingPanel.loading(false);
            }
        });
    }


    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }
}
