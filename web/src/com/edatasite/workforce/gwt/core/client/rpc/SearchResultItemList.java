package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Nov 3, 2009
 * Time: 4:26:12 PM
 */
public class SearchResultItemList implements IsSerializable {
    private int totalFound;
    private SearchResultItem[] foundItems;
    private int qTime;

    public SearchResultItemList() {
    }

    public SearchResultItemList(int totalFound, SearchResultItem[] foundItems) {
        this.totalFound = totalFound;
        this.foundItems = foundItems;
    }

    public SearchResultItemList(int totalFound, SearchResultItem[] foundItems, int qTime) {
        this.totalFound = totalFound;
        this.foundItems = foundItems;
        this.qTime = qTime;
    }

    public int getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(int totalFound) {
        this.totalFound = totalFound;
    }

    public SearchResultItem[] getFoundItems() {
        return foundItems;
    }

    public void setFoundItems(SearchResultItem[] foundItems) {
        this.foundItems = foundItems;
    }

    public int getQTime() {
        return qTime;
    }

    public void setQTime(int qTime) {
        this.qTime = qTime;
    }

    public ListData getListData() {
        return new ListData(foundItems, totalFound);
    }

}
