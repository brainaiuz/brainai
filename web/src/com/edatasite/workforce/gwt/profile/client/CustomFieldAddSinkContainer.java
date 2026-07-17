package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.profile.client.ui.view.EditCustomFieldView;

import java.util.LinkedList;

/**
 * User: Normurod Buriev
 * Date: 7/24/11
 * Time: 12:09 PM
 */
public class CustomFieldAddSinkContainer extends SinksContainer {

    public CustomFieldAddSinkContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {

        if (params.length > 2 && params[2] != null && params[2].equals(ViewName.ProductCategory.name())) {
            addView(new EditCustomFieldView((params[1] != null && !params[1].isEmpty()) ? Integer.valueOf(params[1]) : null));
        } else if (params.length > 2) {
            addView(new EditCustomFieldView(params[2], params[1]));
        } else if (params.length > 1) {
            addView(new EditCustomFieldView("", (params[1] == null || "null".equals(params[1])) ? null : params[1]));
        }
    }
}