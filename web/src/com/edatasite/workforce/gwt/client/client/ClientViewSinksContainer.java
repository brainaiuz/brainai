package com.edatasite.workforce.gwt.client.client;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.prepayment.CustomerPrepaymentListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.AccountBalanceView;
import com.edatasite.workforce.gwt.client.client.ui.view.ViewClientForm;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
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
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunitiesListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfq.RequestForQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.GoodsDeliveredNotesListView;
import com.edatasite.workforce.gwt.project.client.ui.ProjectListView;

import java.util.LinkedList;

public class ClientViewSinksContainer extends SinksContainer implements PermissionConstants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final String section;

    public ClientViewSinksContainer(String name, String description, String[] params, String section) {
        super(name, description, params, CLOSE);
        this.section = section;
    }

    protected void initViews() {
        boolean isAccountingSetup = "true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP));
        boolean isAccountingSection = Utils.isAccounting();

        addView(new ViewClientForm(Property.get(Constants.CLIENT_LIST, wfmStrings.summaryView(), wfmStrings.customer()), id, params.length > 1 && "true".equals(params[1]), "client"));
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCrmAccountId(id);
        fp.setClientId(id);
        fp.setRelationID(id);
        fp.setRelationType(RelationItem.TYPE_CRM_ACCOUNT);
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PREPAYMENT_LIST)) {
            addView(new CustomerPrepaymentListView(fp));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
            addView(new OpportunitiesListView(this.id, RelationItem.TYPE_CRM_ACCOUNT));
        }
        if (isAccountingSetup) {
            boolean isBlocked = params != null && params.length > 1 && "true".equals(params[1]);


            if (isAccountingSection ? Utils.hasPermission(ACCOUNTING_SALES_QUOTE_LIST) : Utils.hasPermission(PM_SALES_QUOTE_LIST)) {
                addView(new SaleQuoteListView(fp, isAccountingSection, isBlocked));
            }
            if (isAccountingSection ? Utils.hasPermission(ACCOUNTING_SALES_ORDER_LIST) : Utils.hasPermission(PM_SALES_ORDER_LIST)) {
                addView(new SaleOrderListView(fp, isAccountingSection, isBlocked));
            }
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_GDN_LIST)) {
                addView(new GoodsDeliveredNotesListView(fp));
            }
            if (isAccountingSection ? Utils.hasPermission(ACCOUNTING_SALES_INVOICE_LIST) : Utils.hasPermission(PM_SALES_INVOICE_LIST)) {
                addView(new SaleInvoiceListView(fp, isAccountingSection, isBlocked));
            }
            if (isAccountingSetup && Utils.hasPermission(ACCOUNTING_AGING_SUMMARY_RECEIVABLE)) {
                addView(new AccountBalanceView(id, CUSTOMER));
            }
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



        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.CUSTOMER, id);
        }
        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(this.id,RelationItem.TYPE_CLIENT));
        }

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
