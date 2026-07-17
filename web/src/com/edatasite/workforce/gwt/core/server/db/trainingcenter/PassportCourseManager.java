package com.edatasite.workforce.gwt.core.server.db.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsPassportCourse;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 23/06/14
 * Time: 19:53
 * To change this template use File | Settings | File Templates.
 */
public interface PassportCourseManager extends Manager<EdsPassportCourse> {
    List<EdsPassportCourse> getPassportCourses(Integer passportID);

    void deletePassportCourses(Integer passportID);
}
