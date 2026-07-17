package com.edatasite.workforce.gwt.invoice.client.container.projectbaseinvoice;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.projectbasedinvoice.ProjectBasedInvoiceAddView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.05.2009
 * Time: 13:12:09
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBaseInvoiceAddSinksContainer extends SinksContainer {

    public ProjectBaseInvoiceAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length > 1) {
            addView(new ProjectBasedInvoiceAddView(params[1]));
        } else {
            addView(new ProjectBasedInvoiceAddView());
        }
    }
}
