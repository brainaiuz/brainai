package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventListView;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.EditCandidateForm;

import java.util.LinkedList;

/**
 * User: hayot
 * Date: 7/3/12
 * Time: 4:57 PM
 */
public class CandidateEditSinksContainer extends SinksContainer {

    public CandidateEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_CANDIDATE)) {
            addView(new EditCandidateForm(id));}
            addView(new EventListView(null, this.id, RelationItem.TYPE_CANDIDATE));
    }
}
