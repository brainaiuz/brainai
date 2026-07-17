package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.AddEditGroupPlacementView;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.GroupPlacementSummaryView;

import java.util.LinkedList;

public class GroupPlacementSummarySinksContainer extends SinksContainer {
    public GroupPlacementSummarySinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params.length == 2) {
            if ("edit".equals(params[0])) {
                addView(new AddEditGroupPlacementView(Integer.parseInt(params[1])));
            } else {
                addView(new GroupPlacementSummaryView(Integer.parseInt(params[1])));
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
