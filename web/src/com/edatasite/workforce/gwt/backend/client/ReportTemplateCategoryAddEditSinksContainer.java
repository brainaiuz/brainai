package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.AddEditReportTemplateCategoryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 01.11.11
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */

public class ReportTemplateCategoryAddEditSinksContainer extends SinksContainer {

    public ReportTemplateCategoryAddEditSinksContainer(String[] params) {
        super("reporttemplatecategoryadd", "Add/Edit Template Category", params);
    }

    protected void initViews() {
        if (params.length == 1) {
            addView(new AddEditReportTemplateCategoryView());                           // add form
        } else if (params.length == 2) {
            addView(new AddEditReportTemplateCategoryView(Integer.valueOf(params[1]))); // edit form
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
