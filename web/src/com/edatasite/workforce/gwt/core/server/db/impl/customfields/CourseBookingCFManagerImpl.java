package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsCourseBookingCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.CourseBookingCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("courseBookingCFManager")
public class CourseBookingCFManagerImpl extends BaseManager<EdsCourseBookingCustomFields> implements CourseBookingCFManager {
    public CourseBookingCFManagerImpl() {
        super(EdsCourseBookingCustomFields.class);
    }
}
