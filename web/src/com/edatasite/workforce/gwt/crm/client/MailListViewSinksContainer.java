package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.MailListMemberListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.MailListSummary;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Date: 29.01.2010
 * Time: 17:13:30
 * To change this template use File | Settings | File Templates.
 */
public class MailListViewSinksContainer extends SinksContainer {
    public MailListViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        super.addView(new MailListSummary(id));
        if (Utils.hasPermission(PermissionConstants.CRM_MAIL_LIST_MEMBERS)) {
            super.addView(new MailListMemberListView(id, false));
            super.addView(new MailListMemberListView(id, true));
        }
    }
}