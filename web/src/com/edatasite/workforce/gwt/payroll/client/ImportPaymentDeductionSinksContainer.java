package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.ImportPaymentDeductionView;

import java.util.LinkedList;

public class ImportPaymentDeductionSinksContainer extends SinksContainer {

    public ImportPaymentDeductionSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews() {
        if (params.length > 1) {
            addView(new ImportPaymentDeductionView(Integer.valueOf(params[1]), params[2]));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
