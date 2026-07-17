package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse.AddCourseScheduledView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/23/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledCourseAddSinkContainer extends SinksContainer {

    public ScheduledCourseAddSinkContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AddCourseScheduledView());
    }
}
