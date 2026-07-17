package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.consignment.ConsignmentAddEditView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.consignment.ConsignmentSummaryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ConsignmentViewSinksContainer extends SinksContainer {

    public ConsignmentViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ConsignmentSummaryView(id));
        addView(new ConsignmentAddEditView(id));
    }
}
