package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsProfileSkill;
import com.edatasite.workforce.gwt.core.server.db.ProfileSkillManager;
import org.springframework.stereotype.Repository;

@Repository("profileSkillManager")
public class ProfileSkillManagerImpl extends BaseManager<EdsProfileSkill> implements ProfileSkillManager {

    public ProfileSkillManagerImpl() {
        super(EdsProfileSkill.class);
    }

}
