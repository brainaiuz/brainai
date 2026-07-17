package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsVacancyItemTableCF;
import com.edatasite.workforce.gwt.core.server.db.VacancyItemTableCFManager;
import org.springframework.stereotype.Repository;

@Repository
public class VacancyItemTableCFManagerImpl extends BaseManager<EdsVacancyItemTableCF> implements VacancyItemTableCFManager {
    public VacancyItemTableCFManagerImpl() {
        super(EdsVacancyItemTableCF.class);
    }
}
