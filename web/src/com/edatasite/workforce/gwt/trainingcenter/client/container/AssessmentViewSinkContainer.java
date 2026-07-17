package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse.AssessmentView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Abdullo
 * Date: 20.09.12
 * Time: 17:49
 */
public class AssessmentViewSinkContainer extends SinksContainer {

    public AssessmentViewSinkContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params.length == 2) {
            addView(new AssessmentView(id, Integer.parseInt(params[1])));
        } else {
            addView(new AssessmentView(id));
        }

    }
}
