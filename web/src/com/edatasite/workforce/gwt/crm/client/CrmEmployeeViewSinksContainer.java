package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.PMNewEmployeeSummaryView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 11:25:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrmEmployeeViewSinksContainer extends SinksContainer {

    public CrmEmployeeViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        super.addView(new PMNewEmployeeSummaryView(this.id));
    }
}