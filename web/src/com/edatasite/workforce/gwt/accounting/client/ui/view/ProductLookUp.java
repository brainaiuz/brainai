package com.edatasite.workforce.gwt.accounting.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.12.2010
 * Time: 18:38:14
 * To change this template use File | Settings | File Templates.
 */
public class ProductLookUp extends LookUp {
    private String type;
//    private Boolean showOnOpportunity;

    private LinkedHashMap<Integer, Object> map;
    private LinkedHashMap<String, Widget> itemWidgetsMap;
    private HashMap<Integer, ProductSerialItem[]> assignedSerials = new HashMap<>();

    private DiscountItem[] discountItems;
    private Integer itemDiscountID;
    private Integer discountItemStaticType;

    private ListLoadConfig config;
    private Command listener;

    private Integer convertedItemId;//for progress invoicing by item
    private Integer saleInvoiceId; //for purchase invoice used as billable expense
    private Integer productID;
    private ArrayList<Integer> locationIds;
    private Integer withoutType;
    private String fromView;
    private Integer itemID;//for PO,PI,SO,SQ,SI items
    private Integer categoryId;
    private Integer brandId;
    private Integer warehouseID;
    private Integer rentalItemId;
    private Date fromDate;
    private Date toDate;

    public ProductLookUp(String type) {
        this.type = type;
        config = new ListLoadConfig();
        config.setStart(0);
        config.setLimit(20);

        map = new LinkedHashMap<>();
    }

//    public ProductLookUp(String type, Boolean showOnOpportunity) {
//        this(type);
//        this.showOnOpportunity = showOnOpportunity;
//    }

    public ProductLookUp(String type, String fromView) {
        this(type);
        this.fromView = fromView;
    }

    public ProductLookUp(String type, String fromView, Integer rentalItemId, Date fromDate, Date toDate) {
        this(type);
        this.fromView = fromView;
        this.rentalItemId = rentalItemId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        addListener(ProductLookUp.this, WfmUiEventType.ON_PRODUCTSERVICE_SAVED, WfmUiEventType.ON_PRODUCTSERVICE_SAVED, WfmUiEventType.ON_PRODUCTSERVICE_SAVED);
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        filterParametrs.setObjectId(productID);
        filterParametrs.setWithoutType(withoutType);
        filterParametrs.setLookUp(true);
        filterParametrs.setInvoiceType(type);
        filterParametrs.setLocationIds(locationIds);
//        filterParametrs.setShowOnOpportunity(showOnOpportunity);
        filterParametrs.setViewType(fromView);
        filterParametrs.setListLoadConfig(config);
        filterParametrs.setCategoryID(categoryId);
        filterParametrs.setBrandID(brandId);
        filterParametrs.setWarehouseID(warehouseID);
        filterParametrs.setRelationID(rentalItemId);
        ProductService.App.get().getCompanyProductsByType(filterParametrs, new AbstractAsyncCallback<ProductSelectItem[]>() {
            public void failure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }

            public void success(ProductSelectItem[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                initItems(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                ProductLookUp.super.getOracle().setFullSearch(true);
                ProductLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    public void addProductItem(SelectItem item) {
        addItem(item);
        initItems(new SelectItem[]{item});
    }

    public void initItems(SelectItem[] items) {
        if (items != null && items.length > 0) {
            for (SelectItem item : items) {
                map.put(item.getId(), item);
            }
        }
    }

    @Override
    public SelectItem getSelectedItem() {
        if (getSelectedData() != null && getSelectedData() instanceof ProductSelectItem) {
            return (ProductSelectItem) getSelectedData();
        }
        return super.getSelectedItem();
    }

    public Object getSelectedData() {
        return map.get(this.getSelectedItemID());
    }

    public LinkedHashMap<String, Widget> getItemWidgetsMap() {
        return itemWidgetsMap;
    }

    public void setItemWidgetsMap(LinkedHashMap<String, Widget> itemWidgetsMap) {
        this.itemWidgetsMap = itemWidgetsMap;
    }

    public ProductSerialItem[] getAssignedSerials() {
        if (assignedSerials != null && getSelectedItemID() != null) {
            return assignedSerials.get(getSelectedItemID());
        }
        return null;
    }

    public void setAssignedSerials(Integer productID, ProductSerialItem[] assignedSerials) {
        this.assignedSerials.put(productID, assignedSerials);
    }

    public DiscountItem[] getDiscountItems() {
        return discountItems;
    }

    public void setDiscountItems(DiscountItem[] discountItems) {
        this.discountItems = discountItems;
    }

    public Integer getItemDiscountID() {
        return itemDiscountID;
    }

    public void setItemDiscountID(Integer itemDiscountID) {
        this.itemDiscountID = itemDiscountID;
    }

    public Integer getDiscountItemStaticType() {
        return discountItemStaticType;
    }

    public void setDiscountItemStaticType(Integer discountItemStaticType) {
        this.discountItemStaticType = discountItemStaticType;
    }

    public void setOnSelectListener(Command onSelectListener) {
        this.listener = onSelectListener;
    }

    public Command getOnSelectListener() {
        return listener;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public void setWithoutType(Integer withoutType) {
        this.withoutType = withoutType;
    }

    public Integer getConvertedItemId() {
        return convertedItemId;
    }

    public void setConvertedItemId(Integer convertedItemId) {
        this.convertedItemId = convertedItemId;
    }

    public Integer getSaleInvoiceId() {
        return saleInvoiceId;
    }

    public void setSaleInvoiceId(Integer saleInvoiceId) {
        this.saleInvoiceId = saleInvoiceId;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Integer warehouseID) {
        this.warehouseID = warehouseID;
    }

    public ArrayList<Integer> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(ArrayList<Integer> locationIds) {
        this.locationIds = locationIds;
    }
}
