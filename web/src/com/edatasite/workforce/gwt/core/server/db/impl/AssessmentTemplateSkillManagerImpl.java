package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSkill;
import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplate;
import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplateSkill;
import com.edatasite.workforce.gwt.core.server.db.AssessmentTemplateSkillManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("assessmentTemplateSkillManager")
public class AssessmentTemplateSkillManagerImpl extends BaseManager<EdsAssessmentTemplateSkill> implements AssessmentTemplateSkillManager {

    public AssessmentTemplateSkillManagerImpl() {
        super(EdsAssessmentTemplateSkill.class);
    }

    public List<EdsAssessmentTemplateSkill> getTemplateSkill(EdsAssessmentTemplate template, EdsSkill skill) {
        return find("from EdsAssessmentTemplateSkill ats where ats.assessmentTemplate=? and " +
                "(ats.skill.deleted is null or ats.skill.deleted <> true) and ats.skill=?", template, skill);
    }


}
