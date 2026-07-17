package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CrmTaskListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EditOpportunityForm;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunityExpenseClaimListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunityHistoryListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ViewOpportunityForm;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice.PurchaseInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.PurchaseOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.rfq.RequestForQuoteListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailListView;

import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_MESSAGE_CENTER;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_OPPORTUNITIES_EXPENSE_CLAIM_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_PURCHASE_INVOICE_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_REQUEST_FOR_QUOTE_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_INVOICE_LIST;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 13-Jul-2009
 * Time: 12:39:14
 */
public class OpportunityViewSinksContainer extends SinksContainer {

    public OpportunityViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        boolean isConvertedLead = false;
        Integer contactId = null;
        Integer accountId = null;
        if (params.length > 3) {
            isConvertedLead = "true".equals(params[1]);
            contactId = params[2].matches(REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[2]) : null;
            accountId = params[3].matches(REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[3]) : null;
        }
        if (params.length >= 2 && "fromCalendar".equals(params[1])) {
            super.addView(new ViewOpportunityForm(id));
        } else if (params.length >= 2) { // FROM OUTLOOK
            super.addView(new ViewOpportunityForm(id, params[1]));
        } else {
            super.addView(new ViewOpportunityForm(id));
        }
        super.addView(new EditOpportunityForm(id));
        if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
            if (contactId != null) {
                addView(new EventListView(null, this.id, RelationItem.TYPE_OPPORTUNITY, contactId, RelationItem.TYPE_CONTACT));
            } else if (accountId != null) {
                addView(new EventListView(null, this.id, RelationItem.TYPE_OPPORTUNITY, accountId, RelationItem.TYPE_CRM_ACCOUNT));
            } else {
                addView(new EventListView(null, this.id, RelationItem.TYPE_OPPORTUNITY));
            }
        }
        if (Utils.hasPermission(CRM_MESSAGE_CENTER)) {
            addView(new EmailListView(RelationItem.TYPE_OPPORTUNITY, this.id));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_TASKS_LIST)) {
            addView(new CrmTaskListView(id, RelationItem.TYPE_OPPORTUNITY, contactId, null));
        }
        if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setOpportunityID(this.id);
            fp.setConvertedLead(isConvertedLead);
            fp.setConvertedLeadId(contactId);
            fp.setRelationID(this.id);
            fp.setRelationType(RelationItem.TYPE_OPPORTUNITY);
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_LIST)) {
                addView(new SaleQuoteListView(fp, false));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_ORDER_LIST)) {
                addView(new SaleOrderListView(fp, false));
            }
            if (Utils.hasPermission(CRM_SALES_INVOICE_LIST)) {
                addView(new SaleInvoiceListView(fp, false));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_PURCHASE_ORDER_LIST)) {
                addView(new PurchaseOrderListView(fp));
            }
            if (Utils.hasPermission(CRM_PURCHASE_INVOICE_LIST)) {
                addView(new PurchaseInvoiceListView(fp));
            }
            if (Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_LIST)) {
                addView(new RequestForQuoteListView(fp, false, false));
            }
            if (Utils.hasPermission(CRM_OPPORTUNITIES_EXPENSE_CLAIM_LIST)) {
                addView(new OpportunityExpenseClaimListView(this.id));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITY_HISTORY_LIST)) {
                addView(new OpportunityHistoryListView(this.id));
            }
            if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
                addView(new WebHookResponseListView(this.id,RelationItem.TYPE_OPPORTUNITY));
            }
        }
        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.OPPORTUNITY, id);
        }
    }
}