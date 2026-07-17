package com.edatasite.workforce.gwt.assessment.client;

import com.edatasite.workforce.gwt.assessment.client.ui.view.ui.SimpleAppraisalView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

public class AssessSinksContainer extends SinksContainer implements Constants {

    public AssessSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (params[2].equals(ASSESSMENT_SIMPLE)) {
            if (params.length > 3 && params[3] != null && (params[3].equals("true") || params[3].equals("false"))) {
                addView(new SimpleAppraisalView(id, params[1], params[3].equals("true")));
            } else {
                addView(new SimpleAppraisalView(id, params[1], false));
            }

        } /*else if (params[2].equals(ASSESSMENT_360)) {
            if (params.length > 3 && params[3] != null && params[3].equals(PA_360_MANAGER_VIEW)) {
                AssessManagerView assessSimpleView = new AssessManagerView(id);
                addView(assessSimpleView);
            } else {
                if (params.length > 3 && params[3] != null && (params[3].equals("true") || params[3].equals("false"))) {
                    Assessment360SimpleView assessSimpleView = new Assessment360SimpleView(id, params[1], params[3].equals("true"));
                    addView(assessSimpleView);
                } else {
                    Assessment360SimpleView assessSimpleView = new Assessment360SimpleView(id, params[1]);
                    addView(assessSimpleView);
                }
            }
        }*/
    }

}
