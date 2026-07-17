package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsCandidateItemTable;
import com.edatasite.workforce.gwt.core.server.db.CandidateItemTableManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CandidateItemTableManagerIml extends BaseManager<EdsCandidateItemTable> implements CandidateItemTableManager {
    public CandidateItemTableManagerIml() {
        super(EdsCandidateItemTable.class);
    }

    @Override
    public List<EdsCandidateItemTable> findByUuid(Integer id, String uuid) {
        return (List<EdsCandidateItemTable>) find("select t from EdsCandidateItemTable t where t.candidate.objectID=? and t.uuid=?", id, uuid);
    }

    @Override
    public List<EdsCandidateItemTable> getByCandidateId(Integer id) {
        return (List<EdsCandidateItemTable>) find("select t from EdsCandidateItemTable t where t.candidate.objectID=?", id);
    }

    @Override
    public void deleteByUUID(String uuid) {
        updateNative("delete from " + getCompanyId() + ".candidateItemTable cf where cf.uuid = '" + uuid + "'");
    }
}
