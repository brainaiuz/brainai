package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.EditCampaignForm;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class CampaignEditSinksContainer extends SinksContainer {

    public CampaignEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new EditCampaignForm(id));
        /*String name = params != null && params.length > 1 ? params[1] : null;
        if (Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
            addView(new MessageListView(id, name));
            addView(new SentMessageListView(id, name));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_LEADS_LIST)) {
            addView(new LeadListView(id, name));
        }
        if (Utils.hasPermission(PermissionConstants.CRM_CONTACTS_LIST)) {
            addView(new ContactListView(id, name));
        }*/
    }
}