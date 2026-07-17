package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsCourseSubjectCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.CourseSubjectCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("courseSubjectCFManager")
public class CourseSubjectCFManagerImpl extends BaseManager<EdsCourseSubjectCustomFields> implements CourseSubjectCFManager {
    public CourseSubjectCFManagerImpl() {
        super(EdsCourseSubjectCustomFields.class);
    }
}



