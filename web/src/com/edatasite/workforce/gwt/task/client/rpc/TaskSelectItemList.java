package com.edatasite.workforce.gwt.task.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskSelectItemList implements IsSerializable, Serializable {

    private ArrayList<TaskSelectItem> results;
    private int totalCount;

    public ArrayList<TaskSelectItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<TaskSelectItem> results) {
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
