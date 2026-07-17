package com.edatasite.workforce.gwt.invoice.client.ui.view.itemserials;

import com.edatasite.workforce.gwt.accounting.client.rpc.itemserials.ItemSerialService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public class ItemSerialMultiLookUp extends MultiSelectLookUp {
    private Integer productId;
    private Integer entityId;//invoiceItemId for Credit/Debit Notes
    private String entityType;//Credit/Debit Note types
    private Integer warehouseId;

    public ItemSerialMultiLookUp(Integer productId) {
        this.productId = productId;
    }

    @Override
    public boolean onCondition(String text) {
        return false;
    }

    public void onLookUpService(ListingFilterParameter fp) {
        fp.setObjectIDs(getSelectedItemIds());
        fp.setItemId(productId);
        fp.setEntityID(entityId);
        fp.setRelationType(entityType);
        fp.setWarehouseID(warehouseId);
        ItemSerialService.App.get().getAvailableSerials(fp, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] result) {
                clearOracleItems();
                setItems(fp.getSearchKey(), result);
            }
        });
    }

    public void onActionPerformed(int type) {

    }

    public void setItems(ArrayList<String> items) {
        if (items != null && items.size() > 0) {
            ArrayList<SelectItem> selectItems = new ArrayList<>();
            for (String item : items) {
                selectItems.add(new SelectItem(null, item));
            }
            setSelectedItems(selectItems);
        }
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
}
