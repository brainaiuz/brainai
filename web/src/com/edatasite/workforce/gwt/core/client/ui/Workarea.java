package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 11, 2008 Time: 10:59:27 PM To
 * change this template use File | Settings | File Templates.
 */

public class Workarea extends FlowPanel implements Constants {

    private View currentView;
    private Map<String, View> viewById = new HashMap<>();

    public Workarea() {
        setStyleName("whiteBGR");
    }

    public View getCurrentView() {
        return currentView;
    }

    public boolean display(View view) {
        boolean result = true;

        if (currentView != null) {
            currentView.removeFromParent();
        }
        currentView = view;
        viewById.put(view.getName(), view);
        showView();
        return result;
    }

    public boolean displayedView(String viewID) {

        if (viewID != null && viewById.containsKey(viewID)) {
            currentView = viewById.get(viewID);
            showView();

            Scheduler.get().scheduleFixedDelay(() ->{
                currentView.reInitialize();
                return false;
            },500);

            return true;
        }
        return false;
    }

    private void showView() {
        clear();

        currentView.setStyleName("workarea");
        currentView.getElement().setId("workarea");

        add(currentView);
    }

    public Map getViewById() {
        return viewById;
    }
}
