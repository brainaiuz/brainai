package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListData;

/**
 * Created by Anvar Akramov on 11/06/2017.
 */
public class VacanciesRequestListTO extends RequestListData {
    private Integer job_family_id;
    private String status_code;

    public VacanciesRequestListTO() {
    }

    public Integer getJob_family_id() {
        return job_family_id;
    }

    public void setJob_family_id(Integer job_family_id) {
        this.job_family_id = job_family_id;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }
}
