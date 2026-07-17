package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AddEditCourseView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.ViewCourseForm;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 7/20/12
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class CourseViewSinksContainer extends SinksContainer {
    public CourseViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new ViewCourseForm(Integer.valueOf(params[0])));
        addView(new AddEditCourseView(Integer.valueOf(params[0])));
    }
}
