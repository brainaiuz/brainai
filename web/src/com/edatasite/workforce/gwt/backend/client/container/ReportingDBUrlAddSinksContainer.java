package com.edatasite.workforce.gwt.backend.client.container;

import com.edatasite.workforce.gwt.backend.client.ui.view.AddReportingDBUrlView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 4/30/12
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingDBUrlAddSinksContainer extends SinksContainer {
    public ReportingDBUrlAddSinksContainer(String[] params) {
        super("reportingdburladd", "Add/Edit Reporting DB URL", params);
    }

    @Override
    protected void initViews() {
        if (params.length == 1) {
            addView(new AddReportingDBUrlView());
        } else if (params.length == 2) {
            addView(new AddReportingDBUrlView(Integer.valueOf(params[1])));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
