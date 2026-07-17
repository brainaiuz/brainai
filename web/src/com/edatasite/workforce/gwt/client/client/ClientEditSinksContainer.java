package com.edatasite.workforce.gwt.client.client;

import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.AccountBalanceView;
import com.edatasite.workforce.gwt.client.client.ui.view.EditClientForm;
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
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfq.RequestForQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;
import com.edatasite.workforce.gwt.project.client.ui.ProjectListView;
import com.google.gwt.user.client.Window;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 3, 2010
 * Time: 6:17:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientEditSinksContainer extends SinksContainer implements PermissionConstants {

    public ClientEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.CLOSE, 257);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setClientId(this.id);
        this.addView(new EditClientForm(this.id, this.params));

        boolean isAccountingSetup = "true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP));
        boolean isAccountingSection = Window.Location.getPath().contains("Accounting.html");
        boolean isPMSection = Window.Location.getPath().contains("ProjectManagement.html");

        if (isAccountingSetup) {
            if (isAccountingSection ? Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_LIST) : Utils.hasPermission(PermissionConstants.PM_SALES_QUOTE_LIST)) {
                this.addView(new SaleQuoteListView(fp, isAccountingSection));
            }
            if (isAccountingSection ? Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_ORDER_LIST) : Utils.hasPermission(PermissionConstants.PM_SALES_ORDER_LIST)) {
                this.addView(new SaleOrderListView(fp, isAccountingSection));
            }
            if (isAccountingSection ? Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST) : Utils.hasPermission(PermissionConstants.PM_SALES_INVOICE_LIST)) {
                this.addView(new SaleInvoiceListView(this.id, false));
            }
        }
        if (isAccountingSetup && (isAccountingSection ? Utils.hasPermission(PermissionConstants.ACCOUNTING_CONTACT_LIST) : Utils.hasPermission(PermissionConstants.CRM_CONTACTS_LIST))) {
            this.addView(new ContactListView(this.id));
        }
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST)) {
            this.addView(new ProjectListView(RelationItem.TYPE_CRM_ACCOUNT, id, true));
        }
        if (Utils.hasModuleEnabled(PermissionConstants.CRM_MODULE) && Utils.hasModuleEnabled(PermissionConstants.CASE_MANAGEMENT) && Utils.hasPermission(PermissionConstants.ACCOUNTING_CASE_LIST)) {
            this.addView(new CaseListView(this.id, RelationItem.TYPE_CRM_ACCOUNT));
        }
        if (!isAccountingSetup || (isAccountingSection && Utils.hasPermission(PermissionConstants.ACCOUNTING_EVENT_LIST))) {
            this.addView(new EventListView(null, this.id, RelationItem.TYPE_CRM_ACCOUNT));
        }
        if (isAccountingSetup && Utils.hasPermission(PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_LIST)) {
            this.addView(new RequestForQuoteListView(fp, isAccountingSection, this.params.length > 1 && "true".equals(this.params[1])));
        }
        if (isAccountingSetup && Utils.hasPermission(ACCOUNTING_AGING_SUMMARY_RECEIVABLE)) {
            this.addView(new AccountBalanceView(id, CUSTOMER));
        }
    }
}
