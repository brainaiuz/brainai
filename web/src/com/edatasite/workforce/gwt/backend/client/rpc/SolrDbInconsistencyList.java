package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: Abdulaziz
 * Date: Oct 20, 2010
 * Time: 2:04:54 PM
 */
public class SolrDbInconsistencyList implements IsSerializable {
    private int totalCount;
    private ArrayList<SolrDbInconsistencyItem> items;
    private Integer companyID;
    private String companyName;

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<SolrDbInconsistencyItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<SolrDbInconsistencyItem> items) {
        this.items = items;
    }
}
