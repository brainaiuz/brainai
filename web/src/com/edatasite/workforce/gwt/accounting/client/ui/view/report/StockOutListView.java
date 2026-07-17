package com.edatasite.workforce.gwt.accounting.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.rpc.StockAdjustmentListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import gwt.material.design.client.ui.html.Span;

public class StockOutListView extends StockAdjustmentsListView {

    public StockOutListView() {
        super("stockout");
        setDescription(property.getSingularWithLocalizedName(AccountingConstants.STOCK_OUT, wfmStrings.stockOut()));
        setAddNew("stockout|add/add");
    }

    @Override
    protected void createNewStockAdjustment() {
        SinksContainerFactory.entryPoint.onHistoryChanged("stockout|add/add");
    }

    @Override
    protected String getSummaryUrl(Integer objectId) {
        return "stockout|summary/" + objectId;
    }

    @Override
    protected String getEditUrl(Integer objectId) {
        return "stockout|edit/" + objectId;
    }

    @Override
    public void initStockAdjustmentList(ListingFilterParameter filterParametrs,
                                        ListingCallback<StockAdjustmentListItem> listingCallback,
                                        Span container) {

        filterParametrs.setViewType(AccountingConstants.STOCK_OUT_TYPE);
        super.initStockAdjustmentList(filterParametrs, listingCallback, container);
    }

    @Override
    public String getPropertyCode() {
        return AccountingConstants.STOCK_OUT;
    }
}
