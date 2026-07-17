package com.edatasite.workforce.gwt.assessment.server.app;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentSkills;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElemsStruct;

/**
 * User: Admin
 * Date: 12.10.2008
 * Time: 19:40:54
 */
public interface AssessmentCircularResolver {

    AssessmentSkills getAssessmentSkillsComments(Integer assessmentId);

    AssessmentSkills getAssessmentGoalsComments(Integer assessmentId);

    SkillAssessmentElemsStruct getSkillAssessmentElemGroups(Integer employeeAssessmentId, Integer currentUserID, boolean hasReviewerSupervisor);

    SkillAssessmentElemsStruct getGoalAssessmentElemGroups(Integer employeeAssessmentId, Integer currentUserID, boolean hasReviewerSupervisor);
}
