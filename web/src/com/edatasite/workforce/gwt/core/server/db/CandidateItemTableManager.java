package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.recruitment.EdsCandidateItemTable;

import java.util.List;

public interface CandidateItemTableManager extends Manager<EdsCandidateItemTable> {

    List<EdsCandidateItemTable> findByUuid(Integer id, String uuid);

    List<EdsCandidateItemTable> getByCandidateId(Integer id);

    void deleteByUUID(String uuid);
}
