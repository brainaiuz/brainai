package com.edatasite.workforce.gwt.news.client.container;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.news.client.news.AddEditNewsView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:26:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewsAddSinksContainer extends SinksContainer {
    public NewsAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS_ADD) || Utils.hasPermission(PermissionConstants.WORKSPACE_COMPANY_NEWS_ADD)) {
            addView(new AddEditNewsView());
        }
    }
}
