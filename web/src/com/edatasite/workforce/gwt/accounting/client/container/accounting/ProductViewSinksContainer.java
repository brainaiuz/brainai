package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.EditProductView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ItemBatchHistoryListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ItemSerialListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductHistoryListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductSummaryView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductsServicesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.StockTransferListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewStockValuationView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.StockAdjustmentsListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunitiesListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;

import java.util.LinkedList;

public class ProductViewSinksContainer extends SinksContainer {
    public ProductViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new ProductSummaryView(id));

        boolean isFromMyUpdate = false;
        boolean inventoryTrackingEnabled = false;
        boolean batchTrackingEnabled = false;
        Integer warehouseID = null;
        String productType = null;
        if (params != null && params.length > 1) {
            try {
                productType = params[1];
            } catch (NumberFormatException e) {
                productType = null;
                e.printStackTrace();
            }
            if (params.length > 2) {
                if ("FROM_MY_UPDATE".equals(params[2])) {
                    isFromMyUpdate = true;
                } else if ("INVENTORY_TRACKING".equals(params[2])) {
                    inventoryTrackingEnabled = true;
                } else if ("BATCH_TRACKING".equals(params[2])) {
                    batchTrackingEnabled = true;
                } else {
                    try {
                        warehouseID = Integer.valueOf(params[3]);
                    } catch (NumberFormatException e) {
                        warehouseID = null;
                        e.printStackTrace();
                    }
                }
            }
        }

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProductId(id);

        if (!isFromMyUpdate) {
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_EDIT)) {
                addView(new EditProductView(id));
            }
            if (inventoryTrackingEnabled && (productType == null || AccountingConstants.INVENTORY_ITEM_STR.equals(productType))) {
                addView(new ItemSerialListView(id));
            }
            if (batchTrackingEnabled && (productType == null || AccountingConstants.INVENTORY_ITEM_STR.equals(productType))) {
                addView(new ItemBatchHistoryListView(id));
            }
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_LIST) && Utils.isMultiWarehouseEnabled() && (productType == null || AccountingConstants.INVENTORY_ITEM_STR.equals(productType) || AccountingConstants.ASSEMBLY_ITEM_STR.equals(productType))) {
                addView(new StockTransferListView(fp));
            }
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_LIST) && (productType == null || AccountingConstants.INVENTORY_ITEM_STR.equals(productType) || AccountingConstants.ASSEMBLY_ITEM_STR.equals(productType))) {
            addView(new StockAdjustmentsListView(fp));
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_VALUATION) && (productType == null || (!AccountingConstants.SERVICE_STR.equals(productType) && !AccountingConstants.NON_INVENTORY_ITEM_STR.equals(productType) && !AccountingConstants.OTHER_CHARGE_STR.equals(productType)))) {
            addView(new NewStockValuationView(id, warehouseID, true));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
            addView(new OpportunitiesListView(id, RelationItem.TYPE_PRODUCT));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_LIST)) {
            addView(new SaleQuoteListView(id));
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_LIST)) {
            addView(new SaleOrderListView(id));
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST) ) {
            addView(new SaleInvoiceListView(fp, true));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST)) {
            addView(new PurchaseOrderListView(id, null));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_LIST)) {
            addView(new PurchaseInvoiceListView(id, null));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_VARIATION_ADD) && Utils.hasGenericAccess(GenericSettingsEnum.INVENTORY_VARIATION_ENABLED)) {
            addView(new ProductsServicesListView(null, id));
        }

        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(id, RelationItem.TYPE_PRODUCT));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_HISTORY_LIST)) {
            addView(new ProductHistoryListView(id));
        }



        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.PRODUCT, id);
        }
    }
}
