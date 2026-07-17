package com.edatasite.workforce.gwt.core.server.db;


import com.edatasite.workforce.core.domain.recruitment.EdsVacancyQuestion;

public interface VacancyCustomQuestionsManager extends Manager<EdsVacancyQuestion> {

    void deleteQuestionsByVacanyId(Integer vacanyId);

}
