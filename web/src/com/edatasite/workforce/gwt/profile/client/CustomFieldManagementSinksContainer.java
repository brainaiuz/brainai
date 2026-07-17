package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.EditCustomFieldView;

import java.util.LinkedList;

/**
 * User: Normurod Buriev
 * Date: 7/24/11
 * Time: 11:59 AM
 */
public class CustomFieldManagementSinksContainer extends SinksContainer implements Constants {

    public CustomFieldManagementSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        //addView(new CustomFieldsListView());
//        addView(new CrmCustomFieldsListView());
//        addView(new PMCustomFieldsListView());
//        addView(new HrmsCustomFieldsListView());
//        addView(new AccountingCustomFieldsListView());
//        addView(new PayrollCustomFieldsListView());
//        addView(new SettingsCustomFieldsListView());

        if (params != null) {
            if (params.length > 2 && params[0] != null && !params[0].isEmpty()) {
                addView(new EditCustomFieldView(id, (params[2] == null || "null".equals(params[2])) ? null : params[2]));
            } else if (params.length >= 1 && params[0] != null && !params[0].isEmpty()) {
                addView(new EditCustomFieldView(id, null));
            }
        }
    }
}
