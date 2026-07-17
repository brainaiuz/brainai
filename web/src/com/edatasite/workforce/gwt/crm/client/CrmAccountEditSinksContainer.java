package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.EditCrmAccountForm;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 3, 2010
 * Time: 6:17:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountEditSinksContainer extends SinksContainer {

    public CrmAccountEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE, 257);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new EditCrmAccountForm(id));
        /*if (Utils.hasPermission(PermissionConstants.CRM_CONTACTS_LIST)) {
            addView(new ContactListView(id));
        }
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST)) {
            addView(new ProjectListView(RelationItem.TYPE_CRM_ACCOUNT, this.id));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
            addView(new OpportunitiesListView(id, RelationItem.TYPE_CRM_ACCOUNT));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
            addView(new EventListView(null, this.id, RelationItem.TYPE_CRM_ACCOUNT));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CASES_LIST)) {
            addView(new CaseListView(this.id, RelationItem.TYPE_CRM_ACCOUNT));
        }
        if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_LIST)) {
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setClientId(this.id);
                addView(new SaleQuoteListView(fp, false));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_LIST)) {
                addView(new SaleInvoiceListView(this.id, false));
            }
        }*/
    }
}