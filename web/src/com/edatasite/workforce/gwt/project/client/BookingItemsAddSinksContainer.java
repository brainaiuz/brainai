package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.BookingItemsAddView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/18/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsAddSinksContainer extends SinksContainer {

    public BookingItemsAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new BookingItemsAddView(params));
    }
}
