package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.EmployerSettingsSummaryView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/4/15
 * Time: 1:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmployerSettingsViewSinksContainer extends SinksContainer {

    public EmployerSettingsViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        EmployerSettingsSummaryView container = new EmployerSettingsSummaryView();
        addView(container);
    }
}
