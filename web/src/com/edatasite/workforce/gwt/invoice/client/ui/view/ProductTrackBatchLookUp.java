package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductTrackBatchLookUp extends LookUp {

    private Integer productId;
    private Integer warehouseId;
    private String viewType;
    private HashMap<Integer, ProductTrackBatchItem> dataMap = new HashMap<>();

    public ProductTrackBatchLookUp(Integer productId, Integer warehouseId, String viewType) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.viewType = viewType;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setItemId(productId);
        filterParametrs.setViewType(viewType);
        filterParametrs.setWarehouseID(warehouseId);

        InvoiceService.App.get().getProductTrachBatches(filterParametrs, new AsyncCallback<ArrayList<ProductTrackBatchItem>>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<ProductTrackBatchItem> result) {
                initItems(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                ProductTrackBatchLookUp.super.getOracle().setFullSearch(true);
                ProductTrackBatchLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    private void initItems(List<ProductTrackBatchItem> items) {
        if (items != null && items.size() > 0) {
            SelectItem[] selectItems = new SelectItem[items.size()];
            for (int i = 0; i < items.size(); i++) {
                ProductTrackBatchItem item = items.get(i);
                dataMap.put(item.getObjectID(), item);
                String serial = item.getExpirationDate() != null ?
                        item.getSerial() + " (" + DateUtils.format(item.getExpirationDate(), DateUtils.format) + ")" :
                        item.getSerial();
                selectItems[i] = new SelectItem(item.getObjectID(), serial);
            }
            setItems(getFilterParametrs().getSearchKey(), selectItems);
        }else {
            getLayout().removeStyleName("is-loading");
        }
    }

    public ProductTrackBatchItem getSelectedData() {
        Integer selectedID = getSelectedItemID();
        if (selectedID != null) {
            return dataMap.get(selectedID);
        }
        return null;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
}
