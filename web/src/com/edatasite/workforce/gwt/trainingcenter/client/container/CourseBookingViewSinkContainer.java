package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AddStudentCourseBookingView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CourseBookingSummaryView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 11/08/12
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */
public class CourseBookingViewSinkContainer extends SinksContainer {

    public CourseBookingViewSinkContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new CourseBookingSummaryView(id));

        if (params != null && params.length > 1) {
            addView(new AddStudentCourseBookingView(id, Integer.valueOf(params[1])));
        }
    }
}
