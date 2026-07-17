package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;

import java.util.HashMap;

public class BrigadaLookUp extends LookUp {
    private String type;
    private Command listener;
    private HashMap<Integer, String> projectCodeMap = new HashMap<>();

    public BrigadaLookUp() {
    }

    public BrigadaLookUp(String type) {
        this.type = type;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setLookUp(true);


        AvailabilityService.App.get().getBrigadasForLookUp(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                initProjectCodes(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                BrigadaLookUp.super.getOracle().setFullSearch(true);
                BrigadaLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    @Override
    public void setSelected(SelectItem selectedItem) {
        super.setSelected(selectedItem);
        initProjectCodes(new SelectItem[]{selectedItem});
    }

    @Override
    public void addItem(SelectItem item) {
        super.addItem(item);
        initProjectCodes(new SelectItem[]{item});
    }

    private void initProjectCodes(SelectItem[] result) {
        for (SelectItem si : result) {
            if (si != null) {
                projectCodeMap.put(si.getId(), si.getDescription());
            }
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSelectedProjectCode() {
        Integer selectedID = getSelectedItemID();
        if (selectedID != null) {
            return projectCodeMap.get(selectedID);
        }
        return null;
    }

    public void setOnSelectListener(Command onSelectListener) {
        this.listener = onSelectListener;
    }

    public Command getOnSelectListener() {
        return listener;
    }
}
