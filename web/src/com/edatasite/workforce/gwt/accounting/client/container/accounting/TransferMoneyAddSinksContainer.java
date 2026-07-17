package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.TransferMoneyView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Feb 24, 2010
 * Time: 6:37:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransferMoneyAddSinksContainer extends SinksContainer {

    public TransferMoneyAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length >= 2) {
            addView(new TransferMoneyView(Integer.parseInt(params[1])));
        } else {
            addView(new TransferMoneyView());
        }
    }
}
