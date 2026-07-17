package com.edatasite.workforce.gwt.trainingcenter.client.container;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.employee.client.ui.EmployeeListView;
import com.edatasite.workforce.gwt.project.client.ui.BookingItemsListView;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.AttendenceSheetView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CertificatesListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CourseBookingListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CourseListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.CourseSubjectListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.InstructorReassignListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.PassportsListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.StudentListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.TrainingContractListView;
import com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse.ScheduledCourseListView;

import java.util.LinkedList;

/**
 * User: Normurod
 * Date: 7/16/12
 * Time: 8:07 PM
 */
public class TCOperationSinksContainer extends SinksContainer implements PermissionConstants {

    public TCOperationSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        if (Utils.hasPermission(PermissionConstants.TC_SCHEDULED_COURSE_LIST_VIEW)) {
            addView(new ScheduledCourseListView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_COURSE_LIST_VIEW)) {
            addView(new CourseListView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_COURSE_SUBJECT_LIST_VIEW)) {
            addView(new CourseSubjectListView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_BOOKING_ITEMS_LIST_VIEW)) {
            addView(new BookingItemsListView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_EMPLOYEE_LIST_VIEW)) {
            addView(new EmployeeListView(EmployeeListView.FROM_TRAINING_CENTER));
        }
        if (Utils.hasPermission(PermissionConstants.TC_STUDENT_LIST_VIEW)) {
            addView(new StudentListView(TCConstants.TC_STUDENTS));
        }
        if (Utils.hasPermission(TC_ATTENDENCE_SHEET)) {
            addView(new AttendenceSheetView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_INSTRUCTOR_REASSIGN_LIST_VIEW)) {
            addView(new InstructorReassignListView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_COURSE_BOOKING_LIST_VIEW)) {
            addView(new CourseBookingListView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_TRAINING_CONTRACT_LIST_VIEW)) {
                addView(new TrainingContractListView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_CERTIFICATES_LIST_VIEW)) {
            addView(new CertificatesListView());
        }
        if (Utils.hasPermission(PermissionConstants.TC_PASSPORT_LIST_VIEW)) {
            addView(new PassportsListView());
        }
    }
}
