package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsVacancyItemTable;
import com.edatasite.workforce.gwt.core.server.db.VacancyItemTableManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VacancyItemTableManagerImpl extends BaseManager<EdsVacancyItemTable> implements VacancyItemTableManager {
    public VacancyItemTableManagerImpl() {
        super(EdsVacancyItemTable.class);
    }

    @Override
    public List<EdsVacancyItemTable> findByUuid(Integer id, String uuid) {
        return (List<EdsVacancyItemTable>) find("select vt from EdsVacancyItemTable vt where vt.vacancy.objectID=? and vt.uuid=?", id, uuid);
    }

    @Override
    public void deleteByUUID(String uuid) {
        updateNative("delete from " + getCompanyId() + ".vacancyItemTable vt where vt.uuid = '" + uuid + "'");
    }
}
