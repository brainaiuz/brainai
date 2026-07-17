package com.edatasite.workforce.gwt.assessment.client;

import com.edatasite.workforce.gwt.assessment.client.ui.view.AppraisalsSettingsView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Sherali Pirnafasov
 */
public class AppraisalsSettingsAddSinksContainer extends SinksContainer {

    public AppraisalsSettingsAddSinksContainer(String name, String description) {
        super(name, description);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new AppraisalsSettingsView());
    }
}
