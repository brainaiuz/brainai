package com.edatasite.workforce.gwt.client.client.ui.view;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/22/12
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubsidiariesLookUp extends LookUp {

    private HashMap<Integer, SelectItem> dataMap = new HashMap<>();

    private boolean isShowHeadOffice; //show parent company also

    public SubsidiariesLookUp() {
    }

    public SubsidiariesLookUp(boolean isShowHeadOffice) {
        this.isShowHeadOffice = isShowHeadOffice;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setShowHeadOffice(isShowHeadOffice);
//        LoadingPanel.get().show(wfmStrings.searching());
        ClientService.App.get().getSubsidiaries(filterParametrs, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
//                LoadingPanel.loading(false);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                setItems(searchKey, result);
                initItems(result);
                SubsidiariesLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    @Override
    public void addItem(SelectItem item) {
        super.addItem(item);
        if (item != null) {
            dataMap.put(item.getId(), item);
        }
    }

    private void initItems(SelectItem[] result) {
        if (result != null && result.length > 0) {
            for (SelectItem res : result) {
                dataMap.put(res.getId(), res);
            }
        }
    }

    public Integer getSubsidiaryCurrencyID() {
        Integer selectedItemID = getSelectedItemID();
        return (selectedItemID != null && selectedItemID > 0 && dataMap.get(selectedItemID) != null) ? Integer.parseInt(dataMap.get(selectedItemID).getDescription()) : null;
    }

}
