package com.edatasite.workforce.gwt.trainingcenter.client;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * User: Normurod
 * Date: 7/16/12
 * Time: 8:30 PM
 */
public interface TCConstants {

    //TC ~ Training Center
    String TC_OPERATION = "operation";
    String TC_ASSESSMENT = "assessment";
    String TC_SCHEDULE_MENU = "consolidatedInvoice";
    String TC_ENQUIRIES = "enquires";
    String TC_ADD_ENQUIRIE = "addenquires";
    String TC_EDIT_ENQUIRIE = "editenquires";
    String TC_VIEW_ENQUIRIE = "viewenquires";
    String TC_VENUES = "venues";

    String TC_SALE_QUOTE = "salequote";
    String TC_STUDENTS = "students";
    String TC_COURSE = "course";
    String TC_COURSE_STUDENTS = "coursestudent";
    String TC_SCHEDULED_COURSE = "scheduledcourse";
    String TC_INSTRUCTOR = "tcInstructor";
    String TC_TRAINING_CONTRACT = "trainingContract";
    String TC_ADD_TRAINING_CONTRACT = "addTrainingContract";
    String TC_EDIT_TRAINING_CONTRACT = "editTrainingContract";
    String TC_CERTIFICATE = "certificate";
    String TC_PASSPORT = "passport";
    String TC_ASSESSMENT_VIEW = "assessmentview";
    String TC_CHANGE_CONTRACT_PRICE = "changeContractPrice";

    String TC_COURSE_BOOKING = "courseBooking";
    String TC_ADD_COURSE_BOOKING = "addCourseBooking";
    String TC_EDIT_COURSE_BOOKING = "editCourseBooking";
    String TC_VIEW_COURSE_BOOKING = "viewCourseBooking";
    String TC_ADD_STUDENT_COURSE_BOOKING = "addStudentCourseBooking";
    String TC_VIEW_STUDENT_COURSE_BOOKING = "viewStudentCourseBooking";

    Integer SC_PUBLIC = 1;
    Integer SC_PRIVATE = 2;

    String SC_PUBLIC_STR = "Public";
    String SC_PRIVATE_STR = "Private";

    SelectItem[] SCHEDULE_COURSE_STATUS = new SelectItem[]{
            new SelectItem(SC_PUBLIC, SC_PUBLIC_STR),
            new SelectItem(SC_PRIVATE, SC_PRIVATE_STR)
    };
    String TC_ATTENDENCE_SHEET = "attendenceSheet";

    String STUDENT_COURSE_SCHEDULE_EXAM_PARENT = "_STUDENT_COURSE_STATUS";
    String STUDENT_COURSE_SCHEDULE_EXAM_FAILED = "_STUDENT_FAIL";
    String STUDENT_COURSE_SCHEDULE_EXAM_PASSED = "_STUDENT_PASSED";

    String PASSPORT_STATUS = "_PASSPORT_STATUS";
    String SPOILED = "SPOILED";
    String ISSUED = "ISSUED";
    String ON_HOLD = "ON_HOLD";
    String LOST = "LOST";

    String STUDENT_MERGE = "student_merge";

    SelectItem[] COLORS = new SelectItem[]{
            new SelectItem(0, "Black"),
            new SelectItem(1, "Red"),
            new SelectItem(2, "Green"),
            new SelectItem(3, "Blue")
    };

}
