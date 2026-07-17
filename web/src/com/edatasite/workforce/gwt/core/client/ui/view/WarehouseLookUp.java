package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/30/12
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseLookUp extends LookUp {

    private Integer productID;
    private String viewType;

    public WarehouseLookUp() {
    }

    public WarehouseLookUp(String viewType) {
        this.viewType = viewType;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setProductId(productID);
        filterParametrs.setViewType(viewType);
        AllInOneService.App.get().getWarehousesForLookUp(filterParametrs, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                WarehouseLookUp.super.getSuggestBox().showSuggestions(searchKey);
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

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getViewType() {
        return this.viewType;
    }

    public void setViewType(final String viewType) {
        this.viewType = viewType;
    }
}
