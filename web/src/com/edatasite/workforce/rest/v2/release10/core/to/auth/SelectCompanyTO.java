package com.edatasite.workforce.rest.v2.release10.core.to.auth;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class SelectCompanyTO extends ResponseData {
    private Integer company_id;

    public SelectCompanyTO() {
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }
}
