package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.trainingcenter.EdsInstructorScheduledCourse;
import com.edatasite.workforce.gwt.core.server.db.InstructorScheduleCourseManager;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 27/07/12
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
@Repository("instructorScheduleCourseManager")
public class InstructorScheduleCourseManagerImpl extends BaseManager<EdsInstructorScheduledCourse> implements InstructorScheduleCourseManager {
    public InstructorScheduleCourseManagerImpl() {
        super(EdsInstructorScheduledCourse.class);
    }

    @Override
    public List<EdsCourseSchedule> getInstructorScheduleCourseByDate(Date date, Integer instructorId, Integer locationId) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return (List<EdsCourseSchedule>) find("SELECT shc FROM EdsInstructorScheduledCourse ishc " +
                " inner join ishc.courseSchedule shc " +
                " WHERE to_date(to_char(ishc.date,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('" + format.format(date) + "','yyyy-MM-dd') AND ishc.instructor.objectID=? AND shc.location.objectID=?", instructorId, locationId);
    }

    @Override
    public EdsInstructorScheduledCourse getInstructorSchedule(Integer instructorId, Integer courseScheduleId, Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return (EdsInstructorScheduledCourse) findSingle("SELECT DISTINCT ishc FROM EdsInstructorScheduledCourse ishc " +
                " inner join ishc.courseSchedule shc " +
                " WHERE to_date(to_char(ishc.date,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('" + format.format(date) + "','yyyy-MM-dd') AND ishc.instructor.objectID=? AND shc.objectID=? AND shc.deleted IS NOT TRUE", instructorId, courseScheduleId);
    }

    @Override
    public List<EdsEmployee> getInstructorByDate(Integer locationId, Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return find("SELECT DISTINCT instructor FROM EdsInstructorScheduledCourse ishc " +
                " LEFT JOIN ishc.courseSchedule csch" +
                " LEFT JOIN ishc.instructor instructor " +
                " LEFT JOIN csch.location location " +
                " WHERE to_date(to_char(ishc.date,'yyyy-MM-dd'),'yyyy-MM-dd')=to_date('" + format.format(date) + "','yyyy-MM-dd') AND location.objectID=?", locationId);
    }
}
