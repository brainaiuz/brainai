/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/13 6:50:14                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.CustomerPrepaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.SupplierPrepaymentListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.ViewCrmAccountForm;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ContactListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CrmTaskListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunitiesListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfq.RequestForQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailListView;
import com.edatasite.workforce.gwt.project.client.ui.ProjectListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountViewSinksContainer extends SinksContainer implements PermissionConstants {

    public CrmAccountViewSinksContainer(final String name, final String description, final String[] params) {
        super(name, description, params, Constants.CLOSE, 257);
    }

    @Override
    protected void initViews(final LinkedList<View> viewList) {

    }

    protected void initViews() {

        if (this.params.length >= 2 && "fromCalendar".equals(this.params[1])) {
            addView(new ViewCrmAccountForm(this.id, true));
        } else if (this.params.length == 2) {
            addView(new ViewCrmAccountForm(this.id, false, "true".equals(this.params[1])));
        } else {
            addView(new ViewCrmAccountForm(this.id));
        }

        if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
            this.addView(new EventListView(null, id, RelationItem.TYPE_CRM_ACCOUNT, id, RelationItem.TYPE_CLIENT));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
            this.addView(new EmailListView(RelationItem.TYPE_CRM_ACCOUNT, id));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_TASKS_LIST)) {
            this.addView(new CrmTaskListView(id, RelationItem.TYPE_CRM_ACCOUNT, false));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CONTACTS_LIST)) {
            this.addView(new ContactListView(this.id));
        }
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST) && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            this.addView(new ProjectListView(RelationItem.TYPE_CRM_ACCOUNT, id, false));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST) && Utils.hasPermission(PermissionConstants.CRM_SALES_TAB)) {
            this.addView(new OpportunitiesListView(this.id, RelationItem.TYPE_CRM_ACCOUNT));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CASES_LIST)) {
            this.addView(new CaseListView(id, RelationItem.TYPE_CRM_ACCOUNT));
        }

        if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
            final ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCrmAccountId(id);
            if ("SupplierCustomer".equals(this.params[2])) {
                fp.setSupplierId(id);
                fp.setClientId(id);
            } else if ("Supplier".equals(this.params[2])) {
                fp.setSupplierId(id);
            } else {
                fp.setClientId(id);
            }
            fp.setRelationID(id);
            fp.setRelationType(RelationItem.TYPE_CRM_ACCOUNT);
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_LIST) && Utils.hasPermission(PermissionConstants.CRM_SALES_TAB)) {
                this.addView(new SaleQuoteListView(fp, false, this.params.length > 1 && "true".equals(this.params[1])));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_ORDER_LIST) && Utils.hasPermission(PermissionConstants.CRM_SALES_TAB)) {
                this.addView(new SaleOrderListView(fp, false, this.params.length > 1 && "true".equals(this.params[1])));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_LIST) && Utils.hasPermission(PermissionConstants.CRM_SALES_TAB)) {
                this.addView(new SaleInvoiceListView(fp, false, this.params.length > 1 && "true".equals(this.params[1])));
            }
            if (Utils.isCRM() ? Utils.hasPermission(PermissionConstants.CRM_REQUEST_FOR_QUOTE_LIST) : Utils.hasPermission(PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_LIST)) {
                this.addView(new RequestForQuoteListView(fp, false, this.params.length > 1 && "true".equals(this.params[1])));
            }
            if (this.id != null) {
                this.addDynamicView(true, this.id);
            }
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PREPAYMENT_LIST)) {
                this.addView(new CustomerPrepaymentListView(fp));
            }
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SUPPLIER_CREDIT_LIST)) {
                this.addView(new SupplierPrepaymentListView(fp));
            }
        }
    }
}