package com.edatasite.workforce.gwt.assessment.client;

import com.edatasite.workforce.gwt.assessment.client.ui.view.ui.AddInitiateSimpleAppraisalView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class InitiateSinksContainer extends SinksContainer {

    public InitiateSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new AddInitiateSimpleAppraisalView());
    }
}
