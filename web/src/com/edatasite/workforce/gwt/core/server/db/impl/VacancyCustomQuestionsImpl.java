package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsVacancyQuestion;
import com.edatasite.workforce.gwt.core.server.db.VacancyCustomQuestionsManager;
import org.springframework.stereotype.Repository;

@Repository
public class VacancyCustomQuestionsImpl extends BaseManager<EdsVacancyQuestion> implements VacancyCustomQuestionsManager {
    public VacancyCustomQuestionsImpl() {
        super(EdsVacancyQuestion.class);
    }

    @Override
    public void deleteQuestionsByVacanyId(Integer vacanyId) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ").append(getCompanyId()).append(".vacancy_questions WHERE vacancy_id=").append(vacanyId);
        this.updateNative(query.toString());

    }
}
