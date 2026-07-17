package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 25.12.12
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class AddCourseSubjectSinksContainer extends SinksContainer {

    public AddCourseSubjectSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if(params.length == 1) {
            addView(new AddCourseSubjectView(null));
        } else if (params.length == 2) {
            addView(new AddCourseSubjectView(Integer.valueOf(params[1])));
        }
    }
}
