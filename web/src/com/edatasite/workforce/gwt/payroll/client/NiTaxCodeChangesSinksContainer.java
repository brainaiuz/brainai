package com.edatasite.workforce.gwt.payroll.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.ui.view.NiTaxChangesListView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 31, 2009
 * Time: 7:16:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class NiTaxCodeChangesSinksContainer extends SinksContainer {

    public NiTaxCodeChangesSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new NiTaxChangesListView(params[0]));
    }
}
