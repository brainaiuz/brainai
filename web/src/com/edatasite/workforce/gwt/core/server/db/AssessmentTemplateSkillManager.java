package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSkill;
import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplate;
import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplateSkill;

import java.util.List;

public interface AssessmentTemplateSkillManager extends Manager<EdsAssessmentTemplateSkill> {
    List<EdsAssessmentTemplateSkill> getTemplateSkill(EdsAssessmentTemplate template, EdsSkill skill);

}
