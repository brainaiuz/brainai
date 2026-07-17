package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.StudentMergeView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 12/4/12
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentMergeSinksContainer extends SinksContainer {
    public StudentMergeSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (params != null && params.length > 1) {
            if (TCConstants.STUDENT_MERGE.equals(params[1])) {
                ArrayList<Integer> ids = new ArrayList<>();
                for (int i = 2; i < params.length; i++) {
                    if (params[i] != null && params[i].matches(Constants.REGEX_INTEGER_POSITIVE)) {
                        ids.add(Integer.parseInt(params[i]));
                    }
                }
                addView(new StudentMergeView("studentMerge", "Merge Students", ids.toArray(new Integer[]{})));
            }
        }
    }
}
