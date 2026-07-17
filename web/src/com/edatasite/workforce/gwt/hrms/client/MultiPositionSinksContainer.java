package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.MultiAddPositionView;

import java.util.LinkedList;

public class MultiPositionSinksContainer extends SinksContainer {

    public MultiPositionSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params != null) {
            switch (params.length) {
                case 2:
                    super.addView(new MultiAddPositionView(Integer.parseInt(params[1])));
                    break;
                default:
                    super.addView(new MultiAddPositionView());
            }
        }
    }
}
