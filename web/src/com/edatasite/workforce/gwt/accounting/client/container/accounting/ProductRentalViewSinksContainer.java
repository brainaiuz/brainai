package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductsServicesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.RentalProductSummaryView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rentalorder.RentalOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Iftixor
 * Date: 09.08.2021
 * Time: 12:10:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductRentalViewSinksContainer extends SinksContainer {
    public ProductRentalViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new RentalProductSummaryView(id));

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setProductId(id);
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST)) {
            addView(new SaleInvoiceListView(fp, true));
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_RENTAL_ORDER_LIST)) {
            addView(new RentalOrderListView(id));
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_LIST)) {
            addView(new ProductsServicesListView(fp));
        }

        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.PRODUCT, id);
        }
    }
}
