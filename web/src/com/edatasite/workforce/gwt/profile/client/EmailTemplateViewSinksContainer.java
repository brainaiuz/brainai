package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.EmailTemplateSummaryView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: muratov
 * Date: Mar 19, 2010
 * Time: 5:55:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailTemplateViewSinksContainer extends SinksContainer {

    public EmailTemplateViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String companyTemplate = null;
        String templateId = null;
//        boolean isImportEml = false;
        if (params.length > 1) {
            templateId = params[0];
            companyTemplate = params[1];
//            isImportEml = ("true".equals(params[2]) ? true : false);
        }
        super.addView(new EmailTemplateSummaryView(templateId != null ? Integer.valueOf(templateId) : id));
//        super.addView(new AddEditEmailTemplatesView(Integer.valueOf(templateId),
//                ((!"".equals(companyTemplate) && companyTemplate != null) ? companyTemplate : null)/*, isImportEml*/));
    }

}
