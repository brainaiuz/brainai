package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCourseRegistration;
import com.edatasite.workforce.gwt.core.server.db.CourseRegistrationManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Mar 3, 2009
 * Time: 7:07:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("courseRegistrationManager")
public class CourseRegistrationManagerImpl extends BaseManager<EdsCourseRegistration> implements CourseRegistrationManager {

    public CourseRegistrationManagerImpl() {
        super(EdsCourseRegistration.class);
    }

    public EdsCourseRegistration get(Integer employeeId, Integer courseId) {
        return (EdsCourseRegistration) findSingle("from EdsCourseRegistration cr where cr.employee.objectID=? and cr.course.objectID=?", employeeId, courseId);
    }
}
