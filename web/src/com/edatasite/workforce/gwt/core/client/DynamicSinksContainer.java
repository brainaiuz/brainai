package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class DynamicSinksContainer extends SinksContainer {

    public DynamicSinksContainer(String name, String description, LinkedList<View> viewList) {
        super(name, description, null, Constants.NONE, -1, true, viewList);
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {
        if (viewList != null && viewList.size() > 0) {
            for (View view : viewList) {
                addView(view);
            }
        }
    }
}
