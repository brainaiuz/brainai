package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.availability.client.ui.view.ShiftEditView;
import com.edatasite.workforce.gwt.availability.client.ui.view.ShiftSummaryView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class ShiftViewSinksContainer extends SinksContainer {
    public ShiftViewSinksContainer(String name, String description, String[] param) {
        super(name, description, param);
    }

    @Override
    protected void initViews() {

        if (params != null) {
            if ("summary".equals(params[0])) {
                addView(new ShiftSummaryView(Integer.parseInt(params[1])));
            } else if ("edit".equals(params[0])) {
                if (params.length > 3) {
                    addView(new ShiftEditView(Integer.parseInt(params[1]),String.valueOf(params[2]),true));
                } else {
                    addView(new ShiftEditView(Integer.parseInt(params[1])));
                }
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
