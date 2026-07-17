package com.edatasite.workforce.gwt.wfmTimer.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.wfmTimer.client.ui.ClockComponent;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Fatxulla
 * Date: 19/02/14
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */

public class IssueTimerViewSinksContainer extends SinksContainer {

    public IssueTimerViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ClockComponent(Integer.valueOf(params[0]), PM_ISSUE_TIMER, params[1]));
    }
}
