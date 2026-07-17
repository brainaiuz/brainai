package com.edatasite.workforce.gwt.news.client.container;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.news.client.news.AddEditNewsView;
import com.edatasite.workforce.gwt.news.client.news.NewsDetailView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:26:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewsViewSinksContainer extends SinksContainer {
    public NewsViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_NEWS)) {
//            Window.alert("" + 1);
            addView(new NewsDetailView(id));
        }
        String add = Utils.isHRMS() ? PermissionConstants.HRMS_COMPANY_NEWS_ADD : PermissionConstants.WORKSPACE_COMPANY_NEWS_ADD;
        String edit = Utils.isHRMS() ? PermissionConstants.HRMS_COMPANY_NEWS_EDIT : PermissionConstants.WORKSPACE_COMPANY_NEWS_EDIT;
        boolean p = id != null ? Utils.hasPermission(edit) : Utils.hasPermission(add);
        if (p) {
            addView(new AddEditNewsView(id));
        }
    }
}
