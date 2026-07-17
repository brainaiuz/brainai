package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.recruitment.EdsVacancyItemTable;

import java.util.List;

public interface VacancyItemTableManager extends Manager<EdsVacancyItemTable> {

    List<EdsVacancyItemTable> findByUuid(Integer id, String uuid);

    void deleteByUUID(String uuid);
}
