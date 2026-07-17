package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.SupplierListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.TrashBinListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankAccountingListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.FixedAssetRegisterListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.consignment.ConsignmentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.AssemblyItemListview;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.BuildAssemblyItemListview;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.InventoryItemsListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductsServicesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.StockAdjustmentsListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.StockOutListView;
import com.edatasite.workforce.gwt.client.client.ui.view.NewClientListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.expenses.client.ui.view.ExpenseListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.RecurringBillsListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.RecurringInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfp.RequestForPurchaseListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfq.RequestForQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;

import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION;


/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 20.02.2009
 * Time: 18:06:05
 * To change this template use File | Settings | File Templates.
 */
public class AccountingSinksContainer extends SinksContainer implements PermissionConstants {


    public AccountingSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    protected void initViews() {
        if ("true".equals(Utils.userSettings.get(ACCOUNTING_IS_SETUP))) {
            initMainViews();
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    //  COMPANY_ID:7619 --> Geomatic Consulting International
    public static boolean isHashAccessForPMRole = Utils.hasRole(PM) && !(CompanyConstants.C7619.equals(Utils.getEncryptedCompanyID()));
    public static boolean hasRolesForAccounting = Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT) || isHashAccessForPMRole || Utils.hasRole(CLIENT);

    private void initMainViews() {

        if (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_LIST)) {
            addView(new SaleQuoteListView());
        }
        if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_LIST)) {
            addView(new SaleOrderListView());
        }
        if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_LIST)) {
            addView(new SaleInvoiceListView());
        }
        if (Utils.hasPermission(ACCOUNTING_RECURRING_INVOICE_LIST)) {
            addView(new RecurringInvoiceListView());
        }

        if (Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_LIST)) {
            addView(new RequestForQuoteListView());
        }
        if (Utils.hasPermission(ACCOUNTING_REQUEST_FOR_PURCHASE_LIST)) {
            addView(new RequestForPurchaseListView());
        }
        if (Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_LIST)) {
            addView(new PurchaseOrderListView());
        }
        if (Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_LIST)) {
            addView(new PurchaseInvoiceListView());
        }
        if (Utils.hasPermission(ACCOUNTING_RECURRING_BILL_LIST)) {
            addView(new RecurringBillsListView());
        }

        if (Utils.hasPermission(ACCOUNTING_FIXED_ASSET_LIST)) {
            addView(new FixedAssetRegisterListView());
        }

        if (Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_LIST) || Utils.hasPermission(ACCOUNTING_COMPANY_EXPENSE_LIST)) {
            addView(new ExpenseListView(ACCOUNTING_CONTEXT));
        }

        if (Utils.hasPermission(ACCOUNTING_CUSTOMER_LIST)) {
            addView(new NewClientListView(true));
        }
        if (Utils.hasPermission(ACCOUNTING_SUPPLIER_LIST)) {
            addView(new SupplierListView());
        }
        if (Utils.hasPermission(ACCOUNTING_PRODUCT_LIST)) {
            addView(new ProductsServicesListView());
        }
        if (Utils.hasPermission(ACCOUNTING_INVENTORY_LIST)) {
            addView(new InventoryItemsListView());
        }
        if (Utils.hasPermission(ACCOUNTING_ASSEMBLY_ITEM_LIST)) {
            addView(new AssemblyItemListview());
        }
        if (Utils.hasPermission(ACCOUNTING_BUILD_ASSEMBLY_LIST)) {
            addView(new BuildAssemblyItemListview());
        }
        if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_LIST)) {
            addView(new BankAccountingListView());
        }
        if (Utils.hasPermission(ACCOUNTING_TRASH_BIN_LIST)) {
            addView(new TrashBinListView());
        }
        if (!Utils.isMultiWarehouseEnabled() && Utils.hasPermission(ACCOUNTING_STOCK_ADJUSTMENT_LIST) ) {
            addView(new StockAdjustmentsListView());
        }

        if (Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION) && !Utils.isMultiWarehouseEnabled() && Utils.hasPermission(ACCOUNTING_STOCK_ADJUSTMENT_LIST) ) {
            addView(new StockOutListView());
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP) || Utils.isMultiCompanySubsidiary()) {
            if (Utils.hasPermission(ACCOUNTING_CONSIGNMENT_LIST_VIEW)) {
                addView(new ConsignmentListView());
            }
        }

        if (Utils.properties.size() > 0) {
            for (PropertyItem item : Utils.properties.values()) {
                if (item.getfID() != null && item.isCustom() && Utils.hasPermission(item.getFormID() + "_" + Utils.getCompanyID())) {
                    addView(new CustomFormItemListView(item.getfID(), item.getPlural(), item.getFormID()));
                }
            }
        }
    }
}
