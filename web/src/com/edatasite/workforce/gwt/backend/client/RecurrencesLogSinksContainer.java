package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.ui.view.RecurrenceLogListView;
import com.edatasite.workforce.gwt.backend.client.ui.view.ServerLogListView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 31.03.11
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */

public class RecurrencesLogSinksContainer extends SinksContainer {
    private static final BackendStrings backendStrings = BackendStrings.App.get();

    public RecurrencesLogSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new RecurrenceLogListView("loadedRecurrenceLog", backendStrings.recurrences()));
        addView(new RecurrenceLogListView("firedRecurrenceLog", backendStrings.firedRecurrences()));
        addView(new RecurrenceLogListView("lateRecurrenceLog", backendStrings.firedWithDelay()));
        addView(new ServerLogListView());
    }
}
