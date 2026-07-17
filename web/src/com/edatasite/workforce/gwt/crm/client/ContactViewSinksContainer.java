package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.contact.client.ui.ViewContactForm;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ChatListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CrmTaskListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunitiesListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SaleInvoiceListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.SaleQuoteListView;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 13-Jul-2009
 * Time: 12:39:14
 * To change this template use File | Settings | File Templates.
 */
public class ContactViewSinksContainer extends SinksContainer {
    public ContactViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE, 257);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length > 1) {
            addView(new ViewContactForm(id, "fromCalendar".equals(params[1])));
        } else {
            addView(new ViewContactForm(id));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
            addView(new EventListView(null, this.id, RelationItem.TYPE_CONTACT));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
            addView(new EmailListView(RelationItem.TYPE_CONTACT, this.id));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_TASKS_LIST)) {
            addView(new CrmTaskListView(this.id, RelationItem.TYPE_CONTACT, null, params.length >= 3 && params[2].matches(REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[2]) : null));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
            addView(new OpportunitiesListView(id, RelationItem.TYPE_CONTACT,true));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CASES_LIST)) {
            addView(new CaseListView(this.id, RelationItem.TYPE_CONTACT));
        }
        if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCrmContactId(this.id);
            fp.setRelationID(this.id);
            fp.setRelationType(RelationItem.TYPE_CONTACT);
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_LIST)) {
                addView(new SaleQuoteListView(fp, false));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_LIST)) {
                addView(new SaleInvoiceListView(fp, false));
            }

        }

        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.CONTACT, id);
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_WHATSAPP)) {
            addView(new ChatListView(id,"contact"));
        }


    }
}
