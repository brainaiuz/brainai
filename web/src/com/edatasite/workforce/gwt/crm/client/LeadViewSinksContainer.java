package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ChatListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CrmTaskListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ViewLeadForm;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleOrderListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 10, 2009
 * Time: 5:20:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadViewSinksContainer extends SinksContainer {

    public LeadViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE, 257);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer accountId = null;
        if (params[1] != null && !params[1].isEmpty()) {
            accountId = params[1].matches(REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[1]) : null;
        }
        addView(new ViewLeadForm(this.id, params.length >= 2 && "fromCalendar".equals(params[1])));

        if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
            if (accountId != null) {
                addView(new EventListView(null, this.id, RelationItem.TYPE_LEAD, accountId, RelationItem.TYPE_CRM_ACCOUNT));
            } else {
                addView(new EventListView(null, this.id, RelationItem.TYPE_LEAD));
            }
        }
        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
            addView(new EmailListView(RelationItem.TYPE_LEAD, this.id));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_TASKS_LIST)) {
            addView(new CrmTaskListView(this.id, RelationItem.TYPE_LEAD, null, params.length >= 2 && params[1].matches(Constants.REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[1]) : null));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CASES_LIST)) {
            addView(new CaseListView(this.id, RelationItem.TYPE_LEAD));
        }
        if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCrmContactId(this.id);
            fp.setRelationID(this.id);
            fp.setRelationType(RelationItem.TYPE_LEAD);
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_LIST)) {
                if (params.length >= 2 && params[1].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                    fp.setCrmAccountId(Integer.valueOf(params[1]));
                }
                addView(new SaleQuoteListView(fp, false));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_ORDER_LIST)) {
                addView(new SaleOrderListView(fp, false));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_LIST)) {
                addView(new SaleInvoiceListView(fp, false));
            }
            if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
                addView(new WebHookResponseListView(this.id, RelationItem.TYPE_LEAD));
            }
        }
        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.LEAD, id);
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_WHATSAPP)) {
            addView(new ChatListView(id,"lead"));
        }

    }
    }

