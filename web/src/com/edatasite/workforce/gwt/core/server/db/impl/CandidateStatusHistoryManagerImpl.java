package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCandidateStatusHistory;
import com.edatasite.workforce.gwt.core.server.db.CandidateStatusHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("candidateStatusHistoryManager")
public class CandidateStatusHistoryManagerImpl extends BaseManager<EdsCandidateStatusHistory> implements CandidateStatusHistoryManager {


    public CandidateStatusHistoryManagerImpl() {
        super(EdsCandidateStatusHistory.class);
    }

    @Override
    public List<EdsCandidateStatusHistory> getCandidateStatusHistories(Integer candidateId) {
        return find("select csh from EdsCandidateStatusHistory csh where csh.candidate.objectID = " + candidateId + " order by csh.objectID desc");
    }
}
