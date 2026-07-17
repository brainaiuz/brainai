package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCustomQuizFormScore;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.CustomQuizFormManager;
import org.springframework.stereotype.Repository;

@Repository
public class CustomQuizFormManagerImpl extends BaseManager<EdsCustomQuizFormScore> implements CustomQuizFormManager {
    public CustomQuizFormManagerImpl() {
        super(EdsCustomQuizFormScore.class);
    }

    @Override
    public EdsCustomQuizFormScore getQuizFormScore(String formId, Integer customFormItemId) {
        EdsUser user = getUser();

        return (EdsCustomQuizFormScore) findSingle("SELECT qf FROM EdsCustomQuizFormScore qf WHERE qf.employee.id = ? and qf.customFormId = ? and qf.customFormItemId.id = ?", user.getObjectID(), formId, customFormItemId);
    }
}
