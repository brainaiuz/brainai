package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.assessment.EdsSkillRating;
import com.edatasite.workforce.gwt.core.server.db.SkillRatingManager;
import org.springframework.stereotype.Repository;

@Repository("skillRatingManager")
public class SkillRatingManagerImpl extends BaseManager<EdsSkillRating> implements SkillRatingManager {
    public SkillRatingManagerImpl() {
        super(EdsSkillRating.class);
    }

}
