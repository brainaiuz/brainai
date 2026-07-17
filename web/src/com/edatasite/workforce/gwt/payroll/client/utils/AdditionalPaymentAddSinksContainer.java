package com.edatasite.workforce.gwt.payroll.client.utils;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.AdditionalPaymentAddEditViewV2;

import java.util.LinkedList;

/**
 * Created by Shohruh on 28 Oct 2016.
 */
public class AdditionalPaymentAddSinksContainer extends SinksContainer {

    private String type;

    public AdditionalPaymentAddSinksContainer(String name, String description, String[] params, String type) {
        super(name, description, params, CLOSE, -1, false);
        this.type = type;
        initialize();
    }

    @Override
    protected void initViews() {
        if (params != null && params.length == 4 && "copy".equals(params[3])) {
            addView(new AdditionalPaymentAddEditViewV2(Integer.valueOf(params[2]), type, params[1], true));
        } else if (params != null && params.length == 3) {
            addView(new AdditionalPaymentAddEditViewV2(Integer.valueOf(params[2]), type, params[1], false));
        } else if (params != null && params.length == 2) {
            addView(new AdditionalPaymentAddEditViewV2(type, params[1]));
        } else {
            addView(new AdditionalPaymentAddEditViewV2(type));
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
