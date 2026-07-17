package com.edatasite.workforce.gwt.project.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.project.client.ui.BookingItemsReservationAddView;
import com.google.gwt.core.client.GWT;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/19/12
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsReservationAddSinksContainer extends SinksContainer {

    public BookingItemsReservationAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
            BookingItemsReservationAddView addReservationView = null;
//        if (Utils.hasPermission(PermissionConstants.PM_BOOKING_EDIT)) {
            if (params != null) {
                if (params.length == 3) {
                    addReservationView = new BookingItemsReservationAddView(params[2] != null && !"".equals(params[2]) && !"null".equals(params[2]) ? Integer.valueOf(params[2]) : null);
                } else if (params.length == 6) {
                    addReservationView = new BookingItemsReservationAddView(params);
                }
            }
//        }
        if (addReservationView != null) {
            addView(addReservationView);
        } else {

            if (params.length == 2 && params[1] != null && !params[1].isEmpty() && !"null".equals(params[1])) {
//                if (Utils.hasPermission(PermissionConstants.PM_BOOKING_EDIT)) {
                addView(new BookingItemsReservationAddView(params));
                GWT.log("I'm here");
//                }
            } else {
                addView(new BookingItemsReservationAddView(params));
            }
        }

    }
}
