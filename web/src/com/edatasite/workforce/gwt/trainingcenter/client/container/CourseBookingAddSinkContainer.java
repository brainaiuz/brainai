package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AddEditCourseBookingFormView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 11/08/12
 * Time: 16:12
 * To change this template use File | Settings | File Templates.
 */
public class CourseBookingAddSinkContainer extends SinksContainer {

    public CourseBookingAddSinkContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 2) {
            addView(new AddEditCourseBookingFormView(Integer.parseInt(params[1])));
        } else {
            addView(new AddEditCourseBookingFormView());
        }

    }
}
