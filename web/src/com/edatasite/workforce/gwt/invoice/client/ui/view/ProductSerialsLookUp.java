package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/11/12
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductSerialsLookUp extends LookUp {

    private Integer productID;
    private Integer projectID;
    private String viewType;

    private HashMap<Integer, ProductSerialItem> dataMap = new HashMap<>();

    public ProductSerialsLookUp(Integer productID, Integer projectID,String viewType) {
        this.productID = productID;
        this.projectID = projectID;
        this.viewType = viewType;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setItemId(productID);
        filterParametrs.setProjectId(projectID);
        filterParametrs.setViewType(viewType);
        InvoiceService.App.get().getProductSerials(filterParametrs, new AsyncCallback<ProductSerialItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ProductSerialItem[] result) {
                getLayout().removeStyleName("is-loading");
                if (filterParametrs.getSearchKey() != null) {
                    getSuggestBox().showSuggestions(filterParametrs.getSearchKey());
                } else {
                    getSuggestBox().showSuggestionList();
                }
                initItems(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                ProductSerialsLookUp.super.getOracle().setFullSearch(true);
                ProductSerialsLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    private void initItems(ProductSerialItem[] items) {
        if (items != null && items.length > 0) {
            for (ProductSerialItem item : items) {
                addProductSerialItem(item);
            }
        }
    }

    public void addProductSerialItem(ProductSerialItem serialItem) {
        addItem(new SelectItem(serialItem.getObjectID(), serialItem.getSerial()));
        dataMap.put(serialItem.getObjectID(), serialItem);
    }

    public ProductSerialItem getSelectedData() {
        Integer selectedID = getSelectedItemID();
        if (selectedID != null) {
            return dataMap.get(selectedID);
        }
        return null;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }
}
