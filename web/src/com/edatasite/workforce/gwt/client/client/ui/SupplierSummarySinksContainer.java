package com.edatasite.workforce.gwt.client.client.ui;

import com.edatasite.workforce.gwt.accounting.client.ui.view.SupplierListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.SupplierPrepaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductsServicesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.AccountBalanceView;
import com.edatasite.workforce.gwt.client.client.ui.view.ViewSupplierForm;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ContactListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfq.RequestForQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsReceivedNotesListView;
import com.edatasite.workforce.gwt.project.client.ui.ProjectListView;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: Apr 13, 2010
 * Time: 6:45:42 PM
 */
public class SupplierSummarySinksContainer extends SinksContainer implements PermissionConstants {

    public SupplierSummarySinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE, 257);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ViewSupplierForm(id));
        boolean isAccountingSetup = "true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP));
        boolean isAccountingSection = Utils.isAccounting();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCrmAccountId(id);
        fp.setSupplierId(id);
        fp.setRelationID(id);
        fp.setRelationType(RelationItem.TYPE_CRM_ACCOUNT);

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST)) {
            addView(new PurchaseOrderListView(fp));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_LIST)) {
            addView(new PurchaseInvoiceListView(fp));
        }
        if (isAccountingSetup && (isAccountingSection ? Utils.hasPermission(ACCOUNTING_CONTACT_LIST) : Utils.hasPermission(CRM_CONTACTS_LIST))) {
            addView(new ContactListView(id));
        }
        if (Utils.hasPermission(PM_PROJECT_LIST)) {
            addView(new ProjectListView(RelationItem.TYPE_CRM_ACCOUNT, id, true));
        }
        if (Utils.hasModuleEnabled(CRM_MODULE) && Utils.hasModuleEnabled(CASE_MANAGEMENT) && Utils.hasPermission(ACCOUNTING_CASE_LIST)) {
            addView(new CaseListView(id, RelationItem.TYPE_CRM_ACCOUNT));
        }
        if (!isAccountingSetup || (isAccountingSection && Utils.hasPermission(ACCOUNTING_EVENT_LIST))) {
            addView(new EventListView(null, id, RelationItem.TYPE_CRM_ACCOUNT));
        }
        if (isAccountingSetup && Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_LIST)) {
            addView(new RequestForQuoteListView(fp, isAccountingSection, params.length > 1 && "true".equals(params[1])));
        }
        if (Utils.hasPermission(ACCOUNTING_PRODUCT_LIST)) {
            addView(new ProductsServicesListView(id, true));
        }
        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new SupplierListView(id));
        }
        if (Utils.hasPermission(ACCOUNTING_AGING_SUMMARY_PAYABLE)) {
            addView(new AccountBalanceView(id, SUPPLIER));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_GRN_LIST)) {
            addView(new GoodsReceivedNotesListView(fp));
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_LIST)) {
            addView(new SupplierPrepaymentListView(fp));
        }
        if (Utils.hasPermission(WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(id, RelationItem.TYPE_SUPPLIER));
        }
        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.SUPPLIER, id);
        }
    }
}