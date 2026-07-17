package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/12/11
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeasurementsLookUp extends LookUp {

    private LinkedHashMap<Integer, Object> map;

    public MeasurementsLookUp() {
        getSuggestBox().setAutoSelectEnabled(false);
        map = new LinkedHashMap<>();
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setLookUp(true);
        AllInOneService.App.get().getUnitMeasurements(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                initMeasurementUnits(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                MeasurementsLookUp.super.getOracle().setFullSearch(true);
                MeasurementsLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });

    }

    public void initMeasurementUnits(SelectItem[] items) {
        if (items != null && items.length > 0) {
            for (SelectItem item : items) {
                map.put(item.getId(), item);
            }
        }
    }

    public void addMeasurementUnit(SelectItem item) {
        addItem(item);
        initMeasurementUnits(new SelectItem[]{item});
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }

    public Object getSelectData() {
        return map.get(this.getSelectedItemID());
    }
}
