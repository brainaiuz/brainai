package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsCourseCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.CourseCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("courseCfManager")
public class CourseCFManagerImpl extends BaseManager<EdsCourseCustomFields> implements CourseCFManager {
    public CourseCFManagerImpl() {
        super(EdsCourseCustomFields.class);
    }
}
