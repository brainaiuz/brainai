package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.view.WebHookResponseListView;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.hrms.client.ui.CandidateLogHistoryListView;
import com.edatasite.workforce.gwt.hrms.client.ui.DependentListView;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.ViewCandidateForm;
import com.edatasite.workforce.gwt.hrms.client.ui.talentprofile.TalentProfileListView;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailListView;

import java.util.LinkedList;

/**
 * User: hayot
 * Date: 7/3/12
 * Time: 5:02 PM
 */
public class CandidateSinksContainer extends SinksContainer {

    public CandidateSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ViewCandidateForm(this.id));
        addView(new EventListView(null, this.id, RelationItem.TYPE_CANDIDATE));
        addView(new EmailListView(RelationItem.TYPE_CANDIDATE, this.id));
        addView(new TalentProfileListView(this.id,true));
        addView(new DependentListView(this.id,true));
        if (id != null) {
            addDynamicView(CustomFieldLookUpTypeEnum.CANDIDATE, id);
        }
        if (Utils.hasPermission(PermissionConstants.WEBHOOK_RESPONSE_TAB_VIEW)) {
            addView(new WebHookResponseListView(id,RelationItem.TYPE_CANDIDATE));
        }
        addView(new CandidateLogHistoryListView(this.id));
    }
}
