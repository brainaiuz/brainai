package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsScheduledCourseCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.ScheduledCourseCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("scheduledCourseCfManager")
public class ScheduledCourseCFManagerImpl extends BaseManager<EdsScheduledCourseCustomFields> implements ScheduledCourseCFManager {
    public ScheduledCourseCFManagerImpl() {
        super(EdsScheduledCourseCustomFields.class);
    }
}
