package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.AddEditEmailTemplatesView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Java6
 * Date: 08.02.13
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class EmailTemplateEditSinksContainer extends SinksContainer {

    public EmailTemplateEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String companyTemplate = null;
        String templateId = null;
        if (params.length > 1) {
            templateId = params[0];
            companyTemplate = params[1];
        }
        super.addView(new AddEditEmailTemplatesView(Integer.valueOf(templateId), (!"".equals(companyTemplate) && companyTemplate != null) ? companyTemplate : null));
    }
}
