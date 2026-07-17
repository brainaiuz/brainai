package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/25/11
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaxLookUp extends LookUp implements CustomCellInterface {

    private static final String NO_TAX = wfmStrings.noTax();

    public LinkedHashMap<Integer, Object> map;
    private String type;
    private boolean excludeExempt;

    private BigDecimal itemTaxAmount = BigDecimal.ZERO;

    private Command listener;

    public TaxLookUp(String type) {
        this.type = type;
        map = new LinkedHashMap<>();
    }

    @Override
    protected String getDefaultText() {
        return NO_TAX;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setLookUp(true);
        filterParametrs.setInvoiceType(type);
        filterParametrs.setExcludeExemptAndOutOfScope(excludeExempt);
        filterParametrs.setShowActive(true);
        AllInOneService.App.get().getCompanyTaxesWithFilter(filterParametrs, new AsyncCallback<TaxItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(TaxItem[] taxItems) {
                setItems(filterParametrs.getSearchKey(), taxItems);
                initTaxItems(taxItems);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                TaxLookUp.super.getOracle().setFullSearch(true);
                TaxLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(NO_TAX);
        getTextBox().getElement().getStyle().setColor("#999999");
    }

    private void initTaxItems(TaxItem[] taxItems) {
        if (taxItems == null) {
            return;
        }
        for (TaxItem item : taxItems) {
            map.put(item.getId(), item);
        }
    }

    public void addTaxItem(TaxItem item) {
        addItem(item);
        initTaxItems(new TaxItem[]{item});
    }

    public TaxItem getData(Integer itemID) {
        return (TaxItem) map.get(itemID);
    }

    public TaxItem getSelectedData() {
        Integer itemID = getSelectedItemID();
        return itemID != null && map.containsKey(itemID) ? (TaxItem) map.get(itemID) : null;
    }

    @Override
    public SelectItem getSelectedItem() {
        if (getSelectedData() != null && getSelectedData() instanceof TaxItem) {
            return getSelectedData();
        }
        return super.getSelectedItem();
    }

    public BigDecimal getItemTaxAmount() {
        return itemTaxAmount;
    }

    public void setItemTaxAmount(BigDecimal itemTaxAmount) {
        this.itemTaxAmount = itemTaxAmount;
    }


    public Command getOnSelectListener() {
        return listener;
    }

    public void setOnSelectListener(Command onSelectListener) {
        this.listener = onSelectListener;
    }

    public boolean isExcludeExempt() {
        return excludeExempt;
    }

    public void setExcludeExempt(boolean excludeExempt) {
        this.excludeExempt = excludeExempt;
    }

    @Override
    public String getDisplayValue() {
        return getValue() != null ? getValue() : NO_TAX;
    }

    @Override
    public void setItemValue(Object value) {
        setSelected((SelectItem) value);

    }

    @Override
    public void setItemFocus(boolean focused) {
        getSuggestBox().setFocus(focused);

    }
}
