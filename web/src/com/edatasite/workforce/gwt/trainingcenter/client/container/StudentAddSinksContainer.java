package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AddStudentView;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 7/18/12
 * Time: 6:40 PM
 */
public class StudentAddSinksContainer extends SinksContainer {

    public StudentAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new AddStudentView(null));
    }
}