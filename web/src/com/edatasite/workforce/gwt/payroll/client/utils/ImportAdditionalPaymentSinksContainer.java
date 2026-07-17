package com.edatasite.workforce.gwt.payroll.client.utils;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.report.AdditionalPaymentImportView;

import java.util.LinkedList;

/**
 * Created by Shohruh on 07 Nov 2016.
 */
public class ImportAdditionalPaymentSinksContainer extends SinksContainer {

    public ImportAdditionalPaymentSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length > 1) {
            addView(new AdditionalPaymentImportView(Integer.valueOf(params[1])));
        }
    }
}
