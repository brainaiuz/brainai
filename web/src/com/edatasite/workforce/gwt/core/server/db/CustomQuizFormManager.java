package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCustomQuizFormScore;

public interface CustomQuizFormManager extends Manager<EdsCustomQuizFormScore> {
    EdsCustomQuizFormScore getQuizFormScore(String formId, Integer customFormItemId);
}
