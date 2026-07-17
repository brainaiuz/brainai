package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CaseSummaryView;
import com.edatasite.workforce.gwt.crm.client.ui.view.CrmTaskListView;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailListView;
import com.edatasite.workforce.gwt.project.client.ui.ContractListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:23:07
 * To change this template use File | Settings | File Templates.
 */
public class CaseViewSinksContainer extends SinksContainer {

    public CaseViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        super.addView(new CaseSummaryView(id));
        if (Utils.hasPermission(PermissionConstants.CRM_TASKS_LIST)) {
            addView(new CrmTaskListView(id, RelationItem.TYPE_CASE, true));
        }
        if (params.length == 2) {
            super.addView(new EmailListView(RelationItem.TYPE_CASE, this.id, Integer.valueOf(params[1])));
        } else {
            super.addView(new EmailListView(RelationItem.TYPE_CASE, this.id));
        }
        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(id, RelationItem.TYPE_CASE));
        }
        if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_LIST)) {
            addView(new ContractListView(RelationItem.TYPE_CASE, id));
        }
        addDynamicView(CustomFieldLookUpTypeEnum.CASE, id);

    }
}