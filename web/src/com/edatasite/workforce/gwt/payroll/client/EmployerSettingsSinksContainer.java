package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.EmployerSettingsAddEditView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 27.02.2009
 * Time: 3:34:47
 * To change this template use File | Settings | File Templates.
 */
public class EmployerSettingsSinksContainer extends SinksContainer {

    public EmployerSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        EmployerSettingsAddEditView container = new EmployerSettingsAddEditView();
        addView(container);
    }
}