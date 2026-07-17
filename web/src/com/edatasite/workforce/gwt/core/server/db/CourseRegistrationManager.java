package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCourseRegistration;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Mar 3, 2009
 * Time: 7:06:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CourseRegistrationManager extends Manager<EdsCourseRegistration> {
    EdsCourseRegistration get(Integer employeeId, Integer courseId);
}
