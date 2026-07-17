package com.edatasite.workforce.gwt.employee.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.SingleEmployeeAddForm;

import java.util.LinkedList;

/**
 * Created by Dilshod Madrahimov on 9/1/15 4:35 PM
 */
public class SingleEmployeeAddSinksContainer extends SinksContainer {

    public SingleEmployeeAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new SingleEmployeeAddForm(params.length > 1 ? params : null));
    }
}
