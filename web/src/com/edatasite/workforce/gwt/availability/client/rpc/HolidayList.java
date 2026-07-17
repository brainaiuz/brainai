package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class HolidayList implements IsSerializable {

    private HolidayItem[] results;
    private int totalCount;

    public HolidayList() {
    }

    public HolidayList(HolidayItem[] results, int totalCount) {
        this.results = results;
        this.totalCount = totalCount;
    }


    public HolidayItem[] getResults() {
        return results;
    }

    public void setResults(HolidayItem[] results) {
        this.results = results;
    }


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ListData getListData() {
        return new ListData(results, totalCount);
    }

}
