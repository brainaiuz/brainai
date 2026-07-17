package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;
import java.util.LinkedHashMap;

public class TimeSlotShortNameLookUp extends LookUp {

    private LinkedHashMap<Integer, SelectItem> map = new LinkedHashMap<>();
    private Date date = null;

    public TimeSlotShortNameLookUp(Date date) {
       this.date = date;
    }

    public void hideIconAndEnableFocusFetch() {
        getLayout().remove(getOpenIcon());
        getTextBox().addFocusHandler(event -> fetchDefaultItems(getFilterParametrs(), getCommand()));
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
        filterParametrs.setLookUp(true);
        AvailabilityService.App.get().getTimeSlotShortNameForLookUp(filterParametrs, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                initItems(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                TimeSlotShortNameLookUp.super.getSuggestBox().showSuggestions(searchKey);

            }
        });
    }

    private void initItems(SelectItem[] result) {
        if (result != null && result.length > 0) {
            for (SelectItem aResult : result) {
                map.put(aResult.getId(), aResult);
            }
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
