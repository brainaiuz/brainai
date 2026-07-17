package com.edatasite.workforce.gwt.client.client.ui;

import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.AccountBalanceView;
import com.edatasite.workforce.gwt.client.client.ui.view.AddSupplierView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ContactListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderListView;
import com.edatasite.workforce.gwt.project.client.ui.ProjectListView;
import com.google.gwt.user.client.Window;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 11, 2009
 * Time: 9:29:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierEditSinksContainer extends SinksContainer implements PermissionConstants {
    public SupplierEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        boolean isAccountingSetup = "true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP));
        boolean isAccountingSection = Window.Location.getPath().contains("Accounting.html");
        boolean isPMSection = Window.Location.getPath().contains("ProjectManagement.html");

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSupplierId(id);
        fp.setRelationID(id);
        fp.setRelationType(RelationItem.TYPE_CRM_ACCOUNT);

        addView(new AddSupplierView(id, params));
        if (isAccountingSetup && Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST)) {
            addView(new PurchaseOrderListView(fp));
        }
        if (isAccountingSetup && Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_LIST)) {
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
        if (isAccountingSetup && Utils.hasPermission(ACCOUNTING_AGING_SUMMARY_PAYABLE)) {
            addView(new AccountBalanceView(id, SUPPLIER));
        }
    }
}
