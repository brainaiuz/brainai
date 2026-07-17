package com.edatasite.workforce.gwt.core.client.rpc.solr;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Murad Satimov
 * Date: 11/13/12
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolrCourseScheduleRepresenter implements IsSerializable {
    public static final String SPLIT = "@";
    public static final String FIELD_COMPOSITE_ID = "oid";
    public static final String FIELD_COMPANY_ID = "companyId";

    public static final String FIELD_COURSE_SCHEDULE_ID = "courseScheduleId";
    public static final String FIELD_COURSE_SCHEDULE_NUMBER = "courseScheduleNumber";
    public static final String FIELD_COURSE_ID = "courseId";
    public static final String FIELD_COURSE_NAME = "courseName";
    public static final String FIELD_COURSE_CODE = "courseCode";
    public static final String FIELD_COURSE_ID_NAME = "courseIdName";
    public static final String FIELD_LANGUAGE_ID = "languageId";
    public static final String FIELD_LANGUAGE_NAME = "languageName";
    public static final String FIELD_LANGUAGE_ID_NAME = "languageIdName";
    public static final String FIELD_ENABLE_OVERTIME = "enableOvertime";
    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_LOCATION_ID = "locationId";
    public static final String FIELD_LOCATION_NAME = "locationName";
    public static final String FIELD_LOCATION_ID_NAME = "locationIdName";
    public static final String FIELD_INSTRUCTOR_ID = "instructorId";
    public static final String FIELD_INSTRUCTOR_NAME = "instructorName";
    public static final String FIELD_INSTRUCTOR_ID_NAME = "instructorIdName";
    public static final String FIELD_DURATION = "duration";
    public static final String FIELD_NUMBER_OF_SEATS = "numberOfSeats";
    public static final String FIELD_ASSESSOR_ID = "assessorId";
    public static final String FIELD_ASSESSOR_NAME = "assessorName";
    public static final String FIELD_STATUS_ID = "statusId";
    public static final String FIELD_STATUS_NAME = "statusName";
    public static final String FIELD_STATUS_ID_NAME = "statusIdName";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_COUNT_OF_STUDENT = "countOfStudent";
    public static final String FIELD_COUNT_OF_CONFIRMED_STUDENT = "countOfConfirmedStudent";
    public static final String FIELD_COMPOSITE = "composite";
    public static final String FIELD_CREATED_DATE = "createdAt";
    public static final String FIELD_MODIFIED_DATE = "modifiedAt";

    public static final String SORTABLE_COURSE_SCHEDULE_NUMBER = "sortableCourseScheduleNumber";
    public static final String SORTABLE_COURSE_NAME = "sortableCourseName";
    public static final String SORTABLE_LANGUAGE_NAME = "sortableLanguageName";
    public static final String SORTABLE_INSTRUCTOR_NAME = "sortableInstructorName";
    public static final String SORTABLE_DURATION = "sortableDuration";
    public static final String SORTABLE_ASSESSOR_NAME = "sortableAssessorName";
    public static final String SORTABLE_START_DATE = "sortableStartDate";
}
