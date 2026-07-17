package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class DynamicFormIntroSinksContainer extends SinksContainer {


    public DynamicFormIntroSinksContainer(String introPageAdd, String introductionPage, String[] params) {
        super(introPageAdd, introductionPage, params);
    }

    @Override
    protected void initViews() {
        String parentFormId = null;
        String type = null;
        if (params != null) {
            if (params.length > 1) {
                parentFormId = params[1];
                type = params[2];
            } else if (params.length == 1) {
                parentFormId = params[0];
                type = "addNew";
            }
        }
        this.addView(new IntroductionPageView(parentFormId, type));
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
