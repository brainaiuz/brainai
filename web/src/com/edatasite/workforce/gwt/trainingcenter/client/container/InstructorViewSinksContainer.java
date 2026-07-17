package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.EditInstructorForm;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.InstructorSummaryView;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 8/6/12
 * Time: 5:34 PM
 */
public class InstructorViewSinksContainer extends SinksContainer {
    public InstructorViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new InstructorSummaryView(id));

        addView(new EditInstructorForm(id));
    }
}