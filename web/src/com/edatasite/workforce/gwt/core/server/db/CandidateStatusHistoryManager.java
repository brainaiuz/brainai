package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCandidateStatusHistory;

import java.util.List;

public interface CandidateStatusHistoryManager extends Manager<EdsCandidateStatusHistory> {

    List<EdsCandidateStatusHistory> getCandidateStatusHistories(Integer candidateId);
}
