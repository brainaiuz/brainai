package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsTalentProfileCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.TalentProfileCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("talentProfileCFManager")
public class TalentProfileCFManagerImpl extends BaseManager<EdsTalentProfileCustomFields> implements TalentProfileCFManager {
    public TalentProfileCFManagerImpl() {
        super(EdsTalentProfileCustomFields.class);
    }
}
