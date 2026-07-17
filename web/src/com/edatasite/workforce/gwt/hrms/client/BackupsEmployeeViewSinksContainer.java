package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.BackupsEmployeeAddEditView;
import com.edatasite.workforce.gwt.hrms.client.ui.BackupsEmployeeSummaryView;

import java.util.LinkedList;

public class BackupsEmployeeViewSinksContainer extends SinksContainer {
    public BackupsEmployeeViewSinksContainer(String naem, String description, String[] param) {
        super(naem, description, param);
    }

    @Override
    protected void initViews() {
        if (params != null) {
            if ("summary".equals(params[0])) {
                addView(new BackupsEmployeeSummaryView(Integer.parseInt(params[1])));
            } else if ("edit".equals(params[0])) {
                addView(new BackupsEmployeeAddEditView(Integer.parseInt(params[1])));
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}