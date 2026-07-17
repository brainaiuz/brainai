package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.SMSTemplateSummary;

import java.util.LinkedList;

/**
 * Created by Azazello on 4/21/15.
 */
public class SMSTemplateViewSinksContainer extends SinksContainer {
    public SMSTemplateViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new SMSTemplateSummary(id));
    }
}
