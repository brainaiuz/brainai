package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.contact.client.ui.EditLeadForm;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddLeadView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 3, 2010
 * Time: 6:17:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadEditSinksContainer extends SinksContainer {

    public LeadEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE, 257);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer crmAccountID = null;
        AddLeadView addLeadView = null;
        if (params.length > 2) {
            if (params[2].matches(REGEX_INTEGER_POSITIVE)) {
                crmAccountID = Integer.valueOf(params[2]);
            }
        }

        if (params.length == 3 && "phone".equals(params[2])) {
            addLeadView = new EditLeadForm(id);
            addLeadView.setDefaultPhoneNumber(params[1]);
        } else if (params.length > 1) {
            if (params[1] != null && !"".equals(params[1])) {
                addLeadView = new EditLeadForm(id, Boolean.valueOf(params[1]));
            } else {
                addLeadView = new EditLeadForm(id);
            }
        } else {
            addLeadView = new EditLeadForm(id);
        }
        addView(addLeadView);
        /*if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCrmContactId(this.id);
            fp.setRelationID(this.id);
            fp.setRelationType(RelationItem.TYPE_LEAD);
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_LIST)) {
                addView(new SaleInvoiceListView(fp, false));
            }
            if (Utils.hasPermission(PermissionConstants.CRM_SALES_QUOTE_LIST)) {
                addView(new SaleQuoteListView(fp, false));
            }
        }
        if (Utils.hasPermission(PermissionConstants.CRM_TASKS_LIST)) {
            addView(new CrmTaskListView(this.id, RelationItem.TYPE_LEAD, null, crmAccountID));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CASES_LIST)) {
            addView(new CaseListView(this.id, RelationItem.TYPE_LEAD));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LIST)) {
            addView(new EventListView(null, this.id, RelationItem.TYPE_LEAD));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
            addView(new EmailListView(RelationItem.TYPE_LEAD, this.id));
        }*/
    }
}