package com.edatasite.workforce.gwt.payroll.client.utils;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.AdditionalPaymentSummaryView;

import java.util.LinkedList;

/**
 * Created by Shohruh on 28 Oct 2016.
 */
public class AdditionalPaymentViewSinksContainer extends SinksContainer {

    private String type;

    public AdditionalPaymentViewSinksContainer(String name, String description, String[] params, String type) {
        super(name, description, params, CLOSE, -1, false);
        this.type = type;
        initialize();
    }

    @Override
    protected void initViews() {
        Integer id = null;
        Integer employeeId = null;
        String statusCode = null;

        if (params != null && params.length > 2) {
            id = Integer.valueOf(params[0]);
            statusCode = params[1];
            employeeId = Integer.valueOf(params[2]);

        } else if (params != null && params.length > 1) {
            statusCode = params[1];
            id = Integer.valueOf(params[0]);

        } else {
            id = Integer.valueOf(params[0]);
        }

        addView(new AdditionalPaymentSummaryView(id, statusCode, employeeId));

    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
