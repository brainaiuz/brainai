package com.edatasite.workforce.gwt.accounting.client.container.inventory;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductCategoriesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductCategorySummaryView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductsServicesListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunitiesListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;

import java.util.LinkedList;


public class ProductCategorySummarySinksContainer extends SinksContainer {
    public ProductCategorySummarySinksContainer(final String name, final String description, final String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(final LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new ProductCategorySummaryView(id));
        addView(new ProductsServicesListView(id));
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCategoryID(id);
        fp.setRelationType(RelationItem.TYPE_PRODUCT_CATEGORY);
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_CATEGORIES_LIST)) {
            addView(new ProductCategoriesListView(id));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
            addView(new OpportunitiesListView(id, RelationItem.TYPE_PRODUCT_CATEGORY));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_LIST)) {
            addView(new SaleQuoteListView(id, RelationItem.TYPE_PRODUCT_CATEGORY));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_LIST)) {
            addView(new SaleOrderListView(fp, true));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST)) {
            addView(new SaleInvoiceListView(fp, true));
        }
        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(id,RelationItem.TYPE_PRODUCT_CATEGORY));
        }

    }
}