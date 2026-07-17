package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddEditSMSTemplatesView;

import java.util.LinkedList;

/**
 * Created by Azazello on 4/21/15.
 */
public class SMSTemplateAddSinksContainer extends SinksContainer {
    public SMSTemplateAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Integer objectID = null;
        if (params != null && params.length > 1) {
            objectID = Integer.parseInt(params[1]);
        }
        addView(new AddEditSMSTemplatesView(id == null ? objectID : id));
    }
}
