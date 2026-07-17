package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.form.DynamicGridFormView;

import java.util.LinkedList;

public class CustomizeGridFormSinksContainer extends SinksContainer {

    public CustomizeGridFormSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {

        if (Utils.hasRole(Constants.ADMIN)) {
            Integer formItemId = null;
            if (params[3] != null && params[3].length() > 0) {
                formItemId = Integer.valueOf(params[3]);
            }
            addView(new DynamicGridFormView(params[1], params[2], formItemId));
        }
    }
}
