package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.contact.client.ui.EditContactForm;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 3, 2010
 * Time: 6:17:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactEditSinksContainer extends SinksContainer {

    public ContactEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE, 257);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length > 1) {
            EditContactForm form = new EditContactForm(id);
            form.setDefaultPhoneNumber(params[1]);
            addView(form);
        } else {
            addView(new EditContactForm(id));
        }
        /*if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCrmContactId(this.id);
            fp.setRelationID(this.id);
            fp.setRelationType(RelationItem.TYPE_CONTACT);
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_LIST)) {
                addView(new SaleInvoiceListView(fp, false));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_LIST)) {
                addView(new SaleQuoteListView(fp, false));
            }
        }
        if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
            addView(new OpportunitiesListView(id, RelationItem.TYPE_CONTACT));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_TASKS_LIST)) {
            addView(new CrmTaskListView(this.id, RelationItem.TYPE_CONTACT, null, params.length >= 2 && params[1].matches(REGEX_INTEGER_POSITIVE) ? Integer.valueOf(params[1]) : null));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CASES_LIST)) {
            addView(new CaseListView(this.id, RelationItem.TYPE_CONTACT));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
            addView(new EventListView(null, this.id, RelationItem.TYPE_CONTACT));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
            addView(new EmailListView(RelationItem.TYPE_CONTACT, this.id));
        }*/
    }
}
