package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: May 29, 2009
 * Time: 5:21:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class BugsPerEmployeesList implements IsSerializable {
    private int totalCount;
    private BugsPerEmployeesListItem[] bugsPerEmployeesListItems;

    public BugsPerEmployeesList() {
    }

    public BugsPerEmployeesList(BugsPerEmployeesListItem[] bugsPerEmployeesListItems, int totalCount) {
        this.bugsPerEmployeesListItems = bugsPerEmployeesListItems;
        this.totalCount = totalCount;
    }


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public BugsPerEmployeesListItem[] getBugsPerEmployeesListItems() {
        return bugsPerEmployeesListItems;
    }

    public void setBugsPerEmployeesListItems(BugsPerEmployeesListItem[] bugsPerEmployeesListItems) {
        this.bugsPerEmployeesListItems = bugsPerEmployeesListItems;
    }

    public BugsPerEmployeesList(int totalCount, BugsPerEmployeesListItem[] bugsPerEmployeesListItems) {
        this.totalCount = totalCount;
        this.bugsPerEmployeesListItems = bugsPerEmployeesListItems;
    }

    public ListData getListData() {
        return new ListData(bugsPerEmployeesListItems, totalCount);
    }
}
