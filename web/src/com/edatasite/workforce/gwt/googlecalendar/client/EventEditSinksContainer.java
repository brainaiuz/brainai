package com.edatasite.workforce.gwt.googlecalendar.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.EditEventView;

import java.util.LinkedList;

/**
 * Author: Azazello
 * Date: 5/24/2018
 * Time: 6:40 PM
 */
public class EventEditSinksContainer extends SinksContainer {
    public EventEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length > 3 && "copyFromExistingData".equals(params[3])) {
            addView(new EditEventView(Integer.parseInt(params[1]), params.length > 2 ? Integer.parseInt(params[2]) : Appointment.EVENT, true));
        } else if (params.length > 3) {
            addView(new EditEventView(Integer.parseInt(params[1]), Integer.parseInt(params[2]), params[3]));
        } else if (params.length == 2) {
            addView(new EditEventView(Integer.parseInt(params[1])));
        } else {
            addView(new EditEventView(Integer.parseInt(params[1]), params.length > 2 ? Integer.parseInt(params[2]) : Appointment.EVENT));
        }
    }
}
