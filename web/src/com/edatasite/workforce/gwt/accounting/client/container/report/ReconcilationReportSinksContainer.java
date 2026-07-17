package com.edatasite.workforce.gwt.accounting.client.container.report;

import com.edatasite.workforce.gwt.accounting.client.ui.view.report.ReconcilationReportView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.07.2010
 * Time: 15:58:28
 * To change this template use File | Settings | File Templates.
 */
public class ReconcilationReportSinksContainer extends SinksContainer {

    public ReconcilationReportSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new ReconcilationReportView(id, params));
    }
}
