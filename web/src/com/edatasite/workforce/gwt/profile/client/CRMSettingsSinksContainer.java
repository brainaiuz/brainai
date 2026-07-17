package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.contactcategory.client.ui.ContactCategoryListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.CRMSettings;
import com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui.EmailFilterListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: Jan 31, 2018
 * Time: 1:43:39 PM
 * To change this template use File | Settings | File Templates.
 */

public class CRMSettingsSinksContainer extends SinksContainer implements Constants {
    public CRMSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, Constants.NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new CRMSettings());
        addView(new EmailFilterListView());
        if (Utils.hasPermission(PermissionConstants.CRM_CONTACT_CATEGORY_LIST)) {
            addView(new ContactCategoryListView());
        }
    }
}
