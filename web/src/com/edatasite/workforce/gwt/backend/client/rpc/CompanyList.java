package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;
import java.util.Date;

public class CompanyList extends ListResult<CompanyListItem> {

    private Date lastpUpdateTime;

    public CompanyList() {

    }

    public CompanyList(ArrayList<CompanyListItem> results, int totalCount) {
        super(results, totalCount);
    }

    public Date getLastpUpdateTime() {
        return lastpUpdateTime;
    }

    public void setLastpUpdateTime(Date lastpUpdateTime) {
        this.lastpUpdateTime = lastpUpdateTime;
    }
}
