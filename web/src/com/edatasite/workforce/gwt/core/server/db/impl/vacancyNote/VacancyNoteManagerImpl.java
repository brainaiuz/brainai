package com.edatasite.workforce.gwt.core.server.db.impl.vacancyNote;

import com.edatasite.workforce.core.domain.recruitment.EdsVacancyNote;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.vacancyNote.VacancyNoteManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Muxriddin on 16.02.22.
 */
@Repository("vacancyNoteManager")
public class VacancyNoteManagerImpl extends BaseManager<EdsVacancyNote> implements VacancyNoteManager {

    public VacancyNoteManagerImpl() {
        super(EdsVacancyNote.class);
    }

    @Override
    public List<EdsVacancyNote> getComments(Integer vacancyId) {
        return find("select vn from EdsVacancyNote vn where vn.vacancy.objectID=" + vacancyId);
    }
}
