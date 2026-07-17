package com.edatasite.workforce.rest.v3.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class QueriesListDTO extends ResponseData {

    private ArrayList<QueryNameDTO> queryNames;
    private Integer delay;

    public ArrayList<QueryNameDTO> getQueryNames() {
        return queryNames;
    }

    public void setQueryNames(ArrayList<QueryNameDTO> queryNames) {
        this.queryNames = queryNames;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }
}
