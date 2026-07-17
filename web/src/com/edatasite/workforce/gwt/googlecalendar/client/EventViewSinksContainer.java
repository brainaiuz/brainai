package com.edatasite.workforce.gwt.googlecalendar.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.EventSummaryView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 11:47:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventViewSinksContainer extends SinksContainer {

    public EventViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params.length == 2 && "true".equals(params[1])) {
            super.addView(new EventSummaryView(id, true));
        } else {
            super.addView(new EventSummaryView(id));
        }
    }
}
