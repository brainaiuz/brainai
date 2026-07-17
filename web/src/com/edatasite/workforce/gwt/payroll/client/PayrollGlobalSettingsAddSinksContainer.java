package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollGlobalSettingsView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/21/15
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollGlobalSettingsAddSinksContainer extends SinksContainer {

    public PayrollGlobalSettingsAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new PayrollGlobalSettingsView());
    }
}
