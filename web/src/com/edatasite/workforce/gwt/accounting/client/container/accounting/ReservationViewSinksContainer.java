package com.edatasite.workforce.gwt.accounting.client.container.accounting;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.EditEventReservationView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.EditReservationView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 5:37:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReservationViewSinksContainer extends SinksContainer {
    public ReservationViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        //addView(new SummaryReservationView(id));
        if (params.length <= 2) {
            addView(new EditReservationView(id));
        } else {
            addView(new EditEventReservationView(id));
        }
    }
}