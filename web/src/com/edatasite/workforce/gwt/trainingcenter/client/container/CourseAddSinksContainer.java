package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AddEditCourseView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 7/20/12
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */

public class CourseAddSinksContainer extends SinksContainer {
    public CourseAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if(params.length == 1) {
            addView(new AddEditCourseView(null));
        } else if (params.length == 2) {
            addView(new AddEditCourseView(Integer.valueOf(params[1])));
        }
    }
}
