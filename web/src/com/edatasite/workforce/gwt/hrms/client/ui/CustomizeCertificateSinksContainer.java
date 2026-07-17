package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by Khasan on 30.09.14.
 */
public class CustomizeCertificateSinksContainer extends SinksContainer {


    public CustomizeCertificateSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 2) {
            addView(new CustomizeCertificateView(Integer.valueOf(params[1])));
        } else if (params.length == 3) {
            addView(new CustomizeCertificateView(Integer.valueOf(params[1]), Integer.valueOf(params[2])));
        } else {
            addView(new CustomizeCertificateView());
        }
    }
}
