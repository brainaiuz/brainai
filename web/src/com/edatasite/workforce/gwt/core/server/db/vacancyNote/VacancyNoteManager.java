package com.edatasite.workforce.gwt.core.server.db.vacancyNote;

import com.edatasite.workforce.core.domain.recruitment.EdsVacancyNote;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by Muxriddin on 16.02.22.
 */
public interface VacancyNoteManager extends Manager<EdsVacancyNote> {

    List<EdsVacancyNote> getComments(Integer vacancyId);

}
