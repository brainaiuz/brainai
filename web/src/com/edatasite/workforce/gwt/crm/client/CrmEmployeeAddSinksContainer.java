package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddEmployeeView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 11:24:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class CrmEmployeeAddSinksContainer extends SinksContainer {

    public CrmEmployeeAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer contactId = null;
        String taskType = null;
        String from = null;
        if (params.length > 4) {
            from = params[4];
        }
        if (params.length > 3) {
            contactId = Integer.valueOf(params[2]);
            taskType = params[3];
        }
        if (params.length > 2) {
            contactId = Integer.valueOf(params[2]);
        }
        super.addView(new AddEmployeeView(from));
    }
}
