package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class AddCandidateResultTO extends ResponseData {
    private Integer candidate_id;

    public AddCandidateResultTO() {
    }

    public AddCandidateResultTO(Integer candidate_id) {
        this.candidate_id = candidate_id;
    }

    public Integer getCandidate_id() {
        return candidate_id;
    }

    public void setCandidate_id(Integer candidate_id) {
        this.candidate_id = candidate_id;
    }
}
