package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsVacancyCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.VacancyCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Azazello on 1/22/16.
 */
@Repository("vacancyCFManager")
public class VacancyCFManagerImpl extends BaseManager<EdsVacancyCustomFields> implements VacancyCFManager {

    public VacancyCFManagerImpl() {
        super(EdsVacancyCustomFields.class);
    }
}
