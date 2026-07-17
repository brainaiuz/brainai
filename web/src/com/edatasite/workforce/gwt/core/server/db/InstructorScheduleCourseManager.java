package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsInstructorScheduledCourse;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 27/07/12
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
public interface InstructorScheduleCourseManager extends Manager<EdsInstructorScheduledCourse> {
    List<EdsCourseSchedule> getInstructorScheduleCourseByDate(Date date, Integer instructorId, Integer locationId);

    EdsInstructorScheduledCourse getInstructorSchedule(Integer instructorId, Integer courseScheduleId, Date date);

    List<EdsEmployee> getInstructorByDate(Integer locationId, Date date);
}
