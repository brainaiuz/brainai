package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.form.CustomFormItemView;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class CustomFormItemViewSinksContainer extends SinksContainer {

    public CustomFormItemViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        Integer fID = null;
        String formID = null;
        String name = null;
        boolean isPage = false;

        if (params.length > 3) {
            fID = Integer.parseInt(params[1]);
            formID = params[2];
            name = params[3];
            if (params[4] != null && "PAGE".equals(params[4])) {
                isPage = true;
            }
        }
        addView(new CustomFormItemView(id, fID, formID, name, isPage));
    }
}
