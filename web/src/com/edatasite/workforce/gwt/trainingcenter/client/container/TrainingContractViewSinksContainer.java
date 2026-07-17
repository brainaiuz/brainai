package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/16/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingContractViewSinksContainer extends SinksContainer {
    public TrainingContractViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
    //    addView(new ViewCourseForm(Integer.valueOf(params[0])));

    }
}
