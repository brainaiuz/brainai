package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.assessment.ConfirmedScheduledCourseListView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 9/14/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TCAssessmentSinksContainer extends SinksContainer {


    public TCAssessmentSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.TC_CONFIRMED_SCHEDULED_COURCE_LIST_VIEW)) {
            addView(new ConfirmedScheduledCourseListView());
        }
    }
}
