package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.AddEditRotationView;
import com.edatasite.workforce.gwt.hrms.client.ui.recruitment.RotationSummaryView;

import java.util.LinkedList;

public class RotationSummarySinksConatiner extends SinksContainer {
    public RotationSummarySinksConatiner(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params.length == 2) {
            if ("edit".equals(params[0])) {
                addView(new AddEditRotationView(Integer.parseInt(params[1])));
            }if ("employee".equals(params[0])) {
                addView(new AddEditRotationView(null,Integer.parseInt(params[1])));
            } else {
                addView(new RotationSummaryView(Integer.parseInt(params[1])));
            }
        }

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
    }
}
