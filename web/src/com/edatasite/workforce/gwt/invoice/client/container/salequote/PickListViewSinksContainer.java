package com.edatasite.workforce.gwt.invoice.client.container.salequote;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.PickListEditView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 23, 2010
 * Time: 7:51:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PickListViewSinksContainer extends SinksContainer {

    public PickListViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new PickListEditView(id));
//        addView(new GoodsDeliveredNotesListView(id));
    }
}