package com.edatasite.workforce.gwt.accounting.client.container.inventory;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AddEditWarehouseView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.StockTransferListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.WarehouseProductsListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.WarehouseView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.StockAdjustmentsListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsDeliveredNotesListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsReceivedNotesListView;

import java.util.LinkedList;
//QUICK LINKS, TABS ON WAREHOUSE SUMMARY VIEW
public class WarehouseViewSinksContainer extends SinksContainer {
    public WarehouseViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }
    protected void initViews() {

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setWarehouseID(id);
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_EDIT)) {
            addView(new AddEditWarehouseView(id)); }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_SUMMARY)) {
            addView(new WarehouseView(id)); }

        if (id != null && Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_LIST)) {
            addView(new WarehouseProductsListView(id,true)); }

        if (id != null && Utils.hasPermission(PermissionConstants.ACCOUNTING_GRN_LIST)) {
            addView(new GoodsReceivedNotesListView(id)); }

        if (id != null && Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_LIST)) {
            addView(new PurchaseInvoiceListView(fp)); }

        if (id != null && Utils.hasPermission(PermissionConstants.ACCOUNTING_GDN_LIST)) {
            addView(new GoodsDeliveredNotesListView(id)); }

        if (id != null && Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST)) {
            addView(new SaleInvoiceListView(fp,true)); }

        if (id != null && Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_LIST)) {
            addView(new StockAdjustmentsListView(fp)); }

        if (id != null && Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_LIST)) {
            addView(new StockTransferListView(fp)); }

    }
    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
