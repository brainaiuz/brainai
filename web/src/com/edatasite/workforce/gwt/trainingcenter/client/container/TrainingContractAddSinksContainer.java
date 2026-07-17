package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AddEditTrainingContractView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/16/12
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingContractAddSinksContainer extends SinksContainer {
    public TrainingContractAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if(params.length == 1) {
            addView(new AddEditTrainingContractView());
        } else if (params.length == 2) {
            addView(new AddEditTrainingContractView(Integer.valueOf(params[1])));
        }
    }
}
