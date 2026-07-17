package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.assessment.EdsAssessmentSchedule;
import com.edatasite.workforce.gwt.core.server.db.AssessmentScheduleManager;
import org.springframework.stereotype.Repository;

@Repository("assessmentScheduleManager")
public class AssessmentScheduleManagerImpl extends BaseManager<EdsAssessmentSchedule> implements AssessmentScheduleManager {

    public AssessmentScheduleManagerImpl() {
        super(EdsAssessmentSchedule.class);
    }

}
