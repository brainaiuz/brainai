package com.edatasite.workforce.gwt.invoice.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11/14/12
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmartShippingMethodLookUp extends LookUp {


    private Map<Integer, Object> map;
    private Integer appliedClientId;

    public SmartShippingMethodLookUp(Command linkcommand) {
        super(true);
        oracle.setLinkCommand(linkcommand);
        oracle.setIsvisiblelink(true);

        map = new LinkedHashMap<>();
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        filterParametrs.setClientId(appliedClientId);
        InvoiceService.App.get().getShippinhMethodsForLookUp(filterParametrs, new AsyncCallback<ShippingMethod[]>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ShippingMethod[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                initItems(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                SmartShippingMethodLookUp.super.getSuggestBox().showSuggestions(searchKey);
                SmartShippingMethodLookUp.super.getOracle().setFullSearch(true);
            }
        });
    }

    private void initItems(ShippingMethod[] items) {
        if (items != null && items.length > 0) {
            for (ShippingMethod item : items) {
                map.put(item.getId(), item);
            }
        }

    }

    public Object getSelectedData() {
        return map.get(getSelectedItemID());
    }

    public void addItems(ShippingMethod[] items) {
        if (items != null && items.length > 0) {
            initItems(items);
        }
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }

    public Integer getAppliedClientId() {
        return appliedClientId;
    }

    public void setAppliedClientId(Integer appliedClientId) {
        this.appliedClientId = appliedClientId;
    }

    public void refresh(Integer appliedClientId) {
        this.appliedClientId = appliedClientId;
        clear();
    }

}
