package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.AddEditReportTemplateView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 22.10.2011
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */

public class ReportingExcelTemplateAddEditSinksContainer extends SinksContainer {

    public ReportingExcelTemplateAddEditSinksContainer(String[] params) {
        super("reportxmltemplateadd", "Add/Edit XML Template", params);
    }

    protected void initViews() {
        if (params.length == 1) {
            addView(new AddEditReportTemplateView());                           // add form
        } else if (params.length == 2) {
            addView(new AddEditReportTemplateView(Integer.valueOf(params[1]))); // edit form
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}