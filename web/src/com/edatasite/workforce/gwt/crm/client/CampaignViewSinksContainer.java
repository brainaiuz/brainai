package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.ContactListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.LeadListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.MessageListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.OpportunitiesListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.SentMessageListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.ViewCampaignForm;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class CampaignViewSinksContainer extends SinksContainer {

    public CampaignViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new ViewCampaignForm(id));
        String name = params != null && params.length > 1 ? params[1] : null;
        if (Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
            addView(new MessageListView(id, name));
            addView(new SentMessageListView(id, name));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_LEADS_LIST)) {
            addView(new LeadListView(id, name));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CONTACTS_LIST)) {
            addView(new ContactListView(id, name));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST)) {
            addView(new OpportunitiesListView(id, RelationItem.TYPE_CAMPAIGN));
        }
    }
}