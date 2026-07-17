package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.BookingItemReservationEditView;
import com.edatasite.workforce.gwt.project.client.ui.BookingItemReservationView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/19/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsReservationViewSinksContainer extends SinksContainer {

	public BookingItemsReservationViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
	protected void initViews() {
        addView(new BookingItemReservationView(Integer.valueOf(params[0])));
        addView(new BookingItemReservationEditView(id));
	}
}
