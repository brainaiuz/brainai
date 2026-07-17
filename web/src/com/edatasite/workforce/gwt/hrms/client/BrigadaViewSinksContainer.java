package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.BrigadaEditView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.BrigadaSummaryView;

import java.util.LinkedList;

public class BrigadaViewSinksContainer extends SinksContainer {

    public BrigadaViewSinksContainer(String name, String description, String[] param) {
        super(name, description, param);
    }

    @Override
    protected void initViews() {
        BrigadaSummaryView brigadaSummaryView = new BrigadaSummaryView(this.id, this);
        brigadaSummaryView.ensureDebugId("brigadaSummaryView");
        super.addView(brigadaSummaryView);


        BrigadaEditView brigadaEditView = new BrigadaEditView(id);
        brigadaEditView.ensureDebugId("brigadaEditView");
        super.addView(brigadaEditView);

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
