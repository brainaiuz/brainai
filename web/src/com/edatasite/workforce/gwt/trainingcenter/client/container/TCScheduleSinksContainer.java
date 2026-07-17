package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.InvoiceGeneratorView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.TCScheduleView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TCScheduleSinksContainer extends SinksContainer {

    public TCScheduleSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.TC_INVOICE_GENERATOR_VIEW)) {
            addView(new InvoiceGeneratorView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_SCHEDUL_VIEW)) {
            addView(new TCScheduleView());
        }
    }
}
