package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.CheckAddEditView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/16/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class CheckAddSinksContainer extends SinksContainer{
    public CheckAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new CheckAddEditView());
    }
}
