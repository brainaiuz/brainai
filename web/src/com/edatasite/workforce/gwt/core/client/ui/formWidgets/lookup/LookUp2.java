package com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

import java.util.HashMap;

/**
 * Created by Dilshod Madrahimov on 10/16/18.
 */

public class LookUp2 extends LookUp {

    private HashMap<Integer, SelectItem> dataMap = new HashMap<>();

    private SelectItem[] items;
    private SelectItem selectedItem;

    public LookUp2() {
        super();
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParameter) {
        if (items != null && items.length > 0) {
            setItems(filterParameter.getSearchKey(), items);
        }
        String searchKey = filterParameter.getSearchKey();
        if (searchKey != null && searchKey.trim().isEmpty()) {
            searchKey = "";
        } else if (searchKey == null) {
            searchKey = "";
        }
        LookUp2.super.getOracle().setFullSearch(true);
        LookUp2.super.getSuggestBox().showSuggestions(searchKey);
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
        if (items != null && items.length > 0) {
            for (SelectItem item : items) {
                dataMap.put(item.getId(), item);
            }
        }
    }

    public SelectItem getSelectedItem() {
        Integer selectedId = getSelectedItemID();
        if (selectedId != null /*&& selectedId > 0*/) {
            return dataMap.get(selectedId);
        }
        return null;
    }

    public void setSelectedByDescription(String description) {
        if (description != null && items != null && description.length() > 0) {
            for (SelectItem item1 : items) {
                if (description.equals(item1.getDescription())) {
                    setSelected(item1);
                    selectedItem = item1;
                    break;
                }
            }
        }
    }


    public SelectItem getSelectedItem(boolean... ifNullReturnFirst) {
        if (selectedItem != null) {
            return selectedItem;
        } else if (getSelectedItem() != null) {
            return getSelectedItem();
        } else if (ifNullReturnFirst != null && ifNullReturnFirst.length > 0 && ifNullReturnFirst[0]) {
            return items != null && items.length > 0 ? items[0] : null;
        }
        return null;
    }
}
