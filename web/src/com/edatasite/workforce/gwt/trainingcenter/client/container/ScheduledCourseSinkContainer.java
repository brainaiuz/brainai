package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.InstructorReassignView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse.*;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 7/23/12
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledCourseSinkContainer extends SinksContainer {

    public ScheduledCourseSinkContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
	protected void initViews() {
        addView(new ScheduledCourseSummaryView(id));

        if (Utils.hasPermission(PermissionConstants.TC_COURSE_SCHEDULED_VIEW)) {
            addView(new CourseScheduledView(id));
        }
        if (Utils.hasPermission(PermissionConstants.TC_REGISTRATED_STUDENT_LIST)) {
            if (params.length >= 2) {
                addView(new RegistratedStudentList(id, Boolean.valueOf(params[1])));
            } else {
                addView(new RegistratedStudentList(id));
            }
        }
        if (Utils.hasPermission(PermissionConstants.TC_INSTRUCTOR_REASSIGN_VIEW)) {
            addView(new InstructorReassignView(id));
        }
        if (Utils.hasPermission(PermissionConstants.TC_CLONE_COURSE_SCHEDULE_VIEW)) {
            addView(new CloneCourseScheduleView(id));
        }
        if (Utils.hasPermission(PermissionConstants.TC_ASSESSMENT_LIST_VIEW)) {
            addView(new AssessmentListView(id));
        }
	}
}
