package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.form.DynamicFormView;

import java.util.LinkedList;

public class CustomizeFormSinksContainer extends SinksContainer {

    public CustomizeFormSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {

        if (Utils.hasRole(Constants.ADMIN)) {
            addView(new DynamicFormView(params[1], params[2]));
        }
    }
}
